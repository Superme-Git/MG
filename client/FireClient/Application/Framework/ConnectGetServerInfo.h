#ifndef __CONNECTGETSERVERINFO_H
#define __CONNECTGETSERVERINFO_H

#include "Singleton.hpp"
#include "network/HttpClient.h"
#include "network/HttpRequest.h"
#include "network/HttpResponse.h"

enum enumServerHttpAdress
{
	eHttpPatchUrl,
	eHttpUpdateUrl,
	eHttpShareUrl,
	eHttpInfoUrl,
	eHttpChatUrl,
	eHttpCommunityUrl,
	eHttpNoticeUrl,
	eHttpConfigUrl,
	eHttpServerInfoUrl,
	eHttpXunFeiServerUrl,
	eHttpHorseRunUrl,
	eHttpJinglingUrl,
	eHttpKongjianUrl
};

enum enumGetServerState
{
	eNumNotConnect,
	eNumConnectSuccess,
	eNumConnectFailed,
};

struct stServerINFO
{
	int areaID;
	std::wstring serverid;
	std::wstring serverArea;
	std::wstring serverName;
	std::wstring serverIp;
	std::wstring serverPort;
	std::wstring serverState;
	std::wstring serverStandby;
	std::wstring serverType;
	std::wstring serverOpenTime;
	std::wstring serverFlag;
};

struct stJinglingINFO
{
	std::string jl_faqid;
	std::string jl_categoryid;
	std::string jl_title;
	std::string jl_style;
	std::string jl_sort;
	std::string jl_status;
	std::string jl_content;
};

struct stQuestTitleID
{
	std::string qid;
	std::string qtitle;
};
struct stRecruitList
{
	std::string roleid;
	std::string name;
	std::string avatar;
	std::string level;
	std::string servername;
	std::string serverid;
};
struct stRecruitOneRoleData
{
	std::string serverid;
	std::string roleid;
	std::string new_serverid;
	std::string new_roleid;
	std::string prize_type;
	std::string amount;
};
struct stRecruitData
{
	int total;
	std::string times_item;
};
class ConnectGetServerInfo : public cocos2d::CCObject, public CSingleton<ConnectGetServerInfo>
{
public:
	void connetGetServerlist();
	void connetGetUserInfo();
	void connetCreateRoleInfo(int serverid, int headid);
	void connetGetRecruitInfo(char* url, int index);
	//0:接口3.校验招募码是存在, 不校验被招募  http://mt3.pengyouquan001.locojoy.com:8830/enlist/check_code?code=7956261002
	//1:接口4.校验招募码, 同时校验被招募人    http://mt3.pengyouquan001.locojoy.com:8830/enlist/check?code=7956261002&new_serverid=1101011002&new_roleid=795626
	//2:接口5.招募人提交招募码 http ://mt3.pengyouquan001.locojoy.com:8830/enlist/submit_code?code=7956261002&new_serverid=1101011002&new_roleid=795626
	//3:接口6.招募人获取被招募人列表 http://mt3.pengyouquan001.locojoy.com:8830/enlist/get_enlist_list?serverid=1101011002&roleid=795626
	//4:接口8.获取某被招募人的奖励进度状态http ://mt3.pengyouquan001.locojoy.com:8830/enlist/get_prize_list?serverid=1101011002&roleid=795626&new_serverid=123&new_roleid=123
	//获取招募人招募人数,和已领取的次数奖励
	//http://mt3.pengyouquan001.locojoy.com:8830/enlist/get_times_prize?serverid=1101011002&roleid=795626
	void HandleCheckRecruit(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void HandleCheckRecruitAndRole(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void HandleSendRecruitCode(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void HandleGetRecruitList(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void HandleCheckRecruitOneRole(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void HandlegetRecruitInfo(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);

	void connetCheckRecruitCode(char* url);
	void connetCheckRecruitCodeAndRoleid(char* url);
	void connetGetOneRoleInfo(char* url);
	void onGetServerlist(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void onGetUserInfo(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void onSerUserInfo(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);

	void setHttpAdressMap(enumServerHttpAdress em, std::string value);
	std::string getHttpAdressByEnum(enumServerHttpAdress em);

	std::vector<stServerINFO>& getAllServers(){ return mServers; }

	enumGetServerState getConnectState(){ return conState; }

	int getRoleHeadInfoByServerID(int serverID);
	void setServerRoleInfoMap(int key, int value);

	void setConnectFromLogin(bool isInLogin){ isLoginConnecting = isInLogin; }

	void clearHeadInfo();

	void doEnterBBS();

	//精灵相关
	void doRequestJingLingQuest(int id);
	void doRequestSearchData(std::string keyWord);
	void onGetJingLingData(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void onGetSingelAnswer(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void onGetSearchData(cocos2d::extension::CCHttpClient* client, cocos2d::extension::CCHttpResponse* response);
	void getRedianIds(std::vector<int>& ids);  //取得热点ids
	void getTuijianIds(std::vector<int>& ids);  //取得推荐ids
	void getKefuIds(std::vector<int>& ids);  //取得客服ids
	void getSearchResultList(std::string q, std::vector<stQuestTitleID>& ids);
	stJinglingINFO getJinglingDataByID(int id); //for lua
	void clearJinglingData();
	//招募
	stRecruitData getRecruitData(){ return m_RecruitData;}
	std::vector<stRecruitList> getRecruitList(){ return m_RecruitList; }
	std::vector<stRecruitOneRoleData> getRecruitOneRole(){ return m_RecruitRoleList; }
private:
	bool isLoginConnecting;
	bool isGetUserInfoSuccessful;
	bool isRetryGetUserInfo;
	std::vector<stServerINFO> mServers;
	enumGetServerState conState;
	std::map<enumServerHttpAdress, std::string> httpAdressMap;
	std::map<int, int> roleServerInfoMap;

	//精灵问答相关
	int m_currentRequestID;
	std::map<int, stJinglingINFO> jinglingDataMap;
	std::map<std::string, std::vector<stQuestTitleID> > questWidthoutContent;
	std::vector<int> redianIds;
	std::vector<int> tuijianIds;
	std::vector<int> kefuIds;

	//招募相关数据
	stRecruitData m_RecruitData; //招募人数和奖励
	std::vector<stRecruitList> m_RecruitList;//招募列表
	std::vector<stRecruitOneRoleData>m_RecruitRoleList;//某个人数据
	void checkVectorContainsAndInsert(std::vector<int>& vec, int value);

	void doAfterGetServerListData(); 
public:
	ConnectGetServerInfo();
	~ConnectGetServerInfo();
};

inline ConnectGetServerInfo* GetServerInfo()
{
	return ConnectGetServerInfo::NewInstance();
}

inline void destroyServerInfo()
{
	ConnectGetServerInfo::RemoveInstance();
}

#endif

