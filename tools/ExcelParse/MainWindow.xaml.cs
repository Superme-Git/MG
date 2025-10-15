using System;
using System.Runtime.InteropServices;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Xml;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Windows.Input;
using System.Linq;
using System.Text;
using System.ComponentModel;
using CSTools;

namespace ExcelParse
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        string mAppPath = System.AppDomain.CurrentDomain.SetupInformation.ApplicationBase;
        string mAppDir = "";
        string mOptionFileName = "";
        string mLogFileName = "ExcelParseLog.txt";

        ToolsIni mOptionIni = null;

        public string mSrcXlsPath = "";//源xls路径
        string mSrcXmlPath = "";//源转义XML路径
        string mDstServerXmlPath = "";//目标服务器XML路径
        string mDstClientBinPath = "";//目标客户端Bin路径
        string mDstClientXmlPath = "";//目标客户端Xml路径
        string mDstServerJavaPath = "";//目标服务器JAVA路径
        string mDstClientCppPath = "";//目标客户端C++路径
        string mDstClientLuaPath = "";//目标获取客户端LUA路径
        string mDstClientPkgPath = "";//目标获取客户端PKG路径

        List<TableDef> mTables;//全部表.
        Dictionary<string, XlsFileData> mDatas; //全部文件数据.
        public const ushort mBinFileVersion=101;

        List<string> mSrcXlsList;//原始XLS列表
        List<string> mSrcXmlList;//原始XML列表

        List<ServerDataDef> mServerDatas=new List<ServerDataDef>();//服务器数据

        //
        static ProgressBarManager mProgressBarManager;
        static SelectFileDlg dlg = null;

        //
        static LogManager mLogManager=null;

        int  mSaveTableType = 0;//0:全部表和代码,1:只写全部表，2：写选中的表和代码，3：只写选中的表
        bool mNeedSaveCode = true;

        public static void LogCall(string log)
        {
            mLogManager.LogCall(log);
            mProgressBarManager.ShowLog(log);
        }
        public static void LogError(string log) 
        {
            mLogManager.LogError(log);
            mProgressBarManager.ShowLog(log);
        }
        public static void LogWarning(string log) 
        {
            mLogManager.LogWarning(log);
            mProgressBarManager.ShowLog(log);
        }
        public static void LogInfo(string log)
        {
            mLogManager.LogInfo(log);
            mProgressBarManager.ShowLog(log);
        }

        public MainWindow()
        {
            InitializeComponent();
            Init();
            mProgressBarManager = new ProgressBarManager();
            mProgressBarManager.Initializer();
        }
        private void Window_Closed(object sender, EventArgs e)
        {
            mProgressBarManager.Release();
            UnInit();
        }
        private string InitTextBox(string configName,System.Windows.Controls.TextBox tb)
        {
            string retString;
            tb.IsReadOnly = true;
            retString = mOptionIni.FindAndAddApp(configName, "Path").FindAndAddItem("Path").GetValue();
            if (!ToolsFile.DirExists(retString))
            {
                retString = mAppPath;
            }
            retString = ToolsFileStr.DirToPath(retString);
            tb.Text = retString;
            return retString;
        }
        private void SetAbsPath(ref string path)
        {
            string subKey = "..";
            bool bRelPath = false;
            string strRoot = mAppDir;
            while (path.StartsWith(subKey))
            {
                bRelPath = true;
                path = path.Substring(mSrcXlsPath.IndexOf("\\") + 1);
                strRoot = strRoot.Substring(0, strRoot.LastIndexOf("\\"));
            }
            if (bRelPath)
                path = strRoot + "\\" + path;
        }
        private void Init()
        {
            mAppDir = mAppPath.Substring(0, mAppPath.Length - 1);
            mOptionFileName = mAppPath + "ExcelParseOption.ini";
            mSrcXlsList = new List<string>();
            mSrcXmlList = new List<string>();
            mTables = new List<TableDef>();
            mDatas = new Dictionary<string, XlsFileData>();
            this.WindowStyle = WindowStyle.ToolWindow;
            mOptionIni = new ToolsIni(mOptionFileName);
            mSrcXlsPath = InitTextBox("SrcXlsPath", TBSrcXlsPath);
            mSrcXmlPath = InitTextBox("SrcXmlPath", TBSrcXmlPath);
            mDstServerXmlPath = InitTextBox("DstServerXmlPath", TBDstServerXmlPath);
            mDstClientBinPath = InitTextBox("DstClientBinPath", TBDstClientBinPath);
            mDstServerJavaPath = InitTextBox("DstServerJavaPath", TBDstServerJavaPath);
            mDstClientCppPath = InitTextBox("DstClientCppPath", TBDstClientCppPath);
            mDstClientLuaPath = InitTextBox("DstClientLuaPath", TBDstClientLuaPath);
            mDstClientPkgPath = InitTextBox("DstClientPkgPath", TBDstClientPkgPath);

            string dstClientBinPath = mDstClientBinPath.Substring(0, mDstClientBinPath.LastIndexOf("\\"));
            mDstClientXmlPath = dstClientBinPath.Substring(0, dstClientBinPath.LastIndexOf("\\"));
            mDstClientXmlPath += "\\autoconfig\\";

            SetAbsPath(ref mSrcXlsPath);
            SetAbsPath(ref mSrcXmlPath);
            SetAbsPath(ref mDstServerXmlPath);
            SetAbsPath(ref mDstClientBinPath);
            SetAbsPath(ref mDstClientXmlPath);
            SetAbsPath(ref mDstClientCppPath);
            SetAbsPath(ref mDstClientLuaPath);
            SetAbsPath(ref mDstClientPkgPath);
        }

        private void UnInitOption()
        {
            return;

            mOptionIni.FindAndAddApp("SrcXlsPath", "Path").FindAndAddItem("Path").SetValue(mSrcXlsPath);
            mOptionIni.FindAndAddApp("SrcXmlPath", "Path").FindAndAddItem("Path").SetValue(mSrcXmlPath);
            mOptionIni.FindAndAddApp("DstServerXmlPath", "Path").FindAndAddItem("Path").SetValue(mDstServerXmlPath);
            mOptionIni.FindAndAddApp("DstClientBinPath", "Path").FindAndAddItem("Path").SetValue(mDstClientBinPath);
            mOptionIni.FindAndAddApp("DstServerJavaPath", "Path").FindAndAddItem("Path").SetValue(mDstServerJavaPath);
            mOptionIni.FindAndAddApp("DstClientCppPath", "Path").FindAndAddItem("Path").SetValue(mDstClientCppPath);
            mOptionIni.FindAndAddApp("DstClientLuaPath", "Path").FindAndAddItem("Path").SetValue(mDstClientLuaPath);
            mOptionIni.FindAndAddApp("DstClientPkgPath", "Path").FindAndAddItem("Path").SetValue(mDstClientPkgPath);
            mOptionIni.WriteToFile();
            mOptionIni.Free();
        }

        private void UnInit()
        {
            UnInitOption();
        }
        private string BtnPaths_Click(System.Windows.Controls.TextBox tb,string path)
        {
            FolderBrowserDialog FBD = new FolderBrowserDialog();
            FBD.SelectedPath = tb.Text;
            if (FBD.ShowDialog() == (DialogResult)1)
            {
                tb.Text = FBD.SelectedPath;// mSrcXmlPath;
                return ToolsFileStr.DirToPath(FBD.SelectedPath);
            }
            return path;
        }
        private void BtnSrcXmlPath_Click(object sender, RoutedEventArgs e){ mSrcXmlPath = BtnPaths_Click(TBSrcXmlPath,mSrcXmlPath); }
        private void BtnSrcXlsPath_Click(object sender, RoutedEventArgs e){ mSrcXlsPath = BtnPaths_Click(TBSrcXlsPath,mSrcXlsPath); }
        private void BtnDstClientBinPath_Click(object sender, RoutedEventArgs e) { mDstClientBinPath = BtnPaths_Click(TBDstClientBinPath, mDstClientBinPath); }
        private void BtnDstClientCppPath_Click(object sender, RoutedEventArgs e){   mDstClientCppPath = BtnPaths_Click(TBDstClientCppPath,mDstClientCppPath); }
        private void BtnDstServerXmlPath_Click(object sender, RoutedEventArgs e){   mDstServerXmlPath = BtnPaths_Click(TBDstServerXmlPath,mDstServerXmlPath); }
        private void BtnDstClientLuaPath_Click(object sender, RoutedEventArgs e){   mDstClientLuaPath = BtnPaths_Click(TBDstClientLuaPath,mDstClientLuaPath); }
        private void BtnDstClientPkgPath_Click(object sender, RoutedEventArgs e){   mDstClientPkgPath = BtnPaths_Click(TBDstClientPkgPath,mDstClientPkgPath); }
        private void BtnDstServerJavaPath_Click(object sender, RoutedEventArgs e){  mDstServerJavaPath = BtnPaths_Click(TBDstServerJavaPath,mDstServerJavaPath);   }
 
        private void UpdateLocalProgress(int currentLoadUnit,int maxLoadUnit,float beginProgress,float maxProgress)
        {
            float rate=(float)currentLoadUnit/(float)maxLoadUnit;
            float localProgress=(maxProgress-beginProgress)*rate;
            mProgressBarManager.ReportProgress((int)(beginProgress+localProgress));
        }
        private void MaxLocalProgress(float maxProgress)
        {
            mProgressBarManager.ReportProgress((int)maxProgress);
        }
        private bool ReadAllXmlDef(float beginProgress,float maxProgress)//读取所有的XML文件
        {
            LogCall("ReadAllXmlDef");
            DirectoryInfo ParentFolder = new DirectoryInfo(mSrcXmlPath);
            FileInfo[] ParentFolderInfo = ParentFolder.GetFiles("*.xml",SearchOption.AllDirectories);
            LogInfo("检测到协议定义文件总数为" + ParentFolderInfo.Length);

            string NewPattern = "*.xml";
            NewPattern = NewPattern.Replace("*", "");
            NewPattern = NewPattern.Replace("?", "");
            mTables.Clear();
            for (int i = 0; i < ParentFolderInfo.Length;i++ )
            {
                FileInfo CurFileInfo = ParentFolderInfo[i];
                if (CurFileInfo.FullName.EndsWith(NewPattern))
                {
                    TableDef td = new TableDef();
                    if (td.ReadXmlDef(CurFileInfo.FullName))
                    {
                        mTables.Add(td);
                        LogInfo(i+". 成功加载结构定义文件" + CurFileInfo.Name);
                    }
                    else
                    {
                        LogInfo(i+". 加载定义文件" + CurFileInfo.Name + "失败!");
                    }
                }
                UpdateLocalProgress(i, ParentFolderInfo.Length, beginProgress, maxProgress);
            }
            MaxLocalProgress(maxProgress);
            return true;
        }
        private void LinkAllXmlDef(float beginProgress,float maxProgress)//链接所有的表头
        {
           LogCall("LinkAllXmlDef");
           for(int i=0;i<mTables.Count;i++)
           {
               for(int j=0;j<mTables[i].beans.Count;j++)
               {
                   mTables[i].LinkBaseClass();
               }
               UpdateLocalProgress(i, mTables.Count, beginProgress, maxProgress);
           }
           MaxLocalProgress(maxProgress);
        }
        private string GetFileFullPath(string filter,string fileName)
        {
            DirectoryInfo parentFolder = new DirectoryInfo(mSrcXlsPath);
            FileInfo[] parentFoldInfo = parentFolder.GetFiles(filter, SearchOption.AllDirectories);
            
            for (int i = 0; i < parentFoldInfo.Length; i++)
            {
                FileInfo CurFileInfo = parentFoldInfo[i];
                string currentName = CurFileInfo.FullName;
                string currentNameEx = "";
                for (int j = 0; j < currentName.Length; j++)
                {
                    if (currentName[j] == '\\')
                        currentNameEx += '/';
                    else
                        currentNameEx += currentName[j];
                }
                List<string> vsName=stovs(currentNameEx, '/');
                if (fileName == vsName[vsName.Count - 1])
                {
                    return CurFileInfo.FullName;
                }
            }
            return null;
        }
        private bool ReadXlsData(Dictionary<string,int> fileList,float beginProgress,float maxProgress)
        {
            LogCall("ReadXlsData");
            mDatas.Clear();
            Dictionary<string, int>.KeyCollection keys = fileList.Keys;
            int count = keys.Count;
            int index = 0;
            foreach (string fileName in keys)
            {
                string fullName = GetFileFullPath("*.xlsx", fileName);
                if (fullName == null)
                    fullName = GetFileFullPath("*.xlsm", fileName);
                if (fullName == null)
                {
                    LogError("查找 "+fileName+" 的完整路径失败!");
                    return false;
                }
                LogInfo(index+". 读取" + fileName + "的数据!");

                XlsFileData data = new XlsFileData();
                if (!data.LoadFromXlsFile(fullName))
                {
                    LogError("读取" + fileName + "的数据失败！");
                }
                else
                {
                    mDatas.Add(fileName, data);
                }
                UpdateLocalProgress(index, keys.Count, beginProgress, maxProgress);
                index++;
            }
            MaxLocalProgress(maxProgress);
            LogInfo("全部数据读取完毕!");
            return true;
        }
        private bool ReadAllXlsData(string filterString,float beginProgress,float maxProgress)//读入数据到表中
        {
            LogCall("ReadAllXlsData");
            DirectoryInfo parentFolder = new DirectoryInfo(mSrcXlsPath);
            FileInfo[] parentFoldInfo = parentFolder.GetFiles(filterString, SearchOption.AllDirectories);
            
            string NewPattern = filterString;
            NewPattern = NewPattern.Replace("*", "");
            NewPattern = NewPattern.Replace("?", "");

            for (int i = 0; i < parentFoldInfo.Length;i++ )
            {
                FileInfo CurFileInfo = parentFoldInfo[i];
                string currentName = CurFileInfo.FullName.Remove(0, mSrcXlsPath.Length);
                string currentNameEx = "";

                for (int j = 0; j < currentName.Length; j++)
                {
                    if (currentName[j] == '\\')
                        currentNameEx += '/';
                    else
                        currentNameEx += currentName[j];
                }
                if (!mDatas.ContainsKey(currentNameEx))
                {
                    XlsFileData data = new XlsFileData();
                    try
                    {
                        if (!data.LoadFromXlsFile(CurFileInfo.FullName))
                        {
                            LogError("丛" + CurFileInfo.FullName + "中读取数据失败!");
                            return false;
                        }
                    }catch(NPOI.POIXMLException e)
                    {
                        string debugInfo = e.InnerException.InnerException.Message;
                        LogError("读取文件" + currentName + "时发生错误 " + debugInfo);
                        return false;
                    }
                    mDatas.Add(currentNameEx, data);
                    LogInfo("    "+i+". 丛" + currentNameEx + "中读取数据成功!");
                }
                UpdateLocalProgress(i, parentFoldInfo.Length, beginProgress, maxProgress);
            }
            MaxLocalProgress(maxProgress);
            return true;
        }
        private bool SetupAllDataToBean(float beginProgress,float maxProgress)
        {
            LogCall("SetupAllDataToBean");

            for(int i=0;i<mTables.Count;i++)
            {
                TableDef td = mTables[i];
                foreach(TableBean tb in td.beans)
                {
                    if (tb.genxml != "client")
                        continue;
                    
                    List<string> vs = MainWindow.stovs(tb.from, ',');
                    for (int j = 0; j < vs.Count; j++)
                    {
                        XlsFileData data;
                        if (mDatas.TryGetValue(vs[j], out data))
                        {
                            if (!tb.SetupData(data))
                            {
                                LogInfo("安装"+vs[j]+"的数据到"+tb.name+"失败!");
                                return false;
                            }
                            else
                                LogInfo(i + ". a安装" + vs[j] + "的数据到" + tb.name+" count="+data.rows.Count);
                        }
                        else
                        {
                            List<string> vsEx = MainWindow.stovs(vs[j], '/');
                            if (vsEx.Count > 0)
                            {
                                string xlsName = vsEx[vsEx.Count - 1];
                                if (mDatas.TryGetValue(xlsName, out data))
                                {
                                    if (!tb.SetupData(data))
                                    {
                                        LogInfo("安装"+xlsName+"的数据到"+tb.name+"失败!");
                                        return false;
                                    }
                                    else
                                        LogInfo(i + ". a安装" + vs[j] + "的数据到" + tb.name + " count=" + data.rows.Count);
                                }
                            }
                        }
                    }
                }
                UpdateLocalProgress(i, mTables.Count, beginProgress, maxProgress);
            }

            //计算所有子列的长度。
            foreach(TableDef td in mTables)
            {
                foreach(TableBean tb in td.beans)
                {
                    if (tb.genxml != "client")
                        continue;

                    if (tb.from.Length > 0)//只设置有XLS的
                    {
                        for (int i = 0; i < tb.cols.Count; i++)
                        {
                            tb.cols[i].SetColLength();
                        }
                    }
                }
            }
            LogInfo("安装全部数据成功!");
            return true;
        }
        private void WriteSelectDataFile(Dictionary<string,int> dstFileList, float beginProgress,float maxProgress)
        {
            LogCall("WriteSelectDataFile");
            Dictionary<string, int>.KeyCollection keys = dstFileList.Keys;
            int index = 0;
            int count = keys.Count;

            for(int i=0;i<mTables.Count;i++)
            {
                TableDef td = mTables[i];
                foreach (TableBean tb in td.beans)
                {
                    if (tb.from.Length > 0)
                    {
                        List<string> vsName = stovs(tb.from, '/');
                        string lastName = vsName[vsName.Count - 1];
                        int data;
                        if (dstFileList.TryGetValue(lastName, out data))
                        {
                            if (tb.genxml == "client")
                            {
                                tb.binFileName = "fire.gsp" + td.space + "." + tb.name + ".bin";
                                tb.WriteClientBinFile(mDstClientBinPath, td.space);
                                tb.WriteClientXmlFile(mDstClientXmlPath, td.space);
                            }
                        }
                    }
                }
                UpdateLocalProgress(i, mTables.Count, beginProgress, maxProgress);
                index++;
            }
            MaxLocalProgress(maxProgress);
        }
        private bool WriteAllDataFile(float beginProgress,float maxProgress)//写全部表到二进制文件中
        {
           
            LogCall("WriteAllDataFile");
            for(int j=0;j<mTables.Count;j++)
            {
                TableDef td = mTables[j];
                for (int i = 0; i < td.beans.Count; i++)
                {
                    if (td.beans[i].genxml == "client")
                    {
              
                        td.beans[i].WriteClientBinFile(mDstClientBinPath, td.space);
                        td.beans[i].WriteClientXmlFile(mDstClientXmlPath, td.space);
                    }
                    else
                        LogWarning("bean(" + td.beans[i].name + ")既不是服务器结构，也不是客户端结构.");
                }
                UpdateLocalProgress(j, mTables.Count, beginProgress, maxProgress);
            }
            MaxLocalProgress(maxProgress);
            return true;
        }
        private bool WriteAllCode(float beginProgress,float maxProgress)
        {
            LogCall("WriteAllCode");
            for(int i=0;i<mTables.Count;i++)
            {
                TableDef td = mTables[i];
                for (int j = 0; j < td.beans.Count; j++)
                {
                    if (!td.beans[j].WriteCode(mDstClientCppPath, mDstClientPkgPath, mDstClientLuaPath, mDstServerJavaPath, td.space))
                    {
                        LogError("Write bean("+td.beans[j].name+") Code failed!");
                        return false;
                    }
                    float currentProgress = (float)i / (float)mTables.Count * (float)maxProgress;
                    mProgressBarManager.ReportProgress((int)(currentProgress + beginProgress));
                }
                UpdateLocalProgress(i, mTables.Count,beginProgress,maxProgress);
            }
            MaxLocalProgress(maxProgress);
            return true;
        }
        private void WriteTableManagerH()
        {
            string filePathName = mDstClientCppPath + "TableDataManager.h";
            File.Delete(filePathName);
            FileStream fs = File.Open(filePathName, FileMode.OpenOrCreate);
            StreamWriter bw = new StreamWriter(fs);

            if (fs != null)
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

                foreach (TableDef td in mTables)
                {
                    for (int i = 0; i < td.beans.Count; i++)
                    {
                        TableBean tb = td.beans[i];
                        if (tb.genxml == "client")
                        {
                            string fileName = "beans/fire/gsp/" + td.space + "/" + tb.name + ".h";
                            bw.Write("#include \"" + fileName + "\"\n");
                        }
                    }
                }

                bw.Write("class TableDataManager\n");
                bw.Write("{\n");
                tableSpace = "    ";
                bw.Write(tableSpace+"public:\n");
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
                tableSpace = "    ";
                bw.Write(tableSpace + "private:\n");
                tableSpace = "        ";
                bw.Write(tableSpace + "std::vector<void*> mDatas;\n");
                bw.Write("};\n");
                bw.Close();
                fs.Close();
            }
        }
        private void WriteTableManagerCPP(string genXml)
        {
            if (!mNeedSaveCode) return;

            // 写CPP.
            string mainCppPathName = mDstClientCppPath+"TableDataManager.cpp";
            if (ToolsFile.FileExists(mainCppPathName))
                File.Delete(mainCppPathName);

            FileStream fs = File.Open(mainCppPathName, FileMode.OpenOrCreate);
            StreamWriter bw = new StreamWriter(fs);

            if (fs != null)
            {
                bw.Write("#include \"TableDataManager.h\"\n");
                bw.Write("#include \"utils/StringUtil.h\"\n");
                
                // 全部的单件实现.
                bw.Write("namespace fire{\n");
                bw.Write("  namespace gsp{\n");
                foreach(TableDef td in mTables)
                {
                    bw.Write("    namespace " + td.space + "{\n");
                    for (int i = 0; i < td.beans.Count;i++ )
                    {
                        TableBean tb = td.beans[i];
                        if (tb.genxml == genXml)
                        {
                            string className = td.beans[i].name + "Table";
                            bw.Write("    "+className + "&  Get" + className + "Instance(){\n");
                            bw.Write("        static " + className + " object;\n");
                            bw.Write("        return object;\n");
                            bw.Write("    }\n");
                        }
                    }
                    bw.Write("    }\n");
                }
                bw.Write("  }\n");
                bw.Write("}\n");
                
                String tableSpace;
                bw.Write("bool TableDataManager::loadAllTable(std::wstring& pathFileName,const std::wstring& path){\n");

                bw.Write("    if(sizeof(unsigned int)!=4)\n        return false;\n");
                bw.Write("    if(sizeof(int)!=4)\n        return false;\n");
                bw.Write("    if(sizeof(long long)!=8)\n        return false;\n");
                bw.Write("    if(sizeof(double)!=8)\n        return false;\n");
                //bw.Write("    if(sizeof(char*)!=4)\n        return false;\n");//shield by eagle(不需要一个指针类型作为判断依据)

                tableSpace = "    ";
                //bw.Write(tableSpace + "FILE* fp;\n");
                bw.Write(tableSpace + "\n");
                bw.Write(tableSpace + "TableDataFileHeader header;\n");
                bw.Write(tableSpace + "std::wstring strTemplateName = L\"/config/autobinconfig/\";\n");
                
                foreach(TableDef td in mTables)
                {
                    for (int i = 0; i < td.beans.Count; i++)
                    {
                        TableBean tb = td.beans[i];
                        if (tb.genxml == genXml)
                        {
                            bw.Write(tableSpace + "fire::gsp::" + td.space + "::" + tb.name + "Table& "+tb.name+"Instance= fire::gsp::" + td.space + "::Get" + tb.name + "TableInstance();\n");
                            bw.Write(tableSpace + "pathFileName = strTemplateName + " + tb.name + "Instance.GetBinFileName();\n"); 
                            bw.Write(tableSpace + "if(!" + tb.name + "Instance.SetupData(pathFileName))\n");
                            bw.Write(tableSpace + "    return false;\n");
                            
                            /*bw.Write(tableSpace + "fopen_s(&fp,ws2s(pathFileName).c_str(),\"rb\");\n");
                            bw.Write(tableSpace + "if(fp){\n");
                            bw.Write(tableSpace + "    fread(&header,sizeof(header),1,fp);\n");
                            bw.Write(tableSpace + "    if(!" + tb.name + "Instance.SetupData(fp,header.version,header.itemCount,header.colCheckNumber))\n");
                            bw.Write(tableSpace + "    {\n");
                            bw.Write(tableSpace + "        fclose(fp);\n");
                            bw.Write(tableSpace + "        return false;\n");
                            bw.Write(tableSpace + "    }\n");
                            bw.Write(tableSpace + "    fclose(fp);\n");*/                            
                        }
                    }
                }
                bw.Write("    return true;\n");
                bw.Write("}\n\n");

                bw.Write("void TableDataManager::releaseAllTable()\n");
                bw.Write("{\n");
                bw.Write("    for(size_t i=0;i<mDatas.size();i++)\n");
                bw.Write("    {\n");
                bw.Write("        free(mDatas[i]);\n");
                bw.Write("    }\n");
                bw.Write("    mDatas.clear();\n");
                bw.Write("}");
                
                bw.Close();
                fs.Close();
            }

            //写tolua_main.pkg
            string mainPkgPathName = mDstClientPkgPath + "TableDataManager.pkg";
            if (ToolsFile.FileExists(mainPkgPathName))
                File.Delete(mainPkgPathName);

            fs = File.Open(mainPkgPathName, FileMode.OpenOrCreate);
            bw = new StreamWriter(fs);

            if(fs!=null)
            {
                bw.Write("$#include \"TableDataManager.h\"\n");

                foreach(TableDef td in mTables)
                {
                    for(int i=0;i<td.beans.Count;i++)
                    {
                        TableBean tb=td.beans[i];
                        if(tb.makelua&&tb.genxml=="client")
                        {
                            string fileName = "GameData/fire/gsp/" + td.space + "/" + tb.name + ".pkg";
                            bw.Write("$pfile \"" + fileName + "\"\n");
                        }
                    }
                }
                bw.Close();
                fs.Close();
            }
        }
        private void backgroundWorker_Make(object sender,DoWorkEventArgs e)
        {
            mLogManager = new LogManager();
            mLogManager.Initialize(null);
            LogInfo("Startup---");

            try
            {
                // 依次加载所有的XLS数据到表数组中.
                if(!ReadXlsData(dlg.mDstFileList, 0, 20))
                {
                    System.Windows.MessageBox.Show("加载数据文件失败!");
                    mLogManager.Release();
                    return;
                }

                // 读取所有的XML文件.
                if (!ReadAllXmlDef(20, 40))
                {
                    System.Windows.MessageBox.Show("读全部XML定义!");
                    mLogManager.Release();
                    return;
                }

                SetupAllServerData(40, 70);//安装数据到服务器结构.
                SaveListServerData(dlg.mDstFileList);

                // 链接定义.
                LinkAllXmlDef(70, 80);
                // 安装数据到BEAN中.
                if(!SetupAllDataToBean(80, 85))
                {
                    System.Windows.MessageBox.Show("安装数据失败！");
                }
                // 写BIN文件.
                WriteSelectDataFile(dlg.mDstFileList, 85, 100);

                // 写源代码文件.
                if (mNeedSaveCode)
                {
                    WriteTableManagerH();
                    WriteTableManagerCPP("client");
                    if (!WriteAllCode(90, 100))
                    {
                        System.Windows.MessageBox.Show("写全部代码失败！");
                        mLogManager.Release();
                        return;
                    }
                }
            }
            catch (Exception exception)
            {
                LogError("发生异常 " + exception.Message);
                System.Windows.MessageBox.Show(exception.Message);
            }
            mLogManager.Release();
        }
        private void BtnMakeSelDstTableAndCode_Click(object sender, RoutedEventArgs e)
        {
            dlg = new SelectFileDlg();
            dlg.mSrcXlsPath = mSrcXlsPath;
            dlg.Initialize();
            dlg.ShowDialog();
            if(dlg.mOk)
            {
                mNeedSaveCode = true;
                mProgressBarManager.Start(backgroundWorker_Make, backgroundWorkder_Over, "load xml def", Left, Top, Width, Height); 
            }
            dlg.Release();
        }

        private void SetupAllServerData(float beginProgress,float maxProgress)
        {
            if (mServerDatas != null)
                mServerDatas.Clear();
            
            // 创建表头.
            for(int i=0;i<mTables.Count;i++)
            {
                TableDef td = mTables[i];

                for(int j=0;j<td.beans.Count;j++)
                {
                    TableBean tb = td.beans[j];
                    if (tb.genxml == "server")
                    {
                        ServerDataDef sdn = new ServerDataDef();
                        sdn.CreateDef(mTables, td.space, tb);
                        mServerDatas.Add(sdn);
                    }
                }
            }
            
            // 加载数据.
            for(int i=0;i<mServerDatas.Count;i++)
            {
                try
                {
                    ServerDataDef dataDef = mServerDatas[i];
                    XlsFileData fileData;
                    string xmlNames = dataDef.GetXlsName();
                    List<string> vsXlsName = stovs(xmlNames, ',');
                    for (int j = 0; j < vsXlsName.Count; j++)
                    {
                        string xlsName = vsXlsName[j];
                        if (mDatas.TryGetValue(xlsName, out fileData))
                        {
                            dataDef.SetupData(fileData);
                        }
                        else
                        {
                            List<string> vsName = stovs(xlsName, '/');
                            if (mDatas.TryGetValue(vsName[vsName.Count - 1], out fileData))
                            {
                                dataDef.SetupData(fileData);
                            }
                            else
                            {
                                mLogManager.LogWarning("bean " + dataDef.GetXlsName() + " setup data failed!");
                            }
                        }
                    }
                }
                catch(Exception e)
                {
                    mLogManager.LogError("load " + mServerDatas[i].GetXlsName() + " failed!\n");
                    return;
                }
                UpdateLocalProgress(i, mServerDatas.Count, beginProgress, maxProgress);
            }
            MaxLocalProgress(maxProgress);

        }
        private void SaveListServerData(Dictionary<string,int> fileNames)
        {
            // 写服务器文件.
            for (int i = 0; i < mServerDatas.Count; i++)
            {
                ServerDataDef dataDef = mServerDatas[i];
                int data;
                if(fileNames.TryGetValue(dataDef.GetXlsName(),out data))
                {
                    dataDef.WriteToXmlFile(mDstServerXmlPath);
                }
            }
        }
        private void SaveAllServerData()
        {
            // 写服务器文件.
            for(int i=0;i<mServerDatas.Count;i++)
            {
                ServerDataDef dataDef = mServerDatas[i];
                dataDef.WriteToXmlFile(mDstServerXmlPath);
            }
        }

        private void backgroundWorker_MakeAllData(object sender, DoWorkEventArgs e)
        {
            mLogManager = new LogManager();
            mLogManager.Initialize(null);
            LogInfo("Startup---");

            try
            {
                // 依次加载所有的XLS数据到表数组中.
                if (!ReadAllXlsData("*.xlsx", 0, 30))
                {
                    System.Windows.MessageBox.Show("加载数据文件失败！");
                    mLogManager.Release();
                    return;
                }
                if (!ReadAllXlsData("*.xlsm", 30, 40))
                {
                    System.Windows.MessageBox.Show("加载XLSM失败！");
                    mLogManager.Release();
                    return;
                }
                // 读取所有的XML文件.
                if (!ReadAllXmlDef(40, 50))
                {
                    System.Windows.MessageBox.Show("加载XmlDef失败!");
                    mLogManager.Release();
                    return;
                }
                SetupAllServerData(50,65);//安装数据到服务器结构.
                SaveAllServerData();

                // 链接定义.
                LinkAllXmlDef(65, 70);
                // 安装数据到BEAN中.
                if (!SetupAllDataToBean(70, 85))
                {
                    System.Windows.MessageBox.Show("安装全部数据到BEAN失败！");
                    mLogManager.Release();
                    return;
                }
                // 写BIN文件.
                if (!WriteAllDataFile(85, 90))
                {
                    System.Windows.MessageBox.Show("写BIN文件失败！");
                    mLogManager.Release();
                    return;
                }
                // 写源代码文件.
                if (mNeedSaveCode)
                {
                    WriteTableManagerH();
                    WriteTableManagerCPP("client");
                    if (!WriteAllCode(90, 100))
                    {
                        System.Windows.MessageBox.Show("写全部代码失败！");
                        mLogManager.Release();
                        return;
                    }
                }
            }
            catch(Exception exception)
            {
                LogError("发生异常 " + exception.Message);
                System.Windows.MessageBox.Show(exception.Message);
                mLogManager.Release();
                return;
            }
            mLogManager.Release();
            //System.Windows.MessageBox.Show("表格转换已经完成");
        }
        private void backgroundWorkder_Over(object sender, RunWorkerCompletedEventArgs e)
        {
            mProgressBarManager.OnEnd();
        }

        private void Gen_AllTableAndCode(object sender, RoutedEventArgs e)
        {
            mNeedSaveCode = true;
            mProgressBarManager.Start(backgroundWorker_MakeAllData, backgroundWorkder_Over, "生成全部表和代码", Left, Top, Width, Height);
        }

        private void Gen_AllTable(object sender, RoutedEventArgs e)
        {
            mNeedSaveCode = false;
            mProgressBarManager.Start(backgroundWorker_MakeAllData, backgroundWorkder_Over, "生成全部表", Left, Top, Width, Height);
        }
      
        public static bool IsBaseType(string type)
        {
            return type=="bool"||type=="int"||type=="long"||type=="double"||type=="string"||type=="String";
        }
        public static bool IsInvalidName(string name)
        {
            return name == "n!u@l#l";
        }
        public static TableBean FindBean(List<TableDef> tables,string type)
        {
            List<string> vsName = MainWindow.stovs(type, '.');
            string name = vsName[vsName.Count - 1];

            for(int i=0;i<tables.Count;i++)
            {
                TableDef td = tables[i];
                for(int j=0;j<td.beans.Count;j++)
                {
                    TableBean tb = td.beans[j];
                    if (tb.name == name)
                        return tb;
                }
            }
            return null;
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
    }
}

