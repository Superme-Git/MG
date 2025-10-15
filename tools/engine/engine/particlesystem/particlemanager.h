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
	// �ӿ�
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
	virtual bool UpdateParticleSystem(ParticleSystemHandle handle, float fTimeDelta); //fTimeDelta��λ����

	// ������Ч�Ƿ�ʹ�þ��������õĹҵ�(�������л�ȡ)�������ʹ��,Ĭ����Ч���ھ���Ľ��ϣ�
	virtual XPEFFECT_BIND_TYPE GetEffectBindType(ParticleSystemHandle handle) const;

	virtual XPEffectAssureResResult EnsureParticleSystemResouce(ParticleSystemHandle handle, bool async = false, ParticleLoadingNotify* pNotify = NULL);

	virtual PSL_ERRORTYPE GetLastError() const;
	virtual void SetSystemConfig(XPCOMPUTERCONFIGTYPE nParam); //���û�������--��2 ��1 ��0

	//
	virtual bool SetSpecialPsTexture( ParticleSystemHandle handle, PictureHandle ShapesTexture, float fu=1.0f, float fv=1.0f);
	virtual PictureHandle GetSpecialPSTextureHandle(ParticleSystemHandle handle) const;
	//ͼƬ���½ǵ��������� fu fv (�����Կ��ϴ�����2������ʱ...)
	virtual bool SetSpecialPsScale( ParticleSystemHandle handle, float fcx=1.0f, float fcy=1.0f);
	//fcx fcy ͼƬ����ʱ�����ű���
	virtual bool SetSilentTime(ParticleSystemHandle handle, float ftime);

	virtual void GarbageCollect();
	virtual void SetGCParam(float fMaxSize, float fTime);
	//fMaxSize��λM��nTime��λ����

	virtual void SetLogPath(const std::wstring& logPath);

	virtual bool SetKeyPointIndex(ParticleSystemHandle handle, int nIndex);

	virtual bool OnResetDevice();

public://���ӡ���ͼ�༭��ר��----�����ط�����ʹ��!
	virtual CParticleSystem* GetParticleSysByHandle(ParticleSystemHandle hHandle) const;
	virtual void ClearAllResouce(); //�ѻ������Դɾ��
	//
public: //ParticleSysResourceReadTask�õ�
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

	//std::set<std::wstring> m_setLoadingURLs; //��¼�첽��ȡʱ���ڶ�ȡ���ļ���
	std::map<std::wstring, std::set<CGeneralParticleSystem*>> m_LoadingNotify;//�첽����С�����֪ͨ

	float m_fCurResourceSize; //��ǰ����ϵͳ��ռ�õ���Դ (M)

	//m_mapPSL����û�м�¼���һ��ʹ��ʱ��.. ��ɾ��m_mapPslTexture���Ӧ�����ʱ��Ӧ��ɾ����Ӧ��PSL
	//Path��shapeListռ���ڴ�ǳ��٣���ʱ�Ͳ������ü�����
	std::map<std::wstring, XHardRef<PSL>*> m_mapPSL;		//psl��---

public:
	std::map<std::wstring, XHardRef<PSTEXTUREINFOSTRUCT>*> m_mapPslTexture; //psl��----������	

private:
 	std::map<std::wstring, XHardRef<PSPICHANDLESTRUCT>*> m_mapD3d9Texture; //ͼƬ��---С����

	std::map<std::wstring, std::vector<POINT>> m_mapPath;	//·����---
	std::map<std::wstring, SHAPE_LIST> m_mapShapeList;		//������Ч����������--- 	
	std::map<ParticleSystemHandle, CParticleSystem*> m_mapParticleSystem;

	std::wstring m_strPath;//PFS���·��
	ParticleSystemHandle m_psHandleSeed;
	CRECT m_ViewPort;
	PSL_ERRORTYPE m_nLastError;
	XPCOMPUTERCONFIGTYPE m_nComputerConfig; //��2 ��1 �� 0 default:2

	float m_fMaxSize;  //����ϵͳ��໺�����M����Դ
	float m_fLastTime; //���һ��ʹ���Ƕ��ٷ�����ǰ
	bool m_bAutoGCFlag;
	float m_fgcTime;//�����������ĳ����ֵ(Ŀǰ��Ϊ10����)������bAutoGCFlag==true�͵���һ����Դ����
	
	//ˮƽ����--��������
//��ֱ����--��Ч���ȼ�
/*
	2     1      0    
0   T	  T		 T
	
1	T	  T		 F
	
2	T	  F		 F
*/

public:
	static std::wstring m_logName;
	// ռ��
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