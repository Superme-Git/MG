#pragma once


#include <list>

namespace Nuclear
{
	typedef int ParticleSystemHandle;
	const ParticleSystemHandle INVALID_PS_HANDLE = 0;

	const DWORD PSDEFAULTCOLOR = 0xff000001;
	const float	PSDEFAULTTIME = -1.0f;
	const float PSUPDATECRITICALTIME = 0.2f;

	class IParticleNotify
	{
	public:
		//virtual void OnStart() {}
		virtual void OnStop() {}
		virtual void OnEnd() {}
		virtual ~IParticleNotify() {}
	};

	struct PSModifyParam 
	{
		//����������ʹ���������껹����Ļ����Ҫ���ݴ���������m_bScreenCoord��ȷ��

		FPOINT m_pointCent; //0 �㷢��--1�ߡ�2Բ��3���� 4�Զ���·��������
		
		float m_fEmitterSpinAngle; //��������ת�Ƕ�--����Ϊ��λ---˳ʱ��Ϊ��---�ߡ����Ρ��Զ���·����״
							   //��ת���Ĵ�PSL�л��

		FPOINT m_pointObject; //��������ʱ��Ŀ���

		float m_fEmissionScale; //psl�Ŵ��� --��psl�ļ��е�m_fpslzoom���
		FPOINT m_point0; //1 �߷����������..
		FPOINT m_point1; //
		float m_fEmissionAngle; //�߷���ʱ�ķ��䷽��..
		CRECT m_rctEmission; //3 ����������λ��..
		
		PSModifyParam()
		{
			m_pointCent = FPOINT(400, 400);
			m_fEmitterSpinAngle = 0.0f;
			m_pointObject = FPOINT(200, 200);

			m_fEmissionScale = 1.0f;
			m_point0 = FPOINT(0.0f, 0.0f);
			m_point1 = FPOINT(800.0f, 0.0f);
			m_fEmissionAngle = 0.0f;
			m_rctEmission = CRECT(100, 100, 300, 300);
		}
	};

	struct PSParameter
	{
		PSModifyParam m_psModifyParam;
		//�����ַ�����..��
		DWORD color;
		float time; //ps����ʱ��..
		int layer;
		bool m_bScreenCoord; //���������Ƿ���Ļ���ꡣĬ��false����ʾ���������Ϊ�������ꡣ������Ч��true��������Ϊfalse
		bool cycle;
		bool autorender;//����ȾʱΪfalse�򲻻���..

		bool m_bVertexRHWflag; //Ĭ��Ϊtrue,Ϊfalse���ʾ����ṹΪD3DFVF_XYZ ���� ��Ҫ����D3D��������任����

		PSParameter()
		{
			m_bScreenCoord = false;
			m_bVertexRHWflag = true;
			color = PSDEFAULTCOLOR; //ʹ��psl��ɫ
			time = PSDEFAULTTIME; //ʹ��pslʱ��
			layer = 0;
			cycle = false;		//�Ƿ�ѭ��
			autorender = false;
		}
	};

	enum PSL_MOVETYPE
	{
		PSLMOVETYPE_DEFAULT = 0,
		PSLMOVETYPE_FOLLOWSPRITE = 1,
		PSLMOVETYPE_FULLSCREEN = 2,
	};

	enum PSL_PARTICLEPATHPOSTYPE
	{//���Զ���·����������Ч��ʼ����λ��
		PARTICLEPATHPOS_START = 0, //·��ͷ
		PARTICLEPATHPOS_RANDOM = 1, //���ϴ���������λ���б���ȡ
		PARTICLEPATHPOS_PREPOS = 2, //���
	};

	enum PSL_PARTICLESIZERANDTYPE
	{//sizeRand��ʹ�÷�ʽ..
		PARTICLESIZERAND_XY = 0, //xy�����
		PARTICLESIZERAND_X = 1, //ֻ���x
		PARTICLESIZERAND_Y = 2, //ֻ���y
	};

	enum PSL_EMISSIONTYPE
	{
		PSLTYPE_POINT = 0,
		PSLTYPE_LINE = 1,
		PSLTYPE_CIRCLE = 2,
		PSLTYPE_RECT = 3,
		PSLTYPE_CUSTOMPATH = 4,
	};

	enum PSL_PATHMIRRORTYPE
	{
		PATHMIRRORTYPE_NONE = 0, //������
		PATHMIRRORTYPE_LR = 1,   //���Ҿ���
		PATHMIRRORTYPE_TB = 2,   //���¾���
		PATHMIRRORTYPE_LRTB = 3, //����ԭ��Գ� --���ĶԳ�
	};

	enum PSL_CONFIGREQ //psl�Ի��ӵ�����Ҫ��..
	{
		PSLCONFIGREQ_L = 0, //����
		PSLCONFIGREQ_M = 1, //����
		PSLCONFIGREQ_H = 2, //����
	};

	struct PSINFO
	{
		CRECT m_rctMaxBox; //������Ч��Χ�С�
		FPOINT m_pointCent; //����ͬPSParameter�е�m_pointCent
		int m_moveType; //PSL_MOVETYPE
		int m_emisType; //PSL_EMISSIONTYPE
		int m_nPSLConfigReq; //ָ��psl�����ȼ�
		bool m_bCycle;

		PSINFO()
		{
			m_rctMaxBox = CRECT(0, 0, 200, 200);
			m_pointCent = FPOINT(400, 400);
			m_moveType = PSLMOVETYPE_DEFAULT;
			m_emisType = PSLTYPE_POINT;
			m_bCycle = false;
			m_nPSLConfigReq = PSLCONFIGREQ_L;
		}
	};

	enum PSL_ERRORTYPE
	{
		PSLLOADERROR_SUCC = 100, //δ����
		PSLLOADERROR_FILE = 5, //��Ч�ļ��򿪳���..
		PSLLOADERROR_TEXTURE = 6, //��Ч��ʹ��������س���
		PSLLOADERROR_PATH = 7, //��Ч��ʹ�õ�·���ļ����س���
		PSLLOADERROR_SHAPELIST = 8, //ת����Ч��ʹ�õ�ǰ��ͼ�ֿ������ļ����س���
	};

	enum PSL_ABSRTACTSHAPETYPE
	{
		PSLSHAPETYPE_VORONOI = 1,
		PSLSHAPETYPE_RECTANGLE = 2,
		PSLSHAPETYPE_TRIANGLE = 3,
	};

}