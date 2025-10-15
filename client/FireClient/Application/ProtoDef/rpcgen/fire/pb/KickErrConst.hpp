
#pragma once


namespace fire { 
namespace pb { 

class KickErrConst : public FireNet::Marshal
{
public:
	enum {
		ERR_GM_KICKOUT = 2049, // 被GM踢下线
		ERR_SERVER_SHUTDOWN = 2050, // 服务器关闭
		ERR_GACD_PUNISH = 2051, // 反外挂答题没有通过
		ERR_RUN_TOO_FAST = 2052, // 走路太快被踢
		ERR_GACD_WAIGUA = 2053, // 使用外挂
		ERR_XUNBAO_SELLROLE = 2054, // 找宝网寄售角色
		ERR_FORBID_USER = 2055, // 账号被单服封禁
		ERR_GACD_KICKOUT = 2056, // gacd发-1踢玩家下线
	};

	KickErrConst() {
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

	bool operator<(const KickErrConst &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const KickErrConst &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
