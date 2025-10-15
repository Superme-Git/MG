package fire.pb.master.proc;

import fire.pb.util.DateValidate;

/**
 * 师傅上线时候的处理
 * @author nobody
 */
public class PMasterOnLineProc extends mkdb.Procedure{
	
	private long masterId;
	
	public PMasterOnLineProc(long masterId){
		this.masterId = masterId;
	}

	@Override
	protected boolean process() throws Exception {

		xbean.MenstorInfo menstor = xtable.Apprents.get(masterId);
		if(menstor == null){
			return false;
		}
		
		for(int i=0; i<menstor.getCurrapprentices().size(); i++){
			xbean.CurrApprent curr =  menstor.getCurrapprentices().get(i);
			long roleId = curr.getApprentinfo().getRoleid();
			xbean.Properties  prop = xtable.Properties.select(roleId);
			if(prop != null){
				curr.getApprentinfo().setLevel(prop.getLevel());
				curr.getApprentinfo().setName(prop.getRolename());
			}	
		}
		
		long curr = System.currentTimeMillis();
		if(!DateValidate.inTheSameDay(curr, menstor.getUpdatetime())){
			for(int i=0; i<menstor.getApprentices().size() ; i++){
				xbean.HasApprent currApp = menstor.getApprentices().get(i);
				long roleId = currApp.getRoleid();
				xbean.Properties  prop = xtable.Properties.select(roleId);
				if(prop != null){
					currApp.setLevel(prop.getLevel());
					currApp.setName(prop.getRolename());
				}
			}
		}
		menstor.setUpdatetime(curr);
		return true;
	}
}
