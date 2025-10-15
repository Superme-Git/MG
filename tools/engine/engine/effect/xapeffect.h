#ifndef __Nuclear_XAPEFFECT_H
#define __Nuclear_XAPEFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif
#include "..\engine\animanager.h"
#include "effect.h"

namespace Nuclear
{
	class XapEffect : public Effect, public AniLoadingNotify
	{
	//�Զ������͡���Ԫ������
	private:
		friend class EffectManager;

	//���ݳ�Ա��
	private:
		int				m_nPlayOnceTime;	//����һ���ʱ�䣨���룩
		DWORD			m_nFrameTime;		//����һ֡��ʱ�䣨���룩 millisecond per frame
		//��Ϊ��m_ani������ʵ����m_nPlayOnesTime����ͨ��m_nFrameTime����֡���������
		//Ҳ����˵����������Ա���ҽ���һ��������ģ���Ϊ��Ȼm_aniҲ���ٶȵ���Ϣ��
		//������GEffect�����ani�ǿ��Ա����ٵģ���fps����¼��Ҳ����m_nFrameTime�ĵ�����
		//�����Ҹ����������ʵ���ʱ��ȥ��m_nPlayOnesTime��Ա
		//��һ��Ŀ����ֱ��ȥ��m_nPlayOnceTime��
		DWORD			m_nCurFrame;		//��ǰ֡
		XWeakRef<XAni>	m_ani;
		XPCOLOR 		m_vertexColor;		//��ɫ
		XPVECTOR4		m_coloration;		//�������������ɫ
		XPEFFECT_SCALE_TYPE	m_scaleType;	//! ���ſ���
		XHardRef<XAni>	*m_pHoldAni;		//Holdס��ani
		bool			m_bScreenCrood;		//�Ƿ���Ļ����
		bool			m_bHoldAni;			//�Ƿ���סAni���ͷ�

	//����������
	protected:
		XapEffect(EngineBase *pEB);
	public:
		virtual ~XapEffect(void);

	//������
	private:
		void CheckHardAni();
		void ResetPlayOnceTimeAndRelBox();
	public:
		virtual void SetScaleType(XPEFFECT_SCALE_TYPE t) { m_scaleType = t; }
		virtual XPEFFECT_SCALE_TYPE GetScaleType() const { return m_scaleType; }
		virtual XPEffectAssureResResult AssureResource(bool async);
		virtual void OnLoadAniReady(const std::wstring &filename);
		virtual void OnLoadAniBaseReady(const std::wstring &filename);
		virtual void Release();
		void SetFps(float fps);
		float GetFps() const { return (m_state == XPES_EMPTY)?(0.0f):(1000.0f / m_nFrameTime); }
		virtual bool Update(DWORD tickTime);
		virtual int LoadAttribute(XMLIO::CINode& node);
		virtual int SaveAttribute(XMLIO::CONode& node);
		virtual XPEffectType GetType() const { return XPET_ANI; }
		virtual void SetEffectBindType(XPEFFECT_BIND_TYPE t);
		virtual XPEFFECT_BIND_TYPE GetEffectBindType() const;
		virtual void HoldRes();

		//����״̬����
		virtual bool Play();

		//! ��Ⱦ
		virtual bool Render(Canvas* canvas);
		
		//����
		virtual int GetPlayOnceTime() const { return m_nPlayOnceTime; }

		virtual void SetScreenCrood(bool b) { m_bScreenCrood = b; }
		virtual bool IsScreenCrood() const { return m_bScreenCrood; }

		virtual bool GetBase(XPBase &base);
		
		virtual void SetVertexColor( XPCOLOR color)  { m_vertexColor = color; }
		virtual void SetTransparent( float alpha) { m_vertexColor.a = static_cast<unsigned char>(alpha * 255); }
		virtual void Colorate( const XPVECTOR4& color) { m_coloration = color; }

		virtual XPCOLOR GetVertexColor() const { return m_vertexColor; }
		virtual float GetTransparent() const { return static_cast<float>(m_vertexColor.a) / 255.0f; }
		virtual const XPVECTOR4& GetColorate() const { return m_coloration; }

		void SetPlayingFrame(int frame) { m_nCurFrame = frame; }
	};

}


#endif