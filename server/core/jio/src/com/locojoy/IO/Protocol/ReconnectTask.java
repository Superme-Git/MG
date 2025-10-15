package com.locojoy.IO.Protocol;

import com.locojoy.base.Runnable;

public class ReconnectTask extends Runnable
{
	public Manager manager;

    public ReconnectTask(Manager m,int priority)
	{
		super(priority);
		manager = m;
	}

    public void run()
    {
        Protocol.Client(manager);  //reconnect
    }
}
