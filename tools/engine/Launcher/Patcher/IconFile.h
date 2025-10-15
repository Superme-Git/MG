//////////////////////////////////////////////////////////////////////////
//
// IconFile.h
//
// Icon资源处理
//
// author : yanghaibo
// date : 2008-11-14
//
#pragma once

class CIconFile
{
public:
	CIconFile();
	~CIconFile();

	BOOL CreateFromData(LPBYTE pBuff,long lDataSize);

	// TODO: 必要时，实现一个从指定文件加载Icon的方法

	HICON GetHICON()
	{
		return m_hIcon;
	};

	HICON Detach()
	{
		HICON hIcon = m_hIcon;
		m_hIcon = NULL;
		return hIcon;
	};

	void SetIconDesiredSize( int cx, int cy );

private:
	HICON m_hIcon;
	int m_cxDesired;
	int m_cyDesired;
};
