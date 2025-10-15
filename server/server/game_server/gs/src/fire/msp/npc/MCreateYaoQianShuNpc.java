
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MCreateYaoQianShuNpc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MCreateYaoQianShuNpc extends __MCreateYaoQianShuNpc__ {
	@Override
	protected void process() {
		// protocol handle
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.RoleYaoQianShuInfoMaps yaoqianMap = xtable.Roleyaoqianshutables.get(roleid);
				if (yaoqianMap == null) {
					yaoqianMap = xbean.Pod.newRoleYaoQianShuInfoMaps();
					xtable.Roleyaoqianshutables.insert(roleid, yaoqianMap);
				}
				
				xbean.RoleYaoQianShuInfo yqInfo = xbean.Pod.newRoleYaoQianShuInfo();
				yaoqianMap.getYaoqianshumaps().put(npckey, yqInfo);
				
				yqInfo.setMapid((int)sceneid);
				yqInfo.setPosx(posx);
				yqInfo.setPosy(posy);
				
				xtable.Roleyaoqianshuinfos.insert(npckey, roleid);
				return true;
			}
			
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730438;

	public int getType() {
		return 730438;
	}

	public long roleid;
	public java.lang.String npcname;
	public long npckey;
	public long sceneid;
	public int posx;
	public int posy;

	public MCreateYaoQianShuNpc() {
		npcname = "";
	}

	public MCreateYaoQianShuNpc(long _roleid_, java.lang.String _npcname_, long _npckey_, long _sceneid_, int _posx_, int _posy_) {
		this.roleid = _roleid_;
		this.npcname = _npcname_;
		this.npckey = _npckey_;
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
		_os_.marshal(npckey);
		_os_.marshal(sceneid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		npcname = _os_.unmarshal_String("UTF-16LE");
		npckey = _os_.unmarshal_long();
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
		if (_o1_ instanceof MCreateYaoQianShuNpc) {
			MCreateYaoQianShuNpc _o_ = (MCreateYaoQianShuNpc)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!npcname.equals(_o_.npcname)) return false;
			if (npckey != _o_.npckey) return false;
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
		_h_ += (int)npckey;
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
		_sb_.append(npckey).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

