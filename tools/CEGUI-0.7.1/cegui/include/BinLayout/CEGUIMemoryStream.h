//////////////////////////////////////////////////////////////////////////
// File:		CEGUIMemoryStream.h
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUIMemoryStream_h
#define _CEGUIMemoryStream_h

#include "CEGUIStream.h"

namespace CEGUI
{
	namespace BinLayout
	{
		class CEGUIEXPORT MemoryStream : public Stream
		{
		private:
			char* mpBuffer;
			int mBufferSize;
			int mSeekPos;
			bool mbAutoDelete;

		public:
			MemoryStream(char* pBuffer, int size, bool bAutoDelete = false);
			virtual ~MemoryStream();

			virtual bool isEOF() const { return mSeekPos >= mBufferSize; }
			virtual int getSeekPos() const { return mSeekPos; }
			virtual void setSeekPos(int pos);
			virtual bool read(void* pDstBuffer, int size);
			virtual bool write(const void* pSrcBuffer, int size);
		};
	}
}

#endif