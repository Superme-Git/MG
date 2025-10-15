package fire.pb.scene.manager;

import fire.pb.scene.MapThread;
import fire.pb.scene.movable.Role;

public class RoleManager {
	
	public Role getRoleByID(long roleID) {

		return roles.get(roleID);
	}

	public Role getRoleByProtocol(mkio.Protocol protocol) {
		final long roleid = gnet.link.Onlines.getInstance()
				.findRoleid(protocol);
		return (-1 == roleid) ? null : roles.get(roleid);
	}

	public void insert(long roleID, Role role) {
		roles.put(roleID, role);
	}

	public void remove(long roleID) {
		roles.remove(roleID);
	}

	public int getSize() {

		return roles.size();
	}

	public java.util.Map<Long, Role> getRoles() {

		return roles;
	}

	public void addRoleToSchool(long roleid, int schoolid) {

		java.util.Set<Long> roleids = schoolRoles.get(schoolid);
		if (roleids == null) {
			roleids = new java.util.HashSet<Long>();
			schoolRoles.put(schoolid, roleids);
		}
		roleids.add(roleid);
	}

	public void removeRoleFromSchool(long roleid) {

		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if (null == role)
			return;

		int schoolid = role.getSchoolId();
		java.util.Set<Long> roleids = schoolRoles.get(schoolid);
		if (roleids != null) {
			roleids.remove(roleid);
		}
	}

	public void addRoleToCamp(long roleid,int camp){
		java.util.Set<Long> roleids = campRoles.get(camp);
		if (roleids == null) {
			roleids = new java.util.HashSet<Long>();
			campRoles.put(camp, roleids);
		}
		roleids.add(roleid);
	}
	
	public void removeRoleFromCamp(long roleid){
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if (null == role)
			return;
        int camp = role.getCamp();
        java.util.Set<Long> roleids = campRoles.get(camp);
        if (roleids != null)
        	roleids.remove(roleid);
	}
	public java.util.Set<Long> getSchoolRoles(int schoolid) {

		return schoolRoles.get(schoolid);
	}

	public static RoleManager getInstance() {

		if (null == instance) {
			instance = new RoleManager();
		}
		if (!Thread.currentThread().getName().equals(MapThread.MAP_THREAD_NAME)
				&& !Thread.currentThread().getName().equals("main")) {
			fire.pb.scene.Scene.LOG.info("有非MapThread线程调用RoleManager，ThreadName = "
					+ Thread.currentThread().getName());
			fire.pb.scene.Scene.LOG.info(fire.pb.util.Parser
					.convertStackTrace2String(Thread.currentThread()
							.getStackTrace()));
		}
		return instance;
	}
	private RoleManager() {

	}
	
	private static RoleManager instance;

	private final java.util.Map<Long, Role> roles = new java.util.HashMap<Long, Role>();
	
	public final java.util.Map<Integer, java.util.Set<Long>> campRoles = new java.util.HashMap<Integer, java.util.Set<Long>>();
	private final java.util.Map<Integer, java.util.Set<Long>> schoolRoles 
			= new java.util.HashMap<Integer, java.util.Set<Long>>();		// map<门派ID， set<角色id> >

}
