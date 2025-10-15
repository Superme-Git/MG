//ifileiomanforparticle

#ifndef __Nuclear_IFILEIO_H
#define __Nuclear_IFILEIO_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "xbuffer.h"

namespace Nuclear
{

	class IFileIO
	{
	public:
		IFileIO() { }
		virtual ~IFileIO() { }

	// 接口
	public:
		// 将文件 filename 的内容读入 data 中
		// @filename 是引擎资源路径，格式兼容pfs路径
		virtual bool GetFileImage(const std::wstring &filename, XBuffer &data) = 0;
	};

};

#endif