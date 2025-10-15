
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SOtherItemTips__ extends mkio.Protocol { }

/** 摆摊Tip返回
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SOtherItemTips extends __SOtherItemTips__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787768;

	public int getType() {
		return 787768;
	}

	public long roleid; // 目标玩家的id
	public int packid;
	public int keyinpack;
	public com.locojoy.base.Octets tips;

	public SOtherItemTips() {
		tips = new com.locojoy.base.Octets();
	}

	public SOtherItemTips(long _roleid_, int _packid_, int _keyinpack_, com.locojoy.base.Octets _tips_) {
		this.roleid = _roleid_;
		this.packid = _packid_;
		this.keyinpack = _keyinpack_;
		this.tips = _tips_;
	}

	public final boolean _validator_() {
		if (packid < 1) return false;
		if (keyinpack < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(packid);
		_os_.marshal(keyinpack);
		_os_.marshal(tips);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		packid = _os_.unmarshal_int();
		keyinpack = _os_.unmarshal_int();
		tips = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SOtherItemTips) {
			SOtherItemTips _o_ = (SOtherItemTips)_o1_;
			if (roleid != _o_.roleid) return false;
			if (packid != _o_.packid) return false;
			if (keyinpack != _o_.keyinpack) return false;
			if (!tips.equals(_o_.tips)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += packid;
		_h_ += keyinpack;
		_h_ += tips.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(packid).append(",");
		_sb_.append(keyinpack).append(",");
		_sb_.append("B").append(tips.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

