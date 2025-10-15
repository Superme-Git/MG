package fire.pb.item.make;

import java.util.HashMap;
import java.util.Map;

public class ShuXing {
	
	private String effectname;
	private Map<Integer, BoDongDuan> bodongMap = new HashMap<Integer, BoDongDuan>();
	
	
	public ShuXing(String effect)
	{
		this.effectname = effect;		
	}

	public void PutBoDongDuan(int index, BoDongDuan bdd)
	{
		this.bodongMap.put(index, bdd);		
	}
	
	public String GetEffectName()
	{
		return this.effectname;
	}
	
	public Map<Integer, BoDongDuan> GetBodongMap()
	{
		return bodongMap;
	}
}
