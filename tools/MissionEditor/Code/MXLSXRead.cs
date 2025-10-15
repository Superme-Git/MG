using System;
using System.Runtime.InteropServices;
using System.Collections.Generic;
using System.Linq;
using System.IO;
using System.Text;
using System.Windows;
using System.Windows.Data;
using System.Reflection;
using System.ComponentModel;
using System.Collections.ObjectModel;

using NPOI;
using NPOI.XSSF.UserModel;
using NPOI.SS.UserModel;

using Excel = Microsoft.Office.Interop.Excel;

namespace Knight.MissionEditor
{
    public class MXLSXRead
    {
        Excel.Application excelApp;
        Excel.Workbook wb;
        Excel.Worksheet ws;
        Dictionary<string, ColInfo> cmap;
        XSSFWorkbook HSSFWB;

        public MXLSXRead(Dictionary<string, ColInfo> map, ref XSSFWorkbook ws)
        {
            HSSFWB = ws;
            cmap = map;
        }

        public MissionDB SetMissionInfo()
        {
            /*int RowCounter     = ws.UsedRange.Rows.Count;
            RowCounter         = ((Excel.Range)ws.Cells[65536, 1]).get_End(Excel.XlDirection.xlUp).Row;
            int ColCounter     = ws.UsedRange.Columns.Count;
            ColCounter         = ((Excel.Range)ws.Cells[1,1024]).get_End(Excel.XlDirection.xlToLeft).Column;*/

            MissionDB db                            = new MissionDB();
            Type tpMissionInfoAIO                   = Type.GetType("Knight.MissionEditor.MissionInfoAIO");
            FieldInfo[] mMissionAIOArray            = tpMissionInfoAIO.GetFields();
             
            //MissionInfo tmpMissionInfo = new MissionInfo();
            //-Type tpMissionInfo                      = Type.GetType("Knight.MissionEditor.MissionInfo");
            //-PropertyInfo[] tmpMissionInfoProperties = tpMissionInfo.GetProperties();
            //FieldInfo[] mMissionInfoArray = tpMissionInfo.GetFields(); 
       
            //-2015 NPOI Method 
            ISheet CurSheet = HSSFWB.GetSheetAt(0);
            
            int totalRow = CurSheet.LastRowNum;
            IRow CurRow     = CurSheet.GetRow(0);
            int totalCol = CurRow.LastCellNum;

            Dictionary<string,List<string>> ListCol = new Dictionary<string,List<string>>();

            //My Own
            for (int i = 0; i < totalCol; ++i)
            {
                string resultStr = "";
              
                List<string> tmpValueList = new List<string>();
                for (int j = 0; j <= totalRow; ++j)
                {
                    IRow XlsRow = CurSheet.GetRow(j);

                    if (j == 0)
                    {
                        resultStr = XlsRow.Cells[i].ToString();
                    }
                    else
                    {
                        if (XlsRow.GetCell(i)!=null)
                        {
                            string strValue = XlsRow.GetCell(i).ToString();
                            if (strValue.Length > 0)
                            {       
                                tmpValueList.Add(strValue);
                            }
                            else 
                            {
                                tmpValueList.Add(null);
                            }

                        }else
                        {
                            tmpValueList.Add(null);
                        }
                    }
                }

                ListCol.Add(resultStr, tmpValueList);
            }
            //

            for (int i = 0; i < totalRow; ++i)
            {
                MissionInfo tmpMissionInfo = new MissionInfo();
                
                //任务ID
                if (ListCol["MissionID"][i] != null)
                {
                    tmpMissionInfo.MissionID = ListCol["MissionID"][i];
                }
                else
                {
                    tmpMissionInfo.MissionID = null;
                }

                //任务自动
                if (ListCol["AutoDo"][i] != null)
                {
                    tmpMissionInfo.AutoDo = ListCol["AutoDo"][i];
                }
                else
                {
                    tmpMissionInfo.AutoDo = null;
                }


                //AIInfo
                //AI ID 
                if (ListCol["AIInfoAIID"][i] != null)
                {
                    tmpMissionInfo.AIInfo.AIID = ListCol["AIInfoAIID"][i];
                }
                else
                {
                    tmpMissionInfo.AIInfo.AIID = null;
                }
                
                //任务的名称
                if (ListCol["MissionName"][i] != null)
                {
                    tmpMissionInfo.MissionName = ListCol["MissionName"][i];
                }
                else
                {
                    tmpMissionInfo.MissionName = null;
                }

                //任务类型：如主线任务
                if (ListCol["MissionTypeString"][i] != null)
                {
                    tmpMissionInfo.MissionTypeString = ListCol["MissionTypeString"][i];
                }
                else
                {
                    tmpMissionInfo.MissionTypeString = null;
                }
               
                //LimitInfo
                //等级限制
                if (ListCol["MinLevel"][i] != null)
                {
                    tmpMissionInfo.LimitInfo.MinLevel = ListCol["MinLevel"][i];
                }
                else
                {
                    tmpMissionInfo.LimitInfo.MinLevel = null;
                }

                //等级限制
                if (ListCol["MaxLevel"][i] != null)
                {
                    tmpMissionInfo.LimitInfo.MaxLevel = ListCol["MaxLevel"][i];
                }
                else
                {
                    tmpMissionInfo.LimitInfo.MaxLevel = null;
                }

                //转生限制
                if (ListCol["TransMinLevel"][i] != null)
                {
                    tmpMissionInfo.LimitInfo.TransMinLevel = ListCol["TransMinLevel"][i];
                }
                else
                {
                    tmpMissionInfo.LimitInfo.TransMinLevel = null;
                }

                if (ListCol["TransMaxLevel"][i] != null)
                {
                    tmpMissionInfo.LimitInfo.TransMaxLevel = ListCol["TransMaxLevel"][i];
                }
                else
                {
                    tmpMissionInfo.LimitInfo.TransMaxLevel = null;
                }

                //RewardInfo
                //变身造型
                if (ListCol["TransformID"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.TransformID = ListCol["TransformID"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.TransformID = null;
                }

                //透明框提示
                if (ListCol["NoteInfo"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.NoteInfo = ListCol["NoteInfo"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.NoteInfo = null;
                }
                
                //经验奖励
                if (ListCol["ExpReward"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.ExpReward = ListCol["ExpReward"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.ExpReward = null;
                }
                    
                //金钱奖励
                if (ListCol["MoneyReward"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.MoneyReward = ListCol["MoneyReward"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.MoneyReward = null;
                }

                //宠物经验奖励
                if (ListCol["PetExpReward"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.PetExpReward = ListCol["PetExpReward"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.PetExpReward = null;
                }

                //金钱奖励
                if (ListCol["MoneyReward"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.MoneyReward = ListCol["MoneyReward"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.MoneyReward = null;
                }

                //宠物经验奖励
                if (ListCol["ShengWang"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.RewardPetID = ListCol["ShengWang"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.RewardPetID = null;
                }

                //储备金奖励
                if (ListCol["SMoney"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.SMoney = ListCol["SMoney"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.SMoney = null;
                }
                    
                //跳转方式
                if (ListCol["RewardMapJumpType"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.JumpType = ListCol["RewardMapJumpType"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.JumpType = null;
                }

                //跳转地图ID
                if (ListCol["RewardMapID"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.rewardMapID = ListCol["RewardMapID"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.rewardMapID = null;
                }
                 
                //跳转坐标X
                if (ListCol["RewardMapXPos"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.rewardMapXPos = ListCol["RewardMapXPos"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.rewardMapXPos = null;
                }
                   
                //跳转坐标Y
                if (ListCol["RewardMapYPos"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.rewardMapYPos = ListCol["RewardMapYPos"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.rewardMapYPos = null;
                } 
                
                //进度条时间
                if (ListCol["ProcessBarTime"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.ProcessBarTime = ListCol["ProcessBarTime"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.ProcessBarTime = null;
                } 
                   
                //进度条文字
                if (ListCol["ProcessBarText"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.ProcessBarText = ListCol["ProcessBarText"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.ProcessBarText = null;
                } 

                //进度条颜色
                if (ListCol["ProcessBarColor"][i] != null)
                {
                    tmpMissionInfo.RewardInfo.ProcessBarColor = ListCol["ProcessBarColor"][i];
                }
                else
                {
                    tmpMissionInfo.RewardInfo.ProcessBarColor = null;
                } 
                
                //ActiveInfo
                //任务类型
                if (ListCol["MissionType"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.MissionType = ListCol["MissionType"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.MissionType = null;
                } 
                
                //Npc ID
                if (ListCol["ActiveInfoNpcID"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.NpcID = ListCol["ActiveInfoNpcID"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.NpcID = null;
                } 

                //Map ID
                if (ListCol["ActiveInfoMapID"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.MapID = ListCol["ActiveInfoMapID"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.MapID = null;
                }

                //地图场景范围
                if (ListCol["ActiveInfoLeftPos"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.LeftPos = ListCol["ActiveInfoLeftPos"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.LeftPos = null;
                }
              
                if (ListCol["ActiveInfoRightPos"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.RightPos = ListCol["ActiveInfoRightPos"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.RightPos = null;
                }
                   
                if (ListCol["ActiveInfoTopPos"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.TopPos = ListCol["ActiveInfoTopPos"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.TopPos = null;
                }
                
                if (ListCol["ActiveInfoBottomPos"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.BottomPos = ListCol["ActiveInfoBottomPos"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.BottomPos = null;
                }

                //对象ID
                if (ListCol["ActiveInfoTargetID"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.TargetID = ListCol["ActiveInfoTargetID"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.TargetID = null;
                }

                //对象Num;
                if (ListCol["ActiveInfoTargetNum"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.TargetNum = ListCol["ActiveInfoTargetNum"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.TargetNum = null;
                }

                //最少结束步骤
                if (ListCol["ActiveInfoMiniStep"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.MiniStep = ListCol["ActiveInfoMiniStep"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.MiniStep = null;
                }  

                //单步结束概率
                if (ListCol["ActiveInfoStepProbability"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.StepProbability = ListCol["ActiveInfoStepProbability"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.StepProbability = null;
                }  
                
                //最大结束步骤
                if (ListCol["ActiveInfoMaxStep"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.MaxStep = ListCol["ActiveInfoMaxStep"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.MaxStep = null;
                }  

                //是否组队完成
                // 0 不可以， 1 必须， 2 都可以
                if (ListCol["ActiveInfoTeamState"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.TeamState = ListCol["ActiveInfoTeamState"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.TeamState = null;
                }   

                //时间限制分钟 
                if (ListCol["ActiveInfoTimeLimit"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.TimeLimit = ListCol["ActiveInfoTimeLimit"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.TimeLimit = null;
                }   

                //是否从新计时
                if (ListCol["ActiveInfoIsRestartTimer"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.IsRestartTimer = ListCol["ActiveInfoIsRestartTimer"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.IsRestartTimer = null;
                }   

                //回收金钱数量
                if (ListCol["ActiveInfoGiveBackMoney"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.GiveBackMoney = ListCol["ActiveInfoGiveBackMoney"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.GiveBackMoney = null;
                }   
                
                //回收Pet ID
                if (ListCol["ActiveInfoGiveBackPetID"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.GiveBackPetID = ListCol["ActiveInfoGiveBackPetID"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.GiveBackPetID = null;
                }    

                //使用物品ID
                if (ListCol["ActiveInfoUseItemID"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.GiveBackPetID = ListCol["ActiveInfoUseItemID"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.GiveBackPetID = null;
                }   
                
                //其他触发类型
                if (ListCol["ActiveInfoOtherType"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.OtherType = ListCol["ActiveInfoOtherType"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.OtherType = null;
                }    

                //QuestionInfo
                //正确答案  
                if (ListCol["QuestionInfoCorrectAnswer"][i] != null)
                {
                    tmpMissionInfo.QuestionInfo.CorrectAnswer = ListCol["QuestionInfoCorrectAnswer"][i];
                }
                else
                {
                    tmpMissionInfo.QuestionInfo.CorrectAnswer = null;
                }  
                
                if (ListCol["QuestionInfoNpcID"][i] != null)
                {
                    tmpMissionInfo.QuestionInfo.NpcID = ListCol["QuestionInfoNpcID"][i];
                }
                else
                {
                    tmpMissionInfo.QuestionInfo.NpcID = null;
                }  
                   
                if (ListCol["QuestionInfoConversion"][i] != null)
                {
                    tmpMissionInfo.QuestionInfo.Conversion = ListCol["QuestionInfoConversion"][i];
                }
                else
                {
                    tmpMissionInfo.QuestionInfo.Conversion = null;
                } 
                
                //描述 max num = 1
                if (ListCol["TaskInfoDescriptionListA"][i] != null)
                {
                    tmpMissionInfo.TaskInfo.DescriptionA = ListCol["TaskInfoDescriptionListA"][i];
                }
                else
                {
                    tmpMissionInfo.TaskInfo.DescriptionA = null;
                }
                    
                //目的 max num = 2
                if (ListCol["TaskInfoPurposeListA"][i] != null)
                {
                    tmpMissionInfo.TaskInfo.PurposeA = ListCol["TaskInfoPurposeListA"][i];
                }
                else
                {
                    tmpMissionInfo.TaskInfo.PurposeA = null;
                }  
                    
                //追踪二
                if (ListCol["TaskInfoTraceListA"][i] != null)
                {
                    tmpMissionInfo.TaskInfo.TraceA = ListCol["TaskInfoTraceListA"][i];
                }
                else
                {
                    tmpMissionInfo.TaskInfo.TraceA = null;
                }
                    
                
  
                //战斗结果
                // 0： 必须失败 , 1: 必须胜利 , 2: 都可以
                if (ListCol["AIInfoBattleResult"][i] != null)
                {
                    tmpMissionInfo.AIInfo.BattleResult = ListCol["AIInfoBattleResult"][i];
                }
                else
                {
                    tmpMissionInfo.AIInfo.BattleResult = null;
                }

                //战斗死亡惩罚
                if (ListCol["AIInfoDeathPunish"][i] != null)
                {
                    tmpMissionInfo.AIInfo.DeathPunish = ListCol["AIInfoDeathPunish"][i];
                }
                else
                {
                    tmpMissionInfo.AIInfo.DeathPunish = null;
                }
                   
                //组队
                // 0 不可以， 1 必须， 2 都可以
                if (ListCol["AIInfoTeamSteate"][i] != null)
                {
                    tmpMissionInfo.ActiveInfo.TeamState = ListCol["AIInfoTeamSteate"][i];
                }
                else
                {
                    tmpMissionInfo.ActiveInfo.TeamState = null;
                }  

                //战斗难度等级
                if (ListCol["AIInfoBattleLevel"][i] != null)
                {
                    tmpMissionInfo.AIInfo.BattleLevel = ListCol["AIInfoBattleLevel"][i];
                }
                else
                {
                    tmpMissionInfo.AIInfo.BattleLevel = null;
                }
                
                //BattleInfo
                //战斗地图类型
                //0： 明雷区  1:暗雷区  
                if (ListCol["BattleInfoBattleMapType"][i] != null)
                {
                    tmpMissionInfo.BattleInfo.BattleMapType = ListCol["BattleInfoBattleMapType"][i];
                }
                else
                {
                    tmpMissionInfo.BattleInfo.BattleMapType = null;
                }
                    
                //战斗地图区域
                if (ListCol["BattleInfoBattleZoneID"][i] != null)
                {
                    tmpMissionInfo.BattleInfo.BattleZoneID = ListCol["BattleInfoBattleZoneID"][i];
                }
                else
                {
                    tmpMissionInfo.BattleInfo.BattleZoneID = null;
                } 
                   
                //掉落物品几率
                if (ListCol["BattleInfoDrop"][i] != null)
                {
                    tmpMissionInfo.BattleInfo.Drop = ListCol["BattleInfoDrop"][i];
                }
                else
                {
                    tmpMissionInfo.BattleInfo.Drop = null;
                } 
                
                //战斗次数
                if (ListCol["BattleInfoBattleTimes"][i] != null)
                {
                    tmpMissionInfo.BattleInfo.BattleTimes = ListCol["BattleInfoBattleTimes"][i];
                }
                else
                {
                    tmpMissionInfo.BattleInfo.BattleTimes = null;
                }
                    
                //怪物数量   
                if (ListCol["BattleInfoMonsterNum"][i] != null)
                {
                    tmpMissionInfo.BattleInfo.MonsterNum = ListCol["BattleInfoMonsterNum"][i];
                }
                else
                {
                    tmpMissionInfo.BattleInfo.MonsterNum = null;
                }    
                   
                //掉落物品ID
                if (ListCol["BattleInfoDropItemID"][i] != null)
                {
                    tmpMissionInfo.BattleInfo.DropItemID = ListCol["BattleInfoDropItemID"][i];
                }
                else
                {
                    tmpMissionInfo.BattleInfo.DropItemID = null;
                }   
                   
                //掉落物品数量
                if (ListCol["BattleInfoDropItemNum"][i] != null)
                {
                    tmpMissionInfo.BattleInfo.DropItemNum = ListCol["BattleInfoDropItemNum"][i];
                }
                else
                {
                    tmpMissionInfo.BattleInfo.DropItemNum = null;
                }   
                
                //ScenarioInfo
                //剧情动画ID
                if (ListCol["ScenarioInfoAnimationID"][i] != null)
                {
                    tmpMissionInfo.ScenarioInfo.AnimationID = ListCol["ScenarioInfoAnimationID"][i];
                }
                else
                {
                    tmpMissionInfo.ScenarioInfo.AnimationID = null;
                }   
                
                //分支剧情
                if (ListCol["ScenarioInfoBranchNpcID"][i] != null)
                {
                    tmpMissionInfo.ScenarioInfo.BranchNpcID = ListCol["ScenarioInfoBranchNpcID"][i];
                }
                else
                {
                    tmpMissionInfo.ScenarioInfo.BranchNpcID = null;
                } 
                    
                //分支对话提示   
                if (ListCol["ScenarioInfoBranchNote"][i] != null)
                {
                    tmpMissionInfo.ScenarioInfo.BranchNote = ListCol["ScenarioInfoBranchNote"][i];
                }
                else
                {
                    tmpMissionInfo.ScenarioInfo.BranchNote = null;
                } 
                
                //需要完成任务
                if (ListCol["ScenarioInfoBranchNote"][i] != null)
                {
                    tmpMissionInfo.ScenarioInfo.BranchNote = ListCol["ScenarioInfoBranchNote"][i];
                }
                else
                {
                    tmpMissionInfo.ScenarioInfo.BranchNote = null;
                }   

                string strRequestMissionName = "RequestMissionList";
                ObservableCollection<MissionIDOC> RequestMissionList = new ObservableCollection<MissionIDOC>();
                for(int j = 0; j < 50; ++j)
                {
                    strRequestMissionName += j.ToString();
                    if(ListCol[strRequestMissionName][i]!=null)
                    {
                        string MissionRequestMissionListCell    = ListCol[strRequestMissionName][i];
                        if (MissionRequestMissionListCell.Length > 0)
                        {
                            MissionIDOC tmpID = new MissionIDOC();
                            tmpID.MissionID = MissionRequestMissionListCell;
                            RequestMissionList.Add(tmpID);    
                        }else
                        {
                            MissionIDOC tmpID = new MissionIDOC();
                            RequestMissionList.Add(null);  
                        }

                    }else
                    {
                       
                        RequestMissionList.Add(null);  
                    }
                    strRequestMissionName = "RequestMissionList";
                }
                tmpMissionInfo.LimitInfo.RequestMissionList = RequestMissionList;

                //需要角色
                string strRequestRoleIDList = "RequestRoleIDList";
                AutoStringList RequestRoleIDList = new AutoStringList();
                for (int j = 0; j < 50; ++j)
                {
                    strRequestRoleIDList += j.ToString();
                   
                    if(ListCol[strRequestRoleIDList][i]!=null)
                    { 
                        string MissionRequestRoleIDListCell = ListCol[strRequestRoleIDList][i];
                        if (MissionRequestRoleIDListCell.Length > 0)
                        {
                            AutoString tmpStr = new AutoString();
                            tmpStr.String = MissionRequestRoleIDListCell;
                            RequestRoleIDList.Add(tmpStr);
                        }else
                        {
                            RequestRoleIDList.Add(null);
                        }

                    }else
                    {
                         AutoString tmpStr = new AutoString();
                         RequestRoleIDList.Add(null);
                    }
                    strRequestRoleIDList = "RequestRoleIDList";
                }
                tmpMissionInfo.LimitInfo.RequestRoleIDList = RequestRoleIDList;

                //后置任务ID
                string strPostMissionList = "PostMissionList";
                ObservableCollection<MissionIDOC> PostMissionList = new ObservableCollection<MissionIDOC>();
                for (int j = 0; j < 50; ++j)
                {
                    strPostMissionList += j.ToString();
                   
                    if(ListCol[strPostMissionList][i]!=null)
                    {
                        string MissionPostMissionListCell = ListCol[strPostMissionList][i];
                        if (MissionPostMissionListCell.Length > 0)
                        {
                            MissionIDOC tmpID = new MissionIDOC();
                            tmpID.MissionID = MissionPostMissionListCell;
                            PostMissionList.Add(tmpID);

                        }else
                        {
                            MissionIDOC tmpID = new MissionIDOC();
                            PostMissionList.Add(null);
                        }
                    }else
                    {
                        PostMissionList.Add(null);
                    }
                    strPostMissionList = "PostMissionList";
                }
                tmpMissionInfo.RewardInfo.PostMissionList = PostMissionList;

                //显示NpcID
                string strDisPlayNPCID = "DisPlayNPCID";
                AutoStringList DisplayNpcList = new AutoStringList();
                for (int j = 0; j < 50; ++j)
                {
                    strDisPlayNPCID += j.ToString();
                    if(ListCol[strDisPlayNPCID][i]!=null)
                    {
                         string MissionDisPlayNPCIDCell = ListCol[strDisPlayNPCID][i];
                         if (MissionDisPlayNPCIDCell.Length > 0)
                         {
                            AutoString tmpStr = new AutoString();
                            tmpStr.String = MissionDisPlayNPCIDCell;
                            DisplayNpcList.Add(tmpStr);
                         }else
                         {
                            AutoString tmpStr = new AutoString();
                            DisplayNpcList.Add(null);
                         }
                     
                    }else
                    {
                        AutoString tmpStr = new AutoString();
                        DisplayNpcList.Add(null);
                    }
                    strDisPlayNPCID = "DisPlayNPCID";
                }
                tmpMissionInfo.RewardInfo.DisplayNpcList = DisplayNpcList;

                //奖励任务列表
                string strRewardItemIDList      = "RewardItemIDList";
                string strRewardItemNumList     = "RewardItemNumList";
                string strRewardItemShapeIDList = "RewardItemShapeIDList";
                string strRewardItemIsBindList  = "RewardItemIsBindList";
                
                ObservableCollection<MissionRewardInfo.RewardItem> RewardItemList = new ObservableCollection<MissionRewardInfo.RewardItem>();
                    
                for (int j = 0; j < 50; ++j)
                {
                    strRewardItemIDList     += j.ToString();
                    strRewardItemNumList    += j.ToString();
                    strRewardItemShapeIDList+= j.ToString();
                    strRewardItemIsBindList += j.ToString();

                    MissionRewardInfo.RewardItem tmpID = new MissionRewardInfo.RewardItem();

                    if(ListCol[strRewardItemIDList][i]!=null)
                    {
                        string MissionRewardItemIDListCell = ListCol[strRewardItemIDList][i];
                        if (MissionRewardItemIDListCell.Length > 0)
                        {
                            tmpID.ID = MissionRewardItemIDListCell;
                        }else
                        {
                            tmpID.ID = null;
                        }

                    }else
                    {
                        tmpID.ID = null;
                    }

                    if(ListCol[strRewardItemNumList][i]!=null)
                    {
                        string MissionRewardItemNumListCell     = ListCol[strRewardItemNumList][i];
                        if (MissionRewardItemNumListCell.Length > 0)
                        {
                            tmpID.Num = MissionRewardItemNumListCell;
                        }else
                        {
                            tmpID.Num = null;
                        }
                    }else
                    {
                        tmpID.Num = null;
                    }

                    if(ListCol[strRewardItemIsBindList][i] != null)
                    {
                        string MissionRewardItemIsBindListCell  = ListCol[strRewardItemIsBindList][i];
                        if (MissionRewardItemIsBindListCell.Length > 0)
                        {

                            tmpID.IsBind = MissionRewardItemIsBindListCell;
                        }else
                        {
                            tmpID.IsBind = null;
                        }
                    }else
                    {
                        tmpID.IsBind = null;
                    }

                    if(ListCol[strRewardItemShapeIDList][i] != null)
                    {
                        string MissionRewardItemShapeIDListCell = ListCol[strRewardItemShapeIDList][i];
                        if (MissionRewardItemShapeIDListCell.Length > 0)
                        {
                            tmpID.ShapeID = MissionRewardItemShapeIDListCell;
                        }else
                        {
                            tmpID.ShapeID = null;
                        }

                    }else
                    {
                        tmpID.ShapeID = null;
                    }

                    RewardItemList.Add(tmpID);
                    strRewardItemIDList     = "RewardItemIDList";
                    strRewardItemNumList    = "RewardItemNumList";
                    strRewardItemShapeIDList= "RewardItemShapeIDList";
                    strRewardItemIsBindList = "RewardItemIsBindList";
                }
                tmpMissionInfo.RewardInfo.RewardItemList = RewardItemList;

                //错误答案 max num = 5;
                string strQuestionInfoWrongAnswerList0 = "QuestionInfoWrongAnswerList0";
                       
                if(ListCol[strQuestionInfoWrongAnswerList0][i]!=null)
                {
                    string MissionQuestionInfoWrongAnswerList0Cell = ListCol[strQuestionInfoWrongAnswerList0][i];
                    if (MissionQuestionInfoWrongAnswerList0Cell.Length > 0)
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerA = MissionQuestionInfoWrongAnswerList0Cell;
                    }else
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerA = null;
                    }

                }else
                {
                    tmpMissionInfo.QuestionInfo.WrongAnswerA = null;
                }

                string strQuestionInfoWrongAnswerList1 = "QuestionInfoWrongAnswerList1";
                if(ListCol[strQuestionInfoWrongAnswerList1][i]!=null)
                {
                    string MissionQuestionInfoWrongAnswerList1Cell = ListCol[strQuestionInfoWrongAnswerList1][i];
                    if (MissionQuestionInfoWrongAnswerList1Cell.Length > 0)
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerB = MissionQuestionInfoWrongAnswerList1Cell;
                    }else
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerB = null;
                    }

                }else
                {
                    tmpMissionInfo.QuestionInfo.WrongAnswerB = null;
                }

                string strQuestionInfoWrongAnswerList2 = "QuestionInfoWrongAnswerList2";
                if(ListCol[strQuestionInfoWrongAnswerList2][i]!=null)
                {
                    string MissionQuestionInfoWrongAnswerList2Cell = ListCol[strQuestionInfoWrongAnswerList2][i];
                    if (MissionQuestionInfoWrongAnswerList2Cell.Length > 0)
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerC = MissionQuestionInfoWrongAnswerList2Cell;
                    }else
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerC = null;
                    }

                }else
                {
                    tmpMissionInfo.QuestionInfo.WrongAnswerB = null;
                }

                string strQuestionInfoWrongAnswerList3 = "QuestionInfoWrongAnswerList3";
                if(ListCol[strQuestionInfoWrongAnswerList3][i]!=null)
                {
                    string MissionQuestionInfoWrongAnswerList3Cell = ListCol[strQuestionInfoWrongAnswerList3][i];
                    if (MissionQuestionInfoWrongAnswerList3Cell.Length > 0)
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerD = MissionQuestionInfoWrongAnswerList3Cell;
                    }else
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerD = null;
                    }

                }else
                {
                    tmpMissionInfo.QuestionInfo.WrongAnswerD = null;
                }

                string strQuestionInfoWrongAnswerList4 = "QuestionInfoWrongAnswerList4";
                if(ListCol[strQuestionInfoWrongAnswerList4][i]!=null)
                {
                    string MissionQuestionInfoWrongAnswerList4Cell = ListCol[strQuestionInfoWrongAnswerList4][i];
                    if (MissionQuestionInfoWrongAnswerList4Cell.Length > 0)
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerE = MissionQuestionInfoWrongAnswerList4Cell;
                    }else
                    {
                        tmpMissionInfo.QuestionInfo.WrongAnswerE = null;
                    }

                }else
                {
                    tmpMissionInfo.QuestionInfo.WrongAnswerE = null;
                }

                //怪物种类
                string strBattleInfoMonsterList = "BattleInfoMonsterList";
                AutoStringList BattleInfoMonsterList = new AutoStringList();
                for (int j = 0; j < 50; ++j)
                {
                    strBattleInfoMonsterList += j.ToString();
                    if(ListCol[strBattleInfoMonsterList][i]!=null)
                    {
                        string MissionBattleInfoMonsterListCell = ListCol[strBattleInfoMonsterList][i];
                        if (MissionBattleInfoMonsterListCell.Length > 0)
                        {
                            AutoString tmpStr = new AutoString();
                            tmpStr.String = MissionBattleInfoMonsterListCell;
                            BattleInfoMonsterList.Add(tmpStr);
                        }else
                        {
                             AutoString tmpStr = new AutoString();
                             BattleInfoMonsterList.Add(null);
                        }

                    }else
                    {
                         AutoString tmpStr = new AutoString();
                         BattleInfoMonsterList.Add(null);
                    }
                    strBattleInfoMonsterList = "BattleInfoMonsterList";
                }
                tmpMissionInfo.BattleInfo.MonsterList = BattleInfoMonsterList;
                    
                //分支选项 
                string strScenarioInfoBranchOptionList0 = "ScenarioInfoBranchOptionList0";
                if(ListCol[strScenarioInfoBranchOptionList0][i]!=null)
                {
                 string MissionScenarioInfoBranchOptionList0Cell = ListCol[strScenarioInfoBranchOptionList0][i];
                 if (MissionScenarioInfoBranchOptionList0Cell.Length > 0)
                 {
                    tmpMissionInfo.ScenarioInfo.BranchOptionA = MissionScenarioInfoBranchOptionList0Cell;
                 }else
                 {
                    tmpMissionInfo.ScenarioInfo.BranchOptionA = null;
                 }
                }else
                {
                    tmpMissionInfo.ScenarioInfo.BranchOptionA = null;
                }

                string strScenarioInfoBranchOptionList1 = "ScenarioInfoBranchOptionList1";
                if(ListCol[strScenarioInfoBranchOptionList1][i]!=null)
                {
                 string MissionScenarioInfoBranchOptionList1Cell = ListCol[strScenarioInfoBranchOptionList1][i];
                 if (MissionScenarioInfoBranchOptionList1Cell.Length > 0)
                 {
                    tmpMissionInfo.ScenarioInfo.BranchOptionB = MissionScenarioInfoBranchOptionList1Cell;
                 }else
                 {
                    tmpMissionInfo.ScenarioInfo.BranchOptionB = null;
                 }
                }else
                {
                    tmpMissionInfo.ScenarioInfo.BranchOptionB = null;
                }

                string strScenarioInfoBranchOptionList2 = "ScenarioInfoBranchOptionList2";
                if(ListCol[strScenarioInfoBranchOptionList2][i]!=null)
                {
                 string MissionScenarioInfoBranchOptionList2Cell = ListCol[strScenarioInfoBranchOptionList2][i];
                 if (MissionScenarioInfoBranchOptionList2Cell.Length > 0)
                 {
                    tmpMissionInfo.ScenarioInfo.BranchOptionC = MissionScenarioInfoBranchOptionList2Cell;
                 }else
                 {
                    tmpMissionInfo.ScenarioInfo.BranchOptionC = null;
                 }
                }else
                {
                    tmpMissionInfo.ScenarioInfo.BranchOptionC = null;
                }

                //对话内容
                string strScenarioInfoNpcConversationList = "ScenarioInfoNpcConversationList";
                string strScenarioInfoNpcID = "ScenarioInfoNpcID";
                ObservableCollection<MissionScenarioInfo.NpcConversation> ScenarioInfoNpcConversationList = new ObservableCollection<MissionScenarioInfo.NpcConversation>();
                for (int j = 0; j < 50; ++j)
                {
                    strScenarioInfoNpcID += j.ToString();
                    strScenarioInfoNpcConversationList += j.ToString();

                    MissionScenarioInfo.NpcConversation tmpCon = new MissionScenarioInfo.NpcConversation();
                    if(ListCol[strScenarioInfoNpcID][i]!=null)
                    {
                        string MissionScenarioInfoNpcIDCell = ListCol[strScenarioInfoNpcID][i];
                        if (MissionScenarioInfoNpcIDCell.Length > 0)
                        {
                            tmpCon.NpcID = MissionScenarioInfoNpcIDCell;
                        }else
                        {
                            tmpCon.NpcID = null;
                        }
                    }else
                    {
                        tmpCon.NpcID = null;
                    }
                    
                    if(ListCol[strScenarioInfoNpcConversationList][i]!=null)
                    {
                     string MissionScenarioInfoNpcConversationListCell = ListCol[strScenarioInfoNpcConversationList][i];
                     if (MissionScenarioInfoNpcConversationListCell.Length > 0)
                     {
                        tmpCon.Conversation = MissionScenarioInfoNpcConversationListCell;
                     }else
                     {
                         tmpCon.Conversation = null;
                     }
                    }

                    ScenarioInfoNpcConversationList.Add(tmpCon);

                    strScenarioInfoNpcID = "ScenarioInfoNpcID";
                    strScenarioInfoNpcConversationList = "ScenarioInfoNpcConversationList";
                }
                tmpMissionInfo.ScenarioInfo.ConversationList = ScenarioInfoNpcConversationList;

                string strScenarioInfoFinishNpcID = "ScenarioInfoFinishNpcID";
                string strScenarioInfoFinishConversationList = "ScenarioInfoFinishConversationList";
                ObservableCollection<MissionScenarioInfo.NpcConversation> FinishConversationList = new ObservableCollection<MissionScenarioInfo.NpcConversation>();
                for (int j = 0; j < 50; ++j)
                {
                    strScenarioInfoFinishNpcID += j.ToString();
                    strScenarioInfoFinishConversationList += j.ToString();
    
                    MissionScenarioInfo.NpcConversation tmpConv = new MissionScenarioInfo.NpcConversation();

                    if (ListCol[strScenarioInfoFinishNpcID][i] != null)
                    {
                        string MissionScenarioInfoFinishNpcIDCell = ListCol[strScenarioInfoFinishNpcID][i];
                        if (MissionScenarioInfoFinishNpcIDCell.Length > 0)
                        {
                            tmpConv.NpcID = MissionScenarioInfoFinishNpcIDCell;
                        }else
                        {
                            tmpConv.NpcID = null;
                        }

                    }else
                    {
                        tmpConv.NpcID = null;
                    }

                    if(ListCol[strScenarioInfoFinishConversationList][i]!=null)
                    {
                        string MissionScenarioInfoFinishConversationListCell = ListCol[strScenarioInfoFinishConversationList][i];
                        if (MissionScenarioInfoFinishConversationListCell.Length > 0)
                        {
                            tmpConv.Conversation = MissionScenarioInfoFinishConversationListCell;
                        }else
                        {
                            tmpConv.Conversation = null;
                        }
                    }else
                    {
                        tmpConv.Conversation = null;
                    }

                    FinishConversationList.Add(tmpConv);
                    strScenarioInfoFinishNpcID            = "ScenarioInfoFinishNpcID";
                    strScenarioInfoFinishConversationList = "ScenarioInfoFinishConversationList";
                }

                tmpMissionInfo.ScenarioInfo.FinishConversationList = FinishConversationList;
                db.MissionInfoList.Insert(i,tmpMissionInfo);
  
            }

            return db;
        }
        public void ReadExcel(string name,ref MissionDB db)
        {

            if (File.Exists(name))
            {
                //2015 NPOI Method
                

                //-Sample
                /*
                ISheet CurSheet  = HSSFWB.GetSheetAt(0);
                IRow CurRow      = CurSheet.GetRow(0);

                int totalRow = CurSheet.LastRowNum;
                int totalCol = CurSheet.GetRow(0).FirstCellNum;

                ICell CurCell    = CurRow.Cells[0];
                string resultStr = CurCell.ToString();
                */
                
                db = SetMissionInfo();
                //-

                //2015 old
                /*excelApp  = new Excel.Application();
                wb          = excelApp.Application.Workbooks.Open(name);
                ws          = (Excel.Worksheet)wb.Sheets.Item[1];
                db          = SetMissionInfo();*/
                //2015
            }
            else
            {
                MessageBox.Show("Can't Find file ");
            }
        }
    }
}
