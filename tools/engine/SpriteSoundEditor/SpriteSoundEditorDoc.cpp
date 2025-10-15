// SpriteSoundEditorDoc.cpp : CSpriteSoundEditorDoc 类的实现
//

#include "stdafx.h"
#include "..\engine\sound\directsoundmanager.h"
#include "..\engine\common\fileiomanager.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\engine\animanager.h"
#include "..\engine\effect\effectmanager.h"
#include "..\engine\engine\configmanager.h"
#include "..\engine\engine\systemresourcemanager.h"
#include "SpriteSoundEditor.h"
#include "SpriteSoundEditorDoc.h"
#include "ToolsSoundSprite.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

// CSpriteSoundEditorDoc

IMPLEMENT_DYNCREATE(CSpriteSoundEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CSpriteSoundEditorDoc, CDocument)
	ON_COMMAND(ID_PLAY_ONCE, &CSpriteSoundEditorDoc::OnPlayOnce)
	ON_UPDATE_COMMAND_UI(ID_PLAY_ONCE, &CSpriteSoundEditorDoc::OnUpdatePlayOnce)
	ON_COMMAND(ID_PLAY_CYCLE, &CSpriteSoundEditorDoc::OnPlayCycle)
	ON_UPDATE_COMMAND_UI(ID_PLAY_CYCLE, &CSpriteSoundEditorDoc::OnUpdatePlayCycle)
	ON_COMMAND(ID_STOP, &CSpriteSoundEditorDoc::OnStop)
	ON_UPDATE_COMMAND_UI(ID_STOP, &CSpriteSoundEditorDoc::OnUpdateStop)
	ON_COMMAND(ID_FILE_SAVE, &CSpriteSoundEditorDoc::OnFileSave)
END_MESSAGE_MAP()


// CSpriteSoundEditorDoc 构造/析构

CSpriteSoundEditorDoc::CSpriteSoundEditorDoc() : m_pSoundManager(NULL), m_pFileIOMan(NULL), 
m_pSprMan(NULL), m_pAniMan(NULL), m_pEffectMan(NULL), m_pRenderer(NULL), m_pConfigMan(NULL),
m_pSystemResourceMan(NULL), m_tick(0), m_pSprite(NULL), m_playMode(SPRITE_STOP),
m_pActionMap(NULL)
{
	XPLOG_EDITOR_CHECK();
	m_bIsInited = false;
	m_bUseBkgImage = false;
	m_clientRect.left = 0;
	m_clientRect.top = 0;
	m_clientRect.right = 0;
	m_clientRect.bottom = 0;
	m_bkgColor.data = 0;
	m_bkgHandle = Nuclear::INVALID_PICTURE_HANDLE;
	m_dir = Nuclear::XPDIR_TOP;
}

CSpriteSoundEditorDoc::~CSpriteSoundEditorDoc()
{
	delete m_pSprite;
	delete m_pSprMan;
	delete m_pSoundManager;
	delete m_pEffectMan;
	delete m_pAniMan;
	delete m_pConfigMan;
	delete m_pSystemResourceMan;
	DestroyRenderer(m_pRenderer); 
	m_pFileIOMan->Destroy();
	delete m_pFileIOMan;
}

BOOL CSpriteSoundEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: 在此添加重新初始化代码
	// (SDI 文档将重用该文档)

	return TRUE;
}

void CSpriteSoundEditorDoc::SetClientRect(const RECT& rect)
{
	m_clientRect = rect;
	if (m_pRenderer)
	{
		m_pRenderer->SetSourceRect(&m_clientRect);
	}
	if (m_pSprite)
	{
		m_pSprite->SetLocation(m_clientRect.Center());
	}
}

