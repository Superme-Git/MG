/***********************************************************************
    filename:   CEGUITinyXMLParser.cpp
    created:    Sun Mar 13 2005
    author:     Paul D Turner
*************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2006 Paul D Turner & The CEGUI Development Team
 *
 *   Permission is hereby granted, free of charge, to any person obtaining
 *   a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including
 *   without limitation the rights to use, copy, modify, merge, publish,
 *   distribute, sublicense, and/or sell copies of the Software, and to
 *   permit persons to whom the Software is furnished to do so, subject to
 *   the following conditions:
 *
 *   The above copyright notice and this permission notice shall be
 *   included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *   OTHER DEALINGS IN THE SOFTWARE.
 ***************************************************************************/
#ifdef HAVE_CONFIG_H
#   include "config.h"
#endif

#include "CEGUITinyXMLParser.h"
#include "CEGUIResourceProvider.h"
#include "CEGUISystem.h"
#include "CEGUIXMLHandler.h"
#include "CEGUIXMLAttributes.h"
#include "CEGUILogger.h"
#include "CEGUIExceptions.h"
#include CEGUI_TINYXML_H
#include "windows.h"
#include "Nuclear.h"
CEGUI::String CharToCEGUIString__(const char* pText)
{
	int w_StrLen=MultiByteToWideChar(CP_ACP,0,pText,-1,NULL,0);
	wchar_t* w_Str=new wchar_t[w_StrLen];
	MultiByteToWideChar(CP_ACP,0,pText,-1,w_Str,w_StrLen);
	//WideCharToMultiByte(CP_ACP,0,w_Str,-1,buf,256,NULL,NULL);
	int charLen=WideCharToMultiByte(CP_UTF8,0,w_Str,-1,NULL,0,NULL,false);
	char*  buf=new char[charLen];
	WideCharToMultiByte(CP_UTF8,0,w_Str,-1,buf,charLen,NULL,false);
	delete[] w_Str;

	CEGUI::String text((CEGUI::utf8*)buf);
	delete buf;

	return text;

}



// Start of CEGUI namespace section
namespace CEGUI
{
    class TinyXMLDocument : public CEGUI_TINYXML_NAMESPACE::TiXmlDocument
    {
    public:
        TinyXMLDocument(XMLHandler& handler, const String& filename, const String& schemaName, const String& resourceGroup);
        TinyXMLDocument(XMLHandler& handler, const String& parseText);
        ~TinyXMLDocument()
        {}
    protected:
        void processElement(const CEGUI_TINYXML_NAMESPACE::TiXmlElement* element);

	protected:
		void processElementChar(const CEGUI_TINYXML_NAMESPACE::TiXmlElement* element);  //add by qzh for richeditbox 分析中文字符

    private:
        XMLHandler* d_handler;
    };

    TinyXMLDocument::TinyXMLDocument(XMLHandler& handler, const String& parseText)
    {
        d_handler = &handler;

		int w_StrLen=MultiByteToWideChar(CP_UTF8,0,parseText.c_str(),-1,NULL,0);
		wchar_t* w_Str=new wchar_t[w_StrLen];
		MultiByteToWideChar(CP_UTF8,0,parseText.c_str(),-1,w_Str,w_StrLen);
		//WideCharToMultiByte(CP_ACP,0,w_Str,-1,buf,256,NULL,NULL);
		int charLen=WideCharToMultiByte(CP_ACP,0,w_Str,-1,NULL,0,NULL,false);
		char*  buf=new char[charLen+1];
		WideCharToMultiByte(CP_ACP,0,w_Str,-1,buf,charLen,NULL,false);
		delete[] w_Str;
		buf[charLen-1]='\n';
		buf[charLen]=0;

        // Parse the document
        CEGUI_TINYXML_NAMESPACE::TiXmlDocument doc;
		if (!doc.Parse((const char*)buf,NULL,CEGUITinyXML::TIXML_ENCODING_UNKNOWN))
        {
            // error detected, cleanup out buffers
            delete[] buf;
            
            // throw exception
            throw FileIOException("TinyXMLParser: an error occurred while "
                                  "parsing the XML document ");
        }

        const CEGUI_TINYXML_NAMESPACE::TiXmlElement* currElement = doc.RootElement();
        if (currElement)
        {
            try
            {
                // function called recursively to parse xml data
                processElementChar(currElement);
            }
            catch(...)
            {
                delete [] buf;
                throw;
            }
        } // if (currElement)

        // Free memory
        delete [] buf;
    }
    

