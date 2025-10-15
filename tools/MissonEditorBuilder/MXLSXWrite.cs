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
//using Knight.MissionEditor.Xaml;
using Excel = Microsoft.Office.Interop.Excel;


namespace Knight.MissionEditor
{
    public class MissionInfoAIO
    {
        //任务ID
        public int MissionID;

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
            try
            {
                MissionID = Convert.ToInt32(mInfo.MissionID);
            }
            catch
            {
                MissionID = 0;
            }

            try
            {
                MissionName = mInfo.MissionName;
            }
            catch
            {
                MissionName = "";
            }
            if (MissionName == null)
                MissionName = "";

            MissionTypeString = "";
            try
            {
                MissionTypeString = mInfo.MissionTypeString;
            }
            catch
            {
                MissionTypeString = "";
            }
            if (MissionTypeString == null)
                MissionTypeString = "";

            try
            {
                MinLevel = Convert.ToInt32(mInfo.LimitInfo.MinLevel);
            }
            catch
            {
                MinLevel = 0;
            }

            try
            {
                MaxLevel = Convert.ToInt32(mInfo.LimitInfo.MaxLevel);
            }
            catch
            {
                MaxLevel = 0;
            }

            try
            {
                TransMinLevel = Convert.ToInt32(mInfo.LimitInfo.TransMinLevel);
            }
            catch
            {
                TransMinLevel = 0;
            }

            try
            {
                TransMaxLevel = Convert.ToInt32(mInfo.LimitInfo.TransMaxLevel);
            }
            catch
            {
                TransMaxLevel = 0;
            }

            RequestMissionList = new int[50];

            for (int i = 0; i < mInfo.LimitInfo.RequestMissionList.Count; ++i)
            {
                try
                {
                    RequestMissionList[i] =
                        Convert.ToInt32(mInfo.LimitInfo.RequestMissionList[i].MissionID);
                }
                catch
                {
                    RequestMissionList[i] = 0;
                }
            }

            RequestRoleIDList = new long[50];

            for (int i = 0; i < mInfo.LimitInfo.RequestRoleIDList.Count; ++i)
            {
                try
                {
                    RequestRoleIDList[i] =
                      Convert.ToInt64(mInfo.LimitInfo.RequestRoleIDList[i].String);
                }
                catch
                {
                    RequestRoleIDList[i] = 0;
                }
            }

            PostMissionList = new int[50];

            for (int i = 0; i < mInfo.RewardInfo.PostMissionList.Count; ++i)
            {
                try
                {
                    PostMissionList[i] =
                        Convert.ToInt32(mInfo.RewardInfo.PostMissionList[i].MissionID);
                }
                catch
                {
                    PostMissionList[i] = 0;
                }
            }

            DisPlayNPCID = new int[50];

            for (int i = 0; i < mInfo.RewardInfo.DisplayNpcList.Count; ++i)
            {
                try
                {
                    DisPlayNPCID[i] =
                        Convert.ToInt32(mInfo.RewardInfo.DisplayNpcList[i].String);
                }
                catch
                {
                    DisPlayNPCID[i] = 0;
                }
            }
            try
            {
                TransformID = Convert.ToInt32(mInfo.RewardInfo.TransformID);
            }
            catch
            {
                TransformID = 0;
            }

            NoteInfo = "";
            try
            {
                NoteInfo = mInfo.RewardInfo.NoteInfo;
            }
            catch
            {
                NoteInfo = "";
            }
            if (NoteInfo == null)
                NoteInfo = "";



            try
            {
                ExpReward = Convert.ToInt64(mInfo.RewardInfo.ExpReward);
            }
            catch
            {
                ExpReward = 0;
            }

            try
            {
                MoneyReward = Convert.ToInt64(mInfo.RewardInfo.MoneyReward);
            }
            catch
            {
                MoneyReward = 0;
            }

            try
            {
                PetExpReward = Convert.ToInt64(mInfo.RewardInfo.PetExpReward);
            }
            catch
            {
                PetExpReward = 0;
            }

            try
            {
                ShengWang = Convert.ToInt32(mInfo.RewardInfo.RewardPetID);
            }
            catch
            {
                ShengWang = 0;
            }

