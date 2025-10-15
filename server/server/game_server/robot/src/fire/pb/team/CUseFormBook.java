
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CUseFormBook__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CUseFormBook extends __CUseFormBook__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794553;

	public int getType() {
		return 794553;
	}

	public int formationid; // 光环ID by changhao
	public java.util.LinkedList<fire.pb.team.UseFormBook> listbook; // 使用的光环书 by changhao

	public CUseFormBook() {
		listbook = new java.util.LinkedList<fire.pb.team.UseFormBook>();
	}

	public CUseFormBook(int _formationid_, java.util.LinkedList<fire.pb.team.UseFormBook> _listbook_) {
		this.formationid = _formationid_;
		this.listbook = _listbook_;
	}

	public final boolean _validator_() {
		for (fire.pb.team.UseFormBook _v_ : listbook)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(formationid);
		_os_.compact_uint32(listbook.size());
		for (fire.pb.team.UseFormBook _v_ : listbook) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		formationid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.team.UseFormBook _v_ = new fire.pb.team.UseFormBook();
			_v_.unmarshal(_os_);
			listbook.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CUseFormBook) {
			CUseFormBook _o_ = (CUseFormBook)_o1_;
			if (formationid != _o_.formationid) return false;
			if (!listbook.equals(_o_.listbook)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += formationid;
		_h_ += listbook.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(formationid).append(",");
		_sb_.append(listbook).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

