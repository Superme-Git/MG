
#ifdef HAVE_CONFIG_H
#   include "config.h"
#endif

#include "CEGUIRichEditbox_xmlHandler.h"
#include "CEGUIExceptions.h"
#include "CEGUILogger.h"
#include "CEGUIXMLAttributes.h"
#include "CEGUISystem.h"
#include "CEGUIXMLParser.h"
#include "elements/CEGUIRichEditbox.h"
#include "elements/CEGUIRichEditboxTextComponent.h"
#include "elements/CEGUIRichEditboxImageComponent.h"
#include "elements/CEGUIRichEditboxBreakLineComponent.h"
#include "elements/CEGUIRichEditboxLinkTextComponent.h"
#include "elements/CEGUIRichEditboxGoToComponent.h"
#include "elements/CEGUIRichEditboxEmotionComponent.h"
#include "elements/CEGUIRichEditboxTipsLinkComponent.h"
#include "elements/CEGUIRichEditboxNameLinkComponent.h"
#include "elements/CEGUIRichEditboxButtonImageComponent.h"
#include "elements/CEGUIRichEditboxFamilyRecruitLinkComponent.h"
#include "elements/CEGUIRichEditboxHttpComponent.h"
#include "elements/CEGUIRichEditboxJoinTeamLinkComponent.h"
#include "elements/CEGUIRichEditboxRequestTeamLinkComponent.h"
#include "elements/CEGUIRichEditboxAnswerquestionLinkComponent.h"
#include "elements/CEGUIRichEditboxOpenDialogComponent.h"
#include "elements/CEGUIRichEditboxRequestOtherQuestComponent.h"
#include "elements/CEGUIRichEditboxCommonLinkComponent.h"



#include "CEGUIFontManager.h"
#include "CEGUIPropertyHelper.h"

//#include "windows.h"