bool CSpriteSoundEditorDoc::CheckEngine(HWND hWnd)
{
	if (m_bIsInited)
		return true;
	m_pFileIOMan = new Nuclear::CFileIOManager();
	if( !m_pFileIOMan->Init(true, true))
	{
		XPLOG_EDITOR(L"PFS路径初始化失败\n");
		return false;
	}
	srand(GetTickCount());
	std::wstring root;
	if (!PFS::CEnv::PFSFileNameToNativeFileName(L"/root/a", root))
	{
		XPLOG_EDITOR(L"取跟路径失败\n");
		return false;
	}
	m_stringPath = root.c_str();
	m_stringPath=m_stringPath.Left(m_stringPath.GetLength() - 2);
	m_dmode.bitperpix = 32;
	m_dmode.height = GetSystemMetrics(SM_CYSCREEN);
	m_dmode.width = GetSystemMetrics(SM_CXSCREEN);
	if( !Nuclear::CreateRenderer(&m_pRenderer, hWnd, m_dmode, m_pFileIOMan, 0) )
	{
		XPLOG_EDITOR(L"Renderer初始化失败\n");
		return false;
	}
	m_pRenderer->SetAutoTestDevice(true);
	m_pRenderer->SetSourceRect(&m_clientRect);

	m_pSystemResourceMan = new Nuclear::SystemResourceManager(this);
	m_pSystemResourceMan->Init();
	m_pConfigMan = new Nuclear::ConfigManager(this);
	m_pConfigMan->Init();

	m_pConfigMan->SetConsoleInfo(Nuclear::XPCSINFO_ALL, false);

	m_pSystemResourceMan->SetSoundInited(true);
	m_pSoundManager = new Nuclear::DirectSoundManager(this);
	if( !m_pSoundManager->Init(hWnd, DSSCL_PRIORITY) ||
		! m_pSoundManager->SetPrimaryBufferFormat( 2, 22050, 16 ) )
	{
		m_pSystemResourceMan->SetSoundInited(false);
		AfxMessageBox(L"声音管理器初始化失败\n");
		return false;
	}
	m_pAniMan = new Nuclear::AniManager(this);
	if( !m_pAniMan->Init() ) 
	{
		AfxMessageBox(L"动画管理器初始化失败");
		return false;
	}
	m_pSprMan = new Nuclear::SpriteManager(this);
	if (!m_pSprMan->PreInit())
	{
		AfxMessageBox(L"精灵管理器初始化整体失败");
		return false;
	}
	size_t SprCount = m_pSprMan->GetInitCount();
	bool succ = true;
	for (size_t i=0;i<SprCount;++i)
	{
		if( m_pSprMan->Init(i, 1) )
		{	
			XPLOG_EDITOR(L"精灵管理器初始化部分失败，第 %d 号精灵有问题！", i);
			succ = false;
		}
	}
	if (!succ)
	{
		AfxMessageBox(L"精灵管理器初始化部分失败，请查看日志并且联系美术～");
	}
	if (!m_pSprMan->PostInit())
	{
		AfxMessageBox(L"精灵管理器初始化失败");
		return false;
	}

	m_pEffectMan = new Nuclear::EffectManager(this);
	m_bIsInited = true;
	return true;
}

void CSpriteSoundEditorDoc::OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type)
{
	switch (type)
	{
	case Nuclear::XPUSAT_DEFAULTACTIONFINISH:
		break;
	case Nuclear::XPUSAT_PLAYACTIONFINISH:
		XPASSERT(m_pSprite);
		switch (m_playMode)
		{
		case SPRITE_STOP:
			break;
		case SPRITE_PLAY_ONCE:
			m_playMode = SPRITE_STOP;
			m_pSprite->PlayActionSilence(m_action);
			m_pSprite->Pause();
			break;
		case SPRITE_PLAY_CYCLE:
			PlayAction();
			break;
		}
		break;
	}
}

