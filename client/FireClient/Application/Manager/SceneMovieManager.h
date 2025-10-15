/***********************************************************************
filename: 	CSceneMovieManager.h
author:		eagle
purpose:	剧情场景管理器
*************************************************************************/
#ifndef __SCENEMOVIE_MANAGER_H
#define __SCENEMOVIE_MANAGER_H


#include "Singleton.hpp"
#include "elements/CEGUIAll.h"
#include "MovieSceneNpc.h"
#include "MovieSection.h"
#include "regionbuffer.h"


class SceneMovieManager:public CSingleton<SceneMovieManager>
{
public:
	SceneMovieManager();
	~SceneMovieManager();

	void DeleAllSection();
	void DeleSection(MovieSection* pSection);
	void ExitMovieScene();
	void EnterMovieScene(int movieID);
	bool LoadMovie(int movieID);
	bool isOnSceneMovie();
	bool CanDrawLoadingBar() const;
	void SetCanDrawLoadingBar(bool bCan);
	int GetCurMoiveMapID();

	void DeleteAllSound();

	bool OnBreakMoive();

	void RequestBreakMovie(bool imm = false);

	bool BeginPlayMovie(int movieID);
	void EndPlayMovie();

	void OnStartSectionStart();

	void ParserSection(const std::wstring& strSectionContent);
	void EndCurSection();

	void Updata(int elapseTime);
 
	bool OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam );
	void StartCurFirstSctionInList();

	void ReturnToScene();
	void ReturnToScene(int mapId, const Nuclear::NuclearLocation& pos);
	void HandleReturn(const std::wstring& strParserText);
	void SetPercent(float fPercent);
	void HandleEnd(const std::wstring& strParserText);
	void HandleStart(const std::wstring& strParserText);

	void HandleShakeCamara(const std::wstring& strParserText);
	void HandleRotateCamara(const std::wstring& strParserText);
	void HandleZoomIn(const std::wstring& strParserText);
	void HandleMoveCamara(const std::wstring& strParserText);
	void HandleZoomOut(const std::wstring& strParserText);
	void HandleSetCamara(const std::wstring& strParserText);
	void HandleSetMap(const std::wstring& strParserText);

	void HandleSetDir(const std::wstring& strParserText);
	void HandleNpcPlayAction(const std::wstring& strParserText);
	void HandleNpcSayPop(const std::wstring& strParserText);
	void HandleNpcSpeak(const std::wstring& strParserText);
	void HandleSpeakStart(const std::wstring& strParserText);
	void HandleMoveNpc(const std::wstring& strParserText);
	void HandleCreateNpc(const std::wstring& strParserText);
	void HandleDeleteNpc(const std::wstring& strParserText);
	void HandleSetNpcMount(const std::wstring& strParserText);
	void HandleSetNpcColor(const std::wstring& strParserText);
	void HandleClearScene(const std::wstring& strParserText);

	void HandleWait(const std::wstring& strParserText);
	void HandlePlayCG(const std::wstring& strParserText);
	void HandleStopSound(const std::wstring& strParserText);
	void HandlePlaySDEffect(const std::wstring& strParserText);
	void HandlePlaySound(const std::wstring& strParserText);
	void HandleSetSceneEffct(const std::wstring& strParserText);

	void HandleForbiddenAutoPlay(const std::wstring& strParserText);

	void SetCamara(int x,int y);
	void SetMap(int mapID, int x, int y);

	void ShakeCamara(int time, float angle);
	void RotateCamara(float angle);
	void ZoomIn(float rate, int zoomTime, int lastTime, int endTime);
	void ZoomOut(float rate, int zoomTime, int lastTime, int endTime);

	void CreateNpc(int id,const std::wstring& identityName,int pos_X,int pos_Y,int modelID, const std::wstring& strNpcName, int title, int dir = 0, int colorTemplate = 1, eActionType actype = eActionStand, 
		float scale = 1.0f, const std::wstring& effectName = L"", int repeat = 1, int ex = 0, int ey = 0, const std::wstring& chengwei = L"", const std::wstring& cwColor = L""); 

	void SetNpcDir(const std::wstring& idenName, int dir);
	void NpcSayPop(const std::wstring& identityName, const std::wstring strContent);
	void NpcSpeak(int headID, const std::wstring& strName, const std::wstring strContent);
	void DeleteNpc(const std::wstring& identityName);
	void NpcPlayAction(const std::wstring& identityName, int actionID, int endType, int dir);
	void MoveNpc(const std::wstring& identityName, int dest_X, int dest_Y, int actionID, int time);
	void SetNpcComponent(const std::wstring& idenName, eSpriteComponentType type, int baseid);
	
	void PlaySoundEffect(const std::wstring& strAudioName);
	void PlayCG(const std::wstring& strCGFileName);
	void StopSound(int id,int fadeTime);
	void PlaySound(const std::wstring& strAudioName, int id, int repeat, int fadeTime);
	void SetSceneEffct(const std::wstring& name, int x, int y, int effectID, int lasttime, int circle, int type);

	void Wait(int time);

	void SetIsMovieEnding(bool b);
	bool isOnMovieEnding();
	void RemoveAllMovieEffect();

	bool IsMaskArea(Nuclear::NuclearLocation logicloc);	//判断是否是遮罩区域
    void ExitMovieCloseWindow();
	void ShowBreakMovieBtn(bool bShow);

	void setSpeakSectionState(bool b);
	void DeleCurSpeakSectionStartNext(bool next);

	bool IsReturningScene() const { return m_bReturningScene; }

