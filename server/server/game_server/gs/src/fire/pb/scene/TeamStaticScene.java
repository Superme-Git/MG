package fire.pb.scene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fire.pb.scene.movable.Role;


/**
 * 队伍可见的静态场景
 * 
 * @author nobody
 *
 */
public class TeamStaticScene extends StaticScene
{
	
	/**
	 * 在screens中获取对role可见的角色
	 * @param indexs screen indexes
	 * @param role
	 * @return
	 */
	@Override
	public java.util.Map<Long, Role> getRoleInScreensByNum(Set<Integer> indexs, int num, Role role)
	{
		if(role == null)
			return super.getRoleInScreensByNum(indexs,num, role);
		java.util.Map<Long, Role> roles = new java.util.HashMap<Long, Role>();
		if(role.getTeam() == null)
			return roles;
		Set<Role> teammates = new HashSet<Role>();
		teammates.add(role.getTeam().getCapitan());
		teammates.addAll(role.getTeam().getMembers());
		teammates.addAll(role.getTeam().getLeavers());
		for(Role teammate : teammates)
		{
			if(teammate == role)
				continue;
			if(teammate.getScene() != role.getScene())
				continue;//不在同一个场景
			if(!indexs.contains(teammate.getMyScreenIndex()))
				continue;//不在屏块中
			roles.put(teammate.getRoleID(), teammate);
		}
		return roles;
	}
	
	@Override
	public Map<Long,Role> getRoleCannotSee(java.util.Set<Integer> indexes, int maxnum, Role role)
	{
		return new HashMap<Long, Role>();//队伍里的人早就看到了，看不到其他人
	}
	
}
