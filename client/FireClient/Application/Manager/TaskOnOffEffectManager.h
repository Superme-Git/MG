#ifndef __FireClient__TaskOnOffEffectManager__
#define __FireClient__TaskOnOffEffectManager__

#include "utils/Utils.h"
#include "../Framework/GameApplication.h"
#include "GameUIManager.h"


class TaskOnOffEffectNotify: public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};

class TaskOnOffEffectManager : public CSingleton<TaskOnOffEffectManager>
{
public:
    TaskOnOffEffectManager();
    ~TaskOnOffEffectManager();

	void effectEnd(Nuclear::IEffect* m_pEffect);
    void DrawEffect();
	void playOffEffect();
	void playOnEffect();

private:
	const static int LOCATION_Y_OFFSET = 100;

    TaskOnOffEffectNotify* m_pOffNotify;
	TaskOnOffEffectNotify* m_pOnNotify;

	Nuclear::IEffect* m_pOffEffect;
	Nuclear::IEffect* m_pOnEffect;
};

inline TaskOnOffEffectManager* gGetTaskOnOffEffectManager()
{
	return TaskOnOffEffectManager::GetInstance();
}

#endif
