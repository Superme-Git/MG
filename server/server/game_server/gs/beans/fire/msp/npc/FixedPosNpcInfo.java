
package fire.msp.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class FixedPosNpcInfo implements Marshal , Comparable<FixedPosNpcInfo>{
	public long scene;
	public int posx;
	public int posy;
	public int npcid;
	public long npckey;

	public FixedPosNpcInfo() {
	}

	public FixedPosNpcInfo(long _scene_, int _posx_, int _posy_, int _npcid_, long _npckey_) {
		this.scene = _scene_;
		this.posx = _posx_;
		this.posy = _posy_;
		this.npcid = _npcid_;
		this.npckey = _npckey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(scene);
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.marshal(npcid);
		_os_.marshal(npckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		scene = _os_.unmarshal_long();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		npcid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof FixedPosNpcInfo) {
			FixedPosNpcInfo _o_ = (FixedPosNpcInfo)_o1_;
			if (scene != _o_.scene) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (npcid != _o_.npcid) return false;
			if (npckey != _o_.npckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)scene;
		_h_ += posx;
		_h_ += posy;
		_h_ += npcid;
		_h_ += (int)npckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(scene).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(FixedPosNpcInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(scene - _o_.scene);
		if (0 != _c_) return _c_;
		_c_ = posx - _o_.posx;
		if (0 != _c_) return _c_;
		_c_ = posy - _o_.posy;
		if (0 != _c_) return _c_;
		_c_ = npcid - _o_.npcid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

}

