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
	//! EffectClip的控制器
	//! 所有方法返回false表示不进行控制
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

	//! 特效中的一个片断,片断仅用于控制特效在时间轴上的关键轴
	//! 在片断上可以绑定一个动画对象,详细的控制在动画对象上完成
	class AbstractEffectClip
	{
		friend class Effect;
		friend class ::CToolsEffect;
		friend class ::CToolsSebind;

	protected:
		Effect*			m_ani;

		//! 起始/结束帧,采用[]的形式
		int				m_startFrame;
		int				m_endFrame;			// 如果endFrame为-1,将由ani的播放回调决定当时已经完成
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

		//! 获得指定类型的关键帧数目
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

		//! 在指定帧增加一个KEY,frame是一个相对编号,相对于本个clips的起始编号,返回Key的编号
		//! 要注意的是在增加KEY时可能会影响之前的编号，
		virtual unsigned int AddPosKey( int frame, const FPOINT& v) = 0;
		virtual unsigned int AddScaleKey( int frame, const FPOINT& v) = 0;
		virtual unsigned int AddRotationKey( int frame, float v) = 0;
		virtual unsigned int AddAlphaKey( int frame, float v) = 0;
		virtual unsigned int AddColorKey( int frame, const XPCOLOR& v) = 0;
		virtual unsigned int AddDivideKey( int frame, float v) = 0;


		//! 清除指定编号的关键帧,注意,清除后可能后续影响编号
		virtual bool	RemoveKey( KeyType kt, unsigned int index) = 0;
		//! 清除指定类型的所有关键帧
		virtual void	RemoveKeys( KeyType kt) = 0;

	public:
		struct KeyPoint
		{
			FPOINT	point[3];//0是切线点，1是前控制点，2是后控制点
			int		t;	//01 是x，23是y，偶数是平滑点，奇数是角点
		};

		struct SamplePoint
		{
			int		id;
			float	scalar;	//当scalar >= 2的时候是y
		};
		
		typedef std::vector<KeyPoint> KP_VECTOR;
		typedef std::vector<SamplePoint> SP_VECTOR;

		virtual const KP_VECTOR& GetToolsKeyPoints( KeyType kt) const = 0;
		virtual const SP_VECTOR& GetToolsSamplePoints( KeyType kt) const = 0;
		virtual void SetToolsKeyPoints( KeyType kt, const KP_VECTOR& keyPoints) = 0;
		virtual void SetToolsSamplePoints( KeyType kt, const SP_VECTOR& samPoints) = 0;
		virtual void ClearToolsKeyPoints( KeyType kt) = 0;
		virtual void ClearSamplePoints( KeyType kt) = 0;

		// 设置控制器,有控制器时曲线变换不起作用
		virtual void SetControl( const IEffectClipControl* control) = 0;
		virtual const IEffectClipControl* GetControl() const = 0;
	};


	typedef std::vector<AbstractEffectClip*> AbstractEffectClip_LIST;
	class EffectClip;
	class GEffect;
	class GEffectReadTask;
	typedef std::vector<EffectClip*>	CLIPS_LIST;
	typedef std::vector<CLIPS_LIST>		LAYER_LIST;

	struct GEffectData	//GEffect的XML数据缓存
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
	//自定义类型、友元、常量
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
	//属性们
	protected:
		int				m_totoalFrames;		//! 总帧数，和fps共同决定播放时间
		float			m_fFps;				//! fps，Frame pre second
		int				m_frontBackDivide;	//! 前后端分界线,小于这个数的层为前层,默认为0,即所有层都为后端
		LAYER_LIST		m_clipLayers;		//! 所有层的动画
		XPCOLOR			m_vertexColor;		//! 颜色
		unsigned char   m_type;				//! 如果有声音，那么是声音的类型
		bool			m_bLoadSucc;		//! 异步加载的时候只要有一个子特效加载成功就是成功
		short			m_priority;			//! 如果有声音，那么是声音的优先级
		XPEFFECT_BIND_TYPE m_bindType;		//! 这个特效的对齐类型，只提供Get、Set方法，由外界使用
		XPEFFECT_SCALE_TYPE m_scaleType;	//! 缩放模式（世界在缩放的时候，到底怎么跟着缩放）
		int				m_loadingCount;		//! 还有几个子特效还没有异步加载
		bool			m_bHasXML;			//! 异步加载的时候，XML文件是否加载完毕
		bool			m_bIsRemoveAudio;	//! 是否移除声音
		bool			m_bScreenCrood;		//! 是否屏幕坐标
		bool			m_bHoldRes;			//! 是否握住资源
		bool			m_bIndependentSound;

	//方法们
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
		virtual bool Update(DWORD tickTime);	//返回false就代表该删除了
		virtual int LoadAttribute(XMLIO::CINode& node);
		virtual int SaveAttribute(XMLIO::CONode& node);
		bool Render(Canvas* canvas);
		//! 渲染前端（前端后渲染，也就是说能挡住人）
		void RenderFront(Canvas* canvas);
		//! 渲染后端（后端先渲染，也就是说能被人挡住）
		void RenderBack(Canvas* canvas);

		virtual void SetScreenCrood(bool b);
		virtual bool IsScreenCrood() const { return m_bScreenCrood; }

		virtual void SetScaleType(XPEFFECT_SCALE_TYPE t);
		virtual XPEFFECT_SCALE_TYPE GetScaleType() const { return m_scaleType; }
		
		virtual XPEffectAssureResResult AssureResource(bool async);
		virtual void Release();
		virtual void HoldRes();

		int	GetDivideLayer() const { return m_frontBackDivide; }
		//! 设置前后端分界层
		bool SetDivideLayer( int layer);
		//! fps影响整个特效的播放时长
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
		//不支持曲线颜色
		virtual void Colorate( const XPVECTOR4& color) {}
		virtual XPCOLOR GetVertexColor() const { return m_vertexColor; }
		virtual float GetTransparent() const { return static_cast<float>(m_vertexColor.a) / 255.0f; }
		virtual const XPVECTOR4& GetColorate() const 
		{
			static XPVECTOR4 color(1.0f, 1.0f, 1.0f, 1.0f);
			return color;
		}

		// 如果特效里面有声音，声音的类型和优先级。注意：要在声音播放出来之前设置，否则无效
		virtual void SetSoundType(unsigned char type);
		virtual unsigned char GetSoundType() const { return m_type; }
		virtual void SetSoundPriority(short priority);
		virtual short GetSoundPriority() const { return m_priority; }
		virtual void RemoveAudio();

		//运行时状态
		float GetPlayingTime() const { return m_nCurTime / 1000.0f; }
		float GetPlayingFrame() const { return GetPlayingTime() * m_fFps; }
		//void SetPlayingFrame(int frame) { m_nCurTime = frame * m_frameTime; }//放ToolsEffect里面


		virtual bool Stop();
	};
}
#endif