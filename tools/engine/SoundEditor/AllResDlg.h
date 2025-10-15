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
		EnvSet envSoundAreas;	//������������
		BkgSet bkgSoundAreas;	//������������
		bool isInDefault;//�Ƿ���Ĭ�ϱ�����������
		_SoundInMapInfo() : isInDefault(false) {}
	} SoundInMapInfo;
	typedef std::map<unsigned int, SoundInMapInfo> SoundInMapInfoMap;//key��������m_soundInfos���±꣬Ҳ�������б��itemdata
	typedef std::vector<Nuclear::XPEnvSoundArea*> EnvAreaVet;
	typedef std::vector<Nuclear::XPBkgSoundArea*> BkgAreaVet;
	typedef struct _MapInfo
	{
		std::wstring mapname;
		bool isModify;
		EnvAreaVet envSoundAreas;	//������������
		BkgAreaVet bkgSoundAreas;	//������������
		Nuclear::XPBkgDefaultSoundArea* pDefBkgSoundArea;				//Ĭ�ϱ���������
		SoundInMapInfoMap soundInMap;
		_MapInfo() : mapname(L""), isModify(false), pDefBkgSoundArea(NULL) {}
	} MapInfo;
	typedef std::vector<MapInfo> PMaps;
	typedef std::map<std::wstring, unsigned int> SoundNameInfoMap;//key �����������֣�value��������m_soundInfos���±�
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
		int ref;			//������
		float size;			//��С��kb��
		PMapIDSet maps;		//�����˴�������map��PMaps�����ID
		bool deleted;		//�Ƿ��Ѿ�ɾ��
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
