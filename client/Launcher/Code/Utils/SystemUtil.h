#ifndef SystemUtil_H
#define SystemUtil_H

#include "../stdafx.h"

#include <gdiplus.h>
#include "FileUtil.h"

using namespace std;
using namespace Gdiplus;

static std::wstring gs_wsRootPath;

static std::wstring GetRootPath()
{
	if (gs_wsRootPath == L"")
	{
		wchar_t wchrFilePath[MAX_PATH] = { 0 };
		GetModuleFileName(NULL, wchrFilePath, MAX_PATH);
		std::wstring wsFilePath = wchrFilePath;
		std::wstring wsFilePathNew = ReplaceStringW(wsFilePath, L"\\", L"/");
		std::wstring wsResult = GetFilePath(wsFilePathNew, L"");
		gs_wsRootPath = wsResult;
#ifdef DEBUG
		gs_wsRootPath = gs_wsRootPath + L"../../resource/";
#endif
	}
	return gs_wsRootPath;
}
static int gettimeofday(struct timeval * val, struct timezone *)
{
	if (val)
	{
		LARGE_INTEGER liTime, liFreq;
		QueryPerformanceFrequency(&liFreq);
		QueryPerformanceCounter(&liTime);
		val->tv_sec = (long)(liTime.QuadPart / liFreq.QuadPart);
		val->tv_usec = (long)(liTime.QuadPart * 1000000.0 / liFreq.QuadPart - val->tv_sec * 1000000.0);
	}
	return 0;
}
static wstring GetRandomNumStr()
{
	wstring wsResult;
	struct timeval now;
	if (gettimeofday(&now, NULL) != 0)
	{
		return wsResult;
	}
	long CurSec = now.tv_sec + now.tv_usec / 1000000;
	if (CurSec < 0)
	{
		CurSec = CurSec * -1;
	}
	while (CurSec > 4294967296 - 1)
	{
		CurSec = CurSec / 2;
	}
	unsigned int uiSec = CurSec;
	wsResult = GetStringUtil().UI2WS(uiSec);
	return wsResult;
}

static int GetImageSize(std::wstring wsSrcFile, unsigned int& uiWidth, unsigned int& uiHeight)
{
	Image* pImage = NULL;
	pImage = Gdiplus::Image::FromFile(wsSrcFile.c_str());
	if (pImage)
	{
		uiWidth = pImage->GetWidth();
		uiHeight = pImage->GetHeight();
		delete pImage;
		return 0;
	}
	return -1;
}
static int CreateRgnFromDC(HDC hdcSrc, unsigned int uiWidth, unsigned int uiHeight, unsigned int uiColorKey, HRGN& hRgnResult)
{
	hRgnResult = CreateRectRgn(0, 0, 0, 0);
	int iResult = 0;
	for (int y = 0; y < uiHeight; y++)
	{
		for (int x = 0; x < uiWidth; x++)
		{
			COLORREF CurColor = GetPixel(hdcSrc, x, y);
			//unsigned int uiColorKeyR = GetRValue(CurColor);
			//unsigned int uiColorKeyG = GetGValue(CurColor);
			//unsigned int uiColorKeyB = GetBValue(CurColor);
			//unsigned int uiColorKeyRGB = RGB(50, 250, 50);
			if (CurColor != uiColorKey)
			{
				HRGN hRgnCur = CreateRectRgn(x, y, x + 1, y + 1);
				CombineRgn(hRgnResult, hRgnResult, hRgnCur, RGN_OR);
				DeleteObject(hRgnCur);
				iResult++;
			}
		}
	}
	return 0;
}
static int CreateRgnFromHandle(HWND hWndSrc, unsigned int uiWidth, unsigned int uiHeight, unsigned int uiColorKey, HRGN& hRgnResult)
{
	HDC hdcSrc = GetWindowDC(hWndSrc);
	int iResult = CreateRgnFromDC(hdcSrc, uiWidth, uiHeight, uiColorKey, hRgnResult);
	return iResult;
}
static int CreateRgnFromImageFile(std::wstring wsSrcFile, HRGN& hRgnResult, unsigned int uiColorKey)
{
	Image* pImage = NULL;
	pImage = Gdiplus::Image::FromFile(wsSrcFile.c_str());
	if (!pImage)
	{
		return -1;
	}
	unsigned int uiWidth = pImage->GetWidth();
	unsigned int uiHeight = pImage->GetHeight();
	HDC hdcTemp = GetDC(0);
	HDC hdcMemory = CreateCompatibleDC(hdcTemp);
	HBITMAP hBitMap = CreateCompatibleBitmap(hdcTemp, uiWidth, uiHeight);
	HBITMAP hBitOld = (HBITMAP)SelectObject(hdcMemory, hBitMap);
	Graphics graph(hdcMemory);
	Point points[] = { Point(0, 0), Point(uiWidth, 0), Point(0, uiHeight) };
	graph.DrawImage(pImage, points, 3);
	int iResult = CreateRgnFromDC(hdcMemory, uiWidth, uiHeight, uiColorKey, hRgnResult);
	graph.ReleaseHDC(hdcMemory);
	SelectObject(hdcMemory, hBitOld);
	DeleteObject(hBitMap);
	DeleteDC(hdcMemory);
	//DeleteDC(hdcTemp);
	return iResult;
}
static int GetRgnDataFromRgn(HRGN hRgnSrc, RGNDATA*& pRgnData)
{
	int iRgnDataSize = GetRegionData(hRgnSrc, 0, 0);
	pRgnData = (RGNDATA*)new unsigned char[iRgnDataSize];
	GetRegionData(hRgnSrc, iRgnDataSize, pRgnData);
	return iRgnDataSize;
}
static int SaveRgnToFile(HRGN hRgnSrc, std::wstring wsDstFile)
{
	RGNDATA* pRgnData;
	int iRgnDataSize = GetRgnDataFromRgn(hRgnSrc, pRgnData);
	PutDataToFileByFS((unsigned char*)&pRgnData[0], iRgnDataSize, wsDstFile);
	delete[] pRgnData;
	return 0;
}
static int GetRgnFromRgnData(RGNDATA* pRgnDataSrc, HRGN& hRgnDst)
{
	hRgnDst = ExtCreateRegion(0, pRgnDataSrc->rdh.dwSize + pRgnDataSrc->rdh.nRgnSize, pRgnDataSrc);
	return 0;
}
static int LoadRgnFormFile(std::wstring wsSrcFile, HRGN& hRgnDst)
{
	unsigned int uiRgnDataSize;
	RGNDATA* pRgnData = NULL;
	GetDataFromFileByFS(wsSrcFile, (unsigned char*&)pRgnData, uiRgnDataSize);
	GetRgnFromRgnData(pRgnData, hRgnDst);
	return 0;
}
//Save
//HRGN hRgnResult = CreateRectRgn(0, 0, 0, 0);
//CreateRgnFromImageFile(L"Launcher.png", hRgnResult);
//SaveRgnToFile(hRgnResult, L"RgnData.bin");
//DeleteObject(hRgnResult);

//Load And Set
//HRGN hRgnResult2;
//LoadRgnFormFile(L"RgnData.bin", hRgnResult2);
//SetWindowRgn(m_hWnd, hRgnResult2, true);
//DeleteObject(hRgnResult2);

#endif