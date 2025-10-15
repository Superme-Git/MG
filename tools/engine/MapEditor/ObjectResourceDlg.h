#ifndef ELEMENT_RES_DLG_H
#define ELEMENT_RES_DLG_H
#include "..\engine\common\xptypes.h"
#include "afxwin.h"
#include <map>

class CMapEditorView;



class CObjectResourceDlg : public CDialog
{
	DECLARE_DYNAMIC(CObjectResourceDlg)

private:
	typedef struct _NameKey {
		Nuclear::PMap::MAP_OBJ_TYPE m_Type;
		Nuclear::SubMap::ObjMapKey m_MapKey;
		bool operator < (const _NameKey& o) const
		{
			if ( ((int)m_MapKey < (int)o.m_MapKey) || 
				(  (int)m_Type < (int)o.m_Type && (int)m_MapKey == (int)o.m_MapKey)  )
				return true;
			return false;
		}
	} sNameKey;

	typedef std::map<sNameKey, int> CountMap;
	typedef std::vector<sNameKey> NameKeyVet;

	CountMap m_Counts;
	NameKeyVet m_NameKeys;
	CListCtrl m_EleResList;
	CMapEditorView* m_pMapEditorView;

public:
	enum { IDD = IDD_OBJ_RESOURCE };
	void ResetLists();
	virtual BOOL Create(CMapEditorView* pMapEditorView);
	virtual ~CObjectResourceDlg();
	CObjectResourceDlg(CWnd* pParent = NULL);   

protected:
	DECLARE_MESSAGE_MAP()
	virtual void OnOK();

	virtual void DoDataExchange(CDataExchange* pDX);    

private:
	void ShowCounts();
	void ResetCounts();

public:
	afx_msg void OnLvnEndlabeleditEleResList(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtonClear();
	afx_msg void OnBnClickedButtonCalRef();
	afx_msg void OnBnClickedRefresh();
	virtual BOOL OnInitDialog();
};

#endif