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
    /// InsertGoto.xaml 的交互逻辑
    /// </summary>
    public partial class InsertGoto : Window
    {
        public delegate void passValueHandler(object sender, PassValuesEventArgs e);

        public event passValueHandler PassValuesEvent;
        public InsertGoto()
        {
            InitializeComponent();
            WindowStartupLocation = WindowStartupLocation.CenterScreen;

            IG_tbShowText.Foreground = (Brush)new BrushConverter().ConvertFromString(MainWindow.getSelectedColor());
        }

        private void handleIGQDClick(object sender, RoutedEventArgs e)
        {
            string rValue = "<G t= c= m= x= y= npcid= ></G>";
            string tText = IG_tbShowText.Text;
            string cText = IG_tbShowText.Foreground.ToString();
            string mText = IG_tbMapID.Text;
            string xText = IG_tbPosX.Text;
            string yText = IG_tbPosY.Text;
            string npcText = IG_tbNPCID.Text;

            int posText = rValue.IndexOf("t=");
            rValue = rValue.Insert(posText + 2, "\"" + tText + "\"");

            int posColor = rValue.IndexOf("c=");
            rValue = rValue.Insert(posColor + 2, "\"" + cText.Substring(1, 8) + "\"");

            int posMap = rValue.IndexOf("m=");
            rValue = rValue.Insert(posMap + 2, "\"" + mText + "\"");

            int posX = rValue.IndexOf("x=");
            rValue = rValue.Insert(posX + 2, "\"" + xText + "\"");

            int posY = rValue.IndexOf("y=");
            rValue = rValue.Insert(posY + 2, "\"" + yText + "\"");

            int posNPC = rValue.IndexOf("npcid=");
            rValue = rValue.Insert(posNPC + 6, "\"" + npcText + "\"");

            PassValuesEventArgs args = new PassValuesEventArgs(tText, rValue);
            PassValuesEvent(this, args);
            Close();
        }
    }
}
