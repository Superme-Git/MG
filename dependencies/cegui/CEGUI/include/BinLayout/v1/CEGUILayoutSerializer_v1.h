//////////////////////////////////////////////////////////////////////////
// File:		CEGUILayoutSerializer_v1.h
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUILayoutSerializer_v1_h_
#define _CEGUILayoutSerializer_v1_h_

#include "../CEGUISerializer.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class LayoutSerializer_v1 : public Serializer
		{
		public:
			virtual bool read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext);
			virtual bool write(Stream& stream, int version, PropertyReceiver* pPropRecv);
		};
	}
}

#endif