#ifndef __Nuclear_REGIONMAP_PTRIGGERSOBJS_H
#define __Nuclear_REGIONMAP_PTRIGGERSOBJS_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"
#include "..\common\xmarshal.h"
#include "mapobjs.h"

//一个触发特效应该包括如下属性：(小括号是属性的单位和类型，大括号是这个属性的行为)
//1   触发区域(一个矩形，上下左右4个整数)。{触发的特效会在这个区域范围内播放，如果是精灵触发，那么精灵必须进入本区域才会触发}
//2.1 触发类型(是否精灵触发) {精灵触发的话，各个精灵各自独立触发，互不影响，触发点为精灵的位置；非精灵触发按照屏幕上的实际面积除以一个Tile的面积（也就是近似于包含tile的个数）四舍五入n，每隔d毫秒触发n次（产生n个随机数）}
//2.2 触发类型(是否游戏时间触发) {游戏时间触发为当游戏内时间在设定好的时间(第3点)内才触发}
//3   触发条件起始时间s和触发条件结束时间e(毫秒) {当2.2为非游戏时间触发时无效，s可以大于e，意味着是当天的晚上和第二天的早晨}
//4   触发概率f(浮点数，区间为[0,1])
//5   尝试触发的间隔d(毫秒)。{达到其它触发条件之后，每d毫秒产生一个在[0,1)的随机数pf，当pf<f的时候代表触发成功}
//6   触发最大时间间隔MaxD(毫秒)。{如果上次触发经过MaxD毫秒之后还没有触发，那么就忽略触发概率进行触发，MaxD==0代表关闭最大时间间隔功能}
//
//09.07.22补充：应该还有触发之后特效的层次，这个层次和策划那边的层次是一致的，分别是
//		a) 地表之上，精灵之下。（在物低之后，物中之前画）
//		b) 精灵和遮挡层，引擎根据底座自动判断遮挡。（在物中参与排序）
//		c) 精灵之上。（在物中之后，在物高之前）
//		d) 最上层。（在光照之后）
//
//09.08.03订正：
//2.1条修改为如下内容：
//	触发类型(是否精灵触发) 
//  {
//		触发方法如下：
//			先求出触发区域和视口的交集矩形rect，rect的面积为s；每个触发特效都会在运行期内有个时间b；
//			非精灵触发: 每个tile的面积为ts，每次update尝试触发[s*b/ts/d]=n个特效(d为触发间隔)，也就是产生n个随机数进一步决定是否触发，之后b要加上本次更新的delta值，再b-=n*d*ts/s（本来b应该是归0的，但是因为如果没有触发的话，delta值要累积到下次update）
//          精灵触发: 求出目前在rect里面的精灵的数量a，每次update尝试触发[a*b/d]=n个特效(d为触发间隔)，也就是产生n个随机数进一步决定是否触发，之后b要加上本次更新的delta值，再b-=n*d/a（本来b应该是归0的，但是因为如果没有触发的话，delta值要累积到下次update）
//6  条修改为如下内容：
//		触发最大时间间隔MaxD(毫秒)。{如果上次触发经过MaxD毫秒之后还没有触发，那么就忽略触发概率触发一次（一个触发特效只触发一次，不管屏幕多大里面的sprite有多少），MaxD==0代表关闭最大时间间隔功能}
//
//目前所有的触发均触发后就扔在地图上，只播放1遍。
//
//脚印不符合以上条件，无法用触发的方式做。
//目前关于脚印的思路是绑定一个粒子发射器在精灵身上，精灵每走一步就原地发射一个无速度无加速度的粒子。但是目前粒子没有方向。
//
//10.01.26补充：
//增加一种新的特效类型：TRIGGER_NONRANDOM_AND_SINGLE。此触发特效的行为如下：
//只在触发区域中心点触发，当触发之后在其特效生命周期内不再触发，不可与TRIGGER_SPRITE同用。
//以后可以考虑将NONRANDOM和SINGLE分开，主要是NONRANDOM和TRIGGER_SPRITE有冲突。

class CToolsMap;

namespace Nuclear
{
	namespace SubMap
	{
		enum TRIGGER_TYPE
		{
			TRIGGER_SPRITE = 1,
			TRIGGER_TIME = 1 << 1, //2,
			TRIGGER_NONRANDOM_AND_SINGLE = 1 << 2, //4,
			TRIGGER_DELETE = 1 << 15
		};

		typedef unsigned short TriggerInfoID;
		typedef unsigned short TriggerSmallID;

		typedef std::map<TriggerSmallID, CRECT> TriggerAreaMap;

		struct TriggerInfo
		{
			ObjMapKey effectMapKey;	//触发器要播放的特效
			unsigned short triggerTypeFlag;		//TRIGGER_TYPE
			ActionTypeFlag actionsFlag;			//包含的动作类型
			DWORD beginTime;
			DWORD endTime;
			float frequency;
			DWORD triDelta;
			DWORD maxDelta;
			CPOINT excursion;					//触发偏移，只用在Sprite触发里面
			XPEFFECT_LAYER layer;
		};

		typedef std::vector<TriggerInfo> TriggerInfoVet;

		struct TriggerObjInfo
		{
			std::wstring name;
			TriggerInfoVet triggerInfos;
			unsigned int nextSamllID;
			TriggerAreaMap areas;
			TriggerObjInfo () : nextSamllID(0) {}
		};

		union TriggerID
		{
			struct {
				TriggerInfoID infoID;
				TriggerSmallID areaID;
			};
			unsigned int id;
			TriggerID(TriggerInfoID _info, TriggerSmallID _area):infoID(_info), areaID(_area) {}
			TriggerID(unsigned int _id = 0):id(_id) {}
		};

		typedef std::map<TriggerInfoID, TriggerObjInfo> TriggerInfoMap;

		class PTriggerObjs
		{
			friend class CToolsMap;
		public:
			//成员数据们
		private:
			TriggerInfoMap m_mapTriggers;
			TriggerInfoID m_nNextId;

			//成员方法们
		private:

		public:
			///////////////////////////////初始化、构造与析构
			PTriggerObjs(void);
			~PTriggerObjs(void);

			///////////////////////////////输入输出
			XOStream& marshal(XOStream &os) const;
			const XIStream& unmarshal(const XIStream &os, unsigned int version);

			//////////////////////////////公共方法们
			TriggerInfoID AddAnTriggerInfoVet(const std::wstring &name, const TriggerInfoVet& obj);
			bool RemoveAnTriggerInfoVet(TriggerInfoID infoID);
			void ModifyAnTriggerInfoVet(TriggerInfoID infoID, const std::wstring &name, const TriggerInfoVet& obj);

			const TriggerInfoMap& GetTriggerInfos() const { return m_mapTriggers; }
			void SetTriggerInfos(const TriggerInfoMap& pmap) { m_mapTriggers = pmap; }

			TriggerID AddAnTriggerArea(TriggerInfoID infoID, const CRECT& area);
			void RemoveAnTriggerArea(TriggerID triggerID);
			void ModifyAnTriggerArea(TriggerID triggerID, const CRECT& newArea);
		};
	}
}
#endif