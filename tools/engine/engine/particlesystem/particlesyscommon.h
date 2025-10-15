#pragma once

#include "..\common\xptypes.h"
#include "..\renderer\ibaserenderer.h"
#include "iparticlesyscommon.h"

namespace Nuclear
{

struct BLEND_MODE
{
	TCHAR name[25];
	int mode;
};

const int NUMBLENDINGMODES = 8;
extern const BLEND_MODE g_BlendingModes[8];
//���powf(fAgeCoef, fvar);//fAgeCoef:0.0f--1.0f,  fvar:0.0f--2.0f����ϵͳ��
//var�ֳ�20��, ����ֵ�ֳ�40��
extern float XP_POWF[11][42];

struct PSTEXTUREINFO
{
	//�д��ڵ�����...
	//�������ݰ����������
	PictureHandle handle;
	int m_nTexWidth; //psl��һ��С����Ŀ���
	int m_nTexHeight;
	int m_nPslTexRow; //С����ϲ�֮��Ĵ������м��м���..
	int m_nPslTexCol; //��
	int m_nTexNum; //С�������

	PSTEXTUREINFO()
	{
		handle = INVALID_PICTURE_HANDLE;
		m_nTexWidth = 0;
		m_nTexHeight = 0;
		m_nPslTexRow = 0;
		m_nPslTexCol = 0;
		m_nTexNum = 0;
	}
};

struct PSTEXTUREINFOSTRUCT
{
	PSTEXTUREINFO PsTextureInfo;
	DWORD dwTime; //��
	float dataSize; //M
	IBaseRenderer* pRenderer;

	PSTEXTUREINFOSTRUCT(IBaseRenderer* prender)
	{
		pRenderer = prender;
		dwTime = 0;
		dataSize = 0.0f;
	}

	~PSTEXTUREINFOSTRUCT()
	{
		pRenderer->FreePicture(PsTextureInfo.handle);
	}

private:
	PSTEXTUREINFOSTRUCT(const PSTEXTUREINFOSTRUCT&);
	PSTEXTUREINFOSTRUCT& operator=(const PSTEXTUREINFOSTRUCT&);
};

struct PSPICHANDLESTRUCT //ͼƬ��---С����
{
	PictureHandle handle;
	DWORD dwTime; //��
	float dataSize; //M
	IBaseRenderer *pRenderer;

	PSPICHANDLESTRUCT(IBaseRenderer* prender)
	{
		pRenderer = prender;
		handle = INVALID_PICTURE_HANDLE;
		dwTime = 0;
		dataSize = 0.0f;
	}

	~PSPICHANDLESTRUCT()
	{
		pRenderer->FreePicture(handle);
	}

private:
	PSPICHANDLESTRUCT(const PSPICHANDLESTRUCT&);
	PSPICHANDLESTRUCT& operator=(const PSPICHANDLESTRUCT&);
};

struct PATHPOINT 
{
	POINT pt;
	BOOL m_bCurve;
	BOOL m_bLink;

	POINT pre_ControlPt;
	POINT next_ControlPt;

	BOOL bKeyPoint; //Ϊtrue����CGeneralParticleSystem::SetKeyPointIndex(int)ʹ�õı�ǵ�

	PATHPOINT()
	{
		pt.x = 0;
		pt.y = 0;
		pre_ControlPt.x = 0;
		pre_ControlPt.y = 0;
		next_ControlPt.x = 0;
		next_ControlPt.y = 0;
		m_bCurve = FALSE;
		m_bLink = FALSE;

		bKeyPoint = FALSE;
	}

	~PATHPOINT()
	{
	}
};

//ת����Ч�ķֿ�������Ϣ -----------����-----
struct Vertex
{
	float u;
	float v;
	Vertex()
	{
		u = 0.0f;
		v = 0.0f;
	}
};

typedef std::vector<Vertex> VERTEX_LIST;

struct SHAPE
{
	VERTEX_LIST vertexs; //�����������ϵͳ����Ҫ... �ڻ��Ƶ�ʱ����Ҫ
	Vertex center; //��ʾÿ�����ӵ�λ��
	void reset()
	{
		vertexs.clear();
	}
};

typedef std::vector<SHAPE> SHAPE_LIST;
//ת����Ч�ķֿ�������Ϣ -----------����-----


struct PSL
{
	int m_nVersion; //2012-3-1 �汾�� ��3�������ڰ汾4����������;����psl��ʱ��Ѱ汾�ż���������ʹ��

