package fire.pb.battle.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fire.script.JavaScript;

/**对象筛选条件*/
public class TargetFilter
{
	int id;//对象条件ID
	float hpPercentMin = -1;//最小hp百分比
	float hpPercentMax = -1;//最大hp百分比
	float mpPercentMin = -1;//最小mp百分比
	float mpPercentMax = -1;//最大mp百分比
	int relation = -1;//AI拥有者与对象的关系	
	List<Integer> unitRelation = new ArrayList<Integer>();//单位关系
	int unitCamp = -1;//单位阵营方
	List<Integer> unitType = new ArrayList<Integer>();//单位类型
	int unitMonsterType = -1;//怪物类型	
	int specialType = 0;//特殊类型		
	int resultType = -1;//当前回合累积到现在的结果类型，死亡和鬼魂会跨回合
	boolean includeDead = false;
	boolean includeHide = false;
	int sex = -1;//性别:1-男；2-女；-1-无性别
	List<Integer> jobs = new LinkedList<Integer>();//职业
	int minLevel = 0;
	int maxLevel = 1000;
	public int sortAttrId = -1;
	boolean sortMaxMin = false;
	public List<Integer> buffs = new LinkedList<Integer>();//拥有某些buff的目标
	public JavaScript buffjs = null;
	List<Integer> monsterIds = new ArrayList<Integer>();//指定ID的怪物
	List<Integer> fighterIds = new ArrayList<Integer>();//指定战斗ID
	
	TargetFilter(int id)
	{
		this.id = id;
	}
}
