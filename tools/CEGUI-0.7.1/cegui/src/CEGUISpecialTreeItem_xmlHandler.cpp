
#ifdef HAVE_CONFIG_H
#   include "config.h"
#endif

#include "CEGUISpecialTreeItem_xmlHandler.h"
#include "CEGUIExceptions.h"
#include "CEGUILogger.h"
#include "CEGUIXMLAttributes.h"
#include "CEGUISystem.h"
#include "CEGUIXMLParser.h"
#include "elements/CEGUISpecialTreeItem.h"
#include "elements/CEGUIRichEditboxTextComponent.h"
#include "elements/CEGUIRichEditboxImageComponent.h"
#include "elements/CEGUIRichEditboxGoToComponent.h"


#include "CEGUIFontManager.h"
#include "CEGUIPropertyHelper.h"

//#include "windows.h"

// Start of CEGUI namespace section
namespace CEGUI
{
//----------------------------------------------------------------------------//
const String SpecialTreeItem_xmlHandler::TextElement("T");
const String SpecialTreeItem_xmlHandler::ImageElement("I");
const String SpecialTreeItem_xmlHandler::GoToElement("G");


const String SpecialTreeItem_xmlHandler::TextAttribute("t");
const String SpecialTreeItem_xmlHandler::ColorAttribute("c");
const String SpecialTreeItem_xmlHandler::FontAttribute("f");
const String SpecialTreeItem_xmlHandler::ImageSetNameAttribute("s");
const String SpecialTreeItem_xmlHandler::ImageAttribute("i");
const String SpecialTreeItem_xmlHandler::MapAttribute("m");
const String SpecialTreeItem_xmlHandler::ChefuAttribute("j");
const String SpecialTreeItem_xmlHandler::GoToDirectlyAttribute("goto");
const String SpecialTreeItem_xmlHandler::XPosAttribute("x");
const String SpecialTreeItem_xmlHandler::YPosAttribute("y");
const String SpecialTreeItem_xmlHandler::NpcidAttribute("npcid");
const String SpecialTreeItem_xmlHandler::idAttribute("id");

const String SpecialTreeItem_xmlHandler::TextColor("TextColor");

//inline CEGUI::String CharToCEGUIString__(const char* pszText)
//{
//	//      ASCII -> Unicode
//	int nLen = strlen(pszText);
//	std::wstring unicode(nLen, 0);
//	unicode.resize(
//		MultiByteToWideChar(CP_ACP, 0, pszText, static_cast<int>(nLen),
//		const_cast<wchar_t*>(unicode.data()), unicode.size())
//		);
//
//	//      Unicode -> UTF-8
//	int len = WideCharToMultiByte(CP_UTF8, 0, unicode.data(), static_cast<int>(unicode.size()),
//		NULL, NULL, NULL, NULL);
//
//	char* szBuffer = new char[len +1];
//	memset(szBuffer,0,len +1);
//
//	WideCharToMultiByte(CP_UTF8, 0,unicode.data(), static_cast<int>(unicode.size()),
//		szBuffer, len+1, NULL, NULL);
//
//	CEGUI::String str((CEGUI::utf8*)szBuffer,len+1);
//	delete[] szBuffer;
//	return str;
//
//}



//----------------------------------------------------------------------------//
SpecialTreeItem_xmlHandler::SpecialTreeItem_xmlHandler(const String& filename,
                                 const String& resource_group) :
    d_SpecialTreeItem(0),
    d_objectRead(true)
{
   
}

