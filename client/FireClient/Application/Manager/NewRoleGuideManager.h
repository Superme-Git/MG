#ifndef __FireClient__NewRoleGuideManager__
#define __FireClient__NewRoleGuideManager__

#include "Singleton.hpp"
#include "XPRenderEffect.h"
#include "GameUIManager.h"

class LockEffect:public XPRenderEffect
{
public:
	LockEffect();
	~LockEffect();
    
	void performPostRenderFunctions();
};


class NewRoleGuideManager : public CSingleton<NewRoleGuideManager>
{
public:
	NewRoleGuideManager();
	~NewRoleGuideManager();
    
	void GuideLevel(int level);
	void ShowLockEffect();
	bool CheckTargetWindow();
	int getCurGuideId();
	bool NeedLockScreen();
	void RemoveGuidePaticleEffect(Nuclear::IEffect* pEffect);
	void UpdateGuidePaticleEffect(Nuclear::IEffect* pEffect, CEGUI::Window* pWnd);
	void FailGuide();
	void FinishGuideInBattle();
	bool isPlayingAni();																			// 判断是否正在播放引导动画
	bool isBattleGuideFinsh(int battleId, int roundId);												// 判断战斗中引导是否结束
	void GuideStartBattle(int battleId, int roundId);												// 根据战斗id与波次id发起引导
	Nuclear::NuclearFRectt GetClickRect(int id);													//得到引导的目标窗口可点击区域
	void FinishGuide();																				//锁屏引导完成
	bool isGuideFinish(int id);																		//某个新手引导是否已经完成

	int getGuideModel();

	bool isClickInvalidWhenLockScreen(float x, float y);	// 锁屏引导是点击无效
};

inline NewRoleGuideManager* gGetNewRoleGuideManager()
{
	return NewRoleGuideManager::GetInstance();
}

#endif /* defined(__FireClient__NewRoleGuideManager__) */
