//////////////////////////////////////////////////////////////////////////
// File:		CEGUIMemoryStream.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/CEGUIMemoryStream.h"

namespace CEGUI
{
	namespace BinLayout
	{
		MemoryStream::MemoryStream(char* pBuffer, int size, bool bAutoDelete/* = false*/)
			: mpBuffer(pBuffer)
			, mBufferSize(size)
			, mSeekPos(0)
			, mbAutoDelete(bAutoDelete)
		{}

		MemoryStream::~MemoryStream()
		{
			if (mbAutoDelete)
			{
				delete[] mpBuffer;
				mpBuffer = NULL;
			}
		}

		void MemoryStream::setSeekPos(int pos)
		{
			mSeekPos = pos < 0 ? 0 : (pos > mBufferSize ? mBufferSize : pos);
		}

		bool MemoryStream::read(void* pDstBuffer, int size)
		{
			int newPos = mSeekPos + size;
			if (newPos <= mBufferSize)
			{
				memcpy(pDstBuffer, mpBuffer + mSeekPos, size);
				mSeekPos = newPos;
				return true;
			}
			return false;
		}

		bool MemoryStream::write(const void* pSrcBuffer, int size)
		{
			int newPos = mSeekPos + size;
			if (newPos <= mBufferSize)
			{
				memcpy(mpBuffer + mSeekPos, pSrcBuffer, size);
				mSeekPos = newPos;
				return true;
			}
			return false;
		}
	}
}