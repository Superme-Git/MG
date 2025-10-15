#pragma once

namespace PFS {

namespace helper { // xuhui 将下面的类放入了helper名字空间

class CMD5Hash : public CUncopyableObject
{
	unsigned char	m_context[88];
public:
	CMD5Hash();
	virtual ~CMD5Hash();
public:
	void Update( const void* data, size_t dataLen);
	void Update( const CDataBlock& data);
	void Update( const unsigned long long& data);
	CDataBlock& Final( CDataBlock& digest);
	FILE_DATA_HASH& Final( FILE_DATA_HASH& digest);
};

class CFileNameHash
{
	CMD5Hash		m_md5;
public:
	CFileNameHash() {}
	~CFileNameHash() {}

	void Update( const std::wstring& filename, const FILE_NAME_HASH& fileHash)
	{
		m_md5.Update( filename.c_str(), filename.size()*sizeof(wchar_t));
		m_md5.Update( fileHash);
	}

	FILE_NAME_HASH& Final( FILE_NAME_HASH& digest);
};

class CMD5FileHashMethod : public CFileHashMethod
{
public:
	CMD5FileHashMethod() { }
	virtual ~CMD5FileHashMethod() {}
public:
	virtual CFileHashMethod* Clone() const
	{
		return new CMD5FileHashMethod; 
	}
	virtual bool MakeFileHash( CBaseFile& file, CDataBlock& digest) const;

	virtual bool MakeFileHash( CBaseFile& file, FILE_DATA_HASH& digest) const;
};

} // namespace helper {

} // namespace PFS {