package fire.pb.battle.ai;

import java.util.LinkedList;
import java.util.List;

import fire.script.JavaScript;


public class Condition
{
	int id;
	public List<Integer> targetFilterIds = new LinkedList<Integer>();//用到的所有目标过滤器ID 
	public JavaScript strGoalRequire = null;//目标过滤条件
	public JavaScript jscondition = null; //JavaScript条件
	
	Condition(int id)
	{
		this.id = id;
	}
}
