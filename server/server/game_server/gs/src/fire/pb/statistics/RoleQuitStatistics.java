package fire.pb.statistics;

import java.util.Calendar;

import fire.pb.SFirstDayExitGame;
import fire.pb.WorldTime;

public class RoleQuitStatistics extends Statistics<xbean.RoleQuitStatistics> {

	public RoleQuitStatistics(long roleid, boolean readonly) {
		super(roleid, readonly);
	}
	
	private final int MAXINDEX = ConfigManager.getInstance().getMaxGiftidx();

	@Override
	protected xbean.RoleQuitStatistics getStatistics() {
		if (readonly) {
			return xtable.Rolequitstatistics.select( roleid );
		} else {
			return xtable.Rolequitstatistics.get( roleid );
		}
	}

	@Override
	protected void newStatistics() {
		if (readonly) {
			statistics = xbean.Pod.newRoleQuitStatisticsData();
		} else {
			statistics = xbean.Pod.newRoleQuitStatistics();
			xtable.Rolequitstatistics.remove(roleid);
			xtable.Rolequitstatistics.insert(roleid, statistics);
		}
	}
	
	public void addMoney(final long money) {
		if (readonly)
			return;
		statistics.setMoney(this.getMoney() + money);
	}
	
	public void addCurrency(final long money, final int moneyType) {
		if (readonly)
			return;
		statistics.getCurrency().put(moneyType, this.getCurrency(moneyType) + money);
	}
	
	public void addExp(final int exp) {
		if (readonly)
			return;
		statistics.setExp( this.getExp() + exp );
	}
	
	private boolean checkLastcount() {
		WorldTime now = WorldTime.getInstance();
		Calendar reward = Calendar.getInstance();
		reward.setTimeInMillis( statistics.getLastcountdate() );
		return checkSameDay( now, reward );
	}
	
	private void sendFirstDayInfo() {
		SFirstDayExitGame send = new SFirstDayExitGame();	
		if (getRewardidx() == 0) {
			WorldTime time = WorldTime.getInstance();
			time.set(Calendar.HOUR_OF_DAY, 0);
			time.add(Calendar.DAY_OF_YEAR, 1);
			time.set(Calendar.MINUTE, 0);
			time.set(Calendar.SECOND, 0);
			send.firstdayleftsecond = (int)((time.getTimeInMillis() - 
					WorldTime.getInstance().getTimeInMillis())/1000);
		} else {
			send.firstdayleftsecond = -1;
		}
		mkdb.Procedure.psendWhileCommit(roleid, send);
	}
	
	public void onLine() {
		final int idx = statistics.getLastrewardidx();
		if (idx > MAXINDEX) {
			sendFirstDayInfo();
			return;
		}
				
		WorldTime now = WorldTime.getInstance();
		Calendar reward = Calendar.getInstance();
		if (statistics.getRewarddate() == 0) {
			statistics.setRewarddate(now.getTimeInMillis());
		}
		reward.setTimeInMillis(statistics.getRewarddate());
		if (!checkSameDay(now, reward)) {
			if (idx == 0) {
				statistics.setLastrewardidx(1);
			}
			incReward(now);
		}
		sendFirstDayInfo();
	}
	
	private boolean checkSameDay( WorldTime date1, Calendar date2 ) {
		if ( date1.get(Calendar.YEAR) != date2.get(Calendar.YEAR) )
			return false;
		return date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR);
	}
	private void initCount() {
		statistics.setLastcountdate( Calendar.getInstance().getTimeInMillis() );
		statistics.setExp( 0 );
		statistics.setMoney( 0 );
	}
	
	private int incReward(WorldTime now) {
		statistics.setLastrewardidx( statistics.getLastrewardidx() + 1 );
		statistics.setRewarddate(now.getTimeInMillis());
		return statistics.getLastrewardidx();
	}

	public long getCurrency(final int moneyType) {
		if (checkLastcount()) {
			if (!statistics.getCurrency().containsKey(moneyType))
				return 0;
			
			return statistics.getCurrency().get(moneyType);
		} 
		else {
			initCount();
			return 0;
		}
	}
	public long getMoney() {
		if ( checkLastcount() ) {
			return statistics.getMoney();
		} else {
			initCount();
			return 0;
		}
	}
	
	public int getExp() {
		if ( checkLastcount() ) {
			return statistics.getExp();
		} else {
			initCount();
			return 0;
		}
	}
	
	public int getRewardidx() {
		return statistics.getLastrewardidx();
	}
	
	public long getRewarddate() {
		return statistics.getRewarddate();
	}
}
