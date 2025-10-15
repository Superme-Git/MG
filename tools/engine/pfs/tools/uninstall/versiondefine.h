#pragma once

// Ϊuninstall���򵥶�Ԥ����icon
#define ICON_NAME			uninstall.ico
#define SMALL_ICON_NAME		uninstall.ico

#if defined PROJECT_MHJY
#include "..\..\..\mhjy\version.inl"
#elif defined PROJECT_MHSD
#include "..\..\..\mhsd\version.inl"
#else
#include "..\..\..\mhzx\version.inl"
#endif

// ����GameClient.rc�ļ�����Ҫ�İ汾����
#define _FullVersion(a,b,c,d)		a##.##b##.##c##.##d
#define _FullVersionComma(a,b,c,d)	a##,##b##,##c##,##d
#define FullVersion				_FullVersion(MajorVersion,MinorVersion,shareVersion,NuclearVersion)
#define FileVersion				_FullVersionComma(MajorVersion,MinorVersion,shareVersion,NuclearVersion)
#define ProductVersion			_FullVersionComma(MajorVersion,MinorVersion,shareVersion,NuclearVersion)
#define MajorVersionText		_STR_(MajorVersion)
#define MinorVersionText		_STR_(MinorVersion)
#define ServerVersionText		_STR_(ServerVersion)
#define ClientVersionText		_STR_(ClientVersion)
#define NuclearVersionText		_STR_(NuclearVersion)
#define shareVersionText		_STR_(shareVersion)
#define FileVersionText			_STR_(FullVersion)
#define ProductVersionText		_STR_(FullVersion) 
#define FileVersionCommaText	MajorVersionText ", " MinorVersionText ", " shareVersionText ", " NuclearVersionText
#define ProductVersionCommaText	MajorVersionText ", " MinorVersionText ", " shareVersionText ", " NuclearVersionText

// ����ǳ���ֵ���ԴĿ¼������ʽ�ǲ����Զ�Ϊ֮
#if defined PROJECT_MHJY
#define COMMON_RESOURCE_DIR		.\../.\../.\../mhjy/res/
#elif defined PROJECT_MHSD
#define COMMON_RESOURCE_DIR		.\../.\../.\../mhsd/res/
#else
#define COMMON_RESOURCE_DIR		.\../.\../.\../mhzx/res/
#endif

#define ICON_FILE_PATH_			CONCAT_MACRO_2(	COMMON_RESOURCE_DIR, ProductIconName )
#define SMALL_ICON_FILE_PATH_	CONCAT_MACRO_2(	COMMON_RESOURCE_DIR, ProductSmallIconName )
#define ICON_FILE_PATH			_STR_(ICON_FILE_PATH_)
#define SMALL_ICON_FILE_PATH	_STR_(SMALL_ICON_FILE_PATH_)

#define APP_TITLE_STRING		"�ҽ�MT3"
#define FileDescriptionString	"The Uninstaller of " ProductName
#define LAUNCHER_TITLE_STRING	"�ҽ�MT3���³���"
