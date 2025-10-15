using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ExcelParse2
{
    class ServerBeanNode
    {
        public enum SBNStyle
        {
            Error,
            BaseAttrib,
            BeanAttrib,
            BaseArray,
            BeanArray,
        };
        public ServerBeanNode mParentNode;
        public ServerBeanNode mBaseClassNode;
        public List<ServerBeanNode> mNodes = new List<ServerBeanNode>();
        private SBNStyle mStyle=SBNStyle.Error;
        private string mType="";
        private string mName="";
        private string mValue = "";
        private string mXlsTitle = "";
        private BeanDef mBeanDef;
        public SBNStyle GetNodeStyle() { return mStyle;  }
        public string GetNodeType() { return mType;  }
        public string GetNodeName() { return mName;  }
        public string GetNodeXlsTitle() { return mXlsTitle; }
        public bool GetBeanDef(out BeanDef beanDef)
        {
            if(mBeanDef!=null)
            {
                beanDef=mBeanDef;
                return true;
            }
            else if(mParentNode!=null)
            {
                return mParentNode.GetBeanDef(out beanDef);
            }
            beanDef = null;
            return true;
        }
        public void Clear()
        {
            if(mBaseClassNode!=null)
                mBaseClassNode.Clear();

            for (int i = 0; i < mNodes.Count; i++)
            {
                mNodes[i].Clear();
            }
        }
        public bool SetupDef(string name, string type, string value,string fromCol)
        {
            mStyle = SBNStyle.Error;
            mType = type;
            mName = name;
            mValue = value;
            mXlsTitle = fromCol;

            if (MainWindow.IsBaseType(type))
            {
                mStyle = SBNStyle.BaseAttrib;
            }
            else if (MainWindow.IsBeanType(type))
            {
                mStyle = SBNStyle.BeanAttrib;
                if (MainWindow.GetBeanDef(out mBeanDef, type))
                {
                    if (mBeanDef.baseClass.Length > 0)
                    {
                        mBaseClassNode = new ServerBeanNode();
                        mBaseClassNode.mParentNode = this;
                        if (!mBaseClassNode.SetupDef("baseClass", mBeanDef.baseClass, "", ""))
                        {
                            return false;
                        }
                    }
                    for (int i = 0; i < mBeanDef.cols.Count; i++)
                    {
                        BeanCol subCol = mBeanDef.cols[i];
                        ServerBeanNode node = new ServerBeanNode();
                        node.mParentNode = this;
                        if (!node.SetupDef(subCol.name, subCol.type, subCol.value, subCol.fromCol))
                        {
                            return false;
                        }
                        mNodes.Add(node);
                    }
                }
            }
            else if (MainWindow.IsArrayType(type))
            {
                if (MainWindow.IsBaseType(value))
                {
                    mStyle = SBNStyle.BaseArray;
                    List<string> vsParam = MainWindow.stovs(fromCol, ',');
                    for (int i = 0; i < vsParam.Count; i++)
                    {
                        ServerBeanNode node = new ServerBeanNode();
                        node.mParentNode = this;
                        if (!node.SetupDef("", value, "", vsParam[i]))
                        {
                            return false;
                        }
                        mNodes.Add(node);
                    }
                }
                else if (MainWindow.IsBeanType(value))
                {
                    mStyle = SBNStyle.BeanArray;
                    BeanDef beanDef;
                    if(MainWindow.GetBeanDef(out beanDef, value))
                    {
                        List<string> vsName = MainWindow.stovs(mValue, '.');
                        if (vsName.Count == 1)
                        {
                            mValue = "fire.pb."+beanDef.spaceName+"."+beanDef.name;
                        }
                        int arrayCount = -1;
                        for(int i=0;i<beanDef.cols.Count;i++)
                        {
                            BeanCol beanCol = beanDef.cols[i];
                            ServerBeanNode node = new ServerBeanNode();
                            node.mParentNode = this;
                            if(!node.SetupDef(beanCol.name,mType,beanCol.type,beanCol.fromCol))
                            {
                                return false;
                            }
                            mNodes.Add(node);
                            if(arrayCount==-1)
                                arrayCount = node.mNodes.Count();
                            else if(arrayCount!=node.mNodes.Count())
                            {
                                MainWindow.Error("bean("+name+")定义"+value+"错误！Bean数组对象的列数不一致!");
                                return false;
                            }
                            else if(arrayCount==0)
                            {
                                MainWindow.Error("bean("+name+")定义" + value + "错误！Bean数组对象的列数为0!");
                                return false;
                            }
                        }
                    }
                }
                else
                {
                    MainWindow.Error("无法识别的array bean("+name+") type="+type+" value=" + value + "!");
                    return false;
                }
            }
            else
            {
                MainWindow.Error("无法识别的bean("+name+") type=" + type + "!");
                return false;
            }
            return true;
        }
        public bool FindNodeFromName(out ServerBeanNode node, string sName)
        {
            if (mName == sName)
            {
                node = this;
                return true;
            }

            if(mBaseClassNode!=null)
            {
                if(mBaseClassNode.FindNodeFromName(out node,sName))
                {
                    return true;
                }
            }

            for (int i = 0; i < mNodes.Count; i++)
            {
                if (mNodes[i].FindNodeFromName(out node, sName))
                {
                    return true;
                }
            }

            node = null;
            return false;
        }
        public bool WriteToXmlFile(ref StreamWriter bw, ref XlsData fileData, string spaceName, int id, bool isArray)
        {
            if(mBaseClassNode!=null)
            {
                mBaseClassNode.WriteToXmlFile(ref bw,ref fileData, spaceName, id, isArray);
            }

            for (int i = 0; i < mNodes.Count; i++)
            {
                SBNStyle style = mNodes[i].GetNodeStyle();
                string name = mNodes[i].GetNodeName();

                if(style==SBNStyle.BaseAttrib)
                {
                    string type = mNodes[i].GetNodeType();
                    string title=mNodes[i].GetNodeXlsTitle();
                 
                    if(!fileData.CheckType(title,type))
                    {
                        MainWindow.Error("策划指定的文件"+fileData.GetFileName()+"的列" + title + "类型和GBeans里定义的类型不一致");
                        return false;
                    }
                    
                    string value;
                    if (fileData.GetXlsData(out value, title, id))
                    {
                        if (!MainWindow.IsInvalidValue(type, value))
                        {
                            MainWindow.Error("表" + fileData.GetFileName() + "的列" + title + "的数据" + value + "不符合类型" + type + "的规范！");
                            return false;
                        }
                        if (value.Length > 0 && !MainWindow.IsInvalidName(value))
                        {
                            name = MainWindow.ConverXmlName(name);
                            value = MainWindow.ConverXmlString(value);
                            bw.Write("      <" + name + ">" + value + "</" + name + ">\n");
                        }
                    }
                    else
                    {
                        MainWindow.Error("丛表"+fileData.GetFileName()+"中获取列<"+title+">,ID<"+id+">失败!");
                        return false;
                    }
                }
                else if(style==SBNStyle.BeanAttrib)
                {
                    mNodes[i].WriteToXmlFile(ref bw, ref fileData, spaceName, id, isArray);
                }
                else if(style==SBNStyle.BaseArray)
                {
                    ServerBeanNode arrayNode = mNodes[i];
                    List<string> datas=new List<string>();
                    for(int j=0;j<arrayNode.mNodes.Count;j++)
                    {
                        string title = arrayNode.mNodes[j].GetNodeXlsTitle();
                        string type = arrayNode.mNodes[j].GetNodeType();
                        string value;
                        if(fileData.GetXlsData(out value,title,id))
                        {
                            if (value.Length > 0 && !MainWindow.IsInvalidName(value))
                            {
                                if (type == "String")
                                    type = "string";
                                else if (type == "int")
                                {
                                    if (value.Length > 0)
                                    {
                                        int nPointPos = value.IndexOf(".");
                                        if (nPointPos != -1)
                                        {
                                            value = value.Substring(0, nPointPos);
                                        }
                                    }
                                }
                                value = MainWindow.ConverXmlString(value);
                                datas.Add("        <"+type+">"+value+"</"+type+">\n");
                            }
                        }
                        else
                        {
                            MainWindow.Error("丛表" + fileData.GetFileName() + "中获取列<" + title + ">,ID<" + id + ">失败!");
                            return false;
                        }
                    }
                    if(datas.Count>0)
                    {
                        string type=mNodes[i].GetNodeType();
                        bw.Write("      <" + name + ">\n");
                        for(int j=0;j<datas.Count;j++)
                        {
                            bw.Write(datas[j]);
                        }
                        bw.Write("      </" + name + ">\n");
                    }
                }
                else if(style==SBNStyle.BeanArray)
                {
                    List<string> writeList=new List<string>();
                    if (!mNodes[i].GetWriteList(ref writeList, ref fileData, spaceName, id, false))
                    {
                        return false;
                    }

                    if (writeList.Count > 0)
                    {
                        bw.Write("      <" + name + ">\n");
                        for (int j = 0; j < writeList.Count; j++)
                        {
                            bw.Write(writeList[j]);
                        }
                        bw.Write("      </" + name + ">\n");
                    }
                    else
                    {
                        bw.Write("      <" + name + "/>\n");
                    }
                }
            }
            return true;
        }
        public bool GetWriteList(ref List<string> writeList, ref XlsData fileData, string spaceName, int id, bool isArray)
        {
            if(mStyle!=SBNStyle.BeanArray)
            {
                MainWindow.Error("该功能仅仅针对二进制数组！\n");
                return false;
            }

            int vectorCount = mNodes[0].mNodes.Count;
            int memberCount = mNodes.Count;
            for (int i = 0; i < vectorCount;i++ )
            {
                List<string> curWriteList = new List<string>();
                for(int j=0;j<memberCount;j++)
                {
                    ServerBeanNode arrayNode = mNodes[j];
                    string title = arrayNode.mNodes[i].GetNodeXlsTitle();
                    string name = arrayNode.GetNodeName();
                    string value;
                    if (fileData.GetXlsData(out value, title, id))
                    {
                        if (value.Length > 0 && !MainWindow.IsInvalidName(value))
                        {
                            name = MainWindow.ConverXmlName(name);
                            curWriteList.Add("            <" + name + ">" + value + "</" + name + ">\n");
                        }
                    }
                    else
                    {
                        MainWindow.Error("获取" + fileData.GetFileName() + "的数据ID=" + id + "失败!");
                        return false;
                    }
                }
                if (curWriteList.Count > 0)
                {
                    writeList.Add("          <" + mValue + ">\n");
                    for (int index = 0; index < curWriteList.Count; index++)
                    {
                        writeList.Add(curWriteList[index]);
                    }
                    writeList.Add("          </" + mValue + ">\n");
                }
            }

            return true;
        }
    }
}
