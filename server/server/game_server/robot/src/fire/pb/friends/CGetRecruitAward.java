
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetRecruitAward__ extends mkio.Protocol { }

/** 招募
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetRecruitAward extends __CGetRecruitAward__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806662;

	public int getType() {
		return 806662;
	}

	public int awardtype; // 奖励类型 1招募人数奖励 2招募玩家充值奖励 3招募玩家等级奖励
	public int awardid; // 配置招募奖励表里的id
	public long recruitrole; // 被招募的角色id
	public java.lang.String recruitserver; // 被招募的角色所在服务器id

	public CGetRecruitAward() {
		recruitserver = "";
	}

	public CGetRecruitAward(int _awardtype_, int _awardid_, long _recruitrole_, java.lang.String _recruitserver_) {
		this.awardtype = _awardtype_;
		this.awardid = _awardid_;
		this.recruitrole = _recruitrole_;
		this.recruitserver = _recruitserver_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(awardtype);
		_os_.marshal(awardid);
		_os_.marshal(recruitrole);
		_os_.marshal(recruitserver, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		awardtype = _os_.unmarshal_int();
		awardid = _os_.unmarshal_int();
		recruitrole = _os_.unmarshal_long();
		recruitserver = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetRecruitAward) {
			CGetRecruitAward _o_ = (CGetRecruitAward)_o1_;
			if (awardtype != _o_.awardtype) return false;
			if (awardid != _o_.awardid) return false;
			if (recruitrole != _o_.recruitrole) return false;
			if (!recruitserver.equals(_o_.recruitserver)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += awardtype;
		_h_ += awardid;
		_h_ += (int)recruitrole;
		_h_ += recruitserver.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(awardtype).append(",");
		_sb_.append(awardid).append(",");
		_sb_.append(recruitrole).append(",");
		_sb_.append("T").append(recruitserver.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

