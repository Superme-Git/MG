///=============================================================================
/// Product   : MHZX
/// Project   : Launcher
/// Module    : ErrorInfo.h
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-12 Created.
///=============================================================================
#pragma once

#include <string>

// 用户自行退出
#define SC_USER_CANCELED			1

namespace upd
{
	//////////////////////////////////////////////////////////////////////////
	//
	// 错误信息 单体类
	//
	struct ErrorInfo
	{
	public:
		DWORD			ErrorCode;
		std::wstring	ErrorDetail;

	protected:
		ErrorInfo(){};
		static ErrorInfo& Instance()
		{
			static ErrorInfo _Instance;
			return _Instance;
		}

		friend const ErrorInfo& GetLastError();
		friend void SetLastError( DWORD dwErrorCode );
		friend void SetLastError( DWORD dwErrorCode, const std::wstring& strDetail );
	};

	inline const ErrorInfo& GetLastError()
	{
		return ErrorInfo::Instance();
	}

	//
	// 格式化系统API产生的错误消息
	//
	extern void __cdecl FormatErrorMessage( DWORD dwErrorCode, std::wstring* pMessage, ... );

	extern void SetLastError( DWORD dwErrorCode );

	void SetLastError( DWORD dwErrorCode, const std::wstring& strDetail );
}
