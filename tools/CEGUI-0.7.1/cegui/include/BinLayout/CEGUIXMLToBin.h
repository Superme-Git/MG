//////////////////////////////////////////////////////////////////////////
// File:		CEGUIXMLToBin.h
// Author:	Ñî´ºÀ×
// Date:		2016/03/03
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUIXMLToBin_h_
#define _CEGUIXMLToBin_h_

#include "CEGUIBase.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class CEGUIEXPORT XMLToBin
		{
		public:
			bool convert(const String& srcFilename) { return convert(srcFilename, srcFilename); }
			bool convert(const String& srcFilename, const String& dstFilename);
		};
	}
}

#endif