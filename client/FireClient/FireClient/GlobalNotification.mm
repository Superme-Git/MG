//  GlobalNotification.cpp
//  FireClient

#include "GlobalNotification.h"
#include "UpdateEngineViewController.h"


GlobalNotification* GlobalNotification::gn = NULL;

id<ProgressNotify,ASIProgressDelegate>& GlobalNotification::getWatcher()
{
    return m_Watcher;
}

void GlobalNotification::onNotifyMsg(NSString* msg)
{
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyMsg:) withObject:msg waitUntilDone:FALSE];
}

void GlobalNotification::onNotifyStart(NSString* msg)
{
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyStart:) withObject:msg waitUntilDone:FALSE];
}

void GlobalNotification::onNotifyEnd()
{
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyEnd) withObject:nil waitUntilDone:FALSE];
}

void GlobalNotification::onNotifyFail()
{
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyFail) withObject:nil waitUntilDone:FALSE];
}

void GlobalNotification::onNotifyStep(int step)
{
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyStep:) withObject:[NSNumber numberWithInt:step] waitUntilDone:FALSE];
}

void GlobalNotification::onNotifyLocalVersion( NSString*localVersion )
{
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyLocalVersion:) withObject:localVersion waitUntilDone:FALSE];
}

void GlobalNotification::onNotifyLatestVersion(NSString* newVersion)
{
    
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyLatestVersion:) withObject:newVersion waitUntilDone:FALSE];
}

void GlobalNotification::onNotifyDownLoadSize(NSString* sizeString)
{
    
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyDownLoadSize:) withObject:sizeString waitUntilDone:FALSE];
}


void GlobalNotification::onNotifyDownLoadEnd()
{    
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyDownLoadEnd) withObject:nil waitUntilDone:FALSE];
}


void GlobalNotification::onNotifyDownLoadSizeTooLarge(unsigned long long total)
{
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyDownLoadSizeTooLarge:) withObject:[NSNumber numberWithUnsignedLongLong:total] waitUntilDone:FALSE];
}

void GlobalNotification::onNotifyAppUpdate(int ret, NSString *downloadurl)
{
    NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithInt:ret], @"ret", downloadurl, @"downloadurl", nil];
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyAppUpdate:) withObject:dic waitUntilDone:FALSE];
}

//void GlobalNotification::onNotifyTips(NSString *tipString)
//{
//    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
//    [viewer performSelectorOnMainThread:@selector(onNotifyTips:) withObject:tipString waitUntilDone:FALSE];
//}


void GlobalNotification::onNotifyShowForm(int FormType, int DownloadSize, NSString *AppURL)
{
    NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:
                         [NSNumber numberWithInt:FormType], @"FormType",
                         [NSNumber numberWithInt:DownloadSize], @"DownloadSize",
                         AppURL, @"AppURL", nil];
    UpdateEngineViewController* viewer = (UpdateEngineViewController*)m_Watcher;
    [viewer performSelectorOnMainThread:@selector(onNotifyShowForm:) withObject:dic waitUntilDone:FALSE];
}

