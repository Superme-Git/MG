
package fire.pb;

import mkdb.Procedure;
import mkdb.Transaction;
import mkdb.Procedure.Task;

public class GsClient {
	
	public static class SendToScene extends Task {
		private mkio.Protocol p;

		public SendToScene( mkio.Protocol p) {
			this.p = p;
		}


		@Override
		public void run() 
		{
			sendToScene(p);
		}
	}

	public static void sendToScene(mkio.Protocol p)
	{
		fire.pb.scene.MapThread.getInstance().add(p);
	}
	
	public static void tSendWhileCommit(mkio.Protocol p)
	{
		if(null != Transaction.current())
			Transaction.tsendWhileCommit(fire.pb.scene.MapThread.serialVersionUID, p);//.texecuteWhileCommit(new SendToScene(p));
		else
			fire.pb.scene.MapThread.getInstance().add(p);//FIXME 将来要去掉，这里先发出去
	}
	
	public static void pSendWhileCommit(mkio.Protocol p)
	{
		if(null != Transaction.current())
			Procedure.psendWhileCommit(fire.pb.scene.MapThread.serialVersionUID, p);//.ppostWhileCommit(new SendToScene(p));
		else
			fire.pb.scene.MapThread.getInstance().add(p);//FIXME 将来要去掉，这里先发出去
	}
}

