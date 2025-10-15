
package fire.msp.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GNotifyMapPetInfo__ extends mkio.Protocol { }

/** GS 向 地图模块 发送消息， 通知展示宠物的信息, 并通知周围玩家
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GNotifyMapPetInfo extends __GNotifyMapPetInfo__ {
	@Override
	protected void process() {
		// protocol handle
		final fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if (role == null) return;
		if(null == role.getScene()) return;
		if (showpetinfo.petid != 0){
			final fire.pb.scene.movable.ScenePet showpet = 
				fire.pb.scene.movable.God.createPet(showpetinfo.petkey, showpetinfo.petname, showpetinfo.petid,  showpetinfo.color, showpetinfo.bodysize,showpetinfo.showeffect);
			
			if(showpet.checkAllTheSame(role.getShowPet()))
				return;//相等则不更新
			role.setShowPet(showpet);
		}else{
			if(role.getShowPet() == null)
				return;//相等则不更新
			role.setShowPet(null);
		}
		
		// 发送给周围玩家
		final fire.pb.pet.SShowPetAround send = new fire.pb.pet.SShowPetAround();
		send.roleid = roleid;
		send.showpetid = showpetinfo.petid;
		send.showpetkey = (int)showpetinfo.petkey;
		send.showpetname = showpetinfo.petname;
		send.colour = (byte)showpetinfo.color;
		send.size = (byte)showpetinfo.bodysize;
		send.showeffect = (byte)showpetinfo.showeffect;
		role.sendWhoSeeMeAndMe(send);	
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724906;

	public int getType() {
		return 724906;
	}

	public long roleid;
	public fire.msp.showpetinfo showpetinfo;

	public GNotifyMapPetInfo() {
		showpetinfo = new fire.msp.showpetinfo();
	}

	public GNotifyMapPetInfo(long _roleid_, fire.msp.showpetinfo _showpetinfo_) {
		this.roleid = _roleid_;
		this.showpetinfo = _showpetinfo_;
	}

	public final boolean _validator_() {
		if (roleid <= 0) return false;
		if (!showpetinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(showpetinfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		showpetinfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GNotifyMapPetInfo) {
			GNotifyMapPetInfo _o_ = (GNotifyMapPetInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!showpetinfo.equals(_o_.showpetinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += showpetinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(showpetinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

