
#pragma once


namespace gnet { 

class GetMaxOnlineNumRes : public GNET::Rpc::Data
{
public:
	int retcode;
	int maxnum;
	int fake_maxnum;
	int online_num;

	GetMaxOnlineNumRes() {
		retcode = 0;
		maxnum = 0;
		fake_maxnum = 0;
		online_num = 0;
	}

	GetMaxOnlineNumRes(int _retcode_, int _maxnum_, int _fake_maxnum_, int _online_num_)
		: retcode(_retcode_), maxnum(_maxnum_), fake_maxnum(_fake_maxnum_), online_num(_online_num_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << maxnum;
		_os_ << fake_maxnum;
		_os_ << online_num;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> maxnum;
		_os_ >> fake_maxnum;
		_os_ >> online_num;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GetMaxOnlineNumRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GetMaxOnlineNumRes *o = dynamic_cast<const GetMaxOnlineNumRes*>(&d)) return *this = *o;
		return *this;
	}

	GetMaxOnlineNumRes& operator=(const GetMaxOnlineNumRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		maxnum = d.maxnum;
		fake_maxnum = d.fake_maxnum;
		online_num = d.online_num;
		return *this;
	}

	bool operator<(const GetMaxOnlineNumRes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetMaxOnlineNumRes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = maxnum - _o_.maxnum;
		if (0 != _c_) return _c_;
		_c_ = fake_maxnum - _o_.fake_maxnum;
		if (0 != _c_) return _c_;
		_c_ = online_num - _o_.online_num;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
