#ifndef __Nuclear_PARTICLEMANAGER_H__
#define __Nuclear_PARTICLEMANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuifileio.h"
#include "nuparticlesyscommon.h"
#include "nuiparticlemanager.h"
#include "nuparticlesystem.h"

namespace Nuclear
{
	class CGeneralParticleSystem;
	class ParticleManager : public IParticleManager
	{
	public:
		ParticleManager(IBaseRenderer *pRenderer, INuclearFileIO* pFileIOMan, bool bAutoGCFlag = true);
		virtual ~ParticleManager();

		virtual void OnViewportUpdate(const NuclearRect& viewport) { m_ViewPort = viewport; }
	
		virtual ParticleSystemHandle CreateParticleSystem(const std::wstring& pslName, const PSParameter& param, bool bLoadRes, bool bLookupSegmPak);
		virtual bool DeleteParticleSystem(ParticleSystemHandle handle);
		virtual bool ReleaseParticleSystemRes(ParticleSystemHandle handle);
		virtual bool ResetParticleSystem(ParticleSystemHandle handle);
		virtual bool ModifyParticleSystem(ParticleSystemHandle handle, const PSModifyParam& param);
		virtual bool ModifyParticleSystem(ParticleSystemHandle handle, const PSParameter& param);
		virtual bool RenderParticleSystem(ParticleSystemHandle handle, bool bUpdate = false, bool bEndScene = true);
		virtual bool RenderParticleSystemByLayer(int layer);
		virtual int GetParticleSystemCount() const;	
		virtual int GetTotalNumActiveParticles() const;
		virtual int GetNumActiveParticles(ParticleSystemHandle handle) const;
		virtual PSINFO GetParticleSystemInfo(ParticleSystemHandle handle) const;
		virtual const PSParameter* GetParticleSystemParameter(ParticleSystemHandle handle) const;
		virtual void SetParticlePath(const std::wstring& pslPath);
		virtual bool InsertParticleNotify(ParticleSystemHandle handle, IParticleNotify* particleNotify);
		virtual bool RemoveParticleNotify(ParticleSystemHandle handle, IParticleNotify* particleNotify);	
		virtual float GetSysLife(ParticleSystemHandle handle) const;
		virtual float GetParticleMaxLife(ParticleSystemHandle handle) const;
		virtual void SetStopFlag(ParticleSystemHandle handle, bool bStop);
		virtual bool UpdateParticleSystem(ParticleSystemHandle handle, float fTimeDelta); //fTimeDelta单位：秒

		// 技能特效是否使用精灵上设置的挂点(从psl中获取)（如果不使用,默认特效挂在精灵的脚上）
		virtual Nuclear_EffectBindType GetEffectBindType(ParticleSystemHandle handle) const;

		virtual Nuclear_EffectAssureResResult EnsureParticleSystemResouce(ParticleSystemHandle handle, bool async = false, ParticleLoadingNotify* pNotify = NULL);

		virtual PSL_ERRORTYPE GetLastError() const;
		virtual void SetSystemConfig(NuclearComputerConfigType nParam); //设置机子配置--高2 中1 低0

		virtual bool SetSpecialPsTexture( ParticleSystemHandle handle, PictureHandle ShapesTexture, float fu=1.0f, float fv=1.0f);
		virtual PictureHandle GetSpecialPSTextureHandle(ParticleSystemHandle handle) const;
		//图片左下角的纹理坐标 fu fv (在烂显卡上创建非2幂纹理时...)
		virtual bool SetSpecialPsScale( ParticleSystemHandle handle, float fcx=1.0f, float fcy=1.0f);
		//fcx fcy 图片绘制时的缩放比例
		virtual bool SetSilentTime(ParticleSystemHandle handle, float ftime);

		virtual void GarbageCollect();
		virtual void SetGCParam(float fMaxSize, float fTime);

		//fMaxSize单位M，nTime单位分钟
		virtual void SetLogPath(const std::wstring& logPath);

		virtual bool SetKeyPointIndex(ParticleSystemHandle handle, int nIndex);

		virtual bool OnResetDevice();

		virtual bool LoadPSLFromMem(const NuclearBuffer& data, PSL* ppsl);
		virtual bool LoadPSLFromMemEx(const NuclearBuffer& data, PSL* ppsl);

		virtual bool LoadPSLFromFileEx(std::wstring& filePath, PSL* ppsl);
		virtual bool LoadPSLFromStream(std::ifstream& FS, PSL* ppsl);

		virtual bool Enabled() { return m_bParticleEnabled; }
		virtual void SetEnabled(bool flag) { m_bParticleEnabled = flag; }

