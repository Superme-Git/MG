import java.io.IOException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXTest {
    public static void main(String[] args) throws IOException {
    	
    			System.out.println( "connect to \"" + args[0] + "\"");
            JMXServiceURL url = new JMXServiceURL(args[0]);
            JMXConnector c = JMXConnectorFactory.connect(url, null);
            
            c.close();
            
            System.out.println( "succeed!");
    }
}
