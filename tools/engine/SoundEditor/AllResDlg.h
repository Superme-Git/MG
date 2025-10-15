#pragma once
#include "afxcmn.h"
#include <map>
#include <vector>
#include <set>

// CAllResDlg dialog
namespace Nuclear
{
	class XPEnvSoundArea;
	class XPBkgSoundArea;
	class XPBkgDefaultSoundArea;
}
class CSoundEditorDoc;
class CAllResDlg : public CDialog
{
	DECLARE_DYNAMIC(CAllResDlg)
private:
	typedef std::set<Nuclear::XPEnvSoundArea*> EnvSet;
	typedef std::set<Nuclear::XPBkgSoundArea*> BkgSet;
	typedef struct _SoundInMapInfo
	{
		EnvSet envSoundAreas;	//环境音区域们
		BkgSet bkgSoundAreas;	//背景音区域们
		bool isInDefault;//是否在默认背景区域里面
		_SoundInMapInfo() : isInDefault(false) {}
	} SoundInMapInfo;
	typedef std::map<unsigned int, SoundInMapInfo> SoundInMapInfoMap;//key是声音在m_soundInfos的下标，也是声音列表的itemdata
	typedef std::vector<Nuclear::XPEnvSoundArea*> EnvAreaVet;
	typedef std::vector<Nuclear::XPBkgSoundArea*> BkgAreaVet;
	typedef struct _MapInfo
	{
		std::wstring mapname;
		bool isModify;
		EnvAreaVet envSoundAreas;	//环境音区域们
		BkgAreaVet bkgSoundAreas;	//背景音区域们
		Nuclear::XPBkgDefaultSoundArea* pDefBkgSoundArea;				//默认背景音区域
		SoundInMapInfoMap soundInMap;
		_MapInfo() : mapname(L""), isModify(false), pDefBkgSoundArea(NULL) {}
	} MapInfo;
	typedef std::vector<MapInfo> PMaps;
	typedef std::map<std::wstring, unsigned int> SoundNameInfoMap;//key 是声音的名字，value是声音在m_soundInfos的下标
	typedef std::set<std::wstring> NotFoundSoundSet;
public:
	typedef struct _SortInfo
	{
		short id;
		short isup;
	} SortInfo;
	typedef std::set<size_t> PMapIDSet;
	typedef struct _SoundInfo
	{
		std::wstring uri;
		std::wstring mapnames;
		int ref;			//引用数
		float size;			//大小（kb）
		PMapIDSet maps;		//引用了此声音的map在PMaps数组的ID
		bool deleted;		//是否已经删除
		_SoundInfo() : uri(L""), ref(0), size(0.0f), deleted(false) {}
	} SoundInfo;
	typedef std::vector<SoundInfo> SoundInfoVector;

private:
	CListCtrl m_resListCtrl;
	SoundInfoVector m_soundInfos;
	SoundNameInfoMap m_soundInfoMap;
	NotFoundSoundSet m_notfoundset;
	PMaps m_maps;
	bool m_bIsInited;
	bool m_bHasCountRef;
	int m_nMinWidth;
	int m_nMinHeight;
	SortInfo m_sortInfo;

public:
	CSoundEditorDoc *pDoc;

public:
	CAllResDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CAllResDlg();

// Dialog Data
	enum { IDD = IDD_SOUND_RESOURCE };
private:
	void RenewSize();
	void ResetLists();
	void ReleaseMaps();
	void ResetMaps();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
public:
	virtual BOOL OnInitDialog();

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedExportToExcel();
	afx_msg void OnBnClickedDelete();
	afx_msg void OnBnClickedRefresh();
	afx_msg void OnBnClickedCountRef();
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnHdnItemclickListRes(NMHDR *pNMHDR, LRESULT *pResult);
};
