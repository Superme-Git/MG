
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSearchPrentice__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSearchPrentice extends __SSearchPrentice__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816447;

	public int getType() {
		return 816447;
	}

	public int pageid; // 页数
	public int totalpage; // 总页数
	public java.util.ArrayList<fire.pb.master.Prentice> prentice; // 徒弟

	public SSearchPrentice() {
		prentice = new java.util.ArrayList<fire.pb.master.Prentice>();
	}

	public SSearchPrentice(int _pageid_, int _totalpage_, java.util.ArrayList<fire.pb.master.Prentice> _prentice_) {
		this.pageid = _pageid_;
		this.totalpage = _totalpage_;
		this.prentice = _prentice_;
	}

	public final boolean _validator_() {
		for (fire.pb.master.Prentice _v_ : prentice)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(pageid);
		_os_.marshal(totalpage);
		_os_.compact_uint32(prentice.size());
		for (fire.pb.master.Prentice _v_ : prentice) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pageid = _os_.unmarshal_int();
		totalpage = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.master.Prentice _v_ = new fire.pb.master.Prentice();
			_v_.unmarshal(_os_);
			prentice.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSearchPrentice) {
			SSearchPrentice _o_ = (SSearchPrentice)_o1_;
			if (pageid != _o_.pageid) return false;
			if (totalpage != _o_.totalpage) return false;
			if (!prentice.equals(_o_.prentice)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pageid;
		_h_ += totalpage;
		_h_ += prentice.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pageid).append(",");
		_sb_.append(totalpage).append(",");
		_sb_.append(prentice).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

