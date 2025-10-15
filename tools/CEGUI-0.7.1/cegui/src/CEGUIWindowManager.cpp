/***********************************************************************
	filename: 	CEGUIWindowManager.cpp
	created:	21/2/2004
	author:		Paul D Turner

	purpose:	Implements the WindowManager class
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
#include "CEGUIWindowManager.h"
#include "CEGUIWindowFactoryManager.h"
#include "CEGUIAnimationManager.h"
#include "CEGUIAnimationInstance.h"
#include "CEGUIWindowFactory.h"
#include "CEGUIWindow.h"
#include "CEGUIExceptions.h"
#include "CEGUIGUILayout_xmlHandler.h"
#include "CEGUIXMLParser.h"
#include "elements/CEGUIGUISheet.h"
#include <iostream>
#include <sstream>
#include "Utils/StringUtil.h"
#ifdef _WIN32
#include "utils/FileUtil.h"
#endif

#include "CEGUIPfsResourceProvider.h"

#ifndef FORCEGUIEDITOR
#include "cocos2d.h"
#endif

#include "BinLayout/CEGUIBinLayoutFileDefine.h"
#include "BinLayout/CEGUIMemoryStream.h"
#include "BinLayout/CEGUIBinLayoutFileSerializer.h"
#include "XMLParserModules/LJXMLParser/CEGUILJXMLParser.h"
#include "CEGUICocos2DRenderer.h"

// Start of CEGUI namespace section
namespace CEGUI
{
/*************************************************************************
	Static Data Definitions
*************************************************************************/
// singleton instance pointer
template<> WindowManager* Singleton<WindowManager>::ms_Singleton	= 0;
// default resource group
String WindowManager::d_defaultResourceGroup;

/*************************************************************************
	Definition of constant data for WindowManager
*************************************************************************/
// Declared in WindowManager
const char	WindowManager::GUILayoutSchemaName[]	= "GUILayout.xsd";
const String WindowManager::GeneratedWindowNameBase("__cewin_uid_");
const String WindowManager::EventNamespace("WindowManager");
const String WindowManager::EventWindowCreated("WindowCreated");
const String WindowManager::EventWindowDestroyed("WindowDestroyed");
    

/*************************************************************************
    Constructor
*************************************************************************/
WindowManager::WindowManager(void) :
    d_uid_counter(0),
    d_lockCount(0),
	d_layoutLoading(0)
{
	d_cacheLayouts.clear();
    char addr_buff[32];
    sprintf(addr_buff, "(%p)", static_cast<void*>(this));
    Logger::getSingleton().logEvent(
        "CEGUI::WindowManager singleton created " + String(addr_buff));
}


/*************************************************************************
	Destructor
*************************************************************************/
WindowManager::~WindowManager(void)
{
	destroyAllWindows();
    cleanDeadPool();

    char addr_buff[32];
    sprintf(addr_buff, "(%p)", static_cast<void*>(this));
    Logger::getSingleton().logEvent(
        "CEGUI::WindowManager singleton destroyed " + String(addr_buff));
}


/*************************************************************************
	Create a new window of the specified type
*************************************************************************/
Window* WindowManager::createWindow(const String& type, const String& name)
{
#if (defined WIN7_32) && (defined _DEBUG)
	std::wstring strName = PFSResourceProvider::GUIStringToWString(name);
#endif

    // only allow creation of Window objects if we are in unlocked state
    if (isLocked())
        throw InvalidRequestException("WindowManager::createWindow - "
                                      "WindowManager is in the locked state.");

	String finalName(name.empty() ? generateUniqueWindowName() : name);
	if (isWindowPresent(finalName))
	{
		CEGUI_LOGERR("WindowManager::createWindow - A Window object with the name '" + type + finalName +"' already exists within the system.\n");
		throw AlreadyExistsException("WindowManager::createWindow - A Window object with the name '" + type + finalName + "' already exists within the system.");
	}

    WindowFactoryManager& wfMgr = WindowFactoryManager::getSingleton();
    WindowFactory* factory = wfMgr.getFactory(type);

    Window* newWindow = factory->createWindow(finalName);

    // see if we need to assign a look to this window
    if (wfMgr.isFalagardMappedType(type))
    {
        const WindowFactoryManager::FalagardWindowMapping& fwm = wfMgr.getFalagardMappingForType(type);
        // this was a mapped type, so assign a look to the window so it can finalise
        // its initialisation
        newWindow->d_falagardType = type;
        newWindow->setWindowRenderer(fwm.d_rendererType);
        newWindow->setLookNFeel(fwm.d_lookName,false);
    }

	{
		core::CMutex::Scoped autoLock(mMutex);
		d_windowRegistry[finalName] = newWindow;
	}

    // fire event to notify interested parites about the new window.
    WindowEventArgs args(newWindow);
	fireEvent(EventWindowCreated, args, EventNamespace);
#ifdef _WIN32
	{
		core::CMutex::Scoped autoLock(mMutex);
		d_lastCreateWindowName = newWindow->getName();
	}
#endif
	return newWindow;
}

