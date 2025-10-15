
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetArchiveInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetArchiveInfo extends __SGetArchiveInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805541;

	public int getType() {
		return 805541;
	}

	public java.util.ArrayList<fire.pb.mission.ArchiveInfo> archiveinfos; // 历程信息

	public SGetArchiveInfo() {
		archiveinfos = new java.util.ArrayList<fire.pb.mission.ArchiveInfo>();
	}

	public SGetArchiveInfo(java.util.ArrayList<fire.pb.mission.ArchiveInfo> _archiveinfos_) {
		this.archiveinfos = _archiveinfos_;
	}

	public final boolean _validator_() {
		for (fire.pb.mission.ArchiveInfo _v_ : archiveinfos)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(archiveinfos.size());
		for (fire.pb.mission.ArchiveInfo _v_ : archiveinfos) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.mission.ArchiveInfo _v_ = new fire.pb.mission.ArchiveInfo();
			_v_.unmarshal(_os_);
			archiveinfos.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetArchiveInfo) {
			SGetArchiveInfo _o_ = (SGetArchiveInfo)_o1_;
			if (!archiveinfos.equals(_o_.archiveinfos)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += archiveinfos.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(archiveinfos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

