package fire.pb.timer;
import java.util.TimerTask;


/**
 * 每15分钟运行的任务
 *
 */
public class PerQuarterTask  extends TimerTask{

	@Override
	public void run() {
		
		/*没用了注释掉 RoleManager.getRolesByConditions(-1, -1, -1, -1, 1000, 
				new IGetRolesCallBack()
				{
					@Override
					public void process(List<Long> roleIds)
					{
						StringBuilder builder = new StringBuilder("15min log:\t");
						for(long roleId : roleIds)
						{
							xbean.Properties pro = xtable.Properties.select(roleId);
							if(pro != null)
							{
								builder.append(roleId).append(",");
								builder.append(pro.getRolename()).append(",");
								builder.append(pro.getLevel()).append(",");
								builder.append(pro.getShape()).append(",");
								builder.append(pro.getSchool()).append(",");
							}
							Integer userId = xtable.Roleid2userid.select(roleId);
							if(userId != null)
							{
								xbean.AUUserInfo userinfo =  xtable.Auuserinfo.select(userId);
								if(userinfo !=null)
								{
									builder.append(userId).append(",");
									builder.append(userinfo.getLoginip()).append(";  ");
								}
							}
						}
						Module.logger.info(builder.toString());
					}
				});*/
	}
}
