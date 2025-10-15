/***********************************************************************
	filename: 	CEGUIItemCellGeneralGeneral.h
*************************************************************************/

#ifndef _CEGUIItemCellGeneralGeneral_h_
#define _CEGUIItemCellGeneralGeneral_h_

#include "../CEGUIWindow.h"
#include "../CEGUIWindowFactory.h"


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{    

	/*!
    \brief
        Generic drag & drop enabled window class
    */

	enum ItemCellGeneralTextType         //cellһ����
	{
		ItemCellGeneralTextType_LeftTop,  //���������Ͻ�
		ItemCellGeneralTextType_LeftBottom,  //���������½�
		ItemCellGeneralTextType_RightBottom,//���������½�
		ItemCellGeneralTextType_Center,//���� by wangjianfeng
		ItemCellGeneralTextType_NUM
	};

	struct ItemCellGeneralTextUnit 
	{
		String Text;
		ItemCellGeneralTextType type;
		colour color;

		ItemCellGeneralTextUnit():
		Text(""),
		type(ItemCellGeneralTextType_RightBottom),
		color(0xFFFFFFFF)
		{
		}
	};

	const colour DefaultItemCellGeneralTextColor=colour(1.0f,1.0f,1.0f,1.0);
	
    
	class CEGUIEXPORT ItemCellGeneral : public Window
    {
    public:
		//����ͨ������ö��
		enum GeneralType
		{
			GT_NONE = 0,
			GT_ITEM = 1,
			GT_SKILL = 2,
			GT_NUM = 3
		};
       
        static const String WidgetTypeName;     //!< Type name for DragContainer.
        static const String EventNamespace;     //!< Namespace for global events
		static const String EventCellClick;    //!< click events
		static const String EventLockCellClick;    //!< click events
		static const int ItemCellGeneralSize = 77;	//skillbox���ֺ���

        ItemCellGeneral(const String& type, const String& name);
        virtual ~ItemCellGeneral(void);

		bool testClassName_impl(const String& class_name) const
		{
			if (class_name==(const utf8*)"ItemCellGeneral")
				return true;

			return Window::testClassName_impl(class_name);
		}
		size_t GetIndex() { return d_index; }
		void SetIndex(size_t index)  {  d_index=index; }
		void SetImage( const Image* image);
		const Image* GetImage() const { return d_image; } 

		void SetImage(String Imageset, String ImageName);

		bool isMouseOnThisCell() { return d_isMouseOnThisCell; }
		void setMouseOnThisCell(bool bMouseOn);// { d_isMouseOnThisCell = bMouseOn;}

		void SetTextUnit(CEGUI::String text,ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom,CEGUI::colour color=DefaultItemCellGeneralTextColor );
		void SetTextUnitText(CEGUI::String text,ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom);
		void SetTextUnitColor(CEGUI::colour color,ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom);
		ItemCellGeneralTextUnit* GetTextUnit(ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom);
		CEGUI::Point GetTextUnitDrawPos(const Rect& rect,ItemCellGeneralTextType type=ItemCellGeneralTextType_RightBottom); //���

		bool isBackGroundEnable()  { return d_BackGroundEnable; }
		void SetBackGroundEnable(bool bEnable);
		int GetCellTypeMask() { return d_CellTypeMask; }
		void SetCellTypeMask(int mask) { d_CellTypeMask=mask; }

		bool IsAshy()  { return d_ashy;}
		void SetAshy(bool bAshy);// { d_ashy = bAshy; invalidate();}

		bool IsLock()  { return d_lock;}
		void SetLockState(bool block);// { d_ashy = bAshy; invalidate();}

		bool isSelected() { return d_selected;}
		void SetSelected(bool bSelect);
		void SetMutiSelected(bool bSelect);

		void SetHaveSelectedState(bool bHaveSelected) { d_haveSelectedState = bHaveSelected;}

		void SetBackGroundImage(const Image* image);
		void SetBackGroundImage(String Imageset, String ImageName);
		bool IsHaveBackGroundImage() { return d_backimage != NULL;}
		const Image* GetBackIamge() const { return d_backimage; } 
		
		void SetEquipColorImage(const Image* image);
		void SetEquipColorImage(String Imageset, String ImageName);
		const Image* GetColorImage() const { return d_colorimage; } 

		void SetCornerImage(const Image* image);
		void SetCornerImage(String Imageset, String ImageName);
		const Image* GetCornerImage() const { return d_cornerimage; } 

		void Clear();

		bool HasFloodLight(){ return d_HasFloodLight; } //�Ƿ�������
		void SetFloodLight(bool bHasFloodLight) { d_HasFloodLight=bHasFloodLight; }

		bool isImageAutoScale() { return d_ImageAutoScale; }
		void SetImageAutoScale(bool bAutoScale);


		//<<==============================================
		//��skillbox�෽�����ֺ������
		void SetGeneralID(int id);
		int  GetGeneralID() const;

		void SetSkillDueDate(int64_t time) { d_skillDueDate = time;}
		int64_t  GetSkillDueDate() const { return d_skillDueDate;}

		void SetSourceBox(const String& pbox);
		String GetSourceBox() const;

		void SetCanPickedInBattle(){d_pickupinbattle = true;}
		bool IsCanPickedInBattle(){return d_pickupinbattle;}
		//>>================================================

		void SetGeneralType(GeneralType type){d_generalType = type;}
		GeneralType GetGeneralType(){return d_generalType;}

		void SetKeyID(int keyID){d_keyID = keyID;}
		int GetKeyID(){return d_keyID;}

	public:
		ItemCellGeneral(const String& type);
		const ItemCellGeneral& operator=(const ItemCellGeneral& t);
		virtual Window* clone(Window* wnd);
		virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

	private:
        void addItemCellGeneralProperties(void);
		void deselectOtherCellInTable(void); //��table�ϵ��������ӵ�ѡ��״̬��Ϊfalse
		size_t d_index;              //���ӵ�λ�ñ�־�ڼ��еڼ��о;�����index
		bool d_isMouseOnThisCell;    //mouse�Ƿ��������������
		bool d_BackGroundEnable;     //���ӱ����Ƿ���ʾ
		bool d_ashy;				 //�����Ƿ���ɫ
		bool d_selected;				 //�����Ƿ��ѡ��״̬
		bool d_haveSelectedState;    //�����Ƿ���Ա�ѡ��
		bool d_lock;				//�����Ƿ�����
		const Image* d_image;		//�����ϵ�ͼƬ
		const Image* d_backimage;		//�����ϵı���ͼ
		const Image* d_colorimage;		//װ������ɫͼ
		const Image* d_cornerimage;		//���½ǵı�־ͼ
		
		void onMouseEnters(MouseEventArgs& e);
		void onMouseLeaves(MouseEventArgs& e);
		void onMouseButtonDown(MouseEventArgs& e);

		ItemCellGeneralTextUnit m_TextUnits[ItemCellGeneralTextType_NUM];
		int d_CellTypeMask;    //�������͵��ж�����
		bool d_HasFloodLight;  //�Ƿ�������Ч��
		bool d_ImageAutoScale; //ͼƬ�Ƿ��Զ�����

		//void onMouseButtonUp(MouseEventArgs& e);
		//void onMouseButtonDown(MouseEventArgs& e);

		//<<==============================================
		//��skillbox���Ա���ֺ������
		String	 sourceBoxName;
		int d_generalID;
		int d_keyID;				//����keyid
		int64_t d_skillDueDate;		//���ܵĵ���ʱ��
		bool d_pickupinbattle;		//ս�����ܷ�ʰȡ
		//>>================================================

		GeneralType d_generalType;	//����˵��(��tips��ʾ��)
    };

} // End of  CEGUI namespace section


#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	
