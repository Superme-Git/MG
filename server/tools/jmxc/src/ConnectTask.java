import java.util.Map;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

class ConnectTask extends Thread
{
  private JMXConnector connector;
  private JMXServiceURL address;
  private Map<String, String[]> props;

  public ConnectTask(JMXServiceURL paramJMXServiceURL, Map<String, String[]> paramMap)
  {
    this.address = paramJMXServiceURL;
    this.props = paramMap;
  }

  public void run()
  {
    try
    {
      this.connector = JMXConnectorFactory.connect(this.address, this.props);
    }
    catch (Exception localException)
    {
    }
  }

  public JMXConnector getConnector()
  {
    return this.connector;
  }
}