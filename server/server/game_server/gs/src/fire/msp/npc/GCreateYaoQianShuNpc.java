
package fire.msp.npc;
import fire.pb.map.SceneNpcManager;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateYaoQianShuNpc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateYaoQianShuNpc extends __GCreateYaoQianShuNpc__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.Scene scene = null;
		try {
			long newnpckey = SceneNpcManager.getInstance().getNextId();
			fire.pb.scene.movable.NPC npc = fire.pb.scene.manager.SceneNpcManager.getInstance().createNpcWithKey(newnpckey, npcid, npcname, 3);
			scene = fire.pb.scene.SceneManager.getInstance().getSceneByID(sceneid);
			
			fire.pb.scene.sPos.GridPos availablePos = fire.pb.scene.MapUtil.genPosInGridScale(scene.getSceneID(), posx, posy, 6,
							1, false).get(0);
			fire.pb.scene.manager.SceneNpcManager.getInstance().summonNpcAndUpdateGS(scene, availablePos.toPosition(), npc, 1800000);

			MCreateYaoQianShuNpc yaoqianshu = new MCreateYaoQianShuNpc();
			yaoqianshu.roleid = roleid;
			yaoqianshu.npcname = npcname;
			yaoqianshu.sceneid = sceneid;
			yaoqianshu.posx = posx;
			yaoqianshu.posy = posy;
			yaoqianshu.npckey = newnpckey;
			fire.pb.scene.SceneClient.pSend(yaoqianshu);
		}
		catch (Exception e) {
			fire.pb.scene.SceneManager.logger.error("create npc for yaoqianshu failed.npcid:"+npcid+"scene:"+sceneid, e);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730437;

	public int getType() {
		return 730437;
	}

	public long roleid;
	public java.lang.String npcname;
	public int npcid;
	public long sceneid;
	public int posx;
	public int posy;

	public GCreateYaoQianShuNpc() {
		npcname = "";
	}

	public GCreateYaoQianShuNpc(long _roleid_, java.lang.String _npcname_, int _npcid_, long _sceneid_, int _posx_, int _posy_) {
		this.roleid = _roleid_;
		this.npcname = _npcname_;
		this.npcid = _npcid_;
		this.sceneid = _sceneid_;
		this.posx = _posx_;
		this.posy = _posy_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(npcname, "UTF-16LE");
		_os_.marshal(npcid);
		_os_.marshal(sceneid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		npcname = _os_.unmarshal_String("UTF-16LE");
		npcid = _os_.unmarshal_int();
		sceneid = _os_.unmarshal_long();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateYaoQianShuNpc) {
			GCreateYaoQianShuNpc _o_ = (GCreateYaoQianShuNpc)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!npcname.equals(_o_.npcname)) return false;
			if (npcid != _o_.npcid) return false;
			if (sceneid != _o_.sceneid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += npcname.hashCode();
		_h_ += npcid;
		_h_ += (int)sceneid;
		_h_ += posx;
		_h_ += posy;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(npcname.length()).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

