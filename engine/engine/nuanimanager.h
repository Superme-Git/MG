#ifndef __Nuclear_ANIMANAGER_H__
#define __Nuclear_ANIMANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nupobject.h"
#include "../common/nuxptypes.h"
#include "../common/nuxref.h"
#include "../common/nuxpmaths.h"
#include "platform/mutex.h"

class CSpriteEditorDoc;
class CSpriteEditorView;
class ResourceAnalyser;

namespace Nuclear
{
	enum XPANI_DIRECTIONMODE
	{
		XPANIDM_NULL,
		XPANIDM_8USE1,	// 0, 0, 0, 0, 0, 0, 0, 0
		XPANIDM_8,		// 0, 1, 2, 3, 4, 5, 6, 7
		XPANIDM_4,		// x, 1, x, 3, x, 5, x, 7
		XPANIDM_3,		// x, x, x, 3, 4, 5, x, x
		XPANIDM_8FROM5,	// 0, 1, 2, 3, 4, t, t, t
		XPANIDM_8FROM1,	// t, t, 2, t, t, t, t, t
        XPANIDM_2,		// x, x, x, 3, x, x, x, 7
		XPANIDM_B2FROM1,// x, x, x, 3, x, t, x, x
		XPANIDM_4FROM2,	// x, t, x, 3, x, t, x, 7
	};

	inline NuclearMatrix3* GetRotationScalingMartrixByDirection(NuclearMatrix3 *pOut, const NuclearFPoint *pP, NuclearDirection dir)
	{
		static const float rtable[8] = { -1.5708f, -0.5061f, 0, 0.5061f, 1.5708f, 2.631f, 3.1416f, -2.631f };
		static const float xs[8] = { 0.8f, 0.9f, 1.f, 0.9f, 0.8f, 0.9f, 1.f, 0.9f,}; // todo
		static const float ys[8] = { 1.3f, 1.1f, 1.f, 1.1f, 1.3f, 1.1f, 1.f, 1.1f,};
		XPMatrixScaling(pOut, pP, xs[dir], ys[dir]);
		NuclearMatrix3 tm;
		XPMatrixRotationPoint(&tm, pP, rtable[dir]);
		(*pOut) *= tm;
		return pOut;
	}
	
	class AniPicReadTask;

	/*	class XAni
		动画序列帧，可能包含多个方向，还包含文件格式，压缩格式，底座位置等信息
		可用于表现场景动画，NPC动画
		可单独渲染，也可以作为装备组件配合Action渲染
	*/
	class XAni
	{	
	public:
		enum XPANI_STATE
		{
			XPANISTATE_NULL,
			XPANISTATE_NO_XAP,		//ani文件都没有Load完……
			XPANISTATE_NOPICTURE,	//一张图片都没有Load完……
			XPANISTATE_BASE_PIC,	//至少第一个方向的第一帧是有的，剩下的图都没有Load完……
			XPANISTATE_PICTURE,
		};

		struct XBigPic
		{
			PictureHandle	handle;			 //返回的图片句柄
			std::wstring	filename;
			int				dir;			//所处的方向
			bool			asyncLoad;		//是否为异步加载
			bool			loaded;			//是否已经加载完成
			XBigPic() : handle(INVALID_PICTURE_HANDLE), asyncLoad(false), loaded(false){}
		};

		struct XPic
		{
			XBigPic* pBigPic;
			XBigPic* pBigPicPart;	//为了染色系统而加的分块图
			NuclearRect offset;		//重心调整
			NuclearFRectt srcrect;
			NuclearPolygon outLine; //坐标系:已经切割后的小图片的左上角为原点，右正下正
			XPic() : pBigPic(NULL), pBigPicPart(NULL), srcrect(0.0f, 0.0f, 1.0f, 1.0f) { }
		};

		typedef std::map<int, int>  UsingDirMap;

