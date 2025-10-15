package fire.pb.cross;

/**
 * 跨服和原服务之间的协议处理
 * @author dc
 */
public class PFinishCopyRoleProc extends mkdb.Procedure {
	
	private FinishCopyRole pro;
	
	public PFinishCopyRoleProc(FinishCopyRole p){
		this.pro = p;
	}

	@Override
	protected boolean process() throws Exception {
		CrossManager.logger.info("PSendRoleDataProc.原服收到来自跨服角色roleId=" + pro.roleid + "的FinishCopyRole协议!");
		if (CrossManager.getInstance().isInCrossServer() || pro.roleid <= 0) {
			CrossManager.logger.error("PSendRoleDataProc.角色 " + pro.roleid +",跨服收到拷贝数据成功协议！");
			return false;
		}
		xbean.CrossTempRole crole = xtable.Crosstemproles.get(pro.roleid);
		if (crole == null) {
			CrossManager.logger.error("PSendRoleDataProc.roleId=" + pro.roleid +"数据拷贝成功时,原服CrossTempRole为空");
			return false;
		}
		crole.setCopyresult(Constant.COPY_DATA_RESULT_SUCC);
		crole.setLastcopytime(System.currentTimeMillis());
		CrossManager.getInstance().notifyClientCross(pro.roleid);
		CrossManager.logger.info("PSendRoleDataProc.roleId=" + pro.roleid +"数据同步已经成功,通知客户端跨服.");
		return true;
	}
	
}
