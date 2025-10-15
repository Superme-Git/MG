
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestImpeachMentView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestImpeachMentView extends __SRequestImpeachMentView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808527;

	public int getType() {
		return 808527;
	}

	public byte impeachstate; // 弹劾状态  0发起弹劾    1响应弹劾
	public short maxnum; // 弹劾成功需要的人数
	public java.lang.String impeachname; // 发起弹劾人的名称
	public long impeachtime; // 发起弹劾时间
	public short curnum; // 当前响应人数

	public SRequestImpeachMentView() {
		impeachname = "";
	}

	public SRequestImpeachMentView(byte _impeachstate_, short _maxnum_, java.lang.String _impeachname_, long _impeachtime_, short _curnum_) {
		this.impeachstate = _impeachstate_;
		this.maxnum = _maxnum_;
		this.impeachname = _impeachname_;
		this.impeachtime = _impeachtime_;
		this.curnum = _curnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(impeachstate);
		_os_.marshal(maxnum);
		_os_.marshal(impeachname, "UTF-16LE");
		_os_.marshal(impeachtime);
		_os_.marshal(curnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		impeachstate = _os_.unmarshal_byte();
		maxnum = _os_.unmarshal_short();
		impeachname = _os_.unmarshal_String("UTF-16LE");
		impeachtime = _os_.unmarshal_long();
		curnum = _os_.unmarshal_short();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestImpeachMentView) {
			SRequestImpeachMentView _o_ = (SRequestImpeachMentView)_o1_;
			if (impeachstate != _o_.impeachstate) return false;
			if (maxnum != _o_.maxnum) return false;
			if (!impeachname.equals(_o_.impeachname)) return false;
			if (impeachtime != _o_.impeachtime) return false;
			if (curnum != _o_.curnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)impeachstate;
		_h_ += maxnum;
		_h_ += impeachname.hashCode();
		_h_ += (int)impeachtime;
		_h_ += curnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(impeachstate).append(",");
		_sb_.append(maxnum).append(",");
		_sb_.append("T").append(impeachname.length()).append(",");
		_sb_.append(impeachtime).append(",");
		_sb_.append(curnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

