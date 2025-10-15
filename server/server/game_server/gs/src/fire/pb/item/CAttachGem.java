package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAttachGem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAttachGem extends __CAttachGem__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId == -1)
			return;
		new fire.pb.item.equip.diamond.PAttachGemToEquip(roleId, keyinpack,
				gemkey, packid).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787493;

	public int getType() {
		return 787493;
	}

	public int keyinpack;
	public byte packid;
	public int gemkey;

	public CAttachGem() {
	}

	public CAttachGem(int _keyinpack_, byte _packid_, int _gemkey_) {
		this.keyinpack = _keyinpack_;
		this.packid = _packid_;
		this.gemkey = _gemkey_;
	}

	public final boolean _validator_() {
		if (keyinpack < 1) return false;
		if (gemkey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(keyinpack);
		_os_.marshal(packid);
		_os_.marshal(gemkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		keyinpack = _os_.unmarshal_int();
		packid = _os_.unmarshal_byte();
		gemkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAttachGem) {
			CAttachGem _o_ = (CAttachGem)_o1_;
			if (keyinpack != _o_.keyinpack) return false;
			if (packid != _o_.packid) return false;
			if (gemkey != _o_.gemkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += keyinpack;
		_h_ += (int)packid;
		_h_ += gemkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(keyinpack).append(",");
		_sb_.append(packid).append(",");
		_sb_.append(gemkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAttachGem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		_c_ = packid - _o_.packid;
		if (0 != _c_) return _c_;
		_c_ = gemkey - _o_.gemkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
