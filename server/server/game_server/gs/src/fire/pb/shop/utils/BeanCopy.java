package fire.pb.shop.utils;

import fire.pb.shop.srv.market.pojo.EquipDaoBean;
import fire.pb.shop.srv.market.pojo.NormalDaoBean;
import fire.pb.shop.srv.market.pojo.PetDaoBean;
import xbean.EquipItem;
import xbean.NormalItem;
import xbean.PetItem;

/**
 * 类与类之间的数据复制
 * 
 * @author liangyanpeng
 *
 */
public class BeanCopy {
	/////////////////////////////////////////////////////////////////////
	// DAO Bean cpoy to MKDB Bean
	public static NormalItem normalDaoCopyMkdbBean(NormalDaoBean orig) {
		NormalItem dest = new xbean.__.NormalItem();
		dest.setId(orig.getId());
		dest.setFirstno(orig.getFirstno());
		dest.setTwono(orig.getTwono());
		dest.setThreeno(orig.getThreeno());
		dest.setNumber(orig.getNumber());
		dest.setUniquid(orig.getUniquid());
		dest.setKey(orig.getKey());
		dest.setRoleid(orig.getRoleid());
		dest.setItemid(orig.getItemid());
		dest.setName(orig.getName());
		dest.setShowtime(orig.getShowtime());
		dest.setExpiretime(orig.getExpiretime());
		dest.setExtid(orig.getExtid());
		dest.setLevel(orig.getLevel());
		dest.setPrice(orig.getPrice());
		dest.setAttentionnumber(orig.getAttentionnumber());
		return dest;
	}
	
	public static EquipItem equipDaoCopyMkdbBean(EquipDaoBean orig) {
		EquipItem dest = new xbean.__.EquipItem();
		dest.setId(orig.getId());
		dest.setFirstno(orig.getFirstno());
		dest.setTwono(orig.getTwono());
		dest.setThreeno(orig.getThreeno());
		dest.setUniquid(orig.getUniquid());
		dest.setKey(orig.getKey());
		dest.setRoleid(orig.getRoleid());
		dest.setItemid(orig.getItemid());
		dest.setName(orig.getName());
		dest.setShowtime(orig.getShowtime());
		dest.setExpiretime(orig.getExpiretime());
		dest.setExtid(orig.getExtid());
		dest.setLevel(orig.getLevel());
		dest.setEffect(orig.getEffect());
		dest.setSkill(orig.getSkill());
		dest.setColor(orig.getColor());
		dest.getAttr().putAll(orig.getAttr());
		dest.getAddattr().putAll(orig.getAddattr());
		dest.setTotalattr(orig.getTotalattr());
		dest.setEquipscore(orig.getEquipscore());
		dest.setNumber(orig.getNumber());
		dest.setPrice(orig.getPrice());
		dest.setAttentionnumber(orig.getAttentionnumber());
		return dest;
	}
	
	public static PetItem petDaoCopyMkdbBean(PetDaoBean orig) {
		PetItem dest = new xbean.__.PetItem();
		dest.setId(orig.getId());
		dest.setFirstno(orig.getFirstno());
		dest.setTwono(orig.getTwono());
		dest.setThreeno(orig.getThreeno());
		dest.setRoleid(orig.getRoleid());
		dest.setShowtime(orig.getShowtime());
		dest.setExpiretime(orig.getExpiretime());
		dest.setUniquid(orig.getUniquid());
		dest.setItemid(orig.getItemid());
		dest.setKey(orig.getKey());
		dest.setName(orig.getName());
		dest.setLevel(orig.getLevel());
		dest.setAttack(orig.getAttack());
		dest.setDefend(orig.getDefend());
		dest.setSpeed(orig.getSpeed());
		dest.setMagicattack(orig.getMagicattack());
		dest.setMagicdef(orig.getMagicdef());
		dest.setMaxhp(orig.getMaxhp());
		dest.setAttackapt(orig.getAttackapt());
		dest.setDefendapt(orig.getDefendapt());
		dest.setPhyforceapt(orig.getPhyforceapt());
		dest.setMagicapt(orig.getMagicapt());
		dest.setSpeedapt(orig.getSpeedapt());
		dest.setDodgeapt(orig.getDodgeapt());
		dest.setGrowrate(orig.getGrowrate());
		dest.getSkills().addAll(orig.getSkills());
		dest.setSkillnumber(orig.getSkillNumber());
		dest.setPetscore(orig.getPetscore());
		dest.setNumber(orig.getNumber());
		dest.setPrice(orig.getPrice());
		dest.setAttentionnumber(orig.getAttentionnumber());
		return dest;
	}
	
