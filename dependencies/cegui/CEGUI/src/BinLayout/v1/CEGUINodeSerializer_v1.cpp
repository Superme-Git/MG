//////////////////////////////////////////////////////////////////////////
// File:		CEGUINodeSerializer_v1.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/v1/CEGUINodeSerializer_v1.h"
#include "BinLayout/CEGUIBinLayoutFileDefine.h"
#include "BinLayout/CEGUIWindowTypeIds.h"
#include "CEGUIWindow.h"
#include "BinLayout/CEGUISerializerManager.h"
#include "../CCCommon.h"

namespace CEGUI
{
	namespace BinLayout
	{
		bool NodeSerializer_v1::read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext)
		{
			int nodeType = -1;
			stream >> nodeType;

			switch (nodeType)
			{
			case NT_Window:
				return readWindow(stream, version, ppPropRecv, pContext);
				break;

			case NT_AutoWindow:
				return readAutoWindow(stream, version, ppPropRecv, pContext);
				break;

			case NT_LayoutImport:
				return readLayoutImport(stream, version, ppPropRecv, pContext);
				break;

			case NT_Event:
				return readEvent(stream, version, ppPropRecv, pContext);
				break;

			default:
				break;
			}

			return false;
		}

		bool NodeSerializer_v1::write(Stream& stream, int version, PropertyReceiver* pPropRecv)
		{
			XMLFileData::NodeData* pNodeData = dynamic_cast<XMLFileData::NodeData*>(pPropRecv);
			if (!pNodeData)
			{
				printf("[%s] pPropRecv cannot dynamic_cast to XMLFileData::NodeData.\n", __FUNCTION__);
				return false;
			}

			int nodeType = pNodeData->getType();
			stream << nodeType;

			switch (nodeType)
			{
			case NT_Window:
				return writeWindow(stream, version, dynamic_cast<XMLFileData::WindowData*>(pNodeData));
				break;

			case NT_AutoWindow:
				return writeAutoWindow(stream, version, dynamic_cast<XMLFileData::AutoWindowData*>(pNodeData));
				break;

			case NT_LayoutImport:
				return writeLayoutImport(stream, version, dynamic_cast<XMLFileData::LayoutImportData*>(pNodeData));
				break;

			case NT_Event:
				return writeEvent(stream, version, dynamic_cast<XMLFileData::EventData*>(pNodeData));
				break;

			default:
				printf("[%s] Invalid node type: %d.\n", __FUNCTION__, nodeType);
				return false;
			}

			return true;
		}

		bool NodeSerializer_v1::readWindow(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext)
		{
			Window* pParentWindow = dynamic_cast<Window*>(*ppPropRecv);

			String windowType, windowName;
			stream >> windowType >> windowName;

			Window* wnd = WindowManager::getSingleton().createWindow(windowType, pContext->d_namingPrefix + windowName);
			if (pParentWindow)
			{
				pParentWindow->addChildWindow(wnd);
			}
			else
			{
				*ppPropRecv = wnd;
			}

			wnd->beginInitialisation();

			if (!readProperties(stream, version, &wnd, pContext))
			{
				cleanupWindow(wnd);
				return false;
			}

			if (!readChilds(stream, version, &wnd, pContext))
			{
				cleanupWindow(wnd);
				return false;
			}

			return true;
		}

		bool NodeSerializer_v1::writeWindow(Stream& stream, int version, XMLFileData::WindowData* pData)
		{
			stream << pData->mType << pData->mName;

			if (!writeProperties(stream, version, pData))
			{
				printf("writeProperties failed when writing WindowData: %s, %s\n", pData->mType.c_str(), pData->mName.c_str());
				return false;
			}

			if (!writeChilds(stream, version, pData))
			{
				printf("writeChilds failed when writing WindowData: %s, %s\n", pData->mType.c_str(), pData->mName.c_str());
				return false;
			}

			return true;
		}

		bool NodeSerializer_v1::readAutoWindow(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext)
		{
			Window* pParentWindow = dynamic_cast<Window*>(*ppPropRecv);
			if (!pParentWindow)
			{
				return false;
			}

			String nameSuffix;
			stream >> nameSuffix;

			String windowName = pParentWindow->getName() + nameSuffix;
			Window* wnd = WindowManager::getSingleton().getWindow(windowName);
			if (!wnd)
			{
				return false;
			}

			if (!readProperties(stream, version, &wnd, pContext))
			{
				return false;
			}

			if (!readChilds(stream, version, &wnd, pContext))
			{
				return false;
			}

			return true;
		}