            try
            {
                SMoney = Convert.ToInt64(mInfo.RewardInfo.SMoney);
            }
            catch
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
            
            try
            {
                RewardMapID = Convert.ToInt32(mInfo.RewardInfo.rewardMapID);
            }
            catch
            {
                RewardMapID = 0;
            }

            try
            {
                RewardMapXPos = Convert.ToInt32(mInfo.RewardInfo.rewardMapXPos);
            }
            catch
            {
                RewardMapXPos = 0;
            }

            try
            {
                RewardMapYPos = Convert.ToInt32(mInfo.RewardInfo.rewardMapYPos);
            }
            catch
            {
                RewardMapYPos = 0;
            }

            try
            {
                ProcessBarTime = Convert.ToInt32(mInfo.RewardInfo.ProcessBarTime);
            }
            catch
            {
                ProcessBarTime = 0;
            }


            try
            {
                ProcessBarText = mInfo.RewardInfo.ProcessBarText;
            }
            catch
            {
                ProcessBarText = "";
            }
            if (ProcessBarText == null)
                ProcessBarText = "";

            try
            {
                ProcessBarColor = mInfo.RewardInfo.ProcessBarColor;
            }
            catch
            {
                ProcessBarColor = "";
            }
            if (ProcessBarColor == null)
                ProcessBarColor = "";

            RewardItemIDList = new int[50];
            RewardItemNumList = new int[50];
            RewardItemShapeIDList = new int[50];
            RewardItemIsBindList = new int[50];
            for (int i = 0; i < mInfo.RewardInfo.RewardItemList.Count; ++i)
            {
                try
                {
                    RewardItemIDList[i] =
                        Convert.ToInt32(mInfo.RewardInfo.RewardItemList[i].ID);
                }
                catch
                {
                    RewardItemIDList[i] = 0;
                }
                try
                {
                    RewardItemNumList[i] =
                        Convert.ToInt32(mInfo.RewardInfo.RewardItemList[i].Num);
                }
                catch
                {
                    RewardItemNumList[i] = 0;
                }

                try
                {
                    RewardItemShapeIDList[i] =
                        Convert.ToInt32(mInfo.RewardInfo.RewardItemList[i].ShapeID);
                }
                catch
                {
                    RewardItemShapeIDList[i] = 0;
                }

                try
                {
                    RewardItemIsBindList[i] =
                        Convert.ToInt32(mInfo.RewardInfo.RewardItemList[i].IsBind);
                }
                catch
                {
                    RewardItemIsBindList[i] = 1;
                }
            }


            {
                string tmpstr = mInfo.ActiveInfo.MissionType.Substring(0,2);
                try
                {
                    MissionType = Convert.ToInt32(tmpstr);
                }
                catch
                {
                    MissionType = 0;
                    MessageBox.Show("任务类型字段有错误，请检查表格");
                }
                

            }

            try
            {
                ActiveInfoNpcID = Convert.ToInt32(mInfo.ActiveInfo.NpcID);
            }
            catch
            {
                ActiveInfoNpcID = 0;
            }

            try
            {
                ActiveInfoMapID = Convert.ToInt32(mInfo.ActiveInfo.MapID);
            }
            catch
            {
                ActiveInfoMapID = 0;
            }

            try
            {
                ActiveInfoLeftPos = Convert.ToInt32(mInfo.ActiveInfo.LeftPos);
            }
            catch
            {
                ActiveInfoLeftPos = 0;
            }

            try
            {
                ActiveInfoTopPos = Convert.ToInt32(mInfo.ActiveInfo.TopPos);
            }
            catch
            {
                ActiveInfoTopPos = 0;
            }

            try
            {
                ActiveInfoRightPos = Convert.ToInt32(mInfo.ActiveInfo.RightPos);
            }
            catch
            {
                ActiveInfoRightPos = 0;
            }

            try
            {
                ActiveInfoBottomPos = Convert.ToInt32(mInfo.ActiveInfo.BottomPos);
            }
            catch
            {
                ActiveInfoRightPos = 0;
            }