	int m_nAbsrtactPsl; //Ĭ��ΪFALSE ---TRUE�ǳ���psl..����������Ϣ
	std::wstring m_strVtxsObjectPath; //����������Ч��Ӧ��vertex�ļ���
	int m_nDirMotion; //����������Ч--�Ƿ������˶�����
	int m_nPSConfigReq;
	int m_nPosType;	  //�㡢Բ�����Ρ�ֱ�߶Ρ��Զ���·��
	int m_nParticlePathInitPosType; //��ͷ��������ϴ�����λ��
	int m_nParticleSizeRandType;
	int m_nMoveType;
	int m_nPathOrder; //�Ƿ�·�������������
	int m_nUVmirrorh; //����ˮƽ����
	int m_nUVmirrorv; //����ֱ����
	FPOINT m_point0;
	FPOINT m_point1;  //1�߷���ʱ������һ���˵�
	FPOINT m_pointEmitterSpinCent; //��������ת����..�������Ч�ҽӵ���������
	FPOINT m_pointParticleHole; //����Ӱ���..�ڶ�
	int m_nSysPosRadiusX;
	int m_nSysPosRadiusY;
	CRECT m_rct;
	// ·���ļ���...
	std::wstring m_strFullPathFile;
	float m_fPathZoom; //·�����ű���..����һ��·���ļ������pslʹ��..�����Ǵ�С��һ��..
	float m_fPathSpin; //·����ת�Ƕ�..ʹ�÷���ͬ��
	int m_nPathMirrorType; //·����������...ʹ�÷���ͬ��
	int m_nNumPointFullPath; //Ϊ�Ժ�ı���·����������ʱ�������ӵ��˶��ٶ�׼��...
	//Ĭ��-1 �򲻸ı��˶��ٶȣ���������·������Ƶ��Ϊ8.0f/dis

	int m_nParFillMode;				//false ����� -- true ���
	int m_nEmitterType;				//false ������ -- true ����
	int m_nSingleParticleAni;		//false �����Ӷ���...
	int m_nParticleAniTextureIndex1;//�����Ӷ����Ƿ�������زĵĵ�һ֡��ʼ
	int m_nAniPlaySpeed;			//0---50 default: 10	
	float m_fAniSpeedRand;			//0---10 default: 0
	int m_nParticleColorChange;		//false �Ƿ���ɫ����
	int m_nParticleColorRand;		//ɫ�����...һ�ֺ���ֵ�bugɫ��Ч��..
	int m_nTrackFx;					//�Ƿ񵯵�����

	int m_nSysMaxParticles;			//0-2000

	float m_fSysLife;				//0--1000 ---100.0f
	int m_nEmissionRate;			//0--1000
	int m_nEmitterSpeed;			//0--1000
	float m_fParticleLifeMin;		//0--1000 ---100.0f
	float m_fParticleLifeMax;		//0--1000 ---100.0f
	float m_fEmissionDirection;		//0--3600 --360.0f
	float m_fEmissionSpread;		//0--3600 --360.0f
	float m_fSpeedMin;				//0--10000 --1000.0f
	float m_fSpeedMax;				//0--10000 --1000.0f
	float m_fGravity;			//-1000--1000 --100.0f
	float m_fGravityRand;			//0--50 --5.0f
	float m_fRadialAcc;			//-200--200 --20.0f
	float m_fRadialAccRand;			//0--50 --5.0f
	float m_fTangAcc;			//-200--200 --20.0f
	float m_fTangAccRand;			//0--50 --5.0f
	float m_fParSizeStrtX;			//0---100.0f
	float m_fParSizeStrtY;			//0---100.0f
	float m_fParSizeEndX;			//0---100.0f
	float m_fParSizeEndY;			//0---100.0f
	float m_fParSizeVar;			//0--100 --1.0f
	float m_fParSpinStrt;			//0--3600 --360.0f
	float m_fParSpinEnd;			//0--3600 --360.0f
	float m_fParSpinVar;			//0--100 --1.0f

	float m_fRotateCentX;			//-1.0---+1.0f (-0.5f---+0.5f��ʾͼ������ϵ�����..)
	float m_fRotateCentY;			//(-0.5--+0.5f)���ֵ��ʾ��ת������ͼ����..

