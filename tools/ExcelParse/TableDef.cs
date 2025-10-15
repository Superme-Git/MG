using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using System.Data;
using System.IO;
using System.Xml;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Windows.Input;
using NPOI.XSSF.UserModel;
using NPOI.SS.UserModel;
using CSTools;


namespace ExcelParse
{
    public class SubColDef
    {
        public string subFromCol; //子列名字.
        public Dictionary<int,string> data;//列数据 key=id,data=字符串信息.
    }
    public class ColDef
    {
        public string name;
        public string type;
        public string fromCol;
        public string value;
        public List<SubColDef> subCols;
        public Dictionary<int,int> colLength;//key = id. data=length
        public void SetColLength()
        {
            if (colLength == null)
                colLength = new Dictionary<int, int>();

            Dictionary<int,string>.KeyCollection keys=subCols[0].data.Keys;
            foreach(int id in keys)
            {
                if(type!="vector")
                {
                    colLength.Add(id, 1);
                }
                else
                {
                    bool bSet=false;
                    for(int i=0;i<subCols.Count;i++)
                    {
                        string data;
                        if(subCols[i].data.TryGetValue(id,out data))
                        {
                            if (data == "n!u@l#l"||data=="")
                            {
                                bSet = true;
                                colLength.Add(id, i);
                                break;
                            }
                        }
                    }

                    if (!bSet)
                    {
                        colLength.Add(id, subCols.Count);
                    }
                }
            }
        }
    }
    public class TableBean
    {
        public string name;//Bean 名字.
        public string genxml;//构建到哪里去.
        public string from;//来自那张表.
        public bool makelua;//是否构建LUA代码.
        public string priority;//优先级.
        public string baseclass;//父Bean.
        public string binFileName; //二进制文件名字.
        public List<ColDef> cols;//所有的列.
        
