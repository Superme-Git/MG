#pragma once
#include "..\common\xptypes.h"

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

	struct LINE
	{
		CPOINT pt0;
		CPOINT pt1;
		LINE(CPOINT point0 = CPOINT(0, 0), CPOINT point1 = CPOINT(0, 0))
		{
			pt0 = point0;
			pt1 = point1;
		}
	};

	struct PSModifyParam 
	{
		//m_pointCent��ʹ���������껹����Ļ����Ҫ���ݴ���������m_bScreenCoord��ȷ��
		//m_pointCent  ���洫������������꣬�ڲ�ʹ��ʱ�Ѿ�תΪ��Ļ�����ˡ�
		FPOINT m_pointCent; //0 �㷢��--1�ߡ�2Բ��3���� 4�Զ���·��������
		
		float m_fEmitterSpinAngle; //��������ת�Ƕ�--����Ϊ��λ---˳ʱ��Ϊ��---�ߡ����Ρ��Զ���·����״
							   //��ת���Ĵ������л��

		FPOINT m_pointObject; //��������ʱ��Ŀ���
		float m_fEmissionScale; //���ӷŴ��� --��psl�ļ��е�m_fpslzoom���
				
		//��������Χ�ı�֮���ڲ���Ӧ���޸����������ͷ���Ƶ��
		float m_fLineLength;//�߷�����߳��� //Ŀǰ��Ӧ��:���ֱ�����Ч..2009-10-15
		//ֻ����ˮƽ�������...
	//	float m_fLineDeltaDis;//����������ĵ��ƫ�ƾ���---���ϸ���������
		float m_fRctWidth;//���εĳ��Ϳ�
		float m_fRctHeight;

		//����ä������---�������������ӾͲ��ɼ���..һϵ������ڷ��������ĵľ�������
		std::vector<CRECT> m_vectorRctBlindArea;
		//�赲��..���Ӳ���Խ������������ڿ��ƶ�������û���ٶȾͻ����Ϊֹͣ...
		std::vector<LINE> m_vectorBlockLine;
		 //������...��������֮���չ��߷�����ɸı��˶�����
		std::vector<LINE> m_vectorReflectLine;
		
		//��������ֵ���������m_pointCent������

		XPEFFECT_SCALE_TYPE m_scaleType;
		float m_fParticleSizeCoef;
		float m_fParticlePosCoef;

		//ָ������Ŀ��㣬�������ϲ�������.. ����������...
		FPOINT m_fpoint0;
		FPOINT m_fpoint1;
		float m_fLineEmitterRotate; //���Է���������ת�Ƕȣ���������������ת���������...����
		//

		PSModifyParam()
		{
			m_pointCent = FPOINT(400, 400);
			m_fEmitterSpinAngle = 0.0f;
			m_pointObject = FPOINT(200, 200);

			m_fEmissionScale = 1.0f;

			m_fLineLength = -1.0f; //�������������С��0���򲻸ı�������Դ����
			m_fRctWidth = -1.0f;
			m_fRctHeight = -1.0f;
			m_vectorRctBlindArea.clear();
			m_vectorBlockLine.clear();
			m_vectorReflectLine.clear();

			m_scaleType = XPEST_NOSCALE;
			m_fParticleSizeCoef = 1.0f;
			m_fParticlePosCoef = 1.0f;

			m_fpoint0 = FPOINT(100, 100);
			m_fpoint1 = FPOINT(300, 300);
			m_fLineEmitterRotate = 0.0f;
		}
	};

	struct PSParameter
	{
		PSModifyParam m_psModifyParam;
		//�����ַ�����..��
		DWORD color;
		float time; //ps����ʱ��..
		int layer;
		bool m_bScreenCoord; //�Ƿ���Ļ���ꡣĬ��false����ʾΪ�������ꡣ
		//Ŀǰ������Ч�����촰�ڵ����ֱ�����Ч��true��������Ϊfalse
		bool cycle;
		bool autorender;//����ȾʱΪfalse�򲻻���..

		bool m_bVertexRHWflag; //Ĭ��Ϊtrue,Ϊfalse���ʾ����ṹΪD3DFVF_XYZ ���� ��Ҫ����D3D��������任����

		BOOL m_bModifyLineEmitter; //�޸��߷�������ǣ���Ϊtrue���޸������ж�Ӧ����
		FPOINT m_ptLineEmitterStrt; //��Ч�� 2011-10-28
		FPOINT m_ptLineEmitterEnd;

		PSParameter()
		{
			m_bScreenCoord = false;
			m_bVertexRHWflag = true;
			color = PSDEFAULTCOLOR; //ʹ��������ɫ
			time = PSDEFAULTTIME; //ʹ������ʱ��
			layer = 0;
			cycle = false;		//�Ƿ�ѭ��
			autorender = false;

			m_bModifyLineEmitter = false;
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
		PARTICLESIZERAND_XY = 0, //xy�� ��ֵ ���
		PARTICLESIZERAND_X = 1, //ֻ���x
		PARTICLESIZERAND_Y = 2, //ֻ���y
		PARTICLESIZERAND_ZOOM = 3, //xy�� �ȱ� ���
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

	enum PSL_CONFIGREQ //���ӶԻ��ӵ�����Ҫ��..
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
		int m_nPSLConfigReq; //ָ�����ӵ����ȼ�
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
		PSLSHAPETYPE_2PIC = 4, //�ֳ�2��ͼƬ���ŷָ��߷��������˶�...
	};

	//enum PSL_LOADRESFLAG
	//{
	//	PSLLOADRESFLAG_UNKNOWERROR = -1, //δ֪����
	//	PSLLOADRESFLAG_READFILEERROR = 0,   //���ļ�����
	//	PSLLOADRESFLAG_SUCC = 1,    //�ɹ�
	//	PSLLOADRESFLAG_LOADING = 2, //���ڶ�ȡ
	//};

}