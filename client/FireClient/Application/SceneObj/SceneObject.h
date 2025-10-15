#pragma once
#include "SceneCommon.h"
#include "BuffCommon.h"
#include "nuisprite.h"
#include "renderer/nuifontmanager.h"

#define DEFAULT_HPBAR_HEIGHT	120
#define DEFAULT_HPBAR_HEIGHT_LONG 110

typedef std::list<int>	BuffIDList;

typedef std::map<int, Nuclear::IEffect*>	BuffEffect;

class SceneObject
{
public:
	SceneObject();
	//SceneObject(DWORD id);
	virtual ~SceneObject();

public:
	virtual bool IsInHighLevel();
	virtual void SetInHighLevel(bool b);

	bool IsFemale();
	bool IsMale();
	bool IsCharacterModel() { return m_iModelID < 1000 || m_iModelID>1000000; }//是否是角色模型

	virtual int	 GetModelID(){ return m_iModelID; }
	virtual void SetHighlightEnable(bool enable){ m_bHighLightEnable = enable; }
	virtual void SetAlpha(unsigned char a);
	virtual unsigned char GetAlpha(){ return m_pSprite != NULL ? m_pSprite->GetAlpha() : false; }
	// 名字
	virtual std::wstring GetName(){ return L""; }
	virtual void SetNameColour(const DWORD& colour) { m_NameColour = colour; }
	virtual DWORD GetNameColour() { return m_NameColour; }
	virtual void SetNameTextureHandle(Nuclear::TextBlockHandle handle);
	Nuclear::TextBlockHandle GetNameTextureHandle() { return m_NameTextureHandle; }
	Nuclear::ITextBlock* GetNameBlock() { return m_pNameTextBlock; }
	virtual void UpdatNameTexture(bool bReleaseOld = false); //bReleaseOld是否释放旧纹理
	virtual void ReleaseNameTexture();
	virtual void ReloadNameTexture();
	DWORD   GetNameShadowColor() { return m_NameShadowColor; }
	void SetNameShadowColor(DWORD color) { m_NameShadowColor = color; }

	DWORD GetMouseOverNameColor() { return m_MouseOverNameColor; }
	DWORD GetMouseOverShadowColor() { return m_MouseOverShadowColor; }

	void SetMouseOverNameColor(DWORD color) { m_MouseOverNameColor = color; }
	void SetMouseOverShadowColor(DWORD color) { m_MouseOverShadowColor = color; }
	// 可见
	virtual void SetVisible(bool v);
	virtual bool IsVisiable() const { return m_bVisible; }
	// ID相关
	virtual int64_t GetID(){ return m_dwID; }
	virtual void  SetID(int64_t id){ m_dwID = id; }
	virtual bool  FitID(int64_t id){ return id == m_dwID; }
	// 单位属性	npc，人物，战斗单位，宠物
	virtual eSceneObjType GetSceneObjectType(){ return m_eSceneType; }
	virtual bool IsSceneObjectType(eSceneObjType type){ return m_eSceneType == type; }
	virtual void UpdateAction(Nuclear::XPUSA_TYPE type);
	virtual bool PlayAction(eActionType actionType, float freq = g_defaultActionFrequence);
	virtual void SetDefaultAction(eActionType actiontype, bool holdlastframe = false, float freq = g_defaultActionFrequence);
	virtual void SetDefaultActionWithoutLogic(eActionType actiontype, bool holdlastframe = false, float freq = g_defaultActionFrequence);
	virtual void Run(int now, int delta);
	virtual bool OnSetCursor();
	// 精灵相关
	virtual Nuclear::ISprite* GetSprite(){ return m_pSprite; }
	virtual bool FitSprite(Nuclear::ISprite* sprite){ return sprite == m_pSprite; }

	virtual void Draw(int now);

	virtual void SetHighlight(bool highlight);

	virtual bool CanBeAttack(){ return false; }
	virtual bool CanBeGiven(){ return false; }
	virtual bool CanJoinTeam(){ return false; }
	virtual bool CanTradeWith(){ return false; }
	virtual bool CanGiveFlower(){ return false; }
	virtual bool CanGiveRose() { return false; }
	virtual bool CanBeUseSkill(int skillid){ return false; }
	virtual bool CanPickedUp(){ return false; }
	virtual bool CanBeVisited(){ return false; }
	virtual int CanBeTriggered(){ return -1; }

