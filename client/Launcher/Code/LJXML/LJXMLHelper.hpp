#ifndef LJXMLHELPER_H_INCLUDED
#define LJXMLHELPER_H_INCLUDED
#include "LJXML.hpp"
namespace LJXMLHelper
{
	template<class Ch = char>
	inline void OutPutAllNode(LJXML::LJXML_Doc<Ch>* XMLDoc)
	{
		LJXML::LJXML_NodeList SibNodeList;
		LJXML::LJXML_Node<Ch>* FirstNode = (LJXML::LJXML_Node<Ch>*)XMLDoc->first_node();
		if (FirstNode == NULL)
		{
			return;
		}
		FirstNode->GetSibNodeList(SibNodeList);
		OutPutSibNode(SibNodeList, 0);
	}
	inline std::string GetLayerSpaceStr(int Layer)
	{
		std::string LayerStr = "";
		for (int i = 0; i < Layer; i++)
		{
			LayerStr += "  ";
		}
		return LayerStr;
	}
	template<class Ch = char>
	inline void OutPutAttr(LJXML::LJXML_Attr<Ch>* SrcAttr, int Layer)
	{
		if (SrcAttr == NULL)
		{
			std::wcout << L"Error\n";
			return;
		}
		std::string LayerSpaceStr = GetLayerSpaceStr(Layer);
		std::wstring AttrName = SrcAttr->name();
		std::wstring AttrValue = SrcAttr->value();
		std::wstring AttrNameWS = AttrName;//LJXMLStringHelper::s2ws(AttrName);
		std::wstring AttrValueWS;

//		LJXMLStringHelper::EncodeLJ encode = LJXMLStringHelper::EncodeLJ::LJ_ANSI;

// 		bool multiBytes = false;
// 		encode = LJXMLStringHelper::GetBufferCodeType((unsigned char*)AttrValue.c_str(), AttrValue.size(), multiBytes);
// 		if (encode == LJXMLStringHelper::EncodeLJ::LJ_UTF_8 || encode == LJXMLStringHelper::EncodeLJ::LJ_UTF_8_NO_MARK || (encode == LJXMLStringHelper::EncodeLJ::LJ_ANSI && !multiBytes))
// 		{
// 			AttrValueWS = LJXMLStringHelper::U8ToU16(AttrValue);
// 		}
// 		else if (encode == LJXMLStringHelper::EncodeLJ::LJ_ANSI && multiBytes)
// 		{
// 			AttrValueWS = LJXMLStringHelper::s2ws(AttrValue);
// 		}
		std::wcout << LayerSpaceStr.c_str() << AttrNameWS.c_str() << L"=" << AttrValueWS.c_str() << L"\n";
	}
	template<class Ch = char>
	inline void OutPutNode(LJXML::LJXML_Node<Ch>* SrcNode, int Layer)
	{
		if (SrcNode == NULL)
		{
			std::wcout << L"Error\n";
			return;
		}
		std::string LayerSpaceStr = GetLayerSpaceStr(Layer);
//		std::string NodeName = SrcNode->name();
//		std::wstring NodeNameWS = LJXMLStringHelper::U8ToU16(NodeName);
//		std::wcout << LayerSpaceStr.c_str() << NodeNameWS.c_str() << L"\n";
		if (SrcNode->value_size() > 0)
		{
			std::wcout << GetLayerSpaceStr(Layer + 1).c_str() << SrcNode->value() << L"\n";
		}
		LJXML::LJXML_AttrList SubAttrList;
		SrcNode->GetSubAttrList(SubAttrList);
		for (size_t i = 0; i < SubAttrList.size(); i++)
		{
			OutPutAttr(SubAttrList[i], Layer + 1);
		}
	}
	inline void OutPutSubNode(LJXML::LJXML_NodeList& SrcNodeList, int Layer)
	{
		for (size_t i = 0; i < SrcNodeList.size(); i++)
		{
			OutPutNode(SrcNodeList[i], Layer);
			LJXML::LJXML_NodeList SubNodeList;
			SrcNodeList[i]->GetSubNodeList(SubNodeList);
			OutPutSubNode(SubNodeList, Layer + 1);
		}
	}
	inline void OutPutSibNode(LJXML::LJXML_NodeList& SrcNodeList, int Layer)
	{
		for (size_t i = 0; i < SrcNodeList.size(); i++)
		{
			OutPutNode(SrcNodeList[i], Layer);
			LJXML::LJXML_NodeList SubNodeList;
			SrcNodeList[i]->GetSubNodeList(SubNodeList);
			OutPutSubNode(SubNodeList, Layer + 1);
		}
	}
}
#endif