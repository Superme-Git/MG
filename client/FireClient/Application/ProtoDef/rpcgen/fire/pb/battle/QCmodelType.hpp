
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 

class QCmodelType : public FireNet::Marshal
{
public:
	enum {
		ONE_FIGHT = 1, // 单人对手界面
		TEAM_FIGHT = 2, // 组队对手界面
		WATCH_FIGHT = 3, // 观战界面
	};

	QCmodelType() {
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

	bool operator<(const QCmodelType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const QCmodelType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
