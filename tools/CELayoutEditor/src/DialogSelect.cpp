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

#include "CELayoutEditor.h"
#include "DialogSelect.h"
#include "Config.h"
#include <algorithm>
#include <map>
#include <wx/dir.h>
#include <wx/filename.h>
//////////////////////////////////////////////////////////////////////////
// DEFINES
//////////////////////////////////////////////////////////////////////////

#define BORDER            5

//////////////////////////////////////////////////////////////////////////
// TYPEDEFS
//////////////////////////////////////////////////////////////////////////

#if wxCHECK_VERSION(2,8,0)
    typedef wxTreeItemIdValue cookieType;
#else
    typedef long cookieType;
#endif

//////////////////////////////////////////////////////////////////////////
// DIALOGADDWINDOW FUNCTIONS
//////////////////////////////////////////////////////////////////////////

//--------------------------------------------------------------------------------
	BEGIN_EVENT_TABLE(DialogSelect, wxDialog)
// Button-click handler(s)
//EVT_TREE_SEL_CHANGED(ID_WIDGET_TREE, DialogSelect::OnSelchangedWidgetTree)
EVT_BUTTON(wxID_OK, DialogSelect::OnOK)
EVT_BUTTON(wxID_CANCEL, DialogSelect::OnCancel)
EVT_LISTBOX_DCLICK(ID_WIDGET_TREE, DialogSelect::OnListBoxSel)
EVT_TEXT(ID_TEXT, DialogSelect::OnChangeText)
END_EVENT_TABLE()

//------------------------------------------------------------------------
// Shared
wxString DialogSelect::m_previousLooknfeel;

//////////////////////////////////////////////////////////////////////////
// DIALOGADDWINDOW FUNCTIONS
//////////////////////////////////////////////////////////////////////////


//------------------------------------------------------------------------
	DialogSelect::DialogSelect(wxWindow* aParentDialog, const wxArrayString&  choices) :
m_windowName(),
m_windowType(),

// Put the elements to NULL, so we can block events during initialization
m_editName(wx_static_cast(wxTextCtrl*, NULL)),
m_widgetTree(wx_static_cast(wxListBox*, NULL)),
m_choices(choices),
// Initially we assume a filtered display (only those that are not dependent on parents) 
// of available widgets

// Store parent window for later use (may be NULL!)

// Put the elements to NULL, so we can block events during initialization
m_filterBox(wx_static_cast(wxRadioBox*, NULL)),
m_okButton(wx_static_cast(wxButton*, NULL)),
wxDialog(aParentDialog, wxID_ANY, wxT("select Window"), wxDefaultPosition, wxSize(350, 700))
{
    // NOTE: Prefer constructor initialization lists (which are 
    // often more efficient) to assignment inside the 
    // constructor. Members in the initialization list should 
    // appear in the order they are declared in the class, as 
    // this is the order they will be initialized (no matter what).
    // See item 4 in 'Effective C++ 3rd Edition' by Scott Meyers
    // Initialize dialog
    Setup();
}

