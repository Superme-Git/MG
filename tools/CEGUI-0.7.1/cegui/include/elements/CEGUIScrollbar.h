/***********************************************************************
	filename: 	CEGUIScrollbar.h
	created:	13/4/2004
	author:		Paul D Turner
	
	purpose:	Interface to base class for Scrollbar widget
*************************************************************************/
/***************************************************************************
 *   Copyright (C) 2004 - 2006 Paul D Turner & The CEGUI Development Team
 *
 *   Permission is hereby granted, free of charge, to any person obtaining
 *   a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including
 *   without limitation the rights to use, copy, modify, merge, publish,
 *   distribute, sublicense, and/or sell copies of the Software, and to
 *   permit persons to whom the Software is furnished to do so, subject to
 *   the following conditions:
 *
 *   The above copyright notice and this permission notice shall be
 *   included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 *   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *   OTHER DEALINGS IN THE SOFTWARE.
 ***************************************************************************/
#ifndef _CEGUIScrollbar_h_
#define _CEGUIScrollbar_h_

#include "../CEGUIBase.h"
#include "../CEGUIWindow.h"
#include "CEGUIScrollbarProperties.h"


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif
;

// Start of CEGUI namespace section
namespace CEGUI
{

enum enumSlideState
{
    StopState,
    SlideState,
    BackState,
    LockState
    
};
class CEGUIEXPORT ScrollbarWindowRenderer : public WindowRenderer
{
public:
    ScrollbarWindowRenderer(const String& name);
	ScrollbarWindowRenderer(){}

    virtual void    updateThumb(void)   = 0;

    virtual float   getValueFromThumb(void) const   = 0;

    virtual float   getAdjustDirectionFromPoint(const Point& pt) const  = 0;

	virtual bool    isThumbOnEnd(void)   = 0;

	virtual void clone(const WindowRenderer* templateRenderer){ WindowRenderer::clone(templateRenderer); }
};

class CEGUIEXPORT Scrollbar : public Window
{
public:
	static const String EventNamespace;				//!< Namespace for global events
    static const String WidgetTypeName;             //!< Window factory name

	static const String EventScrollPositionChanged;		//!< Name of the event fired when the scroll bar position value changes
	static const String EventThumbTrackStarted;			//!< Name of the event fired when the user begins dragging the thumb.
	static const String EventThumbTrackEnded;				//!< Name of the event fired when the user releases the thumb.
	static const String EventScrollConfigChanged;			//!< Name of the event fired when the scroll bar configuration data changes.
    static const String EventScrollbarEnd;
    static const String EventSlideStopped;

    static const String ThumbNameSuffix;            //!< Widget name suffix for the thumb component.
    static const String IncreaseButtonNameSuffix;   //!< Widget name suffix for the increase button component.
    static const String DecreaseButtonNameSuffix;   //!< Widget name suffix for the decrease button component.

	float	getDocumentSize(void) const			{return d_documentSize;}
    
    void    setClickEnable(bool enable) {d_ClickEnable = enable;};
    const bool getClickEnable() {return d_ClickEnable;};


	float	getPageSize(void) const				{return d_pageSize;}

	enumSlideState getSlidState() {return m_SlideState;}

	float	getStepSize(void) const				{return d_stepSize;}


	float	getOverlapSize(void) const			{return d_overlapSize;}


	float	getScrollPosition(void) const		{return d_position;}

	    PushButton* getIncreaseButton() const;

    PushButton* getDecreaseButton() const;


    Thumb* getThumb() const;


	virtual void initialiseComponents(bool bClone);

	void	setDocumentSize(float document_size);


	void	setPageSize(float page_size);


	void	setStepSize(float step_size);


	void	setOverlapSize(float overlap_size);

	void	setScrollPosition(float position,bool checkPos=true);

	bool    isThumbOnEnd(); 

	Scrollbar(const String& type, const String& name);

	virtual ~Scrollbar(void);
    
    void SetVeloctity(float v) { d_velocity=v; }
    float GetVeloctity() { return d_velocity; }
    void SetAcceration(float a) { d_acceleration=a; }
    
    void Lock();
    void Stop();
    void Free();
    static void SetDefaultAcceleration(float a) { d_DefultAcceleration=a; }
    
    virtual	void	onMouseSlide(MouseEventArgs& e);
    virtual	bool	onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer);
    void Slide();
    void Back();
    void End();
    
    void EnbalePanGuesture(bool bEnable) { d_PanGuestureEnable=bEnable; }
    
    void SetPanForVert(bool bForVert) { d_PanForVert=bForVert; }
    
    void SetParentScrollPane(CEGUI::Window* w) {d_parentScrollPane = w;}


public:
	Scrollbar(const String& type);
	const Scrollbar& operator=(const Scrollbar& t);
	virtual Window* clone(Window* wnd);
	virtual bool onRenameTemplatePrefix(const String& sPrefix){ return Window::onRenameTemplatePrefix(sPrefix); }

protected:
	
