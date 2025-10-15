/********************************************************************
	created:	2011/04/20
	created:	20:4:2011   11:40
	filename: 	CEGUIXMLIOParserModule.h
	file path:	cegui\include\XMLParserModules\XMLIOParser
	file base:	CEGUIXMLIOParserModule
	file ext:	h
	purpose:	
*********************************************************************/

#ifndef _CEGUIXMLIOPARSERMODULE_H_
#define _CEGUIXMLIOPARSERMODULE_H_

#include "CEGUIXMLIOParser.h"

/*!
\brief
    exported function that creates an XMLParser based object and returns
    a pointer to that object.
*/
extern "C" CEGUI::XMLParser* createParser(void);

/*!
\brief
    exported function that deletes an XMLParser based object previously
    created by this module.
*/
extern "C" void destroyParser(CEGUI::XMLParser* parser);

#endif // end of guard _CEGUIXMLIOPARSERMODULE_H_
