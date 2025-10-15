
package fire.msp.npc;
import fire.msp.move.DynamicSceneParams;
import fire.pb.move.DynamicSceneType;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneFactory;
import fire.pb.scene.SceneManager;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.sPos.Position;




// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateNPCByPos__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateNPCByPos extends __GCreateNPCByPos__ {
	@Override
	protected void process() {
		// protocol handle
		Scene scene = null;
		try {
			NPC n = SceneNpcManager.getInstance().createNpcWithKey(npc.npckey,npc.npcbaseid, npc.name, npc.dir);
			if (n == null) {
				if (SceneManager.logger.isInfoEnabled()) {
					SceneManager.logger.info(new StringBuilder().append("公会副本刷怪:").append(npc.npcbaseid)
							.append(",sceneid:").append(npc.sceneid)
							.append(",ownerid:").append(npc.ownerid));
				}
			}
			
			if (npc.ownerid > 0) {
				scene = SceneManager.getInstance().getSceneByMapAndOwner((int) npc.sceneid, npc.ownerid);
				if (scene == null) {
					DynamicSceneParams params = new DynamicSceneParams();
					scene = SceneFactory.createDynamicScene((int) npc.sceneid,
							npc.ownerid, "", -1, DynamicSceneType.NORMAL_SCENE,
							params.marshal(new OctetsStream()));
					
					SceneManager.logger.error(new StringBuilder().append("公会副本刷怪:").append(npc.npcbaseid)
							.append(",sceneid:").append(npc.sceneid)
							.append(",ownerid:").append(npc.ownerid)
							.append(",sceneidlong:").append(scene.getSceneID()));
				}
			}
			else
				scene = SceneManager.getInstance().getSceneByID(npc.sceneid);
			SceneNpcManager.getInstance().summonNpcAndUpdateGS(scene, new Position(posx, posy), n,npc.time);
			
			int mapid = (int) npc.sceneid;
			if (mapid >= 1851 && mapid <= 1870) {
				if (SceneManager.logger.isInfoEnabled()) {
					SceneManager.logger.info(new StringBuilder().append("公会副本刷怪:").append(npc.npcbaseid)
							.append("坐标是： X ——").append(posx)
							.append("坐标是： Y ——").append(posx)
							.append(",sceneid:").append(npc.sceneid)
							.append(",ownerid:").append(npc.ownerid)
							.append(",sceneidlong:").append(scene.getSceneID()));
				}
			}
		}
		catch (Exception e) {
			SceneManager.logger.error("create npc by map failed.npcid:"
					+ npc.npcbaseid + ",sceneid:" + npc.sceneid + ",ownerid:" + npc.ownerid + ",scene:" + scene
					, e);
			if (scene != null) {
				SceneManager.logger.error("sceneidlong:" + scene.getSceneID());
			}
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730399;

	public int getType() {
		return 730399;
	}

	public fire.msp.npc.CreateNpcInfo npc;
	public int posx;
	public int posy;

	public GCreateNPCByPos() {
		npc = new fire.msp.npc.CreateNpcInfo();
	}

	public GCreateNPCByPos(fire.msp.npc.CreateNpcInfo _npc_, int _posx_, int _posy_) {
		this.npc = _npc_;
		this.posx = _posx_;
		this.posy = _posy_;
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
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npc.unmarshal(_os_);
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateNPCByPos) {
			GCreateNPCByPos _o_ = (GCreateNPCByPos)_o1_;
			if (!npc.equals(_o_.npc)) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npc.hashCode();
		_h_ += posx;
		_h_ += posy;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npc).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

