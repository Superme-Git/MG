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

namespace CheckExcel
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
                if(outputError)
                    MainWindow.Error("获取文件<" + mFileName + ">的ID<" + id + ">失败\n");
                value = "";
                return false;
            }
            int colIndex;
            if(!mTitleIndex.TryGetValue(title,out colIndex))
            {
                if(outputError)
                    MainWindow.Error("获取文件<"+mFileName+">的标题<"+title+">失败\n");
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
                if (mData[i] != "" && !MainWindow.IsInvalidName(mData[i]))
                {
                    try
                    {
                        mTitleIndex.Add(mData[i], i);
                    }
                    catch(ArgumentException ae)
                    {
                        MainWindow.Error("表" + mFileName + "的表头中有同名的列，列名是"+mData[i]+"！异常信息="+ae.Message);
                        return false;
                    }
                }
            }
            return true;
        }
        public bool makeIdIndexs(string idTitle)
        {
            mIdIndex.Clear();
            int idCol;
            if (mTitleIndex.TryGetValue(idTitle, out idCol))
            {
                for (int i = 1; i < rowCount; i++)
                {
                    int index = i * colCount + idCol;
                    if (index < mData.Count())
                    {
                        string idString = mData[index];
                        if (idString != "" && !MainWindow.IsInvalidName(idString))
                        {
                            try
                            {
                                    int id = Convert.ToInt32(mData[i * colCount + idCol]);
                                    mIdIndex.Add(id, i);
                            }
                            catch (ArgumentException ae)
                            {
                                MainWindow.Error("表" + mFileName + "的中发现ID值" + idString + "有冲突，异常信息=" + ae.Message);
                                return false;
                            }
                            catch(FormatException fe)
                            {
                                MainWindow.Error("表" + mFileName + "的ID值" + idString + "不是一个合法的数字,异常信息="+fe.Message);
                            }
                        }
                    }
                }
            }
            else
            {
                MainWindow.Error("生成表<"+mFileName+">的ID索引失败，因为找不到标题<"+idTitle+">");
                return false;
            }
            return true;
        }
    }
    public class DataManager
    {
        public Dictionary<string, XlsData> mXls=new Dictionary<string,XlsData>();
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
        public bool loadXlsFile(string xlsPathFileName)
        {
            int nStartPos = xlsPathFileName.IndexOf("~$");//滤过临时文件
            if (nStartPos != -1)
            {
                return true;
            }

            MainWindow.Info("开始加载" + xlsPathFileName);
            XlsData data = new XlsData();

            XSSFWorkbook HSSFWB;
            using (FileStream FS = new FileStream(xlsPathFileName, FileMode.Open, FileAccess.Read,FileShare.ReadWrite))
            {
                try
                {
                    HSSFWB = new XSSFWorkbook(FS);
                }
                catch(InvalidOperationException oe)
                {
                    MainWindow.Error("加载表" + xlsPathFileName + "报告无效操作失败! 异常信息="+oe.Message);
                    return false;
                }
                catch(ZipException ze)
                {
                    MainWindow.Error("加载表" + xlsPathFileName + "报告ZIP失败! 异常信息=" + ze.Message);
                    return false;
                }
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
            if(!MainWindow.GetAbsDataPath(out absPath))
            {
                return false;
            }
            List<string> vsAbsPath = MainWindow.stovs(absPath, '\\');
            List<string> vsAbsPathFile = MainWindow.stovs(data.mFileName, '\\');
            if(vsAbsPathFile.Count<vsAbsPath.Count)
            {
                return false;
            }
            for (int i = 0; i < vsAbsPath.Count;i++ )
            {
                vsAbsPathFile.Remove(vsAbsPathFile[0]);
            }

            data.mFileName = MainWindow.vstos(vsAbsPathFile, '\\');
                    /*
                               string strFilePathName;
                               if(!MainWindow.GetAbsPathDataFileName(out strFilePathName, data.GetFileName()))
                               {
                                   return false;
                               }
                               data.mFileName = strFilePathName;*/
            XlsData testData;
            if(mXls.TryGetValue(data.mFileName,out testData))
            {
                testData.Clear();
                mXls.Remove(data.mFileName);
            }
            mXls.Add(data.mFileName, data);
            return true;
        }
    }
}
