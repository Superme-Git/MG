/********************************************************************
	created:	2011/04/20
	created:	20:4:2011   11:33
	filename: 	CEGUIXMLIOParser.h
	file path:	cegui\include\XMLParserModules\XMLIOParser
	file base:	CEGUIXMLIOParser
	file ext:	h
	purpose:	
*********************************************************************/

#ifndef	_CEGUIXMLIOPARSER_H_
#define _CEGUIXMLIOPARSER_H_
#include "config.h"
#include "../../CEGUIXMLParser.h"

#ifdef FORCEGUIEDITOR

namespace CEGUI
{
    /*!
    \brief
    Implementation of XMLParser using TinyXML
     */
    class XMLIOParser : public XMLParser
    {
    public:
        XMLIOParser(void);
        ~XMLIOParser(void);

        // Implementation of public abstract interface
        void parseXMLFile(XMLHandler& handler, const String& filename, const String& schemaName, const String& resourceGroup);
		void parseXMLContent(XMLHandler& handler,const String& content);

    protected:
        // Implementation of abstract interface.
        bool initialiseImpl(void);
        void cleanupImpl(void);
    };

} // End of  CEGUI namespace section


#endif  // end of guard _CEGUIXMLIOPARSER_H_
#endif
