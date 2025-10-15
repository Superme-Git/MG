

#include <set>
#include <map>
#include "..\engine\effect\geffect.h"
#include "ToolsEffect.h"
#include "Action.h"
#include "..\engine\renderer\renderer.h"
#include "sprite.h"
#include "ToolsSebind.h"

#pragma once
class CEffectEditorDoc : public CDocument
{
protected: 
	CEffectEditorDoc();
	DECLARE_DYNCREATE(CEffectEditorDoc)

private:
	friend class CActionList;


private:
	CToolsEffect* m_pToolsEffect;	

	CToolsSebind m_ToolsSebind;
	bool m_bDrawRole;
	typedef std::set<INotify*> NotifySet;
	NotifySet m_NotifySet;
	Sprite *m_pSprite;
	bool m_bDrawClipCenter;
	CActionList* m_pActionList;
	EffectClipSet m_pSelectedEffectClips;
	Nuclear::SpriteManager* m_pSpriteMgr;
	eEffectFileType m_EffectFileType;

public:
	bool m_bNeedReplay;


public:
	eEffectFileType GetEffectFileType() const { return m_EffectFileType; }
	Nuclear::SpriteManager* GetSpriteManager() { return m_pSpriteMgr; }
	void ResetEffect(Nuclear::EngineBase *pEngine);
	void ReleaseRes();

private:
	void InsertOneKeyPointArray(Nuclear::AbstractEffectClip::KP_VECTOR& kps, const float& v, int frame, int firstFrame, int lastFrame);
	void CheckOneKeyPointArray(Nuclear::AbstractEffectClip::KP_VECTOR& kps, const float& v, float maxX);
	void InsertTwoKeyPointArray(Nuclear::AbstractEffectClip::KP_VECTOR& kps, const Nuclear::FPOINT& v, int frame, int firstFrame, int lastFrame);
	void CheckTwoKeyPointArray(Nuclear::AbstractEffectClip::KP_VECTOR& kps, const Nuclear::FPOINT& v, float maxX);
	void SetSelectedEffectClipsKeys(Nuclear::AbstractEffectClip::KeyType kt, const Nuclear::AbstractEffectClip::KP_VECTOR& vKP,
		const Nuclear::AbstractEffectClip::SP_VECTOR& vSP);

public:
	void Tick(DWORD dt);
	void Draw(Nuclear::Renderer* pRenderer);
	inline void SetSpritePosition(const Nuclear::Location &pos) 
	{ 
		assert(m_pSprite != NULL);
		assert(m_pToolsEffect != NULL);
		m_pToolsEffect->SetLocation(pos); 
		m_pSprite->SetPosition(pos);
	}
	void SetSpriteDirection(Nuclear::XPDIRECTION dir);
	Nuclear::AbstractEffectClip* AddEffectClip(int layer, int startFrame, int endFrame);
	bool DelEffectClip(Nuclear::AbstractEffectClip* clip);
	void SetBoundsRect(const Nuclear::CRECT &rt);
	void SetBindType(Nuclear::XPEFFECT_BIND_TYPE t);
	void SetFPS(float fps);
	bool SetTotalFrameNum(int frames);
	bool SetDivideLayer(int layer);
	bool ModifyLayer(Nuclear::AbstractEffectClip* clip, int layer);
	bool ModifyStartEndFrame(Nuclear::AbstractEffectClip* clip, int startFrame, int endFrame, CView* pSender);
	Nuclear::Effect* GetBindEffect() { return m_ToolsSebind.GetEffect(); }

	const Nuclear::Effect* GetBindEffect() const { return m_ToolsSebind.GetEffect(); }
	void SetEffectForBind(Nuclear::Effect *pEffect)
	{
		XPASSERT(m_EffectFileType == EFT_SEBIND);
		m_ToolsSebind.SetEffect(pEffect);
		m_bNeedReplay = true;
	}
	void ResetEffectForBind();
	inline int GetSpriteTotalTime() { return m_pSprite->GetTime(); }
	inline int GetFrameCount() { return m_pSprite->GetFrameCount(); }
	inline std::wstring GetActionName() const { return m_pSprite->GetActionName(); }
	inline std::wstring GetModelName() const { return m_pSprite->GetModelName(); }
	inline void SetActionByName(const std::wstring& name) 
	{ 
		m_pSprite->SetAction(name); 
		ResetEffectForBind();
		UpdateAllViews(NULL);
	}
	inline std::wstring GetComponent(int sc) { return m_pSprite->GetComponent(sc); }
	inline void SetComponent(int sc, const std::wstring& resource, Nuclear::XPCOLOR color=0xffffffff) { m_pSprite->SetComponent(sc, resource, color); }
	inline void SetModelByName(const std::wstring &modelname) 
	{ 
		if (m_pSprite->SetModel(modelname))
			UpdateAllViews(NULL); 
	}
	const Nuclear::CRECT &GetRelativeBouningBox() const;

