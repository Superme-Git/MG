#ifndef __Nuclear_ANIMANAGER_H
#define __Nuclear_ANIMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\pobject.h"
#include "..\common\xptypes.h"
#include "..\common\xref.h"
#include "..\common\xpmaths.h"

class CSpriteEditorDoc;
class CSpriteEditorView;

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
		MAX_DIRMODE,
	};


	inline XPMATRIX3* GetRotationScalingMartrixByDirection(XPMATRIX3 *pOut, const FPOINT *pP, XPDIRECTION dir)
	{
		static const float rtable[8] = { -1.5708f, -0.5061f, 0, 0.5061f, 1.5708f, 2.631f, 3.1416f, -2.631f };
		static const float xs[8] = { 0.8f, 0.9f, 1.f, 0.9f, 0.8f, 0.9f, 1.f, 0.9f,}; // todo
		static const float ys[8] = { 1.3f, 1.1f, 1.f, 1.1f, 1.3f, 1.1f, 1.f, 1.1f,};
		XPMatrixScaling(pOut, pP, xs[dir], ys[dir]);
		XPMATRIX3 tm;
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
		PictureHandle handle;			// Renderer 返回的图片句柄
		std::wstring filename;
		XBigPic() : handle(INVALID_PICTURE_HANDLE) {}
	};

	struct XPic
	{
		XBigPic* pBigPic;
		XBigPic* pBigPicPart;
		CRECT offset;					// 重心调整
		FRECT srcrect;
		XPPolygon outLine; //坐标系:已经切割后的小图片的左上角为原点，右正下正
		XPic() : pBigPic(NULL), pBigPicPart(NULL), srcrect(0.0f, 0.0f, 1.0f, 1.0f) { }
		//bool Check(Renderer *pRenderer);
	};


	XAni() : m_state(XPANISTATE_NULL), m_nFrame(0), m_dirmode(XPANIDM_NULL), m_bindType(XPEBT_ORIGIN), m_sysLevel(0) { }

	//这个pt是相对ani的原点的坐标, r只对alpha模式生效
	bool TestPoint(int dir, int frame, const CPOINT &pt, XPMOUSE_TEST_MODE mode, Renderer *pRenderer, int r, float testval);
	bool TestDir(int dir) const;
	int GetFrameCount() const { return m_nFrame; }
	int GetRegionCount() const { return m_nRegion; }
	int GetPlayTime() const { return m_time; }
	void SetPlayTime(int time) { m_time = time; }
	XPTEXTURE_BLENDMODE GetBlend() const { return m_blend; }
	void SetBlend(XPTEXTURE_BLENDMODE blend) { m_blend = blend; }

	XPANI_STATE GetState() const { return m_state; }
	bool GetPic(int dir, int seq, int reg, XPic &pic) const;
	bool GetBase(XPBase &base) const { base = m_base; return true; }
	void SetBase(const XPBase &base) { m_base = base; } // for editor
	bool GetBorder(CRECT &border) const { border = m_border; return true; }
	void SetBorder(const CRECT &border) { m_border = border; } // for editor
	XPIMAGE_FILEFORMAT GetFileFmt() const { return m_filefmt; }
	void SetFileFmt(XPIMAGE_FILEFORMAT fmt) { m_filefmt = fmt; }
	XPTEXTURE_FORMAT GetTexFmt() const { return m_texfmt; }
	void SetTexFmt(XPTEXTURE_FORMAT fmt) { m_texfmt = fmt; }
	XPANI_DIRECTIONMODE GetDirMode() const { return m_dirmode; }
	int GetDirMask() const;
	XPEFFECT_BIND_TYPE GetEffectBindType() const { return m_bindType; }
	void SetEffectBindType(XPEFFECT_BIND_TYPE t) { m_bindType = t; }
	int GetSysLevel() const { return m_sysLevel; }
	void SetSysLevel(int sys) { m_sysLevel = sys; }
private:

	friend class AniManager;
	friend class AniPicReadTask;
	friend class XapReadTask;
	friend class ::CSpriteEditorDoc;
	friend class ::CSpriteEditorView;

	bool SetPic(int dir, int seq, int reg, const XPic& pic);

	//typedef std::map<std::wstring, XBuffer> Buffers;	//图片数据们
	typedef std::vector<XBigPic*> Buffers;
	typedef std::vector<std::vector<XPic> > VPic;	// 帧×区域二维数组
	typedef std::map<int, VPic> XPicMap;

	XPANI_STATE m_state;				// 内部状态
	XPIMAGE_FILEFORMAT m_filefmt;		// 文件格式
	XPTEXTURE_FORMAT m_texfmt;			// 纹理格式
	XPTEXTURE_BLENDMODE m_blend;		// blend模式
	int m_time;							// 播放时间
	int m_nRegion;						// 组合区域数
	int m_nFrame;						// 帧数
	XPANI_DIRECTIONMODE m_dirmode;		// 方向模式
	int m_color;						// reserved
	XPBase m_base;						// 底座
	CRECT m_border;						// 边框
	XPicMap	m_pics;						// 图片id数据
	Buffers m_buffers;					// 图片数据们
	Buffers m_buffersPart;				// 分块图片数据们
	XPEFFECT_BIND_TYPE m_bindType;		// 绑定模式
	int m_sysLevel;						// 播放级别

};

