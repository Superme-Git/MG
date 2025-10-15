/***********************************************************************
filename:   FalTree.h
created:	5-13-07
author:		Jonathan Welch (Based on Code by David Durant)
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
#ifndef _FalItemTable_h_
#define _FalItemTable_h_

#include "FalModule.h"
#include "../../CEGUIWindowFactory.h"
#include "../../elements/CEGUIItemTable.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    /*!
    \brief
        Tree class for the FalagardBase module.

        This class requires LookNFeel to be assigned.  The LookNFeel should provide the following:

        States:
            - Enabled
            - Disabled

        Named Areas:
            - ItemRenderingArea
            - ItemRenderingAreaHScroll
            - ItemRenderingAreaVScroll
            - ItemRenderingAreaHVScroll

        Child Widgets:
            Scrollbar based widget with name suffix "__auto_vscrollbar__"
            Scrollbar based widget with name suffix "__auto_hscrollbar__"
    */
    class FALAGARDBASE_API FalagardItemTable : public WindowRenderer
    {
    public:
		static const utf8   TypeName[];       //!< type name for this widget.

        /*!
        \brief
            Constructor
        */
        FalagardItemTable(const String& type, const String& name);
		FalagardItemTable(const String& type);
		FalagardItemTable(){}

        /*!
        \brief
            Destructor
        */
		~FalagardItemTable();
		virtual void clone(const WindowRenderer* templateRenderer){ WindowRenderer::clone(templateRenderer); }
    protected:
        Rect getTableRenderArea(void) const;
		void render();
    };

} // End of  CEGUI namespace section


#endif  // end of guard _FalTree_h_
