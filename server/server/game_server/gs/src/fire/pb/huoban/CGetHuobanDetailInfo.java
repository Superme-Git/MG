
package fire.pb.huoban;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetHuobanDetailInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetHuobanDetailInfo extends __CGetHuobanDetailInfo__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId == -1)
			return;
		
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				HuoBanColumn huobanCol = HuoBanColumn.getHuoBanColumn(roleId, false);
				if (huobanCol == null)
					return false;
				HuoBanDetailInfo huobanDetailInfo = huobanCol.getHuoBanDetailInfo(huobanid);
				if (huobanDetailInfo == null)
					return false;
				
				psendWhileCommit(roleId, new SHuobanDetail(huobanDetailInfo));
				
				return true;
			};
			
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818834;

	public int getType() {
		return 818834;
	}

	public int huobanid; // 我的伙伴的key

	public CGetHuobanDetailInfo() {
	}

	public CGetHuobanDetailInfo(int _huobanid_) {
		this.huobanid = _huobanid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(huobanid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		huobanid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetHuobanDetailInfo) {
			CGetHuobanDetailInfo _o_ = (CGetHuobanDetailInfo)_o1_;
			if (huobanid != _o_.huobanid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += huobanid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(huobanid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetHuobanDetailInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = huobanid - _o_.huobanid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

