using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.IO;
using System.Threading;


namespace NpcList
{
    class MapNameMap
    {
        public MapNameMap(string dir)
        {
            _mydir = dir;

            _idMap = new Dictionary<int, string>();

            _nameMap = new Dictionary<string, int>();

            _mapNameList = new List<string>();
        }


        public Dictionary<string, int> GetMapDirList()
        {
            return _nameMap;
        }

        public int GetMapIDFromName(string str)
        {
           // if (idMap.)
            return 0;
        }

        public string GetNameFromID(int id)
        {
            return "";
        }

        private string _mydir;

        private Dictionary<int, string> _idMap;

        private Dictionary<string, int> _nameMap;

        private List<string> _mapNameList;


               
        public bool Init()
        {
            string path = _mydir + "\\config\\AutoConfig\\fire.gsp.map.CMapConfig.xml";
            if (!File.Exists(path))
            {
                Console.WriteLine("无法读取\\config\\AutoConfig\\fire.gsp.map.CMapConfig.xml,请确定其存在。");
                return false;
            }


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
                                int mapid = int.Parse(textReader.Value);

                                textReader.MoveToAttribute("resdir");
                                string mapdir = textReader.Value;

                                _idMap.Add(mapid, mapdir);

                                _nameMap.Add(mapdir, mapid);

                                _mapNameList.Add(mapdir);
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
                //Console.Write("目录{0}下没有找到goto.xml，请更新数据后再试!", selfmapid);
                //Console.ReadKey();
                Environment.Exit(0);
            }

            return true;
        }
    }
}