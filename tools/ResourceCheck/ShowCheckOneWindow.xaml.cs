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

namespace ResourceCheck
{
    public partial class ShowCheckOneWindow : Window
    {
        string mShowType;
        UCCommon mUCCommon;
        Image mImage;
        public ShowCheckOneWindow()
        {
            InitializeComponent();
        }
        private void Window_Closed(object sender, EventArgs e)
        {
            UnInit();
        }
        public void Init(string ShowType, CheckBase CKBase, string ImageURL)
        {
            mShowType = ShowType;
            if (ShowType == "Table" || ShowType == "Effect" || ShowType == "Particle" || ShowType == "Animation")
            {
                mUCCommon = new UCCommon();
                mUCCommon.Init(mShowType);
                mUCCommon.ShowResult(CKBase);

                Thickness NewMargin;
                NewMargin = mUCCommon.Margin;
                NewMargin.Top = 30;
                mUCCommon.Margin = NewMargin;
                GDBG.Children.Add(mUCCommon);
                
            }
            else if (ShowType == "")
            {
                mImage = new Image();
                Uri UU = new Uri(ImageURL);
                try
                {
                    mImage.Source = new BitmapImage(UU);
                    UU = null;
                }
                catch (System.IO.FileFormatException)
                {
                    mImage = null;
                    string ShowText = "图片文件解析失败";
                    if (ImageURL.EndsWith("dds") == true)
	                {
                        ShowText = ShowText + ",暂不支持ARGB8格式的DDS文件";
	                }
                    MessageBox.Show(ShowText);
                    //throw;
                    return;
                }

                mImage.HorizontalAlignment = System.Windows.HorizontalAlignment.Stretch;
                mImage.VerticalAlignment = System.Windows.VerticalAlignment.Stretch;

                Thickness NewMargin;
                NewMargin = mImage.Margin;
                NewMargin.Top = 30;
                mImage.Margin = NewMargin;

                //Width = mImage.Source.Width;
                //Height = mImage.Source.Height + 40 + TBFileName.Height;

                GDBG.Children.Add(mImage);
            }
            else
            {
                return;
            }
            TBFileName.Text = ImageURL;
            ShowDialog();
        }
        public void UnInit()
        {
            if (mUCCommon != null)
            {
                mUCCommon.Free();
                mUCCommon = null;
            }
            if (mImage != null)
            {
                mImage = null;
            }
        }
    }
}
