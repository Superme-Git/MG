
#pragma once


namespace fire { 
namespace pb { 

class LogPriority : public FireNet::Marshal
{
public:
	enum {
		LOG_FORMAT = 15, // 玩家行为标准的日志，如：账号登陆，充值，任务相关。
		LOG_STAT = 16, // 每5分钟记录邮件次数，金钱修改量，物品修改量等。
		LOG_GM = 17, // 记录GM操作的log
		LOG_MONEY = 18, // 记录金钱的产生及消耗数量原因。
		LOG_CHAR2 = 19, // 聊天记录，记录采用BASE64编码记录。
		LOG_COUNTER = 20, // 每5分钟记录邮件次数，金钱修改量，物品修改量等。
		LOG_XINGCHENG = 21, // 玩家详细行为日志，如：移动，打怪，组队，拾取等。
		LOG_TRADE = 22, // 每日商城消耗记录。
		LOG_FUSHI = 23, // 累计记录玩家充值，买，卖，消耗符石数量。
	};

	LogPriority() {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		return _os_;
	}

	bool operator<(const LogPriority &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const LogPriority &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
