package fire.pb.title;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class TitleTask  extends TimerTask{
	@Override
	public void run() {
		//处理限时称号逻辑
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				ConcurrentHashMap<Long, Long> titlemap=new ConcurrentHashMap<Long, Long>();
				titlemap.putAll(Title.titleroleids);
				long time=System.currentTimeMillis();
				for(long roleId:titlemap.values()){
					boolean isremonve=true;
					List<Integer> titleIds = new ArrayList<Integer>();
					xbean.Properties pro = xtable.Properties.select(roleId);
					for (xbean.TitleInfo titleinfo : pro.getTitles().values()) {
						if (titleinfo.getAvailtime() > 0){
							if (titleinfo.getAvailtime() < time){
								titleIds.add(titleinfo.getTitleid());
							}else{
								//有没有过期的
								isremonve=false;
							}
						}
					}
					for (Integer titleid : titleIds) {
						mkdb.Procedure.pexecuteWhileCommit(new PRemoveTitleProc(roleId,
								titleid));
					}
					if(isremonve){
						Title.titleroleids.remove(roleId);
					}
				}
				return true;
			}
		}.submit();
		

		
	}
	
}