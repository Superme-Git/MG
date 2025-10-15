//////////////////////////////////////////////////////////////////////////
// File:		CEGUISerializerManager.h
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUISerializerManager_h
#define _CEGUISerializerManager_h

#include "CEGUISingleton.h"
#include "CEGUISerializer.h"
#include "CEGUIPropertyIds.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class CEGUIEXPORT SerializerManager
		{
			typedef std::map<int, Serializer*> VersionToSerializerMap;

		private:
			static SerializerManager msSingleton;

		private:
			VersionToSerializerMap mLayoutSerializers;
			VersionToSerializerMap mNodeSerializers;
			VersionToSerializerMap mPropertySerializers[PI_COUNT];

		public:
			static SerializerManager& getSingleton();
			static SerializerManager* getSingletonPtr();

			void addLayoutSerializer(int version, Serializer* pSerializer);
			void addNodeSerializer(int version, Serializer* pSerializer);
			void addPropertySerializer(int propId, int version, Serializer* pSerializer);

			Serializer* getLayoutSerializer(int version);
			Serializer* getNodeSerializer(int version);
			Serializer* getPropertySerializer(int propId, int version);
		};
	}
}

#endif