
#pragma once


namespace fire { 
namespace pb { 
namespace move { 

class SceneState : public FireNet::Marshal
{
public:
	enum {
		BATTLE_FIGHTER = 1, // 1:战斗状态
		BATTLE_WATCHER = 2, // 2:观战状态
		BATTLE_REPLAY = 4, // 4:观看录像
		PLAY_CG = 8, // 8:动画播放
		NO_PAY_DAY = 16, // 16没有支付日卡 by changhao
		GM_FLAG = 2097152, // 22:场景中显示gm图标
	};

	SceneState() {
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

	bool operator<(const SceneState &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SceneState &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
