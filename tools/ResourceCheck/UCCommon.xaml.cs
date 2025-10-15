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
    public partial class UCCommon : UserControl
    {
        string mShowType;
        public UCCommon()
        {
            InitializeComponent();
        }
        public void Init(string ShowType)
        {
            mShowType = ShowType;
            ShowResult(null);
        }
        public void Free()
        {
        }
        public void ShowResult(CheckBase CKBase)
        {
            LBFileLive.Items.Clear();
            LBFileDead.Items.Clear();
            if (CKBase == null)
            {
                return;
            }
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

        private void GDBG_SizeChanged(object sender, SizeChangedEventArgs e)
        {
            Thickness NewMargin;
            LBFileLive.Width = e.NewSize.Width / 2;

            NewMargin = LBFileDead.Margin;
            NewMargin.Left = LBFileLive.Width;
            LBFileDead.Margin = NewMargin;
            LBFileDead.Width = e.NewSize.Width - NewMargin.Left;

            NewMargin = LBLFileLiveTitle.Margin;
            NewMargin.Left = LBFileLive.Margin.Left;
            LBLFileLiveTitle.Margin = NewMargin;
            LBLFileLiveTitle.Width = LBFileLive.Width;
            NewMargin = LBLFileDeadTitle.Margin;
            NewMargin.Left = LBFileDead.Margin.Left;
            LBLFileDeadTitle.Margin = NewMargin;
            LBLFileDeadTitle.Width = LBFileDead.Width;

            NewMargin = BtnCopyFileListLive.Margin;
            NewMargin.Left = LBFileLive.Margin.Left;
            BtnCopyFileListLive.Margin = NewMargin;
            BtnCopyFileListLive.Width = LBFileLive.Width;

            NewMargin = BtnCopyFileListDead.Margin;
            NewMargin.Left = LBFileDead.Margin.Left;
            BtnCopyFileListDead.Margin = NewMargin;
            BtnCopyFileListDead.Width = LBFileDead.Width;

        }
        private void LBFileLive_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (LBFileLive.SelectedIndex < 0)
            {
                return;
            }
            string PathNameFullEffect = ToolsFileStr.GetFullName(CheckCommon.sOption.mAppPath, CheckCommon.sOption.mEffectPath);
            PathNameFullEffect = ToolsFileStr.DirToPath(PathNameFullEffect);
            string FileName = LBFileLive.Items[LBFileLive.SelectedIndex].ToString();
            ShowCheckOneWindow SCOW;
            if (FileName.EndsWith("dds") == true)
            {
                MessageBox.Show("暂时不支持DDS格式文件预览");
                return;
            }
            if (FileName.EndsWith("png") == true || FileName.EndsWith("tga") == true)// || FileName.EndsWith("dds") == true
            {
                SCOW = new ShowCheckOneWindow();

                SCOW.Init("", null, PathNameFullEffect + FileName);
                return;
            }
            CheckBase CKBase = CheckCommon.GetCheck2(FileName);
            if (CKBase == null)
            {
                return;
            }
            CKBase.Init();
            CKBase.CheckOne(FileName);
            SCOW = new ShowCheckOneWindow();
            SCOW.Init(CKBase.mCheckType, CKBase, PathNameFullEffect + FileName);            
            CKBase.Free();
            CKBase = null;
        }
        private void BtnCopyFileListLive_Click(object sender, RoutedEventArgs e)
        {
            string ListData = "";
            for (int i = 0; i < LBFileLive.Items.Count; i++)
            {
                ListData = ListData + LBFileLive.Items[i] + "\n";
            }
            Clipboard.SetDataObject(ListData);
        }
        private void BtnCopyFileListDead_Click(object sender, RoutedEventArgs e)
        {
            string ListData = "";
            for (int i = 0; i < LBFileDead.Items.Count; i++)
            {
                ListData = ListData + LBFileDead.Items[i] + "\n";
            }
            Clipboard.SetDataObject(ListData);
        }
    }
}
