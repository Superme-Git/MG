package fire.pb.huoban;

import java.util.ArrayList;
import java.util.List;

import mkdb.Procedure;

public class PUpdateHuoBanZhenRong extends Procedure {
	private long roleid;
	private int formationId;
	private int zhenrongId;
	private List<Integer> ids = new ArrayList<Integer>();
	private int type; //更新类型 1-更新光环，2-参战伙伴，3-系统更新	

	//更新原因 1-系统第一次自动更新 2-光环更新 3-参战伙伴更新 4-伙伴阵容切换
	public static final int FIRST = 1;
	public static final int FORMATION = 2;
	public static final int HUOBAN = 3;
	public static final int BOTH = 4;
	public static final int OTHER = 5; //只调换伙伴的位置
	
	public PUpdateHuoBanZhenRong(long roleid, int formationId, int zhenrongId, List<Integer> ids, int type){
		this.roleid = roleid;
		this.ids = ids;
		this.formationId = formationId;
		this.zhenrongId = zhenrongId;
		this.type = type;
	}
	
	public boolean process(){
		int changeReason = 1;
		xbean.HuoBanZhenrong zhenrongs = xtable.Huobanzhenrongs.get(roleid);
		if (zhenrongs == null) {
			zhenrongs = xbean.Pod.newHuoBanZhenrong();
			zhenrongs.setCurrent(0);
			xtable.Huobanzhenrongs.insert(roleid, zhenrongs);
		}
		if (this.formationId < 0 || this.formationId > 10) return false;
		xbean.HuoBanZhenrongInfo info = zhenrongs.getZhenrong().get(zhenrongId);
		if(info == null){
			info = xbean.Pod.newHuoBanZhenrongInfo();
			info.setZhenfa(formationId);
			zhenrongs.getZhenrong().put(zhenrongId, info);
		}
		
		HuoBanColumn huobancol = HuoBanColumn.getHuoBanColumn(roleid, false);
		if (huobancol == null)
			return false;
		
		for (Integer huobanId : ids) {
			HuoBan huoban = huobancol.getHuoBan(huobanId);
			if (!huoban.canFight())
				return false;
		}
		
		final int currentzhenrong = zhenrongs.getCurrent();
		
		switch (this.type) {
		case FIRST:
			info.getHuoban().clear();
			info.getHuoban().addAll(ids);
			changeReason = 1;
			break;
		case FORMATION:
			info.setZhenfa(formationId);
			changeReason = 2;
			break;
		case HUOBAN: {
			final int size = info.getHuoban().size(); 
			info.getHuoban().clear();
			info.getHuoban().addAll(ids);
			
			if (size == ids.size()) {
				changeReason = 5;
			}
			else {
				changeReason = 3;
			}
			break;
		}
		case BOTH:
			info.setZhenfa(formationId);
			info.getHuoban().clear();
			info.getHuoban().addAll(ids);
			changeReason = 4;
			break;
		default:
			return false;
		}
		
		if (currentzhenrong == zhenrongId) {
			huobancol.setFightHuoBans(ids);
		}
		
		SChangeZhenrong snd = new SChangeZhenrong();
		snd.zhenrong = zhenrongId;
		snd.zhenfa = info.getZhenfa();
		snd.huobanlist.addAll(info.getHuoban());
		snd.reason = changeReason;
		mkdb.Procedure.psendWhileCommit(roleid, snd);
		
		return true;
	}

}
