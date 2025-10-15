package fire.pb.timer;
import java.util.List;
import java.util.TimerTask;

import fire.msp.IGetRolesCallBack;
import fire.pb.main.ConfigManager;
import fire.pb.map.RoleManager;
import fire.pb.master.proc.PMasterRankListProc;
import fire.pb.statistics.StatisticUtil;
import mkdb.Procedure;

/**
 * Date: 2015-07-10
 * @author
 *
 */
public class EndOfDayTask extends TimerTask {
	
	class PClearAndRefreshTasks extends Procedure {

        private List<Long> roleIds ;		
		public PClearAndRefreshTasks(List<Long> roleIds) {
            this.roleIds = roleIds;
		}

		@Override
		protected boolean process() throws Exception {
			//处理活跃度统计
			StatisticUtil.updateOnTheEndOfDay(roleIds,System.currentTimeMillis());
			//师傅榜称谓处理
			pexecute(new PMasterRankListProc());
			
			return true;
		}

	}
	public EndOfDayTask(){
		
	}
	@Override
	public void run() {
		
		RoleManager.getRolesByConditions(-1, -1, -1, -1, -1, 
				new IGetRolesCallBack()
				{
					@Override
					public void process(List<Long> roleIds)
					{
						new PClearAndRefreshTasks(roleIds).call();
					}
				});
	
		ConfigManager.cleanDayMaxRoleNum();
	}
}
