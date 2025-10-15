/***********************************************************************
filename:   FalGroupBtnTree.h
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
#ifndef _FalGroupBtnTree_h_
#define _FalGroupBtnTree_h_

#include "FalModule.h"
#include "../../CEGUIWindowFactory.h"
#include "../../elements/CEGUIGroupBtnTree.h"

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
    class FALAGARDBASE_API FalagardGroupBtnTree : public WindowRenderer
    {
    public:
        static const utf8   TypeName[];       //!< type name for this widget.

        /*!
        \brief
            Constructor
        */
        FalagardGroupBtnTree(const String& type, const String& name);
		FalagardGroupBtnTree(const String& type);
		FalagardGroupBtnTree(){}

        /*!
        \brief
            Destructor
        */
        ~FalagardGroupBtnTree();
		std::vector<String> Split(const String& str, const String& delims);
		String getMaskImage() const;
		void setMaskImage(const String& maskImage);

    public:
        Rect getTreeRenderArea(void) const;
		void render();

		virtual void clone(const WindowRenderer* templateRenderer)
		{
			WindowRenderer::clone(templateRenderer);
			const FalagardGroupBtnTree* t = dynamic_cast<const FalagardGroupBtnTree*>(templateRenderer);
			assert(t);
			mImagesetName0 = t->mImagesetName0;
			mImageName0 = t->mImageName0;
			mImagesetName1 = t->mImagesetName1;
			mImageName1 = t->mImageName1;
		}

	private:
		String		mImagesetName0;
		String		mImageName0;
		String		mImagesetName1;
		String		mImageName1;
	};

} // End of  CEGUI namespace section


#endif  // end of guard _FalTree_h_
