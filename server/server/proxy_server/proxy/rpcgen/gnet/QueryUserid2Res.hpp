
#pragma once


namespace gnet { 

class QueryUserid2Res : public GNET::Rpc::Data
{
public:
	int result;
	int userid;
	int64_t roleid;
	int level;

	QueryUserid2Res() {
		result = 0;
		userid = 0;
		roleid = 0;
		level = 0;
	}

	QueryUserid2Res(int _result_, int _userid_, int64_t _roleid_, int _level_)
		: result(_result_), userid(_userid_), roleid(_roleid_), level(_level_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << result;
		_os_ << userid;
		_os_ << roleid;
		_os_ << level;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> result;
		_os_ >> userid;
		_os_ >> roleid;
		_os_ >> level;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new QueryUserid2Res(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const QueryUserid2Res *o = dynamic_cast<const QueryUserid2Res*>(&d)) return *this = *o;
		return *this;
	}

	QueryUserid2Res& operator=(const QueryUserid2Res & d)
	{
		if (&d == this) return *this;
		result = d.result;
		userid = d.userid;
		roleid = d.roleid;
		level = d.level;
		return *this;
	}

	bool operator<(const QueryUserid2Res &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const QueryUserid2Res &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = result - _o_.result;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = GNET::signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
