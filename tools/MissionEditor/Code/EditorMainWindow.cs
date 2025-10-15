using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.IO;
using System.Reflection;
using System.Collections.Generic;
using Microsoft.Win32;
using Knight.MissionEditor.Xaml;

//2015 Yao NPOI Method
using NPOI;
using NPOI.HSSF.UserModel;
using NPOI.XSSF.UserModel;
using NPOI.SS.UserModel;

namespace Knight.MissionEditor
{
    /************************************************************************/
    /*          EditorMainWindow 是编辑器的主界面，没有使用XAML             */
    /************************************************************************/
    public class EditorMainWindow : Window
    {
        // UI 控件
        public LeftPanel LeftPanel;

        public MiddlePanel midPanel;

        public RightPanel rightPanle;

        public DockPanel mainDockPanel;

        public Menu mainMenu; 

        //2015 Yao NPOI Method
        XSSFWorkbook HSSFWB;
        /************************************************************************/
        /*          EditorMainWindow 构造函数，创建UI控件                                 */
        /************************************************************************/
        public EditorMainWindow()
        {
            Title = "剧情任务编辑器——专用";

            // Create DockPanel.
            DockPanel mainDockPanel = new DockPanel();
            Content = mainDockPanel;

            // Create Menu docked at top.
            mainMenu = new Menu();
            mainDockPanel.Children.Add(mainMenu);
            DockPanel.SetDock(mainMenu, Dock.Top);

            // Create menu items.
            MenuItem itemFile = new MenuItem();
            itemFile.Header = "_File";
            mainMenu.Items.Add(itemFile);

            MenuItem item = new MenuItem();
            item.Header = "_Open MissionDB File";
            itemFile.Items.Add(item);
            item.Click += OpenMissionDB;

            item = new MenuItem();
            item.Header = "_New MissionDB File";
            itemFile.Items.Add(item);
            item.Click += NewMissionDB;


            item = new MenuItem();
            item.Header = "_Close MissionDB File";
            itemFile.Items.Add(item);
            item.Click += CloseMissionDB;

            item = new MenuItem();
            item.Header = "_Save";
            itemFile.Items.Add(item);
            item.Click += SaveButtonClick;


            item = new MenuItem();
            item.Header = "_Save To xlsx";
            itemFile.Items.Add(item);
            item.Click += SaveExcelButtonClick;

            //2015 Yao
            item = new MenuItem();
            item.Header = "_Open Excel";
            itemFile.Items.Add(item);
            item.Click += OpenExcelButtonClick;

            //Create Grid 
            Grid mainGrid = new Grid();
            mainDockPanel.Children.Add(mainGrid);
            mainGrid.ShowGridLines = true;
            for (int i = 0; i < 3; ++i)
            {
                ColumnDefinition coldef = new ColumnDefinition();
                coldef.Width = new GridLength(3, GridUnitType.Star);
                mainGrid.ColumnDefinitions.Add(coldef);
            }

            LeftPanel = new LeftPanel();
            mainGrid.Children.Add(LeftPanel);
            Grid.SetColumn(LeftPanel, 0);

            midPanel = new MiddlePanel();
            mainGrid.Children.Add(midPanel);
            Grid.SetColumn(midPanel, 1);

            rightPanle = new RightPanel();
            mainGrid.Children.Add(rightPanle);
            Grid.SetColumn(rightPanle, 2);

            


            SetEnable(false);
        }
        //////////////////////////////////////////////////////////////////////////

        /************************************************************************/
        /*          打开任务DB文件                                                                      */
        /************************************************************************/
        void CloseMissionDB(object sender, RoutedEventArgs args)
        {
            CloseDB();
        }
        //////////////////////////////////////////////////////////////////////////


        /************************************************************************/
        /*          保存DB文件                                                                      */
        /************************************************************************/
        void  SaveButtonClick(object sender, RoutedEventArgs args)
        {
            string strFilter = "XML Files(*.xml)|*.xml";

            SaveFileDialog dlg = new SaveFileDialog();
            dlg.Filter = strFilter;

            Nullable<bool> result = dlg.ShowDialog();

            if (result == true)
            {
                EditorApp.GetCurMissionDB().filename = dlg.FileName;
                XMLHelper.WriteToXML(EditorApp.GetCurMissionDB());
            }
        }



        /************************************************************************/
        /*          打开任务DB文件                                                                      */
        /************************************************************************/
        void NewMissionDB(object sender, RoutedEventArgs args)
        {
            if (EditorApp.GetCurMissionDB() != null)
            {
                string messageBoxText = "Do you want to save changes?";
                string caption = "确定框";
                MessageBoxButton button = MessageBoxButton.YesNoCancel;
                MessageBoxImage icon = MessageBoxImage.Question;

                MessageBoxResult result = MessageBox.Show(messageBoxText, caption, button, icon);

                switch(result)
                {
                    case MessageBoxResult.Yes:
                        //to do 
                        //save
                        break;
                    case MessageBoxResult.No:
                        NewDB();
                        break;
                    case MessageBoxResult.Cancel:
                        //do nothing
                        break;
                }
            }
            else{
                NewDB();
            }

        }

