
package cross;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class UserInfo implements Marshal {
	public int userid;
	public int func;
	public int funcparm;
	public int loginip;
	public byte blisgm;
	public java.util.ArrayList<Integer> auth;
	public int algorithm; // 账号安全级别
	public byte gender; // 0-female,1-male,2-unknown
	public com.locojoy.base.Octets nickname;

	public UserInfo() {
		auth = new java.util.ArrayList<Integer>();
		nickname = new com.locojoy.base.Octets();
	}

	public UserInfo(int _userid_, int _func_, int _funcparm_, int _loginip_, byte _blisgm_, java.util.ArrayList<Integer> _auth_, int _algorithm_, byte _gender_, com.locojoy.base.Octets _nickname_) {
		this.userid = _userid_;
		this.func = _func_;
		this.funcparm = _funcparm_;
		this.loginip = _loginip_;
		this.blisgm = _blisgm_;
		this.auth = _auth_;
		this.algorithm = _algorithm_;
		this.gender = _gender_;
		this.nickname = _nickname_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(userid);
		_os_.marshal(func);
		_os_.marshal(funcparm);
		_os_.marshal(loginip);
		_os_.marshal(blisgm);
		_os_.compact_uint32(auth.size());
		for (Integer _v_ : auth) {
			_os_.marshal(_v_);
		}
		_os_.marshal(algorithm);
		_os_.marshal(gender);
		_os_.marshal(nickname);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		func = _os_.unmarshal_int();
		funcparm = _os_.unmarshal_int();
		loginip = _os_.unmarshal_int();
		blisgm = _os_.unmarshal_byte();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			auth.add(_v_);
		}
		algorithm = _os_.unmarshal_int();
		gender = _os_.unmarshal_byte();
		nickname = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof UserInfo) {
			UserInfo _o_ = (UserInfo)_o1_;
			if (userid != _o_.userid) return false;
			if (func != _o_.func) return false;
			if (funcparm != _o_.funcparm) return false;
			if (loginip != _o_.loginip) return false;
			if (blisgm != _o_.blisgm) return false;
			if (!auth.equals(_o_.auth)) return false;
			if (algorithm != _o_.algorithm) return false;
			if (gender != _o_.gender) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += func;
		_h_ += funcparm;
		_h_ += loginip;
		_h_ += (int)blisgm;
		_h_ += auth.hashCode();
		_h_ += algorithm;
		_h_ += (int)gender;
		_h_ += nickname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(func).append(",");
		_sb_.append(funcparm).append(",");
		_sb_.append(loginip).append(",");
		_sb_.append(blisgm).append(",");
		_sb_.append(auth).append(",");
		_sb_.append(algorithm).append(",");
		_sb_.append(gender).append(",");
		_sb_.append("B").append(nickname.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

