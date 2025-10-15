package xtable;


public class _DatabaseMetaData_ extends mkdb.util.DatabaseMetaData {
	@Override
	public boolean isVerifyMkdb() {
		return true;
	}
	public void DatabaseMetaData1(){
		// xbeans
		{
			Bean bean = new Bean("Item", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "flags"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "position"
				, "int", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "number"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "numbermap"
				, "map", "int", "int", ""
				, "", "", "8"
				);
			super.addVariableFor(bean
				, "timeout"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "extid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "uniqueid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "loseeffecttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "markettime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "typeid"
				, "int", "", "", ""
				, "-1", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Bag", false, false);
			super.addVariableFor(bean
				, "currency"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "capacity"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "nextid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "items"
				, "map", "int", "Item", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "locked"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BagTimeLock", false, false);
			super.addVariableFor(bean
				, "itemkey"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "timeout"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("DiscardItem", false, false);
			super.addVariableFor(bean
				, "item"
				, "Item", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "deletedate"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("DiscardPet", false, false);
			super.addVariableFor(bean
				, "pet"
				, "PetInfo", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "deletedate"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "reason"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("UniquePet", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Itemrecoverlist", false, false);
			super.addVariableFor(bean
				, "uniqids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Petrecoverlist", false, false);
			super.addVariableFor(bean
				, "uniqids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("User", false, false);
			super.addVariableFor(bean
				, "prevloginroleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "idlist"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "isfirst"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("UserDeviceInfo", false, false);
			super.addVariableFor(bean
				, "telcooper"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "plattype"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "deviid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "imei"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "ip"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "netenvir"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "osver"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "brtype"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstdeviid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "username"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AUUserInfo", false, false);
			super.addVariableFor(bean
				, "retcode"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "func"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "funcparm"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "loginip"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "blisgm"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "auth"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "algorithm"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "nickname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "username"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "mac"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "os"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "platform"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "platname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clientsource"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "idfa"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("YingYongBao", false, false);
			super.addVariableFor(bean
				, "openid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "openkey"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "paytoken"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "pf"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "pfkey"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "zoneid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "platformname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("YybFushiNum", false, false);
			super.addVariableFor(bean
				, "balance"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "genbalance"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "saveamt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fushiall"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("YybFushiNums", false, false);
			super.addVariableFor(bean
				, "rolefushi"
				, "map", "long", "YybFushiNum", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("YybOrder", false, false);
			super.addVariableFor(bean
				, "userid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "status"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "platname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "localsaveamt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "localbalance"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "localgenbalance"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "retrytimes"
				, "int", "", "", ""
				, "10", "", ""
				);
			super.addVariableFor(bean
				, "consumetype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TitleInfo", false, false);
			super.addVariableFor(bean
				, "titleid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "titlename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "availtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BasicFightProperties", false, false);
			super.addVariableFor(bean
				, "str"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "iq"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "cons"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "endu"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "agi"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleAddPointProperties", false, false);
			super.addVariableFor(bean
				, "cons_save"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "iq_save"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "str_save"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "endu_save"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "agi_save"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FormBean", false, false);
			super.addVariableFor(bean
				, "activetimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "exp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleColorType", false, false);
			super.addVariableFor(bean
				, "colorpos1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "colorpos2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Properties", false, false);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "usedname"
				, "list", "", "string", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shape"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sex"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "exp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "race"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "color"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "title"
				, "int", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "hp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "uplimithp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "wound"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "mp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sp"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "bfp"
				, "BasicFightProperties", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "addpointfp"
				, "RoleAddPointProperties", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "point"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "scheme"
				, "int", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "schemechanges"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "schemechangetimes"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allexp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sceneid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posy"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posz"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "laststaticsceneid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "laststaticposx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "laststaticposy"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "laststaticposz"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fightpetkey"
				, "int", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "showpetkey"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sysconfigmap"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "titles"
				, "map", "int", "TitleInfo", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "defultform"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "formationsmap"
				, "map", "int", "FormBean", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clankey"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "oldclankey"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "exitstate"
				, "int", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "onlinetime"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "offlinetime"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bindtel"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "logintimesofday"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "isgetbindtelaward"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "bindteltime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "isbindtelagain"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "bindtelagainnum"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "unbindteltime"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "password"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "forcedelpdtime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "forcedelendtime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "errortimes"
				, "int", "", "", ""
				, "5", "", ""
				);
			super.addVariableFor(bean
				, "lockendtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "isopensafelock"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "sumonlinetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "userid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "addranklist"
				, "boolean", "", "", ""
				, "true", "", ""
				);
			super.addVariableFor(bean
				, "deletetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastloginip"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "platformuid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "viplevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "energy"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "playtimetimes"
				, "int", "", "", ""
				, "10", "", ""
				);
			super.addVariableFor(bean
				, "rolezonghemaxscore"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "skillscore"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "petscore"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "manypetscore"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "levelscore"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "xiulianscroe"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "rolescore"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "equipscore"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "historymaxprofessioncontribute"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "ride"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "bestpetid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolecolor1"
				, "int", "", "", ""
				, "101", "", ""
				);
			super.addVariableFor(bean
				, "rolecolor2"
				, "int", "", "", ""
				, "201", "", ""
				);
			super.addVariableFor(bean
				, "colorroomlist"
				, "list", "", "RoleColorType", ""
				, "", "", "15"
				);
			super.addVariableFor(bean
				, "cruise"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "cruise2"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "cruise3"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "fighteai"
				, "list", "", "BattleAI", ""
				, "", "", "16"
				);
			super.addVariableFor(bean
				, "equipeffect"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "pilot"
				, "int", "", "", ""
				, "2", "", ""
				);
			super.addVariableFor(bean
				, "tupotips"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tupofinish"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "yingfuxishu"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "depotnameinfo"
				, "map", "int", "string", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "expiretime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstprompt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastchargetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lineconfigmap"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ChangeSchoolRecord", false, false);
			super.addVariableFor(bean
				, "shape"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ChangeSchoolInfo", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "ChangeSchoolRecord", ""
				, "", "", "1"
				);
			super.addVariableFor(bean
				, "changeweaponcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "changegemcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("subscription", false, false);
			super.addVariableFor(bean
				, "expiretime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "subscribetime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CrossTempRole", false, false);
			super.addVariableFor(bean
				, "zoneid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastcopytime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "copyresult"
				, "short", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "finishedtable"
				, "map", "string", "boolean", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "crosstatus"
				, "boolean", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "flag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("crossMessageBean", false, false);
			super.addVariableFor(bean
				, "parms"
				, "list", "", "string", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "messageid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("helpcount", false, false);
			super.addVariableFor(bean
				, "expvalue"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shengwangvalue"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanvalue"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanhishelpnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "helpgiveitemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "helpitemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleUpdateTime", false, false);
			super.addVariableFor(bean
				, "dateupdatetime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "weekupdatetime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleUpdateModelNum", false, false);
			super.addVariableFor(bean
				, "tradingbuyfushinum"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("NpcFollowInfo", false, false);
			super.addVariableFor(bean
				, "npcid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "questid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "validdate"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TransfromInfo", false, false);
			super.addVariableFor(bean
				, "transformid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "questid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "validdate"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "ride"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ChatTime", false, false);
			super.addVariableFor(bean
				, "lastworldchattime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastcurrentchattime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastprofessionchattime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastclanchattime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastteamapplychattime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("OfflineFuture", true, false);
			super.addVariableFor(bean
				, "timefuture"
				, "java.util.concurrent.ScheduledFuture<?>", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("OnlinerefreshFuture", true, false);
			super.addVariableFor(bean
				, "furvec"
				, "java.util.concurrent.ScheduledFuture<?>", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PetInfo", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "key"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "uniqid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "initbfp"
				, "BasicFightProperties", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bfp"
				, "BasicFightProperties", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "hp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "mp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "growrate"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "life"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "exp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bornattackapt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "borndefendapt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bornphyforceapt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bornmagicapt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bornspeedapt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "borndodgeapt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "kind"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "effects"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "finalattrs"
				, "map", "int", "float", ""
				, "", "", "50"
				);
			super.addVariableFor(bean
				, "buffpet"
				, "BuffAgent", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "islocked"
				, "boolean", "", "", ""
				, "false", "", ""
				);
			super.addVariableFor(bean
				, "isbinded"
				, "boolean", "", "", ""
				, "false", "", ""
				);
			super.addVariableFor(bean
				, "timeout"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "petscore"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "petbasescore"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "ownerid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "skills"
				, "list", "", "PetSkill", ""
				, "", "", "13"
				);
			super.addVariableFor(bean
				, "point"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "autoaddcons"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "autoaddiq"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "autoaddstr"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "autoaddendu"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "autoaddagi"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "pointresetcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "aptaddcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "growrateaddcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "washcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "treasure"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "petdye1"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "petdye2"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "shenshouinccount"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "marketfreezeexpire"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PetSkill", false, false);
			super.addVariableFor(bean
				, "skillid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "skilltype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "certification"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Pets", false, false);
			super.addVariableFor(bean
				, "capacity"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "nextid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "petmap"
				, "map", "int", "PetInfo", ""
				, "", "", "8"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PetSellCount", false, false);
			super.addVariableFor(bean
				, "count"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "resettime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MultiExpInfo", false, false);
			super.addVariableFor(bean
				, "multiexptime"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "remainnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dupdannum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastbacktime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "refreshtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Decision", false, false);
			super.addVariableFor(bean
				, "operatetype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "operator"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "aim"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "operateid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "isauto"
				, "boolean", "", "", ""
				, "false", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BattleFlags", false, false);
			super.addVariableFor(bean
				, "friendflag"
				, "list", "", "string", ""
				, "", "", "10"
				);
			super.addVariableFor(bean
				, "enemyflag"
				, "list", "", "string", ""
				, "", "", "10"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Fighter", false, false);
			super.addVariableFor(bean
				, "uniqueid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fightername"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "subtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fightertype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battleindex"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fighterkey"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "ishost"
				, "boolean", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "round"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "positionx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "positiony"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inilevel"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "inihp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inimp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inisp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inipetkey"
				, "int", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "footlogoid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shapeid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "actioncount"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fightedpets"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "initattrs"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "initskills"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "useditems"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Protectors", false, false);
			super.addVariableFor(bean
				, "protectorlist"
				, "list", "", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BattleResult", false, false);
			super.addVariableFor(bean
				, "battleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "configid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "environment"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "phase"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battletype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "areatype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "areaconf"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "hostform"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "guestform"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monstertype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleids"
				, "map", "long", "int", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "fighters"
				, "treemap", "int", "Fighter", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "skipeddeadfighters"
				, "set", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monsters"
				, "map", "int", "Monster", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "failedmonsters"
				, "list", "", "FailedMonster", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inifighters"
				, "treemap", "int", "Fighter", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "audience"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "watchers"
				, "treemap", "int", "Fighter", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monsterid"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "round"
				, "int", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "roundtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "scriptplaytime"
				, "long", "", "", ""
				, "1000", "", ""
				);
			super.addVariableFor(bean
				, "decisions"
				, "map", "int", "Decision", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "feedbacks"
				, "set", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battleresult"
				, "int", "", "", ""
				, "2", "", ""
				);
			super.addVariableFor(bean
				, "roundresult"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "protectormap"
				, "map", "int", "Protectors", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battledatas"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoundResultItems", true, false);
			super.addVariableFor(bean
				, "resultitems"
				, "list", "", "fire.pb.battle.NewResultItem", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "aiactions"
				, "list", "", "fire.pb.battle.AIOperation", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fighterfinallyhps"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fighterfinallymps"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BattleInfo", true, false);
			super.addVariableFor(bean
				, "battleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "configid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "background"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "bgm"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "environment"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "gmactiontimetimes"
				, "int", "", "", ""
				, "10", "", ""
				);
			super.addVariableFor(bean
				, "envchangefrom"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "phase"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "round"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "canrunaway"
				, "boolean", "", "", ""
				, "true", "", ""
				);
			super.addVariableFor(bean
				, "autokey"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "battletype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "areatype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "areaconf"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "babyflag"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "babybagfull"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "hostroleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monsterid"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "monstertype"
				, "int", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "battleendhandlers"
				, "vector", "", "fire.pb.battle.BattleEndHandler", ""
				, "", "", "2"
				);
			super.addVariableFor(bean
				, "hostform"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "guestform"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "hostformlevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "guestformlevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "hostformeffcet"
				, "float", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "figterinfoonbegin"
				, "list", "", "fire.pb.battle.FighterInfo", ""
				, "", "", "30"
				);
			super.addVariableFor(bean
				, "roleids"
				, "map", "long", "int", ""
				, "", "", "10"
				);
			super.addVariableFor(bean
				, "fighters"
				, "treemap", "int", "Fighter", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "fighterobjects"
				, "map", "int", "fire.pb.battle.Fighter", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "skipeddeadfighters"
				, "set", "", "int", ""
				, "", "", "10"
				);
			super.addVariableFor(bean
				, "monsters"
				, "map", "int", "Monster", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "failedmonsters"
				, "list", "", "FailedMonster", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "inifighters"
				, "treemap", "int", "Fighter", ""
				, "", "", "10"
				);
			super.addVariableFor(bean
				, "audience"
				, "map", "long", "int", ""
				, "", "", "30"
				);
			super.addVariableFor(bean
				, "watchers"
				, "treemap", "int", "Fighter", ""
				, "", "", "30"
				);
			super.addVariableFor(bean
				, "offlineroleids"
				, "list", "", "long", ""
				, "", "", "10"
				);
			super.addVariableFor(bean
				, "battlebegintime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roundtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "scriptplaytime"
				, "long", "", "", ""
				, "1000", "", ""
				);
			super.addVariableFor(bean
				, "decisions"
				, "map", "int", "Decision", ""
				, "", "", "20"
				);
			super.addVariableFor(bean
				, "feedbacks"
				, "set", "", "long", ""
				, "", "", "20"
				);
			super.addVariableFor(bean
				, "battleresult"
				, "int", "", "", ""
				, "2", "", ""
				);
			super.addVariableFor(bean
				, "roundresult"
				, "map", "int", "int", ""
				, "", "", "30"
				);
			super.addVariableFor(bean
				, "protectormap"
				, "map", "int", "Protectors", ""
				, "", "", "10"
				);
			super.addVariableFor(bean
				, "timeout"
				, "java.util.concurrent.ScheduledFuture<?>", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battleais"
				, "list", "", "BattleAI", ""
				, "", "", "30"
				);
			super.addVariableFor(bean
				, "engine"
				, "fire.script.FightJSEngine", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fighterchangedattrs"
				, "map", "int", "fire.pb.battle.ActionChangedAttrs", ""
				, "", "", "30"
				);
			super.addVariableFor(bean
				, "battledatas"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fieldfighter"
				, "fire.pb.battle.Fighter", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "endprocedures"
				, "list", "", "BattleEndProcedure", ""
				, "", "", "10"
				);
			super.addVariableFor(bean
				, "roundresultitem"
				, "map", "int", "RoundResultItems", ""
				, "", "", "160"
				);
			super.addVariableFor(bean
				, "itemtimelist"
				, "list", "", "int", ""
				, "", "", "40"
				);
			super.addVariableFor(bean
				, "reliveacts"
				, "vector", "", "fire.pb.battle.operate.Operate", ""
				, "", "", "5"
				);
			super.addVariableFor(bean
				, "battledeads"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battlemaxround"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "params"
				, "map", "int", "int", ""
				, "", "", "50"
				);
			super.addVariableFor(bean
				, "quitteamroleids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "absentteamroleids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "teamidlist"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "livedieteam"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BattleEndProcedure", true, false);
			super.addVariableFor(bean
				, "fighterid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "endprocedure"
				, "mkdb.Procedure", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CameraInfo", false, false);
			super.addVariableFor(bean
				, "endtime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "sizebeforezip"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "sizeafterzip"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "camerafileurl"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Monster", false, false);
			super.addVariableFor(bean
				, "monsterid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shape"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "bodysize"
				, "float", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "hp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "uplimithp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "mp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bfp"
				, "BasicFightProperties", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "effects"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "finalattrs"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "buffagent"
				, "BuffAgent", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "ais"
				, "list", "", "BattleAI", ""
				, "", "", "30"
				);
			super.addVariableFor(bean
				, "fightnpctype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monstertype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "specialtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "skills"
				, "list", "", "MonsterSkill", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "immunebuffs"
				, "list", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "extradata"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FailedMonster", false, false);
			super.addVariableFor(bean
				, "fighterid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "ishost"
				, "boolean", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monsterid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shape"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fightnpctype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monstertype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "result"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MonsterSkill", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "skilllevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "castrate"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BattleAI", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "count"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "enableround"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TeamInfo", false, false);
			super.addVariableFor(bean
				, "teamleaderid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "commanderroleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "minlevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "maxlevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "formation"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "formationlevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "switchleaderid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "switchleadertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "successswitchtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "members"
				, "list", "", "TeamMember", ""
				, "", "", "4"
				);
			super.addVariableFor(bean
				, "applierids"
				, "map", "long", "long", ""
				, "", "", "15"
				);
			super.addVariableFor(bean
				, "invitingids"
				, "map", "long", "long", ""
				, "", "", "4"
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "smapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "hugs"
				, "map", "long", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "targetid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "onekeytimestamp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TeamMember", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SingleInvitings", false, false);
			super.addVariableFor(bean
				, "invitingids"
				, "map", "long", "long", ""
				, "", "", "4"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InviteInfo", false, false);
			super.addVariableFor(bean
				, "beinginvited"
				, "boolean", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inviting"
				, "TeamInvite", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "invited"
				, "list", "", "TeamInvite", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TeamInvite", false, false);
			super.addVariableFor(bean
				, "teamid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "invitetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TeamFilter", true, false);
			super.addVariableFor(bean
				, "filter"
				, "fire.pb.team.TeamFilter", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BJTeamInfo", false, false);
			super.addVariableFor(bean
				, "serviceid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bjdata"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ETeamMatch", false, false);
			super.addVariableFor(bean
				, "roleid2matchdata"
				, "map", "long", "TeamMatch", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "teamid2matchdata"
				, "map", "long", "TeamMatch", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "teammatchdatalist"
				, "list", "", "TeamMatch", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolematchdatalist"
				, "list", "", "TeamMatch", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TeamMatch", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "matchtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "targetid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "levelmin"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "levelmax"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "onekeytimestamp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "timestamp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ETeamMelon", false, false);
			super.addVariableFor(bean
				, "melonid2melons"
				, "map", "long", "TeamMelon", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "melontype"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "dataid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dataid2"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "melonerlist"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "watchmelonerlist"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RollItem", false, false);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "countertype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "xiangguanid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "awardid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TeamMelon", false, false);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemdata"
				, "Item", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "awardid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "melonroleids"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "opmelonroleids"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "maxrollpoint"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "opnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battleresult"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ERole", false, false);
			super.addVariableFor(bean
				, "finalattrs"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "effects"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EPet", false, false);
			super.addVariableFor(bean
				, "effects"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "finalattrs"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RecoveryAttr", false, false);
			super.addVariableFor(bean
				, "quality"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EnhancementAttr", false, false);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "attrmap"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BuffRole", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "buffagent"
				, "BuffAgent", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BuffAgent", false, false);
			super.addVariableFor(bean
				, "buffs"
				, "map", "int", "Buff", ""
				, "", "", "64"
				);
			super.addVariableFor(bean
				, "battleendclear"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "timerfutures"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("NotifyList", false, false);
			super.addVariableFor(bean
				, "notifytypeid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "buffids"
				, "list", "", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Buff", false, false);
			super.addVariableFor(bean
				, "index"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "impacttime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "remaintime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "round"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "value"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "effects"
				, "map", "int", "float", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fighterkey"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Effect", false, false);
			super.addVariableFor(bean
				, "effecttypeid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "value"
				, "float", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "enable"
				, "boolean", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("StoredBuffRole", false, false);
			super.addVariableFor(bean
				, "buffs"
				, "map", "int", "Buff", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TimerFuture", true, false);
			super.addVariableFor(bean
				, "future"
				, "java.util.concurrent.ScheduledFuture<?>", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("DebugFlag", false, false);
			super.addVariableFor(bean
				, "debugs"
				, "list", "", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AssistSkill", false, false);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "exp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SpecialSkill", false, false);
			super.addVariableFor(bean
				, "skillid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "equiptype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SkillRole", false, false);
			super.addVariableFor(bean
				, "skillbases"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inborns"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastdegradetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "assistskills"
				, "map", "int", "AssistSkill", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "practiceskill"
				, "map", "int", "PracticeSkill", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "liveskill"
				, "map", "int", "LiveSkill", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "specialskills"
				, "list", "", "SpecialSkill", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "extskills"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "skillsequence"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastusedtime"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FirstFullSkillbase", false, false);
			super.addVariableFor(bean
				, "roleids"
				, "map", "long", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LiveSkill", false, false);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ELiveSkill", false, false);
			super.addVariableFor(bean
				, "liveskilllist"
				, "map", "int", "LiveSkill", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ParticleSkill", false, false);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "exp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PracticeSkill", false, false);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "exp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EParticleSkill", false, false);
			super.addVariableFor(bean
				, "particleskill"
				, "map", "int", "ParticleSkill", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ArchMap", false, false);
			super.addVariableFor(bean
				, "mapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posy"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ImpExamRecord", false, false);
			super.addVariableFor(bean
				, "maxvillexamright"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "currexamaccexp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "currexamaccmoney"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "accrightnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "currexamaccright"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "delwrongval"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chorightval"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "maxcontinueright"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rightquesmap"
				, "map", "int", "int", ""
				, "", "", "4"
				);
			super.addVariableFor(bean
				, "examsendquestime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "examconsumetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "minstateexamtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "maxstateexamright"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "stateexamtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastanswertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "helpcnt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "step"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "flag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "quesidlist"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastquesid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastanswer"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastisright"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "worngqueslist"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "begintime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ImpExamStatePassRole", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "accrightnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "stateexamtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ImpExamStateRecord", false, false);
			super.addVariableFor(bean
				, "idslist"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "passidslist"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "passrolelist"
				, "vector", "", "ImpExamStatePassRole", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("WinnerRoleRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "awardflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("YbNum", false, false);
			super.addVariableFor(bean
				, "num"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sysnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "nopresentnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "snlist"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bindorsysnumtoday"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bindorsysnumtodaytime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fushiall"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("YbNums", false, false);
			super.addVariableFor(bean
				, "roleyb"
				, "map", "long", "YbNum", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BiaoQing", false, false);
			super.addVariableFor(bean
				, "biaoqinginfos"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FirstCharge", false, false);
			super.addVariableFor(bean
				, "firstchargetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstchargeclearpresenttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chargestatus"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Vipinfo", false, false);
			super.addVariableFor(bean
				, "vipexp"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "viplevel"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "bonus"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "gotbonus"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("DayPay", false, false);
			super.addVariableFor(bean
				, "userid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "expiretime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstprompt"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EDayPay", false, false);
			super.addVariableFor(bean
				, "roleid2daypay"
				, "map", "long", "DayPay", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EMonthCard", false, false);
			super.addVariableFor(bean
				, "endtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "grabtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstprompttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("WheelItem", false, false);
			super.addVariableFor(bean
				, "itemtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "times"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bind"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "limit"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "msgid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("WheelInfo", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "npcbaseid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "serviceid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "finish"
				, "boolean", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "basemoney"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "basesmoney"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "baseexp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "msgid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "msgparas"
				, "vector", "", "string", ""
				, "", "", "10"
				);
			super.addVariableFor(bean
				, "index"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "wheelitems"
				, "vector", "", "WheelItem", ""
				, "", "", "50"
				);
			super.addBean(bean);
		}
	}
	public void DatabaseMetaData2(){
		{
			Bean bean = new Bean("WheelInfos", false, false);
			super.addVariableFor(bean
				, "wheellist"
				, "vector", "", "WheelInfo", ""
				, "", "", "20"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("WheelItemLimit", false, false);
			super.addVariableFor(bean
				, "limitmap"
				, "map", "int", "int", ""
				, "", "", "20"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EnhancementData", false, false);
			super.addVariableFor(bean
				, "enhancementattr"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "enhancementtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Equip", false, false);
			super.addVariableFor(bean
				, "equiplevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "repairtimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "endure"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "curmaxendure"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "producer"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "extendure"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "maxendure"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "skill"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "effect"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "treasure"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "attr"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "addattr"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "enhancement"
				, "map", "int", "EnhancementData", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "equipscore"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "diamonds"
				, "list", "", "int", ""
				, "", "", "32"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FriendBean", false, false);
			super.addVariableFor(bean
				, "friendlydegrees"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FriendlyDegreesLimit", false, false);
			super.addVariableFor(bean
				, "todayfriendlydegreesmap"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "resettime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FriendGroups", false, false);
			super.addVariableFor(bean
				, "refusestrangermsg"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "friendmap"
				, "map", "long", "FriendBean", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "friendlydegreeslimit"
				, "FriendlyDegreesLimit", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sign"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "offlinemsglist"
				, "vector", "", "OfflineMsg", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("OfflineMsg", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "content"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "details"
				, "list", "", "binary", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "showinfos"
				, "list", "", "ShowInfoBean", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sendtime"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ShowInfoBean", false, false);
			super.addVariableFor(bean
				, "showtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shopid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "counterid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "thisid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FriendGiveNum", false, false);
			super.addVariableFor(bean
				, "givenummap"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "resettime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleSpace", false, false);
			super.addVariableFor(bean
				, "gift"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "popularity"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "recvgift"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "getgiftnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "getgifttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RecruitAward", false, false);
			super.addVariableFor(bean
				, "getnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "gettime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FestivalGift", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "onlinetotal"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("OfflineMsgList", false, false);
			super.addVariableFor(bean
				, "protocollist"
				, "vector", "", "OfflineMsgProtocol", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("OfflineMsgProtocol", false, false);
			super.addVariableFor(bean
				, "protype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "content"
				, "binary", "", "", ""
				, "", "", "128"
				);
			super.addVariableFor(bean
				, "proclassname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tick"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BroadcastMsgList", false, false);
			super.addVariableFor(bean
				, "msglist"
				, "vector", "", "BroadcastMsg", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BroadcastMsg", false, false);
			super.addVariableFor(bean
				, "msgprotocol"
				, "OfflineMsgProtocol", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "broadtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "broadendtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanIndex", false, false);
			super.addVariableFor(bean
				, "index"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanMemberInfo", false, false);
			super.addVariableFor(bean
				, "clanposition"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "isbannedtalk"
				, "boolean", "", "", ""
				, "false", "", ""
				);
			super.addVariableFor(bean
				, "bannedtalktime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "salutation"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "jointime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "ownbonus"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "bonusgettime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tasknum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "weekclanpoint"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "preweekclanpoint"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "clanhelpnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "claninstnum"
				, "short", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "claninstnumtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanfightnum"
				, "short", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanfightnumtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanSkill", false, false);
			super.addVariableFor(bean
				, "clanskillid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanskilllevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanskillexp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("DynamicMapInfo2", false, false);
			super.addVariableFor(bean
				, "basemapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanInfo", false, false);
			super.addVariableFor(bean
				, "index"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "key"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "oldclanname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "creator"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "popularity"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanlevel"
				, "int", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "clanlevelmax"
				, "int", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "clanaim"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanmaster"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanvicemaster"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "clanrank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanskillmap"
				, "map", "int", "ClanSkill", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "members"
				, "map", "long", "ClanMemberInfo", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "applicants"
				, "map", "long", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "money"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "house"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bonus"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "autostate"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "totalscore"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firemembernum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "claninstservice"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "claneventrecordlist"
				, "vector", "", "ClanEventRecord", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "requestlevel"
				, "short", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "impeachment"
				, "ImpeachMent", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clandkp"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "lastclandkp"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ImpeachMent", false, false);
			super.addVariableFor(bean
				, "impeachroleid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "impeachtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "impeachallnum"
				, "short", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "acceptimpeachroleids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleClanPoint", false, false);
			super.addVariableFor(bean
				, "freezedclanpoint"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "currentclanpoint"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "historyclanpoint"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "weekclanpoint"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "preweekclanpoint"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "oldhistoryclanpoint"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "oldfreezedclanpoint"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanFight", false, false);
			super.addVariableFor(bean
				, "clan1"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanindex1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname1"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan2"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanindex2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname2"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanlevel1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanlevel2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan1scroe"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan2scroe"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "winner"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanFightBattleField", false, false);
			super.addVariableFor(bean
				, "battlefieldid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanfightid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanid1"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanid2"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname1"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname2"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanscore1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanscore2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan1roleids"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan2roleids"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "enterroleids"
				, "map", "long", "string", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan1enterroleids"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan2enterroleids"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan1roleranklist"
				, "list", "", "BattleFieldScroeRank", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan2roleranklist"
				, "list", "", "BattleFieldScroeRank", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan1rolerankmap"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan2rolerankmap"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastclan1rolesortstamp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastclan2rolesortstamp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "victoryboxids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "celebrateboxids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "datetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "activitystarttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "activitypreparetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "activitynoentertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "activityendtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "whichwin"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanFights", false, false);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clan1vschan2"
				, "map", "long", "ClanFight", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EClanfightids", false, false);
			super.addVariableFor(bean
				, "ids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EClanFightStatistics", false, false);
			super.addVariableFor(bean
				, "act"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "entertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "winneritemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clancelebrateitemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "winnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastcbattlefieldrankliststamp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastlosestamp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BattleFieldScroeRank", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "scroe"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanEventRecord", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "eventtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "content"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "objectroleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MedicItem", false, false);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MedicItemList", false, false);
			super.addVariableFor(bean
				, "medicitemrefreshtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "selecttype"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "medicitems"
				, "map", "int", "MedicItem", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanWeekNum", false, false);
			super.addVariableFor(bean
				, "weeknum"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BuyMedicItemNum", false, false);
			super.addVariableFor(bean
				, "buynum"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "impeachdaynum"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RuneInfo", false, false);
			super.addVariableFor(bean
				, "dayrequestnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allgivenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allacceptnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemlevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RequestRuneInfo", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "targetroleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "actiontype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "requesttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemlevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RequestRuneInfoList", false, false);
			super.addVariableFor(bean
				, "requestrunelists"
				, "vector", "", "RequestRuneInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleApplyClan", false, false);
			super.addVariableFor(bean
				, "clankey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "applytiem"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleApplyClanList", false, false);
			super.addVariableFor(bean
				, "onekeylasttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "onekeyapplymap"
				, "map", "long", "RoleApplyClan", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "applymap"
				, "map", "long", "RoleApplyClan", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "ClanRankRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanRankRecord", false, false);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clankey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "membernum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "hotel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanZongHeRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "ClanZongHeRankRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanZongHeRankRecord", false, false);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clankey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "zonghe"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleZongheRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "RoleZongheRankRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleZongheRankRecord", false, false);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleProfessionRankRecord", false, false);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleProfessionRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "RoleProfessionRankRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanProgressRankRecord", false, false);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clankey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "progress"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanmastername"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "copyid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "copyname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bosshp"
				, "float", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanProgressRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "ClanProgressRankRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleRankRecord", false, false);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "RoleRankRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleLevelRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "RoleLevelListRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleLevelListRecord", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "marshaldata"
				, "MarshalRoleLevelRecord", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarshalRoleLevelRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ApprenticelListRecord", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "marshaldata"
				, "MarshalApprenticeRecord", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarshalApprenticeRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "nickname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "schoolid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "apprenticenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RedPackRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "RedPackRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RedPackRecord", false, false);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "marshaldata"
				, "MarshalRedPackRecord", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarshalRedPackRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RedPack", false, false);
			super.addVariableFor(bean
				, "worldredpack"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("GiveFlowerRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "GiveFlowerRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("GiveFlowerRecord", false, false);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "marshaldata"
				, "MarshalGiveFlowerRecord", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarshalGiveFlowerRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ReceFlowerRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "ReceFlowerRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ReceFlowerRecord", false, false);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "marshaldata"
				, "MarshalReceFlowerRecord", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarshalReceFlowerRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("WeekGiveReceGift", false, false);
			super.addVariableFor(bean
				, "weekdata"
				, "map", "long", "GiveReceGift", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("GiveReceGift", false, false);
			super.addVariableFor(bean
				, "givegift"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "recegift"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarshalClanFightRaceRankRecord", false, false);
			super.addVariableFor(bean
				, "clanid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanlevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fightcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "wincount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "scroe"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanFightRaceRankRecord", false, false);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "marshaldata"
				, "MarshalClanFightRaceRankRecord", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanFightRaceRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "ClanFightRaceRankRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarshalClanFightHistroyRankRecord", false, false);
			super.addVariableFor(bean
				, "clanid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "clanlevel"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fightcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "wincount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "totalscroe"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanFightHistroyRankRecord", false, false);
			super.addVariableFor(bean
				, "triggertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "marshaldata"
				, "MarshalClanFightHistroyRankRecord", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanFightHistroyRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "ClanFightHistroyRankRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PetScoreRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "PetScoreListRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PetScoreListRecord", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "marshaldata"
				, "MarshalPetScoreRecord", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarshalPetScoreRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "uniquepetid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "nickname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "petname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "petgrade"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "colour"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP3ScoreRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP3ScoreRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "PvP3ScoreRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP5ScoreRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP5ScoreRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "PvP5ScoreRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BingFengRank", false, false);
			super.addVariableFor(bean
				, "ranks"
				, "map", "int", "BingFengRankList", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BingFengRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "BingFengListRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BingFengListRecord", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "marshaldata"
				, "MarshalBingFengRecord", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarshalBingFengRecord", false, false);
			super.addVariableFor(bean
				, "shool"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rank"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "stage"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rounds"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "times"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ProgressBarInfo", false, false);
			super.addVariableFor(bean
				, "bartype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemkey"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inprogress"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "starttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TreasureMapInfo", false, false);
			super.addVariableFor(bean
				, "awardid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tableindex"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemkey"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TraderWheel", false, false);
			super.addVariableFor(bean
				, "boxtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemindex"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SchoolWheel", false, false);
			super.addVariableFor(bean
				, "awardid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "itemindex"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ShowingMission", false, false);
			super.addVariableFor(bean
				, "missionid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "isleader"
				, "boolean", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CommitedMissions", false, false);
			super.addVariableFor(bean
				, "committed"
				, "list", "", "int", ""
				, "", "", "2048"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CommitedScenarioMission", false, false);
			super.addVariableFor(bean
				, "committed"
				, "list", "", "int", ""
				, "", "", "128"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Mission", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "status"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "path"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "taskvalue"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "deadline"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "round"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AcceptedScenarioMission", false, false);
			super.addVariableFor(bean
				, "accepted"
				, "map", "int", "Mission", ""
				, "", "", "128"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Track", false, false);
			super.addVariableFor(bean
				, "date"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TrackedMission", false, false);
			super.addVariableFor(bean
				, "questids"
				, "map", "int", "Track", ""
				, "", "", "32"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AnYeTask", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "kind"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstitemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstitemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstnpckey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstnpcid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "param1"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "param2"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "param3"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "legend"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "legendtime"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "legendend"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "legenditem"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AnYeData", false, false);
			super.addVariableFor(bean
				, "jointime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "randomtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "refreshtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "times"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "todaynum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "today"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "renxins"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "reqhelptimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "helptimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "legendtask"
				, "int", "", "", ""
				, "999", "", ""
				);
			super.addVariableFor(bean
				, "anyetasks"
				, "map", "int", "AnYeTask", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AnYeLegendFuture", true, false);
			super.addVariableFor(bean
				, "legendfuture"
				, "java.util.concurrent.ScheduledFuture<?>", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
	}
	public void DatabaseMetaData3(){
		{
			Bean bean = new Bean("CircleTaskInfo", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "questid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "queststate"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "round"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sumnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "smallaward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bigaward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "questtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstmapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "givetasknpcid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstnpckey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstnpcid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstitemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstitemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dsty"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastgiveuptime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "takequesttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "acceptquesttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sametimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battlenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firsttasktime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "doneparam"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "npcname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "questclass"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "questgroup"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "autocomp"
				, "int", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "submitnpc"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "openawardnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "catchitsingletime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "catchitdoubletime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "subitemtableid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CircleTaskMap", false, false);
			super.addVariableFor(bean
				, "taskmap"
				, "map", "int", "CircleTaskInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RenXingData", false, false);
			super.addVariableFor(bean
				, "renxinmap"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CircleTaskCompleteTimes", false, false);
			super.addVariableFor(bean
				, "circletaskcounts"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "logtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SpecailquestFuture", true, false);
			super.addVariableFor(bean
				, "questfuture"
				, "map", "int", "java.util.concurrent.ScheduledFuture<?>", ""
				, "", "", "80"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CreateNpcExecType", true, false);
			super.addVariableFor(bean
				, "execinstance"
				, "fire.msp.GMCreateNpcExec", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MazeInfo", false, false);
			super.addVariableFor(bean
				, "mapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "pos"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("VecList", false, false);
			super.addVariableFor(bean
				, "list"
				, "vector", "", "MazeInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ProfessionLeaderInfo", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "refreshtimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastrefreshtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monsterbean"
				, "vector", "", "Monster", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shapecomponent"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ProfessionLeaderTicketInfo", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tickets"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "words"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ProfessionLeaderVoteInfo", false, false);
			super.addVariableFor(bean
				, "votetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "challengetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ProfessionLeaderCand", false, false);
			super.addVariableFor(bean
				, "candidatelist"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TaskDlgInfo", false, false);
			super.addVariableFor(bean
				, "taskid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "taskstate"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "round"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sumnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstmapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstnpckey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "endtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstnpcid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstitemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstitemnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dstx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dsty"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TaskInfos", false, false);
			super.addVariableFor(bean
				, "tasksmap"
				, "map", "int", "TaskDlgInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("NpcInfo", false, false);
			super.addVariableFor(bean
				, "isinbattle"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ApprentceChieve", false, false);
			super.addVariableFor(bean
				, "currnumber"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "total"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "flag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "content"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CurrApprent", false, false);
			super.addVariableFor(bean
				, "titleid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "apprentinfo"
				, "HasApprent", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "achievement"
				, "map", "int", "ApprentceChieve", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "mastercomment"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "apprentcomment"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "baishitime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("HasApprent", false, false);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "camp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shap"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MenstorInfo", false, false);
			super.addVariableFor(bean
				, "currapprentices"
				, "list", "", "CurrApprent", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "apprentices"
				, "list", "", "HasApprent", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "updatetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MenstorAndApprent", false, false);
			super.addVariableFor(bean
				, "flag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "menstortitleid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstbaishitime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstapprentid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shidezhi"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "menstorid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bashitime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bashiflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "eqipmentscore"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "achievement"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "baishitime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastgivefushitime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fushi"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ShiDeZhiRankList", false, false);
			super.addVariableFor(bean
				, "records"
				, "list", "", "MasterRankRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MasterRankRecord", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shidezhi"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RolePos", false, false);
			super.addVariableFor(bean
				, "mapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posy"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "ownerid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dynamicmap"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dynamicposx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dynamicposy"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "status"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "hastask"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RewardData", false, false);
			super.addVariableFor(bean
				, "rewardid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastrewardtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "timewait"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceNpcInfo", false, false);
			super.addVariableFor(bean
				, "npckey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "npcid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "mapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posy"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceInfo", false, false);
			super.addVariableFor(bean
				, "ownerid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleids"
				, "set", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "npcs"
				, "vector", "", "InstanceNpcInfo", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "asistnpcs"
				, "vector", "", "InstanceNpcInfo", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "copyflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "instanceid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "currentstep"
				, "int", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "currentstate"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "currenttaskid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "serviceid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "currentmapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "accepttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "finalcounts"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "subsaves"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceFutureInfo", true, false);
			super.addVariableFor(bean
				, "timeoutfuture"
				, "java.util.concurrent.ScheduledFuture<?>", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceTaskInfo", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "instanceonlyid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "instanceflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "counts"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "defaulttimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "currentstep"
				, "int", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "currenttaskid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "accepttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "totalexp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "stepawardtimes"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "awarding"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceTimeAwardInfo", false, false);
			super.addVariableFor(bean
				, "stepawardtimes"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceInfoCol", false, false);
			super.addVariableFor(bean
				, "lastinstanceid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "counts"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "accepttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "instinfo"
				, "map", "int", "InstanceTaskInfo", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "instcount"
				, "map", "int", "InstanceTimeAwardInfo", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fanpai"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "reset"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceAskInfo", false, false);
			super.addVariableFor(bean
				, "answerinfo"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "instid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "asktime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ActiveUserInfo", false, false);
			super.addVariableFor(bean
				, "firsttimeenter"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeenter"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveday1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveday2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveweek1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveweek2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firsttimeactiveday"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firsttimeactiveweek"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimechargeday1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimechargeday2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimechargeweek1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimechargeweek2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firsttimechargeday"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firsttimechargeweek"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimechargeday"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimechargedaytotalamount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimechargeweek"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimechargeweektotalamount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chargetotalamount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chargetotalrmb"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chargetotalamountlong"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chargetotalrmblong"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chargetotalrmbuntillastday"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chargetotalrmbuntillastdaylong"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "convertchargetotalamouttolong"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fushiconsumetotalamount"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "cashfushiconsumetotalamount"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimefushiconsumeday"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimefushiconsumedaytotalamount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimefushiconsumeweek"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimefushiconsumeweektotalamount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fushibuytotalamount"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fushiselltotalamount"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "cashfushi"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bindfushi"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sysfushi"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fushiinplatform"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ActiveRoleInfo", false, false);
			super.addVariableFor(bean
				, "createtime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "enterworldtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dayonline"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dayonlinetime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "weekonline"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "weekonlinetime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveday1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveday1onlinetime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveday2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveweek1"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveweek1onlinetime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttimeactiveweek2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firsttimeactiveday"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firsttimeactiveweek"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dealmoneyinplatform"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tmpmoneyinplatform"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "moneyincofc"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LianyunAwardInfo", false, false);
			super.addVariableFor(bean
				, "awards"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BeginnerTip", false, false);
			super.addVariableFor(bean
				, "tips"
				, "map", "int", "int", ""
				, "", "", "1024"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Gather", false, false);
			super.addVariableFor(bean
				, "gatherkey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "result"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "endgathertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("GetRolesCallBackInst", true, false);
			super.addVariableFor(bean
				, "callback"
				, "fire.msp.IGetRolesCallBack", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("forbidinfo", false, false);
			super.addVariableFor(bean
				, "forbidtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "reason"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleQuitStatistics", false, false);
			super.addVariableFor(bean
				, "money"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "exp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "currency"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastcountdate"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastrewardidx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rewarddate"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Course", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "count"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("lichengxinxi", false, false);
			super.addVariableFor(bean
				, "licheng"
				, "map", "int", "Course", ""
				, "", "", "1024"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("DayFinishTask", false, false);
			super.addVariableFor(bean
				, "tasklist"
				, "list", "", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ActivityCalendarStatus", false, false);
			super.addVariableFor(bean
				, "week"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "daystatus"
				, "map", "int", "DayFinishTask", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PlayActiveData", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "count"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "count2"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "activeness"
				, "float", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("NewPlayActiveDay", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "activeness"
				, "float", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chests"
				, "map", "int", "int", ""
				, "", "", "32"
				);
			super.addVariableFor(bean
				, "actives"
				, "map", "int", "PlayActiveData", ""
				, "", "", "1024"
				);
			super.addVariableFor(bean
				, "activescount"
				, "map", "int", "int", ""
				, "", "", "1024"
				);
			super.addVariableFor(bean
				, "yingfutime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "yingfuexp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("NewPlayActiveWeek", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "actives"
				, "map", "int", "PlayActiveData", ""
				, "", "", "1024"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("UserPunish", false, false);
			super.addVariableFor(bean
				, "userid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "records"
				, "list", "", "PunishRecord", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "releasetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "waiguatimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sendmsgtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PunishRecord", false, false);
			super.addVariableFor(bean
				, "userid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "type"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "forbidtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "gmuserid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "optime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "reason"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("GacdPicStatus", false, false);
			super.addVariableFor(bean
				, "pictime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "answer"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "result"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "questiontime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CardInfo", false, false);
			super.addVariableFor(bean
				, "userid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "usecardtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "answer"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "parenttype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "type"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LimitItemInfo", false, false);
			super.addVariableFor(bean
				, "maxnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ActivityItemLimit", false, false);
			super.addVariableFor(bean
				, "limititemmap"
				, "map", "int", "LimitItemInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CompensationInfo", false, false);
			super.addVariableFor(bean
				, "receiveawardtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "readflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("Compensation", false, false);
			super.addVariableFor(bean
				, "compensationmap"
				, "map", "int", "CompensationInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SingleCompensationCond", false, false);
			super.addVariableFor(bean
				, "condid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "condparams"
				, "list", "", "string", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SingleCompensationAward", false, false);
			super.addVariableFor(bean
				, "type"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "id"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "flag"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SingleCompensation", false, false);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "validtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "title"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "content"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "awards"
				, "list", "", "SingleCompensationAward", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "givetype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleidsofget"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleidsofexcept"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "conditions"
				, "list", "", "SingleCompensationCond", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "opid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sign"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("CompensationRole", false, false);
			super.addVariableFor(bean
				, "singlecompensations"
				, "map", "long", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ActivityStatus", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "baseid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inistarttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "iniendtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "laststarttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastendtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "costtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InvestQuestionnaire", false, false);
			super.addVariableFor(bean
				, "answer"
				, "map", "int", "int", ""
				, "", "", "128"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LevelSeal", false, false);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceZone", false, false);
			super.addVariableFor(bean
				, "instzoneid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "ownerid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "zonestate"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "extid"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addVariableFor(bean
				, "roleids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "saves"
				, "map", "int", "InstanceSave", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "npcs"
				, "map", "int", "InstanceNpc", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "services"
				, "map", "int", "InstanceNpcServ", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "timers"
				, "map", "int", "InstanceTimer", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "starttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceNpcSave", false, false);
			super.addVariableFor(bean
				, "npckey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "finishcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "count"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceSave", false, false);
			super.addVariableFor(bean
				, "saveid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "subsaves"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "npcsaves"
				, "map", "long", "InstanceNpcSave", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceNpc", false, false);
			super.addVariableFor(bean
				, "npcbaseid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceNpcServ", false, false);
			super.addVariableFor(bean
				, "serviceid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "actid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceTimer", false, false);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "starttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "endtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "futurekey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceActivity", false, false);
			super.addVariableFor(bean
				, "instanceid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "baseid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "starttime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "endtime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InstanceTurnGroup", false, false);
			super.addVariableFor(bean
				, "turngroup"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "turnindex"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "turntype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanSaveInfo", false, false);
			super.addVariableFor(bean
				, "savestate"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "refreshtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanBossInfo", false, false);
			super.addVariableFor(bean
				, "bossmonsterid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "maxhp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "hp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fighteindx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "winroleids"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bossnpckey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanInstances", false, false);
			super.addVariableFor(bean
				, "clanid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "currentzonekey"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "instkey"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inststate"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "inststep"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "saveinfo"
				, "map", "int", "ClanSaveInfo", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "instreset"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bossinfo"
				, "map", "int", "ClanBossInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ClanInstBestLevel", false, false);
			super.addVariableFor(bean
				, "instsave"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleBFInfo", false, false);
			super.addVariableFor(bean
				, "instzoneid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "losetimes"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "receivelastaward"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "lastrank"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "stage"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "lastenterstage"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "stagetime"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "stageround"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "yesterstage"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "lastchangetime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "stagefanpai"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "autogo"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BingFengRole", false, false);
			super.addVariableFor(bean
				, "instzoneid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "times"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "chargetimes"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "infos"
				, "map", "int", "RoleBFInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BingFengWangZuo", false, false);
			super.addVariableFor(bean
				, "deathtimes"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SchoolStageTime", false, false);
			super.addVariableFor(bean
				, "stagetime"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "stageround"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "stagebest"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BingFengInfo", false, false);
			super.addVariableFor(bean
				, "instzoneid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "stagetime"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "stageround"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "stagebest"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "schoolstage"
				, "map", "int", "SchoolStageTime", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ItemRecycleIndex", false, false);
			super.addVariableFor(bean
				, "dayrecycle"
				, "set", "", "long", ""
				, "", "", "8192"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TriggerRole", false, false);
			super.addVariableFor(bean
				, "triggeredids"
				, "list", "", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RandomNameInfo", false, false);
			super.addVariableFor(bean
				, "randomtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EventInfo", true, false);
			super.addVariableFor(bean
				, "event"
				, "fire.pb.WorldEventTab.CrontabTask", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("OnetimeEvent", true, false);
			super.addVariableFor(bean
				, "onetimeevent"
				, "fire.pb.WorldEventTab.OnetimeTask", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TaskEventInfo", false, false);
			super.addVariableFor(bean
				, "eventindexs"
				, "map", "int", "long", ""
				, "", "", "64"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PlatformInfo", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "discount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ServerRoles", false, false);
			super.addVariableFor(bean
				, "rolenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP1Role", false, false);
			super.addVariableFor(bean
				, "grade"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battlenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "winnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "continuewinnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstwinaward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fivebattleaward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastbattletime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastentertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP1RaceRole", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battlenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "winnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP1QueueRole", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "enterqueuetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP1Race", false, false);
			super.addVariableFor(bean
				, "allroles"
				, "list", "", "PvP1RaceRole", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allrolesid"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "waitingqueue"
				, "list", "", "PvP1QueueRole", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP3Role", false, false);
			super.addVariableFor(bean
				, "grade"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battlenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "winnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "continuewinnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstwinaward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fivewinaward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tenbattleaward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastbattletime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastentertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP3RaceRole", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP3QueueRole", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "enterqueuetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP3Race", false, false);
			super.addVariableFor(bean
				, "allroles"
				, "list", "", "PvP3RaceRole", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allrolesid"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "waitingqueue"
				, "list", "", "PvP3QueueRole", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP5Role", false, false);
			super.addVariableFor(bean
				, "grade"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "camp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "excellent"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battlenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "winnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "continuewinnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "firstwinaward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fivebattleaward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastbattletime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastentertime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP5RaceRole", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "score"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battlenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "winnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP5QueueRole", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "enterqueuetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PvP5Race", false, false);
			super.addVariableFor(bean
				, "allrolescampa"
				, "list", "", "PvP5RaceRole", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allrolescampb"
				, "list", "", "PvP5RaceRole", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allrolesidcampa"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allrolesidcampb"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "waitingqueue"
				, "list", "", "PvP5QueueRole", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "isend"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ModifyNameRole", false, false);
			super.addVariableFor(bean
				, "lastbuytime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastmodtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "buycount"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "modcount"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ChargeOrder", false, false);
			super.addVariableFor(bean
				, "status"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "platformsn"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "plattype"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "platformuid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "receipt"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "transcationid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "goodid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "goodnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "userid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "retrytimes"
				, "int", "", "", ""
				, "10", "", ""
				);
			super.addBean(bean);
		}
	}
	public void DatabaseMetaData4(){
		{
			Bean bean = new Bean("ChargeHistory", false, false);
			super.addVariableFor(bean
				, "charges"
				, "map", "long", "ChargeOrder", ""
				, "", "", "40"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AppstoretidStatus", false, false);
			super.addVariableFor(bean
				, "chargesn"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "status"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ChargeRecord", false, false);
			super.addVariableFor(bean
				, "chargecount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "chargetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleChargeLimit", false, false);
			super.addVariableFor(bean
				, "chargeslimit"
				, "map", "int", "ChargeRecord", ""
				, "", "", "40"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RollCardInfo", false, false);
			super.addVariableFor(bean
				, "serviceid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "takeflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "basemoney"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "basesmoney"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "baseexp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "index"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "wheelitems"
				, "vector", "", "WheelItem", ""
				, "", "", "4"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("WelfareBean", false, false);
			super.addVariableFor(bean
				, "buytimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastbuytime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleFutureNotifyMap", true, false);
			super.addVariableFor(bean
				, "notifymap"
				, "map", "int", "java.util.concurrent.ScheduledFuture<?>", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RolesNotifyMap", true, false);
			super.addVariableFor(bean
				, "rolesfuturemap"
				, "map", "long", "RoleFutureNotifyMap", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("InvitePeopleInfo", false, false);
			super.addVariableFor(bean
				, "inviteme"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "aminvites"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "awardhistory"
				, "list", "", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("OffLineHook", false, false);
			super.addVariableFor(bean
				, "hooktime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "remainfivebeitime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fivebeitimestart"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fivebeitotalusetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "flag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TransfromByItemData", false, false);
			super.addVariableFor(bean
				, "useitemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "transformid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "validdate"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TicketItemExInfo", false, false);
			super.addVariableFor(bean
				, "ticketcode"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleLockInfo", false, false);
			super.addVariableFor(bean
				, "password"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "unlocktime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "forceunlocktime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "errortimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fullerrortime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("totalNumber", false, false);
			super.addVariableFor(bean
				, "totalnumber"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MulDayLoginGift", false, false);
			super.addVariableFor(bean
				, "logindays"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "logintime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rewardmap"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FestivalData", false, false);
			super.addVariableFor(bean
				, "rewardmap"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RefreshMonsterNpcInfo", false, false);
			super.addVariableFor(bean
				, "npcinfos"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("timerNpcInfo", false, false);
			super.addVariableFor(bean
				, "npcid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "npcstatus"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battletime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battleendtime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("timerNpcInfoCol", false, false);
			super.addVariableFor(bean
				, "npcinfo"
				, "map", "long", "timerNpcInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("eventNpcInfo", false, false);
			super.addVariableFor(bean
				, "npcid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "npcstatus"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battletime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battleendtime"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("eventNpcInfoCol", false, false);
			super.addVariableFor(bean
				, "npcinfo"
				, "map", "long", "eventNpcInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleTimerNpcInfo", false, false);
			super.addVariableFor(bean
				, "awardtimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastawardtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleActiveTimerNpcInfo", false, false);
			super.addVariableFor(bean
				, "actinfo"
				, "map", "int", "RoleTimerNpcInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleEventNpcInfo", false, false);
			super.addVariableFor(bean
				, "awardtimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastawardtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleEventNpcInfoColumn", false, false);
			super.addVariableFor(bean
				, "eventinfo"
				, "map", "int", "RoleEventNpcInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("npcBattleInfoCol", false, false);
			super.addVariableFor(bean
				, "battleroles"
				, "map", "long", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("WeiBoChoice", false, false);
			super.addVariableFor(bean
				, "times"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "setting"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("WeiBoNotify", false, false);
			super.addVariableFor(bean
				, "weibo"
				, "map", "int", "WeiBoChoice", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "takeawardflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RolenpcjiangliInfo", false, false);
			super.addVariableFor(bean
				, "awardtimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastawardtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RolePingBiRolesInfo", false, false);
			super.addVariableFor(bean
				, "pingbiroles"
				, "map", "long", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleRankNotifyTimeInfo", false, false);
			super.addVariableFor(bean
				, "lasttime"
				, "map", "int", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FuShiRecord", false, false);
			super.addVariableFor(bean
				, "addtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "currentnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleFuShiRecord", false, false);
			super.addVariableFor(bean
				, "records"
				, "vector", "", "FuShiRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RegDay", false, false);
			super.addVariableFor(bean
				, "rewardflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "day"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "suppregflag"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RegMonth", false, false);
			super.addVariableFor(bean
				, "daymap"
				, "map", "int", "RegDay", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "suppregnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RegRec", false, false);
			super.addVariableFor(bean
				, "monthmap"
				, "map", "int", "RegMonth", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastregtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleDoubleChareInfo", false, false);
			super.addVariableFor(bean
				, "activetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "achievement"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "flag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ActivityQuestion", false, false);
			super.addVariableFor(bean
				, "activityquestionstarttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "questionids"
				, "list", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "curquestionindex"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "curquestionstep"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "answerrighttimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "totalanswernum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "grabreward"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "totalexp"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "totalmoney"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "helptimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ChargeReturnProfit", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "value"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "maxvalue"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "status"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EChargeReturnProfit", false, false);
			super.addVariableFor(bean
				, "returnprofitmap"
				, "map", "int", "ChargeReturnProfit", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("npcaward", false, false);
			super.addVariableFor(bean
				, "lasttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "count"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "totalcount"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("npcawardrecords", false, false);
			super.addVariableFor(bean
				, "records"
				, "map", "int", "npcaward", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("HuoBanInfo", false, false);
			super.addVariableFor(bean
				, "id"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "color"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "infight"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "type"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "times"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "settimes"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "weekindex"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("HuoBanColumn", false, false);
			super.addVariableFor(bean
				, "fighthuobans"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "huobans"
				, "map", "int", "HuoBanInfo", ""
				, "", "", "15"
				);
			super.addVariableFor(bean
				, "viphuoban"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "week"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("HuoBanZhenrongInfo", false, false);
			super.addVariableFor(bean
				, "zhenfa"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "huoban"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("HuoBanZhenrong", false, false);
			super.addVariableFor(bean
				, "current"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "zhenrong"
				, "map", "int", "HuoBanZhenrongInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("HuoBanshuxing", false, false);
			super.addVariableFor(bean
				, "huobanname"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shape"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bodytype"
				, "float", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "effects"
				, "map", "int", "float", ""
				, "", "", "120"
				);
			super.addVariableFor(bean
				, "finalattrs"
				, "map", "int", "float", ""
				, "", "", "120"
				);
			super.addVariableFor(bean
				, "skills"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "skillmaster"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battleai"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "levelfactor"
				, "float", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "levelconstant"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("HuoBanShuxingLevel", false, false);
			super.addVariableFor(bean
				, "huobans"
				, "map", "int", "HuoBanshuxing", ""
				, "", "", "1024"
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("HuoBanVip", false, false);
			super.addVariableFor(bean
				, "huobans"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("NumberAndTime", false, false);
			super.addVariableFor(bean
				, "number"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleBuyGoodsLimit", false, false);
			super.addVariableFor(bean
				, "daylimit"
				, "map", "int", "NumberAndTime", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "weeklimit"
				, "map", "int", "NumberAndTime", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monthlimit"
				, "map", "int", "NumberAndTime", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleSaleGoodsLimit", false, false);
			super.addVariableFor(bean
				, "daylimit"
				, "map", "int", "NumberAndTime", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "weeklimit"
				, "map", "int", "NumberAndTime", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "monthlimit"
				, "map", "int", "NumberAndTime", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("GoodsBusiness", false, false);
			super.addVariableFor(bean
				, "buynum"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "salenum"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastnum"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "priorperiodprice"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "price"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "count"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("NpcSaleBusiness", false, false);
			super.addVariableFor(bean
				, "goodsmap"
				, "map", "int", "GoodsBusiness", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "isactive"
				, "short", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LogBean", false, false);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "number"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "money"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarketTradeLog", false, false);
			super.addVariableFor(bean
				, "buylog"
				, "vector", "", "LogBean", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "salelog"
				, "vector", "", "LogBean", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("MarketFloatingGoods", false, false);
			super.addVariableFor(bean
				, "floatingmin"
				, "float", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "floatingmax"
				, "float", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "floatingprice"
				, "float", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "basehangordernum"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "basesalenum"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "hangordernum"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "salenum"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "priorperiodprice"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "price"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "totalmoney"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AttentionGoodsBean", false, false);
			super.addVariableFor(bean
				, "id"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "showtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "expiretime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AttentionGoods", false, false);
			super.addVariableFor(bean
				, "buyattentions"
				, "vector", "", "AttentionGoodsBean", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "publicityattentions"
				, "vector", "", "AttentionGoodsBean", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AttentionRole", false, false);
			super.addVariableFor(bean
				, "roleids"
				, "set", "", "long", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("NormalItem", false, false);
			super.addVariableFor(bean
				, "id"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "firstno"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "twono"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "threeno"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "uniquid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "key"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "extid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "number"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "price"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "attentionnumber"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "showtime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "expiretime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("EquipItem", false, false);
			super.addVariableFor(bean
				, "id"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "firstno"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "twono"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "threeno"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "uniquid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "key"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "extid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "number"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "price"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "attentionnumber"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "showtime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "expiretime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "effect"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "skill"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "color"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "attr"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "addattr"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "totalattr"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "equipscore"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PetItem", false, false);
			super.addVariableFor(bean
				, "id"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "firstno"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "twono"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "threeno"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "uniquid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "key"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "extid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "name"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "number"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "price"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "attentionnumber"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "showtime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "expiretime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "attack"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "defend"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "speed"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "magicattack"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "magicdef"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "maxhp"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "attackapt"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "defendapt"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "phyforceapt"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "magicapt"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "speedapt"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "dodgeapt"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "growrate"
				, "float", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "skills"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "skillnumber"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "petscore"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("PresellItem", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "expiretime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("GoldOrder", false, false);
			super.addVariableFor(bean
				, "pid"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "number"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "price"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "publicity"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "locktime"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "0", "", ""
				);
			super.addVariableFor(bean
				, "time"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleBlackMarket", false, false);
			super.addVariableFor(bean
				, "goldordersale"
				, "map", "long", "GoldOrder", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "goldorderbuy"
				, "map", "long", "GoldOrder", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("HookData", false, false);
			super.addVariableFor(bean
				, "cangetdpoint"
				, "short", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "getdpoint"
				, "short", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "isautobattle"
				, "boolean", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "charoptype"
				, "short", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "charopid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "petoptype"
				, "short", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "petopid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "offlineexp"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastgettime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ItemUseCount", false, false);
			super.addVariableFor(bean
				, "itemid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "usetimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastusetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ItemUse", false, false);
			super.addVariableFor(bean
				, "iteminfo"
				, "map", "int", "ItemUseCount", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BaoXiangInfo", false, false);
			super.addVariableFor(bean
				, "opentimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastopentime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("GongHuiBaoXiangInfo", false, false);
			super.addVariableFor(bean
				, "opentimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastopentime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleBestowCount", false, false);
			super.addVariableFor(bean
				, "opentimes"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lastopentime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleBestowInfo", false, false);
			super.addVariableFor(bean
				, "rolebestowinfo"
				, "map", "long", "RoleBestowCount", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("BestowNpcInfo", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ServiceInfo", false, false);
			super.addVariableFor(bean
				, "year"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "weeks"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "activerecommend"
				, "int", "", "", ""
				, "1", "", ""
				);
			super.addVariableFor(bean
				, "updatetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "days"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("WeekIndex", false, false);
			super.addVariableFor(bean
				, "index"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "instanceindex"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LiveDieRoleInfo", false, false);
			super.addVariableFor(bean
				, "objectid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "invitationtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "selecttype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "acceptflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LDTeamRoleInfoDes", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shape"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LDRoleInfoDes", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rolename"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "shape"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "school"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "teamnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "teamnummax"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LDVideoRoleInfoDes", false, false);
			super.addVariableFor(bean
				, "role1"
				, "LDRoleInfoDes", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "role2"
				, "LDRoleInfoDes", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "teamlist1"
				, "vector", "", "LDTeamRoleInfoDes", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "teamlist2"
				, "vector", "", "LDTeamRoleInfoDes", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "battleresult"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rosenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "videoid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "fighttime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "beforevideosize"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "aftervideosize"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "videourl"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "saveresult"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LDVideoRoleInfoDesList", false, false);
			super.addVariableFor(bean
				, "ldvideoroleinfodeslistall"
				, "vector", "", "LDVideoRoleInfoDes", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LDVideoHisList", false, false);
			super.addVariableFor(bean
				, "ldvidehisinfo"
				, "vector", "", "string", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LDVideoRoleRoseInfo", false, false);
			super.addVariableFor(bean
				, "videoid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "rosenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LDVideoRoleRoseInfoList", false, false);
			super.addVariableFor(bean
				, "ldvideoroleroseinfolist"
				, "map", "string", "LDVideoRoleRoseInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("LiveDieWeekNum", false, false);
			super.addVariableFor(bean
				, "weeknum"
				, "long", "", "", ""
				, "-1", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RedPackRoleHisInfo", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackmoney"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "receivetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RedPackInfo", false, false);
			super.addVariableFor(bean
				, "redpackid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "modeltype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "valuekey"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sendroleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackdes"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackstate"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackallnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackreceivednum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackallmoney"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackreceiveallmoney"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sendtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "backflag"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackrolehisinfolist"
				, "vector", "", "RedPackRoleHisInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RedPackRoleInfoList", false, false);
			super.addVariableFor(bean
				, "redpackinfolist"
				, "map", "string", "RedPackInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RedPackBaseInfo", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sendtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RedPackInfoViewList", false, false);
			super.addVariableFor(bean
				, "redpackinfoviewlist"
				, "map", "string", "RedPackBaseInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SRRedPackNum", false, false);
			super.addVariableFor(bean
				, "modeltype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpacksendnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackreceivenum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpacksendfushinum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("SRRedPackNumList", false, false);
			super.addVariableFor(bean
				, "srredpacknumlist"
				, "map", "int", "SRRedPackNum", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleRedPackRecord", false, false);
			super.addVariableFor(bean
				, "sendnum"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sendgold"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sendfushi"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "receivenum"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "receivegold"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "receivefushi"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleReceiveRedPackRecord", false, false);
			super.addVariableFor(bean
				, "modeltype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "sendroleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "redpackid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "receivegold"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "receivefushi"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "receivetime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleReceiveRedPackRecordList", false, false);
			super.addVariableFor(bean
				, "rolereceiveredpacklist"
				, "map", "string", "RoleReceiveRedPackRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleTradingRecord", false, false);
			super.addVariableFor(bean
				, "tradingid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tradingtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "curnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "price"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tradingtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "state"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleTradingHisRecord", false, false);
			super.addVariableFor(bean
				, "tradingid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tradingtype"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "curnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "allnum"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "price"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "tradingtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleTradingHisRecordList", false, false);
			super.addVariableFor(bean
				, "roletradinghisrecordlist"
				, "vector", "", "RoleTradingHisRecord", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleTradingInfoList", false, false);
			super.addVariableFor(bean
				, "tradinglist"
				, "map", "string", "string", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TradingPrice", false, false);
			super.addVariableFor(bean
				, "tradingid"
				, "string", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "price"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "num"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "createtime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
	}
	public void DatabaseMetaData5(){
		{
			Bean bean = new Bean("TradingMap", false, false);
			super.addVariableFor(bean
				, "selllist"
				, "list", "", "TradingPrice", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "buylist"
				, "list", "", "TradingPrice", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("TaskRecourse", false, false);
			super.addVariableFor(bean
				, "channellist"
				, "vector", "", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleRecourse", false, false);
			super.addVariableFor(bean
				, "recoursetask"
				, "map", "int", "TaskRecourse", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("AccusationInfo", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "accusationedroleids"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lasttime"
				, "long", "", "", ""
				, "0", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("YaoQianShuInfo", false, false);
			super.addVariableFor(bean
				, "roleid"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "lookroleids"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "mapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posy"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleYaoQianShuInfo", false, false);
			super.addVariableFor(bean
				, "lookroleids"
				, "vector", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "mapid"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posx"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "posy"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("RoleYaoQianShuInfoMaps", false, false);
			super.addVariableFor(bean
				, "yaoqianshumaps"
				, "map", "long", "RoleYaoQianShuInfo", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("KickInfo", false, false);
			super.addVariableFor(bean
				, "kicktime"
				, "list", "", "long", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "expire"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("FunOpenClose", false, false);
			super.addVariableFor(bean
				, "funmap"
				, "map", "int", "int", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ServiceSet", false, false);
			super.addVariableFor(bean
				, "bindtelagain"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "bindtelagaintime"
				, "long", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		// cbeans
		{
			Bean bean = new Bean("SkillbaseLevel", false, true);
			super.addVariableFor(bean
				, "level"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		{
			Bean bean = new Bean("ItemRecycleDate", false, true);
			super.addVariableFor(bean
				, "year"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addVariableFor(bean
				, "dayofyear"
				, "int", "", "", ""
				, "", "", ""
				);
			super.addBean(bean);
		}
		// tables
		super.addTable("buffrolestodisk", "DB", "long", false, "StoredBuffRole", "key:properties", "");
		super.addTable("debugflag", "DB", "long", false, "DebugFlag", "key:properties", "");
		super.addTable("bag", "DB", "long", false, "Bag", "key:properties", "");
		super.addTable("yingyongbaoinfos", "MEMORY", "int", false, "YingYongBao", "", "");
		super.addTable("frole", "MEMORY", "long", false, "ERole", "", "");
		super.addTable("rolecircletaskcomplete", "DB", "long", false, "CircleTaskCompleteTimes", "key:properties", "");
		super.addTable("userpunish", "DB", "int", false, "UserPunish", "", "");
		super.addTable("roletradinginfolisttab", "DB", "long", false, "RoleTradingInfoList", "", "");
		super.addTable("bingfenginfos", "DB", "int", false, "BingFengInfo", "", "");
		super.addTable("totalregisternum", "DB", "int", false, "totalNumber", "", "");
		super.addTable("roleranknotifytime", "DB", "long", false, "RoleRankNotifyTimeInfo", "", "");
		super.addTable("gmcreatenpcuniversal", "MEMORY", "long", true, "CreateNpcExecType", "", "");
		super.addTable("roleid2battleid", "MEMORY", "long", false, "long", "", "");
		super.addTable("marketfloatingtab", "DB", "string", false, "MarketFloatingGoods", "", "");
		super.addTable("rolerecourse", "MEMORY", "long", false, "RoleRecourse", "key:properties", "");
		super.addTable("roleid2monthcard", "DB", "long", false, "EMonthCard", "", "");
		super.addTable("rolerankdatalist", "DB", "int", false, "RoleRankList", "", "");
		super.addTable("festival", "DB", "long", false, "FestivalGift", "key:properties", "");
		super.addTable("acceptedscenariomission", "DB", "long", false, "AcceptedScenarioMission", "", "");
		super.addTable("buffroles", "MEMORY", "long", false, "BuffRole", "", "");
		super.addTable("clanid2clanfightids", "DB", "long", false, "EClanfightids", "", "");
		super.addTable("quality", "DB", "long", true, "RecoveryAttr", "", "");
		super.addTable("pvp5roles", "DB", "long", false, "PvP5Role", "", "");
		super.addTable("clanfighthistroylist", "DB", "int", false, "ClanFightHistroyRankList", "", "");
		super.addTable("clanweeknumtab", "DB", "long", false, "ClanWeekNum", "", "");
		super.addTable("rolebuygoodslimits", "DB", "long", false, "RoleBuyGoodsLimit", "key:properties", "");
		super.addTable("roleupdatemodelnumtab", "DB", "long", false, "RoleUpdateModelNum", "key:properties", "");
		super.addTable("role2weibonotify", "DB", "long", false, "WeiBoNotify", "", "");
		super.addTable("npcsalebusinesstab", "DB", "long", false, "NpcSaleBusiness", "key:properties", "");
		super.addTable("rolekaigonghuibaoxiang", "DB", "long", false, "GongHuiBaoXiangInfo", "key:properties", "");
		super.addTable("petrecyclebin", "DB", "long", false, "DiscardPet", "", "");
		super.addTable("activitycalendar", "DB", "long", false, "ActivityCalendarStatus", "key:properties", "");
		super.addTable("roleupdatetimetab", "DB", "long", false, "RoleUpdateTime", "key:properties", "");
		super.addTable("weekindexs", "DB", "int", false, "WeekIndex", "", "");
		super.addTable("instanceactivitys", "DB", "int", false, "InstanceActivity", "", "");
		super.addTable("claninstbestlevel", "DB", "int", false, "ClanInstBestLevel", "", "");
		super.addTable("buymedicitemnum", "DB", "long", false, "BuyMedicItemNum", "", "");
		super.addTable("rolespecialquesttimer", "MEMORY", "long", false, "SpecailquestFuture", "", "");
		super.addTable("battleflag", "DB", "long", false, "BattleFlags", "key:properties", "");
		super.addTable("funopenclosetab", "DB", "int", false, "FunOpenClose", "", "");
		super.addTable("bingfenglists", "DB", "int", false, "BingFengRank", "", "");
		super.addTable("archmaps", "DB", "long", true, "ArchMap", "", "");
		super.addTable("professionleader", "DB", "int", false, "ProfessionLeaderInfo", "", "");
		super.addTable("pvp1roles", "DB", "long", false, "PvP1Role", "", "");
		super.addTable("clanindex", "DB", "int", false, "ClanIndex", "", "");
		super.addTable("getrolescallback", "MEMORY", "long", true, "GetRolesCallBackInst", "", "");
		super.addTable("team", "MEMORY", "long", true, "TeamInfo", "", "");
		super.addTable("roleschargelimit", "DB", "long", false, "RoleChargeLimit", "", "");
		super.addTable("roledoublecharge", "DB", "long", false, "RoleDoubleChareInfo", "", "");
		super.addTable("transformbyitem", "DB", "long", false, "TransfromByItemData", "", "");
		super.addTable("auuserinfo", "MEMORY", "int", false, "AUUserInfo", "", "");
		super.addTable("rolesalegoodslimits", "DB", "long", false, "RoleSaleGoodsLimit", "key:properties", "");
		super.addTable("battle", "MEMORY", "long", true, "BattleInfo", "", "");
		super.addTable("crosstemproles", "MEMORY", "long", false, "CrossTempRole", "", "");
		super.addTable("rolenpcjiangliinfos", "DB", "long", false, "RolenpcjiangliInfo", "", "");
		super.addTable("eventnpcinfotable", "MEMORY", "int", false, "eventNpcInfoCol", "", "");
		super.addTable("apprents", "DB", "long", false, "MenstorInfo", "", "");
		super.addTable("pvp5historyscorelist", "DB", "int", false, "PvP5ScoreRankList", "", "");
		super.addTable("roleshidelist", "DB", "int", false, "ShiDeZhiRankList", "", "");
		super.addTable("roleyaoqianshutables", "DB", "long", false, "RoleYaoQianShuInfoMaps", "", "");
		super.addTable("menstor", "DB", "long", false, "MenstorAndApprent", "", "");
		super.addTable("commitedscenariomission", "DB", "long", false, "CommitedScenarioMission", "", "");
		super.addTable("equiptab", "DB", "long", true, "EquipItem", "", "");
		super.addTable("roleofflinehook", "DB", "long", false, "OffLineHook", "", "");
		super.addTable("subscriptions", "DB", "long", false, "subscription", "key:properties", "");
		super.addTable("ldvideoroleinfodeslisttab", "DB", "int", false, "LDVideoRoleInfoDesList", "", "");
		super.addTable("teamfilters", "MEMORY", "long", false, "TeamFilter", "", "");
		super.addTable("blackmarketbagtab", "DB", "long", false, "Bag", "key:properties", "");
		super.addTable("enhancement", "DB", "long", true, "EnhancementAttr", "", "");
		super.addTable("equips", "DB", "long", true, "Equip", "", "");
		super.addTable("singlecompensations", "DB", "long", true, "SingleCompensation", "", "");
		super.addTable("refreshmonsterinfo", "MEMORY", "int", false, "RefreshMonsterNpcInfo", "", "");
		super.addTable("huobancolumns", "DB", "long", false, "HuoBanColumn", "key:properties", "");
		super.addTable("petdepot", "DB", "long", false, "Pets", "key:properties", "");
		super.addTable("roleeventnpcinfos", "DB", "long", false, "RoleEventNpcInfoColumn", "", "");
		super.addTable("medicitemlisttab", "DB", "long", false, "MedicItemList", "", "");
		super.addTable("professionleadervotechallenge", "DB", "long", false, "ProfessionLeaderVoteInfo", "key:properties", "");
		super.addTable("role2npcawards", "DB", "long", false, "npcawardrecords", "", "");
		super.addTable("marketattentionroletab", "DB", "string", false, "AttentionRole", "", "");
		super.addTable("onlinerefresh", "MEMORY", "long", false, "OnlinerefreshFuture", "", "");
		super.addTable("roleid2activityquestion", "DB", "long", false, "ActivityQuestion", "", "");
		super.addTable("role2instancezone", "DB", "long", false, "long", "", "");
		super.addTable("battlemelonid2melon", "MEMORY", "long", false, "ETeamMelon", "", "");
		super.addTable("roleid2kickinfo", "MEMORY", "long", false, "KickInfo", "", "");
		super.addTable("bagtimelock", "MEMORY", "long", false, "BagTimeLock", "", "");
		super.addTable("roleonoffstate", "MEMORY", "long", false, "int", "", "");
		super.addTable("courses", "DB", "long", false, "lichengxinxi", "key:properties", "");
		super.addTable("requestruneinfotab", "DB", "long", false, "RequestRuneInfoList", "", "");
	}
	public void DatabaseMetaData6(){
		super.addTable("pvp3historyscorelist", "DB", "int", false, "PvP3ScoreRankList", "", "");
		super.addTable("livedieweeknumtab", "DB", "int", false, "LiveDieWeekNum", "", "");
		super.addTable("ldvideoroleroseinfolisttab", "DB", "long", false, "LDVideoRoleRoseInfoList", "", "");
		super.addTable("userid2forbidtime", "MEMORY", "int", false, "forbidinfo", "", "");
		super.addTable("gather", "MEMORY", "long", false, "Gather", "", "");
		super.addTable("timingreward", "DB", "long", false, "RewardData", "key:properties", "");
		super.addTable("npcfollowroles", "MEMORY", "long", false, "NpcFollowInfo", "", "");
		super.addTable("roleid2particleskill", "DB", "long", false, "EParticleSkill", "", "");
		super.addTable("bingfengwangzuos", "MEMORY", "long", true, "BingFengWangZuo", "", "");
		super.addTable("clanfights", "DB", "long", false, "ClanFights", "", "");
		super.addTable("roleprofessionranklist", "DB", "int", false, "RoleProfessionRankList", "", "");
		super.addTable("chattimetab", "MEMORY", "long", false, "ChatTime", "", "");
		super.addTable("multiexp", "DB", "long", false, "MultiExpInfo", "key:properties", "");
		super.addTable("rolehook", "DB", "long", false, "HookData", "key:properties", "");
		super.addTable("ldvideohislisttab", "DB", "int", false, "LDVideoHisList", "", "");
		super.addTable("roleid2bj", "MEMORY", "long", false, "int", "", "");
		super.addTable("platorderhistroy", "DB", "string", false, "long", "", "");
		super.addTable("yybfushi", "DB", "int", false, "YybFushiNums", "", "");
		super.addTable("lianyunaward", "DB", "int", false, "LianyunAwardInfo", "", "");
		super.addTable("anyelegendtimer", "MEMORY", "long", false, "AnYeLegendFuture", "", "");
		super.addTable("petrecover", "DB", "long", false, "Petrecoverlist", "", "");
		super.addTable("watcherid2battleid", "MEMORY", "long", false, "long", "", "");
		super.addTable("levelseal", "DB", "int", false, "LevelSeal", "", "");
		super.addTable("beginnertip", "DB", "long", false, "BeginnerTip", "", "");
		super.addTable("bingfengroles", "DB", "long", false, "BingFengRole", "key:properties", "");
		super.addTable("roleid2liveskill", "DB", "long", false, "ELiveSkill", "", "");
		super.addTable("chargeorder", "DB", "long", true, "ChargeOrder", "", "");
		super.addTable("regtbl", "DB", "long", false, "RegRec", "", "");
		super.addTable("missioncameras", "DB", "long", false, "CameraInfo", "", "");
		super.addTable("opentreasuremaptable", "MEMORY", "long", false, "TreasureMapInfo", "", "");
		super.addTable("requestgoto", "MEMORY", "long", false, "long", "", "");
		super.addTable("professionleaderfightdata", "DB", "long", false, "Monster", "key:properties", "");
		super.addTable("rolespaces", "DB", "long", false, "RoleSpace", "", "");
		super.addTable("pvp3races", "MEMORY", "int", false, "PvP3Race", "", "");
		super.addTable("role2impexam", "DB", "long", false, "ImpExamRecord", "key:properties", "");
		super.addTable("payplatform", "DB", "int", false, "PlatformInfo", "", "");
		super.addTable("instanceturngroups", "DB", "int", false, "InstanceTurnGroup", "", "");
		super.addTable("huobanvips", "DB", "int", false, "HuoBanVip", "", "");
		super.addTable("tradingidtab", "MEMORY", "int", false, "TradingMap", "", "");
		super.addTable("rolequitstatistics", "DB", "long", false, "RoleQuitStatistics", "key:properties", "");
		super.addTable("pettab", "DB", "long", true, "PetItem", "", "");
		super.addTable("roleidclan", "DB", "long", false, "long", "", "");
		super.addTable("clanzonghelist", "DB", "int", false, "ClanZongHeRankList", "", "");
		super.addTable("ticketiteminfo", "DB", "long", true, "TicketItemExInfo", "", "");
		super.addTable("timerfutures", "MEMORY", "long", true, "TimerFuture", "", "");
		super.addTable("fushinum", "DB", "int", false, "YbNums", "", "");
		super.addTable("compensation", "DB", "long", false, "Compensation", "", "");
		super.addTable("huobanzhenrongs", "DB", "long", false, "HuoBanZhenrong", "key:properties", "");
		super.addTable("rolesplayactive", "DB", "long", false, "NewPlayActiveDay", "key:properties", "");
		super.addTable("broadcastmsgtab", "DB", "int", false, "BroadcastMsgList", "", "");
		super.addTable("compensationroles", "DB", "long", false, "CompensationRole", "key:properties", "");
		super.addTable("changeschool", "DB", "long", false, "ChangeSchoolInfo", "key:properties", "");
		super.addTable("opentraderwheeltable", "MEMORY", "long", false, "TraderWheel", "", "");
		super.addTable("rolepetsellcount", "DB", "long", false, "PetSellCount", "key:properties", "");
		super.addTable("clanfightbattlefield", "DB", "long", false, "ClanFightBattleField", "", "");
		super.addTable("role2npcbattle", "MEMORY", "long", false, "long", "", "");
		super.addTable("pvp5races", "MEMORY", "int", false, "PvP5Race", "", "");
		super.addTable("roletradinghisrecordlisttab", "DB", "long", false, "RoleTradingHisRecordList", "", "");
		super.addTable("awarditemlimit", "DB", "int", false, "ActivityItemLimit", "", "");
		super.addTable("usercompensation", "DB", "string", false, "Compensation", "", "");
		super.addTable("rolereceiveredpackrecordtab", "DB", "long", false, "RoleReceiveRedPackRecordList", "", "");
		super.addTable("roleid2worldredpacksendtotal", "DB", "long", false, "RedPack", "", "");
		super.addTable("roleyaoqianshuinfos", "MEMORY", "long", false, "long", "", "");
		super.addTable("yybchargeorder", "DB", "long", true, "YybOrder", "", "");
		super.addTable("instancefuture", "MEMORY", "long", false, "InstanceFutureInfo", "", "");
		super.addTable("roletaskeventtab", "MEMORY", "long", false, "TaskEventInfo", "", "");
		super.addTable("pet", "DB", "long", false, "Pets", "key:properties", "");
		super.addTable("gacdpic", "MEMORY", "int", false, "GacdPicStatus", "", "");
		super.addTable("instanceaskinfos", "MEMORY", "long", false, "InstanceAskInfo", "", "");
		super.addTable("pvp5scorelist", "DB", "int", false, "PvP5ScoreRankList", "", "");
		super.addTable("bestownpc", "MEMORY", "long", false, "BestowNpcInfo", "", "");
		super.addTable("rolemuldayloginreward", "DB", "long", false, "MulDayLoginGift", "", "");
		super.addTable("marketpet", "DB", "long", false, "Pets", "key:properties", "");
		super.addTable("blackmarketpettab", "DB", "long", false, "Pets", "key:properties", "");
		super.addTable("runeinfotab", "DB", "long", false, "RuneInfo", "", "");
		super.addTable("rolezonghelist", "DB", "int", false, "RoleZongheRankList", "", "");
		super.addTable("roleid2chargereturnprofit", "DB", "long", false, "EChargeReturnProfit", "", "");
		super.addTable("redpackinfoviewlisttab", "DB", "int", false, "RedPackInfoViewList", "", "");
		super.addTable("showingmission", "MEMORY", "long", false, "ShowingMission", "", "");
		super.addTable("huobanproperty", "MEMORY", "int", false, "HuoBanShuxingLevel", "", "");
		super.addTable("roleid2teamid", "MEMORY", "long", false, "long", "", "");
		super.addTable("rolenumfornewserver", "DB", "int", false, "ServerRoles", "", "");
		super.addTable("rolerecruitaward", "DB", "long", false, "RecruitAward", "", "");
		super.addTable("wheelitemlimit", "DB", "int", false, "WheelItemLimit", "", "");
		super.addTable("fdqmazelist", "MEMORY", "long", false, "VecList", "", "");
		super.addTable("blackmarkettab", "DB", "long", false, "RoleBlackMarket", "", "");
		super.addTable("activitytable", "DB", "int", false, "ActivityStatus", "", "");
		super.addTable("rolerollcard", "DB", "long", false, "RollCardInfo", "key:properties", "");
		super.addTable("roleid2clanfightid", "DB", "long", false, "long", "", "");
		super.addTable("anyemaxituan", "DB", "long", false, "AnYeData", "", "");
		super.addTable("marketattentiongoodstab", "DB", "long", false, "AttentionGoods", "", "");
		super.addTable("rolelevellist", "DB", "int", false, "RoleLevelRankList", "", "");
		super.addTable("roleid2battlemelonid", "MEMORY", "long", false, "long", "", "");
		super.addTable("notify", "MEMORY", "int", false, "RolesNotifyMap", "", "");
		super.addTable("normaltab", "DB", "long", true, "NormalItem", "", "");
		super.addTable("roleid2userid", "MEMORY", "long", false, "int", "", "");
		super.addTable("worldeventtab", "MEMORY", "long", false, "EventInfo", "", "");
		super.addTable("preselltab", "DB", "long", true, "PresellItem", "", "");
		super.addTable("pvp1races", "MEMORY", "int", false, "PvP1Race", "", "");
		super.addTable("roleinstancetask", "DB", "long", false, "InstanceInfoCol", "key:properties", "");
	}
	public void DatabaseMetaData7(){
		super.addTable("roletradingrecordstab", "DB", "string", false, "RoleTradingRecord", "", "");
		super.addTable("professionleadercandidates", "DB", "int", false, "ProfessionLeaderCand", "", "");
		super.addTable("roleid2professionleadertickets", "DB", "long", false, "ProfessionLeaderTicketInfo", "key:properties", "");
		super.addTable("targetid2teammatch", "MEMORY", "int", false, "ETeamMatch", "", "");
		super.addTable("questbag", "DB", "long", false, "Bag", "key:properties", "");
		super.addTable("claninstances", "DB", "long", false, "ClanInstances", "", "");
		super.addTable("yaoqianshutables", "DB", "long", false, "YaoQianShuInfo", "", "");
		super.addTable("firstchargenew", "DB", "int", false, "FirstCharge", "", "");
		super.addTable("triggerroles", "DB", "long", false, "TriggerRole", "key:properties", "");
		super.addTable("offlinefuture", "MEMORY", "long", false, "OfflineFuture", "", "");
		super.addTable("clans", "DB", "long", true, "ClanInfo", "", "");
		super.addTable("rolerenxingdata", "DB", "long", false, "RenXingData", "key:properties", "");
		super.addTable("activeroletable", "DB", "long", false, "ActiveRoleInfo", "key:properties", "");
		super.addTable("roleuseitemcount", "DB", "long", false, "ItemUse", "key:properties", "");
		super.addTable("activeusertable", "DB", "int", false, "ActiveUserInfo", "", "");
		super.addTable("timernpcinfotable", "MEMORY", "int", false, "timerNpcInfoCol", "", "");
		super.addTable("npcs", "MEMORY", "long", false, "NpcInfo", "", "");
		super.addTable("takebackmarketcontainertab", "DB", "long", false, "int", "", "");
		super.addTable("depot", "DB", "long", false, "Bag", "key:properties", "");
		super.addTable("friendgivenum", "DB", "long", false, "FriendGiveNum", "key:properties", "");
		super.addTable("majorscenariomission", "DB", "long", false, "Mission", "", "");
		super.addTable("rolename2key", "DB", "string", false, "long", "", "");
		super.addTable("rolekaibaoxiang", "DB", "long", false, "BaoXiangInfo", "key:properties", "");
		super.addTable("livedieroleinfotab", "DB", "long", false, "LiveDieRoleInfo", "key:properties", "");
		super.addTable("itemrecover", "DB", "long", false, "Itemrecoverlist", "", "");
		super.addTable("commitedmission", "DB", "long", false, "CommitedMissions", "", "");
		super.addTable("instancetask", "MEMORY", "long", true, "InstanceInfo", "", "");
		super.addTable("bianjieteam", "MEMORY", "int", false, "BJTeamInfo", "", "");
		super.addTable("openschoolwheeltable", "MEMORY", "long", false, "SchoolWheel", "", "");
		super.addTable("clanranklist", "DB", "int", false, "ClanRankList", "", "");
		super.addTable("orderidtab", "DB", "long", true, "long", "", "");
		super.addTable("singleinviting", "MEMORY", "long", false, "SingleInvitings", "", "");
		super.addTable("roleredpackrecordtab", "DB", "long", false, "RoleRedPackRecord", "", "");
		super.addTable("redpacklist", "DB", "int", false, "RedPackRankList", "", "");
		super.addTable("servicesets", "DB", "int", false, "ServiceSet", "", "");
		super.addTable("itemrecyclebin", "DB", "long", false, "DiscardItem", "", "");
		super.addTable("erole", "MEMORY", "long", false, "ERole", "", "");
		super.addTable("markettradelogtab", "DB", "long", false, "MarketTradeLog", "key:properties", "");
		super.addTable("clanfightracelist", "DB", "long", false, "ClanFightRaceRankList", "", "");
		super.addTable("userdeviceinfotab", "DB", "int", false, "UserDeviceInfo", "", "");
		super.addTable("onetimeeventtab", "MEMORY", "long", false, "OnetimeEvent", "", "");
		super.addTable("rolesplayactiveweek", "DB", "long", false, "NewPlayActiveWeek", "key:properties", "");
		super.addTable("rolesfestivaldata", "DB", "long", false, "FestivalData", "", "");
		super.addTable("friends", "DB", "long", false, "FriendGroups", "key:properties", "");
		super.addTable("market", "DB", "long", false, "Bag", "key:properties", "");
		super.addTable("chargehistory", "DB", "int", false, "ChargeHistory", "", "");
		super.addTable("teaminvite", "MEMORY", "long", false, "InviteInfo", "", "");
		super.addTable("roleposes", "DB", "long", false, "RolePos", "key:properties", "");
		super.addTable("roletimernpcinfos", "DB", "long", false, "RoleActiveTimerNpcInfo", "", "");
		super.addTable("fushirecordtable", "DB", "long", false, "RoleFuShiRecord", "", "");
		super.addTable("instancezones", "DB", "long", true, "InstanceZone", "", "");
		super.addTable("livedie2key", "DB", "long", false, "long", "", "");
		super.addTable("roleapplyclantab", "DB", "long", false, "RoleApplyClanList", "", "");
		super.addTable("roleid2weekgiverecegift", "DB", "long", false, "WeekGiveReceGift", "", "");
		super.addTable("rolepingbirolesinfos", "DB", "long", false, "RolePingBiRolesInfo", "", "");
		super.addTable("redpackroleinfolisttab", "DB", "long", false, "RedPackRoleInfoList", "", "");
		super.addTable("progressbartable", "MEMORY", "long", false, "ProgressBarInfo", "", "");
		super.addTable("winnerscore", "DB", "long", false, "WinnerRoleRecord", "key:properties", "");
		super.addTable("randomnametab", "MEMORY", "int", false, "RandomNameInfo", "", "");
		super.addTable("clanprogressranklist", "DB", "int", false, "ClanProgressRankList", "", "");
		super.addTable("tablekeynum", "DB", "int", false, "int", "", "");
		super.addTable("invitepeopleinfo", "DB", "long", false, "InvitePeopleInfo", "", "");
		super.addTable("rolelockscreen", "MEMORY", "long", false, "boolean", "", "");
		super.addTable("skillroles", "DB", "long", false, "SkillRole", "key:properties", "");
		super.addTable("roleid2clanfightstatistics", "DB", "long", false, "EClanFightStatistics", "", "");
		super.addTable("rolesafelock", "DB", "long", false, "RoleLockInfo", "", "");
		super.addTable("uniquepets", "DB", "long", false, "UniquePet", "", "");
		super.addTable("petscorelist", "DB", "int", false, "PetScoreRankList", "", "");
		super.addTable("rolecircletask", "DB", "long", false, "CircleTaskMap", "key:properties", "");
		super.addTable("modnameitemroles", "DB", "long", false, "ModifyNameRole", "", "");
		super.addTable("failedchargeorder", "DB", "long", false, "ChargeOrder", "", "");
		super.addTable("trackedmission", "DB", "long", false, "TrackedMission", "", "");
		super.addTable("roleclanpoint", "DB", "long", false, "RoleClanPoint", "", "");
		super.addTable("offlinemsg", "DB", "long", false, "OfflineMsgList", "", "");
		super.addTable("npcfightnum", "MEMORY", "long", false, "int", "", "");
		super.addTable("temp", "DB", "long", false, "Bag", "", "");
		super.addTable("pvp3roles", "DB", "long", false, "PvP3Role", "", "");
		super.addTable("helpcount", "DB", "long", false, "helpcount", "key:properties", "");
		super.addTable("itemrecycleidx", "DB", "ItemRecycleDate", false, "ItemRecycleIndex", "", "");
		super.addTable("transfromroles", "MEMORY", "long", false, "TransfromInfo", "", "");
		super.addTable("impexamstatetbl", "DB", "int", false, "ImpExamStateRecord", "", "");
		super.addTable("roledaypay", "DB", "int", false, "EDayPay", "", "");
		super.addTable("vipinfo", "DB", "long", false, "Vipinfo", "", "");
		super.addTable("npcbattleinfo", "MEMORY", "long", false, "npcBattleInfoCol", "", "");
		super.addTable("serviceinfos", "DB", "int", false, "ServiceInfo", "", "");
		super.addTable("receflowerlist", "DB", "long", false, "ReceFlowerRankList", "", "");
		super.addTable("srredpacknumlisttab", "DB", "long", false, "SRRedPackNumList", "", "");
		super.addTable("wheelprogress", "DB", "long", false, "WheelInfos", "key:properties", "");
		super.addTable("equip", "DB", "long", false, "Bag", "key:properties", "");
		super.addTable("rolebestow", "DB", "long", false, "RoleBestowInfo", "key:properties", "");
		super.addTable("giveflowerlist", "DB", "long", false, "GiveFlowerRankList", "", "");
		super.addTable("accusationinfos", "DB", "long", false, "AccusationInfo", "", "");
		super.addTable("user", "DB", "int", false, "User", "", "");
		super.addTable("properties", "DB", "long", true, "Properties", "", "");
		super.addTable("roletasks", "DB", "long", false, "TaskInfos", "key:properties", "");
		super.addTable("appstoretidstatus", "DB", "long", false, "AppstoretidStatus", "", "");
	}
	public _DatabaseMetaData_() {
		DatabaseMetaData1();
		DatabaseMetaData2();
		DatabaseMetaData3();
		DatabaseMetaData4();
		DatabaseMetaData5();
		DatabaseMetaData6();
		DatabaseMetaData7();
	}
}

