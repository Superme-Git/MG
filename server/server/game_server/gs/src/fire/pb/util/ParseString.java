package fire.pb.util;
public class ParseString {
	
	
	public static java.util.List<GeneralFormat> parseString2GeneralFormat(String str){
		if (str == null)
			return null;
		java.util.List<GeneralFormat> tlist = new java.util.ArrayList<GeneralFormat>();
		
		String[] semi = str.split(";");
		for (String semi_str : semi){
			GeneralFormat f = new GeneralFormat();
			
			String[] well = semi_str.split("#");
			if (well.length == 1){
				f.setSubid(0);
			}
			else if (well.length == 2){
				f.setSubid(Integer.parseInt(well[1]));
			}
			else{
				throw new RuntimeException("错误的字符串配置格式 " + str);
			}
			
			String[] at = well[0].split("@");
			if (at.length !=2){
				throw new RuntimeException("错误的字符串配置格式 " + str);
			}
			
			f.setId(Integer.parseInt(at[0]));
			f.setRate(Integer.parseInt(at[1]));
			
			tlist.add(f);
		}
		return tlist;
		
	}
	
	public static java.util.List<GeneralFormat> parseString2GeneralFormatWithoutException(String str){
		if (str == null)
			return null;
		java.util.List<GeneralFormat> tlist = new java.util.ArrayList<GeneralFormat>();
		
		String[] semi = str.split(";");
		for (String semi_str : semi){
			GeneralFormat f = new GeneralFormat();
			
			String[] well = semi_str.split("#");
			if (well.length == 1){
				f.setSubid(0);
			}
			else if (well.length == 2){
				f.setSubid(Integer.parseInt(well[1]));
			}
			else{
				return null;
			}
			
			String[] at = well[0].split("@");
			if (at.length !=2){
				return null;
			}
			
			f.setId(Integer.parseInt(at[0]));
			f.setRate(Integer.parseInt(at[1]));
			
			tlist.add(f);
		}
		return tlist;
		
	}
	

	
	public static String toLowerCaseFristOne(String input){
		assert(input!=null);
		if (input.equals("")) {
			return input;
		}
		
		if (Character.isLowerCase(input.charAt(0))) {
			return input;
		}
		
		if (input.length()<2) {
			return input.toLowerCase();
		}
		
		
		return new StringBuilder().append(Character.toLowerCase(input.charAt(0))).append(input.substring(1)).toString();
		
	}
	
	public static String toUperCaseFristOne(String input){
		assert(input!=null);
		if (input.equals("")) {
			return input;
		}
		
		if (Character.isUpperCase(input.charAt(0))) {
			return input;
		}
		
		if (input.length()<2) {
			return input.toUpperCase();
		}
		
		
		return new StringBuilder().append(Character.toUpperCase(input.charAt(0))).append(input.substring(1)).toString();
		
	}
	
	
}
