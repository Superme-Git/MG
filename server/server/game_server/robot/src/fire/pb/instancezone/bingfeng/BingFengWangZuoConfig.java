package fire.pb.instancezone.bingfeng;

import fire.pb.instance.SBingFengCfg;

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

	public int getId() {
		return sBingFengCfg.id;
	}

	public int getMinLv() {
		return sBingFengCfg.minlevel;
	}

	public int getMaxLv() {
		return sBingFengCfg.maxlevel;
	}

	public int getInstzoneid() {
		return sBingFengCfg.instzoneid;
	}

	public int getBattleNpc() {
		return sBingFengCfg.FocusNpc;
	}

	public int getBattleId() {
		return sBingFengCfg.Fightid;
	}

	public int getStage() {
		return sBingFengCfg.state;
	}

	public int getMapid() {
		return sBingFengCfg.Map;
	}

	public int getPosx() {
		return posx;
	}

	public int getPosy() {
		return posy;
	}

	public int getdestMap() {
		return sBingFengCfg.destmap;
	}

	public int getExpAwardid() {
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

	public int getServiceid() {
		return sBingFengCfg.serviceid;
	}

}
