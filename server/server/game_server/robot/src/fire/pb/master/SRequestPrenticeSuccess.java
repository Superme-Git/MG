
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestPrenticeSuccess__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestPrenticeSuccess extends __SRequestPrenticeSuccess__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816442;

	public int getType() {
		return 816442;
	}

	public long masterid; // 师傅的id
	public java.lang.String mastername; // 师父名称
	public int binitiative; // 是否是主动请求当徒弟，1为主动请求，0为师父申请收徒，之所以区分，是因为提示信息不一样

	public SRequestPrenticeSuccess() {
		mastername = "";
	}

	public SRequestPrenticeSuccess(long _masterid_, java.lang.String _mastername_, int _binitiative_) {
		this.masterid = _masterid_;
		this.mastername = _mastername_;
		this.binitiative = _binitiative_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(masterid);
		_os_.marshal(mastername, "UTF-16LE");
		_os_.marshal(binitiative);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		masterid = _os_.unmarshal_long();
		mastername = _os_.unmarshal_String("UTF-16LE");
		binitiative = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestPrenticeSuccess) {
			SRequestPrenticeSuccess _o_ = (SRequestPrenticeSuccess)_o1_;
			if (masterid != _o_.masterid) return false;
			if (!mastername.equals(_o_.mastername)) return false;
			if (binitiative != _o_.binitiative) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)masterid;
		_h_ += mastername.hashCode();
		_h_ += binitiative;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(masterid).append(",");
		_sb_.append("T").append(mastername.length()).append(",");
		_sb_.append(binitiative).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

