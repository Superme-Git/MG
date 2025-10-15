
package cn.com.zc.db.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 快照结构题
 */
public class ScoreSnapShotBean {
	final static String separator = ",";

	final static String INER_SEPARATOR = ";";
	public static final String TITTLE_STRING="roleId,roleName,roleLevel,rolezonghemaxscore,totalCharge,dayCharge,ybNum,[equip{id;uniqid;score}],[decoration{id;uniqid;score}],[pets{id;uniqid;score}],[xiake{id;score};total]";
	
	/**
	 * 角色id
	 */
	private long roleId;
	
	/**
	 * 人物综合实力历史最高评分
	 */
	private int rolezonghemaxscore;
	
	/**
	 * 宠物评分
	 */
	private List<UniqueScore> pets;
	
	/**
	 * 
	 */
	private List<Scorer> xiakes;
	
	/**
	 * 装备评分
	 */
	private List<UniqueScore> equip;
	
	/**
	 * 饰品评分
	 */
	private List<UniqueScore> decoration;
	
	/**
	 * 符石数量
	 */
	private int fsNum;
	
	/**
	 * 单日充值
	 */
	private int dayCharge;
	
	
	private static final Set<Integer> decorationPositions=new HashSet<Integer>();
	
	private String roleName="";
	
	/**
	 * 总充值数
	 */
	private int totalCharge;
	
	
	/**
	 * 等级
	 */
	private int level;
	
	
//	/**
//	 * 武器
//	 */
//	public final static int ARMS = 0;
//	/**
//	 * 护腕
//	 */
//	public final static int CUFF = 1;
//	/**
//	 * 项链
//	 */
//	public final static int ADORN = 2;
//	/**
//	 * 衣服
//	 */
//	public final static int LORICAE = 3;
//	/**
//	 * 腰带
//	 */
//	public final static int WAISTBAND = 4;
//	/**
//	 * 靴子
//	 */
//	public final static int BOOT = 5;
//	/**
//	 * 头饰
//	 */
//	public final static int TIRE = 6;
//	/**
//	 * 披风
//	 */
//	public final static int KITBAG = 7;
//	/**
//	 * 面纱3=VEIL。。。。。乱
//	 */
//	public final static int EYEPATCH = 8;
//	/**
//	 * 面纱2
//	 */
//	public final static int RESPIRATOR = 9;
//	/**
//	 * 面纱1
//	 */
//	public final static int VEIL = 10;
//	/**
//	 * 时装
//	 */
//	public final static int FASHION = 11; // 时装
	
	static{
		//饰品类型集合详情请见d道具类型表.xlsx，由于读取不到配置数据，有更好的方法请修改 
		decorationPositions.add(6);
		decorationPositions.add(7);
		decorationPositions.add(8);
		decorationPositions.add(9);
		decorationPositions.add(10);
	}
	
	
	
	public ScoreSnapShotBean(long roleId,String roleName,int level){
		this.roleId=roleId;
		this.roleName=roleName;
		this.level=level;
		pets=new ArrayList<ScoreSnapShotBean.UniqueScore>();
		xiakes=new ArrayList<ScoreSnapShotBean.Scorer>();
		equip=new ArrayList<ScoreSnapShotBean.UniqueScore>();
		decoration=new ArrayList<ScoreSnapShotBean.UniqueScore>();
	}
	
	public void  addPet(long uid,int id,String name,int score){
		UniqueScore pet=new UniqueScore();
		pet.uniqid=uid;
		pet.id=id;
		pet.name=name;
		pet.score=score;
		pets.add(pet);
	}
	
	
	/**
	 * 装备
	 
	 */
	public void addEquip(long uid,int equipId,int positionId,int score){
		if (decorationPositions.contains(positionId)) {
			addDecoration(uid,equipId, score);
			return;
		}
		UniqueScore pet=new UniqueScore();
		pet.uniqid=uid;
		pet.id=equipId;
		pet.score=score;
		equip.add(pet);
	}
	
