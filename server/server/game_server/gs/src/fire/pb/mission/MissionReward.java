package fire.pb.mission;

import java.util.HashMap;
import java.util.Map;

import fire.pb.activity.award.RewardObjs;


public class MissionReward {
	
	private long money;
	private long smoney;
	private long exp;
	private Map<Integer, RewardObjs> awardResult;
	private Map<Integer, Integer> items;
	
	public MissionReward() {
		this(0, 0,0, new HashMap<Integer, Integer>(), new HashMap<Integer, RewardObjs>());
	}
	
	public MissionReward(final long money, final long smoney, final long exp, final Map<Integer, Integer> items, final Map<Integer, RewardObjs> awardResult ) {
		this.money = money;
		this.smoney = smoney;
		this.exp = exp;
		this.items = items;
		this.awardResult = awardResult;
	}
	
	public void addReward(MissionReward reward) {
		money += reward.getRewardMoney();
		smoney += reward.getRewardSMoney();
		exp += reward.getRewardExp();
		awardResult.putAll(reward.getRewardResult());
		items.putAll(reward.getRewardItems());
	}
	
	public void addRewardExp(final long exp) { this.exp += exp; }
	
	public void addRewardItem(final int itemid, final int itemnum) { 
		Integer curNum = items.get(itemid);
		if (curNum == null) {
			items.put(itemid, itemnum);
		} else {
			items.put(itemid, curNum + itemnum);
		}
	}
	
	public void addRewardItems(final Map<Integer, Integer> items) { this.items.putAll(items); }
	
	public void addRewardMoney(final long money) { this.money += money; }
	
	public void addRewardResult(final Map<Integer, RewardObjs> awardResult) { this.awardResult.putAll(awardResult); }
	
	public void addRewardSMoney(final long smoney) { this.smoney += smoney; }
	
	public long getRewardExp() { return exp; }
	
	public Map<Integer, Integer> getRewardItems() { return items; }
	
	public long getRewardMoney() { return money; }
	
	public Map<Integer, RewardObjs> getRewardResult() { return awardResult; }
	
	public long getRewardSMoney() { return smoney; }
}
