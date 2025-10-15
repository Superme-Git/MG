#pragma once

#include "..\engine\common\xptypes.h"
#include "..\engine\common\groupmanager.h"
#include "..\engine\common\log.h"
#include "..\engine\engine\enginebase.h"
#include "..\engine\engine\titlemanager.h "
#include "..\engine\sprite\sprite.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\sprite\pspritemaze.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\iapp.h"


class COldAniPack;
struct sPlayState 
{
	sPlayState() : m_iCurFrame(0), m_iCurDir(0), 
		m_fAuto(true), m_fCycle(true), m_fPlayOnce(false), m_iStartTick(0) { }
	int m_iStartTick;

	bool m_fPlayOnce;

	bool m_fCycle;
	bool m_fAuto;
	int m_iCurDir;

	int m_iCurFrame;

};

class CPackPropertyDlg;
class CSpriteEditorApplicationDelegate : public Nuclear::IApp
{

	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type) {}
	virtual void OnRenderInit(int now, int step, int totalstep) {}
	virtual void OnRenderUI(int now) {}
	virtual bool OnBeforeRender(int now) {return true;}
	virtual void OnTick(int now, int delta) {}
	virtual bool OnExit() { return true; }

	virtual bool OnInit(int step) { return true; }

};

class CSpriteEditorDoc : public CDocument, public Nuclear::EngineBase
{
	DECLARE_DYNCREATE(CSpriteEditorDoc)

public:
#ifndef _WIN32_WCE
	virtual void Serialize(CArchive& ar);   
#endif
#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif
	virtual ~CSpriteEditorDoc();
	CSpriteEditorDoc();

protected:
	virtual BOOL OnNewDocument();

