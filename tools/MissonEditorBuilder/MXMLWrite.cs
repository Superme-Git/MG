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
using MissonEditorBuilder;


namespace Knight.MissionEditor
{
    /************************************************************************/
    /*          Mission DB 保存一条任务链上的任务,使用对象序列化到XML       */
   /************************************************************************/

    public class MissionDB 
    {
        [XmlIgnore()] //不保存文件名到XML
        public string filename;

        [XmlArray("MissionInfoList")]
        public ObservableCollection<MissionInfo> MissionInfoList;

        public MissionDB()
        {
            filename = "100XXX.xml";
            MissionInfoList = new ObservableCollection<MissionInfo>();
        }

        public string GetMissionNameByID(string missionID)
        {
            foreach (MissionInfo mInfo in MissionInfoList)
            {
                if (mInfo.MissionID == missionID)
                    return mInfo.MissionName;
            }
            return "这个任务不存在";
        }
    }

    /************************************************************************/
    /*          Mission 的信息  ,使用对象序列化到XML       */
    /************************************************************************/
    
    public class MissionInfo : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;

        //任务的ID 
        private string missionid;
        [XmlAttribute()]
        public string MissionID
        {
            get { return missionid;  }
            set
            {
                missionid = value;
                if (this.PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MissionID"));
                }
            }
        }

        //任务的名称
        private string missionname;
        [XmlAttribute()]
        public string MissionName
        {
            get { return missionname; }
            set
            {
                missionname = value;
                if (this.PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MissionName"));
                }
            }
        }

        //任务类型：如主线任务
        private string missiontypenstring;
        [XmlAttribute()]
        public string MissionTypeString
        {
            get
            {
                return missiontypenstring;
             }
            set
            {
                missiontypenstring = value;
                if (this.PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MissionTypeString"));
                }
            }
        }


        //任务限定需求设定    
        public MissionLimitInfo LimitInfo;

        //任务触发方式设定
        public MissionActiveInfo ActiveInfo;

        //任务奖励
        public MissionRewardInfo RewardInfo;

        //答题
        public MissionQuestionInfo QuestionInfo;

        //任务目的信息
        public MissionTaskInfo TaskInfo;

        //任务剧情对话信息 
        public MissionScenarioInfo ScenarioInfo;

        //AI 编辑器
        public MissionAIInfo AIInfo;

        //战斗区域编辑器
        public MissionBattleInfo BattleInfo;


        public MissionInfo()
        {
            LimitInfo = new MissionLimitInfo();
            ActiveInfo = new MissionActiveInfo();
            RewardInfo = new MissionRewardInfo();
            QuestionInfo = new MissionQuestionInfo();
            TaskInfo = new MissionTaskInfo();
            ScenarioInfo = new MissionScenarioInfo();
            AIInfo = new MissionAIInfo();
            BattleInfo = new MissionBattleInfo();
        }
        
