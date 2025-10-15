using System;
using System.Collections.Generic;
using System.Linq;
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
    public partial class MissionReward : Window
    {
        public MissionReward()
        {
            InitializeComponent();

            //Set up UI
            GridView grdvue = new GridView();
            RewardItemList.View = grdvue;

            // Create two GridView columns.
            GridViewColumn col = new GridViewColumn();
            col.Header = "物品ID";
            col.Width = 50;
            col.DisplayMemberBinding = new Binding("ID");
            grdvue.Columns.Add(col);

            col = new GridViewColumn();
            col.Header = "数量";
            col.Width = 50;
            col.DisplayMemberBinding = new Binding("Num");
            grdvue.Columns.Add(col);

            col = new GridViewColumn();
            col.Header = "名称";
            col.Width = 50;
            col.DisplayMemberBinding = new Binding("Name");
            grdvue.Columns.Add(col);

            col = new GridViewColumn();
            col.Header = "造型";
            col.Width = 50;
            col.DisplayMemberBinding = new Binding("ShapeID");
            grdvue.Columns.Add(col);

            col = new GridViewColumn();
            col.Header = "绑定";
            col.Width = 50;
            col.DisplayMemberBinding = new Binding("IsBind");
            grdvue.Columns.Add(col);



            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            //Set up Binding
            PostMissionIDList.ItemsSource = missionInfo.RewardInfo.PostMissionList;

            Binding  TransIDBind = new Binding("TransformID");
            TransIDBind.Source = missionInfo.RewardInfo;
            TransIDBind.Mode = BindingMode.TwoWay;
            TransIDBind.Path = new PropertyPath("TransformID");
            TransIDEditBox.SetBinding(TextBox.TextProperty, TransIDBind);

            DisplayNpcList.ItemsSource = missionInfo.RewardInfo.DisplayNpcList;

            Binding NoteInfoBind = new Binding("NoteInfo");
            NoteInfoBind.Source = missionInfo.RewardInfo;
            NoteInfoBind.Mode = BindingMode.TwoWay;
            NoteInfoBind.Path = new PropertyPath("NoteInfo");
            NoteInfoEditBox.SetBinding(TextBox.TextProperty, NoteInfoBind);

            ExpRewardEditBox.SetMaxLength(8);
            Binding ExpRewardBind = new Binding("ExpReward");
            ExpRewardBind.Source = missionInfo.RewardInfo;
            ExpRewardBind.Mode = BindingMode.TwoWay;
            ExpRewardBind.Path = new PropertyPath("ExpReward");
            ExpRewardEditBox.SetBinding(TextBox.TextProperty, ExpRewardBind);

            Binding MoneyRewardBind = new Binding("MoneyReward");
            MoneyRewardBind.Source = missionInfo.RewardInfo;
            MoneyRewardBind.Mode = BindingMode.TwoWay;
            MoneyRewardBind.Path = new PropertyPath("MoneyReward");
            MoneyRewardEditBox.SetBinding(TextBox.TextProperty, MoneyRewardBind);

            Binding PetExpRewardBind = new Binding("PetExpReward");
            PetExpRewardBind.Source = missionInfo.RewardInfo;
            PetExpRewardBind.Mode = BindingMode.TwoWay;
            PetExpRewardBind.Path = new PropertyPath("PetExpReward");
            PetExpRewardEditBox.SetBinding(TextBox.TextProperty, PetExpRewardBind);

            Binding RewardPetIDBind = new Binding("RewardPetID");
            RewardPetIDBind.Source = missionInfo.RewardInfo;
            RewardPetIDBind.Mode = BindingMode.TwoWay;
            RewardPetIDBind.Path = new PropertyPath("RewardPetID");
            RewardPetIDEditBox.SetBinding(TextBox.TextProperty, RewardPetIDBind);

            Binding SMoneyBind = new Binding("SMoney");
            SMoneyBind.Source = missionInfo.RewardInfo;
            SMoneyBind.Mode = BindingMode.TwoWay;
            SMoneyBind.Path = new PropertyPath("SMoney");
            SMoneyEditBox.SetBinding(TextBox.TextProperty, SMoneyBind);


            Binding MapIDBind = new Binding("rewardMapID");
            MapIDBind.Source = missionInfo.RewardInfo;
            MapIDBind.Mode = BindingMode.TwoWay;
            MapIDBind.Path = new PropertyPath("rewardMapID");
            MapIDEditBox.SetBinding(TextBox.TextProperty, MapIDBind);

            Binding MapXPosBind = new Binding("rewardMapXPos");
            MapXPosBind.Source = missionInfo.RewardInfo;
            MapXPosBind.Mode = BindingMode.TwoWay;
            MapXPosBind.Path = new PropertyPath("rewardMapXPos");
            MapXPosEditBox.SetBinding(TextBox.TextProperty, MapXPosBind);

            Binding MapYPosBind = new Binding("rewardMapYPos");
            MapYPosBind.Source = missionInfo.RewardInfo;
            MapYPosBind.Mode = BindingMode.TwoWay;
            MapYPosBind.Path = new PropertyPath("rewardMapYPos");
            MapYPosEditBox.SetBinding(TextBox.TextProperty, MapYPosBind);


            Binding PBTimeBind = new Binding("ProcessBarTime");
            PBTimeBind.Source = missionInfo.RewardInfo;
            PBTimeBind.Mode = BindingMode.TwoWay;
            PBTimeBind.Path = new PropertyPath("ProcessBarTime");
            ProcessBarTimeEditBox.SetBinding(TextBox.TextProperty, PBTimeBind);

            Binding PBTextBind = new Binding("ProcessBarText");
            PBTextBind.Source = missionInfo.RewardInfo;
            PBTextBind.Mode = BindingMode.TwoWay;
            PBTextBind.Path = new PropertyPath("ProcessBarText");
            ProcessBarText.SetBinding(TextBox.TextProperty, PBTextBind);

            Binding PBColorBind = new Binding("ProcessBarColor");
            PBColorBind.Source = missionInfo.RewardInfo;
            PBColorBind.Mode = BindingMode.TwoWay;
            PBColorBind.Path = new PropertyPath("ProcessBarColor");
            ProcessBarColor.SetBinding(TextBox.TextProperty, PBColorBind);




            if (missionInfo.RewardInfo.JumpType == "无")
                JumpTypeComboBox.SelectedIndex = 0;
            else if (missionInfo.RewardInfo.JumpType == "传送")
                JumpTypeComboBox.SelectedIndex = 1;
            else if (missionInfo.RewardInfo.JumpType == "飞行")
                JumpTypeComboBox.SelectedIndex = 2;
            else if (missionInfo.RewardInfo.JumpType == "副本")
                JumpTypeComboBox.SelectedIndex = 3;
            else
                JumpTypeComboBox.SelectedIndex = 0;

            RewardItemList.ItemsSource = missionInfo.RewardInfo.RewardItemList;
        }// end of MissionReward()

        public void OnAddItem(object sender, RoutedEventArgs args)
        {
            string itemID = RewardItemIDEditBox.Text;

            if (!EditorApp.ItemMap.ContainsKey(itemID))
            {
                MessageBox.Show("根据指定的ID找不到物品，添加失败");
                return;
            }

            if (RewardItemNumEditBox.Text == string.Empty)
            {
                MessageBox.Show("请填写物品数量字段再添加");
                return;
            }





            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();

            MissionRewardInfo.RewardItem item = new MissionRewardInfo.RewardItem();
            item.ID = RewardItemIDEditBox.Text;
            item.Num = RewardItemNumEditBox.Text;
            item.Name = EditorApp.ItemMap[itemID].ItemName;

            if (RewardItemShapeIDEditBox.Text == string.Empty)
            {
                item.ShapeID = "0";
            }
            else
            {
                item.ShapeID = RewardItemShapeIDEditBox.Text;
            }

            if (RewardItemIsBindEditBox.Text == string.Empty)
            {
                item.IsBind = "1";
            }
            else
            {
                item.IsBind = RewardItemIsBindEditBox.Text;
            }

            missionInfo.RewardInfo.RewardItemList.Add(item);
        }

        public void OnDelAllItem(object sender, RoutedEventArgs args)
        {
            MissionRewardInfo.RewardItem rItem = RewardItemList.SelectedItem as MissionRewardInfo.RewardItem;
            if (rItem == null)
                MessageBox.Show("请选择要删除的物品");
            else
            {
                MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
                missionInfo.RewardInfo.RewardItemList.Remove(rItem);
            }
        }

        public void AddNPC(object sender, RoutedEventArgs args)
        {
            string npcid = DisplayNpcIDEditbox.Text;

            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();

            AutoString Aus = new AutoString();
            Aus.String = npcid;
            missionInfo.RewardInfo.DisplayNpcList.Add(Aus);
        }

        public void DelNPC(object sender, RoutedEventArgs args)
        {

            AutoString Aus = DisplayNpcList.SelectedItem as AutoString;

            if (Aus == null)
                MessageBox.Show("请先选择一个要删除的Npc ID");
            else
            {
                MissionInfo missionInfo = EditorApp.GetCurMissionInfo();     
                missionInfo.RewardInfo.DisplayNpcList.Remove(Aus);
            }

        }



        void TransformIDChanged(object sender, TextChangedEventArgs args)
        {
            TextBox tb = sender as TextBox;
            string id = tb.Text;

            if (EditorApp.TransformMap.ContainsKey(id))
            {
                TransformNameLable.Content = EditorApp.TransformMap[id].TransformName;
            }
            else
            {
                TransformNameLable.Content = "错误的ID";
            }
        }
        

        void AddPostMission(object sender, RoutedEventArgs args)
        {
            string MissionID = PostMissionID.Text;

            if (MissionID == EditorApp.GetCurMissionInfo().MissionID)
            {
                MessageBox.Show("不能添加当前任务为后置任务，这个不是盗梦空间不要嵌套");
                return;
            }

            IEnumerable<MissionInfo> result = from missionInfo in EditorApp.GetCurMissionDB().MissionInfoList
                                              where missionInfo.MissionID == MissionID
                                              select missionInfo;

            if (result.Count() == 0)
            {
                MessageBox.Show("警告：添加了一个未生成的任务ID。");
                //return;//策划要求，允许添加未生成的后置任务ID
            }
            MissionIDOC mission = new MissionIDOC();
            mission.MissionID = MissionID;

            EditorApp.GetCurMissionInfo().RewardInfo.PostMissionList.Add(mission);
            
            
        }

        void ClearPostMission(object sender, RoutedEventArgs args)
        {
            MissionIDOC missionID = PostMissionIDList.SelectedItem as MissionIDOC;
            if (missionID == null)
                MessageBox.Show("请先选择一个要删除的任务");
            else
                EditorApp.GetCurMissionInfo().RewardInfo.PostMissionList.Remove(missionID);
        }


        void UpdateJumpNodeChanged(object sender, SelectionChangedEventArgs args)
        {
            ComboBox cb = sender as ComboBox;
            int index = cb.SelectedIndex;

            switch (index)
            {
                case 0:
                    EditorApp.GetCurMissionInfo().RewardInfo.JumpType = "无";
                    break;
                case 1:
                    EditorApp.GetCurMissionInfo().RewardInfo.JumpType = "传送";
                    break;
                case 2:
                    EditorApp.GetCurMissionInfo().RewardInfo.JumpType = "飞行";
                    break;
                case 3:
                    EditorApp.GetCurMissionInfo().RewardInfo.JumpType = "副本";
                    break;
            }

        }

    }//end of Class
}//end of namespace