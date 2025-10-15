using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Windows.Controls;

namespace ExcelParse
{
    class LogManager
    {
        string mLogFileName = "ExcelParseLog.txt";
        FileStream mLogFileStream;
        StreamWriter mLogStreamWriter;
        ListBox mListBox;

        public bool Initialize(ListBox listBox)
        {
            // 初始化LOG系统.
            File.Delete(mLogFileName);
            mLogFileStream = File.Open(mLogFileName, FileMode.OpenOrCreate);
            mLogStreamWriter = new StreamWriter(mLogFileStream);
            mListBox = listBox;
            return true;
        }
        public void Release()
        {
            mLogStreamWriter.Close();
            mLogFileStream.Close();
        }
        private void LogOutput(string s)
        {
            mLogStreamWriter.Write(s);
            /*mListBox.Items.Add(s);
            if (mListBox.Items.Count > 400)
            {
                mListBox.Items.RemoveAt(0);
            }*/
        }
        public void LogInfo(string logText) { LogOutput("info: " + logText + "\n"); }
        public void LogWarning(string logText) { LogOutput("warning: " + logText + "\n"); }
        public void LogError(string logText) { LogOutput("error: " + logText + "\n"); }
        public void LogCall(string callName) { LogOutput("call: <" + callName + ">\n"); }
    }
}
