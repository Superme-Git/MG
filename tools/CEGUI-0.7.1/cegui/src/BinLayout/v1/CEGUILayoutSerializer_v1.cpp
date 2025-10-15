//////////////////////////////////////////////////////////////////////////
// File:		CEGUILayoutSerializer_v1.cpp
// Author:	杨春雷
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/v1/CEGUILayoutSerializer_v1.h"
#include "BinLayout/CEGUISerializerManager.h"
#include "BinLayout/CEGUIBinLayoutFileDefine.h"

namespace CEGUI
{
	namespace BinLayout
	{
		bool LayoutSerializer_v1::read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext)
		{
			// v1 直接开始 Window 节点的读取

			Serializer* pSerializer = SerializerManager::getSingleton().getNodeSerializer(version);
			if (!pSerializer)
			{
				return false;
			}

			return pSerializer->read(stream, version, ppPropRecv, pContext);
		}

		bool LayoutSerializer_v1::write(Stream& stream, int version, PropertyReceiver* pPropRecv)
		{
			// v1 直接开始 Window 节点的写入

			Serializer* pSerializer = SerializerManager::getSingleton().getNodeSerializer(version);
			if (!pSerializer)
			{
				return false;
			}

			return pSerializer->write(stream, version, pPropRecv);
		}
	}
}