#include "FileDownloader.h"
#include "GlobalNotification.h"
#include "../GlobalFunction_Common.cpp"

#include <CommonCrypto/CommonDigest.h>
#import "ZipArchive.h"
//#include <pthread.h>
//pthread_t g_tipstid;
//BOOL tipsloop = NO;
//void* threadLoop(void *param)
//{
//    while (tipsloop == YES) {
//        std::string utfstring = SHARE_Wstring2String(GlobalGetText(rand()%51+900));
//        GlobalNotification::GetInstance().onNotifyTips([NSString stringWithUTF8String:utfstring.c_str()]);
//        sleep(5);
//    }
//    return NULL;
//}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
bool IsDirectoryExisting( const wchar_t * lpszDirName )
{
    NSFileManager* fileManager = [NSFileManager defaultManager];
    std::string utf8string = SHARE_Wstring2String(lpszDirName);
    NSString* nsdir = [NSString stringWithUTF8String:utf8string.c_str()];
    return [fileManager fileExistsAtPath:nsdir];
}

void CreateDir(const std::wstring& dir)
{
    NSFileManager* fileManager = [NSFileManager defaultManager];
    NSError* error;
    std::string utf8string = SHARE_Wstring2String(dir);
    NSString* nsdir = [NSString stringWithUTF8String:utf8string.c_str()];
    if(![fileManager fileExistsAtPath:nsdir])
    {
        //create dir
        [fileManager createDirectoryAtPath:nsdir withIntermediateDirectories:YES attributes:nil error:&error];
    }    
}

void RemoveDir(const std::wstring& dir)
{
    NSFileManager* fileManager = [NSFileManager defaultManager];
    std::string utf8string = SHARE_Wstring2String(dir);
    NSString* nsdir = [NSString stringWithUTF8String:utf8string.c_str()];
    [fileManager removeItemAtPath:nsdir error:nil];
}

