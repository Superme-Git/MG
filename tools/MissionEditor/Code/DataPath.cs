using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.IO;
using Microsoft.Win32;
using Knight.MissionEditor.Xaml;
using System.Windows.Forms;
using System.Xml.Serialization;

namespace Knight.MissionEditor
{
    public class DataPath
    {
        //data Path
        public string path
        {
            get;
            set;
        }

        static public DataPath ReadFromXML()
        {
            if (File.Exists("DataPathConfig.xml"))
            {
                FileStream fs = new FileStream("DataPathConfig.xml", FileMode.Open);

                XmlSerializer serializer = new XmlSerializer(typeof(DataPath));

                DataPath db = serializer.Deserialize(fs) as DataPath;

                return db;
            }
            else
            {
                System.Windows.MessageBox.Show("请先选择数据文件夹目录");
                FolderBrowserDialog dlg = new FolderBrowserDialog();               
                DialogResult result = dlg.ShowDialog();

                while (result == DialogResult.OK)
                {
                    string testfilename = dlg.SelectedPath + "\\fire.gsp.npc.CNpcInAll.xml";
                    if (File.Exists(testfilename))
                    {
                        DataPath newPath = new DataPath();
                        newPath.path = dlg.SelectedPath;

                        FileStream fs = new FileStream("DataPathConfig.xml", FileMode.Create);

                        XmlSerializer serializer = new XmlSerializer(typeof(DataPath));
                        serializer.Serialize(fs, newPath);

                        fs.Flush();
                        fs.Close();
                        return newPath;
                    }
                    else
                    {
                        System.Windows.MessageBox.Show("文件路径不正确请从新选择");
                        result = dlg.ShowDialog();
                    }
               }
                System.Windows.MessageBox.Show("没有数据文件无法初始化编辑器，程序将自动退出");
                Environment.Exit(0);
            }
            return null;
        }
   }



}