using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using CSTools;

namespace ResourceCheck
{
    public partial class UCTable : UserControl
    {
        public UCTable()
        {
            InitializeComponent();
        }
        public void Init()
        {
            RefreshList();
        }
        public void ShowList(CheckBase CKBase)
        {
            LBFile.Items.Clear();
            for (int i = 0; i < CKBase.GetFileCount(); i++)
            {
                LBFile.Items.Add(CKBase.GetFileName(i));
            }
            LBLFileTitle.Content = "准备分析的文件 - " + LBFile.Items.Count.ToString();
        }
        public void ShowResult(CheckBase CKBase)
        {
            LBFileLive.Items.Clear();
            LBFileDead.Items.Clear();
            for (int i = 0; i < CKBase.mFileNameListLive.Count; i++)
            {
                LBFileLive.Items.Add(CKBase.mFileNameListLive[i]);
            }
            LBLFileLiveTitle.Content = "存在的资源文件 - " + LBFileLive.Items.Count.ToString();
            for (int i = 0; i < CKBase.mFileNameListDead.Count; i++)
            {
                LBFileDead.Items.Add(CKBase.mFileNameListDead[i]);
            }
            LBLFileDeadTitle.Content = "丢失的资源文件 - " + LBFileDead.Items.Count.ToString();
        }
        public void RefreshList()
        {
            LBFile.Items.Clear();
            LBFileLive.Items.Clear();
            LBFileDead.Items.Clear();
            CheckTable CKTable = new CheckTable();
            CKTable.Init();
            ShowList(CKTable);
            CKTable.Free();
            CKTable = null;
            if (LBFile.Items.Count > 0)
            {
                LBFile.SelectedIndex = 0;
                LBFile.Focus();
                CheckOne(0);
            }
        }
        public void CheckAll()
        {
            CheckTable CKTable = new CheckTable();
            CKTable.Init();
            CKTable.CheckAll();
            ShowResult(CKTable);
            CKTable.Free();
            CKTable = null;
        }
        public void CheckOne(int Index)
        {
            CheckTable CKTable = new CheckTable();
            CKTable.Init();
            CKTable.CheckOne(Index);
            ShowResult(CKTable);
            CKTable.Free();
            CKTable = null;
        }
        private void GDBG_SizeChanged(object sender, SizeChangedEventArgs e)
        {
            Thickness NewMargin;
            LBFile.Width = e.NewSize.Width / 5;
            NewMargin = LBFileLive.Margin;
            NewMargin.Left = LBFile.Width;
            LBFileLive.Margin = NewMargin;
            LBFileLive.Width = e.NewSize.Width / 5 * 2;
            NewMargin = LBFileDead.Margin;
            NewMargin.Left = LBFile.Width + LBFileLive.Width;
            LBFileDead.Margin = NewMargin;
            LBFileDead.Width = e.NewSize.Width - NewMargin.Left;
            
            NewMargin = LBLFileTitle.Margin;
            NewMargin.Left = LBFile.Margin.Left;
            LBLFileTitle.Margin = NewMargin;
            LBLFileTitle.Width = LBFile.Width;
            NewMargin = LBLFileLiveTitle.Margin;
            NewMargin.Left = LBFileLive.Margin.Left;
            LBLFileLiveTitle.Margin = NewMargin;
            LBLFileLiveTitle.Width = LBFileLive.Width;
            NewMargin = LBLFileDeadTitle.Margin;
            NewMargin.Left = LBFileDead.Margin.Left;
            LBLFileDeadTitle.Margin = NewMargin;
            LBLFileDeadTitle.Width = LBFileDead.Width;
        }
        private void LBTableFile_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (LBFile.SelectedIndex >= 0)
            {
                CheckOne(LBFile.SelectedIndex);
            }
        }
    }
}
