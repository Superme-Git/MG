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
    public partial class UCCommonList : UserControl
    {
        string mCheckType;
        UCCommon mUCCommon;
        public UCCommonList()
        {
            InitializeComponent();
        }
        public void Init(string CheckType)
        {
            mCheckType = CheckType;
            mUCCommon = new UCCommon();
            mUCCommon.Init(mCheckType);
            GDBG.Children.Add(mUCCommon);
        }
        public void Free()
        {
            mUCCommon.Free();
            mUCCommon = null;
        }
        public void ShowList(CheckBase CKBase)
        {
            LBFile.Items.Clear();
            for (int i = 0; i < CKBase.GetFileCount(); i++)
            {
                LBFile.Items.Add(CKBase.GetFileName(i));
            }
            LBLFileTitle.Content = "准备检查的文件 - " + LBFile.Items.Count.ToString();
        }
        public void RefreshList()
        {
            LBFile.Items.Clear();
            CheckBase CKBase = CheckCommon.GetCheck(mCheckType);
            CKBase.Init();
            ShowList(CKBase);
            CKBase.Free();
            CKBase = null;
            if (LBFile.Items.Count > 0)
            {
                LBFile.SelectedIndex = 0;
                LBFile.Focus();
                LBFile.Focus();
                CheckOne(0);
            }
            else
            {
                CheckOne(-1);
            }
        }
        public void CheckAll()
        {
            CheckBase CKBase = CheckCommon.GetCheck(mCheckType);
            CKBase.Init();
            CKBase.CheckAll();
            mUCCommon.ShowResult(CKBase);
            CKBase.Free();
            CKBase = null;
        }
        public void CheckOne(int Index)
        {
            if (Index >= 0)
            {
                CheckBase CKBase = CheckCommon.GetCheck(mCheckType);
                CKBase.Init();
                CKBase.CheckOne(Index);
                mUCCommon.ShowResult(CKBase);
                CKBase.Free();
                CKBase = null;
            }
            else
            {
                mUCCommon.ShowResult(null);
            }
        }
        private void GDBG_SizeChanged(object sender, SizeChangedEventArgs e)
        {
            Thickness NewMargin;
            LBFile.Width = e.NewSize.Width / 3;

            NewMargin = LBLFileTitle.Margin;
            NewMargin.Left = LBFile.Margin.Left;
            LBLFileTitle.Margin = NewMargin;
            LBLFileTitle.Width = LBFile.Width;

            NewMargin = LBLFileTitle.Margin;
            NewMargin.Left = LBFile.Margin.Left;
            LBLFileTitle.Margin = NewMargin;
            LBLFileTitle.Width = LBFile.Width;

            NewMargin = BtnCheckAll.Margin;
            NewMargin.Left = LBFile.Margin.Left;
            BtnCheckAll.Margin = NewMargin;
            BtnCheckAll.Width = LBFile.Width / 2;

            NewMargin = BtnCopyFileList.Margin;
            NewMargin.Left = BtnCheckAll.Margin.Left + BtnCheckAll.Width;
            BtnCopyFileList.Margin = NewMargin;
            BtnCopyFileList.Width = LBFile.Width - BtnCheckAll.Width;

            NewMargin = mUCCommon.Margin;
            NewMargin.Left = LBFile.Width;
            mUCCommon.Margin = NewMargin;
            mUCCommon.Width = e.NewSize.Width - NewMargin.Left;

        }
        private void LBFile_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (LBFile.SelectedIndex >= 0)
            {
                LoadingWindow.ShowEx();
                CheckOne(LBFile.SelectedIndex);
                LoadingWindow.HideEx();
            }
        }
        private void BtnCheckAll_Click(object sender, RoutedEventArgs e)
        {
            RefreshList();
            LoadingWindow.ShowEx();
            CheckAll();
            LoadingWindow.HideEx();
        }

        private void BtnCopyFileList_Click(object sender, RoutedEventArgs e)
        {
            string ListData = "";
            for (int i = 0; i < LBFile.Items.Count; i++)
            {
                ListData = ListData + LBFile.Items[i] + "\n";
            }
            Clipboard.SetDataObject(ListData);
        }
    }
}
