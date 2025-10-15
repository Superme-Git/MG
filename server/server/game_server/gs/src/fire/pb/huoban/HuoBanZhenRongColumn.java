package fire.pb.huoban;

import java.util.Map;
import fire.log.Logger;

public class HuoBanZhenRongColumn {
	
private static final Logger logger = Logger.getLogger("HUOBAN");
	
	final long roleId;
	final boolean readonly;
	final xbean.HuoBanZhenrong zhenrongcolumn;
	
	public static HuoBanZhenRongColumn getZhenRongColumn(long roleId, boolean readonly) {
		if (xtable.Properties.select(roleId) == null) {
			logger.error("构造HuoBanColumn时，角色 " + roleId + " 不存在。");
			//throw new IllegalArgumentException("构造HuoBanColumn时，角色 " + roleId + " 不存在。");
			return null;
		}
		xbean.HuoBanZhenrong col = null;
		if (readonly)
			col = xtable.Huobanzhenrongs.select(roleId);
		else
			col = xtable.Huobanzhenrongs.get(roleId);
		
		if (col == null) {
			if (readonly)
				col = xbean.Pod.newHuoBanZhenrongData();
			else {
				col = xbean.Pod.newHuoBanZhenrong();
				xtable.Huobanzhenrongs.insert(roleId, col);
			}
		}
		
		return new HuoBanZhenRongColumn(roleId, col, readonly);
		
	}
	
	private HuoBanZhenRongColumn(long roleId, xbean.HuoBanZhenrong column, boolean readonly) {
		this.roleId = roleId;
		this.zhenrongcolumn = column;
		this.readonly = readonly;
	}
	
	public xbean.HuoBanZhenrongInfo getHuoBanZhenRongInfo(int zhenrongId) {
		xbean.HuoBanZhenrongInfo info = zhenrongcolumn.getZhenrong().get(zhenrongId);
		if (info == null)
			return null;
		
		return info;
	}
	
	public Map<Integer, xbean.HuoBanZhenrongInfo> getZhenRong() {
		return zhenrongcolumn.getZhenrong();
	}
	
	public int getCurrentZhenRong() {
		return zhenrongcolumn.getCurrent();
	}
	
	public void setCurrentZhenRongHuoBan(java.util.List<Integer> huobans) {
		mkdb.Procedure SetZhenRong = new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.HuoBanZhenrongInfo zhengrong = zhenrongcolumn.getZhenrong().get(zhenrongcolumn.getCurrent());
				if (zhengrong == null)
					return false;
				
				zhengrong.getHuoban().clear();
				zhengrong.getHuoban().addAll(huobans);
				
				SChangeZhenrong snd = new SChangeZhenrong();
				snd.zhenrong = zhenrongcolumn.getCurrent();
				int zhenfa = zhengrong.getZhenfa();
				snd.zhenfa = zhenfa;
				snd.huobanlist.addAll(huobans);
				snd.reason = 4;
				mkdb.Procedure.psendWhileCommit(roleId, snd);
				return true;
			}
		};
		
		if (mkdb.Transaction.current() != null) {
			SetZhenRong.call();
		}
		else {
			SetZhenRong.submit();
		}
	}
}
