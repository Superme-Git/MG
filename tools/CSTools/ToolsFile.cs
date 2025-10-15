using System.Collections.Generic;
using System.IO;

namespace CSTools
{
    class ToolsFile
    {
        static public bool FileExists(string FullFileName)
        {
            bool Result = File.Exists(FullFileName);
            return Result;
        }
        static public bool DirExists(string FullDirName)
        {
            bool Result = Directory.Exists(ToolsFileStr.PathToDir(FullDirName));
            return Result;
        }
        static public int FindSubDir(string ParentPath, List<string> SubDir, string DirSearchPattern, SearchOption DirSearchOption)
        {
            ParentPath = ToolsFileStr.DirToPath(ParentPath);
            int ResultCount = 0;
            DirectoryInfo ParentFolder = new DirectoryInfo(ParentPath);
            DirectoryInfo[] ParentFolderInfo = ParentFolder.GetDirectories();
            ResultCount = ParentFolderInfo.Length;
            if (DirSearchOption == SearchOption.AllDirectories)
            {
                foreach (DirectoryInfo CurFolderInfo in ParentFolderInfo)
                {
                    ResultCount = ResultCount + FindSubDir(CurFolderInfo.FullName, SubDir, DirSearchPattern, DirSearchOption);
                }
            }
            return ResultCount;
        }
        static public int FindSubDir(string ParentPath, List<string> SubDir, string DirSearchPattern, SearchOption DirSearchOption, bool IncludeParentPath)
        {
            int ResultCount = FindSubDir(ParentPath, SubDir, DirSearchPattern, DirSearchOption);
            if (IncludeParentPath == true)
            {
                SubDir.Add(ToolsFileStr.PathToDir(ParentPath));
                ResultCount++;
            }
            return ResultCount;
        }

        static public int FindSubFile(string ParentPath, List<string> SubFile, string FileSearchPattern, SearchOption FileSearchOption)
        {
            SubFile.Clear();
            DirectoryInfo ParentFolder = new DirectoryInfo(ParentPath);
            FileInfo[] ParentFolderInfo = ParentFolder.GetFiles(FileSearchPattern, FileSearchOption);
            string NewPattern = FileSearchPattern;
            NewPattern = NewPattern.Replace("*", "");
            NewPattern = NewPattern.Replace("?", "");
            foreach (FileInfo CurFileInfo in ParentFolderInfo)
            {
                if (CurFileInfo.FullName.EndsWith(NewPattern))
                {
                    SubFile.Add(CurFileInfo.FullName);
                }
            }
            return SubFile.Count;
        }
    }

}
