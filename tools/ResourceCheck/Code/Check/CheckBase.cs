using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ResourceCheck
{
    public class CheckBase
    {
        public string mCheckType;
        public List<string> mPathNameList;
        public List<int> mFilePathIndexAll;
        public List<string> mFileNameListAll;
        public List<string> mFileNameListLive;
        public List<string> mFileNameListDead;
        public CheckBase()
        {
            mPathNameList = new List<string>();
            mFilePathIndexAll = new List<int>();
            mFileNameListAll = new List<string>();
            mFileNameListLive = new List<string>();
            mFileNameListDead = new List<string>();
        }
        public void Free()
        {
            UnInit();
            mPathNameList = null;
            mFilePathIndexAll = null;
            mFileNameListAll = null;
            mFileNameListLive = null;
            mFileNameListDead = null;
        }
        public virtual void Init()
        {
            Clear();
        }
        public virtual void UnInit()
        {
            Clear();
        }
        public virtual void Clear()
        {
            mPathNameList.Clear();
            mFilePathIndexAll.Clear();
            mFileNameListAll.Clear();
            mFileNameListLive.Clear();
            mFileNameListDead.Clear();
        }
        public virtual int GetFileCount()
        {
            return mFileNameListAll.Count;
        }
        public virtual string GetFileName(int Index)
        {
            return mFileNameListAll[Index];
        }
        public void CheckOne(string FileName)
        {
            for (int i = 0; i < mFileNameListAll.Count; i++)
            {
                if (mFileNameListAll[i] == FileName)
                {
                    CheckOne(i);
                    return;
                }
            }
        }
        public void CheckOne(int Index)
        {
            List<int> FileNameIndexList = new List<int>();
            FileNameIndexList.Add(Index);
            CheckAll(FileNameIndexList, mFileNameListLive, mFileNameListDead);
            FileNameIndexList.Clear();
            FileNameIndexList = null;
        }
        public void CheckAll()
        {
            List<int> FileNameIndexList = new List<int>();
            for (int i = 0; i < GetFileCount(); i++)
            {
                FileNameIndexList.Add(i);
            }
            CheckAll(FileNameIndexList, mFileNameListLive, mFileNameListDead);
            FileNameIndexList.Clear();
            FileNameIndexList = null;
        }
        public virtual void CheckOne(int Index, List<string> FileNameListLive, List<string> FileNameListDead)
        {
            FileNameListLive.Clear();
            FileNameListDead.Clear();
        }
        public void CheckAll(List<int> FileNameIndexList, List<string> FileNameListLive, List<string> FileNameListDead)
        {
            List<string> FileNameListLiveTmp = new List<string>();
            List<string> FileNameListDeadTmp = new List<string>();
            Dictionary<string, int> FileNameMapLive = new Dictionary<string, int>();
            Dictionary<string, int> FileNameMapDead = new Dictionary<string, int>();
            for (int i = 0; i < FileNameIndexList.Count; i++)
            {
                FileNameListLiveTmp.Clear();
                FileNameListDeadTmp.Clear();
                CheckOne(FileNameIndexList[i], FileNameListLiveTmp, FileNameListDeadTmp);
                for (int i2 = 0; i2 < FileNameListLiveTmp.Count; i2++)
                {
                    FileNameMapLive[FileNameListLiveTmp[i2]] = 1;
                }
                for (int i2 = 0; i2 < FileNameListDeadTmp.Count; i2++)
                {
                    FileNameMapDead[FileNameListDeadTmp[i2]] = 1;
                }
            }
            FileNameListLiveTmp.Clear();
            FileNameListDeadTmp.Clear();
            FileNameListLiveTmp = null;
            FileNameListDeadTmp = null;
            FileNameListLive.Clear();
            FileNameListDead.Clear();
            foreach (var item in FileNameMapLive)
            {
                FileNameListLive.Add(item.Key);
            }
            foreach (var item in FileNameMapDead)
            {
                FileNameListDead.Add(item.Key);
            }
        }
    }
}
