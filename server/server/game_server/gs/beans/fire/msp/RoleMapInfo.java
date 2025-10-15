
package fire.msp;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleMapInfo implements Marshal , Comparable<RoleMapInfo>{
	public long sceneid;
	public int posx;
	public int posy;
	public int posz;

	public RoleMapInfo() {
	}

	public RoleMapInfo(long _sceneid_, int _posx_, int _posy_, int _posz_) {
		this.sceneid = _sceneid_;
		this.posx = _posx_;
		this.posy = _posy_;
		this.posz = _posz_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(sceneid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.marshal(posz);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		sceneid = _os_.unmarshal_long();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		posz = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleMapInfo) {
			RoleMapInfo _o_ = (RoleMapInfo)_o1_;
			if (sceneid != _o_.sceneid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (posz != _o_.posz) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)sceneid;
		_h_ += posx;
		_h_ += posy;
		_h_ += posz;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(sceneid).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(posz).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(RoleMapInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(sceneid - _o_.sceneid);
		if (0 != _c_) return _c_;
		_c_ = posx - _o_.posx;
		if (0 != _c_) return _c_;
		_c_ = posy - _o_.posy;
		if (0 != _c_) return _c_;
		_c_ = posz - _o_.posz;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

