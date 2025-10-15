
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 

class BattleType : public FireNet::Marshal
{
public:
	enum {
		BATTLE_PVE = 10, // 打怪 ,所有pve %10都是0
		BATTLE_HIDEAREA = 1100, // 暗雷区战斗
		BATTLE_SHOWAREA = 1200, // 明雷区战斗
		BATTLE_BOSS = 30, // pve中的boss战,进战斗的时候需要给一条提示(141614)
		BATTLE_LINE = 40, // 副本战斗
		BATTLE_CLANBOSS = 50, // 公会boss
		BATTLE_SHOUXI = 60, // capital apprentice
		BATTLE_PVP = 11, // 切磋,所有pvp %10都是1
		BATTLE_LIVEDIE = 21, // 生死战
		BATTLE_DUEL_SINGLE = 31, // 单人决斗
		BATTLE_DUEL_TEAM = 41, // 组队决斗
		BATTLE_BINGFENG_WAR = 100, // 冰封王座战斗
		BATTLE_INST_BATTLE = 110, // 公会副本战斗
		BATTLE_CLAN_FIGHT = 201, // 公会战 by changhao
	};

	BattleType() {
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

	bool operator<(const BattleType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const BattleType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
