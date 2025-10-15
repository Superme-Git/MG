package fire.pb.statistics;

public class ConfigManager {
	private static ConfigManager instance = 
		new ConfigManager( fire.pb.main.ConfigManager.getInstance() );
	public static ConfigManager getInstance() { return instance; }
	private ConfigManager( fire.pb.main.ConfigManager cm ) {
		if ( cm == null )
			throw new NullPointerException( "ConfigManager 尚未初始化" );
		maxGiftidx = 0;
	}
	

	private int maxGiftidx;
	public int getMaxGiftidx() {
		return maxGiftidx;
	}
}
