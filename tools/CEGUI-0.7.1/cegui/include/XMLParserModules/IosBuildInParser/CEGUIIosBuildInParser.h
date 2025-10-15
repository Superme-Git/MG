//  CEGUIIosBuildInParser.h
//  CEGUI

#ifndef CEGUI_CEGUIIosBuildInParser_h
#define CEGUI_CEGUIIosBuildInParser_h

#include <CEGUIXMLParser.h>

#define CEGUIIosBuildInParser_API

namespace CEGUI
{
    
    class CEGUIIosBuildInParser_API IosBuildInParser : public XMLParser
    {
    public:
        IosBuildInParser(void);
        virtual ~IosBuildInParser(void);
        
        virtual void parseXMLFile(XMLHandler& handler, const String& filename, const String& schemaName, const String& resourceGroup);
        virtual void parseXMLContent(XMLHandler& handler,const String& content);

    protected:
        virtual bool initialiseImpl(void);
        virtual void cleanupImpl(void);
    };
    
}//endof namespace CEUI

#endif
