package fire.pb.effect;

import java.util.Map;

import fire.pb.DataInit;
import fire.pb.attr.AttrType;
import fire.pb.huoban.Module;
import xbean.BasicFightProperties;
import xbean.HuoBanColumn;
import xbean.HuoBanInfo;

public class HuoBanImpl extends AttrCalcRole {
	
	private final xbean.HuoBanInfo huobanInfo;
	private final xbean.HuoBanColumn column;
	private final long roleid;
	
	public HuoBanImpl(HuoBanInfo huobanInfo, final long roleid) {
		this.roleid = roleid;
		this.huobanInfo = huobanInfo;
		this.column = null;
	}
	
	public HuoBanImpl(HuoBanColumn huobanCol, final long roleid, int huobanId) {
		this.roleid = roleid;
		this.column = huobanCol;
		this.huobanInfo = this.column.getHuobans().get(huobanId);
	}	
	
	public HuoBanImpl(long roleid, int huobanId)
	{
		this(roleid, huobanId, false);
	}

	public HuoBanImpl(long roleid, int huobanId, boolean readonly)
	{		
		this.roleid = roleid;
		if (readonly)
			column = xtable.Huobancolumns.select(roleid);
		else
			column = xtable.Huobancolumns.get(roleid);
		
		if (column == null) {
			throw new IllegalArgumentException("错误的伙伴信息,roleid:" + roleid);
		}
		
		this.huobanInfo = column.getHuobans().get(huobanId);
	}

	@Override
	public int addSp(int v, int maxsp) {		
		return 0;
	}

	@Override
	public Map<Integer, Float> addWound(int v) {		
		return null;
	}

	@Override
	public int setHp(int hp) {		
		return 0;
	}

	@Override
	public int setMp(int mp) {		
		return 0;
	}
	
	@Override
	public int setSp(int sp) {		
		return 0;
	}
	
	@Override
	public int getLevel() {		
		return huobanInfo.getLevel();
	}

	@Override
	public void fullEnergy() {	

	}

	@Override
	public void fullPhyforce() {		

	}

	@Override
	public Map<Integer, Float> fullHpAndRecoverWound() {		
		return null;
	}

	@Override
	public int getHp() {		
		return 0;
	}

	@Override
	public int getMp() {		
		return 0;
	}

	@Override
	public int getSp() {		
		return 0;
	}

	@Override
	public int getWound() {		
		return 0;
	}

	@Override
	public long getNextExp() {		
		return 0;
	}

	@Override
	public Map<Integer, Float> addPoints(int cons, int str, int agi, int endu,
			int iq) {		
		return null;
	}

	@Override
	public Map<Integer, Float> resetPoints()
	{		
		return null;
	}

	@Override
	public Map<Integer, Float> changeScheme(int schemeid) {		
		return null;
	}

	@Override
	public Map<Integer, Float> levelUp() {
		if (getLevel() >= DataInit.PET_LEVEL_MAX)
			return null;
		
		//成长
		
		
		fullHp();
		fullMp();
		return null;
	}
	
	@Override
	public Map<Integer, Float> recovery() {		
		return null;
	}

	@Override
	public Map<Integer, Float> recoverToMaxHpMpPct(double hppct, double mppct,
			boolean ulhp2hp) {
		
		return null;
	}

	@Override
	public void clear() {		

	}

	@Override
	public int getBodySize() {		
		return 0;
	}

	@Override
	public Map<Integer, Float> correctEffects(Map<Integer, Float> buffeffects) {		
		return null;
	}

	@Override
	protected Map<Integer, Float> getEffects() {
		final int rolelv = xtable.Properties.selectLevel(roleid);
		return Module.getHuobanEffect(rolelv, huobanInfo.getId());
	}

	@Override
	protected BasicFightProperties getBFP() {		
		return null;
	}

	@Override
	protected float calcPflimit() {		
		return 0;
	}

	@Override
	protected float calcEnlimit() {		
		return 0;
	}

	@Override
	protected float calcMaxMp() {
		final int rolelv = xtable.Properties.selectLevel(roleid);
		Map<Integer, Float> effects = Module.getHuobanEffect(rolelv, huobanInfo.getId());
		if (effects != null) {
			return effects.get(AttrType.MAX_HP);
		}
		
		return 0.0f;
	}

	@Override
	protected float calcMaxHp() {		
		return 0;
	}

	@Override
	protected float calcSpeed() {		
		return 0;
	}

	@Override
	protected float calcDodge() {		
		return 0;
	}

	@Override
	protected float calcHit() {		
		return 0;
	}

	@Override
	protected float calcDefend() {		
		return 0;
	}

	@Override
	protected float calcMagicDef() {		
		return 0;
	}

	@Override
	protected float calcMagicAttack() {		
		return 0;
	}

	@Override
	protected float calcAttack() {		
		return 0;
	}

	@Override
	protected Map<Integer, Float> getFinalAttrs() {
		final int rolelv = xtable.Properties.selectLevel(roleid);
		return Module.getHuobanFinalAttrs(rolelv, huobanInfo.getId());
	}

	@Override
	protected float calcMedical() {		
		return 0;
	}

	@Override
	protected float calcSeal() {		
		return 0;
	}

	@Override
	protected float calcUnseal() {		
		return 0;
	}

	@Override
	public int getScore() {
		return 0;
	}
}
