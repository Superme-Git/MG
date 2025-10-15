#ifndef __CHECKDISTANCETIMER_H
#define __CHECKDISTANCETIMER_H

#include "../Framework/GameScene.h"

#define g_gridDistance 13

class CheckDisTimer : public Nuclear::INuclearTimer
{
protected:
	class CheckInfo
	{
	public:
		CheckInfo()
			:roleid(-1), roleType(eSceneObjMax), pwindow(NULL), bActive(false), distance(g_gridDistance)
		{ }

		CheckInfo(int64_t id, eSceneObjType type, Dialog* window, bool active = true, int iDistance = g_gridDistance)
			: roleid(id)
			, roleType(type)
			, pwindow(window)
			, bActive(active)
			, distance(iDistance)
			, windowName(window->GetLayoutFileName())
		{ }

		CEGUI::String   windowName;
		Dialog*		pwindow;
		eSceneObjType roleType;
		int64_t roleid;
		int             distance;
		bool			bActive;
	};

private:
	CheckDisTimer(){}

public:

	virtual void OnTimer()
	{
		std::list<CheckInfo>::iterator it = m_checklist.begin();
		while (it != m_checklist.end() && it->bActive)
		{
			if (gGetScene()->GetDistanceFromMainCharacter(it->roleid, it->roleType) >= it->distance)
			{
				if (it->pwindow != NULL)
				{
#ifdef DEBUG
					printf("checkdis timer window name:%s\n", it->windowName.c_str());
#endif
					it->pwindow->CheckDisTimerCallBack();
					it->pwindow->OnClose();
					it = m_checklist.erase(it);
				}
				else
					++it;
			}
			else
				++it;
		}

		it = m_checklist.begin();
		while (it != m_checklist.end())
		{
			if (!it->bActive)
				it = m_checklist.erase(it);
			else
				++it;
		}

		if (m_checklist.empty())
		{
			Nuclear::GetEngine()->CancelTimer(this);
		}
	}

	void CancelAll()
	{
		Nuclear::GetEngine()->CancelTimer(this);
		m_checklist.resize(0);
	}

	void CancelSchedule(Dialog* window)
	{
		std::list<CheckInfo>::iterator it = m_checklist.begin();
		while (it != m_checklist.end())
		{
			if (it->pwindow == window)
			{
				it->bActive = false;
			}
			++it;
		}
	}

	bool Schedule(int64_t roleID, eSceneObjType roleType, Dialog* pwindow, int iDistance = g_gridDistance)
	{
		CheckInfo checkContent(roleID, roleType, pwindow, true, iDistance);
		m_checklist.push_back(checkContent);
		if (m_checklist.size() == 1)
		{
			return Nuclear::GetEngine()->ScheduleTimer(this, 1500/*time 1.5 second*/);
		}
		return true;
	}

	static CheckDisTimer& getInstance()
	{
		static CheckDisTimer SingletonCDT;
		return SingletonCDT;
	} 

private:
	std::list<CheckInfo>		m_checklist;

};
#endif
