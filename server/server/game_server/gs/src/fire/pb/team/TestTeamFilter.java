package fire.pb.team;

public class TestTeamFilter extends TeamFilter
{
	
	@Override
	public boolean checkActive(long leaderId)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean checkEnterTeam(long teamId, long roleId)
	{
		return false;
	}
}
