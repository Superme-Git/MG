package fire.pb.huoban;

import fire.pb.util.CalendarUtil;
import mkdb.Procedure;

public class PAddHuoBanTimes extends Procedure {
	private final long roleid;
	private final int times;
	private final int timetype;
	private final int huobanid;
	
	public PAddHuoBanTimes(final long roleid, final int times, final int timetype, final int huobanid) {
		this.roleid = roleid;
		this.times = times;
		this.timetype = timetype;
		this.huobanid = huobanid;
	}

	@Override
	protected boolean process() throws Exception {
		HuoBanColumn huobanCol = HuoBanColumn.getHuoBanColumn(roleid, false);
		HuoBan huoban = huobanCol.getHuoBan(huobanid);
		
		int nState = huoban.getHuoBanState();
		if (nState == 1)
			return true;
		long nDays = 0;
		if (nState != 3) {
			nDays = System.currentTimeMillis();
		}
		else {
			nDays = huoban.getHuoBanTimes();
		}
		
		huoban.setHuoBanState(3);
		switch (timetype) {
		case 0: // 天
			nDays += times * 24 * 60 * 60 * 1000;
			break;
		case 1: // 小时
			nDays += times * 60 * 60 * 1000;
			break;
		case 2: // 秒
			nDays += times * 1000;
			break;
		default:
			break;
		}
		
		huoban.setHuoBanTimes(nDays);
		huoban.setHuoBanState(3);
		
		SActiveHuoBan activeHuoban = new SActiveHuoBan();
		activeHuoban.huobanid = this.huobanid;
		int timeLast = CalendarUtil.getLastListFormat(nDays) + 10; //分钟
		activeHuoban.state = timeLast;
		
		Procedure.psendWhileCommit(roleid, activeHuoban);
		
		return true;
	}

}
