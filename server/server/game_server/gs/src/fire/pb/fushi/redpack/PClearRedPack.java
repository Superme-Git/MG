package fire.pb.fushi.redpack;

import mkdb.Procedure;

import org.apache.log4j.Logger;

public class PClearRedPack extends Procedure {
	public static final Logger logger = Logger.getLogger("REDPACK");
	public PClearRedPack() {
		
	}

	@Override
	protected boolean process() throws Exception {
		xbean.RedPackInfoViewList redPackInfoList=xtable.Redpackinfoviewlisttab.get(1);
		if(redPackInfoList!=null){
			for(xbean.RedPackBaseInfo redPackBaseInfo:redPackInfoList.getRedpackinfoviewlist().values()){
				if(redPackBaseInfo==null){
					continue;
				}
				mkdb.Procedure proc=new mkdb.Procedure(){
					@Override
					protected boolean process() throws Exception {
						xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.select(redPackBaseInfo.getRoleid());
						if(redPackRoleInfoList==null){
							return false;
						}
						xbean.RedPackInfo redpackinfo=redPackRoleInfoList.getRedpackinfolist().get(redPackBaseInfo.getRedpackid());
						if(redpackinfo==null){
							return false;
						}
						//过滤掉过期的红包
						if(System.currentTimeMillis()-redpackinfo.getSendtime()>=RedPackManage.getValidRedPackTime()){
							//过期的红包，并且已经退款了，需要删除
							if(redpackinfo.getBackflag()==1){
								redPackInfoList.getRedpackinfoviewlist().remove(redPackBaseInfo.getRedpackid());
								logger.info("删除过期红包记录\t红包id"+redPackBaseInfo.getRedpackid());
							}
							return false;
						}
						return true;
					}
				};
				mkdb.Procedure.pexecuteWhileCommit(proc);
			}
		}
		return true;
	}
	


	
	
	

}
