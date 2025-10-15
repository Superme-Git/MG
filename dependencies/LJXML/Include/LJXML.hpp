#ifndef LJXML_H_INCLUDED
#define LJXML_H_INCLUDED
#include "../../RapidXml/Include/rapidxml.hpp"
#include "../../RapidXml/Include/rapidxml_utils.hpp"
#include "../../RapidXml/Include/rapidxml_iterators.hpp"
#include "../../RapidXml/Include/rapidxml_print.hpp"
#include "LJXMLStringHelper.hpp"
#include <list>
#include "../../../common/platform/platform/platform_types.h"

namespace LJXML
{
	typedef wchar_t Char;

	template<class Ch> class LJXML_Attr;
	template<class Ch> class LJXML_Node;
	template<class Ch> class LJXML_Doc;

	typedef std::vector<std::wstring> LJXML_AttrNameList;

	typedef std::vector< LJXML_Attr<Char>* > LJXML_AttrList;

	template<class Ch = Char>
	class LJXML_Attr : public rapidxml::xml_attribute < Ch >
	{
	public:
		int GetSibAttrList(LJXML_AttrList& dstAttrList, bool includeMe = true)
		{
			dstAttrList.clear();
			if (includeMe == true)
			{
				dstAttrList.push_back(this);
			}
			LJXML_Attr<Ch>* sibAttr = (LJXML_Attr<Ch>*)this->next_attribute();
			LJXML_Attr<Ch>* sibAttrOld = NULL;
			while (sibAttr != NULL && sibAttr != sibAttrOld)
			{
				sibAttrOld = sibAttr;
				dstAttrList.push_back(sibAttr);
				sibAttr = (LJXML_Attr<Ch>*)sibAttr->next_attribute();
			}
			int Result = dstAttrList.size();
			return Result;
		}
	};

	typedef std::vector< LJXML_Node<Char>* > LJXML_NodeList;

	template<class Ch = Char>
	class LJXML_Node : public rapidxml::xml_node < Ch >
	{
	public:
		rapidxml::node_type GetType() const
		{
			return this->type();
		}

		std::wstring GetName() const
		{
			std::wstring ret = this->name();
			return ret;
		}

		std::wstring GetValue()
		{
			if (this->value_size() > 0)
			{
				std::wstring Result;
				Result = this->value();
				return Result;
			}
			else
			{
				return L"";
			}
		}

		int GetSibNodeCount()
		{
			LJXML_NodeList dstNodeList;
			int Result = GetSibNodeList(dstNodeList);
			return Result;
		}
		int GetSibNodeList(LJXML_NodeList& dstNodeList, bool includeMe = true)
		{
			dstNodeList.clear();
			if (includeMe == true)
			{
				dstNodeList.push_back(this);
			}
			LJXML_Node<Ch>* sibNode = (LJXML_Node<Ch>*)this->next_sibling();
			LJXML_Node<Ch>* sibNodeOld = NULL;
			while (sibNode != NULL && sibNode != sibNodeOld)
			{
				sibNodeOld = sibNode;
				if (sibNode->type() == rapidxml::node_element)
				{
					dstNodeList.push_back(sibNode);
				}
				sibNode = (LJXML_Node<Ch>*)sibNode->next_sibling();
			}
			int Result = dstNodeList.size();
			return Result;
		}
		int GetSubNodeCount()
		{
			LJXML_NodeList dstNodeList;
			int Result = GetSubNodeList(dstNodeList);
			return Result;
		}
		int GetSubNodeList(LJXML_NodeList& dstNodeList)
		{
			dstNodeList.clear();
			LJXML_Node<Ch>* sibNode = (LJXML_Node<Ch>*)this->next_sibling();
			LJXML_Node<Ch>* subNode = (LJXML_Node<Ch>*)this->first_node();
			LJXML_Node<Ch>* subNodeOld = NULL;
			while (subNode != NULL && subNode != sibNode && subNode != subNodeOld)
			{
				subNodeOld = subNode;
				if (subNode->type() == rapidxml::node_element)//这里可能会对以后替换xmlio中的GetSubText函数有影响
				{
					dstNodeList.push_back(subNode);
				}
				subNode = (LJXML_Node<Ch>*)subNode->next_sibling();
			}
			int Result = dstNodeList.size();
			return Result;
		}