private:

	void InitMovieMap();
	void InitHandler();
	void InitEffectPath();
	void GetValueStr(const std::wstring& str, int idx, std::wstring& val);

private:

	const static int s_iMovieStartFadeTime = 500;

	typedef std::map<int, std::wstring> MovieFileMap;
	typedef MovieFileMap::iterator MovieMapIt;
	typedef std::vector<Nuclear::IEffect*> MovieEffectList;
	typedef MovieEffectList::iterator MEffectIt;

	typedef std::map<int, Nuclear::SoundHandle> SoundHandleMap;
	typedef std::map<int, Nuclear::SoundHandle>::iterator SoundHandleMapIt;

	typedef std::vector<MovieSection*> MovieSectionList;
	typedef MovieSectionList::iterator MovieSectionIt;

	typedef void (SceneMovieManager::*MovieHandler) (const std::wstring& strParserText);
	typedef std::map<std::wstring, MovieHandler> MovieHandlerMap;
	typedef MovieHandlerMap::iterator MovieHandlerMapIt;

	typedef std::map<std::wstring, std::wstring> EffectPathMap;
	typedef EffectPathMap::iterator EffectPathMapIt;

private:
	MovieFileMap m_mapMovieFiles;
	MovieHandlerMap m_mapMovieHandlers;
	static const std::wstring s_strMovieDir;
	MovieSectionList m_vecMovieSections;
	int m_iCurSetctionNeedTime;
	int m_iCurSectionLastTime;
	bool m_bMovieStarted;
	bool m_bSectionEnd;
	bool m_bIsMovieScene;
	bool m_bReturningScene;
	bool m_bCanDrawLoadingBar;
	int m_iCurMovieID;
	MovieSection* m_pCurSpeakSection;
	MovieSectionList m_SpeakSections;
	void AddSpeakSection(MovieSection* pSection);
	void StartCurFirstSpeakSection();
	void DeleSpeakSection(MovieSection* pSection);
	bool m_bSpeakState;

	bool m_bIsMovieSceneEnding;

	int m_curMovieMapID;
    RegionBuffer<unsigned short> m_curMovieRegionBuffer;	//地图区域信息
	void EraseSpaceOfString(std::wstring& strText);
	CEGUI::Window* m_pOldGuiSheet;
	CEGUI::Window* m_pMovieAniSheet;
	CEGUI::Window* m_pTopPanel;
	CEGUI::Window* m_pBottomPanel;
	CEGUI::Window* m_pBottomBreakText;

	std::wstring GetStringByVarName(const std::wstring& varName, const std::wstring& strParseText);
	float GetFloatByVarName(const std::wstring& varName, const std::wstring& strParseText);
	int GetIntByVarName(const std::wstring& varName, const std::wstring& strParseText);
	void GetPosByVarName(int& x,int& y,const std::wstring& varName, const std::wstring& strParseTex);
	MovieSceneNpc* GetMovieNpcByVarName(const std::wstring& varName);
	void AddSection(MovieSection* pSection);
	MovieSection* m_pCurSection;

	std::wstring GetEffectPathByName(const std::wstring& strName);

	Nuclear::SoundHandle GetSoundHandleByID(int id);
    
    bool HandleBreakMovieBtnClick(const CEGUI::EventArgs& e);

	SoundHandleMap m_mapSoundHandle;
	std::map<std::wstring, int64_t > m_mapMovieNpc;
	EffectPathMap m_mapEffectPath;
	MovieEffectList m_vecEffects;

	Nuclear::NuclearCameraUpdateType mOldCamUpdateType;
};

inline SceneMovieManager* gGetSceneMovieManager()
{
	return SceneMovieManager::GetInstance();
}

#endif
