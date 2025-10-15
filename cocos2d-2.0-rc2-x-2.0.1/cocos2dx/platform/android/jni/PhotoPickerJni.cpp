#include "PhotoPickerJni.h"

#include "JniHelper.h"

#include <android/log.h>
#include <jni.h>

#include "CCImage.h"
#include "PhotoPicker.h"
#include <algorithm>

#if 1
#define  LOG_TAG    "PhotoPicker"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define  LOGD(...)
#endif

static const char* CLASS_NAME = "org/cocos2dx/lib/Cocos2dxActivity";

extern "C"
{
	bool openCameraJni()
	{
		cocos2d::JniMethodInfo t;

		if(cocos2d::JniHelper::getStaticMethodInfo(t, CLASS_NAME, "openCamera", "()Z"))
		{
			jboolean ret = t.env->CallStaticBooleanMethod(t.classID, t.methodID);
			t.env->DeleteLocalRef(t.classID);
			return ret;
		}

		return false;
	}

	bool openAlbumJni()
	{
		cocos2d::JniMethodInfo t;

		if(cocos2d::JniHelper::getStaticMethodInfo(t, CLASS_NAME, "openAlbum", "()Z"))
		{
			jboolean ret = t.env->CallStaticBooleanMethod(t.classID, t.methodID);
			t.env->DeleteLocalRef(t.classID);
			return ret;
		}

		return false;
	}

	struct ExtFormatPair
	{
		std::string ext;
		cocos2d::CCImage::EImageFormat format;
	};

	JNIEXPORT void JNICALL Java_org_cocos2dx_lib_Cocos2dxActivity_nativeAddSelectedPhoto(JNIEnv* env, jobject jobj, jstring photoFilename)
	{
		const char* utf = env->GetStringUTFChars(photoFilename, NULL);

		LOGD("[nativeAddSelectedPhoto] utf=%s", utf);

		if(strlen(utf) == 0)
		{
			LOGD("[nativeAddSelectedPhoto] no photo selected!");
			cocos2d::PhotoPicker::shared()->onCancel();
		}
		else
		{
			cocos2d::CCImage* pImg = new cocos2d::CCImage;
			if(pImg)
			{
				cocos2d::CCImage::EImageFormat imgFormat = cocos2d::CCImage::kFmtUnKnown;

				ExtFormatPair extFormatPairArr[] =
				{
						{ ".jpg", cocos2d::CCImage::kFmtJpg },
						{ ".jpeg", cocos2d::CCImage::kFmtJpg },
						{ ".png", cocos2d::CCImage::kFmtPng },
						{ ".tif", cocos2d::CCImage::kFmtTiff },
						{ ".tiff", cocos2d::CCImage::kFmtTiff },
						{ ".dds", cocos2d::CCImage::kFmtDDS },
						{ ".tga", cocos2d::CCImage::kFmtTga },
						{ ".webp", cocos2d::CCImage::kFmtWebP }
				};

				std::string strFilename = utf;
				std::transform(strFilename.begin(), strFilename.end(), strFilename.begin(), ::tolower);

				for(int i = 0; i < sizeof(extFormatPairArr) / sizeof(ExtFormatPair); ++i)
				{
					const ExtFormatPair& extFmt = extFormatPairArr[i];
					if(strFilename.rfind(extFmt.ext) == strFilename.size() - extFmt.ext.size())
					{
						imgFormat = extFmt.format;
						LOGD("[nativeAddSelectedPhoto] selected image is %s", extFmt.ext.c_str());
						break;
					}
				}

				bool bRet = pImg->initWithImageFile(utf, imgFormat);
				pImg->autorelease();

				LOGD("[nativeAddSelectedPhoto] initWithImageFile return %d", bRet);

				cocos2d::PhotoPicker::shared()->addSelectedPhoto(pImg);
			}
			cocos2d::PhotoPicker::shared()->onSelected();
		}

		env->ReleaseStringUTFChars(photoFilename, utf);
	}
}
