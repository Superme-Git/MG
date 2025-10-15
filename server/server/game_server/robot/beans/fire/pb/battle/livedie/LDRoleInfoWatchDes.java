
package fire.pb.battle.livedie;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class LDRoleInfoWatchDes implements Marshal {
	public fire.pb.battle.livedie.LDRoleInfoDes role1; // 对手1
	public fire.pb.battle.livedie.LDRoleInfoDes role2; // 对手1

	public LDRoleInfoWatchDes() {
		role1 = new fire.pb.battle.livedie.LDRoleInfoDes();
		role2 = new fire.pb.battle.livedie.LDRoleInfoDes();
	}

	public LDRoleInfoWatchDes(fire.pb.battle.livedie.LDRoleInfoDes _role1_, fire.pb.battle.livedie.LDRoleInfoDes _role2_) {
		this.role1 = _role1_;
		this.role2 = _role2_;
	}

	public final boolean _validator_() {
		if (!role1._validator_()) return false;
		if (!role2._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(role1);
		_os_.marshal(role2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		role1.unmarshal(_os_);
		role2.unmarshal(_os_);
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof LDRoleInfoWatchDes) {
			LDRoleInfoWatchDes _o_ = (LDRoleInfoWatchDes)_o1_;
			if (!role1.equals(_o_.role1)) return false;
			if (!role2.equals(_o_.role2)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += role1.hashCode();
		_h_ += role2.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(role1).append(",");
		_sb_.append(role2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

