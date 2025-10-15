
package fire.pb.item;
import com.locojoy.base.Octets;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __COtherItemTips__ extends mkio.Protocol { }

/** 摆摊Tip请求
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class COtherItemTips extends __COtherItemTips__ {
	@Override
	protected void process() {
		// protocol handle
		final long askroleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (askroleId < 0)
			return;
		ItemMaps bag = Module.getInstance().getItemMaps(roleid, packid, true);
		if (bag == null)
			return;
		final ItemBase item = bag.getItem(keyinpack);
		if (item == null) {
			return;
		}

		Octets tips = item.getTips();
		if (tips == null) {
			Module.logger.error("错误的tips请求,物品名:" + item.getName());
			return;
		}
		
		SOtherItemTips send = new SOtherItemTips(roleid, packid, keyinpack, tips);
		gnet.link.Onlines.getInstance().sendResponse(this, send);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787767;

	public int getType() {
		return 787767;
	}

	public long roleid; // 目标玩家的id
	public int packid;
	public int keyinpack;

	public COtherItemTips() {
	}

	public COtherItemTips(long _roleid_, int _packid_, int _keyinpack_) {
		this.roleid = _roleid_;
		this.packid = _packid_;
		this.keyinpack = _keyinpack_;
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
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		packid = _os_.unmarshal_int();
		keyinpack = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof COtherItemTips) {
			COtherItemTips _o_ = (COtherItemTips)_o1_;
			if (roleid != _o_.roleid) return false;
			if (packid != _o_.packid) return false;
			if (keyinpack != _o_.keyinpack) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += packid;
		_h_ += keyinpack;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(packid).append(",");
		_sb_.append(keyinpack).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(COtherItemTips _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
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

