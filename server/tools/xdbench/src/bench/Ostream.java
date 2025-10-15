package bench;

import com.goldhuman.Common.Marshal.OctetsStream;

public class Ostream {
	public static void main(String args[]) throws Exception {
		final int count = 40000000;

		{
			System.out.print("OctetsStream: ");
			xdb.util.Elapse elapse = new xdb.util.Elapse();
			OctetsStream os = new OctetsStream();
			os.reserve(4 * count);
			for (int i = 0; i < count; ++i)
				os.marshal(i);
			System.out.println(elapse.elapsedAndReset());
		}
		{
			System.out.print("ByteBuffer: ");
			xdb.util.Elapse elapse = new xdb.util.Elapse();
			java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocateDirect(4 * count);
			for (int i = 0; i < count; ++i)
				bb.putInt(i);
			System.out.println(elapse.elapsedAndReset());
		}
	}
}
