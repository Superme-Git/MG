#ifndef __Nuclear_GEFFECT_H
#define __Nuclear_GEFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "effect.h"


namespace XMLIO
{
	class CFileReader;
}

class CToolsEffect;
class CToolsSebind;
namespace Nuclear
{
	//! EffectClip�Ŀ�����
	//! ���з�������false��ʾ�����п���
	class IEffectClipControl
	{
	public:
		virtual bool	GetPos( FPOINT& pos)  const { return false; }
		virtual bool	GetScale( FPOINT& scale) const { return false; }
		virtual bool	GetRotationRadian( float& radian) const { return false; }
		virtual bool	GetColor( XPCOLOR& color) const { return false; }
		virtual bool	GetAlpha( float& alpha) const { return false; }

		virtual ~IEffectClipControl() {}
	};

	//! ��Ч�е�һ��Ƭ��,Ƭ�Ͻ����ڿ�����Ч��ʱ�����ϵĹؼ���
	//! ��Ƭ���Ͽ��԰�һ����������,��ϸ�Ŀ����ڶ������������
	class AbstractEffectClip
	{
		friend class Effect;
		friend class ::CToolsEffect;
		friend class ::CToolsSebind;

	protected:
		Effect*			m_ani;

		//! ��ʼ/����֡,����[]����ʽ
		int				m_startFrame;
		int				m_endFrame;			// ���endFrameΪ-1,����ani�Ĳ��Żص�������ʱ�Ѿ����
		int				m_layer;
	public:
		AbstractEffectClip()
			: m_ani( NULL), m_startFrame( 0), m_endFrame( 0), m_layer( 0)
		{}

		virtual ~AbstractEffectClip()
		{
			XPSAFE_DELETE( m_ani);
		}

	public:
		Effect* GetAnimation() { return m_ani; }
		const Effect* GetAnimation() const { return m_ani; }
		void SetAnimation( Effect* ani) { m_ani = ani; }
		int	GetStartFrame() const { return m_startFrame; }
		int	GetEndFrame() const { return m_endFrame; }
		int	GetLayer() const { return m_layer; }

	public:
		enum KeyType
		{
			KT_POS,
			KT_SCALE,
			KT_ROTATION,
			KT_ALPHA,
			KT_COLOR,
			KT_DIVIDE,
		};

		//! ���ָ�����͵Ĺؼ�֡��Ŀ
		virtual unsigned int GetKeyCount( KeyType kt) const = 0;

		virtual bool GetPosKeyValue( unsigned int index, int& frame, FPOINT& v) const = 0;
		virtual bool GetScaleKeyValue( unsigned int index, int& frame, FPOINT& v) const = 0;
		virtual bool GetRotationKeyValue( unsigned int index, int& frame, float& v) const = 0;
		virtual bool GetAlphaKeyValue( unsigned int index, int& frame, float& v) const = 0;
		virtual bool GetColorKeyValue( unsigned int index, int& frame, XPCOLOR& v) const = 0;
		virtual bool GetDivideKeyValue( unsigned int index, int& frame, float& v) const = 0;

		virtual int	GetPosKeyValue( int frame, FPOINT& v) const = 0;
		virtual int	GetScaleKeyValue( int frame, FPOINT& v) const = 0;
		virtual int	GetRotationKeyValue( int frame, float& v) const = 0;
		virtual int	GetAlphaKeyValue( int frame, float& v) const = 0;
		virtual int	GetColorKeyValue( int frame, XPCOLOR& v) const = 0;
		virtual int GetDivideKeyValue( int frame, float& v) const = 0;

		virtual bool SetPosKeyValue( unsigned int index, const FPOINT& v) = 0;
		virtual bool SetScaleKeyValue( unsigned int index, const FPOINT& v) = 0;
		virtual bool SetRotationKeyValue( unsigned int index, float v) = 0;
		virtual bool SetAlphaKeyValue( unsigned int index, float v) = 0;
		virtual bool SetColorKeyValue( unsigned int index, const XPCOLOR& v) = 0;
		virtual bool SetDivideKeyValue( unsigned int index, float v) = 0;

		//! ��ָ��֡����һ��KEY,frame��һ����Ա��,����ڱ���clips����ʼ���,����Key�ı��
		//! Ҫע�����������KEYʱ���ܻ�Ӱ��֮ǰ�ı�ţ�
		virtual unsigned int AddPosKey( int frame, const FPOINT& v) = 0;
		virtual unsigned int AddScaleKey( int frame, const FPOINT& v) = 0;
		virtual unsigned int AddRotationKey( int frame, float v) = 0;
		virtual unsigned int AddAlphaKey( int frame, float v) = 0;
		virtual unsigned int AddColorKey( int frame, const XPCOLOR& v) = 0;
		virtual unsigned int AddDivideKey( int frame, float v) = 0;


