
#pragma once


namespace fire { 
namespace pb { 
namespace game { 

class MoneyType : public FireNet::Marshal
{
public:
	enum {
		MoneyType_None = 0, // 无效类型
		MoneyType_SilverCoin = 1, // 银币
		MoneyType_GoldCoin = 2, // 金币
		MoneyType_HearthStone = 3, // 符石
		MoneyType_ProfContribute = 4, // 职业贡献
		MoneyType_RongYu = 5, // 荣誉值
		MoneyType_FactionContribute = 6, // 公会贡献
		MoneyType_ShengWang = 7, // 声望
		MoneyType_FestivalPoint = 8, // 节日积分
		MoneyType_GoodTeacherVal = 9, // 良师值
		MoneyType_RoleExp = 10, // 角色经验
		MoneyType_Activity = 11, // 活跃度
		MoneyType_Energy = 12, // 活力
		MoneyType_EreditPoint = 13, // 信用点
		MoneyType_Item = 99, // 道具
		MoneyType_Num = 15, // 类型数量
	};

	MoneyType() {
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

	bool operator<(const MoneyType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const MoneyType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