	float m_fParAngleStrt;			//0--3600 --360.0f
	float m_fParAngleEnd;			//0--3600 --360.0f
	float m_fParAngleVar;			//0--100 --1.0f

	int m_nParAlphaStrt;			//0--255
	int m_nParAlphaEnd;				//0--255
	float m_fParAlphaVar;			//0--100 --1.0f
	int m_nParAlphaMid;				//������255..���ؼ�֡ģ��˫�ؼ�֡
	float m_fParAlphaMidTime;

	float m_fSizeRandom; //0-1000 --10.0f
	float m_fSpinRandom; //0-3600 -- 360.0f
	float m_fAngleRandom; //0-3600 -- 360.0f
	float m_fAlignToMotionAngle; //-3600-3600 -- -360.0f--360.0f
	int m_nAlignToMotion; //���ӷ��䷽����·������󶨣�
	int m_nAlignToPath; //���ӷ�����·�������--2009-03-13
	int m_nMotionToPath; //��������·���ƶ�-2009-03-26 �����䷽��ͼ��ٶȲ�����Ч��
	//�ɳ�ʼ�����ٶȾ���..ֻҪ��С�����Ƿ���..

	int m_nAlignToEmitter; //���ӷ����뷢������ת�Ƕ���� Ĭ��ֵΪ0����ʾ�����޹ء�2010-03-15

	int m_nAlignToHole; //��������������������˶�..����Ҳ��ת������...

	int m_nParticleRandAngle; //2009-4-6 ���ӳ��ֵĽǶ�����������ӵ��������������в��ٸı�.. �汾1--2
							  //2009-5-31 -------------------����m_fSpinRandom��ֵ�����ϸı�. .�汾3
	int m_nParticleRandAngleSelf; //������� 09-9-15���ӵ�-----����m_fAngleRandom��ֵ�����ϸı�
	//����ʵ�ֺܶ����ڵ��ϵĴ�͸��Ч������Ҷ��.. //SpecialParticleSystemû��ʵ�ָò���

	int m_nParColorStrtR;			//0--255
	int m_nParColorStrtG;			//0--255
	int m_nParColorStrtB;			//0--255
	int m_nParColorEndR;			//0--255
	int m_nParColorEndG;			//0--255
	int m_nParColorEndB;			//0--255
	float m_fParColorVar;			//0--100 --1.0f
	float m_fPSLzoom;				//0--500 --5.0f //Ĭ��ֵ1.0f
	float m_fEmitterSpin;			//��������ת�Ƕ� ��λ���Ƕ�
 
	int m_nBlendMode; //0-7 8��ģʽ --default: 0
	int m_nDestBlendMode; //3�� ���PSL_RENDSTATE

	std::vector<std::wstring> m_vectorTexturePath; //������֧��..

	int m_nRowNum; //2012-3-1 psl�汾4������..������������ //�����Ѷ���������ƴ��һ�Ŵ�ͼ����
	int m_nColNum;
	int m_nTextureNum;

	CRECT m_rctMaxBox;

	//2010-1-25 ��ʾ���ڽ����psl��Ч��λ������
	// ���ڷǽ�����Ч���������Ӧ����(0,0)
	int m_nInterfacePsl; //Ĭ��ΪFALSE ---TRUE���ʾ����ЧΪ������Ч,������Ҫ�������ֵ..
	FPOINT m_ptInterfacialCenter; //����ڽ������ĵ�������꣨ˮƽ��x+����ֱ��y+��

	int m_nSkillEffectSpritePoint; //BOOLֵ �Ƿ�ʹ�þ������Ч�ҵ�.. Ĭ��false

