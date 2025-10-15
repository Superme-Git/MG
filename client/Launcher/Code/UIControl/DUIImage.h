#ifndef DUIImage_H
#define DUIImage_H
#include "../stdafx.h"
#include "../Utils/SystemUtil.h"
#include "DUIBase.h"
using namespace std;

class DUIImage : public DUIBase
{
public:
	wstring m_wsFileName;
	unsigned int m_uiWidthOriginal;
	unsigned int m_uiHeightOriginal;
	unsigned int m_uiColorKey;

	HDC hdcTemp;
	HDC hdcMemory;
	HBITMAP hBitMap;
	HBITMAP hBitOld;
	

	DUIImage(DUIBase* pParent, wstring wsName) :DUIBase(pParent, wsName)
	{
		m_Type = DUIType_Image;
		m_uiWidthOriginal = 0;
		m_uiHeightOriginal = 0;
	}
	~DUIImage()
	{
		SelectObject(hdcMemory, hBitOld);
		DeleteObject(hBitMap);
		DeleteDC(hdcMemory);
		//DeleteDC(hdcTemp);

	}
	int Init(wstring wsFileName, int iX, int iY, unsigned int uiWidth, unsigned int uiHeight, unsigned int uiColorKey)
	{
		m_iX = iX;
		m_iY = iY;
		m_uiWidth = uiWidth;
		m_uiHeight = uiHeight;
		m_uiColorKey = uiColorKey;
		m_wsFileName = wsFileName;
		Image* pImage = NULL;
		pImage = Gdiplus::Image::FromFile(m_wsFileName.c_str());
		if (!pImage)
		{
			return -1;
		}
		m_uiWidthOriginal = pImage->GetWidth();
		m_uiHeightOriginal = pImage->GetHeight();
		hdcTemp = GetDC(0);
		hdcMemory = CreateCompatibleDC(hdcTemp);
		hBitMap = CreateCompatibleBitmap(hdcTemp, uiWidth, uiHeight);
		hBitOld = (HBITMAP)SelectObject(hdcMemory, hBitMap);
		Graphics graph(hdcMemory);
		Point points[] = { Point(0, 0), Point(uiWidth, 0), Point(0, uiHeight) };
		graph.DrawImage(pImage, points, 3);
		graph.ReleaseHDC(hdcMemory);
		delete pImage;
		return 0;
	}
	virtual int DrawMeBefor(HDC hdcBG)
	{
		//StretchBlt(hdcBG, m_iX, m_iY, m_uiWidth, m_uiHeight, hdcMemory, 0, 0, m_uiWidthOriginal, m_uiHeightOriginal, SRCCOPY);
		TransparentBlt(hdcBG, m_iX, m_iY, m_uiWidth, m_uiHeight, hdcMemory, 0, 0, m_uiWidthOriginal, m_uiHeightOriginal, m_uiColorKey);
		return 0;
	}
};
#endif