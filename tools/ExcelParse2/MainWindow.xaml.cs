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

namespace ExcelParse2
{
    class BeanDefInfo
    {
        public bool writeCompare;
        public BeanDef mBeanDef;
    }
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        //路径
        public static string mSrcDataXlsPath = "";//源xls路径
        public static string mSrcDataCsvPath = "";//源csv路径
        public static string mSrcDataTxtPath = "";//源csv路径
        public static string mSrcDataDefXmlPath = "";//源转义XML路径
        public static string mDstClientBinDataPath = "";//目标客户端Bin路径
        public static string mDstClientXmlDataPath = "";//目标客户端Xml路径
        public static bool   mManualConfig = false;//是否是手动配置.
        public static string mDstServerXmlDataPath = "";//目标服务器XML路径
        public static string mDstServerJavaPath = "";//目标服务器JAVA路径
        public static string mDstClientCppPath = "";//目标客户端C++路径
        public static string mDstClientLuaPath = "";//目标获取客户端LUA路径
        public static string mDstClientPkgPath = "";//目标获取客户端PKG路径
        public static string mHostroyBeans = "";//BEAN的缓存.
        public static string mAutoMake = "false";//自动生成.

        public static bool mExportSingleTable = false;
        private static bool mMakeError = false;

        public static int mSortListIndex = 0;
        public static int mSelectFileType = 0;



        //数据管理
        public static DefineManager mDefineMgr = new DefineManager();
        public static DataManager mDataMgr = new DataManager();

        //配置文件
        //public static ToolsIni mOptionIni= new ToolsIni(System.AppDomain.CurrentDomain.BaseDirectory+"/ConExcelParseOption2.ini");
        public static ToolsIni mOptionIni= new ToolsIni("ExcelParseOption2.ini");

        //实例化
        public static MainWindow mInstance = null;

        public Dictionary<int, int> mIdIndex = new Dictionary<int, int>();//key=ID,int=行索引 检查复合表使用

        //文件版本号.
        public static ushort mBinFileVersion = 101;

        //自动生成.
        private void AutoMake()
        {
            mMakeError = false;
            mExportSingleTable = false;
            if (LoadDataDef()
                && LoadAllData()
                && MakeClientData(true, true, true, true)
                && MakeServerData(true, true)
                && MakeClientCode()
                )
            {
                Info("一键数据和代码生成成功！");
            }

            // 生成日志文件.
            try
            {
                File.Delete("onekeymake.log");
                FileStream fs = File.Open("onekeymake.log", FileMode.CreateNew, FileAccess.Write);
                if (fs != null)
                {
                    StreamWriter sw = new StreamWriter(fs, Encoding.UTF8);
                    sw.AutoFlush = true;
                    if (sw != null)
                    {
                        for (int i = 0; i < getInstance().mOutputText.Items.Count; ++i)
                        {
                            sw.Write(getInstance().mOutputText.Items[i]);
                            sw.Write("\n");
                        }
                        sw.Close();
                    }
                    fs.Close();
                }
            }
            catch (System.Exception e2)
            {
                Error("创建文件 onekeymake.log 时失败！异常信息=" + e2.Message);
            }
            try
            {   
                //File.Delete("ConExcelParse2.err");
                if (mMakeError)
                {
//                    FileStream fs = File.Open("ConExcelParse2.err", FileMode.CreateNew, FileAccess.Write);
                    FileStream fs = File.Open("error.log", FileMode.CreateNew);
                    if (fs != null)
                    {
                        StreamWriter sw = new StreamWriter(fs, Encoding.ASCII);
                        sw.AutoFlush = true;
                        if (sw != null)
                        {
                            sw.Write("hello world!");
                        }
                        sw.Close();
                    }
                    fs.Close();
                }
            }
            catch (System.Exception)
            {
            }
        }
        public MainWindow()
        {
            InitializeComponent();
            GetConfigPath();
            mInstance = this;
            //if(mAutoMake=="true")
            //{
            //    if (m_btn11.IsChecked == true)
            //    {
            //        setProgramUse();
            //    }

            //    AutoMake();
            //    this.Close();
            //}
        }
        private void GetConfigPath()
        {
            mSrcDataXlsPath = mOptionIni.FindAndAddApp("SrcDataXlsPath", "Path").FindAndAddItem("Path").GetValue();
            mSrcDataCsvPath = mOptionIni.FindAndAddApp("SrcDataCsvPath", "Path").FindAndAddItem("Path").GetValue();
            mSrcDataTxtPath = mOptionIni.FindAndAddApp("SrcDataTxtPath", "Path").FindAndAddItem("Path").GetValue();
            mSrcDataDefXmlPath = mOptionIni.FindAndAddApp("SrcDataDefXmlPath", "Path").FindAndAddItem("Path").GetValue();
            mDstClientBinDataPath = mOptionIni.FindAndAddApp("DstClientBinDataPath", "Path").FindAndAddItem("Path").GetValue();
            mDstClientXmlDataPath = mOptionIni.FindAndAddApp("DstClientXmlDataPath", "Path").FindAndAddItem("Path").GetValue();
            mDstServerXmlDataPath = mOptionIni.FindAndAddApp("DstServerXmlDataPath", "Path").FindAndAddItem("Path").GetValue();
            if (mOptionIni.FindAndAddApp("DstServerXmlDataPath", "ManualConfig").FindAndAddItem("ManualConfig").GetValue() == "true")
                mManualConfig = true;
            mDstClientCppPath = mOptionIni.FindAndAddApp("DstClientCppPath", "Path").FindAndAddItem("Path").GetValue();
            mDstClientLuaPath = mOptionIni.FindAndAddApp("DstClientLuaPath", "Path").FindAndAddItem("Path").GetValue();
            mDstClientPkgPath = mOptionIni.FindAndAddApp("DstClientPkgPath", "Path").FindAndAddItem("Path").GetValue();
            mDstServerJavaPath = mOptionIni.FindAndAddApp("DstServerJavaPath", "Path").FindAndAddItem("Path").GetValue();
            mHostroyBeans = mOptionIni.FindAndAddApp("Hostroy", "Record").FindAndAddItem("Record").GetValue();
            mAutoMake = mOptionIni.FindAndAddApp("Config", "AutoMake").FindAndAddItem("AutoMake").GetValue();
            string programUse = mOptionIni.FindAndAddApp("Config", "CoderUse").FindAndAddItem("CoderUse").GetValue();
            string SelectFileType = mOptionIni.FindAndAddApp("Config", "SelectFileType").FindAndAddItem("SelectFileType").GetValue();
            string SelectSortType = mOptionIni.FindAndAddApp("Config", "SelectSortType").FindAndAddItem("SelectSortType").GetValue();
            mSortListIndex = int.Parse(SelectSortType);
            if(programUse=="true")
            {
                setProgramUse();
                m_btn11.IsChecked = true;
            }
            else
            {
                m_btn11.IsChecked = false;
            }
        }

