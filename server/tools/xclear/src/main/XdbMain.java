package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mkdb.Trace;
import mylogic.BrowseClans;
import mylogic.BrowseUser;
import mylogic.ClanEventDel;
import mylogic.RemoveAllRoleTable;
import mylogic.UserIdsDel;



public class XdbMain {
	private static final String PROPERTY_FILE = "server.txt";
	private static HashMap<String,String> properties = new HashMap<String,String>();
	
	public static void main(String args[]) {
		loadProperties(PROPERTY_FILE);
		int type=Integer.parseInt(properties.get("servertype"));
		
		//合服删除号需要注意，应用宝的充值是单独一张表，他的合服需要特殊处理是否充值的玩家
		Set<Long> roleIds = new HashSet<Long>();
		Set<Long> clanIds = new HashSet<Long>();
		Set<Integer> userIds = new HashSet<Integer>();
		
		
		System.out.println("开始处理时间\t"+new Date());
		Trace.info("开始处理时间\t"+new Date());
		String xdbConfName = "gsx.mkdb.xml";
		mkdb.MkdbConf conf = new mkdb.MkdbConf(xdbConfName);
		mkdb.Mkdb.getInstance().setConf(conf);
		mkdb.Mkdb.getInstance().start();
		try {
//			//所有处理数据的逻辑
//			ClearBagMoney.clearBagGoldMoney();
			
//			//1.删选等级和时间不符合的玩家
//			SelectDelRoleid.selectDelRole(roleIds);
//			int num1=roleIds.size();
//			//2.从删除列表中移除充值的玩家
//			RemovePayRoleid.removePlayRole(roleIds,type);
//			int num2=roleIds.size();
//			System.out.println("符合删除等级的玩家数量\t"+num1);
//			System.out.println("去掉充值玩家后，符合删除等级的玩家数量\t"+num2+"\t差值\t"+(num1-num2));
			
			//1.遍历user表，删除超过6个角色的玩家
			BrowseUser.browseUsersids(userIds);
			UserIdsDel.remove(type,userIds,roleIds);
			
			//遍历所有的公会信息，清除一下公会事件
			BrowseClans.browseClansids(clanIds);
			ClanEventDel.clearClanEvent(clanIds);
			
			//注意需要删除user里面的角色列表中的id
			//以下删除所有玩家做主键的表
			RemoveAllRoleTable.removeAllRoleTable(roleIds);
			
			
			
		} catch (Exception e) {
			System.out.println("Exception = " + e);
		}
		finally {
			mkdb.Mkdb.getInstance().stop();
			System.out.println("Mkdb stop.");
			System.out.println("结束时间\t"+new Date());
		}
	}
	
	public static boolean loadProperties(String file){
		try{
			File f = new File(file);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String message = null;

			while((message = br.readLine())!=null){
				if(message.length() > 0){
					if(message.charAt(0) != ';' ||
							message.charAt(0) != '#' ||
							message.charAt(0) != '!'){
						String[] chars = message.split("=");
						if(chars.length >= 2){
							chars[1] = chars[1].replace("\\n", "\n");
							properties.put(chars[0],chars[1]);
						}
					}
				}
			}
			return true;
		}catch(Exception e){
			System.out.printf("\t" + "读取设置文件" + file + "失败",e);
		}
		return false;
	}
	
	
	
	
}
