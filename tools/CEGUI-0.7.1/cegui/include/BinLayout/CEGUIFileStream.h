//////////////////////////////////////////////////////////////////////////
// File:		CEGUIFileStream.h
// Author:	Ñî´ºÀ×
// Date:		2016/03/03
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUIFileStream_h_
#define _CEGUIFileStream_h_

#include "CEGUIStream.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class CEGUIEXPORT FileStream : public Stream
		{
		private:
			FILE* mpFile;
			bool mbAutoClose;

		public:
			FileStream(FILE* pFile, bool bAutoClose);
			virtual ~FileStream();

			virtual bool isEOF() const;
			virtual int getSeekPos() const;
			virtual void setSeekPos(int pos);
			virtual bool read(void* pDstBuffer, int size);
			virtual bool write(const void* pSrcBuffer, int size);

			virtual void skip(int delta);
		};
	}
}

#endif