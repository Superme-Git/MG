/***********************************************************************
filename: 	CEGUIMessageTip.h
purpose:	透明提示框
*************************************************************************/

#ifndef _CEGUIMessageTip_h_
#define _CEGUIMessageTip_h_

#include <vector> //yanji 20150922
#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "CEGUIRichEditbox.h"


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	enum TipType
	{
		eMsgTip = 1, //透明消息框，会在消息频道里打出
		eSystemTip = 2, //系统公告，会在系统消息频道打出
	};
	/*!
	\brief
	Base class for Tooltip window renderer objects.
	*/

	/*!
	\brief
	Base class for all the MessageTip type widgets 
	*/
	class CEGUIEXPORT MessageTip : public Window
	{
		
	public:		
		/*************************************************************************
		Constants
		*************************************************************************/
		static const String WidgetTypeName;                 //!< Window factory name
		static const String EventNamespace;                 //!< Namespace for global events
		static const String RichEditboxNameSuffix;          //!< Widget name suffix for the editbox component.
		static const String EventStartFade;
		/*************************************************************************
		Construction and Destruction
		*************************************************************************/
		/*!
		\brief
		Constructor for MessageTip objects
		*/
		MessageTip(const String& type, const String& name);


		/*!
		\brief
		Destructor for MessageTip objects
		*/
		virtual ~MessageTip(void);

		virtual void	initialiseComponents(bool bClone);

		RichEditbox* getRichEditbox() const;
		
		Size		getTextSize() const;

		void		SetDestYPos(float height) { d_destYPos = height;}

		float		GetDestYPos() { return d_destYPos;}

		bool		GetTextureIsLoading();

		void        SetTipsType(TipType type);// { d_tipType = type;}
		TipType     GetTipType(){ return d_tipType;}

		void		InitSysMsgParamter();

		//yanji 20150921
		void		SetStartYPos(float yPos) { d_startYPos = yPos; }
		float		GetStartYPos() { return d_startYPos; }
		void		SetDisplayTime(float fDisplayTime) { d_displayTime = fDisplayTime; }
		float		GetHeightScale() { return d_fHeightScale; }
		void		SetHeightScale(float fHeightScale) { d_fHeightScale = fHeightScale; }

	public:
		MessageTip(const String& type);
		const MessageTip& operator=(const MessageTip& t);
		virtual Window* clone(Window* wnd);
		virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

	protected:
		/*************************************************************************
		  Overridden from Window.
		*************************************************************************/
		virtual void	onMouseClicked(MouseEventArgs& e);
		virtual void	onTextChanged(WindowEventArgs& e);
		
		void			onStartFade(WindowEventArgs& e);
		bool	        editbox_MouseDownHandler(const EventArgs& e);
		
		void			sizeSelf(void);
		void            updateSelf(float elapsed);
		
		
		/*!
		\brief
		Return whether this window was inherited from the given class name at some point in the inheritance hierarchy.

		\param class_name
		The class name that is to be checked.

		\return
		true if this window was inherited from \a class_name. false if not.
		*/
		virtual bool	testClassName_impl(const String& class_name) const
		{
			if (class_name=="MessageTip")	return true;
			return Window::testClassName_impl(class_name);
		}
		
		


		/*************************************************************************
		Implementation Data
		*************************************************************************/
		float		d_destYPos;   //目标高度
		float		d_decHeight;     //每帧减少的高度
		float       d_elapsed;      //!< Used to track state change timings
		float       d_displayTime;  //!< tool-tip display time (seconds that tip is showsn for).
		float       d_fadeTime;     //!< tool-tip fade time (seconds it takes for tip to fade in and/or out).
		TipType		d_tipType; //类型
		bool		d_startFade;
		static const float d_ConstWidth;//固定宽度
		static const float d_ConstHeight;//固定高度
		static const float d_SysTipHeight;//系统公告的高度
		//yanji 20150921 起始高度
		float		d_startYPos;
		float		d_fHeightScale;
		
	};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIButtonBase_h_
