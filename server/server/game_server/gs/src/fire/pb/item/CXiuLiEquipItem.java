
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CXiuLiEquipItem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CXiuLiEquipItem extends __CXiuLiEquipItem__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId=gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleId<0) return;
		
		final PRepairEquipItem repairEquip = new PRepairEquipItem(roleId, keyinpack, packid, repairtype);
		repairEquip.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787475;

	public int getType() {
		return 787475;
	}

	public int repairtype;
	public int packid;
	public int keyinpack; // keyinpack

	public CXiuLiEquipItem() {
	}

	public CXiuLiEquipItem(int _repairtype_, int _packid_, int _keyinpack_) {
		this.repairtype = _repairtype_;
		this.packid = _packid_;
		this.keyinpack = _keyinpack_;
	}

	public final boolean _validator_() {
		if (repairtype < 0 || repairtype > 3) return false;
		if (packid < 1) return false;
		if (keyinpack < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(repairtype);
		_os_.marshal(packid);
		_os_.marshal(keyinpack);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		repairtype = _os_.unmarshal_int();
		packid = _os_.unmarshal_int();
		keyinpack = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CXiuLiEquipItem) {
			CXiuLiEquipItem _o_ = (CXiuLiEquipItem)_o1_;
			if (repairtype != _o_.repairtype) return false;
			if (packid != _o_.packid) return false;
			if (keyinpack != _o_.keyinpack) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += repairtype;
		_h_ += packid;
		_h_ += keyinpack;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(repairtype).append(",");
		_sb_.append(packid).append(",");
		_sb_.append(keyinpack).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CXiuLiEquipItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = repairtype - _o_.repairtype;
		if (0 != _c_) return _c_;
		_c_ = packid - _o_.packid;
		if (0 != _c_) return _c_;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

