
package fire.msp.move;
import fire.pb.scene.SceneManager;
import fire.pb.scene.manager.SceneNpcManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateNpc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateNpc extends __GCreateNpc__ {
	@Override
	protected void process() {
		// protocol handle
		for ( CreateNpcInfo npc : npcs ) {
			fire.pb.scene.movable.NPC ezei = 
				fire.pb.scene.manager.SceneNpcManager.getInstance().createNpc(
				npc.npcid, npc.npcname, npc.dir);
			fire.pb.scene.Scene dstscene = SceneManager.getInstance().getSceneByID(npc.sceneid);
			if (dstscene == null){
				SceneManager.logger.error("帮派日常任务--江湖恶贼找到不目的地图");
				continue;
			}
			
			SceneNpcManager.getInstance().summonNpcAndUpdateGS(dstscene, 
					new fire.pb.scene.sPos.GridPos( npc.xpos, npc.ypos ).toPosition()	, ezei,0);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724903;

	public int getType() {
		return 724903;
	}

	public java.util.LinkedList<fire.msp.move.CreateNpcInfo> npcs;

	public GCreateNpc() {
		npcs = new java.util.LinkedList<fire.msp.move.CreateNpcInfo>();
	}

	public GCreateNpc(java.util.LinkedList<fire.msp.move.CreateNpcInfo> _npcs_) {
		this.npcs = _npcs_;
	}

	public final boolean _validator_() {
		for (fire.msp.move.CreateNpcInfo _v_ : npcs)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(npcs.size());
		for (fire.msp.move.CreateNpcInfo _v_ : npcs) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.msp.move.CreateNpcInfo _v_ = new fire.msp.move.CreateNpcInfo();
			_v_.unmarshal(_os_);
			npcs.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateNpc) {
			GCreateNpc _o_ = (GCreateNpc)_o1_;
			if (!npcs.equals(_o_.npcs)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npcs.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcs).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