        public MissionInfo(string id, string name)
        {
            MissionID = id;
            MissionName = name;
            LimitInfo = new MissionLimitInfo();
            ActiveInfo = new MissionActiveInfo();
            RewardInfo = new MissionRewardInfo();
            QuestionInfo = new MissionQuestionInfo();
            TaskInfo = new MissionTaskInfo();
            ScenarioInfo = new MissionScenarioInfo();
            AIInfo = new MissionAIInfo();
            BattleInfo = new MissionBattleInfo();

        }
    }
    //////////////////////////////////////////////////////////////////////////

    /************************************************************************/
    /*         任务限定需求设定                                                         */
    /************************************************************************/

    public class MissionLimitInfo : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;

        //等级限制
        private string minlevel;
        [XmlAttribute()]
        public string MinLevel
        {
            get
            {
                return minlevel;
            }
            set
            {
                minlevel = value;
                if (this.PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MinLevel"));
                }
            }
        }

        private string maxlevel;
        [XmlAttribute()]
        public string MaxLevel
        {
            get
            {
                return maxlevel;
            }
            set
            {
                maxlevel = value;
                if (this.PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MaxLevel"));
                }
            }
        }

        //转生限制
        private string transminlevel;
        [XmlAttribute()]       
        public string TransMinLevel
        {
            get
            {
                return transminlevel;
            }
            set
            {
                transminlevel = value;
                if (this.PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("TransMinLevel"));
                }
            }
        }

        private string transmaxlevel;
        [XmlAttribute()]
        public string TransMaxLevel
        {
            get
            {
                return transmaxlevel;
            }
            set
            {
                transmaxlevel = value;
                if (this.PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("TransMaxLevel"));
                }
            }
        }

        public ObservableCollection<MissionIDOC> RequestMissionList;


        public AutoStringList RequestRoleIDList;

        public MissionLimitInfo()
        {
            RequestMissionList = new ObservableCollection<MissionIDOC>();
            RequestRoleIDList = new AutoStringList();
        }
    }
    //////////////////////////////////////////////////////////////////////////

    /************************************************************************/
    /*         ObservableCollection<string> 的封装                                                                      */
    /************************************************************************/
    public class AutoString
    {
        private string _string;
        public string String
        {
            get
            {
                return _string;
            }
            set
            {
                _string = value;
            }
        }
        public override string ToString()
        {
            return _string;
        }
     }

    public class AutoStringList : ObservableCollection<AutoString>
    {
        public AutoStringList() : base()
        {

        }      
    }

    public class MissionIDOC
    {
        private string missionid;
        public string MissionID
        {
            get
            {
                return missionid;
            }
            set
            {
                missionid = value;
            }
        }
        public override string ToString()
        {
            return 
                "任务ID:  " + missionid.ToString() +
                "  任务名称  :" + 
                    Program.GetMissionDB().GetMissionNameByID(missionid);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    /************************************************************************/
    /*         任务奖励结果                                                   */
    /************************************************************************/

    public class MissionRewardInfo
    {
        public event PropertyChangedEventHandler PropertyChanged;
        //后置任务ID
        public ObservableCollection<MissionIDOC> PostMissionList;

        //变身造型
        private string transformid;
        [XmlAttribute()]
        public string TransformID
        {
            get
            {
                return transformid;
            }
            set
            {
                transformid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("TransformID"));
                }
            }          
        }

        //显示NPC
        public AutoStringList DisplayNpcList;

        //透明框提示
        private string noteinfo;
        [XmlAttribute()]
        public string NoteInfo
        {
            get
            {
                return noteinfo;
            }
            set
            {
                noteinfo = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("NoteInfo"));
                }
            }
        }
        
        //经验奖励
        private string expreward;
        [XmlAttribute()]
        public string ExpReward
        {
            get
            {
                return expreward;
            }
            set
            {
                expreward = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("ExpReward"));
                }
            }
        }

        //金钱奖励
        private string moneyreward;
        [XmlAttribute()]
        public string MoneyReward
        {
            get
            {
                return moneyreward;
            }
            set
            {
                moneyreward = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MoneyReward"));
                }
            }
        }

        //宠物经验奖励
        private string petexpreward;
        [XmlAttribute()]
        public string PetExpReward
        {
            get
            {
                return petexpreward;
            }
            set
            {
                petexpreward = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("PetExpReward"));
                }
            }
        }

        //历练声望
        private string rewardpetid;
        [XmlAttribute()]
        public string RewardPetID
        {
            get
            {
                return rewardpetid;
            }
            set
            {
                rewardpetid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("RewardPetID"));
                }
            }
        }
        //储备金奖励
        private string smoney;
        [XmlAttribute()]
        public string SMoney
        {
            get
            {
                return smoney;
            }
            set
            {
                smoney = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("SMoney"));
                }
            }
        }

        public string JumpType
        {
            get;
            set;
        }

        //地图ID
        private string rewardmapid;
        [XmlAttribute()]
        public string rewardMapID
        {
            get
            {
                return rewardmapid;
            }
            set
            {
                rewardmapid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("rewardMapID"));
                }
            }
        }

        //地图xpos
        private string rewardmapxpos;
        [XmlAttribute()]
        public string rewardMapXPos
        {
            get
            {
                return rewardmapxpos;
            }
            set 
            {
                rewardmapxpos = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("rewardMapXPos"));

                }
            }
        }

        //地图ypos
        private string rewardmapypos;
        [XmlAttribute()]
        public string rewardMapYPos
        {
            get
            {
                return rewardmapypos;
            }
            set
            {
                rewardmapypos = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("rewardMapYPos"));

                }
            }
        }

        //进度条时间 
        private string processbartime;
        [XmlAttribute()]
        public string ProcessBarTime
        {
            get
            {
                return processbartime;
            }
            set
            {
                processbartime = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("ProcessBarTime"));
                }
            }
        }

        //进度条文字
        private string processbartext;
        [XmlAttribute()]
        public string ProcessBarText
        {
            get
            {
                return processbartext;
            }
            set
            {
                processbartext = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("ProcessBarText"));
                }
            }
        }

        //进度条颜色
        private string processbarcolor;
        [XmlAttribute()]
        public string ProcessBarColor
        {
            get
            {
                return processbarcolor;
            }
            set
            {
                processbarcolor = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("ProcessBarColor"));
                }
            }
        }

        public class RewardItem
        {
            [XmlAttribute()]
            public string ID
            {
                get;
                set;
            }

            [XmlAttribute()]
            public string Num
            {
                get;
                set;
            }
            [XmlAttribute()]
            public string Name
            {
                get;
                set;
            }
            [XmlAttribute()]
            public string ShapeID
            {
                get;
                set;
            }
            [XmlAttribute()]
            public string IsBind
            {
                get;
                set;
            }
        }            
        
        //奖励任务列表
        public ObservableCollection<RewardItem> RewardItemList;
                
        //构造函数
        public MissionRewardInfo()
        {
            DisplayNpcList = new AutoStringList();
            PostMissionList = new ObservableCollection<MissionIDOC>();
            RewardItemList = new ObservableCollection<RewardItem>();
        }

    }


    
    /************************************************************************/
    /*         任务触发方式设定                                                                      */
    /************************************************************************/

    public class MissionActiveInfo
    {
        public event PropertyChangedEventHandler PropertyChanged;
        //任务类型
        private string missiontype;
        [XmlAttribute()]
        public string MissionType
        {
            get
            {
                return missiontype;
            }
            set
            {
                missiontype = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MissionType"));
                }
            }
        }

        //Npc ID
        private string npcid;
        [XmlAttribute()]
        public string NpcID
        {
            get 
            { 
                return npcid; 
            }
            set
            {
                npcid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("NpcID"));
                }
            }

        }

        //Map ID
        private string mapid;
        [XmlAttribute()]
        public string MapID
        {
            get
            {
                return mapid;
            }
            set
            {
                mapid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MapID"));
                }
            }
        }

        //地图场景范围
        private string leftpos;
        [XmlAttribute()]
        public string LeftPos
        {
            get
            {
                return leftpos;
            }
            set
            {
                leftpos = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("LeftPos"));
                }
            }
        }
        private string toppos;
        [XmlAttribute()]
        public string TopPos
        {
            get
            {
                return toppos;
            }
            set
            {
                toppos = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("TopPos"));
                }
            }
        }

        private string rightpos;
        [XmlAttribute()]
        public string RightPos
        {
            get
            {
                return rightpos;
            }
            set
            {
                rightpos = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("RightPos"));
                }
            }
        }

        private string bottompos;
        [XmlAttribute()]
        public string BottomPos
        {
            get
            {
                return bottompos;
            }
            set
            {
                bottompos = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BottomPos"));
                }
            }
        }

        //对象ID 
        private string targetid;
        [XmlAttribute()]
        public string TargetID
        {
            get
            {
                return targetid;
            }
            set
            {
                targetid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("TargetID"));
                }
            }
        }


        //对象Num;
        private string targetnum;
        [XmlAttribute()]
        public string TargetNum
        {
            get
            {
                return targetnum;
            }
            set
            {
                targetnum = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("TargetNum"));
                }
            }
        }

        //最少结束步骤
        private string ministep;
        [XmlAttribute()]
        public string MiniStep
        {
            get
            {
                return ministep;
            }
            set
            {
                ministep = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MiniStep"));
                }
            }
        }


        //单步结束概率
        private string stepprobability;
        [XmlAttribute()]
        public string StepProbability
        {
            get
            {
                return stepprobability;
            }
            set
            {
                stepprobability = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("StepProbability"));
                }
            }
        }

        //最大结束步骤
        private string maxstep;
        [XmlAttribute()]
        public string MaxStep
        {
            get
            {
                return maxstep;
            }
            set
            {
                maxstep = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MaxStep"));
                }
            }
        }

        //是否组队完成
        private string teamstate;
        [XmlAttribute()]
        public string TeamState
        {
            get
            {
                return teamstate;
            }
            set
            {
                teamstate = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("TeamState"));
                }
            }
        }

        //时间限制分钟
        private string timelimit;
        [XmlAttribute()]
        public string TimeLimit
        {
            get
            {
                return timelimit;
            }
            set
            {
                timelimit = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("TimeLimit"));
                }
            }
        }

        //是否从新计时
        private string isrestarttimer;
        [XmlAttribute()]
        public string IsRestartTimer
        {
            get 
            {
                return isrestarttimer;
            }
            set
            {
                isrestarttimer = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("IsRestartTimer"));
                }
            }
        }

        //回收金钱数量
        private string givebackmoney;
        [XmlAttribute()]
        public string GiveBackMoney
        {
            get
            {
                return givebackmoney;
            }
            set
            {
                givebackmoney = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("GiveBackMoney"));
                }
            }
        }

        //回收Pet ID
        private string givebackpetid;
        [XmlAttribute()]
        public string GiveBackPetID
        {
            get
            {
                return givebackpetid;
            }
            set
            {
                givebackpetid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("GiveBackPetID"));
                }
            }
        }

        //使用物品ID
        private string useitemid;
        [XmlAttribute()]
        public string UseItemID
        {
            get
            {
                return useitemid;
            }
            set
            {
                useitemid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("UseItemID"));
                }
            }
        }

        //其他触发方式
        private string othertype;
        [XmlAttribute()]
        public string OtherType
        {
            get
            {
                return othertype;
            }
            set
            {
                othertype = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("OtherType"));
                }
            }
        }
    }// end of class
    //////////////////////////////////////////////////////////////////////////

    /************************************************************************/
    /*         任务触发方式设定                                                                      */
    /************************************************************************/

    public class MissionQuestionInfo
    {
        public event PropertyChangedEventHandler PropertyChanged;

        //正确答案
        private string correctanswer;
        [XmlAttribute()]
        public string CorrectAnswer
        {
            get
            {
                return correctanswer;
            }
            set
            {
                correctanswer = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("CorrectAnswer"));
                }
            }
        }

        //错误答案
        private string wronganswera;
        [XmlAttribute()]
        public string WrongAnswerA
        {
            get
            {
                return wronganswera;
            }
            set
            {
                wronganswera = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("WrongAnswerA"));
                }                
            }
        }

        private string wronganswerb;
        [XmlAttribute()]
        public string WrongAnswerB
        {
            get
            {
                return wronganswerb;
            }
            set
            {
                wronganswerb = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("WrongAnswerB"));
                }
            }
        }

        private string wronganswerc;
        [XmlAttribute()]
        public string WrongAnswerC
        {
            get
            {
                return wronganswerc;
            }
            set
            {
                wronganswerc = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("WrongAnswerC"));
                }
            }
        }

        private string wronganswerd;
        [XmlAttribute()]
        public string WrongAnswerD
        {
            get
            {
                return wronganswerd;
            }
            set
            {
                wronganswerd = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("WrongAnswerD"));
                }
            }
        }

        private string wronganswere;
        [XmlAttribute()]
        public string WrongAnswerE
        {
            get
            {
                return wronganswere;
            }
            set
            {
                wronganswere = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("WrongAnswerE"));
                }
            }
        }

        private string npcid;
        [XmlAttribute()]
        public string NpcID
        {
            get
            {
                return npcid;
            }
            set
            {
                npcid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("NpcID"));
                }
            }
        }

        private string conversion;
        [XmlAttribute()]
        public string Conversion
        {
            get
            {
                return conversion;
            }
            set
            {
                conversion = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("Conversion"));
                }
            }

        }
    }//end of class
    //////////////////////////////////////////////////////////////////////////

    /************************************************************************/
    /*         任务触发方式设定                                                                      */
    /************************************************************************/

    public class MissionTaskInfo
    {
        public event PropertyChangedEventHandler PropertyChanged;

        //描述一
        private string descriptiona;
        [XmlAttribute()]
        public string DescriptionA
        {
            get
            {
                return descriptiona;
            }
            set 
            {
                descriptiona = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("DescriptionA"));
                }
            }
        }
        //目的一
        private string purposea;
        [XmlAttribute()]
        public string PurposeA
        {
            get
            {
                return purposea;
            }
            set
            {
                purposea = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("PurposeA"));
                }
            }
        }
        //追踪一
        private string tracea;
        [XmlAttribute()]
        public string TraceA
        {
            get
            {
                return tracea;
            }
            set
            {
                tracea = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("TraceA"));
                }
            }
        }
        
    }// end of class
    //////////////////////////////////////////////////////////////////////////

    /************************************************************************/
    /*         AI编辑器                                                                            */
    /************************************************************************/
    public class MissionAIInfo
    {
        public event PropertyChangedEventHandler PropertyChanged;

        private string aiid;
        [XmlAttribute()]
        public string AIID
        {
            get
            {
                return aiid;
            }
            set
            {
                aiid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("AIID"));
                }
            }
        }

        [XmlAttribute()]
        public string BattleResult
        {
            get;
            set;
        }

        [XmlAttribute()]
        public string DeathPunish
        {
            get;
            set;
        }

        private string battlelevel;
        [XmlAttribute()]
        public string BattleLevel
        {
            get
            {
                return battlelevel;
            }
            set
            {
                battlelevel = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BattleLevel"));
                }
            }
        }

    }
    //////////////////////////////////////////////////////////////////////////

    /************************************************************************/
    /*         战斗编辑器                                                                            */
    /************************************************************************/

    public class MissionBattleInfo
    {
        public MissionBattleInfo()
        {
            MonsterList = new AutoStringList();
        }

        public event PropertyChangedEventHandler PropertyChanged;

        //战斗地图类型
        private string battlemaptype;
        [XmlAttribute()]
        public string BattleMapType
        {
            get
            {
                return battlemaptype;
            }
            set
            {
                battlemaptype = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BattleMapType"));
                }
            }
        }

        //战斗地图区域
        private string battlezoneid;
        [XmlAttribute()]
        public string BattleZoneID
        {
            get
            {
                return battlezoneid;
            }
            set
            {
                battlezoneid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BattleZoneID"));
                }
            }
        }

        //掉落物品几率
        private string drop;
        [XmlAttribute()]
        public string Drop
        {
            get
            {
                return drop;
            }
            set
            {
                drop = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("Drop"));
                }
            }
        }

        //战斗次数
        private string battletimes;
        [XmlAttribute()]
        public string BattleTimes
        {
            get
            {
                return battletimes;
            }
            set
            {
                battletimes = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BattleTimes"));
                }
            }
        }

        //怪物种类
        //private string monstertype;
        //[XmlAttribute()]
        //public string MonsterType
        //{
        //    get
        //    {
        //        return monstertype;
        //    }
        //    set
        //    {
        //        monstertype = value;
        //        if (PropertyChanged != null)
        //        {
        //            PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MonsterType"));
        //        }
        //    }
        //}

        public AutoStringList MonsterList;


        //怪物数量
        private string monsternum;
        [XmlAttribute()]
        public string MonsterNum
        {
            get
            {
                return monsternum;
            }
            set
            {
                monsternum = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("MonsterNum"));
                }
            }
        }

        //掉落物品ID
        private string dropitemid;
        [XmlAttribute()]
        public string DropItemID
        {
            get
            {
                return dropitemid;
            }
            set
            {
                dropitemid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("DropItemID"));
                }
            }
        }

        //掉落物品数量
        private string dropitemnum;
        [XmlAttribute()]
        public string DropItemNum
        {
            get
            {
                return dropitemnum;
            }
            set
            {
                dropitemnum = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("DropItemNum"));
                }
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////


    /************************************************************************/
    /*         任务剧情编辑                                                                       */
    /************************************************************************/
    public class MissionScenarioInfo
    {
        public event PropertyChangedEventHandler PropertyChanged;

        //剧情动画ID
        private string animationid;
        [XmlAttribute()]
        public string AnimationID
        {
            get
            {
                return animationid;
            }
            set
            {
                animationid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("AnimationID"));
                }
            }
        }
        
        //分支剧情
        private string branchnpcid;
        [XmlAttribute()]
        public string BranchNpcID
        {
            get
            {
                return branchnpcid;
            }
            set
            {
                branchnpcid = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BranchNpcID"));
                }
            }
        }

        //分支对话提示
        private string branchnote;
        [XmlAttribute()]
        public string BranchNote
        {
            get
            {
                return branchnote;
            }
            set
            {
                branchnote = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BranchNote"));
                }
            }
        }
        //分支选项
        private string branchoptiona;
        [XmlAttribute()]
        public string BranchOptionA
        {
            get
            {
                return branchoptiona;
            }
            set
            {
                branchoptiona = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BranchOptionA"));
                }
            }
        }

        private string branchoptionb;
        [XmlAttribute()]
        public string BranchOptionB
        {
            get
            {
                return branchoptionb;
            }
            set
            {
                branchoptionb = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BranchOptionB"));
                }
            }
        }

        private string branchoptionc;
        [XmlAttribute()]
        public string BranchOptionC
        {
            get
            {
                return branchoptionc;
            }
            set
            {
                branchoptionc = value;
                if (PropertyChanged != null)
                {
                    PropertyChanged.Invoke(this, new PropertyChangedEventArgs("BranchOptionC"));
                }
            }
        }

        public class NpcConversation : DependencyObject
        {
            //说话的npcid 如果是168是玩家自己
            public string NpcID
            {
                get;
                set;
            }

            public static readonly DependencyProperty ConversationProperty =
                DependencyProperty.Register("Conversation", typeof(string),
                typeof(NpcConversation), new UIPropertyMetadata(null));
            //对话内容
            public string Conversation
            {
                get { return (string)GetValue(ConversationProperty); }
                set { SetValue(ConversationProperty, value); }
            }
        }

        //对话内容
        public ObservableCollection<NpcConversation> ConversationList;
        //完成后对话
        public ObservableCollection<NpcConversation> FinishConversationList;

        public MissionScenarioInfo()
        {
            ConversationList = new ObservableCollection<NpcConversation>();
            FinishConversationList = new ObservableCollection<NpcConversation>();
        }
      

    }



    /************************************************************************/
    /*          用来辅助序列化和反序列化对象的类       */
    /************************************************************************/
    public class XMLHelper
    {

       static public void WriteToXML( MissionDB db)
        {
            FileStream fs = new FileStream(db.filename, FileMode.Create);

            XmlSerializer serializer = new XmlSerializer(typeof(MissionDB));
            serializer.Serialize(fs, db);

            fs.Flush();
            fs.Close();
        }

       static public MissionDB ReadFromXML(string filename)
       {
           if (File.Exists(filename))
           {
               FileStream fs = new FileStream(filename, FileMode.Open);

               XmlSerializer serializer = new XmlSerializer(typeof(MissionDB));

               MissionDB db = serializer.Deserialize(fs) as MissionDB;

               return db;
           }
           else
           {
               //MessageBox.Show("Can't Find file ");
               Console.WriteLine("Can't Open File : " + filename);
               return null;
           }

       }
    } //end of class
    //////////////////////////////////////////////////////////////////////////



}// end namespace