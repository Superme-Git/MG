package fire.pb.product;

public interface Commontext {
	public static final int EQUIP = 1;
	public static final String REASON_STRING = "装备相关";
	public static final int	WEAPON	= 40104; //武器
	public static final int	WRISTLET	= 1; //护腕
	public static final int	ACCESSORY	= 2; //珠宝
	
	public static final int	HEADDRESS	= 6; //头部
	public static final int	CLOAK	= 7; //背部
	public static final int	BOOT	= 5; //靴子
	public static final int	BELT = 4;//腰带
	
	public static final int PROBABILITY_BASE_VALUE = 10000000;//加权概率的基数
	
	public static final int TIE_NOT_ENOUGH = -3;//寒铁不足
	public static final int TUZHI_NOT_ENOUGH = -4;//图纸不足
	public static final int MONEY_NOT_ENOUGH = -5;//金钱不足
	public static final int ZHI_ZHAO_FU_NOT_ENOUGH = -11;//制造符不足
	public static final int QIANG_HUA_SHI_NOT_ENOUGH = -12;//强化石不足
	public static final int XIU_LI_CAI_LIAO_NOT_ENOUGH = -13;//修理材料不足
	public static final int SKILL_EFFECT_RATE_BASE = 1000000;
	public static final int EQUIP_EFFECT_RATE_BASE = 10000;
	
}
