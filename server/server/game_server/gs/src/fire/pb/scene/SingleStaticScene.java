package fire.pb.scene;


import java.util.HashMap;
import java.util.Map;

import fire.pb.scene.movable.Role;


/**
 * 个人可见的静态场景
 * 
 * @author nobody
 *
 */
public class SingleStaticScene extends StaticScene
{
	
	/**
	 * 在screens中获取对role可见的角色
	 * @param indexs screen indexes
	 * @param role
	 * @return
	 */
	@Override
	public java.util.Map<Long, Role> getRoleInScreensByNum(java.util.Set<Integer> indexs,int num, Role role)
	{
		if(role == null)
			return super.getRoleInScreensByNum(indexs,num, role);
		java.util.Map<Long, Role> roles = new java.util.HashMap<Long, Role>();
		//roles.put(role.getRoleID(),role);//加入自己
		return roles;
	}
	
	@Override
	public Map<Long,Role> getRoleCannotSee(java.util.Set<Integer> indexes, int maxnum, Role role)
	{
		return new HashMap<Long, Role>();//看不到其他人
	}
}
