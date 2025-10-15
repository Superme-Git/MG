#ifndef __Nuclear_XAPBATCH_H__
#define __Nuclear_XAPBATCH_H__

#include "../common/nuxptypes.h"
#include "../common/nulog.h"
#include "../common/nuxref.h"
#include "../engine/nuenginebase.h"

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
		NuclearWeakRef<XAni>		 m_ani;
		std::vector<NuclearFRectt>   m_Rects;			//矩形们，屏幕坐标
		std::vector<NuclearFRectt>	 m_SrcRects;
		std::wstring		 m_name;			//特效的名字
		bool				 m_bLookupSegmpak;	//是否去查子包

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
		bool IsInSegmPak() const { return m_bLookupSegmpak; }
		void SetSegmPak(bool b) { m_bLookupSegmpak = b;}
		virtual bool SetAni(const std::wstring &xapname);
		virtual bool Update(DWORD tickTime);
		virtual void Render(bool realRender);
		void SetAlphaMask(Nuclear::PictureHandle handle) { m_AlphaMask = handle; }
		//屏幕坐标
		virtual void PushARect(const NuclearFRectt& rect, int type) 
		{
			static NuclearFRectt srcRect[16] = {
				NuclearFRectt(0.0f, 0.0f, 1.0f/4, 1.0f/4), NuclearFRectt(1.0f/4, 0.0f, 1.0f/2, 1.0f/4), NuclearFRectt(1.0f/2, 0.0f, 3.0f/4, 1.0f/4), NuclearFRectt(3.0f/4, 0.0f, 1.0f, 1.0f/4),
				NuclearFRectt(0.0f, 1.0f/4, 1.0f/4, 1.0f/2), NuclearFRectt(1.0f/4, 1.0f/4, 1.0f/2, 1.0f/2), NuclearFRectt(1.0f/2, 1.0f/4, 3.0f/4, 1.0f/2), NuclearFRectt(3.0f/4, 1.0f/4, 1.0f, 1.0f/2),
				NuclearFRectt(0.0f, 1.0f/2, 1.0f/4, 3.0f/4), NuclearFRectt(1.0f/4, 1.0f/2, 1.0f/2, 3.0f/4), NuclearFRectt(1.0f/2, 1.0f/2, 3.0f/4, 3.0f/4), NuclearFRectt(3.0f/4, 1.0f/2, 1.0f, 3.0f/4),
				NuclearFRectt(0.0f, 3.0f/4, 1.0f/4, 1.0f), NuclearFRectt(1.0f/4, 3.0f/4, 1.0f/2, 1.0f), NuclearFRectt(1.0f/2, 3.0f/4, 3.0f/4, 1.0f), NuclearFRectt(3.0f/4, 3.0f/4, 1.0f, 1.0f)
			};
			m_Rects.push_back(rect);
			m_SrcRects.push_back(srcRect[type]);
		}
		virtual void ClearRects() { m_Rects.clear();m_SrcRects.clear();}
	};
}

#endif