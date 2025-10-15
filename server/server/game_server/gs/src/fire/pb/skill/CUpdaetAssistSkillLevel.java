
package fire.pb.skill;
import fire.pb.map.SceneNpcManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CUpdaetAssistSkillLevel__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CUpdaetAssistSkillLevel extends __CUpdaetAssistSkillLevel__ {
	@Override
	protected void process() {
		// protocol handle
			
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		fire.pb.PropRole prole = new fire.pb.PropRole(roleId,true);
		
		if(prole.getLevel()<30)
		{
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 140417, null);
			return;
		}
		
		if(!SceneNpcManager.checkDistance(npckey, roleId))
			return;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800438;

	public int getType() {
		return 800438;
	}

	public long npckey;
	public int id;

	public CUpdaetAssistSkillLevel() {
	}

	public CUpdaetAssistSkillLevel(long _npckey_, int _id_) {
		this.npckey = _npckey_;
		this.id = _id_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(id);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		id = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CUpdaetAssistSkillLevel) {
			CUpdaetAssistSkillLevel _o_ = (CUpdaetAssistSkillLevel)_o1_;
			if (npckey != _o_.npckey) return false;
			if (id != _o_.id) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += id;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(id).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CUpdaetAssistSkillLevel _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

