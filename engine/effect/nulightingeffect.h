#ifndef __Nuclear_LIGHTINGEFFECT_H__
#define __Nuclear_LIGHTINGEFFECT_H__

#include "../engine/nuanimanager.h"
#include "../nuisprite.h"
#include "nueffect.h"

namespace Nuclear
{
	class lightingEffect : public Effect, public AniLoadingNotify
	{
	//自定义类型、友元、常量
	private:
		friend class EffectManager;
	//数据成员们
	private:
		int				m_nPlayOnceTime;	//播放一遍的时间（毫秒）
		DWORD			m_nFrameTime;		//播放一帧的时间（毫秒） millisecond per frame
		//因为有m_ani，所以实际上m_nPlayOnesTime可以通过m_nFrameTime和总帧数算出来，
		//也就是说上面两个成员有且仅有一个是冗余的，因为虽然m_ani也有速度的信息，
		//但是在GEffect里面的ani是可以被调速的，用fps来记录，也就是m_nFrameTime的倒数，
		//所以我更倾向于在适当的时候去掉m_nPlayOnesTime成员
		//下一步目标是直接去掉m_nPlayOnceTime。
		DWORD			m_nCurFrame;		//当前帧
		NuclearWeakRef<XAni>	m_ani;
		NuclearColor 		m_vertexColor;		//颜色
		NuclearVector4		m_coloration;		//外面设进来的颜色
		Nuclear_EffectScaleType	m_scaleType;	//! 缩放控制
		NuclearHardRef<XAni>	*m_pHoldAni;		//Hold住的ani
		bool			m_bScreenCrood;		//是否屏幕坐标
		bool			m_bHoldAni;			//是否拿住Ani不释放
		float           m_Fps;

	//构造析构～
	protected:
		lightingEffect(EngineBase *pEB);
	public:
		virtual ~lightingEffect(void);

	//方法们
	private:
		void CheckHardAni();
		void ResetPlayOnceTimeAndRelBox();
	public:
		virtual void SetScaleType(Nuclear_EffectScaleType t) { m_scaleType = t; }
		virtual Nuclear_EffectScaleType GetScaleType() const { return m_scaleType; }
		virtual Nuclear_EffectAssureResResult AssureResource(bool async);
		virtual void OnLoadAniReady(const std::wstring &filename);
		virtual void OnLoadAniBaseReady(const std::wstring &filename);
		virtual void Release();
		void SetFps(float fps);
		float GetFps() const { return m_Fps; }
		virtual bool Update(DWORD tickTime);
		virtual int LoadAttribute(LJXML::LJXML_Node<LJXML::Char>* pNode);
		virtual Nuclear_EffectType GetType() const { return XPET_ANI; }
		virtual void SetEffectBindType(Nuclear_EffectBindType t);
		virtual Nuclear_EffectBindType GetEffectBindType() const;
		virtual void HoldRes();

		void SetTargetPos(int posX, int posY);
		void SetLightTime(DWORD appearTime,DWORD disappearTime,DWORD totalTime);
		//播放状态控制
		virtual bool Play();

		//渲染
		virtual bool RenderBottom(Canvas* canvas) { return true; }
		virtual bool Render(Canvas* canvas, bool realRender);
		
		//毫秒
		virtual int GetPlayOnceTime() const { return m_nPlayOnceTime; }

		virtual void SetScreenCrood(bool b) { m_bScreenCrood = b; }
		virtual bool IsScreenCrood() const { return m_bScreenCrood; }

		virtual bool GetBase(NuclearBase &base);
		
		virtual void SetVertexColor( NuclearColor color)  { m_vertexColor = color; }
		virtual void SetTransparent( float alpha) { m_vertexColor.a = static_cast<unsigned char>(alpha * 255); }
		virtual void Colorate( const NuclearVector4& color) { m_coloration = color; }

		virtual NuclearColor GetVertexColor() const { return m_vertexColor; }
		virtual float GetTransparent() const { return static_cast<float>(m_vertexColor.a) / 255.0f; }
		virtual const NuclearVector4& GetColorate() const { return m_coloration; }

		void SetPlayingFrame(int frame) { m_nCurFrame = frame; }

		virtual void Clone(Effect* p);

		void buildLine(int xfrom, int yfrom, int xto, int yto, NuclearFRectt& picRECT, NuclearFRectt& uvRect);
		NuclearTCT1Vertex* m_pPos;
		int m_TargetX;
		int m_TargetY;
		int drawCount;
		int m_nCurLightingTime;
		DWORD m_appearTime;
		DWORD m_disappearTime;
		DWORD m_totalTime;
		DWORD m_CurrentColor;
	};

}


#endif