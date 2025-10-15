using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace CSTools
{
    class ToolsXmlAttr
    {
        public string mKey;
        public string mValue;
        public ToolsXmlAttr(string Key, string Value)
	    {
		    mKey = Key;
		    mValue = Value;
	    }
    }
    class ToolsXmlNode
    {
	    public string mName;
	    public string mText;
	    public List<ToolsXmlAttr> mAttrMap;
	    public List<ToolsXmlNode> mNodeMap;
        public ToolsXmlNode()
	    {
            mAttrMap = new List<ToolsXmlAttr>();
            mNodeMap = new List<ToolsXmlNode>();
	    }
	    public ToolsXmlNode(string Name, string Text = "") : this()
	    {
		    mName = Name;
		    mText = Text;
	    }
	    public void Free()
	    {
            for (int i = 0; i < mAttrMap.Count; i++)
			{
			    mAttrMap[i] = null;
			}
            mAttrMap.Clear();
            mAttrMap = null;
            for (int i = 0; i < mNodeMap.Count; i++)
			{
			    mNodeMap[i] = null;
			}
            mNodeMap.Clear();
            mNodeMap = null;
	    }
	    public int GetAttrCount()
	    {
		    return mAttrMap.Count();
	    }
	    public ToolsXmlAttr AddAttr(string Key, string Value)
	    {
		    ToolsXmlAttr pAttr = new ToolsXmlAttr(Key, Value);
		    mAttrMap.Add(pAttr);
		    return pAttr;
	    }
	    public ToolsXmlAttr FindAttr(string Key)
	    {
            for (int i = 0; i < mAttrMap.Count; i++)
			{
			    if (mAttrMap[i].mKey == Key)
	            {
                    return mAttrMap[i];		 
	            }
			}
		    return null;
	    }
	    public string FindAttrValue(string Key)
	    {
            for (int i = 0; i < mAttrMap.Count; i++)
			{
			    if (mAttrMap[i].mKey == Key)
	            {
                    return mAttrMap[i].mValue;		 
	            }
			}
		    return "";
	    }
	    public ToolsXmlAttr FindAndAddAttr(string Key, string Value)
	    {
		    ToolsXmlAttr pAttr = FindAttr(Key);
		    if (pAttr == null)
		    {
			    return AddAttr(Key, Value);
		    }
		    return pAttr;
	    }
	    public ToolsXmlAttr GetAttr(int Index)
	    {
		    if (Index < 0 || Index >= GetAttrCount())
		    {
			    return null;
		    }
		    return mAttrMap[Index];
	    }
	    public int GetNodeCount()
	    {
		    return mNodeMap.Count();
	    }
	    public ToolsXmlNode AddNode(string Name, string Text)
	    {
		    ToolsXmlNode pNode = new ToolsXmlNode(Name, Text);
		    mNodeMap.Add(pNode);
		    return pNode;
	    }
	    public void AddNode(ToolsXmlNode pNode)
	    {
		    mNodeMap.Add(pNode);
	    }
        public ToolsXmlNode FindNode(string Name)
	    {
            for (int i = 0; i < mNodeMap.Count; i++)
			{
			    if (mNodeMap[i].mName == Name)
	            {
                    return mNodeMap[i];		 
	            }
			}
		    return null;
	    }
        public ToolsXmlNode FindAndAddNode(string Name, string Text = "")
	    {
		    ToolsXmlNode pNode = FindNode(Name);
		    if (pNode == null)
		    {
			    return AddNode(Name, Text);
		    }
		    return pNode;
	    }
        public ToolsXmlNode GetNode(int Index)
	    {
            if (Index < 0 || Index >= GetNodeCount())
            {
                return null;
            }
            return mNodeMap[Index];
	    }
    }
    
    class ToolsXmlDoc : ToolsXmlNode
    {
        public ToolsXmlDoc()
        {
        }
        public void LoadFromFile(string FileNameFull)
        {
            XmlDocument Doc = new XmlDocument();
            Doc.Load(FileNameFull);
            XmlNodeList NL = Doc.ChildNodes;
            for (int i = 0; i < NL.Count; i++)
            {
                XmlNodeToNode(NL[i], this);
            }
            Doc = null;
        }
        static public ToolsXmlNode XmlNodeToNode(XmlNode NodeSrc, ToolsXmlNode NodeDstParent)
        {
            ToolsXmlNode NodeDst = new ToolsXmlNode();
            NodeDst.mName = NodeSrc.Name;
            NodeDst.mText = NodeSrc.Value;
            NodeDstParent.AddNode(NodeDst);
            if (NodeSrc.Attributes != null)
            {
                for (int i = 0; i < NodeSrc.Attributes.Count; i++)
                {
                    NodeDst.AddAttr(NodeSrc.Attributes[i].Name, NodeSrc.Attributes[i].Value);
                }  
            }
            XmlNodeList NL = NodeSrc.ChildNodes;
            for (int i = 0; i < NL.Count; i++)
            {
                XmlNodeToNode(NL[i], NodeDst);
            }
            return NodeDst;
        }
    }
}