void CSpriteSoundEditorDoc::SetModel(const std::wstring &modelname)
{
	if (m_pSprite)
	{
		m_pSprite->SetModel(modelname);
	} else {
		m_pSprite = new ToolsSoundSprite(this, modelname);
		m_pSprite->SetLocation(m_clientRect.Center());
		switch (m_playMode)
		{
		case SPRITE_STOP:
			m_pSprite->Pause();
			break;
		case SPRITE_PLAY_ONCE:
		case SPRITE_PLAY_CYCLE:
			m_pSprite->Play();
			break;
		}
	}
	m_curSound = 0;
	m_modelname = modelname;
	m_curKeyComponent = L"";
	m_action = L"";
	m_pSprite->SetDirection(m_dir);
	Nuclear::ModelSoundParam &param = m_pSprMan->GetSoundParam(m_modelname);
	if (param.keyComponentID == -1)
	{
		m_pActionMap = param.soundmap[m_curKeyComponent];
	} else {
		m_pActionMap = NULL;
	}
	SetModifiedFlag(FALSE);
}

void CSpriteSoundEditorDoc::PlayAction()
{
	if (m_pSprite && m_playMode != SPRITE_STOP)
	{
		bool play = false;
		if (m_pActionMap)
		{
			Nuclear::SpriteSoundParamVet &vet = (*m_pActionMap)[m_action];
			if ((int)vet.size() > m_curSound)
			{
				m_pSprite->PlayAction(m_action, vet[m_curSound]);
				play = true;
			}
		}
		if (!play)
		{
			m_pSprite->PlayActionSilence(m_action);
		}
		
	}
}

void CSpriteSoundEditorDoc::SetAction(const std::wstring &actionname)
{
	m_action = actionname;
	if (m_action != L"")
	{
		m_pSprite->SetDefaultAction(m_action, Nuclear::XPSALT_SYNC, 1.0f, false);
		PlayAction();
	}
}

void CSpriteSoundEditorDoc::SetDirection(Nuclear::XPDIRECTION dir)
{
	m_dir = dir;
	if (m_pSprite)
	{
		m_pSprite->SetDirection(m_dir);
	}
}

void CSpriteSoundEditorDoc::SetComponent(int i, const std::wstring &component)
{
	if (m_pSprite)
	{
		m_pSprite->SetComponent(i, component);
		Nuclear::ModelSoundParam &param = m_pSprMan->GetSoundParam(m_modelname);
		if (param.keyComponentID == i)
		{
			m_curKeyComponent = component;
			m_pActionMap = param.soundmap[m_curKeyComponent];
		}
	}
}

const Nuclear::ModelSoundParam& CSpriteSoundEditorDoc::GetCurmodelParam() const
{
	return m_pSprMan->GetSoundParam(m_modelname);
}

ADD_SOUND_ERR CSpriteSoundEditorDoc::AddASound(const std::wstring &soundname)
{
	if (m_action == L"")
		return ASE_NO_ACTION;
	if (m_pActionMap == NULL)
	{
		if (m_modelname == L"")
			return ASE_NO_MODEL;
		Nuclear::ModelSoundParam &param = m_pSprMan->GetSoundParam(m_modelname);
		m_pActionMap = new Nuclear::ActionSoundParamMap();
		param.soundmap[m_curKeyComponent] = m_pActionMap;
	}
	Nuclear::SpriteSoundParam tmpParam;
	tmpParam.soundname = soundname;
	(*m_pActionMap)[m_action].push_back(tmpParam);
	SetModifiedFlag(TRUE);
	return ASE_OK;
}

void CSpriteSoundEditorDoc::DelASound()
{
	if (m_action == L"")
		return;
	if (m_pActionMap == NULL)
		return;
	Nuclear::SpriteSoundParamVet &vet = (*m_pActionMap)[m_action];
	vet.erase(vet.begin() + m_curSound);
	if (m_curSound >= ((int)vet.size()) && m_curSound > 0)
	{
		--m_curSound;
	}
	SetModifiedFlag(TRUE);
	return;
}