        [DllImport("User32.dll")]
        public static extern int GetWindowThreadProcessId(IntPtr hWnd, out int ProcessId);
        static public void KillExcel(Microsoft.Office.Interop.Excel.Application theApp)
        {
            int id = 0;
            IntPtr intptr = new IntPtr(theApp.Hwnd);
            System.Diagnostics.Process p = null;
            try
            {
                GetWindowThreadProcessId(intptr, out id);
                p = System.Diagnostics.Process.GetProcessById(id);
                if (p != null)
                {
                    p.Kill();
                    p.Dispose();
                }
            }
            catch (System.Exception ex)
            {

            }
        }
        public bool SetupData(XlsFileData fileData)
        {
            if(fileData.rows.Count<1)
                return false;

            // 创建索引.
            XlsFileDataRow titleRow=fileData.rows[0];

            List<int> indexs=new List<int>();
            int index=0;
            int colCount=0;
            try
            {
                for (int i = 0; i < cols.Count; i++)
                {
                    ColDef cd = cols[i];
                    for (int j = 0; j < cd.subCols.Count; j++)
                    {
                        SubColDef scd = cd.subCols[j];

                        bool findSuccess = false;
                        for (int k = 0; k < titleRow.datas.Count; k++)
                        {
                            string title = titleRow.datas[k];
                            if (title == scd.subFromCol)
                            {
                                indexs.Add(k);
                                findSuccess = true;
                                break;
                            }
                        }
                        if(!findSuccess)
                            MainWindow.LogWarning("在XLS中没有找到列" + scd.subFromCol);
                        ++colCount;
                    }
                }
            }catch(Exception e)
            {
                return false;
            }

            if (indexs.Count != colCount)
            {
                MainWindow.LogError("表"+from+"的列跟定义"+name+"的列，对应不上!");
                return false;
            }

            // 寻找ID数据所在的列.
            string idColFrom="";
            int idColId = 0;
            try
            {
                for (int i = 0; i < cols.Count; i++)
                {
                    for (int j = 0; j < cols[i].subCols.Count; j++)
                    {
                        if (cols[i].name == "id")
                        {
                            idColFrom = cols[i].fromCol;
                            break;
                        }
                    }
                }

                for (int i = 0; i < titleRow.datas.Count; i++)
                {
                    if (titleRow.datas[i] == idColFrom)
                    {
                        idColId = i;
                        break;
                    }
                }
            }
            catch(Exception e)
            {
                return false;
            }
            
            for (int i = 1; i < fileData.rows.Count; i++)
            {
                XlsFileDataRow dataRow = fileData.rows[i];
                int id=-1;
                int j=0;
                int k=0;
                if (dataRow.datas.Count > 0)
                {
                    try
                    {
                        id = Convert.ToInt32(dataRow.datas[idColId]);
                        index = 0;
                        for (j = 0; j < cols.Count; j++)
                        {
                            for (k = 0; k < cols[j].subCols.Count; k++)
                            {
                                SubColDef scd = cols[j].subCols[k];
                                string data = dataRow.datas[indexs[index++]];
                                scd.data.Add(id, data);
                            }
                        }
                    }
                    catch(FormatException e)
                    {
                        //System.Windows.MessageBox.Show("file "+this.from+" id="+dataRow.datas[idColId]+" "+e.Message);
                        //这是SDK的固有BUG。不影响使用。
                        break;
                    }
                    catch(Exception e)
                    {
                        MainWindow.LogInfo("行"+i+",列"+j+",子列"+k+",异常信息="+e.Message);
                        return false;
                    }
                }
                else
                {
                    //System.Windows.MessageBox.Show("dataRow.datas.Count==0");
                }
            }

            return true;
        }
        public string ConverXmlString(string text)
        {
            string xmlText="";
            //if (text.Length > 2 && text[0] == '<')
            {
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
                    else if(text[i]=='&')
                    {
                        xmlText += "&amp;";
                    }
                    else
                        xmlText += text[i];
                }
                return xmlText;
            }
            return text;
        }
        public bool WriteClientXmlFile(string dstClientXmlPath, string space)
        {
            string fullXmlFileName = dstClientXmlPath + "fire.gsp." + space + "." + name + ".xml";

            if (ToolsFile.FileExists(fullXmlFileName))
                File.Delete(fullXmlFileName);

            FileStream fs = File.Open(fullXmlFileName, FileMode.OpenOrCreate);
            if (fs != null)
            {
                StreamWriter bw = new StreamWriter(fs, Encoding.Unicode);
                if (bw != null)
                {
                    bw.Write("<root>\n");

                    //先找到所有的行ID.
                    Dictionary<int, string>.KeyCollection ids = null;
                    for (int col = 0; col < cols.Count; col++)
                    {
                        if (cols[col].name == "id")
                        {
                            ids = cols[col].subCols[0].data.Keys;
                            break;
                        }
                    }

                    foreach (int id in ids)
                    {
                        bool hasVector = false;
                        string strRecord1 = "<record ";
                        string strRecord2 = "";
                        for (int i = 0; i < cols.Count; i++)
                        {
                            string data = "";
                            if (cols[i].type == "int"
                                || cols[i].type == "double"
                                || cols[i].type == "long"
                                || cols[i].type == "string"
                                || cols[i].type == "String"
                                || cols[i].type == "bool"
                                )
                            {
                                if (cols[i].subCols[0].data.TryGetValue(id, out data))
                                {
                                    if (data.Length == 0)
                                        continue;                                    
 
                                    else if (data != "n!u@l#l" && data != "")
                                    {
                                        data = ConverXmlString(data);
                                        strRecord1 += cols[i].name + "=\"" + data + "\" ";
                                    }
                                }
                            }
                            else if (cols[i].type == "vector")
                            {
                                //先检查一下是不是都是有效行.
                                bool enableVector = false;
                                for (int j = 0; j < cols[i].subCols.Count; j++)
                                {
                                    if (cols[i].subCols[j].data.TryGetValue(id, out data))
                                    {
                                        if (data != "n!u@l#l" && data != "")
                                        {
                                            enableVector = true;
                                            hasVector = true;
                                        }
                                    }
                                }

                                //再写代码.
                                if (enableVector)
                                {
                                    strRecord2 += "      <" + cols[i].name + ">\n";
                                    string subData;
                                    string type = cols[i].value;
                                    if (type == "String")
                                        type = "string";

                                    for (int j = 0; j < cols[i].subCols.Count; j++)
                                    {
                                        if (cols[i].subCols[j].data.TryGetValue(id, out subData))
                                        {
                                            subData = ConverXmlString(subData);
                                            if (subData != "n!u@l#l" && subData != "")
                                                strRecord2 += "        <" + type + ">" + subData + "</" + type + ">\n";
                                        }
                                    }
                                    strRecord2 += "      </" + cols[i].name + ">\n";
                                }
                            }
                        }

                        if (hasVector)
                        {
                            strRecord1 += ">\n";
                            bw.Write(strRecord1);
                            bw.Write(strRecord2);
                            bw.Write("</record>\n");
                        }
                        else
                        {
                            strRecord1 += "/>\n";
                            bw.Write(strRecord1);
                        }
                    }

                    bw.Write("</root>");
                    bw.Close();
                    fs.Close();
                    return true;
                }
                bw.Close();
            }
            fs.Close();

            return false;
        }
        public bool WriteClientBinFile(string dstClientBinPath, string space)
        {
            if (genxml != "client")
            {
                return false;
            }
            //计算二进制文件名字.
            binFileName="fire.gsp." + space + "." + name + ".bin";
            string fullBinFileName = dstClientBinPath + "\\" + binFileName;
            if (ToolsFile.FileExists(fullBinFileName))
                File.Delete(fullBinFileName);
            FileStream fs = File.Open(fullBinFileName, FileMode.OpenOrCreate);
            if (fs != null)
            {
                BinaryWriter bw = new BinaryWriter(fs);
                if (bw != null)
                {
                    // 写文件头.
                    bw.Write('L');
                    bw.Write('D');
                    bw.Write('Z');
                    bw.Write('Y');

                    uint fileLength = 0;
                    bw.Write(fileLength);
                    const ushort version = MainWindow.mBinFileVersion;
                    bw.Write(version);
                    ushort memberCount = 0;
                    if(cols[0].subCols[0].data.Count>0)
                        memberCount = (ushort)cols[0].subCols[0].data.Count;
                    bw.Write(memberCount);
                    bw.Write(GetColCheckNumber());

                    if (memberCount > 0)
                    {
                        // 检查是否含有ID列.
                        Dictionary<int, string>.KeyCollection ids = null;
                        for (int i = 0; i < cols.Count; i++)
                        {
                            if (cols[i].name == "id")
                            {
                                if (cols[i].subCols.Count == 1)
                                {
                                    ids = cols[i].subCols[0].data.Keys;
                                    break;
                                }
                            }
                        }

                        if (ids != null)
                        {
                            foreach (int id in ids)
                            {
                                for (int i = 0; i < cols.Count; i++)
                                {
                                    string type = cols[i].type;

                                    int colLength;
                                    if (cols[i].colLength.TryGetValue(id, out colLength))
                                    {
                                        if(type=="vector")
                                        {
                                            WriteBinData(bw,"int",""+colLength);
                                            for (int j = 0; j < colLength; j++)
                                            {
                                                SubColDef scd = cols[i].subCols[j];
                                                string data;
                                                if (scd.data.TryGetValue(id, out data))
                                                {
                                                    if (data == "n!u@l#l")
                                                        data = "";
                                                    WriteBinData(bw,cols[i].value, data);
                                                }
                                                else
                                                {
                                                    WriteBinData(bw, cols[i].value, "");
                                                }
                                            }
                                        }
                                        else
                                        {
                                            string data;
                                            if (cols[i].subCols[0].data.TryGetValue(id, out data))
                                            {
                                                if (data == "n!u@l#l")
                                                    data = "";
                                                WriteBinData(bw, cols[i].type, data);
                                            }
                                            else
                                                WriteBinData(bw, cols[i].type, "");
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            string error = "error " + from;
                        }
                    }
                    // 
                    bw.Close();
                }
                fs.Close();
            }
            return true;
        }
        public string GetCPPType(string type)
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
        public bool WriteCode(string dstClientCppPath, string dstClientPkgPath, string dstClientLuaPath, string dstServerJavaPath, string space)
        {
            string javaPathFileName = dstServerJavaPath;

            bool makeServerCode = false;
            bool makeClientCode = false;

            if (genxml == "server")
                makeServerCode = true;

            if (genxml == "client")
                makeClientCode = true;

            if(genxml=="")
            {
                makeServerCode = true;
                makeClientCode = true;
            }

            if(makeClientCode)
            {
                string cppPathFileName = dstClientCppPath;
                string pkgPathFileName = dstClientPkgPath;
                string luaPathFileName = dstClientLuaPath;

                cppPathFileName += "beans";
                Directory.CreateDirectory(cppPathFileName);
                cppPathFileName += "\\fire";
                Directory.CreateDirectory(cppPathFileName);
                cppPathFileName += "\\gsp";
                Directory.CreateDirectory(cppPathFileName);
                cppPathFileName += "\\" + space;
                Directory.CreateDirectory(cppPathFileName);
                cppPathFileName += "\\" + name + ".h";

                pkgPathFileName += "fire";
                Directory.CreateDirectory(pkgPathFileName);
                pkgPathFileName += "\\gsp";
                Directory.CreateDirectory(pkgPathFileName);
                Directory.CreateDirectory(pkgPathFileName);
                pkgPathFileName += "\\" + space;
                Directory.CreateDirectory(pkgPathFileName);
                pkgPathFileName += "\\" + name + ".pkg";

                if (ToolsFile.FileExists(cppPathFileName))
                    File.Delete(cppPathFileName);

                FileStream fs = File.Open(cppPathFileName, FileMode.OpenOrCreate);

                bool isHaveNameValue = false;
                bool isHaveColourValue = false;
                bool isHaveMapIdValue = false;
                for(int i=0;i<cols.Count;i++)
                {
                    if (cols[i].subCols.Count == 1)
                    {
                        if (cols[i].name == "name")
                            isHaveNameValue = true;
                        else if (cols[i].name == "colour")
                            isHaveColourValue = true;
                        else if (cols[i].name == "mapid"&&(cols[i].type=="string"||cols[i].type=="String"))
                            isHaveMapIdValue = true;
                    }
                }

                if (fs != null)
                {
                    StreamWriter bw = new StreamWriter(fs);
                    if (bw != null)
                    {
                        String tableSpace;
                        //bw.Write("#pragma once\n");
                        bw.Write("#ifndef __" + name + "__h__\n");
                        bw.Write("#define __" + name + "__h__\n");
                        bw.Write("#include <string>\n");
                        bw.Write("#include <vector>\n");
                        bw.Write("#include <map>\n");
                        bw.Write("#include \"fileex.h\"\n");
                        bw.Write("std::wstring s2ws(const std::string& s);\n");
                        bw.Write("\n");
                        bw.Write("namespace fire\n{\n");
                        tableSpace = "    ";
                        bw.Write(tableSpace + "namespace gsp\n");
                        bw.Write(tableSpace + "{\n");
                        tableSpace = "        ";
                        bw.Write(tableSpace + "namespace " + space + "\n");
                        bw.Write(tableSpace + "{\n");
                        tableSpace = "            ";
                        bw.Write(tableSpace + "class " + name + "\n");
                        bw.Write(tableSpace + "{\n");
                        bw.Write(tableSpace + "    public:\n");
                        for (int i = 0; i < cols.Count;i++ )
                        {
                            bw.Write(tableSpace+"    ");
                            String type = cols[i].type;
                            if(cols[i].type=="vector")
                            {
                                type = cols[i].value;
                                bw.Write("std::vector<");
                            }

                            bw.Write(GetCPPType(type));

                            if (cols[i].type == "vector")
                                bw.Write(">");

                            bw.Write(" " + cols[i].name + ";\n");
                        }
                        bw.Write(tableSpace + "};\n");

                        bw.Write(tableSpace + "class " + name + "Table\n");
                        bw.Write(tableSpace + "{\n");
                        bw.Write(tableSpace + "public:\n");
                        string className = name + "Table";
                        bw.Write(tableSpace + "  " + className + "(){ mErrorData.id=-1; }\n");
                        bw.Write(tableSpace + "  virtual ~" + className + "()\n");
                        bw.Write(tableSpace + "  {\n");
                        bw.Write(tableSpace + "      std::map<int," + name + "*>::iterator it=mDatas.begin();\n");
                        bw.Write(tableSpace + "      while(it!=mDatas.end())\n");
                        bw.Write(tableSpace + "      {\n");
                        bw.Write(tableSpace + "          delete it->second;\n");
                        bw.Write(tableSpace + "          ++it;\n");
                        bw.Write(tableSpace + "      }\n");
                        bw.Write(tableSpace + "  }\n");
                        bw.Write(tableSpace + "private:\n");

                        bw.Write(tableSpace + "std::wstring ReadString(PFS::CFile& file)\n");
                        bw.Write(tableSpace + "{\n");
                        bw.Write(tableSpace + "    int stringLength;\n");
                        bw.Write(tableSpace + "    file.Read(&stringLength,sizeof(int));\n");
                        bw.Write(tableSpace + "    char* buf=(char*)malloc(stringLength+1);\n");
                        bw.Write(tableSpace + "    file.Read(buf,stringLength);\n");
                        bw.Write(tableSpace + "    buf[stringLength]=0;\n");
                        bw.Write(tableSpace + "    std::wstring r=s2ws(buf);\n");
                        bw.Write(tableSpace + "    free(buf);\n");
                        bw.Write(tableSpace + "    return r;\n");
                        bw.Write(tableSpace + "}\n");

                        bw.Write(tableSpace + "public:\n");
                        tableSpace = "                ";
                        bw.Write(tableSpace + "bool SetupData(const std::wstring& pathFileName);\n");
                        bw.Write("            public:\n");
                        //bw.Write(tableSpace+"virtual void getAllID(std::vector<int>& ids)=0;\n");
                        //bw.Write(tableSpace+"virtual const CBattleAIConfig& getRecorder(int id)=0;\n");

                        //bw.Write(tableSpace + "  const " + structName + "& GetRecorder(int id){\n");
                        bw.Write(tableSpace + "const "+name + "& getRecorder(int id)\n");
                        bw.Write(tableSpace + "{\n");
                        bw.Write(tableSpace + "    std::map<int," + name + "*>::iterator it=mDatas.find(id);\n");
                        bw.Write(tableSpace + "    if(it!=mDatas.end())\n");
                        bw.Write(tableSpace + "        return *it->second;\n");
                        bw.Write(tableSpace + "    return mErrorData;\n");
                        bw.Write(tableSpace + "}\n");

                        if (isHaveNameValue)
                        {
                            bw.Write(tableSpace + "const " + name + "& getRecorderByName(const std::wstring& name)\n");
                            bw.Write(tableSpace + "{\n");
                            bw.Write(tableSpace + "    std::map<const std::wstring," + name + "*>::iterator it=mNames.find(name);\n");
                            bw.Write(tableSpace + "    if(it!=mNames.end())\n");
                            bw.Write(tableSpace + "        return *it->second;\n");
                            bw.Write(tableSpace + "    return mErrorData;\n");
                            bw.Write(tableSpace + "}\n");
                        }
                        if (isHaveColourValue)
                        {
                            bw.Write(tableSpace + "const " + name + "& getRecorderByColour(const std::wstring& colour)\n");
                            bw.Write(tableSpace + "{\n");
                            bw.Write(tableSpace + "    std::map<const std::wstring," + name + "*>::iterator it=mColours.find(colour);\n");
                            bw.Write(tableSpace + "    if(it!=mColours.end())\n");
                            bw.Write(tableSpace + "        return *it->second;\n");
                            bw.Write(tableSpace + "    return mErrorData;\n");
                            bw.Write(tableSpace + "}\n");
                        }
                        if(isHaveMapIdValue)
                        {
                            bw.Write(tableSpace + "const " + name + "& getRecorderByMapid(const std::wstring& mapid)\n");
                            bw.Write(tableSpace + "{\n");
                            bw.Write(tableSpace + "    std::map<const std::wstring," + name + "*>::iterator it=mMapids.find(mapid);\n");
                            bw.Write(tableSpace + "    if(it!=mMapids.end())\n");
                            bw.Write(tableSpace + "        return *it->second;\n");
                            bw.Write(tableSpace + "    return mErrorData;\n");
                            bw.Write(tableSpace + "}\n");
                        }
                        bw.Write(tableSpace + "void getAllID(std::vector<int>& recordList)\n");
                        bw.Write(tableSpace + "{\n");
                        bw.Write(tableSpace + "    std::map<int,"+name+"*>::iterator it=mDatas.begin();\n");
                        bw.Write(tableSpace + "    while(it!=mDatas.end())\n");
                        bw.Write(tableSpace + "    {\n");
                        bw.Write(tableSpace + "        recordList.push_back(it->first);\n");
                        bw.Write(tableSpace + "        ++it;\n");
                        bw.Write(tableSpace + "    }\n");
                        bw.Write(tableSpace + "}\n");
                        bw.Write(tableSpace + "virtual unsigned int getSize() const{ return mDatas.size(); }\n");
                        bw.Write(tableSpace + "const wchar_t* GetBinFileName(){ return L\"" + binFileName + "\"; }\n");

                        tableSpace = "            ";
                        bw.Write(tableSpace + "private:\n");
                        bw.Write(tableSpace + "    std::map<int," + name + "*> mDatas;\n");
                        if (isHaveNameValue)
                            bw.Write(tableSpace + "    std::map<const std::wstring," + name + "*> mNames;\n");
                        if(isHaveColourValue)
                            bw.Write(tableSpace + "    std::map<const std::wstring," + name + "*> mColours;\n");
                        if (isHaveMapIdValue)
                            bw.Write(tableSpace + "    std::map<const std::wstring," + name + "*> mMapids;\n");
                        bw.Write(tableSpace + "    " + name + " mErrorData;\n");

                        bw.Write(tableSpace + "};\n");
                        bw.Write(tableSpace + className + "&  Get" + className + "Instance();\n");

                        bw.Write(tableSpace + "inline bool "+name+"Table::SetupData(const std::wstring& pathFileName)\n");
                        bw.Write(tableSpace + "{\n");
                        bw.Write(tableSpace + "    PFS::CFile file;\n");
                        bw.Write(tableSpace + "    if (!file.Open(pathFileName, PFS::FM_EXCL, PFS::FA_RDONLY))\n");
                        bw.Write(tableSpace + "        return false;\n");
                        bw.Write(tableSpace + "    unsigned int type;\n");
                        bw.Write(tableSpace + "    file.Read(&type,sizeof(type));\n");
                        bw.Write(tableSpace + "    unsigned int size;\n");
                        bw.Write(tableSpace + "    file.Read(&size,sizeof(size));\n");
                        bw.Write(tableSpace + "    unsigned short version;\n");
                        bw.Write(tableSpace + "    file.Read(&version,sizeof(version));\n");
                        bw.Write(tableSpace + "    if(version!=" + MainWindow.mBinFileVersion + ")\n");
                        bw.Write(tableSpace + "    {\n");
                        bw.Write(tableSpace + "        file.Close();\n");
                        bw.Write(tableSpace + "        return false;\n");
                        bw.Write(tableSpace + "    }\n");
                        bw.Write(tableSpace + "    unsigned short itemCount;\n");
                        bw.Write(tableSpace + "    file.Read(&itemCount,sizeof(version));\n");
                        bw.Write(tableSpace + "    unsigned int colCheckNumber;\n");
                        bw.Write(tableSpace + "    file.Read(&colCheckNumber,sizeof(colCheckNumber));\n");
                        bw.Write(tableSpace + "    if(colCheckNumber!=" + GetColCheckNumber() + ")\n");
                        bw.Write(tableSpace + "    {\n");
                        bw.Write(tableSpace + "        file.Close();\n");
                        bw.Write(tableSpace + "        return false;\n");
                        bw.Write(tableSpace + "    }\n");

                        for (int i = 0; i < cols.Count; i++)
                        {
                            if (cols[i].type == "vector")
                            {
                                bw.Write(tableSpace + "    int colLength;\n");
                                break;
                            }
                        }

                        tableSpace = "                ";
                        bw.Write(tableSpace + "for(size_t i=0;i<itemCount;i++)\n");
                        bw.Write(tableSpace + "{\n");
                        bw.Write(tableSpace + "    " + name + "* object=new " + name + ";\n");
                        for (int i = 0; i < cols.Count; i++)
                        {
                            String type = cols[i].type;
                            if (type == "String" || type == "string")
                            {
                                bw.Write(tableSpace + "    object->" + cols[i].name + "=ReadString(file);\n");
                            }
                            else if (type == "vector")
                            {
                                bw.Write(tableSpace + "    file.Read(&colLength,sizeof(colLength));\n");
                                bw.Write(tableSpace + "    for(int i=0;i<colLength;i++){\n");
                                if (cols[i].value == "String" || cols[i].value == "string")
                                {
                                    bw.Write(tableSpace + "         object->" + cols[i].name + ".push_back(ReadString(file));\n");
                                }
                                else
                                {
                                    bw.Write(tableSpace + "        "+GetCPPType(cols[i].value)+" value;\n");
                                    bw.Write(tableSpace + "        file.Read(&value,sizeof(value));\n");
                                    bw.Write(tableSpace + "        object->"+cols[i].name+".push_back(value);\n");
                                }
                                bw.Write(tableSpace + "    }\n");
                            }
                            else
                            {
                                bw.Write(tableSpace + "    file.Read(&object->" + cols[i].name + ",sizeof(object->" + cols[i].name + "));\n");
                            }
                        }
                        bw.Write(tableSpace + "    mDatas.insert(std::pair<int," + name + "*>(object->id,object));\n");
                        if (isHaveNameValue)
                            bw.Write(tableSpace + "    mNames.insert(std::pair<const std::wstring," + name + "*>(object->name,object));\n");
                        if (isHaveColourValue)
                            bw.Write(tableSpace + "    mColours.insert(std::pair<const std::wstring," + name + "*>(object->colour,object));\n");
                        if (isHaveMapIdValue)
                            bw.Write(tableSpace + "    mMapids.insert(std::pair<const std::wstring," + name + "*>(object->mapid,object));\n");
                        //////////////////
                        tableSpace = "                ";
                        bw.Write(tableSpace + "}\n");
                        bw.Write(tableSpace + "file.Close();\n");
                        bw.Write(tableSpace + "return true;\n");
                        tableSpace = "            ";
                        bw.Write(tableSpace + "}\n");
                        tableSpace = "        ";
                        bw.Write(tableSpace + "}\n");
                        tableSpace = "    ";
                        bw.Write(tableSpace + "}\n");
                        bw.Write("}\n");
                        bw.Write("#endif // __" + name + "__h__\n");
                        bw.Close();
                    }
                    fs.Close();
                }

                // 写PKG文件.
                if (makelua)
                {
                    File.Delete(pkgPathFileName);
                    fs = File.Open(pkgPathFileName, FileMode.OpenOrCreate);

                    if (fs != null)
                    {
                        StreamWriter bw = new StreamWriter(fs);
                        if (bw != null)
                        {

                            String tableSpace;
                            bw.Write("namespace fire\n{\n");
                            tableSpace = "    ";
                            bw.Write(tableSpace + "namespace gsp\n");
                            bw.Write(tableSpace + "{\n");
                            tableSpace = "        ";
                            bw.Write(tableSpace + "namespace " + space + "\n");
                            bw.Write(tableSpace + "{\n");
                            tableSpace = "            ";
                            String structName = name;

                            bw.Write(tableSpace + "class " + name + "\n");
                            bw.Write(tableSpace + "{\n");
                            bw.Write(tableSpace + "    public:\n");
                            for (int i = 0; i < cols.Count; i++)
                            {
                                bw.Write(tableSpace + "    ");
                                String type = cols[i].type;
                                if (cols[i].type == "vector")
                                {
                                    type = cols[i].value;
                                    bw.Write("std::vector<");
                                }

                                bw.Write(GetCPPType(type));

                                if (cols[i].type == "vector")
                                    bw.Write(">");

                                bw.Write(" " + cols[i].name + ";\n");
                            }
                            bw.Write(tableSpace + "};\n");

                            bw.Write(tableSpace + "class " + name + "Table\n");
                            bw.Write(tableSpace + "{\n");
                            string className = name + "Table";
                            bw.Write("            public:\n");
                            bw.Write(tableSpace + structName + "& getRecorder(int id);\n");
 
                            if (isHaveNameValue)
                                bw.Write(tableSpace + "const " + name + "& getRecorderByName(const std::wstring& name);\n");
                            if (isHaveColourValue)
                                bw.Write(tableSpace + "const " + name + "& getRecorderByColour(const std::wstring& colour);\n");
                            if (isHaveMapIdValue)
                                bw.Write(tableSpace + "const " + name + "& getRecorderByMapid(const std::wstring& mapid);\n");
                            bw.Write(tableSpace + "virtual unsigned int getSize();\n");
                            bw.Write(tableSpace + "void getAllID(std::vector<int>& recordList);\n");
                            bw.Write(tableSpace + "};\n");
                            bw.Write(tableSpace + "static " + className + "&  Get" + className + "Instance();\n");
                            tableSpace = "        ";
                            bw.Write(tableSpace + "}\n");
                            tableSpace = "    ";
                            bw.Write(tableSpace + "}\n");
                            bw.Write("}\n");
                            bw.Close();
                        }
                        fs.Close();
                    }
                }
            }

            return true;
        }
        public void LinkBaseClass(List<TableBean> beans)
        {
            TableBean baseClass = FindTableBean(beans,baseclass);
            if(baseClass!=null)
            {
                baseClass.LinkBaseClass(beans);
                foreach (ColDef cd in baseClass.cols)
                {
                    if (!FindCol(cd.name))
                    {
                        ColDef copyCd = new ColDef();

                        copyCd.name = cd.name;
                        copyCd.type = cd.type;
                        copyCd.value = cd.value;
                        copyCd.fromCol = cd.fromCol;
                        copyCd.subCols = new List<SubColDef>();

                        foreach (SubColDef scd in cd.subCols)
                        {
                            SubColDef copySubColDef = new SubColDef();
                            copySubColDef.subFromCol = scd.subFromCol;
                            copySubColDef.data = new Dictionary<int, string>();
                            copyCd.subCols.Add(copySubColDef);
                        }

                        cols.Add(copyCd);
                    }
                }
            }
        }
        private bool WriteBinData(BinaryWriter bw,string type,string data)
        {
            try
            {
                if (type == "int")
                {
                    if (data.Length > 0)
                    {
                        int intData = Convert.ToInt32(data);
                        bw.Write(intData);
                    }
                    else
                        bw.Write((int)0);
                }
                else if(type=="long")
                {
                    if (data.Length > 0)
                    {
                        long longData = Convert.ToInt64(data);
                        bw.Write(longData);
                    }
                    else
                        bw.Write((long)0);
                }
                else if(type=="double")
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
                    return false;
            }
            catch (FormatException e)
            {
                return false;
            }
            return true;
        }
        private TableBean FindTableBean(List<TableBean> beans,string baseClassName)
        {
            for(int i=0;i<beans.Count;i++)
            {
                if (beans[i].name == baseClassName)
                    return beans[i];
            }
            return null;
        }
        private bool FindCol(string name)
        {
            for(int i=0;i<cols.Count;i++)
            {
                if(cols[i].name==name)
                {
                    return true;
                }
            }
            return false;
        }
        private uint GetColCheckNumber()//生成列版本.
        {
            uint colVersion = 0;
            uint allColCount = 0;
            for (int i = 0; i < cols.Count; i++)
            {
                for (int j = 0; j < cols[i].subCols.Count; j++)
                {
                    string type = cols[i].type;
                    if (cols[i].type == "vector")
                        type = cols[i].value;

                    if (type == "bool")
                        colVersion += 1;
                    else if (type == "int")
                        colVersion += 2;
                    else if (cols[i].type == "long")
                        colVersion += 3;
                    else if (cols[i].type == "double")
                        colVersion += 4;
                    else if (cols[i].type == "string" || cols[i].type == "String")
                        colVersion += 5;
                    colVersion += allColCount;
                    ++allColCount;
                }
            }
            colVersion += allColCount * 0x10000;

            return colVersion;
        }
    }
    public class XlsFileDataRow
    {
        public List<string> datas;//列数据.
    }
    public class XlsFileData
    {
        public int RowCount;
        public int ColCount;
        public List<XlsFileDataRow> rows;
        public bool LoadFromXlsFile(string xlsFileName)
        {
            rows = new List<XlsFileDataRow>();
            XSSFWorkbook HSSFWB;
            using (FileStream FS = new FileStream(xlsFileName, FileMode.Open, FileAccess.Read))
            {
                HSSFWB = new XSSFWorkbook(FS);
            }
            ISheet CurSheet = HSSFWB.GetSheetAt(0);

            RowCount = CurSheet.PhysicalNumberOfRows;
            ColCount = CurSheet.GetRow(0).PhysicalNumberOfCells;

            //依次读入数据.
            for (int i = CurSheet.FirstRowNum; i < RowCount + CurSheet.FirstRowNum; i++)
            {
                IRow CurRow = CurSheet.GetRow(i);
                if (CurRow == null)
                {
                    break;
                }
                XlsFileDataRow row = new XlsFileDataRow();
                row.datas = new List<string>();
                for(int j=0;j<CurRow.FirstCellNum;j++)
                {
                    row.datas.Add("n!u@l#l");
                }
                for (int j = CurRow.FirstCellNum; j < ColCount; j++)
                {
                    if (j < 0)
                        break;

                    ICell curCell = CurRow.GetCell(j);
                    if (curCell != null)
                    {
                        if ((curCell as XSSFCell).CellType != CellType.String)
                        {
                            curCell.SetCellType(CellType.String);
                        }
                        string CurCellText = curCell.ToString();
                        row.datas.Add(CurCellText);
                    }
                    else
                        row.datas.Add("n!u@l#l");
                }
                rows.Add(row);
            }
            return true;
        }
    }
    public class TableDef
    {
        public string space;//名字空间
        public List<TableBean> beans;

        public void LinkBaseClass()
        {
            for(int i=0;i<beans.Count;i++)
            {
                if (beans[i].baseclass.Length>0)
                {
                    beans[i].LinkBaseClass(beans);
                }
            }
        }

        public bool ReadXmlDef(string xmlDefFileName)//读入一个XML定义,并且写入到mTables中.
        {
            XmlDocument doc = new XmlDocument();
            doc.Load(xmlDefFileName);
            XmlNode xn = doc.SelectSingleNode("namespace");
            space = ((XmlElement)xn).GetAttribute("name");
            beans = new List<TableBean>();

            XmlNodeList xnl = xn.ChildNodes;
            foreach (XmlNode childXn in xnl)
            {
                //取属性.
                try
                {
                    XmlElement xe = (XmlElement)childXn;
                    TableBean tb = new TableBean();
                    tb.name = xe.GetAttribute("name").ToString();
                    tb.from = xe.GetAttribute("from").ToString();
                    tb.genxml = xe.GetAttribute("genxml").ToString();
                    tb.makelua = false;
                    if (xe.GetAttribute("makelua").ToString() != "lua")
                        tb.makelua = true;
                    tb.baseclass = xe.GetAttribute("baseclass").ToString();
                    tb.priority = xe.GetAttribute("priority").ToString();
                    //
                    XmlNodeList xnlEx = childXn.ChildNodes;
                    int colCount = 0;
                    for (int i = 0; i < xnlEx.Count; i++)
                    {
                        try
                        {
                            XmlElement xeEx = (XmlElement)xnlEx[i];
                            colCount++;
                        }
                        catch (System.InvalidCastException ex) { }
                    }

                    tb.cols = new List<ColDef>();
                    int index=0;
                    for (int i = 0; i < xnlEx.Count; i++)
                    {
                        ColDef col = new ColDef();
                        col.subCols = new List<SubColDef>();
                        try
                        {
                            XmlElement xeEx = (XmlElement)xnlEx[i];
                            col.name = xeEx.GetAttribute("name").ToString();
                            col.fromCol = xeEx.GetAttribute("fromCol").ToString();
                            col.type = xeEx.GetAttribute("type").ToString();
                            col.value = xeEx.GetAttribute("value").ToString();

                            if (col.fromCol.Length>0)
                            {
                                string s = "";
                                for (int j = 0; j < col.fromCol.Length; j++)
                                {
                                    if (col.fromCol[j] == ',')
                                    {
                                        SubColDef scd = new SubColDef();
                                        scd.subFromCol = s;
                                        scd.data = new Dictionary<int, string>();
                                        col.subCols.Add(scd);
                                        s = "";
                                    }
                                    else
                                        s += col.fromCol[j];
                                }
                                if (s.Length > 0)
                                {
                                    SubColDef scd = new SubColDef();
                                    scd.subFromCol = s;
                                    scd.data = new Dictionary<int, string>();
                                    col.subCols.Add(scd);
                                }
                                tb.cols.Add(col);
                                index++;
                            }
                            else
                            {                                
                                tb.cols.Add(col);
                            }
                        }
                        catch (System.InvalidCastException ex)
                        {

                        }
                    }
                    beans.Add(tb);
                }
                catch (System.InvalidCastException ex)
                {
                    //table=null; ?
                }
            }

            return true;
        }

    }
}