	virtual bool CanFBAttack(){ return false; }
	virtual bool CanFBRepair(){ return false; }

	// 移动
	virtual bool MoveTo(Nuclear::NuclearLocation ptMoveTarget, bool straight = false);	//参数为	逻辑坐标
	virtual void StopMove();
	virtual bool IsMoving(){ return m_pSprite != NULL ? m_pSprite->IsMoving() : false; }
	virtual void GetPath(astar::Path & path){ if (m_pSprite) m_pSprite->GetPath(path); }

	// 位置
	virtual void SetLogicLocation(const Nuclear::NuclearLocation& location);
	virtual void SetLogicLocation(int x, int y);
	virtual void SetLocation(const Nuclear::NuclearLocation& location);
	virtual Nuclear::NuclearLocation GetLogicLocation();
	virtual Nuclear::NuclearLocation GetLocation();
	virtual Nuclear::NuclearLocation GetGridLocation();
	virtual Nuclear::NuclearDirection GetDirection() const { return m_pSprite != NULL ? m_pSprite->GetDirection() : Nuclear::XPDIR_TOP; }
	virtual Nuclear::NuclearFPoint GetVectorDirection() const { return m_pSprite != NULL ? m_pSprite->GetVectorDirection() : Nuclear::NuclearFPoint(0.0f, 0.0f); }
	virtual void SetDirection(int tx, int ty);
	virtual void SetDirection(Nuclear::NuclearDirection dir);

	virtual void TurnTo(SceneObject* pSceneObj);

	virtual std::wstring GetActionName(eActionType type);

	virtual std::wstring GetComponentActionName(eActionType type);

	void SavePrevDirection();
	void LoadPrevDirection();

	void SetShadowScale(const float& scale);
	virtual bool NewSpriteModel(const std::wstring& modelname, bool bhighlevel = false);
	virtual bool NewSpriteModel(const int& modelid, bool bhighlevel = false);
	virtual bool NewSpriteModel(const int& modelid, const std::wstring& modelname, const std::wstring hair, const std::wstring head, const std::wstring body);//设置模型,指定模型名字(编辑器专用)
	virtual void SetDyePartIndex(int part, int index);

	virtual void ChangeSpriteModel(const int& modelid);//换造型
	void DeleteSpriteModel();
	virtual void SetComponent(int scid, const std::wstring& resource, Nuclear::NuclearColor color = 0xffffffff/*XRGB*/) { if (m_pSprite) m_pSprite->SetComponent(scid, resource, color); }

	//魔法相关
	virtual Nuclear::IEffect* PlayEffect(const std::wstring &effect_name, int dx, int dy, int times = 1, bool selfRef = true, bool bnofollowscale = false, bool playsound = true, int TopMidBottom = 0, int TimeMode = 0, int iRot = 0);//1:Top,0:Mid,-1:Bottom //0:LoopNum,1:Time
	virtual Nuclear::IEffect* PlayEffect(const std::wstring &effect_name, bool basync = false, bool playsound = true, bool linkframe = false, int times = 1, int TopMidBottom = 0, int TimeMode = 0, int iRot = 0);//1:Top,0:Mid,-1:Bottom
	//播放3D特效
	Nuclear::IEffect* Play3DEffect(const std::wstring &effect_name, const std::wstring &hostname, bool async = false, int times = 1, int x = 0, int y = 0);

	virtual Nuclear::IEffect* SetDurativeEffect(const std::wstring &effect_name, int bindType, int dx, int dy, bool selfRef, bool basync = true, bool bnofollowscale = false, bool alwayontop = false, bool underSprite = false, bool isShadow = false);

	virtual void RemoveDurativeEffect(Nuclear::IEffect* pEffect) { if (m_pSprite) m_pSprite->RemoveDurativeEffect(pEffect); }

	//设置Scale
	virtual void SetScale(float scale) { if (m_pSprite) m_pSprite->SetScale(scale); }
	virtual float GetScale() { return m_pSprite != NULL ? m_pSprite->GetScale() : 1.0f; }