// Start of CEGUI namespace section
namespace CEGUI
{
//----------------------------------------------------------------------------//
const String RichEditbox_xmlHandler::TextElement("T");
const String RichEditbox_xmlHandler::ImageElement("I");
const String RichEditbox_xmlHandler::ButtonImageElement("BI");
const String RichEditbox_xmlHandler::BreakElement("B");
const String RichEditbox_xmlHandler::TextLinkElement("L");
const String RichEditbox_xmlHandler::GoToElement("G");
const String RichEditbox_xmlHandler::EmotionElement("E");
const String RichEditbox_xmlHandler::TipsLinkElement("P");
const String RichEditbox_xmlHandler::NameLinkElement("N");
const String RichEditbox_xmlHandler::FamilyRecruitElement("F");
const String RichEditbox_xmlHandler::LinkHttpElement("Http");
const String RichEditbox_xmlHandler::JoinTeamElement("R");
const String RichEditbox_xmlHandler::RequestTeamElement("RQ");
const String RichEditbox_xmlHandler::AnswerQuestionElement("A");
const String RichEditbox_xmlHandler::OpenDialogElement("U");
const String RichEditbox_xmlHandler::RequestOtherQuestElement("Q");
const String RichEditbox_xmlHandler::CommonLinkElement("C");


const String RichEditbox_xmlHandler::TextAttribute("t");
const String RichEditbox_xmlHandler::ColorAttribute("c");
const String RichEditbox_xmlHandler::FontAttribute("f");

const String RichEditbox_xmlHandler::ImageSetNameAttribute("s");
const String RichEditbox_xmlHandler::ImageAttribute("i");

const String RichEditbox_xmlHandler::HImageSetNameAttribute("hs");
const String RichEditbox_xmlHandler::HImageAttribute("hi");

const String RichEditbox_xmlHandler::PImageSetNameAttribute("ps");
const String RichEditbox_xmlHandler::PImageAttribute("pi");

const String RichEditbox_xmlHandler::MapAttribute("m");
const String RichEditbox_xmlHandler::ChefuAttribute("j");
const String RichEditbox_xmlHandler::GotoDirectlyAttribute("goto");
const String RichEditbox_xmlHandler::XPosAttribute("x");
const String RichEditbox_xmlHandler::YPosAttribute("y");
const String RichEditbox_xmlHandler::NpcidAttribute("npcid");
const String RichEditbox_xmlHandler::NpcKeyAttribute("npckey");
const String RichEditbox_xmlHandler::EmotionID("e");

const String RichEditbox_xmlHandler::ObjType("type");
const String RichEditbox_xmlHandler::ObjKey("key");
const String RichEditbox_xmlHandler::ObjRoleID("roleid");
const String RichEditbox_xmlHandler::ObjBaseID("baseid");
const String RichEditbox_xmlHandler::ObjShopID("shopid");
const String RichEditbox_xmlHandler::ObjCounterID("counter");
const String RichEditbox_xmlHandler::ObjBind("bind");
const String RichEditbox_xmlHandler::ObjLoseEffTime("loseefftime");
const String RichEditbox_xmlHandler::ObjEnableUnderline("underline");



const String RichEditbox_xmlHandler::TextColor("TextColor");
const String RichEditbox_xmlHandler::RoleName("RoleName");

const String RichEditbox_xmlHandler::FamilyID("id"); //家族ID
const String RichEditbox_xmlHandler::FamilyName("name"); //家族名字
const String RichEditbox_xmlHandler::FamilyLeaderID("headID"); //族长
const String RichEditbox_xmlHandler::FamilyLeader("leader"); //族长
const String RichEditbox_xmlHandler::FamilyRank("rank"); //家族排名
const String RichEditbox_xmlHandler::FamilyLevel("level"); //家族等级
const String RichEditbox_xmlHandler::FamilyAim("aim"); //家族宗旨
const String RichEditbox_xmlHandler::HttpAddress("address"); //网页地址

const String RichEditbox_xmlHandler::JoinTeamLeaderId("leaderid");//队长id
const String RichEditbox_xmlHandler::JoinTeamId("teamid");//队伍id
const String RichEditbox_xmlHandler::AnswerQuestionQuestionid("questionid");//答题id
const String RichEditbox_xmlHandler::AnswerQuestionName("name");//答题人名字
const String RichEditbox_xmlHandler::AnswerQuestionRoleid("roleid");//答题人名字
const String RichEditbox_xmlHandler::OpenDialogID("did");  //要打开窗口的id
const String RichEditbox_xmlHandler::requestQuestID("qid");
const String RichEditbox_xmlHandler::CommonLinkArg("arg");//通用链接，参数拼成字符串

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
RichEditbox_xmlHandler::RichEditbox_xmlHandler(const String& filename,
                                 const String& resource_group) :
    d_RichEditbox(0),
    d_objectRead(true),
	d_CheckShied(false)
{
   
}

RichEditbox_xmlHandler::RichEditbox_xmlHandler(const String& parseText,RichEditbox* pEditbox,bool bCheckShied):
d_objectRead(true),
d_CheckShied(bCheckShied)

{
	d_RichEditbox=pEditbox;

	d_vecTextCpn.clear();
	
	if (parseText.size()>0)
	{
		System::getSingleton().getXMLParser()->parseXMLContent(*this,parseText);

    }
}

//----------------------------------------------------------------------------//
RichEditbox_xmlHandler::~RichEditbox_xmlHandler()
{
    if (!d_objectRead)
        delete d_RichEditbox;
}

//----------------------------------------------------------------------------//


//----------------------------------------------------------------------------//
RichEditbox& RichEditbox_xmlHandler::getObject() const
{
    if (!d_RichEditbox)
        throw InvalidRequestException("Font_xmlHandler::getObject: "
            "Attempt to access null object.");

    d_objectRead = true;
    return *d_RichEditbox;
}

//----------------------------------------------------------------------------//
void RichEditbox_xmlHandler::elementStart(const String& element,
                                   const XMLAttributes& attributes)
{
    // handle root Font element
    if (element == TextElement)
        elementTextStart(attributes);
    // handle a Mapping element
    else if (element == ImageElement)
        elementImageStart(attributes);
	else if (element == ButtonImageElement)
		elementButtonImageStart(attributes);
	else if (element == BreakElement)
		elementLineBreakStart(attributes);
	else if (element == TextLinkElement)
		elementLinkTextStart(attributes);
	else if (element == GoToElement)
		elementGoToLinkStart(attributes);
	else if (element == EmotionElement)
		elementEmotionStart(attributes);
	else if (element == TipsLinkElement)
		elementObjTipsStart(attributes);
	else if (element == NameLinkElement)
		elementNameLinkStart(attributes);
	else if (element == FamilyRecruitElement)
		elementFamilyRecruitStart(attributes);
	else if (element == LinkHttpElement)
		elementLinkHttpStart(attributes);
	else if (element == JoinTeamElement)
		elementJoinTeamStart(attributes);
	else if (element == RequestTeamElement)
		elementRequestTeamStart(attributes);
	else if (element == AnswerQuestionElement)
		elementAnswerQusetionStart(attributes);
	else if (element == OpenDialogElement)
		elementOpenDialogStart(attributes);
	else if (element == RequestOtherQuestElement)
		elementRequestRequestStart(attributes);
	else if (element == CommonLinkElement)
		elementCommonLinkStart(attributes);

	
    // anything else is a non-fatal error.
    else if (element!="content")
    {
		Logger::getSingleton().logEvent("RichEditbox_xmlHandler::elementStart: "
			"Unknown element encountered: <" + element + ">", Errors);
    }
}

//----------------------------------------------------------------------------//
void RichEditbox_xmlHandler::elementEnd(const String& element)
{
    if (element == TextElement)
        elementTextEnd();
}

//----------------------------------------------------------------------------//
void RichEditbox_xmlHandler::elementTextStart(const XMLAttributes& attributes)
{
    // get type of font being created
	
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));

	//const char* pText=(const char*)SourceText.data();

	//int w_StrLen=MultiByteToWideChar(CP_ACP,0,SourceText.c_str(),-1,NULL,0);
	//wchar_t* w_Str=new wchar_t[w_StrLen];
	//MultiByteToWideChar(CP_ACP,0,SourceText.c_str(),-1,w_Str,w_StrLen);
	////WideCharToMultiByte(CP_ACP,0,w_Str,-1,buf,256,NULL,NULL);
	//int charLen=WideCharToMultiByte(CP_UTF8,0,w_Str,-1,NULL,0,NULL,false);
	//char*  buf=new char[charLen];
	//WideCharToMultiByte(CP_UTF8,0,w_Str,-1,buf,charLen,NULL,false);
	//delete[] w_Str;

	//CEGUI::String text(buf);
	//delete[] buf;
	//buf[charLen-1]='\n';
	//buf[charLen]=0;

	ColourRect cRect(0xffffffff,0xffffffff,0xffffffff,0xffffffff);
	cRect = d_RichEditbox->GetColourRect();

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
	
	Font *fnt = const_cast<Font *>(d_RichEditbox->getFont());

	if (attributes.exists(FontAttribute))
	{
		fnt=&FontManager::getSingleton().get(attributes.getValueAsString(FontAttribute));
	}

	RichEditboxTextComponent* pComponent=new RichEditboxTextComponent;

	pComponent->setText(SourceText);
	pComponent->setColours(cRect);
	pComponent->setFont(fnt);
    d_RichEditbox->AddRefreshComponent(pComponent);

	if (d_CheckShied)
	{
		d_vecTextCpn.push_back(pComponent);
	}

}