/*************************************************************************
	Destroy the given window by pointer
*************************************************************************/
void WindowManager::destroyWindow(Window* window)
{
	if (!window) return;
    AnimationManager* animMgr = &AnimationManager::getSingleton();

	if (window)
	{
		// this is done because the name is used for the log after the window is destroyed,
		// if we just did getName() we would get a const ref to the Window's internal name
		// string which is destroyed along with the window so wouldn't exist when the log tried
		// to use it (as I soon discovered).
		String name = window->getName();

		destroyWindow(name);
	}

}


/*************************************************************************
	Destroy the given window by name
*************************************************************************/
void WindowManager::destroyWindow(const String& window)
{
#if (defined WIN7_32) && (defined _DEBUG)
	std::wstring strWindowName = PFSResourceProvider::GUIStringToWString(window);
#endif

	Window* wnd = NULL;
	{
		core::CMutex::Scoped autoLock(mMutex);
		WindowRegistry::iterator wndpos = d_windowRegistry.find(window);
		if (wndpos != d_windowRegistry.end())
		{
			wnd = wndpos->second;
			// remove entry from the WindowRegistry.
			d_windowRegistry.erase(wndpos);
		}
	}

	if (wnd)
	{
        // do 'safe' part of cleanup
        wnd->destroy();

        // add window to dead pool
        d_deathrow.push_back(wnd);

        // notify system object of the window destruction
        System::getSingleton().notifyWindowDestroyed(wnd);
#if (defined WIN7_32) && (defined _DEBUG)
        char addr_buff[32];
        sprintf(addr_buff, "(%p)", static_cast<void*>(wnd));
        Logger::getSingleton().logEvent("Window '" + window + "' has been "
            "added to dead pool. " + addr_buff, Informative);
#endif
        // fire event to notify interested parites about window destruction.
        // TODO: Perhaps this should fire first, so window is still usable?
        WindowEventArgs args(wnd);
        fireEvent(EventWindowDestroyed, args, EventNamespace);
	}

}


/*************************************************************************
	Return a pointer to the named window
*************************************************************************/
Window* WindowManager::getWindow(const String& name) const
{
#if (defined WIN32) && (defined _DEBUG)
	std::string na(name.c_str());
#endif

	core::CMutex::Scoped autoLock(mMutex);

	WindowRegistry::const_iterator pos = d_windowRegistry.find(name);

	if (pos == d_windowRegistry.end())
	{
		CEGUI_LOGERR("WindowManager::getWindow - A Window object with the name '" + name +"' does not exist within the system\n");
		//throw UnknownObjectException("WindowManager::getWindow - A Window object with the name '" + name +"' does not exist within the system");//ÆÁ±ÎÖ±½ÓÅ×Òì³£
		return NULL;
	}

	return pos->second;
}


/*************************************************************************
Return a pointer to the named window
*************************************************************************/
Window* WindowManager::getWindowInDeadPool(const String& name) const
{
#if (defined WIN32) && (defined _DEBUG)
	std::string na(name.c_str());
#endif
	WindowVector::const_iterator it = d_deathrow.begin();
	while (it != d_deathrow.begin())
	{
		if ((*it)->getName() == name)
		{
			return *it;
		}
	}
	return NULL;
}

/*************************************************************************
	Return true if a window with the given name is present
*************************************************************************/
bool WindowManager::isWindowPresent(const String& name) const
{
	core::CMutex::Scoped autoLock(mMutex);
	return (d_windowRegistry.find(name) != d_windowRegistry.end());
}


