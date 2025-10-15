import java.io.IOException;
import java.net.SocketTimeoutException;
import java.rmi.ConnectException;
import java.rmi.UnmarshalException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.jmxservice.mt3interfaces.GameOnlineNumBean;
import com.jmxservice.mt3interfaces.LogInfo;

public class jmxc
{
  private JMXConnector connector;
  private MBeanServerConnection mbsc;
  private static int timeout = 7000;
  private static TimeUnit unit = TimeUnit.MILLISECONDS;
  
  
  private static final ThreadFactory daemonThreadFactory = new jmxc.DaemonThreadFactory();

  public jmxc(String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    String[] arrayOfString = { paramString1, paramString2 };
    HashMap localHashMap = new HashMap();
    localHashMap.put("jmx.remote.credentials", arrayOfString);
    JMXServiceURL localJMXServiceURL = new JMXServiceURL(paramString3);
    this.connector = connectWithTimeout(localJMXServiceURL, localHashMap);
    this.mbsc = this.connector.getMBeanServerConnection();
  }

  public void close()
    throws IOException
  {
    if (null == this.connector)
      return;
    this.connector.close();
  }

  public static void echo(Object paramObject)
  {
    System.out.println(paramObject);
  }

  public void printAttributes(String paramString, String[] paramArrayOfString)
    throws Exception
  {
    ObjectName localObjectName = new ObjectName(paramString);
    AttributeList localAttributeList = this.mbsc.getAttributes(localObjectName, paramArrayOfString);
    int i = localAttributeList.size();
    for (int j = 0; j < i; ++j)
    {
      Attribute localAttribute = (Attribute)localAttributeList.get(j);
      echo(new StringBuilder().append(localAttribute.getValue()).append("\t").append(localAttribute.getName()).toString());
    }
  }