		XAni(const std::wstring& uri, bool bPerpetualMem) : m_state(XPANISTATE_NULL), m_nRegion(0), m_nFrame(0), m_dirmode(XPANIDM_NULL), m_bindType(XPEBT_ORIGIN), m_sysLevel(0),
			m_pReleaseFlag(XPANI_RELEASE_WAIT_GC), m_uri(uri), m_perpetualmem(bPerpetualMem) {}

		std::wstring GetAniUri() const { return m_uri; }
		//这个pt是相对ani的原点的坐标, r只对alpha模式生效
		bool TestPoint(int dir, int frame, const NuclearPoint &pt, Nuclear_Mouse_TestMode mode, Renderer *pRenderer, int r, float testval);
		bool TestDir(int dir) const;
		int GetFrameCount() const { return m_nFrame; }
		int GetRegionCount() const { return m_nRegion; }
		int GetPlayTime() const { return m_time; }
		void SetPlayTime(int time) { m_time = time; }
		NuclearTextureBlendMode GetBlend() const { return m_blend; }
		void SetBlend(NuclearTextureBlendMode blend) { m_blend = blend; }

		XPANI_STATE GetState() const { return m_state; }
		bool GetPic(int dir, int seq, int reg, XPic &pic);
		bool GetBase(NuclearBase &base) const { base = m_base; return true; }
		void SetBase(const NuclearBase &base) { m_base = base; } // for editor
		bool GetBorder(NuclearRect &border) const { border = m_border; return true; }
		void SetBorder(const NuclearRect &border) { m_border = border; } // for editor
		NuclearImageFileFormat GetFileFmt() const { return m_filefmt; }
		void SetFileFmt(NuclearImageFileFormat fmt) { m_filefmt = fmt; }
		NuclearTextureFormat GetTexFmt() const { return m_texfmt; }
		void SetTexFmt(NuclearTextureFormat fmt) { m_texfmt = fmt; }
		XPANI_DIRECTIONMODE GetDirMode() const { return m_dirmode; }
		int GetDirMask() const;
		Nuclear_EffectBindType GetEffectBindType() const { return m_bindType; }
		void SetEffectBindType(Nuclear_EffectBindType t) { m_bindType = t; }
		int GetSysLevel() const { return m_sysLevel; }
		void SetSysLevel(int sys) { m_sysLevel = sys; }
    
		bool GetReleaseFlag(){ return m_pReleaseFlag > 0;}
		void SetReleaseFlag(NuclearPaniReleaseFlag flag){ m_pReleaseFlag = flag; }

		UsingDirMap& GetUsingDirs() { return m_usingdirs; }
		bool ExistInUsingDirs(int dir);
		void Update();

		bool assureLoadPic(XPic &pic) const;//加载图片资源

	//protected:
	//	core::CMutex mMutexForBuffers;

	private:

		friend class AniManager;
		friend class AniPicReadTask;
		friend class XapReadTask;
		friend class ::CSpriteEditorDoc;
		friend class ::CSpriteEditorView;

		bool SetPic(int dir, int seq, int reg, const XPic& pic);

		//typedef std::map<std::wstring, NuclearBuffer> Buffers;	//图片数据们
		typedef std::vector<XBigPic*> Buffers;
		typedef std::vector<std::vector<XPic> > VPic;	// 帧×区域二维数组
		typedef std::map<int, VPic> XPicMap;

		XPANI_STATE m_state;				// 内部状态
		NuclearImageFileFormat m_filefmt;	// 文件格式
		NuclearTextureFormat m_texfmt;		// 纹理格式
		NuclearTextureBlendMode m_blend;	// blend模式
		int m_time;							// 播放时间
		int m_nRegion;						// 组合区域数
		int m_nFrame;						// 帧数
		XPANI_DIRECTIONMODE m_dirmode;		// 方向模式
		int m_color;						// reserved
		NuclearBase m_base;					// 底座
		NuclearRect m_border;				// 边框
		XPicMap	m_pics;						// 图片id数据(以方向为key)
		UsingDirMap m_usingdirs;			// 当前帧正在使用的方向
		Buffers m_buffers;					// 图片数据们
		Buffers m_buffersPart;				// 图片数据们染色分块
		Nuclear_EffectBindType m_bindType;	// 绑定模式
		int m_sysLevel;						// 播放级别
		NuclearPaniReleaseFlag m_pReleaseFlag;
		unsigned char m_bDyeEnable;			// 是否可染色
		std::wstring m_uri;					// ani文件名
		bool m_perpetualmem;				// 是否永驻内存
	};

