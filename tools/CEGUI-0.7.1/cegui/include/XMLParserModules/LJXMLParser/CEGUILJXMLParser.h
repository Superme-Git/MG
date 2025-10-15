#ifndef	_CEGUILJXMLPARSER_H_
#define _CEGUILJXMLPARSER_H_

#include "config.h"
#include "../../CEGUIXMLParser.h"

namespace CEGUI
{
    class LJXMLParser : public XMLParser
    {
    public:
        LJXMLParser(void);
        ~LJXMLParser(void);

        // Implementation of public abstract interface
        void parseXMLFile(XMLHandler& handler, const String& filename, const String& schemaName, const String& resourceGroup);
		void parseXMLFileBuf(XMLHandler& handler, char* fileBuf, std::streamsize ss);
		void parseXMLContent(XMLHandler& handler,const String& content);

    protected:
        // Implementation of abstract interface.
        bool initialiseImpl(void);
        void cleanupImpl(void);
    };

	CEGUIEXPORT int OpenFromFile(std::wstring fileName, char*& fileBuf, std::streamsize& ss);
}

#endif