using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CheckExcel
{
    class ServerBeanData
    {
        public ServerBeanNode mRoot=new ServerBeanNode();

        public bool SetupDef(ref BeanDef beanDef)
        {
            if (!mRoot.SetupDef("", beanDef.name, "",""))
            {
                return false;
            }
            return true;
        }

        public bool WriteFile(ref StreamWriter bw, ref XlsData fileData,string spaceName)
        {
            bool result = true;
            ServerBeanNode node;
            if (mRoot.FindNodeFromName(out node, "id"))
            {
                if (fileData.mIdIndex.Count() == 0)
                {
                    if (!fileData.makeIdIndexs(node.GetNodeXlsTitle()))
                    {
                        result = false;
                    }
                }

                if (result == true)
                {
                    Dictionary<int, int>.KeyCollection ids = fileData.mIdIndex.Keys;
                    foreach (int id in ids)
                    {
                        bw.Write("  <entry>\n");
                        bool bError = false;
                        string value;
                        if (!fileData.GetXlsData(out value, "ID", id, false))
                        {
                            if (!fileData.GetXlsData(out value, "id", id, false))
                            {
                                //MainWindow.Error("没有找到列名为ID的列!");
                                bError = true;
                            }
                        }

                        if (bError)
                            bw.Write("    <int>" + id + "</int>\n");
                        else
                            bw.Write("    <int>" + value + "</int>\n");

                        bw.Write("    <fire.gsp." + spaceName + "." + mRoot.GetNodeType() + ">\n");
                        if (!mRoot.WriteToXmlFile(ref bw, ref fileData, spaceName, id, false))
                        {
                            return false;
                        }
                        bw.Write("    </fire.gsp." + spaceName + "." + mRoot.GetNodeType() + ">\n");
                        bw.Write("  </entry>\n");
                    }
                }
            }
            return true;
        }
    }
}
