package fire.pb.gm;

import java.util.Map;

/**
 * 查询怪物数量的GM命令
 * @author Administrator
 *
 */
public class GM_cxgwsl extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final int actid;
		final int cxtype;
		if (args.length >= 2) {
			cxtype = Integer.parseInt(args[0]);
			actid = Integer.parseInt(args[1]);
		}
		else if (args.length >= 1) {
			cxtype = Integer.parseInt(args[0]);
			actid = 0; //查询事件怪物时不需要此参数
		}
		else {
			usage();
			return true;
		}
		
		if (cxtype == 0) { //定时怪物查询
			xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actid);
			if (npcInfo == null) {
				sendToGM("没有怪物了");
				return false;
			}
			int nCnt = 0;
			for (Map.Entry<Long, xbean.timerNpcInfo> npcinfos : npcInfo.getNpcinfo().entrySet()) {
				if (npcinfos.getValue().getNpcstatus() == 0 || npcinfos.getValue().getNpcstatus() == 1) { //没有消失
					nCnt ++;
				}
			}
			
			sendToGM("还有定时怪物数量:" + nCnt);
		}
		else if (cxtype == 1) { //事件怪物查询
			int nCnt = fire.pb.activity.timernpc.TimerNpcService.getInstance().getEventNpcSize();
			sendToGM("还有事件怪物数量:" + nCnt);
		}
		
		return true;
	}

	@Override
	String usage() {
		return "//cxgwsl type actid";
	}

}
