#pragma once

#include "..\engine\effect\geffect.h"

class CToolsEffect :
	public Nuclear::GEffect
{
public:
	bool HasIndependentSound() const { return m_bIndependentSound; }
	virtual bool Update( DWORD tickTime);

	bool SetPlayingFrame( int frame);

	void SetBoundsRect( const Nuclear::CRECT& rc);


	bool ModifyLayer( Nuclear::AbstractEffectClip* clip, int layer);


	bool ModifyStartEndFrame( Nuclear::AbstractEffectClip* clip, int startFrame, int endFrame);


	bool DelClip( Nuclear::AbstractEffectClip* clip);


	bool AddClip( Nuclear::AbstractEffectClip* clip, int layer);

	bool AddClip( Nuclear::AbstractEffectClip* clip);
	Nuclear::AbstractEffectClip* AddClip( int layer, int startFrame, int endFrame);

	void GetClipList( int layer, Nuclear::AbstractEffectClip_LIST& clipList);


	void DelLayer( int layer);


	bool SetTotalFrame( int frames);


	int LoadEffectClips(XMLIO::CINode& root, int flag) { return GEffect::LoadEffectClips(root, flag); }

	int SaveEffect(XMLIO::CONode& root, XMLIO::CONode* soundcliproot, int flag);
	bool SaveEffect(LPCTSTR lpszPathName, int flag);
	virtual ~CToolsEffect(void);

	CToolsEffect(Nuclear::EngineBase* pEB);

private:
	Nuclear::EffectClip* RemoveClip( Nuclear::AbstractEffectClip* clip);


};
