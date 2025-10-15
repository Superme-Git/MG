
#pragma once


namespace fire { 
namespace pb { 
namespace talk { 

class FunModelType : public FireNet::Marshal
{
public:
	enum {
		FUN_TASKITEM = 1, // 任务求助道具功能
		FUN_DIELIVE_CLAN = 2, // 生死战公会频道分享
		FUN_DIELIVE_WORLD = 3, // 生死战世界频道分享
		QIU_ZHU = 4, // 求助
	};

	FunModelType() {
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

	bool operator<(const FunModelType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const FunModelType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