void RichEditbox_xmlHandler::elementLinkTextStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));

	/*colour textColour(0xFFFFFFFF);
	if (attributes.exists(ColorAttribute))
	{
		String colorvalue=attributes.getValueAsString(ColorAttribute);
		textColour=PropertyHelper::stringToColour(colorvalue);


	}*/


	Font *fnt = const_cast<Font *>(d_RichEditbox->getFont());

	if (attributes.exists(FontAttribute))
	{
		fnt=&FontManager::getSingleton().get(attributes.getValueAsString(FontAttribute));
	}

	RichEditboxLinkTextComponent* pComponent=new RichEditboxLinkTextComponent;

	pComponent->setText(SourceText);
	//pComponent->setColours(textColour);
	pComponent->setFont(fnt);
	d_RichEditbox->AddRefreshComponent(pComponent);

}


//----------------------------------------------------------------------------//
void RichEditbox_xmlHandler::elementTextEnd ()
{
    char addr_buff[32];
    sprintf(addr_buff, "(%p)", static_cast<void*>(d_RichEditbox));
    Logger::getSingleton().logEvent("Finished creation of TextComponet '" +
        d_RichEditbox->getName() + "' via XML file. " + addr_buff, Informative);
}

//----------------------------------------------------------------------------//
void RichEditbox_xmlHandler::elementImageStart(const XMLAttributes& attributes)
{
    if (!d_RichEditbox)
        throw InvalidRequestException(
            "RichEditbox_xmlHandler::elementImageStart: Attempt to access null "
            "object.");

	//const char	ImageSetAttribute[]		= "set";
	//const char	ImageAttribute[]	= "image";

	String set(attributes.getValueAsString(ImageSetNameAttribute));
	String image(attributes.getValueAsString(ImageAttribute));

	RichEditboxImageComponent* pComponent=new RichEditboxImageComponent(set,image);
	d_RichEditbox->AddRefreshComponent(pComponent);
}


