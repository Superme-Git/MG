//ifileiomanforparticle

#ifndef __Nuclear_IFILEIO_H__
#define __Nuclear_IFILEIO_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nuxbuffer.h"

namespace Nuclear
{

	class INuclearFileIO
	{
	public:
		INuclearFileIO() { }
		virtual ~INuclearFileIO() { }

	public:
		// 将文件 filename 的内容读入 data 中
		// @filename 是引擎资源路径
		virtual bool GetFileImage(const std::wstring &filename, NuclearBuffer &data, bool lookupSegmPakPath = true) = 0;
	};

};

#endif