
package fire.pb.circletask;



import fire.pb.circletask.SpecialQuestState;
import fire.pb.fushi.FushiManager;
import fire.pb.npc.NpcServiceManager;
import fire.pb.npc.SNpcServiceMapping;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRenXingCircleTask__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRenXingCircleTask extends __CRenXingCircleTask__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		/*
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			boolean ok = fire.pb.fushi.DayPayManager.getInstance().CheckDayPayWithLevel(roleid);
			if (ok == false)
			{
				FushiManager.logger.error("CRenXingCircleTask: 没有点卡");
				return;
			}
		}
		*/			
		
		SNpcServiceMapping conf = NpcServiceManager.getInstance().getServiceMappingByServiceID(serviceid);
		if(null == conf)
			return;
		final int userid = ((gnet.link.Dispatch) this.getContext()).userid;
		new RenXingCircleTaskProc(userid, roleid, conf.getParam1(), SpecialQuestState.DONE, moneytype).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807451;

	public int getType() {
		return 807451;
	}

	public int serviceid; // 该服务id
	public int moneytype; // 货币类型

	public CRenXingCircleTask() {
	}

	public CRenXingCircleTask(int _serviceid_, int _moneytype_) {
		this.serviceid = _serviceid_;
		this.moneytype = _moneytype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(serviceid);
		_os_.marshal(moneytype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		serviceid = _os_.unmarshal_int();
		moneytype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRenXingCircleTask) {
			CRenXingCircleTask _o_ = (CRenXingCircleTask)_o1_;
			if (serviceid != _o_.serviceid) return false;
			if (moneytype != _o_.moneytype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += serviceid;
		_h_ += moneytype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(serviceid).append(",");
		_sb_.append(moneytype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRenXingCircleTask _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = serviceid - _o_.serviceid;
		if (0 != _c_) return _c_;
		_c_ = moneytype - _o_.moneytype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