//----------------------------------------------------------------------------//
void RichEditbox_xmlHandler::elementButtonImageStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementButtonImageStart: Attempt to access null "
		"object.");

	//const char	ImageSetAttribute[]		= "set";
	//const char	ImageAttribute[]	= "image";

	String set(attributes.getValueAsString(ImageSetNameAttribute));
	String image(attributes.getValueAsString(ImageAttribute));

	String hset(attributes.getValueAsString(HImageSetNameAttribute));
	String himage(attributes.getValueAsString(HImageAttribute));

	String pset(attributes.getValueAsString(PImageSetNameAttribute));
	String pimage(attributes.getValueAsString(PImageAttribute));

	int id=attributes.getValueAsInteger(ObjType);

	RichEditboxButtonImageComponent* pComponent=new RichEditboxButtonImageComponent(set,image,hset,himage,pset,pimage,id);
	d_RichEditbox->AddRefreshComponent(pComponent);
}


void RichEditbox_xmlHandler::elementLineBreakStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementImageStart: Attempt to access null "
		"object.");

	RichEditboxBreakLineComponent* pComponent=new RichEditboxBreakLineComponent;
	d_RichEditbox->AddRefreshComponent(pComponent);
}

//ColourRect RichEditbox_xmlHandler::GetColourectByIndex(size_t index);

//----------------------------------------------------------------------------//
void RichEditbox_xmlHandler::elementGoToLinkStart(const XMLAttributes& attributes)
{
	// get type of font being created

	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));

	

	Font *fnt = const_cast<Font *>(d_RichEditbox->getFont());

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
    int npckey = attributes.exists(NpcKeyAttribute) ? attributes.getValueAsInt64(NpcKeyAttribute):0;
	int chefu  = attributes.exists(ChefuAttribute) ? attributes.getValueAsInteger(ChefuAttribute):0;
    int gotodirectly  = attributes.exists(GotoDirectlyAttribute) ? attributes.getValueAsInteger(GotoDirectlyAttribute):1;
	pComponent->setDestPos(mapid,xpos,ypos,npcid,chefu, gotodirectly == 1,npckey);
		
	d_RichEditbox->AddRefreshComponent(pComponent);
}

//----------------------------------------------------------------------------//
void RichEditbox_xmlHandler::elementEmotionStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementImageStart: Attempt to access null "
		"object.");

	//const char	ImageSetAttribute[]		= "set";
	//const char	ImageAttribute[]	= "image";

	int emotionID=attributes.getValueAsInteger(EmotionID);
	

	RichEditboxEmotionComponent* pComponent=new RichEditboxEmotionComponent();
	pComponent->SetAnimateID(emotionID);
	pComponent->SetScale(d_RichEditbox->GetEmotionScale());
	d_RichEditbox->AddRefreshComponent(pComponent);
}

void RichEditbox_xmlHandler::elementObjTipsStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));
	int type=attributes.getValueAsInteger(ObjType);
	CEGUI::String key =attributes.getValueAsString(ObjKey);
	int baseid=attributes.getValueAsInteger(ObjBaseID);
	int64_t roleid=attributes.getValueAsInt64(ObjRoleID);

	int64_t shopID=attributes.getValueAsInt64(ObjShopID);
	int counterID=attributes.getValueAsInteger(ObjCounterID);
	int bind=attributes.getValueAsInteger(ObjBind);
	int64_t loseEffTime=attributes.getValueAsInt64(ObjLoseEffTime);

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

	int enableunderline = 0;
	if (attributes.exists(ObjEnableUnderline))
	{
		enableunderline = attributes.getValueAsInteger(ObjEnableUnderline);
	}

	RichEditboxTipsLinkComponent* pComponent=new RichEditboxTipsLinkComponent(SourceText,"",roleid,type,key,baseid,shopID,counterID,bind,loseEffTime);
	pComponent->setFont(d_RichEditbox->getFont());
	pComponent->setColours(cRect);
	if (enableunderline > 0)
	{
		pComponent->EnableUnderLine(true);
	}
	d_RichEditbox->AddRefreshComponent(pComponent);

}

