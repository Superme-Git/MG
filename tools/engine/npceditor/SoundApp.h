#pragma once
#include "engine\iapp.h"
#include "engine\iengine.h"
//#include "..\engine\iworld.h"
#include "sprite.h"
#include "engine\ieffect.h"
#include <map>
#include "SoundEditorDoc.h"
#include "Npc.h"
#include "NpcPropertyDlg.h"
#include "JumpPointInfDlg.h"
#include "regionbuffer.h"
#include "Monster.h"
//2015 Yao
#include "CMonsterPanel.h"


#define GRID_WIDTH	24
#define GRID_HEIGHT	16
namespace Nuclear
{
	class XPEnvSoundCircleArea;
	class XPEnvSoundPolygonArea;
	class XPEnvSoundArea;
	class Renderer;
};
struct stJumpPointInf //跳转点信息
{
	int id;  //编号
	Nuclear::IEffect* effect;   //对应的特效
	int dest_mapID;    //目标地图
	int dest_X;        //目标点X
	int dest_Y;        //目标点Y

};

const DWORD freeAreaColor[6]=
{
	0x3FFF0000,
	0x3F00FF00,
	0x3F0000FF,
	0x3FFF8F00,
	0x3FFF8FFF,
	0x3F8FFF8F,
};

enum RegionType{
	RY_QIEZUO = 0x1,//切磋
	RY_BOSA = 0x2,//播撒
	RY_BAITAN = 0x4,//摆摊
	RY_KITE = 0x8,		//
	RY_SHUAGUAI = 0x10,  //刷怪
	RY_MINGSHENG = 0x20,  //名胜区
	RY_GAOJISHUGUAI = 0x40,  //高级刷怪区
	RY_AREA11 = 1<<10,  //区域11
	RY_AREA12 = 1<<11,  //区域12
	RY_AREA13 = 1<<12,  //区域13
	RY_AREA14 = 1<<13,  //区域14
	RY_AREA15 = 1<<14,  //区域15
	RY_AREA16 = 1<<15,  //区域16

};

enum QingGongBlockType{   
	QG_1 = 0x1,           //飞跃阻挡1阶
	QG_2 = 0x2,           //飞跃阻挡2阶
	QG_3 = 0x4,           //飞跃阻挡3阶
	QG_4 = 0x8           //飞跃阻挡4阶

};

const char s_BlockLayer2Mask=0x08;
	

class SoundApp : public Nuclear::IApp
{
//自定义常量、类型
private:
	typedef std::map<Nuclear::ISprite*, Sprite*> SpriteMap;
	typedef std::vector<CNpc*> NpcList;
	typedef std::vector<stNpcBaseData*> NpcBaseList;


//成员属性们
	SpriteMap m_spriteMap;
	Nuclear::IEngine *m_pEngine;
	EDIT_STATE m_editState;
	Sprite* m_pHero;
	CSoundEditorDoc* m_pDoc;
	CNpcPropertyDlg* m_pNpcDlg;
	CJumpPointInfDlg* m_pJPInfDlg;
    
	

	Nuclear::PictureHandle m_AreaHandle;
	bool m_bDrawGird;
	bool m_bDrawBlock;
	//区域类型
	int m_regionType;
	unsigned char* m_MazeBuffer;
	size_t	m_mapGridWidth;
	size_t	m_mapGridHeight;
	std::vector<Nuclear::FRECT> m_BlockRectList;                // green 0x1 0x3f04fd16
	
	std::vector<Nuclear::FRECT> m_BlockRectList1;
	std::vector<Nuclear::FRECT> m_BlockRectList2;
	NpcList m_NpcList;  //Npc列表
	NpcBaseList m_NpcBaseList;  //基本Npc列表
	std::wstring m_strNpcXmlName;
	std::wstring m_strGotoXmlName;

	CNpc* m_pCurSelNpc;

	std::map<int,std::wstring> m_NpcShapeMap;
	std::map<int,std::wstring> m_NpcBodyNameMap;
	std::map<int,std::wstring> m_NpcHeadNameMap;
	std::map<int,std::wstring> m_NpcHairNameMap;

	std::map<std::wstring,int> m_NpcDirMap;

	int m_EdgeSize;
	
public:
	SoundApp(void);
	virtual ~SoundApp(void);

	void RemoveAllSprite();
	void RemoveAllNpc();
	void SetState(EDIT_STATE state);
	Sprite* GetHero() { return m_pHero; }
	void Reset();
	void SetDoc(CSoundEditorDoc* pDoc) { m_pDoc = pDoc; }

