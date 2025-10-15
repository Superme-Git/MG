//
//  GameCenter_ios.m
//  FireClient
//
//  Created by mtg on 16/5/6.
//
//

#import <Foundation/Foundation.h>
#import <GameKit/GameKit.h>

#include "GameCenter.h"
#include "CCCommon.h"

bool GameCenter::isAvailable() const
{
    Class gcClass = (NSClassFromString(@"GKLocalPlayer"));
    NSString *reqSysVer = @"4.1";
    NSString *currSysVer = [[UIDevice currentDevice] systemVersion];
    BOOL osVersionSupported = ([currSysVer compare:reqSysVer options:NSNumericSearch] != NSOrderedAscending);
    
    bool bAvailable = (gcClass && osVersionSupported);
    NSLog(@"GameCenter available: %d", bAvailable);
    return bAvailable;
}

void GameCenter::login()
{
    setState(STATE_LOGIN_WAITING);
    
    [[GKLocalPlayer localPlayer] authenticateWithCompletionHandler:^(NSError *error){
        if(!GameCenter::GetInstance())
        {
            return;
        }
        
        if (error == nil) {
            //成功处理
            NSLog(@"GameCenter 登陆成功");
            NSLog(@"1--alias--.%@",[GKLocalPlayer localPlayer].alias);
            NSLog(@"2--authenticated--.%d",[GKLocalPlayer localPlayer].authenticated);
            NSLog(@"3--isFriend--.%d",[GKLocalPlayer localPlayer].isFriend);
            NSLog(@"4--playerID--.%@",[GKLocalPlayer localPlayer].playerID);
            NSLog(@"5--underage--.%d",[GKLocalPlayer localPlayer].underage);
            
            setState(STATE_LOGIN_SUCCESS);
            
            //            cocos2d::CCMessageBox("GameCenter 登陆成功！", "");
            
        }else {
            //错误处理
            NSLog(@"GameCenter 登陆失败: %@",error);
            
            if(error.code == 2)  // 玩家取消登录 GameCenter
            {
                setState(STATE_DISABLED);
            }
            else
            {
                setState(STATE_LOGIN_FAILED);
            }
            
            //            std::string str = [error.description UTF8String];
            //            cocos2d::CCMessageBox(str.c_str(), "");
        }
    }];
}

void GameCenter::requestAchievementInfo()
{
    setState(STATE_REQ_ACHI_WAITING);
    
    [GKAchievement loadAchievementsWithCompletionHandler:^(NSArray<GKAchievement *> * _Nullable achievements, NSError * _Nullable error) {
        if(!GameCenter::GetInstance())
        {
            return;
        }
        
        if(error == nil)
        {
            NSLog(@"GameCenter 请求成就成功");
            
            NSMutableDictionary *achiDict = [[NSMutableDictionary alloc] init];
            for (GKAchievement* pAchi in achievements){
                [achiDict setObject:pAchi forKey:pAchi.identifier];
            }
            
            [GKAchievementDescription loadAchievementDescriptionsWithCompletionHandler:^(NSArray<GKAchievementDescription *> * _Nullable descriptions, NSError * _Nullable error) {
                if(!GameCenter::GetInstance())
                {
                    return;
                }
                
                if (error == nil) {
                    NSLog(@"GameCenter 请求成就描述成功");
                    
                    for (GKAchievementDescription *pAchiDesc in descriptions) {
                        NSString* identifier = pAchiDesc.identifier;
                        
                        int id = [identifier intValue];
                        std::string name = [pAchiDesc.title UTF8String];
                        bool bCompleted = false;
                        int needScore = pAchiDesc.maximumPoints;
                        float percent = 0;
                        
                        GKAchievement* pAchi = [achiDict objectForKey:identifier];
                        if(pAchi != nil)
                        {
                            bCompleted = pAchi.completed;
                            percent = pAchi.percentComplete;
                        }
                        
                        int curScore = needScore * percent * 0.01;
                        
                        addAchievementInfo(id, name, bCompleted, needScore, curScore);
                        
                        NSLog(@"GameCenter 成就：%d, %s, %d, %d/%d", id, name.c_str(), bCompleted, curScore, needScore);
                    }
                    
                    setState(STATE_REQ_ACHI_SUCCESS);
                }
                else
                {
                    NSLog(@"GameCenter 请求成就描述失败：%@", error);
                    
                    setState(STATE_REQ_ACHI_FAILED);
                }
            }];
        }
        else
        {
            NSLog(@"GameCenter 请求成就失败：%@", error);
            
            setState(STATE_REQ_ACHI_FAILED);
        }
    }];
}

void GameCenter::sendAchievementScoreImp(int id, int score)
{
    AchievementInfo* pAchi = getAchievementInfoById(id);
    if(pAchi)
    {
        float fPercent = (pAchi->curScore + score) * 100.f / pAchi->needScore;
        
        GKAchievement* pGKAchi = [[GKAchievement alloc] initWithIdentifier:[NSString stringWithFormat:@"%d",id]];
        pGKAchi.percentComplete = fPercent;
        if(fPercent >= 100)
        {
            pGKAchi.showsCompletionBanner = YES;
        }
        
        [pGKAchi reportAchievementWithCompletionHandler:^(NSError * _Nullable error) {
            if(!GameCenter::GetInstance())
            {
                return;
            }
            
            if(error == nil)
            {
                pAchi->curScore += score;
            }
            else
            {
                NSLog(@"GameCenter 发送成就进度失败：%d", id);
                addFailedSendIdScores(id, score);
            }
        }];
    }
}