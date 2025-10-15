import com.locojoy.IO.Protocol.Protocol;

public class Generator
{
	public static void main(String[] args)
	{
		try
		{
			Class.forName( "com.locojoy.IO.Protocol.Protocol" );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
