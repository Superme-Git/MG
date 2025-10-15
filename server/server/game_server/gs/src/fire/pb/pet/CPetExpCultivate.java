
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPetExpCultivate__ extends mkio.Protocol { }

/** 客户端请求使用经验道具
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPetExpCultivate extends __CPetExpCultivate__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;

		PPetExpCultivate proc = new PPetExpCultivate(roleid, petkey, itemid, itemnum);
		proc.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788523;

	public int getType() {
		return 788523;
	}

	public int petkey; // 宠物key
	public int itemid; // 物品id
	public byte itemnum; // 物品数量

	public CPetExpCultivate() {
	}

	public CPetExpCultivate(int _petkey_, int _itemid_, byte _itemnum_) {
		this.petkey = _petkey_;
		this.itemid = _itemid_;
		this.itemnum = _itemnum_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		if (itemid < 1) return false;
		if (itemnum < 1 || itemnum > 10) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(itemid);
		_os_.marshal(itemnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		itemid = _os_.unmarshal_int();
		itemnum = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPetExpCultivate) {
			CPetExpCultivate _o_ = (CPetExpCultivate)_o1_;
			if (petkey != _o_.petkey) return false;
			if (itemid != _o_.itemid) return false;
			if (itemnum != _o_.itemnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += itemid;
		_h_ += (int)itemnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(itemnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPetExpCultivate _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		_c_ = itemnum - _o_.itemnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

