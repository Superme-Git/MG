#pragma once
// CAnalyzePFSStatic
#include <vector>

struct CValidBlock		// 有效信息块
{
	size_t	m_begin;	// 在文件中开始偏移
	size_t	m_end;		// 在文件中结束偏移
	CValidBlock() : m_begin( 0), m_end( 0) {}
};

typedef std::vector<CValidBlock>	CValidBlockVector;

class CAnalyzePFSStatic : public CStatic
{
	DECLARE_DYNAMIC(CAnalyzePFSStatic)

	size_t					m_filelen;		// 文件大小
	size_t					m_fcOffset;		// FileCentral偏移
	CValidBlockVector		m_blocks;		// 所有的空闲块

	COLORREF				m_crValid;		// 存在文件块的颜色
	COLORREF				m_crEmpty;		// 空闲块颜色
	COLORREF				m_crFC;			// FileCentral颜色

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


