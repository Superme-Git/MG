/********************************************************************
created:	2011/04/20
created:	20:4:2011   11:37
filename: 	CEGUIXMLIOParser.cpp
file path:	cegui\src\XMLParserModules\XMLIOParser
file base:	CEGUIXMLIOParser
file ext:	cpp
purpose:	
*********************************************************************/
#ifdef FORCEGUIEDITOR

#include "CEGUIXMLIOParser.h"
#include "CEGUIResourceProvider.h"
#include "CEGUISystem.h"
#include "CEGUIXMLHandler.h"
#include "CEGUIXMLAttributes.h"
#include "CEGUILogger.h"
#include "CEGUIExceptions.h"
#include "CEGUIPfsResourceProvider.h"
#include "nuclear.h"



// Start of CEGUI namespace section
namespace CEGUI
{
	namespace XMLIOHelper
	{        
        inline std::wstring GUIStringToWString(const CEGUI::String& cs)
		{
			std::wstring ret(cs.length(),0);
			for(CEGUI::String::size_type i=0;i!=cs.length();++i){
				ret[i]=cs[i];
			}
			return ret;
		}
        
		inline CEGUI::String WStringToGUIString(const std::wstring& wstr)
		{
			CEGUI::String str(wstr);
			return str;
		}

	}
	class XMLIOParserDocument : public XMLIO::CFileReader
	{
	public:
		XMLIOParserDocument(XMLHandler& handler, const String& filename, const String& schemaName, const String& resourceGroup);
		XMLIOParserDocument(XMLHandler& handler, const String& parseText);
		~XMLIOParserDocument()
		{}
	protected:
		void processElement(const XMLIO::CINode* element);

	private:
		XMLHandler* d_handler;
	};

	XMLIOParserDocument::XMLIOParserDocument(XMLHandler& handler, const String& parseText)
	{
		d_handler = &handler;

		std::wstring parseTextstr = XMLIOHelper::GUIStringToWString(parseText);
	
		int errorCode = OpenString(parseTextstr);
		// Parse the document
		if (errorCode != XMLIO::EC_SUCCESS)
		{
			// throw exception
			throw FileIOException("XMLIOParser: an error occurred while "
				"parsing the XML document " + parseText);
		}

		XMLIO::CINode root;
		if (GetRootNode(root))
		{
			try
			{
				// function called recursively to parse xml data
				processElement(&root);
			}
			catch(...)
			{
				throw;
			}
		} // if (currElement)
	}


	XMLIOParserDocument::XMLIOParserDocument(XMLHandler& handler, const String& filename, const String& /*schemaName*/, const String& resourceGroup)
	{
		d_handler = &handler;
        
        CEGUI::PFSResourceProvider* rp = static_cast<CEGUI::PFSResourceProvider*>(System::getSingleton().getResourceProvider());
        
		std::wstring pfsfilename = rp-> GetPFSFileName(filename, resourceGroup);

		int nError = OpenFile(XMLIOHelper::GUIStringToWString(pfsfilename));
		if (XMLIO::EC_SUCCESS != nError)
		{
			CloseFile();
            throw FileIOException("XMLIOParser: an error occurred while "
                                  "opening the XML document '" + filename +
                                  "' - check it for potential errors!.");
		}

		XMLIO::CINode root;
		if (!GetRootNode(root))
		{
			// error detected, cleanup out buffers
			CloseFile();

			throw FileIOException("XMLIOParser: an error occurred while "
				"parsing the XML document '" + filename +
				"' - check it for potential errors!.");
		}

		try
		{
			// function called recursively to parse xml data
			processElement(&root);
		}
		catch(...)
		{
			CloseFile();
			throw;
		}
        CloseFile();
	}

	void XMLIOParserDocument::processElement(const XMLIO::CINode* element)
	{
		// build attributes block for the element
		XMLAttributes attrs;

		XMLIO::CAttributesNames attrinames;

		element->GetAttributeNames(attrinames);

		XMLIO::CAttributesNames::iterator it = attrinames.begin();

		while (it != attrinames.end())
		{
			std::wstring aValue;
			element->GetAttribute(*it,aValue);            

			attrs.add((utf8*)(XMLIOHelper::WStringToGUIString(*it).c_str()),(utf8*)(XMLIOHelper::WStringToGUIString(aValue).c_str()));
			++it;
		}	

		//start element
		d_handler->elementStart((utf8*)(XMLIOHelper::WStringToGUIString(element->GetName()).c_str()), attrs);

		// do children
		XMLIO::CNodeList childList;
		element->GetChildren(childList);

		XMLIO::CNodeList::const_iterator childit  = childList.begin();

		while (childit != childList.end())
		{
			switch(childit->GetType())
			{
			case XMLIO::NT_ELEMENT:
				processElement(&*childit);
				break;
			case XMLIO::NT_TEXT:
				if (!childit->GetText().empty())
					d_handler->text((utf8*)(XMLIOHelper::WStringToGUIString(childit->GetText()).c_str()));
				break;
				// Silently ignore unhandled node type
			};
			++childit;
		}

		// end element
		d_handler->elementEnd((utf8*)(XMLIOHelper::WStringToGUIString(element->GetName()).c_str()));

	}

	XMLIOParser::XMLIOParser(void)
	{
		// set ID string
		d_identifierString = "CEGUI::XMLIOParser - XMLIO  based parser module for CEGUI";
	}

	XMLIOParser::~XMLIOParser(void)
	{}

	void XMLIOParser::parseXMLFile(XMLHandler& handler, const String& filename, const String& schemaName, const String& resourceGroup)
	{
        try
		{
            XMLIOParserDocument doc(handler, filename, schemaName, resourceGroup);
        }
		catch(...)
		{
        }
	}

	void XMLIOParser::parseXMLContent(XMLHandler& handler,const String& content)
	{
		try
		{
			XMLIOParserDocument doc(handler,content);
		}
		catch(...)
		{
//			std::wstring errorInfo(content.length(),0);
//			for(CEGUI::String::size_type i=0;i!=content.length();++i){
//				errorInfo[i] = content[i];
//			}
//			errorInfo.append(L"\u914D\u7684\u6709\u95EE\u9898\u8BF7\u68C0\u67E5");
            
            //TODO ADDLOG
			//LOG_OutLog_Warning(errorInfo.c_str());
		}
	}



	bool XMLIOParser::initialiseImpl(void)
	{
		return true;
	}

	void XMLIOParser::cleanupImpl(void)
	{}

} // End of  CEGUI namespace section
#endif