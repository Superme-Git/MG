using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using CSTools;

namespace ResourceCheck
{
    class Option
    {
        public class TableFileInfo
        {
            public class TableFileInfoOne
            {
                public string mFileName;
                public List<string> mColName;
                public TableFileInfoOne()
                {
                    mColName = new List<string>();
                }
                public void Free()
                {
                    mColName.Clear();
                    mColName = null;
                }
                public void LoadFromNode(ToolsXmlNode Node0)
                {
                    mFileName = Node0.FindAttrValue("Name");
                    mFileName = mFileName.Replace("\\", "/");
                    int TmpInt0 = ToolsConvert.StrToI1(Node0.FindAttrValue("ColCount"));
                    for (int i = 0; i < TmpInt0; i++)
                    {
                        AddCol(Node0.FindNode("N" + i.ToString()));
                    }
                }
                public void AddCol(ToolsXmlNode Node0)
                {
                    string ColName = Node0.FindAttrValue("Name");
                    mColName.Add(ColName);
                }
            }
            public string mPathNameFull;
            public List<TableFileInfoOne> mFileInfoOne;
            public TableFileInfo()
            {
                mFileInfoOne = new List<TableFileInfoOne>();
            }
            public void Free()
            {
                mFileInfoOne.Clear();
                mFileInfoOne = null;
            }
            public void LoadFromNode(ToolsXmlNode Node0)
            {
                mPathNameFull = Node0.FindAttrValue("PathName");
                mPathNameFull = mPathNameFull.Replace("\\", "/");
                int TmpInt0 = ToolsConvert.StrToI1(Node0.FindAttrValue("Count"));
                for (int i = 0; i < TmpInt0; i++)
                {
                    AddFileInfoOne(Node0.FindNode("N" + i.ToString()));
                }
            }
            public void AddFileInfoOne(ToolsXmlNode Node0)
            {
                TableFileInfoOne FIO = new TableFileInfoOne();
                FIO.LoadFromNode(Node0);
                mFileInfoOne.Add(FIO);
            }
        }
        public class FileInfoOneBase
        {
            public FileInfoBase mOwner;
            public string mPathName;
            public string mFileNameExt;
            public string mTexturePathName;
            public virtual void LoadFromNode(ToolsXmlNode Node0)
            {
                mPathName = Node0.FindAttrValue("PathName");
                mPathName = mPathName.Replace("\\", "/");
                mFileNameExt = Node0.FindAttrValue("FileNameExt");
                mOwner.mOwner.mFileNameExt.Add(mPathName, mFileNameExt);
                mTexturePathName = Node0.FindAttrValue("TexturePathName");
                mTexturePathName = mTexturePathName.Replace("\\", "/");
            }
        }
        public class FileInfoOneParticle : FileInfoOneBase
        {
            public string mPathPathName;
            public override void LoadFromNode(ToolsXmlNode Node0)
            {
                base.LoadFromNode(Node0);
                mPathPathName = Node0.FindAttrValue("PathPathName");
                mPathPathName = mPathPathName.Replace("\\", "/");
            }
        }
        public class FileInfoBase
        {
            public Option mOwner;
            public List<FileInfoOneBase> mFileInfoOne;
            public FileInfoBase()
            {
                mFileInfoOne = new List<FileInfoOneBase>();
            }
            public void Free()
            {
                mFileInfoOne.Clear();
                mFileInfoOne = null;
            }
            public void LoadFromNode(ToolsXmlNode Node0)
            {
                int TmpInt0 = ToolsConvert.StrToI1(Node0.FindAttrValue("Count"));
                for (int i = 0; i < TmpInt0; i++)
                {
                    AddFileInfoOne(Node0.FindNode("N" + i.ToString()));
                }
            }
            public virtual void AddFileInfoOne(ToolsXmlNode Node0)
            {
                FileInfoOneBase FIO = new FileInfoOneBase();
                FIO.mOwner = this;
                FIO.LoadFromNode(Node0);
                mFileInfoOne.Add(FIO);
            }
        }
        public class FileInfoEffect : FileInfoBase
        {
        }
        public class FileInfoParticle : FileInfoBase
        {
            public override void AddFileInfoOne(ToolsXmlNode Node0)
            {
                FileInfoOneBase FIO = new FileInfoOneParticle();
                FIO.mOwner = this;
                FIO.LoadFromNode(Node0);
                mFileInfoOne.Add(FIO);
            }
        }
        public class FileInfoAnimation : FileInfoBase
        {
        }

        static public Window sMainWindow;

        public string mAppPath;
        public string mAppDir;
        public string mEffectPath;

        public Dictionary<string, string> mFileNameExt;

        public TableFileInfo mTableFileInfo;
        public FileInfoEffect mEffectFileInfo;
        public FileInfoParticle mParticleFileInfo;
        public FileInfoAnimation mAnimationFileInfo;

        public Option(string AppPath)
        {
            mAppPath = AppPath;
            mAppPath = mAppPath.Replace("\\", "/");
            mAppDir = mAppPath.Substring(0, mAppPath.Length - 1);
            mFileNameExt = new Dictionary<string, string>();
            mTableFileInfo = new TableFileInfo();
            mEffectFileInfo = new FileInfoEffect();
            mParticleFileInfo = new FileInfoParticle();
            mAnimationFileInfo = new FileInfoAnimation();
            mEffectFileInfo.mOwner = this;
            mParticleFileInfo.mOwner = this;
            mAnimationFileInfo.mOwner = this;
        }
        public int Init()
        {
            string FileNameFull = mAppPath + "ResourceCheckOption.xml";
            if (ToolsFile.FileExists(FileNameFull) == false)
            {
                return -1;
            }
            ToolsXmlDoc Doc = new ToolsXmlDoc();
            Doc.LoadFromFile(FileNameFull);
            ToolsXmlNode Node0;
            ToolsXmlNode Node1;
            ToolsXmlNode Node2;
            Node0 = Doc.FindNode("Root");
            //Node1 = Node0.FindNode("Common");
            Node1 = Node0.FindNode("Effect");
            mEffectPath = Node1.FindAttrValue("PathName");
            mEffectPath = mEffectPath.Replace("\\", "/");
            Node2 = Node1.FindNode("TableFileInfo");
            mTableFileInfo.LoadFromNode(Node2);
            Node2 = Node1.FindNode("EffectFileInfo");
            mEffectFileInfo.LoadFromNode(Node2);
            Node2 = Node1.FindNode("ParticleFileInfo");
            mParticleFileInfo.LoadFromNode(Node2);
            Node2 = Node1.FindNode("AnimationFileInfo");
            mAnimationFileInfo.LoadFromNode(Node2);
            Doc.Free();
            Doc = null;
            return 0;
        }
        public string GetFileNameExtByPathName(string PathName)
        {
            string Result = "";
            foreach (var item in mFileNameExt)
            {
                if (PathName.StartsWith(item.Key))
                {
                    Result = item.Value;
                    break;
                }
            }
            return Result;
        }
        public void Free()
        {
            mFileNameExt.Clear();
            mFileNameExt = null;
            mTableFileInfo.Free();
            mTableFileInfo = null;
            mEffectFileInfo.Free();
            mEffectFileInfo = null;
            mParticleFileInfo.Free();
            mParticleFileInfo = null;
            mAnimationFileInfo.Free();
            mAnimationFileInfo = null;
        }
    }
}