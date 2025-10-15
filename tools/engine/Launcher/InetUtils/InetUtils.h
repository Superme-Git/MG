//////////////////////////////////////////////////////////////////////////
//
// InetUtils.h
//
//  [10/24/2008 yanghaibo]
//
//////////////////////////////////////////////////////////////////////////

#define WIN32_LEAN_AND_MEAN		// Exclude rarely-used stuff from Windows headers
// Windows Header Files:
#include <windows.h>
#include <wininet.h>

// The following ifdef block is the standard way of creating macros which make exporting 
// from a DLL simpler. All files within this DLL are compiled with the INETUTILS_EXPORTS
// symbol defined on the command line. this symbol should not be defined on any project
// that uses this DLL. This way any other project whose source files include this file see 
// INETUTILS_API functions as being imported from a DLL, whereas this DLL sees symbols
// defined with this macro as being exported.
#ifdef INETUTILS_EXPORTS
#define INETUTILS_API __declspec(dllexport)
#else
#define INETUTILS_API __declspec(dllimport)
#endif

#ifdef __cplusplus
extern "C"
{
#endif // __cplusplus


//
// summary: check the host specified by the url can be reached or not.
//			
// returns: true - yes, false - can not be reached currently.
//
INETUTILS_API BOOL InetCheckConnection(IN LPCTSTR lpszUrl);

//
// summary:
//		download data from internet by an http url.
//
// remarks:
//		if successfully, the data will save to lpszLocalDirTo.
//		if lpszDataFileName is not NULL, it will be filled with the result fully qualified file name.
//		dwDataFileNameSize specifies the buffer size in characters for lpszDataFileName.
//		Before use this API, please use InetCheckConnection() to check the resource can be reached or not.
//
// returns:
//		0 - succeeds. !0, error code same as the return value of GetLastError().
//
INETUTILS_API DWORD InetHttpDownload(IN LPCTSTR lpszUrlFrom, IN LPCTSTR lpszLocalDirTo, 
								   OUT LPTSTR lpszDataFileName, IN DWORD dwDataFileNameSize );

//
// NOT implemented yet!
//
// INETUTILS_API DWORD InetFtpDownload( IN LPCTSTR lpszUrlFrom, IN LPCTSTR lpszLocalDirTo );
//

//
// A test case for InetHttpDownload()
//
//BOOL InetHttpDownload_Test(  )
//{
//	LPCTSTR lpszUrlFrom = TEXT("http://www.sina.com.cn");
//	LPCTSTR lpszLocalDirTo = TEXT("D:\\download");
//	TCHAR FileName[MAX_PATH] = {0};
//	TCHAR Info[1024] = {0};
//
//	if( !InetCheckConnection(lpszUrlFrom) )
//	{
//		StringCchPrintf(Info, 1024, TEXT("%s can not be reached!"),lpszUrlFrom);
//		::MessageBox( NULL, Info, TEXT("Error"), MB_OK | MB_ICONERROR );
//		return false;
//	}
//
//	DWORD nRet = InetHttpDownload( lpszUrlFrom, lpszLocalDirTo, FileName, MAX_PATH );
//	if( 0 == nRet )
//	{
//		StringCchPrintf(Info, 1024, TEXT("%s was downloaded to %s successfully!"),FileName, lpszLocalDirTo);
//		::MessageBox( NULL, Info, TEXT("Info"), MB_OK );
//		return FALSE;
//	}
//	else
//	{
//		StringCchPrintf(Info, 1024, TEXT("Failed to download %s!(%d)"), lpszUrlFrom, nRet);
//		::MessageBox( NULL, Info, TEXT("Error"), MB_OK | MB_ICONERROR );
//	}
//
//	return TRUE;
//}
//

#ifdef __cplusplus
}; // extern "C"
#endif // __cplusplus


#ifdef __cplusplus
// Only for CPP code

namespace InetUtil
{

	class CInetIO
	{
	public:
		CInetIO();
		~CInetIO();
		
		//
		// summary: 
		//		Opens the internet resource file by the URL
		//
		// returns:
		//		TRUE : succeeded to open the file.
		//		FALSE: failed to open the file. Call GetLastError() to query the Win32 error code.
		//
		BOOL Open( LPCTSTR lpszUrl );

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
};

#endif // __cplusplus