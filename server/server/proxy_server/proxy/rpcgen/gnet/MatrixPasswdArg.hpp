
#pragma once


namespace gnet { 

class MatrixPasswdArg : public GNET::Rpc::Data
{
public:
	GNET::Octets account; // 帐号
	GNET::Octets challenge;
	int loginip;
	GNET::Octets loginipoctets;
	int logintype;
	int gameid;
	GNET::Octets gamekey;
	GNET::Octets serverid;
	GNET::Octets selfchannel; // 渠道号
	GNET::Octets childchannel; // 子渠道号
	GNET::Octets deviceid; // 设备唯一标识
	GNET::Octets mid;
	int reserved1;
	GNET::Octets reserved2;

	MatrixPasswdArg() {
		loginip = 0;
		logintype = 0;
		gameid = 0;
		reserved1 = 0;
	}

	MatrixPasswdArg(const GNET::Octets & _account_, const GNET::Octets & _challenge_, int _loginip_, const GNET::Octets & _loginipoctets_, int _logintype_, int _gameid_, const GNET::Octets & _gamekey_, const GNET::Octets & _serverid_, const GNET::Octets & _selfchannel_, const GNET::Octets & _childchannel_, const GNET::Octets & _deviceid_, const GNET::Octets & _mid_, int _reserved1_, const GNET::Octets & _reserved2_)
		: account(_account_), challenge(_challenge_), loginip(_loginip_), loginipoctets(_loginipoctets_), logintype(_logintype_), gameid(_gameid_), gamekey(_gamekey_), serverid(_serverid_), selfchannel(_selfchannel_), childchannel(_childchannel_), deviceid(_deviceid_), mid(_mid_), reserved1(_reserved1_), reserved2(_reserved2_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << account;
		_os_ << challenge;
		_os_ << loginip;
		_os_ << loginipoctets;
		_os_ << logintype;
		_os_ << gameid;
		_os_ << gamekey;
		_os_ << serverid;
		_os_ << selfchannel;
		_os_ << childchannel;
		_os_ << deviceid;
		_os_ << mid;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> account;
		_os_ >> challenge;
		_os_ >> loginip;
		_os_ >> loginipoctets;
		_os_ >> logintype;
		_os_ >> gameid;
		_os_ >> gamekey;
		_os_ >> serverid;
		_os_ >> selfchannel;
		_os_ >> childchannel;
		_os_ >> deviceid;
		_os_ >> mid;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new MatrixPasswdArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const MatrixPasswdArg *o = dynamic_cast<const MatrixPasswdArg*>(&d)) return *this = *o;
		return *this;
	}

	MatrixPasswdArg& operator=(const MatrixPasswdArg & d)
	{
		if (&d == this) return *this;
		account = d.account;
		challenge = d.challenge;
		loginip = d.loginip;
		loginipoctets = d.loginipoctets;
		logintype = d.logintype;
		gameid = d.gameid;
		gamekey = d.gamekey;
		serverid = d.serverid;
		selfchannel = d.selfchannel;
		childchannel = d.childchannel;
		deviceid = d.deviceid;
		mid = d.mid;
		reserved1 = d.reserved1;
		reserved2 = d.reserved2;
		return *this;
	}

};

} 
