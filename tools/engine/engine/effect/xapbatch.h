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
		int					 m_nCurFrameTime;	//��ǰ���ŵ�һ֡�е�����
		DWORD				 m_nFrameTime;		//����һ֡��ʱ�䣨���룩 millisecond per frame
		DWORD				 m_nCurFrame;		//��ǰ֡
		XWeakRef<XAni>		 m_ani;
		std::vector<FRECT>   m_Rects;			//�����ǣ���Ļ����
		std::vector<FRECT>	 m_SrcRects;
		std::wstring		 m_name;				//��Ч������

		PictureHandle		 m_AlphaMask;

	//����������
	public:
		XapBatch(EngineBase* m_pEB);
		virtual ~XapBatch(void);

	//������
	private:
		void CheckHardAni();
		//bool CheckAlphaMask();

	public:
		virtual bool SetAni(const std::wstring &xapname);
		virtual bool Update(DWORD tickTime);
		virtual void Render();
		void SetAlphaMask(Nuclear::PictureHandle handle) { m_AlphaMask = handle; }
		//��Ļ����
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