
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendImpExamStart__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendImpExamStart extends __SSendImpExamStart__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795465;

	public int getType() {
		return 795465;
	}

	public long remaintime; // 距离活动结束时间毫秒
	public byte impexamtype;
	public int historymaxright; // 历史最多答对题目数
	public long historymintime; // 历史最短用时

	public SSendImpExamStart() {
	}

	public SSendImpExamStart(long _remaintime_, byte _impexamtype_, int _historymaxright_, long _historymintime_) {
		this.remaintime = _remaintime_;
		this.impexamtype = _impexamtype_;
		this.historymaxright = _historymaxright_;
		this.historymintime = _historymintime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(remaintime);
		_os_.marshal(impexamtype);
		_os_.marshal(historymaxright);
		_os_.marshal(historymintime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		remaintime = _os_.unmarshal_long();
		impexamtype = _os_.unmarshal_byte();
		historymaxright = _os_.unmarshal_int();
		historymintime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendImpExamStart) {
			SSendImpExamStart _o_ = (SSendImpExamStart)_o1_;
			if (remaintime != _o_.remaintime) return false;
			if (impexamtype != _o_.impexamtype) return false;
			if (historymaxright != _o_.historymaxright) return false;
			if (historymintime != _o_.historymintime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)remaintime;
		_h_ += (int)impexamtype;
		_h_ += historymaxright;
		_h_ += (int)historymintime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(remaintime).append(",");
		_sb_.append(impexamtype).append(",");
		_sb_.append(historymaxright).append(",");
		_sb_.append(historymintime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSendImpExamStart _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(remaintime - _o_.remaintime);
		if (0 != _c_) return _c_;
		_c_ = impexamtype - _o_.impexamtype;
		if (0 != _c_) return _c_;
		_c_ = historymaxright - _o_.historymaxright;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(historymintime - _o_.historymintime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

