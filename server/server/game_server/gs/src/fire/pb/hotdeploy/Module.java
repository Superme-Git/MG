package fire.pb.hotdeploy;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import fire.pb.SysConfig.SHotdeployClass;
import fire.pb.gm.ExtraCommandClassLoader;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import mkdb.Procedure;
import mkio.Protocol;


public class Module implements ModuleInterface {

	static Logger logger = Logger.getLogger(Module.class);
	
	static boolean hasNewProcedureClass = false;
	static boolean hasNewProtocolClass = false;
    static Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    @Override
    public void exit() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void init() {
    	try {
			loadClassFromCache();
		} 
		 catch (ClassNotFoundException e) {
			logger.info("服务器启动时加载proc的对应Class出错，服务器已经加载gs/hotdeploy.jar!!!，请检查/gamedata/xml/auto/fire.pb.SystemSetting.SHotdeployClass.xml文件");
		}
    }

    public ReloadResult loadClassFromCache() throws ClassNotFoundException {
    	classes.clear();

        String jarfilename = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.hotdeploy.jarfile");
        ExtraCommandClassLoader clzloader = new ExtraCommandClassLoader(".", jarfilename);

        Map<Integer,SHotdeployClass> confs = ConfigManager.getInstance().getConf(SHotdeployClass.class);
        for (SHotdeployClass sHotdeployClass : confs.values()){
            if (clzloader.loadByMe(sHotdeployClass.newClassName)) {
            	Class<?> newClass = clzloader.loadClass(sHotdeployClass.newClassName);
            	if (mkdb.Procedure.class.isAssignableFrom(newClass)) 
            		hasNewProcedureClass = true;
            	else if (mkio.Protocol.class.isAssignableFrom(newClass))
            		hasNewProtocolClass = true;
                classes.put(sHotdeployClass.oldClassName,  clzloader.loadClass(sHotdeployClass.newClassName));
            }else{

                return new ReloadResult(false);
            }
        }
        return new ReloadResult(true);
    }
    
	@Override
    public ReloadResult reload() {
        ReloadResult result = null;
        try {
			result = loadClassFromCache();
		} 
		 catch (ClassNotFoundException e) {
			logger.error("服务器热加载proc的时候Class反射出错：", e);
		}
   
		return result;
    }

    public static Procedure getHotdeployProcedure(String className, Procedure oldProc) throws Exception {
        @SuppressWarnings("unchecked")
		Class<? extends mkdb.Procedure> newProcedureClass = (Class<? extends Procedure>) classes.get(className);
        if(newProcedureClass != null){
            Constructor<? extends mkdb.Procedure> constructor = newProcedureClass.getConstructor(Procedure.class);
            return constructor.newInstance(oldProc);
        }
        return null;
    }
    
    public static Protocol getHotdeployProtocol(String className, Protocol oldProt) throws Exception{
    	@SuppressWarnings("unchecked")
		Class<? extends mkio.Protocol> newProtocolClass = (Class<? extends Protocol>) classes.get(className);
    	if(newProtocolClass != null){
    		Constructor<? extends mkio.Protocol> constructor = newProtocolClass.getConstructor(Protocol.class);
    		return constructor.newInstance(oldProt);
    	}
    	return null;
    }
    
    public static boolean hasNewProcedureClass(){
    	return hasNewProcedureClass;
    }
    public static boolean hasNewProtocolClass(){
    	return hasNewProtocolClass;
    }
}
