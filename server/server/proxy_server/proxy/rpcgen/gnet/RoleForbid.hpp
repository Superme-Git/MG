
#pragma once


namespace gnet { 

class RoleForbid : public GNET::Rpc::Data
{
public:
	char ltype; // default 0
	int time; // 剩余封禁时间
	int createtime; // 封禁开始时间
	GNET::Octets reason;

	RoleForbid() {
		ltype = 0;
		time = 0;
		createtime = 0;
	}

	RoleForbid(char _ltype_, int _time_, int _createtime_, const GNET::Octets & _reason_)
		: ltype(_ltype_), time(_time_), createtime(_createtime_), reason(_reason_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << ltype;
		_os_ << time;
		_os_ << createtime;
		_os_ << reason;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> ltype;
		_os_ >> time;
		_os_ >> createtime;
		_os_ >> reason;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new RoleForbid(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const RoleForbid *o = dynamic_cast<const RoleForbid*>(&d)) return *this = *o;
		return *this;
	}

	RoleForbid& operator=(const RoleForbid & d)
	{
		if (&d == this) return *this;
		ltype = d.ltype;
		time = d.time;
		createtime = d.createtime;
		reason = d.reason;
		return *this;
	}

};

} 
