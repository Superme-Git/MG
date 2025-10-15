//////////////////////////////////////////////////////////////////////////
// File:		LocationDetector.h
// Author:	杨春雷
// Date:		2016/05/10
//////////////////////////////////////////////////////////////////////////

#ifndef _LocationDetector_h_
#define _LocationDetector_h_

#include "Singleton.hpp"
#include <string>
#include <vector>

class LocationDetector : public CSingleton < LocationDetector >
{
public:
	struct Location
	{
		double mLongitude;  // 经度
		double mLatitude;  // 纬度
		std::string mName;  // 地名，如：北京.朝阳区.天畅园
	};
	typedef std::vector<Location> LocationArray;

private:
	std::string mFinishCallbackFunc;
	LocationArray mLocations;

public:
	LocationDetector();
	~LocationDetector();
    
    // 返回定位服务是否可用
    bool isAvailable() const;
    // 返回本APP是否被允许使用定位
    bool isAllowed() const;

	// 设置完成定位后的回调脚本函数
	void setFinishCallbackFunction(const std::string& finishCallbackFunc) { mFinishCallbackFunc = finishCallbackFunc; }
	// 开始定位
	void startDetect();

	// 定位完成后可以调用下列函数获取定位信息

	// 获取位置数量
	int getLocationCount() const { return (int)mLocations.size(); }
	// 获取位置
	const Location getLocation(int index) const;

    // called by platform
	void clear() { mLocations.clear(); }
	void addLocation(const Location& loc) { mLocations.push_back(loc); }

	void onFinishDetect(bool bSuccess);
};

#endif