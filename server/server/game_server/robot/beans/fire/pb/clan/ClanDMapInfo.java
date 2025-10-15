
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ClanDMapInfo implements Marshal , Comparable<ClanDMapInfo>{
	public int basemapid;

	public ClanDMapInfo() {
	}

	public ClanDMapInfo(int _basemapid_) {
		this.basemapid = _basemapid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(basemapid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		basemapid = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ClanDMapInfo) {
			ClanDMapInfo _o_ = (ClanDMapInfo)_o1_;
			if (basemapid != _o_.basemapid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += basemapid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(basemapid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ClanDMapInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = basemapid - _o_.basemapid;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

