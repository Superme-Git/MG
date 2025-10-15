///=============================================================================
/// Product   : MHZX
/// Project   : DXTest
/// Module    : CommonUtils.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	Common Utils
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-5-16 Created.
///=============================================================================
#pragma once

namespace UTIL
{
	LPCTSTR GetModuleDirectory();
	LPCTSTR GetWorkDirectory();
	void AbsolutePath( CString& rRelPath );

}// namespace UTIL