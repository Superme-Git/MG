
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSearchMaster__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSearchMaster extends __SSearchMaster__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816438;

	public int getType() {
		return 816438;
	}

	public int pageid; // 页数
	public int totalpage; // 总页数
	public java.util.ArrayList<fire.pb.master.Master> masters; // 师傅

	public SSearchMaster() {
		masters = new java.util.ArrayList<fire.pb.master.Master>();
	}

	public SSearchMaster(int _pageid_, int _totalpage_, java.util.ArrayList<fire.pb.master.Master> _masters_) {
		this.pageid = _pageid_;
		this.totalpage = _totalpage_;
		this.masters = _masters_;
	}

	public final boolean _validator_() {
		for (fire.pb.master.Master _v_ : masters)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(pageid);
		_os_.marshal(totalpage);
		_os_.compact_uint32(masters.size());
		for (fire.pb.master.Master _v_ : masters) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pageid = _os_.unmarshal_int();
		totalpage = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.master.Master _v_ = new fire.pb.master.Master();
			_v_.unmarshal(_os_);
			masters.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSearchMaster) {
			SSearchMaster _o_ = (SSearchMaster)_o1_;
			if (pageid != _o_.pageid) return false;
			if (totalpage != _o_.totalpage) return false;
			if (!masters.equals(_o_.masters)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pageid;
		_h_ += totalpage;
		_h_ += masters.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pageid).append(",");
		_sb_.append(totalpage).append(",");
		_sb_.append(masters).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