void CopyFile(const std::string& src, const std::string& dst)
{
    NSFileManager* fileManager = [NSFileManager defaultManager];
    NSError* error;
    if ([fileManager fileExistsAtPath:[NSString stringWithUTF8String:dst.c_str()]])
    {
        [fileManager removeItemAtPath:[NSString stringWithUTF8String:dst.c_str()] error:nil];
    }
    
    [fileManager copyItemAtPath:[NSString stringWithUTF8String:src.c_str()] toPath:[NSString stringWithUTF8String:dst.c_str()] error:&error];
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void GlobalNotify(const wchar_t* format, ...)
{
    
    va_list ap;
    va_start(ap,format);
    std::wstring instring;
    wchar_t buffer[1024] = {0};
    
    int numforwrite = 0;
    
    numforwrite = vsnwprintf_x( buffer,1024,format,ap);
    
    if ( numforwrite > 0)
    {
        std::wstring str(buffer);
        instring = str;
    }
    
    else
        instring = L"";
    va_end(ap);
    
    std::string utfstring = SHARE_Wstring2String(instring);
    GlobalNotification::GetInstance().onNotifyMsg([NSString stringWithUTF8String:utfstring.c_str()]);
}

void GlobalNotifyMsgAndStep(const std::wstring& msg)
{
    std::string utfstring = SHARE_Wstring2String(msg);
    GlobalNotification::GetInstance().onNotifyStart([NSString stringWithUTF8String:utfstring.c_str()]);
}

void GlobalNotifyEnd()
{
    GlobalNotification::GetInstance().onNotifyEnd();
}

void GlobalNotifyFail()
{
    GlobalNotification::GetInstance().onNotifyFail();
}

void GlobalNotifyStep(int step)
{
    GlobalNotification::GetInstance().onNotifyStep(step);
    GlobalNotification::GetInstance().onNotifyStep(step);//Force Refresh By Reset
}

void GlobalNotifyMsg(const std::wstring& msg)
{
    std::string utfstring = SHARE_Wstring2String(msg);
    GlobalNotification::GetInstance().onNotifyMsg([NSString stringWithUTF8String:utfstring.c_str()]);
}
void GlobalNotify_TextID(int textID)
{
//    GlobalNotify(GlobalGetText(textID).c_str()) ;
}

void GlobalNotifyLocalVersion(const std::wstring& localVersion)
{
    std::string utfstring = SHARE_Wstring2String(localVersion);
    GlobalNotification::GetInstance().onNotifyLocalVersion([NSString stringWithUTF8String:utfstring.c_str()]);
}

void GlobalNotifyNewVersion(const std::wstring& newVersion)
{
    std::string utfstring = SHARE_Wstring2String(newVersion);
    GlobalNotification::GetInstance().onNotifyLatestVersion([NSString stringWithUTF8String:utfstring.c_str()]);
}

void GlobalNotifyDownLoadSize(LJFM::fsize_t downloaded , LJFM::fsize_t total)
{
    GlobalNotification::GetInstance().onNotifyDownLoadSize([NSString stringWithFormat:@"%.2f kb／%.2f kb", downloaded/1024.0f, total/1024.0f]);
}

void GlobalNotifyDownLoadEnd()
{
    GlobalNotification::GetInstance().onNotifyDownLoadEnd();
}

void GlobalNotifyDownLoadSizeTooLarge(unsigned long long total)
{
    GlobalNotification::GetInstance().onNotifyDownLoadSizeTooLarge(total);
}

void GlobalNotifyAppUpdate(int ret, const std::wstring& downloadurl)
{
    std::string utfstring = SHARE_Wstring2String(downloadurl);
    GlobalNotification::GetInstance().onNotifyAppUpdate(ret, [NSString stringWithUTF8String:utfstring.c_str()]);
}
void GlobalNotifyShowForm(int FormType, int DownloadSize, std::wstring AppURL)
{
    std::string utfstring = SHARE_Wstring2String(AppURL);
    GlobalNotification::GetInstance().onNotifyShowForm(FormType, DownloadSize, [NSString stringWithUTF8String:utfstring.c_str()]);
}
//
//void GlobalNotifyTips()
//{
//    if (tipsloop == NO) {
//        tipsloop = YES;
//        pthread_create(&g_tipstid, NULL, threadLoop, NULL);
//        srand((unsigned int)time(NULL));
//    }
//}

bool CheckMD5(std::string fileName, std::string md5FileName)
{
    NSFileHandle* handle = [NSFileHandle fileHandleForReadingAtPath:[NSString stringWithUTF8String:fileName.c_str()]];
    if(handle == nil)
        return false;
    
    CC_MD5_CTX md5_ctx;
    CC_MD5_Init(&md5_ctx);
    
    NSData* filedata;
    do {
        filedata = [handle readDataOfLength:1024];
        CC_MD5_Update(&md5_ctx, [filedata bytes], [filedata length]);
        
    }
    while([filedata length]);
    
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_MD5_Final(result, &md5_ctx);
    [handle closeFile];
    
    NSMutableString *hash = [NSMutableString string];
    for(int i=0;i<CC_MD5_DIGEST_LENGTH;i++)
    {
        [hash appendFormat:@"%02x",result[i]];
    }
    
    NSError *err;
    NSString *hash2 = [NSString stringWithContentsOfFile:[NSString stringWithUTF8String:md5FileName.c_str()] encoding:NSUTF8StringEncoding error:&err] ;
    if (hash2 == nil) {
        return false;
    }
    return [hash isEqualToString:hash2];
}

std::wstring GetFileMD5(std::string fileName)
{
    NSFileHandle* handle = [NSFileHandle fileHandleForReadingAtPath:[NSString stringWithUTF8String:fileName.c_str()]];
    if(handle == nil)
        return L"";
    
    CC_MD5_CTX md5_ctx;
    CC_MD5_Init(&md5_ctx);
    
    NSData* filedata;
    do {
        filedata = [handle readDataOfLength:1024];
        CC_MD5_Update(&md5_ctx, [filedata bytes], [filedata length]);
        
    }
    while([filedata length]);
    
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_MD5_Final(result, &md5_ctx);
    [handle closeFile];
    
    NSMutableString *hash = [NSMutableString string];
    for(int i=0;i<CC_MD5_DIGEST_LENGTH;i++)
    {
        [hash appendFormat:@"%02x",result[i]];
    }
    return (wchar_t*)[hash cStringUsingEncoding:NSUTF32StringEncoding];
}

bool UnzipFile(std::string zipFileName, std::string targetPath)
{
    ZipArchive* za = [[ZipArchive alloc]init];
    if ([za UnzipOpenFile:[NSString stringWithUTF8String:zipFileName.c_str()]]) {
        BOOL ret = [za UnzipFileTo:[NSString stringWithUTF8String:targetPath.c_str()] overWrite:YES];
        if (NO == ret) {
            return false;
        }
        [za UnzipCloseFile];
    }
    [za release];
    return true;
}

BOOL ZipFiles(std::string sZipFileName, std::vector<std::string> sSrcPathFileNames, std::vector<std::string> sDstFileNames)
{
    ZipArchive* za = [[ZipArchive alloc]init];
    NSString* nssZipFileName = [NSString stringWithUTF8String:sZipFileName.c_str()];
    [za CreateZipFile2:nssZipFileName];
    for (int i = 0; i < sDstFileNames.size(); i++)
    {
        NSString* nssCurSrcPathFileName = [NSString stringWithUTF8String:sSrcPathFileNames[i].c_str()];
        NSString* nssCurSrcFileName = [NSString stringWithUTF8String:sDstFileNames[i].c_str()];
        [za addFileToZip:nssCurSrcPathFileName newname:nssCurSrcFileName];
    }
    BOOL BResult = [za CloseZipFile2];
    [za release];
    return BResult;
}

void GlobalNotifyMsgByKey(const std::wstring& key)
{
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    std::string skey = SHARE_Wstring2String(key);
    NSString* SMSG;
    SMSG = [data valueForKey:[NSString stringWithUTF8String:skey.c_str()]];
    GlobalNotification::GetInstance().onNotifyMsg(SMSG);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

bool needToSelectUpdateUrl()
{
#ifdef IOS_LARGE_RESOURCE_UPDATE
    std::string plistPathString = CFileUtil::GetCacheDir() + "/res/updatetype";
    NSString* plistPath = [NSString stringWithUTF8String:plistPathString.c_str()];
    NSDictionary* dicRead = [[NSDictionary alloc] initWithContentsOfFile:plistPath];

    NSString* haveselected = [dicRead objectForKey:@"haveselected"];
    
    
    if (haveselected != nil && [haveselected isEqualToString:@"yes"] )
    {
        [dicRead release];
        return false;
    }
    [dicRead release];
    return true;
#else
    return false;
#endif
}

void saveHaveSelectUpdateUrl()
{
#ifdef IOS_LARGE_RESOURCE_UPDATE
    std::string plistPathString = CFileUtil::GetCacheDir() + "/res/updatetype";
    NSString* plistPath = [NSString stringWithUTF8String:plistPathString.c_str()];
    NSMutableDictionary* dicWrite = [[NSMutableDictionary alloc] init];
    [dicWrite setValue:@"yes" forKey:@"haveselected"];
    [dicWrite writeToFile:plistPath atomically:YES];
    [dicWrite release];
    dicWrite = nil;
#endif
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
