#pragma once
#include <vector>
#include <map>
#include <string>
#include "ToolsMap.h"
#include "imginfo.h"
#include "..\engine\engine\enginebase.h"
#include "..\engine\effect\effect.h"
#include "..\engine\map\plinkedobject.h"
#include "..\engine\world\distortbase.h"


class CAllObjectResourceDlg : public CDialog, public Nuclear::EngineBase
{
	DECLARE_DYNAMIC(CAllObjectResourceDlg)


private:
	typedef std::set<CString> ErrorStringSet;
	typedef std::map<unsigned int, std::vector<unsigned int>> ReferenceMap;
	typedef struct _MapKeys
	{
		int m_MapID;
		Nuclear::SubMap::ObjMapKey m_MapKey;
		bool operator < (const _MapKeys &o) const {
			if (m_MapID < o.m_MapID)
				return true;
			else if (m_MapID == o.m_MapID)
				if (m_MapKey < o.m_MapKey)
					return true;
			return false;
		}
	} sMapKeys;
	typedef std::set<sMapKeys> MapKeySet;

	typedef std::map<std::wstring, sImgInfo*> ImageResourceMap;

	typedef std::map<std::wstring, unsigned int> ObjectNameToInfoMap;
	typedef struct _MapInfo
	{
		std::wstring m_Uri;
		std::wstring m_Names;
		CToolsMap* m_pMap;
	} sMapInfo;
	typedef std::vector<sMapInfo> MapInfoArray;

public:
	typedef struct _ObjInfo
	{
		std::wstring m_Uri;
		Nuclear::PMap::MAP_OBJ_TYPE m_Type;
		int m_DirectRefCount;			
		int m_IndirectRefCount;		
		float m_Size;				
		MapKeySet m_MapKeys;	
		std::wstring m_DirectMapNames;	
		std::wstring m_IndirectFilenames;	
		std::set<std::wstring> m_IndirectFileSet;
		bool m_bDeleted;	
		_ObjInfo() : m_Uri(L""), m_Type(Nuclear::PMap::MAP_OBJ_ELEMENT), m_DirectRefCount(0), m_IndirectRefCount(0), m_Size(0.0f), m_bDeleted(false) {}
	} sObjInfo;
	typedef std::vector<sObjInfo> ObjInfoVector;

	typedef struct _SortInfo
	{
		short m_Id;
		short m_IsUp;
	} sSortInfo;

public:
	
	enum { IDD = IDD_OBJ_RESOURCE };


private:
	ReferenceMap m_IndirectRefMap;
	CListCtrl m_EleResList;
	ObjInfoVector m_ObjInfos;
	ObjectNameToInfoMap m_ObjInfoMap;
	MapInfoArray m_MapInfos;
	bool m_bHasRefCount;
	bool m_bSizing;
	Nuclear::CRECT m_PreviewRect;
	int m_nMinWidth;
	int m_nMinHeight;
	sImgInfo* m_pImgInfo;
	Nuclear::Effect* m_pEffect;
	Nuclear::PLinkedObject *m_pLinkedObject;
	Nuclear::DistortBase *m_pDistort;
	int m_nObjectWidth;
	int m_nObjectHeight;
	ImageResourceMap m_ImgageResources;
	std::map<Nuclear::PictureHandle, Nuclear::PicResrc*> m_PicHandleToResMap;				
	sSortInfo m_SortInfo;
	ErrorStringSet m_ErrorStringSet;


	
	Nuclear::Renderer *m_pRenderer;
	Nuclear::SystemResourceManager *m_pSystemResourceMgr;
	Nuclear::ConfigManager *m_pConfigMgr;
	Nuclear::EffectManager *m_pEffectMgr;
	Nuclear::SpriteManager *m_pSpriteMgr;
	Nuclear::AniManager *m_pAniMgr;



private:
	float GetOtherFileSize(const std::wstring& name);
	float GetEffectSize(const std::wstring& name);
	float GetImageSize(const std::wstring& name);
	void ResetPosition();
	void DrawEffect();
	void DrawElement();
	void DrawDistort();
	void DrawLinkedObject();
	void Tick();
	void DrawElement(LPImgInfo pImgInfo, float x, float y);
	void CheckDistortObjectLoad(const std::wstring& name);
	void CheckLinkedObjectLoad(const std::wstring& name);
	LPImgInfo CheckPictureLoad(const std::wstring& name);
	void FreeAllResources();
	void RenewSize();
	bool CheckRenderer();
	void DeleteDistort(std::wstring& name);
	void DeleteLinkedObject(std::wstring& name);
	void DeleteEffect(std::wstring& name);
	void DeleteElement(std::wstring& name);
	void DeleteObjectFile(sObjInfo& objInfo);
	void ShowSizes();
	void ShowCounts();
	void ReleaseMaps();
	bool CalEffectRefCount(const std::wstring& name, std::map<std::wstring, std::map<std::wstring, int>>&refMap, std::map<std::wstring, std::set<std::wstring>>& notifyMap);
	void AddDistortObjects();
	void AddLinkedObjects();
	void AddEffects();
	void AddElements();
	void AddItem(const std::wstring& name, Nuclear::PMap::MAP_OBJ_TYPE type);
	void AddRefMap(const std::wstring &name, unsigned int id);

public:
	virtual Nuclear::IApp* GetApp() { return NULL; }
	virtual void Render() { }
	virtual int GetTick() const { return GetTickCount(); }
	virtual Nuclear::CRECT GetViewport() const { return Nuclear::CRECT(0, 0, m_PreviewRect.Width(), m_PreviewRect.Height()); }
	virtual Nuclear::ConfigManager* GetConfigManager() { return m_pConfigMgr; }
	virtual Nuclear::Renderer* GetRenderer(HWND hwnd = NULL) { return m_pRenderer; }
	virtual Nuclear::EffectManager* GetEffectManager() { return m_pEffectMgr; }
	virtual Nuclear::AniManager* GetAniManager() { return m_pAniMgr; }
	virtual Nuclear::SpriteManager* GetSpriteManager() { return m_pSpriteMgr; }
	virtual Nuclear::CFileIOManager* GetFileIOManager() { return theApp.GetFileIOManager(); }
	virtual Nuclear::SystemResourceManager* GetSystemResourceManager() { return m_pSystemResourceMgr; }
	virtual Nuclear::StepSoundType* GetStepSoundType() const { return NULL; }
	virtual ~CAllObjectResourceDlg();

	CAllObjectResourceDlg(CWnd* pParent = NULL);   
	void ResetMaps();

	void ResetLists();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

public:
	virtual BOOL OnInitDialog();
	DECLARE_MESSAGE_MAP()
	afx_msg void OnBnClickedButton2();
	afx_msg void OnHdnItemclickEleResList(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTimer(UINT_PTR nIDEvent);
	afx_msg void OnNMClickEleResList(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnPaint();
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnBnClickedButtonClean();
	afx_msg void OnBnClickedButton1();
	afx_msg void OnBnClickedRefresh();
	afx_msg void OnBnClickedButtonCountRef();
};
