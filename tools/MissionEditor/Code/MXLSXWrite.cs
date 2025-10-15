using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.Xml.Serialization;
using System.IO;
using System.Threading;
using System.Reflection;
using System.Windows;
using System.Windows.Data;
using System.ComponentModel;
using System.Collections.ObjectModel;
using Knight.MissionEditor.Xaml;

using Excel = Microsoft.Office.Interop.Excel;
using NPOI.XSSF.UserModel;
using NPOI.SS.UserModel;


namespace Knight.MissionEditor
{
    public class MissionInfoAIO
    {
        //任务ID
        public int MissionID;

        public int AutoDo;

        //任务的名称
        public string MissionName;

        //任务类型：如主线任务
        public string MissionTypeString;

        //等级限制
        public int MinLevel;

        public int MaxLevel;

        //转生限制
        public int TransMinLevel;

        public int TransMaxLevel;

        //需要完成任务
        public int[] RequestMissionList;

        //需要角色
        public long[] RequestRoleIDList;

        //后置任务ID
        public int[] PostMissionList;

        //变身造型
        public int TransformID;

        //透明框提示
        public string NoteInfo;

        //经验奖励
        public long ExpReward;

        //金钱奖励
        public long MoneyReward;

        //宠物经验奖励
        public long PetExpReward;

        //历练声望
        public int ShengWang;

        //储备金奖励
        public long SMoney;

        //跳转方式
        public int RewardMapJumpType; //0：无，1：传送，2：飞行, 3: 副本

        //跳转地图ID
        public int RewardMapID;

        //跳转坐标X
        public int RewardMapXPos;

        //跳转坐标Y
        public int RewardMapYPos;

        //进度条时间 
        public int ProcessBarTime;

        //进度条文字
        public string ProcessBarText;

        //进度条颜色
        public string ProcessBarColor;

        
        //显示NpcID
        public int[] DisPlayNPCID;

        //奖励任务列表
        public int[] RewardItemIDList;
        public int[] RewardItemNumList;
        public int[] RewardItemShapeIDList;
        public int[] RewardItemIsBindList;

        //任务类型
        public int MissionType;

        //Npc ID
        public int ActiveInfoNpcID;

        //Map ID
        public int ActiveInfoMapID;

        //地图场景范围
        public int ActiveInfoLeftPos;
        public int ActiveInfoTopPos;
        public int ActiveInfoRightPos;
        public int ActiveInfoBottomPos;

        //对象ID 
        public int ActiveInfoTargetID;

        //对象Num;
        public int ActiveInfoTargetNum;

        //最少结束步骤
        public int ActiveInfoMiniStep;

        //单步结束概率
        public int ActiveInfoStepProbability;

        //最大结束步骤
        public int ActiveInfoMaxStep;

        //是否组队完成
        // 0 不可以， 1 必须， 2 都可以
        public int ActiveInfoTeamState;

        //时间限制分钟
        public int ActiveInfoTimeLimit;

        //是否从新计时
        public int ActiveInfoIsRestartTimer;

        //回收金钱数量
        public long ActiveInfoGiveBackMoney;

        //回收Pet ID
        public int ActiveInfoGiveBackPetID;

        //使用物品ID
        public int ActiveInfoUseItemID;

        //其他触发类型
        public int ActiveInfoOtherType;

        //正确答案
        public string QuestionInfoCorrectAnswer;

        //错误答案 max num = 5;
        public string[] QuestionInfoWrongAnswerList;

        //
        public int QuestionInfoNpcID;

        //
        public string QuestionInfoConversion;

        //描述 max num = 1
        public string TaskInfoDescriptionListA;

        //目的 max num = 2
        public string TaskInfoPurposeListA;

        //追踪二
        public string TaskInfoTraceListA;

        //AI ID
        public int AIInfoAIID;


        //战斗结果
        // 0： 必须失败 , 1: 必须胜利 , 2: 都可以
        public int AIInfoBattleResult;

        //战斗死亡惩罚
        public int AIInfoDeathPunish;



        //组队
        // 0 不可以， 1 必须， 2 都可以
        public int AIInfoTeamSteate;

        //战斗难度等级
        public string AIInfoBattleLevel;


        //战斗地图类型
        //0： 明雷区  1:暗雷区
        public int BattleInfoBattleMapType;

        //战斗地图区域
        public int BattleInfoBattleZoneID;

        //掉落物品几率
        public int BattleInfoDrop;

        //战斗次数
        public int BattleInfoBattleTimes;

        //怪物种类
        //public int BattleInfoMonsterType;
        public int[] BattleInfoMonsterList;

        //怪物数量
        public int BattleInfoMonsterNum;

        //掉落物品ID
        public int BattleInfoDropItemID;

        //掉落物品数量
        public int BattleInfoDropItemNum;

        //剧情动画ID
        public int ScenarioInfoAnimationID;

        //分支剧情
        public int ScenarioInfoBranchNpcID;

        //分支对话提示
        public string ScenarioInfoBranchNote;

        //分支选项 
        //max num = 3
        public string[] ScenarioInfoBranchOptionList;

        //对话内容
        public string[] ScenarioInfoNpcConversationList;
        //说话的NPC ：0 是自己
        public int[] ScenarioInfoNpcID;

        //完成后对话
        public string[] ScenarioInfoFinishConversationList;
        //完成后对话NPC ：0 是自己
        public int[] ScenarioInfoFinishNpcID;



        //////////////////////////////////////////////////////////////////////////

