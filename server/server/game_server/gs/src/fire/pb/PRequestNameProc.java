package fire.pb;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import fire.pb.util.Misc;
import fire.pb.util.NameManage;
import mkdb.util.UniqName;


public class PRequestNameProc extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("SYSTEM");
	private int userid;
	private short sex;
	public CRequestName cRequestName;
	
	public PRequestNameProc(final int userid,short sex,CRequestName cRequestName){
		this.userid = userid;
		this.sex=sex;
		this.cRequestName=cRequestName;
	}
	public boolean process(){
		long cur_time = System.currentTimeMillis();
		xbean.RandomNameInfo randomNameInfo = xtable.Randomnametab.get(userid);
		if (randomNameInfo == null){
			randomNameInfo = xbean.Pod.newRandomNameInfo();
			xtable.Randomnametab.insert(userid, randomNameInfo);
		}
		Long lastTime = randomNameInfo.getRandomtime();
		if (lastTime!=null&&cur_time - lastTime < 900){
			return true;
		}
        randomNameInfo.setRandomtime(cur_time);
		String name = getRandomName(sex==1 ? true:false);//1男  2女
		if (name!=null&&name.length()>0){
			SGiveName sGiveName = new SGiveName(name);
			gnet.link.Onlines.getInstance().sendResponse(cRequestName, sGiveName);
		}
		return true;
	}
	
	private String getRandomName(boolean isman) {
       for (int i = 0; i < 100; i++) {
			StringBuilder result_name = new StringBuilder();
			String x_name = "";// 生成姓
			String m_name = "";// 生成名
			if (isman) {// 男
				int findex = Misc.getRandomBetween(0,NameManage.x_man_num - 1);
				x_name = NameManage.x_man_list.get(findex);
				int lIndex = Misc.getRandomBetween(0,NameManage.m_man_num - 1);
				m_name = NameManage.m_man_list.get(lIndex);
			} else {
				int findex = Misc.getRandomBetween(0,NameManage.x_woman_num - 1);
				x_name = NameManage.x_woman_list.get(findex);
				int lIndex = Misc.getRandomBetween(0,NameManage.m_woman_num - 1);
				m_name = NameManage.m_woman_list.get(lIndex);
			}
			result_name.append(x_name).append(m_name);

			int length = 0;
			try {
				length = (new String(result_name.toString().getBytes("gb2312"),"ISO-8859-1")).length();
			} catch (UnsupportedEncodingException e) {
				return null;
			}
			// 在随机时进行屏蔽和非法字符判断，给出结果为合法有效名称。
			int resultCode = fire.pb.util.CheckName.checkValid(result_name.toString());
			if (resultCode < 0) {
				logger.info("随机名字的时候包含非法字符   " + result_name.toString());
				continue;
			}
			// 查询名字是否重复
			if (UniqName.exist("role", result_name.toString()) == UniqName.RPC_NOT_EXISTS
					&& length <= fire.pb.CCreateRole.NAMELEN_MAX) {
				return result_name.toString();
			}
	   }
       
       return null;
	}

}
