// DistortionEditorDoc.h : CDistortionEditorDoc 类的接口
//
#pragma once

#include "DistortionEditorView.h"
#include "DistortionInfoView.h"
#include "DistortionStatusView.h"

#include "..\engine\map\pdistortionobject.h"

class CDistortionEditorDoc : public CDocument
{
protected: // 仅从序列化创建
	CDistortionEditorDoc();
	DECLARE_DYNCREATE(CDistortionEditorDoc)

// 属性
private:
	Nuclear::PDistortionObject m_distortionObject;

	CString m_strDistortionFilePath;

// 操作
public:
	Nuclear::PDistortionObject& GetDistortionObject() { return m_distortionObject; }
	void SetDisFileName(CString strDistortionFilePath) { m_strDistortionFilePath = strDistortionFilePath; }

// 重写
public:
	virtual BOOL OnNewDocument();
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	virtual void Serialize(CArchive& ar);

public:
	CDistortionEditorView* GetDistortionEditorView();
	CDistortionInfoView* GetDistortionInfoView();
	CDistortionStatusView* GetDistortionStatusView();

// 实现
public:
	virtual ~CDistortionEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()

};


