using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.IO;
using System.Threading;

using Excel = Microsoft.Office.Interop.Excel;



namespace NpcList
{
    class NpcMap
    {
        public NpcMap(string dir)
        {
            _basedir = dir;
            _basedir += "\\map\\";

            _npcList = new Dictionary<int, MapInfo>();

            filename = Directory.GetCurrentDirectory() + "\\npc跳转点.xlsx";
        }

        public bool BuildList(Dictionary<string,int> nameMap)
        {

            foreach (KeyValuePair<string,int> nameNode in nameMap)
            {
                int npccout = 0;
                string path = _basedir + nameNode.Key + "\\npc.dat";

                int mapid = nameNode.Value;


                if (!File.Exists(path))
                {
                    Console.WriteLine("{0}下没有npc.dat",nameNode.Key);
                }
                else
                {
                    FileStream fs;
                    try
                    {
                        fs = new FileStream(path, FileMode.Open);
                        XmlTextReader textReader = new XmlTextReader(fs);

                        while (textReader.Read())
                        {
                            switch (textReader.NodeType)
                            {
                                case XmlNodeType.Element:
                                    if (textReader.HasAttributes)
                                    {
                                        textReader.MoveToAttribute("id");
                                        int npcid = int.Parse(textReader.Value);

                                        textReader.MoveToAttribute("posx");
                                        int x = int.Parse(textReader.Value) / 16 ;

                                        textReader.MoveToAttribute("posy");
                                        int y = int.Parse(textReader.Value) / 16;

                                        MapInfo mapinfo = new MapInfo(mapid, x, y,nameNode.Key);

                                        if (!_npcList.ContainsKey(npcid))
                                        {
                                            _npcList.Add(npcid, mapinfo);
                                            ++npccout;
                                        }
                                        //else
                                        //    Console.WriteLine("npcid 为{0}已经存在", npcid);
                                    }

                                    break;
                                case XmlNodeType.Text:
                                    break;
                                case XmlNodeType.EndElement:
                                    break;
                            }
                        }
                        fs.Close();
                    }
                    catch (System.IO.FileNotFoundException)
                    {

                    }
                    Console.WriteLine("{0}下添加{1}个Npc", nameNode.Key, npccout);

                }


            }
            
            return true;
        }


        public void WriteToExcel()
        {
            Console.WriteLine("正在写入excel");

            excelApp = new Excel.Application();
            object m = Type.Missing;

            wb = excelApp.Application.Workbooks.Add(m);

            sheet = wb.Worksheets.Add(m, m, m, m) as Excel.Worksheet;

            sheet.Cells[1, 1] = "npcid";
            sheet.Cells[1, 2] = "mapid";
            sheet.Cells[1, 3] = "x";
            sheet.Cells[1, 4] = "y";
            sheet.Cells[1, 5] = "dir";


            int row = 2;
            foreach (KeyValuePair<int, MapInfo> npcnode in _npcList)
            {
                sheet.Cells[row, 1] = npcnode.Key.ToString();
                sheet.Cells[row, 2] = npcnode.Value._mapid.ToString();
                sheet.Cells[row, 3] = npcnode.Value._x.ToString();
                sheet.Cells[row, 4] = npcnode.Value._y.ToString();
                sheet.Cells[row, 5] = npcnode.Value._dir.ToString();
                ++row;

                if (row % 20 == 0)
                    Console.Write("=");
            }


            //object m = Type.Missing;
            wb.SaveCopyAs(filename);
            wb.Close(false, m, m);
            excelApp.Quit();
            
            
        }

        private string _basedir;
        //private List<string> _mapList;

        private Dictionary<int, MapInfo> _npcList;


        Excel.Application excelApp;
        Excel.Workbook wb;
        Excel.Worksheet sheet;
        string filename;
    }
}
