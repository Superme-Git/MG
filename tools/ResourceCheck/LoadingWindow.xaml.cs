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
    public partial class LoadingWindow : Window
    {
        static public LoadingWindow sLoadingWindow;

        public LoadingWindow()
        {
            InitializeComponent();
        }
        
        static public void ShowEx()
        {
            if (sLoadingWindow != null)
            {
                return;
            }
            sLoadingWindow = new LoadingWindow();
            sLoadingWindow.Owner = Option.sMainWindow;
            //sLoadingWindow.Height = Option.sMainWindow.Height;
            //sLoadingWindow.Width = Option.sMainWindow.Width;
            Option.sMainWindow.IsEnabled = false;
            
            //Uri U = new Uri("F:/Work/MT_G/tools/ResourceCheck/B.png");
            //sLoadingWindow.ImgIcon.Source = new BitmapImage(U);
            sLoadingWindow.Show();
            
        }
        static public void HideEx()
        {
            if (sLoadingWindow == null)
            {
                return;
            }
            sLoadingWindow.Close();
            sLoadingWindow = null;
            Option.sMainWindow.IsEnabled = true;   
        }
    }
}
