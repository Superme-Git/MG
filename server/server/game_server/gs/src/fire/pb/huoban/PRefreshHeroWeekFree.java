package fire.pb.huoban;
import mkdb.Procedure;

public class PRefreshHeroWeekFree extends Procedure {
	private long roleid;
	private int week;
	
	public PRefreshHeroWeekFree(final long roleid, final int week) {
		this.roleid = roleid;
		this.week = week;
	}
	
	public boolean process() {
		HuoBanColumn huobanColumn = HuoBanColumn.getHuoBanColumn(roleid, false);
		if (huobanColumn == null)
			return false;
		
		huobanColumn.sendHuoBanListOnChange();
		Module.logger.info(new StringBuffer().append("weekfresh: ").append("roleid:").append(roleid).append("week:").append(week));
		return true;
	}

}
