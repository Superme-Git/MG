
package fire.pb.fushi.payday;

import fire.pb.fushi.DayPayManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CQueryConsumeDayPay__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 是否扣日卡 by changhao
 */
public class CQueryConsumeDayPay extends __CQueryConsumeDayPay__ {
	@Override
	protected void process() {
		
		if (yesorno == 1)
		{			
			final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
			if (roleid < 0)
				return;
			mkdb.Procedure queryconsumedaypay = new mkdb.Procedure()
			{
				@Override
				protected boolean process()
				{		
					gnet.link.Role linkrole = gnet.link.Onlines.getInstance().find(roleid);
					if (linkrole != null)
					{
						int userid = linkrole.getUserid();
						if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
						{
							fire.pb.fushi.DayPayManager.getInstance().ProcessDayPay(userid, roleid);	
						}					
					}
					
					return true;
				}
			};
			
			queryconsumedaypay.submit();	
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812595;

	public int getType() {
		return 812595;
	}

	public int yesorno; // 0是没有1是消耗 by changhao

	public CQueryConsumeDayPay() {
	}

	public CQueryConsumeDayPay(int _yesorno_) {
		this.yesorno = _yesorno_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(yesorno);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		yesorno = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CQueryConsumeDayPay) {
			CQueryConsumeDayPay _o_ = (CQueryConsumeDayPay)_o1_;
			if (yesorno != _o_.yesorno) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += yesorno;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(yesorno).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CQueryConsumeDayPay _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = yesorno - _o_.yesorno;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

