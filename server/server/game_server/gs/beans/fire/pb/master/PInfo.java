
package fire.pb.master;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class PInfo implements Marshal {
	public fire.pb.master.PBaseInfo prentice; // 徒弟的基本信息
	public java.util.HashMap<Integer,fire.pb.master.Achive> achivemap; // 徒弟的成就

	public PInfo() {
		prentice = new fire.pb.master.PBaseInfo();
		achivemap = new java.util.HashMap<Integer,fire.pb.master.Achive>();
	}

	public PInfo(fire.pb.master.PBaseInfo _prentice_, java.util.HashMap<Integer,fire.pb.master.Achive> _achivemap_) {
		this.prentice = _prentice_;
		this.achivemap = _achivemap_;
	}

	public final boolean _validator_() {
		if (!prentice._validator_()) return false;
		for (java.util.Map.Entry<Integer, fire.pb.master.Achive> _e_ : achivemap.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(prentice);
		_os_.compact_uint32(achivemap.size());
		for (java.util.Map.Entry<Integer, fire.pb.master.Achive> _e_ : achivemap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		prentice.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.master.Achive _v_ = new fire.pb.master.Achive();
			_v_.unmarshal(_os_);
			achivemap.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PInfo) {
			PInfo _o_ = (PInfo)_o1_;
			if (!prentice.equals(_o_.prentice)) return false;
			if (!achivemap.equals(_o_.achivemap)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += prentice.hashCode();
		_h_ += achivemap.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prentice).append(",");
		_sb_.append(achivemap).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

