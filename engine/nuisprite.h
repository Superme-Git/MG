#ifndef __Nuclear_ISPRITE_H__
#define __Nuclear_ISPRITE_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <list>
#include <string>
#include "common/nuxptypes.h"
#include "nuiselectableobj.h"
#include "nuieffect.h"

class cPartParam;

namespace astar
{
	struct Point;
	typedef std::list<Point> Path;	//为了不include "astar\apath.hpp"，只好手动保持一致了。。。。。。
}

namespace Nuclear
{

	// 可以设置的装备组件
	// sprite 目前分模型(model)配置
	// 模型列表在 /model/sprites.xml 中
	// 每个模型在分别定义自己的每层配置，比如模型 male 的层配置在 /model/male/layerdef.xml 中
	// 引擎从/sprite/[modelname]/layerdef.xml 加载配置，然后层数和每层意义可以通过IQuery相关接口获得
	// 最多支持 XPEC_MAXCOUNT 层
	// 之前的单层精灵暂时不再对外暴露，仅对内使用

	// by wuweixin
	// 精灵使用两套坐标系, 世界坐标对应实际地图上的像素坐标
	// 逻辑坐标对应可达图、服务器逻辑格子, 接口中A*相关以及接口名显式带logic的, 坐标均使用逻辑坐标
	// 其余均为世界坐标
	// 两种坐标系存在一个换算关系
	// 世界坐标.x ==  逻辑坐标.x * N
	// 世界坐标.y ==  逻辑坐标.y
	// N = [1, 2]
	// 目前转换只在sprite内部实现使用, 以后有需要再考虑暴露坐标转换接口

	enum SPRITE_COMPONENT
	{
		XPSC_ALL = -1, // 特殊值，不计在拼层之内
		XPSC_MAXCOUNT = 12,
	};

	// 用于回调接口 IApp::OnUpdateSpriteAction(ISprite * sprite, XPUSAT_TYPE type)
	enum XPUSA_TYPE
	{
		XPUSAT_MOVESTATE,			// 移动状态动作更新回调
		XPUSAT_MOVEINFLEXION,		// 移动经过路径拐点时的回调
		XPUSAT_PLAYACTIONFINISH,	// PlayAction  结束回调
		XPUSAT_DEFAULTACTIONFINISH,	// DefaultAction 结束回调
		XPUSAT_TELEPORTEND			// 瞬间移动结束回调
	};

	enum XPSPREFF_FLAG
	{
		XPSPREFFFLAG_BIND_EFF_POS = 1 << 0,		//精灵的特效挂点绑定点，就是relpos加上精灵的GetEffectPos，与XPSPREFFFLAG_BIND_WEAPON冲突
		XPSPREFFFLAG_BIND_AUTO = 1 << 1,		//根据特效的GetEffectBindType来自动决定是否打上XPSPREFFFLAG_BIND_EFF_POS flag；如果是XPEBT_EFFECT_POS，那么打上；与XPSPREFFFLAG_BIND_WEAPON和XPSPREFFFLAG_BIND_EFF_POS都冲突
		XPSPREFFFLAG_BIND_WEAPON = 1 << 2,		//默认是武器绑定点0
		XPSPREFFFLAG_BIND_WEAPON_0 = 1 << 3,	//武器绑定点1：会随动作变化而改变（每一帧）。此时忽略relpos。与XPSPREFFFLAG_BIND_EFF_POS，XPSPREFFFLAG_SELF_REF，XPSPREFFFLAG_BIND_WEAPON_1冲突
		XPSPREFFFLAG_BIND_WEAPON_1 = 1 << 4,	//武器绑定点2：会随动作变化而改变（每一帧）。此时忽略relpos。与XPSPREFFFLAG_BIND_EFF_POS，XPSPREFFFLAG_SELF_REF，XPSPREFFFLAG_BIND_WEAPON_0冲突
		XPSPREFFFLAG_LINKING_FRAME = 1 << 5,	//只对ani特效有用，让特效的帧和角色的帧强制同步
		XPSPREFFFLAG_SELF_REF = 1 << 6,			//relpos会根据角色当前方向重新计算位置，与XPSPREFFFLAG_BIND_EFF_POS冲突，在某些特效是XPEBT_EFFECT_POS的情况下，与XPSPREFFFLAG_BIND_AUTO冲突
		XPSPREFFFLAG_ASYNC = 1 << 7,			//异步读取资源
		XPSPREFFFLAG_NO_FOLLOW_SCALE = 1 << 8,	//精灵缩放的时候特效不会跟着缩放
		XPSPREFFFLAG_NO_SOUND = 1 << 9,			//不播放特效里面的声音
		XPSPREFFFLAG_DRAW_ON_TOP = 1 << 10,		//带有这个标记的特效在最上层播放
		XPSPREFFFLAG_DRAW_BEFORE_SPRITE = 1<<11,//带有这个标记的特效比角色先绘制,和XPSPREFFFLAG_DRAW_ON_TOP有冲突
		XPSPREFFFLAG_DRAW_ON_BOTTOM = 1 << 12,//带有这个标记的特效比角色先绘制,并且是在所有角色之前先绘制 和XPSPREFFFLAG_DRAW_ON_TOP 及 XPSPREFFFLAG_DRAW_BEFORE_SPRITE 有冲突
		XPSPREFFFLAG_BIND_EFF_POS_WITH_DIR = 1 << 13,  // 与 XPSPREFFFLAG_BIND_EFF_POS 相同，但会跟随精灵的朝向
	};

