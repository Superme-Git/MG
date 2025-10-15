//////////////////////////////////////////////////////////////////////////
//
// InetIO.h
//
//  [10/27/2008 yanghaibo]
//
//////////////////////////////////////////////////////////////////////////
#pragma once

// Windows Header Files:
#define WIN32_LEAN_AND_MEAN		// Exclude rarely-used stuff from Windows headers
#include <windows.h>
#include <wininet.h>

namespace PFS
{
	class CInetIOException
	{
	protected:
		DWORD m_dwErrorCode;

	public:
		CInetIOException( DWORD dwErrorCode )
			: m_dwErrorCode( dwErrorCode )
		{
		}
		
		DWORD GetErrorCode() const
		{
			return m_dwErrorCode;
		}
	};

	class CInetIO
	{
	public:
		CInetIO();
		~CInetIO();

		//
		// summary: 
		//		Opens the internet resource file by the URL
		//
		// parameters:
		//		[in] lpszUrl - object url
		//		[in] lRangeBegin - the first bytes to download(0 means the first byte).
		//		[in/optional] lRangeEnd - the last bytes to download(-1 means the last byte).
		//
		// returns:
		//		TRUE : succeeded to open the file.
		//		FALSE: failed to open the file. Call GetLastError() to query the Win32 error code.
		//
		BOOL Open( LPCTSTR lpszUrl );
		BOOL Open( LPCTSTR lpszUrl, LONG lRangeBegin, LONG lRangeEnd = -1L );

		//
		// summary:
		//		Opens the internet object by the connection handle and object name.
		//
		// parameters:
		//		[in] hConnect - A handle to an HTTP session returned by InternetConnect. 
		//		[in] lpszObjectName - the object name.
		//		[in] lRangeBegin - the first bytes to download(0 means the first byte).
		//		[in/optional] lRangeEnd - the last bytes to download(-1 means the last byte).
		// returns:
		//		TRUE: succeeded to open the file.
		//		FALSE: failed to open the file. Call GetLastError() to query the Win32 error code.
		//
		BOOL Open( HINTERNET hConnect, LPCTSTR lpszObjectName );
		BOOL Open( HINTERNET hConnect, LPCTSTR lpszObjectName, LONG lRangeBegin, LONG lRangeEnd = -1L );

		//
		// summary: 
		//		Gets the internet resource file name
		//
		// remarks:
		//		This DataFileName contains the relative path to the URL server address.
		//		So, if you specify the URL as http://www.webserver.com/a/b/c.zip the DataFileName
		//		probably will be /a/b/c.zip but not the c.zip.
		//		You can use PathStripPath(DataFileName) to remove the relative path if needed.
		//
		LPCTSTR GetDataFileName();

		//
		// summary: 
		//		Queries the server to determine the amount of data available.
		//
		// returns: 
		//		The file size. 0 means no data available. 
		DWORD GetSize();

		//
		// summary: 
		//		Sets the file pointer.
		//
		// parameters:
		//		lDistanceToMove - [in] Number of bytes to move the file pointer.
		//							A positive value moves the pointer forward in the file;
		//							a negative value moves it backward. 
		//
		//		dwMoveMethod - 
		//			FILE_BEGIN		Starting point is zero or the beginning of the file.
		//							If FILE_BEGIN is specified, lDistanceToMove is interpreted 
		//							as an unsigned location for the new file pointer. 
		//			FILE_CURRENT	Current value of the file pointer is the starting point. 
		//			FILE_END		Current end-of-file position is the starting point. 
		//							This method fails if the content length is unknown. 
		//
		// returns:
		//		The current file position if the function succeeds, or -1 otherwise.
		// 
		DWORD Seek( LONG lDistanceToMove, DWORD dwMoveMethod );

		//
		// summary: 
		//		Gets the file pointer position.
		//
		DWORD GetPos();

		//
		// summary: 
		//		Reads data from the internet resource file
		//
		// parameters:
		//		lpBuffer - [out] Pointer to a buffer that receives the data. 
		//		dwSize  - [in] Number of bytes to be read. 
		//	
		// returns: 
		//		The number of bytes read. 
		//
		DWORD Read( void* lpBuffer, DWORD dwSize );

		//
		// summary: 
		//		Writes data to an open Internet file.
		//
		// parameters:
		//	lpBuffer -	[in] Pointer to a buffer that contains the data to be written to the file. 
		//	dwSize - 	[in] Number of bytes to be written to the file. 
		//	
		// returns: 
		//		The number of bytes written to the buffer. 
		//
		// throws: 
		//		CInetIOException
		//
		DWORD Write( const void* lpBuffer, DWORD dwSize );

		//
		// summary: 
		//		Closes the file and releases the internal resources.
		//
		void Close();

	private:
		HINTERNET m_hInternet;
		HINTERNET m_hFile;
		TCHAR m_szDataFileName[MAX_PATH];
	};

}; // namespace PFS
