#ifndef LJFP_XML_H
#define LJFP_XML_H

#include "LJFP_Node.h"
#include "../LJXML/LJXML.hpp"//这个一定要在miniz.c之前,不然miniz.c里面定义的内存相关内容会冲了这里面的东西


class LJFP_NodeEx : public LJFP_Node
{
public:
	LJFP_NodeEx(std::wstring Name, std::wstring Text = L"")
		: LJFP_Node(Name, Text)
	{

	}
	virtual ~LJFP_NodeEx()
	{
		m_AttrList.clear();
	}
	virtual LJFP_NodeEx* AddNode(std::wstring Name, std::wstring Text)
	{
		LJFP_NodeEx* pNode = new LJFP_NodeEx(Name, Text);
		m_NodeMap[Name] = pNode;
		return pNode;
	}
	virtual void AddNode(LJFP_Node* pNode)
	{
		LJFP_Node::AddNode(pNode);
	}
	LJFP_NodeEx* FindNode(std::wstring Name)
	{
		return (LJFP_NodeEx*)LJFP_Node::FindNode(Name);
	}
	LJFP_NodeEx* FindAndAddNode(std::wstring Name, std::wstring Text = L"")
	{
		return (LJFP_NodeEx*)LJFP_Node::FindAndAddNode(Name, Text);
	}
	static int LoadFromXMLNode(LJXML::LJXML_Node<LJXML::Char>* pXMLNode, LJFP_NodeEx* pParentNode, LJFP_NodeEx*& pNode)
	{
		pNode = new LJFP_NodeEx(pXMLNode->name(), pXMLNode->value());
		{
			LJXML::LJXML_AttrList AL;
			pXMLNode->GetSubAttrList(AL);
			LJXML::LJXML_AttrList::iterator it = AL.begin();
			while (it != AL.end())
			{
				std::wstring wso = (*it)->value();
				std::string s;
				s.resize(wso.size());
				for (int i = 0; i < wso.size(); i++)
				{
					s[i] = wso[i];
				}
				std::wstring ws = s2ws(s);
				pNode->AddAttr((*it)->name(), ws);
				//pNode->AddAttr((*it)->name(), (*it)->value());
				it++;
			}
		}
		{
			LJXML::LJXML_NodeList NL;
			pXMLNode->GetSubNodeList(NL);
			LJXML::LJXML_NodeList::iterator it = NL.begin();
			LJFP_NodeEx* pSubNode = NULL;
			while (it != NL.end())
			{
				LoadFromXMLNode(*it, pNode, pSubNode);
				it++;
			}
		}
		if (pParentNode)
		{
			pParentNode->AddNode(pNode);
		}
		return 0;
	}
	static int SaveToXMLNode(LJFP_NodeEx* pNode, LJXML::LJXML_Node<LJXML::Char>* pXMLNodeParent)
	{
		int iResult = 0;
		LJXML::LJXML_Node<LJXML::Char>* pXMLNode;
		pXMLNode = (LJXML::LJXML_Node<LJXML::Char>*)LJXML::LJXML_Tool<LJXML::Char>::CreateElement(pXMLNodeParent, pNode->m_Name, pNode->m_Text, pNode->m_AttrList);
		std::map<std::wstring, LJFP_Node*>::iterator it = pNode->m_NodeMap.begin();
		while (it != pNode->m_NodeMap.end())
		{
			iResult = SaveToXMLNode(((LJFP_NodeEx*)it->second), pXMLNode);
			it++;
		}
		return iResult;
	}
	static int LoadFromXMLFile(std::wstring strFileName, LJFP_NodeEx*& pNode)
	{
		LJXML::LJXML_Doc<LJXML::Char> pXMLNode;
		bool bResult = pXMLNode.LoadFromFile(LJXMLStringHelper::ws2s(strFileName));
		if (!bResult)
		{
			return -1;
		}

		int iResult = LoadFromXMLNode((LJXML::LJXML_Node<LJXML::Char>*)pXMLNode.first_node(), NULL, pNode);
		pXMLNode.clear();
		if (iResult != 0)
		{
			return iResult;
		}
		return 0;
	}
	static int SaveToXMLFile(std::wstring strFileName, LJFP_NodeEx* pNode)
	{
		LJXML::LJXML_Doc<LJXML::Char> pXMLNode;
		pNode->BuildAttrList();
		int iResult = SaveToXMLNode(pNode, (LJXML::LJXML_Node<LJXML::Char>*)&pXMLNode);
		if (iResult != 0)
		{
			return iResult;
		}
		pXMLNode.SaveToFile(LJXMLStringHelper::ws2s(strFileName));
		pXMLNode.clear();
		pNode->ClearAttrList();
		return 0;
	}
};

#endif //LJFP_XML_H