	void SetBlockPoint(CPoint point,bool bAllScreen,bool tab,bool brush,unsigned char mask=0x03);
	bool ReadMazeBuffer(CString mazefilemame);
	bool WriteMazeBuffer(CString mazefilemame);
	void NewMazeBuffer()
	{
		m_mapGridWidth = (m_pDoc->GetMapSize().x + GRID_WIDTH - 1)/GRID_WIDTH;
		m_mapGridHeight = m_pDoc->GetMapSize().y/ GRID_HEIGHT;

		if (NULL!=m_MazeBuffer)
		{
			delete []m_MazeBuffer;
			m_MazeBuffer=NULL;
		}
		m_MazeBuffer = new unsigned char[m_mapGridWidth*m_mapGridHeight];
		memset(m_MazeBuffer,0,m_mapGridWidth*m_mapGridHeight);
	}

	void CheckMazeLayer2IsNew(); //检测2层阻挡是不是没刷过，没有任何二层阻挡则认为是新的，把这层的阻挡全刷上
	unsigned char GetMazeMask(CPoint pixel);
	void SetMazeMask(unsigned int mask,CPoint pixel,bool bAllScreen,bool brush = false);
	unsigned char* GetMazeBuffer(){return m_MazeBuffer;}
	void ClearMazeMask(unsigned int mask,CPoint pixel,bool bAllScreen);
	void ClearMazeMask(unsigned int mask,CPoint center,int edge,bool bAllScreen);
	int  GetNewNpcBaseID(); //获得新的npcbaseID
	CNpc* m_pEditNpc;  //编辑器选中的NPC

	bool CheckLonelyIsland();	//检查孤岛
	void JumpToIsland(int islandIdx); //跳到孤岛的第一个格子坐为屏幕的中心
//NPC编辑相关
	void LoadAllBaseNpc();
	void LoadNpcShapeMap();
	void RemoveAllBaseNpc();
	const stNpcBaseData* GetNpcBaseById(int id) ;
	void ResetMapNpc(std::wstring NpcBasefilemame);
	void AddNpc(int id,int nBaseId,int dir,Nuclear::Location loc);
	void RemoveNpc(CNpc* pNpc);
	bool GetXmlNodeList(std::wstring path, XMLIO::CFileReader& fr, XMLIO::CNodeList& typenl);
	bool SelectNpc(CPoint pt);
	void MoveSelectNpc(CPoint pt);
	void PutNpc(CPoint pt);
	CNpc* AddNewNpc(int baseid);
	void InitNpcBaseComBox(CComboBox* pComBox);
	bool AddNewNpcBase(int id,int type,std::wstring strName,std::wstring strModel=L"male",std::wstring strTitle=L"",int chat1=1,int chat2=1,int chat3=1 );
	bool AddNewNpcBase(stNpcBaseData* pNewNpcBase);
	bool CheckNpcBaseUsed(int id); //检查该npcbaseid是否已在当前地图中存在
	bool IsExistBaseID(int id); //查表base表中是否有该BaseId
	void InitNpcTypeComBox(CComboBox* pComBox);
	void DrawSelectNpcBox();
	void DrawNpcName();
	void InitTypeEditBox(int type,CEdit* pEditBox);
	void InitDirCombox(int dirType,CComboBox* pComBox);
	int  GetComboxIdxByTypeAndDir(int dirType,int dir); //根据方向类型和方向得到combox对应的选项索引
	int GetDirByComboxIdxAndDirType(int dirType,int idx);  //根据方向类型和combox索引得到对应的方向

	bool OnSaveNpcBaseInf(); 
	bool OnSaveNpcInf();//存储NPCBASE信息
	void DelCurEditNpc();


	//2015 Yao
	CMonsterPanel* m_pMonPanel;
	void CallMonsterDlg();
	void LoadAllMonster();

	typedef std::vector<stMonsterBaseData*> MonsterBaseList;
	MonsterBaseList m_monsterBaseList;
	void CreateMonCombo(CComboBox* pMonsterCombo);

	struct MonsterInfo
	{
		std::wstring ID;
		Nuclear::XPCOLOR Color;
	    Nuclear::FRECT Area;
	};
	std::map<int,MonsterInfo> m_MonBlockList; 

	int CheckMonsterPoint(const unsigned int& w, const unsigned int& h);
	int GetMonKind(int id);
	void SetMonKind(int id);

	//2015 Yao MonsterSetting
	void AddMonsterPoint(const int& x, const int& y);
	void DelMonsterPoint(const int& x, const int& y);
	bool SaveMonsterKind(std::wstring filename);
	bool ReadMonsterKind(std::wstring filename);

	void SetAllMask(unsigned int mask);
	void ClearAllMask(unsigned int mask);
	

//实现
	virtual bool OnInit(int step);
	bool isInited() { return m_bEngineInited; }
	virtual bool OnExit();

