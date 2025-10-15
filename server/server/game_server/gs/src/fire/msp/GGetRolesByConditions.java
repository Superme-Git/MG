
package fire.msp;

import java.util.HashSet;
import java.util.Set;

import fire.pb.scene.Scene;
import fire.pb.scene.SceneClient;
import fire.pb.scene.SceneManager;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GGetRolesByConditions__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GGetRolesByConditions extends __GGetRolesByConditions__ {
	@Override
	protected void process() {
		Set<Long> sendroleIds = new HashSet<Long>(); 
		int rolenum = 0;
		if(limitnum == -1)
			limitnum = Integer.MAX_VALUE;
		if(sceneid != -1)
		{//优先按场景索引
			Scene scene = SceneManager.getInstance().getSceneByID(sceneid);
			if (scene == null)
			{
				SceneManager.logger.error("GGetRolesByConditions:sceneid error[" + sceneid + "]");
				return;
			}
			
			for(Role role : scene.getSceneRoles().values())
			{
				if(schoolid != -1 && role.getSchoolId() != schoolid)
					continue;
				if(minlevel != -1 && role.getLevel() < minlevel)
					continue;
				if(maxlevel != -1 && role.getLevel() > maxlevel)
					continue;
				sendroleIds.add(role.getRoleID());
				rolenum++;
				if(rolenum >= limitnum)
					break;
			}
		}
		else if(schoolid != -1)
		{//按门派索引
			Set<Long> schoolroles = RoleManager.getInstance().getSchoolRoles(schoolid);
			for(long sroleId :  schoolroles)
			{
				Role role = RoleManager.getInstance().getRoleByID(sroleId);
				if(minlevel != -1 && role.getLevel() < minlevel)
					continue;
				if(maxlevel != -1 && role.getLevel() > maxlevel)
					continue;
				sendroleIds.add(sroleId);
				rolenum++;
				if(rolenum >= limitnum)
					break;
			}
		}
		else
		{//全遍历过滤等级
			for(Role role : RoleManager.getInstance().getRoles().values())
			{
				if(minlevel != -1 && role.getLevel() < minlevel)
					continue;
				if(maxlevel != -1 && role.getLevel() > maxlevel)
					continue;
				sendroleIds.add(role.getRoleID());
				rolenum++;
				if(rolenum >= limitnum)
					break;
			}
		}
		
		MGetRolesByConditions snd = new MGetRolesByConditions();
		snd.callbackid = callbackid;
		snd.roleids.addAll(sendroleIds);
		SceneClient.pSend(snd);
	}
	
//	public static void getRolesByConditions(int schoolId, int minlevel, int maxlevel, long sceneId, int limitnum, IGetRolesCallBack callback) 
//	{
//		if(Transaction.current() != null)
//			new PGetRolesByConditions(schoolId, minlevel, maxlevel, sceneId, limitnum, callback).call();
//		else
//			new PGetRolesByConditions(schoolId, minlevel, maxlevel, sceneId, limitnum, callback).submit();
//	}
//	
//	static class PGetRolesByConditions extends xdb.Procedure
//	{
//		private IGetRolesCallBack callback2; // 回调Id
//		private int schoolid2; // 门派ID，如果无限制填-1
//		private int minlevel2; // 最小等级，如果无限制填-1
//		private int maxlevel2; // 最大等级，如果无限制填-1
//		private long sceneid2; // 场景ID，如果无限制填-1
//		private int limitnum2; // 限制个数，无限制填-1
//		public PGetRolesByConditions(int schoolid, int minlevel, int maxlevel, long sceneid, int limitnum, IGetRolesCallBack callback)
//		{
//			this.callback2 = callback;
//			this.schoolid2 = schoolid;
//			this.minlevel2 = minlevel;
//			this.maxlevel2 = maxlevel;
//			this.sceneid2 = sceneid;
//			this.limitnum2 = limitnum;
//		}
//		@Override
//		protected boolean process()
//		{
//			if(callback2 == null)
//				return true;
//			xbean.GetRolesCallBackInst callbackbean = xbean.Pod.newGetRolesCallBackInst();
//			callbackbean.setCallback(callback2);
//			long callbackId =  xtable.Getrolescallback.insert(callbackbean);
//			GGetRolesByConditions getroles = new GGetRolesByConditions(callbackId, schoolid2, minlevel2, maxlevel2, sceneid2, limitnum2);
//			GsClient.sendToScene(getroles);
//			return true;
//		}
//		
//	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720907;

	public int getType() {
		return 720907;
	}

	public long callbackid; // 回调Id
	public int schoolid; // 门派ID，如果无限制填-1
	public int minlevel; // 最小等级，如果无限制填-1
	public int maxlevel; // 最大等级，如果无限制填-1
	public long sceneid; // 场景ID，如果无限制填-1
	public int limitnum; // 限制个数，无限制填-1

	public GGetRolesByConditions() {
	}

	public GGetRolesByConditions(long _callbackid_, int _schoolid_, int _minlevel_, int _maxlevel_, long _sceneid_, int _limitnum_) {
		this.callbackid = _callbackid_;
		this.schoolid = _schoolid_;
		this.minlevel = _minlevel_;
		this.maxlevel = _maxlevel_;
		this.sceneid = _sceneid_;
		this.limitnum = _limitnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(callbackid);
		_os_.marshal(schoolid);
		_os_.marshal(minlevel);
		_os_.marshal(maxlevel);
		_os_.marshal(sceneid);
		_os_.marshal(limitnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		callbackid = _os_.unmarshal_long();
		schoolid = _os_.unmarshal_int();
		minlevel = _os_.unmarshal_int();
		maxlevel = _os_.unmarshal_int();
		sceneid = _os_.unmarshal_long();
		limitnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GGetRolesByConditions) {
			GGetRolesByConditions _o_ = (GGetRolesByConditions)_o1_;
			if (callbackid != _o_.callbackid) return false;
			if (schoolid != _o_.schoolid) return false;
			if (minlevel != _o_.minlevel) return false;
			if (maxlevel != _o_.maxlevel) return false;
			if (sceneid != _o_.sceneid) return false;
			if (limitnum != _o_.limitnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)callbackid;
		_h_ += schoolid;
		_h_ += minlevel;
		_h_ += maxlevel;
		_h_ += (int)sceneid;
		_h_ += limitnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(callbackid).append(",");
		_sb_.append(schoolid).append(",");
		_sb_.append(minlevel).append(",");
		_sb_.append(maxlevel).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(limitnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GGetRolesByConditions _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(callbackid - _o_.callbackid);
		if (0 != _c_) return _c_;
		_c_ = schoolid - _o_.schoolid;
		if (0 != _c_) return _c_;
		_c_ = minlevel - _o_.minlevel;
		if (0 != _c_) return _c_;
		_c_ = maxlevel - _o_.maxlevel;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(sceneid - _o_.sceneid);
		if (0 != _c_) return _c_;
		_c_ = limitnum - _o_.limitnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

