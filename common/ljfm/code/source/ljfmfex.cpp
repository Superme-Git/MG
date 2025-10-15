#include "../include/common.h"
#include "../include/ljfmfex.h"

namespace LJFM
{
	LJFMF::~LJFMF()
	{
		if (m_file)
			LJFMOpen::CloseFile(m_file);
	}
	void LJFMF::AttackBaseFile(LJFMBF* file)
	{
		assert(NULL == m_file);
		m_file = file;
	}
	LJFMBF* LJFMF::DetackBaseFile()
	{
		LJFMBF* temp = m_file;
		m_file = NULL;
		return temp;
	}
	size_t LJFMF::Read(void* data, size_t size)
	{
		assert(m_file);
		return m_file->Read(data, size);
	}
	size_t LJFMF::Write(const void* data, size_t size)
	{
		assert(m_file);
		return m_file->Write(data, size);
	}
	fsize_t LJFMF::GetSize()
	{
		assert(m_file);
		return m_file->GetSize();
	}
	fsize_t LJFMF::Seek(foff_t pos, FILE_SEEK_METHOD fsm)
	{
		assert(m_file);
		return m_file->Seek(pos, fsm);
	}
	fsize_t LJFMF::GetPos()
	{
		assert(m_file);
		return m_file->GetPos();
	}
	bool LJFMF::SetEnd()
	{
		assert(m_file);
		return m_file->SetEnd();
	}
	LJFMID LJFMF::GetImage()
	{
		assert(m_file);
		return m_file->GetImage();
	}

	bool LJFMF::ReadAll(void* data, fsize_t size)
	{
		assert(m_file);
		return m_file->ReadAll(data, size);
	}

	std::wstring LJFMF::GetFullPathFileName(const std::wstring& filename)
	{
		return LJFMOpen::GetFullPathFileName(filename);
	}

	bool LJFMF::Open(const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa)
	{
		assert(NULL == m_file);

		if (filename.empty())
		{
			LJFMOpen::SetLastError(EC_INVALID_FILENAME);
			assert(!filename.empty());
			return false;
		}

		if (filename[0] == L'/')
		{
			m_file = LJFMOpen::OpenFile(filename, fm, fa);
		}
		else
		{
			assert(false);
		}
		return NULL != m_file;
	}

	void LJFMF::Close()
	{
		if (m_file != NULL)
		{
			LJFMOpen::CloseFile(m_file);
			m_file = NULL;
		}
	}

	size_t LJFMIF::Read(void* data, size_t size)
	{
		fsize_t leftsize = m_data.GetSize() - m_sizeOffset;
		if (leftsize < size)
			size = (size_t)leftsize;
		if (size)
		{
			memcpy(data, (const char*)m_data.GetData() + m_sizeOffset, size);
			m_sizeOffset += size;
		}
		return size;
	}
	size_t LJFMIF::Write(const void* data, size_t size)
	{
		return LJFM::FILE_SIZE_ERROR;
	}
	fsize_t LJFMIF::GetSize()
	{
		return m_data.GetSize();
	}
	fsize_t LJFMIF::Seek(foff_t pos, LJFM::FILE_SEEK_METHOD fsm)
	{
		fsize_t newoffset = LJFM::FILE_SIZE_ERROR;

		switch (fsm)
		{
		case LJFM::FSM_CUR:
			newoffset = m_sizeOffset + pos;
			break;
		case LJFM::FSM_SET:
			newoffset = pos;
			break;
		case LJFM::FSM_END:
			newoffset = m_data.GetSize() + pos;
			break;
		default:
			break;
		}
		if (newoffset > m_data.GetSize())
			return LJFM::FILE_SIZE_ERROR;
		m_sizeOffset = newoffset;
		return m_sizeOffset;
	}
	fsize_t LJFMIF::GetPos()
	{
		return m_sizeOffset;
	}
	bool LJFMIF::SetEnd()
	{
		return false;
	}

	LJFM::LJFMID LJFMIF::GetImage()
	{
		return m_data;
	}

	bool LJFMIF::ReadAll(void* data, fsize_t size)
	{
		if (m_data.GetSize() > size)
		{

			LJFM::LJFMOpen::SetLastError(LJFM::EC_INSUFFICIENT_BUFFER);
			return false;
		}

		if (m_data.GetSize() > MAX_MEMORY_DATA_SIZE)
		{
			LJFM::LJFMOpen::SetLastError(LJFM::EC_FILESIZE_TOO_LARGE);
			return false;
		}

		memcpy(data, m_data.GetData(), (size_t)m_data.GetSize());
		return true;
	}

	bool LJFMIF::Open(const LJFMID& image)
	{
		m_data = image;
		m_sizeOffset = 0;
		return true;
	}
	bool LJFMIF::Open(const LJFMDB& data)
	{
		m_data = LJFMDBToID(data);
		m_sizeOffset = 0;
		return true;
	}

	//----------------------------------------
	size_t LJFMDF::Read(void* data, size_t size)
	{
		LJFM::LJFMOpen::SetLastError(LJFM::EC_FILE_WRITEONLY);
		return LJFM::FILE_SIZE_ERROR;
	}
	size_t LJFMDF::Write(const void* data, size_t size)
	{
		fsize_t orgsize = m_data.GetSize();
		if (m_offset < orgsize)
		{
			if (m_offset + size > orgsize)
			{
				m_data.Resize(m_offset);
				m_data.Append(data, size);
			}
			else
			{
				memcpy((char*)m_data.GetData() + m_offset, data, size);
			}
		}
		else
		{
			m_data.Resize(m_offset);
			m_data.Append(data, size);
		}

		m_offset += size;
		return size;
	}
	fsize_t LJFMDF::GetSize()
	{
		return m_data.GetSize();
	}
	fsize_t LJFMDF::Seek(foff_t pos, LJFM::FILE_SEEK_METHOD fsm)
	{
		fsize_t	_offset = m_offset;
		switch (fsm)
		{
		case LJFM::FSM_CUR:
			_offset += pos;
			break;
		case LJFM::FSM_SET:
			_offset = pos;
			break;
		case LJFM::FSM_END:
			_offset = m_data.GetSize() + pos;
			break;
		default:
			break;
		}

		m_offset = (size_t)_offset;
		return m_offset;
	}
	fsize_t LJFMDF::GetPos()
	{
		return m_offset;
	}
	bool LJFMDF::SetEnd()
	{
		m_data.Resize(m_offset);
		return true;
	}
	LJFM::LJFMID LJFMDF::GetImage()
	{
		return LJFMDBToID(m_data);
	}

	bool LJFMDF::ReadAll(void* data, fsize_t size)
	{
		LJFM::LJFMOpen::SetLastError(LJFM::EC_FILE_WRITEONLY);
		return false;
	}

	//-----------------------------------------------
	size_t LJFMMF::Read(void* data, size_t size)
	{
		fsize_t all = m_data.GetSize();
		size_t read = (size_t)all - m_offset;
		if (read > size)
		{
			read = size;
		}
		if (read > 0)
		{
			memcpy(data, (char*)m_data.GetData() + m_offset, read);
			m_offset += read;
			return read;
		}
		else
		{
			return 0;
		}
	}

	bool LJFMMF::ReadAll(void* data, fsize_t size)
	{
		if (m_data.GetSize() > size)
		{

			LJFM::LJFMOpen::SetLastError(LJFM::EC_INSUFFICIENT_BUFFER);
			return false;
		}

		memcpy(data, m_data.GetData(), m_data.GetSize());
		return true;
	}
}
