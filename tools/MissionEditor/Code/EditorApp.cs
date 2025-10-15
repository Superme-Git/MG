//----------------------------------------------
// BindTheButton.cs (c) 2006 by Charles Petzold
//----------------------------------------------
using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Collections.Generic;
using System.Reflection;
using System.Xml;
using System.IO;

namespace Knight.MissionEditor
{
    static public class GetEditorApp
    {
        static public EditorApp myApp;
    }

    public class EditorApp : Application
    {
        /************************************************************************/
        /*  data                                                                                                */
        static public readonly Dictionary<string, ItemMapNode> ItemMap;
        static public readonly Dictionary<string, NpcNode> NpcMap;
        static public readonly Dictionary<string, TransformNode> TransformMap;
        static public readonly Dictionary<string, PetNode> PetMap;


        static protected MissionInfo curMissionInfo;
        static protected MissionDB curMissionDB;
        public EditorMainWindow myMainWindow;
        static public DataPath dataPath;
        /************************************************************************/
        public delegate void CurMissionInfoChangedHandler();
        public delegate void CurMissionDBChangedHandler();
     
        public CurMissionInfoChangedHandler CurMissionInfoChanged;

        //不再使用
        public CurMissionDBChangedHandler CurMissionDBChanged;

        protected void OnCurMissionInfoChanged()
        {
            if (CurMissionInfoChanged != null)
            {
                CurMissionInfoChanged();
            }
        }

        protected void OnCurMissionDBChanged()
        {
            if (CurMissionDBChanged != null)
            {
                CurMissionDBChanged();
            }
        }

        public void SetCurMissionInfo( MissionInfo mInfo)
        {
            if (curMissionInfo != mInfo)
            {
                curMissionInfo = mInfo;
                OnCurMissionInfoChanged();
            }
        }

        public void SetCurMissionDb( MissionDB mDB)
        {
            curMissionDB = mDB;

            myMainWindow.LeftPanel.mTreeView.ItemsSource 
                = curMissionDB.MissionInfoList;

            //myMainWindow.midPanel.preMissionEditor.preMissionIDCombo.ItemsSource
            //    = curMissionDB.MissionInfoList;        
        }

        public bool DelCurMissionFromDB()
        {
            bool bValue =  curMissionDB.MissionInfoList.Remove(curMissionInfo);
            return bValue;
        }

        static public MissionInfo GetCurMissionInfo()
        {
            return curMissionInfo;
        }

        static public MissionDB GetCurMissionDB()
        {
            return curMissionDB;
        }

        static public void GetMissionInfoByID(string id,out MissionInfo missionInfo)
        {
            foreach (MissionInfo mInfo in curMissionDB.MissionInfoList)
            {
                if (id == mInfo.MissionID)
                {
                    missionInfo = mInfo;
                    return;
                }
            }
            missionInfo = null;
        }

        public void EmptyFun()
        {

        }


        [STAThread]
        public static void Main()
        {
            GetEditorApp.myApp  = new EditorApp();

            GetEditorApp.myApp.Run();
        }


        static EditorApp()
        {
            dataPath = DataPath.ReadFromXML();

            new ItemMapBuilder(out ItemMap);
            new NpcMapBuilder(out NpcMap);
            new TransformMapBuilder(out TransformMap);
            new PetMapBuilder(out PetMap);


        }
                                 
        protected override void OnStartup(StartupEventArgs args)
        {
            base.OnStartup(args);

            myMainWindow = new EditorMainWindow();

            myMainWindow.Show();
        }

        protected override void OnSessionEnding(SessionEndingCancelEventArgs args)
        {
            base.OnSessionEnding(args);

            MessageBoxResult result =
                MessageBox.Show("Do you want to save your data?",
                                MainWindow.Title, MessageBoxButton.YesNoCancel,
                                MessageBoxImage.Question, MessageBoxResult.Yes);

            args.Cancel = (result == MessageBoxResult.Cancel);
        }


    }
}
