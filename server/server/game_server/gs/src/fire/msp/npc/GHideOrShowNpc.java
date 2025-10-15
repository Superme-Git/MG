
package fire.msp.npc;
import fire.pb.move.SAddUserScreen;
import fire.pb.move.SRemoveUserScreen;
import fire.pb.scene.manager.SceneNpcManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GHideOrShowNpc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GHideOrShowNpc extends __GHideOrShowNpc__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.config.TemplateNpc tnpc =  fire.pb.scene.manager.TemplateNpcManager.getInstance().getTemplateNpc(npcid);
		
		if(null == tnpc) return;
		if(tnpc.checkVisible() ==  (0==state)) return;
		
		tnpc.setVisible(0 == state);
		
		java.util.List<fire.pb.scene.movable.NPC> npcs = SceneNpcManager.getInstance().getNpcByID(npcid);
		if (!npcs.isEmpty()){
			if (state == 0){
				// 显示    添加通知客户端添加NPC
				for (fire.pb.scene.movable.NPC npc : npcs){
					npc.setVisible(true);
					final SAddUserScreen snd = new SAddUserScreen();
					snd.npclist.add(npc.getNpcBase());
					npc.sendAround(snd);
				}
			}
			else if (state == 1){
				// 隐藏
				for (fire.pb.scene.movable.NPC npc : npcs){
					npc.setVisible(false);
					final SRemoveUserScreen snd = new SRemoveUserScreen();
					snd.npcids.add(npc.getUniqueID());
					//final int screenindex = npc.getPos().toScreenPos().getScreenIndex();
					npc.sendAround(snd);
				}
			}
		}
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730421;

	public int getType() {
		return 730421;
	}

	public int npcid; // 要隐藏或显示的npc
	public int state; // 1表示隐藏，0表示隐藏

	public GHideOrShowNpc() {
	}

	public GHideOrShowNpc(int _npcid_, int _state_) {
		this.npcid = _npcid_;
		this.state = _state_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npcid);
		_os_.marshal(state);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npcid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GHideOrShowNpc) {
			GHideOrShowNpc _o_ = (GHideOrShowNpc)_o1_;
			if (npcid != _o_.npcid) return false;
			if (state != _o_.state) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npcid;
		_h_ += state;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcid).append(",");
		_sb_.append(state).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GHideOrShowNpc _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = npcid - _o_.npcid;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