		//! ���ָ����ŵĹؼ�֡,ע��,�������ܺ���Ӱ����
		virtual bool	RemoveKey( KeyType kt, unsigned int index) = 0;
		//! ���ָ�����͵����йؼ�֡
		virtual void	RemoveKeys( KeyType kt) = 0;

	public:
		struct KeyPoint
		{
			FPOINT	point[3];//0�����ߵ㣬1��ǰ���Ƶ㣬2�Ǻ���Ƶ�
			int		t;	//01 ��x��23��y��ż����ƽ���㣬�����ǽǵ�
		};

		struct SamplePoint
		{
			int		id;
			float	scalar;	//��scalar >= 2��ʱ����y
		};
		
		typedef std::vector<KeyPoint> KP_VECTOR;
		typedef std::vector<SamplePoint> SP_VECTOR;

		virtual const KP_VECTOR& GetToolsKeyPoints( KeyType kt) const = 0;
		virtual const SP_VECTOR& GetToolsSamplePoints( KeyType kt) const = 0;
		virtual void SetToolsKeyPoints( KeyType kt, const KP_VECTOR& keyPoints) = 0;
		virtual void SetToolsSamplePoints( KeyType kt, const SP_VECTOR& samPoints) = 0;
		virtual void ClearToolsKeyPoints( KeyType kt) = 0;
		virtual void ClearSamplePoints( KeyType kt) = 0;

		// ���ÿ�����,�п�����ʱ���߱任��������
		virtual void SetControl( const IEffectClipControl* control) = 0;
		virtual const IEffectClipControl* GetControl() const = 0;
	};


	typedef std::vector<AbstractEffectClip*> AbstractEffectClip_LIST;
	class EffectClip;
	class GEffect;
	class GEffectReadTask;
	typedef std::vector<EffectClip*>	CLIPS_LIST;
	typedef std::vector<CLIPS_LIST>		LAYER_LIST;

	struct GEffectData	//GEffect��XML���ݻ���
	{
		XMLIO::CFileReader *pFr;
		XMLIO::CINode *pRoot;
		int tick;
		bool bLoading;
		bool Parse(const XBuffer& buffer);
		GEffectData() : pFr(NULL), pRoot(NULL), tick(0), bLoading(true) {}
		~GEffectData();
	};
	class GEffectXMLCaches
	{
	private:
		typedef std::map<std::wstring, GEffectData> GEffectDataMap;
		typedef std::set<GEffect*> GEffectSet;
		typedef std::map<std::wstring, GEffectSet> LoadingGEffects;
		friend class GEffectReadTask;

	private:
		GEffectDataMap m_datas;
		LoadingGEffects m_notifys;
		EngineBase *m_pEngine;
	public:
		GEffectXMLCaches(EngineBase *pEB) : m_pEngine(pEB) {}
		~GEffectXMLCaches() {}

		const GEffectData& GetGEffectData(const std::wstring &name, GEffect *pGEffect, bool async);
		void RemoveNotify(const std::wstring &name, GEffect *pGEffect);

	};

	class GEffect : public Effect, public IEffectNotify
	{
	//�Զ������͡���Ԫ������
	private:
		friend class EffectManager;

	public:
		static const int MAX_CLIP_LAYER = 10;
		enum ERROR_CODE
		{
			EC_S_OK = 0,
			EC_LOAD_NO_RES = 1,
			EC_LOAD_RES_FAILD = 2,
			EC_LOAD_ATTRIBUTE_FAILD = 4,
			EC_LOAD_XML_ERROR = 8,
			EC_PARSE_XML_ERROR = 16
		};
	//������
	protected:
		int				m_totoalFrames;		//! ��֡������fps��ͬ��������ʱ��
		float			m_fFps;				//! fps��Frame pre second
		int				m_frontBackDivide;	//! ǰ��˷ֽ���,С��������Ĳ�Ϊǰ��,Ĭ��Ϊ0,�����в㶼Ϊ���
		LAYER_LIST		m_clipLayers;		//! ���в�Ķ���
		XPCOLOR			m_vertexColor;		//! ��ɫ
		unsigned char   m_type;				//! �������������ô������������
		bool			m_bLoadSucc;		//! �첽���ص�ʱ��ֻҪ��һ������Ч���سɹ����ǳɹ�
		short			m_priority;			//! �������������ô�����������ȼ�
		XPEFFECT_BIND_TYPE m_bindType;		//! �����Ч�Ķ������ͣ�ֻ�ṩGet��Set�����������ʹ��
		XPEFFECT_SCALE_TYPE m_scaleType;	//! ����ģʽ�����������ŵ�ʱ�򣬵�����ô�������ţ�
		int				m_loadingCount;		//! ���м�������Ч��û���첽����
		bool			m_bHasXML;			//! �첽���ص�ʱ��XML�ļ��Ƿ�������
		bool			m_bIsRemoveAudio;	//! �Ƿ��Ƴ�����
		bool			m_bScreenCrood;		//! �Ƿ���Ļ����
		bool			m_bHoldRes;			//! �Ƿ���ס��Դ
		bool			m_bIndependentSound;

