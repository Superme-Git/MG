using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ExcelParse
{
    class ServerNodeAttr
    {
        public string mFrom;
        public string mType;
        public string mName;
        public int mColId;
        public int mSubColId;
        public Dictionary<int, string> mDatas=new Dictionary<int,string>();
        private string ConverXmlName(string name)
        {
            string retName = "";
            for(int i=0;i<name.Length;i++)
            {
                retName += name[i];
                if (name[i] == '_')
                    retName += name[i];
            }
            return retName;
        }
        private string ConverXmlString(string text)
        {
            string xmlText = "";
            //if (text.Length > 2 && text[0] == '<')
            {
                for (int i = 0; i < text.Length; i++)
                {
                    if (text[i] == '<')
                    {
                        xmlText += "&lt;";
                    }
                    else if (text[i] == '>')
                    {
                        xmlText += "&gt;";
                    }
                    else if (text[i] == '"')
                    {
                        xmlText += "&quot;";
                    }
                    else if (text[i] == '&')
                    {
                        xmlText += "&amp;";
                    }
                    else
                        xmlText += text[i];
                }
                return xmlText;
            }
            return text;
        }
        public bool SetupData(XlsFileData fileData,int idIndex,int dataIndex)
        {
            for(int i=1;i<fileData.rows.Count;i++)
            {
                try
                {
                    XlsFileDataRow dataRow = fileData.rows[i];
                    if (dataRow.datas.Count>0)
                    {
                        string idString = dataRow.datas[idIndex];
                        if (idString == "")
                            continue;//快速跳过.
                        try
                        {
                            int id = Convert.ToInt32(idString);
                            string dataString = dataRow.datas[dataIndex];
                            try
                            {
                                mDatas.Add(id, dataString);
                            }
                            catch (ArgumentException ae)
                            {
                                continue;//已经添加了相同的键值.
                            }
                        }
                        catch (FormatException fe)
                        {
                            MainWindow.LogWarning(fe.Message + " ID格式不正确，跳过!");
                        }
                    }
                }
                catch(Exception  e)
                {
                    return false;
                }
            }
            return true;
        }
        public void WriteToXmlFile(ref StreamWriter bw, int id, bool isArray)
        {
            string data;
            if (mDatas.TryGetValue(id, out data))
            {
                if (!MainWindow.IsInvalidName(data)&&data!="")
                {
                    string name = ConverXmlName(mName);
                    data = ConverXmlString(data);
                    if (isArray)
                        bw.Write("        <" + mType + ">" + data + "</" + mType + ">\n");
                    else
                        bw.Write("      <" + name + ">" + data + "</" + name + ">\n");
                }
            }
        }
        public void WriteToXmlFileEx(StreamWriter bw, int id)
        {
            string data;
            if (mDatas.TryGetValue(id, out data))
            {
                if (!MainWindow.IsInvalidName(data) && data != "")
                {
                    string name = ConverXmlName(mName);
                    data = ConverXmlString(data);
                    bw.Write("            <" + name + ">" + data + "</" + name + ">\n");
                }
            }
        }
    }
    class ServerNode
    {
        public TableBean mBean;
        public bool mIsArray;
        public bool mIsBaseTypeArray;
        public string mArrayName;
        public string mName;//节点名字.
        public List<ServerNode> mNodes=new List<ServerNode>();//子节点.
        public List<ServerNodeAttr> mAttribs=new List<ServerNodeAttr>();//节点属性.
        public bool CreateNode(List<TableDef> tables,string space,TableBean bean,int colId)//创建节点.
        {
            if (bean.name == "SActivityNpcPos")
            {
                int asdr=3;
            }
            mBean=bean;
            ColDef cd=bean.cols[colId];
            if (MainWindow.IsBaseType(cd.type))
            {
                ServerNodeAttr attr = new ServerNodeAttr();
                attr.mFrom = cd.fromCol;
                attr.mName = cd.name;
                attr.mType = cd.type;
                if (attr.mType == "String")
                    attr.mType = "string";
                attr.mColId = colId;
                attr.mSubColId = 0;
                mAttribs.Add(attr);
            }
            else if (cd.type == "vector")
            {
                ServerNode node = new ServerNode();
                node.mIsArray = true;
                node.mArrayName = cd.name;              
                if (MainWindow.IsBaseType(cd.value))
                {
                    node.mIsBaseTypeArray = true;
                    for (int i = 0; i < cd.subCols.Count; i++)
                    {
                        SubColDef scd = cd.subCols[i];
                        ServerNodeAttr attr = new ServerNodeAttr();
                        attr.mFrom = scd.subFromCol;
                        attr.mName = cd.name;
                        attr.mType = cd.value;
                        if (attr.mType == "String")
                            attr.mType = "string";
                        attr.mColId = colId;
                        attr.mSubColId = i;
                        node.mAttribs.Add(attr);
                    }
                }
                else
                {
                    node.mIsBaseTypeArray = false;
                    node.mBean = MainWindow.FindBean(tables,cd.value);
                    List<string> vsName=MainWindow.stovs(cd.value, '.');
                    node.mName = "fire.gsp."+space+"."+vsName[vsName.Count-1];
                    if(node.mBean!=null)
                    {
                        for(int i=0;i<node.mBean.cols.Count;i++)
                        {
                            ServerNode childNode = new ServerNode();
                            childNode.mBean = node.mBean;
                            childNode.mIsArray = true;
                            childNode.mIsBaseTypeArray = false;
                            childNode.mArrayName = node.mBean.cols[i].name;
                            childNode.mName = node.mBean.cols[i].fromCol;

                            ColDef theCd=node.mBean.cols[i];
                            for(int j=0;j<theCd.subCols.Count;j++)
                            {
                                SubColDef theScd = theCd.subCols[j];
                                ServerNodeAttr attr = new ServerNodeAttr();
                                attr.mFrom = theScd.subFromCol;
                                attr.mName = theCd.name;
                                attr.mType = theCd.type;
                                if (attr.mType == "String")
                                    attr.mType = "string";
                                attr.mColId = i;
                                attr.mSubColId = j;
                                childNode.mAttribs.Add(attr);
                            }
                            node.mNodes.Add(childNode);
                        }
                    }
                    else
                    {
                        MainWindow.LogWarning("加载服务器定义时，没有找到名字为" + cd.value + "的bean");
                    }
                }
                mNodes.Add(node);
            }
            else
            {
                ServerNode node=new ServerNode();
                TableBean theBean=MainWindow.FindBean(tables,cd.type);
                for(int i=0;i<theBean.cols.Count;i++)
                {
                    node.CreateNode(tables, space,theBean, i);
                }
                mNodes.Add(node);
            }
            return true;
        }
        public bool SetupData(XlsFileData fileData,string idName)
        {
            XlsFileDataRow titleRow = fileData.rows[0];
         
            //寻找ID列.
            int idCol=-1;
            for (int i = 0; i < titleRow.datas.Count;i++ )
            {
                if(titleRow.datas[i]==idName)
                {
                    idCol = i;
                    break;
                }
            }
            if(idCol==-1)
            {
                return false;
            }

            //填充数据列.
            for (int i = 0; i < titleRow.datas.Count; i++)
            {
                string title = titleRow.datas[i];
                List<ServerNodeAttr> attrs=new List<ServerNodeAttr>();
                FindAttrFromTitle(ref attrs,title);
                for (int j = 0; j < attrs.Count; j++)
                {
                    if (!attrs[j].SetupData(fileData, idCol, i))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        public ServerNodeAttr FindAttrFromName(string attrName)
        {
            for(int i=0;i<mAttribs.Count;i++)
            {
                if (mAttribs[i].mName == attrName)
                    return mAttribs[i];
            }
            for (int i = 0; i < mNodes.Count;i++ )
            {
                ServerNodeAttr attr = mNodes[i].FindAttrFromName(attrName);
                if (attr != null)
                    return attr;
            }
            return null;
        }
        public void FindAttrFromTitle(ref List<ServerNodeAttr> attrs,string attrTitle)
        {
            for(int i=0;i<mAttribs.Count;i++)
            {
                if (mAttribs[i].mFrom == attrTitle)
                {
                    attrs.Add(mAttribs[i]);
                }
            }
            for(int i=0;i<mNodes.Count;i++)
            {
                mNodes[i].FindAttrFromTitle(ref attrs,attrTitle);
            }
        }
        public void WriteToXmlFile(StreamWriter bw,int id,bool isArray)
        {
            for (int i = 0; i < mAttribs.Count;i++ )
            {
                mAttribs[i].WriteToXmlFile(ref bw,id,isArray);
            }
            for (int i = 0; i < mNodes.Count;i++ )
            {
                if(mNodes[i].mIsArray)
                {
                    bw.Flush();
                    long backPos=bw.BaseStream.Position;
                    bw.Write("      <" + mNodes[i].mArrayName + ">\n");
                    bw.Flush();
                    long beginPos = bw.BaseStream.Position;
                    if(mNodes[i].mIsBaseTypeArray)
                    {
                        mNodes[i].WriteToXmlFile(bw, id, true);
                    }
                    else
                    {
                        ServerNode theNode=mNodes[i];
                        if(theNode.mNodes.Count>0)
                        {
                            int nodeCount = theNode.mNodes.Count;
                            int attribCount = theNode.mNodes[0].mAttribs.Count();
                            for (int j = 0; j < attribCount; j++)
                            {
                                bw.Flush();
                                long backPosEx = bw.BaseStream.Position;
                                bw.Write("          <"+theNode.mName+">\n");
                                bw.Flush();
                                long beginPosEx = bw.BaseStream.Position;
                                for (int k = 0; k < nodeCount;k++)
                                {
                                    ServerNodeAttr attr = theNode.mNodes[k].mAttribs[j];
                                    attr.WriteToXmlFileEx(bw, id);
                                }
                                bw.Flush();
                                if (bw.BaseStream.Position > beginPosEx)
                                    bw.Write("          </" + theNode.mName + ">\n");
                                else
                                    bw.BaseStream.Seek(backPosEx, 0);
                            }
                        }
                    }
                    bw.Flush();

                    if (bw.BaseStream.Position > beginPos)
                        bw.Write("      </" + mNodes[i].mArrayName + ">\n");
                    else
                    {
                        bw.BaseStream.Seek(backPos, 0);
                        if (!mNodes[i].mIsBaseTypeArray)
                        {
                            bw.Write("      <" + mNodes[i].mArrayName + "/>\n");
                        }
                    }
                }
                else
                {
                    int wer=3;
                }
            }
        }
        public bool IsEnableAttribs(int id)
        {
            for(int i=0;i<mAttribs.Count;i++)
            {
                string data;
                if(mAttribs[i].mDatas.TryGetValue(id,out data))
                {
                    if(!MainWindow.IsInvalidName(data))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    class ServerDataDef//服务器数据.
    {
        private ServerNode mRoot=new ServerNode();//入口节点.
        private string mXlsName;//xls的文件名字.
        public string mSpaceName;
        public string mBeanName;
        public string GetXlsName() { return mXlsName;  }
        public void CreateDef(List<TableDef> tables, string space,TableBean tb)
        {
            if(tb.baseclass!="")
            {
                TableBean baseClassBean=MainWindow.FindBean(tables, tb.baseclass);
                CreateDef(tables, space, baseClassBean);
            }
            for(int i=0;i<tb.cols.Count;i++)
            {
                mRoot.CreateNode(tables,space,tb,i);
            }
            mXlsName = tb.from;
            mSpaceName = space;
            mBeanName = tb.name;
        }
        public void SetupData(XlsFileData fileData)
        {
            ServerNodeAttr idAttr = mRoot.FindAttrFromName("id");
            mRoot.SetupData(fileData,idAttr.mFrom);
        }
        public void WriteToXmlFile(string dstPath)
        {
            string fullXmlFileName = dstPath + "fire.gsp." + mSpaceName + "." + mRoot.mBean.name + ".xml";
            File.Delete(fullXmlFileName);

            FileStream fs = File.Open(fullXmlFileName, FileMode.CreateNew);
            if (fs != null)
            {
                StreamWriter bw = new StreamWriter(fs, Encoding.UTF8);
                if (bw != null)
                {
                    bw.Write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
                    bw.Write("<tree-map>\n");
                    bw.Write("  <no-comparator/>\n");
                    
                    ServerNodeAttr node=mRoot.FindAttrFromName("id");
                    if(node!=null)
                    {
                        Dictionary<int, string>.KeyCollection keys=node.mDatas.Keys;
                        foreach(int id in keys)
                        {
                            bw.Write("  <entry>\n");
                            bw.Write("    <int>" + id + "</int>\n");
                            bw.Write("    <fire.gsp." + mSpaceName + "." + mBeanName + ">\n");
                            mRoot.WriteToXmlFile(bw, id,false);
                            bw.Write("    </fire.gsp." + mSpaceName + "." + mBeanName + ">\n");
                            bw.Write("  </entry>\n");
                        }
                    }
                    bw.Write("</tree-map>");
                    //bw.Flush();
                    bw.Close();
                }
                //fs.Flush();
                fs.Close();
            }
        }
    }
}