	TinyXMLDocument::TinyXMLDocument(XMLHandler& handler, const String& filename, const String& /*schemaName*/, const String& resourceGroup)
	{
		d_handler = &handler;

		// use resource provider to load file data
		// Fixed using patch from tracker item #000057
		// cegui_mk2-0.4.1-fixtinyxml.patch
		PFS::CImageData rawXMLData;
		System::getSingleton().getResourceProvider()->loadRawDataContainer(filename, rawXMLData, resourceGroup);

		// Create a buffer with extra bytes for a newline and a terminating null
		size_t size = rawXMLData.GetSize();
		char* buf = new char[size + 2];
		memcpy(buf, rawXMLData.GetData(), size);
		// PDT: The addition of the newline is a kludge to resolve an issue
		// whereby parse returns 0 if the xml file has no newline at the end but
		// is otherwise well formed.
		buf[size] = '\n';
		buf[size+1] = '\0';

		//rawXMLData.insert(rawXMLData.end(),"\n\0",2);
		// Parse the document
		CEGUI_TINYXML_NAMESPACE::TiXmlDocument doc;
		if (!doc.Parse((const char*)buf))
		{
			// error detected, cleanup out buffers
			delete[] buf;
			//System::getSingleton().getResourceProvider()->
			//	unloadRawDataContainer(rawXMLData);
			// throw exception
			throw FileIOException("TinyXMLParser: an error occurred while "
				"parsing the XML document '" + filename +
				"' - check it for potential errors!.");
		}

		const CEGUI_TINYXML_NAMESPACE::TiXmlElement* currElement = doc.RootElement();
		if (currElement)
		{
			try
			{
				// function called recursively to parse xml data
				processElement(currElement);
			}
			catch(...)
			{
				delete [] buf;
				//System::getSingleton().getResourceProvider()->unloadRawDataContainer(rawXMLData);
				throw;
			}
		} // if (currElement)

		// Free memory
		delete [] buf;
		//System::getSingleton().getResourceProvider()->unloadRawDataContainer(rawXMLData);
	}

    void TinyXMLDocument::processElementChar(const CEGUI_TINYXML_NAMESPACE::TiXmlElement* element)
    {
        // build attributes block for the element
        XMLAttributes attrs;

        const CEGUI_TINYXML_NAMESPACE::TiXmlAttribute *currAttr = element->FirstAttribute();
        while (currAttr)
        {
			const char* pTextValue=currAttr->Value();
			const char* pTextName=currAttr->Name();

			

			CEGUI::String ValueText=CharToCEGUIString__(pTextValue);
			CEGUI::String NameText=CharToCEGUIString__(pTextName);

			attrs.add(NameText,ValueText);
            currAttr = currAttr->Next();
        }

        // start element
        d_handler->elementStart((utf8*)element->Value(), attrs);

        // do children
        const CEGUI_TINYXML_NAMESPACE::TiXmlNode* childNode = element->FirstChild();
        while (childNode)
        {
            switch(childNode->Type())
            {
            case CEGUI_TINYXML_NAMESPACE::TiXmlNode::ELEMENT:
                processElementChar(childNode->ToElement());
                break;
            case CEGUI_TINYXML_NAMESPACE::TiXmlNode::TEXT:
                if (childNode->ToText()->Value() != '\0')
                    d_handler->text((utf8*)childNode->ToText()->Value());
                break;

                // Silently ignore unhandled node type
            };
            childNode = childNode->NextSibling();
        }

        // end element
        d_handler->elementEnd((utf8*)element->Value());

		
    }

	void TinyXMLDocument::processElement(const CEGUI_TINYXML_NAMESPACE::TiXmlElement* element)
	{
		// build attributes block for the element
		XMLAttributes attrs;

		const CEGUI_TINYXML_NAMESPACE::TiXmlAttribute *currAttr = element->FirstAttribute();
		while (currAttr)
		{
			attrs.add((utf8*)currAttr->Name(),(utf8*)currAttr->Value());
			currAttr = currAttr->Next();
		}

		// start element
		d_handler->elementStart((utf8*)element->Value(), attrs);

		// do children
		const CEGUI_TINYXML_NAMESPACE::TiXmlNode* childNode = element->FirstChild();
		while (childNode)
		{
			switch(childNode->Type())
			{
			case CEGUI_TINYXML_NAMESPACE::TiXmlNode::ELEMENT:
				processElement(childNode->ToElement());
				break;
			case CEGUI_TINYXML_NAMESPACE::TiXmlNode::TEXT:
				if (childNode->ToText()->Value() != '\0')
					d_handler->text((utf8*)childNode->ToText()->Value());
				break;

				// Silently ignore unhandled node type
			};
			childNode = childNode->NextSibling();
		}

		// end element
		d_handler->elementEnd((utf8*)element->Value());
	}

    TinyXMLParser::TinyXMLParser(void)
    {
        // set ID string
        d_identifierString = "CEGUI::TinyXMLParser - Official tinyXML based parser module for CEGUI";
    }

    TinyXMLParser::~TinyXMLParser(void)
    {}

    void TinyXMLParser::parseXMLFile(XMLHandler& handler, const String& filename, const String& schemaName, const String& resourceGroup)
    {
      TinyXMLDocument doc(handler, filename, schemaName, resourceGroup);
    }

	void TinyXMLParser::parseXMLContent(XMLHandler& handler,const String& content)
	{
		try
		{
			TinyXMLDocument doc(handler,content);
		}
		catch(...)
		{
			std::wstring errorInfo(content.length(),0);
			for(CEGUI::String::size_type i=0;i!=content.length();++i){
				errorInfo[i] = content[i];
			}
			errorInfo.append(L"配的有问题请检查");

			LOG_OutLog_Iffish(errorInfo.c_str());
		}
	}



    bool TinyXMLParser::initialiseImpl(void)
    {
        // This used to prevent deletion of line ending in the middle of a text.
        // WhiteSpace cleaning will be available throught the use of String methods directly
        //CEGUI_TINYXML_NAMESPACE::TiXmlDocument::SetCondenseWhiteSpace(false);
        return true;
    }

    void TinyXMLParser::cleanupImpl(void)
    {}

} // End of  CEGUI namespace section
