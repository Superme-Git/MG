package fire.pb;

import org.apache.log4j.Logger;


/**
 * 援助统计功能清除信息
 *
 */
public class PClearHelpCountProc extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("SYSTEM");
	private long roleid;
	private int updatetype=0;//
	
	public static final int UPDATE_DAY=1;//每天更新
	public static final int UPDATE_WEEK=0;//每周更新
	
	public PClearHelpCountProc(final long roleid,int type){
		this.roleid = roleid;
		this.updatetype=type;
	}
	public boolean process(){
		xbean.helpcount helpcount = xtable.Helpcount.get(roleid);
    	if (helpcount != null) {
//    		if(updatetype==UPDATE_DAY){
//    			helpcount.setExpvalue(0);
//    			logger.info("玩家角色id\t"+roleid+"\t清除周援助统计经验值\t");
//    		}else{
    			helpcount.setShengwangvalue(0);
    			helpcount.setClanvalue(0);
    			helpcount.setExpvalue(0);
    			helpcount.setHelpgiveitemnum(0);
    			helpcount.setHelpitemnum(0);
    			logger.info("玩家角色id\t"+roleid+"\t清除周援助统计\t");
//    		}
    		
    	}
		
		return true;
	}

}
