
package fire.msp.circletask;

import java.util.List;

import fire.pb.scene.MapUtil;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.sPos.GridPos;
import fire.pb.util.Misc;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateNpcForCatchIt__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateNpcForCatchIt extends __GCreateNpcForCatchIt__ {
	@Override
	protected void process() {
		// protocol handle
		Scene scene = null;
		GridPos gpos = null;
		try {
			final long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();
			fire.pb.scene.movable.NPC npc = fire.pb.scene.manager.SceneNpcManager.getInstance().createNpc(npcid, name, 3, npcKey);
			scene = fire.pb.scene.SceneManager.getInstance().getSceneByID(mapid);
			gpos = MapUtil.getRandomArrivablePos(mapid, MapUtil.TOUFANG_AREA);
			SceneNpcManager.getInstance().summonNpcAndUpdateGS(scene, gpos.toPosition(), npc);
			GridPos gPos2 = null;
			int lefttopx = Math.max(0, gpos.getX()-this.width);
			int lefttopy = Math.max(0, gpos.getY()-this.height);
			int width = Math.min(gpos.getX()+this.width, scene.getWidth());
			int height = Math.min(gpos.getY()+this.height, scene.getHighth());
			List<fire.pb.scene.sPos.GridPos> availablePosList = fire.pb.scene.MapUtil.getRandomArrivablePosList(mapid, lefttopx, lefttopy, width, height);
			gPos2 = availablePosList.get(Misc.getRandomBetween(0, availablePosList.size()-1));
			
			MCreateNpcForCatchIt mCreateNpcForCatchIt = new MCreateNpcForCatchIt();
			mCreateNpcForCatchIt.roleid = roleid;
			mCreateNpcForCatchIt.mapid = mapid;
			mCreateNpcForCatchIt.name = name;
			mCreateNpcForCatchIt.battleid = battleid;
			mCreateNpcForCatchIt.npcid = npcid;
			mCreateNpcForCatchIt.npckey = npcKey;
			mCreateNpcForCatchIt.posx = gpos.getX();
			mCreateNpcForCatchIt.posy = gpos.getY();
			mCreateNpcForCatchIt.posx1 = gPos2.getX();
			mCreateNpcForCatchIt.posy1 = gPos2.getY();
			mCreateNpcForCatchIt.givetasknpcid = givetasknpcid;
			mCreateNpcForCatchIt.questtype = questtype;
			mCreateNpcForCatchIt.questid = questid;
			mCreateNpcForCatchIt.teamnum = teamnum;
			mCreateNpcForCatchIt.firsttime = firsttime;
			fire.pb.scene.SceneClient.pSend(mCreateNpcForCatchIt);
		} catch (Exception e) {
			SceneManager.logger.error("create npc for catchit failed.npcid:"+npcid+"scene:"+mapid+"pos:"+gpos, e);
			
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 741905;

	public int getType() {
		return 741905;
	}

	public long roleid;
	public int npcid;
	public int mapid;
	public int battleid; // 战斗信息
	public java.lang.String name;
	public int givetasknpcid; // 给予任务的npcid
	public int questtype; // 子任务类型
	public int questid; // 子任务ID
	public int teamnum; // 队伍人数,0表示不需要组队
	public int width;
	public int height;
	public byte firsttime; // 1 表示从NPC处接取任务, 0 表示自动接取的

	public GCreateNpcForCatchIt() {
		name = "";
	}

	public GCreateNpcForCatchIt(long _roleid_, int _npcid_, int _mapid_, int _battleid_, java.lang.String _name_, int _givetasknpcid_, int _questtype_, int _questid_, int _teamnum_, int _width_, int _height_, byte _firsttime_) {
		this.roleid = _roleid_;
		this.npcid = _npcid_;
		this.mapid = _mapid_;
		this.battleid = _battleid_;
		this.name = _name_;
		this.givetasknpcid = _givetasknpcid_;
		this.questtype = _questtype_;
		this.questid = _questid_;
		this.teamnum = _teamnum_;
		this.width = _width_;
		this.height = _height_;
		this.firsttime = _firsttime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(npcid);
		_os_.marshal(mapid);
		_os_.marshal(battleid);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(givetasknpcid);
		_os_.marshal(questtype);
		_os_.marshal(questid);
		_os_.marshal(teamnum);
		_os_.marshal(width);
		_os_.marshal(height);
		_os_.marshal(firsttime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		npcid = _os_.unmarshal_int();
		mapid = _os_.unmarshal_int();
		battleid = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		givetasknpcid = _os_.unmarshal_int();
		questtype = _os_.unmarshal_int();
		questid = _os_.unmarshal_int();
		teamnum = _os_.unmarshal_int();
		width = _os_.unmarshal_int();
		height = _os_.unmarshal_int();
		firsttime = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateNpcForCatchIt) {
			GCreateNpcForCatchIt _o_ = (GCreateNpcForCatchIt)_o1_;
			if (roleid != _o_.roleid) return false;
			if (npcid != _o_.npcid) return false;
			if (mapid != _o_.mapid) return false;
			if (battleid != _o_.battleid) return false;
			if (!name.equals(_o_.name)) return false;
			if (givetasknpcid != _o_.givetasknpcid) return false;
			if (questtype != _o_.questtype) return false;
			if (questid != _o_.questid) return false;
			if (teamnum != _o_.teamnum) return false;
			if (width != _o_.width) return false;
			if (height != _o_.height) return false;
			if (firsttime != _o_.firsttime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += npcid;
		_h_ += mapid;
		_h_ += battleid;
		_h_ += name.hashCode();
		_h_ += givetasknpcid;
		_h_ += questtype;
		_h_ += questid;
		_h_ += teamnum;
		_h_ += width;
		_h_ += height;
		_h_ += (int)firsttime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(battleid).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(givetasknpcid).append(",");
		_sb_.append(questtype).append(",");
		_sb_.append(questid).append(",");
		_sb_.append(teamnum).append(",");
		_sb_.append(width).append(",");
		_sb_.append(height).append(",");
		_sb_.append(firsttime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

