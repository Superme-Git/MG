
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CListPack__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CListPack extends __CListPack__ {
	@Override
	protected void process() {
		final long roleId=gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleId<0) return;
		if (packid != BagTypes.BAG && packid != BagTypes.DEPOT && packid != BagTypes.QUEST)
			return;
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				if (BagTypes.QUEST != packid) {
					ItemMaps ic = Module.getInstance().getItemMaps(roleId, packid, false);
					if (!ic.doList()) {
						return false;
					}
				}
				else {
					ItemMaps ic = Module.getInstance().getItemMaps(roleId, packid, false);
					if (!ic.doListquest()) {
						return false;
					}
				}
				
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787444;

	public int getType() {
		return 787444;
	}

	public int packid;
	public long npcid;

	public CListPack() {
	}

	public CListPack(int _packid_, long _npcid_) {
		this.packid = _packid_;
		this.npcid = _npcid_;
	}

	public final boolean _validator_() {
		if (packid < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(packid);
		_os_.marshal(npcid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packid = _os_.unmarshal_int();
		npcid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CListPack) {
			CListPack _o_ = (CListPack)_o1_;
			if (packid != _o_.packid) return false;
			if (npcid != _o_.npcid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packid;
		_h_ += (int)npcid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packid).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CListPack _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = packid - _o_.packid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npcid - _o_.npcid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