	DECLARE_MESSAGE_MAP()
public:	
	afx_msg void OnUpdateExport(CCmdUI *pCmdUI);
	afx_msg void OnExport();
	afx_msg void OnUpdateChunkCopy(CCmdUI *pCmdUI);
	afx_msg void OnChunkCopy();
	afx_msg void OnFileSave();
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	afx_msg void OnUpdatePlayOnce(CCmdUI *pCmdUI);
	afx_msg void OnPlayOnce();
	afx_msg void OnUpdateDrawEdge(CCmdUI *pCmdUI);
	afx_msg void OnDrawEdge();
	afx_msg void OnUpdateStatChunk(CCmdUI *pCmdUI);
	afx_msg void OnStatChunk();
	afx_msg void OnUpdateBirdOption(CCmdUI *pCmdUI);
	afx_msg void OnBirdOpition(UINT nID);
	afx_msg void OnUpdate32826(CCmdUI *pCmdUI);
	afx_msg void OnAnimationRefManager();
	afx_msg void OnUpdateOptBorderOn(CCmdUI *pCmdUI);
	afx_msg void OnOptBorderOn();
	afx_msg void OnUpdateModelDelete(CCmdUI *pCmdUI);
	afx_msg void OnModelDelete();
	afx_msg void OnUpdateModelInfo(CCmdUI *pCmdUI);
	afx_msg void OnModelInfo();
	afx_msg void OnModelNew();
	afx_msg void OnUpdateSpriteDown(CCmdUI *pCmdUI);
	afx_msg void OnSpriteDown();
	afx_msg void OnUpdateSpriteUp(CCmdUI *pCmdUI);
	afx_msg void OnSpriteUp();
	afx_msg void OnUpdateSpriteRotation(CCmdUI *pCmdUI);
	afx_msg void OnSpriteRotation();
	afx_msg void OnUpdateSpriteDeleteAll(CCmdUI *pCmdUI);
	afx_msg void OnSpriteDeleteAll();
	afx_msg void OnUpdateSpriteDel(CCmdUI *pCmdUI);
	afx_msg void OnSpriteDel();
	afx_msg void OnUpdateSpriteAdd(CCmdUI *pCmdUI);
	afx_msg void OnSpriteAdd();
	afx_msg void OnAnimationGroupManager();
	afx_msg void OnToolRenameEquipment();
	afx_msg void OnToolRSrcList();
	afx_msg void OnTest();
	afx_msg void OnUpdateOptPixList(CCmdUI *pCmdUI);
	afx_msg void OnOptPixList();
	afx_msg void OnUpdateOptUnwrap(CCmdUI *pCmdUI);
	afx_msg void OnOptUnwrap();
	afx_msg void OnToolPack();
	afx_msg void OnToolRename();
	afx_msg void OnUpdateOptBaseOn(CCmdUI *pCmdUI);
	afx_msg void OnOptBaseOn();
	afx_msg void OnUpdateAniInfo(CCmdUI *pCmdUI);
	afx_msg void OnAniInfo();
	afx_msg void OnOpenAnimation();
	afx_msg void OnAnimationOpen();
	afx_msg void OnUpdateOptCrossOn(CCmdUI *pCmdUI);
	afx_msg void OnOptCrossOn();
	afx_msg void OnPack();
	afx_msg void OnOptBgPicSelect();
	afx_msg void OnUpdateOptBgPicSt(CCmdUI *pCmdUI);
	afx_msg void OnOptBgPicSt();
	afx_msg void OnUpdateOptBgPicOn(CCmdUI *pCmdUI);
	afx_msg void OnOptBgPicOn();
	afx_msg void OnOptBgColor();
	afx_msg void OnUpdatePlayCycle(CCmdUI *pCmdUI);
	afx_msg void OnPlayCycle();
	afx_msg void OnUpdatePlayAuto(CCmdUI *pCmdUI);
	afx_msg void OnPlayAuto();
	afx_msg void OnUpdatePlayRr(CCmdUI *pCmdUI);
	afx_msg void OnPlayRr();
	afx_msg void OnUpdatePlayLr(CCmdUI *pCmdUI);
	afx_msg void OnPlayLr();
	afx_msg void OnUpdatePlayLast(CCmdUI *pCmdUI);
	afx_msg void OnPlayLast();
	afx_msg void OnUpdatePlayFirst(CCmdUI *pCmdUI);
	afx_msg void OnPlayFirst();
	afx_msg void OnUpdatePlayPrev(CCmdUI *pCmdUI);
	afx_msg void OnUpdatePlayNext(CCmdUI *pCmdUI);
	afx_msg void OnPlayPrev();
	afx_msg void OnPlayNext();
	virtual void OnCloseDocument();
	afx_msg void OnUpdateAnimationInfo(CCmdUI *pCmdUI);
	afx_msg void OnAnimationInfo();
	afx_msg void OnAnimationNew();
	afx_msg void OnEquipmentNew();

	void BatchModify(CString dir);

	bool LoadAnimationRes(Nuclear::XAni *pAni);

	void UpdateControlView();

	void PlayReset();
	void PlayDir(int i);

	void PlayRR();
	void PlayLR();
	void PlayFrame(int i);

	void PlayLast();
	void PlayFirst();
	void PlayPrev();
	void PlayNext();
	bool IsNewAnimation(){return m_AddNewAnimation;};

	bool m_AddNewAnimation;
	std::wstring m_AniFilename;


	typedef std::vector<Nuclear::CRECT> vRectSet;
	void static ModifySpriteMazeRect(Nuclear::PSpriteMaze *pMaze, const RECT &rect);


	void AddEffectPosition(const CSize &pt)
	{
		Nuclear::CPOINT rpt = GetEffectPosition();
		rpt.x += pt.cx;
		rpt.y += pt.cy;
		m_pSpriteMgr->SetEffectPos(m_CurModelName, rpt);
	}

