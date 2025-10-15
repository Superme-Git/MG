package fire;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;

import sun.misc.Signal;
import sun.misc.SignalHandler;
import fire.log.Logger;

/**linux 下用kill -15 pid 来正常关闭服务器*/
public class TestSignal implements SignalHandler {
	Logger logger=Logger.getLogger("SYSTEM");
	private static TestSignal instance;
	public static TestSignal getInstance() {
		if (instance == null) {
			instance = new TestSignal();
			Signal.handle(new Signal("TERM"), instance);
			
		}
		return instance;
	}
 
    private void signalCallback(Signal sn) {
        logger.info("监听到关服信号--------------------------------------"+sn.getName());
        if(!SystemInfos.isWindowsOS()){
        	try {
				//调用关闭服务器接口
				long time = 60;
				ManagementFactory.getPlatformMBeanServer().setAttribute(new javax.management.ObjectName("bean:name=stopper"), new javax.management.Attribute("StopTime",time));
				List<String> pars = new LinkedList<String>();
				pars.add(String.valueOf(time/60));
				fire.pb.talk.MessageMgr.broadcastMsgNotify(141390, 0, pars); 
				fire.pb.talk.MessageMgr.broadcastMsgNotify(141391, 0, pars);
			} catch(Exception e){
				e.printStackTrace();
			}
        }
    }
 
    @Override
    public void handle(Signal signalName) {
        signalCallback(signalName);
    }

}
