LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := main.cpp

LOCAL_CFLAGS += -DPLATFORM_ANDROID

LOCAL_MODULE := poke

# for now, until I do a full rebuild.
LOCAL_PRELINK_MODULE := false

LOCAL_SHARED_LIBRARIES += liblog
LOCAL_SHARED_LIBRARIES += libutils libui libcutils
LOCAL_SHARED_LIBRARIES += libbinder

include $(BUILD_EXECUTABLE)