	void	updateThumb(void);

	float	getValueFromThumb(void) const;

	float	getAdjustDirectionFromPoint(const Point& pt) const;


    /*!
    \brief
        update the size and location of the thumb to properly represent the current state of the scroll bar
    */
    //virtual void    updateThumb_impl(void)   = 0;


    /*!
    \brief
        return value that best represents current scroll bar position given the current location of the thumb.

    \return
        float value that, given the thumb widget position, best represents the current position for the scroll bar.
    */
    //virtual float   getValueFromThumb_impl(void) const   = 0;


    /*!
    \brief
        Given window location \a pt, return a value indicating what change should be 
        made to the scroll bar.

    \param pt
        Point object describing a pixel position in window space.

    \return
        - -1 to indicate scroll bar position should be moved to a lower value.
        -  0 to indicate scroll bar position should not be changed.
        - +1 to indicate scroll bar position should be moved to a higher value.
    */
    //virtual float   getAdjustDirectionFromPoint_impl(const Point& pt) const  = 0;


	/*!
	\brief
		handler function for when thumb moves.
	*/
	bool	handleThumbMoved(const EventArgs& e);


	/*!
	\brief
		handler function for when the increase button is clicked.
	*/
	bool	handleIncreaseClicked(const EventArgs& e);


	/*!
	\brief
		handler function for when the decrease button is clicked.
	*/
	bool	handleDecreaseClicked(const EventArgs& e);


	/*!
	\brief
		handler function for when thumb tracking begins
	*/
	bool	handleThumbTrackStarted(const EventArgs& e);


	/*!
	\brief
		handler function for when thumb tracking begins
	*/
	bool	handleThumbTrackEnded(const EventArgs& e);


	/*!
	\brief
		handler function for when scrollbar position changed
	*/
	bool	handleScrollPositionChanged(const EventArgs& e);

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
		if (class_name=="Scrollbar")	return true;
		return Window::testClassName_impl(class_name);
	}

    // validate window renderer
    virtual bool validateWindowRenderer(const String& name) const
    {
        return (name == "Scrollbar");
    }

	/*************************************************************************
		New event handlers for slider widget
	*************************************************************************/
	/*!
	\brief
		Handler triggered when the scroll position changes
	*/
	virtual void	onScrollPositionChanged(WindowEventArgs& e);


	/*!
	\brief
		Handler triggered when the user begins to drag the scroll bar thumb. 
	*/
	virtual void	onThumbTrackStarted(WindowEventArgs& e);


	/*!
	\brief
		Handler triggered when the scroll bar thumb is released
	*/
	virtual void	onThumbTrackEnded(WindowEventArgs& e);


	/*!
	\brief
		Handler triggered when the scroll bar data configuration changes
	*/
	virtual void	onScrollConfigChanged(WindowEventArgs& e);

    virtual void    onScrollbarEnd(WindowEventArgs& e);

	/*************************************************************************
		Overridden event handlers
	*************************************************************************/
	virtual void	onMouseButtonDown(MouseEventArgs& e);
	virtual	void	onMouseWheel(MouseEventArgs& e);
    
    virtual void    updateSelf(float elapsed);


    float getWeakenRatio(float pos);
    
	/*************************************************************************
		Implementation Data
	*************************************************************************/
	float	d_documentSize;		//!< The size of the document / data being scrolled thorugh.
	float	d_pageSize;			//!< The size of a single 'page' of data.
	float	d_stepSize;			//!< Step size used for increase / decrease button clicks.
	float	d_overlapSize;		//!< Amount of overlap when jumping by a page.
	float	d_position;			//!< Current scroll position.
    
    float   d_velocity;
    float   d_acceleration;
    bool    d_Lock;
    static  float d_DefultAcceleration;
    float   d_TotalSlideTime;
    bool    d_ClickEnable;
    CEGUI::Window* d_parentScrollPane;


private:
	/*************************************************************************
		Static Properties for this class
	*************************************************************************/
	static ScrollbarProperties::DocumentSize	d_documentSizeProperty;
	static ScrollbarProperties::PageSize		d_pageSizeProperty;
	static ScrollbarProperties::StepSize		d_stepSizeProperty;
	static ScrollbarProperties::OverlapSize		d_overlapSizeProperty;
	static ScrollbarProperties::ScrollPosition	d_scrollPositionProperty;

    
	/*************************************************************************
		Private methods
	*************************************************************************/
	void	addScrollbarProperties(void);
    bool    isTheSameDir();
    bool    isInertiaEnd();
    void    EndInertia();
    enumSlideState m_SlideState;
    float   m_BackElapseTime;
    float   m_SlideElapseTime;
    float   m_SlideStartPos;
    float   m_SlideDstPos;
    
    bool    d_PanGuestureEnable;
    bool    d_PanForVert;

	float	m_Offset;
	bool	d_StopStep;
	float	m_ticktime;
};

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif	// end of guard _CEGUIScrollbar_h_