	public://粒子、地图编辑器专用----其它地方请勿使用!
		virtual CParticleSystem* GetParticleSysByHandle(ParticleSystemHandle hHandle) const;
		virtual void ClearAllResouce(); //把缓存的资源删掉
		void NotifyLoadingUrl(const std::wstring &url, bool ok);
		bool SaveAsyncReadTextureResource(const NuclearBuffer& data, std::wstring strTextureFileName);
		bool SaveAsyncReadTextureResource(cocos2d::CCImage* pImage, std::wstring strTextureFileName);

		bool InsertPslTexture(std::vector<NuclearHardRef<PSPICHANDLESTRUCT> >& vectorD3DTexture, const std::wstring &pslName);

		IBaseRenderer* GetRenderer() const { return m_pRenderer; }

	private:
		bool LoadPSLFromFile(const std::wstring& filePath, PSL* ppsl, bool bLookupSegmPak);
		bool LoadTextureFromFile(const std::wstring& strTextureFileName, PSPICHANDLESTRUCT* picHandleStruct, bool bLookupSegmPak);

		bool LoadPathFromFile(PSL* ppsl, std::vector<POINT>& vectorFullPathPoint, bool bLookupSegmPak);	
		bool LoadShapeListFromFile(const std::wstring& fileName, SHAPE_LIST& shapelist, bool bLookupSegmPak);
		void AdjustPSParameter(PSModifyParam& param);	
		void SetSpecialPsVertexs(ParticleSystemHandle handle, const SHAPE_LIST& shapelist);
		void Init();
		void Destroy();
		bool GenPSTextureInfo(std::vector<NuclearHardRef<PSPICHANDLESTRUCT> >& vectorD3DTexture, PSTEXTUREINFOSTRUCT* ppslTexture);

		bool IsLoading(const std::wstring &url) { return m_LoadingNotify.find(url) != m_LoadingNotify.end(); }
		void InsertLoadingUrl(const std::wstring &url, CGeneralParticleSystem* pPS) 
		{ 
			m_LoadingNotify[url].insert(pPS);
		}

	private:
		IBaseRenderer *m_pRenderer;
		INuclearFileIO* m_pFileIOMan;

		int m_nBitsPixel;

		bool m_bParticleEnabled;

		std::map<std::wstring, std::set<CGeneralParticleSystem*> > m_LoadingNotify;//异步加载小纹理的通知

		float m_fCurResourceSize; //当前粒子系统所占用的资源 (M)

		//m_mapPSL里面没有记录最后一次使用时间.. 在删除m_mapPslTexture里对应纹理的时候应该删除对应的PSL,Path和shapeList占用内存非常少，暂时就不加引用计数了
		std::map<std::wstring, NuclearHardRef<PSL>*> m_mapPSL;		//psl名---
		std::map<std::wstring, NuclearHardRef<PSTEXTUREINFOSTRUCT>*> m_mapPslTexture; //psl名----大纹理	
 		std::map<std::wstring, NuclearHardRef<PSPICHANDLESTRUCT>*> m_mapD3d9Texture; //图片名---小纹理

		std::map<std::wstring, std::vector<POINT> > m_mapPath;	//路径名---
		std::map<std::wstring, SHAPE_LIST> m_mapShapeList;		//抽象特效顶点数据名--- 	
		std::map<ParticleSystemHandle, CParticleSystem*> m_mapParticleSystem;

		std::wstring m_strPath;
		ParticleSystemHandle m_psHandleSeed;
		NuclearRect m_ViewPort;
		PSL_ERRORTYPE m_nLastError;
		NuclearComputerConfigType m_nComputerConfig; //高2 中1 低 0 default:2

		float m_fMaxSize;  //粒子系统最多缓存多少M的资源
		float m_fLastTime; //最近一次使用是多少分钟以前
		bool m_bAutoGCFlag;
		float m_fgcTime;//当这个数大于某个阈值(目前定为1分钟)、并且bAutoGCFlag==true就调用一次资源回收
	
		//水平方向--机器配置
		//垂直方向--特效优先级
		/*
			2     1      0    
			0   T	  T		 T

			1	T	  T		 F

			2	T	  F		 F
		*/
	public:
		static std::wstring m_logName;

	private:
		ParticleManager();
		ParticleManager(const ParticleManager&);
		ParticleManager& operator=(const ParticleManager&);
	};

	struct MHZX_PSLOG
	{
		static DWORD s_nProcessId;
		static const wchar_t *fn()
		{
			static wchar_t name[MAX_PATH +1];
			memcpy(name,  ParticleManager::m_logName.c_str(), sizeof(wchar_t)*( ParticleManager::m_logName.length() +1));
			return name;
		}
	};
	#define XPLOG_PSMHZX xwprint<MHZX_PSLOG>

}

#endif