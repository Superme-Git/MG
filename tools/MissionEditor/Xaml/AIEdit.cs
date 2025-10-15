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
    public partial class AIEdit : Window
    {
        public AIEdit()
        {
            InitializeComponent();

            //set up Bind
            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            Binding AIIDBind = new Binding("AIID");
            AIIDBind.Source = missionInfo.AIInfo;
            AIIDBind.Mode = BindingMode.TwoWay;
            AIIDBind.Path = new PropertyPath("AIID");
            AIIDEditBox.SetBinding(TextBox.TextProperty, AIIDBind);



            if (missionInfo.AIInfo.BattleResult == "要求胜利")
                ResultComboBox.SelectedIndex = 0;
            else if (missionInfo.AIInfo.BattleResult == "均可")
                ResultComboBox.SelectedIndex = 1;
            else
                ResultComboBox.SelectedIndex = -1;

            if (missionInfo.AIInfo.DeathPunish == "0：不接受")
                DeathPunish.SelectedIndex = 0;
            else
                DeathPunish.SelectedIndex = 1;

            Binding BattleLevelBind = new Binding("BattleLevel");
            BattleLevelBind.Source = missionInfo.AIInfo;
            BattleLevelBind.Mode = BindingMode.TwoWay;
            BattleLevelBind.Path = new PropertyPath("BattleLevel");
            BattleLevelEditBox.SetBinding(TextBox.TextProperty, BattleLevelBind);                 

        }//end of func AIEdit

        void UpdateBattleResult(object sender,SelectionChangedEventArgs args)
        {
            ComboBox cb = sender as ComboBox;
            int index = cb.SelectedIndex;

            switch(index)
            {
                case 0:
                    EditorApp.GetCurMissionInfo().AIInfo.BattleResult = "要求胜利";
                    break;
                case 1:
                    EditorApp.GetCurMissionInfo().AIInfo.BattleResult = "均可";
                    break;
            }

        }//end of 

        void UpdateDeathPunish(object sender, SelectionChangedEventArgs args)
        {
            ComboBox cb = sender as ComboBox;
            int index = cb.SelectedIndex;

            switch (index)
            {
                case 0:
                    EditorApp.GetCurMissionInfo().AIInfo.DeathPunish = "0：不接受";
                    break;
                case 1:
                    EditorApp.GetCurMissionInfo().AIInfo.DeathPunish = "1：接受";
                    break;
            }

        }//end of 

        //////////////////////////////////////////////////////////////////////////

    }//end of calss AIEdit
}//end of namespace 
