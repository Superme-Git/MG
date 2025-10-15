/***********************************************************************
filename: 	CEGUIGroupButton.h
*************************************************************************/
#ifndef _CEGUIGroupButton_h_
#define _CEGUIGroupButton_h_

#include "../CEGUIBase.h"
#include "CEGUIButtonBase.h"
#include "CEGUIGroupButtonProperties.h"


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	/*!
	\brief
	Base class to provide the logic for Group Button widgets.

	*/
	class CEGUIEXPORT GroupButton : public ButtonBase
	{
	public:
		static const String EventNamespace;				//!< Namespace for global events
		static const String WidgetTypeName;             //!< Window factory name

		/*************************************************************************
		Event name constants
		*************************************************************************/
		// generated internally by Window
		static const String EventSelectStateChanged;			//!< The selected state of the widget has changed.

        enum enumGroupButtonState
		{
			eNormalState,
			eHoverState,
			ePushedState,
			eDisabledState,
			eSelectState
		};
		/*************************************************************************
		Accessor Functions
		*************************************************************************/ 
		/*!
		\brief
		return true if the group button is selected (has the checkmark)

		\return
		true if this widget is selected, false if the widget is not selected.
		*/
		bool	isSelected(void) const				{return d_selected;}


		/*!
		\brief
		return the groupID assigned to this group button

		\return
		ulong value that identifies the Group Button group this widget belongs to.
		*/
		ulong	getGroupID(void) const				{return d_groupID;}


		/*!
		\brief
		Return a pointer to the RadioButton object within the same group as this RadioButton, that
		is currently selected.

		\return
		Pointer to the GroupButton object that is the GroupButton within the same group as this GroupButton,
		and is attached to the same parent window as this GroupButton, that is currently selected.
		Returns NULL if no button within the group is selected, or if 'this' is not attached to a parent window.
		*/
		GroupButton*	getSelectedButtonInGroup(void) const;


		/*************************************************************************
		Manipulator Functions
		*************************************************************************/
		/*!
		\brief
		set whether the radio button is selected or not

		\param select
		true to put the group button in the selected state, false to put the group button in the
		deselected state.  If changing to the selected state, any previously selected group button
		within the same group is automatically deselected.

		\return
		Nothing.
		*/
		void	setSelected(bool select, bool fireEvent = true);


		/*!
		\brief
		set the groupID for this group button

		\param group
		ulong value specifying the radio button group that this widget belongs to.

		\return	
		Nothing.
		*/
		void	setGroupID(ulong group);

		void    setStateTextColour(enumGroupButtonState state,const ColourRect& cl);

		const ColourRect& GetStateColour(enumGroupButtonState state);

		const ColourRect& GetStateColour();

		void    setStateBordcerColour(enumGroupButtonState state,const colour& cl);
		const colour& GetStateBorderColour(enumGroupButtonState state);
		const colour& GetStateBorderColour();

		void SetBorderEnable(bool b) { d_BorderEnable = b; }
		bool isBorderEnable() { return d_BorderEnable; }

		void SetStateImageExtendID(int id) { d_StateImageExtendID = id; }
		int  GetStateImageExtendID() const { return d_StateImageExtendID; }

		/*************************************************************************
		Construction / Destruction
		*************************************************************************/
		GroupButton(const String& type, const String& name);
		virtual ~GroupButton(void);

public:
		GroupButton(const String& type);
		const GroupButton& operator=(const GroupButton& t);
		virtual Window* clone(Window* wnd);
		virtual bool onRenameTemplatePrefix(const String& sPrefix){ return ButtonBase::onRenameTemplatePrefix(sPrefix); }

	protected:
		/*************************************************************************
		Implementation Functions
		*************************************************************************/
		/*!
		\brief
		Deselect any selected group buttons attached to the same parent within the same group
		(but not do not deselect 'this').
		*/
		void	deselectOtherButtonsInGroup(void) const;


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
			if (class_name=="RadioButton")	return true;
			return ButtonBase::testClassName_impl(class_name);
		}


		/*************************************************************************
		New Radio Button Events
		*************************************************************************/
		/*!
		\brief
		event triggered internally when the select state of the button changes.
		*/
		virtual void	onSelectStateChanged(WindowEventArgs& e);


		/*************************************************************************
		Overridden Event handlers
		*************************************************************************/
		virtual void	onMouseButtonUp(MouseEventArgs& e);
        
        virtual void	onClicked(WindowEventArgs& e);


		/*************************************************************************
		Implementation Data
		*************************************************************************/
		bool		d_selected;				// true when radio button is selected (has checkmark)
		ulong		d_groupID;				// radio button group ID

		ColourRect  d_TextNormalColourRect;
		ColourRect  d_TextHoverColourRect;
		ColourRect  d_TextPushedColourRect;
		ColourRect  d_TextDisableColourRect;
		ColourRect  d_TextSelectColourRect;
		
		colour      d_NormalBorderColour; //描边颜色
		colour      d_HoverBorderColour;
		colour      d_PushedBorderColour;
		colour      d_DisableBorderColour;
		colour      d_SelectBorderColour;

		bool        d_BorderEnable; //是否描边
		char        d_StateImageExtendID; //当前状态的图片换成加上扩展ID的图片,0:不换

		void initialiseComponents(bool bClone);  //设置完外观后设置各种状态的文字颜色
	private:
		/*************************************************************************
		Static Properties for this class
		*************************************************************************/
		static GroupButtonProperties::Selected	d_selectedProperty;
		static GroupButtonProperties::GroupID	d_groupIDProperty;
		static GroupButtonProperties::StateImageExtendID d_stateImageExtendIDProperty;
		static GroupButtonProperties::EnableClickAni d_enableClickAniProperty;

		/*************************************************************************
		Private methods
		*************************************************************************/
		void	addGroupButtonProperties(void);
	};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIRadioButton_h_
