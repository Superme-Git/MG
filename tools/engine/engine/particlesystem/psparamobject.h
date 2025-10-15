#pragma once
#include "..\common\pobject.h"
#include "iparticlesyscommon.h"

namespace Nuclear
{
	class CPsParamObject : public PObject
	{
	public:
		CPsParamObject(void);
		virtual ~CPsParamObject(void);

		enum PSPARAMVER
		{
			VERSION01 = 1, 
			//�汾1��¼���ݣ���������ת�Ƕȡ��߷��䳤�ȡ����η����ȡ��߶ȡ�
			//ä�����赲�ߡ�������
			//��ʱ�ͼ�¼���ϼ�������..
			CURVER = 1,
		};	

		struct PSPARAM
		{
			//�汾1�д�Ĳ���---���в�����PSModifyParam�ṹ�е�ͬ��������Ӧ----����
			float m_fEmitterSpinAngle;//��������ת�Ƕ�--����Ϊ��λ---˳ʱ��Ϊ��--
			float m_fLineLength;//�߷�����߳���//ֻ����ˮƽ�������...			
			float m_fRctWidth;//���εĳ��Ϳ�
			float m_fRctHeight;
			std::vector<CRECT> m_vectorRctBlindArea;//����ä��
			std::vector<LINE> m_vectorBlockLine;//�赲��..
			std::vector<LINE> m_vectorReflectLine;//������...
			//����

			PSPARAM()
			{
				m_fEmitterSpinAngle = 0.0f;
				m_fLineLength = -1.0f; //�������������С��0���򲻸ı�������Դ����
				m_fRctWidth = -1.0f;
				m_fRctHeight = -1.0f;
				m_vectorRctBlindArea.clear();
				m_vectorBlockLine.clear();
				m_vectorReflectLine.clear();
			}
		};

	public:
		const PSPARAM& GetPsParam() const { return m_psParam; }
		void SetPsParam(const PSPARAM& psParam) { m_psParam = psParam; } //�༭��ר�õ�

		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);

	private:
		PSPARAM m_psParam;

	private:
		CPsParamObject(const CPsParamObject&);
		CPsParamObject& operator=(const CPsParamObject&);
	};
}
