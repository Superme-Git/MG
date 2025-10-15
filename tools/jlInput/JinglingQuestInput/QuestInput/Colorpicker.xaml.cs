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
using System.Windows.Navigation;
using System.ComponentModel;

namespace QuestInput
{
    /// <summary>
    /// Colorpicker.xaml 的交互逻辑
    /// </summary>
    public partial class Colorpicker : UserControl
    {
        private static string sColor = "";
        public Colorpicker()
        {
            InitializeComponent();
        }

        public Brush SelectedColor
        {
            get { return (Brush)GetValue(SelectedColorProperty); }
            set { SetValue(SelectedColorProperty, value); }
        }

        public static readonly DependencyProperty SelectedColorProperty = DependencyProperty.Register("SelectedColor", typeof(Brush), typeof(Colorpicker), new UIPropertyMetadata(null));

        private void superCombo_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            sColor = superCombo.SelectedValue.ToString();
        }

        public static string getSelectedColor()
        {
            return sColor;
        }
    }
}
