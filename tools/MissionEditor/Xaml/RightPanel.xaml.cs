using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Knight.MissionEditor;

namespace Knight.MissionEditor.Xaml
{
    /// <summary>
    /// RightPanel.xaml 的交互逻辑
    /// </summary>
    public partial class RightPanel : StackPanel
    {
        public  Dictionary<int, string> MissionTypeMap;

        int GetMissionTypeKey(string value)
        {
            foreach (var pair in MissionTypeMap)
            {
                if (pair.Value == value)
                    return pair.Key;               
            }
            return -1;
        }

        public RightPanel()
        {
            InitializeComponent();
            //事件
            GetEditorApp.myApp.CurMissionInfoChanged += 
                new EditorApp.CurMissionInfoChangedHandler(InitComboBox);

            //添加任务类型 
            MissionTypeMap = new Dictionary<int, string>();
            MissionTypeMap.Add(0, "10:点击NPC");
            MissionTypeMap.Add(1, "11：给予金钱");
            MissionTypeMap.Add(2, "12：给予物品");
            MissionTypeMap.Add(3, "13：给与宠物");
            MissionTypeMap.Add(4, "17：答题");
            MissionTypeMap.Add(5, "22：使用物品");
            MissionTypeMap.Add(6, "32：练功区掉落物品");
            MissionTypeMap.Add(7, "34：练功区战斗计场次");
            MissionTypeMap.Add(8, "35：练功区战斗计数量");
            MissionTypeMap.Add(9, "50：无条件");
            MissionTypeMap.Add(10, "54：步数触发");
            MissionTypeMap.Add(11, "56：区域触发护送");
            MissionTypeMap.Add(12, "40：NPC战斗");
            MissionTypeMap.Add(13, "58：等级限制任务");
            MissionTypeMap.Add(14, "59：特殊类型");
            //////////////////////////////////////////////////////////////////////////
            //事件
            GetEditorApp.myApp.CurMissionInfoChanged +=
                new EditorApp.CurMissionInfoChangedHandler(Init);
        }

