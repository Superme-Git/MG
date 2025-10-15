package fire.pb.master.proc;

import fire.pb.master.MasterManager;
import fire.pb.title.Title;

/**
 * 师傅榜称谓处理
 * @author nobody
 */
public class PMasterRankListProc extends mkdb.Procedure{

	public PMasterRankListProc(){
		
	}

	@Override
	protected boolean process() throws Exception {
		xbean.ShiDeZhiRankList shideRank = xtable.Roleshidelist.select(1);
		if(shideRank == null || shideRank.getRecords().size() == 0){
			return false;
		}		
		for(int i=0; i<shideRank.getRecords().size(); i++){
			int titleId = MasterManager.getInstance().getTitleIdByRankIndex(i);
			long roleId = shideRank.getRecords().get(i).getRoleid();
			if(titleId > 0){
				 giveTitle(roleId, titleId);
			}
		}
		return true;
	}
	
	
	public void giveTitle(final long roleId, final int titleId){
		mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure(){
			@Override
			public boolean process(){
				
				Title title = new Title(roleId, false);
				if (title.roleHaveTitle(titleId)){
					title.removeTitle(titleId);
				}
				if (!title.roleHaveTitle(titleId)){
					title.addTitle(titleId, "", -1);
					title.onTitle(titleId);
				}
				return true;
			}
		});
	}
	
	
	
	
}
