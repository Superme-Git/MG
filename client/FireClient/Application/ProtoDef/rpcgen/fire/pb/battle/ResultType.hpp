
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 

class ResultType : public FireNet::Marshal
{
public:
	enum {
		RESULT_HPCHANGE = 1, // 1:目标HP变化
		RESULT_MPCHANGE = 2, // 2:目标MP变化
		RESULT_SPCHANGE = 4, // 3:目标SP变化
		RESULT_ULHPCHANGE = 8, // 4:当前血上限（伤）变化
		RESULT_REST = 16, // 5:休息
		RESULT_HURT = 32, // 6:目标受伤
		RESULT_CRITIC = 64, // 7:目标被暴击
		RESULT_DEFENCE = 128, // 8:目标防御
		RESULT_PARRY = 256, // 9:目标招架（类似躲闪，只有普通攻击会触发招架）
		RESULT_DODGE = 512, // 10:目标闪避
		RESULT_RUNAWAY = 1024, // 11:目标逃跑
		RESULT_SEIZE = 2048, // 12:目标被捕捉
		RESULT_SUMMONBACK = 4096, // 13:目标被召回
		RESULT_DEATH = 8192, // 14:目标死亡，倒在原地
		RESULT_KICKOUT = 16384, // 15:目标被击飞（没有鬼魂技能的怪和宠物死亡时）
		RESULT_GHOST = 32768, // 16:目标进入鬼魂状态（有鬼魂技能的怪和宠物死亡时）
		RESULT_RELIVE = 65536, // 17:复活
		RESULT_SUMMONPET = 131072, // 18:目标招唤宠物
		RESULT_IGNORE_PHYDIC_EFEN = 262144, // 19:忽略防御
		RESULT_ABORBE = 524288, // 20:吸收
		RESULT_FIRE_MANA = 1048576, // 21:烧蓝
		RESULT_GODBLESS = 2097152, // 22:神佑
		RESULT_EPCHANGE = 4194304, // 23:目标EP变化
		RESULT_DEAD_FULL_RELIVE = 8388608, // 24:满血复活
		RESULT_SHAPECHAGE = 16777216, // 25:模型改变
	};

	ResultType() {
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

	bool operator<(const ResultType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ResultType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
