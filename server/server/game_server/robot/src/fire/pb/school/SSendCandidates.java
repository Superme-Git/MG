
package fire.pb.school;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendCandidates__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendCandidates extends __SSendCandidates__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810434;

	public int getType() {
		return 810434;
	}

	public int alreadyvote; // 1表示已经投票过,0表示本周还没投票
	public java.util.ArrayList<fire.pb.school.CandidateInfo> candidatelist;
	public long shouxikey;

	public SSendCandidates() {
		candidatelist = new java.util.ArrayList<fire.pb.school.CandidateInfo>();
	}

	public SSendCandidates(int _alreadyvote_, java.util.ArrayList<fire.pb.school.CandidateInfo> _candidatelist_, long _shouxikey_) {
		this.alreadyvote = _alreadyvote_;
		this.candidatelist = _candidatelist_;
		this.shouxikey = _shouxikey_;
	}

	public final boolean _validator_() {
		for (fire.pb.school.CandidateInfo _v_ : candidatelist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(alreadyvote);
		_os_.compact_uint32(candidatelist.size());
		for (fire.pb.school.CandidateInfo _v_ : candidatelist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(shouxikey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		alreadyvote = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.school.CandidateInfo _v_ = new fire.pb.school.CandidateInfo();
			_v_.unmarshal(_os_);
			candidatelist.add(_v_);
		}
		shouxikey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendCandidates) {
			SSendCandidates _o_ = (SSendCandidates)_o1_;
			if (alreadyvote != _o_.alreadyvote) return false;
			if (!candidatelist.equals(_o_.candidatelist)) return false;
			if (shouxikey != _o_.shouxikey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += alreadyvote;
		_h_ += candidatelist.hashCode();
		_h_ += (int)shouxikey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(alreadyvote).append(",");
		_sb_.append(candidatelist).append(",");
		_sb_.append(shouxikey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