  public Object invoke(String paramString1, String paramString2, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws Exception
  {
    ObjectName localObjectName = new ObjectName(paramString1);
    return this.mbsc.invoke(localObjectName, paramString2, paramArrayOfObject, paramArrayOfString);
  }

  public void test2()
    throws Exception
  {
    String str = "t1";
    LogInfo localLogInfo = new LogInfo();
    System.out.println(invoke("IWEB:type=RoleControl", "getRoleIdByName", new Object[] { str, localLogInfo }, new String[] { "java.lang.String", "com.jmxservice.mt3interfaces.LogInfo" }));
  }

  public void test()
    throws Exception
  {
    long l = 4096L;
    LogInfo localLogInfo = new LogInfo();
    System.out.println(invoke("IWEB:type=RoleControl", "roleid2Uid", new Object[] { Long.valueOf(l), localLogInfo }, new String[] { "long", "com.jmxservice.mt3interfaces.LogInfo" }));
  }

  public void shutdownGs(Integer paramInteger)
    throws Exception
  {
    invoke(this.connector, "bean:name=stopper", "stop", new Object[] { paramInteger }, new String[] { Integer.TYPE.getName() });
  }

  public boolean keepAlive()
    throws Exception
  {
    LogInfo localLogInfo = new LogInfo(Integer.valueOf(-1), "jmxc", "call from jmxc");
    Object localObject = invokeWithTimeout(this.connector, "IWEB:type=GameControl", "keepAlive", new Object[] { localLogInfo }, new String[] { "com.jmxservice.mt3interfaces.LogInfo" });
    if (localObject == null)
      return false;
    return ((Boolean)localObject).booleanValue();
  }

  public void shutdownAuany()
    throws Exception
  {
    invokeWithTimeout(this.connector, "bean:name=stopper", "stop", new Object[] { Integer.valueOf(0) }, new String[] { Integer.TYPE.getName() });
  }

  public String bindMacToLahu()
    throws Exception
  {
    Object localObject = invokeWithTimeout(this.connector, "bean:name=bindmac", "bind", new Object[0], new String[0]);
    if (localObject == null)
      return null;
    return ((String)localObject);
  }

  public void shutdownUn()
    throws Exception
  {
    invokeWithTimeout(this.connector, "bean:name=stopper", "stop", new Object[] { Integer.valueOf(0) }, new String[] { Integer.TYPE.getName() });
  }

  public GameOnlineNumBean GetMaxOnlineNum()
    throws Exception
  {
    LogInfo localLogInfo = new LogInfo(Integer.valueOf(-1), "jmxc", "call from jmxc");
    Object localObject = invokeWithTimeout(this.connector, "IWEB:type=GameControl", "getOnlineNum", new Object[] { localLogInfo }, new String[] { "com.jmxservice.mt3interfaces.LogInfo" });
    if (localObject == null)
      return null;
    return ((GameOnlineNumBean)localObject);
  }

  public int getUserNumInQueue()
    throws Exception
  {
    LogInfo localLogInfo = new LogInfo(Integer.valueOf(-1), "jmxc", "call from jmxc");
    Object localObject = invokeWithTimeout(this.connector, "IWEB:type=GameControl", "getUserNumInQueue", new Object[] { localLogInfo }, new String[] { "com.jmxservice.mt3interfaces.LogInfo" });
    if (localObject == null)
      return -1;
    return ((Integer)localObject).intValue();
  }

  public int execGM(String[] paramArrayOfString)
    throws Exception
  {
    StringBuilder localStringBuilder = new StringBuilder();
//    localStringBuilder.append("//");
    int i = -1;
    long l = -1L;
    for (int j = 5; j < paramArrayOfString.length; ++j)
      if (paramArrayOfString[j].startsWith("userId="))
        i = Integer.valueOf(paramArrayOfString[j].substring(7)).intValue();
      else if (paramArrayOfString[j].startsWith("roleId="))
        l = Long.valueOf(paramArrayOfString[j].substring(7)).longValue();
      else
        localStringBuilder.append(paramArrayOfString[j]).append("\n");
    Object localObject = invokeWithTimeout(this.connector, "gs.counter:type=GMProcMXBeant", "execute", new Object[] { Integer.valueOf(i), Long.valueOf(l), localStringBuilder.toString() }, new String[] { Integer.class.getName(), Long.class.getName(), String.class.getName() });
    if (localObject == null)
      return -1;
    System.out.println((String)localObject);
    return 1;
  }

  public String reload()
    throws Exception
  {
    Object localObject = invokeWithTimeout(this.connector, "IWEB:type=Reload", "reload", new Object[0], new String[0]);
    if (localObject == null)
      return null;
    return ((String)localObject);
  }

  public String refreshRankList()
    throws Exception
  {
    Object localObject = invokeWithTimeout(this.connector, "gs.counter:type=RankList", "doSortRankList", new Object[0], new String[0]);
    if (localObject == null)
      return null;
    return ((String)localObject);
  }

  public String doExpState()
    throws Exception
  {
    Object localObject = invokeWithTimeout(this.connector, "gs.counter:type=ForceActiveLog", "doExpState", new Object[0], new String[0]);
    if (localObject == null)
      return null;
    return ((String)localObject);
  }

  public String getCacheInfo()
    throws Exception
  {
    Object localObject = invokeWithTimeout(this.connector, "gs.counter:type=CacheInfo", "getCacheInfo", new Object[0], new String[0]);
    if (localObject == null)
      return null;
    return ((String)localObject);
  }

  public static Object invoke(JMXConnector paramJMXConnector, String paramString1, String paramString2, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws Exception
  {
    return paramJMXConnector.getMBeanServerConnection().invoke(new ObjectName(paramString1), paramString2, paramArrayOfObject, paramArrayOfString);
  }

  public static Object invokeWithTimeout(JMXConnector paramJMXConnector, String paramString1, String paramString2, Object[] paramArrayOfObject, String[] paramArrayOfString)
    throws Exception
  {
    ExecutorService localExecutorService = Executors.newSingleThreadExecutor(daemonThreadFactory);
    Future localFuture = localExecutorService.submit(new Callable()
    {
      public Object call()
        throws Exception
      {
        try
        {
          return paramJMXConnector.getMBeanServerConnection().invoke(new ObjectName(paramString1), paramString2, paramArrayOfObject, paramArrayOfString);
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          throw localException;
        }
      }
    });
    Object localObject = null;
    try
    {
      localObject = localFuture.get(timeout, unit);
    }
    catch (Exception localException1)
    {
    }
    try
    {
      localExecutorService.shutdownNow();
    }
    catch (Exception localException2)
    {
    }
    return localObject;
  }

  public static JMXConnector connectWithTimeout(JMXServiceURL paramJMXServiceURL, Map<String, String[]> paramMap)
    throws IOException
  {
    ArrayBlockingQueue localArrayBlockingQueue = new ArrayBlockingQueue(1);
    ExecutorService localExecutorService = Executors.newSingleThreadExecutor(daemonThreadFactory);
    localExecutorService.submit(new Runnable()
    {
      public void run()
      {
        try
        {
          JMXConnector localJMXConnector = JMXConnectorFactory.connect(paramJMXServiceURL, paramMap);
          if (!(localArrayBlockingQueue.offer(localJMXConnector)))
            localJMXConnector.close();
        }
        catch (Throwable localThrowable)
        {
        	localArrayBlockingQueue.offer(localThrowable);
        }
      }
    });
    Object localObject1=null;
    try
    {
      localObject1 = localArrayBlockingQueue.poll(timeout, unit);
      if ((localObject1 == null) && (!(localArrayBlockingQueue.offer(""))))
        localObject1 = localArrayBlockingQueue.take();
    }
    catch (InterruptedException localInterruptedException)
    {
    }
    finally
    {
      localExecutorService.shutdown();
    }
    if (localObject1 == null)
      throw new SocketTimeoutException(new StringBuilder().append("Connect timed out: ").append(paramJMXServiceURL).toString());
    if (localObject1 instanceof JMXConnector)
      return ((JMXConnector)localObject1);
    try
    {
      throw ((Throwable)localObject1);
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    catch (RuntimeException localRuntimeException)
    {
      throw localRuntimeException;
    }
    catch (Error localError)
    {
      throw localError;
    }
    catch (Throwable localThrowable)
    {
      throw new IOException(localThrowable.toString());
    }
  }

  private static <T extends Throwable> T initCause(T paramT, Throwable paramThrowable)
  {
    paramT.initCause(paramThrowable);
    return paramT;
  }

  /**
   * "" "" "192.168.32.44" "1098" "gm" "userId=9845" "roleId=4097" "addgold#100"
   * @param paramArrayOfString
   * @throws Exception
   */
  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    if (paramArrayOfString.length < 5)
    {
      System.out.println("Usage:  java jmxc username password ip port function(shutdownUn|shutdownGs|keepAlive|GetMaxOnlineNum) [waitsec]");
      return;
    }
    String str1 = paramArrayOfString[0];//username
    String str2 = paramArrayOfString[1];//password
    String str3 = paramArrayOfString[2];//ip
    String str4 = paramArrayOfString[3];//port
    String str5 = paramArrayOfString[4];
    if ((!(str5.equals("shutdownGs"))) && (!(str5.equals("shutdownUn"))) && (!(str5.equals("shutdownAuany"))) && (!(str5.equals("keepAlive"))) && (!(str5.equals("GetMaxOnlineNum"))) && (!(str5.equals("getUserNumInQueue"))) && (!(str5.equals("reload"))) && (!(str5.equals("refreshRankList"))) && (!(str5.equals("doExpState"))) && (!(str5.equals("bindMacToLahu"))) && (!(str5.equals("getCacheInfo"))) && (!(str5.equals("gm"))))
    {
      System.out.println("function not found!");
      return;
    }
    jmxc localjmxc = null;
    try
    {
      int i = Integer.valueOf(str4).intValue() + 2687;
      localjmxc = new jmxc(str1, str2, new StringBuilder().append("service:jmx:rmi://").append(str3).append(":").append(i).append("/jndi/rmi://").append(str3).append(":").append(str4).append("/jmxrmi").toString());
    }
    catch (Exception localException1)
    {
      if (str5.equals("keepAlive"))
      {
        localException1.printStackTrace();
        System.out.println("0");
        return;
      }
      if (str5.equals("GetMaxOnlineNum"))
      {
        System.out.println("0");
      }
      else
      {
        System.out.println("jmxc connect error");
        throw new Exception(localException1);
      }
    }
    try
    {
      if (str5.equals("shutdownGs"))
      {
        Integer localInteger = Integer.valueOf(300);
        if (paramArrayOfString.length > 5)
          try
          {
            localInteger = Integer.valueOf(paramArrayOfString[5]);
          }
          catch (Exception localException11)
          {
          }
        localjmxc.shutdownGs(localInteger);
      }
      else if (str5.equals("shutdownUn"))
      {
        try
        {
          localjmxc.shutdownUn();
        }
        catch (UnmarshalException localUnmarshalException1)
        {
        }
        catch (ConnectException localConnectException1)
        {
        }
      }
      else if (str5.equals("shutdownAuany"))
      {
        try
        {
          localjmxc.shutdownAuany();
        }
        catch (UnmarshalException localUnmarshalException2)
        {
        }
        catch (ConnectException localConnectException2)
        {
        }
      }
      else if (str5.equals("keepAlive"))
      {
        try
        {
          boolean bool = localjmxc.keepAlive();
          if (bool)
            System.out.println("1");
          else
            System.out.println("0");
        }
        catch (Exception localException2)
        {
          System.out.println("0");
        }
      }
      else if (str5.equals("GetMaxOnlineNum"))
      {
        try
        {
          GameOnlineNumBean localGameOnlineNumBean = localjmxc.GetMaxOnlineNum();
          if (localGameOnlineNumBean != null)
            System.out.println(localGameOnlineNumBean.curronlinenum);
          else
            System.out.println("0");
        }
        catch (Exception localException3)
        {
          System.out.println("0");
        }
      }
      else if (str5.equals("bindMacToLahu"))
      {
        try
        {
          localjmxc.bindMacToLahu();
        }
        catch (Exception localException4)
        {
          System.out.println("0");
        }
      }
      else if (str5.equals("getUserNumInQueue"))
      {
        try
        {
          int j = localjmxc.getUserNumInQueue();
          System.out.println(j);
        }
        catch (Exception localException5)
        {
          System.out.println("-1");
        }
      }
      else if (str5.equals("reload"))
      {
        try
        {
          String str6 = localjmxc.reload();
          System.out.println(str6);
        }
        catch (Exception localException6)
        {
          System.out.println("0");
        }
      }
      else if (str5.equals("refreshRankList"))
      {
        try
        {
          String str7 = localjmxc.refreshRankList();
          System.out.println(str7);
        }
        catch (Exception localException7)
        {
          System.out.println("0");
        }
      }
      else if (str5.equals("doExpState"))
      {
        try
        {
          String str8 = localjmxc.doExpState();
          System.out.println(str8);
        }
        catch (Exception localException8)
        {
          System.out.println("0");
        }
      }
      else if (str5.equals("getCacheInfo"))
      {
        try
        {
          String str9 = localjmxc.getCacheInfo();
          System.out.println(str9);
        }
        catch (Exception localException9)
        {
          System.out.println("0");
        }
      }
      else if (str5.equals("gm"))
      {
        try
        {
          int k = localjmxc.execGM(paramArrayOfString);
          if (k != 1)
            System.out.println(k);
        }
        catch (Exception localException10)
        {
          System.out.println("0");
        }
      }
    }
    finally
    {
    }
  }

  private static class DaemonThreadFactory implements ThreadFactory
  {
    public Thread newThread(Runnable paramRunnable)
    {
      Thread localThread = Executors.defaultThreadFactory().newThread(paramRunnable);
      localThread.setDaemon(true);
      return localThread;
    }
  }
}