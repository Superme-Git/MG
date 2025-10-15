
package fire.pb.school;

import java.util.Map.Entry;

import xbean.ProfessionLeaderInfo;
import gnet.link.Onlines;

import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CShouxiShape__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CShouxiShape extends __CShouxiShape__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid <=0) return;
		
		Npc npc = SceneNpcManager.selectNpcByKey(shouxikey);
		if (npc==null)return;
		School school = School.getSchoolByNpcid(npc.getNpcID());
		if (school!=null){
			xbean.ProfessionLeaderInfo sxi = xtable.Professionleader.select(school.getValue());
			if (sxi!=null){
				long shouxiid=sxi.getRoleid();
				if (shouxiid>0){
					sendNpcShape(roleid,shouxiid,sxi);
				}
			}
		}
//		}
	}


	private void sendNpcShape(long roleid,long shouxiid,ProfessionLeaderInfo sxi) {
      xbean.Properties prop = xtable.Properties.select(shouxiid);
      int roleShape = prop.getShape();
    
      SShouxiShape shape = new SShouxiShape();
		shape.shouxikey = shouxikey;
		shape.shape = roleShape;
		//fire.pb.map.Role.getPlayerComponents(shouxiid, shape.components);
		for (Entry<Integer, Integer> entry : sxi.getShapecomponent().entrySet()) {
			shape.components.put(entry.getKey().byteValue(), entry.getValue());
		}
		shape.name = prop.getRolename();
	   Onlines.getInstance().send(roleid,shape);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810439;

	public int getType() {
		return 810439;
	}

	public long shouxikey; // 首席-弟子的baseid

	public CShouxiShape() {
	}

	public CShouxiShape(long _shouxikey_) {
		this.shouxikey = _shouxikey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(shouxikey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		shouxikey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CShouxiShape) {
			CShouxiShape _o_ = (CShouxiShape)_o1_;
			if (shouxikey != _o_.shouxikey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)shouxikey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(shouxikey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CShouxiShape _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(shouxikey - _o_.shouxikey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

