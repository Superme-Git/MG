using System;
using System.Text;

namespace CSTools
{
    class ToolsStr
    {
        static public string CombineStr(params string[] Str)
        {
            string Result = "";
            for (int i = 0; i < Str.Length; i++)
            {
                Result += Str[i];
            }
            return Result;
        }        
        static public string[] StrArr(params string[] Str)
        {
            return Str;
        }
        static public string Strs(string SampleStr, int Count)
        {
            string Result = "";
            for (int i = 0; i < Count; i++)
            {
                Result += SampleStr;
            }
            return Result;
        }
        static public string Spaces(int Count)
        {
            return Strs(" ", Count);//32 \s
        }
        static public string Tabs(int Count)
        {
            return Strs("\t", Count);//9 \t
        }
        static public string DH()
        {
            return ",";
        }
        static public string YH()
        {
            return "\"";
        }
        static public string XG()
        {
            return "\\";
        }
        static public string Enter()
        {
            return "\r\n";
        }

        static public Byte[] UToA(string UnicodeStr)
        {
            System.Text.Encoding GB2312 = System.Text.Encoding.GetEncoding("GB2312");
            Byte[] Result = GB2312.GetBytes(UnicodeStr);
            return Result;
            
            //Byte[] UnicodeByte = Encoding.Unicode.GetBytes(V);
            //string Result = Encoding.ASCII.GetString(UnicodeByte);
            //Byte[] AsciiByte = Encoding.Default.GetBytes(Result);
            //Result = Encoding.Unicode.GetString(AsciiByte);

            //byte[] uc = Encoding.Unicode.GetBytes(s);

            //Convert.ToString(s)

            //char[] chararray = new char[] { 'a', 'b', 'c' };
            //Converter<char,byte> del = delegate(char,input)
            //{
            //    return (byte)input;
            //}
            //byte[] byteArray = Array.ConvertAll<char,byte>(chararray,del);
            //string str = Encoding.Default.GetString(byteArray);

        }

        static public string AToU(Byte[] AsciiBytes)
        {
            string Result = Encoding.Default.GetString(AsciiBytes);
            return Result;
        }


        static public Byte[] StrToBA(string Val)
        {
            Byte[] Result = Encoding.Default.GetBytes(Val);
            return Result;
        }
        static public string BAToStr(Byte[] Val)
        {
            string Result = Encoding.Default.GetString(Val);
            return Result;
        }

    }
}