/*************************************************************************
	Destroy all Window objects
*************************************************************************/
void WindowManager::destroyAllWindows(void)
{
	WindowRegistry tmpWindowRegistry;
	{
		core::CMutex::Scoped autoLock(mMutex);
		tmpWindowRegistry = d_windowRegistry;
	}
	
	for (WindowRegistry::iterator it = tmpWindowRegistry.begin(); it != tmpWindowRegistry.end(); ++it)
	{
		const String& window_name = it->first;
		destroyWindow(window_name);
	}
}

static std::map<String, Window*> s_WindowTemplate;

/*************************************************************************
	Creates a set of windows (a Gui layout) from the information in the
	specified XML file.
*************************************************************************/
Window* WindowManager::loadWindowLayout(const String& fileName, const String& name_prefix, const String& resourceGroup, PropertyCallback* callback, void* userdata)
{
	std::string filename;
	static int asciiOffset = 'A' - 'a';
	for (size_t i = 0; i < fileName.size(); ++i)
	{
		char c = fileName[i];
		if (c >= 'A'&&c <= 'Z')
			filename += c - asciiOffset;
		else
			filename += c;
	}
	
	setLoading(true);

	if (filename.empty())
	{
		//throw InvalidRequestException("WindowManager::loadWindowLayout - Filename supplied for gui-layout loading must be valid.");
		CEGUI_LOGERR("WindowManager::loadWindowLayout - Filename is null supplied for gui-layout loading must be valid.");
		return NULL;
	}
	// log the fact we are about to load a layout
	Logger::getSingleton().logEvent("---- Beginning loading of GUI layout from '" + filename + "' ----", Informative);
	
	bool cacheLayout = d_cacheLayouts.find(filename) != d_cacheLayouts.end();

	if (cacheLayout)
	{
		Window* rootWnd = NULL;

		bool bItEqualEnd = false;
		{
			core::CMutex::Scoped autoLock(mMutex);
			std::map<String, Window*>::iterator it = s_WindowTemplate.find("@template@" + filename);
			bItEqualEnd = (it == s_WindowTemplate.end());
			if (!bItEqualEnd)
			{
				rootWnd = it->second;
			}
		}
		
		if (bItEqualEnd)
		{
			rootWnd = loadWindowLayoutFromFile(fileName, "@template@", resourceGroup, callback, userdata);

			{
				core::CMutex::Scoped autoLock(mMutex);
				s_WindowTemplate["@template@" + filename] = rootWnd;
			}
		}

		// log the completion of loading
		Logger::getSingleton().logEvent("---- Successfully completed loading of GUI layout from '" + filename + "' ----", Standard);
	
		Window* cloneWnd = cloneWindow(rootWnd, name_prefix);
		setLoading(false);

		return cloneWnd;
	}
	else
	{
		Window* pWindow = loadWindowLayoutFromFile(filename, name_prefix, resourceGroup, callback, userdata);
		setLoading(false);

		return pWindow;
	}

	return NULL;
}

Window* WindowManager::cloneWindow(Window* rootWnd, const String& name_prefix/* = ""*/)
{
	Window* cloneWnd = rootWnd->clone(NULL);
	bool renameResult = cloneWnd->renameTemplatePrefix(name_prefix);
	assert(renameResult);
	bool onRenameResult = cloneWnd->onRenameTemplatePrefix(name_prefix);
	assert(onRenameResult);
	cloneWnd->onSetTemplateLookNFeel();
	cloneWnd->onZChange_impl();

	return cloneWnd;
}

Window* WindowManager::cloneWindowFromTemplate(const String& filename, const String& name_prefix/* = ""*/)
{
	Window* rootWnd = NULL;

	{
		core::CMutex::Scoped autoLock(mMutex);
		String templateFilename = "@template@" + filename;
		std::map<String, Window*>::iterator it = s_WindowTemplate.find(templateFilename);
		if (it != s_WindowTemplate.end())
		{
			rootWnd = it->second;
		}
	}
	
	if (rootWnd)
	{
		return cloneWindow(rootWnd, name_prefix);
	}

	return NULL;
}

