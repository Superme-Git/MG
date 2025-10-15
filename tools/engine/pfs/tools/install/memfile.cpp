/*************************************************************************
 *                                                                       *
 *                              MEMFILE.CPP                              *
 *                                                                       *
 *************************************************************************

                          Copyright 1996 Echidna

   DESCRIPTION


   PROGRAMMERS


   FUNCTIONS

   TABS : 5 9

   HISTORY
		03/28/96 : Created.

 *************************************************************************/

/**************************** I N C L U D E S ****************************/
#include "stdafx.h"
#include "stdio.h"
#include "memfile.h"


/*************************** C O N S T A N T S ***************************/


/******************************* T Y P E S *******************************/


/************************** P R O T O T Y P E S **************************/


/***************************** G L O B A L S *****************************/


/****************************** M A C R O S ******************************/


/**************************** R O U T I N E S ****************************/

MEMFILE *MEMFILE_Load (LPCTSTR filename)
{
	MEMFILE* mf = NULL;
	HANDLE	fh;
	DWORD	len;
	
	fh = CreateFile (filename, GENERIC_READ, FILE_SHARE_READ|FILE_SHARE_READ|FILE_SHARE_DELETE, NULL, OPEN_EXISTING, 0, NULL); 
	if (fh != INVALID_HANDLE_VALUE)
	{
		len = GetFileSize(fh, NULL);
		if (len && len != INVALID_FILE_SIZE)
		{
			mf = (MEMFILE*)HeapAlloc (GetProcessHeap(), 0 ,len  + sizeof (MEMFILE));
			if (mf)
			{
                DWORD numRead;
                
				FillMemory (mf, sizeof (MEMFILE), 0);

				mf->buffer = ((uint8*)mf)+sizeof (MEMFILE);
			
				mf->curPtr    = mf->buffer;
				mf->size      = len;
				mf->bytesLeft = len;
	
				ReadFile (fh, mf->buffer, len, &numRead, NULL);
			}
		}
		CloseHandle (fh);
	}
	return mf;
}

MEMFILE *MEMFILE_OpenData( const void* pvData, long size )
{
	MEMFILE * mf = (MEMFILE*)HeapAlloc(GetProcessHeap(), 0, sizeof(MEMFILE));
	if (mf)
	{
		mf->buffer	  = (uint8 *)pvData; // Attach 行为，并没有copy内存，MEMFILE_Close时也不释放pvData
		mf->curPtr    = mf->buffer;
		mf->size      = size;
		mf->bytesLeft = size;
	}

	return mf;
}

void MEMFILE_Close (MEMFILE *mf)
{
	HeapFree (GetProcessHeap(), 0, mf);
}

int MEMFILE_Read(MEMFILE *mf, void *buf, long len)
{
	if (mf->bytesLeft)
	{
		long	copyBytes;

		copyBytes = (mf->bytesLeft < len) ? mf->bytesLeft : len;

		CopyMemory (buf, mf->curPtr, copyBytes);

		mf->bytesLeft -= copyBytes;
		mf->curPtr    += copyBytes;

		return copyBytes;
	}
	return 0;
}

int MEMFILE_Seek (MEMFILE *mf, long pos, int type)
{
	switch (type)
	{
	case SEEK_SET:
		mf->curPtr    = mf->buffer + pos;
		mf->bytesLeft = mf->size   - pos;
		break;
	case SEEK_CUR:
		mf->curPtr    += pos;
		mf->bytesLeft -= pos;
		break;
	case SEEK_END:
		mf->curPtr     = mf->buffer + mf->size + pos;
		mf->bytesLeft  = -pos;
		break;
	}
	return mf->size - mf->bytesLeft;
}


