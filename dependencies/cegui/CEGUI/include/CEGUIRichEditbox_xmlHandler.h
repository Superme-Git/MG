/***********************************************************************
    filename:   CEGUIRichEditbox_xmlHandler.h
*************************************************************************/

#ifndef _CEGUIRichEditbox_xmlHandler_h_
#define _CEGUIRichEditbox_xmlHandler_h_

#include "CEGUIXMLHandler.h"
#include "CEGUIString.h"
#include <vector>


// Start of CEGUI namespace section

namespace CEGUI
{
//! Handler class used to parse the Font XML files to create Font objects.
class RichEditbox;

class RichEditboxTextComponent;

class CEGUIEXPORT RichEditbox_xmlHandler : public XMLHandler
{
public:
    
    static const String TextElement;
	static const String ImageElement;
	static const String BreakElement;  //换行
	static const String TextLinkElement; //链接文本
	static const String GoToElement; //自动寻路链接
	static const String EmotionElement; //表情
	static const String TipsLinkElement; //物品tips链接
	static const String NameLinkElement; //名字链接
	static const String ButtonImageElement; //按钮图片
	static const String FamilyRecruitElement; //家族招募
	static const String LinkHttpElement; //网页链接
	static const String JoinTeamElement; //申请入队
	static const String RequestTeamElement; //请求队伍信息 by yq 2016-03-25
	static const String AnswerQuestionElement; //答题
	static const String OpenDialogElement; //打开UI窗口
	static const String RequestOtherQuestElement; //精灵答题请求某个id的题目
	static const String CommonLinkElement;	//通用链接，参数拼成字符串


	static const String TextAttribute;

	static const String ColorAttribute;
	static const String FontAttribute;

	static const String ImageSetNameAttribute;
	static const String ImageAttribute;

	static const String HImageSetNameAttribute;//imagebutton 有焦点的图片属性
	static const String HImageAttribute;

	static const String PImageSetNameAttribute;//imagebutton 被按下的图片属性
	static const String PImageAttribute;

	static const String ChefuAttribute;
    static const String GotoDirectlyAttribute;
	static const String MapAttribute;
	static const String XPosAttribute;
	static const String YPosAttribute;
	static const String NpcidAttribute;
    static const String NpcKeyAttribute;
	static const String EmotionID;

	static const String ObjType;  //物品类型
	static const String ObjKey;  //物品key
	static const String ObjRoleID; //物品主人的roleid
	static const String ObjBaseID; //物品主人的roleid
	static const String ObjShopID; //物品主人的roleid
	static const String ObjCounterID; //物品主人的roleid
	static const String ObjBind; //是否绑定
	static const String ObjLoseEffTime; //失效时间
	static const String ObjEnableUnderline;  //是否有下划线

	static const String TextColor;
	static const String RoleName; //玩家名

	static const String FamilyID; //家族ID
	static const String FamilyName; //家族名字
    static const String FamilyLeaderID; //族长ID
	static const String FamilyLeader; //族长
	static const String FamilyRank; //家族排名
	static const String FamilyLevel; //家族等级
	static const String FamilyAim; //家族宗旨
	static const String HttpAddress; //网页地址

	static const String JoinTeamLeaderId;//队长id
	static const String JoinTeamId;//队伍id

	static const String AnswerQuestionQuestionid;//答题id
	static const String AnswerQuestionName;//答题人名字
	static const String	AnswerQuestionRoleid;
	static const String OpenDialogID;
	static const String CommonLinkArg;//通用链接，参数拼成字符串


	static const String requestQuestID;

    //! Constructor.
    RichEditbox_xmlHandler(const String& filename, const String& resource_group);
	RichEditbox_xmlHandler(const String& parseText,RichEditbox* pBox,bool bCheckShied=false);


    //! Destructor.
    ~RichEditbox_xmlHandler();

    //! Return reference to the created Richeditbox object.
    RichEditbox& getObject() const;
	void SetObject(RichEditbox* pRichEditbox){ d_RichEditbox=pRichEditbox; }

    // XMLHandler overrides
    void elementStart(const String& element, const XMLAttributes& attributes);
    void elementEnd(const String& element);

	static ColourRect GetColourectByIndex(size_t index);

	void CheckShied(); //检查屏蔽字

private:
    //! handles the opening Font XML element.
    void elementTextStart(const XMLAttributes& attributes);
    //! handles the closing Font XML element.
    void elementTextEnd();
    //! handles the opening Mapping XML element.
    void elementImageStart(const XMLAttributes& attributes);
    void elementButtonImageStart(const XMLAttributes& attributes);

	void elementLineBreakStart(const XMLAttributes& attributes);
	void elementLinkTextStart(const XMLAttributes& attributes);

    void elementGoToLinkStart(const XMLAttributes& attributes);

	void elementEmotionStart(const XMLAttributes& attributes);

	void elementObjTipsStart(const XMLAttributes& attributes);

	void elementNameLinkStart(const XMLAttributes& attributes);

	void elementFamilyRecruitStart(const XMLAttributes& attributes);

	void elementLinkHttpStart(const XMLAttributes& attributes);

	void elementJoinTeamStart(const XMLAttributes& attributes);
	void elementRequestTeamStart(const XMLAttributes& attributes); //请求队伍信息 by yq 2016-03-25

	void elementAnswerQusetionStart(const XMLAttributes& attributes);

	void elementOpenDialogStart(const XMLAttributes& attributes);

	void elementRequestRequestStart(const XMLAttributes& attributes);


	void elementCommonLinkStart(const XMLAttributes& attributes);

    //! Font object that we are constructing.
    RichEditbox* d_RichEditbox;
    //! inidcates whether client read the created object
    
	
	mutable bool d_objectRead;
	bool d_CheckShied;
	std::vector<RichEditboxTextComponent*> d_vecTextCpn;
};

} // End of  CEGUI namespace section

#endif
