#import "UpdateEngineViewController.h"
#import "UpdateEngine.h"
#import "GlobalNotification.h"
#include "FireClientAppDelegate.h"
#import "utils/IOS_Utils.h"
#include "GlobalFunction.h"
#include "log/CoreLog.h"

@implementation UpdateEngineViewController
@synthesize m_pStatusLabel;
//@synthesize m_pProgress;
@synthesize m_pPercentLabel;
@synthesize m_pCurVersion;
@synthesize m_pCurVersionLabel;
@synthesize m_pNewVersion;
@synthesize m_pNewVersionLabel;
@synthesize m_pBackImage;
@synthesize m_downloadurl;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self)
    {
        m_State = 0;
        // Custom initialization
    }
    return self;
}
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
{
    return toInterfaceOrientation == UIInterfaceOrientationLandscapeLeft || toInterfaceOrientation == UIInterfaceOrientationLandscapeRight;
}
-(NSUInteger)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskLandscape;
}
- (void)didReceiveMemoryWarning
{
    printf("receive memory warning\n");
}
-(BOOL)shouldAutorotate
{
    return YES;
}
- (void)dealloc
{
    [m_pStatusLabel release];
//    [m_pProgress release];
    [m_pPercentLabel release];
    [m_pCurVersion release];
    [m_pNewVersion release];
//    [m_pDownLoadSize release];
    [m_pCurVersionLabel release];
    [m_pNewVersionLabel release];
    [_m_pProImg release];
    [_m_pFormBGImage release];
    [_m_pFormView release];
    [_m_pFormTitle release];
    [_m_pFormText release];
    [_m_pFormCancel release];
    [_m_pFormOK release];
    [_m_pFormWIFI release];
    [_m_pStepView release];
    [_m_pStepImageBG release];
    [super dealloc];
}
CGRect ScreenRect = [UIScreen mainScreen].bounds;
CGSize ScreenSize = ScreenRect.size;
CGFloat ScreenScale = [UIScreen mainScreen].scale;
CGFloat UIFontScaleFinal = 1.0;
- (void)ResetUIRectEx2:(UIView*)UI AUIFontSize:(CGFloat)UIFontSize AUIScaleWH:(CGFloat)UIScaleWH AUIScaleX:(CGFloat)UIScaleX AUIScaleY:(CGFloat)UIScaleY AUIFontScale:(CGFloat)UIFontScale
{
    CGFloat UIX = UI.frame.origin.x;
    CGFloat UIY = UI.frame.origin.y;
    CGFloat UIW = UI.frame.size.width;
    CGFloat UIH = UI.frame.size.height;
    CGFloat NewUIX = UIX * UIScaleX;
    CGFloat NewUIY = UIY * UIScaleY;
    CGFloat NewUIW = UIW * UIScaleWH;
    CGFloat NewUIH = UIH * UIScaleWH;
    
    [UI setFrame:CGRectMake(NewUIX, NewUIY, NewUIW, NewUIH)];
    [UI.layer setFrame:CGRectMake(NewUIX, NewUIY, NewUIW, NewUIH)];
    if (UIFontSize > 0)
    {
        [UI setFont:[UIFont systemFontOfSize:UIFontSize * UIFontScale]];
    }
}
static CGFloat fontsize = 0;
- (void)viewDidAppear:(BOOL) animated
{
    [self.m_pFormView setHidden:TRUE];
    [self.m_pStepView setHidden:TRUE];
    m_Step = 0;
    [self onNotifyMsg:@""];
    [self onNotifyStep:0];
    //[self.m_pFormView setHidden:FALSE];
    //[self.m_pStepView setHidden:FALSE];
    [super viewDidAppear: animated];
    if (fontsize < 0.1)
    {
        CGRect r = [self.m_pBackImage bounds];
        CGFloat f = [self.m_pPercentLabel.font pointSize];
        fontsize = f*(r.size.height/(CGFloat)720);
        //[self.m_pPercentLabel setFont:[UIFont systemFontOfSize:fontsize]];
        //[self.m_pStatusLabel setFont:[UIFont systemFontOfSize:fontsize]];
        [self.m_pCurVersion setFont:[UIFont systemFontOfSize:fontsize]];
        [self.m_pCurVersionLabel setFont:[UIFont systemFontOfSize:fontsize]];
        [self.m_pNewVersion setFont:[UIFont systemFontOfSize:fontsize]];
        [self.m_pNewVersionLabel setFont:[UIFont systemFontOfSize:fontsize]];
    }
    CGFloat UIScaleX = 1.0;
    CGFloat UIScaleY = 1.0;
    CGFloat UIScaleY2 = 1.0;
    CGFloat UIScaleWH = UIScaleX;
    CGFloat UIFontScale = 1.0;
    if (ScreenScale == 1)
    {
        if (ScreenSize.height == 1024 && ScreenSize.width == 768)//Pad2
        {
            UIScaleX = 1024.0 / 1280;
            UIScaleY = 1.5;
            UIScaleY2 = 1.15;
            UIScaleWH = UIScaleX;
            UIFontScale = 1.0;
        }
    }
    else if (ScreenScale == 2)
    {
        if (ScreenSize.height == 1366 && ScreenSize.width == 1024)//PadPro
        {
            UIScaleX = 1024.0 / 1280;
            UIScaleY = 1.4;
            UIScaleY2 = 1.09;
            UIScaleWH = UIScaleX;
            UIFontScale = 1.0;
        }
        else if (ScreenSize.height == 1024 && ScreenSize.width == 768)//PadRetina PadAir PadAir2
        {
            UIScaleX = 1024.0 / 1280;
            UIScaleY = 1.5;
            UIScaleY2 = 1.15;
            UIScaleWH = UIScaleX;
            UIFontScale = 1.0;
        }
        else if (ScreenSize.height == 667 && ScreenSize.width == 375)//6 6S
        {
            UIScaleX = 480.0 / 1280;
            UIScaleY = 320.0 / 720;
            UIScaleY2 = 320.0 / 720;
            UIScaleWH = UIScaleY;
            UIFontScale = 0.5;
            UIScaleX = UIScaleY;
        }
        else if (ScreenSize.height == 568 && ScreenSize.width == 320)//5 5S
        {
            UIScaleX = 568.0 / 1280;
            UIScaleY = 320.0 / 720;
            UIScaleY2 = 320.0 / 720;
            UIScaleWH = UIScaleX;
            UIFontScale = 0.5;
        }
        else if (ScreenSize.height == 480 && ScreenSize.width == 320)//4S
        {
            UIScaleX = 480.0 / 1280;
            UIScaleY = 320.0 / 720;
            UIScaleY2 = 320.0 / 720;
            UIScaleWH = UIScaleX;
            UIFontScale = 0.5;
        }
        else
        {
            UIScaleX = ScreenSize.height * 1.0 / 1280;
            UIScaleY = ScreenSize.width * 1.0 / 720;
            UIScaleY2 = ScreenSize.width * 1.0 / ScreenSize.height;
            UIScaleWH = UIScaleY;
            UIFontScale = 0.5;
            UIScaleX = UIScaleY;
        }
    }
    else if (ScreenScale == 3)
    {
        if (ScreenSize.height == 736 && ScreenSize.width == 414)//6P
        {
            UIScaleX = 736.0 / 1280;
            UIScaleY = 414.0 / 720;
            UIScaleY2 = 414.0 / 736.0;
            UIScaleWH = UIScaleY;
            UIFontScale = 0.5;
            UIScaleX = UIScaleY;
        }
        else if (ScreenSize.height == 667 && ScreenSize.width == 375)//6SP
        {
            UIScaleX = 480.0 / 1280;
            UIScaleY = 320.0 / 720;
            UIScaleY2 = 320.0 / 720;
            UIScaleWH = UIScaleY;
            UIFontScale = 0.5;
            UIScaleX = UIScaleY;
        }
        else
        {
            UIScaleX = ScreenSize.height * 1.0 / 1280;
            UIScaleY = ScreenSize.width * 1.0 / 720;
            UIScaleY2 = ScreenSize.width * 1.0 / ScreenSize.height;
            UIScaleWH = UIScaleY;
            UIFontScale = 0.5;
            UIScaleX = UIScaleY;
        }
    }
    else
    {
        UIScaleX = ScreenSize.height * 1.0 / 1280;
        UIScaleY = ScreenSize.width * 1.0 / 720;
        UIScaleY2 = ScreenSize.width * 1.0 / 720;
        UIScaleWH = UIScaleY;
        UIFontScale = 0.5;
        UIScaleX = UIScaleY;
    }
    [self ResetUIRectEx2:self.m_pFormView AUIFontSize:0 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleY AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pFormBGImage AUIFontSize:0 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleX AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pFormTitle AUIFontSize:26 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleX AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pFormText AUIFontSize:24 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleX AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pFormWIFI AUIFontSize:18 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleX AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pFormCancel AUIFontSize:25 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleX AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pFormOK AUIFontSize:25 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleX AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pStepView AUIFontSize:0 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleY2 AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pStepImageBG AUIFontSize:0 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleX AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pStatusLabel AUIFontSize:22 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleX AUIFontScale:UIFontScale];
    [self ResetUIRectEx2:self.m_pPercentLabel AUIFontSize:22 AUIScaleWH:UIScaleWH AUIScaleX:UIScaleX AUIScaleY:UIScaleX AUIFontScale:UIFontScale];
    UIFontScaleFinal = UIFontScale;

    [self performSelectorInBackground:@selector(UpdateMethod) withObject:nil];
}
-(void)UpdateMethod
{
    UpdateEngine::Run();  
}
-(void)StartUpdate
{
    [self performSelectorInBackground:@selector(UpdateMethod) withObject:nil];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
//    [self.m_pDownLoadSize setHidden:TRUE];
//    [self.m_pProgress setHidden:TRUE];
	
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    [self.m_pCurVersionLabel setText:[data valueForKey:@"current_version"]];
    [self.m_pNewVersionLabel setText:[data valueForKey:@"latest_version"]];
    [self.m_pCurVersionLabel setHidden:TRUE];
    [self.m_pNewVersionLabel setHidden:TRUE];
    
    [self.m_pFormOK setTitle:[data valueForKey:@"upformstrok"] forState:0];
    [self.m_pFormOK setTitle:[data valueForKey:@"upformstrok"] forState:1];
    [self.m_pFormCancel setTitle:[data valueForKey:@"upformstrcancel"] forState:0];
    [self.m_pFormCancel setTitle:[data valueForKey:@"upformstrcancel"] forState:1];
    [self.m_pFormTitle setText:[data valueForKey:@"upformstrtitle"]];
    [self.m_pFormWIFI setText:[data valueForKey:@"upformstrwifi"]];

    GlobalNotification::InitWithWatcher(self);
}
- (void)viewDidUnload
{
    [self setM_pStatusLabel:nil];
//    [self setM_pProgress:nil];
    [self setM_pPercentLabel:nil];
    [self setM_pCurVersion:nil];
    [self setM_pNewVersion:nil];
//    [self setM_pDownLoadSize:nil];
    [self setM_pCurVersionLabel:nil];
    [self setM_pNewVersionLabel:nil];
//    [self setM_pLoadingTips:nil];
//    [self setM_pImage:nil];
    [self setM_pBackImage:nil];
    
    [self setM_pProImg:nil];
    [self setM_pFormBGImage:nil];
    [self setM_pFormView:nil];
    [self setM_pFormTitle:nil];
    [self setM_pFormText:nil];
    [self setM_pFormCancel:nil];
    [self setM_pFormOK:nil];
    [self setM_pFormWIFI:nil];
    [self setM_pStepView:nil];
    [self setM_pStepImageBG:nil];
    [super viewDidUnload];
}
-(void)ReturnToGame
{
    FireClientAppDelegate* ka = [[UIApplication sharedApplication] delegate];
    [ka returnToGameController];
}
#pragma mark-ProgressNotify
-(void) onNotifyMsg: (NSString*)msg
{
    [self.m_pPercentLabel setText:msg];
    NSString* CurMsg = msg;
    if (m_Step > 0)
    {
        NSString* CurStep = [NSString stringWithFormat:@"%d%%", m_Step];
        NSString* CurText = [NSString stringWithFormat:@"%@ %@", CurMsg, CurStep];
        [self.m_pStatusLabel setText:CurText];
        if (![CurText isEqual:@""])
        {
            [self.m_pStepView setHidden:FALSE];
        }
    }
    else
    {
        [self.m_pStatusLabel setText:CurMsg];
        if (![CurMsg isEqual:@""])
        {
            [self.m_pStepView setHidden:FALSE];
        }
    }
}
-(void) onNotifyStart : (NSString*)msg
{
    //[self onNotifyMsg:@""];
    //[self onNotifyStep:0];
    //[self.m_pStatusLabel setText:msg];
    //[self.m_pProgress setHidden:FALSE];
    [self.m_pCurVersionLabel setHidden:FALSE];
    [self.m_pNewVersionLabel setHidden:FALSE];
    //[self.m_pPercentLabel setText:[NSString stringWithFormat:@"%d%%",1]];
    //[self.m_pProgress setProgress:1/100.0f];
}
-(void) onNotifyEnd
{
    //[self.m_pPercentLabel setText:[NSString stringWithFormat:@"%d%%",100]];
//    [self.m_pProgress setHidden:FALSE];
//    [self.m_pProgress setProgress:1.0f];
    [self ReturnToGame];
}
-(void) onNotifyFail
{
    //[self.m_pPercentLabel setText:[NSString stringWithFormat:@"%d%%",100]];
//    [self.m_pProgress setHidden:FALSE];
//    [self.m_pProgress setProgress:1.0f];
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    m_State = 4;
    UIAlertView* alert = [[UIAlertView alloc] initWithTitle:[data valueForKey:@"str3"] message:[data valueForKey:@"str4"] delegate:self cancelButtonTitle:[data valueForKey:@"str5"] otherButtonTitles:[data valueForKey:@"str6"] , nil];
    [alert show];
    [data release];
}
-(void) onNotifyStep: (NSNumber*) step
{
    m_Step = step.intValue;
    NSString* CurMsg = [self.m_pPercentLabel text];
    if (m_Step > 0)
    {
        NSString* CurStep = [NSString stringWithFormat:@"%d%%", m_Step];
        NSString* CurText = [NSString stringWithFormat:@"%@ %@", CurMsg, CurStep];
        [self.m_pStatusLabel setText:CurText];
        if (![CurText isEqual:@""])
        {
            [self.m_pStepView setHidden:FALSE];
        }
    }
    else
    {
        [self.m_pStatusLabel setText:CurMsg];
        if (![CurMsg isEqual:@""])
        {
            [self.m_pStepView setHidden:FALSE];
        }
    }

    //[self.m_pPercentLabel setText:CurText];
//    [self.m_pProgress setHidden:FALSE];
//    [self.m_pProgress setProgress:[step intValue]/100.0f];
}
-(void) onNotifyLocalVersion: (NSString*)localVersion
{
    [self.m_pCurVersionLabel setHidden:FALSE];
    [self.m_pCurVersion setText:localVersion];
}
-(void) onNotifyLatestVersion: (NSString*)newVersion
{
    [self.m_pNewVersionLabel setHidden:FALSE];
    [self.m_pNewVersion setText:newVersion];
}
-(void) onNotifyDownLoadSize: (NSString*)sizeString
{
//    [self.m_pDownLoadSize setHidden:FALSE];
//    [self.m_pDownLoadSize setText:sizeString];
}
-(void) onNotifyDownLoadEnd
{
//    [self.m_pDownLoadSize setHidden:TRUE];
}
-(void) onNotifyDownLoadSizeTooLarge:(NSNumber*) total
{
    core::Logger::flurryEvent(L"update_notify_download_largesize");
    m_State = 1;
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    
    NSString* msg = [NSString stringWithFormat:[data valueForKey:@"str7"] , [total unsignedLongLongValue]/(1024.0f*1024.0f)];
    UIAlertView* alert = [[UIAlertView alloc] initWithTitle:[data valueForKey:@"str8"] message:msg delegate:self cancelButtonTitle:[data valueForKey:@"str9"] otherButtonTitles:[data valueForKey:@"str10"], nil];
    [alert show];
    [data release];
}
-(void) onNotifyAppUpdate:(NSDictionary *)dic
{
    NSString *url = [dic objectForKey:@"downloadurl"];
    NSNumber *ret = [dic objectForKey:@"ret"];
//    NSLog(@"UpdateEngineViewController::onNotifyAppUpdate :  %d, %@", [ret intValue], url);
    self.m_downloadurl = url;
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    if([ret intValue] == 2)
    {
        m_State = 2;
//        NSString* msg = [data valueForKey:@"str11"];
//        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:[data valueForKey:@"str12"] message:msg delegate:self cancelButtonTitle:[data valueForKey:@"str10"] otherButtonTitles:nil, nil];
//        [alert show];
    }
    if([ret intValue] == 3)
    {
        m_State = 3;
        NSString* msg = [data valueForKey:@"str11"];
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:[data valueForKey:@"str12"] message:msg delegate:self cancelButtonTitle:[data valueForKey:@"str13"] otherButtonTitles:nil, nil];
        [alert show];
    }
    [data release];
}
//-(void) onNotifyTips:(NSString *)tipString
//{
////    [self.m_pLoadingTips setText:tipString];
//}
- (void)request:(ASIHTTPRequest *)request didReceiveBytes:(long long)bytes
{
//    if (UpdateManager::GetInstance() && UpdateManager::GetInstance()->IsFirstDownloading())
//    {
//        //GlobalNotifyDownLoadSize(UpdateManager::GetInstance()->GetFirstDownloadCurSize() + bytes, UpdateManager::GetInstance()->GetFirstDownloadTotalSize());
//        UpdateManager::GetInstance()->SetFirstDownloadCurSize(UpdateManager::GetInstance()->GetFirstDownloadCurSize() + bytes);
//        float step = 0.0f;
//        step = (float)UpdateManager::GetInstance()->GetFirstDownloadCurSize() / UpdateManager::GetInstance()->GetFirstDownloadTotalSize() * 100;
//        GlobalNotifyStep(step);
//    }
//    else if(CDownloadFiles::GetInstance())
//        CDownloadFiles::GetInstance()->NotifySingleFileRecieved(bytes);
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(m_State == 1)
    {
        if (buttonIndex == 1)
        {
            NSLog(@"OK clicked");
            core::Logger::flurryEvent(L"update_alert_downloadLargeSize_ok_click");
            UpdateEngine::Continue();
        }
        else
        {
            NSLog(@"Cancel clicked");
            core::Logger::flurryEvent(L"update_alert_downloadLargeSize_cancel_click");
            exit(0);
        }
    }
    else if(m_State == 2)
    {
        NSLog(@"OK clicked");
    }
    else if(m_State == 3)
    {
        exit(0);
    }
    else if(m_State == 4)
    {
        if (buttonIndex == 1)
        {
            NSLog(@"OK clicked");
            core::Logger::flurryEvent(L"update_alert_net_unreachable_updateFail_retry");
            [self StartUpdate];
        }
        else
        {
            NSLog(@"Cancel clicked");
            core::Logger::flurryEvent(L"update_alert_updateFail_exitgame");
            exit(0);
        }
    }
}

