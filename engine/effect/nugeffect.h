#ifndef __Nuclear_GEFFECT_H__
#define __Nuclear_GEFFECT_H__

#include "nueffect.h"

class ToolsEffect;
class ToolsSebind;
namespace Nuclear
{
	//! EffectClip的控制器
	//! 所有方法返回false表示不进行控制
	class IEffectClipControl
	{
	public:
		virtual bool	GetPos( NuclearFPoint& pos)  const { return false; }
		virtual bool	GetScale( NuclearFPoint& scale) const { return false; }
		virtual bool	GetRotationRadian( float& radian) const { return false; }
		virtual bool	GetColor( NuclearColor& color) const { return false; }
		virtual bool	GetAlpha( float& alpha) const { return false; }

		virtual ~IEffectClipControl() {}
	};

	//! 特效中的一个片断,片断仅用于控制特效在时间轴上的关键轴
	//! 在片断上可以绑定一个动画对象,详细的控制在动画对象上完成
	class AbstractEffectClip
	{
		friend class Effect;
		friend class ::ToolsEffect;
		friend class ::ToolsSebind;

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
		Effect*	GetAnimation() { return m_ani; }
		const Effect* GetAnimation() const { return m_ani; }
		void	SetAnimation( Effect* ani) { m_ani = ani; }
		int		GetStartFrame() const { return m_startFrame; }
		void	SetStartFrame(int startFrame) { m_startFrame = startFrame; }
		int		GetEndFrame() const { return m_endFrame; }
		void	SetEndFrame(int endFrame) { m_endFrame = endFrame; }
		int		GetLayer() const { return m_layer; }
		void	SetLayer(int layer) { m_layer = layer; }

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

		virtual bool GetPosKeyValue( unsigned int index, int& frame, NuclearFPoint& v) const = 0;
		virtual bool GetScaleKeyValue( unsigned int index, int& frame, NuclearFPoint& v) const = 0;
		virtual bool GetRotationKeyValue( unsigned int index, int& frame, float& v) const = 0;
		virtual bool GetAlphaKeyValue( unsigned int index, int& frame, float& v) const = 0;
		virtual bool GetColorKeyValue( unsigned int index, int& frame, NuclearColor& v) const = 0;
		virtual bool GetDivideKeyValue( unsigned int index, int& frame, float& v) const = 0;

		virtual int	GetPosKeyValue( int frame, NuclearFPoint& v) const = 0;
		virtual int	GetScaleKeyValue( int frame, NuclearFPoint& v) const = 0;
		virtual int	GetRotationKeyValue( int frame, float& v) const = 0;
		virtual int	GetAlphaKeyValue( int frame, float& v) const = 0;
		virtual int	GetColorKeyValue( int frame, NuclearColor& v) const = 0;
		virtual int GetDivideKeyValue( int frame, float& v) const = 0;

		virtual bool SetPosKeyValue( unsigned int index, const NuclearFPoint& v) = 0;
		virtual bool SetScaleKeyValue( unsigned int index, const NuclearFPoint& v) = 0;
		virtual bool SetRotationKeyValue( unsigned int index, float v) = 0;
		virtual bool SetAlphaKeyValue( unsigned int index, float v) = 0;
		virtual bool SetColorKeyValue( unsigned int index, const NuclearColor& v) = 0;
		virtual bool SetDivideKeyValue( unsigned int index, float v) = 0;

		//! 在指定帧增加一个KEY,frame是一个相对编号,相对于本个clips的起始编号,返回Key的编号
		//! 要注意的是在增加KEY时可能会影响之前的编号，
		virtual unsigned int AddPosKey( int frame, const NuclearFPoint& v) = 0;
		virtual unsigned int AddScaleKey( int frame, const NuclearFPoint& v) = 0;
		virtual unsigned int AddRotationKey( int frame, float v) = 0;
		virtual unsigned int AddAlphaKey( int frame, float v) = 0;
		virtual unsigned int AddColorKey( int frame, const NuclearColor& v) = 0;
		virtual unsigned int AddDivideKey( int frame, float v) = 0;

		//! 清除指定编号的关键帧,注意,清除后可能后续影响编号
		virtual bool	RemoveKey( KeyType kt, unsigned int index) = 0;
		//! 清除指定类型的所有关键帧
		virtual void	RemoveKeys( KeyType kt) = 0;

	public:
		struct KeyPoint
		{
			NuclearFPoint	point[3];//0是切线点，1是前控制点，2是后控制点
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

