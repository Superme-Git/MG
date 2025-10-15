
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 

class OperationType : public FireNet::Marshal
{
public:
	enum {
		ACTION_ATTACK = 1, // 攻击
		ACTION_SKILL = 2, // 使用技能
		ACTION_USEITEM = 3, // 使用物品
		ACTION_DEFEND = 4, // 防御
		ACTION_PROTECT = 5, // 保护
		ACTION_SUMMON = 6, // 召唤宠物
		ACTION_WITHDRAW = 7, // 召还宠物
		ACTION_CATHCH = 8, // 捕捉
		ACTION_ESCAPE = 9, // 逃跑
		ACTION_REST = 10, // 休息
		ACTION_SPECIAL_SKILL = 11, // 特殊技能
		ACTION_SUMMON_INSTANT = 12, // 瞬时召唤
		ACTION_ESCAPE_INSTANT = 13, // 瞬时逃跑
		ACTION_FAILURE = 14, // 操作失败
		ACTION_BATTLE_END = 15, // 战斗结束,只有AI怪的AI指令有这个Action
		ACTION_ENVIRONMENTDEMO = 16, // 不带施法者的Demo，attackID填0
		ACTION_ENVIRONMENTCHANGE = 17, // 战场环境改变 operateid填战场环境id
		ACTION_ROUNDENDDEMO = 18, // 回合末结算demo
		ACTION_UNIQUE_SKILL = 19, // 绝技
		ACTION_FAILURE_NO_WONDER = 20, // 操作失败_不带叹号的！
	};

	OperationType() {
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

	bool operator<(const OperationType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const OperationType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
