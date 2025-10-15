#ifndef __Nuclear_PICRESRC_H__
#define __Nuclear_PICRESRC_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <map>
#include <vector>
#include "../common/nuxptypes.h"

namespace Nuclear
{

	struct PicResrc
	{
		std::wstring uri;
		NuclearPoolType pooltype;
		PictureHandle handle;
		int mapRef;
		PicResrc() : handle(INVALID_PICTURE_HANDLE), mapRef(0) { }
		PicResrc(const std::wstring &str) : uri(str), handle(INVALID_PICTURE_HANDLE), mapRef(0) { }
	};//����Ͳ�����xptypes�����ˣ���������ĵط�����Ҫ

	typedef std::map<std::wstring, PicResrc*> PicResrcMap;
	typedef std::vector<PicResrc*> PicResrcVet;

}
#endif