	const Nuclear::CPOINT &GetEffectPosition() { return m_pSpriteMgr->GetEffectPos(m_CurModelName); }
	void AddHorseHeight(int addY) 
	{
		if (m_CurHorse == L"") 
			return;
		m_pSpriteMgr->SetHorseHeight(m_CurModelName, m_CurHorse, GetHorseHeight() + addY); 
	}
	int GetHorseHeight() const 
	{ 
		if (m_CurHorse == L"") 
			return 0;
		return m_pSpriteMgr->GetHorseHeight(m_CurModelName, m_CurHorse); 
	}
	void AddTitleLocation(int addY) { m_pSpriteMgr->SetTitlePos(m_CurModelName, GetTitlePosition() + addY); }
	int GetTitlePosition() const { return m_pSpriteMgr->GetTitlePos(m_CurModelName); }
	void SetAdMode(int mode) { m_adMode = mode; }
	int GetAdMode() const {return m_adMode; }
	bool PackByDir(std::wstring path,std::wstring partpath, std::wstring dstpath, bool recur, int nframe, Nuclear::XPANI_DIRECTIONMODE dirmode, Nuclear::CPOINT packcenter, int &time, int timeMode,
		int sysLevel, Nuclear::XPTEXTURE_BLENDMODE blend, Nuclear::XPIMAGE_FILEFORMAT filefmt, Nuclear::XPTEXTURE_FORMAT texfmt, Nuclear::XPEFFECT_BIND_TYPE bt);

	void AdjustBase(int xoff0, int yoff0, int xoff2, int yoff2);

	void AdjustBorder(int ltxoff, int ltyoff, int rbxoff, int rbyoff);
	void AdjustBaseCenter(int xoff, int yoff);
	void AdjustOffset(int xoff, int yoff);
	int GetDyeEnable();

	void ChangeDye(int enable,int fenkuai);
	void OpenAnimationGroup(const std::wstring base);

	void OpenAnimationFile(const CString &url);

	void OpenAnimationFile();
	void ShowEquipment(int ectype, bool f);
	void ReloadEquipment(int ectype);
	void SelectEquipment(int ectype, std::wstring name);
	void OpenHSLDlg();
	void ChangeEquipmentColor(int ectype);
	void SelectEquipment(int ectype);
	void DeleteEquipment(int ectype);
	void NewEquipment(int ectype);
	virtual Nuclear::IApp* GetApp()  { return &m_AppDelegate; } 

	virtual int GetTick() const;
	virtual Nuclear::CRECT GetViewport() const { return Nuclear::CRECT(); }
	virtual Nuclear::TitleManager* GetTitleManager() { return m_pTitleMgr; }
	virtual Nuclear::Renderer* GetRenderer(HWND hwnd = NULL) { return m_pRenderer; }
	virtual Nuclear::EffectManager* GetEffectManager() { return NULL; }
	virtual Nuclear::AniManager* GetAniManager() { return m_pAniMgr; }
	virtual Nuclear::ConfigManager* GetConfigManager() { return m_pConfigMgr; }
	virtual Nuclear::SpriteManager* GetSpriteManager() { return m_pSpriteMgr; }
	virtual Nuclear::CFileIOManager* GetFileIOManager() { return m_pFileIOMgr; }
	virtual Nuclear::StepSoundType* GetStepSoundType() const { return NULL; }
	virtual astar::PathFinder * GetPathFinder() { return &m_PathFinder; }
	bool Save(bool prompt, bool saveact, int saveequip);

	void ChangeModel(const std::wstring &newmodelname);
	void RenameAnimation(const std::wstring &name);

	void DeleteAnimation(const std::wstring &name);
	void OpenAction(std::wstring name);
	void PlayStaticSound();
	Nuclear::Renderer *m_pRenderer;

	CPackPropertyDlg *m_pPackProDlg;

	int m_nPicDis;

	int m_nQuality;
	int m_SysLevel;
	BOOL m_bBindType;
	std::wstring m_PackDstPath;
	std::wstring m_PartPath;
	int m_PackTime;
	Nuclear::CPOINT m_PackAdjOffset;
	Nuclear::CPOINT m_PackCenter;
	Nuclear::XPANI_DIRECTIONMODE m_DirMode;
	Nuclear::XPTEXTURE_BLENDMODE m_Blend;
	Nuclear::XPTEXTURE_FORMAT m_TexFormat;
	Nuclear::XPIMAGE_FILEFORMAT m_FileFormat;
	char m_nBrushSize;


