
#pragma once


namespace fire { 
namespace pb { 
namespace npc { 

class ImpExamAssistType : public FireNet::Marshal
{
public:
	enum {
		NOASSIST = 0, // 不使用协助
		DELWRONG = 1, // 删除一个错误答案
		CHORIGHT = 2, // 直接选择正确答案
	};

	ImpExamAssistType() {
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

	bool operator<(const ImpExamAssistType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ImpExamAssistType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