//-----------------------------------------------------------------------
void DialogSelect::Setup()
{
    // All "groups". Put the parent's name + type in the text field, when a parent is set
    wxStaticBox* const parentGroup = new wxStaticBox(this, wxID_ANY, wxT("select"));
    // All static fields
    wxStaticText* const staticName = new wxStaticText(this, wxID_ANY, wxT("please input name"));
	wxStaticBox* const staticWidgets = new wxStaticBox(this, wxID_ANY, wxT("piease select one file"));
    // All edit fields. Default the name to "parentName/"
	m_editName = new wxTextCtrl(this, ID_TEXT, wxT(""));


    // Size the components vertically
    wxBoxSizer* const rootSizer = new wxBoxSizer(wxVERTICAL);

    // Sizes the tree - this one may be expanded
    // NOTE: No need to free the custodial pointer parentGroup since
    // it will be handled internally by wxWidgets wxStaticBoxSizer::wxStaticBoxSizer()
    wxStaticBoxSizer* const nameSizer = new wxStaticBoxSizer(parentGroup, wxVERTICAL);

    // NOTE: No need to free the custodial pointer staticName since
    // it will be handled internally by wxWidgets wxSizer::Add()
    (void)nameSizer->Add(staticName, 0, wxEXPAND | wxALL, BORDER);
    (void)nameSizer->Add(m_editName, 0, wxEXPAND | wxALL, BORDER);

    // Add to the root sizer (may not stretch)
    // NOTE: No need to free the custodial pointer nameSizer since
    // it will be handled internally by wxWidgets wxSizer::Add()
    (void)rootSizer->Add(nameSizer, 0, wxEXPAND | wxALL, BORDER);

	wxStaticBoxSizer* const treeSizer = new wxStaticBoxSizer(staticWidgets, wxVERTICAL);
	m_widgetTree = new wxListBox(this, ID_WIDGET_TREE, wxDefaultPosition, wxDefaultSize, m_choices);
	
	wxString currentFile(Options::GetInstancePtr()->GetCurrentLayout());
	if (!currentFile.empty())
	{
		for (int i = 0; i < m_choices.size(); i++)
		{
			if (m_choices[i] == currentFile)
			{
				m_widgetTree->SetSelection(i);
				break;
			}
		}
	}

	(void)treeSizer->Add(m_widgetTree, 1, wxEXPAND | wxALL, BORDER);

	// Add to the root sizer (may not stretch)
	// NOTE: No need to free the custodial pointer treeSizer since
	// it will be handled internally by wxWidgets wxSizer::Add()
	(void)rootSizer->Add(treeSizer, 1, wxEXPAND | wxALL, BORDER);
    //wxBoxSizer* const buttonSizer = new wxBoxSizer(wxHORIZONTAL);
    //m_okButton = new wxButton(this, wxID_OK, wxT("Ok"));
    //// Disable until a widget gets selected.
    //(void)m_okButton->Enable(false);
    //(void)buttonSizer->Add(m_okButton, 0, wxEXPAND | wxALL, BORDER);
    //(void)buttonSizer->Add(new wxButton(this, wxID_CANCEL, wxT("Cancel")), 0, wxEXPAND | wxALL, BORDER);

    //// Add to the root sizer (may stretch)
    //// NOTE: No need to free the custodial pointer buttonSizer since
    //// it will be handled internally by wxWidgets wxSizer::Add()
    //(void)rootSizer->Add(buttonSizer, 0, wxEXPAND | wxCENTER, BORDER);

    // Initially we assume a filtered display (only those that are not dependent on parents) 
    // of available widgets

    // Apply root sizer
    // NOTE: No need to free the custodial pointer rootSizer since
    // it will be handled internally by wxWidgets wxWindowBase::SetSizer()
    SetSizer(rootSizer);
    // Build up the widget list to choose from

    // NB: We should not call 'Show' on a wxDialog we later call ShowModal for.
    //(void)wxDialog::Show(true);	

    // Somehow the initial name value gets selected by default, which is annoying cause if you start typing, you overwrite the selection.
    // Therefore we pull this trick, required after Show.
    m_editName->SetFocus();
    m_editName->SetInsertionPointEnd();
}

//------------------------------------------------------------------------
void DialogSelect::InitWidgetTree()
{
    // Validations
    wxASSERT_MSG(m_widgetTree != NULL, wxT("Widget tree invalid or NULL"));

    const CEGUI::String separator("/");
	std::map<CEGUI::String, wxTreeItemId> looks;
    std::map<CEGUI::String, wxTreeItemId>::iterator lookIter;

    // If the tree is already populated (probably by a previous 
    // call to this function), clear it so that we avoid duplicate data and
    // errors because of duplicate root nodes (only one per tree allowed)
	if (!m_widgetTree->IsEmpty())
	{
		m_widgetTree->Clear();
	}

	for (int ii = 0; ii < m_choices.Count(); ii++){
		m_widgetTree->Insert(m_choices.Item(ii), ii);
	}
	//m_widgetTree = new wxListBox(this, ID_WIDGET_TREE, wxDefaultPosition, wxDefaultSize, m_choices);
    // Build up the widget tree. (Not recursive.)

}

