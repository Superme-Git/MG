#pragma once
#include <vector>
#include <string>
#include "../../pfs2.h"

class CLayoutManager
{
	CWnd*				m_pWnd;
	std::wstring		m_path;
	bool				m_bfullscreen;// �Ƿ���ȫ��ģʽ��ʾ��������
	bool				m_bstretch;// �Ƿ�������ģʽ��Ⱦ
	bool				m_bfitscreen; // ����ͼ�Ƿ�ǡ���ʺ������С

	int					m_fgWidth; // ǰ��ͼ���
	int					m_fgHeight;// ǰ��ͼ�߶�
	HDC					m_dcBg;		// ���ñ���ͼ��dc

	typedef std::vector<HDC>	CFgVector;
	CFgVector	m_dcFgs;		// ����ǰ��ͼ��dc
	
	CLayoutManager(void);
	~CLayoutManager(void);

	void FullScreen();

	// ʹ���ھ���
	void CenterWindow( int width, int height);

public:
	
	static CLayoutManager& GetInstance();

	// ����
	bool Create( CWnd* pWnd, const std::wstring& path, bool bFullScreen = false);

	// �л�ȫ��
	void ToggleFullScreen();

	// �Ƿ�������������ƶ���������
	bool CanMoveOnDesktop() const
	{
		return !m_bfullscreen && !m_bfitscreen;
	}

	// ������ͼƬ
	bool ReadBackgroundBmp();

	// ��ǰ��ͼƬ
	bool ReadForegroundBmp( int index);
	bool ReadForegroundTga( int index);

	bool Draw( HDC hDes, int index);

	// �� logo.jpg
	bool ReadLogoJpg();

	// �� copyright.txt
	bool ReadCopyrightTxt( PFS::CDataBlock& data);

	size_t GetFgCount() 
	{
		return m_dcFgs.size();
	}
};
