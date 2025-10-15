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
			PictureHandle	handle;			 //���ص�ͼƬ���
			std::wstring	filename;
			int				dir;			//�����ķ���
			bool			asyncLoad;		//�Ƿ�Ϊ�첽����
			bool			loaded;			//�Ƿ��Ѿ��������
			XBigPic() : handle(INVALID_PICTURE_HANDLE), asyncLoad(false), loaded(false){}
		};

		struct XPic
		{
			XBigPic* pBigPic;
			XBigPic* pBigPicPart;	//Ϊ��Ⱦɫϵͳ���ӵķֿ�ͼ
			NuclearRect offset;		//���ĵ���
			NuclearFRectt srcrect;
			NuclearPolygon outLine; //����ϵ:�Ѿ��и���СͼƬ�����Ͻ�Ϊԭ�㣬��������
			XPic() : pBigPic(NULL), pBigPicPart(NULL), srcrect(0.0f, 0.0f, 1.0f, 1.0f) { }
		};

		typedef std::map<int, int>  UsingDirMap;

		XAni(const std::wstring& uri, bool bPerpetualMem) : m_state(XPANISTATE_NULL), m_nRegion(0), m_nFrame(0), m_dirmode(XPANIDM_NULL), m_bindType(XPEBT_ORIGIN), m_sysLevel(0),
			m_pReleaseFlag(XPANI_RELEASE_WAIT_GC), m_uri(uri), m_perpetualmem(bPerpetualMem) {}

		std::wstring GetAniUri() const { return m_uri; }
		//���pt�����ani��ԭ�������, rֻ��alphaģʽ��Ч
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

		bool assureLoadPic(XPic &pic) const;//����ͼƬ��Դ

	//protected:
	//	core::CMutex mMutexForBuffers;

	private:

		friend class AniManager;
		friend class AniPicReadTask;
		friend class XapReadTask;
		friend class ::CSpriteEditorDoc;
		friend class ::CSpriteEditorView;

		bool SetPic(int dir, int seq, int reg, const XPic& pic);

		//typedef std::map<std::wstring, NuclearBuffer> Buffers;	//ͼƬ������
		typedef std::vector<XBigPic*> Buffers;
		typedef std::vector<std::vector<XPic> > VPic;	// ֡�������ά����
		typedef std::map<int, VPic> XPicMap;

		XPANI_STATE m_state;				// �ڲ�״̬
		NuclearImageFileFormat m_filefmt;	// �ļ���ʽ
		NuclearTextureFormat m_texfmt;		// �����ʽ
		NuclearTextureBlendMode m_blend;	// blendģʽ
		int m_time;							// ����ʱ��
		int m_nRegion;						// ���������
		int m_nFrame;						// ֡��
		XPANI_DIRECTIONMODE m_dirmode;		// ����ģʽ
		int m_color;						// reserved
		NuclearBase m_base;					// ����
		NuclearRect m_border;				// �߿�
		XPicMap	m_pics;						// ͼƬid����(�Է���Ϊkey)
		UsingDirMap m_usingdirs;			// ��ǰ֡����ʹ�õķ���
		Buffers m_buffers;					// ͼƬ������
		Buffers m_buffersPart;				// ͼƬ������Ⱦɫ�ֿ�
		Nuclear_EffectBindType m_bindType;	// ��ģʽ
		int m_sysLevel;						// ���ż���
		NuclearPaniReleaseFlag m_pReleaseFlag;
		unsigned char m_bDyeEnable;			// �Ƿ��Ⱦɫ
		std::wstring m_uri;					// ani�ļ���
		bool m_perpetualmem;				// �Ƿ���פ�ڴ�
	};

	/*
	ani �ļ�
	*/
	class PAniPack : public NuclearObject
	{
	public:

		struct FileSec
		{
			NuclearRect offset; //(-offsetx, -offsety, -offsetx+width,-offsety+height)
			std::wstring strPicPath; //��ͼƬ
			NuclearFRectt rctData; //СͼƬ�����ڴ�ͼƬ�е�λ��,����ڴ�ͼƬ���Ͻǵ���������

			//�����������ѡ�б߽�ĵ�����(�����СͼƬ���Ͻǣ�ˮƽ����ֱ��Ϊ������)
			std::vector<NuclearPoint> outLinePoints; //----�汾14����

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
		Nuclear_EffectBindType m_bindType;		// ��ģʽ
		NuclearBase m_base;
		NuclearPoint m_center;
		NuclearRect m_border;		// �߿�
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
		int			m_nPicGCTime;		//ͼƬgc�ȴ���ʱ�䣬Ĭ��60000��60�룩
		int			m_nXapGCTime;		//ani�ṹgc�ȴ���ʱ�䣬Ĭ��180000��30���ӣ�
		int         m_nGcCooldown;		//gcһ�ε���ȴʱ�䣬Ĭ��10000��10�룩
		int         m_nCooldown;		//��ȴʱ���ʱ��
    
		// ��Դ����ʱ����ɾ����map��hardref==1��ani��Ϊ�˲����ڴ棬��������ʱÿ1024֡��һ��weakref==1��
		ResourceMap m_rsrcmap;
		LoadingMap m_LoadingUrls;
		LoadingXapMap m_LoadingXaps;
		core::CMutex mMutexForLoadingXaps;

		//�Ƿ�ÿ֡������ʱ��������
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

		//����Ѿ�load��ɷ���true���������loading����false
		Nuclear_EffectAssureResResult AsyncLoadAni(const std::wstring &uri, AniLoadingNotify* pNotify, bool bLookupSegmPak, unsigned char bDye = 0);
		//����������ɺ���ҪRemove��ֻ���ڻ�û�м�����ɵ����NotifyҪ��ɾ���˲���ҪRemove
		//ͨ���������Notify�����������
		void RemoveLoadingNotify(const std::wstring &uri, AniLoadingNotify* pNotify);
		NuclearWeakRef<XAni> GetAni(const std::wstring &uri, bool async, bool bLookupSegmPak, bool bPerpetualMem = false, unsigned char bDye = 0, AniLoadingNotify* pNotify = NULL);

		// ����������ڴ�ani���� ani�����治Ӧ��ֱ�ӵ���
		bool LoadAniFromPack(XAni &ani, const PAniPack &pack, const std::wstring &packbaseuri, bool async);

		// �����������ж��ani��ͼƬ
		void FreeAni(XAni &ani, bool straight = false);//straight�Ƿ�ֱ��������ɾ��
		void FreeAniPic(XAni &ani, bool bCullUsingDir = false);
    
		void Run(int delta);
		void setGcCooldown(int cooldown){m_nGcCooldown = cooldown;}
		void Update();

	protected:
		bool syncLoadAni(NuclearHardRef<XAni>& ani, bool bLookupSegmPak);

	};

}

#endif
