
package fire.msp.circletask;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GFindShoolQuestDemonsNpc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GFindShoolQuestDemonsNpc extends __GFindShoolQuestDemonsNpc__ {
	@Override
	protected void process() {
		// protocol handle
		
		java.util.Set<Long> objlist = fire.pb.scene.manager.RoleManager.getInstance().getSchoolRoles(objschool);
		java.util.List<fire.pb.scene.movable.Role> objrolelist = new java.util.ArrayList<fire.pb.scene.movable.Role>();
		if (objlist != null){
			for (Long rid:objlist){
				fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(rid);
				if (role == null)
					continue;
				if (role.getLevel() >= levelmin && role.getLevel() <= levelmax)
					objrolelist.add(role);
			}
		}
		if (!objrolelist.isEmpty()){
			int rand = fire.pb.util.Misc.getRandomBetween(0, objrolelist.size()-1);
			fire.pb.scene.movable.Role dstrole = objrolelist.get(rand);
			npcinfo.name = dstrole.getName();
			npcinfo.shape = dstrole.getModeid();
		}
		
		java.util.List<fire.msp.npc.NpcInfo> rlist = fire.pb.scene.manager.SceneNpcManager.getInstance().createNpc(npcinfo);
		
		// 失败返回GS
		if (rlist == null || rlist.isEmpty() || rlist.size() < npcinfo.num){
			fire.msp.npc.MCreateNpcUniversalRet mret = new fire.msp.npc.MCreateNpcUniversalRet();
			mret.interfaceid = interfaceid;
			mret.result = 0;
			fire.pb.scene.SceneClient.pSend(mret);
			return;
		}
		// 成功  同步到GS NPC的信息
		fire.msp.npc.MUpdateNpcInfo mUpdateNpcInfo = new fire.msp.npc.MUpdateNpcInfo();
		mUpdateNpcInfo.npcs.addAll(rlist);
		fire.pb.scene.SceneClient.pSend(mUpdateNpcInfo);
		
		if (interfaceid > 0){
			fire.msp.npc.MCreateNpcUniversalRet mret = new fire.msp.npc.MCreateNpcUniversalRet();
			mret.interfaceid = interfaceid;
			mret.result = 1;
			mret.npclist.addAll(rlist);
			fire.pb.scene.SceneClient.pSend(mret);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 741900;

	public int getType() {
		return 741900;
	}

	public long interfaceid;
	public int objschool; // 寻找玩家所属的门派
	public int levelmin; // 最低等级
	public int levelmax; // 最高等级
	public fire.msp.npc.CreateNpcInfoUniverse npcinfo; // 找不到玩家时，按照npcinfo生成任务npc

	public GFindShoolQuestDemonsNpc() {
		npcinfo = new fire.msp.npc.CreateNpcInfoUniverse();
	}

	public GFindShoolQuestDemonsNpc(long _interfaceid_, int _objschool_, int _levelmin_, int _levelmax_, fire.msp.npc.CreateNpcInfoUniverse _npcinfo_) {
		this.interfaceid = _interfaceid_;
		this.objschool = _objschool_;
		this.levelmin = _levelmin_;
		this.levelmax = _levelmax_;
		this.npcinfo = _npcinfo_;
	}

	public final boolean _validator_() {
		if (!npcinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(interfaceid);
		_os_.marshal(objschool);
		_os_.marshal(levelmin);
		_os_.marshal(levelmax);
		_os_.marshal(npcinfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		interfaceid = _os_.unmarshal_long();
		objschool = _os_.unmarshal_int();
		levelmin = _os_.unmarshal_int();
		levelmax = _os_.unmarshal_int();
		npcinfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GFindShoolQuestDemonsNpc) {
			GFindShoolQuestDemonsNpc _o_ = (GFindShoolQuestDemonsNpc)_o1_;
			if (interfaceid != _o_.interfaceid) return false;
			if (objschool != _o_.objschool) return false;
			if (levelmin != _o_.levelmin) return false;
			if (levelmax != _o_.levelmax) return false;
			if (!npcinfo.equals(_o_.npcinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)interfaceid;
		_h_ += objschool;
		_h_ += levelmin;
		_h_ += levelmax;
		_h_ += npcinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(interfaceid).append(",");
		_sb_.append(objschool).append(",");
		_sb_.append(levelmin).append(",");
		_sb_.append(levelmax).append(",");
		_sb_.append(npcinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