/*
ani 文件
*/
class PAniPack : public PObject
{
public:

	struct FileSec
	{
		CRECT offset; //(-offsetx, -offsety, -offsetx+width,-offsety+height)
		std::wstring strPicPath; //大图片
		FRECT rctData; //小图片数据在大图片中的位置,相对于大图片左上角的纹理坐标

		//用于描述物件选中边界的点序列(相对于小图片左上角，水平右竖直下为正方向)
		std::vector<CPOINT> outLinePoints; //----版本14加入

		FileSec()
		{
			offset = CRECT(0, 0, 0, 0);
			strPicPath = L"";
			rctData = FRECT(0.0f, 0.0f, 0.0f, 0.0f);
			outLinePoints.clear();
		}
	};

private:

	friend class AniManager;
	friend class CSpriteEditorDoc;
	friend class CSpriteEditorView;
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
	XPEFFECT_BIND_TYPE m_bindType;		// 绑定模式
	XPBase m_base;
	CPOINT m_center;
	CRECT m_border;		// 边框
	std::vector<int> m_dirs;
	std::vector<std::vector<std::vector<FileSec> > > m_data;
	std::wstring strPicPathPrefix;

public:
	virtual XOStream& marshal(XOStream &) const;
	virtual const XIStream& unmarshal(const XIStream &);

	int GetFrameCount() const { return m_nFrame; }
	int GetRegionCount() const { return m_nRegion; }
	int GetPlayTime() const { return m_time; }
	void SetPlayTime(int time) { m_time = time; }

	CPOINT GetCenter() const { return m_center; }
	void SetCenter(const CPOINT &center) { m_center = center; }

	bool GetBase(XPBase &base) const { base = m_base; return true; }
	void SetBase(const XPBase &base) { m_base = base; } // for editor
	bool GetBorder(CRECT &border) const { border = m_border; return true; }
	void SetBorder(const CRECT &border) { m_border = border; } // for editor
	XPIMAGE_FILEFORMAT GetFileFmt() const { return (XPIMAGE_FILEFORMAT)m_filefmt; }
	XPTEXTURE_FORMAT GetTexFmt() const { return (XPTEXTURE_FORMAT)m_texfmt; }

	void SetBlend(XPTEXTURE_BLENDMODE blend) { m_blend = blend; }
	void SetTime(int time) { m_time = time; }
	void SetEffectBindType(XPEFFECT_BIND_TYPE t) { m_bindType = t; }
	void SetSystemLevel(int syslevel) { m_systemLevel = syslevel; }

	std::vector<int>& GetDirs() { return m_dirs; }
	std::vector<std::vector<std::vector<FileSec> > >& GetDatas() { return m_data; }

	const std::wstring& GetPicPathPrefix() const { return strPicPathPrefix; }
	void SetPicPathPrefix(const std::wstring& val) { strPicPathPrefix = val; }
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
	typedef std::map<std::wstring, XHardRef<XAni>*> ResourceMap;
	typedef std::set<AniLoadingNotify*> LoadingNotifySet;
	typedef std::set<std::wstring> LoadingPicSet;
	struct LoadingPicParam
	{
		LoadingNotifySet notifys;
		LoadingPicSet remainPics;
		XHardRef<XAni> ani;
	};
	typedef std::map<std::wstring, LoadingPicParam> LoadingMap;
	
	struct LoadingXapParam
	{
		LoadingNotifySet notifys;
	};

	typedef std::map<std::wstring, LoadingXapParam> LoadingXapMap;
	EngineBase *m_pEB;
	int			m_nPicGCTime;		//图片gc等待的时间，默认20000（20秒）
	int			m_nXapGCTime;		//ani结构gc等待的时间，默认1200000（20分钟）

	// 资源紧张时可以删掉此map中hardref==1的ani，为了测试内存，现在先临时每1024帧清一次weakref==1的
	ResourceMap m_rsrcmap;
	LoadingMap m_LoadingUrls;
	LoadingXapMap m_LoadingXaps;

private:
	friend class AniPicReadTask;
	friend class XapReadTask;
	//XWeakRef<XAni> GetAni(XBuffer& buffer, const std::wstring &uri);
	//bool CheckAni(const std::wstring &uri, XWeakRef<XAni>& ani);

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

	void GarbageCollection(int nowtick);

	//如果已经load完成返回true，如果正在loading返回false
	XPEffectAssureResResult AsyncLoadAni(const std::wstring &uri, AniLoadingNotify* pNotify);
	bool SyncLoadAni(XHardRef<XAni> ani);
	//正常加载完成后不需要Remove，只有在还没有加载完成但这个Notify要被删除了才需要Remove
	//通常会在这个Notify的析构里面调
	void RemoveLoadingNotify(const std::wstring &uri, AniLoadingNotify* pNotify);
	XWeakRef<XAni> GetAni(const std::wstring &uri, bool async);
	bool SyncLoadXap(const std::wstring &uri, XAni &ani);

	// 这个方法用于从ani加载 ani，引擎不应该直接调用
	bool LoadAniFromPack(XAni &ani, const PAniPack &pack, const std::wstring &packbaseuri);

	// 这个方法用于卸载ani的图片
	void FreeAni(XAni &ani);
	void FreeAniPic(XAni &ani);
};

};

#endif