        public MissionInfoAIO(MissionInfo mInfo)
        {
            //
            if(mInfo.MissionID!=null)
            {
                if (mInfo.MissionID.Length > 0)
                {
                    MissionID = Convert.ToInt32(mInfo.MissionID);
                }else
                {
                    MissionID = 0;
                }
            }
            else
            {
                MissionID = 0;
            }

            if (mInfo.AutoDo != null)
            {
                if (mInfo.AutoDo.Length > 0)
                {
                    AutoDo = Convert.ToInt32(mInfo.AutoDo);
                }
                else
                {
                    AutoDo = 0;
                }
            }
            else
            {
                AutoDo = 0;
            }


            if (mInfo.AIInfo.AIID != null)
            {
                if (mInfo.AIInfo.AIID.Length > 0)
                {
                    AIInfoAIID = Convert.ToInt32(mInfo.AIInfo.AIID);
                }
                else
                {
                    AIInfoAIID = 0;
                }
            }
            else
            {
                AIInfoAIID = 0;
            }

            if (mInfo.MissionName != null)
            {
                if (mInfo.MissionName.Length > 0)
                {
                    MissionName = mInfo.MissionName;
                }
                else
                {
                    MissionName = null;
                }
            }
            else
            {
                MissionName = null;
            }

            if (mInfo.MissionTypeString != null)
            {
                if (mInfo.MissionTypeString.Length > 0)
                {
                    MissionTypeString = mInfo.MissionTypeString;
                }
                else
                {
                    MissionTypeString = null;
                }
            }
            else
            {
                MissionTypeString = "";
            }

            if (mInfo.LimitInfo.MinLevel != null)
            {
                if (mInfo.LimitInfo.MinLevel.Length > 0)
                {
                    MinLevel = Convert.ToInt32(mInfo.LimitInfo.MinLevel);
                }
                else
                {
                    MinLevel = 0;
                }
            }
            else
            {
                MinLevel = 0;
            }

            if (mInfo.LimitInfo.TransMinLevel != null)
            {
                if (mInfo.LimitInfo.TransMinLevel.Length > 0)
                {
                    TransMinLevel = Convert.ToInt32(mInfo.LimitInfo.TransMinLevel);
                }
                else
                {
                    TransMinLevel = 0;
                }
            }
            else
            {
                TransMinLevel = 0;
            }

            if (mInfo.LimitInfo.TransMaxLevel != null)
            {
                if (mInfo.LimitInfo.TransMaxLevel.Length > 0)
                {
                    TransMaxLevel = Convert.ToInt32(mInfo.LimitInfo.TransMaxLevel);
                }
                else
                {
                    TransMaxLevel = 0;
                }
            }
            else
            {
                TransMaxLevel = 0;
            }

            RequestMissionList = new int[50];
            for (int i = 0; i < mInfo.LimitInfo.RequestMissionList.Count; ++i)
            {
                if(mInfo.LimitInfo.RequestMissionList[i]!=null)
                {
                    if (mInfo.LimitInfo.RequestMissionList[i].MissionID.Length > 0)
                    {
                        RequestMissionList[i] =
                            Convert.ToInt32(mInfo.LimitInfo.RequestMissionList[i].MissionID);
                    }else
                    {
                        RequestMissionList[i] = 0;
                    }
                }
                else
                {
                    RequestMissionList[i] = 0;
                }
            }

            RequestRoleIDList = new long[50];

            for (int i = 0; i < mInfo.LimitInfo.RequestRoleIDList.Count; ++i)
            {
                if(mInfo.LimitInfo.RequestRoleIDList[i]!=null)
                {
                    if (mInfo.LimitInfo.RequestRoleIDList[i].String.Length > 0)
                    {
                        RequestRoleIDList[i] =
                            Convert.ToInt64(mInfo.LimitInfo.RequestRoleIDList[i].String);
                    }else
                    {
                        RequestRoleIDList[i] = 0;
                    }
                }
                else
                {
                    RequestRoleIDList[i] = 0;
                }
            }

            PostMissionList = new int[50];
            for (int i = 0; i < mInfo.RewardInfo.PostMissionList.Count; ++i)
            {
                if (mInfo.RewardInfo.PostMissionList[i] != null)
                {
                    if (mInfo.RewardInfo.PostMissionList[i].MissionID.Length > 0)
                    {
                        PostMissionList[i] =
                            Convert.ToInt32(mInfo.RewardInfo.PostMissionList[i].MissionID);
                    }else
                    {
                        PostMissionList[i] = 0;
                    }
                }
                else
                {
                    PostMissionList[i] = 0;
                }
            }

            DisPlayNPCID = new int[50];
            for (int i = 0; i < mInfo.RewardInfo.DisplayNpcList.Count; ++i)
            {
                if(mInfo.RewardInfo.DisplayNpcList[i]!=null)
                {
                    if (mInfo.RewardInfo.DisplayNpcList[i].String.Length > 0)
                    {
                        DisPlayNPCID[i] =
                            Convert.ToInt32(mInfo.RewardInfo.DisplayNpcList[i].String);
                    }else
                    {
                        DisPlayNPCID[i] = 0;
                    }
                }
                else
                {
                    DisPlayNPCID[i] = 0;
                }
            }

            if(mInfo.RewardInfo.TransformID!=null)
            {
                if(mInfo.RewardInfo.TransformID.Length>0)
                {
                    TransformID = Convert.ToInt32(mInfo.RewardInfo.TransformID);
                }else
                {
                    TransformID = 0;
                }
            }
            else
            {
                TransformID = 0;
            }

            if (mInfo.RewardInfo.NoteInfo != null)
            {
                if (mInfo.RewardInfo.NoteInfo.Length > 0)
                {
                    NoteInfo = mInfo.RewardInfo.NoteInfo;
                }else
                {
                    NoteInfo = null;
                }
            }
            else
            {
                NoteInfo = null;
            }

            if (mInfo.RewardInfo.ExpReward != null)
            {
                if (mInfo.RewardInfo.ExpReward.Length > 0)
                {
                    ExpReward = Convert.ToInt64(mInfo.RewardInfo.ExpReward);
                }else
                {
                    ExpReward = 0;
                }
            }
            else
            {
                ExpReward = 0;
            }

            if(mInfo.RewardInfo.MoneyReward!=null)
            {
                if (mInfo.RewardInfo.MoneyReward.Length>0)
                {
                    MoneyReward = Convert.ToInt64(mInfo.RewardInfo.MoneyReward);
                
                }else
                {
                    MoneyReward = 0;
                }
            }
            else
            {
                MoneyReward = 0;
            }

            if(mInfo.RewardInfo.PetExpReward!=null)
            {
                if(mInfo.RewardInfo.PetExpReward.Length>0)
                {
                    PetExpReward = Convert.ToInt64(mInfo.RewardInfo.PetExpReward);
                }else
                {
                    PetExpReward = 0;
                }
            }
            else
            {
                PetExpReward = 0;
            }

            if(mInfo.RewardInfo.RewardPetID!=null)
            {
                if(mInfo.RewardInfo.RewardPetID.Length>0)
                {
                    ShengWang = Convert.ToInt32(mInfo.RewardInfo.RewardPetID);
                }
                else
                {
                    ShengWang = 0;
                }
            }
            else
            {
                ShengWang = 0;
            }

            if(mInfo.RewardInfo.SMoney!=null)
            {
                if(mInfo.RewardInfo.SMoney.Length>0)
                {
                    SMoney = Convert.ToInt64(mInfo.RewardInfo.SMoney);
                }else
                {
                    SMoney = 0;
                }
            }
            else
            {
                SMoney = 0;
            }

            if (mInfo.RewardInfo.JumpType == "无")
                RewardMapJumpType = 0;
            else if (mInfo.RewardInfo.JumpType == "传送")
                RewardMapJumpType = 1;
            else if (mInfo.RewardInfo.JumpType == "飞行")
                RewardMapJumpType = 2;
            else if (mInfo.RewardInfo.JumpType == "副本")
                RewardMapJumpType = 3;
            
            if(mInfo.RewardInfo.rewardMapID!=null)
            {
                if(mInfo.RewardInfo.rewardMapID.Length>0)
                {
                    RewardMapID = Convert.ToInt32(mInfo.RewardInfo.rewardMapID);
                }else
                {
                    RewardMapID = 0;
                }
            }
            else
            {
                RewardMapID = 0;
            }

            if(mInfo.RewardInfo.rewardMapXPos!=null)
            {
                if(mInfo.RewardInfo.rewardMapXPos.Length > 0)
                {
                    RewardMapXPos = Convert.ToInt32(mInfo.RewardInfo.rewardMapXPos);
                
                }else
                {
                    RewardMapXPos = 0;
                }
            }
            else
            {
                RewardMapXPos = 0;
            }

            if(mInfo.RewardInfo.rewardMapYPos!=null)
            {
                if(mInfo.RewardInfo.rewardMapYPos.Length > 0)
                {
                    RewardMapYPos = Convert.ToInt32(mInfo.RewardInfo.rewardMapYPos);
                
                }else
                {
                    RewardMapYPos = 0;
                }
            }
            else
            {
                RewardMapYPos = 0;
            }

            if(mInfo.RewardInfo.ProcessBarTime!=null)
            {
                if(mInfo.RewardInfo.ProcessBarTime.Length > 0)
                {
                    ProcessBarTime = Convert.ToInt32(mInfo.RewardInfo.ProcessBarTime);
                
                }else
                {
                    ProcessBarTime = 0;
                }
            }
            else
            {
                ProcessBarTime = 0;
            }

            if(mInfo.RewardInfo.ProcessBarText!=null)
            {
                if(mInfo.RewardInfo.ProcessBarText.Length>0)
                {

                    ProcessBarText = mInfo.RewardInfo.ProcessBarText;
                
                }else
                {
                    ProcessBarText = null;
                }
            }
            else
            {
                ProcessBarText = null;
            }
            
            if(mInfo.RewardInfo.ProcessBarColor!=null)
            {
                if(mInfo.RewardInfo.ProcessBarColor.Length>0)
                {
                    ProcessBarColor = mInfo.RewardInfo.ProcessBarColor;
                }else
                {
                    ProcessBarColor = null;
                }
            }
            else
            {
                ProcessBarColor = null;
            }
            
            RewardItemIDList        = new int[50];
            RewardItemNumList       = new int[50];
            RewardItemShapeIDList   = new int[50];
            RewardItemIsBindList    = new int[50];
            for (int i = 0; i < mInfo.RewardInfo.RewardItemList.Count; ++i)
            {
                if(mInfo.RewardInfo.RewardItemList[i].ID!=null)
                {
                    if(mInfo.RewardInfo.RewardItemList[i].ID.Length>0)
                    {
                        RewardItemIDList[i] =
                            Convert.ToInt32(mInfo.RewardInfo.RewardItemList[i].ID);
                    }else
                    {
                        RewardItemIDList[i] = 0;
                    }
                }
                else
                {
                        RewardItemIDList[i] = 0;
                }

                if(mInfo.RewardInfo.RewardItemList[i].Num!=null)
                {
                    if(mInfo.RewardInfo.RewardItemList[i].Num.Length>0)
                    {
                        RewardItemNumList[i] =
                            Convert.ToInt32(mInfo.RewardInfo.RewardItemList[i].Num);
                    }else
                    {
                        RewardItemNumList[i] = 0;
                    }
                }
                else
                {
                    RewardItemNumList[i] = 0;
                }

                if(mInfo.RewardInfo.RewardItemList[i].ShapeID!=null)
                {
                    if(mInfo.RewardInfo.RewardItemList[i].ShapeID.Length>0)
                    {
                        RewardItemShapeIDList[i] =
                            Convert.ToInt32(mInfo.RewardInfo.RewardItemList[i].ShapeID);
                    }else
                    {
                        RewardItemShapeIDList[i] = 0;
                    }
                }
                else
                {
                    RewardItemShapeIDList[i] = 0;
                }

                if(mInfo.RewardInfo.RewardItemList[i].IsBind!=null)
                {
                    if(mInfo.RewardInfo.RewardItemList[i].IsBind.Length > 0)
                    {
                        RewardItemIsBindList[i] =
                            Convert.ToInt32(mInfo.RewardInfo.RewardItemList[i].IsBind);
                    }else
                    {
                        RewardItemIsBindList[i] = 0;
                    }
                }
                else
                {
                    RewardItemIsBindList[i] = 0;
                }
            }

            if (mInfo.ActiveInfo.MissionType!=null)
            {
                string tmpstr;

                if(mInfo.ActiveInfo.MissionType.Length > 0)
                {
                    tmpstr = mInfo.ActiveInfo.MissionType.Substring(0, 2);
                    MissionType = Convert.ToInt32(tmpstr);
                }
                else
                {
                    MissionType = 0;
                    MessageBox.Show("任务类型字段有错误，请检查表格");
                }

            }else
            {
                MissionType = 0;
            }

            if (mInfo.ActiveInfo.NpcID!=null)
            {
                if(mInfo.ActiveInfo.NpcID.Length>0)
                {
                    ActiveInfoNpcID = Convert.ToInt32(mInfo.ActiveInfo.NpcID);
                }else
                {
                    ActiveInfoNpcID = 0;
                }
            }
            else
            {
                ActiveInfoNpcID = 0;
            }

            if(mInfo.ActiveInfo.MapID!=null)
            {
                if(mInfo.ActiveInfo.MapID.Length>0)
                {
                    ActiveInfoMapID = Convert.ToInt32(mInfo.ActiveInfo.MapID);
                }else
                {
                    ActiveInfoMapID = 0;
                }
            }
            else
            {
                ActiveInfoMapID = 0;
            }

            if(mInfo.ActiveInfo.LeftPos!=null)
            {
                if(mInfo.ActiveInfo.LeftPos.Length>0)
                {
                    ActiveInfoLeftPos = Convert.ToInt32(mInfo.ActiveInfo.LeftPos);
                }else
                {
                    ActiveInfoLeftPos = 0;
                }
            }
            else
            {
                ActiveInfoLeftPos = 0;
            }

            if(mInfo.ActiveInfo.TopPos!=null)
            {
                if(mInfo.ActiveInfo.TopPos.Length>0)
                {
                    ActiveInfoTopPos = Convert.ToInt32(mInfo.ActiveInfo.TopPos);
                }else
                {
                    ActiveInfoTopPos = 0;
                }
            }
            else
            {
                ActiveInfoTopPos = 0;
            }

            if(mInfo.ActiveInfo.RightPos!=null)
            {
                if(mInfo.ActiveInfo.RightPos.Length>0)
                {
                    ActiveInfoRightPos = Convert.ToInt32(mInfo.ActiveInfo.RightPos);
            
                }else
                {
                    ActiveInfoRightPos = 0;
                }
            }
            else
            {
                ActiveInfoRightPos = 0;
            }

            if(mInfo.ActiveInfo.BottomPos!=null)
            {
                if(mInfo.ActiveInfo.BottomPos.Length>0)
                {
                    ActiveInfoBottomPos = Convert.ToInt32(mInfo.ActiveInfo.BottomPos);
                }else
                {
                    ActiveInfoRightPos = 0;
                }
            }
            else
            {
                ActiveInfoRightPos = 0;
            }

            if(mInfo.ActiveInfo.BottomPos!=null)
            {
                if(mInfo.ActiveInfo.BottomPos.Length>0)
                {
                    ActiveInfoBottomPos = Convert.ToInt32(mInfo.ActiveInfo.BottomPos);
                }else
                {
                    ActiveInfoRightPos  = 0;
                }
            }
            else
            {
                ActiveInfoRightPos = 0;
            }

            if(mInfo.ActiveInfo.TargetID!=null)
            {
                if(mInfo.ActiveInfo.TargetID.Length>0)
                {
                    ActiveInfoTargetID = Convert.ToInt32(mInfo.ActiveInfo.TargetID);
                }else
                {
                    ActiveInfoTargetID = 0;
                }
            }
            else
            {
                ActiveInfoTargetID = 0;
            }

            if(mInfo.ActiveInfo.TargetNum!=null)
            {
                if(mInfo.ActiveInfo.TargetNum.Length>0)
                {
                    ActiveInfoTargetNum = Convert.ToInt32(mInfo.ActiveInfo.TargetNum);
                }else
                {
                    ActiveInfoTargetNum = 0;
                }
            }
            else
            {
                ActiveInfoTargetNum = 0;
            }

            if(mInfo.ActiveInfo.TargetNum!=null)
            {
                if(mInfo.ActiveInfo.TargetNum.Length>0)
                {
                    ActiveInfoTargetNum = Convert.ToInt32(mInfo.ActiveInfo.TargetNum);
                }else
                {
                    ActiveInfoTargetNum = 0;
                }
            }
            else
            {
                ActiveInfoTargetNum = 0;
            }

            if(mInfo.ActiveInfo.MiniStep!=null)
            {
                if(mInfo.ActiveInfo.MiniStep.Length>0)
                {
                    ActiveInfoMiniStep = Convert.ToInt32(mInfo.ActiveInfo.MiniStep);
                }else
                {
                    ActiveInfoMiniStep = 0;
                }
            }
            else
            {
                ActiveInfoMiniStep = 0;
            }

            if(mInfo.ActiveInfo.MiniStep!=null)
            {
                if(mInfo.ActiveInfo.StepProbability.Length>0)
                {
                    ActiveInfoStepProbability = Convert.ToInt32(mInfo.ActiveInfo.StepProbability);
                }else
                {
                    ActiveInfoStepProbability = 0;
                }
            }
            else
            {
                ActiveInfoStepProbability = 0;
            }

            if(mInfo.ActiveInfo.MaxStep!=null)
            {
                if(mInfo.ActiveInfo.MaxStep.Length>0)
                {
                    ActiveInfoMaxStep = Convert.ToInt32(mInfo.ActiveInfo.MaxStep);
                }else
                {
                    ActiveInfoMaxStep = 0;
                }
            }
            else
            {
                ActiveInfoMaxStep = 0;
            }


            if (mInfo.ActiveInfo.TeamState == "Yes")
                ActiveInfoTeamState = 1;
            else if (mInfo.ActiveInfo.TeamState == "No")
                ActiveInfoTeamState = 0;
            else if (mInfo.ActiveInfo.TeamState == "Both")
                ActiveInfoTeamState = 2;
            else
                ActiveInfoTeamState = 2;

            if(mInfo.ActiveInfo.TimeLimit!=null)
            {
                if(mInfo.ActiveInfo.TimeLimit.Length>0)
                {
                    ActiveInfoTimeLimit = Convert.ToInt32(mInfo.ActiveInfo.TimeLimit);
                }else
                {
                     ActiveInfoTimeLimit = 0;
                }
            }
            else
            {
                ActiveInfoTimeLimit = 0;
            }

            if (mInfo.ActiveInfo.IsRestartTimer == "Yes")
                ActiveInfoIsRestartTimer = 1;
            else
                ActiveInfoIsRestartTimer = 0;

            if(mInfo.ActiveInfo.GiveBackMoney!=null)
            {
                if(mInfo.ActiveInfo.GiveBackMoney.Length>0)
                {
                    ActiveInfoGiveBackMoney = Convert.ToInt64(mInfo.ActiveInfo.GiveBackMoney);
                }
            }else
            {
                ActiveInfoGiveBackMoney = 0;
            }

            if(mInfo.ActiveInfo.GiveBackPetID!=null)
            {
                if(mInfo.ActiveInfo.GiveBackPetID.Length > 0)
                {
                    ActiveInfoGiveBackPetID = Convert.ToInt32(mInfo.ActiveInfo.GiveBackPetID);
                }else
                {
                    ActiveInfoGiveBackPetID = 0;
                }
            }
            else
            {
                ActiveInfoGiveBackPetID = 0;
            }

            if(mInfo.ActiveInfo.UseItemID!=null)
            {
                if(mInfo.ActiveInfo.UseItemID.Length>0)
                {
                    ActiveInfoUseItemID = Convert.ToInt32(mInfo.ActiveInfo.UseItemID);
                }else
                {
                    ActiveInfoUseItemID = 0;
                }
            }
            else
            {
                ActiveInfoUseItemID = 0;
            }

            if(mInfo.ActiveInfo.OtherType!=null)
            {
                if(mInfo.ActiveInfo.OtherType.Length>0)
                {
                    ActiveInfoOtherType = Convert.ToInt32(mInfo.ActiveInfo.OtherType);
                }else
                {
                    ActiveInfoOtherType = 0;
                }
            }
            else
            {
                ActiveInfoOtherType = 0;
            }
            
            
            if(mInfo.QuestionInfo.CorrectAnswer!=null)
            {
                if(mInfo.QuestionInfo.CorrectAnswer.Length>0)
                {
                    QuestionInfoCorrectAnswer = mInfo.QuestionInfo.CorrectAnswer;
                }else
                {
                    QuestionInfoCorrectAnswer = "";
                }
            }
            else
            {
                QuestionInfoCorrectAnswer = "";
            }
            
            QuestionInfoWrongAnswerList = new string[50];

            for (int i = 0; i < 50; ++i)
                QuestionInfoWrongAnswerList[i] = "";

            if (mInfo.QuestionInfo.WrongAnswerA != null)
                QuestionInfoWrongAnswerList[0]
                     = mInfo.QuestionInfo.WrongAnswerA;

            if (mInfo.QuestionInfo.WrongAnswerB != null)
                QuestionInfoWrongAnswerList[1]
                     = mInfo.QuestionInfo.WrongAnswerB;

            if (mInfo.QuestionInfo.WrongAnswerC != null)
                QuestionInfoWrongAnswerList[2]
                    = mInfo.QuestionInfo.WrongAnswerC;

            if (mInfo.QuestionInfo.WrongAnswerD != null)
                QuestionInfoWrongAnswerList[3]
                     = mInfo.QuestionInfo.WrongAnswerD;

            if (mInfo.QuestionInfo.WrongAnswerE != null)
                QuestionInfoWrongAnswerList[4]
                 = mInfo.QuestionInfo.WrongAnswerE;

            if(mInfo.QuestionInfo.NpcID!=null)
            {
                if(mInfo.QuestionInfo.NpcID.Length>0)
                {
                    QuestionInfoNpcID = Convert.ToInt32(mInfo.QuestionInfo.NpcID);
            
                }else
                {
                    QuestionInfoNpcID = 0;
                }
            }
            else
            {
                QuestionInfoNpcID = 0;
            }

            if(mInfo.QuestionInfo.Conversion!=null)
            {
                if(mInfo.QuestionInfo.Conversion.Length>0)
                {
                    QuestionInfoConversion = mInfo.QuestionInfo.Conversion;
                }else
                {
                    QuestionInfoConversion = "";
                }
            }
            else
            {
                QuestionInfoConversion = "";
            }
      
            if(mInfo.TaskInfo.DescriptionA!=null)
            {
                if(mInfo.TaskInfo.DescriptionA.Length>0)
                {
                    TaskInfoDescriptionListA = mInfo.TaskInfo.DescriptionA;
                }else
                {
                    TaskInfoDescriptionListA = "";
                }
            }
            else
            {
                TaskInfoDescriptionListA = "";
            }
            
            
            if(mInfo.TaskInfo.PurposeA!=null)
            {
                if(mInfo.TaskInfo.PurposeA!=null)
                {
                    TaskInfoPurposeListA = mInfo.TaskInfo.PurposeA;
            
                }else
                {
                     TaskInfoPurposeListA = "";
                }
            }
            else
            {
                TaskInfoPurposeListA = "";
            }
            
            if(mInfo.TaskInfo.TraceA!=null)
            {
                if(mInfo.TaskInfo.TraceA.Length>0)
                {
                    TaskInfoTraceListA = mInfo.TaskInfo.TraceA;
            
                }else
                {
                    TaskInfoTraceListA = "";
                }
            }
            else
            {
                TaskInfoTraceListA = "";
            }

            

            if(mInfo.AIInfo.BattleLevel!=null)
            {
                if(mInfo.AIInfo.BattleLevel.Length>0)
                {
                    AIInfoBattleLevel = mInfo.AIInfo.BattleLevel;
                }else
                {
                    AIInfoBattleLevel = "";
                }
            }
            else
            {
                AIInfoBattleLevel = "";
            }

            if (mInfo.AIInfo.BattleResult == "要求胜利")
                AIInfoBattleResult = 1;
            else if (mInfo.AIInfo.BattleResult == "均可")
                AIInfoBattleResult = 0;
            else
                AIInfoBattleResult = 0;

            if (mInfo.AIInfo.DeathPunish == "0：不接受")
                AIInfoDeathPunish = 0;
            else
                AIInfoBattleResult = 1;

            if (mInfo.ActiveInfo.TeamState == "Yes")
                AIInfoTeamSteate = 1;
            else if (mInfo.ActiveInfo.TeamState == "No")
                AIInfoTeamSteate = 0;
            else if (mInfo.ActiveInfo.TeamState == "Both")
                AIInfoTeamSteate = 2;

            if (mInfo.BattleInfo.BattleMapType == "明雷区")
                BattleInfoBattleMapType = 0;
            else
                BattleInfoBattleMapType = 1;

            if(mInfo.BattleInfo.BattleZoneID!=null)
            {
                if(mInfo.BattleInfo.BattleZoneID.Length>0)
                {
                    BattleInfoBattleZoneID = Convert.ToInt32(mInfo.BattleInfo.BattleZoneID);
                }else
                {
                    BattleInfoBattleZoneID = 0;
                }
            }
            else
            {
                BattleInfoBattleZoneID = 0;
            }

            if(mInfo.BattleInfo.Drop!=null)
            {
                if(mInfo.BattleInfo.Drop.Length>0)
                {
                    BattleInfoDrop = Convert.ToInt32(mInfo.BattleInfo.Drop);
                }else
                {
                    BattleInfoDrop = 0;
                }
            }
            else
            {
                BattleInfoDrop = 0;
            }


            if(mInfo.BattleInfo.BattleTimes!=null)
            {
                if(mInfo.BattleInfo.BattleTimes.Length>0)
                {

                    BattleInfoBattleTimes = Convert.ToInt32(mInfo.BattleInfo.BattleTimes);

                }else
                {
                    BattleInfoBattleTimes = 0;
                }
            }
            else
            {
                BattleInfoBattleTimes = 0;
            }

            BattleInfoMonsterList = new int[50];
            for (int i = 0; i < mInfo.BattleInfo.MonsterList.Count; ++i)
            {
                if(mInfo.BattleInfo.MonsterList[i]!=null)
                {
                    if(mInfo.BattleInfo.MonsterList[i].String.Length > 0)
                    {
                        BattleInfoMonsterList[i] =
                            Convert.ToInt32(mInfo.BattleInfo.MonsterList[i].String);
                    }else
                    {
                        BattleInfoMonsterList[i] = 0;
                    }
                }
                else
                {
                    BattleInfoMonsterList[i] = 0;
                }
            }


            if(mInfo.BattleInfo.MonsterNum!=null)
            {
                if(mInfo.BattleInfo.MonsterNum.Length>0)
                {
                    BattleInfoMonsterNum = Convert.ToInt32(mInfo.BattleInfo.MonsterNum);
                }else
                {
                    BattleInfoMonsterNum = 0;
                }
            }
            else
            {
                BattleInfoMonsterNum = 0;
            }


            if(mInfo.BattleInfo.DropItemID!=null)
            {
                if(mInfo.BattleInfo.DropItemID.Length>0)
                {
                    BattleInfoDropItemID = Convert.ToInt32(mInfo.BattleInfo.DropItemID);
                }else
                {
                    BattleInfoDropItemID = 0;
                }
            }
            else
            {
                BattleInfoDropItemID = 0;
            }

            if(mInfo.BattleInfo.DropItemNum!=null)
            {
                if(mInfo.BattleInfo.DropItemNum.Length>0)
                {
                    BattleInfoDropItemNum = Convert.ToInt32(mInfo.BattleInfo.DropItemNum);
            
                }
            }
            else
            {
                BattleInfoDropItemNum = 0;
            }

            if(mInfo.ScenarioInfo.AnimationID!=null)
            {
                if(mInfo.ScenarioInfo.AnimationID.Length>0)
                {
                    ScenarioInfoAnimationID = Convert.ToInt32(mInfo.ScenarioInfo.AnimationID);
                }
            }
            else
            {
                ScenarioInfoAnimationID = 0;
            }

            if(mInfo.ScenarioInfo.BranchNpcID!=null)
            {
                if(mInfo.ScenarioInfo.BranchNpcID.Length>0)
                {
                    ScenarioInfoBranchNpcID = Convert.ToInt32(mInfo.ScenarioInfo.BranchNpcID);
                }
            }
            else
            {
                ScenarioInfoBranchNpcID = 0;
            }

           
            if(mInfo.ScenarioInfo.BranchNote!=null)
            {
                if(mInfo.ScenarioInfo.BranchNote.Length>0)
                {
                    ScenarioInfoBranchNote = mInfo.ScenarioInfo.BranchNote;
                }else
                {
                    ScenarioInfoBranchNote = "";
                }
            }
            else
            {
                ScenarioInfoBranchNote = "";
            }
           

            ScenarioInfoBranchOptionList = new string[50];
            for (int i = 0; i < 50; ++i)
                ScenarioInfoBranchOptionList[i] = "";

            if (mInfo.ScenarioInfo.BranchOptionA != null)
                ScenarioInfoBranchOptionList[0] =
                    mInfo.ScenarioInfo.BranchOptionA;

            if (mInfo.ScenarioInfo.BranchOptionB != null)
                ScenarioInfoBranchOptionList[1] =
                    mInfo.ScenarioInfo.BranchOptionB;

            if (mInfo.ScenarioInfo.BranchOptionC != null)
                ScenarioInfoBranchOptionList[2] =
                    mInfo.ScenarioInfo.BranchOptionC;


            ScenarioInfoNpcConversationList = new string[50];
            ScenarioInfoNpcID = new int[50];

            for (int i = 0; i < 50; ++i)
            {
                ScenarioInfoNpcConversationList[i] = "";
            }

            for (int i = 0; i < mInfo.ScenarioInfo.ConversationList.Count; ++i)
            {
                if (mInfo.ScenarioInfo.ConversationList[i].NpcID != "")
                {
                    ScenarioInfoNpcID[i] =
                        Convert.ToInt32(mInfo.ScenarioInfo.ConversationList[i].NpcID);
                }

                if (mInfo.ScenarioInfo.ConversationList[i].Conversation != null)
                    ScenarioInfoNpcConversationList[i] =mInfo.ScenarioInfo.ConversationList[i].Conversation;
            }

            ScenarioInfoFinishConversationList = new string[50];

            ScenarioInfoFinishNpcID = new int[50];

            for (int i = 0; i < 50; ++i)
            {
                ScenarioInfoFinishConversationList[i] = "";
            }

            for (int i = 0; i < mInfo.ScenarioInfo.FinishConversationList.Count; ++i)
            {
                if (mInfo.ScenarioInfo.FinishConversationList[i].NpcID != "")
                {
                    ScenarioInfoFinishNpcID[i] =
                        Convert.ToInt32(mInfo.ScenarioInfo.FinishConversationList[i].NpcID);
                }
                if (mInfo.ScenarioInfo.FinishConversationList[i].Conversation != null)
                    ScenarioInfoFinishConversationList[i] =
                        mInfo.ScenarioInfo.FinishConversationList[i].Conversation;
            }
            /* 
            //try
            //{
            //    BattleInfoMonsterType = Convert.ToInt32(mInfo.BattleInfo.MonsterType);
            //}
            //catch
            //{
            //    BattleInfoMonsterType = 0;
            //}
            */
        }
    }//////////////////////////////////////////////////////////////////////////

