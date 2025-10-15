using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using System.IO;

namespace ExcelParse
{
    /// <summary>
    /// SelectFileDlg.xaml 的交互逻辑
    /// </summary>
    public partial class SelectFileDlg : Window
    {
        public SelectFileDlg()
        {
            InitializeComponent();
            mSrcXlsPath = "";
            mDstFileList = new Dictionary<string,int>();
        }

        public string mSrcXlsPath;
        public Dictionary<string,int> mDstFileList;
        public bool mOk;
        FileInfo[] parentXlsxFoldInfo;
        FileInfo[] parentXlsmFoldInfo;
        
        public void Initialize()
        {
            mLeftList.Items.Clear();
            mRightList.Items.Clear();
            mDstFileList.Clear();

            DirectoryInfo parentFolder = new DirectoryInfo(mSrcXlsPath);
            parentXlsxFoldInfo = parentFolder.GetFiles("*.xlsx", SearchOption.AllDirectories);
            
            string NewPattern = "*.xlsx";
            NewPattern = NewPattern.Replace("*", "");
            NewPattern = NewPattern.Replace("?", "");

            refreshItem(parentXlsxFoldInfo, "");

            DirectoryInfo parentFolder2 = new DirectoryInfo(mSrcXlsPath);
            parentXlsmFoldInfo = parentFolder.GetFiles("*.xlsm", SearchOption.AllDirectories);

            string NewPattern2 = "*.xlsm";
            NewPattern2 = NewPattern.Replace("*", "");
            NewPattern2 = NewPattern.Replace("?", "");

            refreshItem(parentXlsmFoldInfo, "");
        }

        public void Release()
        { 
        }
        private void mLeftButton_Click(object sender,RoutedEventArgs e)
        {
            if(mLeftList.SelectedItem!=null)
            {
                object o = (object)mLeftList.SelectedItem;
                mDstFileList.Add(o.ToString(), 0);
                mRightList.Items.Add(mLeftList.SelectedItem);
                mLeftList.Items.Remove(mLeftList.SelectedItem);
            }
        }
        private void mRightButton_Click(object sender, RoutedEventArgs e)
        {
            if(mRightList.SelectedItem!=null)
            {
                object o = (object)mRightList.SelectedItem;
                mDstFileList.Remove(o.ToString());
                mLeftList.Items.Add(mRightList.SelectedItem);
                mRightList.Items.Remove(mRightList.SelectedItem);
            }
        }

        private void mOkButton_Click(object sender, RoutedEventArgs e)
        {
            mOk=true;
            Close();
        }

        private void mCancelButton_Click(object sender, RoutedEventArgs e)
        {
            mOk=false;
            Close();
        }

        private void refreshItem(FileInfo [] fi,string text)
        {
            if (fi == null)
                return;

            if (text.Length == 0)
            {
                for (int i = 0; i < fi.Length; i++)
                {
                    mLeftList.Items.Add(fi[i].Name);
                }
            }
            else
            {
                for (int i = 0; i < fi.Length; i++)
                {
                    for (int j = 0; j < text.Length && j < fi[i].Name.Length; j++)
                    {
                        if (text[j] != fi[i].ToString()[j])
                        {
                            continue;
                        }
                        int data;
                        if (!mDstFileList.TryGetValue(fi[i].Name, out data))
                        {
                            mLeftList.Items.Add(fi[i].Name);
                        }
                    }
                }
            }
        }

        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            TextBox tb = (TextBox)sender;
            
            string text = tb.Text;
            mLeftList.Items.Clear();
            refreshItem(parentXlsxFoldInfo, text);
            refreshItem(parentXlsmFoldInfo, text);
            
        }
    }
}
