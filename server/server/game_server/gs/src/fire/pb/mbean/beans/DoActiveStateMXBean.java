package fire.pb.mbean.beans;

import fire.log.Module;

/**
 * 
 * 给JMX Server一个事件的接口来刷新运营log
 * 
 *
 */

public interface DoActiveStateMXBean {
	
	void doRefreshActiveLog();
	
	void doExpState();

}

class DoActiveState implements DoActiveStateMXBean{

	@Override
	public void doRefreshActiveLog() {
		//Log文件扫描
		Module.analyseLogFromXml();
	}
	
	@Override
	public void doExpState(){
		try {
		} catch (Exception e) {
			e.printStackTrace();
			Module.logger.error("通过JMX调用Exp统计出错：  ", e);
		}
	}
	
}