		int GetChildren(LJXML_NodeList& dstNodeList)
		{
			return GetSubNodeList(dstNodeList);
		}

		int GetSubAttrCount()
		{
			LJXML_AttrList dstAttrList;
			int Result = GetSubAttrList(dstAttrList);
			return Result;
		}
		int GetSubAttrList(LJXML_AttrList& dstAttrList)
		{
			dstAttrList.clear();
			LJXML_Attr<Ch>* SubAttr = (LJXML_Attr<Ch>*)this->first_attribute();
			if (SubAttr == NULL)
			{
				return 0;
			}
			SubAttr->GetSibAttrList(dstAttrList);
			int Result = dstAttrList.size();
			return Result;
		}
		int GetSubAttrNameList(LJXML_AttrNameList& dstAttrNameList)
		{
			dstAttrNameList.clear();
			LJXML_AttrList dstAttrList;
			int attrCount = GetSubAttrList(dstAttrList);
			for (int i = 0; i <attrCount; i++)
			{
				dstAttrNameList.push_back(dstAttrList[i]->name());
			}
			dstAttrList.clear();
			int Result = dstAttrNameList.size();
			return Result;
		}

		bool GetAttribute(const std::wstring& attrName, std::wstring& attrValue)
		{
			LJXML_Attr<Ch>* pAttr = (LJXML_Attr<Ch>*)this->first_attribute(attrName.c_str());
			if (pAttr)
			{
				attrValue = pAttr->value();
			}
			return (pAttr != NULL);
		}

		int GetAttributeInteger(const std::wstring& attrName, int defValue = 0)
		{
			int ret = defValue;
			rapidxml::xml_attribute<Ch>* pAttr = this->first_attribute(attrName.c_str());
			if (pAttr && pAttr->value_size() > 0)
			{
				 ret = _wtoi(pAttr->value());
			}
			return ret;
		}

		unsigned int GetAttributeUnsignedInteger(const std::wstring& attrName, unsigned int defValue = 0)
		{
			unsigned int ret = defValue;
			rapidxml::xml_attribute<Ch>* pAttr = this->first_attribute(attrName.c_str());
			if (pAttr && pAttr->value_size() > 0)
			{
				ret = _wtou(pAttr->value());
			}
			return ret;
		}

		int GetIntAttribute(const std::wstring& attrName, int defValue = 0)
		{
			return GetAttributeInteger(attrName, defValue);
		}
	};
	template<class Ch = Char>
	class LJXML_Doc : public rapidxml::xml_document < Ch >
	{
	public:
		typedef std::basic_fstream<Ch, std::char_traits<Ch> > fstream_type;
		typedef std::basic_string<Ch, std::char_traits<Ch>, std::allocator<Ch> > string_type;
	public:
		LJXML_Doc() : mCharData(NULL) {}
		~LJXML_Doc() {	Release(); }

		int LoadFromChar(Ch* charData)
		{
			this->parse0(charData);
			return 0;
		}
		Ch* LoadFromString(const string_type& strData)
		{
			Release();
			size_t ss = strData.size();
			mCharData = new Ch[ss + 1];
			memcpy(mCharData, &strData[0], ss*sizeof(Ch));
			mCharData[ss] = 0xffff;
			LoadFromChar(mCharData);
			return mCharData;
		}
		Ch* LoadFromStream(fstream_type& fs)
		{
			Release();
			std::streamsize ss = 0;
			fs.seekg(0, std::ios_base::end);
			ss = fs.tellg();
			fs.seekg(0, std::ios_base::beg);
			mCharData = new Ch[ss + 1];
			fs.read(&mCharData[0], ss);
			mCharData[ss] = 0xffff;
			LoadFromChar(mCharData);
			return mCharData;
		}
// 		bool LoadFromFile(const string_type& fileName)
// 		{
// 			fstream_type fs;
// 			fs.open(fileName, std::ios_base::binary | std::ios_base::in);
// 			bool b = false;
// 			if (fs.is_open())
// 			{
// 				b = LoadFromStream(fs) != NULL;
// 				fs.close();
// 			}
// 			return b;
// 		}
		bool LoadFromFile(const std::string& fileName)
		{
			fstream_type fs;
			fs.open(fileName.c_str(), std::ios_base::binary | std::ios_base::in);
			bool b = false;
			if (fs.is_open())
			{
				b = LoadFromStream(fs) != NULL;
				fs.close();
			}
			return b;
		}

