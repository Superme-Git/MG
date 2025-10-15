#pragma once
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <windows.h>
#include <string>
#endif

namespace LJFM { class CFileIO; class CFileImage; }

namespace LJFMX
{
	class CLJFSZipFile : public LJFM::LJFMBF
	{
		fsize_t m_UnitPos;
		fsize_t m_UnitSize;

		unsigned char* m_DataUnCompress;
		unsigned char* m_DataDeCode;

		unsigned char* m_pDataBegin;
		unsigned char* m_pDataCur;

		std::wstring m_FullPathFileName;
	
	public:
		CLJFSZipFile();
		virtual ~CLJFSZipFile();
	public:
		virtual bool Open(void* FileInfo);
		virtual void Close();
		virtual bool UnCompress(void* pFI, unsigned char* pDataSrc, unsigned char*& pDataDst);
		virtual bool DeCode(void* pFI, unsigned char* pDataSrc, unsigned char*& pDataDst);
	public:
		virtual size_t Read(void* data, size_t size);
		virtual size_t Write(const void* data, size_t size);
		virtual fsize_t GetSize();
		virtual fsize_t Seek(foff_t pos, LJFM::FILE_SEEK_METHOD fsm);
		virtual fsize_t GetPos();
		virtual bool SetEnd();
		virtual LJFM::LJFMID GetImage();
		virtual bool ReadAll(void* data, fsize_t size );
		virtual std::wstring GetFullPathFileName();
	};

}