	char m_ChunkState;
	BOOL m_bDrawEdge;
	int m_nChunkSettingState;
	int m_MaskDir;
	int m_Time;
	int m_nFrame;
	int m_adMode; 


	int m_State; 
	Nuclear::PictureHandle	 m_BgPicHandle;

	bool m_bPixList;
	bool m_bUnwrap;
	bool m_bBgPicSt;
	bool m_bBgPicOn;
	bool m_bBaseOn;
	bool m_bBorderOn;
	bool m_bCrossOn;
	byte m_BirdType;	
	DWORD m_BgColor;

	sPlayState m_PlayState;
	void ResetLayer();

	void SpriteRunTo(int x, int y); 
	void SpriteMove(float xoff, float yoff); 
	void SpriteMoveTo(float x, float y); 
	void SelectSprite(float x, float y); 
	void SwitchSprite();
	void DownSprite();
	void UpSprite();
	void RotateSprite(); 
	void DeleteAllSprites(); 
	void DeleteSprites(); 
	void AddSprite(); 
	Nuclear::FPOINT m_CurSpriteOffset;
	int m_CurSprite;
	std::vector<Nuclear::XHardRef<Nuclear::Sprite> > m_Sprites;
	astar::PathFinder m_PathFinder;


	std::vector<Nuclear::XAni> m_CurAniGroup; 
	std::wstring m_CurAniBase; 
	bool m_bHugeTexture;

	bool m_bAniDirty;
	DWORD m_AniColor;
	bool m_bModifyOutLine; 

	Nuclear::XAni m_CurAni;


	std::wstring m_CurAniName;
	Nuclear::SpriteManager::eDyeType m_CurDyeType;
	std::wstring m_PicPathPrefix;

	struct sEquipLayer
	{
		Nuclear::Component m_Equip;
		bool m_bShowEc;
		bool m_bDirty;
		DWORD m_Color;
		sEquipLayer(Nuclear::EngineBase *pEngine) : m_Equip(pEngine), m_bShowEc(true), m_bDirty(false), m_Color(0xffffffff) { }
		sEquipLayer() : m_Equip(NULL)
		{
			XPASSERT(false);
		}
	};


	std::vector<sEquipLayer> m_CurEquips;

	Nuclear::XPDyeShaderParam* m_pDyeShaderParam;

	int m_nEnableDye;
	std::wstring m_CurActionName;


	bool m_bMazeSizeDirty;
	bool m_bMazeDirty;
	bool m_bDirty;
	Nuclear::Action m_CurAction;
	int m_nScale;

	int m_nFreq;
	int m_nVol;
	std::wstring m_CurSoundName;	
	std::wstring m_CurHorse;
	std::wstring m_CurModelName;
	float m_fSpriteSpeed;
	Nuclear::PSpriteMaze *m_pSpriteMaze;



	Nuclear::XHardRef<Nuclear::Title> m_TitleArr[3]; 
	Nuclear::TitleManager *m_pTitleMgr;
	Nuclear::ConfigManager *m_pConfigMgr;
	Nuclear::SpriteManager *m_pSpriteMgr;
	Nuclear::AniManager *m_pAniMgr;
	Nuclear::CFileIOManager *m_pFileIOMgr;
	Nuclear::DirectSoundManager* m_pSoundManager;
	CSpriteEditorApplicationDelegate m_AppDelegate;


private:
	std::wstring m_CurModelPath;
	float m_fBigSpritesSize; 

	void CreatePacks(const std::vector<CString>& imgList,const wchar_t* sExpName, int nSeq);

	void GetFilterFiles(std::vector<CString>& returnList,const CString& sPath,const CString& sFilter);
	CString GetPath(wchar_t* szTitle);
	CString GetFile(wchar_t* szTitle);
	DWORD64 GetDirSize(CString strFullPath);
	bool ChangePackFormat(std::wstring baseuri, Nuclear::PAniPack &pack, Nuclear::XPTEXTURE_FORMAT fmt);

