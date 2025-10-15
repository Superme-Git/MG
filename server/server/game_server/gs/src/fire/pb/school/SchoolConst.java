package fire.pb.school;

import java.util.Properties;

import fire.pb.main.ConfigManager;
import fire.pb.util.FireProp;

public class SchoolConst {
	static Properties prop = ConfigManager.getInstance().getPropConf("school");
   public static final int WARRIOR=FireProp.getIntValue(prop, "mtgserv.warrior.id");
   public static final int PRIEST=FireProp.getIntValue(prop, "mtgserv.priest.id");
   public static final int PALADIN=FireProp.getIntValue(prop, "mtgserv.paladin.id");
   public static final int HUNTER=FireProp.getIntValue(prop, "mtgserv.hunter.id");
   public static final int WARLOCK=FireProp.getIntValue(prop, "mtgserv.warlock.id");
   public static final int SAMAN=FireProp.getIntValue(prop, "mtgserv.saman.id");
   public static final int MAGIC=FireProp.getIntValue(prop, "mtgserv.magic.id");
   public static final int ROGUE=FireProp.getIntValue(prop, "mtgserv.rogue.id");
   public static final int DRUID=FireProp.getIntValue(prop, "mtgserv.druid.id");
   
   public static final int WARRIOR_SHOUXI=FireProp.getIntValue(prop, "mtgserv.shouxi.warrior.id");
   public static final int WARRIOR_SHOUXI_TITLE=FireProp.getIntValue(prop, "mtgserv.shouxi.warrior.tltleid");
   
   public static final int PRIEST_SHOUXI=FireProp.getIntValue(prop, "mtgserv.shouxi.priest.id");
   public static final int PRIEST_SHOUXI_TITLE=FireProp.getIntValue(prop, "mtgserv.shouxi.priest.tltleid");
   public static final int PALADIN_SHOUXI=FireProp.getIntValue(prop, "mtgserv.shouxi.paladin.id");
   public static final int PALADIN_SHOUXI_TITLE=FireProp.getIntValue(prop, "mtgserv.shouxi.paladin.tltleid");
   public static final int HUNTER_SHOUXI=FireProp.getIntValue(prop, "mtgserv.shouxi.hunter.id");
   public static final int HUNTER_SHOUXI_TITLE=FireProp.getIntValue(prop, "mtgserv.shouxi.hunter.titleid");
   public static final int WARLOCK_SHOUXI=FireProp.getIntValue(prop, "mtgserv.shouxi.warlock.id");
   public static final int WARLOCK_SHOUXI_TITLE=FireProp.getIntValue(prop, "mtgserv.shouxi.warlock.titleid");
   public static final int SAMAN_SHOUXI=FireProp.getIntValue(prop, "mtgserv.shouxi.saman.id");
   public static final int SAMAN_SHOUXI_TITLE=FireProp.getIntValue(prop, "mtgserv.shouxi.saman.titleid");
   public static final int MAGIC_SHOUXI=FireProp.getIntValue(prop, "mtgserv.shouxi.magic.id");
   public static final int MAGIC_SHOUXI_TITLE=FireProp.getIntValue(prop, "mtgserv.shouxi.magic.titleid");
   public static final int ROGUE_SHOUXI=FireProp.getIntValue(prop, "mtgserv.shouxi.rogue.id");
   public static final int ROGUE_SHOUXI_TITLE=FireProp.getIntValue(prop, "mtgserv.shouxi.rogue.titleid");
   public static final int DRUID_SHOUXI=FireProp.getIntValue(prop, "mtgserv.shouxi.druid.id");
   public static final int DRUID_SHOUXI_TITLE=FireProp.getIntValue(prop, "mtgserv.shouxi.druid.titleid");
   
   public static final int TOUPIAO_MINLEVEL=FireProp.getIntValue(prop, "mtgserv.shouxi.toupiaoMinLevel");
   public static final int TOUPIAO_MINPHFORCE=FireProp.getIntValue(prop, "mtgserv.shouxi.toupiaoMinTili");
   public static final int TOUPIAO_SMONEY=FireProp.getIntValue(prop, "mtgserv.shouxi.toupiaoMinSMoney");
   public static final int CHALLENGE_MINLEVEL=FireProp.getIntValue(prop, "mtgserv.shouxi.challenge.MinLevel");
   public static final int CHALLENGE_MINMENGGONG=FireProp.getIntValue(prop, "mtgserv.shouxi.challenge.menggong");
   public static final int CHALLENGE_MINMONEY=FireProp.getIntValue(prop, "mtgserv.shouxi.challenge.money");
   public static final int DEFAULT_SHOUXI_ID =FireProp.getIntValue(prop, "mtgserv.shouxi.defaultBattleNpcID");
   
   public static final int WARRIOR_SHOUXI_BATTLE_NPC=30980;
   public static final int PALADIN_SHOUXI_BATTLE_NPC=30987;
   public static final int HUNTER_SHOUXI_BATTLE_NPC=30985;
   public static final int DRUID_SHOUXI_BATTLE_NPC=30990;
   public static final int MAGIC_SHOUXI_BATTLE_NPC=30982;
   public static final int PRIEST_SHOUXI_BATTLE_NPC=30981;
   public static final int SAMAN_SHOUXI_BATTLE_NPC=30983;
   public static final int ROGUE_SHOUXI_BATTLE_NPC=30995;
   public static final int WARLOCK_SHOUXI_BATTLE_NPC=30999;
   
   public static final int BATTLE_PET_NPC_1=25417;
   public static final int BATTLE_PET_NPC_2=25418;
   public static final int BATTLE_PET_NPC_3=25419;
   public static final int BATTLE_PET_NPC_4=25420;
   
   
   public static final int MSG_VOTE_SUCC =FireProp.getIntValue(prop, "mtgserv.shouxi.voteSucc");
   public static final int BECOME_SHOUXI =FireProp.getIntValue(prop, "mtgserv.shouxi.becomeShouxi");
   public static final int NOT_BE_SHOUXI =FireProp.getIntValue(prop, "mtgserv.shouxi.notShouxi");
   public static final int REFRESH_SUCC =FireProp.getIntValue(prop, "mtgserv.shouxi.refreshSucc");
   
   public static final int BE_SHOUXI_SUCC = 141605;
   public static final int MALE_SHOUXI_ONLINE = 141606;
   public static final int FEMALE_SHOUXI_ONLINE = 141607;
   public static final int MALE_SHOUXI_OFFLINE = 141608;
   public static final int FEMALE_SHOUXI_OFFLINE = 141609;
   public static final int CHALLENGE_SHOUXI_SUCC = 141782;
}
