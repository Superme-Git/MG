using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;
using Knight.MissionEditor;

namespace MissonEditorBuilder
{
    class Program
    {
        static void Main(string[] args)
        {
            if (!CheckArgs(args))
            {
                Console.WriteLine("参数不对!例子如下:");
                Console.WriteLine("MissonEditorBuilder.exe -inputxmlfile infilename -outputfile outfilename [0,1]（0的话outfilename是绝对路径,1是当前目录)");
                return;
            }

            db = XMLHelper.ReadFromXML(input_file_name_);

            if (db == null)
                return;
            else
            {
                Console.WriteLine("读取XML..........");
            }

            Type tp = Type.GetType("Knight.MissionEditor.MissionInfoAIO");
            Dictionary<string, ColInfo> cmap = ColInfoBuild.BuildInfo(tp);
            if (is_local_name != 0)
            {
                ouput_file_name_ = System.Environment.CurrentDirectory + "/" + ouput_file_name_;
            }
            MXLSWrite xls = new MXLSWrite(ouput_file_name_, cmap);
            xls.WriteAllData(ref db);
            xls.Save();
        }

        static bool CheckArgs(string[] args)
        {
            if (args.Length != ArgsNum)
            {
                return false;
            }

            int input_index = -1;
            int ouput_index = -1;
            for (int i = 0; i < ArgsNum; i += 2)
            {
                string arg = args[i];
                if (string.Compare(arg,"-inputxmlfile",true) == 0)
                {
                    input_index = i;
                }
                else if (string.Compare(arg, "-outputfile", true) == 0)
                {
                    ouput_index = i;
                }
            }

            if (input_index == -1 || 
                ouput_index == -1 || 
                input_index == ouput_index)
            {
                return false;
            }

            input_file_name_ = args[input_index + 1];
            ouput_file_name_ = args[ouput_index + 1];
            try
            {
                is_local_name = Convert.ToInt32(args[ArgsNum - 1]);
            }
            catch (OverflowException)
            {
                is_local_name = -1;
            }
            catch (FormatException)
            {
                is_local_name = -1;
            }

            if (is_local_name >= 0)
            {
                return true;
            }

            return false;

        }


        public static MissionDB GetMissionDB()
        {
            return db;
        }


        private static int ArgsNum = 5;
        private static MissionDB db;

        private static string input_file_name_;
        private static string ouput_file_name_;
        private static int is_local_name = -1;
    }
}
