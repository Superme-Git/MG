
import com.locojoy.IO.*;
import com.locojoy.base.*;
import com.locojoy.IO.Protocol.*;
import com.locojoy.base.Security.*;

public class Client
{
	public static void main(String[] args)
	{
		try
		{
			// Conf.GetInstance(Server.class.getResource("/io.conf"));
			Conf.GetInstance(args[0]);
			Manager manager = new ClientManager();
			Protocol.Client(manager);
			ThreadPool.AddTask(PollIO.GetTask());

			for (;;)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (Exception e)
				{
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
