
#pragma once


namespace fire { 
namespace pb { 
namespace npc { 

class TransmitTypes : public FireNet::Marshal
{
public:
	enum {
		impexamsystem = 4, // 智慧试炼考试系统拉人
		winnercall = 5, // 冠军试炼拉人
		backschoolwhile20lv = 6, // 20级时师傅拉你回职业
		singlepvp = 10, // 单人pvp拉人
		pvp3 = 15, // pvp3拉人
		pvp5 = 16, // pvp5拉人
	};

	TransmitTypes() {
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

	bool operator<(const TransmitTypes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const TransmitTypes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
