LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

OPENCV_CAMERA_MODULES:=off
#OPENCV_INSTALL_MODULES:=off
OPENCV_LIB_TYPE:=STATIC
include D:\project\opencv\OpenCV-3.1.0-android-sdk\OpenCV-android-sdk\sdk\native\jni\OpenCV.mk

LOCAL_C_INCLUDE := D:\project\opencv\OpenCV-3.1.0-android-sdk\OpenCV-android-sdk\sdk\native\jni\include

LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog -ldl
LOCAL_MODULE    := imageprocess
LOCAL_SRC_FILES := imageprocess.cpp

include $(BUILD_SHARED_LIBRARY)