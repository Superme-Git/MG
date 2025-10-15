/***********************************************************************
filename:   FalGroupBtnTree.cpp
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
#include "FalGroupBtnTree.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "CEGUIWindowManager.h"
#include "elements/CEGUIScrollbar.h"
#include "elements/CEGUIPanelbox.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardGroupBtnTree::TypeName[] = "Falagard/GroupBtnTree";

	std::vector<String> FalagardGroupBtnTree::Split(const String& str, const String& delims)
	{
		std::vector<String> ret;

		if (str.empty())
			return ret;

		// Use STL methods
		size_t start, pos;
		start = 0;
		do
		{
			pos = str.find_first_of(delims, start);
			if (pos == start)
			{
				// Do nothing
				start = pos + 1;
			}
			else if (pos == String::npos)
			{
				// Copy the rest of the string
				ret.push_back(str.substr(start));
				break;
			}
			else
			{
				// Copy up to delimiter
				ret.push_back(str.substr(start, pos - start));
				start = pos + 1;
			}
			// parse up to next real data
			start = str.find_first_not_of(delims, start);

		} while (pos != String::npos);

		return ret;
	}


    FalagardGroupBtnTree::FalagardGroupBtnTree(const String& type, const String&) :
        WindowRenderer(type)
    {
    }

    FalagardGroupBtnTree::FalagardGroupBtnTree(const String& type) :
        WindowRenderer(type)
    {
    }

    FalagardGroupBtnTree::~FalagardGroupBtnTree()
    {
    }

	String FalagardGroupBtnTree::getMaskImage() const
	{
		String strRet;
		if (d_window->getType().find("GroupBtnTree") != String::npos)
		{
			if (!mImagesetName0.empty() && !mImageName0.empty() && !mImagesetName1.empty() && mImageName1.empty())
			{
				strRet = mImagesetName0 + "," + mImageName0 + "," + mImagesetName1 + "," + mImageName1;
			}
		}
		return strRet;
	}

	void FalagardGroupBtnTree::setMaskImage(const String& maskImage)
	{
		if (d_window->getType().find("GroupBtnTree") != String::npos)
		{
			std::vector<String> splits = Split(maskImage, ",");
			if (splits.size() != 4)
			{
				return;
			}

			String imagesetName0 = splits[0];
			String imageName0 = splits[1];
			String imagesetName1 = splits[2];
			String imageName1 = splits[3];

			GroupBtnTree* tree = (GroupBtnTree*)d_window;
			tree->SetMaskImage(0, imagesetName0, imageName0);
			tree->SetMaskImage(1, imagesetName1, imageName1);
		}
	}

    Rect FalagardGroupBtnTree::getTreeRenderArea(void) const
    {
        // get WidgetLookFeel for the assigned look.
        const WidgetLookFeel& wlf = getLookNFeel();
		
		bool v_visible = false;
		if(d_window->getType().find("GroupBtnTree")!=String::npos)
		{
			GroupBtnTree* tree = (GroupBtnTree*)d_window;
			v_visible = tree->getVertScrollbar()->isVisible(true);
		}
		else if(d_window->getType().find("Panelbox")!=String::npos)
		{
			 Panelbox* panel = (Panelbox*)d_window;
			v_visible = panel->getVertScrollbar()->isVisible(true);
		}		
     
        // if either of the scrollbars are visible, we might want to use another text rendering area
        if (v_visible)
        {
            String area_name("ItemRenderingArea");
            if (v_visible)
            {
                area_name += "V";
            }
            area_name += "Scroll";

            if (wlf.isNamedAreaDefined(area_name))
            {
                return wlf.getNamedArea(area_name).getArea().getPixelRect(*d_window);
            }
        }

        // default to plain ItemRenderingArea
        return wlf.getNamedArea("ItemRenderingArea").getArea().getPixelRect(*d_window);
    }

	void FalagardGroupBtnTree::render()
	{
		 Rect rect = getTreeRenderArea();
		 const WidgetLookFeel& wlf = getLookNFeel();
		 const StateImagery* imagery;
		 if(d_window->getType().find("GroupBtnTree")!=String::npos)
		 {
			 GroupBtnTree* tree = (GroupBtnTree*)d_window;
			 //Set the render area for this.
			 tree->setItemRenderArea(rect);

			 //Get the Falagard imagery to render
			 imagery = &wlf.getStateImagery(tree->isDisabled()? "Disabled" : "Enabled");
			 //Render the window
			 imagery->render(*tree);
			 //Fix Scrollbars
			 tree->doScrollbars();
			 //Render the tree.
			 tree->doTreeRender();
		 }
		 else if(d_window->getType().find("Panelbox")!=String::npos)
		 {
			 Panelbox* panel = (Panelbox*)d_window;
			 //Set the render area for this.
			 panel->setItemRenderArea(rect);

			 //Get the Falagard imagery to render
			 imagery = &wlf.getStateImagery(panel->isDisabled()? "Disabled" : "Enabled");
			 //Render the window
			 imagery->render(*panel);
			 //Fix Scrollbars
			 panel->doScrollbars();
			 //Render the tree.
			 panel->doPanelRender();
		 }
		 
	}
} // End of  CEGUI namespace section