Window* WindowManager::loadWindowLayoutFromFile(const String& filename, const String& name_prefix/* = ""*/, const String& resourceGroup/* = ""*/, PropertyCallback* callback/* = 0*/, void* userdata/* = 0*/)
{
	CEGUI::PFSResourceProvider* rp = static_cast<CEGUI::PFSResourceProvider*>(System::getSingleton().getResourceProvider());
	std::wstring pfsfilename = rp->GetPFSFileName(filename, resourceGroup.empty() ? d_defaultResourceGroup : resourceGroup);
	char* fileBuf = NULL;
	std::streamsize ss = 0;
	if (OpenFromFile(pfsfilename, fileBuf, ss) != 0)
	{
		//throw FileIOException("WindowManager: an error occurred while opening the layout file '" + filename + "' - check it for potential errors!.");
		CEGUI_LOGERR("WindowManager: an error occurred while opening the layout file '" + filename + "' - check it for potential errors!.\n");
		return NULL;
	}

	bool isBinLayout = (ss >= 4 && 0 == memcmp(fileBuf, BinLayout::LAYOUT_BIN_FILE_MAGIC, 4));

	if (isBinLayout)
	{
		BinLayout::MemoryStream stream(fileBuf, ss, true);
		BinLayout::BinLayoutFileSerializer serializer;
		int version = -1;
		PropertyReceiver* pRet = NULL;
		BinLayout::Serializer::Context context = { name_prefix, callback, userdata };
		serializer.read(stream, version, &pRet, &context);

		return dynamic_cast<Window*>(pRet);
	}
	else
	{
		GUILayout_xmlHandler handler(name_prefix, callback, userdata);
		try
		{
			System::getSingleton().getXMLParser()->parseXMLFileBuf(handler, fileBuf, ss);
			delete[] fileBuf;
			fileBuf = NULL;
		}
		catch (...)
		{
			throw;
		}

		return handler.getLayoutRootWindow();
	}

	return NULL;
}

void WindowManager::addTemplateWindow(const String& filename, Window* rootWnd)
{
	if (rootWnd)
	{
		core::CMutex::Scoped autoLock(mMutex);
		s_WindowTemplate["@template@" + filename] = rootWnd;
	}
}

void WindowManager::setLoading(bool val)
{
	core::CMutex::Scoped autoLock(mMutex);
	if (val)
	{
		++d_layoutLoading;
	}
	else
	{
		if (--d_layoutLoading < 0)
		{
			d_layoutLoading = 0;
		}
	}
}

bool WindowManager::isDeadPoolEmpty(void) const
{
    return d_deathrow.empty();
}

void WindowManager::cleanDeadPool(void)
{
#if defined(WIN7_32) && defined(_CHECKCACSH_)
	std::map<Window*, String>::iterator it = d_deathrowName.begin();
	while (it != d_deathrowName.end())
	{
		if (IsBadReadPtr(it->first,4))
			CEGUI_LOGINSANE("deadpool invalid window " + it->second.c_str());
		else
			CEGUI_LOGINSANE("deadpool Window " + it->second.c_str());
		++it;
	}
#endif

	////
    WindowVector::reverse_iterator curr = d_deathrow.rbegin();
    for (; curr != d_deathrow.rend(); ++curr)
    {
// in debug mode, log what gets cleaned from the dead pool (insane level)
#if defined(WIN7_32) && defined (_DEBUG)
		std::wstring strWindowName = PFSResourceProvider::GUIStringToWString((*curr)->getName());
        CEGUI_LOGINSANE("Window '" + (*curr)->getName() + "' about to be finally destroyed from dead pool.");
#endif
#ifdef _WIN32
		std::multimap<unsigned int, WindowManager::StatInfo>::iterator itStat = this->d_Statlayouts.begin();
		while (itStat != d_Statlayouts.end())
		{
			if ((*curr)->getName() == itStat->second.name)
			{
				itStat = d_Statlayouts.erase(itStat);
			}
			else
				++itStat;
		}
#endif
        WindowFactory* factory = WindowFactoryManager::getSingleton().getFactory((*curr)->getType());
        factory->destroyWindow(*curr);

    }

    // all done here, so clear all pointers from dead pool
    d_deathrow.clear();
}

void WindowManager::writeWindowLayoutToStream(const Window& window, OutStream& out_stream, bool writeParent) const
{
#ifdef FORCEGUIEDITOR
    XMLSerializer xml(out_stream);
    // output GUILayout start element
    xml.openTag("GUILayout");
    // see if we need the parent attribute to be written
    if ((window.getParent() != 0) && writeParent)
    {
        xml.attribute("Parent", window.getParent()->getName());
    }
    // write windows
    window.writeXMLToStream(xml);
    // write closing GUILayout element
    xml.closeTag();
#endif
}