    public class MXLSWrite
    {
        Excel.Application excelApp;
        Excel.Workbook wb;
        Excel.Worksheet sheet;
        string filename;
        Dictionary<string, ColInfo> cmap;
        //2015 NPOI Method
        XSSFWorkbook HSSFWB;
        ISheet CurSheet;
        FileStream FS;
        Dictionary<string,int> colDic;

        public MXLSWrite(string name, Dictionary<string, ColInfo> map, ref XSSFWorkbook ws)
        {
            


            //2015 NPOI Method
            HSSFWB = new XSSFWorkbook();
            CurSheet = HSSFWB.CreateSheet("0");
            filename = name;
            cmap = map;

            colDic = new Dictionary<string, int>();
        }

        public void WriteTitle()
        {
            IRow CurRow = CurSheet.CreateRow(0);
            int cellCounter = 0;
            foreach (var colInfo in cmap)
            {
                if (!colInfo.Value.isArray)
                {
                    //2015 Yao NPOI Method 
                    colDic.Add(colInfo.Key.ToString(),cellCounter);
                    CurRow.CreateCell(cellCounter).SetCellValue(colInfo.Key.ToString());
                    cellCounter++;
                    //-sheet.Cells[1, colInfo.Value.startcol] = colInfo.Key;
                    
                }
                else
                {
                    for (int i = 0; i < 50; ++i)
                    {
                        string name = colInfo.Key + i.ToString();
                        colDic.Add(name, cellCounter);
                        CurRow.CreateCell(cellCounter).SetCellValue(name);
                        cellCounter++;
                        //-sheet.Cells[1, colInfo.Value.startcol + i] = name;

                    }
                }
                
            }
            int a = 0;
        }

