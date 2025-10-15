
#pragma once

/*
�ٶ���֤����

��ҳ����ƶ��������������������β����
    �ͻ��� : 0________trail0__________1_________trail1_________2

�����������ӳٺ�����ʱ�����ġ�
    ������ : 0________trail0__________1(time0)
                                      1_________trail1_________2(time1)

  time Ϊ�������յ�β���ķ���������ʱ�䡣
    delta = (time1 - trail1.length / player.speed) - time0
	�� delta = 0  ����ٶ�=�������ٶ�
	�� delta > 0  ����ٶ�<�������ٶ�
	�� delta < 0  ����ٶ�>�������ٶȡ�

�������׶���
  ���ȶ��壺 delta < eMinDelta ���и����⣺��ʱ��ҿ��԰�delta������eMinDelta֮�ϣ����ܳ������١�
  �޸ļ�������Ϊ: deltaTotal < eMinDeltaTotal (deltaTotal Ϊÿ��delta���ۻ�ֵ)

deltaTotal ���ʷ���

  [�Ȳ������������]

  ���������Ļ�Ӱ�� delta �����أ��磺�����ӳٶ������ͻ��˱����ʱ������
  ����������ϵͳ������ִ��״̬�仯��Ҳ������ ����������Щ����ɷ�����
  �ͺ��յ�trail����Щͳ��Ϊdelay��delay�϶��Ǹ�>0���������delay�㶨���䣬
  ��ô���еĵ���ʱ�䶼˳�ӣ����˵�һ��delta������delta�Ժ�0����ʱdeltaTotal����delay��

  ʵ����delay�ᶶ������Щ��������Ϊ����ģ���֤����Ҳ��֪����ô֤�����ݺ��Էֲ����ԣ�
  ��ô deltaTotal ����0�����䶯���ֲܷ�ƫ��0��
  ���� deltaTotal ��ֵ��Ϊ [ eMinDeltaTotal, eMaxDeltaTotal ]

  deltaTotal �����ޣ������ֹͣ�ƶ���һ��ʱ���Ժ���ٶ�һ�Σ���ʱdelta�ӽ�ֹͣ��ʱ�䣬�����ȥ������
  �����Ͽ���һֱ�ۻ�����ʵ���������ֵΪ����������������ʱ�䡣�Ϳ����������ά��һ���ˡ�

  deltaTotal �����ޣ����ֹͣ�����ƫ�����delta���� eMinDeltaTotal ���������Ա��벻ͣ�ƶ�����������
  �����ǵ�һ��trail��delay���Ժ�����ƶ���deltaTotalʵ����Լ����0������eMinDeltaTotalʵ�����ǽӽ�delay��������

  [�������]
  deltaTotal �������׷����Ȼ��ۺܴ����ֵ��Ȼ��������ס����Ա����������һ����С�����ƣ����Բ��ܳ������ֵ��

�������޵�����
  �������ޱȽ����ơ����޵�����ʵ������վ��״̬����ġ������������ÿ����ҿ�ʼ�ƶ���ʱ��
  ���³�ʼ��deltaTotal����û��������⡣������������ͻ��ᵽ�ƶ�ֹͣ״̬����ƭ���档
  Ϊ�˷�ֹ���ף��������������״̬������ƶ�ʱҪ������������ɣ�ֹͣʱ������������档
  �����ұ���Ѱ·�����ƶ���Ȼ����β��������ʵ������ì�ܵġ�Ӱ������С�

eMinDeltaTotal, eMaxDeltaTotal ����ԭ��
  ��������£��ܸ��Ƕ��� deltaTotal ��ֵ�����ֲ������𲻱�Ҫ�����ء�
  �����ΧҲ���������һ���Կ����׵�ʱ�䣬�������޷��ۻ������Գ���ʱ����������һ����޷����׵ġ�

�ٶȱ��������
  �ٶȱ����ͬ��Ҳ��Ӱ����㣬ȷ��������ʱ��Ҫ����������ء�
  �磬�������ٶȱȿͻ����ٶ�����ߣ����ʱ����ڵ��ƶ������ƫ�����delta�������eMaxDeltaTotal���ơ�
  ���ٲ�ͬ�������ᷢ�������ء�����ʱ���п��ܷ��������ء�

  ������ʱ���ȸ��ķ�������Ȼ��֪ͨ�ͻ��ˡ�
  ������ʱ����֪ͨ�ͻ��ˣ�Ȼ����ġ�

  ������Ҫ��֤�������˳��˵ʵ�������е㷳������ eMinDeltaTotal, eMaxDeltaTotal ʱ�������ֱ�Ӱ��ٶȱ��
  ��ɵ����������ڣ����������ֲ�������ۻ���ȥ���߼��Ŀ����Ϳ��Բ�����������أ��򻯿�����

  �ܽ᣺�������û�п��ǲ����ϸ�ͬ�������ٶȵķ�����������鷳��
  �ܽ᣺�߼���������������������㶨��

*/

#include "astar.hpp"
#include "atimer.hpp"

namespace astar
{

// �����������������λ��
class Location
{
	Point  location;   // λ���Լ�����
	float  speed;      // �ٶȡ�����/���롣

	int    lasttime;   // ���������ﵱǰlocation��ʱ�䡣���롣
	float  deltaTotal; // ���롣XXX �������������ʹ��������������м�ֵʹ��4��5�롣

public:
	// TODO ʵ��Ӧ��ʱ���ù��캯����ʼ�����ܲ����㡣��ʼ����ʽ������
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

		// ��������� finder.scantrail �����
		eTrailSizeError     = -1, // trail.size ���Ϸ���������ڵ���2��
		eTrailLocationError = -2, // ��ǰλ���޷�ƴ�ӡ�
		eTrailWalkError     = -3, // �����˲��ɵ�������
	};

	// �����յ����β���޸ķ�����λ�á���������ʱ����������ص���һ����ȷ��λ�á�
	// ������
	//     finder astar Ѱ·
	//     trail  �յ������β��
	// ������
	//     ��β����֤ʧ�ܣ���Ҫ����Э���������λ�á�
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

		if (length > speed * 2000) // ���ã��ͻ���β�����ͼ�� + ���������
			return reset(eTrailLengthError);

		int nowtime = astar::Timer::tickcount();
		deltaTotal += (nowtime - length / speed) - this->lasttime;

		// TODO ����������£���ʵ�ʲ��Թ۲�deltaTotal�ķ�Χ��
		const float eMinDeltaTotal = -100.f; // û�ж�������,��㶨����һ�����ĳɿ����á�
		const float eMaxDeltaTotal = 1000.f; // �����һ�����׷�Χ eMaxDeltaTotal-eMinDeltaTotal

#if(0 && !defined(_MSC_VER))	// ����deltaTotal�ķ�Χ
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

	// ֱ�Ӷ�λ������Ҫ��֤·�����ٶȡ�
	void setLocation(const Point & location)
	{
		this->deltaTotal = 0.f;
		this->location = location;
		this->lasttime = astar::Timer::tickcount();
	}

private:
	// ����������Ҫ����������״̬����ʱ��Ҫ����Э���������λ�á�
	TrailError reset(int error)
	{
		this->lasttime = astar::Timer::tickcount();
		this->deltaTotal = 0.f;
		return (TrailError)error;
	}
};

} // namespace astar
