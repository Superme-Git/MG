
package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 任务结构
*/
public class MissionInfo implements Marshal , Comparable<MissionInfo>{
	public int missionid;
	public int missionstatus;
	public int missionvalue; // 任务状态值
	public int missionround; // 任务的环数
	public long dstnpckey;

	public MissionInfo() {
	}

	public MissionInfo(int _missionid_, int _missionstatus_, int _missionvalue_, int _missionround_, long _dstnpckey_) {
		this.missionid = _missionid_;
		this.missionstatus = _missionstatus_;
		this.missionvalue = _missionvalue_;
		this.missionround = _missionround_;
		this.dstnpckey = _dstnpckey_;
	}

	public final boolean _validator_() {
		if (missionid < 0) return false;
		if (missionstatus < 0) return false;
		if (dstnpckey < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(missionid);
		_os_.marshal(missionstatus);
		_os_.marshal(missionvalue);
		_os_.marshal(missionround);
		_os_.marshal(dstnpckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		missionid = _os_.unmarshal_int();
		missionstatus = _os_.unmarshal_int();
		missionvalue = _os_.unmarshal_int();
		missionround = _os_.unmarshal_int();
		dstnpckey = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MissionInfo) {
			MissionInfo _o_ = (MissionInfo)_o1_;
			if (missionid != _o_.missionid) return false;
			if (missionstatus != _o_.missionstatus) return false;
			if (missionvalue != _o_.missionvalue) return false;
			if (missionround != _o_.missionround) return false;
			if (dstnpckey != _o_.dstnpckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += missionid;
		_h_ += missionstatus;
		_h_ += missionvalue;
		_h_ += missionround;
		_h_ += (int)dstnpckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missionid).append(",");
		_sb_.append(missionstatus).append(",");
		_sb_.append(missionvalue).append(",");
		_sb_.append(missionround).append(",");
		_sb_.append(dstnpckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MissionInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = missionid - _o_.missionid;
		if (0 != _c_) return _c_;
		_c_ = missionstatus - _o_.missionstatus;
		if (0 != _c_) return _c_;
		_c_ = missionvalue - _o_.missionvalue;
		if (0 != _c_) return _c_;
		_c_ = missionround - _o_.missionround;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(dstnpckey - _o_.dstnpckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

}

