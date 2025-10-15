using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;


namespace Knight.MissionEditor
{

    public class LeftPanel : StackPanel
    {
        // UI 
        public MissionListView mTreeView;
        public Button AddMissionBT;
        public Button DelMissionBT;

        public LeftPanel()
        {
            //ID and Name
            StackPanel IDPanel = new StackPanel();
            IDPanel.Orientation = Orientation.Horizontal;
            IDPanel.Children.Add(new MLable("任务列表"));

            //AddMissionBT
            AddMissionBT = new Button();
            AddMissionBT.Content = "添加";
            AddMissionBT.Click += AddMissionOnClick;
            AddMissionBT.Margin = new Thickness(5);
            AddMissionBT.Width = 68;
            IDPanel.Children.Add(AddMissionBT);

            //DelMissionBT
            DelMissionBT = new Button();
            DelMissionBT.Content = "删除";
            DelMissionBT.Click += DelMissionOnClick;
            DelMissionBT.Margin = new Thickness(5);
            DelMissionBT.Width = 68;
            IDPanel.Children.Add(DelMissionBT);

            Children.Add(IDPanel);

            mTreeView = new MissionListView();     
            Children.Add(mTreeView);
        }
        //////////////////////////////////////////////////////////////////////////

        /************************************************************************/
        /*         添加一个任务                                                                            */
        /************************************************************************/
        private void AddMissionOnClick(object sender, RoutedEventArgs args)
        {
            MissionInfo newMission = new MissionInfo();
            MissionDB curMissionDB = EditorApp.GetCurMissionDB();
            curMissionDB.MissionInfoList.Add(newMission);
            GetEditorApp.myApp.SetCurMissionInfo(newMission);
        }
        //////////////////////////////////////////////////////////////////////////

        /************************************************************************/
        /*         删除一个任务                                                                            */
        /************************************************************************/
        private void DelMissionOnClick(object sender, RoutedEventArgs args)
        {
           if ( EditorApp.GetCurMissionDB()!= null || 
               EditorApp.GetCurMissionInfo() != null )
           {
               if (GetEditorApp.myApp.DelCurMissionFromDB())
               {
                   if (EditorApp.GetCurMissionDB().MissionInfoList.Count > 0)
                   {
                       GetEditorApp.myApp.SetCurMissionInfo(EditorApp.GetCurMissionDB().MissionInfoList[0]);
                   }
                   else{
                       GetEditorApp.myApp.SetCurMissionInfo(null);
                   }
               }
           }
        }
        //////////////////////////////////////////////////////////////////////////

    }
}
