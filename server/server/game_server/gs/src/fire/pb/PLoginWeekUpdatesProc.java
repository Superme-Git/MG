package fire.pb;

import org.apache.log4j.Logger;

/**
 * 登陆的时候处理玩家每周更新事件
 *
 */
public class PLoginWeekUpdatesProc extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("SYSTEM");
	private long roleId;
	
	public PLoginWeekUpdatesProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.RoleUpdateTime roleUpdateTime=xtable.Roleupdatetimetab.get(roleId);
		if(roleUpdateTime==null){
			roleUpdateTime=xbean.Pod.newRoleUpdateTime();
			xtable.Roleupdatetimetab.insert(roleId, roleUpdateTime);
		}
		if(roleUpdateTime.getWeekupdatetime()==0){
			roleUpdateTime.setWeekupdatetime(fire.pb.util.DateValidate.getWeekNumber());
		}
		if(roleUpdateTime.getWeekupdatetime()!=fire.pb.util.DateValidate.getWeekNumber()){
			//处理周更新
			pexecute(new PClearHelpCountProc(roleId,PClearHelpCountProc.UPDATE_WEEK));//处理援助统计
			logger.info("角色id\t"+roleId+"--------每周更新");
		}
		roleUpdateTime.setWeekupdatetime(fire.pb.util.DateValidate.getWeekNumber());
		
		return true;
	}
	
}
