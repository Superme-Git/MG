#include "../engine/engine/enginebase.h"
#include "../engine/iapp.h"
// SpriteSoundEditorDoc.h : CSpriteSoundEditorDoc 类的接口
//

#pragma once

class ToolsSoundSprite;

namespace Nuclear
{
	struct SpriteSoundParam;
	typedef std::vector<SpriteSoundParam> SpriteSoundParamVet;
	typedef std::map<std::wstring, SpriteSoundParamVet> ActionSoundParamMap;//key是动作名
	typedef ActionSoundParamMap* PActionSoundParamMap;
	struct ModelSoundParam;
}

enum SPRITE_PLAY_MODE
{
	SPRITE_STOP = 0,
	SPRITE_PLAY_ONCE,
	SPRITE_PLAY_CYCLE
};

enum ADD_SOUND_ERR
{
	ASE_OK,
	ASE_NO_MODEL,
	ASE_NO_ACTION,
};

class CSpriteSoundEditorDoc : public CDocument, public Nuclear::EngineBase, Nuclear::IApp
{
protected: // 仅从序列化创建
	CSpriteSoundEditorDoc();
	DECLARE_DYNCREATE(CSpriteSoundEditorDoc)

// 属性
private:
	bool m_bIsInited;
	bool m_bUseBkgImage;
	Nuclear::DirectSoundManager *m_pSoundManager;
	Nuclear::CFileIOManager *m_pFileIOMan;
	Nuclear::SpriteManager *m_pSprMan;
	Nuclear::AniManager *m_pAniMan;
	Nuclear::EffectManager *m_pEffectMan;
	Nuclear::Renderer *m_pRenderer;
	Nuclear::ConfigManager *m_pConfigMan;
	Nuclear::SystemResourceManager *m_pSystemResourceMan;
	Nuclear::CRECT m_clientRect;
	Nuclear::XDisplayMode m_dmode;
	Nuclear::ActionSoundParamMap *m_pActionMap;
	DWORD m_tick;
	ToolsSoundSprite *m_pSprite;
	std::wstring m_action;
	std::wstring m_curKeyComponent;
	std::wstring m_modelname;
	int m_curSound;
	SPRITE_PLAY_MODE m_playMode;
	Nuclear::XPCOLOR m_bkgColor;
	Nuclear::PictureHandle m_bkgHandle;
	Nuclear::FRECT m_bkgRect;
	Nuclear::XPDIRECTION m_dir;
	CString m_stringPath;

private:
	void PlayAction();

// 操作
public:
	bool CheckEngine(HWND hWnd);
	void SetClientRect(const RECT& rect);
	void MainLoop();
	void SetModel(const std::wstring &modelname);
	void SetAction(const std::wstring &actionname);
	void SetDirection(Nuclear::XPDIRECTION dir);
	void SetComponent(int i, const std::wstring &component);
	void SetCurSound(int id) { m_curSound = id; }
	int GetCurSound() const { return m_curSound; }
	const std::wstring &GetCurAction() const { return m_action; }
	const std::wstring &GetCurKeyComponent() const { return m_curKeyComponent; }
	const Nuclear::ModelSoundParam &GetCurmodelParam() const;
	const Nuclear::PActionSoundParamMap GetCurParamMap() const { return m_pActionMap; }
	CString GetCfgPath() { return m_stringPath + L"\\EffectEditorCfg.ini"; }
	CString GetPath() { return m_stringPath; }

//可以考虑Redoundo。。。
	ADD_SOUND_ERR AddASound(const std::wstring &soundname);
	void DelASound();
	void SetSoundVol(unsigned char v);
	void SetSoundFreq(unsigned char f);
	void SetKeyLayer(int i);
////////////////////
	unsigned char GetCurSoundVolume();
	unsigned char GetCurSoundFreq();
	void Save();
	

// 重写
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar) {}
	//EngineBase
	virtual Nuclear::SystemResourceManager* GetSystemResourceManager() { return m_pSystemResourceMan; }
	virtual Nuclear::DirectSoundManager* GetSoundManager() { return m_pSoundManager; }
	virtual Nuclear::CFileIOManager* GetFileIOManager() { return m_pFileIOMan; }
	virtual Nuclear::SpriteManager* GetSpriteManager() { return m_pSprMan; }
	virtual Nuclear::AniManager* GetAniManager() { return m_pAniMan; }
	virtual Nuclear::EffectManager* GetEffectManager() { return m_pEffectMan; }
	virtual Nuclear::Renderer* GetRenderer(HWND hwnd = NULL) { return m_pRenderer; }
	virtual Nuclear::StepSoundType* GetStepSoundType() const { return NULL; }
	virtual Nuclear::ConfigManager* GetConfigManager() { return m_pConfigMan; }
	virtual Nuclear::CRECT GetViewport() const { return m_clientRect; }
	virtual int GetTick() const { return m_tick; }
	virtual Nuclear::IApp* GetApp() { return this; }

	//IApp
	virtual bool OnInit(int step) { return true; }
	virtual bool OnExit() { return true; }

	virtual void OnTick(int now, int delta) {}
	virtual bool OnBeforeRender(int now/*ms*/) { return true; }
	virtual void OnRenderUI(int now) {}
	virtual void OnRenderInit(int now, int step, int totalstep) {}

	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear:: XPUSA_TYPE type);


// 实现
public:
	virtual ~CSpriteSoundEditorDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// 生成的消息映射函数
protected:
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL SaveModified();
	afx_msg void OnPlayOnce();
	afx_msg void OnUpdatePlayOnce(CCmdUI *pCmdUI);
	afx_msg void OnPlayCycle();
	afx_msg void OnUpdatePlayCycle(CCmdUI *pCmdUI);
	afx_msg void OnStop();
	afx_msg void OnUpdateStop(CCmdUI *pCmdUI);	
	afx_msg void OnFileSave();
//	afx_msg void On32775();
};


