using System;
using System.IO;
using System.Xml;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NPOI.XSSF.UserModel;
using NPOI.SS.UserModel;
using ICSharpCode.SharpZipLib.Zip;

namespace LanguageConverters
{
    public class XlsData
    {
        public List<string> mData = new List<string>();
        public int rowCount;
        public int colCount;
        public Dictionary<string, int> mTitleIndex = new Dictionary<string, int>(); //KEY=名字 DATA=列索引
        public Dictionary<string, string> mTitleCommit = new Dictionary<string, string>();//KEY=名字 DATA=说明
        public Dictionary<int, int> mIdIndex = new Dictionary<int, int>();//key=ID,int=行索引
        public string mFileName;

        public void Clear()
        {
            mData.Clear();
            mTitleCommit.Clear();
            mTitleIndex.Clear();
        }
        public string GetFileName()
        {
            return mFileName;
        }
        public bool CheckType(string title,string type)
        {
            string localType;
            if (!mTitleCommit.TryGetValue(title, out localType))
                return true;
            return localType == type;
        }
        public bool GetXlsData(out string value,string title,int id,bool outputError=true)
        {
            int rowIndex;
            if(!mIdIndex.TryGetValue(id,out rowIndex))
            {
                //if(outputError)
                //    Form1.Error("获取文件<" + mFileName + ">的ID<" + id + ">失败\n");
                value = "";
                return false;
            }
            int colIndex;
            if(!mTitleIndex.TryGetValue(title,out colIndex))
            {
                //if(outputError)
                //    Form1.Error("获取文件<"+mFileName+">的标题<"+title+">失败\n");
                value = "";
                return false;
            }
            value = mData[rowIndex * colCount + colIndex];
            return true;
        }
        public bool makeTitleIndexs()
        {
            mTitleIndex.Clear();
            for (int i = 0; i < colCount; i++)
            {
                if (mData[i] != "" && !v.IsInvalidName(mData[i]))
                {
                    try
                    {
                        mTitleIndex.Add(mData[i], i);
                    }
                    catch(ArgumentException ae)
                    {
                        v.Error("表" + mFileName + "的表头中有同名的列，列名是"+mData[i]+"！异常信息="+ae.Message);
                        return false;
                    }
                }
            }
            return true;
        }
        public bool makeTitleIndexsCsv()
        {
            mTitleIndex.Clear();
            for (int i = 0; i < colCount; i++)
            {
                if (mData[i] != "\"\"" && !v.IsInvalidName(mData[i]))
                {
                    try
                    {
                        mTitleIndex.Add(mData[i], i);
                    }
                    catch (ArgumentException ae)
                    {
                        v.Error("表" + mFileName + "的表头中有同名的列，列名是" + mData[i] + "！异常信息=" + ae.Message);
                        return false;
                    }
                }
            }
            return true;
        }

    }


    public class xmlData
    {
        public Dictionary<string, string> mPath2Value = new Dictionary<string, string>();
        public string mFileName;

        public void Clear()
        {
            mFileName = "";
            mPath2Value.Clear();
        }
        public string GetFileName()
        {
            return mFileName;
        }
    }

