
package fire.pb.item;

import mkdb.Procedure;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetPackInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}


public class CGetPackInfo extends __CGetPackInfo__ {
	@Override
	protected void process() {
		final long roleId=gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleId<0) return;
		/*if (packid == BagTypes.DEPOT) {
//			if(!Depot.useDepotCheck(roleId, npcid))
//				return ;
			new Procedure(){
				protected boolean process(){
					final ItemMaps bag = Module.getInstance().getItemMaps(roleId,packid,false);
					final SGetPackInfo res = new SGetPackInfo();
					res.packid = packid;
					res.baginfo = bag.getPackInfo();
					gnet.link.Onlines.getInstance().send(roleId, res);
					return true;
				}
			}.submit();

		}*/
		if (packid == BagTypes.MARKET) {// newly added function by liangyanpeng
			if (!fire.pb.shop.srv.market.Market.useMarketCheck(roleId))
				return;
			new Procedure() {
				protected boolean process() {
					final ItemMaps bag = Module.getInstance().getItemMaps(roleId, packid, false);
					final SGetPackInfo res = new SGetPackInfo();
					res.packid = packid;
					res.baginfo = bag.getPackInfo();
					gnet.link.Onlines.getInstance().send(roleId, res);
					return true;
				}
			}.submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787442;

	public int getType() {
		return 787442;
	}

	public int packid;
	public long npcid;

	public CGetPackInfo() {
	}

	public CGetPackInfo(int _packid_, long _npcid_) {
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
		if (_o1_ instanceof CGetPackInfo) {
			CGetPackInfo _o_ = (CGetPackInfo)_o1_;
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

	public int compareTo(CGetPackInfo _o_) {
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

