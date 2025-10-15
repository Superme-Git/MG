
#pragma once


namespace cross { 

class ServerInfo : public GNET::Rpc::Data
{
public:
	int zoneid;
	int online_num;
	int max_online_num;

	ServerInfo() {
		zoneid = 0;
		online_num = 0;
		max_online_num = 0;
	}

	ServerInfo(int _zoneid_, int _online_num_, int _max_online_num_)
		: zoneid(_zoneid_), online_num(_online_num_), max_online_num(_max_online_num_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << zoneid;
		_os_ << online_num;
		_os_ << max_online_num;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> zoneid;
		_os_ >> online_num;
		_os_ >> max_online_num;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new ServerInfo(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const ServerInfo *o = dynamic_cast<const ServerInfo*>(&d)) return *this = *o;
		return *this;
	}

	ServerInfo& operator=(const ServerInfo & d)
	{
		if (&d == this) return *this;
		zoneid = d.zoneid;
		online_num = d.online_num;
		max_online_num = d.max_online_num;
		return *this;
	}

	bool operator<(const ServerInfo &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ServerInfo &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = online_num - _o_.online_num;
		if (0 != _c_) return _c_;
		_c_ = max_online_num - _o_.max_online_num;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