    public class DataManager
    {
        public Dictionary<string, XlsData> mXls = new Dictionary<string, XlsData>();
        public Dictionary<string, xmlData> mXml = new Dictionary<string, xmlData>();
        public void Clear()
        {
            Dictionary<string, XlsData>.KeyCollection ids = mXls.Keys;
            foreach(string name in ids)
            {
                XlsData data;
                if(mXls.TryGetValue(name,out data))
                {
                    data.Clear();
                }
            }
            mXls.Clear();


            mXml.Clear();
        }
        public int GetFileCount()
        {
            return mXls.Count + mXml.Count;
        }
        public string GetColType(string sText)
        {
            string sType="";
            if(sText.Length>0&&sText[0]=='<')
            {
                for(int i=1;i<sText.Length;i++)
                {
                    if(sText[i]=='>')
                    {
                        return sType;
                    }
                    sType+=sText[i];
                }
            }
            return sType;
        }
        public string GetColData(string sText,string sType)
        {
            string sResult = "";
            if(sText.Length>sType.Length)
            {
                for(int i=sType.Length+2;i<sText.Length;i++)
                {
                    if (sText[i] == '<')
                        return sResult;
                    sResult += sText[i];
                }
            }
            return "";
        }
        public bool saveXmlFileForNewValue(string xmlPathFileName, Dictionary<string, string> _NeedModifyData)
        {



            XmlDocument doclayout = new XmlDocument();
            try
            {
                doclayout.Load(xmlPathFileName);
            }
            catch (System.Xml.XmlException e)
            {
                v.Info("Read Layout File Error:->" + xmlPathFileName + " error:->" + e.Message);
                return false;
            }

            for(int i = 0; i < _NeedModifyData.Count; ++ i)
            {
                KeyValuePair<string,string> keyvalue = _NeedModifyData.ElementAt(i);

                List<string> pPathList = v.stovs(keyvalue.Key, '\\');
                pPathList.RemoveAt(0);

                foreach (XmlNode node in doclayout.DocumentElement.ChildNodes)
                {
                    parsexmlnodechild_FindAndModify(node, pPathList, keyvalue.Value);
                }
            }
            

            
            using (FileStream FSO = new FileStream(xmlPathFileName, FileMode.Create, FileAccess.Write))
            {
                doclayout.Save(FSO);
                FSO.Close();
            }

            return true;
        }
        public bool saveXlsFileForNewValue(string xlsPathFileName, Dictionary<int, string> _NeedModifyData)
        {
            XSSFWorkbook HSSFWB;
            FileStream FS = null;
            using (FS = new FileStream(xlsPathFileName, FileMode.Open, FileAccess.Read))
            {
                try
                {
                    HSSFWB = new XSSFWorkbook(FS);
                    FS.Close();
                }
                catch (InvalidOperationException oe)
                {
                    v.Error("加载表" + xlsPathFileName + "报告无效操作失败! 异常信息=" + oe.Message);
                    return false;
                }
                catch (ZipException ze)
                {
                    v.Error("加载表" + xlsPathFileName + "报告ZIP失败! 异常信息=" + ze.Message);
                    return false;
                }
            }

            ISheet curSheet = HSSFWB.GetSheetAt(0);
            int rowCount = curSheet.PhysicalNumberOfRows;
            int colCount = curSheet.GetRow(0).PhysicalNumberOfCells;


            for (int mm = 0; mm < _NeedModifyData.Count; ++mm)
            {
                KeyValuePair<int,string> idvalue = _NeedModifyData.ElementAt(mm);
                int nValueIndex = idvalue.Key;
                string sNewValue = idvalue.Value;

                int nDataCount = 0;
                for (int i = curSheet.FirstRowNum; i < rowCount + curSheet.FirstRowNum; i++)
                {
                    IRow CurRow = curSheet.GetRow(i);
                    if (CurRow == null)
                    {
                        break;
                    }
                    for (int j = CurRow.FirstCellNum; j < colCount; j++)
                    {
                        if (j < 0)
                            break;

                        ICell curCell = CurRow.GetCell(j);
                        if (curCell != null)
                        {
                            if (nDataCount++ == nValueIndex)
                            {
                                curCell.SetCellValue(sNewValue);
                            }
                        }
                        else
                        {
                            if (nDataCount++ == nValueIndex)
                            {
                                curCell = CurRow.CreateCell(j);
                                curCell.SetCellType(CellType.String);
                                curCell.SetCellValue(sNewValue);
                            }
                        }
                    }
                }

            }


            using (FileStream FSO = new FileStream(xlsPathFileName, FileMode.Create, FileAccess.Write))
            {
                HSSFWB.Write(FSO);
                FSO.Close();
            }

            return true;

        }
        public bool saveXlsFileForNewValue(string xlsPathFileName,int nValueIndex,string sNewValue)
        {

            XSSFWorkbook HSSFWB;
            FileStream FS = null;
            using (FS = new FileStream(xlsPathFileName, FileMode.Open, FileAccess.Read))
            {
                try
                {
                    HSSFWB = new XSSFWorkbook(FS);
                    FS.Close();
                }
                catch (InvalidOperationException oe)
                {
                    v.Error("加载表" + xlsPathFileName + "报告无效操作失败! 异常信息=" + oe.Message);
                    return false;
                }
                catch (ZipException ze)
                {
                    v.Error("加载表" + xlsPathFileName + "报告ZIP失败! 异常信息=" + ze.Message);
                    return false;
                }
            }

            ISheet curSheet = HSSFWB.GetSheetAt(0);
            int rowCount = curSheet.PhysicalNumberOfRows;
            int colCount = curSheet.GetRow(0).PhysicalNumberOfCells;

            int nDataCount = 0;
            for (int i = curSheet.FirstRowNum; i < rowCount + curSheet.FirstRowNum; i++)
            {
                IRow CurRow = curSheet.GetRow(i);
                if (CurRow == null)
                {
                    break;
                }
                for (int j = CurRow.FirstCellNum; j < colCount; j++)
                {
                    if (j < 0)
                        break;

                    ICell curCell = CurRow.GetCell(j);
                    if (curCell != null)
                    {
                        if(nDataCount++ == nValueIndex)
                        {
                            curCell.SetCellValue(sNewValue);
                        }
                    }
                    else
                    {
                        if (nDataCount++ == nValueIndex)
                        {
                            curCell = CurRow.CreateCell(j);
                            curCell.SetCellType(CellType.String);
                            curCell.SetCellValue(sNewValue);
                        }
                    }
                }
            }
            



            using (FileStream FSO = new FileStream(xlsPathFileName, FileMode.Create, FileAccess.Write))
            {
                HSSFWB.Write(FSO);
                FSO.Close();
            }
            
            return true;
        }
        
