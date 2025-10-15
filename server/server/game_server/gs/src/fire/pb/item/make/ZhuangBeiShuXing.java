package fire.pb.item.make;

import java.util.HashMap;
import java.util.Map;

public class ZhuangBeiShuXing {	
	
	private Map<Integer, ShuXing> erandomMap = new HashMap<Integer, ShuXing>();
	
	public ZhuangBeiShuXing()
	{		
	}
	
	public void PutERandom(int n, ShuXing erandom)
	{
		erandomMap.put(n, erandom);
	}
	
	public Map<Integer, ShuXing> GetERandom()
	{
		return erandomMap;
	}
}