	//������
	protected:
		GEffect(EngineBase *pEB);
	public:
		virtual ~GEffect(void);

	protected:
		void ResetAllClip();
		void DeleteAllClip();

		int LoadEffectClips(XMLIO::CINode& root, int flag);

		
	public:
		int OnAsyncLoadedXML(XMLIO::CINode *pRoot);
		virtual void OnAsyncLoaded(IEffect *pEffect, bool succeeded) override;
		int LoadEffect(XMLIO::CINode& root, int flag);
		int LoadEffect(const std::wstring &name);

		virtual XPEffectType GetType() const { return XPET_GEFFECT; }
		virtual void SetEffectBindType(XPEFFECT_BIND_TYPE t) { m_bindType = t; }
		virtual XPEFFECT_BIND_TYPE GetEffectBindType() const { return m_bindType; }
		virtual bool Update(DWORD tickTime);	//����false�ʹ����ɾ����
		virtual int LoadAttribute(XMLIO::CINode& node);
		virtual int SaveAttribute(XMLIO::CONode& node);
		bool Render(Canvas* canvas);
		//! ��Ⱦǰ�ˣ�ǰ�˺���Ⱦ��Ҳ����˵�ܵ�ס�ˣ�
		void RenderFront(Canvas* canvas);
		//! ��Ⱦ��ˣ��������Ⱦ��Ҳ����˵�ܱ��˵�ס��
		void RenderBack(Canvas* canvas);

		virtual void SetScreenCrood(bool b);
		virtual bool IsScreenCrood() const { return m_bScreenCrood; }

		virtual void SetScaleType(XPEFFECT_SCALE_TYPE t);
		virtual XPEFFECT_SCALE_TYPE GetScaleType() const { return m_scaleType; }
		
		virtual XPEffectAssureResResult AssureResource(bool async);
		virtual void Release();
		virtual void HoldRes();

		int	GetDivideLayer() const { return m_frontBackDivide; }
		//! ����ǰ��˷ֽ��
		bool SetDivideLayer( int layer);
		//! fpsӰ��������Ч�Ĳ���ʱ��
		bool SetFps( float fps)
		{
			if (fps <= 0.0f)
				return false;
			m_fFps = fps;
			return true;
		}
		float GetFPS() const { return m_fFps; }
		int GetTotalFrames() const { return m_totoalFrames;}
		virtual int	GetPlayOnceTime() const { return static_cast<int>(m_totoalFrames * 1000 / m_fFps); }
		virtual bool GetBase(XPBase &base);
		virtual void SetVertexColor( XPCOLOR color) { m_vertexColor = color; }
		virtual void SetTransparent( float alpha) { m_vertexColor.a = static_cast<unsigned char>( alpha * 255.0f ); }
		//��֧��������ɫ
		virtual void Colorate( const XPVECTOR4& color) {}
		virtual XPCOLOR GetVertexColor() const { return m_vertexColor; }
		virtual float GetTransparent() const { return static_cast<float>(m_vertexColor.a) / 255.0f; }
		virtual const XPVECTOR4& GetColorate() const 
		{
			static XPVECTOR4 color(1.0f, 1.0f, 1.0f, 1.0f);
			return color;
		}

		// �����Ч���������������������ͺ����ȼ���ע�⣺Ҫ���������ų���֮ǰ���ã�������Ч
		virtual void SetSoundType(unsigned char type);
		virtual unsigned char GetSoundType() const { return m_type; }
		virtual void SetSoundPriority(short priority);
		virtual short GetSoundPriority() const { return m_priority; }
		virtual void RemoveAudio();

		//����ʱ״̬
		float GetPlayingTime() const { return m_nCurTime / 1000.0f; }
		float GetPlayingFrame() const { return GetPlayingTime() * m_fFps; }
		//void SetPlayingFrame(int frame) { m_nCurTime = frame * m_frameTime; }//��ToolsEffect����


		virtual bool Stop();
	};
}
#endif