	/*
	ani 文件
	*/
	class PAniPack : public NuclearObject
	{
	public:

		struct FileSec
		{
			NuclearRect offset; //(-offsetx, -offsety, -offsetx+width,-offsety+height)
			std::wstring strPicPath; //大图片
			NuclearFRectt rctData; //小图片数据在大图片中的位置,相对于大图片左上角的纹理坐标

			//用于描述物件选中边界的点序列(相对于小图片左上角，水平右竖直下为正方向)
			std::vector<NuclearPoint> outLinePoints; //----版本14加入

			FileSec()
			{
				offset = NuclearRect(0, 0, 0, 0);
				strPicPath = L"";
				rctData = NuclearFRectt(0.0f, 0.0f, 0.0f, 0.0f);
				outLinePoints.clear();
			}
		};

	private:

		friend class AniManager;
		friend class CSpriteEditorDoc;
		friend class CSpriteEditorView;
		friend class ResourceAnalyser;
		enum { 
			VERSION = 15,  // ani 中记录了一个字符串，若此字符串不为空，则用于拼凑图片文件名
			VERSION_14 = 14,	 //ani中记录了一个多边形，用于选中判断
			VERSION_13 = 13, //打包时对素材的alpha值做降噪处理,有可能减小图片尺寸。
			VERSION_12 = 12,
			VERSION_NO_CENTER = 11,
			VERSION_OLD_BASE = 10,
			VERSION_OLDXAP = 9,
			OLD_VERSION_NO_SYS_LEVEL = 8,
			OLD_VERSION_NO_BIND_TYPE = 7,
		};
		/*
		5->6 加边框 m_border
		6->7 方向模式调整
		*/

		int m_nXapVersion;

		int m_filefmt;		// 文件格式
		int m_texfmt;		// 纹理格式
		int m_blend;		// blend模式	
		int m_time;			// 正常播放时间，单位毫秒
		int m_nRegion;		// 组合区域数
		int m_nFrame;
		int m_dirmode;		// 方向模式
		int m_color;
		int m_systemLevel;	//! 播放级别
		Nuclear_EffectBindType m_bindType;		// 绑定模式
		NuclearBase m_base;
		NuclearPoint m_center;
		NuclearRect m_border;		// 边框
		std::vector<int> m_dirs;
		std::vector<std::vector<std::vector<FileSec> > > m_data;
		std::wstring strPicPathPrefix;

	public:
		virtual NuclearOStream& marshal(NuclearOStream &) const;
		virtual const INuclearStream& unmarshal(const INuclearStream &);

		NuclearPoint GetCenter() const { return m_center; }
		void SetCenter(const NuclearPoint &center) { m_center = center; }

		bool GetBase(NuclearBase &base) const { base = m_base; return true; }
		void SetBase(const NuclearBase &base) { m_base = base; } // for editor
		bool GetBorder(NuclearRect &border) const { border = m_border; return true; }
		void SetBorder(const NuclearRect &border) { m_border = border; } // for editor
		NuclearImageFileFormat GetFileFmt() const { return (NuclearImageFileFormat)m_filefmt; }
		NuclearTextureFormat GetTexFmt() const { return (NuclearTextureFormat)m_texfmt; }

		void SetBlend(NuclearTextureBlendMode blend) { m_blend = blend; }
		void SetTime(int time) { m_time = time; }
		void SetEffectBindType(Nuclear_EffectBindType t) { m_bindType = t; }
		void SetSystemLevel(int syslevel) { m_systemLevel = syslevel; }

	};

	/*
	class AniManager 管理所有 XAni 资源的加载和释放，lru引用计数做在这一层
					 同时管理编辑器需要的各种操作
	*/

