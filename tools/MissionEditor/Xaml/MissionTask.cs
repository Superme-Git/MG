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
    public partial class MissionTask : Window
    {
        public MissionTask(object rp)
        {
            InitializeComponent();


            //事件
            GetEditorApp.myApp.CurMissionInfoChanged +=
                new EditorApp.CurMissionInfoChangedHandler(Init);

            this.Closed += new EventHandler(OnClosed);

            rightpanel = rp;
            Init();

        }//end of func MissionTask

        public void OnClosed(object sender, EventArgs e)
        {
            ((RightPanel)rightpanel).clearTaskdlg();
        }

        public void Init()
        {

            //Set up Binding 
            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            if (missionInfo != null)
            {

                Binding DescriptionABind = new Binding("DescriptionA");
                DescriptionABind.Source = missionInfo.TaskInfo;
                DescriptionABind.Mode = BindingMode.TwoWay;
                DescriptionABind.Path = new PropertyPath("DescriptionA");
                DescriptionAEditBox.SetBinding(TextBox.TextProperty, DescriptionABind);

                Binding PurposeABind = new Binding("PurposeA");
                PurposeABind.Source = missionInfo.TaskInfo;
                PurposeABind.Mode = BindingMode.TwoWay;
                PurposeABind.Path = new PropertyPath("PurposeA");
                PurposeAEditBox.SetBinding(TextBox.TextProperty, PurposeABind);

                Binding TraceABind = new Binding("TraceA");
                TraceABind.Source = missionInfo.TaskInfo;
                TraceABind.Mode = BindingMode.TwoWay;
                TraceABind.Path = new PropertyPath("TraceA");
                TraceAEditBox.SetBinding(TextBox.TextProperty, TraceABind);
            }
        }

        private object rightpanel;
    }//end of calss MissionTask
}//end of namespace 