            try
            {
                ActiveInfoTargetID = Convert.ToInt32(mInfo.ActiveInfo.TargetID);
            }
            catch
            {
                ActiveInfoTargetID = 0;
            }

            try
            {
                ActiveInfoTargetNum = Convert.ToInt32(mInfo.ActiveInfo.TargetNum);
            }
            catch
            {
                ActiveInfoTargetNum = 0;
            }

            try
            {
                ActiveInfoMiniStep = Convert.ToInt32(mInfo.ActiveInfo.MiniStep);
            }
            catch
            {
                ActiveInfoMiniStep = 0;
            }

            try
            {
                ActiveInfoStepProbability = Convert.ToInt32(mInfo.ActiveInfo.StepProbability);
            }
            catch
            {
                ActiveInfoStepProbability = 0;
            }

            try
            {
                ActiveInfoMaxStep = Convert.ToInt32(mInfo.ActiveInfo.MaxStep);
            }
            catch
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



            try
            {
                ActiveInfoTimeLimit = Convert.ToInt32(mInfo.ActiveInfo.TimeLimit);
            }
            catch
            {
                ActiveInfoTimeLimit = 0;
            }

            if (mInfo.ActiveInfo.IsRestartTimer == "Yes")
                ActiveInfoIsRestartTimer = 1;
            else
                ActiveInfoIsRestartTimer = 0;

            try
            {
                ActiveInfoGiveBackMoney = Convert.ToInt64(mInfo.ActiveInfo.GiveBackMoney);
            }
            catch
            {
                ActiveInfoGiveBackMoney = 0;
            }

            try
            {
                ActiveInfoGiveBackPetID = Convert.ToInt32(mInfo.ActiveInfo.GiveBackPetID);
            }
            catch
            {
                ActiveInfoGiveBackPetID = 0;
            }

            try
            {
                ActiveInfoUseItemID = Convert.ToInt32(mInfo.ActiveInfo.UseItemID);
            }
            catch
            {
                ActiveInfoUseItemID = 0;
            }

            try
            {
                ActiveInfoOtherType = Convert.ToInt32(mInfo.ActiveInfo.OtherType);
            }
            catch
            {
                ActiveInfoOtherType = 0;
            }


            QuestionInfoCorrectAnswer = "";
            try
            {
                QuestionInfoCorrectAnswer = mInfo.QuestionInfo.CorrectAnswer;
            }
            catch
            {
                QuestionInfoCorrectAnswer = "";
            }
            if (QuestionInfoCorrectAnswer == null)
                QuestionInfoCorrectAnswer = "";

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


            
            try
            {
                QuestionInfoNpcID = Convert.ToInt32(mInfo.QuestionInfo.NpcID);
            }
            catch
            {
                QuestionInfoNpcID = 0;
            }

            QuestionInfoConversion = "";
            try
            {
                QuestionInfoConversion = mInfo.QuestionInfo.Conversion;
            }
            catch
            {
                QuestionInfoConversion = "";
            }
            if (QuestionInfoConversion == null)
                QuestionInfoConversion = "";

            TaskInfoDescriptionListA = "";
            try
            {
                TaskInfoDescriptionListA = mInfo.TaskInfo.DescriptionA;
            }
            catch
            {
                TaskInfoDescriptionListA = "";
            }
            if (TaskInfoDescriptionListA == null)
                TaskInfoDescriptionListA = "";


            TaskInfoPurposeListA = "";
            try
            {
                TaskInfoPurposeListA = mInfo.TaskInfo.PurposeA;
            }
            catch
            {
                TaskInfoPurposeListA = "";
            }
            if (TaskInfoPurposeListA == null)
                TaskInfoPurposeListA = "";


            TaskInfoTraceListA = "";
            try
            {
                TaskInfoTraceListA = mInfo.TaskInfo.TraceA;
            }
            catch
            {
                TaskInfoTraceListA = "";
            }
            if (TaskInfoTraceListA == null)
                TaskInfoTraceListA = "";

            try
            {
                AIInfoAIID = Convert.ToInt32(mInfo.AIInfo.AIID);
            }
            catch
            {
                AIInfoAIID = 0;
            }