        public void WriteData(MissionInfoAIO mInfo, int row)
        {
            int r = row;
            Type tp = Type.GetType("Knight.MissionEditor.MissionInfoAIO");

            FieldInfo[] mbArray = tp.GetFields();
            IRow tmpCurRow = CurSheet.CreateRow(row);
            int colCounter  = 0;
            
            foreach (FieldInfo mb in mbArray)
            {
                ColInfo colInfo = cmap[mb.Name];
                if (!colInfo.isArray)
                {
                    colDic.TryGetValue(mb.Name, out colCounter);
                    if (mb.GetValue(mInfo) != null)
                    {
                        string st = mb.GetValue(mInfo).ToString();
                        tmpCurRow.CreateCell(colCounter).SetCellValue(st);
                    }else
                    {
                        tmpCurRow.CreateCell(colCounter).SetCellValue("");
                    }
                    //-sheet.Cells[row, colInfo.startcol] = st;
                }
                else
                {
                    if (colInfo.vartype == "Int32[]")
                    {
                        int[] array = mb.GetValue(mInfo) as int[];
                        for (int i = 0; i < 50; ++i)
                        {
                            string tmpColName = mb.Name + i.ToString();
                            colDic.TryGetValue(tmpColName, out colCounter);
                            if (array[i] != 0)
                            {
                                tmpCurRow.CreateCell(colCounter).SetCellValue(array[i].ToString());
                            }else
                            {
                                tmpCurRow.CreateCell(colCounter).SetCellValue("");
                            }
                            //-sheet.Cells[row, colInfo.startcol + i] = array[i].ToString();
                        }
                    }
                    else if (colInfo.vartype == "Int64[]")
                    {
                        long[] array = mb.GetValue(mInfo) as long[];
                        for (int i = 0; i < 50; ++i)
                        {
                            string tmpColName = mb.Name + i.ToString();
                            colDic.TryGetValue(tmpColName, out colCounter);
                            if (array[i] != 0)
                            {
                                tmpCurRow.CreateCell(colCounter).SetCellValue(array[i].ToString());    
                            }
                            else
                            {
                                tmpCurRow.CreateCell(colCounter).SetCellValue("");
                            }
                            //-sheet.Cells[row, colInfo.startcol + i] = array[i].ToString();
                        }
                    }
                    else if (colInfo.vartype == "System.String[]")
                    {
                        string[] array = mb.GetValue(mInfo) as string[];
                        for (int i = 0; i < 50; ++i)
                        {
                            string tmpColName = mb.Name + i.ToString();
                            colDic.TryGetValue(tmpColName, out colCounter);
                            if (array[i] != "")
                            {
                                tmpCurRow.CreateCell(colCounter).SetCellValue(array[i].ToString());
                                
                            }else
                            {
                                tmpCurRow.CreateCell(colCounter).SetCellValue("");
                            }
                            //-sheet.Cells[row, colInfo.startcol + i] = array[i];
                        }    
                    }    
                }
            }
        }

