package fire.pb.huoban;

import fire.pb.effect.HuoBanImpl;
import mkdb.Procedure;

public class PHuoBanLevelUp extends Procedure {
	
	private final long roleId;
	private final int level;

	public PHuoBanLevelUp(long roleid, int level) {
		this.roleId = roleid;
		this.level = level;
	}

	@Override
	protected boolean process() throws Exception {
		
		HuoBanColumn huobancol = HuoBanColumn.getHuoBanColumn(roleId, false);
		if (huobancol == null)
			return false;
		
		for (HuoBan huoban : huobancol.getAllHuoBans().values()) {
			huoban.setLevel(level);
			final fire.pb.effect.Role ehuoban = huoban.getEffectRole(roleId);
			((HuoBanImpl) ehuoban).levelUp();
		}
		
//		HuoBan huoban = huobancol.getHuoBan(huobanId);
//		if (huoban == null)
//			return false;
//		
//		huoban.setLevel(level);
//
//		final fire.pb.effect.Role ehuoban = huoban.getEffectRole();
//		Map<Integer, Float> changeattrs = new HashMap<Integer, Float>();
//		((HuoBanImpl) ehuoban).levelUp();
//		Map<Integer, Float> res = ((HuoBanImpl) ehuoban).levelUp();
//		if (res != null) {
//			changeattrs.putAll(res);
//		}
		
		return true;
	}

}