-(void) onNotifyShowForm: (NSDictionary *)dic
{
    NSNumber *FormType = [dic objectForKey:@"FormType"];
    NSNumber *DownloadSize = [dic objectForKey:@"DownloadSize"];
    NSString *DownloadURL = [dic objectForKey:@"AppURL"];
    
    m_FormType = FormType.intValue;
    [self.m_pPercentLabel setText:DownloadURL];
    if (m_FormType == 1)
    {
        [self ShowForm1];
    }
    if (m_FormType == 2)
    {
        [self ShowForm2];
    }
    if (m_FormType == 3)
    {
        [self ShowForm3];
    }
    if (m_FormType == 4)
    {
        [self ShowForm4 : DownloadSize.intValue];
    }
    if (m_FormType == 5)
    {
        [self ShowForm5];
    }
}
-(void) ShowForm1
{
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    
    [self.m_pFormText setText:[data valueForKey:@"upformstr11"]];//@"获取版本信息失败，是否重试？"
    [self.m_pFormText setTextAlignment:NSTextAlignmentCenter];
    [self.m_pFormText setFont:[UIFont systemFontOfSize:22 * UIFontScaleFinal]];
    [self.m_pFormView setHidden:FALSE];
    
    
//    NSString* S1;
//    S1 = [NSString stringWithFormat:@"height = %f", ScreenSize.height];
//    NSString* S2;
//    S2 = [NSString stringWithFormat:@"width = %f", ScreenSize.width];
//    NSString* S3;
//    S3 = [NSString stringWithFormat:@"ScreenScale = %f", ScreenScale];
//    NSString* S0 ;
//    S0 = [NSString stringWithFormat: @"%@___%@___%@", S1, S2, S3];
//    [self.m_pFormText setText:S0];
    
}
-(void) ShowForm2
{
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    [self.m_pFormText setText:[data valueForKey:@"upformstr21"]];//@"版本过旧，请前往AppStore更新"
    [self.m_pFormText setTextAlignment:NSTextAlignmentCenter];
    [self.m_pFormText setFont:[UIFont systemFontOfSize:22 * UIFontScaleFinal]];
    [self.m_pFormView setHidden:FALSE];
}
-(void) ShowForm3
{
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    [self.m_pFormText setText:[data valueForKey:@"upformstr31"]];//@"获取文件列表失败，是否重试？"
    [self.m_pFormText setTextAlignment:NSTextAlignmentCenter];
    [self.m_pFormText setFont:[UIFont systemFontOfSize:22 * UIFontScaleFinal]];
    [self.m_pFormView setHidden:FALSE];
}
-(void) ShowForm4:(int) DownloadSize
{
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    NSString* S1;
    //S1 = @"检测到新版本可以更新，本次更新包文件大小约为";
    S1 = [data valueForKey:@"upformstr41"];
    NSString* S2;
    if (DownloadSize >= 1024)
    {
        S2 = [NSString stringWithFormat:@"%dMB",DownloadSize / 1024];
    }
    else
    {
        S2 = [NSString stringWithFormat:@"%dKB",DownloadSize];
    }
    NSString* S3;
    //S3 = @"，点击确定下载更新，点击取消退出游戏";
    S3 = [data valueForKey:@"upformstr42"];
    NSString* S0 ;
    S0 = [NSString stringWithFormat: @"%@%@%@", S1, S2, S3];
    [self.m_pFormText setText:S0];
    [self.m_pFormText setTextAlignment:NSTextAlignmentCenter];
    [self.m_pFormText setFont:[UIFont systemFontOfSize:22 * UIFontScaleFinal]];
    [self.m_pFormView setHidden:FALSE];
}
-(void) ShowForm5
{
    NSString* plistPath = [[NSBundle mainBundle] pathForResource:@"appsrc" ofType:@"plist"];
    NSMutableDictionary* data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    [self.m_pFormText setText:[data valueForKey:@"upformstr51"]];//@"下载失败，请检查网络状态和设备存储空间是否足够，是否重试？"
    [self.m_pFormText setTextAlignment:NSTextAlignmentCenter];
    [self.m_pFormText setFont:[UIFont systemFontOfSize:22 * UIFontScaleFinal]];
    [self.m_pFormView setHidden:FALSE];
}
- (IBAction)btnFormCancelTouchUpIn:(id)sender
{
    [self.m_pFormView setHidden:TRUE];
    exit(0);
    m_FormResult = -1;
    UpdateEngine::ContinueEx(m_FormResult);
}
- (IBAction)btnFormOKTouchUpIn:(id)sender
{
    [self.m_pFormView setHidden:TRUE];
    if (m_FormType == 1)
    {
        m_FormResult = 2;
    }
    if (m_FormType == 2)
    {
        NSString* AppURL = [self.m_pPercentLabel text];
        IOS_MHSD_UTILS::OpenURL([AppURL UTF8String]);
        m_FormResult = -1;
        exit(0);
    }
    if (m_FormType == 3)
    {
        m_FormResult = 4;
    }
    if (m_FormType == 4)
    {
        m_FormResult = 6;
    }
    if (m_FormType == 5)
    {
        m_FormResult = 6;
    }
    UpdateEngine::ContinueEx(m_FormResult);
}
@end