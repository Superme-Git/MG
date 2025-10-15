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
	static const String BreakElement;  //����
	static const String TextLinkElement; //�����ı�
	static const String GoToElement; //�Զ�Ѱ·����
	static const String EmotionElement; //����
	static const String TipsLinkElement; //��Ʒtips����
	static const String NameLinkElement; //��������
	static const String ButtonImageElement; //��ťͼƬ
	static const String FamilyRecruitElement; //������ļ
	static const String LinkHttpElement; //��ҳ����
	static const String JoinTeamElement; //�������
	static const String RequestTeamElement; //���������Ϣ by yq 2016-03-25
	static const String AnswerQuestionElement; //����
	static const String OpenDialogElement; //��UI����
	static const String RequestOtherQuestElement; //�����������ĳ��id����Ŀ
	static const String CommonLinkElement;	//ͨ�����ӣ�����ƴ���ַ���


	static const String TextAttribute;

	static const String ColorAttribute;
	static const String FontAttribute;

	static const String ImageSetNameAttribute;
	static const String ImageAttribute;

	static const String HImageSetNameAttribute;//imagebutton �н����ͼƬ����
	static const String HImageAttribute;

	static const String PImageSetNameAttribute;//imagebutton �����µ�ͼƬ����
	static const String PImageAttribute;

	static const String ChefuAttribute;
    static const String GotoDirectlyAttribute;
	static const String MapAttribute;
	static const String XPosAttribute;
	static const String YPosAttribute;
	static const String NpcidAttribute;
    static const String NpcKeyAttribute;
	static const String EmotionID;

	static const String ObjType;  //��Ʒ����
	static const String ObjKey;  //��Ʒkey
	static const String ObjRoleID; //��Ʒ���˵�roleid
	static const String ObjBaseID; //��Ʒ���˵�roleid
	static const String ObjShopID; //��Ʒ���˵�roleid
	static const String ObjCounterID; //��Ʒ���˵�roleid
	static const String ObjBind; //�Ƿ��
	static const String ObjLoseEffTime; //ʧЧʱ��
	static const String ObjEnableUnderline;  //�Ƿ����»���

	static const String TextColor;
	static const String RoleName; //�����

	static const String FamilyID; //����ID
	static const String FamilyName; //��������
    static const String FamilyLeaderID; //�峤ID
	static const String FamilyLeader; //�峤
	static const String FamilyRank; //��������
	static const String FamilyLevel; //����ȼ�
	static const String FamilyAim; //������ּ
	static const String HttpAddress; //��ҳ��ַ

	static const String JoinTeamLeaderId;//�ӳ�id
	static const String JoinTeamId;//����id

	static const String AnswerQuestionQuestionid;//����id
	static const String AnswerQuestionName;//����������
	static const String	AnswerQuestionRoleid;
	static const String OpenDialogID;
	static const String CommonLinkArg;//ͨ�����ӣ�����ƴ���ַ���


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

	void CheckShied(); //���������

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
	void elementRequestTeamStart(const XMLAttributes& attributes); //���������Ϣ by yq 2016-03-25

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
