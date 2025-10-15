#ifndef __MAPWALKER_H
#define __MAPWALKER_H


enum eMapWalkerType
{
	eMapWalkerType_Normal,		//普通的寻路至某点
	eMapWalkerType_JumpPoint,	//寻路至跳转点
	eMapWalkerType_VisiteNpc,	//寻路至npc
	eMapWalkerType_OpenPack,	//寻路至目的点自动打开包裹栏
	eMapWalkerType_ReturnTeam,	//自动归队寻路
};

class MapWalker
{
public:
	MapWalker();

	void SetTarget(int x,int y,int mapId,int npcid,int chefu=0,bool returnteam=false,bool blinkfindpath=false,int64_t npckey=0);
	
	void CrossGoTo();

	//避免寻路到跳转点
	bool CheckTarget(const int& x, const int &y, const int& mapid);

	void RunPacing();
	bool CanGoTo(const int& mapid);
	void ClearGoTo(bool bfinishgoto = false, bool clearTreasureInfo = false);
	bool GetAStarState();
	void SetAStarState(bool bOn);

	int	 GetTargetMapID();
	eMapWalkerType GetMapWalkerType();

	bool IsThroughChefu();
	bool IsThroughLinanTrans();
	bool IsThroughFamilyManager();

	Nuclear::NuclearLocation GetTargetPos();

	Nuclear::NuclearLocation GetCurrentMapTarget();

	int  GetFactionTargetMapID();
private:
	MapWalker(const MapWalker&);
	MapWalker& operator=(const MapWalker&);
	void BuildMapPath();
	Nuclear::NuclearLocation GetTranspotLoc();		//跳转点
	Nuclear::NuclearLocation GetFactionMapTranspotLoc();
private:
	//跳转点范围
	static const int s_JumpToS = 10 ;
	eMapWalkerType m_MapWalkerType;
	int m_iFactionTargetMapID;
	int m_iThroughChefu;
	int m_targetNpcID;
    int64_t m_targetNpcKey;
	int m_xPos;												// 目标点
	int m_yPos;
	int m_targetMapID;										// 目标地图ID
	Nuclear::NuclearLocation m_pCurrentMapTarget;					//当前地图的目标点
	bool m_bStopThenPace;									//寻路终止后在附近踱步
	bool m_bOnAstar;
	bool m_bLinkFindPath;									//是否是点击超链接引起的寻路
	std::multimap<int,Nuclear::NuclearLocation> m_MapJumpNode;		//key 是mapid，value是跳转点
	std::map<int, Nuclear::NuclearLocation> m_MapTransport;		//当时为了节约key是SourceMapID * 10000 + destMapID  value是跳转点信息
};



#endif

