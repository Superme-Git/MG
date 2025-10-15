
package fire.pb;

import gnet.link.Role;

import java.util.HashMap;
import java.util.Map;

import xbean.AUUserInfo;
import fire.log.RemoteLogParam;
import fire.pb.main.ConfigManager;
import fire.pb.util.InetAddressUtil;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPingStat__ extends mkio.Protocol { }

/** 客户端通告服务器10分钟内ping值 1分钟一个PingStatEntry
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPingStat extends __CPingStat__ {
	@Override
	protected void process() {
		// protocol handle
		Role role = gnet.link.Onlines.getInstance().find(this);
		if(role == null)
			return;
		AUUserInfo auUserInfo = xtable.Auuserinfo.select(role.getUserid());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(RemoteLogParam.FROM, ConfigManager.getGsZoneId());
		params.put(RemoteLogParam.ACCOUNTID, role.getUserid());
		params.put(RemoteLogParam.ACCOUNT, auUserInfo!=null?auUserInfo.getUsername():"unknown");
		params.put(RemoteLogParam.ROLEID, role.getRoleid());
		params.put(RemoteLogParam.NAME, xtable.Properties.selectRolename(role.getRoleid()));
		params.put(RemoteLogParam.PEER,InetAddressUtil.ipInt2String(auUserInfo!= null?auUserInfo.getLoginip():0));
		params.put(RemoteLogParam.MAXTIME,pingstats._max);
		params.put(RemoteLogParam.MINTIME,pingstats._min);
		params.put(RemoteLogParam.AVGTIME,pingstats._avg);
		params.put(RemoteLogParam.LOSSRATE, losspercent);
		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786490;

	public int getType() {
		return 786490;
	}

	public long beginstamp; // 开始时间戳
	public fire.pb.PingStatEntry pingstats; // 10分钟内统计数据
	public short losspercent; // 丢包率（losspercent%100）

	public CPingStat() {
		pingstats = new fire.pb.PingStatEntry();
	}

	public CPingStat(long _beginstamp_, fire.pb.PingStatEntry _pingstats_, short _losspercent_) {
		this.beginstamp = _beginstamp_;
		this.pingstats = _pingstats_;
		this.losspercent = _losspercent_;
	}

	public final boolean _validator_() {
		if (!pingstats._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(beginstamp);
		_os_.marshal(pingstats);
		_os_.marshal(losspercent);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		beginstamp = _os_.unmarshal_long();
		pingstats.unmarshal(_os_);
		losspercent = _os_.unmarshal_short();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPingStat) {
			CPingStat _o_ = (CPingStat)_o1_;
			if (beginstamp != _o_.beginstamp) return false;
			if (!pingstats.equals(_o_.pingstats)) return false;
			if (losspercent != _o_.losspercent) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)beginstamp;
		_h_ += pingstats.hashCode();
		_h_ += losspercent;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(beginstamp).append(",");
		_sb_.append(pingstats).append(",");
		_sb_.append(losspercent).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPingStat _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(beginstamp - _o_.beginstamp);
		if (0 != _c_) return _c_;
		_c_ = pingstats.compareTo(_o_.pingstats);
		if (0 != _c_) return _c_;
		_c_ = losspercent - _o_.losspercent;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

