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
using System.Windows.Shapes;

namespace CheckExcel
{
    /// <summary>
    /// SelectXlsDlg.xaml 的交互逻辑
    /// </summary>
    public partial class SelectXlsDlg : Window
    {
        public SelectXlsDlg()
        {
            InitializeComponent();
        }
        private void RefreshSrcList(string filter)
        {
            Dictionary<string, int> selectIndex=new Dictionary<string,int>();
            for(int i=0;i<mDstList.Items.Count;i++)
            {
                selectIndex.Add(mDstList.Items[i].ToString(), i);
            }

            mSrcList.Items.Clear();
            List<string> files = MainWindow.GetFiles(MainWindow.mSrcDataXlsPath, filter);
            for(int i=0;i<files.Count;i++)
            {
                int key;
                if (!selectIndex.TryGetValue(files[i], out key))
                {
                    mSrcList.Items.Add(files[i]);
                }
            }
        }

        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            mInputBox.Text = "*";
        }

        private void mDstList_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            string selectXls = mDstList.Items[mDstList.SelectedIndex].ToString();
            mDstList.Items.RemoveAt(mDstList.SelectedIndex);
            mSrcList.Items.Add(selectXls);
        }

        private void mSrcList_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            string selectXls = mSrcList.Items[mSrcList.SelectedIndex].ToString();
            mSrcList.Items.RemoveAt(mSrcList.SelectedIndex);
            mDstList.Items.Add(selectXls);
        }

        private void Button_ClickOk(object sender, RoutedEventArgs e)
        {
            mSelectFiles.Clear();
            for(int i=0;i<mDstList.Items.Count;i++)
            {
                mSelectFiles.Add(mDstList.Items[i].ToString());
            }
            Close();
        }

        private void Button_ClickCancel(object sender, RoutedEventArgs e)
        {
            mSelectFiles.Clear();
            Close();
        }
        public List<string> mSelectFiles=new List<string>();

        private void mInputBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            string text = mInputBox.Text;
            RefreshSrcList(text);
        }
    }
}
