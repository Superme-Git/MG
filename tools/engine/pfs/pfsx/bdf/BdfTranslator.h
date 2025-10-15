//////////////////////////////////////////////////////////////////////////
//
// BdfTranslator.h
//
// created:	2010/05/21
// author : yanghaibo
//

#ifdef _MSC_VER
#pragma once
#endif

#ifndef __BDF_TRANSLATOR_H__
#define __BDF_TRANSLATOR_H__

namespace PFSX {
	namespace BDF {

		//
		// Initialize the lib
		//
		int Initialize();

		long GetEncoderCount();
		void* GetEncoderItem(long lIndex);
		long GetDecoderCount();
		void* GetDecoderItem(long lIndex);

		//
		// BDFTranslater �����ṩ������ӿ�
		//

		// ���ɶ����Ʋ����ļ�
		// �ɰ汾�ļ�ΪoldFile,�°汾�ļ�ΪnewFile�������ļ�ΪdiffFile��
		int DiffNativeFile( const wchar_t* oldFile, const wchar_t* newFile, const wchar_t* diffFile );

		// �Ծɰ汾�ļ�oldFile�򲹶��������ļ�ΪdiffFile, ���겹������ļ�ΪnewFile
		int PatchNativeFile( const wchar_t* oldFile, const wchar_t* diffFile, const wchar_t* newFile );
	}
}

#endif // __BDF_TRANSLATOR_H__