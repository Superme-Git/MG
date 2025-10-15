#include "stdafx.h"
#include "..\engine\renderer\renderer.h"
#include "..\engine\sprite\spritemanager.h"
#include "Sprite.h"


void Sprite::Render()
{	
	if (!m_pAction)
		return;
	if( !m_pAction->TestDir(m_Direction) ) return;

	int d = (m_pEngine->GetTick() - m_nBeginTick) % Nuclear::XP_EFFECT_CYCLE;
	d = d > Nuclear::XP_EFFECT_HALF_CYCLE?Nuclear::XP_EFFECT_CYCLE - d : d;
	m_fEffectAlpha = m_fMinEffectAlpha + m_fSlopeEffectAlpha * d;
	int linkframe = m_pAction->GetLineFrame(m_nCurFrame);

	int ne = m_pEngine->GetSpriteManager()->GetLayerCount(m_ModelName);
	std::vector<int> ecorder(ne);

	m_pAction->GetFrameOrder(m_nCurFrame, m_Direction, &ecorder[0], ne);

	std::wstring curActionName = m_pAction->GetLinkName(m_nCurFrame);
	if( curActionName == L"" )
		curActionName = m_CurActionName;

	Nuclear::DrawPictureParam param;
	Nuclear::XPCOLOR color;

	bool hasRenderEffect = false;
	bool firstEffect = false;
	int effectFrameI = -1;
	for(int i=0; i< (int)m_CurComponents.size(); ++i)
	{
		int ec = ecorder[i];
		if( ec < 0 || ec >= ne ) continue;
		if( NULL == m_CurComponents[ec] ) continue;
		if (((1 << ec) & m_FlagEffectLayer) != 0)
			effectFrameI = i;
	}
	switch(m_Direction)
	{
	case 0:
	case 5:
	case 6:
	case 7:
		firstEffect = true;				
		break;
	default:
		break;
	}
	Nuclear::Renderer *pRenderer = m_pEngine->GetRenderer();

	for(int i=0; (i < ne || effectFrameI > -1); ++i)
	{
		int ec = i<ne?ecorder[i]:0;
		if (effectFrameI > -1)
		{
			if (firstEffect)
			{
				ec = ecorder[effectFrameI];
				effectFrameI = -1;
				i = -1;
			} else {
				if (i == ne)
				{
					ec = ecorder[effectFrameI];
					effectFrameI = -1;
				}
			}
		}
		if( ec < 0 || ec >= ne ) continue;
		if( NULL == m_CurComponents[ec] ) continue;

		Nuclear::XHardRef<Nuclear::XAni> ani = m_CurComponents[ec]->GetAni(m_ModelName, curActionName, false);
		if( ani.IsValid() )
		{
			if (ani->GetState() == Nuclear::XAni::XPANISTATE_NOPICTURE )
			{
				m_pEngine->GetAniManager()->SyncLoadAni(ani);
			}
			Nuclear::XAni::XPic pic;
			Nuclear::XPBase base;
			if( ani->GetBase(base) )
			{
				bool bIsEffectlayer = ((1 << ec) & m_FlagEffectLayer) != 0;
				if (bIsEffectlayer && i != -1 && i != ne)
					continue;
				for(int k=0; k<ani->GetRegionCount();++k)
				{
					if( ani->GetPic(m_Direction, linkframe, k, pic) && pic.pBigPic != NULL && pic.pBigPic->handle != Nuclear::INVALID_PICTURE_HANDLE)
					{
						Nuclear::FRECT rect(pic.offset.ToFRECT());
						if (bIsEffectlayer)
						{
							rect *= Nuclear::XP_EFFECT_SCALE;
						}
						rect += m_Position;
						if ((ani->GetDirMode() == Nuclear::XPANIDM_8FROM5 && m_Direction > Nuclear::XPDIR_BOTTOM) ||
							(ani->GetDirMode() == Nuclear::XPANIDM_B2FROM1 && m_Direction == Nuclear::XPDIR_BOTTOMLEFT) ||
							(ani->GetDirMode() == Nuclear::XPANIDM_4FROM2 && m_Direction == Nuclear::XPDIR_BOTTOMLEFT) ||
							(ani->GetDirMode() == Nuclear::XPANIDM_4FROM2 && m_Direction == Nuclear::XPDIR_TOPRIGHT)
							)
						{
							rect.left = 2*(m_Position.x) - rect.left;
							rect.right = 2*(m_Position.x) - rect.right;
						}
						param.handle = pic.pBigPic->handle;
						param.pSrcrect = &pic.srcrect;
						param.pRect = &rect;
						color = ((k==ani->GetRegionCount()-1)?m_Colors[ec]:0xffffffff);
						color.a = bIsEffectlayer?static_cast<unsigned char>(255*m_fEffectAlpha):255;
						param.blend = bIsEffectlayer?Nuclear::XPTEXBM_ADDITIVE_ALPHA:Nuclear::XPTEXBM_DEFAULT;
						param.pColor = &color;
						param.colorCount = 1;
						m_pEngine->GetRenderer()->DrawPicture(param);
					}
				}
			}
		}

	}
}

