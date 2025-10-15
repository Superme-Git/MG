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
            LBTableFile.Items.Clear();
            CheckTable CKTable = new CheckTable();
            CKTable.Init();
            for (int i = 0; i < CKTable.GetFileCount(); i++)
            {
                LBTableFile.Items.Add(CKTable.GetFileName(i));
            }
            LBLTableFileTitle.Content = "数据表文件 - " + LBTableFile.Items.Count.ToString();
            CKTable.Free();
            CKTable = null;
            if (LBTableFile.Items.Count > 0)
            {
                LBTableFile.SelectedIndex = 0;
                LBTableFile.Focus();
                CheckOne(0);
            }
        }
        public void ShowResult(CheckTable CKTable)
        {
            LBEffectFileLive.Items.Clear();
            LBEffectFileDead.Items.Clear();
            for (int i = 0; i < CKTable.mFileNameListLive.Count; i++)
            {
                LBEffectFileLive.Items.Add(CKTable.mFileNameListLive[i]);
            }
            LBLEffectFileLiveTitle.Content = "存在的特效文件 - " + LBEffectFileLive.Items.Count.ToString();
            for (int i = 0; i < CKTable.mFileNameListDead.Count; i++)
            {
                LBEffectFileDead.Items.Add(CKTable.mFileNameListDead[i]);
            }
            LBLEffectFileDeadTitle.Content = "丢失的特效文件 - " + LBEffectFileDead.Items.Count.ToString();
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
            LBTableFile.Width = e.NewSize.Width / 5;
            NewMargin = LBEffectFileLive.Margin;
            NewMargin.Left = LBTableFile.Width;
            LBEffectFileLive.Margin = NewMargin;
            LBEffectFileLive.Width = e.NewSize.Width / 5 * 2;
            NewMargin = LBEffectFileDead.Margin;
            NewMargin.Left = LBTableFile.Width + LBEffectFileLive.Width;
            LBEffectFileDead.Margin = NewMargin;
            LBEffectFileDead.Width = e.NewSize.Width - NewMargin.Left;
            
            NewMargin = LBLTableFileTitle.Margin;
            NewMargin.Left = LBTableFile.Margin.Left;
            LBLTableFileTitle.Margin = NewMargin;
            LBLTableFileTitle.Width = LBTableFile.Width;
            NewMargin = LBLEffectFileLiveTitle.Margin;
            NewMargin.Left = LBEffectFileLive.Margin.Left;
            LBLEffectFileLiveTitle.Margin = NewMargin;
            LBLEffectFileLiveTitle.Width = LBEffectFileLive.Width;
            NewMargin = LBLEffectFileDeadTitle.Margin;
            NewMargin.Left = LBEffectFileDead.Margin.Left;
            LBLEffectFileDeadTitle.Margin = NewMargin;
            LBLEffectFileDeadTitle.Width = LBEffectFileDead.Width;
        }
        private void LBTableFile_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (LBTableFile.SelectedIndex >= 0)
            {
                CheckOne(LBTableFile.SelectedIndex);
            }
        }
    }
}
