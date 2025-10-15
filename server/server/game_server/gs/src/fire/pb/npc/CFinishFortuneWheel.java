
package fire.pb.npc;

import fire.pb.circletask.CircleTaskManager;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CFinishFortuneWheel__ extends mkio.Protocol { }

/** 客户端请求领奖
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CFinishFortuneWheel extends __CFinishFortuneWheel__ {
	@Override
	protected void process() {
		// protocol handle
		final long	roleid = gnet.link.Onlines.getInstance().findRoleid(this);
        if(roleid<0) return;
    	fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
    	Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		int masterid = CircleTaskManager.getInstance().getMasterMap().get(prole.getSchool()).masterid;
		if (npc != null && npc.getNpcID() == masterid){
		}
        new PFinishFortuneWheel(roleid,npckey,serviceid,1).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795446;

	public int getType() {
		return 795446;
	}

	public long npckey; // npckey为npc的唯一ID
	public int serviceid; // 当前转盘服务id

	public CFinishFortuneWheel() {
	}

	public CFinishFortuneWheel(long _npckey_, int _serviceid_) {
		this.npckey = _npckey_;
		this.serviceid = _serviceid_;
	}

	public final boolean _validator_() {
		if (npckey < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(serviceid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		serviceid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CFinishFortuneWheel) {
			CFinishFortuneWheel _o_ = (CFinishFortuneWheel)_o1_;
			if (npckey != _o_.npckey) return false;
			if (serviceid != _o_.serviceid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += serviceid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(serviceid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CFinishFortuneWheel _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = serviceid - _o_.serviceid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

