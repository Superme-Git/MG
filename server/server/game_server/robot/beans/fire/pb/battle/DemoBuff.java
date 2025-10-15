
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class DemoBuff implements Marshal , Comparable<DemoBuff>{
	public int fighterid;
	public int buffid;
	public int round; // -1为删除该buff，0为没有回合数的buff，>0为回合数

	public DemoBuff() {
	}

	public DemoBuff(int _fighterid_, int _buffid_, int _round_) {
		this.fighterid = _fighterid_;
		this.buffid = _buffid_;
		this.round = _round_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(fighterid);
		_os_.marshal(buffid);
		_os_.marshal(round);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		fighterid = _os_.unmarshal_int();
		buffid = _os_.unmarshal_int();
		round = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof DemoBuff) {
			DemoBuff _o_ = (DemoBuff)_o1_;
			if (fighterid != _o_.fighterid) return false;
			if (buffid != _o_.buffid) return false;
			if (round != _o_.round) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += fighterid;
		_h_ += buffid;
		_h_ += round;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(fighterid).append(",");
		_sb_.append(buffid).append(",");
		_sb_.append(round).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(DemoBuff _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = fighterid - _o_.fighterid;
		if (0 != _c_) return _c_;
		_c_ = buffid - _o_.buffid;
		if (0 != _c_) return _c_;
		_c_ = round - _o_.round;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

