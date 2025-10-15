/***********************************************************************
    filename:   FalStaticText.h
    created:    Tue Jul 5 2005
    author:     Paul D Turner <paul@cegui.org.uk>
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
#ifndef _FalStaticText_h_
#define _FalStaticText_h_

#include "FalModule.h"
#include "FalStatic.h"
#include "FalStaticTextProperties.h"
#include "../../falagard/CEGUIFalEnums.h"
#include "../../CEGUIColourRect.h"
#include <vector>

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
    /*!
    \brief
        StaticText class for the FalagardBase module.

        This class requires LookNFeel to be assigned.  The LookNFeel should provide the following:

        States:
            - Enabled                     - basic rendering for enabled state.
            - Disabled                    - basic rendering for disabled state.
            - EnabledFrame                - frame rendering for enabled state
            - DisabledFrame               - frame rendering for disabled state.
            - WithFrameEnabledBackground  - backdrop rendering for enabled state with frame enabled.
            - WithFrameDisabledBackground - backdrop rendering for disabled state with frame enabled.
            - NoFrameEnabledBackground    - backdrop rendering for enabled state with frame disabled.
            - NoFrameDisabledBackground   - backdrop rendering for disabled state with frame disabled.

        Named Areas (missing areas will default to 'WithFrameTextRenderArea'):
            WithFrameTextRenderArea
            WithFrameTextRenderAreaHScroll
            WithFrameTextRenderAreaVScroll
            WithFrameTextRenderAreaHVScroll
            NoFrameTextRenderArea
            NoFrameTextRenderAreaHScroll
            NoFrameTextRenderAreaVScroll
            NoFrameTextRenderAreaHVScroll
    */
    class FALAGARDBASE_API FalagardStaticText : public FalagardStatic
    {
    public:
        static const utf8   TypeName[];       //!< type name for this widget.

        /*************************************************************************
            Child Widget name suffix constants
        *************************************************************************/
        static const String VertScrollbarNameSuffix;   //!< Widget name suffix for the vertical scrollbar component.
        static const String HorzScrollbarNameSuffix;   //!< Widget name suffix for the horizontal scrollbar component.

        /************************************************************************
            Constructor / Destructor
        *************************************************************************/
        /*!
        \brief
            Constructor
        */
		FalagardStaticText(){};
        FalagardStaticText(const String& type);

        //! Destructor.
        ~FalagardStaticText();

        /************************************************************************
            Scrolled text implementation
        *************************************************************************/
        /*!
        \brief
            Return a ColourRect object containing the colours used when rendering this widget.
        */
        ColourRect    getTextColours(void) const                {return d_textCols;}

        /*!
        \brief
            Return the current horizontal formatting option set for this widget.
        */
        HorizontalTextFormatting getHorizontalFormatting(void) const   {return    d_horzFormatting;}

        /*!
        \brief
            Return the current vertical formatting option set for this widget.
        */
        VerticalTextFormatting getVerticalFormatting(void) const     {return d_vertFormatting;}

        /*!
        \brief
            Sets the colours to be applied when rendering the text.
        */
        void    setTextColours(const ColourRect& colours) ;

        /*!
        \brief
            Set the vertical formatting required for the text.
        */
        void    setVerticalFormatting(VerticalTextFormatting v_fmt);

        /*!
        \brief
            Set the horizontal formatting required for the text.
        */
        void    setHorizontalFormatting(HorizontalTextFormatting h_fmt);

        /*!
        \brief
            Return whether the vertical scroll bar is set to be shown if needed.
        */
        bool    isVerticalScrollbarEnabled(void) const  {return d_enableVertScrollbar;}

        /*!
        \brief
            Return whether the horizontal scroll bar is set to be shown if needed.
        */
        bool    isHorizontalScrollbarEnabled(void) const    {return d_enableHorzScrollbar;}

        /*!
	    \brief
            Set whether the vertical scroll bar will be shown if needed.
        */
        void    setVerticalScrollbarEnabled(bool setting);

        /*!
        \brief
            Set whether the horizontal scroll bar will be shown if needed.
        */
        void    setHorizontalScrollbarEnabled(bool setting);

        //! return the current horizontal formatted text extent in pixels.
        float getHorizontalTextExtent() const;

        //! return the current vertical formatted text extent in pixels.
        float getVerticalTextExtent() const;

        void render(void);

		
		bool isBorder()const { return d_Border; }

		colour GetBorderColour() const{ return d_BorderColor; }
		void SetBorderColour(colour color) ;
		void SetBorderEnable(bool b) ;
		void SetDefaultBorderEnable(bool b);
        void SetDefaultColorEnable(bool b) ;
		bool isDefaultBorderEnable() const { return d_EnableDefaultBorder; }
		
		bool isDefaultColorEnable() const { return d_DefaultColorEnable; }
		bool isTitle() const { return d_isTitle; }
		void SetTitleEnable(bool bTitle);
		
        virtual void update(float elapsed);

		virtual void clone(const WindowRenderer* templateRenderer)
		{
			FalagardStatic::clone(templateRenderer);
			const FalagardStaticText* t = dynamic_cast<const FalagardStaticText*>(templateRenderer);
			assert(t);
			d_horzFormatting = t->d_horzFormatting;
			d_vertFormatting = t->d_vertFormatting;
			d_textCols = t->d_textCols;
			d_enableVertScrollbar = t->d_enableVertScrollbar;
			d_enableHorzScrollbar = t->d_enableHorzScrollbar;
			d_formattedRenderedString = NULL;//t.d_formattedRenderedString可能不为空，没关系，这个东西是在运行状态创建的.
			//d_formattedRenderedString = t->d_formattedRenderedString;
			d_connections = t->d_connections;
			d_formatValid = t->d_formatValid;
			d_Border = t->d_Border;
			d_BorderColor = t->d_BorderColor;
			d_EnableDefaultBorder = t->d_EnableDefaultBorder;
			d_DefaultColorEnable = t->d_DefaultColorEnable;
			d_isTitle = t->d_isTitle;
			d_curTextStatus = t->d_curTextStatus;
		}

    protected:
        //! update string formatting (gets area size to use from looknfeel)
        void updateFormatting() const;
        //! update string formatting using given area size.
        void updateFormatting(const Size& sz) const;

        // overridden from FalagardStatic base class
        void onLookNFeelAssigned();
        void onLookNFeelUnassigned();

        // text field with scrollbars methods
        void renderScrolledText(void);
        void configureScrollbars(bool bCheckOldVerShow=true);
        Scrollbar* getVertScrollbar(void) const;
        Scrollbar* getHorzScrollbar(void) const;
        virtual Rect getTextRenderArea(void) const;
        Size getDocumentSize(const Rect& renderArea) const;
        void setupStringFormatter() const;

        // overridden event handlers
        bool onTextChanged(const EventArgs& e);
        bool onSized(const EventArgs& e);
        bool onFontChanged(const EventArgs& e);
        bool onMouseWheel(const EventArgs& e);

        // event subscribers
        bool handleScrollbarChange(const EventArgs& e);

        // properties
        static FalagardStaticTextProperties::TextColours    d_textColoursProperty;
        static FalagardStaticTextProperties::VertFormatting d_vertFormattingProperty;
        static FalagardStaticTextProperties::HorzFormatting d_horzFormattingProperty;
        static FalagardStaticTextProperties::VertScrollbar  d_vertScrollbarProperty;
        static FalagardStaticTextProperties::HorzScrollbar  d_horzScrollbarProperty;
        static FalagardStaticTextProperties::HorzExtent     d_horzExtentProperty;
        static FalagardStaticTextProperties::VertExtent     d_vertExtentProperty;
		static FalagardStaticTextProperties::BorderEnable     d_BorderEnableProperty;
		static FalagardStaticTextProperties::BorderColour     d_BorderColourProperty;
		static FalagardStaticTextProperties::DefaultBorderEnable     d_DefaultBorderProperty;
		static FalagardStaticTextProperties::DefaultColourEnable     d_DefaultColourProperty;
		static FalagardStaticTextProperties::Title     d_TitleProperty;


        // implementation data
        //! Horizontal formatting to be applied to the text.
        HorizontalTextFormatting d_horzFormatting;
        //! Vertical formatting to be applied to the text.
        VerticalTextFormatting d_vertFormatting;
        mutable  ColourRect      d_textCols;             //!< Colours used when rendering the text.
        bool            d_enableVertScrollbar;  //!< true if vertical scroll bar is enabled.
        bool            d_enableHorzScrollbar;  //!< true if horizontal scroll bar is enabled.

        //! Class that renders RenderedString with some formatting.
        mutable FormattedRenderedString* d_formattedRenderedString;

        typedef std::vector<Event::Connection> ConnectionList;
        ConnectionList  d_connections;

        //! true when string formatting is up to date.
        mutable bool d_formatValid;
		bool d_Border;
		colour d_BorderColor;
		bool d_EnableDefaultBorder;
		bool d_DefaultColorEnable;		
		bool d_isTitle;
    protected:
        virtual const Point getOffset() { return Point(0.0f, 0.0f);}
    private:
        bool d_curTextStatus;
    };

} // End of  CEGUI namespace section

#if defined(_MSC_VER)
#	pragma warning(pop)
#endif

#endif  // end of guard _FalStaticText_h_
