
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CDropItem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CDropItem extends __CDropItem__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return ;
		if ( packid == BagTypes.EQUIP ) {
			fire.pb.talk.MessageMgr.sendMsgNotify( roleId, 142375, null );
			return;
		}
		new PDestroyItem(roleId, packid, keyinpack).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787449;

	public int getType() {
		return 787449;
	}

	public int packid;
	public int keyinpack;
	public int npcid;

	public CDropItem() {
		npcid = -1;
	}

	public CDropItem(int _packid_, int _keyinpack_, int _npcid_) {
		this.packid = _packid_;
		this.keyinpack = _keyinpack_;
		this.npcid = _npcid_;
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
		_os_.marshal(packid);
		_os_.marshal(keyinpack);
		_os_.marshal(npcid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packid = _os_.unmarshal_int();
		keyinpack = _os_.unmarshal_int();
		npcid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CDropItem) {
			CDropItem _o_ = (CDropItem)_o1_;
			if (packid != _o_.packid) return false;
			if (keyinpack != _o_.keyinpack) return false;
			if (npcid != _o_.npcid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packid;
		_h_ += keyinpack;
		_h_ += npcid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packid).append(",");
		_sb_.append(keyinpack).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CDropItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = packid - _o_.packid;
		if (0 != _c_) return _c_;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		_c_ = npcid - _o_.npcid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

