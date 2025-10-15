
package fire.msp.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRemoveMonstershow__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRemoveMonstershow extends __GRemoveMonstershow__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.manager.MonstershowManager.getInstance().remove( monsterkey );
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 727904;

	public int getType() {
		return 727904;
	}

	public long monsterkey; // 地图上的npckey

	public GRemoveMonstershow() {
	}

	public GRemoveMonstershow(long _monsterkey_) {
		this.monsterkey = _monsterkey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(monsterkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		monsterkey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRemoveMonstershow) {
			GRemoveMonstershow _o_ = (GRemoveMonstershow)_o1_;
			if (monsterkey != _o_.monsterkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)monsterkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(monsterkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GRemoveMonstershow _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(monsterkey - _o_.monsterkey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