	SpecialTreeItem_xmlHandler::SpecialTreeItem_xmlHandler(const String& parseText,SpecialTreeItem* pEditbox):
d_objectRead(true)
{
	d_SpecialTreeItem=pEditbox;
	if (parseText.size()>0)
	{
		System::getSingleton().getXMLParser()->parseXMLContent(*this,parseText);

	}
}

//----------------------------------------------------------------------------//
SpecialTreeItem_xmlHandler::~SpecialTreeItem_xmlHandler()
{
    if (!d_objectRead)
        delete d_SpecialTreeItem;
}

//----------------------------------------------------------------------------//


//----------------------------------------------------------------------------//
SpecialTreeItem& SpecialTreeItem_xmlHandler::getObject() const
{
    if (!d_SpecialTreeItem)
        throw InvalidRequestException("Font_xmlHandler::getObject: "
            "Attempt to access null object.");

    d_objectRead = true;
    return *d_SpecialTreeItem;
}

//----------------------------------------------------------------------------//
void SpecialTreeItem_xmlHandler::elementStart(const String& element,
                                   const XMLAttributes& attributes)
{
    // handle root Font element
    if (element == TextElement)
        elementTextStart(attributes);
    // handle a Mapping element
    else if (element == ImageElement)
        elementImageStart(attributes);
	else if (element == GoToElement)
		elementGoToLinkStart(attributes);
    // anything else is a non-fatal error.
    else if (element!="content")
        Logger::getSingleton().logEvent("RichEditbox_xmlHandler::elementStart: "
            "Unknown element encountered: <" + element + ">", Errors);
}

//----------------------------------------------------------------------------//
void SpecialTreeItem_xmlHandler::elementEnd(const String& element)
{
    if (element == TextElement)
        elementTextEnd();
}

//----------------------------------------------------------------------------//
void SpecialTreeItem_xmlHandler::elementTextStart(const XMLAttributes& attributes)
{
    // get type of font being created
	
	if (!d_SpecialTreeItem)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));

	ColourRect cRect(0xffffffff,0xffffffff,0xffffffff,0xffffffff);
	if (attributes.exists(ColorAttribute))
	{
		String colorvalue=attributes.getValueAsString(ColorAttribute);
		cRect.d_top_left = PropertyHelper::stringToColour(colorvalue);
		cRect.d_bottom_left = cRect.d_top_left;
		cRect.d_top_right = cRect.d_top_left;
		cRect.d_bottom_right = cRect.d_top_left;
	}

	if (attributes.exists(TextColor))
	{
		String colorvalue = attributes.getValueAsString(TextColor);
		cRect = PropertyHelper::stringToColourRect(colorvalue);
	}
	
	Font *fnt = const_cast<Font *>(d_SpecialTreeItem->getFont());

	if (attributes.exists(FontAttribute))
	{
		fnt=&FontManager::getSingleton().get(attributes.getValueAsString(FontAttribute));
	}

	RichEditboxTextComponent* pComponent=new RichEditboxTextComponent;

	pComponent->setText(SourceText);
	pComponent->setColours(cRect);
	pComponent->setFont(fnt);
    d_SpecialTreeItem->addChildCompent(pComponent);
}


//----------------------------------------------------------------------------//
void SpecialTreeItem_xmlHandler::elementTextEnd ()
{
    char addr_buff[32];
    sprintf(addr_buff, "(%p)", static_cast<void*>(d_SpecialTreeItem));
    Logger::getSingleton().logEvent("Finished creation of TextComponet '" +
        d_SpecialTreeItem->getText() + "' via XML file. " + addr_buff, Informative);
}

//----------------------------------------------------------------------------//
void SpecialTreeItem_xmlHandler::elementImageStart(const XMLAttributes& attributes)
{
    if (!d_SpecialTreeItem)
        throw InvalidRequestException(
            "RichEditbox_xmlHandler::elementImageStart: Attempt to access null "
            "object.");

	//const char	ImageSetAttribute[]		= "set";
	//const char	ImageAttribute[]	= "image";

	String set(attributes.getValueAsString(ImageSetNameAttribute));
	String image(attributes.getValueAsString(ImageAttribute));
	int specialid = attributes.exists(idAttribute) ? attributes.getValueAsInteger(idAttribute):0;
	
	RichEditboxImageComponent* pComponent=new RichEditboxImageComponent(set,image,specialid);
	d_SpecialTreeItem->addChildCompent(pComponent);
}



//ColourRect RichEditbox_xmlHandler::GetColourectByIndex(size_t index);

//----------------------------------------------------------------------------//
void SpecialTreeItem_xmlHandler::elementGoToLinkStart(const XMLAttributes& attributes)
{
	// get type of font being created

	if (!d_SpecialTreeItem)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));

	

	Font *fnt = const_cast<Font *>(d_SpecialTreeItem->getFont());

	if (attributes.exists(FontAttribute))
	{
		fnt=&FontManager::getSingleton().get(attributes.getValueAsString(FontAttribute));
	}

	RichEditboxGoToComponent* pComponent=new RichEditboxGoToComponent;

	pComponent->setText(SourceText);
	pComponent->setFont(fnt);
	
	if (attributes.exists(ColorAttribute))
	{
		String colorvalue=attributes.getValueAsString(ColorAttribute);
		colour textColour=PropertyHelper::stringToColour(colorvalue);
		pComponent->setColours(textColour);
	}

	int mapid = attributes.exists(MapAttribute) ? attributes.getValueAsInteger(MapAttribute):0;
	int xpos = attributes.exists(XPosAttribute) ? attributes.getValueAsInteger(XPosAttribute):0;
	int ypos = attributes.exists(YPosAttribute) ? attributes.getValueAsInteger(YPosAttribute):0;
	int npcid = attributes.exists(NpcidAttribute) ? attributes.getValueAsInteger(NpcidAttribute):0;
	int chefu  = attributes.exists(ChefuAttribute) ? attributes.getValueAsInteger(ChefuAttribute):0;
    int gotodirectly  = attributes.exists(GoToDirectlyAttribute) ? attributes.getValueAsInteger(GoToDirectlyAttribute):1;
	pComponent->setDestPos(mapid,xpos,ypos,npcid,chefu, gotodirectly == 1);


	d_SpecialTreeItem->addChildCompent(pComponent);
}
} // End of  CEGUI namespace section
