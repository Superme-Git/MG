package fire.pb.cross;

public class CheckCopyDataResultTask implements Runnable{
	
	private long roleId;
	
	public CheckCopyDataResultTask(long roleId) {
		this.roleId = roleId;
	}

	@Override
	public void run() {
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				Integer userId = xtable.Properties.selectUserid(roleId);
				if (userId != null) {
					long transferingRole = CrossManager.getInstance().getTransferRoleByUserId(userId);
					if (transferingRole == roleId){
						CrossManager.getInstance().removeTransferRoleByUserId(userId);
					}
				}
				long now = System.currentTimeMillis();
				xbean.CrossTempRole crole = xtable.Crosstemproles.get(roleId);
				if (crole == null || now - crole.getLastcopytime() > Constant.CROSS_COPY_DATA_EXPIRE_TIME || crole.getCopyresult() != Constant.COPY_DATA_RESULT_SUCC) {
					CrossManager.logger.info("CheckCopyDataResultTask.roleId=" + roleId + "跨服失败，请稍后再尝试...");
					if (crole != null) 
						crole.setCopyresult(Constant.COPY_DATA_RESULT_FAILED);
				}
				return true;
			}
		}.submit();
	}
	


}
