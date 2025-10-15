LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := cocos_spine_static

LOCAL_MODULE_FILENAME := libSpine

LOCAL_SRC_FILES := spine-c/src/spine/Animation.c \
                   spine-c/src/spine/AnimationState.c \
                   spine-c/src/spine/AnimationStateData.c \
                   spine-c/src/spine/Atlas.c \
                   spine-c/src/spine/AtlasAttachmentLoader.c \
                   spine-c/src/spine/Attachment.c \
                   spine-c/src/spine/AttachmentLoader.c \
                   spine-c/src/spine/Bone.c \
                   spine-c/src/spine/BoneData.c \
                   spine-c/src/spine/BoundingBoxAttachment.c \
                   spine-c/src/spine/Event.c \
                   spine-c/src/spine/EventData.c \
                   spine-c/src/spine/extension.c \
                   spine-c/src/spine/IkConstraint.c \
                   spine-c/src/spine/IkConstraintData.c \
                   spine-c/src/spine/Json.c \
                   spine-c/src/spine/MeshAttachment.c \
                   spine-c/src/spine/RegionAttachment.c \
                   spine-c/src/spine/Skeleton.c \
                   spine-c/src/spine/SkeletonBounds.c \
                   spine-c/src/spine/SkeletonData.c \
                   spine-c/src/spine/SkeletonJson.c \
                   spine-c/src/spine/Skin.c \
                   spine-c/src/spine/SkinnedMeshAttachment.c \
                   spine-c/src/spine/Slot.c \
                   spine-c/src/spine/SlotData.c \
                   spine-cocos2dx/src/spine/PolygonBatch.cpp \
                   spine-cocos2dx/src/spine/SkeletonAnimation.cpp \
                   spine-cocos2dx/src/spine/SkeletonRenderer.cpp \
                   spine-cocos2dx/src/spine/spine-cocos2dx.cpp \

LOCAL_C_INCLUDES := $(LOCAL_PATH)/spine-c/include/spine \
                    $(LOCAL_PATH)/spine-c/include \
                    $(LOCAL_PATH)/spine-cocos2dx/include \
                    $(LOCAL_PATH)/spine-cocos2dx/include/spine \

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/spine-c/include/spine \
                           $(LOCAL_PATH)/spine-c/include \
                           $(LOCAL_PATH)/spine-cocos2dx/include \
                           $(LOCAL_PATH)/spine-cocos2dx/include/spine \

LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static

LOCAL_LDLIBS := -llog \
                    
include $(BUILD_STATIC_LIBRARY)

$(call import-module,cocos2dx)
