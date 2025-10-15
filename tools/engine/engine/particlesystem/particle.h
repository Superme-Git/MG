#pragma once

#include "commonfuncs.h"

namespace Nuclear
{

struct CParticle
{

	CParticle()
	{
		m_fSizeX = 0.0f; 
		m_fSizeY = 0.0f; 
		m_fLifetime = 0.0f; 
		
		m_fAge = 0.0f; 
		m_fFullScreenTime = 0.0f;

		m_fSpin = 0.0f; 
		m_fAngle = 0.0f; 
		m_fParSizeStrtX = 0.0f;	
		m_fParSizeStrtY = 0.0f;	
		m_fParSizeEndX = 0.0f;		
		m_fParSizeEndY = 0.0f;		
		m_fParSizeVar = 0.0f;
		m_nStrTextureIndex = 0;
		m_nCurTextureIndex = 0;
		m_nStrtPathIndex = 0;
		m_nCurPathIndex = 0;
		m_fPathMotionSpeed = 0.0f;
		m_fAniSpeed = 10.0f;

		m_fParAlphaMidTime = 0.5f;
	}


	float m_fLifetime;  //���� --����ʱ����䣬�ɷ�����������
	float m_fSizeX;		//��С --��m_vPosһ������ı��ε�λ��
	float m_fSizeY;		//��С --��m_vPosһ������ı��ε�λ��
	float m_fSpin;		//��ת�Ƕ�
	float m_fAngle;		//�����Ƕ�

	XPCOLOR m_color;
	XPVECTOR3 m_vPos; //�������ĵ�λ�� --�������ĵ�λ�ü����ĸ���������
	XPVECTOR3 m_vDir; //�ٶ�(ʸ��)

	//ֻ��CParticleSystem.Update()��ѭ������...
	__forceinline bool Update(float fTimeDelta)
	{
		//����
		m_fAge += fTimeDelta;

		if( fTimeDelta>PSUPDATECRITICALTIME )
			fTimeDelta = 0.0f;

		//���������ڵ�...
		if( m_fAge+m_fFullScreenTime > m_fLifetime)
			return false; //����

		//��������
	//	m_vPos += m_vDir * fTimeDelta;//����������..��һ��moveType���ֻ����ps��֪��λ����Ϣ..ȫ��
		float coef = 1.0f - m_fAge/m_fLifetime;

		float coefAlpha = GetPowfLerpValue(coef, m_fAlphaVar);
		float fAlpha = 0.0f;
		//��midֵģ��˫�ؼ�֡midAlpha��������10.0f...��Ч��ϵ��뷨..
		if(1.0f-coef < m_fParAlphaMidTime)
			fAlpha = coefAlpha*(m_colorStrt.a - m_nAlphaMid) + m_nAlphaMid;
		else
			fAlpha = coefAlpha*(m_nAlphaMid - m_colorEnd.a) + m_colorEnd.a;

		fAlpha = fAlpha>255.0f ? 255.0f:fAlpha;
		m_color.a = (unsigned char)(fAlpha);

		float coefCR = GetPowfLerpValue(coef, m_fColorVar);

		m_color.r = (unsigned char)( coefCR*(m_colorStrt.r - m_colorEnd.r) +m_colorEnd.r);
		m_color.g = (unsigned char)( coefCR*(m_colorStrt.g - m_colorEnd.g) +m_colorEnd.g);
		m_color.b = (unsigned char)( coefCR*(m_colorStrt.b - m_colorEnd.b) +m_colorEnd.b);

		float coefSZ = GetPowfLerpValue(coef, m_fParSizeVar);

		m_fSizeX = coefSZ*(m_fParSizeStrtX - m_fParSizeEndX) + m_fParSizeEndX;
		m_fSizeY = coefSZ*(m_fParSizeStrtY - m_fParSizeEndY) + m_fParSizeEndY;

		float coefSP = GetPowfLerpValue(coef, m_fSpinVar);

		m_fSpin = coefSP*(m_fSpinStrt - m_fSpinEnd) + m_fSpinEnd;

		float coefAn = GetPowfLerpValue(coef, m_fAngleVar);

		m_fAngle = coefAn*(m_fAngleStrt - m_fAngleEnd) + m_fAngleEnd;
		return true; //���ŵ�����
	}

	float m_fParSizeStrtX;			//0---100.0f
	float m_fParSizeStrtY;			//0---100.0f
	float m_fParSizeEndX;			//0---100.0f
	float m_fParSizeEndY;			//0---100.0f
	float m_fParSizeVar;			//0---1.0f

	float m_fColorVar;
	XPCOLOR m_colorStrt;
	XPCOLOR m_colorEnd;

	float m_fAlphaVar;
	int m_nAlphaMid; //alpha��ֵ�����Դ���255����һ���ؼ�֡��ģ���2���ؼ�֡
	float m_fParAlphaMidTime;
	
	float m_fSpinStrt;
	float m_fSpinEnd;
	float m_fSpinVar;

	float m_fAngleStrt;
	float m_fAngleEnd;
	float m_fAngleVar;

	float m_fAge; //����ȫ����Ч�����ֵ���ᱻǿ���޸ĵ�...
	float m_fFullScreenTime; //ȫ����Ч�б�ǿ�Ƽ����˵��������� ---һ�������������ֵ��Ҫ�޸�Ϊ0

	XPVECTOR3 m_vDir0; //��ʼ�����ٶ�(ʸ��)
	float m_fAngle0; //�����...��ʼ����Ƕ�

	XPVECTOR3 m_vPos0; //�������ĵ��ʼλ�� --������������ܷ�������ѭ��ʱ��Ĵ��� 09-6-4

	int m_nStrTextureIndex; //��������
	int m_nCurTextureIndex; //��ǰ��������..Ϊʵ�ֵ����Ӷ��������..
	//�����²�����ʱ�����... ������ϵͳ������֧��...�� --����ʵ�ֵ����Ӷ���..
	float m_fAniSpeed; //ÿ�����ӵı任Ƶ�ʶ����ܲ���ͬ

	float m_fPathMotionSpeed; //�ɳ�ʼ�����ٶȾ���..ֻҪ��С�����Ƿ��򡢼��ٶ�..
	int m_nStrtPathIndex;
	int m_nCurPathIndex; //��ǰ������·�����������û��༭��·��..������ʸ���ַ�·�����е��ĸ�λ��...
	//����ʵ����������·���˶�...����¼��ǰ�����˶������ĸ���..
};

};