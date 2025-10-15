
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestAsApprentice__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestAsApprentice extends __SRequestAsApprentice__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816440;

	public int getType() {
		return 816440;
	}

	public long prenticeid; // 徒弟id
	public java.lang.String prenticename; // 徒弟名称
	public int school; // 职业
	public int level; // 等级
	public java.lang.String requestword; // 留言

	public SRequestAsApprentice() {
		prenticename = "";
		requestword = "";
	}

	public SRequestAsApprentice(long _prenticeid_, java.lang.String _prenticename_, int _school_, int _level_, java.lang.String _requestword_) {
		this.prenticeid = _prenticeid_;
		this.prenticename = _prenticename_;
		this.school = _school_;
		this.level = _level_;
		this.requestword = _requestword_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(prenticeid);
		_os_.marshal(prenticename, "UTF-16LE");
		_os_.marshal(school);
		_os_.marshal(level);
		_os_.marshal(requestword, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		prenticeid = _os_.unmarshal_long();
		prenticename = _os_.unmarshal_String("UTF-16LE");
		school = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		requestword = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestAsApprentice) {
			SRequestAsApprentice _o_ = (SRequestAsApprentice)_o1_;
			if (prenticeid != _o_.prenticeid) return false;
			if (!prenticename.equals(_o_.prenticename)) return false;
			if (school != _o_.school) return false;
			if (level != _o_.level) return false;
			if (!requestword.equals(_o_.requestword)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)prenticeid;
		_h_ += prenticename.hashCode();
		_h_ += school;
		_h_ += level;
		_h_ += requestword.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prenticeid).append(",");
		_sb_.append("T").append(prenticename.length()).append(",");
		_sb_.append(school).append(",");
		_sb_.append(level).append(",");
		_sb_.append("T").append(requestword.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

