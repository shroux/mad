LOCAL_PATH := $(call my-dir)
 
include $(CLEAR_VARS)
 
LOCAL_LDLIBS    := -llog
 
LOCAL_MODULE    := main
LOCAL_SRC_FILES := main.c
 
include $(BUILD_SHARED_LIBRARY)