        public void WriteAllData(ref MissionDB mDB)
        {
            WriteTitle();

            int i = 1;
            foreach (MissionInfo missionInfo in mDB.MissionInfoList)
            {

                MissionInfoAIO mAIO = new MissionInfoAIO(missionInfo);

                WriteData(mAIO, i++);

            }

            WriteGbeans();
            
            
        }
        /////////////////////////////////////////////////////////////////////////

        public void WriteGbeans()
        {
            string fname = filename.Substring(filename.LastIndexOf("\\") + 1);
            XmlTextWriter gbeansWriter = new XmlTextWriter("mainmissioninfo.xml", Encoding.UTF8);

            GBeansInfo clientBeans = new GBeansInfo("CMainMissionInfo", fname, "client");
            gbeansWriter.WriteStartDocument(true);
            gbeansWriter.WriteWhitespace("\n");
            gbeansWriter.WriteStartElement("namespace");
            gbeansWriter.WriteAttributeString("name", "task");
            gbeansWriter.WriteWhitespace("\n");

            //client beans
            gbeansWriter.WriteStartElement("bean");
            gbeansWriter.WriteAttributeString("name", clientBeans.name);
            gbeansWriter.WriteAttributeString("from", clientBeans.from);
            gbeansWriter.WriteAttributeString("genxml", clientBeans.genxml);
            gbeansWriter.WriteWhitespace("\n");
            foreach (var colInfo in cmap)
            {
                GBeanRowInfo beanInfo = new GBeanRowInfo(colInfo.Value);
                gbeansWriter.WriteStartElement("variable");
                gbeansWriter.WriteAttributeString("name", beanInfo.name);
                gbeansWriter.WriteAttributeString("fromCol", beanInfo.fromCol);
                gbeansWriter.WriteAttributeString("type", beanInfo.type);
                if (beanInfo.value != null)
                    gbeansWriter.WriteAttributeString("value", beanInfo.value);

                gbeansWriter.WriteEndElement();
                gbeansWriter.WriteWhitespace("\n");
            }
            gbeansWriter.WriteEndElement();
            gbeansWriter.WriteWhitespace("\n");

            GBeansInfo serverBeans = new GBeansInfo("SMainMissionInfo", fname, "server");

            //server beans
            gbeansWriter.WriteStartElement("bean");
            gbeansWriter.WriteAttributeString("name", serverBeans.name);
            gbeansWriter.WriteAttributeString("from", serverBeans.from);
            gbeansWriter.WriteAttributeString("genxml", serverBeans.genxml);
            gbeansWriter.WriteWhitespace("\n");
            foreach (var colInfo in cmap)
            {
                GBeanRowInfo beanInfo = new GBeanRowInfo(colInfo.Value);
                gbeansWriter.WriteStartElement("variable");
                gbeansWriter.WriteAttributeString("name", beanInfo.name);
                gbeansWriter.WriteAttributeString("fromCol", beanInfo.fromCol);
                gbeansWriter.WriteAttributeString("type", beanInfo.type);
                if (beanInfo.value != null)
                    gbeansWriter.WriteAttributeString("value", beanInfo.value);

                gbeansWriter.WriteEndElement();
                gbeansWriter.WriteWhitespace("\n");
            }
            gbeansWriter.WriteEndElement();
            gbeansWriter.WriteWhitespace("\n");


            gbeansWriter.WriteEndElement();

            gbeansWriter.Flush();
            gbeansWriter.Close();

        }