        public string GetLayoutXmlNodeName(XmlNode node)
        {
            if (node.Attributes != null && node.Attributes.Count == 2)
            {
                if (node.Attributes[0].Name == "Type" && node.Attributes[1].Name == "Name")
                {
                    return node.Attributes[1].Value;
                }
            }
            return "";
        }
        public string FindXmlNodeNamePath(string sPreName,XmlNode node)
        {
            string pathname;
            if(sPreName == "")
            {
                pathname = GetLayoutXmlNodeName(node);
            }
            else
            {
                pathname = sPreName + "\\" + GetLayoutXmlNodeName(node);
            }

            if(node.ParentNode != null)
            {
                return FindXmlNodeNamePath(pathname, node.ParentNode);
            }

            List<string> pathlist = v.stovs(pathname, '\\');
            pathlist.Reverse();
            pathname = v.vstos(pathlist, '\\');

            return pathname;
        }
        public void parsexmlnodechild_FindAndModify(XmlNode node, List<string> pathlist,string sNewValue)
        {
            if (node.ChildNodes.Count <= 0 || pathlist.Count <= 0)
            {
                return;
            }

            if (node.Attributes != null && node.Attributes.Count == 2)
            {
                if (node.Name == "Window" && node.Attributes[1].Value == pathlist[0])
                {
                    pathlist.RemoveAt(0);

                    if (pathlist.Count == 0)
                    {
                        foreach (XmlNode child_ in node.ChildNodes)
                        {
                            if (child_.Attributes != null && child_.Attributes.Count == 2)
                            {
                                string attName = child_.Attributes[0].Name;
                                string attValue = child_.Attributes[0].Value;
                                if (attName == "Name" && attValue == "Text")
                                {
                                    child_.Attributes[1].Value = sNewValue;
                                    return;
                                }
                            }
                        }

                        return;
                    }
                }
            }

            foreach (XmlNode child in node.ChildNodes)
            {
                if (child.ChildNodes.Count > 0)
                {
                    parsexmlnodechild_FindAndModify(child, pathlist, sNewValue);
                }
            }
        }
        public void parsexmlnodechild(XmlNode node,xmlData pXmlData)
        {
            if (node.ChildNodes.Count <= 0)
            {
                return;
            }

            foreach (XmlNode child in node.ChildNodes)
            {
                if (child.Attributes != null && child.Attributes.Count == 2)
                {
                    string attName = child.Attributes[0].Name;
                    string attValue = child.Attributes[0].Value;
                    string attTextValue = "";
                    if (attName == "Name" && attValue == "Text")
                    {
                        attTextValue = child.Attributes[1].Value;
                        int ValueCount = 0;
                        for (int j = 0; j < attTextValue.Length; ++j)
                        {
                            if ((int)attTextValue[j] > 127)
                            {
                                // is not ascii
                                ValueCount++;
                                break;
                            }
                        }
                        if(ValueCount > 0)
                        {
                            // i am ckj
                            

                            string pathname = FindXmlNodeNamePath("", child);
                            pXmlData.mPath2Value.Add(pathname, attTextValue);

                            v.Info("find ckj:->" + pathname+"->"+ attTextValue);
                        }
                    }
                }

                if (child.ChildNodes.Count > 0)
                {
                    parsexmlnodechild(child, pXmlData);
                }
            }
        }
        public bool loadXmlFileAndParseCKJText(string xmlPathFileName)
        {
            int nStartPos = xmlPathFileName.IndexOf("~$");//滤过临时文件
            if (nStartPos != -1)
            {
                return true;
            }
            v.Info("开始加载" + xmlPathFileName);

            XmlDocument doclayout = new XmlDocument();
            try
            {
                doclayout.Load(xmlPathFileName);
            }
            catch(System.Xml.XmlException e)
            {
                v.Info("Read Layout File Error:->" + xmlPathFileName+" error:->"+e.Message);
                return false;
            }



            xmlData onexmlfile = new xmlData();

            foreach(XmlNode node in doclayout.DocumentElement.ChildNodes){
                parsexmlnodechild(node, onexmlfile);
            }

            if (onexmlfile.mPath2Value.Count > 0)
                mXml.Add(xmlPathFileName, onexmlfile);

            return true;
        }
        public bool loadXlsFile(string xlsPathFileName)
        {
            int nStartPos = xlsPathFileName.IndexOf("~$");//滤过临时文件
            if (nStartPos != -1)
            {
                return true;
            }

            v.Info("开始加载" + xlsPathFileName);
            XlsData data = new XlsData();

            XSSFWorkbook HSSFWB;
            try
            {
                using (FileStream FS = new FileStream(xlsPathFileName, FileMode.Open, FileAccess.Read))
                {
                    try
                    {
                        HSSFWB = new XSSFWorkbook(FS);
                        FS.Close();
                    }
                    catch (InvalidOperationException oe)
                    {
                        v.Error("加载表" + xlsPathFileName + "报告无效操作失败! 异常信息=" + oe.Message);
                        return false;
                    }
                    catch (ZipException ze)
                    {
                        v.Error("加载表" + xlsPathFileName + "报告ZIP失败! 异常信息=" + ze.Message);
                        return false;
                    }
                }
            }
            catch (System.IO.IOException e)
            {
                v.Error("打开表(" + xlsPathFileName + ")失败! 异常信息=" + e.Message);
                return false;
            }

            ISheet curSheet = HSSFWB.GetSheetAt(0);
            data.rowCount = curSheet.PhysicalNumberOfRows;
            data.colCount = curSheet.GetRow(0).PhysicalNumberOfCells;

            //依次读入数据.
            for (int i = curSheet.FirstRowNum; i < data.rowCount + curSheet.FirstRowNum; i++)
            {
                IRow CurRow = curSheet.GetRow(i);
                if (CurRow == null)
                {
                    break;
                }
                for (int j = 0; j < CurRow.FirstCellNum; j++)
                {
                    data.mData.Add("n!u@l#l");
                }
                for (int j = CurRow.FirstCellNum; j < data.colCount; j++)
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
                       
                        //检查标题的特殊约定... 如果策划使用<类型说明>列名<类型说明>的方式提示GBEANS里的类型必须是这个，而且GBEANS里的定义不一致，就在这里发出警告。
                        if(i==0)
                        {
                            if(CurCellText.Length>0&&CurCellText[0]=='<')
                            {
                                string CurCellType = GetColType(CurCellText);
                                CurCellText = GetColData(CurCellText,CurCellType);
                                data.mTitleCommit.Add(CurCellText, CurCellType);
                            }
                        }

                        data.mData.Add(CurCellText);
                    }
                    else
                        data.mData.Add("n!u@l#l");
                }
            }
            
