
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SOpenClanMedic__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SOpenClanMedic extends __SOpenClanMedic__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808440;

	public int getType() {
		return 808440;
	}

	public int selecttype; // 选择几倍产药
	public int buyitemnum; // 当天已经购买数量
	public java.util.ArrayList<fire.pb.clan.MedicItem> medicitemlist; // 药品信息

	public SOpenClanMedic() {
		medicitemlist = new java.util.ArrayList<fire.pb.clan.MedicItem>();
	}

	public SOpenClanMedic(int _selecttype_, int _buyitemnum_, java.util.ArrayList<fire.pb.clan.MedicItem> _medicitemlist_) {
		this.selecttype = _selecttype_;
		this.buyitemnum = _buyitemnum_;
		this.medicitemlist = _medicitemlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.MedicItem _v_ : medicitemlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(selecttype);
		_os_.marshal(buyitemnum);
		_os_.compact_uint32(medicitemlist.size());
		for (fire.pb.clan.MedicItem _v_ : medicitemlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		selecttype = _os_.unmarshal_int();
		buyitemnum = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.MedicItem _v_ = new fire.pb.clan.MedicItem();
			_v_.unmarshal(_os_);
			medicitemlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SOpenClanMedic) {
			SOpenClanMedic _o_ = (SOpenClanMedic)_o1_;
			if (selecttype != _o_.selecttype) return false;
			if (buyitemnum != _o_.buyitemnum) return false;
			if (!medicitemlist.equals(_o_.medicitemlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += selecttype;
		_h_ += buyitemnum;
		_h_ += medicitemlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(selecttype).append(",");
		_sb_.append(buyitemnum).append(",");
		_sb_.append(medicitemlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

