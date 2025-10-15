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
	��������֡�����ܰ���������򣬻������ļ���ʽ��ѹ����ʽ������λ�õ���Ϣ
	�����ڱ��ֳ���������NPC����
	�ɵ�����Ⱦ��Ҳ������Ϊװ��������Action��Ⱦ
*/
class XAni
{
	
public:
	enum XPANI_STATE
	{
		XPANISTATE_NULL,
		XPANISTATE_NO_XAP,		//ani�ļ���û��Load�ꡭ��
		XPANISTATE_NOPICTURE,	//һ��ͼƬ��û��Load�ꡭ��
		XPANISTATE_BASE_PIC,	//���ٵ�һ������ĵ�һ֡���еģ�ʣ�µ�ͼ��û��Load�ꡭ��
		XPANISTATE_PICTURE,
	};

	struct XBigPic
	{
		PictureHandle handle;			// Renderer ���ص�ͼƬ���
		std::wstring filename;
		XBigPic() : handle(INVALID_PICTURE_HANDLE) {}
	};

	struct XPic
	{
		XBigPic* pBigPic;
		XBigPic* pBigPicPart;
		CRECT offset;					// ���ĵ���
		FRECT srcrect;
		XPPolygon outLine; //����ϵ:�Ѿ��и���СͼƬ�����Ͻ�Ϊԭ�㣬��������
		XPic() : pBigPic(NULL), pBigPicPart(NULL), srcrect(0.0f, 0.0f, 1.0f, 1.0f) { }
		//bool Check(Renderer *pRenderer);
	};


	XAni() : m_state(XPANISTATE_NULL), m_nFrame(0), m_dirmode(XPANIDM_NULL), m_bindType(XPEBT_ORIGIN), m_sysLevel(0) { }

	//���pt�����ani��ԭ�������, rֻ��alphaģʽ��Ч
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

	//typedef std::map<std::wstring, XBuffer> Buffers;	//ͼƬ������
	typedef std::vector<XBigPic*> Buffers;
	typedef std::vector<std::vector<XPic> > VPic;	// ֡�������ά����
	typedef std::map<int, VPic> XPicMap;

	XPANI_STATE m_state;				// �ڲ�״̬
	XPIMAGE_FILEFORMAT m_filefmt;		// �ļ���ʽ
	XPTEXTURE_FORMAT m_texfmt;			// �����ʽ
	XPTEXTURE_BLENDMODE m_blend;		// blendģʽ
	int m_time;							// ����ʱ��
	int m_nRegion;						// ���������
	int m_nFrame;						// ֡��
	XPANI_DIRECTIONMODE m_dirmode;		// ����ģʽ
	int m_color;						// reserved
	XPBase m_base;						// ����
	CRECT m_border;						// �߿�
	XPicMap	m_pics;						// ͼƬid����
	Buffers m_buffers;					// ͼƬ������
	Buffers m_buffersPart;				// �ֿ�ͼƬ������
	XPEFFECT_BIND_TYPE m_bindType;		// ��ģʽ
	int m_sysLevel;						// ���ż���

};

/*
ani �ļ�
*/
class PAniPack : public PObject
{
public:

	struct FileSec
	{
		CRECT offset; //(-offsetx, -offsety, -offsetx+width,-offsety+height)
		std::wstring strPicPath; //��ͼƬ
		FRECT rctData; //СͼƬ�����ڴ�ͼƬ�е�λ��,����ڴ�ͼƬ���Ͻǵ���������

		//�����������ѡ�б߽�ĵ�����(�����СͼƬ���Ͻǣ�ˮƽ����ֱ��Ϊ������)
		std::vector<CPOINT> outLinePoints; //----�汾14����

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
		VERSION = 15,  // ani �м�¼��һ���ַ����������ַ�����Ϊ�գ�������ƴ��ͼƬ�ļ���
		VERSION_14 = 14,	 //ani�м�¼��һ������Σ�����ѡ���ж�
		VERSION_13 = 13, //���ʱ���زĵ�alphaֵ�����봦��,�п��ܼ�СͼƬ�ߴ硣
		VERSION_12 = 12,
		VERSION_NO_CENTER = 11,
		VERSION_OLD_BASE = 10,
		VERSION_OLDXAP = 9,
		OLD_VERSION_NO_SYS_LEVEL = 8,
		OLD_VERSION_NO_BIND_TYPE = 7,
	};
	/*
	5->6 �ӱ߿� m_border
	6->7 ����ģʽ����
	*/

	int m_nXapVersion;

	int m_filefmt;		// �ļ���ʽ
	int m_texfmt;		// �����ʽ
	int m_blend;		// blendģʽ	
	int m_time;			// ��������ʱ�䣬��λ����
	int m_nRegion;		// ���������
	int m_nFrame;
	int m_dirmode;		// ����ģʽ
	int m_color;
	int m_systemLevel;	//! ���ż���
	XPEFFECT_BIND_TYPE m_bindType;		// ��ģʽ
	XPBase m_base;
	CPOINT m_center;
	CRECT m_border;		// �߿�
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
class AniManager �������� XAni ��Դ�ļ��غ��ͷţ�lru���ü���������һ��
				 ͬʱ����༭����Ҫ�ĸ��ֲ���
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
	int			m_nPicGCTime;		//ͼƬgc�ȴ���ʱ�䣬Ĭ��20000��20�룩
	int			m_nXapGCTime;		//ani�ṹgc�ȴ���ʱ�䣬Ĭ��1200000��20���ӣ�

	// ��Դ����ʱ����ɾ����map��hardref==1��ani��Ϊ�˲����ڴ棬��������ʱÿ1024֡��һ��weakref==1��
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

	//����Ѿ�load��ɷ���true���������loading����false
	XPEffectAssureResResult AsyncLoadAni(const std::wstring &uri, AniLoadingNotify* pNotify);
	bool SyncLoadAni(XHardRef<XAni> ani);
	//����������ɺ���ҪRemove��ֻ���ڻ�û�м�����ɵ����NotifyҪ��ɾ���˲���ҪRemove
	//ͨ���������Notify�����������
	void RemoveLoadingNotify(const std::wstring &uri, AniLoadingNotify* pNotify);
	XWeakRef<XAni> GetAni(const std::wstring &uri, bool async);
	bool SyncLoadXap(const std::wstring &uri, XAni &ani);

	// ����������ڴ�ani���� ani�����治Ӧ��ֱ�ӵ���
	bool LoadAniFromPack(XAni &ani, const PAniPack &pack, const std::wstring &packbaseuri);

	// �����������ж��ani��ͼƬ
	void FreeAni(XAni &ani);
	void FreeAniPic(XAni &ani);
};

};

#endif
