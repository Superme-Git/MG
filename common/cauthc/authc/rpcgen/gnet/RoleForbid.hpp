
#pragma once


namespace gnet { 

class RoleForbid : public FireNet::Marshal
{
public:
	char ltype; // default 0
	int time; // 剩余封禁时间
	int createtime; // 封禁开始时间
	FireNet::Octets reason;

	RoleForbid() {
		ltype = 0;
		time = 0;
		createtime = 0;
	}

	RoleForbid(char _ltype_, int _time_, int _createtime_, const FireNet::Octets & _reason_)
		: ltype(_ltype_), time(_time_), createtime(_createtime_), reason(_reason_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << ltype;
		_os_ << time;
		_os_ << createtime;
		_os_ << reason;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> ltype;
		_os_ >> time;
		_os_ >> createtime;
		_os_ >> reason;
		return _os_;
	}

};

} 
