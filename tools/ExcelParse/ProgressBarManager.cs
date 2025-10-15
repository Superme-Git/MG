using System;
using System.Collections.Generic;
using System.Windows.Controls;
using System.Linq;
using System.Text;
using System.ComponentModel;

namespace ExcelParse
{
    class ProgressBarManager
    {
        private ProgressBarWindow mProgressBarWindows;//进度条窗口.
        private BackgroundWorker mBackgroundWorker;//多线程管理.
        private DoWorkEventHandler mBackgroundWorkder_Start;//开启异步功能.
        private ProgressChangedEventHandler mBackgroundWorkder_Change;//
        private RunWorkerCompletedEventHandler mBackgroundWorkder_Stop;//结束异步功能.
        public void ShowLog(string log)
        {
            /*
            mProgressBarWindows.mLogWindow.Items.Add(log);
            if(mProgressBarWindows.mLogWindow.Items.Count>400)
            {
                mProgressBarWindows.mLogWindow.Items.Remove(mProgressBarWindows.mLogWindow.Items[0]);
            }*/
        }
        public void Initializer()
        {
            mBackgroundWorker = new BackgroundWorker();
            mBackgroundWorker.WorkerReportsProgress = true;
            mBackgroundWorker.WorkerSupportsCancellation = true;

            mProgressBarWindows = new ProgressBarWindow();
        }
        public void Release()
        {
            ReleaseHandlers();
            mProgressBarWindows.Close();
        }
        public void Start(DoWorkEventHandler start, RunWorkerCompletedEventHandler stop, string title, double parentWindowLeft, double parentWindowTop, double parentWindowWidth, double parentWindowHeight)
        {
            mBackgroundWorkder_Start = new DoWorkEventHandler(start);
            mBackgroundWorker.DoWork+=mBackgroundWorkder_Start;
            mBackgroundWorkder_Change = new ProgressChangedEventHandler(OnChange);
            mBackgroundWorker.ProgressChanged += mBackgroundWorkder_Change;
            mBackgroundWorkder_Stop = new RunWorkerCompletedEventHandler(stop);
            mBackgroundWorker.RunWorkerCompleted += mBackgroundWorkder_Stop;

            //设置进度条刻度.
            mProgressBarWindows.mTaskProgress.Value = 0;
            mProgressBarWindows.mTaskProgress.Minimum = 0;
            mProgressBarWindows.mTaskProgress.Maximum = 100;

            //窗口居中
            mProgressBarWindows.Left = parentWindowLeft+(parentWindowWidth-mProgressBarWindows.Width)/2;
            mProgressBarWindows.Top = parentWindowTop+(parentWindowHeight-mProgressBarWindows.Height)/2;

            //窗口独占
            mProgressBarWindows.Topmost=true;

            //设置标题
            mProgressBarWindows.Title=title;

            // 显示
            mProgressBarWindows.Show();

            // 启动异步操作.
            mBackgroundWorker.RunWorkerAsync();
        }
        public void OnEnd()
        {
            ReleaseHandlers();
            mProgressBarWindows.Hide();
        }
        public void ReportProgress(int progress)
        {
            mBackgroundWorker.ReportProgress(progress);
        }
        private void OnChange(object sender,ProgressChangedEventArgs e)
        {
            mProgressBarWindows.mTaskProgress.Value = e.ProgressPercentage;
        }
        private void ReleaseHandlers()
        {
            if(mBackgroundWorkder_Start!=null)
            {
                mBackgroundWorker.DoWork-=mBackgroundWorkder_Start;
            }
            if(mBackgroundWorkder_Stop!=null)
            {
                mBackgroundWorker.RunWorkerCompleted-=mBackgroundWorkder_Stop;
            }
            if(mBackgroundWorkder_Change!=null)
            {
                mBackgroundWorker.ProgressChanged-=mBackgroundWorkder_Change;
            }
        }
    }
}
