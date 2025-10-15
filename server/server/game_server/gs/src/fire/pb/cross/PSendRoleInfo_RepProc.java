package fire.pb.cross;

/**
 * 跨服和原服务之间的协议处理
 * @author dc
 */
public class PSendRoleInfo_RepProc extends mkdb.Procedure{
	
	private final SendRoleInfo_Rep pro;
	
	public PSendRoleInfo_RepProc(SendRoleInfo_Rep p){
		this.pro = p;
	}

	@Override
	protected boolean process() throws Exception {
		CrossManager.logger.info("PSendRoleInfo_RepProc.原服务收到跨服的roleId=" + pro.roleid + "的SendRoleInfo_Rep协议!");
		if (CrossManager.getInstance().isInCrossServer() || pro.roleid <= 0) {
			return false;
		}
		
		xbean.CrossTempRole crole = xtable.Crosstemproles.get(pro.roleid);
		if(crole == null){
			crole = xbean.Pod.newCrossTempRole();
			xtable.Crosstemproles.insert(pro.roleid, crole);
		}
		if (crole.getCopyresult() == Constant.COPY_DATA_RESULT_FAILED || 
				System.currentTimeMillis() - crole.getLastcopytime() > Constant.CROSS_COPY_DATA_EXPIRE_TIME) {
			CrossManager.getInstance().beginSendRoleData(pro.roleid, crole);
			return true;
		}
		if(crole.getCopyresult() == Constant.COPY_DATA_RESULT_PROGRESSING){
			CrossManager.logger.error("PSendRoleInfo_RepProc.角色 " + pro.roleid + "正在跨服中,请稍后...");
			return true;
		}
		CrossManager.logger.info("PSendRoleInfo_RepProc.角色roleId=" + pro.roleid + "数据2小时内已经同步过,可以直接跨服!");
		CrossManager.getInstance().notifyClientCross(pro.roleid);
		return true;
	}
}
