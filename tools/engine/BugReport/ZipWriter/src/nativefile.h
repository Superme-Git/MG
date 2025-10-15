#pragma once

namespace ZIP { class CFileIO; }

namespace ZIP {

class CNativeFile : public ZIP::CBaseFile
{
	friend class CNativeImageData;
	
	ZIP::CFileIO*		m_fileio;

public:
	CNativeFile();
	virtual ~CNativeFile();

public:
	virtual bool Open( const std::wstring& filename,
		ZIP::FILE_MODE fm, ZIP::FILE_ACCESS fa);

	virtual void Close();

public:
	virtual size_t Read( void* data, size_t size);
	virtual size_t Write( const void* data, size_t size);
	virtual fsize_t GetSize();
	virtual fsize_t Seek( foff_t pos, ZIP::FILE_SEEK_METHOD fsm);
	virtual fsize_t GetPos();
	virtual bool SetEnd();
	virtual bool ReadAll( void* data, fsize_t size );
};

} // namespace ZIP {