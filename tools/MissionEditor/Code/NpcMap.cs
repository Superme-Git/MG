//////////////////////////////////////////////////////////////////////////
//  NpcMap 用来读表，建立NpcMap显示Npc信息
//////////////////////////////////////////////////////////////////////////


using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;

namespace Knight.MissionEditor
{
    public class NpcNode{
        public readonly string ItemID;
        public readonly string ItemName;

        public NpcNode(string id, string name)
        {
            ItemID = id;
            ItemName = name;
        }
    }

    public class NpcMapBuilder
    {
        public NpcMapBuilder( out Dictionary<string,NpcNode> itemMap)
        {
            itemMap = new Dictionary<string, NpcNode>();

            FileStream fs;
            try
            {
                string path = EditorApp.dataPath.path + "\\fire.gsp.npc.CNpcInAll.xml";
                fs = new FileStream(path, FileMode.Open);
                XmlTextReader textReader = new XmlTextReader(fs);

                while (textReader.Read())
                {
                    switch (textReader.NodeType)
                    {
                        case XmlNodeType.Element:
                            if (textReader.HasAttributes)
                            {
                                string id = "";
                                string tmpstr="";
                                id = textReader.GetAttribute("id");
                                tmpstr = textReader.GetAttribute("name");
                                NpcNode tmpNode = new NpcNode(id, tmpstr);
                                itemMap.Add(tmpNode.ItemID, tmpNode);
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
        }
    }
}
