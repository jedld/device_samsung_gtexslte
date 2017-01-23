#
# Copyright (C) 2010 ARM Limited. All rights reserved.
#
# Copyright (C) 2008 The Android Open Source Project
#
# Copyright (C) 2016 The CyanogenMod Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

ifneq ($(TARGET_SIMULATOR),true)

LOCAL_PATH := $(call my-dir)

# HAL module implemenation, not prelinked and stored in
# hw/<OVERLAY_HARDWARE_MODULE_ID>.<ro.product.board>.so
include $(CLEAR_VARS)
LOCAL_PRELINK_MODULE := false

ifeq ($(shell test $(PLATFORM_SDK_VERSION) -ge 21 && echo OK),OK)
	LOCAL_MODULE_RELATIVE_PATH := hw
else
	LOCAL_MODULE_PATH := $(TARGET_OUT_SHARED_LIBRARIES)/hw
endif

MALI_DDK_TEST_PATH := hardware/arm/

LOCAL_MODULE := gralloc.$(TARGET_BOARD_PLATFORM)
#LOCAL_MODULE_TAGS := optional

LOCAL_SHARED_LIBRARIES := liblog libcutils libGLESv1_CM libGLES_mali libion

SHARED_MEM_LIBS := \
	libion_sprd \
	libhardware

LOCAL_SHARED_LIBRARIES := \
	liblog \
	libcutils \
	libGLESv1_CM \
	$(SHARED_MEM_LIBS) \

LOCAL_C_INCLUDES := \
	system/core/include \
	$(TARGET_OUT_INTERMEDIATES)/KERNEL_OBJ/usr/include/video/ \
	$(TARGET_OUT_INTERMEDIATES)/KERNEL_OBJ/usr/include/ \
	$(TARGET_OUT_INTERMEDIATES)/KERNEL_OBJ/ \

LOCAL_ADDITIONAL_DEPENDENCIES += \
	$(TARGET_OUT_INTERMEDIATES)/KERNEL_OBJ/usr \

LOCAL_EXPORT_C_INCLUDE_DIRS := \
	$(LOCAL_PATH) \
	$(LOCAL_C_INCLUDES) \

LOCAL_CFLAGS := -DLOG_TAG=\"gralloc.$(TARGET_BOARD_PLATFORM)\" -DGRALLOC_32_BITS -DPLATFORM_SDK_VERSION=$(PLATFORM_SDK_VERSION)

ifeq ($(strip $(USE_UI_OVERLAY)),true)
        LOCAL_CFLAGS += -DUSE_UI_OVERLAY
endif

ifneq ($(strip $(TARGET_BUILD_VARIANT)), user)
        LOCAL_CFLAGS += -DDUMP_FB
        LOCAL_CFLAGS += -DSPRD_MONITOR_FBPOST
endif

ifeq ($(strip $(TARGET_GPU_USE_TILE_ALIGN)), true)
	LOCAL_CFLAGS +=  -DGPU_USE_TILE_ALIGN
endif


ifeq ($(strip $(USE_SPRD_DITHER)) , true)
LOCAL_SHARED_LIBRARIES += libdither
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../dither/
LOCAL_CFLAGS += -DSPRD_DITHER_ENABLE
endif

LOCAL_SRC_FILES := \
	gralloc_module.cpp \
	alloc_device.cpp \
	framebuffer_device.cpp \
	dump_bmp.cpp

#LOCAL_CFLAGS+= -DMALI_VSYNC_EVENT_REPORT_ENABLE
include $(BUILD_SHARED_LIBRARY)

endif