void Sprite::SetFrame(int frame)
{
	assert(frame >= 0);
	if (!m_pAction)
	{
		m_nCurTime = 0;
		m_nCurFrame = -1;
		m_fCurFrame = static_cast<float>(m_nCurFrame);
		return;
	}
	m_nCurFrame = (frame < GetFrameCount())?frame:0;
	m_nCurTime = static_cast<DWORD>(m_pAction->GetFrameStartTime(m_nCurFrame) * m_pAction->GetTime());
	m_fCurFrame = static_cast<float>(m_nCurFrame);
}

void Sprite::Tick(DWORD deltaTime)
{
	if (!m_pAction)
	{
		m_nCurTime = 0;
		m_nCurFrame = -1;
		return;
	}
	m_nCurTime += deltaTime;
	m_nCurTime %= m_pAction->GetTime();
	float fCurTime = static_cast<float>(m_nCurTime) / m_pAction->GetTime();
	int i;
	int n = m_pAction->GetFrameCount();
	for(i=n-1; i>=0;--i)
	{
		if( fCurTime > m_pAction->GetFrameStartTime(i) )
		{
			break;
		}
	}
	m_nCurFrame = i > 0? i : 0;
	float fEndTime = (m_nCurFrame==n-1)?1.0f:m_pAction->GetFrameStartTime(m_nCurFrame+1);
	float fStartTime = m_pAction->GetFrameStartTime(m_nCurFrame);
	m_fCurFrame = (fCurTime - fStartTime) / (fEndTime - fStartTime);
	m_fCurFrame += m_nCurFrame;
}

void Sprite::SetAction(const std::wstring& action_name)
{
	if( action_name == L"" )
	{
		m_CurActionName = L"";
		m_pAction = NULL;
		return;
	}
	std::wstring actionref_name = m_pEngine->GetSpriteManager()->GetActionRef(m_ModelName, action_name);
	if( actionref_name == L"" ) 
	{
		m_CurActionName = L"";
		return;
	}
	const Nuclear::Action *act = m_pEngine->GetSpriteManager()->GetAction(m_ModelName, actionref_name);
	if( act == NULL ) 
	{
		m_CurActionName = L"";
		return;
	}
	if (m_CurActionName == actionref_name)
		return;
	m_pAction = act;
	m_CurActionName = actionref_name;

	m_nCurFrame = 0;
	m_nCurTime = 0;
	m_fCurFrame = 0.0f;
}

std::wstring Sprite::GetComponent(int sc)
{
	if( sc < 0 || sc >= (int)m_CurComponents.size() ) return L"";
	return m_CurComponents[sc]==NULL?L"":m_CurComponents[sc]->GetName();
}

void Sprite::SetComponent(int sc, const std::wstring& resource, Nuclear::XPCOLOR color)
{
	if( sc < 0 || sc >= (int)m_CurComponents.size() ) return;
	Nuclear::Component *equip = m_pEngine->GetSpriteManager()->GetComponent(m_ModelName, sc, resource);
	m_CurComponents[sc] = equip;
	m_Colors[sc] = color;
	m_Colors[sc].a = 255;
}

bool Sprite::SetModel(const std::wstring &modelname)
{
	if( modelname == m_ModelName ) 
		return false;

	m_ModelName = modelname;

	Nuclear::SpriteManager *pSprMan = m_pEngine->GetSpriteManager();
	m_FlagEffectLayer = 0;
	m_CurComponents.clear();
	m_Colors.clear();
	if( modelname != L"" )
	{
		Nuclear::SpriteManager::XModel* pM = pSprMan->GetXModel(modelname);
		if (pM && pM->m_nType == 1)
			return false;

		m_CurComponents.resize(pSprMan->GetLayerCount(modelname), NULL);
		int layerCount = pSprMan->GetLayerCount(modelname);
		m_Colors.resize(layerCount, 0xffffffff);
		unsigned char flag = 1;
		for (int i=0;i<layerCount;++i)
		{
			if (pSprMan->GetLayerEffectEnable(modelname, i))
			{
				m_FlagEffectLayer |= flag;
			}
			flag <<= 1;
		}
	} else {
		m_pAction = NULL;
		m_nCurTime = 0;
		m_nCurFrame = 0;
		m_fCurFrame = 0.0f;
		m_CurActionName = L"";
	}
	return true;
}


Sprite::~Sprite(void)
{
}

Sprite::Sprite(Nuclear::EngineBase *pEB) : m_ModelName(L""), m_pAction(NULL), m_CurActionName(L""), m_pEngine(pEB), 
m_Direction(Nuclear::XPDIR_TOP), m_nCurTime(0), m_nCurFrame(0), m_fCurFrame(0.0f)
{
	m_FlagEffectLayer = 0;
	m_fMinEffectAlpha = 0.5f;
	m_fSlopeEffectAlpha = (1.0f - m_fMinEffectAlpha) / Nuclear::XP_EFFECT_HALF_CYCLE;
	m_fEffectAlpha = m_fMinEffectAlpha;
	m_nBeginTick = pEB->GetTick();
	m_CurComponents.clear();
	m_Colors.clear();
}