	virtual int GetShapeID() { return 0; }

	void SetSpriteReleaseFlag(Nuclear::NuclearPaniReleaseFlag flag){ if (m_pSprite) m_pSprite->SetComponentAniReleaseFlag(flag); };

	void SetActionType(eActionType e){ m_actionType = e; }
	eActionType GetActionType(){ return m_actionType; }
	bool IsAction(eActionType e){ return m_actionType == e; }

	virtual Nuclear::NuclearLocation GetScreenPosition();
	void ShowHideAllTalk(bool bvisible);							//进战斗时隐藏所有头顶talk

	//头顶聊天泡泡相关
	void AddClientTalk(const std::wstring& strTalk, DWORD color = 0xFFFFFFFF, bool bCheckShied = true);
	void AddTalk(const std::wstring& strTalk, bool bCheckShied = true);  //接收外部消息的接口
	bool HandleRemoveTalkBox(const CEGUI::EventArgs& e);

	virtual void StandRandomAction(int now, int delta);

	long GetXPos(){ return GetLocation().x; }
	long GetYPos(){ return GetLocation().y; }

	void SetPartParam(int iLayerIndex, int iPartIndex, std::vector<float> vParam);
	std::vector<float> GetPartParam(int iLayerIndex, int iPartIndex);
	void SetPartParamIndex(int iLayerIndex, int iPartIndex, int iColorIndex);
	int GetPartParamIndex(int iLayerIndex, int iPartIndex);

	virtual void SetMoveMask(eMoveMask movemask);
	virtual eMoveMask GetMoveMask(){ return m_eMoveMask; }

	int GetAutoMove();
	void SetAutoMove(int iAutoMove);

	static inline bool sComparewithSceneObjType(SceneObject* p1, SceneObject* p2)
	{
		if (p1->GetSceneObjectType() < p2->GetSceneObjectType())			return true;
		else if (p1->GetSceneObjectType() == p2->GetSceneObjectType())		return p1->GetLocation().y > p2->GetLocation().y;
		return false;
	}

protected:

	void UpdateShadowInfo(int shadowsize);
	void AddTalk(const std::wstring& strTalk, float V_Offset, bool bCheckShied = true);  //添加泡泡解析文本 V_Offset为上一个泡泡屏幕Y坐标
	void OnScreenLocationChange(int offset_X, int offset_Y);  //角色屏幕画的位置改变,x,y分别为相对上次改变的量
	virtual int GetJumpYOffset(){ return 0; }

protected:

	struct sTalkUnit
	{
		CEGUI::RichEditbox* pEditBox;
		float fDestYPos;//目的y坐标

		sTalkUnit()
		{
			pEditBox = NULL;
			fDestYPos = 0.0f;
		}
	};

	std::vector<sTalkUnit> m_listTalks;
	std::vector<CEGUI::RichEditbox*> m_listEditBox;
	Nuclear::NuclearLocation m_LastLocation;

	std::vector<std::vector<int> >m_vPartColorIndex;

	eSceneObjType	m_eSceneType;	//场景单位类型，NPC还是人物还是宠物还是战斗单位
	eActionType		m_actionType;
	UINT				m_titleIndex;
	DWORD				m_NameColour;  //名字颜色值
	DWORD               m_NameShadowColor; //名字的阴影颜色
	Nuclear::TextBlockHandle  m_NameTextureHandle;  //名字纹理句柄
	Nuclear::ITextBlock* m_pNameTextBlock; //名字的TextBlock

	DWORD               m_MouseOverNameColor; //鼠标经过时的名字颜色
	DWORD               m_MouseOverShadowColor; //鼠标经过时的名字颜色阴影颜色

	int				m_iModelID;		//造型ID

	int				m_iAutoMove;

private:
	int64_t			m_dwID;			//ID

	bool			m_bVisible;		//是否可见

	bool			m_bHighLightEnable;

	bool			m_bInHighLevel;		//精灵是否在高层

	int				m_iRandomActionTime;

	eMoveMask		m_eMoveMask;

	int				m_PrevDir;

	Nuclear::ISprite*	m_pSprite;

	Nuclear::IEffect*	m_pSpriteShadow;	//精灵的影子
};