            AIInfoBattleLevel = "";
            try
            {
                AIInfoBattleLevel = mInfo.AIInfo.BattleLevel;
            }
            catch
            {
                AIInfoBattleLevel = "";
            }
            if (AIInfoBattleLevel == null)
                AIInfoBattleLevel = "";


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


            try
            {
                BattleInfoBattleZoneID = Convert.ToInt32(mInfo.BattleInfo.BattleZoneID);

            }
            catch
            {
                BattleInfoBattleZoneID = 0;
            }

            try
            {
                BattleInfoDrop = Convert.ToInt32(mInfo.BattleInfo.Drop);
            }
            catch
            {
                BattleInfoDrop = 0;
            }

            try
            {
                BattleInfoBattleTimes = Convert.ToInt32(mInfo.BattleInfo.BattleTimes);
            }
            catch
            {
                BattleInfoBattleTimes = 0;
            }

            //try
            //{
            //    BattleInfoMonsterType = Convert.ToInt32(mInfo.BattleInfo.MonsterType);
            //}
            //catch
            //{
            //    BattleInfoMonsterType = 0;
            //}


            BattleInfoMonsterList = new int[50];

            for (int i = 0; i < mInfo.BattleInfo.MonsterList.Count; ++i)
            {
                try
                {
                    BattleInfoMonsterList[i] =
                        Convert.ToInt32(mInfo.BattleInfo.MonsterList[i].String);
                }
                catch
                {
                    BattleInfoMonsterList[i] = 0;
                }
            }

            try
            {
                BattleInfoMonsterNum = Convert.ToInt32(mInfo.BattleInfo.MonsterNum);
            }
            catch
            {
                BattleInfoMonsterNum = 0;
            }

            try
            {
                BattleInfoDropItemID = Convert.ToInt32(mInfo.BattleInfo.DropItemID);
            }
            catch
            {
                BattleInfoDropItemID = 0;
            }

            try
            {
                BattleInfoDropItemNum = Convert.ToInt32(mInfo.BattleInfo.DropItemNum);
            }
            catch
            {
                BattleInfoDropItemNum = 0;
            }

            try
            {
                ScenarioInfoAnimationID = Convert.ToInt32(mInfo.ScenarioInfo.AnimationID);
            }
            catch
            {
                ScenarioInfoAnimationID = 0;
            }

            try
            {
                ScenarioInfoBranchNpcID = Convert.ToInt32(mInfo.ScenarioInfo.BranchNpcID);
            }
            catch
            {
                ScenarioInfoBranchNpcID = 0;
            }

            ScenarioInfoBranchNote = "";
            try
            {
                ScenarioInfoBranchNote = mInfo.ScenarioInfo.BranchNote;
            }
            catch
            {
                ScenarioInfoBranchNote = "";
            }
            if (ScenarioInfoBranchNote == null)
                ScenarioInfoBranchNote = "";

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
                ScenarioInfoNpcID[i] =
                    Convert.ToInt32(mInfo.ScenarioInfo.ConversationList[i].NpcID);

                if (mInfo.ScenarioInfo.ConversationList[i].Conversation != null)
                    ScenarioInfoNpcConversationList[i] =
                        mInfo.ScenarioInfo.ConversationList[i].Conversation;
            }

            ScenarioInfoFinishConversationList = new string[50];

            ScenarioInfoFinishNpcID = new int[50];

            for (int i = 0; i < 50; ++i)
            {
                ScenarioInfoFinishConversationList[i] = "";
            }

