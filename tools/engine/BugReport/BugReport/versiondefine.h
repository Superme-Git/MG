#pragma once

#include "..\..\mhzx\version.inl"

// 定义GameClient.rc文件中需要的版本数据
#define _FullVersion(a,b,c,d)		a##.##b##.##c##.##d
#define _FullVersionComma(a,b,c,d)	a##,##b##,##c##,##d
#define FullVersion				_FullVersion(MajorVersion,MinorVersion,shareVersion,NuclearVersion)
#define FileVersion				_FullVersionComma(MajorVersion,MinorVersion,shareVersion,NuclearVersion)
#define ProductVersion			_FullVersionComma(MajorVersion,MinorVersion,shareVersion,NuclearVersion)
#define MajorVersionText		_STR_(MajorVersion)
#define MinorVersionText		_STR_(MinorVersion)
#define NuclearVersionText		_STR_(NuclearVersion)
#define shareVersionText		_STR_(shareVersion)
#define FileVersionText			_STR_(FullVersion)
#define ProductVersionText		_STR_(FullVersion) 
#define FileVersionCommaText	MajorVersionText ", " MinorVersionText ", " shareVersionText ", " NuclearVersionText
#define ProductVersionCommaText	MajorVersionText ", " MinorVersionText ", " shareVersionText ", " NuclearVersionText

// 下面非常奇怪的资源目录命名方式是不得以而为之
#define COMMON_RESOURCE_DIR		.\../.\../mhzx/res/

#define ICON_FILE_PATH_					CONCAT_MACRO_2(	COMMON_RESOURCE_DIR, ProductIconName )
#define SMALL_ICON_FILE_PATH_			CONCAT_MACRO_2(	COMMON_RESOURCE_DIR, ProductSmallIconName )
#define ICON_FILE_PATH					_STR_(ICON_FILE_PATH_)
#define SMALL_ICON_FILE_PATH			_STR_(SMALL_ICON_FILE_PATH_)

#if defined PRODUCT_CHT
#define APP_NAME			診斷日誌搜集/發送工具
#else
#define APP_NAME			诊断日志搜集/发送工具
#endif

#define APP_TITLE_SYMBOL	CONCAT_MACRO_2(PRODUCT_NAME_SYMBOL, APP_NAME )
#define APP_TITLE_STRING	_STR_(APP_TITLE_SYMBOL)
#define FileDescriptionString	APP_TITLE_STRING
