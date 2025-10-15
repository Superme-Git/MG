using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
namespace ExcelParse2
{
    /// <summary>
    /// SelectXlsDlg.xaml 的交互逻辑
    /// </summary>
    public partial class SelectXlsDlg : Window
    {
        private static bool initFinish = false;
        public SelectXlsDlg()
        {
            InitializeComponent();
            initFinish = true;
            mSrcCombox.SelectedIndex = MainWindow.mSortListIndex;
        }
        private void RefreshSrcList(string filter)
        {
            m_ListDir.Clear();
            Dictionary<string, int> selectIndex=new Dictionary<string,int>();
            for(int i=0;i<mDstList.Items.Count;i++)
            {
                selectIndex.Add(mDstList.Items[i].ToString(), i);
            }

            mSrcList.Items.Clear();
            string strPath = "";
            if (MainWindow.mSelectFileType == 0)
            {
                strPath = MainWindow.mSrcDataXlsPath;
            }
            else if (MainWindow.mSelectFileType == 1)
            {
                strPath = MainWindow.mSrcDataCsvPath;
            }
            else if (MainWindow.mSelectFileType == 2)
            {
                strPath = MainWindow.mSrcDataTxtPath;
            }
            List<string> files = MainWindow.GetFiles(strPath, filter);
            if (MainWindow.mSortListIndex == 0)
            {
                for (int i = 0; i < files.Count; i++)
                {
                    int key;
                    if (!selectIndex.TryGetValue(files[i], out key))
                    {
                        mSrcList.Items.Add(files[i]);
                        m_ListDir.Add(files[i], mSrcList.Items.Count-1);
                    }
                }
            }
            else if (MainWindow.mSortListIndex == 1)
            {
                List<string> newList = new List<string>();
                for (int i = 0; i < files.Count; i++)
                {
                    int key;
                    if (!selectIndex.TryGetValue(files[i], out key))
                    {
                        newList.Add(files[i]);
                    }
                }
                for (int mm = 0; mm < newList.Count; mm++)
                {
                    for (int nn = mm + 1; nn < newList.Count; nn++)
                    {
                        FileInfo fileinfo1 = new FileInfo(newList[mm]);
                        DateTime dataTime1 = fileinfo1.LastWriteTime;

                        FileInfo fileinfo2 = new FileInfo(newList[nn]);
                        DateTime dataTime2 = fileinfo2.LastWriteTime;

                        if (DateTime.Compare(dataTime1, dataTime2) < 0)
                        {
                            string strTemp;
                            strTemp = newList[mm];
                            newList[mm] = newList[nn];
                            newList[nn] = strTemp;
                        }
                    }
                }

                for (int ii = 0; ii < newList.Count; ii++)
                {
                    mSrcList.Items.Add(newList[ii]);
                    m_ListDir.Add(newList[ii], mSrcList.Items.Count - 1);
                }
                

            }

        }

        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            mInputBox.Text = "*";
            mAllListFiles.Clear();
            string strPath = "";
            if (MainWindow.mSelectFileType == 0)
            {
                strPath = MainWindow.mSrcDataXlsPath;
            }
            else if (MainWindow.mSelectFileType == 1)
            {
                strPath = MainWindow.mSrcDataCsvPath;
            }
            else if (MainWindow.mSelectFileType == 2)
            {
                strPath = MainWindow.mSrcDataTxtPath;
            }
            mAllListFiles = MainWindow.GetFiles(strPath, "*");
            nCheckIndex = 2;
        }

