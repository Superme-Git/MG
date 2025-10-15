///=============================================================================
/// Product   : MHZX
/// Project   : ErrorLookup
/// Module    : ErrorInfo.h
/// Copyright : (c) 2010 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	
///=============================================================================
/// Revision History :
/// 	V1.0 - 2010-08-16 Created.
///=============================================================================
#pragma once

namespace EC
{
	// = 0, not find the error code.
	// < 0, error occurred for buffer too small.
	//		The absolute value the the needed buffer size counted as characters
	//		(excluding the terminating null character).
	// > 0, succeeded, and it is size of the copied string.
	int GetErrorDescription( LANGID langId, DWORD dwErrorCode,
		LPWSTR lpBuffer, DWORD nSize );
} // namespace EC
