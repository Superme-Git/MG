
package fire.pb.game;

import fire.msp.npc.GFindNpcWithNpcId;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CUseXueYueKey__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CUseXueYueKey extends __CUseXueYueKey__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid <= 0) return;
		
		if (fire.pb.buff.Module.existState(roleid, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 131451, null);
			return;
		}
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				fire.msp.npc.GFindNpcWithNpcId send2Scene = new GFindNpcWithNpcId();
				send2Scene.npcid = npckid;
				send2Scene.roleid = roleid;
				fire.pb.GsClient.pSendWhileCommit(send2Scene);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810369;

	public int getType() {
		return 810369;
	}

	public int npckid; // npcid

	public CUseXueYueKey() {
	}

	public CUseXueYueKey(int _npckid_) {
		this.npckid = _npckid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CUseXueYueKey) {
			CUseXueYueKey _o_ = (CUseXueYueKey)_o1_;
			if (npckid != _o_.npckid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npckid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CUseXueYueKey _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = npckid - _o_.npckid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