		bool NodeSerializer_v1::writeAutoWindow(Stream& stream, int version, XMLFileData::AutoWindowData* pData)
		{
			stream << pData->mNameSuffix;

			if (!writeProperties(stream, version, pData))
			{
				printf("writeProperties failed when writing AutoWindowData: %s\n", pData->mNameSuffix.c_str());
				return false;
			}

			if (!writeChilds(stream, version, pData))
			{
				printf("writeChilds failed when writing AutoWindowData: %s\n", pData->mNameSuffix.c_str());
				return false;
			}

			return true;
		}

		bool NodeSerializer_v1::readLayoutImport(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext)
		{
			Window* pParentWindow = dynamic_cast<Window*>(*ppPropRecv);

			String prefixAttr, filenameAttr, resourceGroupAttr;
			stream >> prefixAttr >> filenameAttr >> resourceGroupAttr;

			String prefixName = pContext->d_namingPrefix + prefixAttr;

			Window* subLayout = WindowManager::getSingleton().loadWindowLayout(filenameAttr, prefixName, resourceGroupAttr, pContext->d_propertyCallback, pContext->d_userData);

			if (subLayout && pParentWindow)
			{
				pParentWindow->addChildWindow(subLayout);
			}

			return true;
		}

		bool NodeSerializer_v1::writeLayoutImport(Stream& stream, int version, XMLFileData::LayoutImportData* pData)
		{
			stream << pData->mPrefix << pData->mFilename << pData->mResourceGroup;
			return true;
		}

		bool NodeSerializer_v1::readEvent(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext)
		{
			String eventName, functionName;
			stream >> eventName >> functionName;

			Window* pWindow = dynamic_cast<Window*>(*ppPropRecv);
			if (pWindow)
			{
				pWindow->subscribeScriptedEvent(eventName, functionName);
			}

			return true;
		}

		bool NodeSerializer_v1::writeEvent(Stream& stream, int version, XMLFileData::EventData* pData)
		{
			stream << pData->mName << pData->mFunction;
			return true;
		}

		bool NodeSerializer_v1::readProperties(Stream& stream, int& version, Window** ppWindow, Context* pContext)
		{
			int propCount = 0;
			stream >> propCount;
			for (int i = 0; i < propCount; ++i)
			{
				int propId = -1;
				stream >> propId;
				Serializer* pPropSerializer = SerializerManager::getSingleton().getPropertySerializer(propId, version);
				if (!pPropSerializer)
				{
					bool bPropSerializerNotFound = true;

					if (propId == PI_LuaForDialog || propId == PI_LuaUsed)
					{
#ifndef FORCEGUIEDITOR
						bPropSerializerNotFound = false;
#endif
					}

					if (bPropSerializerNotFound)
					{
						String curWindowName, curWindowType;
						Window* pCurWindow = *ppWindow;
						if (pCurWindow)
						{
							curWindowName = pCurWindow->getName();
							curWindowType = pCurWindow->getType();
						}
#if (defined WIN7_32) && (defined _DEBUG)
						String msg = String("Property Serializer not found! propId=") + propId + ", version=" + version + ", prop name: " + PropertyIdUtil::getPropNameById(propId) + ", cur window: " + curWindowName + ", type: " + curWindowType;
						Logger::getSingleton().logEvent(msg, Errors);
#endif
						return false;
					}
				}

				if (pPropSerializer && !pPropSerializer->read(stream, version, (PropertyReceiver**)ppWindow, pContext))
				{
					String curWindowName, curWindowType;
					Window* pCurWindow = *ppWindow;
					if (pCurWindow)
					{
						curWindowName = pCurWindow->getName();
						curWindowType = pCurWindow->getType();
					}
#if (defined WIN7_32) && (defined _DEBUG)
					String msg = String("Property Serializer read failed! propId=") + propId + ", version=" + version + ", prop name: " + PropertyIdUtil::getPropNameById(propId) + ", cur window: " + curWindowName + ", type: " + curWindowType;
					Logger::getSingleton().logEvent(msg, Errors);
#endif
				}
			}

			return true;
		}

