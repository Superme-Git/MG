package fire.pb.scene;

public class StaticScene extends Scene {

	private int weatherId = -1;
	private String weatherParm = "";

	public void emptyProcess() {
	}

	/**
	 * 静态场景ID与地图ID相同
	 */
	public int getMapID() {
		return MapUtil.getBaseMapIdBySceneId(getSceneID());
	}

	public String getOwnerName() {
		return "";
	}

	public void load() {
		MapInfo info = new MapInfo(getMapID());
		info.load();
		setMapInfo(info);

		loadNpc();

		Scene.LOG.info("加载静态场景\t" + getName() + "\t" + getMapID());
	}

	@Override
	public long getOwnerID() {
		return 0;
	}

	public void setWeatherId(int weatherId) {
		this.weatherId = weatherId;
	}

	public void setWeatherParm(String weatherParm) {
		this.weatherParm = weatherParm;
	}

	public int getWeatherId() {
		return weatherId;
	}

	public String getWeatherParm() {
		return weatherParm;
	}
}
