#pragma once
#include <vector>
#include <string>
#include "../../pfs2.h"

class CLayoutManager
{
	CWnd*				m_pWnd;
	std::wstring		m_path;
	bool				m_bfullscreen;// 是否以全屏模式显示背景窗口
	bool				m_bstretch;// 是否以拉伸模式渲染
	bool				m_bfitscreen; // 背景图是否恰好适合桌面大小

	int					m_fgWidth; // 前景图宽度
	int					m_fgHeight;// 前景图高度
	HDC					m_dcBg;		// 放置背景图的dc

	typedef std::vector<HDC>	CFgVector;
	CFgVector	m_dcFgs;		// 放置前景图的dc
	
	CLayoutManager(void);
	~CLayoutManager(void);

	void FullScreen();

	// 使窗口居中
	void CenterWindow( int width, int height);

public:
	
	static CLayoutManager& GetInstance();

	// 创建
	bool Create( CWnd* pWnd, const std::wstring& path, bool bFullScreen = false);

	// 切换全屏
	void ToggleFullScreen();

	// 是否可以在桌面上移动背景窗口
	bool CanMoveOnDesktop() const
	{
		return !m_bfullscreen && !m_bfitscreen;
	}

	// 读背景图片
	bool ReadBackgroundBmp();

	// 读前景图片
	bool ReadForegroundBmp( int index);
	bool ReadForegroundTga( int index);

	bool Draw( HDC hDes, int index);

	// 读 logo.jpg
	bool ReadLogoJpg();

	// 读 copyright.txt
	bool ReadCopyrightTxt( PFS::CDataBlock& data);

	size_t GetFgCount() 
	{
		return m_dcFgs.size();
	}
};
