package mkdb.logs;

import mkdb.Log;
import mkdb.LogKey;

public abstract class LogString extends Note implements Log {
	protected LogKey logkey;
	protected String _xdb_saved;

	protected LogString(LogKey logkey, String saved) {
		this.logkey = logkey;
		this._xdb_saved = saved;
	}

	@Override
	public void commit() {
		mkdb.Logs.logNotify(logkey.getXBean(), new LogNotify(logkey, this));
	}
}