	/**
	 * 饰品
	 
	 */
	private void addDecoration(long uid,int equipId,int score){
		UniqueScore pet=new UniqueScore();
		pet.uniqid=uid;
		pet.id=equipId;
		pet.score=score;
		decoration.add(pet);
	}
	

	/**
	 
	 */
	private Scorer createScore(int equipId, int score) {
		Scorer pet=new Scorer();
		pet.id=equipId;
		pet.score=score;
		return pet;
	}
	
	
	
	
	
	/**
	 */
	public void  addXiaKe(int id,int score){
		Scorer pet = createScore(id, score);
		xiakes.add(pet);
	}
	
	
	/**
	 * @return the roleId
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}





	/**
	 * @return the rolezonghemaxscore
	 */
	public int getRolezonghemaxscore() {
		return rolezonghemaxscore;
	}





	/**
	 * @param rolezonghemaxscore the rolezonghemaxscore to set
	 */
	public void setRolezonghemaxscore(int rolezonghemaxscore) {
		this.rolezonghemaxscore = rolezonghemaxscore;
	}

	
	

	/**
	 * 评分体
	 */
	private  class Scorer{
		 int id;
		 String name="";
		 int score;
	}
	
	
	/**
	 * 评分
	 */
	private class UniqueScore extends Scorer{
		 long uniqid;
	}
	
	

	@Override
	public String toString(){
		StringBuilder builder=new StringBuilder();
		builder.append(roleId);
		builder.append(separator).append(roleName);
		builder.append(separator).append(level);
		builder.append(separator).append(rolezonghemaxscore);
		builder.append(separator).append(this.totalCharge);
		builder.append(separator).append(this.dayCharge);
		builder.append(separator).append(this.fsNum);

		//装备
		builder.append(separator);
		appendUnique(builder,equip);
		
		//饰品
		builder.append(separator);
		appendUnique(builder,decoration);
		
		builder.append(separator);
		appendUnique(builder,pets);
		
		builder.append(separator);
		appendXiaKe(builder);
		return builder.toString();
	}

	private void appendUnique(StringBuilder  builder,List<UniqueScore> uniqueScores){
		builder.append("[");
		for (UniqueScore tmp : uniqueScores) {
			builder.append("{");
			builder.append(tmp.id);
			builder.append(INER_SEPARATOR);
			builder.append(tmp.uniqid);
			builder.append(INER_SEPARATOR);
			builder.append(tmp.score);
			builder.append("}");
			builder.append(INER_SEPARATOR);
		}
		if (!uniqueScores.isEmpty()) {
			//删除最后一个
			builder.deleteCharAt(builder.lastIndexOf(INER_SEPARATOR));
		}
		builder.append("]");
	}
	
	
	/**
	 
	 */
	private void appendXiaKe(StringBuilder builder) {
		builder.append("[");
		int total=0;
		for (Scorer pet : xiakes) {
			total+=pet.score;
			builder.append("{");
			builder.append(pet.id);
			builder.append(INER_SEPARATOR);
			builder.append(pet.score);
			builder.append("}");
			builder.append(INER_SEPARATOR);
		}
		//删除最后一个
		builder.append(total);
		builder.append("]");
	}
	


	/**
	 * @return the ybNum
	 */
	public int getYbNum() {
		return fsNum;
	}

	/**
	 * @param ybNum the ybNum to set
	 */
	public void setYbNum(int ybNum) {
		this.fsNum = ybNum;
	}

	/**
	 * @return the totalCharge
	 */
	public int getTotalCharge() {
		return totalCharge;
	}

	/**
	 * @param totalCharge the totalCharge to set
	 */
	public void setTotalCharge(int totalCharge) {
		this.totalCharge = totalCharge;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the dayCharge
	 */
	public int getDayCharge() {
		return dayCharge;
	}

	/**
	 * @param dayCharge the dayCharge to set
	 */
	public void setDayCharge(int dayCharge) {
		this.dayCharge = dayCharge;
	}
	
	
	
}
