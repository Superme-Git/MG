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
		// BDFTranslater 单独提供的特殊接口
		//

		// 生成二进制差异文件
		// 旧版本文件为oldFile,新版本文件为newFile，差异文件为diffFile。
		int DiffNativeFile( const wchar_t* oldFile, const wchar_t* newFile, const wchar_t* diffFile );

		// 对旧版本文件oldFile打补丁，补丁文件为diffFile, 打完补丁后的文件为newFile
		int PatchNativeFile( const wchar_t* oldFile, const wchar_t* diffFile, const wchar_t* newFile );
	}
}

#endif // __BDF_TRANSLATOR_H__