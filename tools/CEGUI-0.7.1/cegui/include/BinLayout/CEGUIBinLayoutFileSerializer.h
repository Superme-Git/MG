//////////////////////////////////////////////////////////////////////////
// File:		CEGUIBinLayoutFileSerializer.h
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUIBinLayoutFileSerializer_h_
#define _CEGUIBinLayoutFileSerializer_h_

#include "CEGUISerializer.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class CEGUIEXPORT BinLayoutFileSerializer : public Serializer
		{
		public:
			virtual bool read(Stream& stream, int& version, PropertyReceiver** ppPropRecv, Context* pContext);
			virtual bool write(Stream& stream, int version, PropertyReceiver* pPropRecv);
		};
	}
}

#endif