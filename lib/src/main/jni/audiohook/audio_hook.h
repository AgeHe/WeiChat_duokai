//
// Created by Administrator on 2017/1/19.
//
#ifndef VIRTUALAPP_AUDIO_HOOK_H
#define VIRTUALAPP_AUDIO_HOOK_H

#include <jni.h>
#include <stdlib.h>
#include <android/log.h>
#include <dlfcn.h>
#include <stddef.h>
#include <fcntl.h>
#include <sys/system_properties.h>
#include <stdio.h>

#define TAG "HOOKAUDIO"
#define JAVA_CLASS "com/lody/virtual/audioFixer"


#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,  TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,  TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

__BEGIN_DECLS
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved);
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved);
__END_DECLS

#endif //VIRTUALAPP_CAMERA_HOOK_H
