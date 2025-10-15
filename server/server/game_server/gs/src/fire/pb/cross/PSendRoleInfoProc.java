package fire.pb.cross;

public class PSendRoleInfoProc extends mkdb.Procedure{
	
	private final SendRoleInfo pro;
	
	public PSendRoleInfoProc(SendRoleInfo p){
		this.pro = p;
	}

	@Override
	protected boolean process() throws Exception {
		CrossManager.logger.info("PSendRoleInfoProc.跨服收到来自服务器:" + pro.myzoneid + "的角色roleId=" + pro.roleid + ",userid="+pro.userid +"的SendRoleInfo协议!");
		if (!CrossManager.getInstance().isInCrossServer()){
			return false;
		}
		if (pro.myzoneid <= 0) {
			CrossManager.logger.error("PSendRoleInfoProc.角色[" + pro.roleid +"]所在服务器id:" + pro.myzoneid + ",错误！");
			return false;
		}
		
		//设置跨服里的帐号角色对应关系
		xbean.User user = xtable.User.get(pro.userid);
		if (user == null) {
			user = xbean.Pod.newUser();
			user.setCreatetime(System.currentTimeMillis());
			xtable.User.insert(pro.userid, user);
		}
		if (!user.getIdlist().contains(pro.roleid)){
			user.getIdlist().add(pro.roleid);
		}
		
		xbean.CrossTempRole crole = xtable.Crosstemproles.get(pro.roleid);
		if (crole == null) {
			crole = xbean.Pod.newCrossTempRole();
			crole.setZoneid(pro.myzoneid);
			xtable.Crosstemproles.insert(pro.roleid, crole);
		}
		
		if (System.currentTimeMillis() - crole.getLastcopytime() > Constant.CROSS_COPY_DATA_EXPIRE_TIME) {
			crole.setLastcopytime(System.currentTimeMillis());
			crole.getFinishedtable().clear();
			crole.setCopyresult(Constant.COPY_DATA_RESULT_PROGRESSING);
			crole.setZoneid(pro.myzoneid);
			crole.setCrosstatus(false);
		}
		
		if (pro.needcleardata == 1){
			crole.getFinishedtable().clear();  //防止原服务器重启了
		}

		SendRoleInfo_Rep snd = new SendRoleInfo_Rep(pro.roleid, pro.myzoneid, Constant.FROM_CROSS);
		CrossManager.getInstance().sendToServerByServerId(pro.myzoneid,Constant.SendRoleInfo_Rep, snd);
		CrossManager.logger.info("PSendRoleInfoProc.跨服角色roleId=" + pro.roleid +",userid="+pro.userid +"发送SendRoleInfo_Rep协议到服务器:" + pro.myzoneid);
		return true;
	}

}