//------------------------------------------------------------------------

//------------------------------------------------------------------------
bool DialogSelect::Filter(const CEGUI::String& aWidgetType, const CEGUI::String& aWidgetName, const CEGUI::Window* const aParentWindow) const
{
    // Check for NULL
	if (!aParentWindow)
	{	// Allow alot everything since the new window is the first one
		return !RequiresParent (aWidgetName);	// Check with the name!
	}
    
	// When the parent is a MenuBar or a PopupMenu, we may only add MenuItems
    if (aParentWindow->testClassName("Menubar")/* || aParentWindow->testClassName("PopupMenu")*/)
    {
        return (aWidgetType == "MenuItem");
    }
    // We never explicitly add PopupMenus
    // When the parent is a MenuItem, we have two options:
    // 1. When the item does not have a PopupMenu yet, we may only add a PopupMenu
    // 2. When the item already has a PopupMenu, we may only add a MenuItem. This works in combination with
    //  DialogMain::InitLayoutTree (the PopupMenu skipping part) and the EditorDocument::AddNewWindow 
    // (the step-into PopupMenu part).
    if (aParentWindow->testClassName("MenuItem"))
    {
        /*if (!((MenuItem*)aParentWindow)->getPopupMenu())
        {
        return (aWidget == "PopupMenu");
        }*/
        return (aWidgetType == "MenuItem");
    }
    // When the parent is a TabControl, we may only add Default windows
    if (aParentWindow->testClassName("TabControl"))
    {
        //return (aWidget == "DefaultGUISheet");
        return (aWidgetType == "DefaultWindow");
    }

    // Are we to filter the displayed widgets by their parent requirement?
    if(m_treeFilter)
        // Last test: only allow widgets who make sense on their own.
        return !RequiresParent (aWidgetName);
    else
        // No parent filter required here
        return true;
}

//------------------------------------------------------------------------
bool DialogSelect::RequiresParent(const CEGUI::String& windowType) const
{
	// Although the filtering is now based on a widget's 'base' type, this method
	// needs some rework too. This method will let widgets pass when they are not logically expected
	// to have a required parent. E.g. a ComboDropList requires a ComboBox parent.
	// It does not filter on automatic windows, such as the sample just given ;)
	//LogDebug (wxT("%s"), StringHelper::ToWXString(windowType));
    if (windowType.find("ComboDropList") != CEGUI::String::npos ||
        windowType.find("ComboEditBox") != CEGUI::String::npos ||
        windowType.find("HorizontalScrollbarThumb") != CEGUI::String::npos ||
        windowType.find("LargeVerticalScrollbar") != CEGUI::String::npos ||
        windowType.find("LargeVerticalScrollbarThumb") != CEGUI::String::npos ||
        windowType.find("MenuItem") != CEGUI::String::npos ||
        windowType.find("PopupMenu") != CEGUI::String::npos ||
        windowType.find("SliderThumb") != CEGUI::String::npos ||
        windowType.find("TabButton") != CEGUI::String::npos ||
        windowType.find("TabButtonPane") != CEGUI::String::npos ||
        windowType.find("TabContentPane") != CEGUI::String::npos ||   // Auto window for a TabControl (?)
        windowType.find("Tooltip") != CEGUI::String::npos ||
        windowType.find("VerticalScrollbarThumb") != CEGUI::String::npos ||
        windowType.find("VUMeter") != CEGUI::String::npos)
    {
        //LogDebug (wxT("%s requires a parent."), StringHelper::ToWXString(windowType));
        return true;
    }
    return false;
}

