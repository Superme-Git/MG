#include "stdafx.h"
#include "TaskOnOffEffectManager.h"
#include "Utils2.h"

TaskOnOffEffectManager::TaskOnOffEffectManager():
m_pOnNotify(NULL),
m_pOffNotify(NULL),
m_pOnEffect(NULL),
m_pOffEffect(NULL)
{ }

TaskOnOffEffectManager::~TaskOnOffEffectManager()
{
    if(m_pOnEffect) {
		Nuclear::GetEngine()->ReleaseEffect(m_pOnEffect);
    }
    
    if(m_pOffEffect) {
		Nuclear::GetEngine()->ReleaseEffect(m_pOffEffect);
    }
}

void TaskOnOffEffectManager::effectEnd(Nuclear::IEffect* m_pEffect)
{
	if (m_pEffect == m_pOnEffect && m_pOnEffect != NULL) {
		Nuclear::GetEngine()->ReleaseEffect(m_pOnEffect);
		m_pOnEffect = NULL;
		m_pOnNotify = NULL;
	}

	if (m_pEffect == m_pOffEffect && m_pOffEffect != NULL) {
		Nuclear::GetEngine()->ReleaseEffect(m_pOffEffect);
		m_pOffEffect = NULL;
		m_pOffNotify = NULL;
	}
}

void TaskOnOffEffectManager::DrawEffect()
{
	if (m_pOnEffect) {
		Nuclear::GetEngine()->DrawEffect(m_pOnEffect);
	}

	if (m_pOffEffect) {
		Nuclear::GetEngine()->DrawEffect(m_pOffEffect);
	}
}

void TaskOnOffEffectManager::playOffEffect()
{
	if (m_pOffEffect) {
		m_pOffEffect->RemoveNotify(m_pOffNotify);
		delete m_pOffNotify;
		m_pOffNotify = NULL;

		Nuclear::GetEngine()->ReleaseEffect(m_pOffEffect); // ReleaseEffect内会delete特效，外面千万别再释放了
		m_pOffEffect = NULL;
	}

	Nuclear::NuclearDisplayMode mode = Nuclear::GetEngine()->GetRenderer()->GetDisplayMode();
	m_pOffEffect = Nuclear::GetEngine()->CreateEffect(MHSD_UTILS::GetEffectPath(10369), true);
	if (m_pOffEffect == NULL) {
		return;
	}
	m_pOffEffect->SetLocation(Nuclear::NuclearLocation(mode.width / 2, mode.height / 2 - LOCATION_Y_OFFSET));


	m_pOffNotify = new TaskOnOffEffectNotify;
	m_pOffEffect->AddNotify(m_pOffNotify);
}

void TaskOnOffEffectManager::playOnEffect()
{
	if (m_pOnEffect) {
		m_pOnEffect->RemoveNotify(m_pOnNotify);
		delete m_pOnNotify;
		m_pOnNotify = NULL;

		Nuclear::GetEngine()->ReleaseEffect(m_pOnEffect);
		m_pOnEffect = NULL;
	}

	Nuclear::NuclearDisplayMode mode = Nuclear::GetEngine()->GetRenderer()->GetDisplayMode();
	m_pOnEffect = Nuclear::GetEngine()->CreateEffect(MHSD_UTILS::GetEffectPath(10370), true);
	m_pOnEffect->SetLocation(Nuclear::NuclearLocation(mode.width / 2, mode.height / 2 - LOCATION_Y_OFFSET));

	if (m_pOnEffect == NULL) {
		return;
	}

	m_pOnNotify = new TaskOnOffEffectNotify;
	m_pOnEffect->AddNotify(m_pOnNotify);
}

void TaskOnOffEffectNotify::OnDelete(Nuclear::IEffect *pEffect)
{
	delete this;
}

void TaskOnOffEffectNotify::OnEnd(Nuclear::IEffect *pEffect)
{
	if (pEffect != NULL) {
		pEffect->RemoveNotify(this);
		if (TaskOnOffEffectManager::GetInstance()) {
			TaskOnOffEffectManager::GetInstance()->effectEnd(pEffect);
		}
		delete this;
	}
}