	enum XPSPRITE_ACTION_LOAD_TYPE
	{
		XPSALT_SYNC = 0,	//同步加载
		XPSALT_ASYNC,		//异步加载
		XPSALT_BASE_ASYNC,	//异步加载，当加载完第一个方向第一帧的时候就画出来
	};

	class ISprite;
	//2D精灵的Notify，在方法里面带有ISprite*参数是使到这个Notify更灵活
	//这个Notify在丢失设备、最小化的时候都会起作用
	class SpriteNotify
	{
	public:
		virtual void OnPlayFrame(ISprite* pSprite, const std::wstring &actionName, int frame) = 0;
	};

	//3D精灵的Notify，在方法里面带有ISprite*参数是使到这个Notify更灵活
	//这个Notify在丢失设备、最小化的时候都会起作用
	//当特别卡的时候，可能一帧之内会攒多个回调，所以是个vector
	//需要注意的是，通告的位置如果在动作的最后几帧，有可能触发不到回调
	class SpriteEventNotify
	{
	public:
		virtual void OnEvents(ISprite* pSprite, const std::vector<int>& eventIDs) = 0;
	};

	//渲染Sprite名称的回调
	class ISprite : virtual public ISelectableObj
	{
	public:

		ISprite(){}
		virtual ~ISprite(){}

		typedef void(*DrawName_CallBack)(ISprite* pSprite);

		// 注册/取消Notify，需要注意的是，不能在这个Notify的回调内部取消Notify。
		// 如果很卡以至于跳过了一帧，而这帧刚好有个Notify，那么这个Notify也会被跳过一次
		// 这两个接口给2D精灵用
		virtual void RegisterFrameNotify(SpriteNotify* pNotify, const std::wstring &actionName, int frame) = 0;
		virtual void DeregisterFrameNotify(SpriteNotify* pNotify, const std::wstring &actionName, int frame) = 0;

		// 注册渲染名称的回调函数
		virtual void RegisterDrawNameCB(DrawName_CallBack drawNameCB) = 0;
		virtual void DeregisterDrawNameCB() = 0;

		virtual void EnableStepSound(bool b) = 0;
		virtual bool IsStepSoundEnable() const = 0;

		//编辑器使用
		virtual void ClearAllNotify() = 0;

		virtual const std::wstring &GetSoundActorName() const = 0;

		//编辑器使用，效率可能会很低
		virtual void DumpAllActionNames(std::vector<std::wstring> &actions) const = 0;

		// 飞行
		virtual void SetScale(float scale) = 0;
		virtual float GetScale () const = 0;

		// 设置位置。location 世界像素坐标。
		virtual void SetLocation(const NuclearLocation& location) = 0;
		// 得到位置.
		virtual NuclearLocation GetLocation() const = 0;

		// 设置和得到方向。direction 世界坐标方向
		virtual void SetDirection( NuclearDirection direction ) = 0;
		virtual NuclearDirection GetDirection() const = 0;
		virtual NuclearFPoint GetVectorDirection() const = 0;

		// 这个自动控制，需要涉水、冲锋的时候自动用RenderTarget绘制
		void UseRenderTarget(bool b) {}
		bool IsUseRenderTarget() const { return false; }

		// 设置是否可见
		virtual void SetVisible(bool v) = 0; // default true
		virtual bool IsVisiable() const = 0;

		// 设置是否可见实体（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetEntityVisible(bool v) = 0; // default true
		virtual bool IsEntityVisible() const = 0;

