package fire.pb.scene.movable;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fire.pb.scene.manager.SceneNpcManager;

public class Monstershow extends NPC{
	

	
	Monstershow(long npckey, int baseID ) {
		super(npckey, baseID, "", null, null, 0);
	}
	
	public boolean isLoser( long roleid ) {
		Long deadline = losers.get( roleid );
		if ( deadline != null ) {
			Calendar now = Calendar.getInstance();
			SceneNpcManager.logger.debug( "系统时间为" + (now.getTimeInMillis() / 1000) + ", 超时时间为" + deadline
					+ ", 差距为" + (deadline - now.getTimeInMillis() / 1000));
			if ( now.getTimeInMillis() / 1000 < deadline ) {
				return true;
			} else {
				losers.remove( roleid );
			}
		}
		return false;
	}
	
	public boolean addLoser( final java.util.List<Long> ids ) {
		Calendar now = Calendar.getInstance();
		Calendar futureline = Calendar.getInstance();
		futureline.add( Calendar.SECOND, DELAY );
		for ( Long id : ids ) {
			Long deadline = losers.get( id );
			if ( deadline == null || now.getTimeInMillis() / 1000 - deadline > DELAY ) {
				losers.put( id, futureline.getTimeInMillis() / 1000 );
				return true;
			} 
		}
		return false;
	}
	
	public boolean addLoser( final long roleid ) {
		Calendar now = Calendar.getInstance();
		Long deadline = losers.get( roleid );
		if ( deadline == null || now.getTimeInMillis() / 1000 - deadline > DELAY ) {
			now.add( Calendar.SECOND, DELAY );
			losers.put( roleid, now.getTimeInMillis() / 1000 );
			return true;
		} 
		return false;
	}
	
	
	private static final int DELAY = 10; // 失败后DELAY秒内不能触发该怪
	public volatile Map<Long, Long> losers = new HashMap<Long, Long>();
}