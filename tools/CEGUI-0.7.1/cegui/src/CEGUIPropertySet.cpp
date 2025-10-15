/***********************************************************************
	filename: 	CEGUIPropertySet.cpp
	created:	21/2/2004
	author:		Paul D Turner
	
	purpose:	Implements PropertySet class
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
#include "CEGUIPropertySet.h"
#include "CEGUIProperty.h"
#include "CEGUIExceptions.h"

#if (defined WIN7_32) && (defined _DEBUG)
#include <CEGUIPfsResourceProvider.h>
#include "CEGUILogger.h"
#endif

// Start of CEGUI namespace section
namespace CEGUI
{

static std::map<String, int> s_basePropertyIndex;
static std::vector<Property*> s_basePropertyData;

void PropertySet::addBaseProperty(Property* property)
{
#ifdef FORCEGUIEDITOR
	addProperty(property);
#else
	std::map<String, int>::iterator it = s_basePropertyIndex.find(property->getName());
	size_t key;
	if (it == s_basePropertyIndex.end())
	{
		key = s_basePropertyData.size();
		s_basePropertyIndex.insert(std::pair<String, int>(property->getName(), key));
		s_basePropertyData.push_back(property);
	}
	else
		key = it->second;
	this->d_baseProperties.insert(key);
#endif
}

/*************************************************************************
	Add a new property to the set
*************************************************************************/
void PropertySet::addProperty(Property* property)
{
	if (!property)
	{
		throw NullObjectException("The given Property object pointer is invalid.");
	}
	
	//std::string name(property->getName().c_str());
	if (d_properties.find(property->getName()) != d_properties.end())
	{
		throw AlreadyExistsException("A Property named '" + property->getName() + "' already exists in the PropertySet.");
	}

	d_properties[property->getName()] = property;
}

/*************************************************************************
	Remove a property from the set
*************************************************************************/
void PropertySet::removeProperty(const String& name)
{
	PropertyRegistry::iterator pos = d_properties.find(name);

	if (pos != d_properties.end())
	{
		d_properties.erase(pos);
	}
	else
		assert(0);
}

/*************************************************************************
	Remove all properties from the set
*************************************************************************/
void PropertySet::clearProperties(void)
{
	d_properties.clear();
	d_baseProperties.clear();
}

const Property* PropertySet::getBaseProperty(const String& name) const
{
	std::map<String, int>::const_iterator it = s_basePropertyIndex.find(name);
	if (it != s_basePropertyIndex.end())
	{
		int key = it->second;
		std::set<uint8>::iterator it2 = d_baseProperties.find(key);
		if (it2 != d_baseProperties.end())
		{
			int index = *it2;
			if (index < s_basePropertyData.size())
				return s_basePropertyData[index];
		}
	}

	return NULL;
}

Property* PropertySet::getBaseProperty(const String& name)
{
	std::map<String, int>::const_iterator it = s_basePropertyIndex.find(name);
	if (it != s_basePropertyIndex.end())
	{
		int key = it->second;
		std::set<uint8>::const_iterator it2 = d_baseProperties.find(key);
		if (it2 != d_baseProperties.end())
		{
			int index = *it2;
			if (index < s_basePropertyData.size())
				return s_basePropertyData[index];
		}
	}

	return NULL;
}

/*************************************************************************
	Return true if a property with the given name is in the set
*************************************************************************/
bool PropertySet::isPropertyPresent(const String& name) const
{
	if(getBaseProperty(name) != NULL)
		return true;

	return (d_properties.find(name) != d_properties.end());
}

/*************************************************************************
	Return the help string for a property
*************************************************************************/
//const String& PropertySet::getPropertyHelp(const String& name) const
//{
//	PropertyRegistry::const_iterator pos = d_properties.find(name);
//
//	if (pos == d_properties.end())
//	{
//		throw UnknownObjectException("There is no Property named '" + name + "' available in the set.");
//	}
//
//	return pos->second->getHelp();
//}

