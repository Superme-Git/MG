
using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;

namespace Knight.MissionEditor
{
    public class PetNode
    {
        public readonly string PetID;
        public readonly string PetName;

        public PetNode(string id, string name)
        {
            PetID = id;
            PetName = name;
        }
    }

    public class PetMapBuilder
    {
        public PetMapBuilder(out Dictionary<string, PetNode> petMap)
        {
            petMap = new Dictionary<string, PetNode>();

            FileStream fs;
            try
            {
                string path = EditorApp.dataPath.path + "\\fire.gsp.pet.CPetAttr.xml";
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
                                PetNode tmpNode = new PetNode(id, tmpstr);
                                petMap.Add(tmpNode.PetID, tmpNode);
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