            for (int i = 0; i < mInfo.ScenarioInfo.FinishConversationList.Count; ++i)
            {
                ScenarioInfoFinishNpcID[i] =
                    Convert.ToInt32(mInfo.ScenarioInfo.FinishConversationList[i].NpcID);
                
                if (mInfo.ScenarioInfo.FinishConversationList[i].Conversation != null)
                    ScenarioInfoFinishConversationList[i] =
                        mInfo.ScenarioInfo.FinishConversationList[i].Conversation;
            }
        }
    }//////////////////////////////////////////////////////////////////////////

    public class MXLSWrite
    {
        Excel.Application excelApp;
        Excel.Workbook wb;
        Excel.Worksheet sheet;
        string filename;
        Dictionary<string, ColInfo> cmap;

        public MXLSWrite(string name, Dictionary<string, ColInfo> map)
        {
            excelApp = new Excel.Application();
            object m = Type.Missing;
            //wb = excelApp.Workbooks.Open(name,
            //                             m, m, m, m, m, m,
            //                             Excel.XlPlatform.xlWindows,
            //                             m, m, m, m, m, m, m);

            wb = excelApp.Application.Workbooks.Add(m);

            sheet = wb.Worksheets.Add(m, m, m, m) as Excel.Worksheet;
            filename = name;
            cmap = map;
        }

        public void WriteTitle()
        {
            Console.WriteLine("写入表头.......");
            foreach (var colInfo in cmap)
            {
                if (!colInfo.Value.isArray)
                {
                    sheet.Cells[1, colInfo.Value.startcol] = colInfo.Key;
                }
                else
                {
                    for (int i = 0; i < 50; ++i)
                    {
                        string name = colInfo.Key + i.ToString();
                        sheet.Cells[1, colInfo.Value.startcol + i] = name;
                    }
                }

                Console.WriteLine("写入表头字段 : " + colInfo.Key);
            }
        }

        public void WriteData(MissionInfoAIO mInfo, int row)
        {
            int r = row;
            Type tp = Type.GetType("Knight.MissionEditor.MissionInfoAIO");

            FieldInfo[] mbArray = tp.GetFields();

            Console.Write(".");

            foreach (FieldInfo mb in mbArray)
            {
                ColInfo colInfo = cmap[mb.Name];

                if (!colInfo.isArray)
                {
                    string st = mb.GetValue(mInfo).ToString();
                    sheet.Cells[row, colInfo.startcol] = st;
                }
                else
                {
                    if (colInfo.vartype == "Int32[]")
                    {
                        int[] array = mb.GetValue(mInfo) as int[];
                        for (int i = 0; i < 50; ++i)
                        {
                            if (array[i] != 0)
                                sheet.Cells[row, colInfo.startcol + i] = array[i].ToString();
                        }
                    }
                    else if (colInfo.vartype == "Int64[]")
                    {
                        long[] array = mb.GetValue(mInfo) as long[];
                        for (int i = 0; i < 50; ++i)
                        {
                            if (array[i] != 0)
                                sheet.Cells[row, colInfo.startcol + i] = array[i].ToString();
                        }
                    }
                    else if (colInfo.vartype == "System.String[]")
                    {
                        string[] array = mb.GetValue(mInfo) as string[];
                        for (int i = 0; i < 50; ++i)
                        {
                            if (array[i] != "")
                                sheet.Cells[row, colInfo.startcol + i] = array[i];
                        }
                    }
                }
            }
        }

        public void WriteAllData(ref MissionDB mDB)
        {
            Console.WriteLine("写入Exel....");
            WriteTitle();

            int i = 2;
            Console.Write("写入Exel数据....");
            foreach (MissionInfo missionInfo in mDB.MissionInfoList)
            {

                MissionInfoAIO mAIO = new MissionInfoAIO(missionInfo);
                WriteData(mAIO, i++);
            }
            Console.WriteLine("");
            Console.WriteLine("写入Exel数据OK");

            WriteGbeans();
            
            
        }
        /////////////////////////////////////////////////////////////////////////

        public void WriteGbeans()
        {
            Console.Write("写入Gbeans xml");
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
                Console.Write(".");
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
                Console.Write(".");
            }
            gbeansWriter.WriteEndElement();
            gbeansWriter.WriteWhitespace("\n");


            gbeansWriter.WriteEndElement();

            gbeansWriter.Flush();
            gbeansWriter.Close();
            Console.WriteLine("Gbeans OK ");
        }


        public void Save()
        {
            Console.WriteLine("保存Xlsx");
            object m = Type.Missing;
            wb.SaveCopyAs(filename);
            wb.Close(false, m, m);
            excelApp.Quit();
            Console.WriteLine("保存Xlsx OK");

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