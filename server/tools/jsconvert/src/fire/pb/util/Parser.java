package fire.pb.util;

import java.util.HashMap;
import java.util.Map;

import fire.script.JavaScript;

public class Parser {
	public static Map<Integer, JavaScript> parseFightJsEffects(String effectstrs) throws Exception {
		Map<Integer, JavaScript> effectMap = new HashMap<Integer, JavaScript>();
		if (effectstrs != null && !effectstrs.equals("")) {
			try {
				String[] effectStr = effectstrs.split(";");
				for (int i = 0; i < effectStr.length; i++) {
					String[] tmpstrs = effectStr[i].split("=");
					if (tmpstrs.length < 2)
						throw new Exception("Wrong effects str：	  "
								+ effectstrs);

					int effectId = fire.pb.effect.Module.getInstance().getIdByName(tmpstrs[0].trim());// 第一个“=”之前的字符串是Effectname
					String effectValueJS = effectStr[i].substring(tmpstrs[0].length() + 1);// 取第一个“=”之后的字符串，是JS公式
					JavaScript compiledJS = new JavaScript(effectValueJS);
					effectMap.put(effectId, compiledJS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR: ParseFightJsEffects fail. effectstrs = "
								+ effectstrs);
			}

		}
		return effectMap;
	}

	public static String convertStackTrace2String(Exception e) {
		String tracestr = e.toString();
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i < trace.length; i++)
			tracestr += ("\tat " + trace[i] + "\n");
		return tracestr;
	}

	public static class ID2Odds {
		public ID2Odds(int num) {
			ids = new int[num];
			odds = new int[num];
		}

		public final int[] ids;
		public final int[] odds;

	}
}
