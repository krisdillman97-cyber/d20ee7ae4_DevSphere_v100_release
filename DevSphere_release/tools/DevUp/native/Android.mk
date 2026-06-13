LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := devsphere_native
LOCAL_SRC_FILES := jni/main.cpp
include $(BUILD_SHARED_LIBRARY)
