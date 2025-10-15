
package fire.pb.item;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPutOnEquip__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPutOnEquip extends __CPutOnEquip__ {
	@Override
	protected void process() {
		final long roleId=gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0) {
			return;
		}
		// protocol handle
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 131451, null);
			return;
		}
		new PPutOnEquip(roleId, packkey, dstpos).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787445;

	public int getType() {
		return 787445;
	}

	public int packkey;
	public int dstpos;

	public CPutOnEquip() {
	}

	public CPutOnEquip(int _packkey_, int _dstpos_) {
		this.packkey = _packkey_;
		this.dstpos = _dstpos_;
	}

	public final boolean _validator_() {
		if (packkey < 1) return false;
		if (dstpos < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(packkey);
		_os_.marshal(dstpos);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packkey = _os_.unmarshal_int();
		dstpos = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPutOnEquip) {
			CPutOnEquip _o_ = (CPutOnEquip)_o1_;
			if (packkey != _o_.packkey) return false;
			if (dstpos != _o_.dstpos) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packkey;
		_h_ += dstpos;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packkey).append(",");
		_sb_.append(dstpos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPutOnEquip _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = packkey - _o_.packkey;
		if (0 != _c_) return _c_;
		_c_ = dstpos - _o_.dstpos;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

