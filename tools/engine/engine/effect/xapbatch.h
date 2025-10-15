#ifndef __Nuclear_XAPBATCH_H
#define __Nuclear_XAPBATCH_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"
#include "..\common\log.h"
#include "..\common\xref.h"
#include "..\engine\enginebase.h"

namespace Nuclear
{
	class XAni;
	class XapBatch
	{
	private:
		EngineBase*			 m_pEB;
		int					 m_nCurFrameTime;	//当前播放到一帧中的哪里
		DWORD				 m_nFrameTime;		//播放一帧的时间（毫秒） millisecond per frame
		DWORD				 m_nCurFrame;		//当前帧
		XWeakRef<XAni>		 m_ani;
		std::vector<FRECT>   m_Rects;			//矩形们，屏幕坐标
		std::vector<FRECT>	 m_SrcRects;
		std::wstring		 m_name;				//特效的名字

		PictureHandle		 m_AlphaMask;

	//构造析构～
	public:
		XapBatch(EngineBase* m_pEB);
		virtual ~XapBatch(void);

	//方法们
	private:
		void CheckHardAni();
		//bool CheckAlphaMask();

	public:
		virtual bool SetAni(const std::wstring &xapname);
		virtual bool Update(DWORD tickTime);
		virtual void Render();
		void SetAlphaMask(Nuclear::PictureHandle handle) { m_AlphaMask = handle; }
		//屏幕坐标
		virtual void PushARect(const FRECT& rect, int type) 
		{
			static FRECT srcRect[16] = {
				FRECT(0.0f, 0.0f, 1.0f/4, 1.0f/4), FRECT(1.0f/4, 0.0f, 1.0f/2, 1.0f/4), FRECT(1.0f/2, 0.0f, 3.0f/4, 1.0f/4), FRECT(3.0f/4, 0.0f, 1.0f, 1.0f/4),
				FRECT(0.0f, 1.0f/4, 1.0f/4, 1.0f/2), FRECT(1.0f/4, 1.0f/4, 1.0f/2, 1.0f/2), FRECT(1.0f/2, 1.0f/4, 3.0f/4, 1.0f/2), FRECT(3.0f/4, 1.0f/4, 1.0f, 1.0f/2),
				FRECT(0.0f, 1.0f/2, 1.0f/4, 3.0f/4), FRECT(1.0f/4, 1.0f/2, 1.0f/2, 3.0f/4), FRECT(1.0f/2, 1.0f/2, 3.0f/4, 3.0f/4), FRECT(3.0f/4, 1.0f/2, 1.0f, 3.0f/4),
				FRECT(0.0f, 3.0f/4, 1.0f/4, 1.0f), FRECT(1.0f/4, 3.0f/4, 1.0f/2, 1.0f), FRECT(1.0f/2, 3.0f/4, 3.0f/4, 1.0f), FRECT(3.0f/4, 3.0f/4, 1.0f, 1.0f)
			};
			m_Rects.push_back(rect);
			m_SrcRects.push_back(srcRect[type]);
		}
		virtual void ClearRects() { m_Rects.clear();m_SrcRects.clear();}
	};
}

#endif