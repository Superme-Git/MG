
package fire.pb.clan;

import java.util.Collection;

import mkdb.Procedure;


/**
 * 药品刷新逻辑
 *
 */
public class PClanMedicItem extends Procedure {
	public static final int SELECT_BASE=0;//正常产药
	public static final int SELECT_DOUBLE=1;//双倍
	public static final int SELECT_THREE=2;//三倍
	
	
	public static final int SELECT_BASE_NUM=1;//正常产药每组数量
	public static final int SELECT_DOUBLE_NUM=2;//双倍每组数量
	public static final int SELECT_THREE_NUM=3;//三倍每组数量
	
	public PClanMedicItem() {
		super();
	}


	@Override
	protected boolean process() throws Exception {
		Collection<xbean.ClanInfo> clanInfoList =  Module.clanListMap.values();
		for (xbean.ClanInfo clan : clanInfoList) {
			mkdb.Procedure proc=fire.pb.clan.srv.ClanManage.refreshMedic(clan.getKey());
			mkdb.Procedure.pexecuteWhileCommit(proc);
		}
		return true;
	}
	
	
	
	
}

