using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace LanguageConverters
{
    //子列定义
    public class BeanSubCol
    {
        public string name;
    }
    //列定义
    public class BeanCol
    {
        public string name;
        public string fromCol;
        public string type;
        public string value;
        public string minLimit;
        public string maxLimit;
        public List<BeanSubCol> subCols;
        public string data;
        
    }
    //表定义
    public class BeanDef
    {
        public string name;
        public string from;
        public string genXml;
        public bool makeLua;
        public string baseClass;
        public string priority;
        public string spaceName;
        public List<BeanCol> cols;
        public bool GetCol(out BeanCol beanColEx,string colName)
        {
            for (int i = 0; i < cols.Count();i++ )
            {
                if(cols[i].name==colName)
                {
                    beanColEx = cols[i];
                    return true;
                }
            }
            beanColEx = null;
            return false;
        }
        public bool GetIdColIndex(out string idTitleName)
        {
            for(int i=0;i<cols.Count;i++)
            {
                if (cols[i].name == "id")
                {
                    idTitleName = cols[i].fromCol;
                    return true;
                }
            }
            idTitleName = "";
            return false;
        }
        public uint GetColCheckNumber()
        {
            uint colVersion = 0;
            uint allColCount = 0;
            for (int i = 0; i < cols.Count; i++)
            {
                for (int j = 0; j < cols[i].subCols.Count; j++)
                {
                    string type = cols[i].type;
                    if (cols[i].type == "vector")
                        type = cols[i].value;

                    if (type == "bool")
                        colVersion += 1;
                    else if (type == "int")
                        colVersion += 2;
                    else if (cols[i].type == "long")
                        colVersion += 3;
                    else if (cols[i].type == "double")
                        colVersion += 4;
                    else if (cols[i].type == "string" || cols[i].type == "String")
                        colVersion += 5;
                    colVersion += allColCount;
                    ++allColCount;
                }
            }
            colVersion += allColCount * 0x10000;

            return colVersion;
        }
        public bool isHaveNameValue()
        {
            BeanCol beanCol;
            if (GetCol(out beanCol, "name"))
                return beanCol.type == "string" || beanCol.type == "String";
            return false;
        }
        public bool isHaveColourValue()
        {
            BeanCol beanCol;
            if (GetCol(out beanCol, "colour"))
                return beanCol.type == "string" || beanCol.type == "String";
            return false;
        }
        public bool isHaveMapidValue()
        {
            BeanCol beanCol;
            if (GetCol(out beanCol, "mapid"))
                return beanCol.type == "string" || beanCol.type == "String";
            return false;
        }
    }

    public class DefineManager
    {
        public List<BeanDef> mCommonDef = new List<BeanDef>();
        public List<BeanDef> mServerDef=new List<BeanDef>();
        public List<BeanDef> mClientDef=new List<BeanDef>();
        public Dictionary<string, BeanDef> mBeanNameIndex=new Dictionary<string,BeanDef>();
        public Dictionary<string, List<BeanDef>> mFileNameIndex=new Dictionary<string,List<BeanDef>>();
        public void init()
        {
        }
        public void Clear()
        {
            mCommonDef.Clear();
            mServerDef.Clear();
            mClientDef.Clear();
            mBeanNameIndex.Clear();
            mFileNameIndex.Clear();
        }
        private int getColCount(XmlNodeList xnlEx)
        {
            int colCount = 0;
            for (int i = 0; i < xnlEx.Count; i++)
            {
                Type nodeType = xnlEx[i].GetType();
                if(nodeType.Name=="XlmElement")
                {
                    XmlElement xeEx = (XmlElement)xnlEx[i];
                    colCount++;
                }
            }
            return colCount;
        }
        public bool loadDefFromFile(string xmlDefFileName)
        {
            XmlDocument doc = new XmlDocument();
            doc.Load(xmlDefFileName);
            XmlNode xn = doc.SelectSingleNode("namespace");
            string spaceName = ((XmlElement)xn).GetAttribute("name");
            if(spaceName=="fire"&&xn.ChildNodes.Count>0)
            {
                xn = xn.ChildNodes[0];
                spaceName = ((XmlElement)xn).GetAttribute("name");
                if(spaceName=="pb"&&xn.ChildNodes.Count>0)
                {
                    xn = xn.ChildNodes[0];
                    spaceName = ((XmlElement)xn).GetAttribute("name");
                }
            }

            XmlNodeList xnl = xn.ChildNodes;
            foreach (XmlNode childXn in xnl)
            {
                //取属性.
                Type nodeType = childXn.GetType();
                if(nodeType.Name=="XmlElement")
                { 
                    XmlElement xe = (XmlElement)childXn;
                    BeanDef beanDef = new BeanDef();
                    beanDef.name = xe.GetAttribute("name").ToString();
                    beanDef.from = xe.GetAttribute("from").ToString();
                    beanDef.genXml = xe.GetAttribute("genxml").ToString();
                    beanDef.makeLua = false;
                    if (xe.GetAttribute("gencode").ToString() == "mtlua")
                        beanDef.makeLua = true;
                    beanDef.baseClass = xe.GetAttribute("baseclass").ToString();
                    beanDef.priority = xe.GetAttribute("priority").ToString();
                    beanDef.spaceName = spaceName;

                    int colCount = getColCount(childXn.ChildNodes);
                    beanDef.cols = new List<BeanCol>();
                    for (int i = 0; i < childXn.ChildNodes.Count; i++)
                    {
                        nodeType = childXn.ChildNodes[i].GetType();
                        if (nodeType.Name == "XmlElement")
                        {
                            BeanCol beanCol = new BeanCol();
                            XmlElement xeEx = (XmlElement)childXn.ChildNodes[i];
                            beanCol.name = xeEx.GetAttribute("name").ToString();
                            beanCol.fromCol = xeEx.GetAttribute("fromCol").ToString();
                            beanCol.type = xeEx.GetAttribute("type").ToString();
                            beanCol.value = xeEx.GetAttribute("value").ToString();
                            beanCol.minLimit = xeEx.GetAttribute("min");
                            beanCol.maxLimit = xeEx.GetAttribute("max");
                            if (xeEx.NextSibling != null && xeEx.NextSibling.Value != null)
                            {
                                beanCol.data = "";
                                bool recordBegin = false;
                                for (int j = 0; j < xeEx.NextSibling.Value.Length;j++ )
                                {
                                    if (recordBegin == false && xeEx.NextSibling.Value[j] == ' ')
                                    {
                                        continue;
                                    }
                                    else
                                        recordBegin = true;
                                    if (xeEx.NextSibling.Value[j] == '<')
                                        break;
                                    if (xeEx.NextSibling.Value[j] == '\n')
                                        break;
                                    if (xeEx.NextSibling.Value[j] == '\r')
                                        break;
                                    beanCol.data += xeEx.NextSibling.Value[j];
                                }
                            }
                            else
                                beanCol.data = "";

                            if (beanCol.fromCol.Length > 0)
                            {
                                List<string> subCols = v.stovs(beanCol.fromCol, ',');
                                beanCol.subCols = new List<BeanSubCol>();
                                for (int j = 0; j < subCols.Count; j++)
                                {
                                    BeanSubCol subCol = new BeanSubCol();
                                    subCol.name = subCols[j];
                                    beanCol.subCols.Add(subCol);
                                }
                            }
                            beanDef.cols.Add(beanCol);
                        }
                    }

                    if (beanDef.genXml == "server")
                    {
                        mServerDef.Add(beanDef);
                    }
                    else if (beanDef.genXml == "client")
                    {
                        if (beanDef.baseClass.Length == 0)
                        {
                            mClientDef.Add(beanDef);
                        }
                        else
                        {
                            v.Error("在解析BEAN<" + beanDef.baseClass + ">时发现base class=" + beanDef.baseClass + ",目前不支持客户端BEAN定义基类！");
                            return false;
                        }
                    }
                    else if (beanDef.genXml == "")
                    {
                        mCommonDef.Add(beanDef);
                    }
                    else
                    {
                        v.Error("BEAN格式错误 " + beanDef.name + "的genXml " + beanDef.genXml + " 不支持！");
                        return false;
                    }
                    try
                    {
                        if (beanDef.name != "")
                            mBeanNameIndex.Add(beanDef.name, beanDef);
                        else
                            v.Warning("main.xml的非标准格式.");
                    }
                    catch(ArgumentException e)
                    {
                        v.Error("已经读入名字为" + beanDef.name + "的Bean,请检查相关定义 异常信息=" + e.Message);
                        return false;
                    }

                    List<string> xlsFileNames = v.stovs(beanDef.from, ',');
                    for (int i = 0; i < xlsFileNames.Count; i++)
                    {
                        string xlsFileName = v.ConverPath(xlsFileNames[i]);
                        List<BeanDef> beanDefs;
                        if (!mFileNameIndex.TryGetValue(xlsFileName, out beanDefs))
                        {
                            beanDefs = new List<BeanDef>();
                            mFileNameIndex.Add(xlsFileName, beanDefs);
                        }
                        beanDefs.Add(beanDef);
                    }
                }
            }

            return true;
        }
        public bool GetBeanFromBeanName(out BeanDef beanDef,string type)
        {
            if(mBeanNameIndex.TryGetValue(type, out beanDef))
            {
                return true;
            }
            List<string> vsType = v.stovs(type,'.');
            if (vsType.Count > 1)
            {
                string lastType = vsType[vsType.Count - 1];
                return mBeanNameIndex.TryGetValue(lastType, out beanDef);
            }
            return false;
        }
        public bool GetBeanFromFileName(out List<BeanDef> beanDefs,string pathFileName)
        {
            return mFileNameIndex.TryGetValue(pathFileName, out beanDefs);
        }
    }
}