		bool LoadFromBuffer(const void* pBuffer, size_t size)
		{
			std::wstring wstr;
			LJXMLStringHelper::EncodeLJ codeType;
			if (LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)pBuffer, size, wstr, codeType))
			{
				return LoadFromString(wstr) != NULL;
			}
			return false;
		}

		LJXML::LJXML_Node<Ch>* GetRootNode()
		{
			return (LJXML::LJXML_Node<Ch>*)this->first_node();
		}

		int SaveToStream(fstream_type& fs)
		{
			fs << *this;
			return 0;
		}
// 		int SaveToFile(const string_type& fileName)
// 		{
// 			fstream_type fs;
// 			fs.open(fileName, std::ios_base::out);
// 			if (fs.is_open())
// 			{
// 				SaveToStream(fs);
// 				fs.close();
// 			}
// 			return 0;
// 		}
		int SaveToFile(const std::string& fileName)
		{
#ifdef ANDROID
			//string_type text;
			//rapidxml::print(std::back_inserter(text), *this);
			//FILE* fp = fopen(fileName.c_str(), "w+");
			//if (fp)
			//{
			//	fwrite(text.c_str(), sizeof(Ch), text.length(), fp);
			//	fclose(fp);
			//}
			//return 0;
			std::string text;
			rapidxml::print(std::back_inserter(text), *this);
			FILE* fp = fopen(fileName.c_str(), "w+");
			if (fp)
			{
				fwrite(text.c_str(), sizeof(char), strlen(text.c_str()), fp);
				fclose(fp);
			}
			return 0;
#else
			fstream_type fs;
			fs.open(fileName.c_str(), std::ios_base::binary | std::ios_base::out);
			if (fs.is_open())
			{
				SaveToStream(fs);
				fs.close();
			}
			return 0;
#endif
		}
		bool Release()
		{
			if (mCharData)
			{
				delete[] mCharData;
				mCharData = NULL;
				return true;
			}
			return false;
		}

	protected:

		Ch* mCharData;
	};

	template<class Ch = Char>
	class LJXML_Tool
	{
	public:
		typedef std::basic_string<Ch, std::char_traits<Ch>, std::allocator<Ch> > string_type;
		typedef std::list<string_type> list_type;
		typedef typename list_type::iterator list_iterator_type;
	public:

		static rapidxml::xml_node<Ch>* CreateElement(
			rapidxml::xml_node<Ch>* pParent,
			const string_type& strElementName,
			const string_type& strElementContent = string_type(),
			const list_type& vecAttributeString = list_type()
			)
		{
			return CreateElement(pParent, strElementName, strElementContent,
				const_cast<list_type&>(vecAttributeString).begin(), const_cast<list_type&>(vecAttributeString).end());
		}

		static rapidxml::xml_node<Ch>* CreateElement(
			rapidxml::xml_node<Ch>* pParent,
			const string_type& strElementName,
			const string_type& strElementContent,
			list_iterator_type itBegin,
			list_iterator_type itEnd
			)
		{
			if (pParent == NULL)
				return NULL;

			auto pDoc = pParent->document();
			if (pDoc == NULL)
				return NULL;

			auto pElement = pDoc->allocate_node(rapidxml::node_element);
			if (pElement == NULL)
				return NULL;

			pElement->name(strElementName.c_str(), strElementName.length());
			pElement->value(strElementContent.c_str(), strElementContent.length());

			for (auto it = itBegin; it != itEnd;)
			{
				list_iterator_type itName = it++;
				if (it == itEnd)
					break;
				list_iterator_type itValue = it++;

				auto pAttribute = pDoc->allocate_attribute();
				if (pAttribute)
				{
					pAttribute->name(itName->c_str(), itName->length());
					pAttribute->value(itValue->c_str(), itValue->length());
					pElement->append_attribute(pAttribute);
				}
			}

			pParent->append_node(pElement);
			return pElement;
		}
	};
}
#endif