void CSpriteSoundEditorDoc::SetSoundVol(unsigned char v)
{
	if (m_pActionMap)
	{
		Nuclear::ModelSoundParam &param = m_pSprMan->GetSoundParam(m_modelname);
		if (!(m_curKeyComponent == L"" && param.keyComponentID > -1))
		{
			if (m_action != L"")
			{
				Nuclear::SpriteSoundParamVet &vet = (*m_pActionMap)[m_action];
				if ((int)vet.size() > m_curSound)
				{
					vet[m_curSound].vol = v;
					SetModifiedFlag(TRUE);
				}
			}
		}
	}
}

void CSpriteSoundEditorDoc::SetSoundFreq(unsigned char f)
{
	if (m_pActionMap)
	{
		Nuclear::ModelSoundParam &param = m_pSprMan->GetSoundParam(m_modelname);
		if (!(m_curKeyComponent == L"" && param.keyComponentID > -1))
		{
			if (m_action != L"")
			{
				Nuclear::SpriteSoundParamVet &vet = (*m_pActionMap)[m_action];
				if ((int)vet.size() > m_curSound)
				{
					vet[m_curSound].freq = f;
					SetModifiedFlag(TRUE);
				}
			}
		}
	}
}

void CSpriteSoundEditorDoc::SetKeyLayer(int i)
{
	Nuclear::ModelSoundParam &param = m_pSprMan->GetSoundParam(m_modelname);
	if (param.keyComponentID != i)
	{
		SetModifiedFlag(TRUE);
		m_pActionMap = NULL;
		param.ClearSoundMap();
		param.keyComponentID = i;
		m_curSound = 0;
		if (m_pSprite && i != -1)
		{
			if (!m_pSprite->GetComponent(i, m_curKeyComponent))
			{
				m_curKeyComponent = L"";
			}
		} else {
			m_curKeyComponent = L"";
		}
	}
}

////////////////////
unsigned char CSpriteSoundEditorDoc::GetCurSoundVolume()
{
	if (m_pActionMap)
	{
		Nuclear::ModelSoundParam &param = m_pSprMan->GetSoundParam(m_modelname);
		if (!(m_curKeyComponent == L"" && param.keyComponentID > -1))
		{
			if (m_action != L"")
			{
				Nuclear::SpriteSoundParamVet &vet = (*m_pActionMap)[m_action];
				if ((int)vet.size() > m_curSound)
				{
					return (unsigned char)vet[m_curSound].vol;
				}
			}
		}
	}
	return 255;
}

unsigned char CSpriteSoundEditorDoc::GetCurSoundFreq()
{
	if (m_pActionMap)
	{
		Nuclear::ModelSoundParam &param = m_pSprMan->GetSoundParam(m_modelname);
		if (!(m_curKeyComponent == L"" && param.keyComponentID > -1))
		{
			if (m_action != L"")
			{
				Nuclear::SpriteSoundParamVet &vet = (*m_pActionMap)[m_action];
				if ((int)vet.size() > m_curSound)
				{
					return (unsigned char)vet[m_curSound].freq;
				}
			}
		}
	}
	return 128;
}

void CSpriteSoundEditorDoc::Save()
{
	if (m_modelname != L"")
	{
		m_pSprMan->SaveSoundParam(m_modelname);
		SetModifiedFlag(FALSE);
	}
}

void CSpriteSoundEditorDoc::MainLoop()
{
	if (!m_bIsInited)
		return;
	DWORD tick = Nuclear::GetMilliSeconds();
	int delta = tick - m_tick;
	m_tick = tick;
	m_pSoundManager->OnTick();
	m_pEffectMan->OnTick();
	m_pRenderer->Begin(m_bkgColor);
	if (m_bUseBkgImage && m_bkgHandle != Nuclear::INVALID_PICTURE_HANDLE)
	{
		m_pRenderer->DrawPicture(m_bkgHandle, m_bkgRect, 0xFFFFFFFF);
	}
	if (m_pSprite && m_action.size() > 0)
	{
		m_pSprite->Update();
		m_pSprite->Render(NULL);
	}
	m_pRenderer->End();
	m_pAniMan->GarbageCollection(m_tick);
	m_pSoundManager->GarbageCollection(m_tick);

}


