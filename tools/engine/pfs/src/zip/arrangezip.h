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

	// ����FileCentral
	// bZipUpdate �����Ƿ��� ZipUpdate ģʽ
	void SetFileCentral( ZIPFILE::CFileCentral& fc, const std::wstring& filename, 
		size_t size, unsigned short usMethod, bool& bZipUpdate);
	bool WriteFileData( PFS::CBaseFile* basefile, bool bZipUpdate);

public:
	CArrangeZip( CZipFS* zfs);

	~CArrangeZip() {}

	virtual void Arrange() = 0;
};

// ���շ�ʽ������������zip�ļ���������˷ѵĴ��̿ռ䣬���ٶȽ���
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

// �ǽ��շ�ʽ����δ�޸ĵ��ļ��������޸ĵ��ļ����뵽ԭ�ȡ����޸ġ��򡰱�ɾ�����ļ��Ĵ��̿ռ�
// ע�⣬���ַ�ʽҲ��ֱ����ԭ�ļ����޸ģ�����Ƿ��ж���Դ�ļ��ᱻ�𻵣��´��޷���
class CArrangeZipUnCompact : public CArrangeZip
{
	struct CEmptyBlock		// ���п���Ϣ
	{
		size_t	m_offset;	// ���ļ��е�ƫ��
		int		m_size;		// ռ�õĴ��̿ռ�
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

	// ˢ�¿��п�
	void RefreshEmptyBlock( CEmptyBlockVector::iterator& iFind, size_t writesize);

	// ����ѹ�����С
	size_t GetDeflateBound( size_t filesize, unsigned short usMethod, bool bZipUpdate);

public:
	CArrangeZipUnCompact( CZipFS* zfs) : CArrangeZip( zfs), m_lastOffset( 0) {}

	virtual void Arrange();

};

// ��Ҫʵ��ѡ�������������
class CArrangeZipManager
{

public:
	CArrangeZipManager( CZipFS* zfs);
};

}	// namespace helper

}	// namespace PFS