		// 设置是否可见名字（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetTitleVisible(bool v) = 0; // default true
		virtual bool IsTitleVisible() const = 0;

		// 设置Alpha
		virtual void SetAlpha(unsigned char a) = 0; //默认是255
		virtual unsigned char GetAlpha() const = 0; //默认是255

		bool IsCoveredAlpha() const { return true; }
		void SetCoveredAlpha(bool b) {}

		//是否实体，例如建筑。默认关闭
		//在XPSTT_ELEMENT_TRANSLUCENT下开启这个会导致精灵绘制两遍，有一定的性能下降。
		//在XPSTT_SPRITE_TRANSLUCENT下开启这会导致这个精灵少绘制一遍，有一定的性能提升。- -b
		virtual bool IsSolidMask() const = 0;
		virtual void SetSolidMask(bool b) = 0;

		//3D角色是否每帧更新，主角以及队伍中角色应该每帧更新，默认false
		virtual bool IsUpdateEveryFrame() const = 0;
		virtual void SetUpdateEveryFrame(bool b) = 0;

		// 新版头顶文字，用UI来画
		virtual void SetTitle(EntitativeTitleHandle handle, NuclearTitleAlign align = XPTA_DEFAULT) = 0;
		virtual EntitativeTitleHandle GetTitle() = 0;
		virtual EntitativeTitleHandle UnsetTitle() = 0;
		virtual void SetTitleSize(int width, int height) = 0;
		virtual int GetTitleWidth() const = 0;
		virtual int GetTitleHeight() const = 0;

		// 如果是L""，代表在原来的在原点基础上偏移的方法
		virtual void SetTitleBindSocket(const std::wstring &socket) = 0;
		virtual std::wstring GetTitleBindSocket() const = 0;

		// 获得冒泡物件（例如伤害文字）的初始Y偏移，向上为正
		virtual int GetBubbleItemOffset() const = 0;

		// 获得被击特效挂点的位置（相对像素坐标）
		virtual const NuclearPoint& GetEffectPos() const = 0;

		// 设置精灵模型
		virtual bool SetModel(const std::wstring &modelname, bool async) = 0;
		bool SetModel(const std::wstring &modelname) { return SetModel(modelname, false); }

		virtual void SetActive(bool active) = 0;
		// 返回精灵模型的名字
		virtual const std::wstring& GetModelName() const = 0;

		// 设置精灵的装备组件
		virtual bool SetComponent(int scid, const std::wstring& resource, NuclearColor color = 0xffffffff/*XRGB*/, std::wstring wsLayerName = L"") = 0;
		virtual bool GetComponent(int scid, std::wstring& resource) = 0;
		virtual void* GetComponent(int scid) = 0;
		virtual bool GetComponentColor(int scid, NuclearColor &color) = 0;
		// 设置坐骑的名字（当骑马的时候调用，下马的时候设上L""）
		virtual void SetRideName(const std::wstring& name) = 0;
		// 精灵特效层（如果有的话）的Alpha上下限[0~1]
		virtual void SetMinMaxEffectAlpha(float minalpha, float maxalpha) = 0;
		virtual float GetMinEffectAlpha() const = 0;
		virtual float GetMaxEffectAlpha() const = 0;

		// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
		virtual NuclearPrefetchResult PrefetchAction(const std::wstring &action_name) = 0;

		// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
		virtual void HoldAction(const std::wstring &action_name) = 0;
		// 只有Hold住的Action才需要Release
		virtual void ReleaseAction(const std::wstring &action_name) = 0;

		virtual void SetHoldLastFrame(bool bHoldLastFrame) = 0;

