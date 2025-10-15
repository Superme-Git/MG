//////////////////////////////////////////////////////////////////////////
// File:		GameCenter.h
// Author:	杨春雷
// Date:		2016/05/05
//////////////////////////////////////////////////////////////////////////

#ifndef _GameCenter_h_
#define _GameCenter_h_

#include "Singleton.hpp"
#include <string>
#include <map>
#include <list>

// 对接 ios 上的 game center

class GameCenter : public CSingleton<GameCenter>
{
public:
	enum STATE
	{
        STATE_DISABLED,
		STATE_INIT,
		STATE_LOGIN_WAITING,
		STATE_LOGIN_SUCCESS,
		STATE_LOGIN_FAILED,
		STATE_MAIN,
		STATE_REQ_ACHI_WAITING,
		STATE_REQ_ACHI_SUCCESS,
		STATE_REQ_ACHI_FAILED,
	};

	struct AchievementInfo
	{
		int id;
		std::string name;
		bool completed;
		int needScore;
		int curScore;
	};
	typedef std::map<int, AchievementInfo> AchievementInfoMap;

	struct IdScorePair
	{
		int id;
		int score;
	};
	typedef std::list<IdScorePair> IdScorePairList;

	static const int MAX_TIMEOUT = 1 * 60 * 1000;  // 1 minute

private:
	STATE mState;
	int mTimeout;

	AchievementInfoMap mAchievementInfos;

	IdScorePairList mFailedSendIdScores;

public:
	GameCenter();
	~GameCenter();

	bool isAvailable() const;

	void setState(STATE state);

	void login();
	void requestAchievementInfo();

	AchievementInfo* getAchievementInfoById(int id);

	void sendAchievementScore(int id, int score);

	void update(int delta);

private:
	void addAchievementInfo(int id, const std::string& name, bool bCompleted, int needScore, int curScore);

	void sendAchievementScoreImp(int id, int score);
	void addFailedSendIdScores(int id, int score);

	void updateRelogin(int delta);
	void updateRerequestAchievementInfo(int delta);
	void updateResend(int delta);
};

#endif