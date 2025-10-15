
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRuneGive__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRuneGive extends __CRuneGive__ {
	@Override
	protected void process() {
		// protocol handle
		//捐献符文
		final long selfroleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(selfroleid < 0) {
			return;
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				return fire.pb.clan.srv.ClanManage.sendCRuneGive(selfroleid,roleid,givetype,givevalue,itemkey,bagtype);
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808509;

	public int getType() {
		return 808509;
	}

	public long roleid;
	public int givetype; // 捐赠类型  0活力  1道具
	public int givevalue; // 物品code
	public int itemkey; // 物品key
	public int bagtype; // 背包类型

	public CRuneGive() {
	}

	public CRuneGive(long _roleid_, int _givetype_, int _givevalue_, int _itemkey_, int _bagtype_) {
		this.roleid = _roleid_;
		this.givetype = _givetype_;
		this.givevalue = _givevalue_;
		this.itemkey = _itemkey_;
		this.bagtype = _bagtype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(givetype);
		_os_.marshal(givevalue);
		_os_.marshal(itemkey);
		_os_.marshal(bagtype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		givetype = _os_.unmarshal_int();
		givevalue = _os_.unmarshal_int();
		itemkey = _os_.unmarshal_int();
		bagtype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRuneGive) {
			CRuneGive _o_ = (CRuneGive)_o1_;
			if (roleid != _o_.roleid) return false;
			if (givetype != _o_.givetype) return false;
			if (givevalue != _o_.givevalue) return false;
			if (itemkey != _o_.itemkey) return false;
			if (bagtype != _o_.bagtype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += givetype;
		_h_ += givevalue;
		_h_ += itemkey;
		_h_ += bagtype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(givetype).append(",");
		_sb_.append(givevalue).append(",");
		_sb_.append(itemkey).append(",");
		_sb_.append(bagtype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRuneGive _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = givetype - _o_.givetype;
		if (0 != _c_) return _c_;
		_c_ = givevalue - _o_.givevalue;
		if (0 != _c_) return _c_;
		_c_ = itemkey - _o_.itemkey;
		if (0 != _c_) return _c_;
		_c_ = bagtype - _o_.bagtype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

