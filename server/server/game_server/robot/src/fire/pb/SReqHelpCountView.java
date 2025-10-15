
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqHelpCountView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqHelpCountView extends __SReqHelpCountView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786533;

	public int getType() {
		return 786533;
	}

	public long expvalue;
	public long expvaluemax;
	public int shengwangvalue;
	public int shengwangvaluemax;
	public int factionvalue;
	public int factionvaluemax;
	public int helpgiveitemnum; // 援助物品次数
	public int helpgiveitemnummax; // 援助物品次数最大值
	public int helpitemnum; // 求助物品次数
	public int helpitemnummax; // 求助物品次数最大值

	public SReqHelpCountView() {
	}

	public SReqHelpCountView(long _expvalue_, long _expvaluemax_, int _shengwangvalue_, int _shengwangvaluemax_, int _factionvalue_, int _factionvaluemax_, int _helpgiveitemnum_, int _helpgiveitemnummax_, int _helpitemnum_, int _helpitemnummax_) {
		this.expvalue = _expvalue_;
		this.expvaluemax = _expvaluemax_;
		this.shengwangvalue = _shengwangvalue_;
		this.shengwangvaluemax = _shengwangvaluemax_;
		this.factionvalue = _factionvalue_;
		this.factionvaluemax = _factionvaluemax_;
		this.helpgiveitemnum = _helpgiveitemnum_;
		this.helpgiveitemnummax = _helpgiveitemnummax_;
		this.helpitemnum = _helpitemnum_;
		this.helpitemnummax = _helpitemnummax_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(expvalue);
		_os_.marshal(expvaluemax);
		_os_.marshal(shengwangvalue);
		_os_.marshal(shengwangvaluemax);
		_os_.marshal(factionvalue);
		_os_.marshal(factionvaluemax);
		_os_.marshal(helpgiveitemnum);
		_os_.marshal(helpgiveitemnummax);
		_os_.marshal(helpitemnum);
		_os_.marshal(helpitemnummax);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		expvalue = _os_.unmarshal_long();
		expvaluemax = _os_.unmarshal_long();
		shengwangvalue = _os_.unmarshal_int();
		shengwangvaluemax = _os_.unmarshal_int();
		factionvalue = _os_.unmarshal_int();
		factionvaluemax = _os_.unmarshal_int();
		helpgiveitemnum = _os_.unmarshal_int();
		helpgiveitemnummax = _os_.unmarshal_int();
		helpitemnum = _os_.unmarshal_int();
		helpitemnummax = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqHelpCountView) {
			SReqHelpCountView _o_ = (SReqHelpCountView)_o1_;
			if (expvalue != _o_.expvalue) return false;
			if (expvaluemax != _o_.expvaluemax) return false;
			if (shengwangvalue != _o_.shengwangvalue) return false;
			if (shengwangvaluemax != _o_.shengwangvaluemax) return false;
			if (factionvalue != _o_.factionvalue) return false;
			if (factionvaluemax != _o_.factionvaluemax) return false;
			if (helpgiveitemnum != _o_.helpgiveitemnum) return false;
			if (helpgiveitemnummax != _o_.helpgiveitemnummax) return false;
			if (helpitemnum != _o_.helpitemnum) return false;
			if (helpitemnummax != _o_.helpitemnummax) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)expvalue;
		_h_ += (int)expvaluemax;
		_h_ += shengwangvalue;
		_h_ += shengwangvaluemax;
		_h_ += factionvalue;
		_h_ += factionvaluemax;
		_h_ += helpgiveitemnum;
		_h_ += helpgiveitemnummax;
		_h_ += helpitemnum;
		_h_ += helpitemnummax;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(expvalue).append(",");
		_sb_.append(expvaluemax).append(",");
		_sb_.append(shengwangvalue).append(",");
		_sb_.append(shengwangvaluemax).append(",");
		_sb_.append(factionvalue).append(",");
		_sb_.append(factionvaluemax).append(",");
		_sb_.append(helpgiveitemnum).append(",");
		_sb_.append(helpgiveitemnummax).append(",");
		_sb_.append(helpitemnum).append(",");
		_sb_.append(helpitemnummax).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SReqHelpCountView _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(expvalue - _o_.expvalue);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(expvaluemax - _o_.expvaluemax);
		if (0 != _c_) return _c_;
		_c_ = shengwangvalue - _o_.shengwangvalue;
		if (0 != _c_) return _c_;
		_c_ = shengwangvaluemax - _o_.shengwangvaluemax;
		if (0 != _c_) return _c_;
		_c_ = factionvalue - _o_.factionvalue;
		if (0 != _c_) return _c_;
		_c_ = factionvaluemax - _o_.factionvaluemax;
		if (0 != _c_) return _c_;
		_c_ = helpgiveitemnum - _o_.helpgiveitemnum;
		if (0 != _c_) return _c_;
		_c_ = helpgiveitemnummax - _o_.helpgiveitemnummax;
		if (0 != _c_) return _c_;
		_c_ = helpitemnum - _o_.helpitemnum;
		if (0 != _c_) return _c_;
		_c_ = helpitemnummax - _o_.helpitemnummax;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

