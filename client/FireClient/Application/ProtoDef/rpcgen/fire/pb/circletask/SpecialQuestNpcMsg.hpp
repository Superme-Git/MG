
#pragma once


namespace fire { 
namespace pb { 
namespace circletask { 

class SpecialQuestNpcMsg : public FireNet::Marshal
{
public:
	enum {
		SPECIAL_QUEST_FORTUNEWHEEL_MSG = 142436, // 职业大转盘的msg
	};

	SpecialQuestNpcMsg() {
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

	bool operator<(const SpecialQuestNpcMsg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SpecialQuestNpcMsg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
