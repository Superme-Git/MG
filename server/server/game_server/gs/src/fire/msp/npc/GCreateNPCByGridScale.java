
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateNPCByGridScale__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateNPCByGridScale extends __GCreateNPCByGridScale__ {
	@Override
	protected void process() {
		fire.pb.scene.movable.NPC n = fire.pb.scene.manager.SceneNpcManager.getInstance().createNpcWithKey(npc.npckey,npc.npcbaseid, npc.name, npc.dir);
		fire.pb.scene.Scene scene = fire.pb.scene.SceneManager.getInstance().getSceneByID(npc.sceneid);
		fire.pb.scene.sPos.GridPos availablePos = fire.pb.scene.MapUtil.genPosInGridScale(npc.sceneid,posx,posy, scale, 1, toufangarea!=0).get(0);
		fire.pb.scene.manager.SceneNpcManager.getInstance().summonNpcAndUpdateGS(scene, availablePos.toPosition(), n,npc.time);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730400;

	public int getType() {
		return 730400;
	}

	public fire.msp.npc.CreateNpcInfo npc;
	public int posx; // 指定的坐标x
	public int posy; // 指定的坐标y
	public int scale; // 范围，单位为格子（GridPos）的个数
	public int toufangarea; // 值为0的时候 必须放入投放区， 否则在地图上随机放

	public GCreateNPCByGridScale() {
		npc = new fire.msp.npc.CreateNpcInfo();
	}

	public GCreateNPCByGridScale(fire.msp.npc.CreateNpcInfo _npc_, int _posx_, int _posy_, int _scale_, int _toufangarea_) {
		this.npc = _npc_;
		this.posx = _posx_;
		this.posy = _posy_;
		this.scale = _scale_;
		this.toufangarea = _toufangarea_;
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
		_os_.marshal(scale);
		_os_.marshal(toufangarea);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npc.unmarshal(_os_);
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		scale = _os_.unmarshal_int();
		toufangarea = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateNPCByGridScale) {
			GCreateNPCByGridScale _o_ = (GCreateNPCByGridScale)_o1_;
			if (!npc.equals(_o_.npc)) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (scale != _o_.scale) return false;
			if (toufangarea != _o_.toufangarea) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npc.hashCode();
		_h_ += posx;
		_h_ += posy;
		_h_ += scale;
		_h_ += toufangarea;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npc).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(scale).append(",");
		_sb_.append(toufangarea).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