		bool isPropertyDeleted(const String& propName)
		{
			String propDeleted[] =
			{
				"OnCreateAnimationName",
				"OnCreateAnimationDeltaTime",
				"OnCloseAnimationName",
				"OnCloseAnimationDeltaTime",
				"OnCreateEffectName",
				"OnCloseEffectName",
				"OnCreateEffectDeltaTime",
				"OnCreateEffectCycle",
				"OnCloseEffectDeltaTime",
			};

			for (int i = 0; i < sizeof(propDeleted) / sizeof(String); ++i)
			{
				if (propName == propDeleted[i])
				{
					return true;
				}
			}

			return false;
		}

		bool NodeSerializer_v1::writeProperties(Stream& stream, int version, XMLFileData::NodeData* pData)
		{
			XMLFileData::NodeData::Array propArr;

			for (int i = 0; i < pData->getChildCount(); ++i)
			{
				XMLFileData::NodeData* pChildNodeData = pData->getChild(i);
				if (pChildNodeData && pChildNodeData->getType() == NT_Property)
				{
					XMLFileData::PropertyData* pPropData = static_cast<XMLFileData::PropertyData*>(pChildNodeData);
					if (!isPropertyDeleted(pPropData->mName))
					{
						propArr.push_back(pChildNodeData);
					}
				}
			}

			int propCount = propArr.size();
			stream << propCount;

			for (int i = 0; i < propCount; ++i)
			{
				XMLFileData::PropertyData* pPropData = dynamic_cast<XMLFileData::PropertyData*>(propArr[i]);

				int propId = PropertyIdUtil::getPropIdByName(pPropData->mName);
				if (propId < 0 || propId >= PI_COUNT)
				{
					printf("Invalid property Id: %s, %s, propId=%d\n", pPropData->mName.c_str(), pPropData->mValue.c_str(), propId);
					return false;
				}

				stream << propId;

				Serializer* pPropSerializer = SerializerManager::getSingleton().getPropertySerializer(propId, version);
				if (!pPropSerializer)
				{
					bool bPropSerializerNotFound = true;

					if (propId == PI_LuaForDialog || propId == PI_LuaUsed)
					{
#ifndef FORCEGUIEDITOR
						bPropSerializerNotFound = false;
#endif
					}

					if (bPropSerializerNotFound)
					{
						printf("cannot find a Serializer for property: %s, %s, propId=%d, version=%d\n", pPropData->mName.c_str(), pPropData->mValue.c_str(), propId, version);
						return false;
					}
				}

				if (pPropSerializer && !pPropSerializer->write(stream, version, pPropData))
				{
					printf("Property Serializer write failed: %s, %s, propId=%d, version=%d\n", pPropData->mName.c_str(), pPropData->mValue.c_str(), propId, version);
					return false;
				}
			}

			return true;
		}

		bool NodeSerializer_v1::readChilds(Stream& stream, int& version, Window** ppWindow, Context* pContext)
		{
			int childCount = 0;
			stream >> childCount;
			for (int i = 0; i < childCount; ++i)
			{
				if (!read(stream, version, (PropertyReceiver**)ppWindow, pContext))
				{
					return false;
				}
			}

			return true;
		}

		bool NodeSerializer_v1::writeChilds(Stream& stream, int version, XMLFileData::NodeData* pData)
		{
			XMLFileData::NodeData::Array childArr;

			for (int i = 0; i < pData->getChildCount(); ++i)
			{
				XMLFileData::NodeData* pChildNodeData = pData->getChild(i);
				if (pChildNodeData && pChildNodeData->getType() != NT_Property)
				{
					childArr.push_back(pChildNodeData);
				}
			}

			int childCount = childArr.size();
			stream << childCount;

			for (int i = 0; i < childCount; ++i)
			{
				XMLFileData::NodeData* pChildNodeData = childArr[i];
				if (!write(stream, version, pChildNodeData))
				{
					printf("write child failed at index: %d\n", i);
					return false;
				}
			}

			return true;
		}

		void NodeSerializer_v1::cleanupWindow(Window* wnd)
		{
			if (wnd)
			{
				// detach from parent
				if (wnd->getParent())
				{
					wnd->getParent()->removeChildWindow(wnd);
				}

				// destroy the window
				WindowManager::getSingleton().destroyWindow(wnd);
			}
		}
	}
}