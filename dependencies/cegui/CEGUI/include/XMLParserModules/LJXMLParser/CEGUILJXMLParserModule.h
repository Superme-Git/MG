#ifndef _CEGUILJXMLPARSERMODULE_H_
#define _CEGUILJXMLPARSERMODULE_H_

#include "CEGUILJXMLParser.h"

extern "C" CEGUI::XMLParser* createParser(void);

extern "C" void destroyParser(CEGUI::XMLParser* parser);

#endif
