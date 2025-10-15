
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetArchiveAward__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetArchiveAward extends __CGetArchiveAward__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid <= 0) {
			return;
		}
		
		fire.pb.course.CourseManager.getInstance().getArchiveAward(roleid, archiveid);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805542;

	public int getType() {
		return 805542;
	}

	public int archiveid; // 历程id

	public CGetArchiveAward() {
	}

	public CGetArchiveAward(int _archiveid_) {
		this.archiveid = _archiveid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(archiveid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		archiveid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetArchiveAward) {
			CGetArchiveAward _o_ = (CGetArchiveAward)_o1_;
			if (archiveid != _o_.archiveid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += archiveid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(archiveid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetArchiveAward _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = archiveid - _o_.archiveid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