void RichEditbox_xmlHandler::elementNameLinkStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String strName(attributes.getValueAsString(RoleName));
	int64_t roleid=attributes.getValueAsInt64(ObjRoleID);
	int nameColorType=attributes.getValueAsInteger(ColorAttribute,0);

	RichEditboxNameLinkComponent* pComponent=new RichEditboxNameLinkComponent(roleid,strName);
	pComponent->setFont(d_RichEditbox->getFont());
	pComponent->SetNameColorType(nameColorType);
	d_RichEditbox->AddRefreshComponent(pComponent);

}

void RichEditbox_xmlHandler::elementFamilyRecruitStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	
	int iID=attributes.getValueAsInteger(FamilyID);
	CEGUI::String strFamilyName(attributes.getValueAsString(FamilyName));
	CEGUI::String strFamilyLeader(attributes.getValueAsString(FamilyLeader));
	CEGUI::String strFamilyAim(attributes.getValueAsString(FamilyAim));
	int iLevel=attributes.getValueAsInteger(FamilyLevel);
	int iRank=attributes.getValueAsInteger(FamilyRank);
	int64_t iLeaderID=attributes.getValueAsInt64(FamilyLeaderID);


	RichEditboxFamilyRecruitLinkComponent* pComponent=new RichEditboxFamilyRecruitLinkComponent(iID,strFamilyName,
		iLeaderID,strFamilyLeader,iRank,iLevel,strFamilyAim);
	pComponent->setFont(d_RichEditbox->getFont());
	d_RichEditbox->AddRefreshComponent(pComponent);

}

//----------------------------------------------------------------------------//
void RichEditbox_xmlHandler::elementLinkHttpStart(const XMLAttributes& attributes)
{
	// get type of font being created

	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));
	CEGUI::String AddressText(attributes.getValueAsString(HttpAddress));

	Font *fnt = const_cast<Font *>(d_RichEditbox->getFont());

	if (attributes.exists(FontAttribute))
	{
		fnt=&FontManager::getSingleton().get(attributes.getValueAsString(FontAttribute));
	}

	RichEditboxHttpComponent* pComponent=new RichEditboxHttpComponent(SourceText,AddressText,fnt);

	if (attributes.exists(ColorAttribute))
	{
		String colorvalue=attributes.getValueAsString(ColorAttribute);
		colour textColour=PropertyHelper::stringToColour(colorvalue);
		pComponent->setColours(textColour);
	}

	d_RichEditbox->AddRefreshComponent(pComponent);
}

void RichEditbox_xmlHandler::elementAnswerQusetionStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));
	int questionid = attributes.getValueAsInteger(AnswerQuestionQuestionid);
	int roleid = attributes.getValueAsInteger(AnswerQuestionRoleid);
	int type = attributes.getValueAsInteger(ObjType);
	CEGUI::String name(attributes.getValueAsString(AnswerQuestionName));
	RichEditboxAnswerQuestionLinkComponent *component = new RichEditboxAnswerQuestionLinkComponent(SourceText, questionid, name, roleid, type);
	if (attributes.exists(ColorAttribute))
	{
		String colorvalue = attributes.getValueAsString(ColorAttribute);
		colour textColour = PropertyHelper::stringToColour(colorvalue);
		component->setColours(textColour);
	}
	d_RichEditbox->AddRefreshComponent(component);
}

void RichEditbox_xmlHandler::elementJoinTeamStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));
	int leaderid = attributes.getValueAsInt64(JoinTeamLeaderId);
	RichEditboxJoinTeamLinkComponent *component = new RichEditboxJoinTeamLinkComponent(SourceText, leaderid);
	if (attributes.exists(ColorAttribute))
	{
		String colorvalue = attributes.getValueAsString(ColorAttribute);
		colour textColour = PropertyHelper::stringToColour(colorvalue);
		component->setColours(textColour);
	}
	d_RichEditbox->AddRefreshComponent(component);
}

