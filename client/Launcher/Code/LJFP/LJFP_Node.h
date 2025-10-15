#ifndef LJFP_Node_H
#define LJFP_Node_H

#include "LJFP_Var.h"
#include "LJFP_StringUtil.h"

typedef std::list<std::wstring> WStrList;

class LJFP_Attr
{
public:
	std::wstring m_Key;
	std::wstring m_Value;
	LJFP_Attr(std::wstring Key, std::wstring Value)
	{
		m_Key = Key;
		m_Value = Value;
	}
};
class LJFP_Node
{
public:
	std::wstring m_Name;
	std::wstring m_Text;
	std::map<std::wstring, LJFP_Attr*> m_AttrMap;
	std::map<std::wstring, LJFP_Node*> m_NodeMap;
	WStrList m_AttrList;
	LJFP_Node(std::wstring Name, std::wstring Text = L"")
	{
		m_Name = Name;
		m_Text = Text;
	}
	virtual ~LJFP_Node()
	{
		{
			std::map<std::wstring, LJFP_Attr*>::iterator it = m_AttrMap.begin();
			while (it != m_AttrMap.end())
			{
				delete it->second;
				it++;
			}
			m_AttrMap.clear();
		}
		{
			std::map<std::wstring, LJFP_Node*>::iterator it = m_NodeMap.begin();
			while (it != m_NodeMap.end())
			{
				delete it->second;
				it++;
			}
			m_NodeMap.clear();
		}
	}
	unsigned int GetAttrCount()
	{
		return m_AttrMap.size();
	}
	LJFP_Attr* AddAttr(std::wstring Key, std::wstring Value)
	{
		LJFP_Attr* pAttr = new LJFP_Attr(Key, Value);
		m_AttrMap[Key] = pAttr;
		return pAttr;
	}
	LJFP_Attr* FindAttr(std::wstring Key)
	{
		std::map<std::wstring, LJFP_Attr*>::iterator it = m_AttrMap.find(Key);
		if (it != m_AttrMap.end())
		{
			return it->second;
		}
		return NULL;
	}
	std::wstring FindAttrValue(std::wstring Key)
	{
		LJFP_Attr* pAttr = FindAttr(Key);
		if (pAttr)
		{
			return pAttr->m_Value;
		}
		return L"";
	}
	LJFP_Attr* FindAndAddAttr(std::wstring Key, std::wstring Value)
	{
		LJFP_Attr* pAttr = FindAttr(Key);
		if (!pAttr)
		{
			return AddAttr(Key, Value);
		}
		return pAttr;
	}
	LJFP_Attr* GetAttr(unsigned int uiIndex)
	{
		if (uiIndex >= GetAttrCount())
		{
			return NULL;
		}
		std::map<std::wstring, LJFP_Attr*>::iterator it = m_AttrMap.begin();
		for (unsigned int i = 0; i < uiIndex; i++)
		{
			it++;
		}
		return it->second;
	}
	unsigned int GetNodeCount()
	{
		return m_NodeMap.size();
	}
	virtual LJFP_Node* AddNode(std::wstring Name, std::wstring Text)
	{
		LJFP_Node* pNode = new LJFP_Node(Name, Text);
		m_NodeMap[Name] = pNode;
		return pNode;
	}
	virtual void AddNode(LJFP_Node* pNode)
	{
		m_NodeMap[pNode->m_Name] = pNode;
	}
	LJFP_Node* FindNode(std::wstring Name)
	{
		std::map<std::wstring, LJFP_Node*>::iterator it = m_NodeMap.find(Name);
		if (it != m_NodeMap.end())
		{
			return it->second;
		}
		return NULL;
	}
	LJFP_Node* FindAndAddNode(std::wstring Name, std::wstring Text = L"")
	{
		LJFP_Node* pNode = FindNode(Name);
		if (!pNode)
		{
			pNode = AddNode(Name, Text);
		}
		return pNode;
	}
	LJFP_Node* GetNode(unsigned int uiIndex)
	{
		if (uiIndex >= GetNodeCount())
		{
			return NULL;
		}
		std::map<std::wstring, LJFP_Node*>::iterator it = m_NodeMap.begin();
		for (unsigned int i = 0; i < uiIndex; i++)
		{
			it++;
		}
		return it->second;
	}
	int BuildAttrList()
	{
		m_AttrList.clear();
		{
			std::map<std::wstring, LJFP_Attr*>::iterator it = m_AttrMap.begin();
			while (it != m_AttrMap.end())
			{
				m_AttrList.push_back(it->first);
				m_AttrList.push_back(it->second->m_Value);
				it++;
			}
		}
		{
			std::map<std::wstring, LJFP_Node*>::iterator it = m_NodeMap.begin();
			while (it != m_NodeMap.end())
			{
				it->second->BuildAttrList();
				it++;
			}
		}
		return 0;
	}
	int ClearAttrList()
	{
		m_AttrList.clear();
		std::map<std::wstring, LJFP_Node*>::iterator it = m_NodeMap.begin();
		while (it != m_NodeMap.end())
		{
			it->second->ClearAttrList();
			it++;
		}
		return 0;
	}
	static int LoadFromStream(std::ifstream& FS, LJFP_Node* pParentNode, LJFP_Node*& pNode)
	{
		unsigned int ui = 0;
		std::wstring wsKey = L"";
		std::wstring wsValue = L"";
		std::string sCur = "";
		FS.read((char*)&ui, 4);//Name
		sCur.resize(ui);
		FS.read((char*)sCur.c_str(), ui);
		wsKey = s2ws(sCur);
		FS.read((char*)&ui, 4);//Text
		sCur.resize(ui);
		FS.read((char*)sCur.c_str(), ui);
		wsValue = s2ws(sCur);
		pNode = new LJFP_Node(wsKey, wsValue);
		{
			FS.read((char*)&ui, 4);//AttrCount
			unsigned int AttrCount = ui;
			for (unsigned int i = 0; i < AttrCount; i++)
			{
				FS.read((char*)&ui, 4);//Key
				sCur.resize(ui);
				FS.read((char*)sCur.c_str(), ui);
				wsKey = s2ws(sCur);
				FS.read((char*)&ui, 4);//Value
				sCur.resize(ui);
				FS.read((char*)sCur.c_str(), ui);
				wsValue = s2ws(sCur);
				pNode->AddAttr(wsKey, wsValue);
			}
		}
		{
			FS.read((char*)&ui, 4);//AttrCount
			for (unsigned int i = 0; i < ui; i++)
			{
				LJFP_Node* pSubNode = NULL;
				LoadFromStream(FS, pNode, pSubNode);
			}
		}
		if (pParentNode)
		{
			pParentNode->AddNode(pNode);
		}
		return 0;
	}
	static int LoadFromFile(std::wstring strFileName, LJFP_Node*& pNode)
	{
		std::ifstream FS(StringUtil::WS2S(strFileName).c_str(), std::ios_base::binary);
		if (FS.is_open() == false)
		{
			return -1;
		}
		FS.seekg(0, std::ios_base::beg);
		return LoadFromStream(FS, NULL, pNode);
	}
	static int SaveToStream(std::ofstream& FS, LJFP_Node* pNode)
	{
		unsigned int ui = 0;
		std::string sCur = "";
		sCur = ws2s(pNode->m_Name);//Name
		ui = sCur.size();
		FS.write((char*)&ui, 4);
		FS.write((char*)sCur.c_str(), ui);
		sCur = ws2s(pNode->m_Text);//Text
		ui = sCur.size();
		FS.write((char*)&ui, 4);
		FS.write((char*)sCur.c_str(), ui);
		{
			ui = pNode->m_AttrMap.size();//AttrCount
			FS.write((char*)&ui, 4);
			std::map<std::wstring, LJFP_Attr*>::iterator it = pNode->m_AttrMap.begin();//Attr
			while (it != pNode->m_AttrMap.end())
			{
				sCur = ws2s(it->first);//Key
				ui = sCur.size();
				FS.write((char*)&ui, 4);
				FS.write((char*)sCur.c_str(), ui);
				sCur = ws2s(it->second->m_Value);//Key
				ui = sCur.size();
				FS.write((char*)&ui, 4);
				FS.write((char*)sCur.c_str(), ui);
				it++;
			}
		}
		{
			ui = pNode->m_NodeMap.size();//NodeCount
			FS.write((char*)&ui, 4);
			std::map<std::wstring, LJFP_Node*>::iterator it = pNode->m_NodeMap.begin();//Node
			while (it != pNode->m_NodeMap.end())
			{
				it->second->SaveToStream(FS, it->second);
				it++;
			}
		}
		return 0;
	}
	static int SaveToFile(std::wstring strFileName, LJFP_Node* pNode)
	{
		if (pNode == NULL)
		{
			return -1;
		}
		std::ofstream FS(StringUtil::WS2S(strFileName).c_str(), std::ios_base::binary);
		if (FS.is_open() == false)
		{
			return -2;
		}
		FS.seekp(0, std::ios_base::beg);
		return SaveToStream(FS, pNode);
	}
};

#endif//LJFP_Node_H