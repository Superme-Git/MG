using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSTools;

namespace ResourceCheck
{
    class CheckEffectOne
    {
        static public void ParseFile(string FileNameFull, List<string> FileNameList)
        {
            ToolsXmlDoc Doc = new ToolsXmlDoc();
            Doc.LoadFromFile(FileNameFull);
            ToolsXmlNode Node0;
            ToolsXmlNode Node1;
            ToolsXmlAttr Attr0;
            Node0 = Doc.FindNode("data");
            for (int i = 0; i < Node0.GetNodeCount(); i++)
            {
                Node1 = Node0.GetNode(i);
                if (Node1.mName != "clip")
                {
                    continue;
                }
                Attr0 = Node1.FindAttr("r_f");
                if (Attr0 == null)
                {
                    continue;
                }
                FileNameList.Add(Attr0.mValue.Replace("\\", "/"));
            }
            Doc.Free();
            Doc = null;
        }
        static public void CheckFile(int Index, string FileNameFull, List<string> FileNameListLive, List<string> FileNameListDead)
        {
            List<string> FileNameList;
            FileNameList = new List<string>();
            ParseFile(FileNameFull, FileNameList);
            string PathNameFull = ToolsFileStr.GetFullName(CheckCommon.sOption.mAppPath, CheckCommon.sOption.mEffectPath);
            PathNameFull = ToolsFileStr.DirToPath(PathNameFull);
            CheckCommon.CheckFileExist(PathNameFull, FileNameList, FileNameListLive, FileNameListDead);
            FileNameList.Clear();
            FileNameList = null;
        }
    }
    public class CheckEffect : CheckBase
    {
        public CheckEffect()
        {
            mCheckType = "Effect";
        }
        public override void Init()
        {
            base.Init();
            string FullPathName = ToolsFileStr.GetFullName(CheckCommon.sOption.mAppPath, CheckCommon.sOption.mEffectPath);
            FullPathName = ToolsFileStr.DirToPath(FullPathName);
            List<string> FileNameListAllTmp;
            FileNameListAllTmp = new List<string>();
            for (int i = 0; i < CheckCommon.sOption.mEffectFileInfo.mFileInfoOne.Count; i++)
            {
                mPathNameList.Add(CheckCommon.sOption.mEffectFileInfo.mFileInfoOne[i].mPathName);
                ToolsFile.FindSubFile(FullPathName + mPathNameList[mPathNameList.Count - 1], FileNameListAllTmp,
                    "*." + CheckCommon.sOption.mEffectFileInfo.mFileInfoOne[i].mFileNameExt, SearchOption.AllDirectories);
                for (int i2 = 0; i2 < FileNameListAllTmp.Count; i2++)
                {
                    string OfFileName = ToolsFileStr.GetOfName(FullPathName, FileNameListAllTmp[i2]);
                    mFileNameListAll.Add(OfFileName);
                    mFilePathIndexAll.Add(i);
                }
                FileNameListAllTmp.Clear();
            }
            FileNameListAllTmp.Clear();
            FileNameListAllTmp = null;
        }
        public override void CheckOne(int Index, List<string> FileNameListLive, List<string> FileNameListDead)
        {
            base.CheckOne(Index, FileNameListLive, FileNameListDead);
            string PathNameFull = ToolsFileStr.GetFullName(CheckCommon.sOption.mAppPath, CheckCommon.sOption.mEffectPath);
            PathNameFull = ToolsFileStr.DirToPath(PathNameFull);
            CheckEffectOne.CheckFile(mFilePathIndexAll[Index], PathNameFull + mFileNameListAll[Index], FileNameListLive, FileNameListDead);
        }
    }
}
