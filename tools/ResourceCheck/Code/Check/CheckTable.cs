using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSTools;

namespace ResourceCheck
{
    class CheckTableOne
    {
        static public void CheckFile(string FileNameFull, List<string> ColNameList, List<string> FileNameListLive, List<string> FileNameListDead)
        {
            List<string> FileNameList;
            FileNameList = new List<string>();
            ToolsTable TT = new ToolsTable(FileNameFull);
            for (int i = 0; i < ColNameList.Count; i++)
            {
                int CurColIndex = TT.ColIndexOfName(ColNameList[i]);
                if (CurColIndex < 0)
                {
                    continue;
                }
                for (int i2 = 0; i2 < TT.GetDataRowCount(); i2++)
                {
                    string CurFileName = TT.GetDataCell(i2, CurColIndex);
                    CurFileName = CurFileName.Replace("\\", "/");
                    if (CurFileName != "")
	                {
                        if (CurFileName.EndsWith("_") == true)
                        {
                            FileNameList.Add(CurFileName + "1");
                            FileNameList.Add(CurFileName + "2");
                        }
                        else
                        {
                            FileNameList.Add(CurFileName);
                        }
	                }
                }
            }
            string PathNameFull = ToolsFileStr.GetFullName(CheckCommon.sOption.mAppPath, CheckCommon.sOption.mEffectPath);
            PathNameFull = ToolsFileStr.DirToPath(PathNameFull);
            CheckCommon.CheckFileExist(PathNameFull, FileNameList, FileNameListLive, FileNameListDead);
        }
    }
    public class CheckTable : CheckBase
    {
        public CheckTable()
        {
            mCheckType = "Table";
        }
        public override int GetFileCount()
        {
            return CheckCommon.sOption.mTableFileInfo.mFileInfoOne.Count;
        }
        public override string GetFileName(int Index)
        {
            return CheckCommon.sOption.mTableFileInfo.mFileInfoOne[Index].mFileName;
        }
        public List<string> GetTableColName(int Index)
        {
            return CheckCommon.sOption.mTableFileInfo.mFileInfoOne[Index].mColName;
        }
        public override void CheckOne(int Index, List<string> FileNameListLive, List<string> FileNameListDead)
        {
            base.CheckOne(Index, FileNameListLive, FileNameListDead);
            CheckTableOne.CheckFile(
                CheckCommon.sOption.mTableFileInfo.mPathNameFull + CheckCommon.sOption.mTableFileInfo.mFileInfoOne[Index].mFileName,
                CheckCommon.sOption.mTableFileInfo.mFileInfoOne[Index].mColName, FileNameListLive, FileNameListDead);
        }
    }
}