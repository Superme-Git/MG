
#pragma once


namespace openau { 

class ErrorCodes : public GNET::Rpc::Data
{
public:
	enum {
		error_succeed = 0, // �ɹ�
		error_invalid = 1, // ʧ�ܣ�token/sessionid��Ч��������Ч
		error_plat_args = 2, // ��ƽ̨�ķ��ʵĲ����쳣
		error_plat_trans = 3, // ��ƽ̨�ķ����쳣
		error_xdb_store = 4, // xdb�洢�쳣
	};

	ErrorCodes() {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new ErrorCodes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const ErrorCodes *o = dynamic_cast<const ErrorCodes*>(&d)) return *this = *o;
		return *this;
	}

	ErrorCodes& operator=(const ErrorCodes & d)
	{
		if (&d == this) return *this;
		return *this;
	}

	bool operator<(const ErrorCodes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ErrorCodes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
