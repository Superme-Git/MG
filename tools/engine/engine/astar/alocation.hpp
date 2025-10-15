
#pragma once

/*
速度验证分析

玩家持续移动。向服务器报告了两次尾迹。
    客户端 : 0________trail0__________1_________trail1_________2

不考虑网络延迟和其他时间消耗。
    服务器 : 0________trail0__________1(time0)
                                      1_________trail1_________2(time1)

  time 为服务器收到尾迹的服务器本地时间。
    delta = (time1 - trail1.length / player.speed) - time0
	当 delta = 0  玩家速度=服务器速度
	当 delta > 0  玩家速度<服务器速度
	当 delta < 0  玩家速度>服务器速度。

加速作弊定义
  首先定义： delta < eMinDelta 这有个问题：此时玩家可以把delta控制在eMinDelta之上，就能持续加速。
  修改加速作弊为: deltaTotal < eMinDeltaTotal (deltaTotal 为每次delta的累积值)

deltaTotal 性质分析

  [先不考虑玩家作弊]

  其他正常的会影响 delta 的因素，如：网络延迟抖动，客户端报告的时间间隔，
  计算误差，操作系统，程序执行状态变化，也许还有天 气。所有这些会造成服务器
  滞后收到trail，这些统称为delay。delay肯定是个>0的数。如果delay恒定不变，
  那么所有的到达时间都顺延，除了第一个delta，其他delta仍恒0。此时deltaTotal等于delay。

  实际上delay会抖动，这些抖动定性为随机的（不证明，也不知道怎么证明，暂忽略分布特性）
  那么 deltaTotal 将在0附近变动，总分布偏向0。
  定义 deltaTotal 的值域为 [ eMinDeltaTotal, eMaxDeltaTotal ]

  deltaTotal 的上限：当玩家停止移动，一段时间以后才再动一次，此时delta接近停止的时间，如此下去。。。
  理论上可以一直累积，现实中这个上限值为服务器启动以来的时间。就看服务器多久维护一次了。

  deltaTotal 的下限：玩家停止会造成偏正向的delta，对 eMinDeltaTotal 不利，所以必须不停移动。累死他。
  不考虑第一个trail的delay，以后持续移动的deltaTotal实际上约等于0。这样eMinDeltaTotal实际上是接近delay的正数。

  [玩家作弊]
  deltaTotal 上限作弊法：先积累很大的正值，然后加速作弊。所以必须对上限做一个较小的限制，绝对不能超过这个值。

设置上限的问题
  设置上限比较郁闷。上限的问题实际上是站立状态引起的。如果服务器在每次玩家开始移动的时候。
  重新初始化deltaTotal，就没有这个问题。不过这样问题就会归结到移动停止状态的欺骗上面。
  为了防止作弊，服务器管理这个状态。玩家移动时要向服务器获得许可，停止时再向服务器报告。
  这和玩家本地寻路自主移动，然后发送尾迹的做法实际上是矛盾的。影响操作感。

eMinDeltaTotal, eMaxDeltaTotal 配置原则
  正常情况下，能覆盖多数 deltaTotal 的值，而又不会引起不必要的拉回。
  这个范围也决定了玩家一次性可作弊的时间，但由于无法累积，从稍长的时间来看，玩家还是无法作弊的。

速度变更的问题
  速度变更不同步也会影响计算，确定上下限时需要考虑这个因素。
  如，服务器速度比客户端速度先提高，这个时间差内的移动会造成偏正向的delta，最后受eMaxDeltaTotal限制。
  加速不同步，不会发生被拉回。减速时，有可能发生被拉回。

  ？加速时，先更改服务器，然后通知客户端。
  ？减速时，先通知客户端，然后更改。

  不过，要保证这个更改顺序，说实话还是有点烦。配置 eMinDeltaTotal, eMaxDeltaTotal 时，如果能直接把速度变更
  造成的误差包含在内，而这个误差又不会持续累积下去，逻辑的开发就可以不考虑这个因素，简化开发。

  总结：上面根本没有考虑采用严格同步两边速度的方案。这个更麻烦。
  总结：逻辑不考虑这个误差。让配置来搞定。

*/

