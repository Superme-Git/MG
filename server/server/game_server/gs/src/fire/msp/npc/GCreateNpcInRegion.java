
package fire.msp.npc;

import java.util.List;

import fire.pb.scene.SceneManager;
import fire.pb.util.Misc;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateNpcInRegion__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateNpcInRegion extends __GCreateNpcInRegion__ {
	@Override
	protected void process() {
		fire.pb.scene.movable.NPC n = fire.pb.scene.manager.SceneNpcManager.getInstance().createNpcWithKey(npc.npckey,npc.npcbaseid, npc.name, npc.dir);
		fire.pb.scene.Scene scene = fire.pb.scene.SceneManager.getInstance().getSceneByID(npc.sceneid);
		List<fire.pb.scene.sPos.GridPos> availablePosList = fire.pb.scene.MapUtil.getRandomArrivablePosList(npc.sceneid, lefttopx, lefttopy, width, height);
		if (availablePosList.size()>0){
			fire.pb.scene.sPos.GridPos availablePos = availablePosList.get(Misc.getRandomBetween(0, availablePosList.size()-1));
			fire.pb.scene.manager.SceneNpcManager.getInstance().summonNpcAndUpdateGS(scene, availablePos.toPosition(), n,npc.time);
		}else {
			SceneManager.logger.error("can't find availablePos.npcid:"+npc.npcbaseid+"npckey"+npc.npckey+"sceneid:"+npc.sceneid+"lefttopx:"+lefttopx+"lefttopy:"+lefttopy+"width:"+width+"height:"+height);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730424;

	public int getType() {
		return 730424;
	}

	public fire.msp.npc.CreateNpcInfo npc;
	public int lefttopx; // 指定的左上角坐标x
	public int lefttopy; // 指定的左上角坐标y
	public int width;
	public int height;

	public GCreateNpcInRegion() {
		npc = new fire.msp.npc.CreateNpcInfo();
	}

	public GCreateNpcInRegion(fire.msp.npc.CreateNpcInfo _npc_, int _lefttopx_, int _lefttopy_, int _width_, int _height_) {
		this.npc = _npc_;
		this.lefttopx = _lefttopx_;
		this.lefttopy = _lefttopy_;
		this.width = _width_;
		this.height = _height_;
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
		_os_.marshal(lefttopx);
		_os_.marshal(lefttopy);
		_os_.marshal(width);
		_os_.marshal(height);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npc.unmarshal(_os_);
		lefttopx = _os_.unmarshal_int();
		lefttopy = _os_.unmarshal_int();
		width = _os_.unmarshal_int();
		height = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateNpcInRegion) {
			GCreateNpcInRegion _o_ = (GCreateNpcInRegion)_o1_;
			if (!npc.equals(_o_.npc)) return false;
			if (lefttopx != _o_.lefttopx) return false;
			if (lefttopy != _o_.lefttopy) return false;
			if (width != _o_.width) return false;
			if (height != _o_.height) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npc.hashCode();
		_h_ += lefttopx;
		_h_ += lefttopy;
		_h_ += width;
		_h_ += height;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npc).append(",");
		_sb_.append(lefttopx).append(",");
		_sb_.append(lefttopy).append(",");
		_sb_.append(width).append(",");
		_sb_.append(height).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

