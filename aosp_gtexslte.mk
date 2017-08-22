# Release name
PRODUCT_RELEASE_NAME := gtexslte

# Inherit from the common Open Source product configuration
$(call inherit-product, $(SRC_TARGET_DIR)/product/core.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/aosp_base_telephony.mk)

$(call inherit-product, device/samsung/gtexslte/device_gtexslte.mk)

# Use specific resolution for bootanimation
TARGET_BOOTANIMATION_SIZE := 720x480

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := gtexslte
PRODUCT_NAME := aosp_gtexslte
PRODUCT_BRAND := samsung
PRODUCT_MODEL := SM-T285
PRODUCT_MANUFACTURER := samsung
PRODUCT_CHARACTERISTICS := tablet

TARGET_SCREEN_WIDTH := 800
TARGET_SCREEN_HEIGHT := 1280
