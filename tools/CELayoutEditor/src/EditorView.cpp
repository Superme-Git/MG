///////////////////////////////////////////////////////////////////////////////
//  For project details and authors, refer to "CELayoutEditor.cpp".
//
//  This file is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; either version 2
//  of the License, or (at your option) any later version.
//
//  This file is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
//  To view the licence online, go to: http://www.gnu.org/copyleft/gpl.html
////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////
// INCLUDES
//////////////////////////////////////////////////////////////////////////

#include "pch.h"

#include "EditorView.h"
#include "CELayoutEditor.h"
#include "EditorCanvas.h"

#include <wx/filename.h>


//////////////////////////////////////////////////////////////////////////
// EDITORVIEW IMPLEMENTATION
//////////////////////////////////////////////////////////////////////////

IMPLEMENT_DYNAMIC_CLASS(EditorView, wxView)

//////////////////////////////////////////////////////////////////////////
// EDITORVIEW FUNCTIONS
//////////////////////////////////////////////////////////////////////////

//------------------------------------------------------------------------
EditorView::EditorView() :
m_frame(wx_static_cast(EditorFrame*, NULL)),
m_renderCanvas(wx_static_cast(EditorCanvas*, NULL)),
m_background(wx_static_cast(CEGUI::GeometryBuffer*, NULL)),
wxView()
{ 
    // NOTE: Prefer constructor initialization lists (which are 
    // often more efficient) to assignment inside the 
    // constructor. Members in the initialization list should 
    // appear in the order they are declared in the class, as 
    // this is the order they will be initialized (no matter what).
    // See item 4 in 'Effective C++ 3rd Edition' by Scott Meyers
}

//------------------------------------------------------------------------
bool EditorView::IsBackgroundAvailable() const
{
    return (m_background != NULL);
}

//------------------------------------------------------------------------
void EditorView::InitBackground()
{
    // First call?
    if (!m_background)
    {
        try
        {
            m_background = &CEGUI::System::getSingleton().getRenderer()->createGeometryBuffer();
			// Apply last stored settings
			SetBackgroundImage(Options::GetInstancePtr()->GetCurrentBackground());
        }
        // If something goes wrong, show user
        SHOW_EXCEPTION("EditorView::InitBackground", "Error", "Unexpected standard exception while creating background widget", "Unexpected non-standard exception while creating background widget");
    }
}

//------------------------------------------------------------------------
void EditorView::SetBackgroundImage(const wxString& fullpath)
{   // Only process when a file is given
    if (!fullpath.IsEmpty())
    {   // Remove current imageset. This call just silently bails out when not found.
        CEGUI::ImagesetManager::getSingleton().destroy("BackgroundImage");
        CEGUI::DefaultResourceProvider* const rp = wx_static_cast(CEGUI::DefaultResourceProvider*, CEGUI::System::getSingleton().getResourceProvider());
        const CEGUI::String currentImagesetsDirectory = rp->getResourceGroupDirectory("imagesets");
        try
        {
            // Just to be sure.
            InitBackground();
            // Okay, behold... because ImageSets get loaded from the predefined resource group,
            // we change it for the moment.
            wxFileName filename(fullpath);
            const wxString path = filename.GetPath() + wxT("/");
            const wxString file = filename.GetFullName();
            rp->setResourceGroupDirectory("imagesets", StringHelper::ToCEGUIString(path));
            (void)CEGUI::ImagesetManager::getSingleton().
                    createFromImageFile("BackgroundImage",
                                        StringHelper::ToCEGUIString(file));
            // Store this setting (full path this time, because this method will be called for it)
            Options::GetInstancePtr()->SetCurrentBackground(fullpath);

			UpdateBackground();
			DrawBackground();
			// Always make visible, otherwise the user wouldn't be picking a background image
            SetBackgroundVisibility(true);
        }
        // If something goes wrong, show user
        SHOW_EXCEPTION("EditorView::SetBackgroundImage", "Error",
                       "Unexpected standard exception while setting background image",
                       "Unexpected non-standard exception while setting background image");

        // Restore (also after an exception!)
        rp->setResourceGroupDirectory("imagesets", currentImagesetsDirectory);
    }
}