            data.mFileName = xlsPathFileName;
            if(!data.makeTitleIndexs())
            {
                return false;
            }

            string absPath;
            if(!v.GetAbsDataPath(out absPath))
            {
                return false;
            }
            List<string> vsAbsPath = v.stovs(absPath, '\\');
            List<string> vsAbsPathFile = v.stovs(data.mFileName, '\\');
            if(vsAbsPathFile.Count<vsAbsPath.Count)
            {
                return false;
            }
            for (int i = 0; i < vsAbsPath.Count;i++ )
            {
                vsAbsPathFile.Remove(vsAbsPathFile[0]);
            }

            //vsAbsPathFile.RemoveAt(0);
            vsAbsPathFile.RemoveAt(0);
            data.mFileName = v.vstos(vsAbsPathFile, '\\');
                    /*
                               string strFilePathName;
                               if(!Form1.GetAbsPathDataFileName(out strFilePathName, data.GetFileName()))
                               {
                                   return false;
                               }
                               data.mFileName = strFilePathName;*/
            //int nFileTypeIndex = data.mFileName.IndexOf(".");
            //data.mFileName = data.mFileName.Substring(0, nFileTypeIndex);
            if(data.mFileName == "")
            {
                v.Info("null file name:->" + xlsPathFileName);
            }
            XlsData testData;
            if(mXls.TryGetValue(data.mFileName,out testData))
            {
                testData.Clear();
                mXls.Remove(data.mFileName);
            }
            mXls.Add(data.mFileName, data);
            return true;
        }
        public bool loadMultiPageXlsFile(string xlsPathFileName)
        {
            int nStartPos = xlsPathFileName.IndexOf("~$");//滤过临时文件
            if (nStartPos != -1)
            {
                return true;
            }

            v.Info("开始加载" + xlsPathFileName);
            

            XSSFWorkbook HSSFWB;
            try
            {
                using (FileStream FS = new FileStream(xlsPathFileName, FileMode.Open, FileAccess.Read))
                {
                    try
                    {
                        HSSFWB = new XSSFWorkbook(FS);
                        FS.Close();
                    }
                    catch (InvalidOperationException oe)
                    {
                        v.Error("加载表" + xlsPathFileName + "报告无效操作失败! 异常信息=" + oe.Message);
                        return false;
                    }
                    catch (ZipException ze)
                    {
                        v.Error("加载表" + xlsPathFileName + "报告ZIP失败! 异常信息=" + ze.Message);
                        return false;
                    }
                }
            }
            catch (System.IO.IOException e)
            {
                v.Error("表(" + xlsPathFileName + ")无法打开! 异常信息=" + e.Message);
                return false;
            }

            Dictionary<int, string> pNameArray = new Dictionary<int, string>();
            {
                // read name index
                ISheet curSheet = HSSFWB.GetSheet("nameindex");
                int rowCount = curSheet.PhysicalNumberOfRows;
                int colCount = curSheet.GetRow(0).PhysicalNumberOfCells;

                for (int i = curSheet.FirstRowNum; i < rowCount + curSheet.FirstRowNum; i++)
                {
                    IRow CurRow = curSheet.GetRow(i);
                    if (CurRow == null)
                    {
                        break;
                    }
                    int nIndex = int.Parse(CurRow.GetCell(0).ToString());
                    string sValue = CurRow.GetCell(1).ToString();
                    pNameArray[nIndex] = sValue;
                }
            }


            for(int sheetindex = 0; sheetindex < HSSFWB.NumberOfSheets; ++ sheetindex)
            {
                XlsData data = new XlsData();
                ISheet curSheet = HSSFWB.GetSheetAt(sheetindex);
                if (curSheet.SheetName == "nameindex")
                    continue;

                data.rowCount = curSheet.PhysicalNumberOfRows;
                data.colCount = curSheet.GetRow(0).PhysicalNumberOfCells;

                //依次读入数据.
                for (int i = curSheet.FirstRowNum; i < data.rowCount + curSheet.FirstRowNum; i++)
                {
                    IRow CurRow = curSheet.GetRow(i);
                    if (CurRow == null)
                    {
                        break;
                    }
                    for (int j = 0; j < CurRow.FirstCellNum; j++)
                    {
                        data.mData.Add("n!u@l#l");
                    }
                    for (int j = CurRow.FirstCellNum; j < data.colCount; j++)
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

                            ////检查标题的特殊约定... 如果策划使用<类型说明>列名<类型说明>的方式提示GBEANS里的类型必须是这个，而且GBEANS里的定义不一致，就在这里发出警告。
                            //if (i == 0)
                            //{
                            //    if (CurCellText.Length > 0 && CurCellText[0] == '<')
                            //    {
                            //        string CurCellType = GetColType(CurCellText);
                            //        CurCellText = GetColData(CurCellText, CurCellType);
                            //        data.mTitleCommit.Add(CurCellText, CurCellType);
                            //    }
                            //}

                            data.mData.Add(CurCellText);
                        }
                        else
                            data.mData.Add("n!u@l#l");
                    }
                }

                data.mFileName = pNameArray[int.Parse(curSheet.SheetName)];

                


                XlsData testData;
                if (mXls.TryGetValue(data.mFileName, out testData))
                {
                    testData.Clear();
                    mXls.Remove(data.mFileName);
                }
                mXls.Add(data.mFileName, data);
                v.Info(data.mFileName + "加载成功");
            }

            
            return true;
        }
        public bool LoadTxtFile(string txt)
        {
            return LoadCsvFile(txt);
        }
        public bool LoadCsvFile(string strCsvPathFileName)
        {
            v.Info("开始加载" + strCsvPathFileName);
            XlsData data = new XlsData();
            try
            {
                FileStream fs = File.Open(strCsvPathFileName, FileMode.Open);
                if (fs != null)
                {
                    StreamReader sr = new StreamReader(fs, Encoding.GetEncoding("GB2312"));

                    if (sr != null)
                    {
                        
                        string strReadLine = "";
                        strReadLine = sr.ReadLine();
                        if (strReadLine != null)
                        {
                            string[] strs = strReadLine.Split(',');
                            List<string> strListData = new List<string>();
                            if (!AddNewDataLine(strReadLine, out strListData))
                            {
                                v.Info(strCsvPathFileName + "数据有问题");
                                return false;
                            }
                            data.colCount = strListData.Count;
                            for (int ii = 0; ii < strListData.Count; ii++)
                            {
                                if (strListData[ii].Length > 0 )
                                {
                                    data.mData.Add(strListData[ii]);
                                }
                                else
                                {
                                    data.mData.Add("n!u@l#l");
                                }

                            }
                        }
                        int nRow = 1;
                        while (strReadLine != null)
                        {
                            strReadLine = sr.ReadLine();
                            if (strReadLine != null)
                            {
                                string[] strs = strReadLine.Split(',');
                                List<string> strListData = new List<string>();
                                if (!AddNewDataLine(strReadLine, out strListData))
                                {
                                    v.Info(strCsvPathFileName + "数据有问题");
                                    return false;
                                }
                                for (int ii = 0; ii < strListData.Count; ii++)
                                {
                                    if (strListData[ii].Length > 0 && strListData[ii] != "\"\"")
                                    {
                                        data.mData.Add(strListData[ii]);
                                    }
                                    else
                                    {
                                        data.mData.Add("n!u@l#l");
                                    }

                                }
                                nRow++;
                            }

                        }

                        data.rowCount = nRow;

                        data.mFileName = strCsvPathFileName;

                        if (!data.makeTitleIndexsCsv())
                        {
                            return false;
                        }

                        string absPath;
                        if (!v.GetAbsDataPath(out absPath))
                        {
                            return false;
                        }
                        List<string> vsAbsPath = v.stovs(absPath, '\\');
                        List<string> vsAbsPathFile = v.stovs(data.mFileName, '\\');
                        if (vsAbsPathFile.Count < vsAbsPath.Count)
                        {
                            return false;
                        }
                        for (int i = 0; i < vsAbsPath.Count; i++)
                        {
                            vsAbsPathFile.Remove(vsAbsPathFile[0]);
                        }

                        data.mFileName = v.vstos(vsAbsPathFile, '\\');
                        int nFileTypeIndex = data.mFileName.IndexOf(".");
                        data.mFileName = data.mFileName.Substring(0, nFileTypeIndex);
                        XlsData testData;
                        if (mXls.TryGetValue(data.mFileName, out testData))
                        {
                            testData.Clear();
                            mXls.Remove(data.mFileName);
                        }
                        mXls.Add(data.mFileName, data);

                        sr.Close();
                    }
                    fs.Close();
                    v.Info("文件:" + System.IO.Path.GetFullPath(strCsvPathFileName) + "加载成功");
                }
            }
            catch (InvalidOperationException oe)
            {
                v.Error("加载表" + strCsvPathFileName + "报告无效操作失败! 异常信息=" + oe.Message);
                return false;
            }

            return true;
        }
        private bool IfOddQuota(string dataLine)
        {
            int quotaCount;
            bool oddQuota;
            quotaCount = 0;
            for (int i = 0; i < dataLine.Length; i++)
            {
                if (dataLine[i] == '\"')
                {
                    quotaCount++;
                }
            }
            oddQuota = false;
            if (quotaCount % 2 == 1)
            {
                oddQuota = true;
            }
            return oddQuota;
        }
        private bool IfOddStartQuota(string dataCell)
        {
            int quotaCount;
            bool oddQuota;
            quotaCount = 0;
            for (int i = 0; i < dataCell.Length; i++)
            {
                if (dataCell[i] == '\"')
                {
                    quotaCount++;
                }
                else
                {
                    break;
                }
            }
            oddQuota = false;
            if (quotaCount % 2 == 1)
            {
                oddQuota = true;
            }
            return oddQuota;
        }
        private bool IfOddEndQuota(string dataCell)
        {
            int quotaCount;
            bool oddQuota;
            quotaCount = 0;
            for (int i = dataCell.Length - 1; i >= 0; i--)
            {
                if (dataCell[i] == '\"')
                {
                    quotaCount++;
                }
                else
                {
                    break;
                }
            }
            oddQuota = false;
            if (quotaCount % 2 == 1)
            {
                oddQuota = true;
            }
            return oddQuota;
        }
        private bool AddNewDataLine(string newDataLine, out List<string> colAL)
        {
            colAL = new List<string>();
            string[] dataArray = newDataLine.Split(',');
            bool oddStartQuota;        //是否以奇数个引号开始
            string cellData;
            oddStartQuota = false;
            cellData = "";
            for (int i = 0; i < dataArray.Length; i++)
            {
                if (oddStartQuota)
                {
                    //因为前面用逗号分割,所以要加上逗号
                    cellData += "," + dataArray[i];
                    //是否以奇数个引号结尾
                    if (IfOddEndQuota(dataArray[i]))
                    {
                        colAL.Add(GetHandleData(cellData));
                        oddStartQuota = false;
                        continue;
                    }
                }
                else
                {
                    //是否以奇数个引号开始
                    if (IfOddStartQuota(dataArray[i]))
                    {
                        //是否以奇数个引号结尾,不能是一个双引号,并且不是奇数个引号
                        if (IfOddEndQuota(dataArray[i]) && dataArray[i].Length > 2 && !IfOddQuota(dataArray[i]))
                        {
                            colAL.Add(GetHandleData(dataArray[i]));
                            oddStartQuota = false;
                            continue;
                        }
                        else
                        {
                            oddStartQuota = true;
                            cellData = dataArray[i];
                            continue;
                        }
                    }
                    else
                    {
                        colAL.Add(GetHandleData(dataArray[i]));
                    }
                }
            }
            if (oddStartQuota)
            {
                return false;
            }
            return true;
        }
        private string GetHandleData(string fileCellData)
        {
            if (fileCellData == "")
            {
                return "";
            }
            if (IfOddStartQuota(fileCellData))
            {
                if (IfOddEndQuota(fileCellData))
                {
                    return fileCellData.Substring(1, fileCellData.Length - 2).Replace("\"\"", "\"");
                }
                else
                {
                    throw new Exception("数据引号无法匹配" + fileCellData);
                }
            }
            else
            {
                if (fileCellData.Length > 2 && fileCellData[0] == '\"')
                {
                    fileCellData = fileCellData.Substring(1, fileCellData.Length - 2).Replace("\"\"", "\"");
                }
            }
            return fileCellData;
        }
    }

}
