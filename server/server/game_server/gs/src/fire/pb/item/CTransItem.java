package fire.pb.item;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CTransItem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CTransItem extends __CTransItem__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		if (srcpackid == BagTypes.DEPOT || dstpackid == BagTypes.DEPOT) {
			if (!Depot.useDepotCheck(roleId, npcid))
				return;
		}

		if (!checkMove(roleId))
			return;

		if (dstpackid == BagTypes.BAG)
			page = -1; // 现在背包没有page的概念了
		new PMoveItemProc(roleId, srckey, number, srcpackid, dstpackid, dstpos,
				page).submit();
	}

	private boolean checkMove(final long roleId) {
		if (srcpackid == BagTypes.QUEST) {
			if (dstpackid != BagTypes.QUEST)
				return false;
		} else {
			if (srcpackid != BagTypes.BAG && srcpackid != BagTypes.DEPOT
					&& srcpackid != BagTypes.TEMP) {
				return false;
			}
			if (dstpackid != BagTypes.BAG && dstpackid != BagTypes.DEPOT) {
				return false;
			}
		}
		return true;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787447;

	public int getType() {
		return 787447;
	}

	public int srcpackid;
	public int srckey;
	public int number;
	public int dstpackid;
	public int dstpos;
	public int page;
	public long npcid;

	public CTransItem() {
		npcid = -1;
	}

	public CTransItem(int _srcpackid_, int _srckey_, int _number_, int _dstpackid_, int _dstpos_, int _page_, long _npcid_) {
		this.srcpackid = _srcpackid_;
		this.srckey = _srckey_;
		this.number = _number_;
		this.dstpackid = _dstpackid_;
		this.dstpos = _dstpos_;
		this.page = _page_;
		this.npcid = _npcid_;
	}

	public final boolean _validator_() {
		if (srcpackid < 1) return false;
		if (srckey < 1) return false;
		if (number < -1) return false;
		if (dstpackid < 1) return false;
		if (dstpos < -1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(srcpackid);
		_os_.marshal(srckey);
		_os_.marshal(number);
		_os_.marshal(dstpackid);
		_os_.marshal(dstpos);
		_os_.marshal(page);
		_os_.marshal(npcid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		srcpackid = _os_.unmarshal_int();
		srckey = _os_.unmarshal_int();
		number = _os_.unmarshal_int();
		dstpackid = _os_.unmarshal_int();
		dstpos = _os_.unmarshal_int();
		page = _os_.unmarshal_int();
		npcid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CTransItem) {
			CTransItem _o_ = (CTransItem)_o1_;
			if (srcpackid != _o_.srcpackid) return false;
			if (srckey != _o_.srckey) return false;
			if (number != _o_.number) return false;
			if (dstpackid != _o_.dstpackid) return false;
			if (dstpos != _o_.dstpos) return false;
			if (page != _o_.page) return false;
			if (npcid != _o_.npcid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += srcpackid;
		_h_ += srckey;
		_h_ += number;
		_h_ += dstpackid;
		_h_ += dstpos;
		_h_ += page;
		_h_ += (int)npcid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(srcpackid).append(",");
		_sb_.append(srckey).append(",");
		_sb_.append(number).append(",");
		_sb_.append(dstpackid).append(",");
		_sb_.append(dstpos).append(",");
		_sb_.append(page).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CTransItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = srcpackid - _o_.srcpackid;
		if (0 != _c_) return _c_;
		_c_ = srckey - _o_.srckey;
		if (0 != _c_) return _c_;
		_c_ = number - _o_.number;
		if (0 != _c_) return _c_;
		_c_ = dstpackid - _o_.dstpackid;
		if (0 != _c_) return _c_;
		_c_ = dstpos - _o_.dstpos;
		if (0 != _c_) return _c_;
		_c_ = page - _o_.page;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npcid - _o_.npcid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
