#include "stdafx.h"
#include "ToolsEffect.h"
#include "..\engine\effect\effectclip.h"
#include "..\engine\common\log.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

bool CToolsEffect::SetPlayingFrame( int frame)
{
	Nuclear::XPEffectState oldState = m_state;
	Stop();
	if ( !Play())
		return false;
	int frameTime = static_cast<int>(1000.0f / m_fFps);
	for ( int i = 0; i <= frame; ++i)
	{
		Update(frameTime);
	}

	if ( oldState != Nuclear::XPES_PLAYING)
		Pause();

	return true;
}

void CToolsEffect::SetBoundsRect( const Nuclear::CRECT& rc)
{
	m_relBBox = rc;
}


bool CToolsEffect::ModifyLayer( Nuclear::AbstractEffectClip* clip, int layer)
{
	if ( layer < 0 || layer >= MAX_CLIP_LAYER)
		return false;


	Nuclear::EffectClip* effectClip = RemoveClip( clip);
	if ( effectClip == NULL)
		return false;

	effectClip->m_layer = layer;
	Nuclear::CLIPS_LIST& clipList = m_clipLayers[layer];
	clipList.push_back( effectClip);

	return true;
}


bool CToolsEffect::ModifyStartEndFrame( Nuclear::AbstractEffectClip* clip, int startFrame, int endFrame)
{
	if ( startFrame < 0 || (endFrame < startFrame && endFrame != -1))
		return false;

	if ( endFrame >= m_totoalFrames)
		return false;

	clip->m_startFrame = startFrame;
	clip->m_endFrame = endFrame;

	return true;
}


bool CToolsEffect::DelClip( Nuclear::AbstractEffectClip* clip)
{
	return RemoveClip( clip) != NULL;
}


Nuclear::EffectClip* CToolsEffect::RemoveClip( Nuclear::AbstractEffectClip* clip)
{
	int layer = clip->GetLayer();
	assert( layer >= 0 && layer < MAX_CLIP_LAYER);
	if ( layer < 0 || layer >= MAX_CLIP_LAYER)
		return NULL;

	Nuclear::CLIPS_LIST& clipList = m_clipLayers.at( layer);
	Nuclear::CLIPS_LIST::iterator it = std::find( clipList.begin(), clipList.end(), clip);
	if ( it == clipList.end())
		return NULL;

	Nuclear::EffectClip* effectClip = *it;
	clipList.erase( it);

	return effectClip;
}


bool CToolsEffect::AddClip( Nuclear::AbstractEffectClip* clip, int layer)
{
	Nuclear::EffectClip* effectClip = dynamic_cast<Nuclear::EffectClip*>( clip);
	if ( !effectClip)
		return false;

	if ( layer < 0 || layer >= MAX_CLIP_LAYER)
		return false;

	int startFrame = effectClip->GetStartFrame();
	int endFrame = effectClip->GetEndFrame();
	if ( startFrame < 0 || (endFrame < startFrame && endFrame != -1))
		return false;
	if ( endFrame >= m_totoalFrames)
		return false;
	effectClip->m_layer = layer;

	Nuclear::CLIPS_LIST& clipList = m_clipLayers[layer];
	clipList.push_back( effectClip);

	return true;	
}

bool CToolsEffect::AddClip( Nuclear::AbstractEffectClip* clip)
{
	Nuclear::EffectClip* effectClip = dynamic_cast<Nuclear::EffectClip*>( clip);
	if ( !effectClip)
		return false;

	int layer = effectClip->GetLayer();
	if ( layer < 0 || layer >= MAX_CLIP_LAYER)
		return false;

	int startFrame = effectClip->GetStartFrame();
	int endFrame = effectClip->GetEndFrame();
	if ( startFrame < 0 || (endFrame < startFrame && endFrame != -1))
		return false;
	if ( endFrame >= m_totoalFrames)
		return false;

	Nuclear::CLIPS_LIST& clipList = m_clipLayers[layer];
	clipList.push_back( effectClip);

	return true;	
}

Nuclear::AbstractEffectClip* CToolsEffect::AddClip( int layer, int startFrame, int endFrame)
{
	if ( layer < 0 || layer >= MAX_CLIP_LAYER)
		return NULL;

	if ( startFrame < 0 || (endFrame < startFrame && endFrame != -1))
		return NULL;

	if ( endFrame >= m_totoalFrames)
		return NULL;

	Nuclear::EffectClip* clip = new Nuclear::EffectClip();
	if ( clip == NULL)
		return NULL;

	clip->m_layer = layer;
	clip->m_startFrame = startFrame;
	clip->m_endFrame = endFrame;

	Nuclear::CLIPS_LIST& clipList = m_clipLayers[layer];
	clipList.push_back( clip);

	return clip;
}

inline bool operator< ( const Nuclear::AbstractEffectClip& obj1, const Nuclear::AbstractEffectClip& obj2)
{
	return obj1.GetStartFrame() < obj2.GetStartFrame();
}

inline bool EffectClipCompare( const Nuclear::AbstractEffectClip* obj1, const Nuclear::AbstractEffectClip* obj2)
{
	return ((*obj1) < (*obj2));
}

void CToolsEffect::GetClipList( int layer, Nuclear::AbstractEffectClip_LIST& clipList)
{
	if ( layer < 0 || layer >= (int)m_clipLayers.size())
		return;

	Nuclear::CLIPS_LIST& innerList = m_clipLayers[layer];
	clipList.resize( innerList.size());
	std::copy( innerList.begin(), innerList.end(), clipList.begin());
	std::sort( clipList.begin(), clipList.end(), EffectClipCompare);
}

