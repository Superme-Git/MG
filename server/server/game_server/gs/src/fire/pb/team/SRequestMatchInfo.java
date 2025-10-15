
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestMatchInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestMatchInfo extends __SRequestMatchInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794513;

	public int getType() {
		return 794513;
	}

	public int teammatchnum; // 队伍匹配数量 by changhao
	public int playermatchnum; // 个人匹配数量 by changhao

	public SRequestMatchInfo() {
	}

	public SRequestMatchInfo(int _teammatchnum_, int _playermatchnum_) {
		this.teammatchnum = _teammatchnum_;
		this.playermatchnum = _playermatchnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(teammatchnum);
		_os_.marshal(playermatchnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teammatchnum = _os_.unmarshal_int();
		playermatchnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestMatchInfo) {
			SRequestMatchInfo _o_ = (SRequestMatchInfo)_o1_;
			if (teammatchnum != _o_.teammatchnum) return false;
			if (playermatchnum != _o_.playermatchnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += teammatchnum;
		_h_ += playermatchnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teammatchnum).append(",");
		_sb_.append(playermatchnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRequestMatchInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = teammatchnum - _o_.teammatchnum;
		if (0 != _c_) return _c_;
		_c_ = playermatchnum - _o_.playermatchnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

