
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetEquipTips__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetEquipTips extends __SGetEquipTips__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787653;

	public int getType() {
		return 787653;
	}

	public int packid;
	public int keyinpack;
	public int key2inpack;
	public com.locojoy.base.Octets tips;

	public SGetEquipTips() {
		tips = new com.locojoy.base.Octets();
	}

	public SGetEquipTips(int _packid_, int _keyinpack_, int _key2inpack_, com.locojoy.base.Octets _tips_) {
		this.packid = _packid_;
		this.keyinpack = _keyinpack_;
		this.key2inpack = _key2inpack_;
		this.tips = _tips_;
	}

	public final boolean _validator_() {
		if (keyinpack <= 0) return false;
		if (key2inpack < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(packid);
		_os_.marshal(keyinpack);
		_os_.marshal(key2inpack);
		_os_.marshal(tips);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packid = _os_.unmarshal_int();
		keyinpack = _os_.unmarshal_int();
		key2inpack = _os_.unmarshal_int();
		tips = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetEquipTips) {
			SGetEquipTips _o_ = (SGetEquipTips)_o1_;
			if (packid != _o_.packid) return false;
			if (keyinpack != _o_.keyinpack) return false;
			if (key2inpack != _o_.key2inpack) return false;
			if (!tips.equals(_o_.tips)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packid;
		_h_ += keyinpack;
		_h_ += key2inpack;
		_h_ += tips.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packid).append(",");
		_sb_.append(keyinpack).append(",");
		_sb_.append(key2inpack).append(",");
		_sb_.append("B").append(tips.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

