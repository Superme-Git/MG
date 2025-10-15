#pragma once

namespace PP
{
	// = 0, not find the error code.
	// < 0, error occurred for buffer too small.
	//		The absolute value the the needed buffer size counted as characters
	//		(excluding the terminating null character).
	// > 0, succeeded, and it is size of the copied string.
	int GetErrorDescription( LANGID langId, int ErrorCode,
		LPWSTR lpBuffer, DWORD nSize );
} // namespace PP
