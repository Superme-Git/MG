//////////////////////////////////////////////////////////////////////////
// File:		CEGUIFileStream.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/03/03
//////////////////////////////////////////////////////////////////////////

#include "BinLayout/CEGUIFileStream.h"

namespace CEGUI
{
	namespace BinLayout
	{
		FileStream::FileStream(FILE* pFile, bool bAutoClose)
			: mpFile(pFile)
			, mbAutoClose(bAutoClose)
		{
		}

		FileStream::~FileStream()
		{
			if (mpFile && mbAutoClose)
			{
				fclose(mpFile);
				mpFile = NULL;
			}
		}

		bool FileStream::isEOF() const
		{
			return 0 != feof(mpFile);
		}

		int FileStream::getSeekPos() const
		{
			return ftell(mpFile);
		}

		void FileStream::setSeekPos(int pos)
		{
			fseek(mpFile, pos, SEEK_SET);
		}

		bool FileStream::read(void* pDstBuffer, int size)
		{
			return 1 == fread(pDstBuffer, size, 1, mpFile);
		}

		bool FileStream::write(const void* pSrcBuffer, int size)
		{
			return 1 == fwrite(pSrcBuffer, size, 1, mpFile);
		}

		void FileStream::skip(int delta)
		{
			fseek(mpFile, delta, SEEK_CUR);
		}
	}
}