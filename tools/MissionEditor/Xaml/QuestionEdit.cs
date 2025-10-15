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
    public partial class QuestionEdit : Window
    {
        public QuestionEdit()
        {
            InitializeComponent();

            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();

            Binding CorrectBind = new Binding("CorrectAnswer");
            CorrectBind.Source = missionInfo.QuestionInfo;
            CorrectBind.Mode = BindingMode.TwoWay;
            CorrectBind.Path = new PropertyPath("CorrectAnswer");
            CorrectAnswerEditBox.SetBinding(TextBox.TextProperty, CorrectBind);

            Binding ABind = new Binding("WrongAnswerA");
            ABind.Source = missionInfo.QuestionInfo;
            ABind.Mode = BindingMode.TwoWay;
            ABind.Path = new PropertyPath("WrongAnswerA");
            WrongAnswerAEditBox.SetBinding(TextBox.TextProperty, ABind);

            Binding BBind = new Binding("WrongAnswerB");
            BBind.Source = missionInfo.QuestionInfo;
            BBind.Mode = BindingMode.TwoWay;
            BBind.Path = new PropertyPath("WrongAnswerB");
            WrongAnswerBEditBox.SetBinding(TextBox.TextProperty, BBind);

            Binding CBind = new Binding("WrongAnswerC");
            CBind.Source = missionInfo.QuestionInfo;
            CBind.Mode = BindingMode.TwoWay;
            CBind.Path = new PropertyPath("WrongAnswerC");
            WrongAnswerCEditBox.SetBinding(TextBox.TextProperty, CBind);

            Binding DBind = new Binding("WrongAnswerD");
            DBind.Source = missionInfo.QuestionInfo;
            DBind.Mode = BindingMode.TwoWay;
            DBind.Path = new PropertyPath("WrongAnswerD");
            WrongAnswerDEditBox.SetBinding(TextBox.TextProperty, DBind);

            Binding EBind = new Binding("WrongAnswerE");
            EBind.Source = missionInfo.QuestionInfo;
            EBind.Mode = BindingMode.TwoWay;
            EBind.Path = new PropertyPath("WrongAnswerE");
            WrongAnswerEEditBox.SetBinding(TextBox.TextProperty, EBind);

            Binding NpcIDBind = new Binding("NpcID");
            NpcIDBind.Source = missionInfo.QuestionInfo;
            NpcIDBind.Mode = BindingMode.TwoWay;
            NpcIDBind.Path = new PropertyPath("NpcID");
            NpcIDEditBox.SetBinding(TextBox.TextProperty, NpcIDBind);

            Binding ConBind = new Binding("Conversion");
            ConBind.Source = missionInfo.QuestionInfo;
            ConBind.Mode = BindingMode.TwoWay;
            ConBind.Path = new PropertyPath("Conversion");
            ConversionEditBox.SetBinding(TextBox.TextProperty, ConBind);


        }//end of func MissionTask


        void NpcIDChanged(object sender, TextChangedEventArgs args)
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
    }//end of calss MissionTask
}//end of namespace 
