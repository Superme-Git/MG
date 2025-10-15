package fire.pb.battle.ai;

import java.util.LinkedList;
import java.util.List;

import fire.script.JavaScript;

public class TargetFilter
{
	int id;//对象条件ID
	public List<Integer> buffs = new LinkedList<Integer>();//拥有某些buff的目标
	public JavaScript buffjs = null;
	
	TargetFilter(int id)
	{
		this.id = id;
	}
}
