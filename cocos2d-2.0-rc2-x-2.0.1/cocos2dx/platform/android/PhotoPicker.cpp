//////////////////////////////////////////////////////////////////////////
// File:		PhotoPicker.cpp
// Author:	杨春雷
// Date:		2016/04/25
//////////////////////////////////////////////////////////////////////////

#include "../PhotoPicker.h"
#include "jni/PhotoPickerJni.h"

NS_CC_BEGIN

bool PhotoPicker::openCamera()
{
	releaseSelectedPhotos();
	return openCameraJni();
}

bool PhotoPicker::openAlbum()
{
	releaseSelectedPhotos();
	return openAlbumJni();
}

NS_CC_END