void CToolsEffect::DelLayer( int layer)
{
	if ( layer < 0 || layer >= (int)m_clipLayers.size())
		return;
	Nuclear::CLIPS_LIST& clipList = m_clipLayers[layer];
	for ( Nuclear::CLIPS_LIST::iterator i = clipList.begin(), e = clipList.end(); i != e; ++i)
	{
		Nuclear::EffectClip* clip = *i;
		clip->Release();
		delete clip;
	}
	clipList.clear();
}

bool CToolsEffect::SetTotalFrame( int frames)
{
	if ( frames < 0)
		return false;

	for ( Nuclear::LAYER_LIST::iterator layerIter = m_clipLayers.begin(), layerEnd = m_clipLayers.end(); layerIter != layerEnd; ++layerIter)
	{
		Nuclear::CLIPS_LIST& clipsList = *layerIter;

		for ( Nuclear::CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
		{
			Nuclear::EffectClip* clip = *i;

			if ( clip->GetEndFrame() >= frames)
				return false;
		}
	}
	m_totoalFrames = frames;
	if (GetPlayingFrame() >= frames)
	{
		m_nCurTime = GetPlayOnceTime();
	}	
	return true;
}


bool CToolsEffect::Update(DWORD tickTime)
{
	if (m_state == Nuclear::XPES_PLAYING) 
		return GEffect::Update(tickTime);
	if (m_nCurTime < 0)
	{
		m_nCurTime = 0;
		tickTime = 0;
	}
	float curFrame = GetPlayingFrame();
	Nuclear::FPOINT pos = m_pos;
	pos += m_translate;
	Nuclear::XPVECTOR4 color = m_vertexColor.ToXPVECTOR4();
	Nuclear::EffectClip* pClip;
	for ( Nuclear::LAYER_LIST::iterator lyIter = m_clipLayers.begin(), lyEnd = m_clipLayers.end(); lyIter != lyEnd; ++lyIter)
	{
		Nuclear::CLIPS_LIST& clipsList = *lyIter;
		for ( Nuclear::CLIPS_LIST::iterator i = clipsList.begin(), e = clipsList.end(); i != e; ++i)
		{
			pClip = *i;
			if (pClip->GetAnimation())
			{
				if (pClip->GetAnimation()->GetType() != Nuclear::XPET_AUDIO)
				{
					pClip->Update( curFrame, 0, pos, m_scale, m_rotationRadian, color, m_dir);
				}
			}
			pClip->ToolSetValid( curFrame );
		}
	}
	return true;
}

int CToolsEffect::SaveEffect(XMLIO::CONode& root, XMLIO::CONode* soundcliproot, int flag)
{
	root.SetAttribute( L"t_f", m_totoalFrames);
	root.SetAttribute( L"d_l", m_frontBackDivide);
	root.SetAttribute( L"b_l", m_relBBox.left);
	root.SetAttribute( L"b_t", m_relBBox.top);
	root.SetAttribute( L"b_r", m_relBBox.right);
	root.SetAttribute( L"b_b", m_relBBox.bottom);
	root.SetAttribute( L"fps", GetFPS());
	root.SetAttribute( L"bT", static_cast<int>(m_bindType));
	m_bIndependentSound = false;

	for ( Nuclear::LAYER_LIST::iterator i = m_clipLayers.begin(), e = m_clipLayers.end(); i != e; ++i)
	{
		Nuclear::CLIPS_LIST& clipsList = *i;
		for ( Nuclear::CLIPS_LIST::iterator j = clipsList.begin(), k = clipsList.end(); j != k; ++j)
		{
			Nuclear::EffectClip* clip = *j;
			XMLIO::CONode clipNode;
			Effect* pAni = clip->GetAnimation();
			if ((flag & Nuclear::EFFECT_IO_EDIT) != 0 && soundcliproot && pAni && pAni->GetType() == Nuclear::XPET_AUDIO)
			{
				m_bIndependentSound = true;
				soundcliproot->CreateSubElement( L"clip", clipNode);
			} else {
				root.CreateSubElement( L"clip", clipNode);
			}
			if ( clip->Save( clipNode, flag))
			{
				XPLOG_ERROR( L"保存特效文件中的clips错误\n");
			}
		}
	}
	if (!m_bIndependentSound)
		root.SetAttribute( L"hasA", m_bIndependentSound?1:0);
	return 0;
}


bool CToolsEffect::SaveEffect(LPCTSTR lpszPathName, int flag)
{
	XMLIO::CFileWriter fw, audioFw;
	XMLIO::CONode node, audioNode;
	fw.GetRootNode(node);
	audioFw.GetRootNode(audioNode);
	if (SaveEffect(node, &audioNode, flag) == 0)
	{
		int retcode = fw.SaveNative(lpszPathName);
		if (m_bIndependentSound)
		{
			std::wstring audioUri = lpszPathName;
			audioUri = audioUri.substr(0, audioUri.size() - 8) + L"audio.inf";
			audioFw.SaveNative(audioUri.c_str());
		}
		return retcode == XMLIO::EC_SUCCESS;
	}
	return false;
}

CToolsEffect::~CToolsEffect(void)
{
}

CToolsEffect::CToolsEffect(Nuclear::EngineBase* pEB) : GEffect(pEB)
{
	m_fFps = 8.0f;
}

