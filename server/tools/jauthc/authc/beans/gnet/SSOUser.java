
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SSOUser implements Marshal {
	public byte isagent;
	public long userid;
	public com.locojoy.base.Octets account;
	public com.locojoy.base.Octets agentname;
	public com.locojoy.base.Octets agentaccount;

	public SSOUser() {
		isagent = 0;
		account = new com.locojoy.base.Octets();
		agentname = new com.locojoy.base.Octets();
		agentaccount = new com.locojoy.base.Octets();
	}

	public SSOUser(byte _isagent_, long _userid_, com.locojoy.base.Octets _account_, com.locojoy.base.Octets _agentname_, com.locojoy.base.Octets _agentaccount_) {
		this.isagent = _isagent_;
		this.userid = _userid_;
		this.account = _account_;
		this.agentname = _agentname_;
		this.agentaccount = _agentaccount_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(isagent);
		_os_.marshal(userid);
		_os_.marshal(account);
		_os_.marshal(agentname);
		_os_.marshal(agentaccount);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		isagent = _os_.unmarshal_byte();
		userid = _os_.unmarshal_long();
		account = _os_.unmarshal_Octets();
		agentname = _os_.unmarshal_Octets();
		agentaccount = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSOUser) {
			SSOUser _o_ = (SSOUser)_o1_;
			if (isagent != _o_.isagent) return false;
			if (userid != _o_.userid) return false;
			if (!account.equals(_o_.account)) return false;
			if (!agentname.equals(_o_.agentname)) return false;
			if (!agentaccount.equals(_o_.agentaccount)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)isagent;
		_h_ += (int)userid;
		_h_ += account.hashCode();
		_h_ += agentname.hashCode();
		_h_ += agentaccount.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(isagent).append(",");
		_sb_.append(userid).append(",");
		_sb_.append("B").append(account.size()).append(",");
		_sb_.append("B").append(agentname.size()).append(",");
		_sb_.append("B").append(agentaccount.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

