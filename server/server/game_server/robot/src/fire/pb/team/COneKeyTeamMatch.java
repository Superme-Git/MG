
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __COneKeyTeamMatch__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class COneKeyTeamMatch extends __COneKeyTeamMatch__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794498;

	public int getType() {
		return 794498;
	}

	public int channeltype; // 频道类型 14组队申请 1当前频道 4帮派 5世界频道 by changhao
	public java.lang.String text; // 发送的文本 by changhao

	public COneKeyTeamMatch() {
		text = "";
	}

	public COneKeyTeamMatch(int _channeltype_, java.lang.String _text_) {
		this.channeltype = _channeltype_;
		this.text = _text_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(channeltype);
		_os_.marshal(text, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		channeltype = _os_.unmarshal_int();
		text = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof COneKeyTeamMatch) {
			COneKeyTeamMatch _o_ = (COneKeyTeamMatch)_o1_;
			if (channeltype != _o_.channeltype) return false;
			if (!text.equals(_o_.text)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += channeltype;
		_h_ += text.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(channeltype).append(",");
		_sb_.append("T").append(text.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

