
#pragma once

#include <map>

namespace gnet { 

class ServerAttr : public GNET::Rpc::Data
{
public:
	enum {
		FLAG_DOUBLE_DROP = 4,
		FLAG_DOUBLE_SP = 8,
	};

	int flags; // 当前服务器状态，即上面列出的状态
	char load;
	std::map<int, int> extra;

	ServerAttr() {
		flags = 0;
		load = 0;
	}

	ServerAttr(int _flags_, char _load_, const std::map<int, int> & _extra_)
		: flags(_flags_), load(_load_), extra(_extra_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << flags;
		_os_ << load;
		_os_ << GNET::MarshalContainer(extra);
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> flags;
		_os_ >> load;
		_os_ >> GNET::MarshalContainer(extra);
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new ServerAttr(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const ServerAttr *o = dynamic_cast<const ServerAttr*>(&d)) return *this = *o;
		return *this;
	}

	ServerAttr& operator=(const ServerAttr & d)
	{
		if (&d == this) return *this;
		flags = d.flags;
		load = d.load;
		extra = d.extra;
		return *this;
	}

};

} 
