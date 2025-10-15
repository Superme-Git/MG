
#pragma once


namespace fire { 
namespace pb { 

class BasicFightProperties : public FireNet::Marshal
{
public:
	short cons; // 体质
	short iq; // 智力
	short str; // 力量
	short endu; // 耐力
	short agi; // 敏捷

	BasicFightProperties() {
		cons = 0;
		iq = 0;
		str = 0;
		endu = 0;
		agi = 0;
	}

	BasicFightProperties(short _cons_, short _iq_, short _str_, short _endu_, short _agi_)
		: cons(_cons_), iq(_iq_), str(_str_), endu(_endu_), agi(_agi_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << cons;
		_os_ << iq;
		_os_ << str;
		_os_ << endu;
		_os_ << agi;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> cons;
		_os_ >> iq;
		_os_ >> str;
		_os_ >> endu;
		_os_ >> agi;
		return _os_;
	}

	bool operator<(const BasicFightProperties &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const BasicFightProperties &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = cons - _o_.cons;
		if (0 != _c_) return _c_;
		_c_ = iq - _o_.iq;
		if (0 != _c_) return _c_;
		_c_ = str - _o_.str;
		if (0 != _c_) return _c_;
		_c_ = endu - _o_.endu;
		if (0 != _c_) return _c_;
		_c_ = agi - _o_.agi;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
