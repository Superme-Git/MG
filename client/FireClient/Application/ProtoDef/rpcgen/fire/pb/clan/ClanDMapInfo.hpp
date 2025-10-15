
#pragma once


namespace fire { 
namespace pb { 
namespace clan { 

class ClanDMapInfo : public FireNet::Marshal
{
public:
	int basemapid;

	ClanDMapInfo() {
		basemapid = 0;
	}

	ClanDMapInfo(int _basemapid_)
		: basemapid(_basemapid_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << basemapid;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> basemapid;
		return _os_;
	}

	bool operator<(const ClanDMapInfo &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ClanDMapInfo &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = basemapid - _o_.basemapid;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
