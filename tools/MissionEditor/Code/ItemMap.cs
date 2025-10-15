using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;
using NPOI.OpenXml4Net;

namespace Knight.MissionEditor
{
    //节点
    public class ItemMapNode
    {
        //data
        //Item ID
        public readonly string ItemID;
        public readonly string ItemName;

        public ItemMapNode(string id, string name)
        {
            ItemID = id;
            ItemName = name;
        }
      
       }

    public class ItemMapBuilder
    {
        public ItemMapBuilder(out Dictionary<string, ItemMapNode> itemMap)
        {
            itemMap = new Dictionary<string, ItemMapNode>();

            FileStream fs;
            try
            {
                string path = EditorApp.dataPath.path + "\\fire.gsp.item.CItemAttr.xml";
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

                                //for (int j = 0; j < textReader.AttributeCount; ++j)
                                //{
                                //    textReader.MoveToAttribute(j);
                                //    if (j == 0)
                                //        id = int.Parse(textReader.Value);
                                //    else if (j == 2)
                                //        tmpstr = textReader.Value;
                                //}
                                //if (textReader.Name == "record")
                                {
                                    id = textReader.GetAttribute("id");
                                    tmpstr = textReader.GetAttribute("name");


                                    ItemMapNode tmpNode = new ItemMapNode(id, tmpstr);
                                    itemMap.Add(tmpNode.ItemID, tmpNode);

                                    //Console.Write(id);
                                    //Console.Write("\n");
 
                                }
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
