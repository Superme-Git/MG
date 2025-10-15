
package fire.msp.move;

import fire.msp.PRoleChangeMap;
import fire.pb.map.Npc;
import fire.pb.map.Position;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.SceneNpcManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MUpdateUnitPos__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MUpdateUnitPos extends __MUpdateUnitPos__ {
	@Override
	protected void process()
	{
		// protocol handle
		if (unittype == 1)
		{
			Role role = RoleManager.getInstance().getRoleByID(key);
			if (role == null)
				return;
			
			if (role.getScene() != sceneid)
			{
				new PRoleChangeMap(key, (long) role.getScene(), sceneid).submit();
			}
			role.updateMapInfo(sceneid, posx, posy, posz);
		} else if (unittype == 2)
		{
			Npc npc = SceneNpcManager.getInstance().getNpcs().get(key);
			if(npc == null)
				return;
			npc.setScene(sceneid);
			npc.setPos(new Position(posx, posy, posz));
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724923;

	public int getType() {
		return 724923;
	}

	public int unittype; // 1 = role, 2 = npc
	public long key; // roleid或者npckey
	public long sceneid;
	public int posx;
	public int posy;
	public int posz;

	public MUpdateUnitPos() {
	}

	public MUpdateUnitPos(int _unittype_, long _key_, long _sceneid_, int _posx_, int _posy_, int _posz_) {
		this.unittype = _unittype_;
		this.key = _key_;
		this.sceneid = _sceneid_;
		this.posx = _posx_;
		this.posy = _posy_;
		this.posz = _posz_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(unittype);
		_os_.marshal(key);
		_os_.marshal(sceneid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.marshal(posz);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		unittype = _os_.unmarshal_int();
		key = _os_.unmarshal_long();
		sceneid = _os_.unmarshal_long();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		posz = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MUpdateUnitPos) {
			MUpdateUnitPos _o_ = (MUpdateUnitPos)_o1_;
			if (unittype != _o_.unittype) return false;
			if (key != _o_.key) return false;
			if (sceneid != _o_.sceneid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (posz != _o_.posz) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += unittype;
		_h_ += (int)key;
		_h_ += (int)sceneid;
		_h_ += posx;
		_h_ += posy;
		_h_ += posz;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(unittype).append(",");
		_sb_.append(key).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(posz).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MUpdateUnitPos _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = unittype - _o_.unittype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(key - _o_.key);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(sceneid - _o_.sceneid);
		if (0 != _c_) return _c_;
		_c_ = posx - _o_.posx;
		if (0 != _c_) return _c_;
		_c_ = posy - _o_.posy;
		if (0 != _c_) return _c_;
		_c_ = posz - _o_.posz;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

