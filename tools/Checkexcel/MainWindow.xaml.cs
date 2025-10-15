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
using System.Windows.Navigation;
using System.Windows.Shapes;
using CSTools;
using System.Threading;

namespace CheckExcel
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        //路径
        public static string mSrcDataXlsPath = "";//源xls路径
        public static string mSrcDataEffectPath = "";//Effect路径
        public static string mSrcDataMusicPath = "";//音效路径

        public static string mEffectFile = "";
        public static string mEffectCol = "";
        public static string mMusicFile = "";
        public static string mMusicCol = "";


        public static Dictionary<int, string> m_SrcDataXlsData = new Dictionary<int, string>();
        public static Dictionary<int, string> m_SrcDataColData = new Dictionary<int, string>();

        private static bool mMakeError = false;

        //数据管理
        public static DefineManager mDefineMgr = new DefineManager();
        public static DataManager mDataMgr = new DataManager();

        public static DataManager mDataManager = new DataManager();

        //配置文件
        //public static ToolsIni mOptionIni= new ToolsIni(System.AppDomain.CurrentDomain.BaseDirectory+"/ConExcelParseOption2.ini");
        public static ToolsIni mOptionIni = new ToolsIni("ExcelFileCheck.ini");

        //实例化
        public static MainWindow mInstance = null;

        //文件版本号.
        public static ushort mBinFileVersion = 101;

        public List<string> mMusicFileNames;

        //自动生成.

        //有用
        public MainWindow()
        {
            InitializeComponent();
            GetConfigPath();
            mInstance = this;
        }
        //有用
        private void GetConfigPath()
        {
            m_SrcDataXlsData.Clear();
            m_SrcDataColData.Clear();
            mSrcDataXlsPath = mOptionIni.FindAndAddApp("XlsPath", "Path").FindAndAddItem("Path").GetValue();
            mSrcDataEffectPath = mOptionIni.FindAndAddApp("EffectPath", "Path").FindAndAddItem("Path").GetValue();
            mSrcDataMusicPath = mOptionIni.FindAndAddApp("MusicPath", "Path").FindAndAddItem("Path").GetValue();
            mEffectFile = mOptionIni.FindAndAddApp("EXlsName", "File").FindAndAddItem("File").GetValue();
            mEffectCol = mOptionIni.FindAndAddApp("EColName", "Col").FindAndAddItem("Col").GetValue();
            string[] strFileS = mEffectFile.Split(',');
            string[] strColS = mEffectCol.Split(',');
            for(int i = 0; i < strFileS.Length; i++){
                m_SrcDataXlsData.Add(i, strFileS[i]);
                m_SrcDataColData.Add(i, strColS[i]);
            }
        }


        public static MainWindow getInstance()
        {
            return mInstance;
        }
        public static void Error(string info)
        {
            getInstance().mOutputText.Dispatcher.BeginInvoke(new Action(() => { getInstance().mOutputText.Items.Insert(0, "error:" + info); }));
            //getInstance().mOutputText.Items.Insert(0, "error:"+info);
            mMakeError = true;
        }
        public static void Warning(string info)
        {
            getInstance().mOutputText.Dispatcher.BeginInvoke(new Action(() => { getInstance().mOutputText.Items.Insert(0, "warning:" + info); }));
            //getInstance().mOutputText.Items.Insert(0, "warning:"+info);
        }
        public static void Info(string info)
        {
            getInstance().mOutputText.Dispatcher.BeginInvoke(new Action(() => { getInstance().mOutputText.Items.Insert(0, "info:" + info); }));
            //getInstance().mOutputText.Items.Insert(0, "info:" + info);
        }
        /*
        public static void MessageBox(string info)
        {
            System.Windows.MessageBox.Show(info);
        }*/
        public static List<string> GetFiles(string path,string filter)
        {
            List<string> result = new List<string>();
            try
            {
                DirectoryInfo parentFolder = new DirectoryInfo(path);
                FileInfo[] parentFolderInfo = parentFolder.GetFiles(filter, SearchOption.AllDirectories);
                for (int i = 0; i < parentFolderInfo.Length; i++)
                {
                    result.Add(parentFolderInfo[i].FullName);
                }
            }
            catch(ArgumentException e)
            {
                MainWindow.Error("路径" + path + "无效,异常信息=" + e.Message);
            }
            return result;
        }
        public static bool IsBaseType(string type)
        {
            return type == "bool" || type == "int" || type == "long" || type == "double" || type == "string" || type == "String";
        }
        public static bool IsBeanType(string beanName)
        {
            BeanDef beanDef;
            return MainWindow.GetBeanDef(out beanDef, beanName);
        }
        public static bool IsArrayType(string type)
        {
            if (type == "vector")
            {
                return true;
            }
            return false;
        }
        public static bool IsInvalidValue(string type,string value)
        {
            if(type=="int"&&value!="n!u@l#l")
            {
                for(int i=0;i<value.Length;i++)
                {
                    if (value[i] == '-')
                        continue;
                    if(value[i]<'0'||value[i]>'9')
                    {
                        return false;
                    }
                }
            }
            else if(type=="float"&&value!="n!u@l#l")
            {
                for (int i = 0; i < value.Length; i++)
                {
                    if (value[i] == '-')
                        continue;
                    if (value[i] == '.')
                        continue;
                    if(value[i] < '0' || value[i] > '9')
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        public static bool IsInvalidName(string name)
        {
            return name == "n!u@l#l";
        }
        public static string ConverXmlName(string name)
        {
            string retName = "";
            for (int i = 0; i < name.Length; i++)
            {
                retName += name[i];
                if (name[i] == '_')
                    retName += name[i];
            }
            return retName;
        }
        public static string ConverXmlString(string text)
        {
            string xmlText = "";
            //if (text.Length > 2 && text[0] == '<')
            //{
                for (int i = 0; i < text.Length; i++)
                {
                    if (text[i] == '<')
                    {
                        xmlText += "&lt;";
                    }
                    else if (text[i] == '>')
                    {
                        xmlText += "&gt;";
                    }
                    else if (text[i] == '"')
                    {
                        xmlText += "&quot;";
                    }
                    else if (text[i] == '&')
                    {
                        xmlText += "&amp;";
                    }
                    else
                        xmlText += text[i];
                }
            //}

            return xmlText;
        }
        public static List<string> stovs(string src, char splitChar)
        {
            List<string> vs = new List<string>();
            string s = "";
            for (int i = 0; i < src.Length; i++)
            {
                if (src[i] == splitChar)
                {
                    vs.Add(s);
                    s = "";
                }
                else
                    s += src[i];
            }
            if (s.Length > 0)
                vs.Add(s);

            return vs;
        }

        public static bool GetAbsDataPath(out string absPathFileName)//获取绝对数据路径,配置的都是相对的.
        {
            absPathFileName = "";
            string strExecPath = System.IO.Directory.GetCurrentDirectory();
            List<string> vsExecPath = MainWindow.stovs(strExecPath, '\\');
            List<string> vsConfigPath = MainWindow.stovs(MainWindow.mSrcDataXlsPath, '\\');
            for (int i = 0; i < vsConfigPath.Count; i++)
            {
                if (vsConfigPath[i] == "..")
                {
                    vsConfigPath.RemoveAt(i);
                    int removeIndex = vsExecPath.Count - 1;
                    if (removeIndex > 0)
                    {
                        vsExecPath.RemoveAt(removeIndex);
                    }
                    else
                    {
                        MainWindow.Error("计算绝对路径失败，工作目录为" + strExecPath + ",配置目录为" + MainWindow.mSrcDataXlsPath);
                        return false;
                    }
                    i--;
                }
                else
                {
                    break;
                }
            }
            List<string> absPath = vsExecPath;
            for (int i = 0; i < vsConfigPath.Count; i++)
            {
                absPath.Add(vsConfigPath[i]);
            }
            absPathFileName = vstos(absPath, '\\');
            return true;
        }
        public static string vstos(List<string> vsParams, char splitChar)
        {
            string retString = "";
            for (int i = 0; i < vsParams.Count; i++)
            {
                retString += vsParams[i];
                if (i + 1 < vsParams.Count)
                {
                    retString += splitChar;
                }
            }
            return retString;
        }
        public static string ConverPath(string path)
        {
            string retString="";
            for(int i=0;i<path.Length;i++)
            {
                if (path[i] == '/')
                {
                    retString += '\\';
                }
                else
                    retString += path[i];
            }
            return retString;
        }
        private bool LoadAllData()
        {
            mDataMgr.Clear();
            int jj = 0;
            List<string> files = MainWindow.GetFiles(mSrcDataXlsPath, "*.xlsm");
            List<string> files1 = MainWindow.GetFiles(mSrcDataXlsPath, "*.xlsx");
            for (int i = 0; i < files.Count; i++)
            {
                if (!mDataMgr.loadXlsFile(files[i]))
                {
                    return false;
                }
                jj++;
                m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 5 + 90 * (jj * 1.0 / (files.Count + files1.Count) * 1.0); }));
            }
            for (int i = 0; i < files1.Count; i++)
            {
                if (!mDataMgr.loadXlsFile(files1[i]))
                {
                    return false;
                }
                jj++;
                m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 5 + 90 * (jj * 1.0 / (files.Count + files1.Count) * 1.0); }));
            }
           
            return true;
        }
        public static bool GetBeanDef(out BeanDef beanDef, string beanName)
        {
            return mDefineMgr.GetBeanFromBeanName(out beanDef, beanName);
        }
        public static bool GetXlsData(out XlsData xlsData,string fileName)
        {
            return mDataMgr.mXls.TryGetValue(fileName, out xlsData);
        }
        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            mOutputText.Items.Add("----------------------------------------------------------------------------");
            mOutputText.Items.Add(" 检验文件工具");
            mOutputText.Items.Add(" 1、特效检查需要配置，配置文件《ExcelFileCheck.ini》 ");
            mOutputText.Items.Add("    [EXlsName] 为表格名字 ");
            mOutputText.Items.Add("    [EColName] 为列名 ");
            mOutputText.Items.Add(" 2、音效检查在表格里搜索所有.mp3文件然后去资源路径下搜索");
            mOutputText.Items.Add("----------------------------------------------------------------------------");
        }
        private void Window_Unloaded(object sender, RoutedEventArgs e)
        {
            /*
            byte[] buffer1=Encoding.Default.GetBytes(mHostroyXls);
            byte[] buffer2=Encoding.Convert(Encoding.UTF8,Encoding.Default,buffer1,0,buffer1.Length);
            string strHostroy=Encoding.Default.GetString(buffer2,0,buffer2.Length);
            mOptionIni.FindAndAddApp("Hostroy", "Record").FindAndAddItem("Record").SetValue(strHostroy);
            mOptionIni.WriteToFile();
            */
            mOptionIni.Free();
        }




        private void ComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

        }
        private bool LoadData()
        {
            mDataManager.Clear();
            for (int i = 0; i < m_SrcDataXlsData.Count; i++)
            {
                string strXlsName = "";
                if (!m_SrcDataXlsData.TryGetValue(i, out strXlsName))
                {
                    Error("程序异常！");
                    return false;
                }
                strXlsName = mSrcDataXlsPath +"\\" + strXlsName;
                if (!mDataManager.loadXlsFile(strXlsName))
                {
                    return false;
                }
                m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 5 + 90 * (i * 1.0 / (m_SrcDataXlsData.Count + m_SrcDataXlsData.Count) * 1.0); }));
            }
            Info("表格加载成功！");
            return true;

        }
        //检查所有表格
        private bool CheckAllFile()
        {
            
            for (int i = 0; i < m_SrcDataXlsData.Count; i++)
            {
                if (!CheckFile(i))
                {
                    return false;
                }
            }
            return true;
        }
        //检查单个表格
        private bool CheckFile(int i)
        {
            //1 检查表格名称
            XlsData data;
            if (!CheckXleHas(i, out data))
            {
                return false;
            }

            //2 检查检查列名
            int col = 0;
            if (!CheckColHas(i, data, out col))
            {
                return false;
            }
            //3 遍历列查找文件是否存在
            if (!CheckAllRow(data, col))
            {
                Error("错误！！！有无效的数据");
                return false;
            }
            return true;
        }
        private bool CheckAllRow(XlsData data, int col)
        {
            for (int ii = data.colCount; ii < data.mData.Count; ii++)
            {
                if (ii % data.colCount == col)
                {
                    if (data.mData[ii] == "n!u@l#l" || data.mData[ii] == "")
                    {
                        continue;
                    }
                    else
                    {
                            string strFile = mSrcDataEffectPath + "\\" + data.mData[ii] + ".eff.inf";
                            if (!File.Exists(strFile))
                            {
                                Error(data.mFileName + "中<" + data.mData[col] + ">列," + "第" + (ii / data.colCount + 1) + "行,内容“" + data.mData[ii] + "”找不到文件");
                            }
                    }


                }
            }
            return true;
        }
        private bool CheckColHas(int i, XlsData data, out int col)
        {
            col = -1;
            string strXlsName = "";
            string strColName = "";
            if (m_SrcDataXlsData.TryGetValue(i, out strXlsName))
            {
                if (m_SrcDataColData.TryGetValue(i, out strColName))
                {
                    col = CheckFileFindIndex(strColName, data);
                    if (col == -1)
                    {
                        Error(strXlsName + "中没有列" + strColName);
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
            }
            return true;
        }
        private bool CheckXleHas(int i, out XlsData data)
        {
            string strXlsName = "";
            if (m_SrcDataXlsData.TryGetValue(i, out strXlsName))
            {
                if (mDataManager.mXls.TryGetValue("xlsdir\\" + strXlsName, out data))
                {
                    return true;
                }
            }
            Error(strXlsName +"不存在");
            data = null;
            return false;
        }
        private int CheckFileFindIndex(string strColName, XlsData data)
        {
            int nColIndex = -1;
            for (int j = 0; j < data.colCount; j++)
            {
                if (data.mData[j] == strColName)
                {
                    nColIndex = j;
                }
            }
            return nColIndex;
        }
        private void thread_CheckEffect()
        {
            if (LoadData() && CheckAllFile())
            {
                Info("检验结束！");
            }
            threadEnd();
        }
        private void thread_CheckMusic()
        {
            if (LoadAllData() && LoadAllMusic() && CheckAllMusic())
            {
                Info("检验结束!");
            }
            threadEnd();
        }
        private bool CheckAllMusic()
        {
            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr.mXls.Keys;
            foreach (string name in ids)
            {
                XlsData data;
                if (mDataMgr.mXls.TryGetValue(name, out data))
                {
                    if (!CheckOnXlsOfMusick(data))
                    {
                        return false;
                    }
                }
            }
            
            return true;
        }
        private bool CheckOnXlsOfMusick(XlsData data)
        {
            for (int ii = data.colCount; ii < data.mData.Count; ii++)
            {
                int indexMp3 = data.mData[ii].IndexOf(".mp3");
                if (data.mData[ii] == "n!u@l#l" || data.mData[ii] == "" || indexMp3 < 0)
                {
                    continue;
                }
                else
                {
                    bool blnHas = false;
                    for (int jj = 0; jj < mMusicFileNames.Count; jj++)
                    {
                        int nindex = mMusicFileNames[jj].IndexOf(data.mData[ii]);

                        if (nindex >= 0)
                        {
                            blnHas = true;
                            break;
                        }

                    }
                    if (!blnHas)
                    {
                        Error(data.mFileName + "中<" + data.mData[ii % data.colCount] + ">列," + "第" + (ii / data.colCount + 1) + "行,内容“" + data.mData[ii] + "”找不到文件");
                    }
                }


                
            }
            return true;
        }
        private bool LoadAllMusic()
        {
            mMusicFileNames = MainWindow.GetFiles(MainWindow.mSrcDataMusicPath, "*");
            if (mMusicFileNames.Count == 0)
            {
                Error("找不到音效文件！");
            }
            return true;
        }
        private void threadStart()
        {
            m_Button1.IsEnabled = false;
            m_ComBox.IsEnabled = false;
            m_pro.Visibility = Visibility.Visible;
        }
        private void threadEnd()
        {
            m_Button1.Dispatcher.BeginInvoke(new Action(() => { m_Button1.IsEnabled = true; }));
            m_ComBox.Dispatcher.BeginInvoke(new Action(() => { m_ComBox.IsEnabled = true; }));

            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Visibility = Visibility.Hidden; }));
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 0; }));

        }
        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            if (m_ComBox.SelectedIndex == 0)
            {
                Thread thread = new Thread(thread_CheckEffect);
                thread.Start();
            }
            else if (m_ComBox.SelectedIndex == 1)
            {
                Thread thread = new Thread(thread_CheckMusic);
                thread.Start();
            }
            
            
            threadStart();
        }

        private void ComboBox_SelectionChanged_1(object sender, SelectionChangedEventArgs e)
        {
            m_SrcDataXlsData.Clear();
            m_SrcDataColData.Clear();
        }
    }
}
