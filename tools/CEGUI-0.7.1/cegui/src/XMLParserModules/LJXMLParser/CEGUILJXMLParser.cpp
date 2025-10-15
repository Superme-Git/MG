#include "CEGUILJXMLParser.h"
#include "CEGUILJXMLParserHelper.h"
#include "CEGUIResourceProvider.h"
#include "CEGUISystem.h"
#include "CEGUIXMLHandler.h"
#include "CEGUIXMLAttributes.h"
#include "CEGUILogger.h"
#include "CEGUIExceptions.h"
#include "CEGUIPfsResourceProvider.h"
#include "nuclear.h"
#include "../../../../common/ljfm/code/include/common.h"
#include "../../../../common/ljfm/code/include/ljfmbase.h"
#include "../../../../dependencies/LJXML/Include/LJXML.hpp"
namespace CEGUI
{
	enum
	{
		EC_SUCCESS = 0,
		EC_CREATE_XML_DOCUMENT = -3000,
		EC_PARSE_XML_CONTEXT,
		EC_FILE_SIZE_LACK,
		EC_GET_ROOT_ELEMENT,
		EC_XML_ENCODE,
		EC_XML_DECODE,
		EC_BAD_OCTETS_XML_DATA, // 无效的OCTETS XML数据
		EC_X2O_TRANSLATE_FAILED, // X2O变换失败
		EC_O2X_TRANSLATE_FAILED, // O2X变换失败
		EC_NOT_SUPPORT_WRITER,	// 不支持写操作
	};

	void ProcessNode(XMLHandler& handler, LJXML::LJXML_Node<LJXML::Char>* XMLNode, LJXMLStringHelper::EncodeLJ CodeType)
	{
		XMLAttributes attrs;
		LJXML::LJXML_AttrList SubAttrList;
		XMLNode->GetSubAttrList(SubAttrList);
		for (size_t i = 0; i < SubAttrList.size(); i++)
		{
			std::wstring StrAttrName = SubAttrList[i]->name();
			std::wstring StrAttrValue = SubAttrList[i]->value();
			String AttrName(StrAttrName);//LJXMLHelper::U8ToU16(StrAttrName)//属性名称应该不会是中文的所以可以不转以节省时间

			String AttrValue(StrAttrValue);
			attrs.add(AttrName, AttrValue);
		}
		std::wstring strNodeName = XMLNode->name();
		String NodeName = strNodeName;
		(&handler)->elementStart(NodeName, attrs);//start element
		LJXML::LJXML_NodeList SubNodeList;
		XMLNode->GetSubNodeList(SubNodeList);
		for (size_t i = 0; i < SubNodeList.size(); i++)
		{
			ProcessNode(handler, SubNodeList[i], CodeType);
		}
		//(&handler)->elementEnd((utf8*)(NodeName.c_str()));// end element
		(&handler)->elementEnd(NodeName);// end element
	}
	void ProcessDoc(XMLHandler& handler, LJXML::LJXML_Doc<LJXML::Char>* XMLDoc, LJXMLStringHelper::EncodeLJ CodeType)
	{
		LJXML::LJXML_NodeList SibNodeList;
		LJXML::LJXML_Node<LJXML::Char>* FirstNode = (LJXML::LJXML_Node<LJXML::Char>*)XMLDoc->first_node();
		if (FirstNode == NULL)
		{
			return;
		}
		FirstNode->GetSibNodeList(SibNodeList);
		for (size_t i = 0; i < SibNodeList.size(); i++)
		{
			ProcessNode(handler, SibNodeList[i], CodeType);
		}
	}

	int OpenFromFile(std::wstring fileName, char*& fileBuf, std::streamsize& ss)
	{
		int nError = 0;

		// 同步读取
		LJFMX::CSyncFile pfsFile;
		if (pfsFile.Open(fileName))
		//if (pfsFile.Open(fileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
		{
			unsigned int prefix = 0;
			std::size_t readsize = sizeof(unsigned int);
			readsize = pfsFile.Read(&prefix, readsize);
			if (readsize == LJFM::FILE_SIZE_ERROR)
			{
				return LJFM::LJFMOpen::GetLastError();
			}
			if (sizeof(unsigned int) != readsize)
			{
				return -1;
			}
			pfsFile.Seek(0, LJFM::FSM_SET);

			ss = pfsFile.GetSize() + 2;
			fileBuf = new char[ss + 1];
			if (pfsFile.ReadAll(fileBuf, ss))
			{
				fileBuf[ss - 2] = 0;
				fileBuf[ss - 1] = 0;
				fileBuf[ss] = 0;
				return 0;
			}

			delete[] fileBuf;
			return 1;
		}
		else
		{
			nError = LJFM::LJFMOpen::GetLastError();
		}
		return nError;
	}

	LJXMLParser::LJXMLParser(void)
	{
		d_identifierString = "CEGUI::LJXMLParser - RapidXml  based parser module for CEGUI";// set ID string
	}
	LJXMLParser::~LJXMLParser(void)
	{
	}
	void LJXMLParser::parseXMLFile(XMLHandler& handler, const String& filename, const String& schemaName, const String& resourceGroup)
	{
		char* fileBuf = NULL;

        try
		{
			CEGUI::PFSResourceProvider* rp = static_cast<CEGUI::PFSResourceProvider*>(System::getSingleton().getResourceProvider());
			std::wstring pfsfilename = rp->GetPFSFileName(filename, resourceGroup);
			std::streamsize ss = 0;
			if (OpenFromFile(pfsfilename, fileBuf, ss) != EC_SUCCESS)
			{
				CEGUI_LOGERR("LJXMLParser: an error occurred while opening the XML document '" + filename + "' - check it for potential errors!.\n");
				throw FileIOException("LJXMLParser: an error occurred while opening the XML document '" + filename + "' - check it for potential errors!.");
			}

			parseXMLFileBuf(handler, fileBuf, ss);
		}
		catch(...)
		{	
        }

		if (fileBuf)
		{
			delete[] fileBuf;
			fileBuf = NULL;
		}
	}

	void LJXMLParser::parseXMLFileBuf(XMLHandler& handler, char* fileBuf, std::streamsize ss)
	{
		try
		{
			std::wstring wStr;
			LJXMLStringHelper::EncodeLJ codeType;
			LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, wStr, codeType);

			codeType = LJXMLStringHelper::LJ_ANSI;
			LJXML::LJXML_Doc<LJXML::Char> doc;
			LJXML::Char* charData = doc.LoadFromString(wStr);
			if (!doc.first_node())
			{
				//throw FileIOException("LJXMLParser: an error occurred while parsing XML file buf. check it for potential errors!.");
				CEGUI_LOGERR("LJXMLParser: an error occurred while parsing XML file buf. check it for potential errors!.\n");
				return;
			}
			ProcessDoc(handler, &doc, codeType);
		}
		catch (...)
		{
			throw;
		}
	}

	void LJXMLParser::parseXMLContent(XMLHandler& handler,const String& content)
	{
		LJXML::LJXML_Doc<LJXML::Char> doc;

		try
		{
			LJXML::Char* charData = doc.LoadFromString(LJXMLParserHelper::GUIStringToWString(content));
		}
		catch(...)
		{
			return;
		}

		try
		{
			ProcessDoc(handler, &doc, LJXMLStringHelper::LJ_ANSI);
		}
		catch (...)
		{
		}
	}
	bool LJXMLParser::initialiseImpl(void)
	{
		return true;
	}
	void LJXMLParser::cleanupImpl(void)
	{
	}
} // End of  CEGUI namespace section
