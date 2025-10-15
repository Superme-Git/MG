package fire.pb.redirect;

public class RedirectInfo {
	RedirectInfo(int map, int x, int y, int z){
		mapid = map;
		gposx = x;
		gposy = y;
		gposz = z;
	}
	
	public int getMapID(){return mapid;}
	public int getGPosx(){return gposx;}
	public int getGPosy(){return gposy;}
	public int getGPosz(){return gposz;}
	
	private int mapid;
	private int gposx;
	private int gposy;
	private int gposz;
}
