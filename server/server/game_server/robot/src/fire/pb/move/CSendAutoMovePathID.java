
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSendAutoMovePathID__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSendAutoMovePathID extends __CSendAutoMovePathID__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790492;

	public int getType() {
		return 790492;
	}

	public int pathid; // 巡游ID
	public int pathid2; // 路线ID
	public int pathid3; // 路线id3 by changhao

	public CSendAutoMovePathID() {
	}

	public CSendAutoMovePathID(int _pathid_, int _pathid2_, int _pathid3_) {
		this.pathid = _pathid_;
		this.pathid2 = _pathid2_;
		this.pathid3 = _pathid3_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(pathid);
		_os_.marshal(pathid2);
		_os_.marshal(pathid3);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pathid = _os_.unmarshal_int();
		pathid2 = _os_.unmarshal_int();
		pathid3 = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSendAutoMovePathID) {
			CSendAutoMovePathID _o_ = (CSendAutoMovePathID)_o1_;
			if (pathid != _o_.pathid) return false;
			if (pathid2 != _o_.pathid2) return false;
			if (pathid3 != _o_.pathid3) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pathid;
		_h_ += pathid2;
		_h_ += pathid3;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pathid).append(",");
		_sb_.append(pathid2).append(",");
		_sb_.append(pathid3).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSendAutoMovePathID _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = pathid - _o_.pathid;
		if (0 != _c_) return _c_;
		_c_ = pathid2 - _o_.pathid2;
		if (0 != _c_) return _c_;
		_c_ = pathid3 - _o_.pathid3;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

