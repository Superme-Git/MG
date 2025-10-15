// DXTestDoc.h : interface of the CDXTestDoc class
//


#pragma once


class CDXTestDoc : public CDocument
{
protected: // create from serialization only
	CDXTestDoc();
	DECLARE_DYNCREATE(CDXTestDoc)

// Attributes
public:

// Operations
public:

// Overrides
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// Implementation
public:
	virtual ~CDXTestDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
};


