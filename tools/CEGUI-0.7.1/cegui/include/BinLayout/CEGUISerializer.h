//////////////////////////////////////////////////////////////////////////
// File:		CEGUISerializer.h
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUISerializer_h
#define _CEGUISerializer_h

#include "CEGUIStream.h"
#include "CEGUIString.h"
#include "CEGUIWindowManager.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class CEGUIEXPORT Serializer
		{
		public:
			struct Context
			{
				String d_namingPrefix;
				WindowManager::PropertyCallback* d_propertyCallback;
				void* d_userData;
			};

		public:
			virtual bool read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext) = 0;
			virtual bool write(Stream& stream, int version, PropertyReceiver* pPropRecv) = 0;
		};
	}
}

#endif