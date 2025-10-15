#pragma once
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <windows.h>
#include <string>
#endif

#include <fstream>

namespace LJFM { class CFileIO; class CFileImage; }

namespace LJFMX
{
	class CLJFSFile : public LJFM::LJFMBF
	{
		std::fstream m_FS;
		bool m_bUnit;
		unsigned int m_UnitOffset;
		unsigned int m_UnitSize;
		unsigned int m_FSSize;

		std::wstring m_FullPathFileName;
	
	public:
		CLJFSFile();
		virtual ~CLJFSFile();
	public:
		virtual bool Open(void* FileInfo);
		virtual void Close();
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
