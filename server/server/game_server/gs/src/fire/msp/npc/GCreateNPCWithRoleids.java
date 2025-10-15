
package fire.msp.npc;


import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.sPos.Position;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateNPCWithRoleids__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateNPCWithRoleids extends __GCreateNPCWithRoleids__ {
	
	public static final int HUA_JIAO_NPC_ID = 11153;
	public static final int SURPER_HUA_JIAO_NPC_ID = 11270;
	
	@Override
	protected void process() {
		// protocol handle
		NPC n = SceneNpcManager.getInstance().createNpcWithKey(npc.npckey,npc.npcbaseid, npc.name, npc.dir);
		Scene scene = SceneManager.getInstance().getSceneByID(npc.sceneid);
		SceneNpcManager.getInstance().summonNpcAndUpdateGS(scene, new Position(posx, posy), n,npc.time);
		
		//如果招出来的NPC是花轿的话，需要按照特定的AI来让NPC行走
		if(this.npc.npcbaseid == HUA_JIAO_NPC_ID || this.npc.npcbaseid == SURPER_HUA_JIAO_NPC_ID){
			fire.pb.scene.npcai.AIManager.getInstance().npcMoveByAiID(this.npc.npckey, 1, roleids);
		}
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730435;

	public int getType() {
		return 730435;
	}

	public fire.msp.npc.CreateNpcInfo npc;
	public int posx;
	public int posy;
	public java.util.HashSet<Long> roleids;

	public GCreateNPCWithRoleids() {
		npc = new fire.msp.npc.CreateNpcInfo();
		roleids = new java.util.HashSet<Long>();
	}

	public GCreateNPCWithRoleids(fire.msp.npc.CreateNpcInfo _npc_, int _posx_, int _posy_, java.util.HashSet<Long> _roleids_) {
		this.npc = _npc_;
		this.posx = _posx_;
		this.posy = _posy_;
		this.roleids = _roleids_;
	}

	public final boolean _validator_() {
		if (!npc._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npc);
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.compact_uint32(roleids.size());
		for (Long _v_ : roleids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npc.unmarshal(_os_);
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			roleids.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateNPCWithRoleids) {
			GCreateNPCWithRoleids _o_ = (GCreateNPCWithRoleids)_o1_;
			if (!npc.equals(_o_.npc)) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (!roleids.equals(_o_.roleids)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npc.hashCode();
		_h_ += posx;
		_h_ += posy;
		_h_ += roleids.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npc).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(roleids).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

