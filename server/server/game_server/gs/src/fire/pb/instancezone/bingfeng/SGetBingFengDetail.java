
package fire.pb.instancezone.bingfeng;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetBingFengDetail__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetBingFengDetail extends __SGetBingFengDetail__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 804566;

	public int getType() {
		return 804566;
	}

	public java.lang.String rolename;
	public int usetime;
	public short stagestate; // 自己是否通关, 0 未通关, 1 已通关
	public int myusetime;

	public SGetBingFengDetail() {
		rolename = "";
	}

	public SGetBingFengDetail(java.lang.String _rolename_, int _usetime_, short _stagestate_, int _myusetime_) {
		this.rolename = _rolename_;
		this.usetime = _usetime_;
		this.stagestate = _stagestate_;
		this.myusetime = _myusetime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(usetime);
		_os_.marshal(stagestate);
		_os_.marshal(myusetime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolename = _os_.unmarshal_String("UTF-16LE");
		usetime = _os_.unmarshal_int();
		stagestate = _os_.unmarshal_short();
		myusetime = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetBingFengDetail) {
			SGetBingFengDetail _o_ = (SGetBingFengDetail)_o1_;
			if (!rolename.equals(_o_.rolename)) return false;
			if (usetime != _o_.usetime) return false;
			if (stagestate != _o_.stagestate) return false;
			if (myusetime != _o_.myusetime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolename.hashCode();
		_h_ += usetime;
		_h_ += stagestate;
		_h_ += myusetime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(usetime).append(",");
		_sb_.append(stagestate).append(",");
		_sb_.append(myusetime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

