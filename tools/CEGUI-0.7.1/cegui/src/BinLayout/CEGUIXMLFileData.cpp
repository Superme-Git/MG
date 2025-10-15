//////////////////////////////////////////////////////////////////////////
// File:		CEGUIXMLFileData.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/03/03
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/CEGUIXMLFileData.h"
#include "CEGUIGUILayout_xmlHandler.h"
#include "CEGUIXMLAttributes.h"

namespace CEGUI
{
	namespace BinLayout
	{
		XMLFileData::XMLFileData()
			: mpRootData(NULL)
			, mpCurrentData(NULL)
		{
		}

		XMLFileData::~XMLFileData()
		{
			if (mpRootData)
			{
				delete mpRootData;
				mpRootData = NULL;
			}
		}

		void XMLFileData::elementStart(const String& element, const XMLAttributes& attributes)
		{
			NodeData* pNewData = NULL;

			//std::string e(element.c_str());
			// handle root GUILayoutElement element
			if (element == GUILayout_xmlHandler::GUILayoutElement)
			{
				elementGUILayoutStart(attributes);
			}
			// handle Window element (create window and make an entry on our "window stack")
			else if (element == GUILayout_xmlHandler::WindowElement)
			{
				pNewData = elementWindowStart(attributes);
			}
			// handle AutoWindow element (get an auto child from the window on top of the "window stack")
			else if (element == GUILayout_xmlHandler::AutoWindowElement)
			{
				pNewData = elementAutoWindowStart(attributes);
			}
			// handle Property element (set property for window at top of stack)
			else if (element == GUILayout_xmlHandler::PropertyElement)
			{
				pNewData = elementPropertyStart(attributes);
			}
			// handle layout import element (attach a layout to the window at the top of the stack)
			else if (element == GUILayout_xmlHandler::LayoutImportElement)
			{
				pNewData = elementLayoutImportStart(attributes);
			}
			// handle event subscription element
			else if (element == GUILayout_xmlHandler::EventElement)
			{
				pNewData = elementEventStart(attributes);
			}
			// anything else is an error which *should* have already been caught by XML validation
			else
			{
				Logger::getSingleton().logEvent("GUILayout_xmlHandler::startElement - Unexpected data was found while parsing the gui-layout file: '" + element + "' is unknown.", Errors);
			}

			if (pNewData)
			{
				if (mpCurrentData)
				{
					mpCurrentData->addChild(pNewData);
				}
				else
				{
					mpRootData = pNewData;
				}

				mpCurrentData = pNewData;
			}
		}

		void XMLFileData::elementEnd(const String& element)
		{
			if (mpCurrentData)
			{
				mpCurrentData = mpCurrentData->getParent();
			}
		}

		void XMLFileData::elementGUILayoutStart(const XMLAttributes& attributes)
		{
			if (mpRootData)
			{
				delete mpRootData;
				mpRootData = NULL;
			}

			mpCurrentData = NULL;
		}

		XMLFileData::NodeData* XMLFileData::elementWindowStart(const XMLAttributes& attributes)
		{
			WindowData* pData = new WindowData;

			pData->mType = attributes.getValueAsString(GUILayout_xmlHandler::WindowTypeAttribute);
			pData->mName = attributes.getValueAsString(GUILayout_xmlHandler::WindowNameAttribute);

			return pData;
		}

		XMLFileData::NodeData* XMLFileData::elementAutoWindowStart(const XMLAttributes& attributes)
		{
			AutoWindowData* pData = new AutoWindowData;

			pData->mNameSuffix = attributes.getValueAsString(GUILayout_xmlHandler::AutoWindowNameSuffixAttribute);

			return pData;
		}

		XMLFileData::NodeData* XMLFileData::elementPropertyStart(const XMLAttributes& attributes)
		{
			PropertyData* pData = new PropertyData;

			pData->mName = attributes.getValueAsString(GUILayout_xmlHandler::PropertyNameAttribute);
			pData->mValue = attributes.getValueAsString(GUILayout_xmlHandler::PropertyValueAttribute);

			return pData;
		}

		XMLFileData::NodeData* XMLFileData::elementLayoutImportStart(const XMLAttributes& attributes)
		{
			LayoutImportData* pData = new LayoutImportData;

			pData->mPrefix = attributes.getValueAsString(GUILayout_xmlHandler::LayoutImportPrefixAttribute);
			pData->mFilename = attributes.getValueAsString(GUILayout_xmlHandler::LayoutImportFilenameAttribute);
			pData->mResourceGroup = attributes.getValueAsString(GUILayout_xmlHandler::LayoutImportResourceGroupAttribute);

			return pData;
		}

		XMLFileData::NodeData* XMLFileData::elementEventStart(const XMLAttributes& attributes)
		{
			EventData* pData = new EventData;

			pData->mName = attributes.getValueAsString(GUILayout_xmlHandler::EventNameAttribute);
			pData->mFunction = attributes.getValueAsString(GUILayout_xmlHandler::EventFunctionAttribute);

			return pData;
		}
	}
}