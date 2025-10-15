using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSTools;

namespace ResourceCheck
{
    class CheckParticleOne
    {
        static public void ParseFile(string FileNameFull, string TexturePathName, string PathPathName, List<string> FileNameList)
        {
            ToolsStream TS = new ToolsStream(FileNameFull, FileMode.Open, FileAccess.Read);
            int TmpInt0;
            string TmpStr0;
            for (int i = 0; i < 2; i++)
            {
                TS.ReadInt();
            }
            TmpStr0 = TS.ReadStrEx();
            for (int i = 0; i < 14; i++)
            {
                TS.ReadInt();
            }
            TmpInt0 = TS.ReadInt();
            if (TmpInt0 == 4)
            {
                TmpStr0 = TS.ReadStrEx();    
            }
            for (int i = 0; i < 76; i++)
            {
                TS.ReadInt();
            }
            TmpStr0 = TS.ReadStrEx();
            TmpInt0 = TS.ReadInt();
            for (int i = 0; i < TmpInt0; i++)
            {
                TmpStr0 = TS.ReadStrEx();
                FileNameList.Add(TexturePathName + TmpStr0.Replace("\\", "/"));
            }
            TS.Close();
            TS = null;
        }
        static public void CheckFile(int Index, string FileNameFull, List<string> FileNameListLive, List<string> FileNameListDead)
        {
            List<string> FileNameList;
            FileNameList = new List<string>();
            ParseFile(FileNameFull, CheckCommon.sOption.mParticleFileInfo.mFileInfoOne[Index].mTexturePathName,
                ((Option.FileInfoOneParticle)CheckCommon.sOption.mParticleFileInfo.mFileInfoOne[Index]).mPathPathName, FileNameList);
            string PathNameFull = ToolsFileStr.GetFullName(CheckCommon.sOption.mAppPath, CheckCommon.sOption.mEffectPath);
            PathNameFull = ToolsFileStr.DirToPath(PathNameFull);
            for (int i = 0; i < FileNameList.Count; i++)
            {
                string PathFileNameFull = PathNameFull + FileNameList[i];
                if (ToolsFile.FileExists(PathFileNameFull) == true)
                {
                    FileNameListLive.Add(FileNameList[i]);
                }
                else
                {
                    FileNameListDead.Add(FileNameList[i]);
                }
            }
            FileNameList.Clear();
            FileNameList = null;
        }
    }
    public class CheckParticle : CheckBase
    {
        public CheckParticle()
        {
            mCheckType = "Particle";
        }
        public override void Init()
        {
            base.Init();
            string FullPathName = ToolsFileStr.GetFullName(CheckCommon.sOption.mAppPath, CheckCommon.sOption.mEffectPath);
            FullPathName = ToolsFileStr.DirToPath(FullPathName);
            List<string> FileNameListAllTmp;
            FileNameListAllTmp = new List<string>();
            for (int i = 0; i < CheckCommon.sOption.mParticleFileInfo.mFileInfoOne.Count; i++)
            {
                mPathNameList.Add(CheckCommon.sOption.mParticleFileInfo.mFileInfoOne[i].mPathName);
                ToolsFile.FindSubFile(FullPathName + mPathNameList[mPathNameList.Count - 1], FileNameListAllTmp,
                    "*." + CheckCommon.sOption.mParticleFileInfo.mFileInfoOne[i].mFileNameExt, SearchOption.AllDirectories);
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
            CheckParticleOne.CheckFile(mFilePathIndexAll[Index], PathNameFull + mFileNameListAll[Index], FileNameListLive, FileNameListDead);
        }
    }
}
