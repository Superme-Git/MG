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
    public partial class ScenarioPanel : Window
    {
        public ScenarioPanel()
        {
            InitializeComponent();

           // //set up UI 
           // GridView grdvue = new GridView();
           //// ConversationList.View = grdvue;

           // // Create two GridView columns.
           // GridViewColumn col = new GridViewColumn();
           // col.Header = "Npc名称";
           // col.Width = 60;
           // col.DisplayMemberBinding = new Binding("NpcID");
           // grdvue.Columns.Add(col);

           // col = new GridViewColumn();
           // col.Header = "对话内容";
           // col.Width = 500;
           // col.DisplayMemberBinding = new Binding("Conversation");
           // grdvue.Columns.Add(col);

           // grdvue = new GridView();
           // FinishConversationList.View = grdvue;

           // // Create two GridView columns.
           // col = new GridViewColumn();
           // col.Header = "Npc名称";
           // col.Width = 60;
           // col.DisplayMemberBinding = new Binding("NpcID");
           // grdvue.Columns.Add(col);

           // col = new GridViewColumn();
           // col.Header = "对话内容";
           // col.Width = 500;
           // col.DisplayMemberBinding = new Binding("Conversation");
           // grdvue.Columns.Add(col);


            //set up data binding
            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();

            ConversationList.ItemsSource = missionInfo.ScenarioInfo.ConversationList;

            FinishConversationList.ItemsSource = missionInfo.ScenarioInfo.FinishConversationList;

            Binding AnimationIDBind = new Binding("AnimationID");
            AnimationIDBind.Source = missionInfo.ScenarioInfo;
            AnimationIDBind.Mode = BindingMode.TwoWay;
            AnimationIDBind.Path = new PropertyPath("AnimationID");
            AnimationIDEditBox.SetBinding(TextBox.TextProperty, AnimationIDBind);

            Binding BranchNpcIDBind = new Binding("BranchNpcID");
            BranchNpcIDBind.Source = missionInfo.ScenarioInfo;
            BranchNpcIDBind.Mode = BindingMode.TwoWay;
            BranchNpcIDBind.Path = new PropertyPath("BranchNpcID");
            BranchNpcIDEditBox.SetBinding(TextBox.TextProperty, BranchNpcIDBind);

            Binding BranchConversationBind = new Binding("BranchNote");
            BranchConversationBind.Source = missionInfo.ScenarioInfo;
            BranchConversationBind.Mode = BindingMode.TwoWay;
            BranchConversationBind.Path = new PropertyPath("BranchNote");
            BranchConversationEditBox.SetBinding(TextBox.TextProperty, BranchConversationBind);

            Binding BranchOptionABind = new Binding("BranchOptionA");
            BranchOptionABind.Source = missionInfo.ScenarioInfo;
            BranchOptionABind.Mode = BindingMode.TwoWay;
            BranchOptionABind.Path = new PropertyPath("BranchOptionA");
            BranchOptionAEditBox.SetBinding(TextBox.TextProperty, BranchOptionABind);

            Binding BranchOptionBBind = new Binding("BranchOptionB");
            BranchOptionBBind.Source = missionInfo.ScenarioInfo;
            BranchOptionBBind.Mode = BindingMode.TwoWay;
            BranchOptionBBind.Path = new PropertyPath("BranchOptionB");
            BranchOptionBEditBox.SetBinding(TextBox.TextProperty, BranchOptionBBind);

            Binding BranchOptionCBind = new Binding("BranchOptionC");
            BranchOptionCBind.Source = missionInfo.ScenarioInfo;
            BranchOptionCBind.Mode = BindingMode.TwoWay;
            BranchOptionCBind.Path = new PropertyPath("BranchOptionC");
            BranchOptionCEditBox.SetBinding(TextBox.TextProperty, BranchOptionCBind);
        }

        void IDChanged(object sender, TextChangedEventArgs args)
        {
            TextBox tb = sender as TextBox;
            string id = tb.Text;

            if (EditorApp.NpcMap.ContainsKey(id))
            {
                DisplayNpcNameLable.Content = EditorApp.NpcMap[id].ItemName;
            }          
            else
            {
                DisplayNpcNameLable.Content = "错误的ID";
            }
        }

        void NpcIDChanged(object sender, TextChangedEventArgs args)
        {
            TextBox tb = sender as TextBox;
            string id = tb.Text;

            if (EditorApp.NpcMap.ContainsKey(id))
            {
                HideNpcNameLable.Content = EditorApp.NpcMap[id].ItemName;
            }
            else
            {
                HideNpcNameLable.Content = "错误的ID";
            }
        }

        void AddConversation(object sender, RoutedEventArgs args)
        {
            if (DisPlayNpcIDEditbox.Text == null || DisPlayNpcIDEditbox.Text == "")
            {
                MessageBox.Show("NPC的ID不能为空");
                return;
            }
            MissionScenarioInfo.NpcConversation npccon = new MissionScenarioInfo.NpcConversation();
            npccon.NpcID = DisPlayNpcIDEditbox.Text;
            npccon.Conversation = ConversationEditBox.Text;

            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            missionInfo.ScenarioInfo.ConversationList.Add(npccon);

        }

        void ClearConversation(object sender, RoutedEventArgs args)
        {
            MissionScenarioInfo.NpcConversation npccon =
                 ConversationList.SelectedItem as MissionScenarioInfo.NpcConversation;

            if (npccon == null)
            {
                MessageBox.Show("请先选择一个要删除的对话");
            }
            else
            {
                MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
                missionInfo.ScenarioInfo.ConversationList.Remove(npccon);
            }
        }

        void AddFinishConversation(object sender,RoutedEventArgs args)
        {
            if (DisPlayNpcIDEditbox.Text == null || DisPlayNpcIDEditbox.Text == "")
            {
                MessageBox.Show("NPC的ID不能为空");
                return;
            }
            MissionScenarioInfo.NpcConversation npccon = new MissionScenarioInfo.NpcConversation();
            npccon.NpcID = DisPlayNpcIDEditbox.Text;
            npccon.Conversation = ConversationEditBox.Text;

            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            missionInfo.ScenarioInfo.FinishConversationList.Add(npccon);
        }

        void ClearFinishConversation(object sender,RoutedEventArgs args)
        {
            MissionScenarioInfo.NpcConversation npccon =
                FinishConversationList.SelectedItem as MissionScenarioInfo.NpcConversation;

            if (npccon == null)
            {
                MessageBox.Show("请先选择一个要删除的对话");              
            }
            else
            {
                MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
                missionInfo.ScenarioInfo.FinishConversationList.Remove(npccon);
            }
        }


    }//end of calss ScenarioPanel

    public class BoolToVisibilityConverter : IValueConverter
    {
        public object Convert(object value, Type targetType,
          object parameter, System.Globalization.CultureInfo culture)
        {
            bool param = bool.Parse(parameter as string);
            bool val = (bool)value;

            return val == param ?
              Visibility.Visible : Visibility.Hidden;
        }

        public object ConvertBack(object value, Type targetType,
          object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}//end of namespace 
