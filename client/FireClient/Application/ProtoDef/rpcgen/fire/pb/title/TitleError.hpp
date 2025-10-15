
#pragma once


namespace fire { 
namespace pb { 
namespace title { 

class TitleError : public FireNet::Marshal
{
public:
	enum {
		Title_Err_Unknown = -1,
		Title_Err_Unexist = -2, // 系统中不存在这个称谓
		Title_Err_Full = -3,
		Title_Err_Nuowned = -4, // 用户没有这个称谓
	};

	TitleError() {
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

	bool operator<(const TitleError &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const TitleError &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