	virtual void OnTick(int now, int delta);	// 主线程主循环中每帧调用，可处理客户端中与渲染无关的日常工作，如处理协议等
	virtual bool OnBeforeRender(int now/*ms*/); // 引擎每帧渲染之前调用，用于客户端更新渲染相关数据，返回false则不渲染，可用此函数内wait来控制帧率
	virtual void OnRenderUI(int now);
    void OnRenderInit(int now, int step, int totalstep) {}	
	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type); // 当精灵状态发生改变时，回调这个函数更新动作。
	afx_msg void OnDrawGird();
	afx_msg void OnUpdateDrawGird(CCmdUI *pCmdUI);
	afx_msg void OnDrawBlock();
	afx_msg void OnUpdateDrawBlock(CCmdUI *pCmdUI);

	void SetDrawGrid(bool b){m_bDrawGird = b;}
	void SetDrawBlock(bool b){m_bDrawBlock = b;}
	void SetRegionType(int t){m_regionType = t;}
	void OnRenderUnderSprite() {}

//以下为编辑地图跳转点相关
public:
	int AddJumpPoint(int Logic_X,int Logic_Y,int destMapID,int Dest_X,int Dest_Y);
	int  SelectJumpPointByPoint(CPoint pt); //选择跳转点
	void DelectJumpPoint(int id); //删除一个跳转点
	void LoadJumpPoint(const std::wstring& strGotoXmlName ); //载入该地图的跳转点信息
	bool SaveJumpPointInf(); //保存信息
	void DrawJumpPoint(); //画选择的跳转点的标志
	void AddNewJumpPoint();
	void SetSelJumpPoint(int id);
	void SetEditJumPoint(int id);
	int GenerateNewJumpPointID();
	void ChangeEditJPInf(int mapID,int Dest_X,int Dest_Y);
	void DelCurEditJumPoint();
    void MoveSelJumpPoint(CPoint pt);
	int GetCurSelectJumPoint() { return m_selectJumpPointID; }
	void DeleAllJumPoint();
	void ShowJumpInf(CPoint pt);
	bool PointCanJump(CPoint pt);
	CPoint FixPoint(const CPoint& pt);
	
	//设置区域
	void SetRegion(const int& x, const int& y , RegionType rt);
	void SetRegionBig(const int& x, const int& y , RegionType rt);
	void DelRegion(const int& x, const int& y , RegionType rt);
	void DelRegionBig(const int& x, const int& y , RegionType rt);
	bool GetIsTypeOfRegion(const int& x, const int& y , RegionType rt);
	void WriteRegionBufferToFile(std::wstring filename);
	void ReadRegionBufferFromFile(std::wstring filename);
	
	
	//2015 Yao
	std::map<unsigned int,int> m_Monbuffer;
	int monKind;
	int monIndex;
	std::vector<Nuclear::XPCOLOR> m_BlockColorList;

private:
	std::vector<stJumpPointInf> m_vecJumpVector;
	int m_selectJumpPointID;
	int m_EditJumpPointID;
	const static int s_iJumPointGridNum=5;  //一个跳转点占的格子数N*N
	const static int s_iJumpPointOffset_X=1; // 跳转区域中心对应的跳转效果的location偏格子数,负值则左偏
	const static int s_iJumpPointOffset_Y=0; // 跳转区域中心对应的跳转效果的location下偏格子数,负值则上移

	bool m_bEngineInited;

//////////////////////////////////////////////////////////////////////////
private: 
	CRegionBuffer<unsigned short> m_RegionBuffer;
       
	
///////////////////跳跃阻挡点相关
public:
	void NewJumpBlockBuffer();
	bool LoadJumpBlockInf(std::wstring mazefilemame);
	unsigned char GetJumpBlock(CPoint pixel);
	void SetJumpBlock(CPoint pixel,bool brush,unsigned char type);
	void ClearJumpBlock(CPoint center,int edge);
	void DrawJumpBlockRect();
	void ReleaseJumpBlockInf();
	void SaveJumpBlockInf(std::wstring mazefilemame);
	void SaveIslandInf(std::wstring mazefilemame,int layer); //保存孤岛信息

	int processBlockInfo(char* blockinfo,int x_grid_num,int y_grid_num);
	void processIsland(char* blockinfo,int x, int y,int x_grid_num,int y_grid_num,  int islandindex);
	bool validPos(char* blockinfo, int x, int y, int tmpx, int tmpy,int x_grid_num,
		int y_grid_num);
	void getAdjacentPos(std::vector<Nuclear::Location>& vecPos,int x, int y,int maxx,int maxy);

	void prinfBlock(char* blockinfo,int x_grid_num,int y_grid_num);

	void processAdjacentPoses(char* blockinfo, const std::vector<Nuclear::Location>& inputVecPos,
		std::vector<Nuclear::Location>& outVecPos,int x_grid_num,int y_grid_num, char islandindex);
	void DrawGuDaoInf(int layer);

	void DrawFreeArea(int regionType);
	DWORD GetFreeAreaColor(int regionType);

private:
	unsigned char* m_JumpBlockBuffer; //跳跃阻挡点
	char* m_IslandBuffer;
	int m_IsLandNumber; //孤岛个数

	char* m_IslandBuffer2; //二层孤岛
	int m_IsLandNumber2; //二层孤岛个数

//////////////////////////////////////////////////////////////////////////


};
