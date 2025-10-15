/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxComponent_h_
#define _CEGUIRichEditboxComponent_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "../CEGUIFont.h"
#include "CEGUIRichEditboxProperties.h"


#include <vector>


//#if defined(_MSC_VER)
//#	pragma warning(push)
//#	pragma warning(disable : 4251)
//#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	enum RichEditboxComponentType
	{
		RichEditboxComponentType_None,
		RichEditboxComponentType_Text,
		RichEditboxComponentType_Image,
		RichEditboxComponentType_Break,   //����
		RichEditboxComponentType_LinkText,   //���ı�����
		RichEditboxComponentType_GoToText,   //�Զ�Ѱ·����
		RichEditboxComponentType_Emotion,       //����
		RichEditboxComponentType_Tips,         //��Ʒ�����ܣ�����������
		RichEditboxComponentType_NameLink,         //�����������
		RichEditboxComponentType_ButtonImage,         //��ťͼƬ
		RichEditboxComponentType_FamilyRecruitLink,         //������ļ����
		RichEditboxComponentType_HttpAddress,   //��ҳ����
		RichEditboxComponentType_JoinTeam,	//�������
		RichEditboxComponentType_RequestTeam,	//���������Ϣ by yq 2016-03-25
		RichEditboxComponentType_AnswerQuestion,	//����
		RichEditboxComponentType_OpenDialog,  //��dialog����
		RichEditboxComponentType_CommonLink	//ͨ�����ӣ�����ƴ���ַ���

	};
	class CEGUIEXPORT RichEditboxComponent:  public EventSet 
	{
	public:
		//! Destructor.
		
		static const String EventNamespace;
		static const String EventMouseButtonDown;			//!< The read-only mode for the edit box has been changed.

		static const String EventMouseEnter; //mouse�����¼�
		static const String EventMouseLeave; //mouse�뿪�¼�

		static const String EventGuideEnd; //���������¼�



		RichEditboxComponent();

		virtual ~RichEditboxComponent(){}

		//! Copy constructor.
		 RichEditboxComponent(const RichEditboxComponent& other);
		//! Assignment.
		 RichEditboxComponent& operator=(const RichEditboxComponent& rhs);

		//! draw the component.
		virtual void draw(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect,
			const float vertical_space,
			const float space_extra,
			bool bTextBorder) {}  //btextborderΪ�����Ƿ���� 

		virtual void DrawSelectFlag(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect,
			const Image* pSeleImage=NULL,
			size_t selectStartIdx=0,size_t selectEndIdx=0);

		virtual void DrawMouseOnFlag(GeometryBuffer* buffer, const Vector2& position,
			const ColourRect* mod_colours,
			const Rect* clip_rect);

		

		//! return the pixel size of the rendered component.
		virtual Size getPixelSize() { return Size(0.0f,d_LineSpace); }
        
        virtual Size getDrawPixelSize() { return getPixelSize(); }
		//! return whether the component can be split
		virtual bool canSplit() {return false;}

		virtual RichEditboxComponent* split(float split_point,
			bool first_component) {return NULL;}

		//! clone this component.
		virtual RichEditboxComponent* clone()  {return NULL;}

		//! return the total number of spacing characters in the string.
		virtual size_t getSpaceCount() {return 0;}

		RichEditboxComponentType GetType() const{ return d_Type; }

		virtual Point GetOffsetOfIndex(size_t index);
		virtual size_t GetMaxIndex(){return 1;}
		virtual size_t GetIndexByWidth( float width);
		virtual bool HandleBackSpace(size_t offset){ return true; }  //����true����ɾ����ǰ��component
		virtual  RichEditboxComponent* HandleChar(size_t offset,const String& InsetText,const ColourRect& ColorRect=ColourRect()){ return NULL; }   //����true���������µ�textcomponent
		virtual  RichEditboxComponent* InsertComponent(size_t offset,RichEditboxComponent* pComponent){ return NULL; }   //����true���������µ�component


		virtual String GenerateParseText(){ return String("");}
		virtual String GeneratePureText(size_t startPos,size_t endPos){ return ""; }
		bool IsMouseOn() { return d_bIsMouseOn; }

		//��øÿؼ���ֺ��µĹ��λ�ã�curidx::��ǰλ�� newcaratIdx�µĹ��λ�� ����true������¹�����ڲ�ֺ���¿ؼ���
		virtual bool GetNewCaratPos(float width,size_t curIdx,size_t& newCaratIdx);
        
        virtual bool isPushed() { return false; }

		

		
    protected:
		//! Protected constructor.
		Rect d_padding;
		bool d_bIsMouseOn;
		RichEditboxComponentType d_Type;
	public:
		virtual	bool    onMouseButtonDown(MouseEventArgs& e); 
		virtual bool	onMouseButtonUp(MouseEventArgs& e){ return false; } //����true˵���ӿؼ�״̬�б仯����Ҫredraw
		virtual	void	onMouseDoubleClicked(MouseEventArgs& e){ return; }
		virtual void	onMouseMove(MouseEventArgs& e){  return; }
		virtual void    onMouseEnter();
		virtual void    onMouseLeave();

		virtual bool  updata(float fElapse) { return false; }

		void* GetUserData() { return d_UserData; }
		void  SetUserData( void* pData )
		{
			d_UserData=pData;
		}

		void SetUserID(int64_t id) { d_UserID = id;}
		int64_t GetUserID(){ return d_UserID;}

		void SetLineSpace(float fLineSpace) { d_LineSpace=fLineSpace;  }
		float GetLineSpace() { return d_LineSpace; }

		virtual int GetCharCount() { return 1; } //����ַ�����

		virtual size_t GetCharNumByIndex(size_t index) { return 1; }

		virtual bool isOnlySpace() { return false; } //ֻ��ֻ�п��ַ�

		void SetHasGuide(bool bGuide) { d_HasGuide=bGuide; }

		virtual bool isTextType() { return false; } //�Ƿ����������͵��ӿؼ�

		static void InitSpecialChar(); //��ʼ������xmlת���ַ�
		static void ReplaceSpecialChar(String& text);  //�滻����xmlת���ַ�

		static std::map<String,String> d_mapSpecialChar; //����xmlת���ַ�

	private:
		
		void* d_UserData;

		int64_t d_UserID;//
		float d_LineSpace;  //�м��
		bool d_HasGuide;  //�Ƿ�������


	};
}

//#if defined (_MSC_VER)
//#	pragma warning(pop)
//#endif

#endif // end of guard _CEGUICentredRenderedString_h_
