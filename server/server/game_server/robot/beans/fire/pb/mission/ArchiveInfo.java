
package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ArchiveInfo implements Marshal , Comparable<ArchiveInfo>{
	public int archiveid; // 历程id
	public int state; // 状态 0 未完成, 1 已经完成, 2 已经领奖

	public ArchiveInfo() {
	}

	public ArchiveInfo(int _archiveid_, int _state_) {
		this.archiveid = _archiveid_;
		this.state = _state_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(archiveid);
		_os_.marshal(state);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		archiveid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ArchiveInfo) {
			ArchiveInfo _o_ = (ArchiveInfo)_o1_;
			if (archiveid != _o_.archiveid) return false;
			if (state != _o_.state) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += archiveid;
		_h_ += state;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(archiveid).append(",");
		_sb_.append(state).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ArchiveInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = archiveid - _o_.archiveid;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

