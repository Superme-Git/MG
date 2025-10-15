#ifndef IMGINFO_H
#define IMGINFO_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\engine\map\pimg.h"
struct sImgInfo
{
	int m_nFileHeight;
	int m_nFileWidth;
	std::vector<Nuclear::PictureHandle> m_HandleArray;
	std::vector<Nuclear::CRECT> m_RectArray;
	BYTE m_nCol;
	BYTE m_nRow;
	POINT m_Positions[4]; 
	POINT m_CenterPoint; 

	sImgInfo& operator = (Nuclear::PImg &obj)
	{
		m_CenterPoint.x = obj.CenterPoint.x;
		m_CenterPoint.y = obj.CenterPoint.y;
		for (int i=0;i<4;i++)
		{
			m_Positions[i].x = obj.pos[i].x;
			m_Positions[i].y = obj.pos[i].y;
		}
		m_nRow = obj.m_nRow;
		m_nCol = obj.m_nCol;
		m_RectArray = obj.m_vectorRct;
		m_HandleArray.clear();
		m_HandleArray.reserve(obj.m_vectorRct.size());
		m_nFileWidth = obj.m_nFileWidth;
		m_nFileHeight = obj.m_nFileHeight;
		return *this;
	}
};

typedef sImgInfo *LPImgInfo;
#endif