//------------------------------------------------------------------------
void DialogSelect::OnOK(wxCommandEvent& WXUNUSED(event))
{
    // Validations
    wxASSERT_MSG(m_editName != NULL, wxT("Edit name invalid or NULL"));

    // Copy the values from the controls (for some reason doesn't work when closed)
    m_windowName = m_editName->GetValue();
    // We already got the type in the tree handler
    // Pass to superclass
#if wxCHECK_VERSION(2,8,0)
    // new way of returning
    AcceptAndClose();
#else
    wxDialog::OnOK(event);
    // Hide to pass control back to caller
    Show(false);
#endif
}
bool DialogSelect::GetFileNames(const wxString& path, const wxString& wildcard, wxArrayString& filenames) const
{
	filenames.clear();
	if (!path.IsEmpty())
	{	// Find all layouts
		wxDir::GetAllFiles(path, &filenames, wildcard, wxDIR_FILES);
	}
	return filenames.GetCount() > 0;
}
void DialogSelect::OnChangeText(wxCommandEvent& event){
	wxArrayString	filenames;
	wxArrayString	choices;
	size_t			index;

	// by bayaer, 
	wxString currentFile(Options::GetInstancePtr()->GetCurrentLayout());
	//wxString currentFile(L"thename");
	//wxString currentFileA(L"thesome");
	//int num = currentFile.Cmp(currentFile);

	int currentID = -1;

	// Call helper
	Options* const opt = Options::GetInstancePtr();
	wxString path = wxGetCwd() + opt->GetPathToLayouts();
	// We accept .layout and .xml

	// by bayaer,
	wxString aa = m_editName->GetValue() + wxT("*.layout");
	GetFileNames(path, wxT("*") + m_editName->GetValue() + wxT("*.layout"), filenames);
	for (index = 0; index < filenames.GetCount(); index++)
	{
		//filenames[index]
		size_t startPos = path.size();
		size_t strSize = filenames[index].size() - startPos;
		wxString nCmpStr = filenames[index];
		size_t endPos = filenames[index].find_last_of(L"\\") + 1;
		nCmpStr = nCmpStr.substr(endPos);
		//nCmpStr = filenames[index].replace(startPos, strSize,);
		if (nCmpStr.Cmp(currentFile) == 0)
		{
			currentID = index;
			break;
		}
	}
	filenames.Clear();

	if (GetFileNames(path, wxT("*")+ m_editName->GetValue() + wxT("*.layout"), filenames))
	{
		for (index = 0; index < filenames.GetCount(); index++)
		{
			choices.Add(wxFileName(filenames[index]).GetFullName());
		}
	}
	if (GetFileNames(path, wxT("*")+ m_editName->GetValue() + wxT("*.xml"), filenames))
	{
		for (index = 0; index < filenames.GetCount(); index++)
		{
			choices.Add(wxFileName(filenames[index]).GetFullName());
		}
	}
	if (choices.GetCount() > 0)
	{	// Sort, because we appended from two arrays
		choices.Sort();

	}
	m_choices = choices;
	InitWidgetTree();
}
//------------------------------------------------------------------------
void DialogSelect::OnCancel(wxCommandEvent& WXUNUSED(event))
{
#if wxCHECK_VERSION(2,8,0)
    // this is no longer supported
    EndDialog(wxID_CANCEL);
#else
    wxDialog::OnCancel(event);
    Show(false);
#endif
}

//------------------------------------------------------------------------
void DialogSelect::OnListBoxSel(wxCommandEvent& event) 
{
	int ii = event.GetSelection();
	m_selectLayout = m_choices.Item(ii);
	AcceptAndClose();
    // Check type of filter selected
    // NOTE: The comparison is used to avoid the annoying warning C4800 
    //       int' : forcing value to bool 'true' or 'false' (performance warning)

    // Rebuild widget tree to take in account the current filter
}
