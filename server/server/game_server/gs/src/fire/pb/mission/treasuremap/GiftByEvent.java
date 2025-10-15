package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.List;

import fire.pb.util.Misc;

public class GiftByEvent {
	public int id; //事件id
	public String name; //事件名称
	public int type; //事件类型
	public int group; //事件刷怪组Id 跟事件刷新表.xlsx对应
	public int battleId; //战斗ID
	
	public int skillId; //技能ID
	public int messageId; //给自己的提示
	public int noticeId; //全服通告的提示
	
	public List<Integer> battleAwardId = new ArrayList<Integer>(); //战斗奖励Id
	public List<Integer> battleAwardRate = new ArrayList<Integer>(); //战斗奖励概率
	
	public int getAwardId() {
		if (battleAwardRate.size() == 0)
			return 0;
		
		int nIndex = Misc.getProbability(battleAwardRate);
		if (nIndex < battleAwardId.size()) {
			return battleAwardId.get(nIndex);
		}
		
		return 0;
	}

}