	class EngineBase;

	class AniLoadingNotify
	{
	public:
		virtual void OnLoadAniReady(const std::wstring &filename) = 0;
		virtual void OnLoadAniBaseReady(const std::wstring &filename) = 0;
	};

	class AniManager
	{
		typedef std::map<std::wstring, NuclearHardRef<XAni>*> ResourceMap;
		typedef std::set<AniLoadingNotify*> LoadingNotifySet;
		typedef std::set<std::wstring> LoadingPicSet;
		struct LoadingPicParam
		{
			LoadingNotifySet notifys;
			LoadingPicSet remainPics;
			NuclearHardRef<XAni> ani;
		};
		typedef std::map<std::wstring, LoadingPicParam> LoadingMap;
	
		struct LoadingXapParam
		{
			LoadingNotifySet notifys;
		};

		typedef std::map<std::wstring, LoadingXapParam> LoadingXapMap;
		EngineBase *m_pEB;
		int			m_nPicGCTime;		//图片gc等待的时间，默认60000（60秒）
		int			m_nXapGCTime;		//ani结构gc等待的时间，默认180000（30分钟）
		int         m_nGcCooldown;		//gc一次的冷却时间，默认10000（10秒）
		int         m_nCooldown;		//冷却时间计时器
    
		// 资源紧张时可以删掉此map中hardref==1的ani，为了测试内存，现在先临时每1024帧清一次weakref==1的
		ResourceMap m_rsrcmap;
		LoadingMap m_LoadingUrls;
		LoadingXapMap m_LoadingXaps;
		core::CMutex mMutexForLoadingXaps;

		//是否每帧单步限时加载纹理
		bool m_bStepLoadTexture;

	private:
		friend class AniPicReadTask;
		friend class XapReadTask;

	public:

		AniManager(EngineBase *pEB);
		~AniManager() { Destroy(); }

	public:
		bool Init() { return true; }
		void Destroy();

		void SetAniPicGCTime(int t) { m_nPicGCTime = t; }
		int GetAniPicGCTime() const { return m_nPicGCTime; }
		void SetAniXapGCTime(int t) { m_nXapGCTime = t; }
		int GetAniXapGCTime() const { return m_nXapGCTime; }
		void SetStepLoadTexture(bool b) { m_bStepLoadTexture = b; }
		bool GetStepLoadTexture() const { return m_bStepLoadTexture; }

		void GarbageCollection(int nowtick);
		void GarbageCollectionAll(bool bImmediately);

		bool SyncLoadAni(NuclearHardRef<XAni>& ani, bool bLookupSegmPak, unsigned char bDye = 0);

		//如果已经load完成返回true，如果正在loading返回false
		Nuclear_EffectAssureResResult AsyncLoadAni(const std::wstring &uri, AniLoadingNotify* pNotify, bool bLookupSegmPak, unsigned char bDye = 0);
		//正常加载完成后不需要Remove，只有在还没有加载完成但这个Notify要被删除了才需要Remove
		//通常会在这个Notify的析构里面调
		void RemoveLoadingNotify(const std::wstring &uri, AniLoadingNotify* pNotify);
		NuclearWeakRef<XAni> GetAni(const std::wstring &uri, bool async, bool bLookupSegmPak, bool bPerpetualMem = false, unsigned char bDye = 0, AniLoadingNotify* pNotify = NULL);

		// 这个方法用于从ani加载 ani，引擎不应该直接调用
		bool LoadAniFromPack(XAni &ani, const PAniPack &pack, const std::wstring &packbaseuri, bool async);

		// 这个方法用于卸载ani的图片
		void FreeAni(XAni &ani, bool straight = false);//straight是否直接无条件删除
		void FreeAniPic(XAni &ani, bool bCullUsingDir = false);
    
		void Run(int delta);
		void setGcCooldown(int cooldown){m_nGcCooldown = cooldown;}
		void Update();

	protected:
		bool syncLoadAni(NuclearHardRef<XAni>& ani, bool bLookupSegmPak);

	};

}

#endif