/*************************************************************************
	Return the current value of a property
*************************************************************************/
String PropertySet::getProperty(const String& name) const
{
	const Property* baseProperty = getBaseProperty(name);
	if (baseProperty)
		return baseProperty->get(this);

	PropertyRegistry::const_iterator pos = d_properties.find(name);

	if (pos == d_properties.end())
	{
//		//去除多余的没用的属性
//		if (name == "DisabledImage" || name == "HorzLabelFormatting" || name == "NormalImage" || name == "VertLabelFormatting" || name == "ButtonDefaultBorderEnable")
//			return "";//直接返回，没必要报异常
//
//#if (defined WIN7_32) && (defined _DEBUG)
//		std::wstring strName = PFSResourceProvider::GUIStringToWString(name);
//		CEGUI_LOGINSANE("There is no Property named '" + name + "' available in the set.")
//#endif
//		throw UnknownObjectException("There is no Property named '" + name + "' available in the set.");
//#if (defined WIN7_32) && (defined _DEBUG)
//		CEGUI_LOGERR("There is no Property named '" + name + "' available in the set.\n", CEGUI::Error);
//#endif
		return "";//修改为不抛异常直接返回空字符串
	}

	return pos->second->get(this);
}

/*************************************************************************
	Set the current value of a property
*************************************************************************/
void PropertySet::setProperty(const String& name,const String& value)
{
	if (name.empty())
	{
		return;
	}

	Property* baseProperty = getBaseProperty(name);
	if (baseProperty)
		return baseProperty->set(this, value);

	PropertyRegistry::iterator pos = d_properties.find(name);

	if (pos == d_properties.end())
	{

//#if (defined WIN7_32) && (defined _DEBUG)
//		std::wstring strName = PFSResourceProvider::GUIStringToWString(name);
//		CEGUI_LOGINSANE("There is no Property named '" + name + "' available in the set.")
//#endif
		//throw UnknownObjectException("There is no Property named '" + name + "' available in the set.");
		return;//非Windows和Debug就直接返回，不再抛异常
	}

	pos->second->set(this, value);
}


/*************************************************************************
	Return a PropertySet::PropertyIterator object to iterate over the
	available Properties.
*************************************************************************/
PropertySet::Iterator PropertySet::getIterator(void) const
{
	return Iterator(d_properties.begin(), d_properties.end());
}


/*************************************************************************
	Returns whether a Property is at it's default value.
*************************************************************************/
bool PropertySet::isPropertyDefault(const String& name) const
{
	const Property* baseProperty = getBaseProperty(name);
	if (baseProperty)
		return baseProperty->isDefault(this);

	PropertyRegistry::const_iterator pos = d_properties.find(name);

	if (pos == d_properties.end())
	{
//#if (defined WIN7_32) && (defined _DEBUG)
//		std::wstring strName = PFSResourceProvider::GUIStringToWString(name);
//		CEGUI_LOGINSANE("There is no Property named '" + name + "' available in the set.")
//#endif
//		throw UnknownObjectException("There is no Property named '" + name + "' available in the set.");
		return false;
	}

	return pos->second->isDefault(this);
}


/*************************************************************************
	Returns the default value of a Property as a String.	
*************************************************************************/
String PropertySet::getPropertyDefault(const String& name) const
{
	const Property* baseProperty = getBaseProperty(name);
	if (baseProperty)
		return baseProperty->getDefault(this);

	PropertyRegistry::const_iterator pos = d_properties.find(name);

	if (pos == d_properties.end())
	{
//#if (defined WIN7_32) && (defined _DEBUG)
//		std::wstring strName = PFSResourceProvider::GUIStringToWString(name);
//		CEGUI_LOGINSANE("There is no Property named '" + name + "' available in the set.")
//#endif
//		throw UnknownObjectException("There is no Property named '" + name + "' available in the set.");
		return "";
	}

	return pos->second->getDefault(this);
}

} // End of  CEGUI namespace section