        //////////////////////////////////////////////////////////////////////////

        /************************************************************************/
        /*          打开任务DB文件                                                                      */
        /************************************************************************/
        void OpenMissionDB(object sender, RoutedEventArgs args)
        {
            string strFilter ="XML Files(*.xml)|*.xml";

            OpenFileDialog dlg = new OpenFileDialog();
            dlg.CheckFileExists = true;
            dlg.Filter = strFilter;

            if ((bool)dlg.ShowDialog(this))
            {
                Stream strm = null;

                try
                {
                    //strm = new FileStream(dlg.FileName, FileMode.Open);
                    GetEditorApp.myApp.SetCurMissionDb(XMLHelper.ReadFromXML(dlg.FileName));
                    if (EditorApp.GetCurMissionDB().MissionInfoList.Count > 0)
                    {
                        GetEditorApp.myApp.SetCurMissionInfo(EditorApp.GetCurMissionDB().MissionInfoList[0]);
                    }
                    SetEnable(true);

                }
                catch (Exception exc)
                {
                    MessageBox.Show(exc.Message, Title);
                }
                finally
                {
                    if (strm != null)
                        strm.Close();
                }
            }
        }
        //////////////////////////////////////////////////////////////////////////


        /************************************************************************/
        /*          Disable 除了Menu外的控件                                                      */
        /************************************************************************/
        public void SetEnable(bool bState)
        {
            LeftPanel.IsEnabled = bState;
            midPanel.IsEnabled = bState;
            rightPanle.IsEnabled = bState;
        }
        //////////////////////////////////////////////////////////////////////////
        public void SaveExcelButtonClick(object sender, RoutedEventArgs args)
        {
            MissionDB db = EditorApp.GetCurMissionDB();

            if (db != null)
            {
                string strFilter = "XML Files(*.xlsx)|*.xlsx";

                SaveFileDialog dlg = new SaveFileDialog();
                dlg.Filter = strFilter;

                Nullable<bool> result = dlg.ShowDialog();

                if (result == true)
                {
                    Type tp = Type.GetType("Knight.MissionEditor.MissionInfoAIO");
                    Dictionary<string, ColInfo> cmap = ColInfoBuild.BuildInfo(tp);

                    MXLSWrite xls = new MXLSWrite(dlg.FileName, cmap, ref HSSFWB);

                    xls.WriteAllData(ref db);
                    xls.Save();

                    EditorApp.GetCurMissionDB().filename = dlg.FileName.Substring(0, dlg.FileName.Length - 4) + "xml";
                    XMLHelper.WriteToXML(EditorApp.GetCurMissionDB());
                    MessageBox.Show("保存文件OK");
                }
                //////////////////////////////////////////////////////////////////////////

            }
            else
                MessageBox.Show("请先打开一个任务文件");

        }


        /************************************************************************/
        /*          CloseDB                                                                                  */
        /************************************************************************/
        public void CloseDB()
        {

            GetEditorApp.myApp.SetCurMissionDb(new MissionDB());
            
            SetEnable(false);

        }
       //////////////////////////////////////////////////////////////////////////

        /************************************************************************/
        /*                  New DB file                                                                     */
        /************************************************************************/
        public void NewDB()
        {
            GetEditorApp.myApp.SetCurMissionDb(new MissionDB());
            GetEditorApp.myApp.SetCurMissionInfo(new MissionInfo());

            SetEnable(true);
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        public void OpenExcelButtonClick(object sender, RoutedEventArgs args)
        {
            string strFilter ="XML Files(*.xlsx)|*.xlsx";

            OpenFileDialog dlg = new OpenFileDialog();
            dlg.CheckFileExists = true;
            dlg.Filter = strFilter;

            if ((bool)dlg.ShowDialog(this))
            {
                Stream strm = null;
                try
                {
                    Type tp = Type.GetType("Knight.MissionEditor.MissionInfoAIO");
                    Dictionary<string, ColInfo> cmap = ColInfoBuild.BuildInfo(tp);

                    //2015 Yao NPOI Method
                    using (FileStream FS = new FileStream(dlg.FileName, FileMode.Open, FileAccess.Read))
                    {
                        HSSFWB = new XSSFWorkbook(FS);
                        FS.Close();
                    }

                    MXLSXRead xlsReader = new MXLSXRead(cmap, ref HSSFWB);
                    MissionDB tmpDb = EditorApp.GetCurMissionDB();
                    xlsReader.ReadExcel(dlg.FileName, ref tmpDb);
                    GetEditorApp.myApp.SetCurMissionDb(tmpDb);
                    GetEditorApp.myApp.SetCurMissionInfo(tmpDb.MissionInfoList[0]);
                    SetEnable(true);
                }
                catch (Exception exc)
                {
                    MessageBox.Show(exc.Message, Title);
                }
                finally
                {
                    if (strm != null)
                        strm.Close();
                }
            }

        }
        //////////////////////////////////////////////////////////////////////////

    }//end of  public class EditorMainWindow : Window
}
