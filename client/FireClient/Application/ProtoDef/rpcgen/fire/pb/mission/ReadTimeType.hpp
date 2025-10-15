
#pragma once


namespace fire { 
namespace pb { 
namespace mission { 

class ReadTimeType : public FireNet::Marshal
{
public:
	enum {
		TREASE_MAP = 1, // 挖宝图
		USE_TASK_ITEM = 2, // 使用任务道具
		FOSSICK = 3, // 采集
		OPEN_BOX = 4, // 开宝箱
		END_TALK_QUEST = 5, // 结束对话类任务
		BEGIN_BATTLE_QUEST = 6, // 6开始战斗类任务
		END_AREA_QUEST = 7, // 区域护送
	};

	ReadTimeType() {
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

	bool operator<(const ReadTimeType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ReadTimeType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