        private void mDstList_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            string selectXls = mDstList.Items[mDstList.SelectedIndex].ToString();
            mDstList.Items.RemoveAt(mDstList.SelectedIndex);
            mSrcList.Items.Add(selectXls);
        }

        private void mSrcList_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {

            if (nCheckIndex == 1)//只添加单表
            {
                string selectXls = mSrcList.Items[mSrcList.SelectedIndex].ToString();
                mSrcList.Items.RemoveAt(mSrcList.SelectedIndex);
                mDstList.Items.Add(selectXls);
            }
            else if (nCheckIndex == 2)//添加复合表
            {
                List<string>result = GetAllItem();
                if (result.Count > 0)
                {
                    for (int ii = 0; ii < result.Count; ii++)
                    {
                        for (int jj = 0; jj < mAllListFiles.Count; jj++)
                        {
                            int index = mAllListFiles[jj].IndexOf(MainWindow.ConverPath(result[ii]));
                            if (index >= 0)
                            {
                                int index2 = mSrcList.Items.IndexOf(mAllListFiles[jj]);
                                if (index2 >= 0)
                                {
                                    mSrcList.Items.RemoveAt(index2);
                                }
                                mDstList.Items.Add(mAllListFiles[jj]);
                            }
                        }
                    }
                }
                else
                {
                    string selectXls = mSrcList.Items[mSrcList.SelectedIndex].ToString();
                    mSrcList.Items.RemoveAt(mSrcList.SelectedIndex);
                    mDstList.Items.Add(selectXls);
                }

            }
            else if (nCheckIndex == 3)//添加文件夹
            {
                string selectXls = mSrcList.Items[mSrcList.SelectedIndex].ToString();
                int nIndex = selectXls.LastIndexOf("\\");
                string strFolder = "";
                for (int i = 0; i < nIndex; i++)
                {
                    strFolder += selectXls[i];
                }
                List<string> result = new List<string>();
                for (int ii = 0; ii < mSrcList.Items.Count; ii++)
                {
                    int indexFolder = mSrcList.Items[ii].ToString().IndexOf(strFolder);
                    if (indexFolder >= 0)
                    {
                        result.Add(mSrcList.Items[ii].ToString());
                    }
                }
                for (int jj = 0; jj < result.Count; jj++)
                {
                    mSrcList.Items.Remove(result[jj]);
                    mDstList.Items.Add(result[jj]);
                }
            }

        }

        private List<string> GetServerBean()
        {
            List<string> result= new List<string>();
            string selectXls = mSrcList.Items[mSrcList.SelectedIndex].ToString();
            List<BeanDef> serverList = MainWindow.mDefineMgr.mServerDef;
            for (int ii = 0; ii < serverList.Count; ii++)
            {
                string str = serverList[ii].from;
                int index = str.IndexOf(',');
                if (index >= 0)
                {
                    int index2 = -1;
                    string[] strList = str.Split(',');
                    for (int jj = 0; jj < strList.Length; jj++)
                    {
                        index2 = selectXls.IndexOf(MainWindow.ConverPath(strList[jj]));
                        if (index2 >= 0)
                        {
                            break;
                        }
                    }
                    if (index2 >= 0)
                    {
                        for (int mm = 0; mm < strList.Length; mm++)
                        {
                            result.Add(strList[mm]);
                        }

                    }
                }
                else
                {
                    continue;
                }
            }
            return result;
        }
        private List<string> GetClientBean()
        {
            List<string> result = new List<string>();
            string selectXls = mSrcList.Items[mSrcList.SelectedIndex].ToString();
            List<BeanDef> serverList = MainWindow.mDefineMgr.mClientDef;
            for (int ii = 0; ii < serverList.Count; ii++)
            {
                string str = serverList[ii].from;
                int index = str.IndexOf(',');
                if (index >= 0)
                {

                    string[] strList = str.Split(',');
                    int index2 = -1;
                    for (int jj = 0; jj < strList.Length; jj++)
                    {
                        index2 = selectXls.IndexOf(MainWindow.ConverPath(strList[jj]));
                        if (index2 >= 0)
                        {
                            break;
                        }
                    }
                    if (index2 >= 0)
                    {
                        for (int mm = 0; mm < strList.Length; mm++)
                        {
                            result.Add(strList[mm]);
                        }
                    
                    }

                }
                else
                {
                    continue;
                }
            }
            return result;
        }
        private List<string> GetAllItem()
        {
            List<string> resultClient = GetClientBean();
            List<string> resultServer = GetServerBean();
            List<string> result = new List<string>();
            for (int ii = 0; ii < resultClient.Count; ii++) 
            {
                bool has = false;
                for (int jj = 0; jj < result.Count; jj++)
                {
                    if (result[jj] == resultClient[ii])
                    {
                        has = true;
                    }
                }
                if (!has)
                {
                    result.Add(resultClient[ii]);
                }
            }
            for (int ii = 0; ii < resultServer.Count; ii++)
            {
                bool has = false;
                for (int jj = 0; jj < result.Count; jj++)
                {
                    if (result[jj] == resultServer[ii])
                    {
                        has = true;
                    }
                }
                if (!has)
                {
                    result.Add(resultServer[ii]);
                }
            }
            return result;
        }
        private void Button_ClickOk(object sender, RoutedEventArgs e)
        {
            mSelectFiles.Clear();
            for(int i=0;i<mDstList.Items.Count;i++)
            {
                mSelectFiles.Add(mDstList.Items[i].ToString());
            }
            Close();
        }

        private void Button_ClickCancel(object sender, RoutedEventArgs e)
        {
            initFinish = false;
            mSelectFiles.Clear();
            Close();
        }
        public List<string> mSelectFiles=new List<string>();
        public List<string> mAllListFiles = new List<string>();
        public Dictionary<string, int> m_ListDir = new Dictionary<string, int>();
        public int nCheckIndex = 0;
        private void mInputBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            string text = mInputBox.Text;
            RefreshSrcList(text);
            
        }

        private void ComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (initFinish)
            {
                MainWindow.mSortListIndex = mSrcCombox.SelectedIndex;
                MainWindow.mOptionIni.FindAndAddApp("Config", "SelectSortType").FindAndAddItem("SelectSortType").SetValue(mSrcCombox.SelectedIndex.ToString());
                MainWindow.mOptionIni.WriteToFile();
                if (mSrcCombox.SelectedIndex == 0)
                {
                    mInputBox.IsEnabled = true;
                }
                else if (mSrcCombox.SelectedIndex == 1)
                {
                    mInputBox.Text = "*";
                    mInputBox.IsEnabled = false;
                }
                RefreshSrcList(mInputBox.Text);
            }

           
        }

        private void ChangeCheckIndex()
        {

            if (mSrcCheckBox2.IsChecked == true)
            {
                nCheckIndex = 2;
            }
            else if (mSrcCheckBox3.IsChecked == true)
            {
                nCheckIndex = 3;
            }
            else
            {
                nCheckIndex = 1;
            }
            
        }
        private void CheckBox_Checked_3(object sender, RoutedEventArgs e)
        {
            if (mSrcCheckBox3.IsChecked == true)
            {
                if (mSrcCheckBox2 != null)
                {
                    mSrcCheckBox2.IsChecked = false;
                }
            }
            ChangeCheckIndex();
        }
        private void CheckBox_Checked_2(object sender, RoutedEventArgs e)
        {
            if (mSrcCheckBox2.IsChecked == true)
            {
                if (mSrcCheckBox3 != null)
                {
                    mSrcCheckBox3.IsChecked = false;
                }
                
            }
            ChangeCheckIndex();
        }

    }
}
