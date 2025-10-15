#pragma once

#include "particlesystem.h"
#include "iparticlemanager.h"

namespace Nuclear
{
	class CGeneralParticleSystem :	public CParticleSystem
	{
	public:
		CGeneralParticleSystem(const XHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer,
			const std::vector<POINT>& vectorFullPathPoint, const std::wstring& pslName);

		virtual ~CGeneralParticleSystem(void);

	public:
		virtual bool Render(bool bUpdate = false, bool bEndScene = true); 
		virtual bool Update(float fTimeDelta); //����������������ٵ���render()
		
		void SetParticleSysResouce(const XHardRef<PSTEXTUREINFOSTRUCT>& pRefPSTextureInfo);
		void SetPicHandleRefResource(const std::vector<XHardRef<PSPICHANDLESTRUCT>>& vectorD3DTexture);

		virtual void Reset(void);

		virtual void ResetVerticesVector();
		void ResizeParticleArry(int nSysMaxParticles, int nEmissionRate = -1); //�༭�������ֱ�����Ч��Ҫ�õ�

		void SetLoadedResFlag(bool bLoadedRes);
		bool GetLoadedResFlag();
		bool ReleaseResource();

		bool SetKeyPointIndex(int nIndex); //�ؼ�֡·������ǰʹ�õ��ĸ��ؼ���

		void SetParticleLoadingNotify(ParticleLoadingNotify* pNotify) { m_pLoadingNotify = pNotify; }
		ParticleLoadingNotify* GetParticleLoadingNotify() const { return m_pLoadingNotify; }

	public://�༭��ר��...
		XHardRef<PSTEXTUREINFOSTRUCT> GetPSTextureInfo() { return m_pRefPSTextureInfo; } //����֮���޸�..
		void SetPSLPathPointVector(const std::wstring& strPathFileName);
		bool IsLoadFaild() const { return m_bLoadFaild; }
		void SetLoadFaild() { m_bLoadFaild = true; }
		int m_restTexRes;//�첽��ʱ�򣬻�ʡ�¶�����Դû�м��أ�ֻ���첽��ʱ����

	private:
//		template<class VERTEX>
//		bool Render(PictureHandle handle);

		//����Ч�ʿ���--��std::vector<VERTEX> vertices;�Ƶ����Ա������,ȡ���������ģ�庯��
		bool Render_TCT(PictureHandle handle, bool bEndScene = true); //TCT1VERTEX
		bool Render_UTCT(PictureHandle handle); //UTCT1VERTEX

		void InitParticlePos(CParticle* part);
		void InitParticleSpeed(CParticle* part, const POINT& objPoint);
		bool CreateNewParticles(float fTimeDelta); //update��..����������
		bool LoadFullPathPoint(); //---��ͬ·�����������»�Ҫ�޸����ӵ�һЩ�ٶȲ���
		void ComputePathAngle();//����m_vectorPathAngle;
		void SetPathVector(const std::vector<POINT>& vectorFullPathPoint);

		void InitTexturePicSize(); //���� m_fparticleSize ������ȷ�ж������Ƿ��ܳ����ӿڷ�Χ...

	private:
		XHardRef<PSTEXTUREINFOSTRUCT> m_pRefPSTextureInfo; //��ͨ������Ч�õľ���ƴ�Ӻ�Ĵ�����
		
		std::vector<XHardRef<PSPICHANDLESTRUCT>> m_vectorD3DTexture; //��������Ч�õ���С����ͼ
		//��¼��Ŀ����ʹС��������ü�����һ����delete������Ч����releaseResource��ʱ���һ���ﵽ���������Դ��Ŀ��
	
		int m_nKeyPointIndex; //���ڵ���-1������ -1 ��ʾʹ������·���������ʹ�����ֵ��

		std::vector<FPOINT> m_vectorFullPathPoint; //·����---ʸ���ַ�·����bezier����·��---�ڹ��캯������������ʼ�����vector
		std::vector<float> m_vectorPathAngle; //align to path ר��
		std::list<int> m_listPrePathIndex; //��������·���˶�������,�²��������ӽ�����

		//m_psParameter.m_psModifyParam.m_bModifyLineEmitter == TRUE�����������²������ӵ�λ��
		int m_nLinePosIndex; //��������˳����

		int m_nPosIndex; //��ʾ�²������ӵ�λ�� PSLTYPE_CUSTOMPATH
		float m_fparticleSize; //�����ж������Ƿ������Ļ
		//���ںܴ�����ӣ����׼ȷ�ж������Ƿ����Ļ��.. ��Ҫ������������Ŀ�ߺͷŴ���.

		// ��������������m_ppsl������ܿ��ܲ�ͬ��������ⲿ������ݷ�������Χ�޸���...
		int m_nCurSysMaxParticles; //��ǰϵͳ���������������������ʱ����ж�����						
		int m_nCurEmissionRate; //��ǰϵͳ�������ķ���Ƶ��..����������ʱ����ж�����

		std::vector<TCT1VERTEX> m_vertices_TCT;
		std::vector<UTCT1VERTEX> m_vertices_UTCT;
		
		bool m_bLoadedRes; //�Ƿ��Ѿ���������Դ���ؽ�����..m_vertices_TCT�ռ��Ƿ�������
		bool m_bLoadFaild; //�Ƿ����ʧ�ܡ���ֻ�����첽���һ����ļ�û�м������ʱ�����

		ParticleLoadingNotify* m_pLoadingNotify;//�첽������ɣ��ɹ�orʧ�ܣ���֪ͨ
	};

};
