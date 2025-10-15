package com.pwrd.console.command;

import java.util.List;
import java.util.Map;

import com.pwrd.command.Command;
import com.pwrd.io.IO;
import com.pwrd.xql.XQLExecutor;

public class XQLCommand implements Command
{

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(String commandStr)
	{
		XQLExecutor executor = new XQLExecutor(commandStr);
		if(executor.execute())
		{
			
			switch(executor.getOperationType())
			{
			case XQL_OPERATION_SELECT:
			{
				List<Map.Entry> result = executor.getResultList();
				IO.getInstance().println("���β�ѯ������" + result.size() + "����¼");
				for(Map.Entry entry : result)
					IO.getInstance().print(entry.toString() + "\n");
				break;
			}
			case XQL_OPERATION_UPDATE:
				IO.getInstance().print("���ι���" + executor.getCount() + "����¼������\n");
			case XQL_OPERATION_DELETE:
				IO.getInstance().print("���ι���" + executor.getCount() + "����¼��ɾ��\n");
				break;
			}
		}
	}

}