#include "astar.hpp"
#include "atimer.hpp"

namespace astar
{

// 服务器用来管理玩家位置
class Location
{
	Point  location;   // 位置以及方向
	float  speed;      // 速度。像素/毫秒。

	int    lasttime;   // 服务器到达当前location的时间。毫秒。
	float  deltaTotal; // 毫秒。XXX ？？？如果这里使用整数，则计算中间值使用4舍5入。

public:
	// TODO 实际应用时，用构造函数初始化可能不方便。初始化方式待定。
	Location(const Point & _loc, float speed)
	{
		this->location = _loc;
		this->lasttime   = astar::Timer::tickcount();
		this->speed      = speed;
		this->deltaTotal = 0.f;
	}

	void  setSpeed(float speed)       { this->speed = speed; }
	float getSpeed() const            { return this->speed; }
	const Point & getLocation() const { return this->location; }

	enum TrailError
	{
		eTrailOk            = 0,  // ok
		eTrailSpeedError    = 1,  // speed error
		eTrailLengthError   = 2,  // length too long

		// 下面错误是 finder.scantrail 报告的
		eTrailSizeError     = -1, // trail.size 不合法。必须大于等于2。
		eTrailLocationError = -2, // 当前位置无法拼接。
		eTrailWalkError     = -3, // 经过了不可到达区域。
	};

	// 根据收到玩家尾迹修改服务器位置。发生错误时，把玩家拉回到上一次正确的位置。
	// 参数：
	//     finder astar 寻路
	//     trail  收到的玩家尾迹
	// 错误处理：
	//     当尾迹验证失败，需要发送协议重至玩家位置。
	TrailError setLocation(const PathFinder & finder, const Path & trail)
	{
		if (speed <= 0.f)
			return reset(eTrailSpeedError);

		float length;
		int result = finder.scantrailGrid(this->location, trail, length);
		if(-3 == result)
			result = finder.scantrailPixel(this->location, trail, length);
		if (0 != result)
			return reset(result);

		if (length > speed * 2000) // 配置：客户端尾迹发送间隔 + 最大网络间隔
			return reset(eTrailLengthError);

		int nowtime = astar::Timer::tickcount();
		deltaTotal += (nowtime - length / speed) - this->lasttime;

		// TODO 不作弊情况下，用实际测试观察deltaTotal的范围。
		const float eMinDeltaTotal = -100.f; // 没有定量分析,随便定义了一个。改成可配置。
		const float eMaxDeltaTotal = 1000.f; // 允许的一次作弊范围 eMaxDeltaTotal-eMinDeltaTotal

#if(0 && !defined(_MSC_VER))	// 测试deltaTotal的范围
		static float val_min = FLT_MAX;
		static float val_max = -FLT_MAX;
		if(deltaTotal < val_min)
		{
			val_min = deltaTotal;
			GNET::Log::log(LOG_INFO, "new min deltaTotal = %f", val_min);
		}
		if(deltaTotal > val_max)
		{
			val_max = deltaTotal;
			GNET::Log::log(LOG_INFO, "new max deltaTotal = %f", val_max);
		}
		GNET::Log::log(LOG_INFO, "trail length = %.3f, deltaTotal = %.3f, deltaTotalMin = %.3f, deltaTotalMax = %.3f", length, deltaTotal, val_min, val_max);
#endif

		if (deltaTotal < eMinDeltaTotal)
			return reset(eTrailSpeedError);

		if (deltaTotal > eMaxDeltaTotal)
			deltaTotal = eMaxDeltaTotal;

		this->location = trail.back();
		this->lasttime = nowtime;
		return eTrailOk;
	}

	// 直接定位，不需要验证路径和速度。
	void setLocation(const Point & location)
	{
		this->deltaTotal = 0.f;
		this->location = location;
		this->lasttime = astar::Timer::tickcount();
	}

private:
	// 发生错误需要重至服务器状态。此时需要发送协议重至玩家位置。
	TrailError reset(int error)
	{
		this->lasttime = astar::Timer::tickcount();
		this->deltaTotal = 0.f;
		return (TrailError)error;
	}
};

} // namespace astar
