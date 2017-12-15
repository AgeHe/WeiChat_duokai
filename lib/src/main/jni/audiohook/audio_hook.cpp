//
// Created by Administrator on 2017/1/19.
// 主要就是替换了传入camera_native_setup方法的包名 将插件的包名 换成宿主的包名
// 以此来通过权限的校验
//

#include "audio_hook.h"

JavaVM *g_vm_audio;

typedef void (*Bridge_DalvikBridgeFunc)(const void **, void *, const void *,         void *);

typedef jobject (*Native_nativeSetUpFunc)(JNIEnv *, jobject weak_this,  jstring pkg);

typedef jobject (*Native_nativeSetUpDalvikFunc)(JNIEnv *,
                                                jobject weak_this, jstring pkg);





void marka() {
    // Do nothing
};


//将nativemark和 mark方法相联系起来 这个调用将nativeMark 最后会转到mark上
static JNINativeMethod gMarkMethods[] = {
        {"nativeMark", "()V", (void *) marka}
};

static struct {
    bool isArt;//是否是art虚拟机
    int nativeOffset;
    void *art_work_around_app_jni_bugs;
    char *(*GetCstrFromString)(void *);
    void *(*GetStringFromCstr)(const char *);
    Bridge_DalvikBridgeFunc orig_DalvikBridgeFunc;
    Native_nativeSetUpFunc orig_native_openDexNativeFunc;
    Native_nativeSetUpDalvikFunc orig_native_openDexNativeDalvikFunc;
} gOffset;



char *newPkgName;//需要替换成的包名





static jobject new_nativeSetUpFunc(JNIEnv *env,
                                   jobject weak_this) {
    jstring newPkg = env->NewStringUTF(newPkgName);
    LOGW("camera success %s",newPkgName);
    return gOffset.orig_native_openDexNativeFunc(env,  weak_this, newPkg);

}

//dalvik虚拟机的操作
static jobject new_nativeSetUpDalvikFunc(JNIEnv *env,
                                         jobject weak_this, jint event) {
    LOGW("Hoho,new_nativeSetUpDalvikFunc.");
    jstring newPkg = env->NewStringUTF(newPkgName);
    LOGW("camera success %s",newPkgName);
    return gOffset.orig_native_openDexNativeDalvikFunc(env, weak_this,  newPkg);

}

static void new_bridge_nativeSetUpFunc(const void **args, void *pResult, const void *method,  void *self) {
    LOGW("Hoho,进入不是art虚拟机操作.");
    JNIEnv *env = NULL;
    g_vm_audio->GetEnv((void **) &env, JNI_VERSION_1_6);
    g_vm_audio->AttachCurrentThread(&env, NULL);

    typedef char* (*GetCstrFromString)(void *);
    typedef void* (*GetStringFromCstr)(const char*);

    const char* origPkg0 = args[0] == NULL ? NULL : gOffset.GetCstrFromString((void*) args[0]);
    LOGD("The original package0 is: %s", origPkg0);

    const char* origPkg = args[2] == NULL ? NULL : gOffset.GetCstrFromString((void*) args[2]);
    LOGD("The original package2 is: %s", origPkg);

    const char* origPkg3 = args[3] == NULL ? NULL : gOffset.GetCstrFromString((void*) args[3]);
    LOGD("The original package3 is: %s", origPkg3);

    args[3] = gOffset.GetStringFromCstr(newPkgName);//替换包名
    gOffset.orig_DalvikBridgeFunc(args, pResult, method, self);
}