	PSL()
	{
		m_nAbsrtactPsl = FALSE;
		m_strVtxsObjectPath = L"";
		m_nDirMotion = FALSE;
		m_nPSConfigReq = PSLCONFIGREQ_L;
		m_nPosType = 0;
		m_nParticlePathInitPosType = PARTICLEPATHPOS_START;
		m_nMoveType = PSLMOVETYPE_DEFAULT;
		m_nParticleSizeRandType = PARTICLESIZERAND_XY;
		m_nEmitterType = 1;
		m_nSingleParticleAni = 0;
		m_nParticleAniTextureIndex1 = 0;
		m_nAniPlaySpeed = 10;
		m_fAniSpeedRand = 0.0f;
		m_nParFillMode = 0;
		m_nParticleColorChange = 0;
		m_nParticleColorRand = 0;
		m_nTrackFx = 0;

		m_nPathOrder = 0;
		m_nUVmirrorh = 0;
		m_nUVmirrorv = 0;

		m_point0 = FPOINT(0, 0);
		m_point1 = FPOINT(0, 0);
		m_pointEmitterSpinCent = FPOINT();
		m_pointParticleHole = FPOINT();
		m_nSysPosRadiusX = 100;
		m_nSysPosRadiusY = 100;
		m_rct = CRECT(-50, -50, 50, 50);//���η��������������
		m_strFullPathFile = L"";
		m_fPathZoom = 1.0f;
		m_fPathSpin = 0.0f;
		m_nPathMirrorType = PATHMIRRORTYPE_NONE;
		m_nNumPointFullPath = -1;

		m_fSysLife = 5.0f;
		m_nEmissionRate = 100;
		m_nEmitterSpeed = 100;
		m_fParticleLifeMin = 1.0f;
		m_fParticleLifeMax = 5.0f;
		m_fEmissionDirection = 0.0f;
		m_fEmissionSpread = 0.0f;
		m_fSpeedMin = 0.0f;
		m_fSpeedMax = 100.0f;
		m_fGravity = 0.0f;
		m_fGravityRand = 0.0f;
		m_fRadialAcc = 0.0f;
		m_fRadialAccRand = 0.0f;
		m_fTangAcc = 0.0f;
		m_fTangAccRand = 0.0f;
		m_fParSizeStrtX = 1.0f;
		m_fParSizeStrtY = 1.0f;
		m_fParSizeEndX = 1.0f;
		m_fParSizeEndY = 1.0f;
		m_fParSizeVar = 0.5f;
		m_fParSpinStrt = 0.0f;
		m_fParSpinEnd = 0.0f;
		m_fParSpinVar = 0.5f;
		
		m_fRotateCentX = 0.0f;
		m_fRotateCentY = 0.0f;

		m_fParAngleStrt = 0.0f;
		m_fParAngleEnd = 0.0f;
		m_fParAngleVar = 0.5f;
		m_nParAlphaStrt = 255;
		m_nParAlphaEnd = 0;
		m_fParAlphaVar = 0.5f;
		m_nParAlphaMid = 128;
		m_fParAlphaMidTime = 0.5f;

		m_nSysMaxParticles = 1000;

		m_fSizeRandom = 0.0f; //0-1000 --10.0f
		m_fSpinRandom = 0.0f; //0-3600 -- 360.0f
		m_fAngleRandom = 0.0f; //0-3600 -- 360.0f
		m_fAlignToMotionAngle = 0.0f;
		m_nAlignToMotion = 0;
		m_nAlignToPath = 0;
		m_nMotionToPath = 0;
		m_nAlignToEmitter = 0;
		m_nAlignToHole = 0;
		m_nParticleRandAngle = 0;
		m_nParticleRandAngleSelf = 0;

		m_nParColorStrtR = 255;
		m_nParColorStrtG = 255;
		m_nParColorStrtB = 0;
		m_nParColorEndR = 255;
		m_nParColorEndG = 0;
		m_nParColorEndB = 0;
		m_fParColorVar = 0.5f;
		m_fPSLzoom = 1.0f;
		m_fEmitterSpin = 0.0f;

		m_nBlendMode = g_BlendingModes[0].mode;
		m_nDestBlendMode = PSLRENDSTATE_ALPHA;

		std::wstring pTempTexPath = L"pirerreE.tga";
		m_vectorTexturePath.clear();
		m_vectorTexturePath.push_back(pTempTexPath);
		
		m_nRowNum = 1;
		m_nColNum = 1;
		m_nTextureNum = 1;

		m_rctMaxBox = CRECT(-400, -400, 400, 400);

		m_nInterfacePsl = FALSE;
		m_ptInterfacialCenter = FPOINT(0, 0);

		m_nSkillEffectSpritePoint = FALSE;
	}

};

typedef int PSLHandle;
const PSLHandle INVALID_PSL_HANDLE = 0;

};