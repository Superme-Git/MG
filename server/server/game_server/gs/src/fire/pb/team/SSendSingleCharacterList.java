
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendSingleCharacterList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendSingleCharacterList extends __SSendSingleCharacterList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794472;

	public int getType() {
		return 794472;
	}

	public java.util.LinkedList<fire.pb.team.SingleCharacterBasic> singlecharacterlist;

	public SSendSingleCharacterList() {
		singlecharacterlist = new java.util.LinkedList<fire.pb.team.SingleCharacterBasic>();
	}

	public SSendSingleCharacterList(java.util.LinkedList<fire.pb.team.SingleCharacterBasic> _singlecharacterlist_) {
		this.singlecharacterlist = _singlecharacterlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.team.SingleCharacterBasic _v_ : singlecharacterlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(singlecharacterlist.size());
		for (fire.pb.team.SingleCharacterBasic _v_ : singlecharacterlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.team.SingleCharacterBasic _v_ = new fire.pb.team.SingleCharacterBasic();
			_v_.unmarshal(_os_);
			singlecharacterlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendSingleCharacterList) {
			SSendSingleCharacterList _o_ = (SSendSingleCharacterList)_o1_;
			if (!singlecharacterlist.equals(_o_.singlecharacterlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += singlecharacterlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(singlecharacterlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

