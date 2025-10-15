
#pragma once


namespace fire { 
namespace pb { 
namespace npc { 

class NpcServiceMappingTypes : public FireNet::Marshal
{
public:
	enum {
		NONE = 0,
		ACCEPT_CIRCLE_TASK = 1, // 接任务
		SUBMIT_CIRCLE_TASK = 2, // 提交任务
		QUERY_CIRCLE_TASK = 3, // 查询任务
		QUERY_CIRCLE_TEAM = 4, // 便捷组队
		QUERY_CIRCLE_BATTLE = 5, // 进入战斗
		CIRCLE_PRODUCE = 6, // 打造物品
		RENXING_CIRCLE_TASK = 7, // 任性任务
		CHALLENGE_NPC = 9, // NPC挑战
		ENTER_INST = 10, // 进入副本
		POP_UI = 11, // 弹界面服务
		QUERY_CAMERA_URL = 12, // 请求录像路径
		ACCEPT_TUPO = 13, // 接突破任务
	};

	NpcServiceMappingTypes() {
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

	bool operator<(const NpcServiceMappingTypes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const NpcServiceMappingTypes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