	//////////////////////////////////////////////////////////////////////
	// MKDB Bean cpoy to DAO Bean
	public static NormalDaoBean normalMkdbCopyDaoBean(NormalItem orig) {
		NormalDaoBean dest = new NormalDaoBean();
		dest.setId(orig.getId());
		dest.setFirstno(orig.getFirstno());
		dest.setTwono(orig.getTwono());
		dest.setThreeno(orig.getThreeno());
		dest.setNumber(orig.getNumber());
		dest.setUniquid(orig.getUniquid());
		dest.setKey(orig.getKey());
		dest.setRoleid(orig.getRoleid());
		dest.setItemid(orig.getItemid());
		dest.setName(orig.getName());
		dest.setShowtime(orig.getShowtime());
		dest.setExpiretime(orig.getExpiretime());
		dest.setExtid(orig.getExtid());
		dest.setLevel(orig.getLevel());
		dest.setPrice(orig.getPrice());
		dest.setAttentionnumber(orig.getAttentionnumber());
		return dest;
	}
	
	public static EquipDaoBean equipMkdbCopyDaoBean(EquipItem orig) {
		EquipDaoBean dest = new EquipDaoBean();
		dest.setId(orig.getId());
		dest.setFirstno(orig.getFirstno());
		dest.setTwono(orig.getTwono());
		dest.setThreeno(orig.getThreeno());
		dest.setUniquid(orig.getUniquid());
		dest.setKey(orig.getKey());
		dest.setRoleid(orig.getRoleid());
		dest.setItemid(orig.getItemid());
		dest.setName(orig.getName());
		dest.setShowtime(orig.getShowtime());
		dest.setExpiretime(orig.getExpiretime());
		dest.setExtid(orig.getExtid());
		dest.setLevel(orig.getLevel());
		dest.setEffect(orig.getEffect());
		dest.setSkill(orig.getSkill());
		dest.setColor(orig.getColor());
		dest.setTotalattr(orig.getTotalattr());
		dest.setEquipscore(orig.getEquipscore());
		dest.setNumber(orig.getNumber());
		dest.setPrice(orig.getPrice());
		dest.setAttentionnumber(orig.getAttentionnumber());
		return dest;
	}
	
	public static PetDaoBean petMkdbCopyDaoBean(PetItem orig) {
		PetDaoBean dest = new PetDaoBean();
		dest.setId(orig.getId());
		dest.setFirstno(orig.getFirstno());
		dest.setTwono(orig.getTwono());
		dest.setThreeno(orig.getThreeno());
		dest.setRoleid(orig.getRoleid());
		dest.setShowtime(orig.getShowtime());
		dest.setExpiretime(orig.getExpiretime());
		dest.setUniquid(orig.getUniquid());
		dest.setItemid(orig.getItemid());
		dest.setKey(orig.getKey());
		dest.setName(orig.getName());
		dest.setLevel(orig.getLevel());
		dest.setAttack(orig.getAttack());
		dest.setDefend(orig.getDefend());
		dest.setSpeed(orig.getSpeed());
		dest.setMagicattack(orig.getMagicattack());
		dest.setMagicdef(orig.getMagicdef());
		dest.setMaxhp(orig.getMaxhp());
		dest.setAttackapt(orig.getAttackapt());
		dest.setDefendapt(orig.getDefendapt());
		dest.setPhyforceapt(orig.getPhyforceapt());
		dest.setMagicapt(orig.getMagicapt());
		dest.setSpeedapt(orig.getSpeedapt());
		dest.setDodgeapt(orig.getDodgeapt());
		dest.setGrowrate(orig.getGrowrate());
		dest.getSkills().addAll(orig.getSkills());
		dest.setSkillNumber(orig.getSkillnumber());
		dest.setPetscore(orig.getPetscore());
		dest.setNumber(orig.getNumber());
		dest.setPrice(orig.getPrice());
		dest.setAttentionnumber(orig.getAttentionnumber());
		return dest;
	}
	

}