void WindowManager::writeWindowLayoutToStream(const String& window, OutStream& out_stream, bool writeParent) const
{
#ifdef FORCEGUIEDITOR
    writeWindowLayoutToStream(*getWindow(window), out_stream, writeParent);
#endif
}

String WindowManager::generateUniqueWindowName()
{
	core::CMutex::Scoped autoLock(mMutex);

    // build name
    std::ostringstream uidname;
    uidname << GeneratedWindowNameBase.c_str() << d_uid_counter;

    // update counter for next time
    unsigned long old_uid = d_uid_counter;
    ++d_uid_counter;

    // log if we ever wrap-around (which should be pretty unlikely)
    if (d_uid_counter < old_uid)
        Logger::getSingleton().logEvent("UID counter for generated window names has wrapped around - the fun shall now commence!");

    // return generated name as a CEGUI::String.
    return String(uidname.str());
}

void WindowManager::setupCacheLayout(const char* layoutName)
{
	d_cacheLayouts.insert(layoutName);
}

const std::set<String>& WindowManager::getCacheLayout()
{
	return d_cacheLayouts;
}

void WindowManager::registerWindow(Window* window,const String& prefix,const String& name)
{
	core::CMutex::Scoped autoLock(mMutex);
	d_windowRegistry[prefix + name] = window;
}

void WindowManager::renameWindow(const String& window, const String& new_name,bool force)
{
    renameWindow(getWindow(window), new_name,force);
}

void WindowManager::renameWindow(Window* window, const String& new_name, bool force)
{
	if (window)
	{
		bool bPosEqualEnd = false;

		{
			core::CMutex::Scoped autoLock(mMutex);
			WindowRegistry::iterator pos = d_windowRegistry.find(window->getName());
			bPosEqualEnd = (pos == d_windowRegistry.end());

			if (!bPosEqualEnd)
			{
				if (!force)
				{
					d_windowRegistry.erase(pos);
				}
			}
		}

		if (!bPosEqualEnd || force)
		{
			// erase old window name from registry
			if (!force)
			{
				try
				{
					// attempt to rename the window
					window->rename(new_name);
				}
				// rename fails if target name already exists
				catch (AlreadyExistsException&)
				{
					core::CMutex::Scoped autoLock(mMutex);
					// re-add window to registry under it's old name
					d_windowRegistry[window->getName()] = window;
					// rethrow exception.
					throw;
				}
			}

			core::CMutex::Scoped autoLock(mMutex);
			// add window to registry under new name
			d_windowRegistry[new_name] = window;
		}
	}
}

/*************************************************************************
	Return a WindowManager::WindowIterator object to iterate over the
	currently defined Windows.
*************************************************************************/
WindowManager::WindowIterator WindowManager::getIterator(void) const
{
	core::CMutex::Scoped autoLock(mMutex);
	return WindowIterator(d_windowRegistry.begin(), d_windowRegistry.end());
}

/*************************************************************************
    Outputs the names of ALL existing windows to log (DEBUG function).
*************************************************************************/
void WindowManager::DEBUG_dumpWindowNames(String zone)
{
    Logger::getSingleton().logEvent("WINDOW NAMES DUMP (" + zone + ")");
    Logger::getSingleton().logEvent("-----------------");
    CEGUI::WindowManager::WindowIterator windowIt = getIterator();
    while (!windowIt.isAtEnd())
    {
        Logger::getSingleton().logEvent("Window : " + windowIt.getCurrentValue()->getName());
        ++windowIt;
    }
    Logger::getSingleton().logEvent("-----------------");
}

//----------------------------------------------------------------------------//
void WindowManager::lock()
{
	core::CMutex::Scoped autoLock(mMutex);
    ++d_lockCount;
}

//----------------------------------------------------------------------------//
void WindowManager::unlock()
{
	core::CMutex::Scoped autoLock(mMutex);
    if (d_lockCount > 0)
        --d_lockCount;
}

//----------------------------------------------------------------------------//
bool WindowManager::isLocked() const
{
	core::CMutex::Scoped autoLock(mMutex);
    return d_lockCount != 0;
}

//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