        public void Init()
        {
            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            if (missionInfo != null)
            {
                Binding NpcIDBing = new Binding("NpcIDBing");
                NpcIDBing.Source = missionInfo.ActiveInfo;
                NpcIDBing.Mode = BindingMode.TwoWay;
                NpcIDBing.Path = new PropertyPath("NpcID");              
                NpcIDEditBox.SetBinding(TextBox.TextProperty,NpcIDBing);

                Binding LeftPosBind = new Binding("LeftPosBind");
                LeftPosBind.Source = missionInfo.ActiveInfo;
                LeftPosBind.Mode = BindingMode.TwoWay;
                LeftPosBind.Path = new PropertyPath("LeftPos");
                LeftPosEditBox.SetBinding(TextBox.TextProperty, LeftPosBind);

                Binding TopPosBind = new Binding("TopPosBind");
                TopPosBind.Source = missionInfo.ActiveInfo;
                TopPosBind.Mode = BindingMode.TwoWay;
                TopPosBind.Path = new PropertyPath("TopPos");
                TopPosEditBox.SetBinding(TextBox.TextProperty, TopPosBind);

                Binding RightPosBind = new Binding("RightPosBind");
                RightPosBind.Source = missionInfo.ActiveInfo;
                RightPosBind.Mode = BindingMode.TwoWay;
                RightPosBind.Path = new PropertyPath("RightPos");
                RightPosEditBox.SetBinding(TextBox.TextProperty, RightPosBind);

                Binding BottomPosBind = new Binding("BottomPosBind");
                BottomPosBind.Source = missionInfo.ActiveInfo;
                BottomPosBind.Mode = BindingMode.TwoWay;
                BottomPosBind.Path = new PropertyPath("BottomPos");
                BottomPosEditBox.SetBinding(TextBox.TextProperty, BottomPosBind);

                Binding TargetNumBind = new Binding("TargetNumBind");
                TargetNumBind.Source = missionInfo.ActiveInfo;
                TargetNumBind.Mode = BindingMode.TwoWay;
                TargetNumBind.Path = new PropertyPath("TargetNum");
                TargetNumEditBox.SetBinding(TextBox.TextProperty, TargetNumBind);

                Binding TargetIDBind = new Binding("TargetIDBind");
                TargetIDBind.Source = missionInfo.ActiveInfo;
                TargetIDBind.Mode = BindingMode.TwoWay;
                TargetIDBind.Path = new PropertyPath("TargetID");
                TargetIDEditBox.SetBinding(TextBox.TextProperty, TargetIDBind);

                Binding MiniStepBind = new Binding("MiniStepBind");
                MiniStepBind.Source = missionInfo.ActiveInfo;
                MiniStepBind.Mode = BindingMode.TwoWay;
                MiniStepBind.Path = new PropertyPath("MiniStep");
                MiniStepEditBox.SetBinding(TextBox.TextProperty, MiniStepBind);

                Binding StepProbabilityBind = new Binding("StepProbabilityBind");
                StepProbabilityBind.Source = missionInfo.ActiveInfo;
                StepProbabilityBind.Mode = BindingMode.TwoWay;
                StepProbabilityBind.Path = new PropertyPath("StepProbability");
                StepProbabilityEditBox.SetBinding(TextBox.TextProperty, StepProbabilityBind);

                Binding MaxStepBind = new Binding("MaxStepBind");
                MaxStepBind.Source = missionInfo.ActiveInfo;
                MaxStepBind.Mode = BindingMode.TwoWay;
                MaxStepBind.Path = new PropertyPath("MaxStep");
                MaxStepEditBox.SetBinding(TextBox.TextProperty, MaxStepBind);

                Binding TimeLimitBind = new Binding("TimeLimitBind");
                TimeLimitBind.Source = missionInfo.ActiveInfo;
                TimeLimitBind.Mode = BindingMode.TwoWay;
                TimeLimitBind.Path = new PropertyPath("TimeLimit");
                TimeLimitEditBox.SetBinding(TextBox.TextProperty, TimeLimitBind);

                Binding MapIDBind = new Binding("MapIDBind");
                MapIDBind.Source = missionInfo.ActiveInfo;
                MapIDBind.Mode = BindingMode.TwoWay;
                MapIDBind.Path = new PropertyPath("MapID");
                BindingExpressionBase beb = MapIDEditBox.SetBinding(TextBox.TextProperty, MapIDBind);

                Binding GiveBackMoneyBind = new Binding("GiveBackMoney");
                GiveBackMoneyBind.Source = missionInfo.ActiveInfo;
                GiveBackMoneyBind.Mode = BindingMode.TwoWay;
                GiveBackMoneyBind.Path = new PropertyPath("GiveBackMoney");
                GiveBackMoneyEditBox.SetBinding(TextBox.TextProperty, GiveBackMoneyBind);


                Binding GiveBackPetBind = new Binding("GiveBackPetID");
                GiveBackPetBind.Source = missionInfo.ActiveInfo;
                GiveBackPetBind.Mode = BindingMode.TwoWay;
                GiveBackPetBind.Path = new PropertyPath("GiveBackPetID");
                GiveBackPetIDEditBox.SetBinding(TextBox.TextProperty, GiveBackPetBind);

                Binding UseItemIDBind = new Binding("UseItemID");
                UseItemIDBind.Source = missionInfo.ActiveInfo;
                UseItemIDBind.Mode = BindingMode.TwoWay;
                UseItemIDBind.Path = new PropertyPath("UseItemID");
                UseItemIDEditBox.SetBinding(TextBox.TextProperty, UseItemIDBind);

                Binding OtherTypeBind = new Binding("OtherType");
                OtherTypeBind.Source = missionInfo.ActiveInfo;
                OtherTypeBind.Mode = BindingMode.TwoWay;
                OtherTypeBind.Path = new PropertyPath("OtherType");
                OtherTypeEditBox.SetBinding(TextBox.TextProperty, OtherTypeBind);

            }
        }


        //事件处理
        public void OnMissionTypeChanged(object sender, SelectionChangedEventArgs args)
        {
            ComboBox cb = sender as ComboBox;
            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            int index = cb.SelectedIndex;
            if (index >= 0)
            {
                missionInfo.ActiveInfo.MissionType = MissionTypeMap[index];
            }
        }

