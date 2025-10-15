package fire.pb.util;

import java.util.HashMap;
import java.util.Map;

import fire.log.Module;
import fire.pb.CCreateRole;
import fire.pb.main.ConfigManager;
import fire.pb.main.ServerArea;
import fire.pb.talk.MessageMgr;




public class CheckName {
	
	public static final int SPECIAL_WORD_TOO_MANY = -1;//特殊字符过多
	public static final int NONE_CHARACTER = -2;//无汉字或字母
	public static final int WORD_ILLEGALITY = -3;//非法字符
	public static final int WORD_LEGAL = 0;//字符合法
	public static final Map<String, String> SERVERAREA2NAMEREG = new HashMap<String, String>();
	static{
		SERVERAREA2NAMEREG.put(ServerArea.KOREA, "[\uac00-\ud7ff\u1100-\u11ff\u3130-\u318f]");
		SERVERAREA2NAMEREG.put(ServerArea.THAILAND, "[\u0e00-\u0e7f]");
	}
	
	
	public static int checkValid(final String name){
		int counter = 0;
		int specialCounter = 0;
		

		for (int i=0; i<name.length(); i++){
			String regexStr = getRegexStr();
			
			//匹配字符
			if (name.substring(i, i+1).matches(regexStr)){
				counter ++;
				continue;
			}
			
			//取得index位置的char Value
			final int asc = name.charAt(i);
			if (asc >='a' &&  asc<='z'
					|| asc >= 'A' && asc<='Z'){
				counter ++;
				continue;
			}
			
			if(asc >= '0' && asc <= '9'){
				continue;
			}
		
			
			//最后匹配火星文，如都不符合，则字符非法
			boolean isIllegal = false;
			int length = Module.SPECIAL_CHAR_ARRAY.length;
			for(int j=0; j<length; j++){
				if(asc == Module.SPECIAL_CHAR_ARRAY[j]){
					specialCounter ++;
					isIllegal = true;
					if(specialCounter > 3){
						return SPECIAL_WORD_TOO_MANY;
					}
					break;
				}
			}
			if(!isIllegal){
				return WORD_ILLEGALITY;
			}
				
		}
		
		if(counter < 1){
			return NONE_CHARACTER;
		}
		
		if (name.matches("(.*)(GM|Gm|gM|gm)(.*)")){
			return WORD_ILLEGALITY;
		}
		if (!fire.pb.util.StringValidateUtil.checkValidName(name)) {
			return WORD_ILLEGALITY;
		}
		
		return WORD_LEGAL;
	}

	public static int nameLen(){
		if (ConfigManager.getInstance().serverArea.equals(ServerArea.THAILAND)) 
			return 16;
		return CCreateRole.NAMELEN_MAX;
	}
	
	
	public static String getRegexStr() {

		//韩国版匹配韩文
		String regString = SERVERAREA2NAMEREG.get(ConfigManager.getInstance().serverArea);
		if (regString!=null)
			return regString;
		else
			return "[\u4e00-\u9fa5]";//默认返回汉字
	}

	public static boolean checkAndSendMessage(long roleid,String name, boolean isProcedure){
		int resultCode = fire.pb.util.CheckName.checkValid(name);
		int msgId = 0;
		if(resultCode == CheckName.WORD_ILLEGALITY){
			msgId = 142260;
		}else if(resultCode == CheckName.SPECIAL_WORD_TOO_MANY){
			msgId = 142294;
		}else if(resultCode == CheckName.NONE_CHARACTER){
			msgId = 142293;
		}
		
		if(msgId != 0){
			if(isProcedure){
				MessageMgr.psendMsgNotify(roleid, msgId, null);
			}else{
				MessageMgr.sendMsgNotify(roleid, msgId, null);
			}
			return false;
		}else{
			return true;
		}
		
	}
	
}
