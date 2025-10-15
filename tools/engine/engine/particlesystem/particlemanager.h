#ifndef __Nuclear_PARTICLEMANAGER_H
#define __Nuclear_PARTICLEMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\ifileio.h"
#include "particlesyscommon.h"
#include "iparticlemanager.h"
#include "particlesystem.h"

namespace Nuclear
{
class CGeneralParticleSystem;
class ParticleManager : public IParticleManager
{
public:
	ParticleManager(IBaseRenderer *pRenderer, IFileIO* pFileIOMan, bool bAutoGCFlag = true);
	virtual ~ParticleManager();

public:
	// 接口
	virtual void OnViewportUpdate(const CRECT& viewport);

	virtual ParticleSystemHandle CreateParticleSystem(const std::wstring& pslName, const PSParameter& param, bool bLoadRes = true);
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

	// 技能特效是否使用精灵上设置的挂点(从粒子中获取)（如果不使用,默认特效挂在精灵的脚上）
	virtual XPEFFECT_BIND_TYPE GetEffectBindType(ParticleSystemHandle handle) const;

	virtual XPEffectAssureResResult EnsureParticleSystemResouce(ParticleSystemHandle handle, bool async = false, ParticleLoadingNotify* pNotify = NULL);

	virtual PSL_ERRORTYPE GetLastError() const;
	virtual void SetSystemConfig(XPCOMPUTERCONFIGTYPE nParam); //设置机子配置--高2 中1 低0

	//
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

public://粒子、地图编辑器专用----其它地方请勿使用!
	virtual CParticleSystem* GetParticleSysByHandle(ParticleSystemHandle hHandle) const;
	virtual void ClearAllResouce(); //把缓存的资源删掉
	//
public: //ParticleSysResourceReadTask用的
	void NotifyLoadingUrl(const std::wstring &url, bool ok);
	bool SaveAsyncReadTextureResource(const XBuffer& data, std::wstring strTextureFileName);

public:
	bool InsertPslTexture(std::vector<XHardRef<PSPICHANDLESTRUCT>>& vectorD3DTexture, const std::wstring &pslName);

	bool LoadPSLFromFile(const std::wstring& filePath, PSL* ppsl);
	bool LoadPSLFromMem(const XBuffer& data, PSL* ppsl);
	bool LoadPSLFromFile2(const std::wstring& filePath, PSL* ppsl);
	bool LoadPSLFromMemEx(const XBuffer &data, PSL* ppsl);
	bool LoadPSLFromFileEx(std::wstring& filePath, PSL* ppsl);
	bool LoadPSLFromStream(std::ifstream& FS, PSL* ppsl);
	bool LoadTextureFromFile(const std::wstring& strTextureFileName, PSPICHANDLESTRUCT* picHandleStruct);

	bool LoadPathFromFile(PSL* ppsl, std::vector<POINT>& vectorFullPathPoint);	
	bool LoadShapeListFromFile(const std::wstring& fileName, SHAPE_LIST& shapelist);
	void AdjustPSParameter(PSModifyParam& param);	
	void SetSpecialPsVertexs(ParticleSystemHandle handle, const SHAPE_LIST& shapelist);
	void Init();
	void Destroy();
	bool GenPSTextureInfo(std::vector<XHardRef<PSPICHANDLESTRUCT>>& vectorD3DTexture, PSTEXTUREINFOSTRUCT* ppslTexture);

	bool IsLoading(const std::wstring &url) { return m_LoadingNotify.find(url) != m_LoadingNotify.end(); }
	void InsertLoadingUrl(const std::wstring &url, CGeneralParticleSystem* pPS) 
	{ 
		m_LoadingNotify[url].insert(pPS);
	}

private:
	IBaseRenderer *m_pRenderer;
	IFileIO* m_pFileIOMan;

	int m_nBitsPixel;

	//std::set<std::wstring> m_setLoadingURLs; //记录异步读取时正在读取的文件名
	std::map<std::wstring, std::set<CGeneralParticleSystem*>> m_LoadingNotify;//异步加载小纹理的通知

	float m_fCurResourceSize; //当前粒子系统所占用的资源 (M)

	//m_mapPSL里面没有记录最后一次使用时间.. 在删除m_mapPslTexture里对应纹理的时候应该删除对应的PSL
	//Path和shapeList占用内存非常少，暂时就不加引用计数了
	std::map<std::wstring, XHardRef<PSL>*> m_mapPSL;		//psl名---

public:
	std::map<std::wstring, XHardRef<PSTEXTUREINFOSTRUCT>*> m_mapPslTexture; //psl名----大纹理	

private:
 	std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*> m_mapD3d9Texture; //图片名---小纹理

	std::map<std::wstring, std::vector<POINT>> m_mapPath;	//路径名---
	std::map<std::wstring, SHAPE_LIST> m_mapShapeList;		//抽象特效顶点数据名--- 	
	std::map<ParticleSystemHandle, CParticleSystem*> m_mapParticleSystem;

	std::wstring m_strPath;//PFS相对路径
	ParticleSystemHandle m_psHandleSeed;
	CRECT m_ViewPort;
	PSL_ERRORTYPE m_nLastError;
	XPCOMPUTERCONFIGTYPE m_nComputerConfig; //高2 中1 低 0 default:2

	float m_fMaxSize;  //粒子系统最多缓存多少M的资源
	float m_fLastTime; //最近一次使用是多少分钟以前
	bool m_bAutoGCFlag;
	float m_fgcTime;//当这个数大于某个阈值(目前定为10分钟)、并且bAutoGCFlag==true就调用一次资源回收
	
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
	// 占坑
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
		static TCHAR name[MAX_PATH +1];
		memcpy(name,  ParticleManager::m_logName.c_str(), sizeof(TCHAR)*( ParticleManager::m_logName.length() +1));
		return name;
	}
};
#define XPLOG_PSMHZX xwprint<MHZX_PSLOG>

};

#endif