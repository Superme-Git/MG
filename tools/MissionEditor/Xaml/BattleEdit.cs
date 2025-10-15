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
    public partial class BattleEdit : Window
    {
        public BattleEdit()
        {
            InitializeComponent();

            DropEditBox.SetMaxLength(5);

            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            //set up bind
            //Binding BattleMapIDBind = new Binding("BattleMapID");
            //BattleMapIDBind.Source = missionInfo.BattleInfo;
            //BattleMapIDBind.Mode = BindingMode.TwoWay;
            //BattleMapIDBind.Path = new PropertyPath("BattleMapID");
            //MapIDEditBox.SetBinding(TextBox.TextProperty, BattleMapIDBind);

            if (missionInfo.BattleInfo.BattleMapType == "明雷区")
                BattleTypeComboBox.SelectedIndex = 0;
            else if (missionInfo.BattleInfo.BattleMapType == "暗雷区")
                BattleTypeComboBox.SelectedIndex = 1;
            else
                BattleTypeComboBox.SelectedIndex = -1;


            Binding ZoneIDBind = new Binding("BattleZoneID");
            ZoneIDBind.Source = missionInfo.BattleInfo;
            ZoneIDBind.Mode = BindingMode.TwoWay;
            ZoneIDBind.Path = new PropertyPath("BattleZoneID");
            ZoneIDEditBox.SetBinding(TextBox.TextProperty, ZoneIDBind);

            Binding DropBind = new Binding("Drop");
            DropBind.Source = missionInfo.BattleInfo;
            DropBind.Mode = BindingMode.TwoWay;
            DropBind.Path = new PropertyPath("Drop");
            DropEditBox.SetBinding(TextBox.TextProperty, DropBind);

            Binding BattleTimesBind = new Binding("BattleTimes");
            BattleTimesBind.Source = missionInfo.BattleInfo;
            BattleTimesBind.Mode = BindingMode.TwoWay;
            BattleTimesBind.Path = new PropertyPath("BattleTimes");
            BattleTimesEditBox.SetBinding(TextBox.TextProperty, BattleTimesBind);

            //Binding MonsterTypeBind = new Binding("MonsterType");
            //MonsterTypeBind.Source = missionInfo.BattleInfo;
            //MonsterTypeBind.Mode = BindingMode.TwoWay;
            //MonsterTypeBind.Path = new PropertyPath("MonsterType");
            //MonsterTypeEditBox.SetBinding(TextBox.TextProperty, MonsterTypeBind);

            MonsterList.ItemsSource = missionInfo.BattleInfo.MonsterList;


            Binding MonsterNumBind = new Binding("MonsterNum");
            MonsterNumBind.Source = missionInfo.BattleInfo;
            MonsterNumBind.Mode = BindingMode.TwoWay;
            MonsterNumBind.Path = new PropertyPath("MonsterNum");
            MonsterNumEditBox.SetBinding(TextBox.TextProperty, MonsterNumBind);

            Binding DropItemIDBind = new Binding("DropItemID");
            DropItemIDBind.Source = missionInfo.BattleInfo;
            DropItemIDBind.Mode = BindingMode.TwoWay;
            DropItemIDBind.Path = new PropertyPath("DropItemID");
            DropItemIDEditBox.SetBinding(TextBox.TextProperty, DropItemIDBind);

            Binding DropItemNumBind = new Binding("DropItemNum");
            DropItemNumBind.Source = missionInfo.BattleInfo;
            DropItemNumBind.Mode = BindingMode.TwoWay;
            DropItemNumBind.Path = new PropertyPath("DropItemNum");
            DropItemNumEditBox.SetBinding(TextBox.TextProperty, DropItemNumBind);
                



        }//end of func BattleEdit

        public void UpdataMapType(object sender, SelectionChangedEventArgs args)
        {
            ComboBox cb = sender as ComboBox;
            int index = cb.SelectedIndex;
            if (index == 0)
                EditorApp.GetCurMissionInfo().BattleInfo.BattleMapType = "明雷区";
            else if (index == 1)
                EditorApp.GetCurMissionInfo().BattleInfo.BattleMapType = "暗雷区";
        }//end TeamStateComboBox

        public void OnAdd(object sender, RoutedEventArgs args)
        {
            string monster = MonsterTypeEditBox.Text;

            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();

            AutoString Aus = new AutoString();
            Aus.String = monster;
            missionInfo.BattleInfo.MonsterList.Add(Aus);
        }

        public void OnDel(object sender, RoutedEventArgs args)
        {
            AutoString Aus = MonsterList.SelectedItem as AutoString;

            if (Aus == null)
                MessageBox.Show("请先选择一个要删除的怪物种类");
            else
            {
                MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
                missionInfo.BattleInfo.MonsterList.Remove(Aus);
            }
        }


    }//end of calss BattleEdit
}//end of namespace 
