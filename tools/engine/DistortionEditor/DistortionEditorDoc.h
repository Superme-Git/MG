// DistortionEditorDoc.h : CDistortionEditorDoc ��Ľӿ�
//
#pragma once

#include "DistortionEditorView.h"
#include "DistortionInfoView.h"
#include "DistortionStatusView.h"

#include "..\engine\map\pdistortionobject.h"

class CDistortionEditorDoc : public CDocument
{
protected: // �������л�����
	CDistortionEditorDoc();
	DECLARE_DYNCREATE(CDistortionEditorDoc)

// ����
private:
	Nuclear::PDistortionObject m_distortionObject;

	CString m_strDistortionFilePath;

// ����
public:
	Nuclear::PDistortionObject& GetDistortionObject() { return m_distortionObject; }
	void SetDisFileName(CString strDistortionFilePath) { m_strDistortionFilePath = strDistortionFilePath; }

// ��д
public:
	virtual BOOL OnNewDocument();
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual void Serialize(CArchive& ar);

public:
	CDistortionEditorView* GetDistortionEditorView();
	CDistortionInfoView* GetDistortionInfoView();
	CDistortionStatusView* GetDistortionStatusView();

// ʵ��
public:
	virtual ~CDistortionEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// ���ɵ���Ϣӳ�亯��
protected:
	DECLARE_MESSAGE_MAP()

};


