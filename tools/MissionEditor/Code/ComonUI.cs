//////////////////////////////////////////////////////////////////////////
//   定义一些公用的UI
//////////////////////////////////////////////////////////////////////////

using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Documents;

namespace Knight.MissionEditor
{
    // 一个有默认的Lable 
    public class MLable : Label
    {
        // data 
        public TextBlock txtBlc;

        public MLable(string text)
        {
            Background = Brushes.AliceBlue;
            Foreground = Brushes.DarkSalmon;
            Margin = new Thickness(5);

            txtBlc = new TextBlock();
            txtBlc.FontSize = 12;
            txtBlc.TextAlignment = TextAlignment.Center;
            Content = txtBlc;

            txtBlc.Inlines.Add(new Italic(new Run(text)));

            if (text.Length < 6)
                Width = 70;
        }
    }// end of MLable

    //////////////////////////////////////////////////////////////////////////
    // 自己实现了一个只允许数字输入的TextBox
    /////////////////////////////////////////////////////////////////////////
    public class IntNumEditBox : TextBox
    {
        protected int MaxOfLength;

        public IntNumEditBox()
        {
            MaxOfLength = 6;
        }

        public void SetMaxLength(int length)
        {
            MaxOfLength = length;
        }
        public IntNumEditBox(int maxLength)
        {
            Margin = new Thickness(5);
            Width = 50;
            MaxOfLength = maxLength;
            TextAlignment = TextAlignment.Right;
        }

        protected override void OnPreviewTextInput(TextCompositionEventArgs args)
        {
            args.Handled = !AreAllValidNumericChars(args.Text);
            base.OnPreviewTextInput(args);
        }

        protected  virtual bool AreAllValidNumericChars(string str)
        {
            if (Text.Length >= MaxOfLength)
                return false;

            bool ret = true;
            for (int i = 0; i < str.Length; ++i)
            {
               ret &= char.IsDigit(str[i]);
            }
            return ret;
        }
     } //end of IntNumEditBox

    //////////////////////////////////////////////////////////////////////////
    // 单行 的TextBox
    /////////////////////////////////////////////////////////////////////////
    public class SingleLenEditBox : TextBox
    {
        public SingleLenEditBox()
        {
            Margin = new Thickness(5);
            Width = 80;
        }
    }

}//end of namespace