using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSTools;

namespace ResourceCheck
{
    class CheckCommon
    {
        static public Option sOption;
        static public void CheckFileExist(string PathNameFull, List<string> FileNameList, List<string> FileNameListLive, List<string> FileNameListDead)
        {
            for (int i = 0; i < FileNameList.Count; i++)
            {
                string FileNameFull = AppExtName(FileNameList[i]);
                string PathFileNameFull = PathNameFull + FileNameFull;
                if (ToolsFile.FileExists(PathFileNameFull) == true)
                {
                    FileNameListLive.Add(CheckCommon.AppExtName(FileNameList[i]));
                }
                else
                {
                    FileNameListDead.Add(CheckCommon.AppExtName(FileNameList[i]));
                }
            }
        }
        static public string AppExtName(string PathFileName)
        {
            string Result;
            Result = sOption.GetFileNameExtByPathName(PathFileName);
            if (Result != "")
            {
                Result = PathFileName + "." + sOption.GetFileNameExtByPathName(PathFileName);    
            }
            else
            {
                Result = PathFileName;
            }
            return Result;
        }
        static public CheckBase GetCheck(string CheckType)
        {
            CheckBase CKBase = null;
            if (CheckType == "Table")
            {
                CKBase = new CheckTable();
            }
            else if (CheckType == "Effect")
            {
                CKBase = new CheckEffect();
            }
            else if (CheckType == "Particle")
            {
                CKBase = new CheckParticle();
            }
            else if (CheckType == "Animation")
            {
                CKBase = new CheckAnimation();
            }
            return CKBase;
        }
        static public CheckBase GetCheck2(string FileName)
        {
            CheckBase CKBase = null;
            if (FileName.StartsWith("geffect") == true)
            {
                CKBase = new CheckEffect();
            }
            else if (FileName.StartsWith("animation") == true || FileName.StartsWith("electric") == true)
            {
                CKBase = new CheckAnimation();
            }
            else if (FileName.StartsWith("particle/psl/") == true)
            {
                CKBase = new CheckParticle();
            }
            //else if (CheckType == "Animation")
            //{
            //    CKBase = new CheckAnimation();
            //}
            return CKBase;
        }
    }
}
