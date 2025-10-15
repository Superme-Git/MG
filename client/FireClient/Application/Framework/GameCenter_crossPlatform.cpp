//////////////////////////////////////////////////////////////////////////
// File:		GameCenter_crossPlatform.cpp
// Author:	杨春雷
// Date:		2016/05/05
//////////////////////////////////////////////////////////////////////////

#include "GameCenter.h"

GameCenter::GameCenter()
	: mState(STATE_INIT)
	, mTimeout(0)
{
}

GameCenter::~GameCenter()
{
	// do last chance send achievement scores to game center
    if(STATE_MAIN == mState)
    {
        update(mTimeout);
    }
}

void GameCenter::setState(STATE state)
{
	if (mState != state)
	{
		mState = state;

		switch (mState)
		{
		case STATE_INIT:
			mTimeout = 0;
			mAchievementInfos.clear();
			mFailedSendIdScores.clear();
			break;

		case STATE_LOGIN_FAILED:
			mTimeout = MAX_TIMEOUT;
			break;

		case STATE_REQ_ACHI_FAILED:
			mTimeout = MAX_TIMEOUT;
			break;
		}
	}
}

GameCenter::AchievementInfo* GameCenter::getAchievementInfoById(int id)
{
	AchievementInfo* pRet = NULL;

	AchievementInfoMap::iterator it = mAchievementInfos.find(id);
	if (it != mAchievementInfos.end())
	{
		pRet = &(it->second);
	}

	return pRet;
}

void GameCenter::sendAchievementScore(int id, int score)
{
	AchievementInfo* pAchInfo = getAchievementInfoById(id);

	if (pAchInfo)
	{
		if (!pAchInfo->completed)
		{
			if ((pAchInfo->curScore + score) >= pAchInfo->needScore)
			{
				pAchInfo->completed = true;
			}

			sendAchievementScoreImp(id, score);
		}
	}
	else
	{
		if (mState == STATE_MAIN)
		{
			// 不存在此 id 的成就项
		}
		else
		{
			addFailedSendIdScores(id, score);
		}
	}
}

void GameCenter::update(int delta)
{
	switch (mState)
	{
	case STATE_INIT:
		login();
		break;

	case STATE_LOGIN_FAILED:
		updateRelogin(delta);
		break;

	case STATE_LOGIN_SUCCESS:
		requestAchievementInfo();
		break;
            
    case STATE_REQ_ACHI_SUCCESS:
        setState(STATE_MAIN);
        break;

	case STATE_REQ_ACHI_FAILED:
		updateRerequestAchievementInfo(delta);
		break;

	case STATE_MAIN:
		updateResend(delta);
		break;
	}
}

void GameCenter::addAchievementInfo(int id, const std::string& name, bool bCompleted, int needScore, int curScore)
{
    AchievementInfo achi = {id, name, bCompleted, needScore, curScore};
    mAchievementInfos[id] = achi;
}

void GameCenter::addFailedSendIdScores(int id, int score)
{
	IdScorePair idScore = {id, score};
	mFailedSendIdScores.push_back(idScore);

	if (mTimeout <= 0)
	{
		mTimeout = MAX_TIMEOUT;
	}
}

void GameCenter::updateRelogin(int delta)
{
	if (mTimeout > 0)
	{
		mTimeout -= delta;

		if (mTimeout <= 0)
		{
			login();
		}
	}
}

void GameCenter::updateRerequestAchievementInfo(int delta)
{
	if (mTimeout > 0)
	{
		mTimeout -= delta;

		if (mTimeout <= 0)
		{
			requestAchievementInfo();
		}
	}
}

void GameCenter::updateResend(int delta)
{
	if (mTimeout > 0)
	{
		mTimeout -= delta;
	}

	if (mTimeout <= 0)
	{
		if (!mFailedSendIdScores.empty())
		{
			IdScorePairList tmpList = mFailedSendIdScores;
			mFailedSendIdScores.clear();

			for (IdScorePairList::iterator it = tmpList.begin(); it != tmpList.end(); ++it)
			{
				const IdScorePair& idScore = *it;
				sendAchievementScoreImp(idScore.id, idScore.score);
			}
		}
	}
}