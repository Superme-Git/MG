
#pragma once


namespace fire { 
namespace pb { 
namespace master { 

class DataConfig : public FireNet::Marshal
{
public:
	enum {
		REG_MONEY = 20000, // 登记师父扣除的游戏币
		MONEY1 = 10000, // 徒弟3——7天不在线，解除关系后需扣除师父10000游戏币
		MONEY2 = 100000, // 徒弟3天以内不在线，解除关系后需扣除师父100000游戏币
		REN_QI = 100, // 徒弟3天以内不在线，解除关系后需扣除师父100人气值
		TAIXUEFUZI_ID = 10215, // 太学夫子id
		EVALUATER_LEVEL1 = 120, // 师父收益一
		EVALUATER_LEVEL2 = 100, // 师父收益二
		EVALUATER_LEVEL3 = 80, // 师父收益三
		EVALUATER_LEVEL4 = 60, // 师父收益四
		EVALUATER_LEVEL5 = 40, // 师父收益五
	};

	DataConfig() {
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

	bool operator<(const DataConfig &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const DataConfig &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
