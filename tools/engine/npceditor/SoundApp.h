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
struct stJumpPointInf //��ת����Ϣ
{
	int id;  //���
	Nuclear::IEffect* effect;   //��Ӧ����Ч
	int dest_mapID;    //Ŀ���ͼ
	int dest_X;        //Ŀ���X
	int dest_Y;        //Ŀ���Y

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
	RY_QIEZUO = 0x1,//�д�
	RY_BOSA = 0x2,//����
	RY_BAITAN = 0x4,//��̯
	RY_KITE = 0x8,		//
	RY_SHUAGUAI = 0x10,  //ˢ��
	RY_MINGSHENG = 0x20,  //��ʤ��
	RY_GAOJISHUGUAI = 0x40,  //�߼�ˢ����
	RY_AREA11 = 1<<10,  //����11
	RY_AREA12 = 1<<11,  //����12
	RY_AREA13 = 1<<12,  //����13
	RY_AREA14 = 1<<13,  //����14
	RY_AREA15 = 1<<14,  //����15
	RY_AREA16 = 1<<15,  //����16

};

enum QingGongBlockType{   
	QG_1 = 0x1,           //��Ծ�赲1��
	QG_2 = 0x2,           //��Ծ�赲2��
	QG_3 = 0x4,           //��Ծ�赲3��
	QG_4 = 0x8           //��Ծ�赲4��

};

const char s_BlockLayer2Mask=0x08;
	

class SoundApp : public Nuclear::IApp
{
//�Զ��峣��������
private:
	typedef std::map<Nuclear::ISprite*, Sprite*> SpriteMap;
	typedef std::vector<CNpc*> NpcList;
	typedef std::vector<stNpcBaseData*> NpcBaseList;


//��Ա������
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
	//��������
	int m_regionType;
	unsigned char* m_MazeBuffer;
	size_t	m_mapGridWidth;
	size_t	m_mapGridHeight;
	std::vector<Nuclear::FRECT> m_BlockRectList;                // green 0x1 0x3f04fd16
	
	std::vector<Nuclear::FRECT> m_BlockRectList1;
	std::vector<Nuclear::FRECT> m_BlockRectList2;
	NpcList m_NpcList;  //Npc�б�
	NpcBaseList m_NpcBaseList;  //����Npc�б�
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

	void CheckMazeLayer2IsNew(); //���2���赲�ǲ���ûˢ����û���κζ����赲����Ϊ���µģ��������赲ȫˢ��
	unsigned char GetMazeMask(CPoint pixel);
	void SetMazeMask(unsigned int mask,CPoint pixel,bool bAllScreen,bool brush = false);
	unsigned char* GetMazeBuffer(){return m_MazeBuffer;}
	void ClearMazeMask(unsigned int mask,CPoint pixel,bool bAllScreen);
	void ClearMazeMask(unsigned int mask,CPoint center,int edge,bool bAllScreen);
	int  GetNewNpcBaseID(); //����µ�npcbaseID
	CNpc* m_pEditNpc;  //�༭��ѡ�е�NPC

	bool CheckLonelyIsland();	//���µ�
	void JumpToIsland(int islandIdx); //�����µ��ĵ�һ��������Ϊ��Ļ������
//NPC�༭���
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
	bool CheckNpcBaseUsed(int id); //����npcbaseid�Ƿ����ڵ�ǰ��ͼ�д���
	bool IsExistBaseID(int id); //���base�����Ƿ��и�BaseId
	void InitNpcTypeComBox(CComboBox* pComBox);
	void DrawSelectNpcBox();
	void DrawNpcName();
	void InitTypeEditBox(int type,CEdit* pEditBox);
	void InitDirCombox(int dirType,CComboBox* pComBox);
	int  GetComboxIdxByTypeAndDir(int dirType,int dir); //���ݷ������ͺͷ���õ�combox��Ӧ��ѡ������
	int GetDirByComboxIdxAndDirType(int dirType,int idx);  //���ݷ������ͺ�combox�����õ���Ӧ�ķ���

	bool OnSaveNpcBaseInf(); 
	bool OnSaveNpcInf();//�洢NPCBASE��Ϣ
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
	

//ʵ��
	virtual bool OnInit(int step);
	bool isInited() { return m_bEngineInited; }
	virtual bool OnExit();

	virtual void OnTick(int now, int delta);	// ���߳���ѭ����ÿ֡���ã��ɴ���ͻ���������Ⱦ�޹ص��ճ��������紦��Э���
	virtual bool OnBeforeRender(int now/*ms*/); // ����ÿ֡��Ⱦ֮ǰ���ã����ڿͻ��˸�����Ⱦ������ݣ�����false����Ⱦ�����ô˺�����wait������֡��
	virtual void OnRenderUI(int now);
    void OnRenderInit(int now, int step, int totalstep) {}	
	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type); // ������״̬�����ı�ʱ���ص�����������¶�����
	afx_msg void OnDrawGird();
	afx_msg void OnUpdateDrawGird(CCmdUI *pCmdUI);
	afx_msg void OnDrawBlock();
	afx_msg void OnUpdateDrawBlock(CCmdUI *pCmdUI);

	void SetDrawGrid(bool b){m_bDrawGird = b;}
	void SetDrawBlock(bool b){m_bDrawBlock = b;}
	void SetRegionType(int t){m_regionType = t;}
	void OnRenderUnderSprite() {}

//����Ϊ�༭��ͼ��ת�����
public:
	int AddJumpPoint(int Logic_X,int Logic_Y,int destMapID,int Dest_X,int Dest_Y);
	int  SelectJumpPointByPoint(CPoint pt); //ѡ����ת��
	void DelectJumpPoint(int id); //ɾ��һ����ת��
	void LoadJumpPoint(const std::wstring& strGotoXmlName ); //����õ�ͼ����ת����Ϣ
	bool SaveJumpPointInf(); //������Ϣ
	void DrawJumpPoint(); //��ѡ�����ת��ı�־
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
	
	//��������
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
	const static int s_iJumPointGridNum=5;  //һ����ת��ռ�ĸ�����N*N
	const static int s_iJumpPointOffset_X=1; // ��ת�������Ķ�Ӧ����תЧ����locationƫ������,��ֵ����ƫ
	const static int s_iJumpPointOffset_Y=0; // ��ת�������Ķ�Ӧ����תЧ����location��ƫ������,��ֵ������

	bool m_bEngineInited;

//////////////////////////////////////////////////////////////////////////
private: 
	CRegionBuffer<unsigned short> m_RegionBuffer;
       
	
///////////////////��Ծ�赲�����
public:
	void NewJumpBlockBuffer();
	bool LoadJumpBlockInf(std::wstring mazefilemame);
	unsigned char GetJumpBlock(CPoint pixel);
	void SetJumpBlock(CPoint pixel,bool brush,unsigned char type);
	void ClearJumpBlock(CPoint center,int edge);
	void DrawJumpBlockRect();
	void ReleaseJumpBlockInf();
	void SaveJumpBlockInf(std::wstring mazefilemame);
	void SaveIslandInf(std::wstring mazefilemame,int layer); //����µ���Ϣ

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
	unsigned char* m_JumpBlockBuffer; //��Ծ�赲��
	char* m_IslandBuffer;
	int m_IsLandNumber; //�µ�����

	char* m_IslandBuffer2; //����µ�
	int m_IsLandNumber2; //����µ�����

//////////////////////////////////////////////////////////////////////////


};
