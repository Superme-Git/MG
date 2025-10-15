using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace CSTools
{
    class ToolsIni
    {
        public class ToolsIniItem
        {
            string mKey;
            string mValue;
            public ToolsIniItem(string Key, string Value)
            {
                mKey = Key;
                mValue = Value;
            }
            public string GetKey()
            {
                return mKey;
            }
            public void SetKey(string KeyName)
            {
                mKey = KeyName;
            }
            public string GetValue()
            {
                return mValue;
            }
            public void SetValue(string Value)
            {
                mValue = Value;
            }
            public void WriteToFile(StreamWriter SW)
            {
                SW.WriteLine(mKey + "=" + mValue);
            }
        }
        public class ToolsIniApp
        {
            string mName;
            List<ToolsIni.ToolsIniItem> mItemList;
            public ToolsIniApp(string Name)
            {
                mName = Name;
                mItemList = new List<ToolsIni.ToolsIniItem>();
            }

            public void Clear()
            {
                mItemList.Clear();
            }

            public string GetName()
            {
                return mName;
            }
            public void SetName(string AppName)
            {
                mName = AppName;
            }

            public void AddItem(string Item)
            {
                List<String> KeyValue;
                KeyValue = Item.Split(ToolsStr.StrArr("="), StringSplitOptions.RemoveEmptyEntries).ToList<string>();
                string Key = "";
                string Value = "";
                if (KeyValue.Count > 0)
                {
                    Key = KeyValue[0];
                }
                else
                {
                    return;
                }
                if (KeyValue.Count > 1)
                {
                    Value = KeyValue[1];
                }
                //Key.Trim();
                //Value.TrimStart();
                ToolsIniItem NewItem = new ToolsIniItem(Key,Value);
                mItemList.Add(NewItem);
            }

            public void WriteToFile(StreamWriter SW)
            {
                SW.WriteLine("[" + mName + "]");
                for (int i = 0; i < mItemList.Count; i++)
                {
                    mItemList[i].WriteToFile(SW);
                } 
            }

            public ToolsIniItem Find(string KeyName)
            {
                for (int i = 0; i < mItemList.Count; i++)
                {
                    if (mItemList[i].GetKey() == KeyName)
                    {
                        return mItemList[i];
                    }
                }
                return null;
            }

            public ToolsIniItem AddItem(string KeyName, string Value)
            {
                ToolsIniItem NewItem = new ToolsIniItem(KeyName,"");
                NewItem.SetValue(Value);
                mItemList.Add(NewItem);
                return NewItem;
            }

            public ToolsIniItem FindAndAddItem(string KeyName, string Value = "",bool ForceSetValue = false)
            {
                ToolsIniItem Result = Find(KeyName);
                if (Result != null)
                {
                    if (ForceSetValue)
                    {
                        Result.SetValue(Value);
                    }
                    return Result;
                }
                Result = AddItem(KeyName, Value);
                return Result;
            }

            public ToolsIniItem ReNameKey(string KeyName, string NewKeyName)
            {
                ToolsIniItem Result = Find(KeyName);
                if (Result != null)
                {
                    return Result;
                }
                Result.SetKey(NewKeyName);
                return Result;
            }

        }

        string mFullFileName = "";
        List<ToolsIni.ToolsIniApp> mAppList;

        public ToolsIni(string FullFileName)
        {
            mAppList = new List<ToolsIni.ToolsIniApp>();
            bool Result = LoadFromFile(FullFileName);
        }
        public void Clear()
        {
            for (int i = 0; i < mAppList.Count; i++)
            {
                mAppList[i].Clear();
            }
            mAppList.Clear();
         
        }
        public void Free()
        {
            Clear();
            mAppList = null;
        }

        public bool LoadFromFile(string FullFileName)
        {
            Clear();
            mFullFileName = FullFileName;
            if (!ToolsFile.FileExists(FullFileName))
            {
                return false;
            }
            
            StreamReader SR = new StreamReader(FullFileName,Encoding.ASCII);
            if (SR == null)
            {
                return false;
            }
            string StrLine = SR.ReadToEnd();
            List<String> LineList;
            LineList = StrLine.Split(ToolsStr.StrArr("\r\n"), StringSplitOptions.RemoveEmptyEntries).ToList<string>();
            for (int i = 0; i < LineList.Count; i++)
            {
                StrLine = LineList[i];
                if (StrLine.StartsWith("["))
                {
                    string NewAppName = StrLine.Substring(1, StrLine.Length - 2);
                    ToolsIniApp NewApp = new ToolsIniApp(NewAppName);
                    for (int i2 = i + 1; i2 < LineList.Count; i2++)
                    {
                        string StrLine2 = LineList[i2];
                        if (!StrLine2.StartsWith("["))
                        {
                            NewApp.AddItem(StrLine2);
                        }
                        else
                        {
                            i = i2 - 1;
                            break;
                        }
                    }
                    mAppList.Add(NewApp);
                }
            }
            SR.Close();
            SR = null;
            return true;
        }

        public bool WriteToFile()
        {
            if (mFullFileName == "")
            {
                return false;
            }
            if (mAppList == null)
            {
                return false;
            }
            if (mAppList.Count == 0)
            {
                return false;
            }
            StreamWriter SW = new StreamWriter(mFullFileName, false, Encoding.ASCII);
            for (int i = 0; i < mAppList.Count; i++)
            {
                mAppList[i].WriteToFile(SW);
            }
            SW.Close();
            SW = null;
            return true;
        }
        public bool WriteToFile(string FullFileName)
        {
            mFullFileName = FullFileName;
            return WriteToFile();
        }
        
        public ToolsIni.ToolsIniApp Find(string AppName)
        {
            for (int i = 0; i < mAppList.Count; i++)
            {
                if (mAppList[i].GetName() == AppName)
                {
                    return mAppList[i];
                }
            }
            return null;
        }

        public ToolsIniApp AddApp(string AppName, string KeyName = "", string Value = "", bool ForceSetValue = false)
        {
            ToolsIni.ToolsIniApp NewApp = new ToolsIni.ToolsIniApp(AppName);
            NewApp.FindAndAddItem(KeyName, Value, ForceSetValue);
            mAppList.Add(NewApp);
            return NewApp;
        }

        public ToolsIniApp FindAndAddApp(string AppName, string KeyName = "", string Value = "", bool ForceSetValue = false)
        {
            ToolsIniApp Result = Find(AppName);
            if (Result != null)
            {
                Result.FindAndAddItem(KeyName, Value, ForceSetValue);
                return Result;
            }
            Result = AddApp(AppName, KeyName, Value, ForceSetValue);
            return Result;
        }

        public ToolsIniApp ReNameApp(string AppName, string NewAppName)
        {
            ToolsIniApp Result = Find(AppName);
            if (Result != null)
            {
                return Result;
            }
            Result.SetName(NewAppName);
            return Result;
        }

    }
}
