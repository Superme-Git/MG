using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSTools;

namespace ResourceCheck
{
    class CheckAnimationOne
    {
        static public void ParseFile(string FileNameFull, List<string> FileNameList)
        {
            string FileName = ToolsFileStr.GetName(FileNameFull);

            int VERSION_OLD_BASE = 10;
            int VERSION_NO_CENTER = 11;
            int VERSION_13 = 13;

            ToolsStream TS = new ToolsStream(FileNameFull, FileMode.Open, FileAccess.Read);
            int TmpInt0;
            //string TmpStr0;
            int dummy;
            dummy = TS.ReadInt();
            if (dummy < VERSION_OLD_BASE)
            {
                return;
            }

            TS.ReadInt(); //m_filefmt;
            TS.ReadInt(); //m_texfmt;
            TS.ReadInt(); //m_blend;
            TS.ReadInt(); //m_time;
            int m_nRegion = TS.ReadInt(); //m_nRegion;
            int m_nFrame = TS.ReadInt(); //m_nFrame;
            int m_dirmode = TS.ReadInt(); //m_dirmode;
            TS.ReadInt(); //m_color;
            TS.ReadInt(); //m_systemLevel;
            int bindtype = TS.ReadInt();

            if (dummy > VERSION_OLD_BASE)
            {
                TS.ReadInt(); //m_base.left;
                TS.ReadInt();
                TS.ReadInt(); //m_base.right;
                TS.ReadInt();
                if (dummy > VERSION_NO_CENTER)
                {
                    TS.ReadInt(); //m_center;
                    TS.ReadInt();
                }
            }
            else
            {
                TS.ReadInt(); //oldcenter;
                TS.ReadInt();
                TS.ReadInt(); //m_base.left;
                TS.ReadInt();
                TS.ReadInt(); //unuse;
                TS.ReadInt();
                TS.ReadInt(); //m_base.right;
                TS.ReadInt();
                TS.ReadInt(); //unuse;
                TS.ReadInt();
            }

            TmpInt0 = TS.ReadInt(); //m_border;
            TmpInt0 = TS.ReadInt();
            TmpInt0 = TS.ReadInt();
            TmpInt0 = TS.ReadInt();

            //const int XPANIDM_NULL = 0;
            const int XPANIDM_8USE1 = 1;	// 0, 0, 0, 0, 0, 0, 0, 0
            const int XPANIDM_8 = 2;		// 0, 1, 2, 3, 4, 5, 6, 7
            const int XPANIDM_4 = 3;		// x, 1, x, 3, x, 5, x, 7
            const int XPANIDM_3 = 4;		// x, x, x, 3, 4, 5, x, x
            const int XPANIDM_8FROM5 = 5;	// 0, 1, 2, 3, 4, t, t, t
            const int XPANIDM_8FROM1 = 6;	// t, t, 2, t, t, t, t, t
            const int XPANIDM_2 = 7;		// x, x, x, 3, x, x, x, 7
            const int XPANIDM_B2FROM1 = 8;// x, x, x, 3, x, t, x, x
            const int XPANIDM_4FROM2 = 9;	// x, t, x, 3, x, t, x, 7

            int m_dirs_size = 0;
            switch (m_dirmode)
            {
                case XPANIDM_3:
                    m_dirs_size = 3;
                    break;
                case XPANIDM_4:
                    m_dirs_size = 4;
                    break;
                case XPANIDM_8:
                    m_dirs_size = 8;
                    break;
                case XPANIDM_8FROM5:
                    m_dirs_size = 5;
                    break;
                case XPANIDM_8FROM1:
                    m_dirs_size = 1;
                    break;
                case XPANIDM_8USE1:
                    m_dirs_size = 1;
                    break;
                case XPANIDM_2:
                    m_dirs_size = 2;
                    break;
                case XPANIDM_B2FROM1:
                    m_dirs_size = 1;
                    break;
                case XPANIDM_4FROM2:
                    m_dirs_size = 2;
                    break;
                default:
                    break;
            }
            
            for (int i = 0; i < m_dirs_size; i++)
            {
                for (int j = 0; j < m_nFrame; j++)
                {
                    for (int k = 0; k < m_nRegion; k++)
                    {
                        TmpInt0 = TS.ReadInt(); //m_data[i][j][k].offset;
                        TmpInt0 = TS.ReadInt();
                        TmpInt0 = TS.ReadInt();
                        TmpInt0 = TS.ReadInt();
                        
                        string strPicPath; //m_data[i][j][k].strPicPath;

                        int Count = TS.ReadInt();
                        Byte[] TmpBA0 = new Byte[Count * 2];
                        TS.ReadBA(ref TmpBA0, Count * 2);
                        strPicPath = Encoding.Unicode.GetString(TmpBA0);
                        FileNameList.Add(FileName + strPicPath);

                        TS.ReadInt(); //m_data[i][j][k].rctData
                        TS.ReadInt();
                        TS.ReadInt();
                        TS.ReadInt();

                        if (dummy > VERSION_13)
                        {
                            int npt = 0;
                            npt = TS.ReadInt();
                            
                            for (int m = 0; m < npt; m++)
                            {
                                TS.ReadInt(); //m_data[i][j][k].outLinePoints[m];
                                TS.ReadInt();
                            }
                        }
                        else
                        {
                        }
                    }
                }
            }
            TS.Close();
            TS = null;
        }
        static public void CheckFile(int Index, string FileNameFull, List<string> FileNameListLive, List<string> FileNameListDead)
        {
            List<string> FileNameList;
            FileNameList = new List<string>();
            ParseFile(FileNameFull, FileNameList);
            string PathNameFullEffect = ToolsFileStr.GetFullName(CheckCommon.sOption.mAppPath, CheckCommon.sOption.mEffectPath);
            PathNameFullEffect = ToolsFileStr.DirToPath(PathNameFullEffect);
            string PathNameFull = ToolsFileStr.DirToPath(ToolsFileStr.GetPath(FileNameFull).Replace("\\", "/"));
            for (int i = 0; i < FileNameList.Count; i++)
            {
                string PathFileNameFull = PathNameFull + FileNameList[i];
                string FileNameOf = ToolsFileStr.GetOfName(PathNameFullEffect, PathFileNameFull);
                if (ToolsFile.FileExists(PathFileNameFull) == true)
                {
                    FileNameListLive.Add(FileNameOf);
                }
                else
                {
                    FileNameListDead.Add(FileNameOf);
                }
            }
            FileNameList.Clear();
            FileNameList = null;
        }
    }
    public class CheckAnimation : CheckBase
    {
        public CheckAnimation()
        {
            mCheckType = "Animation";
        }
        public override void Init()
        {
            base.Init();
            string FullPathName = ToolsFileStr.GetFullName(CheckCommon.sOption.mAppPath, CheckCommon.sOption.mEffectPath);
            FullPathName = ToolsFileStr.DirToPath(FullPathName);
            List<string> FileNameListAllTmp;
            FileNameListAllTmp = new List<string>();
            for (int i = 0; i < CheckCommon.sOption.mAnimationFileInfo.mFileInfoOne.Count; i++)
            {
                mPathNameList.Add(CheckCommon.sOption.mAnimationFileInfo.mFileInfoOne[i].mPathName);
                ToolsFile.FindSubFile(FullPathName + mPathNameList[mPathNameList.Count - 1], FileNameListAllTmp,
                    "*." + CheckCommon.sOption.mAnimationFileInfo.mFileInfoOne[i].mFileNameExt, SearchOption.AllDirectories);
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
            CheckAnimationOne.CheckFile(mFilePathIndexAll[Index], PathNameFull + mFileNameListAll[Index], FileNameListLive, FileNameListDead);
        }
    }
}
