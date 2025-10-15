package fire.pb.battle.ai;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import fire.script.JavaScript;

/**条件需求*/
public class Require
{
	int index;
	String strBattleRound = null;//整场战斗持续的回合数,默认null时，没有回合限制
	String strFighterRound = null;//AI怪出现的回合数。默认null时，没有回合限制
	List<Integer> beActedSkills = new LinkedList<Integer>();//被使用过某个技能
	float  odds = 1;//触发几率,默认为1
	public Map<Integer,String> goalFilters = new HashMap<Integer, String>();//目标过滤条件，String中保存数量比较
	public List<Integer> goalFilterIds = new LinkedList<Integer>();//用到的所有目标过滤器ID 
	public JavaScript strGoalRequire = null;//目标过滤条件
	public JavaScript requireJS = null; //JavaScript条件
	public int bounusnum = 0;
	
	Require(int index)
	{
		this.index = index;
	}
}