        private bool LoadDataDef()
        {
            mDefineMgr.Clear();
            List<string> files=MainWindow.GetFiles(mSrcDataDefXmlPath,"*.xml");
            for(int i=0;i<files.Count;i++)
            {
                if(!mDefineMgr.loadDefFromFile(files[i]))
                {
                    return false;
                }
            }
            return true;
        }
        private void LoadDataDefBtn()
        {
            if (LoadDataDef())
            {
                Info("成功加载服务器Bean" + mDefineMgr.mServerDef.Count + "个,客户端Bean+" + mDefineMgr.mClientDef.Count + "个.");
            }
            OpenBtn();
        }
        private void OnClick_LoadDataDef(object sender, RoutedEventArgs e)
        {
            Thread thread = new Thread(new ThreadStart(LoadDataDefBtn));
            thread.Start();
            CloseBtn();

        }
        private bool LoadConfigData()
        {
            string strAbsDataPath;
            if (!GetAbsDataPath(out strAbsDataPath))
                return false;

            List<string> beanList = MainWindow.stovs(MainWindow.mHostroyBeans, ',');
            for (int i = 0; i < beanList.Count; i++)
            {
                string beanName = beanList[i];
                BeanDef beanDef;
                if(mDefineMgr.GetBeanFromBeanName(out beanDef,beanName))
                {
                    List<string> listDataFiles = stovs(beanDef.from, ',');
                    for(int j=0;j<listDataFiles.Count;j++)
                    {
                        string xlsFileName = listDataFiles[j];
                        string xlsFilePathName = strAbsDataPath + "\\"+ xlsFileName;
                        string strxlsFilePath = ConverPath(xlsFilePathName);
                        if (mSelectFileType == 0)
                        {
                            
                            if (ToolsFile.FileExists(strxlsFilePath + ".xlsx"))
                            {
                                if (!mDataMgr.loadXlsFile(strxlsFilePath +".xlsx"))
                                {
                                    return false;
                                }
                            }
                            else
                            {
                                if (!mDataMgr.loadXlsFile(strxlsFilePath +".xlsm"))
                                {
                                    return false;
                                }
                            }
                        }
                        else if (mSelectFileType == 1)
                        {
                            if (!mDataMgr.LoadCsvFile(strxlsFilePath+".csv"))
                            {
                                return false;
                            }
                        }
                        else if (mSelectFileType == 2)
                        {
                            if (!mDataMgr.LoadCsvFile(strxlsFilePath + ".txt"))
                            {
                                return false;
                            }
                        }

                    }
                }
                else
                {
                    Error("没有找到命名为" + beanName + "的BEAN.");
                    return false;
                }
            }
            return true;
        }
        private void LoadConfigDataBtn()
        {
            mExportSingleTable = true;
            if (LoadConfigData())
            {
                Info("成功加载" + mDataMgr.mXls.Count + "个xls.");
            }
            OpenBtn();
        }
        private void OnClick_LoadConfigData(object sender,RoutedEventArgs e)
        {
            Thread thread = new Thread(new ThreadStart(LoadConfigDataBtn));
            thread.Start();
            CloseBtn();

        }
        private bool LoadAllData()
        {
            switch (mSelectFileType)
            {
                case 0:
                    {
                        mDataMgr.Clear();
                        List<string> files = MainWindow.GetFiles(mSrcDataXlsPath, "*.xlsm");
                        List<string> files1 = MainWindow.GetFiles(mSrcDataXlsPath, "*.xlsx");
                        for (int i = 0; i < files.Count; i++)
                        {
                            if (!mDataMgr.loadXlsFile(files[i]))
                            {
                                return false;
                            }
                        }


                        for (int i = 0; i < files1.Count; i++)
                        {
                            if (!mDataMgr.loadXlsFile(files1[i]))
                            {
                                return false;
                            }
                        }
                        break;
                    }
                case 1:
                    {
                        mDataMgr.Clear();
                        List<string> files = MainWindow.GetFiles(mSrcDataCsvPath, "*.csv");
                        for (int ii = 0; ii < files.Count; ii++)
                        {
                            if (!mDataMgr.LoadCsvFile(files[ii]))
                            {
                                return false;
                            }
                        }
                        break;
                    }
                case 2:
                    {
                        mDataMgr.Clear();
                        List<string> files = MainWindow.GetFiles(mSrcDataTxtPath, "*.txt");
                        for (int ii = 0; ii < files.Count; ii++)
                        {
                            if (!mDataMgr.LoadTxtFile(files[ii]))
                            {
                                return false;
                            }
                        }
                        break;
                    }
            }
            return true;
        }
        private void LoadAllDataBtn()
        {
            mExportSingleTable = false;
            if (LoadAllData())
            {
                Info("正确加载全部数据,一共" + mDataMgr.mXls.Count + "个文件");
            }
            OpenBtn();
        }
        private void OnClick_LoadAllData(object sender, RoutedEventArgs e)
        {
            Thread thread = new Thread(new ThreadStart(LoadAllDataBtn));
            thread.Start();
            CloseBtn();
        }
        private bool MakeServerData(bool clearDir,bool makeAll)
        {
            if(clearDir)
            {
                ClearDirection(mDstServerXmlDataPath);
                Directory.CreateDirectory(mDstServerXmlDataPath);
            }

            for(int i=0;i<mDefineMgr.mServerDef.Count;i++)
            {
                mIdIndex.Clear();
                BeanDef beanDef = mDefineMgr.mServerDef[i];
                bool enableAllData=true;
                List<string> vsXlsFileName=stovs(beanDef.from,',');
                if (makeAll)
                {
                    for (int j = 0; j < vsXlsFileName.Count; j++)
                    {
                        XlsData xlsData;
                        string xlsFilePathName = MainWindow.ConverPath(vsXlsFileName[j]);
                        if (!mDataMgr.mXls.TryGetValue(xlsFilePathName, out xlsData))
                        {
                            Error("尝试获取BEAN " + beanDef.name + " 对应的文件" + xlsFilePathName + "失败！");
                            return false;
                        }
                    }
                }
                else
                {
                    bool enableData = true;
                    for (int j = 0; j < vsXlsFileName.Count; j++)
                    {
                        XlsData xlsData;
                        string xlsFilePathName = MainWindow.ConverPath(vsXlsFileName[j]);
                        if (!mDataMgr.mXls.TryGetValue(xlsFilePathName, out xlsData))
                        {
                            enableData = false;
                            break;
                        }
                    }
                    if (enableData == false)
                        continue;
                }
                if(enableAllData)
                {
                    string fullXmlFileName = MainWindow.mDstServerXmlDataPath + "\\fire.pb." + beanDef.spaceName + "." + beanDef.name + ".xml";
                    if (mExportSingleTable)//add by eagle
                        File.Delete(fullXmlFileName);

                    FileStream fs = File.Open(fullXmlFileName, FileMode.CreateNew);
                    if (fs != null)
                    {
                        StreamWriter bw = new StreamWriter(fs, Encoding.UTF8);
                        if (bw != null)
                        {
                            bw.Write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
                            bw.Write("<tree-map>\n");
                            bw.Write("  <no-comparator/>\n");
                            for (int j = 0; j < vsXlsFileName.Count; j++)
                            {
                                XlsData xlsData;
                                mDataMgr.mXls.TryGetValue(MainWindow.ConverPath(vsXlsFileName[j]), out xlsData);
                                ServerBeanData serverBean = new ServerBeanData();
                                if (serverBean.SetupDef(ref beanDef))
                                {
                                    if (!serverBean.WriteFile(ref bw, ref xlsData,beanDef.spaceName))
                                    {
                                        return false;
                                    }
                                }
                            }
                            
                            bw.Write("</tree-map>");
                            bw.Close();
                        }
                        fs.Close();
                    }
                }
            }
            return true;
        }
        private void MakeServerDataBtn()
        {
            if (mDefineMgr.mServerDef.Count == 0 && mDefineMgr.mClientDef.Count == 0)
            {
                Warning("无法生成，尚未加载任何数据定义！");
                OpenBtn();
                return;
            }
            if (mDataMgr.mXls.Count == 0)
            {
                Warning("无法生成，尚未加载任何数据！");
                OpenBtn();
                return;
            }
            if (MakeServerData(true, true))
            {
                Info("正确生成服务器数据!");
            }
            OpenBtn();

        }
        private void OnClick_MakeServerData(object sender, RoutedEventArgs e)
        {
            Thread thread = new Thread(new ThreadStart(MakeServerDataBtn));
            thread.Start();
            CloseBtn();
        }
        private bool WriteBinData(BinaryWriter bw, string type, string data)
        {
            try
            {
                if (type == "int")
                {
                    if (data.Length > 0)
                    {
                        int nPointPos = data.IndexOf(".");
                        if (nPointPos != -1)
                        {
                            data = data.Substring(0,nPointPos);
                        }

                        int intData = Convert.ToInt32(data);
                        bw.Write(intData);
                    }
                    else
                        bw.Write((int)0);
                }
                else if (type == "long")
                {
                    if (data.Length > 0)
                    {
                        long longData = Convert.ToInt64(data);
                        bw.Write(longData);
                    }
                    else
                        bw.Write((long)0);
                }
                else if (type == "double")
                {
                    if (data.Length > 0)
                    {
                        double doubleData = Convert.ToDouble(data);
                        bw.Write(doubleData);
                    }
                    else
                        bw.Write((double)0);
                }
                else if (type == "bool")
                {
                    if (data.Length > 0)
                    {
                        bool boolData = Convert.ToBoolean(data);
                        bw.Write(boolData);
                    }
                    else
                        bw.Write((int)0);
                }
                else if (type == "String" || type == "string")
                {
                    byte[] str = System.Text.UnicodeEncoding.UTF8.GetBytes(data);
                    bw.Write((str).Length);
                    bw.Write(str);
                }
                else
                {
                    Error("写二进制数据" + type + "=" + data + "时到文件，发现未知的数据说明！");
                    return false;
                }
            }
            catch (FormatException e)
            {
                Error("写二进制数据" + type + "=" + data + "时，发现数据格式错！异常信息=" + e.Message);
                return false;
            }
            return true;
        }
        private bool MakeClientData(bool bMakeXml,bool bMakeBin,bool clearDir,bool makeAll)//生成客户端数据.
        {
            if (clearDir)
            {
                ClearDirection(mDstClientBinDataPath);
                Directory.CreateDirectory(mDstClientBinDataPath);
                ClearDirection(mDstClientXmlDataPath);
                Directory.CreateDirectory(mDstClientXmlDataPath);
            }

            for (int i = 0; i < mDefineMgr.mClientDef.Count; i++)
            {
                mIdIndex.Clear();
                BeanDef beanDef = mDefineMgr.mClientDef[i];
                List<string> vsXlsFileName = stovs(beanDef.from, ',');

                if (makeAll)
                {
                    for (int j = 0; j < vsXlsFileName.Count; j++)
                    {
                        XlsData xlsData;
                        string xlsFilePathName = MainWindow.ConverPath(vsXlsFileName[j]);
                        if (!mDataMgr.mXls.TryGetValue(xlsFilePathName, out xlsData))
                        {
                            Error("没有找到BEAN(" + beanDef.name + ")对应的XLS文件" + xlsFilePathName);
                            return false;
                        }
                    }
                }
                else
                {
                    bool dataEnable = true;
                    for (int j = 0; j < vsXlsFileName.Count; j++)
                    {
                        XlsData xlsData;
                        string xlsFilePathName = MainWindow.ConverPath(vsXlsFileName[j]);
                        if (!mDataMgr.mXls.TryGetValue(xlsFilePathName, out xlsData))
                        {
                            dataEnable = false;
                            break;
                        }
                    }
                    if(dataEnable==false)
                    {
                        continue;
                    }
                }
                if (bMakeBin)
                {
                    //写二进制文件.
                    string binFileName = mDstClientBinDataPath + "\\" + beanDef.spaceName + "." + beanDef.name.ToLower() + ".bin";
                    if (mExportSingleTable && ToolsFile.FileExists(binFileName))//modify by eagle
                        File.Delete(binFileName);
                    FileStream fs = File.Open(binFileName, FileMode.CreateNew);
                    if (fs == null)
                    {
                        Error("创建文件" + binFileName + "失败！");
                        return false;
                    }
                    BinaryWriter bw = new BinaryWriter(fs);
                    if(bw==null)
                    {
                        fs.Close();
                        Error("创建文件" + binFileName + "写入器失败！");
                        return false;
                    }
                    bw.Write('L');
                    bw.Write('D');
                    bw.Write('Z');
                    bw.Write('Y');
                    uint fileLength = 0;//最后覆盖。
                    bw.Write(fileLength);
                    bw.Write(MainWindow.mBinFileVersion);
                    ushort memberCount = 0;//最后覆盖。
                    bw.Write(memberCount);
                    bw.Write(beanDef.GetColCheckNumber());
                    
                    for(int j=0;j<vsXlsFileName.Count;j++)
                    {
                        XlsData xlsData;
                        string xlsFilePathName = MainWindow.ConverPath(vsXlsFileName[j]);
                        mDataMgr.mXls.TryGetValue(xlsFilePathName, out xlsData);

                        string idTitleName;
                        if(!beanDef.GetIdColIndex(out idTitleName))
                        {
                            MainWindow.Error("表" + xlsData.GetFileName() + "中没有名字是id的列！");
                            return false;
                        }
                        
                        int idTitleIndex;
                        if (xlsData.mTitleIndex.TryGetValue(idTitleName, out idTitleIndex))
                        {
                            string idTitleString = xlsData.mData[idTitleIndex];
                            if (!xlsData.makeIdIndexs(idTitleString))
                            {
                                bw.Close();
                                fs.Close();
                                File.Delete(binFileName);
                                return false;
                            }
                        }
                        else
                        {
                            MainWindow.Error("表" + xlsData.GetFileName() + "中没有找到名字为" + idTitleName + "的列！");
                            return false;
                        }

                        Dictionary<int, int>.KeyCollection ids = xlsData.mIdIndex.Keys;
                        foreach (int id in ids)
                        {
                            for (int k = 0; k < beanDef.cols.Count; k++)
                            {
                                BeanCol beanCol=beanDef.cols[k];
                                string type = beanCol.type;
                                if (MainWindow.IsArrayType(type))
                                {
                                    type = beanCol.value;
                                }
                                List<string> vsData=new List<string>();
                                for(int m=0;m<beanCol.subCols.Count();m++)
                                {
                                    BeanSubCol subCol = beanCol.subCols[m];
                                    string title=subCol.name;
                                    string strData;
                                    if(xlsData.GetXlsData(out strData,title,id))
                                    {
                                        if (MainWindow.IsInvalidName(strData))
                                            strData = "";
                                        if (MainWindow.IsBaseType(type))
                                        {
                                            vsData.Add(strData);
                                        }
                                        else
                                        {
                                            bw.Close();
                                            fs.Close();
                                            File.Delete(binFileName);
                                            Error("写文件" + binFileName + "时发现非法数据类型"+type+"！");
                                            return false;
                                        }
                                    }
                                    else
                                    {
                                        Error("cuowu!");
                                        return false;
                                    }
                                }
                                int length = 0;
                                if (MainWindow.IsArrayType(beanCol.type))//写长度
                                {
                                    for (; length < vsData.Count(); length++)
                                    {
                                        if (vsData[length] == "")
                                            break;
                                    }
                                    bw.Write(length);
                                }
                                else
                                    length = 1;

                                for(int m=0;m<length;m++)//写数据.
                                {
                                    if (!WriteBinData(bw, type, vsData[m]))
                                    {
                                        Info("出错文件是"+binFileName+"的列"+beanCol.name);
                                        Info("出错bean名字是"+beanDef.name);
                                        Info("出错xls名字是"+beanDef.from);
                                        bw.Close();
                                        fs.Close();
                                        File.Delete(binFileName);
                                        return false;
                                    }
                                }
                            }
                            ++memberCount;
                        }
                    }

                    //改写文件长度
                    fileLength = (uint)bw.BaseStream.Length;
                    bw.Seek(4, 0);
                    bw.Write(fileLength);
                    bw.Write(MainWindow.mBinFileVersion);
                    bw.Write(memberCount);

                    //改写数组长度
                    bw.Close();
                    fs.Close();
                }
                //写XML文件.
                if(bMakeXml)
                {
                    string xmlFileName = mDstClientXmlDataPath + "\\" + beanDef.spaceName + "." + beanDef.name + ".xml";
                    if (mExportSingleTable && ToolsFile.FileExists(xmlFileName))//modify by eagle
                        File.Delete(xmlFileName);
                    FileStream fs = File.Open(xmlFileName, FileMode.OpenOrCreate);
                    if(fs!=null)
                    {
                        StreamWriter bw = new StreamWriter(fs, Encoding.Unicode);
                        if(bw!=null)
                        {
                            bw.Write("<root>\n");
                            for (int j = 0; j < vsXlsFileName.Count; j++)
                            {
                                XlsData xlsData;
                                string xlsFilePathName = MainWindow.ConverPath(vsXlsFileName[j]);
                                mDataMgr.mXls.TryGetValue(xlsFilePathName, out xlsData);
                                Dictionary<int, int>.KeyCollection ids = xlsData.mIdIndex.Keys;
                                foreach (int id in ids)
                                {
                                    //写属性
                                    bw.Write("<record ");
                                    for(int k=0;k<beanDef.cols.Count;k++)
                                    {
                                        BeanCol beanCol=beanDef.cols[k];
                                        if(MainWindow.IsBaseType(beanCol.type))
                                        {
                                            string strData;
                                            if(!xlsData.GetXlsData(out strData,beanCol.fromCol,id))
                                            {
                                                return false;
                                            }
                                            if (strData == "")
                                                continue;
                                            if(MainWindow.IsInvalidName(strData))
                                                continue;
                                            strData = ConverXmlString(strData);
                                            bw.Write(beanCol.name + "=\"" + strData + "\" ");
                                        }
                                    }
                                    string arraysText = "";
                                    //写数组
                                    for(int k=0;k<beanDef.cols.Count;k++)
                                    {
                                        bool enableWriteArray = false;
                                        string arrayText = "";

                                        BeanCol beanCol = beanDef.cols[k];
                                        if(MainWindow.IsArrayType(beanCol.type))
                                        {
                                            arrayText += "      <" + beanCol.name + ">\n";
                                            for (int m = 0; m < beanCol.subCols.Count;m++ )
                                            {
                                                BeanSubCol subCol = beanCol.subCols[m];
                                                string strData;
                                                if(!xlsData.GetXlsData(out strData,subCol.name,id))
                                                    return false;
                                                if(strData=="")
                                                    break;
                                                if(MainWindow.IsInvalidName(strData))
                                                    continue;
                                                strData = ConverXmlString(strData);
                                                string type=beanCol.value;
                                                if (beanCol.value == "String")
                                                    type = "string";
                                                else if (beanCol.value == "int")
                                                {
                                                    if (strData.Length > 0)
                                                    {
                                                        int nPointPos = strData.IndexOf(".");
                                                        if (nPointPos != -1)
                                                        {
                                                            strData = strData.Substring(0, nPointPos);
                                                        }
                                                    }
                                                }
                                                arrayText += "        <" + type + ">" + strData + "</" + type + ">\n";
                                                enableWriteArray = true;
                                            }
                                            arrayText += "      </" + beanCol.name + ">\n";
                                        }
                                        if (enableWriteArray)
                                            arraysText += arrayText;
                                    }
                                    if(arraysText.Length>0)
                                    {
                                        bw.Write(">\n");
                                        bw.Write(arraysText);
                                        bw.Write("</record>\n");
                                    }
                                    else
                                    {
                                        bw.Write("/>\n");
                                    }
                                }
                            }

                            bw.Write("</root>");
                            bw.Close();
                        }
                        fs.Close();
                    }
                }
            }
            return true;
        }
        private void MakeClientDataBtn()
        {
            if (mDefineMgr.mServerDef.Count == 0 && mDefineMgr.mClientDef.Count == 0)
            {
                Warning("无法生成，尚未加载任何数据定义！");
                OpenBtn();
                return;
            }
            if (mDataMgr.mXls.Count == 0)
            {
                Warning("无法生成，尚未加载任何数据！");
                OpenBtn();
                return;
            }
            if (MakeClientData(true, true, true, true))
            {
                Info("成功生成客户端数据");
            }
            OpenBtn();
        }
        private void OnClick_MakeClientData(object sender, RoutedEventArgs e)
        {
            Thread thread = new Thread(new ThreadStart(MakeClientDataBtn));
            thread.Start();
            CloseBtn();

        }
        private bool ClearDirection(string path)//清除目录及所有文件.
        {
            try
            {
                string[] dirs = Directory.GetDirectories(path);
                for (int i = 0; i < dirs.Length; i++)
                {
                    ClearDirection(dirs[i]);
                }
                string[] files = Directory.GetFiles(path, "*");
                for (int i = 0; i < files.Length; i++)
                {
                    try
                    {
                        File.Delete(files[i]);
                    }
                    catch (System.Exception e)
                    {
                        MainWindow.Error("删除文件" + files[i] + "失败，异常信息=" + e.Message);
                        return false;
                    }
                }
                try
                {
                    Directory.Delete(path);
                }
                catch (System.Exception e)
                {
                    MainWindow.Error("删除目录" + path + "失败，异常信息=" + e.Message);
                    return false;
                }
            }
            catch(DirectoryNotFoundException e)
            {
                MainWindow.Warning("没有找到指定的目录"+path+"，可能已经被删除，或者配置文件出错！异常信息="+e.Message);
                return false;
            }
            return true;
        } 
        private string GetCPPType(string type)
        {
            if (type == "bool")
                return "bool";
            else if (type == "int")
                return "int";
            else if (type == "long")
                return "long long";
            else if (type == "double")
                return "double";
            else if (type == "String" || type == "string")
                return "std::wstring";
            return type;
        }
        private string GetJAVAType(string type,string value)
        {
            if (type == "bool")
                return "boolean";
            if (type == "int")
                return "int";
            if (type == "String" || type == "string")
                return "String";
            if(type=="vector")
            {
                if (value == "int")
                    return "java.util.ArrayList<Integer>";
                else if (value == "long")
                    return "java.util.ArrayList<Long>";
                else if (value == "double")
                    return "java.util.ArrayList<Double>";
                else
                    return "java.util.ArrayList<" + GetJAVAType(value, "") + ">";
            }
            if (type == "double")
                return "double";
            if (type == "long")
                return "long";
            return type;
        }
        private string GetJAVADefault(string type)
        {
            if (type == "bool")
                return " false";
            if (type == "int")
                return " 0";
            if (type == "String" || type == "string")
                return " null";
            if (type == "vector")
                return "";
            if (type == "double")
                return " 0.0";
            if (type == "long")
                return " 0L";
            return " 0";
        }
        private bool MakeClientManagerCode()
        {
           

            // 写头文件.
            string filePathName = mDstClientCppPath + "\\TableDataManager.h";
            File.Delete(filePathName);
            FileStream fs = File.Open(filePathName, FileMode.OpenOrCreate);

            if (fs != null)
            {
                StreamWriter bw = new StreamWriter(fs);
                bw.AutoFlush = true;
                if (bw!=null)
                {
                    String tableSpace;
                    bw.Write("#pragma once\n");
                    bw.Write("#include <vector>\n");

                    bw.Write("struct TableDataFileHeader\n");
                    bw.Write("{\n");
                    bw.Write("    unsigned int type;//'LDZY'.\n");
                    bw.Write("    unsigned int size;//file size.\n");
                    bw.Write("    unsigned short version;//file version.\n");
                    bw.Write("    unsigned short itemCount;//data item count.\n");
                    bw.Write("    unsigned int colCheckNumber;//col change check.\n");
                    bw.Write("};\n");

                    for (int i = 0; i < mDefineMgr.mClientDef.Count(); i++)
                    {
                        BeanDef beanDef = mDefineMgr.mClientDef[i];
                        if(beanDef.makeLua)
                        {
                            continue;
                        }
                        string fileName = beanDef.spaceName + "/" + beanDef.name + ".h";
                        bw.Write("#include \"" + fileName + "\"\n");
                    }

                    bw.Write("#include \"TableBase.h\"\n");

                    bw.Write("class TableDataManager\n");
                    bw.Write("{\n");
                    tableSpace = "    ";
                    bw.Write(tableSpace + "public:\n");
                    tableSpace = "        ";
                    bw.Write(tableSpace + "TableDataManager(){};\n");
                    bw.Write(tableSpace + "~TableDataManager(){};\n");
                    bw.Write(tableSpace + "static TableDataManager& instance()\n");
                    bw.Write(tableSpace + "{\n");
                    tableSpace = "            ";
                    bw.Write(tableSpace + "static TableDataManager dm;\n");
                    bw.Write(tableSpace + "return dm;\n");
                    tableSpace = "        ";
                    bw.Write(tableSpace + "}\n");
                    tableSpace = "        ";
                    bw.Write(tableSpace + "bool loadAllTable(std::wstring& pathFileName,const std::wstring& path);\n");
                    bw.Write(tableSpace + "void releaseAllTable();\n");

                    bw.Write(tableSpace + "void CheckCorrectTableName(std::wstring& strFileNamePath);;\n");
                    bw.Write(tableSpace + "std::wstring GetCorrectTableName(){ return mStrCorrectPath; }\n");
                    bw.Write(tableSpace + "void SetCorrectTableName(std::wstring& strFileNamePath){ mStrCorrectPath = strFileNamePath; }\n");
                    bw.Write(tableSpace + "void InsertTable(std::wstring& strFileNamePath, TableBase* pTable); \n");

                    tableSpace = "    ";
                    bw.Write(tableSpace + "private:\n");
                    tableSpace = "        ";
                    bw.Write(tableSpace + "std::vector<void*> mDatas;\n");
                    bw.Write(tableSpace + "std::wstring mStrCorrectPath;\n");
                    bw.Write(tableSpace + "std::map< std::wstring, TableBase* > mMapTable;\n");
                    bw.Write("};\n");
                    bw.Close();
                }
                fs.Close();
            }

            string filePathNameTableBase = mDstClientCppPath + "\\TableBase.h";
            File.Delete(filePathNameTableBase);
            fs = File.Open(filePathNameTableBase, FileMode.OpenOrCreate);

            if (fs != null)
            {
                StreamWriter bw = new StreamWriter(fs);
                bw.AutoFlush = true;
                if (bw != null)
                {
                    String tableSpace;
                    bw.Write("#ifndef __TableBase__h__\n");
                    bw.Write("#define __TableBase__h__\n");

                    bw.Write("class TableBase\n");
                    bw.Write("{ \n");
                    tableSpace = " ";
                    bw.Write(tableSpace+"public:\n");
                    tableSpace = "      ";
                    bw.Write(tableSpace+"TableBase(){};\n");
                    bw.Write(tableSpace+"~TableBase(){};\n");
                    bw.Write(tableSpace+"virtual void  ReleaseData(){};\n");
                    bw.Write("}; \n");
                    bw.Write("#endif \n");
                    bw.Close();
                }
                fs.Close();
            }

            //写CPP文件.
            string mainCppPathName = mDstClientCppPath+"\\TableDataManager.cpp";
            if (ToolsFile.FileExists(mainCppPathName))
                File.Delete(mainCppPathName);

            fs = File.Open(mainCppPathName, FileMode.OpenOrCreate);
            if (fs != null)
            {
                StreamWriter bw = new StreamWriter(fs);
                bw.AutoFlush = true;
                if(bw!=null)
                {
                    bw.Write("#include \"TableDataManager.h\"\n");
                    bw.Write("#include \"utils/StringUtil.h\"\n");
                    bw.Write("#include \"CCLuaEngine.h\"\n");
                   

                    // 全部的单件实现.
                    bw.Write("namespace GameTable\n");
                    bw.Write("{\n");
                    for (int i = 0; i < mDefineMgr.mClientDef.Count;i++ )
                    {
                        BeanDef beanDef = mDefineMgr.mClientDef[i];
                        if(beanDef.makeLua)
                        {
                            continue;
                        }
                        bw.Write("  namespace " + beanDef.spaceName);
                        bw.Write("  {\n");
                        string className = beanDef.name + "Table";
                        //获取实例函数.
                        bw.Write("    " + className + "&  Get" + className + "Instance()\n");
                        bw.Write("    {\n");
                        bw.Write("        static " + className + " object;\n");
                        bw.Write("        if(!object.isLoaded())\n");
                        bw.Write("        {\n");
                        bw.Write("               std::wstring strFileNamePath = object.GetBinFileName();\n");
                        bw.Write("               TableDataManager::instance().InsertTable(strFileNamePath, &object);\n");
                        
                        bw.Write("               TableDataManager::instance().CheckCorrectTableName(strFileNamePath);\n");
                        bw.Write("               std::wstring pathFileName = TableDataManager::instance().GetCorrectTableName();\n");
                        bw.Write("               object.SetupData(pathFileName);\n");
                        bw.Write("         }\n");
                        bw.Write("        return object;\n");
                        bw.Write("    }\n");
                        //安装数据函数.
                        bw.Write("    bool " + beanDef.name + "Table::SetupData(const std::wstring& pathFileName)\n");
                        bw.Write("    {\n");
                        bw.Write("        LJFM::LJFMF file;\n");
                        bw.Write("        if (!file.Open(pathFileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))\n");
                        bw.Write("            return false;\n");
                        bw.Write("        unsigned int type;\n");
                        bw.Write("        file.Read(&type,sizeof(type));\n");
                        bw.Write("        unsigned int size;\n");
                        bw.Write("        file.Read(&size,sizeof(size));\n");
                        bw.Write("        unsigned short version;\n");
                        bw.Write("        file.Read(&version,sizeof(version));\n");
                        bw.Write("        if(version!=" + mBinFileVersion + ")\n");
                        bw.Write("        {\n");
                        bw.Write("            file.Close();\n");
                        bw.Write("            return false;\n");
                        bw.Write("        }\n");
                        bw.Write("        unsigned short itemCount;\n");
                        bw.Write("        file.Read(&itemCount,sizeof(version));\n");
                        bw.Write("        unsigned int colCheckNumber;\n");
                        bw.Write("        file.Read(&colCheckNumber,sizeof(colCheckNumber));\n");
                        bw.Write("        if(colCheckNumber!=" + beanDef.GetColCheckNumber() + ")\n");
                        bw.Write("        {\n");
                        bw.Write("            file.Close();\n");
                        bw.Write("            return false;\n");
                        bw.Write("        }\n");
                        bw.Write("        for(size_t i=0;i<itemCount;i++)\n");
                        bw.Write("        {\n");
                        bw.Write("            " + beanDef.name + "* object=new " + beanDef.name + ";\n");
                        bw.Write("            int j=0;\n");
                        string s="            ";
                        for (int j = 0; j < beanDef.cols.Count; j++)
                        {
                            BeanCol beanCol = beanDef.cols[j];
                            string type = beanCol.type;
                            if (MainWindow.IsArrayType(type))
                            {
                                type = beanCol.value;
                                bw.Write(s + "int " + beanCol.name + "Length;\n");
                                bw.Write(s + "file.Read(&" + beanCol.name + "Length,sizeof(int));\n");
                                bw.Write(s + "for(j=0;j<" + beanCol.name + "Length;j++)\n");
                                bw.Write(s + "{\n");
                                bw.Write(s + "    " + GetCPPType(type) + " " + beanCol.name + "Data;\n");
                                if (type == "string" || type == "String")
                                    bw.Write(s + "    " + beanCol.name + "Data = ReadString(file);\n");
                                else if (IsBaseType(type))
                                    bw.Write(s + "    file.Read(&" + beanCol.name + "Data,sizeof(" + GetCPPType(type) + "));\n");
                                else if (type == "long long")
                                {
                                    Warning("SetupData 数据类型long long将被截断! bean=" + beanDef.name + " value name=" + beanCol.name);
                                    bw.Write(s + "    file.Read(&" + beanCol.name + "Data,sizeof(int));\n");
                                }
                                else
                                {
                                    Error("意外的数据类型" + type);
                                    return false;
                                }
                                bw.Write(s + "    object->" + beanCol.name + ".push_back(" + beanCol.name + "Data);\n");
                                bw.Write(s + "}\n");
                            }
                            else
                            {
                                if (type == "String" || type == "string")
                                {
                                    bw.Write(s + "object->" + beanCol.name + "=ReadString(file);\n");
                                }
                                else if (MainWindow.IsBaseType(type))
                                {
                                    bw.Write(s + "file.Read(&object->" + beanCol.name + ",sizeof(object->" + beanCol.name + "));\n");
                                }
                            }
                        }
                        bw.Write("            mDatas.insert(std::pair<int," + beanDef.name + "*>(object->id,object));\n");
                             s = "            ";
                        if (beanDef.isHaveNameValue())
                            bw.Write(s + "mNames.insert(std::pair<const std::wstring," + beanDef.name + "*>(object->name,object));\n");
                        if (beanDef.isHaveColourValue())
                            bw.Write(s + "mColours.insert(std::pair<const std::wstring," + beanDef.name + "*>(object->colour,object));\n");
                        if (beanDef.isHaveMapidValue())
                            bw.Write(s + "mMapids.insert(std::pair<const std::wstring," + beanDef.name + "*>(object->mapid,object));\n");
                        bw.Write("        }\n");
                        bw.Write("        file.Close();\n");
                        bw.Write("        return true;\n");
                        bw.Write("    }\n");
                        /*
                        // 释放内存函数.
                        bw.Write("    void  "+ beanDef.name + "Table::ReleaseData()\n");
                        bw.Write("    {\n");
                        bw.Write("        mDatas.clear();\n");
                        if (beanDef.isHaveNameValue())
                            bw.Write(s + "mNames.clear();\n");
                        if (beanDef.isHaveColourValue())
                            bw.Write(s + "mColours.clear();\n");
                        if (beanDef.isHaveMapidValue())
                            bw.Write(s + "mMapids.clear();\n");
                        bw.Write("    }\n");
                        */
                        bw.Write("  }\n");
                    }
                    bw.Write("}\n");

                    bw.Write("void TableDataManager::CheckCorrectTableName(std::wstring& strFileNamePath){\n");
                    bw.Write("      mStrCorrectPath = strFileNamePath;\n");
                    bw.Write("      cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData(\"g_CheckCorrectTableName\", ws2s(strFileNamePath).c_str());\n");
                    bw.Write("}\n");


                    bw.Write("void TableDataManager::releaseAllTable()\n");
                    bw.Write("{ \n");
                    bw.Write("   std::map< std::wstring, TableBase* >::iterator it =  mMapTable.begin(); \n");
                    bw.Write("   while (it != mMapTable.end()) \n");
                    bw.Write("  { \n");
                    bw.Write("      TableBase* pTable = it->second;\n");
                    bw.Write("      ++it;\n");
                    bw.Write("      if (pTable) \n");
                    bw.Write("      { \n");
                    bw.Write("          pTable->ReleaseData();\n");
                    bw.Write("      } \n");

                    bw.Write("  }\n");
                    bw.Write("  mMapTable.clear();\n");
                    bw.Write("}\n");

                    bw.Write("void TableDataManager::InsertTable(std::wstring& strFileNamePath, TableBase* pTable)\n");
                    bw.Write("{ \n");
                    bw.Write("      mMapTable.insert(std::make_pair(strFileNamePath, pTable)); \n");
                    bw.Write("}\n");


                    bw.Write("bool TableDataManager::loadAllTable(std::wstring& pathFileName,const std::wstring& path){\n");
                    bw.Write("    if(sizeof(unsigned int)!=4)\n        return false;\n");
                    bw.Write("    if(sizeof(int)!=4)\n        return false;\n");
                    bw.Write("    if(sizeof(long long)!=8)\n        return false;\n");
                    bw.Write("    if(sizeof(double)!=8)\n        return false;\n");

                    
                    //bw.Write("    if(sizeof(char*)!=4)\n        return false;\n");//shield by eagle(不需要一个指针类型作为判断依据)
                    /*
                    tableSpace = "    ";
                    //bw.Write(tableSpace + "FILE* fp;\n");
                    bw.Write(tableSpace + "\n");
                    bw.Write(tableSpace + "TableDataFileHeader header;\n");
                    bw.Write(tableSpace + "std::wstring strTemplateName = L\"/table/bintable/\";\n");

                    for (int i = 0; i < mDefineMgr.mClientDef.Count; i++)
                    {
                        BeanDef beanDef = mDefineMgr.mClientDef[i];
                        if(beanDef.makeLua)
                        {
                            continue;
                        }
                        bw.Write(tableSpace + "fire::gsp::" + beanDef.spaceName + "::" + beanDef.name + "Table& " + beanDef.name + "Instance= fire::gsp::" + beanDef.spaceName + "::Get" + beanDef.name + "TableInstance();\n");
                        bw.Write(tableSpace + "pathFileName = strTemplateName + " + beanDef.name + "Instance.GetBinFileName();\n");
                        bw.Write(tableSpace + "if(!" + beanDef.name + "Instance.SetupData(pathFileName))\n");
                        bw.Write(tableSpace + "    return false;\n");
                    }
                    */
                    bw.Write("    return true;\n");
                    /*
                    bw.Write("}\n\n");

                    bw.Write("void TableDataManager::releaseAllTable()\n");
                    bw.Write("{\n");
                    bw.Write("    for(size_t i=0;i<mDatas.size();i++)\n");
                    bw.Write("    {\n");
                    bw.Write("        free(mDatas[i]);\n");
                    bw.Write("    }\n");
                    bw.Write("    mDatas.clear();\n");
                    */
                    bw.Write("}\n");
                    bw.Close();
                }
                fs.Close();
            }

            //写PKG.
            string mainPkgPathName = mDstClientPkgPath + "\\TableDataManager.pkg";
            if (ToolsFile.FileExists(mainPkgPathName))
                File.Delete(mainPkgPathName);

            fs = File.Open(mainPkgPathName, FileMode.OpenOrCreate);
            if (fs != null)
            {
                StreamWriter bw = new StreamWriter(fs);
                if (bw != null)
                {
                    bw = new StreamWriter(fs);
                    if (fs != null)
                    {
                        bw.Write("$#include \"TableDataManager.h\"\n");
                        for (int i = 0; i < mDefineMgr.mClientDef.Count;i++ )
                        {
                            BeanDef beanDef = mDefineMgr.mClientDef[i];
                            if (beanDef.makeLua==false)
                            {
                                string fileName = "GameTable/" + beanDef.spaceName + "/" + beanDef.name + ".pkg";
                                bw.Write("$pfile \"" + fileName + "\"\n");
                            }
                        }
                        bw.Write("class TableDataManager \n");
                        bw.Write("{ \n");
                        bw.Write("public: \n");
                        bw.Write("  TableDataManager(){}; \n");
                        bw.Write("  ~TableDataManager(){}; \n");
                        bw.Write("  static TableDataManager& instance(); \n");

                        bw.Write("void releaseAllTable(); \n");
                        bw.Write("void CheckCorrectTableName(std::wstring& strFileNamePath); \n");
                        bw.Write("std::wstring GetCorrectTableName(); \n");
                        bw.Write("void SetCorrectTableName(std::wstring& strFileNamePath); \n");
                        bw.Write("void InsertTable(std::wstring& strFileNamePath, TableBase* pTable); \n");

                        bw.Write("}; \n");
                    }
                    bw.Close();
                }
                fs.Close();
            }

            return true;
        }
        private bool MakeClientCppCode()
        {
            ClearDirection(mDstClientCppPath);
            Directory.CreateDirectory(mDstClientCppPath);

            //生成新的文件体系
            for (int i = 0; i < mDefineMgr.mClientDef.Count(); i++)
            {
                BeanDef beanDef = mDefineMgr.mClientDef[i];
                if (beanDef.makeLua == true)
                {
                    continue;
                }
                string cppPath = mDstClientCppPath;
                Directory.CreateDirectory(cppPath);
                cppPath += "\\" + beanDef.spaceName;
                Directory.CreateDirectory(cppPath);
                cppPath += "\\" + beanDef.name + ".h";

                try
                {
                    FileStream fs = File.Open(cppPath, FileMode.CreateNew, FileAccess.Write);
                    if (fs != null)
                    {
                        StreamWriter sw = new StreamWriter(fs);
                        sw.AutoFlush = true;
                        if (sw != null) 
                        {
                            //bw.Write("#pragma once\n");
                            sw.Write("#ifndef __" + beanDef.name + "__h__\n");
                            sw.Write("#define __" + beanDef.name + "__h__\n");
                            sw.Write("#include <string>\n");
                            sw.Write("#include <vector>\n");
                            sw.Write("#include <map>\n");
                            sw.Write("#include \"ljfmfex.h\"\n");
                            sw.Write("#include \"TableBase.h\"\n");
                            sw.Write("std::wstring s2ws(const std::string& s);\n");
                            sw.Write("\n");
                            sw.Write("namespace GameTable\n");
                            sw.Write("{\n");
                            sw.Write("    namespace " + beanDef.spaceName + "\n");
                            sw.Write("    {\n");
                            //数据的接口类
                            sw.Write("        class " + beanDef.name + "\n");
                            sw.Write("        {\n");
                            sw.Write("            public:\n");
                            string s = "            ";
                            for (int j = 0; j < beanDef.cols.Count; j++)
                            {
                                BeanCol beanCol = beanDef.cols[j];
                                if (MainWindow.IsArrayType(beanCol.type))
                                {
                                    sw.Write(s + " std::vector<" + GetCPPType(beanCol.value) + "> " + beanCol.name + ";\n");
                                }
                                else if(IsBaseType(beanCol.type))
                                    sw.Write(s + GetCPPType(beanCol.type) + " " + beanCol.name + ";\n");
                                else if(beanCol.type=="long long")
                                    sw.Write(s+"int "+beanCol.name+";\n");
                            }
                            sw.Write("        };\n");
                            //表类
                            sw.Write("        class " + beanDef.name + "Table  :public  TableBase \n");
                            sw.Write("        {\n");
                            sw.Write("        public:\n");
                            sw.Write("          " + beanDef.name + "Table(){ mErrorData.id=-1; }\n");
                            sw.Write("          virtual ~" + beanDef.name + "Table()\n");
                            sw.Write("          {\n");
                            sw.Write("              std::map<int," + beanDef.name + "*>::iterator it=mDatas.begin();\n");
                            sw.Write("              while(it!=mDatas.end())\n");
                            sw.Write("              {\n");
                            sw.Write("                  delete it->second;\n");
                            sw.Write("                  ++it;\n");
                            sw.Write("              }\n");
                            sw.Write("          }\n");
                            sw.Write("        private:\n");
                            sw.Write("        std::wstring ReadString(LJFM::LJFMF& file)\n");
                            sw.Write("        {\n");
                            sw.Write("            int stringLength;\n");
                            sw.Write("            file.Read(&stringLength,sizeof(int));\n");
                            sw.Write("            char* buf=(char*)malloc(stringLength+1);\n");
                            sw.Write("            file.Read(buf,stringLength);\n");
                            sw.Write("            buf[stringLength]=0;\n");
                            sw.Write("            std::wstring r=s2ws(buf);\n");
                            sw.Write("            free(buf);\n");
                            sw.Write("            return r;\n");
                            sw.Write("        }\n");
                            sw.Write("        public:\n");//安装数据.
                            sw.Write("            bool SetupData(const std::wstring& pathFileName);\n");
                            sw.Write("            virtual void ReleaseData()\n");
                            sw.Write("          {\n");
                            sw.Write("              std::map<int," + beanDef.name + "*>::iterator it=mDatas.begin();\n");
                            sw.Write("              while(it!=mDatas.end())\n");
                            sw.Write("              {\n");
                            sw.Write("                  delete it->second;\n");
                            sw.Write("                  ++it;\n");
                            sw.Write("              }\n");
                            sw.Write("              mDatas.clear();\n"); 
                            sw.Write("          }\n");

                            sw.Write("            bool isLoaded(){ return !!mDatas.size(); }\n");

                            sw.Write("        public:\n");//根据ID获取记录.
                            sw.Write("            const " + beanDef.name + "& getRecorder(int id)\n");
                            sw.Write("            {\n");
                            sw.Write("                std::map<int," + beanDef.name + "*>::iterator it=mDatas.find(id);\n");
                            sw.Write("                if(it!=mDatas.end())\n");
                            sw.Write("                    return *it->second;\n");
                            sw.Write("                return mErrorData;\n");
                            sw.Write("            }\n");
                                 s = "            ";
                           
                            if(beanDef.isHaveNameValue())
                            {
                                sw.Write(s + "const " + beanDef.name + "& getRecorderByName(const std::wstring& name)\n");
                                sw.Write(s + "{\n");
                                sw.Write(s + "    std::map<const std::wstring," + beanDef.name + "*>::iterator it=mNames.find(name);\n");
                                sw.Write(s + "    if(it!=mNames.end())\n");
                                sw.Write(s + "        return *it->second;\n");
                                sw.Write(s + "    return mErrorData;\n");
                                sw.Write(s + "}\n");
                            }
                            if(beanDef.isHaveColourValue())
                            {
                                sw.Write(s + "const " + beanDef.name + "& getRecorderByColour(const std::wstring& colour)\n");
                                sw.Write(s + "{\n");
                                sw.Write(s + "    std::map<const std::wstring," + beanDef.name + "*>::iterator it=mColours.find(colour);\n");
                                sw.Write(s + "    if(it!=mColours.end())\n");
                                sw.Write(s + "        return *it->second;\n");
                                sw.Write(s + "    return mErrorData;\n");
                                sw.Write(s + "}\n");
                            }
                            if(beanDef.isHaveMapidValue())
                            {
                                sw.Write(s + "const " + beanDef.name + "& getRecorderByMapid(const std::wstring& mapid)\n");
                                sw.Write(s + "{\n");
                                sw.Write(s + "    std::map<const std::wstring," + beanDef.name + "*>::iterator it=mMapids.find(mapid);\n");
                                sw.Write(s + "    if(it!=mMapids.end())\n");
                                sw.Write(s + "        return *it->second;\n");
                                sw.Write(s + "    return mErrorData;\n");
                                sw.Write(s + "}\n");
                            }
                            sw.Write("            void getAllID(std::vector<int>& recordList)\n");
                            sw.Write("            {\n");
                            sw.Write("                std::map<int," + beanDef.name + "*>::iterator it=mDatas.begin();\n");
                            sw.Write("                while(it!=mDatas.end())\n");
                            sw.Write("                {\n");
                            sw.Write("                    recordList.push_back(it->first);\n");
                            sw.Write("                    ++it;\n");
                            sw.Write("                }\n");
                            sw.Write("            }\n");
                            sw.Write("            virtual unsigned int getSize() const{ return mDatas.size(); }\n");

                            
                            sw.Write("            const wchar_t* GetBinFileName(){ return L\"/table/bintable/" + beanDef.spaceName + "." + beanDef.name.ToLower() + ".bin\"; }\n");

                            sw.Write("        private:\n");
                            sw.Write("            std::map<int," + beanDef.name + "*> mDatas;\n");
                            if (beanDef.isHaveNameValue())
                                sw.Write(s + "std::map<const std::wstring," + beanDef.name + "*> mNames;\n");
                            if (beanDef.isHaveColourValue())
                                sw.Write(s + "std::map<const std::wstring," + beanDef.name + "*> mColours;\n");
                            if (beanDef.isHaveMapidValue())
                                sw.Write(s + "std::map<const std::wstring," + beanDef.name + "*> mMapids;\n");
                            sw.Write("            " + beanDef.name + " mErrorData;\n");
                            sw.Write("        };\n");
                            sw.Write("        " + beanDef.name + "Table&  Get" + beanDef.name + "TableInstance();\n");
                            sw.Write("    }\n");
                            sw.Write("}\n");
                            sw.Write("#endif // __" + beanDef.name + "__h__\n");
                        }
                    }
                }
                catch(System.Exception e)
                {
                    Error("文件" + cppPath + "没有正确删除，创建表格失败！异常信息="+e.Message);
                    return false;
                }
            }
            return true;
        }
        private bool MakeClientPkgCode()
        {
            ClearDirection(mDstClientPkgPath);
            Directory.CreateDirectory(mDstClientPkgPath);

            for (int i = 0; i < mDefineMgr.mClientDef.Count();i++ )
            {
                BeanDef beanDef = mDefineMgr.mClientDef[i];
                if(beanDef.makeLua)
                {
                    continue;
                }
                string pkgPath = mDstClientPkgPath;
                Directory.CreateDirectory(pkgPath);
                pkgPath += "\\" + beanDef.spaceName;
                Directory.CreateDirectory(pkgPath);
                pkgPath += "\\" + beanDef.name + ".pkg";
                
                try
                {
                    FileStream fs = File.Open(pkgPath, FileMode.CreateNew, FileAccess.Write);
                    if(fs!=null)
                    {
                        StreamWriter sw = new StreamWriter(fs);
                        sw.AutoFlush = true;
                        if(sw!=null)
                        {
                            String tableSpace;
                            sw.Write("namespace GameTable\n{\n");
                            tableSpace = "    ";
                            sw.Write(tableSpace + "namespace " + beanDef.spaceName + "\n");
                            sw.Write(tableSpace + "{\n");
                            tableSpace = "        ";
                            String structName = beanDef.name;

                            sw.Write(tableSpace + "class " + beanDef.name + "\n");
                            sw.Write(tableSpace + "{\n");
                            sw.Write(tableSpace + "    public:\n");
                            for (int j = 0; j < beanDef.cols.Count; j++)
                            {
                                sw.Write(tableSpace + "    ");
                                String type = beanDef.cols[j].type;
                                if (beanDef.cols[j].type == "vector")
                                {
                                    type = beanDef.cols[j].value;
                                    sw.Write("std::vector<");
                                }

                                sw.Write(GetCPPType(type));

                                if (beanDef.cols[j].type == "vector")
                                    sw.Write(">");

                                sw.Write(" " + beanDef.cols[j].name + ";\n");
                            }
                            sw.Write(tableSpace + "};\n");

                            sw.Write(tableSpace + "class " + beanDef.name + "Table\n");
                            sw.Write(tableSpace + "{\n");
                            string className = beanDef.name + "Table";
                            sw.Write("            public:\n");
                            sw.Write(tableSpace + structName + "& getRecorder(int id);\n");

                            BeanCol beanColEx;
                            if (beanDef.GetCol(out beanColEx,"name")&&(beanColEx.type=="String"||beanColEx.type=="string"))
                                sw.Write(tableSpace + "const " + beanDef.name + "& getRecorderByName(const std::wstring& name);\n");
                            if (beanDef.GetCol(out beanColEx, "colour") && (beanColEx.type == "String" || beanColEx.type == "string"))
                                sw.Write(tableSpace + "const " + beanDef.name + "& getRecorderByColour(const std::wstring& colour);\n");
                            if (beanDef.GetCol(out beanColEx, "mapid") && (beanColEx.type == "String" || beanColEx.type == "string"))
                                    sw.Write(tableSpace + "const " + beanDef.name + "& getRecorderByMapid(const std::wstring& mapid);\n");
                            sw.Write(tableSpace + "virtual unsigned int getSize();\n");
                            sw.Write(tableSpace + "void getAllID(std::vector<int>& recordList);\n");
                            sw.Write(tableSpace + "virtual void ReleaseData();\n");

                            sw.Write(tableSpace + "};\n");
                            sw.Write(tableSpace + "static " + className + "&  Get" + className + "Instance();\n");
                            //sw.Write(tableSpace + "void " + className + "::ReleaseData()\n");
                            tableSpace = "    ";
                            sw.Write(tableSpace + "}\n");
                            sw.Write("}\n");
                            sw.Close();
                        }
                    }
                }
                catch(System.Exception e)
                {
                    Error("文件" + pkgPath + "没有正确删除，创建表格失败！异常信息=" + e.Message);
                    return false;
                }
            }
            return true;
        }
        private bool MakeClientLuaCode()
        {
            ClearDirection(mDstClientLuaPath);

            for (int i = 0; i < mDefineMgr.mClientDef.Count(); i++)
            {
                BeanDef beanDef = mDefineMgr.mClientDef[i];
                if(beanDef.makeLua==false)
                {
                    continue;
                }
                string luaPath = mDstClientLuaPath;
                try
                {
                    Directory.CreateDirectory(luaPath);
                    luaPath += "\\" + beanDef.spaceName;
                    Directory.CreateDirectory(luaPath);
                    luaPath += "\\" + beanDef.name + ".lua";
                }
                catch(UnauthorizedAccessException e)
                {
                    Error("目录" + luaPath + "无法访问，异常信息=" + e.Message);
                    return false;
                }

                try
                {
                    FileStream fs = File.Open(luaPath.ToLower(), FileMode.CreateNew, FileAccess.Write);
                    if (fs != null)
                    {
                        StreamWriter sw = new StreamWriter(fs, Encoding.ASCII);
                        sw.AutoFlush = true;
                        if (sw != null)
                        {
                            //bw.Write("#pragma once\n");
                            sw.Write("\r\n");
                            sw.Write("require \"utils.binutil\"\r\n");
                            sw.Write("\r\n");
                            string className=beanDef.name+"Table";
                            sw.Write(className + " = {}\r\n");
                            sw.Write(className + ".__index = " + className + "\r\n");
                            sw.Write("\r\n");
                            sw.Write("function " + className + ":new()\r\n");
                            sw.Write("\tlocal self = {}\r\n");
                            sw.Write("\tsetmetatable(self, " + className + ")\r\n");
                            sw.Write("\tself.m_cache = {}\r\n");
                            sw.Write("\tself.allID = {}\r\n");
                            sw.Write("\treturn self\r\n");
                            sw.Write("\r\n");
                            sw.Write("end\r\n");
                            sw.Write("\r\n");
                            sw.Write("function " + className + ":getRecorder(id)\r\n");
                            sw.Write("\treturn self.m_cache[id]\r\n");
                            sw.Write("end\r\n");
                            sw.Write("\r\n");
                            sw.Write("function " + className + ":getAllID()\r\n");
                            sw.Write("\treturn self.allID\r\n");
                            sw.Write("end\r\n");
                            sw.Write("\r\n");
                            sw.Write("function " + className + ":getSize()\r\n");
                            sw.Write("\treturn self.memberCount\r\n");
                            sw.Write("end\r\n");
                            sw.Write("\r\n");
                            string checkCode = "\tif not status then return false end\r\n";
                            sw.Write("function " + className + ":LoadBeanFromBinFile(filename)\r\n");
                            sw.Write("\tlocal util = BINUtil:new()\r\n");
                            sw.Write("\tlocal ret = util:init(filename)\r\n");
                            sw.Write("\tif not ret then\r\n");
                            sw.Write("\t\treturn false\r\n");
                            sw.Write("\tend\r\n");
                            sw.Write("\tlocal status=1\r\n");
                            sw.Write("\tlocal fileType,fileLength,version,memberCount,checkNumber\r\n");
                            sw.Write("\tstatus,fileType=util:Load_int()\r\n");
                            sw.Write(checkCode);
                            sw.Write("\tif fileType~=1499087948 then\n\t\treturn false\r\n\tend\r\n");
                            sw.Write("\tstatus,fileLength=util:Load_int()\r\n");
                            sw.Write(checkCode);
                            sw.Write("\tstatus,version=util:Load_short()\r\n");
                            sw.Write(checkCode);
                            sw.Write("\tif version~=" + mBinFileVersion + " then\r\n\t\treturn false\r\n\tend\r\n");
                            sw.Write("\tstatus,memberCount=util:Load_short()\r\n");
                            sw.Write(checkCode);
                            sw.Write("\tself.memberCount=memberCount\r\n");
                            sw.Write("\tstatus,checkNumber=util:Load_int()\r\n");
                            sw.Write(checkCode);
                            sw.Write("\tif checkNumber~=" + beanDef.GetColCheckNumber() + " then\r\n\t\treturn false\r\n\tend\r\n");
                            sw.Write("\tfor i=0,memberCount-1 do\r\n");
                            sw.Write("\t\tlocal bean={}\r\n");
                            for (int j = 0; j < beanDef.cols.Count; j++)
                            {
                                BeanCol beanCol = beanDef.cols[j];
                                sw.Write("\t\tstatus,bean." + beanCol.name + " = ");
                                if (IsBaseType(beanCol.type))
                                {
                                    if (beanCol.type == "int")
                                        sw.Write("util:Load_int()\r\n");
                                    else if (beanCol.type == "long")
                                        sw.Write("util:Load_long()\r\n");
                                    else if (beanCol.type == "double")
                                        sw.Write("util:Load_double()\r\n");
                                    else if (beanCol.type == "bool")
                                        sw.Write("util:Load_bool()\r\n");
                                    else if (beanCol.type == "string" || beanCol.type == "String")
                                        sw.Write("util:Load_string()\r\n");
                                }
                                else
                                {
                                    if (beanCol.value == "int")
                                        sw.Write("util:Load_Vint()\r\n");
                                    else if (beanCol.value == "long")
                                        sw.Write("util:Load_Vlong()\r\n");
                                    else if (beanCol.value == "double")
                                        sw.Write("util:Load_Vdouble()\r\n");
                                    else if (beanCol.value == "bool")
                                        sw.Write("util:Load_Vbool()\r\n");
                                    else if (beanCol.value == "string" || beanCol.value == "String")
                                    {
                                        sw.Write("util:Load_Vstring()\r\n");
                                    }
                                }
                                sw.Write("\t\tif not status then return false end\r\n");
                            }
                            sw.Write("\t\tself.m_cache[bean.id]=bean\r\n");
                            sw.Write("\t\ttable.insert(self.allID, bean.id)\r\n");
                            sw.Write("\tend\r\n");
                            sw.Write("\tutil:release()\r\n");
                            sw.Write("\treturn true\r\n");
                            sw.Write("end\r\n");
                            sw.Write("\r\n");
                            sw.Write("return " + className + "\r\n");
                            sw.Close();
                        }
                        fs.Close();
                    }
                }
                catch(System.Exception e)
                {
                    Error("文件" + luaPath + "没有正确删除，创建表格失败！异常信息=" + e.Message);
                    return false;
                }
            }
            return true;
        }
        private byte[] ConverJavaValueName(string name)
        {
            byte[] aaa = System.Text.UnicodeEncoding.UTF8.GetBytes(name);
            for(int i=0;i<aaa.Length;i++)
            {                
                if (i == 0 && aaa[i] >= 'a' && aaa[i] <= 'z')
                {
                    aaa[i] -= 32;
                }
            }
            return aaa;
        }
        private bool MakeServerCode(bool clearDir)
        {
            List<BeanDefInfo> beans = new List<BeanDefInfo>();
            for (int i = 0; i < mDefineMgr.mServerDef.Count;i++ )
            {
                BeanDefInfo bdi = new BeanDefInfo();
                bdi.writeCompare = true;
                bdi.mBeanDef = mDefineMgr.mServerDef[i];
                beans.Add(bdi);
            }
            for (int i = 0; i < mDefineMgr.mServerDef.Count;i++ )
            {
                // 增加基类是公用类的.
                BeanDef beanDef = mDefineMgr.mServerDef[i];
                if(beanDef.baseClass.Count()>0)
                {
                    for (int k = 0; k < mDefineMgr.mCommonDef.Count(); ++k)
                    {
                        List<string> vs = stovs(beanDef.baseClass, '.');
                        if (mDefineMgr.mCommonDef[k].name == vs[vs.Count()-1])
                        {
                            BeanDefInfo bdi = new BeanDefInfo();
                            bdi.writeCompare = false;
                            bdi.mBeanDef = mDefineMgr.mCommonDef[k];
                            beans.Add(bdi);
                        }
                    }
                }
                
                // 增加数组定义是公用类的.
                for (int j = 0; j < beanDef.cols.Count(); ++j)
                {
                    BeanCol beanCol = beanDef.cols[j];
                    if (beanCol.type == "vector")
                    {
                        for (int k = 0; k < mDefineMgr.mCommonDef.Count(); ++k)
                        {
                            List<string> vs = stovs(beanCol.value, '.');
                            if (mDefineMgr.mCommonDef[k].name == vs[vs.Count()-1])
                            {
                                BeanDefInfo bdi = new BeanDefInfo();
                                bdi.writeCompare = false;
                                bdi.mBeanDef = mDefineMgr.mCommonDef[k];
                                beans.Add(bdi);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < beans.Count; i++)
            {
                BeanDef beanDef = beans[i].mBeanDef;
                string javaPath = mDstServerJavaPath;
                javaPath += "\\fire";
                Directory.CreateDirectory(javaPath);
                javaPath += "\\pb";
                Directory.CreateDirectory(javaPath);
                javaPath += "\\" + beanDef.spaceName;
                Directory.CreateDirectory(javaPath);
                javaPath += "\\" + beanDef.name + ".java";

                try
                {
                    File.Delete(javaPath);
                    FileStream fs = File.Open(javaPath, FileMode.CreateNew, FileAccess.Write);
                    if (fs != null)
                    {
                        StreamWriter sw = new StreamWriter(fs, Encoding.ASCII);
                        sw.AutoFlush = true;
                        if (sw != null)
                        {
                            sw.Write("package fire.pb." + beanDef.spaceName + ";\r\n");
                            sw.Write("\r\n");
                            sw.Write("\r\n");
                            if (beanDef.baseClass != "")
                            {
                                sw.Write("public class " + beanDef.name + "  extends " + beanDef.baseClass + " {\r\n");
                            }
                            else
                            {
                                if (beans[i].writeCompare)
                                {
                                    sw.Write("public class " + beanDef.name + " implements mytools.ConvMain.Checkable ,Comparable<" + beanDef.name + ">{\r\n");
                                }
                                else
                                {
                                    sw.Write("public class " + beanDef.name + " implements mytools.ConvMain.Checkable {\r\n");
                                }
                            }
                            sw.Write("\r\n");
                            if (beans[i].writeCompare)
                            {
                                sw.Write("\tpublic int compareTo(" + beanDef.name + " o){\r\n");
                                sw.Write("\t\treturn this.id-o.id;\r\n");
                                sw.Write("\t}\r\n");
                            }
                            sw.Write("\r\n");
                            sw.Write("\t\r\n");

                            if (beanDef.baseClass != "")
                            {
                                sw.Write("\tpublic " + beanDef.name + "(" + beanDef.baseClass + " arg){\r\n");
                                sw.Write("\t\tsuper(arg);\r\n");
                                sw.Write("\t}\r\n");
                            }
                            sw.Write("\t\r\n");

                            sw.Write("\tstatic class NeedId extends RuntimeException{\r\n");
                            sw.Write("\r\n");
                            sw.Write("\t\t/**\r\n");
                            sw.Write("\t\t * \r\n");
                            sw.Write("\t\t */\r\n");
                            sw.Write("\t\tprivate static final long serialVersionUID = 1L;\r\n");
                            sw.Write("\t\t\r\n");
                            sw.Write("\t}\r\n");
                            sw.Write("\tpublic " + beanDef.name + "(){\r\n");
                            sw.Write("\t\tsuper();\r\n");
                            sw.Write("\t}\r\n");
                            sw.Write("\tpublic " + beanDef.name + "(" + beanDef.name + " arg){\r\n");
                            if (beanDef.baseClass != "")
                                sw.Write("\t\tsuper(arg);\r\n");
                            for (int j = 0; j < beanDef.cols.Count; j++)
                            {
                                BeanCol beanCol = beanDef.cols[j];
                                byte[] byteFromCol = System.Text.UnicodeEncoding.UTF8.GetBytes(beanCol.name);
                                sw.Write("\t\tthis.");
                                sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                sw.Write("=arg.");
                                sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                sw.Write(" ;\r\n");
                            }
                            sw.Write("\t}\r\n");
                            sw.Write("\tpublic void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){\r\n");
                            if (beanDef.baseClass != "")
                                sw.Write("\t\t\tsuper.checkValid(objs);\r\n");
                            for (int j = 0; j < beanDef.cols.Count; j++)
                            {
                                BeanCol beanCol = beanDef.cols[j];
                                byte[] byteFromCol = System.Text.UnicodeEncoding.UTF8.GetBytes(beanCol.name);
                                byte[] byteFromCol2 = System.Text.UnicodeEncoding.UTF8.GetBytes("所以不满足条件 ");
                                if (beanCol.minLimit.Length > 0 || beanCol.maxLimit.Length > 0)
                                {
                                    sw.Write("\t\t\tdo{\r\n");
                                    sw.Write("\t\t\t\t" + GetJAVAType(beanCol.type, beanCol.value) + " tmprefvalue=");
                                    sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                    sw.Write(";\r\n");
                                    sw.Write("\t\t\t\t\r\n");
                                    string longString = "";
                                    if (beanCol.type == "long")
                                        longString = "L";
                                    if (beanCol.minLimit.Length > 0)
                                    {
                                        sw.Write("\t\t\t\tif(tmprefvalue < " + beanCol.minLimit + longString + ") throw new RuntimeException(\"" + beanDef.name + ".");
                                        sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                        sw.Write("=\"+tmprefvalue+\",");
                                        sw.BaseStream.Write(byteFromCol2, 0, byteFromCol2.Length);
                                        sw.Write(beanDef.name + ".");
                                        sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                        sw.Write(" < " + beanCol.minLimit + longString + "\");\r\n");
                                    }
                                    if (beanCol.maxLimit.Length > 0)
                                    {
                                        sw.Write("\t\t\t\tif(tmprefvalue > " + beanCol.maxLimit + longString + ") throw new RuntimeException(\"" + beanDef.name + ".");
                                        sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                        sw.Write("=\"+tmprefvalue+\",");
                                        sw.BaseStream.Write(byteFromCol2, 0, byteFromCol2.Length);
                                        sw.Write(beanDef.name + ".");
                                        sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                        sw.Write(" > " + beanCol.maxLimit + longString + "\");\r\n");
                                    }
                                    sw.Write("\t\t\t}while(false);\r\n");
                                }
                            }
                            sw.Write("\t}\r\n");
                            for (int j = 0; j < beanDef.cols.Count; j++)
                            {
                                BeanCol beanCol = beanDef.cols[j];
                                sw.Write("\t/**\r\n");

                                byte[] byteFromCol = System.Text.UnicodeEncoding.UTF8.GetBytes(beanCol.name);
                                byte[] byteFromCol2 = ConverJavaValueName(beanCol.name);
                                string beanColdata = beanCol.data;
                                beanColdata = beanColdata.Trim();
                                byte[] byteFromCol3 = System.Text.UnicodeEncoding.UTF8.GetBytes(beanColdata);
                                sw.Write("\t * ");
                                sw.BaseStream.Write(byteFromCol3, 0, byteFromCol3.Length);
                                sw.Write("\r\n");
                                sw.Write("\t */\r\n");
                                sw.Write("\tpublic " + GetJAVAType(beanCol.type, beanCol.value) + " ");
                                sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                if (beanCol.type == "vector")
                                    sw.Write("  ;\r\n");
                                else
                                    sw.Write("  =" + GetJAVADefault(beanCol.type) + "  ;\r\n");
                                sw.Write("\t\r\n");

                                sw.Write("\tpublic " + GetJAVAType(beanCol.type, beanCol.value) + " get");
                                sw.BaseStream.Write(byteFromCol2, 0, byteFromCol2.Length);
                                sw.Write("(){\r\n");
                                sw.Write("\t\treturn this.");
                                sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                sw.Write(";\r\n");
                                sw.Write("\t}\r\n");
                                sw.Write("\t\r\n");

                                sw.Write("\tpublic void set");
                                sw.BaseStream.Write(byteFromCol2, 0, byteFromCol2.Length);
                                sw.Write("(" + GetJAVAType(beanCol.type, beanCol.value) + " v){\r\n");
                                sw.Write("\t\tthis.");
                                sw.BaseStream.Write(byteFromCol, 0, byteFromCol.Length);
                                sw.Write("=v;\r\n");
                                sw.Write("\t}\r\n");
                                sw.Write("\t\r\n");
                            }
                            sw.Write("\t\r\n");
                            sw.Write("};");
                            sw.Close();
                        }
                        fs.Close();
                    }
                }
                catch (System.Exception e)
                {
                    Error("创建文件" + javaPath + "时失败！异常信息=" + e.Message);
                    return false;
                }
            }

            return true;
        }
        private bool MakeClientCode()
        {
            if (!MakeClientCppCode())
                return false;
            if (!MakeClientPkgCode())
                return false;
            if (!MakeClientManagerCode())
                return false;
            if (!MakeClientLuaCode())
                return false;
            return true;
        }
        private void MakeClientCodeBtn()
        {
            if (mDefineMgr.mServerDef.Count == 0 && mDefineMgr.mClientDef.Count == 0)
            {
                Warning("无法生成，尚未加载任何数据定义！");
                OpenBtn();
                return;
            }
            if (MakeClientCode())
            {
                Info("成功生成客户端代码!");
            }
            OpenBtn();
        }
        private void OnClick_MakeClientCode(object sender, RoutedEventArgs e)
        {
            Thread thread = new Thread(new ThreadStart(MakeClientCodeBtn));
            thread.Start();
            CloseBtn();

        }
        private void MakeServerCodeBtn()
        {
            if (MakeServerCode(false))
            {
                Info("正确生成服务器使用的java代码!");
            }
            OpenBtn();

        }
        private void OnClick_MakeServerCode(object sender, RoutedEventArgs e)
        {
            Thread thread = new Thread(new ThreadStart(MakeServerCodeBtn));
            thread.Start();
            CloseBtn();
        }
        private void OnClick_ClearText(object sender,RoutedEventArgs e)
        {
            mOutputText.Items.Clear();
        }
        private void SelectDataBtn()
        {

            if (LoadDataDef()
                && MakeClientData(true, true, false, false)
                && MakeServerData(false, false)
                )
            {
                Info("一键生成选定表格成功！");
            }
            OpenBtn();
        }
        public void OnekeyMake_SelectData(object serder, RoutedEventArgs e)
        {
            mExportSingleTable = true;
            LoadDataDef();
            SelectXlsDlg dlg = new SelectXlsDlg();
            bool? a = dlg.ShowDialog();
            mDataMgr.Clear();
            mDefineMgr.Clear();

            if (dlg.mSelectFiles.Count == 0)
            {
                Error("没有选择任何文件！");
                return;
            }
            for (int i = 0; i < dlg.mSelectFiles.Count; i++)
            {
                if (mSelectFileType == 0)
                {
                    if (!mDataMgr.loadXlsFile(dlg.mSelectFiles[i]))
                    {
                        return;
                    }
                }
                else if (mSelectFileType == 1)
                {
                    if (!mDataMgr.LoadCsvFile(dlg.mSelectFiles[i]))
                    {
                        return;
                    }
                }
                else if (mSelectFileType == 2)
                {
                    if (!mDataMgr.LoadTxtFile(dlg.mSelectFiles[i]))
                    {
                        return;
                    }
                }

            }
            Thread thread = new Thread(new ThreadStart(SelectDataBtn));
            thread.Start();
            CloseBtn();
        }

        private void CloseBtn()
        {
            m_btn1.IsEnabled = false;
            m_btn2.IsEnabled = false;
            m_btn3.IsEnabled = false;
            m_btn4.IsEnabled = false;
            m_btn5.IsEnabled = false;
            m_btn6.IsEnabled = false;
            m_btn7.IsEnabled = false;
            m_btn8.IsEnabled = false;
            m_btn9.IsEnabled = false;
            m_btn10.IsEnabled = false;
            m_btn11.IsEnabled = false;
            m_btn12.IsEnabled = false;
            m_btn13.IsEnabled = false;
            m_combox1.IsEnabled = false;
        }
        private void OpenBtn()
        {
            m_btn1.Dispatcher.BeginInvoke(new Action(() => { m_btn1.IsEnabled = true; }));
            m_btn2.Dispatcher.BeginInvoke(new Action(() => { m_btn2.IsEnabled = true; }));
            m_btn3.Dispatcher.BeginInvoke(new Action(() => { m_btn3.IsEnabled = true; }));
            m_btn4.Dispatcher.BeginInvoke(new Action(() => { m_btn4.IsEnabled = true; }));
            m_btn5.Dispatcher.BeginInvoke(new Action(() => { m_btn5.IsEnabled = true; }));
            m_btn6.Dispatcher.BeginInvoke(new Action(() => { m_btn6.IsEnabled = true; }));
            m_btn7.Dispatcher.BeginInvoke(new Action(() => { m_btn7.IsEnabled = true; }));
            m_btn8.Dispatcher.BeginInvoke(new Action(() => { m_btn8.IsEnabled = true; }));
            m_btn9.Dispatcher.BeginInvoke(new Action(() => { m_btn9.IsEnabled = true; }));
            m_btn10.Dispatcher.BeginInvoke(new Action(() => { m_btn10.IsEnabled = true; }));
            m_btn11.Dispatcher.BeginInvoke(new Action(() => { m_btn11.IsEnabled = true; }));
            m_btn12.Dispatcher.BeginInvoke(new Action(() => { m_btn12.IsEnabled = true; }));
            m_btn13.Dispatcher.BeginInvoke(new Action(() => { m_btn13.IsEnabled = true; }));
            m_combox1.Dispatcher.BeginInvoke(new Action(() => { m_combox1.IsEnabled = true; }));
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 0; }));
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Visibility = Visibility.Hidden; }));
        }
        private void OnekeyMake_DataBtn()
        {
            mExportSingleTable = false;
            if (LoadDataDef()
                && LoadAllData()
                && MakeClientData(true, true, true, true)
                && MakeServerData(true, true)
                )
            {
                Info("一键数据生成成功！");
            }
            OpenBtn();
        }
        public void OnekeyMake_Data(object serder, RoutedEventArgs e)
        {
            Thread thread = new Thread(new ThreadStart(OnekeyMake_DataBtn));
            thread.Start();
            CloseBtn();
        }
        private bool LoadAllDataPro(){
            switch (mSelectFileType)
            {
                case 0:
                    {
                        mDataMgr.Clear();
                        List<string> files = MainWindow.GetFiles(mSrcDataXlsPath, "*.xlsm");
                        List<string> files1 = MainWindow.GetFiles(mSrcDataXlsPath, "*.xlsx");
                        int jj = 0;
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
                        break;
                    }
                case 1:
                    {
                        mDataMgr.Clear();
                        List<string> files = MainWindow.GetFiles(mSrcDataCsvPath, "*.csv");
                        for (int ii = 0; ii < files.Count; ii++)
                        {
                            if (!mDataMgr.LoadCsvFile(files[ii]))
                            {
                                return false;
                            }
                            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 5 + 90 * (ii * 1.0 / (files.Count + files.Count) * 1.0); }));
                        }
                        break;
                    }
                case 2:
                    {
                        mDataMgr.Clear();
                        List<string> files = MainWindow.GetFiles(mSrcDataTxtPath, "*.txt");
                        for (int ii = 0; ii < files.Count; ii++)
                        {
                            if (!mDataMgr.LoadTxtFile(files[ii]))
                            {
                                return false;
                            }
                            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 5 + 90 * (ii * 1.0 / (files.Count + files.Count) * 1.0); }));
                        }
                        break;
                    }
            }


            
            return true;
        }
        private void DataAndCodeBtn()
        {
            //DoDataDelegate d = DoData;
            mExportSingleTable = false;
            if (!LoadDataDef())
            {
                OpenBtn();
                return;
            }
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 5; }));
            if (!LoadAllDataPro())
            {
                OpenBtn();
                return;
            }
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 90; }));
            if (!MakeClientData(true, true, true, true))
            {
                OpenBtn();
                return;
            }
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 95; }));
            if (!MakeServerData(true, true))
            {
                OpenBtn();
                return;
            }
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 98; }));
            if (!MakeClientCode())
            {
                OpenBtn();
                return;
            }
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 100; }));

            Thread thread = new Thread(new ThreadStart(MakeServerCodeBtn));
            thread.Start();

            Info("一键数据生成成功！");
            OpenBtn();            
        }
        public void OnekeyMake_DataAndCode(object sender, RoutedEventArgs e)
        {
            Thread thread = new Thread(new ThreadStart(DataAndCodeBtn));
            thread.Start();
            CloseBtn();
            m_pro.Visibility = Visibility.Visible;
        }
        public static List<string> stovs(string src,char splitChar)
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
        public static string vstos(List<string> vsParams,char splitChar)
        {
            string retString = "";
            for(int i=0;i<vsParams.Count;i++)
            {
                retString += vsParams[i];
                if(i+1<vsParams.Count)
                {
                    retString += splitChar;
                }
            }
            return retString;
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
            mOutputText.Items.Add("自动打表功能说明：");
            mOutputText.Items.Add("    【一键生成选定数据】弹出一个对话框，选择生成的表格，并生成相关的数据。");
            mOutputText.Items.Add("    【一键生成数据】生产所有的数据。");
            mOutputText.Items.Add("    【一键生成代码和数据】生成所有的数据和所有代码。");
            mOutputText.Items.Add("----------------------------------------------------------------------------");
            mOutputText.Items.Add("手动打表功能说明：");
            mOutputText.Items.Add("    【加载定义】加载BEANS下的所有XML数据定义.");
            mOutputText.Items.Add("    【加载配置表格】根据[Hostory]Record中定义的BEAN相关表格数据.");
            mOutputText.Items.Add("    【加载全部表格】加载所有的表格数据，时间会比较长，大约3分钟.");
            mOutputText.Items.Add("    【生成服务器数据】根据加载的内容生成服务器相关的XML数据.");
            mOutputText.Items.Add("    【生成客户端数据】根据加载的内容生成客户端相关的BIN和工具使用的XML");
            mOutputText.Items.Add("    【生成客户端代码】根据定义生成C++代码和PKG代码，此功能无需加载任何数据");
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
        public static bool GetAbsDataPath(out string absPathFileName)//获取绝对数据路径,配置的都是相对的.
        {
            absPathFileName = "";
            string strExecPath = System.IO.Directory.GetCurrentDirectory();
            List<string> vsExecPath = MainWindow.stovs(strExecPath, '\\');
            string strconfigpath = "";
            if (mSelectFileType == 0)
            {
                strconfigpath = MainWindow.mSrcDataXlsPath;
            }
            else if (mSelectFileType == 1)
            {
                strconfigpath = MainWindow.mSrcDataCsvPath;
            }
            else if (mSelectFileType == 2)
            {
                strconfigpath = MainWindow.mSrcDataTxtPath;
            }
            List<string> vsConfigPath = MainWindow.stovs(strconfigpath, '\\');
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
                        MainWindow.Error("计算绝对路径失败，工作目录为" + strExecPath + ",配置目录为" + strconfigpath);
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
            for (int i = 0; i < vsConfigPath.Count;i++ )
            {
                absPath.Add(vsConfigPath[i]);
            }
            absPathFileName = vstos(absPath, '\\');
            return true;
        }

        private void setProgramUse()
        {
            mSrcDataXlsPath     = "..\\" + mSrcDataXlsPath;
            mSrcDataCsvPath     = "..\\" + mSrcDataCsvPath;
            mSrcDataTxtPath = "..\\" + mSrcDataTxtPath;
            mSrcDataDefXmlPath  = "..\\" + mSrcDataDefXmlPath;
            if (!mManualConfig)
            {
                int nStartPos = mDstServerXmlDataPath.IndexOf("\\gs");//找到gs目录
                string prePath = mDstServerXmlDataPath.Substring(0,nStartPos);
                string suffPath = mDstServerXmlDataPath.Substring(nStartPos, mDstServerXmlDataPath.Length - nStartPos);
                mDstServerXmlDataPath = "..\\" + prePath + "\\serverbin" + suffPath;
            }
        }

        private void OnCheckProgram(object sender, RoutedEventArgs e)
        {
            string strProgramUse = "";
            if (m_btn11.IsChecked == true)
            {
                strProgramUse = "true";
            }
            else
            {
                strProgramUse = "false";
            }
            mOptionIni.FindAndAddApp("Config", "CoderUse").FindAndAddItem("CoderUse").SetValue(strProgramUse);
            GetConfigPath();
            mOptionIni.WriteToFile();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Thread thread = new Thread(excelTocsv);
            thread.Start();
            CloseBtn();
            m_pro.Visibility = Visibility.Visible;
        }
        private void excelTocsv()
        {
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 5; }));
            if (!LoadAllDataPro())
            {
                OpenBtn();
                return;
            }
            if (!Directory.Exists(mSrcDataCsvPath))
            {
                Directory.CreateDirectory(mSrcDataCsvPath);
                Info("目录：" + System.IO.Path.GetFullPath(mSrcDataCsvPath) + "创建成功");
            }
            if (!CreatCsvOrTxtFile(mSrcDataCsvPath,".csv"))
            {
                OpenBtn();
                return;
            }
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 100; }));
            Info("Csv文件全部生成成功！");
            OpenBtn();
        }
        private void excelTotxt()
        {
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 5; }));
            if (!LoadAllDataPro())
            {
                OpenBtn();
                return;
            }
            if (!Directory.Exists(mSrcDataTxtPath))
            {
                Directory.CreateDirectory(mSrcDataTxtPath);
                Info("目录：" + System.IO.Path.GetFullPath(mSrcDataTxtPath) + "创建成功");
            }
            if (!CreatCsvOrTxtFile(mSrcDataTxtPath,".txt"))
            {
                OpenBtn();
                return;
            }
            m_pro.Dispatcher.BeginInvoke(new Action(() => { m_pro.Value = 100; }));
            Info("Txt文件全部生成成功！");
            OpenBtn();
        }
        private bool CreatCsvOrTxtFile(string strPath, string strFileExtension)
        {
            Dictionary<string, XlsData>.KeyCollection ids = mDataMgr.mXls.Keys;
            foreach (string name in ids)
            {
                XlsData data;
                if (mDataMgr.mXls.TryGetValue(name, out data))
                {
                    int index = 1;
                    string strFileName = data.mFileName;

                    string csvFileName = strPath + "\\" + strFileName + strFileExtension;
                    int startindex = 0;
                    while (index > 0)
                    {
                        index = strFileName.IndexOf("\\", startindex);
                        startindex = index + 1;
                        string strDirectory = "";
                        for(int jj = 0; jj<index; jj++){
                            strDirectory += strFileName[jj];
                        }
                        if (!Directory.Exists(strPath + "\\" + strDirectory))
                        {
                            Directory.CreateDirectory(strPath + "\\" + strDirectory);
                            Info("目录：" + System.IO.Path.GetFullPath(strPath + "\\" + strDirectory) + "创建成功");
                            
                        }
                    }
                    if ( ToolsFile.FileExists(csvFileName))//modify by eagle
                        File.Delete(csvFileName);
                    FileStream fs = File.Open(csvFileName, FileMode.OpenOrCreate);
                    if (fs != null)
                    {
                        StreamWriter bw = new StreamWriter(fs, Encoding.GetEncoding("GB2312"));
                        if (bw != null)
                        {
                            StringBuilder strb = new StringBuilder();
                            for (int jj = 0; jj < data.mData.Count; jj++)
                            {
                                string strData = data.mData[jj];
                                strData = strData.Replace("\r\n", "");
                                strData = strData.Replace("\"", "\"\"");
                                strData = strData.Replace("\n", "");
                                if (jj % data.colCount == data.colCount - 1)
                                {
                                    if (strData == "n!u@l#l")
                                    {
                                        strb.Append("");
                                    }
                                    else
                                    {
                                        strb.Append("\"" + strData + "\"");
                                    }
                                    bw.WriteLine(strb);
                                    strb = new StringBuilder();
                                }
                                else
                                {
                                    if (strData == "n!u@l#l")
                                    {
                                        strb.Append("");
                                    }
                                    else
                                    {

                                        strb.Append("\"" + strData + "\"");
 
                                    }
                                    strb.Append(",");

                                }
                            }
                            bw.Close();    
                        }
                        fs.Close();
                        Info("文件" + System.IO.Path.GetFullPath(csvFileName) + "创建成功");
                    }

                }
            }
            return true;
        }

        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            Thread thread = new Thread(excelTotxt);
            thread.Start();
            CloseBtn();
            m_pro.Visibility = Visibility.Visible;
            
        }

        private void m_combox1_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            mSelectFileType = m_combox1.SelectedIndex;
            if (m_btn12 != null && m_btn13 != null)
            {
                if (mSelectFileType == 0)
                {
                    m_btn12.IsEnabled = true;
                    m_btn13.IsEnabled = true;
                }
                else
                {
                    m_btn12.IsEnabled = false;
                    m_btn13.IsEnabled = false;
                }
            }

        }
    }
}
