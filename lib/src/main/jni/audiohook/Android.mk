LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := audiohook

LOCAL_CFLAGS := -Wno-error=format-security -fpermissive
LOCAL_CFLAGS += -fno-rtti -fno-exceptions

LOCAL_C_INCLUDES += $(LOCAL_PATH)/hook

LOCAL_SRC_FILES := audio_hook.cpp


LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)

