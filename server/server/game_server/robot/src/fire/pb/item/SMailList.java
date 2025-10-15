
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SMailList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SMailList extends __SMailList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787700;

	public int getType() {
		return 787700;
	}

	public java.util.ArrayList<fire.pb.item.MailInfo> maillist; // 邮件列表

	public SMailList() {
		maillist = new java.util.ArrayList<fire.pb.item.MailInfo>();
	}

	public SMailList(java.util.ArrayList<fire.pb.item.MailInfo> _maillist_) {
		this.maillist = _maillist_;
	}

	public final boolean _validator_() {
		for (fire.pb.item.MailInfo _v_ : maillist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(maillist.size());
		for (fire.pb.item.MailInfo _v_ : maillist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.item.MailInfo _v_ = new fire.pb.item.MailInfo();
			_v_.unmarshal(_os_);
			maillist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SMailList) {
			SMailList _o_ = (SMailList)_o1_;
			if (!maillist.equals(_o_.maillist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += maillist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(maillist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

