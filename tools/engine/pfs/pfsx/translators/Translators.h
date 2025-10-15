//////////////////////////////////////////////////////////////////////////
//
// Translators.h
//
// 定义访问所有PFSX Lib缺省提供的Translators的接口
//
// 现在这些 PFSX Lib 已经包括:
// 	pfsx-jpeg.lib
// 	pfsx-pic.lib
// 	pfsx-targa.lib
// 	pfsx-zip.lib
//
// module : pfsx-translators.dll
// author : yanghaibo
// date: 2008-11-12
//
#pragma once

#ifdef TRANSLATORS_EXPORTS
#define TRANSLATORS_API __declspec( dllexport )
#else
#define TRANSLATORS_API __declspec( dllimport )
#endif

#ifdef __cplusplus
extern "C"
{
#endif

TRANSLATORS_API long TRANS_GetEncoderCount();
TRANSLATORS_API void* TRANS_GetEncoderItem(long lIndex);
TRANSLATORS_API long TRANS_GetDecoderCount();
TRANSLATORS_API void* TRANS_GetDecoderItem(long lIndex);

#ifdef __cplusplus
};
#endif