//------------------------------------------------------------------------
void EditorView::SetBackgroundVisibility(bool visible) 
{
    // Validations
    wxASSERT_MSG(m_background != NULL, wxT("Background is empty or invalid!"));

    if (m_background)
    {
        // Store this setting
        Options::GetInstancePtr()->SetBackgroundVisible(visible);

        if (CEGUI::System::getSingletonPtr())
		{
            CEGUI::System::getSingleton().signalRedraw();
			wxGetApp().GetMainFrame()->Refresh();
		}
    }
}

//------------------------------------------------------------------------
void EditorView::DrawBackground()
{
    if (Options::GetInstancePtr()->IsBackgroundVisible())
        m_background->draw();
}

//------------------------------------------------------------------------
void EditorView::UpdateBackground()
{
    if (!m_background || !CEGUI::ImagesetManager::getSingleton().isDefined("BackgroundImage"))
        return;

    CEGUI::Rect dest(0, 0, 0, 0);
    dest.setSize(CEGUI::System::getSingleton().getRenderer()->getDisplaySize());
    m_background->setClippingRegion(dest);

    try
    {
        CEGUI::Imageset& bgis = CEGUI::ImagesetManager::getSingleton().get("BackgroundImage");
        // Pre-draw the image into the geometry buffer ready for rendering
        bgis.getImage("full_image").draw(*m_background, dest, static_cast<CEGUI::Rect*>(0));
    }
    // If something goes wrong, show user
    SHOW_EXCEPTION("EditorView::UpdateBackground", "Error",
                   "Unexpected standard exception while updating background image",
                   "Unexpected non-standard exception while updating background image");
}

//------------------------------------------------------------------------
bool EditorView::OnCreate(wxDocument *document, long WXUNUSED(flags) )
{
    // Single-window mode
    m_frame = wxGetApp().GetMainFrame();
    m_renderCanvas = m_frame->GetCanvas();
    m_renderCanvas->setView (this);

    // Associate the appropriate frame with this view.
    SetFrame(m_frame);

    // Tell the frame about the document
    m_frame->SetDocument(wx_static_cast(EditorDocument*, document));

    UpdateFrameTitle();

    // initialise support for background imagery
    InitBackground();

    // Make sure the document manager knows that this is the
    // current view.
    wxView::Activate(true);

	gDocument = wx_static_cast(EditorDocument*, document);

    return true;
}

//------------------------------------------------------------------------
bool EditorView::OnClose(bool WXUNUSED(deleteWindow))
{
    if (!GetDocument()->Close())
    {
        return false;
    }

    if(m_renderCanvas)
    {
        m_renderCanvas->ClearBackground();
        m_renderCanvas->setView (wx_static_cast(wxView*, NULL));
        m_renderCanvas = wx_static_cast(EditorCanvas*, NULL);
    }
    SetFrame(wx_static_cast(wxFrame*, NULL));

    // Tell the frame about the document
    if(m_frame)
    {
        m_frame->SetDocument(wx_static_cast(EditorDocument*, NULL));
        m_frame = wx_static_cast(EditorFrame*, NULL);
    }

	// Cleanup the background buffer
	if (m_background)
	{
		m_background->reset();
		CEGUI::System::getSingleton().getRenderer()->destroyGeometryBuffer(*m_background);
		m_background = wx_static_cast(CEGUI::GeometryBuffer*, NULL);
	}

    wxView::Activate(false);

    UpdateFrameTitle();

    return true;
}

//------------------------------------------------------------------------
void EditorView::OnUpdate(wxView* WXUNUSED(sender), wxObject* WXUNUSED(hint))
{
    if (m_frame && m_frame->IsShown() && m_renderCanvas)
    {
        //LogDebug(wxT("Needs repaint."));
        m_renderCanvas->Refresh(false);
        m_renderCanvas->Render();
    }
}

//------------------------------------------------------------------------
void EditorView::OnChangeFilename()
{
    UpdateFrameTitle();
}

//------------------------------------------------------------------------
void EditorView::UpdateFrameTitle()
{

    wxString title (wxTheApp->GetAppName());

    if (GetDocument())
    {
        title << wxT(" - [") << GetDocument()->GetFilename() << wxT("]");
    }
    if (m_frame)
    {
        m_frame->SetTitle(title);
    }
}

//------------------------------------------------------------------------
void EditorView::OnDraw(wxDC* WXUNUSED(dc))
{
}
