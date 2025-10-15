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
using System.IO;
using CSTools;
namespace QuestInput
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        private string[] CEGUIFonts = { "simhei-10", "simhei-12", "simhei-13", "simhei-14", "simhei-16", "simhei-18", "simhei-20"};
        private int[] mFontSize = { 10, 12, 13, 14, 16, 18, 20 };
        private static string defaultColor = "#FF000000";
        private static int m_defalutColor = 0;
        private static string m_Color1 = "";
        private static string m_Color2 = "";
        private static string m_Color3 = "";
        private static string m_Color4 = "";
        private static int m_defalutFontSize = 0;
        private static double m_FontSize1 = 0;
        private static double m_FontSize2 = 0;
        private static double m_FontSize3 = 0;
        private static double m_FontSize4 = 0;
        private static string m_strPhrase1 = "";
        private static string m_strPhrase2 = "";
        public static ToolsIni mOptionIni = new ToolsIni("Qusetinputinfo.ini");
        private static string m_selectColor = "";
        private static double m_selectFontSize = 0;
        private static string m_ColorLink = "";
        private static string m_bgColor1 = "#FFFFFFFF";
        private static string m_bgColor2 = "#ff000000";
        private static string m_bgColor3 = "#ffac9075";
        private static string m_bgColor4 = "#ff0c0605";
        private static int m_SelectBg = 1;
        public MainWindow()
        {
            InitializeComponent();
            
            WindowStartupLocation = WindowStartupLocation.CenterScreen;
            GetConfigPath();
            UpdateUI();
        }

        private void GetConfigPath()
        {
            string defalutColor = mOptionIni.FindAndAddApp("Color", "Defalut").FindAndAddItem("Defalut").GetValue();
            m_defalutColor = int.Parse(defalutColor);
            m_Color1 = mOptionIni.FindAndAddApp("Color", "Color1").FindAndAddItem("Color1").GetValue();
            m_Color2 = mOptionIni.FindAndAddApp("Color", "Color2").FindAndAddItem("Color2").GetValue();
            m_Color3 = mOptionIni.FindAndAddApp("Color", "Color3").FindAndAddItem("Color3").GetValue();
            m_Color4 = mOptionIni.FindAndAddApp("Color", "Color4").FindAndAddItem("Color4").GetValue();
            string defalutFontSize = mOptionIni.FindAndAddApp("Color", "Defalut").FindAndAddItem("Defalut").GetValue();
            m_defalutFontSize = int.Parse(defalutFontSize);
            string FontSize1 = mOptionIni.FindAndAddApp("Text", "Text1").FindAndAddItem("Text1").GetValue();
            m_FontSize1 = int.Parse(FontSize1);
            string FontSize2 = mOptionIni.FindAndAddApp("Text", "Text2").FindAndAddItem("Text2").GetValue();
            m_FontSize2 = int.Parse(FontSize2);
            string FontSize3 = mOptionIni.FindAndAddApp("Text", "Text3").FindAndAddItem("Text3").GetValue();
            m_FontSize3 = int.Parse(FontSize3);
            string FontSize4 = mOptionIni.FindAndAddApp("Text", "Text4").FindAndAddItem("Text4").GetValue();
            m_FontSize4 = int.Parse(FontSize4);
            m_strPhrase1 = mOptionIni.FindAndAddApp("Phrase", "Phrase1").FindAndAddItem("Phrase1").GetValue();
            m_strPhrase2 = mOptionIni.FindAndAddApp("Phrase", "Phrase2").FindAndAddItem("Phrase2").GetValue();
            m_ColorLink = mOptionIni.FindAndAddApp("Color", "ColorLink").FindAndAddItem("ColorLink").GetValue();
        }
        private void UpdateUI()
        {
            m_rBtnColor1.IsChecked = false;
            m_rBtnColor2.IsChecked = false;
            m_rBtnColor3.IsChecked = false;
            m_rBtnColor4.IsChecked = false;
            if (m_defalutColor == 1)
            {
                m_rBtnColor1.IsChecked = true;
                m_selectColor = m_Color1;
            }
            else if (m_defalutColor == 2)
            {
                m_rBtnColor2.IsChecked = true;
                m_selectColor = m_Color2;
            }
            else if (m_defalutColor == 3)
            {
                m_rBtnColor3.IsChecked = true;
                m_selectColor = m_Color3;
            }
            else if (m_defalutColor == 4)
            {
                m_rBtnColor4.IsChecked = true;
                m_selectColor = m_Color4;
            }
            m_rBtnFont1.IsChecked = false;
            m_rBtnFont2.IsChecked = false;
            m_rBtnFont3.IsChecked = false;
            m_rBtnFont4.IsChecked = false;

            if (m_defalutColor == 1)
            {
                m_rBtnFont1.IsChecked = true;
                m_selectFontSize = m_FontSize1;
            }
            else if (m_defalutColor == 2)
            {
                m_rBtnFont2.IsChecked = true;
                m_selectFontSize = m_FontSize2;
            }
            else if (m_defalutColor == 3)
            {
                m_rBtnFont3.IsChecked = true;
                m_selectFontSize = m_FontSize3;
            }
            else if (m_defalutColor == 4)
            {
                m_rBtnFont4.IsChecked = true;
                m_selectFontSize = m_FontSize4;
            }
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Background = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Text = m_selectColor;
            tCurrentFont.Text = "simhei-" + m_selectFontSize;


            m_rBtnColor1.Background = (Brush)brushConverter.ConvertFromString(m_Color1);
            m_rBtnColor1.Content = m_Color1;

            m_rBtnColor2.Background = (Brush)brushConverter.ConvertFromString(m_Color2);
            m_rBtnColor2.Content = m_Color2;

            m_rBtnColor3.Background = (Brush)brushConverter.ConvertFromString(m_Color3);
            m_rBtnColor3.Content = m_Color3;

            m_rBtnColor4.Background = (Brush)brushConverter.ConvertFromString(m_Color4);
            m_rBtnColor4.Content = m_Color4;

            m_rBtnFont1.Content = "simhei-" + m_FontSize1;
            m_rBtnFont2.Content = "simhei-" + m_FontSize2;
            m_rBtnFont3.Content = "simhei-" + m_FontSize3;
            m_rBtnFont4.Content = "simhei-" + m_FontSize4;

            m_BtnPhrase1.Content = m_strPhrase1;
            m_BtnPhrase2.Content = m_strPhrase2;

            tShowText.TextWrapping = TextWrapping.Wrap;

            brush = (Brush)brushConverter.ConvertFromString(m_bgColor1);
            m_bgBtn1.Background = brush;

            brush = (Brush)brushConverter.ConvertFromString(m_bgColor2);
            m_bgBtn2.Background = brush;

            brush = (Brush)brushConverter.ConvertFromString(m_bgColor3);
            m_bgBtn3.Background = brush;

            brush = (Brush)brushConverter.ConvertFromString(m_bgColor4);
            m_bgBtn4.Background = brush;

            createEmotion();
        }

        private void createEmotion()
        {
            ComboBox box = this.tEmotionCombobox;
            for (int i = 100; i < 246; i++)
            {
                ComboBoxItem item = new ComboBoxItem();
                StackPanel p = new StackPanel();
                Image A = new Image();
                A.Width = 30;
                A.Height = 30;
                A.Source = new BitmapImage(new Uri("pack://application:,,,/biaoqing/" + i + ".png"));
                p.Children.Add(A);
                item.Content = p;
                box.Items.Add(item);
            }
        }

        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            
        }

        /* 点击设置字体颜色*/
        private void handleSetFontStyleClick(object sender, RoutedEventArgs e)
        {
            SetFontStyle fDlg = new SetFontStyle();
            fDlg.PassValuesEvent += new SetFontStyle.passValueHandler(ReceiveValues);
            fDlg.Show();
        }

        /*关闭设置字体颜色界面 取得返回值 */
        private void ReceiveValues(object sender, PassValuesEventArgs e)
        {
            int fontIndex = e.Value1;
            string colorValue = e.Value2;
            int fSize = 12;
            if (fontIndex == -1) 
            {
                tCurrentFont.Text = CEGUIFonts[0];
                fSize = mFontSize[0];
            }
            else
            {
                tCurrentFont.Text = CEGUIFonts[fontIndex];
                fSize = mFontSize[fontIndex];
            }

            if (colorValue == "")
            {
                tCurrentColor.Text = defaultColor;
                colorValue = defaultColor;
            }
            else
            {
                tCurrentColor.Text = colorValue;
            }

            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(colorValue);
            tCurrentColor.Background = (Brush)brushConverter.ConvertFromString(colorValue);

            inputBox.Foreground = brush;
            inputBox.FontSize = fSize;
        }

        /*点击添加事件 */
        private void handleAddClick(object sender, RoutedEventArgs e)
        {
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tShowText.Inlines.Add(new Run(inputBox.Text) { Foreground = brush, FontSize = m_selectFontSize });
            inputBox.Clear();
        }

        /*点击换行事件*/
        private void handleLineBreak(object sender, RoutedEventArgs e)
        {
            tShowText.Inlines.Add(new LineBreak());
        }

        /*点击删除上一段事件*/
        private void handleRemoveLast(object sender, RoutedEventArgs e)
        {
            tShowText.Inlines.Remove(tShowText.Inlines.LastInline);
        }

        private void handleCreateCEGUIStringValue(object sender, RoutedEventArgs e)
        {
            Clipboard.SetText(getDataForText());

            Notify dlg = new Notify();
            dlg.Show();
            tShowText.Text = "";
        }

        private void handleInsertEmotion(object sender, RoutedEventArgs e)
        {
            int index = tEmotionCombobox.SelectedIndex;
            if (index != -1)
            {
                BrushConverter brushConverter = new BrushConverter();
                Brush brush = (Brush)brushConverter.ConvertFromString(defaultColor);
                tShowText.Inlines.Add(new Run("##" + (100 + index)) { Foreground = brush, FontSize = inputBox.FontSize });
            }
        }

        /*点击寻路至NPC按钮*/
        private void handleClickInsertGotoButton(object sender, RoutedEventArgs e)
        {
            InsertGoto fDlg = new InsertGoto();
            fDlg.PassValuesEvent += new InsertGoto.passValueHandler(ReceiveInsertGoValues);
            fDlg.Show();
        }

        private void ReceiveInsertGoValues(object sender, PassValuesEventArgs e)
        {
            string textForShowing = e.sValue1;
            string textCEGUI = e.sValue2;
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_ColorLink);
            tShowText.Inlines.Add(new InsertInline() { Text = textForShowing, Foreground = brush, FontSize = m_selectFontSize, TextDecorations = TextDecorations.Underline, CEGUIString = textCEGUI });
        }

        private void ReceiveInsertOpenDialogValues(object sender, PassValuesEventArgs e)
        {
            string textForShowing = e.sValue1;
            string textCEGUI = e.sValue2;
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_ColorLink);
            tShowText.Inlines.Add(new InsertInline() { Text = textForShowing, Foreground = brush, FontSize = m_selectFontSize, TextDecorations = TextDecorations.Underline, CEGUIString = textCEGUI });
        }

        private void ReceiveInsertRequestQuestValues(object sender, PassValuesEventArgs e)  
        {
            string textForShowing = e.sValue1;
            string textCEGUI = e.sValue2;
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_ColorLink);
            tShowText.Inlines.Add(new InsertInline() { Text = textForShowing, Foreground = brush, FontSize = m_selectFontSize, TextDecorations = TextDecorations.Underline, CEGUIString = textCEGUI });
        }

        public static string getSelectedColor()
        {
            String rColorValue = "";
            rColorValue = m_ColorLink;
            if (rColorValue == "")
            {
                rColorValue = defaultColor;
            }
            return rColorValue;
        }

        /*点击打开界面链接*/
        private void handleInsertOpenDialogClick(object sender, RoutedEventArgs e)
        {
            InsertOpenDialog fDlg = new InsertOpenDialog();
            fDlg.PassValuesEvent += new InsertOpenDialog.passValueHandler(ReceiveInsertOpenDialogValues);
            fDlg.Show();
        }

        private void handleRequestOtherQuest(object sender, RoutedEventArgs e)
        {
            InsertRequestOtherQuest fDlg = new InsertRequestOtherQuest();
            fDlg.PassValuesEvent += new InsertRequestOtherQuest.passValueHandler(ReceiveInsertOpenDialogValues);
            fDlg.Show();
        }

        public static void NOTIFY_CREATE_DATA_COMPLETE()
        {

        }

        private void CreatFile_Click(object sender, RoutedEventArgs e)
        {
            
            string strDirectory = "\\" + "精灵文件";
            string str = System.IO.Path.GetFullPath(strDirectory);
            if (!Directory.Exists(strDirectory))
            {
                Directory.CreateDirectory(strDirectory);
            }
            string strFileName = DateTime.Now.ToString();
            string strFileExtension = ".txt";
            strFileName = strFileName.Replace(" ", "-");
            strFileName = strFileName.Replace("/", "");
            strFileName = strFileName.Replace(":", "");
            FileStream fs = File.Open(strDirectory + "\\" + strFileName + strFileExtension, FileMode.OpenOrCreate);
            if (fs != null)
            {
                StreamWriter bw = new StreamWriter(fs, Encoding.GetEncoding("GB2312"));
                if (bw != null)
                {
                    StringBuilder strb = new StringBuilder();
                    strb.Append(getDataForText());
                    bw.WriteLine(strb);
                    bw.Close();  
                }
                fs.Close();
                Notify dlg = new Notify();
                dlg.Show();
                tShowText.Text = "";
            }
        }
        private string getDataForText()
        {
            String clipBordText = "";
            foreach (Inline line in tShowText.Inlines)
            {
                String value = "<T t= c= f=\"simhei-\"></T>";
                if (line is InsertInline)
                {
                    InsertInline gotoLink = (InsertInline)line;
                    value = gotoLink.CEGUIString;
                }
                else if (line is Run)
                {
                    Run r = (Run)line;
                    Brush brush = r.Foreground;
                    String text = r.Text;
                    int fontSize = (int)r.FontSize;
                    if (text.IndexOf("##") != -1) //插入表情
                    {
                        string evalue = "<E e=></E>";
                        int posEmotion = evalue.IndexOf("e=");
                        value = evalue.Insert(posEmotion + 2, "\"" + text.Substring(2, 3) + "\"");
                    }
                    else
                    {
                        int posText = value.IndexOf("t=");
                        value = value.Insert(posText + 2, "\"" + text + "\"");
                        int posColor = value.IndexOf("c=");
                        value = value.Insert(posColor + 2, "\"" + brush.ToString().Substring(1, 8) + "\"");
                        int posFont = value.IndexOf("simhei");
                        value = value.Insert(posFont + 7, fontSize.ToString());
                    }
                }
                else if (line is LineBreak)
                {
                    value = "<B></B>";
                }

                clipBordText += value;
            }
            return clipBordText;
        }

        private void Window_Unloaded(object sender, RoutedEventArgs e)
        {
            mOptionIni.Free();
        }

        private void m_rBtnColor1_Checked(object sender, RoutedEventArgs e)
        {
            m_selectColor = m_Color1;
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Background = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Text = m_selectColor;
            m_defalutColor = 1;
            mOptionIni.FindAndAddApp("Color", "Defalut").FindAndAddItem("Defalut").SetValue("" + m_defalutColor);
            mOptionIni.WriteToFile();
        }

        private void m_rBtnColor2_Checked(object sender, RoutedEventArgs e)
        {
            m_selectColor = m_Color2;
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Background = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Text = m_selectColor;
            m_defalutColor = 2;
            mOptionIni.FindAndAddApp("Color", "Defalut").FindAndAddItem("Defalut").SetValue("" + m_defalutColor);
            mOptionIni.WriteToFile();
        }

        private void m_rBtnColor3_Checked(object sender, RoutedEventArgs e)
        {
            m_selectColor = m_Color3;
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Background = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Text = m_selectColor;
            m_defalutColor = 3;
            mOptionIni.FindAndAddApp("Color", "Defalut").FindAndAddItem("Defalut").SetValue("" + m_defalutColor);
            mOptionIni.WriteToFile();
        }

        private void m_rBtnColor4_Checked(object sender, RoutedEventArgs e)
        {
            m_selectColor = m_Color4;
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Background = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tCurrentColor.Text = m_selectColor;
            m_defalutColor = 4;
            mOptionIni.FindAndAddApp("Color", "Defalut").FindAndAddItem("Defalut").SetValue("" + m_defalutColor);
            mOptionIni.WriteToFile();

        }

        private void m_rBtnFont1_Checked(object sender, RoutedEventArgs e)
        {
            m_selectFontSize = m_FontSize1;
            m_defalutFontSize = 1;
            tCurrentFont.Text = "simhei-" + m_selectFontSize;
            mOptionIni.FindAndAddApp("Text", "Defalut").FindAndAddItem("Defalut").SetValue("" + m_defalutColor);
            mOptionIni.WriteToFile();
        }

        private void m_rBtnFont2_Checked(object sender, RoutedEventArgs e)
        {
            m_selectFontSize = m_FontSize2;
            m_defalutFontSize = 2;
            tCurrentFont.Text = "simhei-" + m_selectFontSize;
            mOptionIni.FindAndAddApp("Text", "Defalut").FindAndAddItem("Defalut").SetValue("" + m_defalutColor);
            mOptionIni.WriteToFile();
        }

        private void m_rBtnFont3_Checked(object sender, RoutedEventArgs e)
        {
            m_selectFontSize = m_FontSize3;
            m_defalutFontSize = 3;
            tCurrentFont.Text = "simhei-" + m_selectFontSize;
            mOptionIni.FindAndAddApp("Text", "Defalut").FindAndAddItem("Defalut").SetValue("" + m_defalutColor);
            mOptionIni.WriteToFile();
        }

        private void m_rBtnFont4_Checked(object sender, RoutedEventArgs e)
        {
            m_selectFontSize = m_FontSize4;
            m_defalutFontSize = 4;
            tCurrentFont.Text = "simhei-" + m_selectFontSize;
            mOptionIni.FindAndAddApp("Text", "Defalut").FindAndAddItem("Defalut").SetValue("" + m_defalutColor);
            mOptionIni.WriteToFile();
        }

        private void m_BtnPhrase1_Click(object sender, RoutedEventArgs e)
        {
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tShowText.Inlines.Add(new Run(m_strPhrase1) { Foreground = brush, FontSize = m_selectFontSize });
        }

        private void m_BtnPhrase2_Click(object sender, RoutedEventArgs e)
        {
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_selectColor);
            tShowText.Inlines.Add(new Run(m_strPhrase2) { Foreground = brush, FontSize = m_selectFontSize });
        }

        private void m_bgBtn1_Click(object sender, RoutedEventArgs e)
        {
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_bgColor1);
            scrollView.Background = brush;
        }

        private void m_bgBtn2_Click(object sender, RoutedEventArgs e)
        {
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_bgColor2);
            scrollView.Background = brush;
        }

        private void m_bgBtn3_Click(object sender, RoutedEventArgs e)
        {
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_bgColor3);
            scrollView.Background = brush;
        }

        private void m_bgBtn4_Click(object sender, RoutedEventArgs e)
        {
            BrushConverter brushConverter = new BrushConverter();
            Brush brush = (Brush)brushConverter.ConvertFromString(m_bgColor4);
            scrollView.Background = brush;
        }
    }
}
