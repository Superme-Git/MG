package fire.pb.skill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.script.JavaScript;


public class BuffUnit
{
	final public int buffIndex;
	public JavaScript rateJavascript;
	public JavaScript roundJavascript;
	public JavaScript timeJavascript;
	public Map<Integer,JavaScript> effectJavascriptMap;	
	public JavaScript confilictStateJavascript;
	public int[] requiredBuffIds;
	public int[] confilictBuffIds;
	public JavaScript oddsJavascript;
	public int  flagtype;

	
	public BuffUnit(BuffUnit o)
	{
		this.buffIndex = o.buffIndex;
		this.rateJavascript = o.rateJavascript;
		this.roundJavascript = o.roundJavascript;
		this.timeJavascript = o.timeJavascript;
		this.confilictStateJavascript = o.confilictStateJavascript;

		this.effectJavascriptMap  = new HashMap<Integer, JavaScript>();
		for (Map.Entry<Integer, JavaScript> entry : o.effectJavascriptMap.entrySet())
			this.effectJavascriptMap.put(entry.getKey(), entry.getValue());
	
		if(o.requiredBuffIds != null)
		{
			requiredBuffIds = new int[o.requiredBuffIds.length];
			for(int i = 0; i < o.requiredBuffIds.length; i++)
				this.requiredBuffIds[i] = o.requiredBuffIds[i];
		}
	
		if(o.confilictBuffIds != null)
		{
			confilictBuffIds = new int[o.confilictBuffIds.length];
			for(int i = 0; i < o.confilictBuffIds.length; i++)
				this.confilictBuffIds[i] = o.confilictBuffIds[i];
		}

		this.oddsJavascript = o.oddsJavascript;
		this.flagtype = o.flagtype;		
	}
	
	public BuffUnit copy() {
		return new BuffUnit(this);
	}	
	
	
	public BuffUnit(int buffIndex)
	{
		this.buffIndex = buffIndex;
		flagtype = 0;		
	}	
	
	public void ReplacerateJs(String jsstr)
	{
		if (jsstr != null)
			rateJavascript = new JavaScript(jsstr);
		else
			rateJavascript = new JavaScript("1");
	}	
	public void ReplaceroundJs(String jsstr)
	{
		if (jsstr != null)
			roundJavascript = new JavaScript(jsstr);
		else
			roundJavascript = new JavaScript("1");
	}	
	
	
	public void ReplacetimeJs(String jsstr)
	{
		if (jsstr != null)
			timeJavascript = new JavaScript(jsstr);
		else
			timeJavascript = new JavaScript("1");
	}		
	
	public void ReplaceconfilictStateJs(String jsstr)
	{
		if (jsstr != null)
			confilictStateJavascript = new JavaScript(jsstr);
		else
			confilictStateJavascript = new JavaScript("1");
	}	
	
	public void ReplaceoddsJs(String jsstr)
	{
		if (jsstr != null)
			oddsJavascript = new JavaScript(jsstr);
		else
			oddsJavascript = new JavaScript("1");
	}	

	public void  parseUsedAttrTypes(List<Integer> usedattrtypea, List<Integer> usedattrtypeb)
	{
		if(rateJavascript!= null) rateJavascript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(roundJavascript!= null) roundJavascript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(timeJavascript!= null) timeJavascript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(confilictStateJavascript!= null) confilictStateJavascript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(oddsJavascript!= null) oddsJavascript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		for (Map.Entry<Integer, JavaScript> entry : effectJavascriptMap.entrySet())
			entry.getValue().parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
	}

}
