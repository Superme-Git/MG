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
    /// InsertRequestOtherQuest.xaml 的交互逻辑
    /// </summary>
    public partial class InsertRequestOtherQuest : Window
    {
        public delegate void passValueHandler(object sender, PassValuesEventArgs e);

        public event passValueHandler PassValuesEvent;
        public InsertRequestOtherQuest()
        {
            InitializeComponent();

            WindowStartupLocation = WindowStartupLocation.CenterScreen;

            IR_tbShowText.Foreground = (Brush)new BrushConverter().ConvertFromString(MainWindow.getSelectedColor());
        }

        private void handleRequestOtherQuestSureClick(object sender, RoutedEventArgs e)
        {
            String rValue = "<Q t= qid= c= ></Q>";
            string tText = IR_tbShowText.Text;
            string cText = IR_tbShowText.Foreground.ToString();
            string qid = IR_tbQuestID.Text;

            int posText = rValue.IndexOf("t=");
            rValue = rValue.Insert(posText + 2, "\"" + tText + "\"");

            int posColor = rValue.IndexOf("c=");
            rValue = rValue.Insert(posColor + 2, "\"" + cText.Substring(1, 8) + "\"");

            int posDid = rValue.IndexOf("qid=");
            rValue = rValue.Insert(posDid + 4, "\"" + qid + "\"");

            PassValuesEventArgs args = new PassValuesEventArgs(tText, rValue);
            PassValuesEvent(this, args);
            Close();
        }
    }
}
