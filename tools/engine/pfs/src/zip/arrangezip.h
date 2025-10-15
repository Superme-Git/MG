#pragma once
#include "zip.h"

namespace PFS
{

namespace helper
{

class CArrangeZip
{
protected:
	CZipFS*							m_zfs;
	CPFSWriter						m_zipwriter;
	CDataBlock						m_out;
	typedef std::vector<ZIPFILE::CFileCentral*>		CFileCentralVector;
	CFileCentralVector				m_vtModify;

	// 设置FileCentral
	// bZipUpdate 返回是否是 ZipUpdate 模式
	void SetFileCentral( ZIPFILE::CFileCentral& fc, const std::wstring& filename, 
		size_t size, unsigned short usMethod, bool& bZipUpdate);
	bool WriteFileData( PFS::CBaseFile* basefile, bool bZipUpdate);

public:
	CArrangeZip( CZipFS* zfs);

	~CArrangeZip() {}

	virtual void Arrange() = 0;
};

// 紧凑方式整理，整理完后的zip文件不会出现浪费的磁盘空间，但速度较慢
class CArrangeZipCompact : public CArrangeZip
{
	CFileCentralVector				m_vtNoModify;
	CFileCentralVector				m_vtLastModify;

	bool							m_bUseTempFile;
	std::wstring					m_realfilename;
	std::wstring					m_tempfilename;
	PFSX::CNativeFile				m_tempfile;

	bool Create();

	void Init();

	void End();

	void WriteNoModify();

	void WriteModify( CFileCentralVector& vFileCentrals );

public:
	CArrangeZipCompact( CZipFS* zfs) : CArrangeZip( zfs), m_bUseTempFile( true) {}

	virtual void Arrange();
};

// 非紧凑方式整理，未修改的文件不动，修改的文件插入到原先“被修改”或“被删除”文件的磁盘空间
// 注意，这种方式也是直接在原文件上修改，如果非法中断则源文件会被损坏，下次无法打开
class CArrangeZipUnCompact : public CArrangeZip
{
	struct CEmptyBlock		// 空闲块信息
	{
		size_t	m_offset;	// 在文件中的偏移
		int		m_size;		// 占用的磁盘空间
		CEmptyBlock() : m_offset( 0), m_size( 0) {}

		friend bool operator<( const CEmptyBlock& left, const CEmptyBlock& right)
		{
			return left.m_size < right.m_size;
		}
	};

	typedef std::vector<CEmptyBlock>	CEmptyBlockVector;
	CEmptyBlockVector	m_vtEmptyBlock;
	size_t			m_lastOffset;

	bool Create();

	void Init();

	void WriteModify();

	void End();

	bool WriteOneModify( ZIPFILE::CFileCentral* central);

	bool WriteModifyCentral( const std::wstring& filename, unsigned short usMethod, size_t filesize,
		CEmptyBlockVector::iterator& iFind, size_t& estimatesize, bool& bZipUpdate);

	// 刷新空闲块
	void RefreshEmptyBlock( CEmptyBlockVector::iterator& iFind, size_t writesize);

	// 估算压缩后大小
	size_t GetDeflateBound( size_t filesize, unsigned short usMethod, bool bZipUpdate);

public:
	CArrangeZipUnCompact( CZipFS* zfs) : CArrangeZip( zfs), m_lastOffset( 0) {}

	virtual void Arrange();

};

// 主要实现选择哪种整理策略
class CArrangeZipManager
{

public:
	CArrangeZipManager( CZipFS* zfs);
};

}	// namespace helper

}	// namespace PFS