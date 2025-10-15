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

namespace QuestInput
{
    /// <summary>
    /// SetFontStyle.xaml 的交互逻辑
    /// </summary>
    public partial class SetFontStyle : Window
    {
        public delegate void passValueHandler(object sender, PassValuesEventArgs e);

        public event passValueHandler PassValuesEvent;

        private int sFontIndex = 0;
        public SetFontStyle()
        {
            InitializeComponent();
            WindowStartupLocation = WindowStartupLocation.CenterScreen;
        }

        private void handleFontSelected(object sender, SelectionChangedEventArgs e)
        {
            sFontIndex = fontCombox.SelectedIndex;
        }

        private void handleQDButtonClick(object sender, RoutedEventArgs e)
        {
            PassValuesEventArgs args = new PassValuesEventArgs(sFontIndex, Colorpicker.getSelectedColor());
            PassValuesEvent(this, args);
            Close();
        }
    }
}
