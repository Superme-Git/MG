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

	// �ӿ�
	public:
		// ���ļ� filename �����ݶ��� data ��
		// @filename ��������Դ·������ʽ����pfs·��
		virtual bool GetFileImage(const std::wstring &filename, XBuffer &data) = 0;
	};

};

#endif