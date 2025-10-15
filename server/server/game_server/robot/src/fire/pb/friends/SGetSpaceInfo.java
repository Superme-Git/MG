
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetSpaceInfo__ extends mkio.Protocol { }

/** 获取某角色空间数据结果
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetSpaceInfo extends __SGetSpaceInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806640;

	public int getType() {
		return 806640;
	}

	public int giftnum; // 礼物数量
	public int popularity; // 人气
	public int revnum; // 收到礼物数量

	public SGetSpaceInfo() {
	}

	public SGetSpaceInfo(int _giftnum_, int _popularity_, int _revnum_) {
		this.giftnum = _giftnum_;
		this.popularity = _popularity_;
		this.revnum = _revnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(giftnum);
		_os_.marshal(popularity);
		_os_.marshal(revnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		giftnum = _os_.unmarshal_int();
		popularity = _os_.unmarshal_int();
		revnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetSpaceInfo) {
			SGetSpaceInfo _o_ = (SGetSpaceInfo)_o1_;
			if (giftnum != _o_.giftnum) return false;
			if (popularity != _o_.popularity) return false;
			if (revnum != _o_.revnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += giftnum;
		_h_ += popularity;
		_h_ += revnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(giftnum).append(",");
		_sb_.append(popularity).append(",");
		_sb_.append(revnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SGetSpaceInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = giftnum - _o_.giftnum;
		if (0 != _c_) return _c_;
		_c_ = popularity - _o_.popularity;
		if (0 != _c_) return _c_;
		_c_ = revnum - _o_.revnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

