using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;

namespace CSTools
{
    class ToolsFileStr
    {
        [DllImport("shlwapi")]
        private static extern bool PathIsRelative(string lpszPath);

        static public bool NameIsOf(string OfName)
        {
            bool Result = PathIsRelative(OfName);
            return Result;
        }
        static public string DirToPath(string V)
        {
            if (V == "")
            {
                return "";
            }
            string Result = V;
            if (!Result.EndsWith("\\"))
            {
                Result += "\\";
            }
            return Result;
        }
        static public string PathToDir(string V)
        {
            string Result = V;
            while (Result != "" && Result.EndsWith("\\"))
            {
                Result = Result.Substring(0, Result.Length - 1);
            }
            return Result;
        }
        static public string GetDir(string FullName)
        {
            string Result = Path.GetDirectoryName(FullName);
            return Result;
        }
        static public string GetPath(string FullName)
        {
            string Result = DirToPath(GetDir(FullName));
            return Result;
        }
        static public string GetNameExt(string FullName)
        {
            string Result = Path.GetFileName(FullName);
            return Result;
        }
        static public string GetName(string FullName)
        {
            string Result = Path.GetFileNameWithoutExtension(FullName);
            return Result;
        }
        static public string GetExt(string FullName,bool IncDot = true)
        {
            if (!Path.HasExtension(FullName))
            {
                return "";
            }
            string Result = Path.GetExtension(FullName);
            if (!IncDot)
            {
                Result = Result.Substring(1);
            }
            return Result;
        }
        static public string GetFullName(string BasePath, string OfName)
        {
            if (BasePath == "" || OfName == "")
            {
                return BasePath;
            }
            if (!NameIsOf(OfName))
            {
                return BasePath;
            }
            string Result = BasePath;
            string[] SplitDelimit = new string[2];
            SplitDelimit[0] = "\\";
            SplitDelimit[1] = "/";

            string[] BasePathArr = BasePath.Split(SplitDelimit, StringSplitOptions.RemoveEmptyEntries);
            string[] OfNameArr = OfName.Split(SplitDelimit, StringSplitOptions.RemoveEmptyEntries);

            List<string> BasePathList = BasePathArr.ToList<string>();
            List<string> OfNameList = OfNameArr.ToList<string>();

            for (int i = 0; i < OfNameArr.Length; i++)
            {
                if (OfNameArr[i] == "..")
                {
                    if (BasePathList.Count > 0)
                    {
                        BasePathList.RemoveAt(BasePathList.Count - 1);
                        OfNameList.RemoveAt(0);
                    }
                    else
                    {
                        return Result;
                    }
                }
            }
            Result = "";
            for (int i = 0; i < BasePathList.Count; i++)
            {
                if (i != 0)
                {
                    Result += SplitDelimit[0];
                }
                Result += BasePathList[i];
            }
            for (int i = 0; i < OfNameList.Count; i++)
            {
                Result += SplitDelimit[0] + OfNameList[i];
            }
            return Result;
        }
        static public string GetOfName(string BasePath, string FullName)
        {
            if (BasePath == "" || FullName == "")
            {
                return BasePath;
            }
            if (BasePath == FullName)
            {
                return "";
            }
            string Result = BasePath;
            string[] SplitDelimit = new string[2];
            SplitDelimit[0] = "\\";
            SplitDelimit[1] = "/";

            string[] BasePathArr = BasePath.Split(SplitDelimit.ToArray(), StringSplitOptions.RemoveEmptyEntries);
            string[] FullNameArr = FullName.Split(SplitDelimit.ToArray(), StringSplitOptions.RemoveEmptyEntries);

            List<string> BasePathList = BasePathArr.ToList<string>();
            List<string> FullNameList = FullNameArr.ToList<string>();

            int SameCount = 0;
            int BackupCount = 0;
            for (int i = 0; i < FullNameArr.Length; i++)
            {
                if (BasePathArr.Length > i)
                {
                    if (FullNameArr[i] == BasePathArr[i])
                    {
                        SameCount++;
                    }
                    else
                    {
                        BackupCount++;
                    }
                }
                else
                {
                    BackupCount++;
                }
            }
            if (SameCount + BackupCount < BasePathArr.Length)
            {
                BackupCount += BasePathArr.Length - (SameCount + BackupCount);
            }
            for (int i = 0; i < SameCount; i++)
            {
                FullNameList.RemoveAt(0);
            }
            for (int i = 0; i < BackupCount; i++)
            {
                FullNameList.Insert(0, "..");
            }
            Result = "";
            for (int i = 0; i < FullNameList.Count; i++)
            {
                if (i != 0)
                {
                    Result += SplitDelimit[0];
                }
                Result += FullNameList[i];
            }
            return Result;
        }
    }
}
