//////////////////////////////////////////////////////////////////////////
// File:		PhotoPicker.h
// Author:	Ñî´ºÀ×
// Date:		2016/04/25
//////////////////////////////////////////////////////////////////////////

#ifndef _PhotoPicker_h_
#define _PhotoPicker_h_

#include <string>
#include "cocoa/CCArray.h"
#include "CCImage.h"
#include "script_support/CCScriptSupport.h"

NS_CC_BEGIN

class CC_DLL PhotoPicker
{
protected:
	std::string mOnSelectedCallback;
	std::string mOnCancelCallback;
	CCArray mSelectedPhotos;

#ifdef ANDROID
	int mFramesCD;
#endif

public:
	PhotoPicker()
#ifdef ANDROID
		: mFramesCD(0)
#endif
	{}
	virtual ~PhotoPicker() {}

	static PhotoPicker* shared()
    {
        static PhotoPicker sInstance;
        return &sInstance;
    }

	virtual bool openCamera();
	virtual bool openAlbum();

	void setOnSelectedCallback(const char* szScript) { mOnSelectedCallback = szScript; }
	void setOnCancelCallback(const char* szScript) { mOnCancelCallback = szScript; }

	virtual int getSelectedPhotoCount() { return mSelectedPhotos.count(); }
	virtual cocos2d::CCImage* getSelectedPhoto(int index) { return dynamic_cast<cocos2d::CCImage*>(mSelectedPhotos.objectAtIndex(index)); }
	virtual void releaseSelectedPhotos() { mSelectedPhotos.removeAllObjects(); }

	// add a selected photo - called by platform
    virtual void addSelectedPhoto(CCImage* pImg) { mSelectedPhotos.addObject(pImg); }
	// finish select - called by platform
	void onSelected()
	{
#ifdef ANDROID
		mFramesCD = 5;
#else
		if (!mOnSelectedCallback.empty())
		{
			CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction(mOnSelectedCallback.c_str());
		}
#endif
	}
	// cancel select - called by platform
	void onCancel()
	{
		if (!mOnCancelCallback.empty())
		{
			CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction(mOnCancelCallback.c_str());
		}
	}

	void update(float dt)
	{
#ifdef ANDROID
		if (mFramesCD > 0)
		{
			--mFramesCD;
			if (mFramesCD == 0)
			{
				if (!mOnSelectedCallback.empty())
				{
					CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction(mOnSelectedCallback.c_str());
				}
			}
		}
#endif
	}
};

NS_CC_END

#endif