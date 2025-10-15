//////////////////////////////////////////////////////////////////////////
// File:		CEGUISerializerManager.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/03/02
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/CEGUISerializerManager.h"

namespace CEGUI
{
	namespace BinLayout
	{
		SerializerManager SerializerManager::msSingleton;

		SerializerManager& SerializerManager::getSingleton()
		{
			return msSingleton;
		}

		SerializerManager* SerializerManager::getSingletonPtr()
		{
			return &msSingleton;
		}

		void SerializerManager::addLayoutSerializer(int version, Serializer* pSerializer)
		{
			VersionToSerializerMap::iterator it = mLayoutSerializers.find(version);
			if (it != mLayoutSerializers.end())
			{
				Logger::getSingleton().logEvent(String("Already exist one layout Serializer of version ") + version + String(". The old one will be replaced by the new one."), Warnings);
			}
			mLayoutSerializers[version] = pSerializer;
		}

		void SerializerManager::addNodeSerializer(int version, Serializer* pSerializer)
		{
			VersionToSerializerMap::iterator it = mNodeSerializers.find(version);
			if (it != mNodeSerializers.end())
			{
				Logger::getSingleton().logEvent(String("Already exist one node Serializer of version ") + version + String(". The old one will be replaced by the new one."), Warnings);
			}
			mNodeSerializers[version] = pSerializer;
		}

		void SerializerManager::addPropertySerializer(int propId, int version, Serializer* pSerializer)
		{
			if (propId < 0 || propId >= PI_COUNT)
			{
				Logger::getSingleton().logEvent(String("Invalid property Id") + propId, Errors);
				return;
			}

			VersionToSerializerMap& serializerMap = mPropertySerializers[propId];
			VersionToSerializerMap::iterator it = serializerMap.find(version);
			if (it != serializerMap.end())
			{
				Logger::getSingleton().logEvent(String("Already exist one property Serializer! propId=") + propId + ", version=" + version + ", prop name: " + PropertyIdUtil::getPropNameById((PropertyId)propId) + String(". The old one will be replaced by the new one."), Warnings);
			}
			serializerMap[version] = pSerializer;
		}

		Serializer* SerializerManager::getLayoutSerializer(int version)
		{
			VersionToSerializerMap::iterator it = mLayoutSerializers.find(version);
			if (it == mLayoutSerializers.end())
			{
				Logger::getSingleton().logEvent(String("Cannot find layout Serializer of version ") + version, Errors);
				return NULL;
			}
			return it->second;
		}

		Serializer* SerializerManager::getNodeSerializer(int version)
		{
			VersionToSerializerMap::iterator it = mNodeSerializers.find(version);
			if (it == mNodeSerializers.end())
			{
				Logger::getSingleton().logEvent(String("Cannot find node Serializer of version ") + version, Errors);
				return NULL;
			}
			return it->second;
		}

		Serializer* SerializerManager::getPropertySerializer(int propId, int version)
		{
			if (propId < 0 || propId >= PI_COUNT)
			{
				Logger::getSingleton().logEvent(String("Invalid property Id") + propId, Errors);
				return NULL;
			}

			VersionToSerializerMap& serializerMap = mPropertySerializers[propId];
			VersionToSerializerMap::iterator it = serializerMap.find(version);
			if (it == serializerMap.end())
			{
				PropertyId pid = (PropertyId)propId;
				if (pid != PI_LuaClass && pid != PI_LuaEventOnClicked && pid != PI_LuaForDialog && pid != PI_LuaMemberName) //properties used in editor
				{
					Logger::getSingleton().logEvent(String("Cannot find property Serializer! propId=") + propId + ", version=" + version + ", prop name: " + PropertyIdUtil::getPropNameById((PropertyId)propId), Errors);
				}
				return NULL;
			}
			return it->second;
		}
	}
}