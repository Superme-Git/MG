#include "../include/common.h"
#include "../include/ljfileinfo.h"
#include "../../../platform/utils/StringCover.h"

namespace LJFMX
{

	CLJFSFile::CLJFSFile() 
	{
		m_bUnit = false;
		m_UnitOffset = 0;;
		m_UnitSize = 0;
		m_FSSize = 0;
	}
	CLJFSFile::~CLJFSFile() 
	{ 
		Close();
	}
	bool CLJFSFile::Open(void* FileInfo)
	{
		if (FileInfo == NULL)
		{
			return false;
		}
		Close();
		LJFP_FileInfo* FI = (LJFP_FileInfo*)FileInfo;
		m_FS.open(ws2s(FI->GetFullPathFileName()).c_str(), std::ios_base::binary | std::ios_base::in);
		if (!m_FS.is_open())
		{
			return false;
		}
		m_FS.seekg(0, std::ios_base::end);
		m_FSSize = m_FS.tellg();
		m_FS.seekg(0, std::ios_base::beg);
		m_bUnit = FI->m_PackIndex > 0;
		m_UnitOffset = FI->m_Pos;
		m_UnitSize = FI->m_Size;
		m_FullPathFileName = FI->GetFullPathFileName();
		if (m_UnitSize == 0)
		{
			m_UnitSize = m_FSSize;
		}
		return true;
	}
	void CLJFSFile::Close()
	{
		if (m_FS.is_open())
		{
			m_FS.close();
		}
	}
	size_t CLJFSFile::Read(void* data, size_t size)
	{
		m_FS.read((char*)data, size);
		return size;
	}
	size_t CLJFSFile::Write(const void* data, size_t size)
	{
		m_FS.write((char*)data, size);
		return size;
	}
	fsize_t CLJFSFile::GetSize()
	{
		return m_UnitSize;
	}
	fsize_t CLJFSFile::Seek(foff_t pos, LJFM::FILE_SEEK_METHOD fsm)
	{
		if (m_bUnit)
		{
			m_FS.seekg(pos + m_UnitOffset, (std::ios_base::seekdir)fsm);
			return pos;
		}
		else
		{
			m_FS.seekg(pos, (std::ios_base::seekdir)fsm);
			return pos;
		}
	}
	fsize_t CLJFSFile::GetPos()
	{
		if (m_bUnit)
		{
			unsigned int uiPos = m_FS.tellg();
			return  uiPos - m_UnitOffset;
		}
		else
		{
			return m_FS.tellg();
		}
	}
	bool CLJFSFile::SetEnd()
	{
		if (m_bUnit)
		{
			m_FS.seekg(m_UnitOffset + m_UnitSize, std::ios_base::beg);
			return m_UnitOffset + m_UnitSize;
		}
		else
		{
			m_FS.seekg(0, std::ios_base::end);
			return m_FS.tellg();
		}
	}
	LJFM::LJFMID CLJFSFile::GetImage()
	{
		return BaseFileToImageDataEx(*this, 0, m_UnitSize);
	}
	bool CLJFSFile::ReadAll(void* data, fsize_t size)
	{
		m_FS.seekg(m_UnitOffset, std::ios_base::beg);
		m_FS.read((char*)data, size);
		return true;
	}
	std::wstring CLJFSFile::GetFullPathFileName()
	{
		return m_FullPathFileName;
	}
}
