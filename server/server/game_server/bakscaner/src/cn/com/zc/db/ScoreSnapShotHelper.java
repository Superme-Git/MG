package cn.com.zc.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.com.zc.db.bean.ScoreSnapShotBean;
import cn.com.zc.db.util.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 取得玩家的评分快照
 */
public class ScoreSnapShotHelper {
	/**
	 * 玩家的快照结构体
	 */
	
	private static Map<Long,ScoreSnapShotBean> beans=new HashMap<Long, ScoreSnapShotBean>() ;
	

	/**
	 
	 */
	public static ScoreSnapShotBean getScoreSnapShotBean(long roleId){
			return beans.get(roleId);
	}
	
	
	
	
	public static ScoreSnapShotBean initScoreSnapShotBean(long roleId,String roleName,int level){
		if (!beans.containsKey(roleId)) {
		}
		beans.put(roleId,new ScoreSnapShotBean(roleId,roleName,level));
		
		return getScoreSnapShotBean(roleId);
	}
	
	/**
	 * 内存受不了
	 
	 */
	@Deprecated 
	public static void toXml(String path,String fileName) {
		XStream xStream = new XStream(new DomDriver());
		File active = new File(path +fileName);
		try {
			if(active.exists()){
				active.delete();
			}
			active.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		OutputStream os = null;
		try {
			os = new FileOutputStream(active);
			xStream.toXML(beans.values(),os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(os != null){
			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void toFile(String path,String fileName){
		StringBuilder builder=new StringBuilder(999999);
		builder.append(ScoreSnapShotBean.TITTLE_STRING);
		for (ScoreSnapShotBean bean : beans.values()) {
			builder.append("\n");
			builder.append(bean.toString());
		}
		FileUtils.toFile(path, fileName,builder.toString());
	}
	
}