	class GEffect : public Effect, public IEffectNotify
	{
	//自定义类型、友元、常量
	private:
		friend class EffectManager;
	public:
		typedef std::vector<EffectClip*>	CLIPS_LIST;
		typedef std::vector<CLIPS_LIST>		LAYER_LIST;
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
		NuclearColor			m_vertexColor;		//! 颜色
		unsigned char   m_type;				//! 如果有声音，那么是声音的类型
		Nuclear_EffectBindType m_bindType;		//! 这个特效的对齐类型，只提供Get、Set方法，由外界使用
		bool			m_bIndependentSound;//! 是否有独立的声音Clip文件
		LAYER_LIST		m_clipLayers;		//! 所有层的动画
		bool			m_bLoadSucc;		//! 异步加载的时候只要有一个子特效加载成功就是成功
		short			m_priority;			//! 如果有声音，那么是声音的优先级
		int				m_loadingCount;		//! 还有几个子特效还没有异步加载

	//方法们
	protected:
		GEffect(EngineBase *pEB);
	public:
		virtual ~GEffect(void);

	protected:
		void ResetAllClip();
		void DeleteAllClip();

		int LoadEffectClips(LJXML::LJXML_Node<LJXML::Char>* pRoot, int flag);
		
	public:
		virtual void OnAsyncLoaded(IEffect *pEffect, bool succeeded);

		int LoadEffect(const std::wstring &name);
		int LoadEffect(LJXML::LJXML_Node<LJXML::Char>* pRoot, int flag);

		virtual Nuclear_EffectType GetType() const { return XPET_GEFFECT; }
		virtual void SetEffectBindType(Nuclear_EffectBindType t) { m_bindType = t; }
		virtual Nuclear_EffectBindType GetEffectBindType() const { return m_bindType; }
		virtual bool Update(DWORD tickTime);	//返回false就代表该删除了
		virtual int LoadAttribute(LJXML::LJXML_Node<LJXML::Char>* pNode);
		
		virtual bool RenderBottom(Canvas* canvas) {	return true; }
		bool Render(Canvas* canvas, bool realRender);
		//! 渲染前端（前端后渲染，也就是说能挡住人）
		void RenderFront(Canvas* canvas, bool realRender);
		//! 渲染后端（后端先渲染，也就是说能被人挡住）
		void RenderBack(Canvas* canvas, bool realRender);

		virtual void SetScreenCrood(bool b);
		virtual bool IsScreenCrood() const;

		virtual void SetScaleType(Nuclear_EffectScaleType t);
		virtual Nuclear_EffectScaleType GetScaleType() const;
		
		virtual Nuclear_EffectAssureResResult AssureResource(bool async);
		virtual void Release();
		virtual void HoldRes();

		int		GetDivideLayer() const { return m_frontBackDivide; }
		//! 设置前后端分界层
		bool	SetDivideLayer( int layer);
	
		bool	GetIndependentSound() const { return m_bIndependentSound; }
		void	SetIndependentSound(bool independentSound) { m_bIndependentSound = independentSound; }

		//! fps影响整个特效的播放时长
		bool SetFps( float fps)
		{
			if (fps <= 0.0f)
				return false;
			m_fFps = fps;
			//m_frameTime = static_cast<DWORD>(1000 / fps);
			return true;
		}
		float	GetFPS() const { return m_fFps; }
		int		GetTotalFrames() const { return m_totoalFrames;}
		void	SetTotalFrames(int totoalFrames) { m_totoalFrames = totoalFrames; }
		virtual int	GetPlayOnceTime() const { return m_fFps > 0 ? static_cast<int>(m_totoalFrames * 1000 / m_fFps): 0; }
		virtual bool GetBase(NuclearBase &base);
		virtual void SetVertexColor( NuclearColor color) { m_vertexColor = color; }
		virtual void SetTransparent( float alpha) { m_vertexColor.a = static_cast<unsigned char>( alpha * 255.0f ); }
		virtual void AddEffectClip(EffectClip* clip);

		//不支持曲线颜色
		virtual void Colorate( const NuclearVector4& color) {}
		virtual NuclearColor GetVertexColor() const { return m_vertexColor; }
		virtual float GetTransparent() const { return static_cast<float>(m_vertexColor.a) / 255.0f; }
		virtual const NuclearVector4& GetColorate() const  { static NuclearVector4 color(1.0f, 1.0f, 1.0f, 1.0f); return color; }

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

		virtual void Clone(Effect* p);
	};
}

#endif
