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
		virtual bool UpdateParticleSystem(ParticleSystemHandle handle, float fTimeDelta); //fTimeDelta��λ����

		// ������Ч�Ƿ�ʹ�þ��������õĹҵ�(��psl�л�ȡ)�������ʹ��,Ĭ����Ч���ھ���Ľ��ϣ�
		virtual Nuclear_EffectBindType GetEffectBindType(ParticleSystemHandle handle) const;

		virtual Nuclear_EffectAssureResResult EnsureParticleSystemResouce(ParticleSystemHandle handle, bool async = false, ParticleLoadingNotify* pNotify = NULL);

		virtual PSL_ERRORTYPE GetLastError() const;
		virtual void SetSystemConfig(NuclearComputerConfigType nParam); //���û�������--��2 ��1 ��0

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

		virtual bool LoadPSLFromMem(const NuclearBuffer& data, PSL* ppsl);
		virtual bool LoadPSLFromMemEx(const NuclearBuffer& data, PSL* ppsl);

		virtual bool LoadPSLFromFileEx(std::wstring& filePath, PSL* ppsl);
		virtual bool LoadPSLFromStream(std::ifstream& FS, PSL* ppsl);

		virtual bool Enabled() { return m_bParticleEnabled; }
		virtual void SetEnabled(bool flag) { m_bParticleEnabled = flag; }

	public://���ӡ���ͼ�༭��ר��----�����ط�����ʹ��!
		virtual CParticleSystem* GetParticleSysByHandle(ParticleSystemHandle hHandle) const;
		virtual void ClearAllResouce(); //�ѻ������Դɾ��
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

		std::map<std::wstring, std::set<CGeneralParticleSystem*> > m_LoadingNotify;//�첽����С�����֪ͨ

		float m_fCurResourceSize; //��ǰ����ϵͳ��ռ�õ���Դ (M)

		//m_mapPSL����û�м�¼���һ��ʹ��ʱ��.. ��ɾ��m_mapPslTexture���Ӧ�����ʱ��Ӧ��ɾ����Ӧ��PSL,Path��shapeListռ���ڴ�ǳ��٣���ʱ�Ͳ������ü�����
		std::map<std::wstring, NuclearHardRef<PSL>*> m_mapPSL;		//psl��---
		std::map<std::wstring, NuclearHardRef<PSTEXTUREINFOSTRUCT>*> m_mapPslTexture; //psl��----������	
 		std::map<std::wstring, NuclearHardRef<PSPICHANDLESTRUCT>*> m_mapD3d9Texture; //ͼƬ��---С����

		std::map<std::wstring, std::vector<POINT> > m_mapPath;	//·����---
		std::map<std::wstring, SHAPE_LIST> m_mapShapeList;		//������Ч����������--- 	
		std::map<ParticleSystemHandle, CParticleSystem*> m_mapParticleSystem;

		std::wstring m_strPath;
		ParticleSystemHandle m_psHandleSeed;
		NuclearRect m_ViewPort;
		PSL_ERRORTYPE m_nLastError;
		NuclearComputerConfigType m_nComputerConfig; //��2 ��1 �� 0 default:2

		float m_fMaxSize;  //����ϵͳ��໺�����M����Դ
		float m_fLastTime; //���һ��ʹ���Ƕ��ٷ�����ǰ
		bool m_bAutoGCFlag;
		float m_fgcTime;//�����������ĳ����ֵ(Ŀǰ��Ϊ1����)������bAutoGCFlag==true�͵���һ����Դ����
	
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