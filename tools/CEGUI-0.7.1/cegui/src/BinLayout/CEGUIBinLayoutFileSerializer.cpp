//////////////////////////////////////////////////////////////////////////
// File:		CEGUILayoutBinFileSerializer.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/CEGUIBinLayoutFileSerializer.h"
#include "BinLayout/CEGUIBinLayoutFileDefine.h"
#include "BinLayout/CEGUISerializerManager.h"

namespace CEGUI
{
	namespace BinLayout
	{
		bool BinLayoutFileSerializer::read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext)
		{
			char magic[4];
			stream >> magic >> version;

			if (0 != memcmp(magic, LAYOUT_BIN_FILE_MAGIC, 4))
			{
				return false;
			}

			Serializer* pSerializer = SerializerManager::getSingleton().getLayoutSerializer(version);
			if (!pSerializer)
			{
				return false;
			}

			return pSerializer->read(stream, version, ppPropRecv, pContext);
		}

		bool BinLayoutFileSerializer::write(Stream& stream, int version, PropertyReceiver* pPropRecv)
		{
			stream << LAYOUT_BIN_FILE_MAGIC << version;

			Serializer* pSerializer = SerializerManager::getSingleton().getLayoutSerializer(version);
			if (!pSerializer)
			{
				return false;
			}

			return pSerializer->write(stream, version, pPropRecv);
		}
	}
}