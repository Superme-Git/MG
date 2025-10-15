#ifndef __FireClient__GlobalNotification__
#define __FireClient__GlobalNotification__

#import "ProgressNotify.h"
#import "ASIProgressDelegate.h"

class GlobalNotification
{
public:
    static GlobalNotification& InitWithWatcher(id<ProgressNotify,ASIProgressDelegate> pn)
    {        
        if(!gn)
        {
            gn = new GlobalNotification(pn);
        }
        return *gn;
    }
    
    static void Destroy()
    {
        if(gn)
        {
            delete gn;
            gn = NULL;
        }
    }
    
    static GlobalNotification& GetInstance()
    {
        return *gn;
    }
    
public:
    void onNotifyMsg(NSString* msg);
    
    void onNotifyStart(NSString* msg);
    
    void onNotifyEnd();
    
    void onNotifyFail();
    
    void onNotifyStep(int step);    
    
    void onNotifyLocalVersion( NSString*localVersion );
    
    void onNotifyLatestVersion(NSString* newVersion);
    
    void onNotifyDownLoadSize(NSString* sizeString);
    
    void onNotifyDownLoadEnd();
    
    void onNotifyDownLoadSizeTooLarge(unsigned long long total);
    
    void onNotifyAppUpdate(int ret, NSString *downloadurl);
    
//    void onNotifyTips(NSString* tipString);
    
    void onNotifyShowForm(int FormType, int DownloadSize, NSString *AppURL);
    
    id<ProgressNotify,ASIProgressDelegate>& getWatcher();

private:
    GlobalNotification(id<ProgressNotify,ASIProgressDelegate> pn):m_Watcher(pn){}
    ~GlobalNotification() { m_Watcher = NULL; }
    id<ProgressNotify,ASIProgressDelegate> m_Watcher;
    
    static GlobalNotification* gn;
};



#endif /* defined(__FireClient__GlobalNotification__) */
