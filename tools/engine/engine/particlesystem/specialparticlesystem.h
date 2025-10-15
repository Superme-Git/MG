#pragma once

#include "particlesystem.h"

namespace Nuclear
{

class CSpecialParticleSystem :	public CParticleSystem
{
public:
	CSpecialParticleSystem(const XHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer, const std::wstring& pslName);

	~CSpecialParticleSystem(void);

public:
	virtual bool Render(bool bUpdate = false, bool bEndScene = true); 
	//���Ƴ�������..ʹ�õĶ�������ͨ��particles��shapeList�õ��������������..
	virtual bool Update(float fTimeDelta); //����������������ٵ���render()

	virtual void Reset(void);

	virtual void ResetVerticesVector();

	virtual bool SetShapePsTexture(PictureHandle, float fu=1.0f, float fv=1.0f); //���Կ��ϴ����ķ�2������
	virtual void SetShapePsScale(float fcx=1.0f, float fcy=1.0f); //�����������ű���
	virtual void SetShapePsVertexs(const SHAPE_LIST& shapeList);
	virtual void SetSilentTime(float ftime); //ת����Ч�����ʱ����ͣ���ڵ�һ֡
	virtual PictureHandle GetSpecialPSTextureHandle()const { return m_pD3dTextureShapes; }

private:
	bool CreateNewParticles(float fTimeDelta); //update��..����������
	void InitParticlePos(CParticle* part)const;

private:
	//�����ǳ�������Ч�����е�---
	//�������Ӷ�������
	SHAPE_LIST m_shapeList; //��ʼ����...updateֻ�޸�particles���������..	
	PictureHandle m_pD3dTextureShapes; //����������Ч�õ�����... ----��particleManager���洫������..�����治������
	int m_nShapeTextureWidth; //���㱬ը���ġ�ͨ������������㶥��ֵ������ÿ��Ŀ�ʼ������������...
	int m_nShapeTextureHeight;
	float m_fmaxu; //������Ч��������..
	float m_fmaxv;
	float m_fShapePsScalex; //���ű���
	float m_fShapePsScaley;
	float m_fSilentTime; //�����ʱ����ͣ���ڵ�һ֡
	float m_fSilentAge;  //�Ѿ�ͣ����ʱ��

	bool m_bDir; //�˶����� -- �������ӵ��˶�����պ��෴

};

};