package fire.pb;

import robot.CountCreateRoleUtils;
import robot.LoginUI;
import robot.Main;
import mkio.ILoginIns;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SCreateRole__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SCreateRole extends __SCreateRole__ {
	@Override
	protected void process() {
		// mkdb.Trace.error("服务器返回创建角色成功：" + Main.GetTaskType());
		// 只创建不进入游戏
		if (Main.GetTaskType() == 17) {
//			mkdb.Trace.error("testNum--------------:"
//					+ CountCreateRoleUtils.currentCreateNum.get());
			if (Main.getCount() == CountCreateRoleUtils.currentCreateNum.get()) {
//				String str = "roleid=" + newinfo.roleid + "\tuserid="
//						+ newinfo.rolename + "==========创建角色成功==============="
//						+ System.currentTimeMillis();
//				mkdb.Trace.error(str);
				CountCreateRoleUtils.currentCreateNum.set(1);
				CountCreateRoleUtils.currentStartCreateNum.set(0);
			} else {
				CountCreateRoleUtils.addCreateNum();
			}
			ILoginIns ins = (ILoginIns) this.getContext();
			LoginUI ui = (LoginUI) ins.getLoginUI();
			StringBuilder sb = new StringBuilder();
			sb.append("roleid=").append(newinfo.roleid).append("\tusername=")
					.append(ui.getUserName()).append("\tuserid=")
					.append(ui.getUserid()).append("==========创建角色成功!!!");
			mkdb.Trace.error(sb.toString());
			return;
		}
		ILoginIns ins = (ILoginIns) this.getContext();
		ins.send(new fire.pb.CEnterWorld(newinfo.roleid, 2));
		// String str = "roleid=" + newinfo.roleid + "\tuserid="
		// + "==========请求登陆===============世界" + ins.getHostAddress();
		// mkdb.Trace.error(str);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786436;

	public int getType() {
		return 786436;
	}

	public final static int CREATE_OK = 1; // 成功
	public final static int CREATE_ERROR = 2; // 失败
	public final static int CREATE_INVALID = 3; // 名称不合法
	public final static int CREATE_DUPLICATED = 4; // 重名
	public final static int CREATE_OVERCOUNT = 5; // 创建的新角色数量过多
	public final static int CREATE_OVERLEN = 6; // 角色名过长
	public final static int CREATE_SHORTLEN = 7; // 角色名过短

	public fire.pb.RoleInfo newinfo; // 创建后的角色信息

	public SCreateRole() {
		newinfo = new fire.pb.RoleInfo();
	}

	public SCreateRole(fire.pb.RoleInfo _newinfo_) {
		this.newinfo = _newinfo_;
	}

	public final boolean _validator_() {
		if (!newinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(newinfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		newinfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SCreateRole) {
			SCreateRole _o_ = (SCreateRole)_o1_;
			if (!newinfo.equals(_o_.newinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += newinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(newinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
