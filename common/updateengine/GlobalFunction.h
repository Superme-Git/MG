//  GlobalFunction.h
//  FireClient


#ifndef __FireClient__GlobalFunction__
#define __FireClient__GlobalFunction__

#include "../../ljfm/code/include/ljfmopen.h"
#include <vector>

#define BRF_ROOT  L"/.brief"
#define RES_ROOT  L"/.cache"
#define RUN_ROOT  L"/.run"
#define APP_ROOT  L"/.app"


extern bool IsDirectoryExisting( const wchar_t * lpszDirName );

extern void CreateDir(const std::wstring& dir);
extern void RemoveDir(const std::wstring& dir);

extern void CopyDirectoryWithNotify(const std::string& src, const std::string& dst);
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
extern void CopyFile(const std::string& src, const std::string& dst);
#endif
//////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#ifdef ANDROID
extern void CopyGameSo();
extern void UploadErrorFiles(const std::wstring& fileName);
extern std::wstring getResourceUpdateUrl();
#endif

extern bool needToSelectUpdateUrl(); 
extern void saveHaveSelectUpdateUrl();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

extern void GlobalNotify(const wchar_t* format, ...);

extern void GlobalNotifyMsgAndStep(const std::wstring& msg);

extern void GlobalNotifyEnd();

extern void GlobalNotifyFail();

extern void GlobalNotifyStep(int step);
#ifdef ANDROID
/**
 * 设置进度，会自动随着时间缓慢增长
 * 
 * @param spro
 *            起始值
 * @param epro
 *            终点值
 * @param sec
 *            每增长1%需要的时间（秒）
 */
extern void GlobalNotifyStep2(int spro, int epro, int sec);
#endif

extern void GlobalNotifyMsg(const std::wstring& msg);

extern void GlobalNotify_TextID(int textID);

extern void GlobalNotifyLocalVersion(const std::wstring& localVersion);

extern void GlobalNotifyNewVersion(const std::wstring& newVersion);

extern void GlobalNotifyDownLoadSize(LJFM::fsize_t downloaded , LJFM::fsize_t total);

extern void GlobalNotifyDownLoadEnd();

extern void GlobalNotifyDownLoadSizeTooLarge(unsigned long long total);

extern void GlobalNotifyAppUpdate(int ret, const std::wstring& downloadurl);

extern void GlobalNotifyShowForm(int FormType, int DownloadSize, std::wstring AppURL);

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
extern void CheckCopy();
extern void CopyPatcher();
#else
extern bool CheckMD5(std::string fileName, std::string md5FileName);
#endif

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
extern std::wstring GetFileMD5(std::string fileName);
extern bool UnzipFile(std::string zipFileName, std::string targetPath);
extern void GlobalNotifyMsgByKey(const std::wstring& key);
#ifdef ANDROID
#else
extern BOOL ZipFiles(std::string sZipFileName, std::vector<std::string> sSrcPathFileNames, std::vector<std::string> sDstFileNames);

#endif
#endif

#endif /* defined(__FireClient__GlobalFunction__) */
