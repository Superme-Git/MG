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
		// ���ļ� filename �����ݶ��� data ��
		// @filename ��������Դ·��
		virtual bool GetFileImage(const std::wstring &filename, NuclearBuffer &data, bool lookupSegmPakPath = true) = 0;
	};

};

#endif