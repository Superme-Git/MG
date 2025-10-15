#pragma once
// CAnalyzePFSStatic
#include <vector>

struct CValidBlock		// ��Ч��Ϣ��
{
	size_t	m_begin;	// ���ļ��п�ʼƫ��
	size_t	m_end;		// ���ļ��н���ƫ��
	CValidBlock() : m_begin( 0), m_end( 0) {}
};

typedef std::vector<CValidBlock>	CValidBlockVector;

class CAnalyzePFSStatic : public CStatic
{
	DECLARE_DYNAMIC(CAnalyzePFSStatic)

	size_t					m_filelen;		// �ļ���С
	size_t					m_fcOffset;		// FileCentralƫ��
	CValidBlockVector		m_blocks;		// ���еĿ��п�

	COLORREF				m_crValid;		// �����ļ������ɫ
	COLORREF				m_crEmpty;		// ���п���ɫ
	COLORREF				m_crFC;			// FileCentral��ɫ

public:
	CAnalyzePFSStatic();
	virtual ~CAnalyzePFSStatic();

	CValidBlockVector& GetValidBlockVector() { return m_blocks; }
	size_t GetFileLen() { return m_filelen; }
	size_t GetFCOffset() { return m_fcOffset; }
	void SetFileLen( size_t len) { m_filelen = len; }
	void SetFCOffset( size_t offset) { m_fcOffset = offset; }

	void SetValidColor( DWORD crValid) { m_crValid = crValid; }
	void SetEmptyColor( DWORD crEmpty) { m_crEmpty = crEmpty; }
	void SetFCColor( DWORD crFC) { m_crFC = crFC; }

protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnPaint();
};


