// ParticleSystemEditorDoc.h : CParticleSystemEditorDoc 类的接口
//
#pragma once

#include "ParticleSystemEditorView.h"
#include "ParticleParameterView.h"
#include "ParticleSysInfoView.h"

class CParticleSystemEditorDoc : public CDocument
{
protected: // 仅从序列化创建
	CParticleSystemEditorDoc();
	DECLARE_DYNCREATE(CParticleSystemEditorDoc)

public:
	virtual ~CParticleSystemEditorDoc();

//private:
//	CParticleSystem* m_pParticleSys;

//public:
//	CParticleSystem* GetParticleSys() { return m_pParticleSys; }
//	void SetParticleSys(CParticleSystem* pParticleSys) { m_pParticleSys = pParticleSys; }

// 重写
public:
	virtual BOOL OnNewDocument();
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual void Serialize(CArchive& ar);

	CParticleSystemEditorView* GetParticleSystemEditorView();
	CParticleParameterView* GetParticleParameterView();
	CParticleSysInfoView* GetParticleSysInfoView();

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
};


