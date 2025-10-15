package fire.pb.skill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.script.JavaScript;


public class BuffArg
{
	final public int buffId;
	public JavaScript rateJs;
	public JavaScript roundJs;
	public JavaScript timeJs;
	public Map<Integer,JavaScript> effectJsMap;	
	public JavaScript confilictStateJs;
	public JavaScript oddsJs;

	
	public BuffArg(BuffArg o)
	{
		this.buffId = o.buffId;
		this.rateJs = o.rateJs;
		this.roundJs = o.roundJs;
		this.timeJs = o.timeJs;
		this.confilictStateJs = o.confilictStateJs;

		this.effectJsMap  = new HashMap<Integer, JavaScript>();
		for (Map.Entry<Integer, JavaScript> entry : o.effectJsMap.entrySet())
			this.effectJsMap.put(entry.getKey(), entry.getValue());
	

		this.oddsJs = o.oddsJs;
	}
	
	public BuffArg copy() {
		return new BuffArg(this);
	}	
	
	
	public BuffArg(int buffId)
	{
		this.buffId = buffId;
	}	
	
	public void ReplacerateJs(String jsstr)
	{
		if (jsstr != null)
			rateJs = new JavaScript(jsstr);
		else
			rateJs = new JavaScript("1");
	}	
	public void ReplaceroundJs(String jsstr)
	{
		if (jsstr != null)
			roundJs = new JavaScript(jsstr);
		else
			roundJs = new JavaScript("1");
	}	
	
	
	public void ReplacetimeJs(String jsstr)
	{
		if (jsstr != null)
			timeJs = new JavaScript(jsstr);
		else
			timeJs = new JavaScript("1");
	}		
	
	public void ReplaceconfilictStateJs(String jsstr)
	{
		if (jsstr != null)
			confilictStateJs = new JavaScript(jsstr);
		else
			confilictStateJs = new JavaScript("1");
	}	
	
	public void ReplaceoddsJs(String jsstr)
	{
		if (jsstr != null)
			oddsJs = new JavaScript(jsstr);
		else
			oddsJs = new JavaScript("1");
	}	

	public void  parseUsedAttrTypes(List<Integer> usedattrtypea, List<Integer> usedattrtypeb)
	{
		if(rateJs!= null) rateJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(roundJs!= null) roundJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(timeJs!= null) timeJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(confilictStateJs!= null) confilictStateJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(oddsJs!= null) oddsJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		for (Map.Entry<Integer, JavaScript> entry : effectJsMap.entrySet())
			entry.getValue().parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
	}

}
