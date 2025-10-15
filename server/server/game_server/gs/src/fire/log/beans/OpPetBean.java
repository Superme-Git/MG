package fire.log.beans;

import java.util.ArrayList;
import java.util.Arrays;

import xbean.PetSkill;

public class OpPetBean {
	private	int	PetId;					//宝宝id（包含npc仓库里的宝宝）
	private	int	PetLvl;					//宝宝等级
	private	int	IsBind;					//是否绑定	0未绑定,1绑定
	private	int	IsBatt;					//是否参战	0未，1是
	private	int	PetSco;					//宝宝评分
	private	int	Bloo;					//气血上限
	private	int	Magi;					//魔法上限
	private	int	IsTrea;					//是否珍品	0否，1是
	private	long ItKey;					//唯一类型ID
	private	int	PhyCh;					//体质目前加点值
	private	int	MagiCh;					//魔力目前加点值
	private	int	Stro;					//力量目前加点值
	private	int	Endu;					//耐力目前加点值
	private	int	FlexCh;					//敏捷目前加点值
	private	ArrayList<Integer>	QA = new ArrayList<Integer>();		//7项资质数据
	private	ArrayList<PetSkill>	SkilSta = new ArrayList<PetSkill>();	//拥有的技能列表（记录每项技能id+技能状态（分为认证和非认证））
	
	public OpPetBean(int petId, int petLvl, int isBind, int isBatt, 
			int petSco, int bloo, int magi, int isTrea, long itKey, 
			int phyCh, int magiCh, int stro, int endu, int flexCh,  
			int bphy,int bmagi,int bstr,int bendu,int bflex, java.util.List<PetSkill> skilSta) {
		PetId = petId;
		PetLvl = petLvl;
		IsBind = isBind;
		IsBatt = isBatt;
		PetSco = petSco;
		Bloo = bloo;
		Magi = magi;
		IsTrea = isTrea;
		ItKey = itKey;
		PhyCh = phyCh;
		MagiCh = magiCh;
		Stro = stro;
		Endu = endu;
		FlexCh = flexCh;
		QA.addAll(Arrays.asList(bphy, bmagi, bstr, bendu, bflex));
		SkilSta.addAll(skilSta);
	}
	public int getPetId() {
		return PetId;
	}
	public int getPetLvl() {
		return PetLvl;
	}
	public int getIsBind() {
		return IsBind;
	}
	public int getIsBatt() {
		return IsBatt;
	}
	public int getPetSco() {
		return PetSco;
	}
	public int getBloo() {
		return Bloo;
	}
	public int getMagi() {
		return Magi;
	}
	public int getIsTrea() {
		return IsTrea;
	}
	public long getItKey() {
		return ItKey;
	}
	public int getPhyCh() {
		return PhyCh;
	}
	public int getMagiCh() {
		return MagiCh;
	}
	public int getStro() {
		return Stro;
	}
	public int getEndu() {
		return Endu;
	}
	public int getFlexCh() {
		return FlexCh;
	}
	public ArrayList<Integer> getQA() {
		return QA;
	}
	public ArrayList<PetSkill> getSkilSta() {
		return SkilSta;
	}
	
	
}
