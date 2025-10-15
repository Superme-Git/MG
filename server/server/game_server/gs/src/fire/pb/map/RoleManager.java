
package fire.pb.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.locojoy.base.Marshal.OctetsStream;

import fire.msp.GGetRolesByConditions;
import fire.msp.IGetRolesCallBack;
import fire.msp.role.GSendAroundExceptMe;
import fire.pb.GsClient;
import mkdb.Transaction;


public class RoleManager {
	
	private static final RoleManager instance = new RoleManager();
	
	public static RoleManager getInstance()
	{
		return instance;
	}
	private RoleManager() {
		
	}
	private Map<Long,Role> roles = new ConcurrentHashMap<Long, Role>(); 
	
	public Map<Long,Role> getRoles()
	{
		return roles;
	}
	
	/**
	 * 只读，可在存储过程外调用
	 * @param roleID
	 * @return
	 */
	public Role getRoleByID(long roleID)
	{
		return roles.get(roleID);
	}
	
	/**
	 * 只读，可在存储过程外调用
	 * @param roleID
	 * @return
	 */
	public Role removeRoleByID(long roleID)
	{
		return roles.remove(roleID);
	}
	
	/**
	 * 只读，可在存储过程外调用
	 * @param roleID
	 * @return
	 */
	public Role createRole(long roleID, long sceneId, int posx, int posy)
	{
		Role role = roles.get(roleID);
		if(role!= null)
			return role;
		role = new Role(roleID);
		role.setScene(sceneId);
		role.setPos(new Position(posx, posy));
		roles.put(roleID, role);
		return role;
	}
	
	/**
	 * 将协议发给除了自己的周边角色
	 * 
	 * @param p2
	 */
	public static void sendAroundExceptMe(long roleId , mkio.Protocol p2) {
		GSendAroundExceptMe gSendAroundExceptMe = new GSendAroundExceptMe();
		gSendAroundExceptMe.roleid = roleId;
		gSendAroundExceptMe.protype = p2.getType();
		gSendAroundExceptMe.protocol =  p2.marshal(new OctetsStream());
		GsClient.sendToScene(gSendAroundExceptMe);
	}


	/**
	 * 将协议发给除了自己的周边角色
	 * 
	 * @param p2
	 */
	public static void psendAroundExceptMeWhileCommit(long roleId , mkio.Protocol p2) {
		GSendAroundExceptMe gSendAroundExceptMe = new GSendAroundExceptMe();
		gSendAroundExceptMe.roleid = roleId;
		gSendAroundExceptMe.protype = p2.getType();
		gSendAroundExceptMe.protocol =  p2.marshal(new OctetsStream());
		GsClient.pSendWhileCommit(gSendAroundExceptMe);
	}
	
	/**
	 * 根据条件从地图模块获取角色
	 * @param schoolId 门派，-1为无限制
	 * @param minlevel 最小等级，-1为无限制
	 * @param maxlevel 最大等级，-1为无限制
	 * @param sceneId 场景ID，-1为无限制
	 * @param limitnum 限制个数，-1为无限制
	 * @param callback 回调接口
	 */
	public static void getRolesByConditions(int schoolId, int minlevel, int maxlevel, long sceneId, int limitnum, IGetRolesCallBack callback) 
	{
		if(Transaction.current() != null)
			new PGetRolesByConditions(schoolId, minlevel, maxlevel, sceneId, limitnum, callback).call();
		else
			new PGetRolesByConditions(schoolId, minlevel, maxlevel, sceneId, limitnum, callback).submit();
	}
	
	/**
	 * 
	 * 通过条件从场景获取在线的角色IDs，并存储回调接口等获取完毕调用
	 * 
	 * @author nobody
	 *
	 */
	static class PGetRolesByConditions extends mkdb.Procedure
	{
		private IGetRolesCallBack callback; // 回调Id
		private int schoolid; // 门派ID，如果无限制填-1
		private int minlevel; // 最小等级，如果无限制填-1
		private int maxlevel; // 最大等级，如果无限制填-1
		private long sceneid; // 场景ID，如果无限制填-1
		private int limitnum; // 限制个数，无限制填-1
		public PGetRolesByConditions(int schoolid, int minlevel, int maxlevel, long sceneid, int limitnum, IGetRolesCallBack callback)
		{
			this.callback = callback;
			this.schoolid = schoolid;
			this.minlevel = minlevel;
			this.maxlevel = maxlevel;
			this.sceneid = sceneid;
			this.limitnum = limitnum;
		}
		@Override
		protected boolean process()
		{
			if(callback == null)
				return true;
			xbean.GetRolesCallBackInst callbackbean = xbean.Pod.newGetRolesCallBackInst();
			callbackbean.setCallback(callback);
			long callbackId =  xtable.Getrolescallback.insert(callbackbean);
			GGetRolesByConditions getroles = new GGetRolesByConditions(callbackId, schoolid, minlevel, maxlevel, sceneid, limitnum);
			GsClient.sendToScene(getroles);
			return true;
		}
		
	}
}