		// fScaleForTotalTime表示这个动作总时间的缩放比例
		// 默认动作，每个精灵必须有一个默认动作
		virtual bool SetDefaultAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame) = 0;
		virtual const std::wstring &GetDefaultAction() const = 0;
		// 临时动作，每个精灵有0~1个， 临时动作播放完毕之后自动切回默认动作, action_name 传空字符串取消临时动作
		// 返回true代表真的播放了，返回false代表播的其实是上一个动作，目前的还没有下载好呢
		virtual bool PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime) = 0;
		virtual const std::wstring &GetCurActionName() const = 0;

		// 单位是秒，如果没有这个动作，返回false
		virtual bool GetActionTimeByName(const std::wstring& action_name, float &time) = 0;

		// 当前动作的播到哪帧（此数据在UpdateAction的时候更新，在UpdateAction前为上一帧的数据，UpdateAction后为本帧的数据）
		virtual int GetCurrentFrame() const = 0;
		// 当前动作的总帧数
		virtual int GetTotalFrame() const = 0;
		
		// 绑在精灵上播放的效果

		//设置绑定文件，当设置成L""的时候为取消
		//id只能是0或者1，对应不同的手
		virtual bool SetBindFile(int id, const std::wstring &bindFile) = 0;
		virtual std::wstring GetBindFile(int id) = 0;

		// 设置和取消持续效果，需要手工取消，个数无上限
		// bindType精灵特殊绑定点
		virtual IEffect* SetDurativeEffect(const std::wstring &effect_name, const NuclearPoint& relpos, unsigned int flag) = 0;
		virtual IEffect* SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, const NuclearPoint& relpos, unsigned int flag) = 0;
		virtual void RemoveDurativeEffect(IEffect* pEffect) = 0;

		// 修改绑在精灵身上的特效的位置
		virtual bool ChangeEffectOffset(IEffect* pEffect, const NuclearPoint &relpos) = 0;

		// 临时效果，播放完毕自动取消，个数无上限, times是播放次数, 如果有声音，soundtype就是其声音类型
		virtual IEffect* PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype, int TimeMode = 0, int iRot = 0) = 0;
		//只能给3D精灵和3D特效用
		virtual IEffect* PlayEffect(const std::wstring &effect_name, const NuclearPoint& relpos, int times, const std::wstring &hostname, bool async) = 0;

		virtual void EnableShadow(bool b) = 0;
		virtual bool IsEnableShadow() = 0;
		
		// 精灵瞬移拖残影
		//telTime是移动的时间，blurTime是残影持续的时间，总的时间是telTime+blurTime，在telTime之后就已经移动到位
		virtual void TeleportWithBlur(const NuclearLocation &point, int telTime, int blurTime) = 0;
		virtual void StopTeleport(const NuclearLocation &point) = 0;

		// 精灵支持8、4、2、1四种方向
		virtual void SetTurnDir(int iDir) = 0;	// 默认是8方向
		virtual int  GetTurnDir() const = 0;

		// 是否高亮
		virtual void SetHighlight(bool b) = 0;
		virtual bool IsHighlight() const = 0;

		// 检查能否选择ani的时候的模式
		virtual void SetMouseTestMode(Nuclear_Mouse_TestMode m) = 0;
		virtual Nuclear_Mouse_TestMode GetMouseTestMode() const = 0;

		//动作声音
		virtual void SetActionSoundPriority(short priority) = 0;
		virtual short GetActionSoundPriority() const = 0;

		// 精灵移动速度，单位：像素/毫秒。初始为 0.160
		virtual void  SetMoveSpeed(float speed) = 0;
		virtual float GetMoveSpeed() const = 0;

		// 停止移动。清除未走完的路径。
		virtual void StopMove() = 0;
		// 是否移动中。还有路需要走，没有到达目的地。
		virtual bool IsMoving() const = 0;
		// 设置暂停状态。这个个完全独立的控制状态，与是否真的在移动中( IsMoving )无关。
		//       true  精灵将不再移动，但保持所有现有的移动状态，如，当前路径和尾迹。
		//             新的移动命令（尾迹）全部在这种状态下，只会被记录。
		//       false 精灵恢复到移动状态。
		virtual void SetMoveSuspended(bool moveSuspended) = 0;
		// 是否暂停状态。
		virtual bool IsMoveSuspended() const = 0;

		// 寻找到target的路径，并且移动过去。如果寻路失败，保持当前路径不变。
		//
		// targetX,targetY
		//       目标坐标。世界像素坐标。
		// range
		//       [!注意] 虽然targetX targetY都是世界坐标, 但range是已逻辑坐标为标准的, 比较诡异
		//       与目标的直线可达距离小于range时，停止移动。
		//       由于要求停止点直线可达目标，在迷宫中，实际停止距离里目标比range少。
		//       range 主要用于处理战斗，此时目标一般也处于移动状态。由于网络延迟，
		//       目标的位置本来不是准确的。延迟问题造成的range不准确比迷宫造成的特殊路径会更频繁。
		//       先忽略迷宫下的特殊情况。根据实际效果再调整。
		// size
		//       指定查找范围：精灵当前坐标点(GetLocation)为中心，size宽高的rect。
		//       范围之外都看作不可达。传入NULL表示搜索整个地图。坐标为像素坐标。
		// @return
		//       0 寻路成功，精灵进入移动状态。
		//       1 寻路失败。精灵状态不变。
		//       2 寻路成功，精灵进入站立状态。此时，目标已经在范围内。
		virtual void MoveTo(int targetX, int targetY, int range, const CSIZE * size, bool straight = false) = 0;
		void MoveTo(const NuclearLocation & target, int range, const CSIZE * size, bool straight = false) { this->MoveTo(target.x, target.y, range, size, straight); }

		// 把trail接到当前路径后面，让精灵沿尾迹路径中的点移动。如果trail和当前路径无法无缝连接。
		// 当前路径会被清除，并把精灵重新定位到trail.front()。
		//	  trail 尾迹路径。逻辑坐标。[注意] trail会被内部修改，调用之后不再有意义。		
		virtual void  StartShake() = 0;	// 开始抖动 
		virtual void  StopShake() = 0;	// 结束抖动
		virtual void SetCurveMove(bool b) = 0;//移动中是否需要走弧形

		virtual void SetFlyScaleTgt(int iFlyScaleTgt) = 0;//因飞行而引起的缩放目标值
		virtual void SetFlyScaleCur(int iFlyScaleCur) = 0;
		virtual int GetFlyScaleCur() = 0;
		virtual void SetFlyScaleStep(int iFlyScaleStep) = 0;
		virtual int GetFlyScaleStep() = 0;
		virtual void SetFlyOffsetYTgt(int iFlyOffsetYTgt) = 0;//因飞行而引起的Y轴偏移(实际上是模拟Z轴)目标值
		virtual void SetFlyOffsetYCur(int iFlyOffsetYCur) = 0;
		virtual int GetFlyOffsetYCur() = 0;
		virtual void SetFlyOffsetYStep(int iFlyOffsetYStep) = 0;
		virtual int GetFlyOffsetYStep() = 0;

		virtual void SetUseWorldScale(int iUseWorldScale) = 0;
		virtual int GetUseWorldScale() = 0;

		virtual void MoveTo(astar::Path & trail) = 0;

		// 获取当前path的一个拷贝, 在使用MoveTo后调用可得到即将要走的路径(逻辑坐标)
		virtual void GetPath(astar::Path & path) = 0;

		// 是否记录尾迹。一般只有主角需要。默认为false。
		virtual void KeepMoveTrail(bool keep) = 0;
		virtual bool IsKeepMoveTrail() const = 0;

		// 把当前路径尾迹取走并添加到trail后。通过返回值判断是否添加了尾迹。
		// 一般都用传入空trail获取尾迹。如果多次收集，内部不检查是否可以无缝拼接。
		virtual bool TakeMoveTrail(astar::Path & trail /*out*/) = 0;

		// 设置精灵可达图掩码。为0，忽略障碍。
		virtual void SetMoveMask(unsigned int mask) = 0;

		// 无损设置精灵位置方向，一般用于服务器发现移动错误重至客户端位置。否则后续的路径验证会持续失败。
		//  [注意] SetLocationDirection(const astar::Point &)使用逻辑坐标
		//         世界坐标设置请使用SetLocation()、SetDirection()
		virtual void SetLocationDirection(const astar::Point &) = 0;

		// 取得精灵的逻辑坐标
		virtual NuclearLocation GetLogicLocation() const = 0;

		// 转向目标点
		virtual void SetDirection(int targetx, int targety) = 0;
		void SetDirection(const Nuclear::NuclearPoint & target) { SetDirection(target.x, target.y); }

		// [!注意] 虽然target是世界坐标, 但range是已逻辑坐标为标准的, 比较诡异
		virtual bool InAttackRange( const Nuclear::NuclearLocation & target, int range, unsigned int mask) = 0;
        
		virtual void SetComponentAniReleaseFlag(Nuclear::NuclearPaniReleaseFlag flag) = 0;

		virtual void SetPartParam(int iLayerIndex, int iPartIndex, std::vector<float> vParam) = 0;
		virtual std::vector<float> GetPartParam(int iLayerIndex, int iPartIndex) = 0;

		virtual int GetLayerCount() = 0;
		virtual int GetLayerIndexByName(std::wstring wsName) = 0;
		virtual std::wstring GetLayerNameByIndex(int iIndex) = 0;
		virtual bool IsPartLayer(int iIndex) = 0;

		virtual void SetDyePartIndex(int part, int index) = 0;
		virtual bool GetDyeEnable() = 0;
		virtual int GetDyePartCount() = 0;
		virtual int GetDyeProjCount(int part) = 0;

		virtual void* GetUserData() const = 0;
		virtual void SetUserData(void* pData) = 0;
	};
}

#endif