
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSynchroBossHp__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSynchroBossHp extends __SSynchroBossHp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793459;

	public int getType() {
		return 793459;
	}

	public int bossmonsterid; // boss怪物id
	public byte flag; // 0:开始 1:更新
	public long maxhp;
	public long hp;
	public java.lang.String rolename; // 角色名称
	public long changehp; // 造成的伤害

	public SSynchroBossHp() {
		rolename = "";
	}

	public SSynchroBossHp(int _bossmonsterid_, byte _flag_, long _maxhp_, long _hp_, java.lang.String _rolename_, long _changehp_) {
		this.bossmonsterid = _bossmonsterid_;
		this.flag = _flag_;
		this.maxhp = _maxhp_;
		this.hp = _hp_;
		this.rolename = _rolename_;
		this.changehp = _changehp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(bossmonsterid);
		_os_.marshal(flag);
		_os_.marshal(maxhp);
		_os_.marshal(hp);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(changehp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		bossmonsterid = _os_.unmarshal_int();
		flag = _os_.unmarshal_byte();
		maxhp = _os_.unmarshal_long();
		hp = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		changehp = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSynchroBossHp) {
			SSynchroBossHp _o_ = (SSynchroBossHp)_o1_;
			if (bossmonsterid != _o_.bossmonsterid) return false;
			if (flag != _o_.flag) return false;
			if (maxhp != _o_.maxhp) return false;
			if (hp != _o_.hp) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (changehp != _o_.changehp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += bossmonsterid;
		_h_ += (int)flag;
		_h_ += (int)maxhp;
		_h_ += (int)hp;
		_h_ += rolename.hashCode();
		_h_ += (int)changehp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(bossmonsterid).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(maxhp).append(",");
		_sb_.append(hp).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(changehp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

