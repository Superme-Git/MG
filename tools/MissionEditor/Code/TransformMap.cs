//////////////////////////////////////////////////////////////////////////
//  NpcMap 用来读表，建立NpcMap显示Npc信息
//////////////////////////////////////////////////////////////////////////


using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;

namespace Knight.MissionEditor
{
    public class TransformNode
    {
        public readonly string TransformID;
        public readonly string TransformName;

        public TransformNode(string id, string name)
        {
            TransformID = id;
            TransformName = name;
        }
    }

    public class TransformMapBuilder
    {
        public TransformMapBuilder(out Dictionary<string, TransformNode> transformMap)
        {
            transformMap = new Dictionary<string, TransformNode>();

            FileStream fs;
            try
            {
                string path = EditorApp.dataPath.path + "\\fire.gsp.npc.cnpcshape.xml";
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
                                string tmpstr = "";
                                id = textReader.GetAttribute("id");
                                tmpstr = textReader.GetAttribute("name");
                                TransformNode tmpNode = new TransformNode(id, tmpstr);
                                transformMap.Add(tmpNode.TransformID, tmpNode);
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
