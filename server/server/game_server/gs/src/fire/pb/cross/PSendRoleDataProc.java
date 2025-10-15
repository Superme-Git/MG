package fire.pb.cross;

import fire.pb.game.STransferTable;

/**
 * 跨服和原服务之间的协议处理
 * @author dc
 */
public class PSendRoleDataProc extends mkdb.Procedure{
	
	private final SendRoleData pro;
	
	public PSendRoleDataProc(SendRoleData p){
		this.pro = p;
	}
	
	@Override
	protected boolean process() throws Exception {
		CrossManager.logger.info("PSendRoleDataProc.跨服收到来自角色roleId=" + pro.roleid + "的SendRoleData协议!表名:" + pro.tablename);
//		if (!CrossManager.getInstance().isInCrossServer() || pro.roleid == 0){
//			CrossManager.logger.error("角色 " + pro.roleid +" 原服务收到 玩家的拷贝数据协议");
//			return false;
//		}

		xbean.CrossTempRole crole = xtable.Crosstemproles.get(pro.roleid);
		if (crole == null) {
			CrossManager.logger.error("PSendRoleDataProc.角色roleId=" + pro.roleid + "的CrossTempRole为空!");
			return false;
		}

		STransferTable transferTable = CrossManager.getInstance().getSTransferTable(pro.tablename);
		if(transferTable == null){
			CrossManager.logger.error("PSendRoleDataProc.角色roleId=" + pro.roleid + "的数据表" + pro.tablename +"的配置不存在!");
			return false;
		}
		if (pro.isemptytable != 1) {
			CrossManager.logger.info("PSendRoleDataProc.角色roleId=" + pro.roleid + "的表=" + pro.tablename + "不为空,需要同步.");
			TransferDataManager.getInstance().setTableData(pro.tablename, transferTable.keyType, transferTable.valueType, pro.keydata, pro.valuedata);
		}
		for (TableData relationTableData : pro.relationdata) {
			final STransferTable relateTableInfo = CrossManager.getInstance().getSTransferTable(relationTableData.tablename);
			if(relateTableInfo == null){
				CrossManager.logger.error("PSendRoleDataProc.角色roleId=" + pro.roleid + "的关联数据表" + pro.tablename +"的配置不存在!");
				return false;
			}
			TransferDataManager.getInstance().setTableData(relateTableInfo.tableName, relateTableInfo.keyType, relateTableInfo.valueType, relationTableData.keydata, relationTableData.valuedata);
		}
		crole.getFinishedtable().put(pro.tablename, true);
		CrossManager.logger.info("PSendRoleDataProc.跨服接收到角色roleId=" + pro.roleid + "的数据表:" + pro.tablename);
		//检查是否拷贝完成
		if (crole.getFinishedtable().size() >= CrossManager.getInstance().getSTransferTableSize()) {
			crole.setCopyresult(Constant.COPY_DATA_RESULT_SUCC);
			crole.setLastcopytime(System.currentTimeMillis());
			CrossManager.getInstance().sendToServerByServerId(crole.getZoneid(), Constant.FinishCopyRole, new FinishCopyRole(pro.roleid));
			//CrossManager.getInstance().addCrossServerBuff(pro.roleid);
			CrossManager.logger.info("PSendRoleDataProc.服务器["+crole.getZoneid()+"]的角色[" + pro.roleid + "]数据同居检查完成,通知原服务器角色可以开始跨服！");
		}

		return true;
	}
}
