package fire.pb.callback;

/**
 * @author dc
 * 一些网络字节大小端转换的工具函数
 */
public class BitConverter {

	public static byte[] shortToHostBytes(final short integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer
				: integer)) / 8;
		byte[] byteArray = new byte[2];

		for (int n = 0; n < byteNum; n++)
			byteArray[n] = (byte) (integer >>> (n * 8));

		return byteArray;
	}

	public static byte[] intToHostBytes(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer
				: integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[n] = (byte) (integer >>> (n * 8));

		return byteArray;
	}

	public static short hostBytesToShort(final byte[] b) {
		int targets = (b[0] & 0xff) | ((b[1] << 8) & 0xff00);
		return (short) targets;
	}

	public static short hostBytesToShort(final byte[] b, int offset) {
		int targets = (b[offset] & 0xff) | ((b[offset + 1] << 8) & 0xff00);
		return (short) targets;
	}

	public static int hostBytesToInt(final byte[] b) {
		int targets = 0;
		for (int i = 0; i < 4; i++) {
			targets += (b[i] & 0xFF) << (8 * i);
		}
		return targets;
	}

	public static int hostBytesToInt(final byte[] b, int offset) {
		int targets = 0;
		for (int i = 0; i < 4; i++) {
			targets += (b[i + offset] & 0xFF) << (8 * i);
		}
		return targets;
	}

	public static byte[] shortToNetworkBytes(final short s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	public static byte[] intToNetworkBytes(final int s) {
		byte[] targets = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	public static short networkBytesToShort(final byte[] b) {
		int s = 0;
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		s = s * 256;
		if (b[1] >= 0) {
			s = s + b[1];
		} else {
			s = s + 256 + b[1];
		}
		return (short) s;
	}
	
	public static short networkBytesToShort(final byte[] b, int offset) {
		int s = 0;
		if (b[offset] >= 0) {
			s = s + b[offset];
		} else {
			s = s + 256 + b[offset];
		}
		s = s * 256;
		if (b[offset + 1] >= 0) {
			s = s + b[offset + 1];
		} else {
			s = s + 256 + b[offset + 1];
		}
		return (short) s;
	}

	public static int networkBytesToInt(final byte[] b) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[i] >= 0) {
				s = s + b[i];
			} else {
				s = s + 256 + b[i];
			}
			s = s * 256;
		}
		if (b[3] >= 0) {
			s = s + b[3];
		} else {
			s = s + 256 + b[3];
		}
		return s;
	}

	public static int networkBytesToInt(final byte[] b, int offset) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[i + offset] >= 0) {
				s = s + b[i + offset];
			} else {
				s = s + 256 + b[i + offset];
			}
			s = s * 256;
		}
		if (b[3 + offset] >= 0) {
			s = s + b[3 + offset];
		} else {
			s = s + 256 + b[3 + offset];
		}
		return s;
	}
}