	bool SetPlayingFrame(int frame);
	Nuclear::XPEffectState GetPlayState() const;
	float GetPlayingFrame() const;
	Nuclear::XPEFFECT_BIND_TYPE GetBindType() const;
	float GetFPS() const;
	void GetClipListByLayer (int layer, Nuclear::AbstractEffectClip_LIST& clipList);
	int GetDivideLayer() const;
	int GetTotalFrames() const;
	bool Pause();

	bool Play();
	bool Stop();
	bool GetClipStartEndFrame(const Nuclear::AbstractEffectClip* pClip, eAnimationPlayType& playType, int& nStartFrame, 
		int& nEndFrame) const;
	bool SetFrameDivideLayer(int layer);


	bool SetSelectedAudioSoundProperty(int nID, unsigned char value);

	bool SetScaleForSelectedClip(const Nuclear::FPOINT& scale);
	bool SetRotationForSelectedClip(float angle);
	bool ModifyAdjPosForSelectedClip(CPoint adjPoint);
	bool SetSelectedClipColors(const ColorKeyMap& keyMap);
	bool SetSelectedClipSingleKeys(Nuclear::AbstractEffectClip::KeyType kt, const Nuclear::AbstractEffectClip::KP_VECTOR& vKP,
		const Nuclear::AbstractEffectClip::SP_VECTOR& vSP, const SingleKeyMap& keyMap);
	bool SetSelectedClipDoubleKeys(Nuclear::AbstractEffectClip::KeyType kt, const Nuclear::AbstractEffectClip::KP_VECTOR& vKP,
		const Nuclear::AbstractEffectClip::SP_VECTOR& vSP, const DoubleKeyMap& keyMap);
	bool SetSelectedClipSpec(bool spec, CView* pSender);
	bool SaveSelectedClipPlayModeAndTime(Nuclear::XPPLAY_MODE type, int nTime, CView* pSender);
	bool SetSelectedClipVertexColor(const Nuclear::XPCOLOR& color, CView* pSender);
	bool SetSelectedClipFPS(float fps, CView* pSender);
	void MoveSelectedClips(int addX, int addY);
	void inline SetSelectClipsControl(const Nuclear::IEffectClipControl* control)
	{
		EffectClipSet::iterator iter = m_pSelectedEffectClips.begin();
		for (;iter!=m_pSelectedEffectClips.end();++iter)
		{
			(*iter)->SetControl(control);
		}
	}
	bool inline SelectClip(Nuclear::AbstractEffectClip* pClip) 
	{
		if (m_EffectFileType == EFT_SEBIND)
			return false;
		if (m_pSelectedEffectClips.find(pClip) == m_pSelectedEffectClips.end())
		{
			m_pSelectedEffectClips.insert(pClip);
			return true;
		}
		return false;
	}
	void inline UnSelectedAllClip() 
	{ 
		if (m_EffectFileType == EFT_SEBIND)
			return;
		m_pSelectedEffectClips.clear(); 
	}
	size_t inline UnSelectClip(Nuclear::AbstractEffectClip* pClip) 
	{ 
		if (m_EffectFileType == EFT_SEBIND)
			return 0;
		return m_pSelectedEffectClips.erase(pClip); 
	}
	const EffectClipSet& GetSelectedClips() { return m_pSelectedEffectClips; }
	Nuclear::XPDIRECTION GetDirection() { return m_pToolsEffect->GetDirection(); }

	bool Init() { m_pToolsEffect->Stop(); }
	void SetDrawRole(bool isDraw) { m_bDrawRole = isDraw; }
	bool IsDrawRole() { return m_bDrawRole; }

public:
	virtual BOOL DoSave(LPCTSTR lpszPathName, BOOL bReplace = TRUE);
	virtual void Serialize(CArchive& ar);
	virtual BOOL OnNewDocument();

public:
	bool AddNotify (INotify *pNotify)
	{
		if (m_NotifySet.find(pNotify) == m_NotifySet.end())
		{
			m_NotifySet.insert(pNotify);
			return true;
		}
		return false;
	}
	virtual ~CEffectEditorDoc();

#ifdef _DEBUG
	virtual void Dump(CDumpContext& dc) const;
	virtual void AssertValid() const;
#endif

protected:


protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnFileOpen();
	afx_msg void OnFileNew();
	afx_msg void OnRefresh();
	virtual BOOL OnSaveDocument(LPCTSTR lpszPathName);
	virtual BOOL OnOpenDocument(LPCTSTR lpszPathName);
	afx_msg void OnFileExport();
	afx_msg void OnUpdateEditRedo(CCmdUI *pCmdUI);
	afx_msg void OnEditRedo();
	afx_msg void OnUpdateEditUndo(CCmdUI *pCmdUI);
	afx_msg void OnEditUndo();
	afx_msg void OnUpdateClipCenter(CCmdUI *pCmdUI);
	afx_msg void OnClipCenter();

};