        /************************************************************************/
        /* 任务奖励edit按钮                                                                     */
        /************************************************************************/
        void MissionRewordClick(object sender, RoutedEventArgs args)
       {
           MissionReward dlg = new MissionReward();
           dlg.Owner = GetEditorApp.myApp.myMainWindow;
           dlg.ShowDialog();
       }

        void ScenarioClick(object sender, RoutedEventArgs args)
        {
            ScenarioPanel dlg = new ScenarioPanel();
            dlg.Owner = GetEditorApp.myApp.myMainWindow;
            dlg.ShowDialog();
        }

        void TaskClick(object sender, RoutedEventArgs args)
        {
            if (taskdlg == null)
                taskdlg = new MissionTask(this);   
            taskdlg.Show();
        }

        void QuestionClick(object sender, RoutedEventArgs args)
        {
            QuestionEdit dlg = new QuestionEdit();
            dlg.Owner = GetEditorApp.myApp.myMainWindow;
            dlg.ShowDialog();
        }

        void BattleClick(object sender, RoutedEventArgs args)
        {
            BattleEdit dlg = new BattleEdit();
            dlg.Owner = GetEditorApp.myApp.myMainWindow;
            dlg.ShowDialog();
        }

        void AIClick(object sender, RoutedEventArgs args)
        {
            AIEdit dlg = new AIEdit();
            dlg.Owner = GetEditorApp.myApp.myMainWindow;
            dlg.ShowDialog();
        }

        public void InitComboBox()
        {
            MissionInfo mInfo = EditorApp.GetCurMissionInfo();

            if (mInfo != null)
            {
                if (mInfo.ActiveInfo.TeamState == "Yes")
                    TeamStateComboBox.SelectedIndex = 0;
                else if (mInfo.ActiveInfo.TeamState == "No")
                    TeamStateComboBox.SelectedIndex = 1;
                else if (mInfo.ActiveInfo.TeamState == "Both")
                    TeamStateComboBox.SelectedIndex = 2;
                else
                    TeamStateComboBox.SelectedIndex = -1;


                if (mInfo.ActiveInfo.IsRestartTimer == "Yes")
                    IsRestartTimerComboBox.SelectedIndex = 0;
                else if (mInfo.ActiveInfo.IsRestartTimer == "No")
                    IsRestartTimerComboBox.SelectedIndex = 1;
                else
                    IsRestartTimerComboBox.SelectedIndex = -1;

                MissionTypeComboBox.SelectedIndex = GetMissionTypeKey(mInfo.ActiveInfo.MissionType);
            }

        }// end Init
        //////////////////////////////////////////////////////////////////////////

        public void UpdataTeamState(object sender, SelectionChangedEventArgs args)
        {
            ComboBox cb = sender as ComboBox;
            int index = cb.SelectedIndex;
            if (index == 0)
                EditorApp.GetCurMissionInfo().ActiveInfo.TeamState = "Yes";
            else if (index == 1)
                EditorApp.GetCurMissionInfo().ActiveInfo.TeamState = "No";
            else if (index == 2)
                EditorApp.GetCurMissionInfo().ActiveInfo.TeamState = "Both";

                
        }//end TeamStateComboBox
        //////////////////////////////////////////////////////////////////////////

        public void UpdataRestartTimer(object sender, SelectionChangedEventArgs args)
        {
            ComboBox cb = sender as ComboBox;
            int index = cb.SelectedIndex;
            if (index == 0)
                EditorApp.GetCurMissionInfo().ActiveInfo.IsRestartTimer = "Yes";
            else if (index == 1)
                EditorApp.GetCurMissionInfo().ActiveInfo.IsRestartTimer = "No";
        }// end UpdataRestartTimer
        //////////////////////////////////////////////////////////////////////////

        public void NpcTextBoxChanged(object sender, TextChangedEventArgs args)
        {
            TextBox tb = sender as TextBox;
            string id = tb.Text;

            if (EditorApp.NpcMap.ContainsKey(id))
            {
                NpcNameLable.Content = EditorApp.NpcMap[id].ItemName;
            }
            else
            {
                NpcNameLable.Content = "错误的ID";
            }
        }

        public void clearTaskdlg()
        {
            taskdlg = null;
        }

        private MissionTask taskdlg = null;
    }
}
