// ParticleSystemEditorDoc.h : CParticleSystemEditorDoc ��Ľӿ�
//
#pragma once

#include "ParticleSystemEditorView.h"
#include "ParticleParameterView.h"
#include "ParticleSysInfoView.h"

class CParticleSystemEditorDoc : public CDocument
{
protected: // �������л�����
	CParticleSystemEditorDoc();
	DECLARE_DYNCREATE(CParticleSystemEditorDoc)

public:
	virtual ~CParticleSystemEditorDoc();

//private:
//	CParticleSystem* m_pParticleSys;

//public:
//	CParticleSystem* GetParticleSys() { return m_pParticleSys; }
//	void SetParticleSys(CParticleSystem* pParticleSys) { m_pParticleSys = pParticleSys; }

// ��д
public:
	virtual BOOL OnNewDocument();
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual void Serialize(CArchive& ar);

	CParticleSystemEditorView* GetParticleSystemEditorView();
	CParticleParameterView* GetParticleParameterView();
	CParticleSysInfoView* GetParticleSysInfoView();

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()
};