        public void Save()
        {
            //2015 NPOI Method
            object m = Type.Missing;
            ISheet test = HSSFWB.GetSheet("0");
            IRow testRow = test.GetRow(0);
            //sheet.SaveAs(filename);
            FS = new FileStream(filename, FileMode.Create, FileAccess.Write);
            HSSFWB.Write(FS);
            FS.Close();
            //-wb.SaveCopyAs(filename);
            //-wb.Close(false, m, m);
            //-excelApp.Quit();
        }
    }
    //////////////////////////////////////////////////////////////////////////

    public class GBeansInfo
    {
        [XmlAttribute()]
        public string name;

        [XmlAttribute()]
        public string from;

        [XmlAttribute()]
        public string genxml;


        public GBeansInfo(string n,string f,string g)
        {
            name = n;
            from = f;
            genxml = g;
        }

    }

    //////////////////////////////////////////////////////////////////////////

    public class GBeanRowInfo
    {
        [XmlAttribute()]
        public string name;
        [XmlAttribute()]
        public string type;
        [XmlAttribute()]
        public string fromCol;
        [XmlAttribute()]
        public string value;

        public GBeanRowInfo()
        {

        }

        public GBeanRowInfo(ColInfo colInfo)
        {
            if (colInfo.name == "MissionID")
                name = "id";
            else
                name = colInfo.name;

            fromCol = colInfo.name;

            if (colInfo.vartype == "Int32[]")
            {
                type = "vector";
                value = "int";
            }
            else if (colInfo.vartype == "Int64[]")
            {
                type = "vector";
                value = "long";
            }
            else if (colInfo.vartype == "System.String[]")
            {
                type = "vector";
                value = "string";
            }
            else if (colInfo.vartype == "Int32")
            {
                type = "int";
            }
            else if (colInfo.vartype == "Int64")
            {
                type = "long";
            }
            else if (colInfo.vartype == "System.String")
            {
                type = "string";
            }
            else if (colInfo.vartype == "Boolean")
            {
                type = "bool";
            }
            else
            {
                type = "Unknown";
            }

            if (colInfo.isArray)
            {
                fromCol = "";
                string tmpstr;
                for (int i = 0; i < 50; ++i)
                {
                    tmpstr = colInfo.name + i.ToString();
                    fromCol += tmpstr;
                    if (i != 49)
                    {
                        fromCol += ",";
                    }
                }
            }
            else
            {
                fromCol = colInfo.name;
            }
        }
    }

}