void RichEditbox_xmlHandler::elementOpenDialogStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));
	int dialogID = attributes.getValueAsInt64(OpenDialogID);
	RichEditboxOpenDialogComponent *component = new RichEditboxOpenDialogComponent(SourceText, dialogID);
	if (attributes.exists(ColorAttribute))
	{
		String colorvalue = attributes.getValueAsString(ColorAttribute);
		colour textColour = PropertyHelper::stringToColour(colorvalue);
		component->setColours(textColour);
	}
	d_RichEditbox->AddRefreshComponent(component);
}

void RichEditbox_xmlHandler::elementRequestRequestStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));
	int questID = attributes.getValueAsInt64(requestQuestID);
	RichEditboxRequestOtherQuestComponent *component = new RichEditboxRequestOtherQuestComponent(SourceText, questID);
	if (attributes.exists(ColorAttribute))
	{
		String colorvalue = attributes.getValueAsString(ColorAttribute);
		colour textColour = PropertyHelper::stringToColour(colorvalue);
		component->setColours(textColour);
	}
	d_RichEditbox->AddRefreshComponent(component);
}


void RichEditbox_xmlHandler::elementRequestTeamStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));
	int teamid = attributes.getValueAsInt64(JoinTeamId);
	RichEditboxRequestTeamLinkComponent *component = new RichEditboxRequestTeamLinkComponent(SourceText, teamid);
	if (attributes.exists(ColorAttribute))
	{
		String colorvalue = attributes.getValueAsString(ColorAttribute);
		colour textColour = PropertyHelper::stringToColour(colorvalue);
		component->setColours(textColour);
	}
	d_RichEditbox->AddRefreshComponent(component);
}

void RichEditbox_xmlHandler::elementCommonLinkStart(const XMLAttributes& attributes)
{
	if (!d_RichEditbox)
		throw InvalidRequestException(
		"RichEditbox_xmlHandler::elementTextStart: Attempt to access null "
		"object.");

	CEGUI::String SourceText(attributes.getValueAsString(TextAttribute));
	CEGUI::String arg(attributes.getValueAsString(CommonLinkArg));

	RichEditboxCommonLinkComponent *component = new RichEditboxCommonLinkComponent(SourceText, arg);
	if (attributes.exists(ColorAttribute))
	{
		String colorvalue = attributes.getValueAsString(ColorAttribute);
		colour textColour = PropertyHelper::stringToColour(colorvalue);
		component->setColours(textColour);
	}
	d_RichEditbox->AddRefreshComponent(component);
}

void RichEditbox_xmlHandler::CheckShied() //检查屏蔽字
{
	if (d_CheckShied)
	{
		String fullText("");
		std::vector<TextCpnInf> vecTextCpnInfs;
		vecTextCpnInfs.clear();
		std::vector<RichEditboxTextComponent*>::iterator it=d_vecTextCpn.begin();
		size_t curIdx=0;
		for (;it!=d_vecTextCpn.end();++it)
		{
			String cpnText=(*it)->getText();
			fullText+=cpnText;

			TextCpnInf inf(0,0);
			inf.startIdx=curIdx;
			inf.endIdx=curIdx+cpnText.length()-1;
			vecTextCpnInfs.push_back(inf);
			curIdx+=cpnText.length();
		}

		bool bShied=System::getSingleton().CheckShied(fullText);
		if (bShied)
		{
			std::vector<RichEditboxTextComponent*>::iterator it=d_vecTextCpn.begin();
			int curIdx=0;
			for (size_t i=0;i<vecTextCpnInfs.size();++i)
			{
				size_t startIdx=vecTextCpnInfs[i].startIdx;
				size_t endIdx=vecTextCpnInfs[i].endIdx;
				if (startIdx<fullText.size()&&endIdx<fullText.size()
					&&i<d_vecTextCpn.size()&&endIdx>=startIdx)
				{
					String cpnText=fullText.substr(startIdx,endIdx-startIdx+1);
					d_vecTextCpn[i]->setText(cpnText);
				}
				
				
			}

		}
	}
	
}



} // End of  CEGUI namespace section
