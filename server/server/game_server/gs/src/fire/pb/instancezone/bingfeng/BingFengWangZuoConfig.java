

package fire.pb.instancezone.bingfeng;

import fire.pb.instance.SBingFengCfg;
import fire.pb.util.Misc;


public class BingFengWangZuoConfig {

	private SBingFengCfg sBingFengCfg;
	
	private int posx;
	private int posy;
	
	private int[] ratios;
	private int[] awardids;

	public BingFengWangZuoConfig(SBingFengCfg sBingFengCfg) {
		this.sBingFengCfg = sBingFengCfg;
		String[] strs = sBingFengCfg.ZuoBiao.split(",");
		posx = Integer.parseInt(strs[0]);
		posy = Integer.parseInt(strs[1]);
		strs = sBingFengCfg.BaoxiangRate.split(";");
		ratios = new int[strs.length];
		for (int i = 0; i < strs.length; i++) {
			ratios[i] = Integer.parseInt(strs[i]);
		}
		strs = sBingFengCfg.awardid.split(";");
		awardids = new int[strs.length];
		for (int i = 0; i < strs.length; i++) {
			awardids[i] = Integer.parseInt(strs[i]);
		}
	}
	
	public int getId(){
		return sBingFengCfg.id;
	}
	
	public int getMinLv(){
		return sBingFengCfg.minlevel;
	}
	
	public int getMaxLv(){
		return sBingFengCfg.maxlevel;
	}
	
	public int getInstzoneid(){
		return sBingFengCfg.instzoneid;
	}
	
	public int getBattleNpc(){
		return sBingFengCfg.FocusNpc;
	}
	
	public int getBattleId(){
		return sBingFengCfg.Fightid;
	}
	
	public int getStage(){
		return sBingFengCfg.state;
	}
	
	public int getMapid(){
		return sBingFengCfg.Map;
	}
	public int getPosx(){
		return posx;
	}
	public int getPosy(){
		return posy;
	}
	public int getdestMap(){
		return sBingFengCfg.destmap;
	}
	
	public int getExpAwardid(){
		return sBingFengCfg.expawardid;
	}
	
	public boolean getIsBoos() {
		return sBingFengCfg.boss == 1;
	}
	
	public int getFanPaiId(int grade) {
		return sBingFengCfg.fanpailist.get(grade - 1);
	}
	
	public int getFanPaiTimes() {
		return sBingFengCfg.fanpaitime;
	}
	
	public int getLandId() {
		return sBingFengCfg.instzoneid;
	}
	
	public int giveAward(){
		int randomValue = Misc.getRandomBetween(1, 1000);
		int index = -1;
		for (int i = 0; i < ratios.length; i++) {
			if (randomValue <= ratios[i]){
				index = i;
				break;
			}else {
				randomValue = randomValue - ratios[i];
			}
		}
		if (index < awardids.length && index >=0)
			return awardids[index];
		else
			return 0;
	}
	
}

