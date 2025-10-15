
package fire.msp.npc;


import fire.pb.move.SRoleEnterScene;
import fire.pb.scene.BlockInfo;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GEnterOrOutSafeAear__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GEnterOrOutSafeAear extends __GEnterOrOutSafeAear__ {
	@Override
	protected void process() {
		// protocol handle
		
//		if((serviceid % 2) == 0){
//			if(!FactionWarManager.isInSafeArea(roleid)){
//				Message.sendMsgNotify(roleid, 142867, null);
//				return;
//			}
//			
//			Team team = TeamManager.selectTeamByRoleId(roleid);
//			if(null == team){
//				Long enterTime = FactionWarManager.getInstance().getRoleEnterSafeAreaTime(roleid);
//				if(!safeTimeOut(enterTime)){
//					Message.sendMsgNotify(roleid, 141285, null);
//					return;
//				}
//				if(enterTime != null){
//					FactionWarManager.getInstance().removeRoleEnterSafeAreaTime(roleid);
//				}
//			}else{
//				List<Long> roleids = team.getNormalMemberIds();
//				List<String> param = new ArrayList<String>();
//				boolean isFirst = true;
//				StringBuffer stringBuffer = new StringBuffer();
//				for(Long id : roleids){
//					Long enterTime = FactionWarManager.getInstance().getRoleEnterSafeAreaTime(id);
//					if(!safeTimeOut(enterTime)){
//						if(isFirst){
//							stringBuffer.append(new PropRole(id, true).getName());
//							isFirst = false;
//						}else{
//							stringBuffer.append("、" + new PropRole(id, true).getName());
//						}
//					}else{
//						if(enterTime != null){
//							FactionWarManager.getInstance().removeRoleEnterSafeAreaTime(id);
//						}
//					}
//				}
//				
//				if(stringBuffer.length() > 0){
//					param.add(stringBuffer.toString());
//					Message.sendMsgNotify(roleid, 141288, param);
//					return;
//				}
//				
//			}
//			
//			
//		}else if(FactionWarManager.isInSafeArea(roleid)){
//			Message.sendMsgNotify(roleid, 142866, null);
//			return;
//		}
		
		fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		role.justGoto(role.getScene().getSceneID(), x, y, BlockInfo.FLOOR_0_Z, SRoleEnterScene.CHEFU);
		
	}
	
//	private static boolean safeTimeOut(Long enterTime) {
//		if(null == enterTime)
//			return true;
//		
//		if((System.currentTimeMillis()-enterTime)>1000*60*3)
//			return true;
//		
//		return false;
//	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730428;

	public int getType() {
		return 730428;
	}

	public long roleid;
	public int x;
	public int y;
	public int serviceid;

	public GEnterOrOutSafeAear() {
	}

	public GEnterOrOutSafeAear(long _roleid_, int _x_, int _y_, int _serviceid_) {
		this.roleid = _roleid_;
		this.x = _x_;
		this.y = _y_;
		this.serviceid = _serviceid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(x);
		_os_.marshal(y);
		_os_.marshal(serviceid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		x = _os_.unmarshal_int();
		y = _os_.unmarshal_int();
		serviceid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GEnterOrOutSafeAear) {
			GEnterOrOutSafeAear _o_ = (GEnterOrOutSafeAear)_o1_;
			if (roleid != _o_.roleid) return false;
			if (x != _o_.x) return false;
			if (y != _o_.y) return false;
			if (serviceid != _o_.serviceid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += x;
		_h_ += y;
		_h_ += serviceid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(x).append(",");
		_sb_.append(y).append(",");
		_sb_.append(serviceid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GEnterOrOutSafeAear _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = x - _o_.x;
		if (0 != _c_) return _c_;
		_c_ = y - _o_.y;
		if (0 != _c_) return _c_;
		_c_ = serviceid - _o_.serviceid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

