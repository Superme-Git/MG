using System;
using System.Windows;
using System.Windows.Data;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Collections.Specialized;
using System.Collections.Generic;


namespace Knight.MissionEditor
{
     /************************************************************************/
    /*                  MissionListView  显示任务链条里面所有任务                                                       */
    /************************************************************************/
    public class MissionListView : ListView
    {
        public MissionListView()
        {
            // Create a GridView as the View of the ListView.
            GridView grdvue = new GridView();
            View = grdvue;

            // Create two GridView columns.
            GridViewColumn col = new GridViewColumn();
            col.Header = "任务ID";
            col.Width = 100;
            col.DisplayMemberBinding = new Binding("MissionID");
            grdvue.Columns.Add(col);

            col = new GridViewColumn();
            col.Header = "任务名称";
            col.Width = 100;
            col.DisplayMemberBinding = new Binding("MissionName");
            grdvue.Columns.Add(col);

            col = new GridViewColumn();
            col.Header = "任务类型";
            col.Width = 100;
            col.DisplayMemberBinding = new Binding("MissionTypeString");
            grdvue.Columns.Add(col);

            Height = 600;
            Margin = new Thickness(10);

            //消息处理
            SelectionChanged += OnMissionChanged;

            //GetEditorApp.myApp.CurMissionDBChanged +=
            //    new EditorApp.CurMissionDBChangedHandler(Init);         
        }
        //////////////////////////////////////////////////////////////////////////
        
        /// <任务ID改变>
        ///  由任务ID，初始化相关其他界面
        /// </任务ID改变>
        /// <param name="sender"></param>
        /// <param name="args"></param>
        void OnMissionChanged(object sender, SelectionChangedEventArgs args)
        {
            ListBox lstbox = sender as ListBox;
            MissionInfo obj = lstbox.SelectedItem as MissionInfo;
            GetEditorApp.myApp.SetCurMissionInfo(obj);         
        }


        //public void Init( )
        //{
        //    //Items.Clear();
        //    //MissionDB db = EditorApp.GetCurMissionDB();

        //    //foreach (MissionInfo mInfo in db.MissionInfoList)
        //    //{
        //    //    Items.Add(mInfo);
        //    //}
        //    ItemsSource = EditorApp.GetCurMissionDB().MissionInfoList;

        //}

    }
}
