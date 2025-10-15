//  #import <UIKit/UIKit.h>.h
//  FireClient

#import <UIKit/UIKit.h>
#import "ASIProgressDelegate.h"
#import "ProgressNotify.h"

@interface UpdateEngineViewController : UIViewController <ProgressNotify,UIAlertViewDelegate,ASIProgressDelegate>
{
    int m_State;
    
    int m_FormType;
    int m_FormResult;
    int m_FormWhileParam;
    
    int m_Step;
    
}

@property (retain, nonatomic) NSMutableArray *m_progimgs;
@property (retain, nonatomic) NSDictionary *m_progdic;
@property (retain, nonatomic) NSMutableArray *m_jumpimgs;
@property (retain, nonatomic) NSDictionary *m_jumpdic;

@property (retain, nonatomic) NSMutableArray *m_imgvarray;

@property (retain, nonatomic) NSString *m_downloadurl;

@property (retain, nonatomic) IBOutlet UILabel *m_pStatusLabel;

//@property (retain, nonatomic) IBOutlet UIProgressView *m_pProgress;

@property (retain, nonatomic) IBOutlet UILabel *m_pPercentLabel;

@property (retain, nonatomic) IBOutlet UILabel *m_pCurVersion;

@property (retain, nonatomic) IBOutlet UILabel *m_pCurVersionLabel;

@property (retain, nonatomic) IBOutlet UILabel *m_pNewVersion;

@property (retain, nonatomic) IBOutlet UILabel *m_pNewVersionLabel;

//@property (retain, nonatomic) IBOutlet UILabel *m_pDownLoadSize;

//@property (retain, nonatomic) IBOutlet UILabel *m_pLoadingTips;

//@property (retain, nonatomic) IBOutlet UIImageView *m_pImage;
@property (retain, nonatomic) IBOutlet UIImageView *m_pProImg;

@property (retain, nonatomic) IBOutlet UIImageView *m_pBackImage;

@property (retain, nonatomic) IBOutlet UIView *m_pFormView;
@property (retain, nonatomic) IBOutlet UIImageView *m_pFormBGImage;
@property (retain, nonatomic) IBOutlet UILabel *m_pFormTitle;
@property (retain, nonatomic) IBOutlet UITextView *m_pFormText;
@property (retain, nonatomic) IBOutlet UIButton *m_pFormCancel;
@property (retain, nonatomic) IBOutlet UIButton *m_pFormOK;
@property (retain, nonatomic) IBOutlet UILabel *m_pFormWIFI;
@property (retain, nonatomic) IBOutlet UIView *m_pStepView;
@property (retain, nonatomic) IBOutlet UIImageView *m_pStepImageBG;




-(void)ReturnToGame;

-(void)UpdateMethod;

-(void)StartUpdate;

-(void)request:(ASIHTTPRequest *)request didReceiveBytes:(long long)bytes;

@end
