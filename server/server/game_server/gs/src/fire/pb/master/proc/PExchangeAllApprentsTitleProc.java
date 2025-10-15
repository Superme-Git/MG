package fire.pb.master.proc;

import java.util.ArrayList;
import java.util.List;

import fire.pb.master.MasterManager;
import fire.pb.title.STitleConfig;
import fire.pb.title.Title;
import fire.pb.title.TitleManager;

/**
 * 更新各个徒弟的称谓id
 * @author nobody
 */
public class PExchangeAllApprentsTitleProc extends mkdb.Procedure{
	
	private long mentorId;
	private String mentorName;
	
	public PExchangeAllApprentsTitleProc(long mentorId, String mentorName){
		this.mentorId = mentorId;
		this.mentorName = mentorName;
	}

	@Override
	protected boolean process() throws Exception {
		
		//师徒关系表
		xbean.MenstorInfo menstor = xtable.Apprents.get(mentorId);
		if(menstor == null || menstor.getCurrapprentices().size() == 0){
			return false;
		}
		
		List<Long> roleIds = new ArrayList<Long>();
		for(xbean.CurrApprent curr: menstor.getCurrapprentices()){
			roleIds.add(curr.getApprentinfo().getRoleid());
		}
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));
		
		
		for(int i=0; i<menstor.getCurrapprentices().size(); i++){
			xbean.CurrApprent curr = menstor.getCurrapprentices().get(i);
			int appTitleId = MasterManager.getAppentceTitleIdByIndex(i);
			if(appTitleId > 0 && appTitleId != curr.getTitleid()){
				long roleId = curr.getApprentinfo().getRoleid();
				Title title = new Title(roleId, false);
				title.removeTitle(curr.getTitleid());
				STitleConfig titleConfig = TitleManager.getTitleConfigById(appTitleId);
				if(titleConfig != null){
					title.addTitle(appTitleId, mentorName+titleConfig.getTitlename(), -1);
					title.onTitle(appTitleId);
				}
			}
		}
		return true;
	}
}