	int CreatePack(bool (*callback)(const wchar_t*), std::wstring path, std::wstring partpath, std::wstring dstpath, std::wstring px, 
		Nuclear::XPANI_DIRECTIONMODE dirmode, int nseq, Nuclear::CPOINT center, int ptime, int sysLevel, 
		Nuclear::XPTEXTURE_BLENDMODE blend, Nuclear::XPIMAGE_FILEFORMAT filefmt,Nuclear::XPTEXTURE_FORMAT texfmt, Nuclear::XPEFFECT_BIND_TYPE bt
		,const wchar_t* wsSpecifyImageFlieName=NULL);

	struct sPicPosInfo 
	{
		int m_nFileID; 
		Nuclear::CSIZE m_Size; 
		Nuclear::CRECT m_Rect; 
	};

	int WriteNewAniFile(const std::map<int, sPicPosInfo>& picPosInfos,
		const std::wstring& aniPath, const COldAniPack& pack);

	struct sAniPic
	{
		sAniPic& operator=(const sAniPic &r)
		{
			m_Dir = r.m_Dir;				
			m_nFrame = r.m_nFrame;			
			m_nRegion = r.m_nRegion;		
			m_Rect = r.m_Rect;				
			m_nID = r.m_nID;				
			m_Handle = r.m_Handle;			
			m_PartHandle = r.m_PartHandle;			

			return *this;
		}
		void OffsetRect(int x, int y)
		{
			m_Rect.left += x;
			m_Rect.top += y;
			m_Rect.right += x;
			m_Rect.bottom += y;
		}
		int Width() const
		{
			return m_Rect.Width();
		}
		int Height() const
		{
			return m_Rect.Height();
		}
		sAniPic()
		{
			m_Dir = 0;
			m_nFrame = 0;
			m_nRegion = 0;
			m_Rect = Nuclear::CRECT(0, 0, 0, 0);
			m_nID = 0;
			m_Handle = Nuclear::INVALID_PICTURE_HANDLE;
			m_PartHandle = Nuclear::INVALID_PICTURE_HANDLE;
		}
		Nuclear::PictureHandle m_Handle;
		Nuclear::PictureHandle m_PartHandle;
		int m_nID;	 
		Nuclear::CRECT m_Rect;
		int m_nRegion;		
		int m_nFrame;			
		int m_Dir;		
	};


	int PicArrange(const std::vector<std::vector<sAniPic>>& rctPackingResult, const std::vector<SIZE>& PicSize,
		const std::wstring& aniPath, Nuclear::XPIMAGE_FILEFORMAT ffmt, Nuclear::XPTEXTURE_FORMAT fmt, std::map<int, sPicPosInfo>& picPosInfos);

	int GetBigPicSize(const std::vector<std::vector<sAniPic>>& rctPackingResult, std::vector<SIZE>& PicSize);

	int RectPacking(std::vector<sAniPic>& vectorPicRect, std::vector<std::vector<sAniPic>>& rctPackingResult);

	int GetAniPicRects(const COldAniPack& pack, std::vector<sAniPic>& vectorPicRect);

	std::vector<std::vector<std::vector<std::vector<Nuclear::CPOINT> > > > m_OutLinePoints; 

	float m_fPreDataSize;
	float m_fCurDataSize;
	int RecalculateAlphaPicBorder(COldAniPack& pack, float& fSavingCoef);
	bool OldAniToNewAni(const std::wstring& aniPath, const std::wstring& newAniPath);



	int Version10AniToNewAni(const COldAniPack& pack, const std::wstring& newAniPath);

	int EnumSpritePaths(CString fullPath, std::vector<CString>& strSpritePaths);

	int EnumAniFiles(CString path, std::vector<CString>& filenames);
	bool ModifyAniOutLine(std::wstring url, Nuclear::PAniPack& pack);

	afx_msg void OnOldAniToNewAni();

public:
	afx_msg void OnCollectAniInfo();
	afx_msg void OnOldAniFileListToNewAni();
};