// CSpriteSoundEditorDoc 诊断

#ifdef _DEBUG
void CSpriteSoundEditorDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CSpriteSoundEditorDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG


// CSpriteSoundEditorDoc 命令

void CSpriteSoundEditorDoc::OnPlayOnce()
{
	m_playMode = SPRITE_PLAY_ONCE;
	if (m_pSprite)
	{
		PlayAction();
		m_pSprite->Play();
	}
}

void CSpriteSoundEditorDoc::OnUpdatePlayOnce(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio(m_playMode == SPRITE_PLAY_ONCE);
}

void CSpriteSoundEditorDoc::OnPlayCycle()
{
	m_playMode = SPRITE_PLAY_CYCLE;
	if (m_pSprite)
	{
		PlayAction();
		m_pSprite->Play();
	}
}

void CSpriteSoundEditorDoc::OnUpdatePlayCycle(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio(m_playMode == SPRITE_PLAY_CYCLE);
}

void CSpriteSoundEditorDoc::OnStop()
{
	m_playMode = SPRITE_STOP;
	if (m_pSprite)
	{
		m_pSprite->Pause();
	}
}

void CSpriteSoundEditorDoc::OnUpdateStop(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio(m_playMode == SPRITE_STOP);
}

BOOL CSpriteSoundEditorDoc::SaveModified()
{
	if (!IsModified())
		return TRUE; 

	switch (AfxMessageBox(L"这个模型的设置还没有保存，是否保存？", MB_YESNOCANCEL))
	{
	case IDYES:
		Save();
		break;
	case IDNO:
		break;
	case IDCANCEL:
		return FALSE;
		break;
	}
	return TRUE;
}

void CSpriteSoundEditorDoc::OnFileSave()
{
	Save();
}

//void CSpriteSoundEditorDoc::On32775()
//{
//	FILE *f = Nuclear::FOpen(L"D:\\works\\ddd.csv", L"r");
//	wchar_t tempstr[500];
//	std::wstring modelname, actionname, compname, soundurl;
//	std::set<std::wstring> comps;
//	int freq;
//	int volume;
//	if (f)
//	{
//		while (!feof(f))
//		{
//			fwscanf(f, L"%s\n", tempstr);
//			std::wstring tmpwstr = tempstr;
//			std::wstring::size_type act = 0, comp = 0, surl = 0, freq = 0;
//			act = tmpwstr.find_first_of(L',', 0);
//			comp = tmpwstr.find_first_of(L',', act + 1);
//			surl = tmpwstr.find_first_of(L',', comp + 1);
//			freq = tmpwstr.find_first_of(L',', surl + 1);
//			int length = (int)act;
//			if (length > 0)
//			{
//				Save();
//				modelname = tmpwstr.substr(0, act);
//				SetModel(modelname);
//				comps.clear();
//				if (modelname == L"male" || modelname == L"female")//主角
//				{
//					SetKeyLayer(3);
//				}
//			}
//			length = comp - act - 1;
//			if (length > 0)
//			{
//				actionname = tmpwstr.substr(act + 1, length);
//			}
//			length = surl - comp - 1;
//			if (length > 0)
//			{
//				compname = tmpwstr.substr(comp + 1, length);
//				comps.insert(compname);
//				SetComponent(3, compname);
//			} else {
//				compname = L"";
//			}
//			length = freq - surl - 1;
//			if (length > 0)
//			{
//				soundurl = tmpwstr.substr(surl + 1, length);
//			}
//			SetAction(actionname);
//			if (compname == L"" && ((modelname == L"male" || modelname == L"female")))
//			{
//				std::set<std::wstring>::iterator it = comps.begin(), ie = comps.end();
//				for (;it!=ie;++it)
//				{
//					SetComponent(3, *it);
//					AddASound(soundurl);
//				}
//			} else {
//				AddASound(soundurl);
//			}
//		}
//		Save();
//	}
//	fclose(f);
//}