//查找jmethodID的指针后 去查native_up
void searchJniOffset_camera(JNIEnv *env, bool isArt) {

    jclass g_class = env->FindClass(JAVA_CLASS);
    jmethodID mtd_nativeHook = env->GetStaticMethodID(g_class, "nativeMark", "()V");//调用nativeMark方法 为了获取查找jmethodID的指针

    size_t startAddress = (size_t) mtd_nativeHook;
    size_t targetAddress = (size_t) marka;
    if (isArt && gOffset.art_work_around_app_jni_bugs) {
        targetAddress = (size_t) gOffset.art_work_around_app_jni_bugs;
    }

    int offset = 0;
    bool found = false;
    while (true) {
        if (*((size_t *) (startAddress + offset)) == targetAddress) {
            found = true;
            break;
        }
        offset += 4;
        if (offset >= 100) {
            LOGW("Ops: Unable to find the jni function.");
            break;
        }
    }

    if (found) {
        gOffset.nativeOffset = offset;
        if (!isArt) {
            gOffset.nativeOffset += (sizeof(int) + sizeof(void *));
        }
        LOGW("Hoho, Get the offset : %d.", gOffset.nativeOffset);
    }
}
// 替换实现改为我们自己在这定义的new_nativeSetUpDalvikFunc 在这个方法里就是替换了包名
inline void replaceImplementation(JNIEnv *env, jobject javaMethod, jboolean isArt) {
    LOGW("Hoho,jinru replaceImplementation");
    size_t mtd_openDexNative = (size_t) env->FromReflectedMethod(javaMethod);
    int nativeFuncOffset = gOffset.nativeOffset;
    void **jniFuncPtr = (void **) (mtd_openDexNative + nativeFuncOffset);

    if (!isArt) {
        LOGD("The vm is dalvik");
        gOffset.orig_DalvikBridgeFunc = (Bridge_DalvikBridgeFunc) (*jniFuncPtr);
        *jniFuncPtr = (void *) new_bridge_nativeSetUpFunc;
    } else {
        char vmSoName[4] = {0};
        __system_property_get("ro.build.version.sdk", vmSoName);
        int sdk;
        sscanf(vmSoName, "%d", &sdk);
        LOGD("The vm is art and the sdk int is %d", sdk);

        if (sdk < 21) {
            LOGW("Hoho,jinru <21");
            gOffset.orig_native_openDexNativeDalvikFunc = (Native_nativeSetUpDalvikFunc) (*jniFuncPtr);
            *jniFuncPtr = (void *) new_nativeSetUpDalvikFunc;
        } else {
            gOffset.orig_native_openDexNativeFunc = (Native_nativeSetUpFunc) (*jniFuncPtr);
            *jniFuncPtr = (void *) new_nativeSetUpFunc;
            LOGW("jinru >21 end");
        }
    }

}


//暴露出来最开始调用的方法
void native_hook(JNIEnv *env, jclass clazz, jobject method, jstring  pkg, jboolean isArt) {
    newPkgName = (char *) env->GetStringUTFChars(pkg, NULL);
    g_vm_audio->GetEnv((void **) &env, JNI_VERSION_1_6);
    g_vm_audio->AttachCurrentThread(&env, NULL);

    jclass g_class = env->FindClass(JAVA_CLASS);
    if (env->RegisterNatives(g_class, gMarkMethods, 1) < 0) {
        return;
    }

    gOffset.isArt = isArt;

    char vmSoName[15] = {0};
    __system_property_get("persist.sys.dalvik.vm.lib", vmSoName);
    LOGW("Find the so name : %s.", strlen(vmSoName) == 0 ? "<EMPTY>" : vmSoName);

    void *vmHandle = dlopen(vmSoName, 0);
    if (!vmHandle) {
        LOGE("Unable to open the %s.", vmSoName);
        vmHandle = RTLD_DEFAULT;
    }


    if (isArt) {
        gOffset.art_work_around_app_jni_bugs = dlsym(vmHandle, "art_work_around_app_jni_bugs");
    } else {
        gOffset.GetCstrFromString = (char *(*)(void *)) dlsym(vmHandle,"_Z23dvmCreateCstrFromStringPK12StringObject");
        if (!gOffset.GetCstrFromString) {
            gOffset.GetCstrFromString = (char *(*)(void *)) dlsym(vmHandle,
                                                                  "dvmCreateCstrFromString");
        }

        gOffset.GetStringFromCstr = (void *(*)(const char *)) dlsym(vmHandle,
                                                                    "_Z23dvmCreateStringFromCstrPKc");
        if (!gOffset.GetStringFromCstr) {
            gOffset.GetStringFromCstr = (void *(*)(const char *)) dlsym(vmHandle,  "dvmCreateStringFromCstr");
        }
    }

    searchJniOffset_camera(env, isArt);
    replaceImplementation(env, method, isArt);
}



//将hooknativemethod 和本地的native_hook联系起来 这个调用将hooknativemethod 最后会转到native_hook上
static JNINativeMethod gMethods[] = {
        { "hookNativeMethod", "(Ljava/lang/Object;Ljava/lang/String;Z)V",
                (void *) native_hook }
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    g_vm_audio = vm;
    JNIEnv *env;

    LOGE("JNI_OnLoad_audio start");
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        LOGE("GetEnv() FAILED!!!");
        return JNI_ERR;
    }

    jclass javaClass = env->FindClass(JAVA_CLASS);
    LOGE("we have found the class: %s", JAVA_CLASS);
    if (javaClass == NULL) {
        LOGE("unable to find class: %s", JAVA_CLASS);
        return JNI_ERR;
    }

    env->UnregisterNatives(javaClass);
    if (env->RegisterNatives(javaClass, gMethods, 1) < 0) {
        LOGE("register methods FAILED!!!");
        return JNI_ERR;
    }

    env->DeleteLocalRef(javaClass);
    LOGI("JavaVM::GetEnv() SUCCESS!");
    return JNI_VERSION_1_6;
}