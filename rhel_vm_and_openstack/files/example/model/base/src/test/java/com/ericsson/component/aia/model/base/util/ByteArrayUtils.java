package com.ericsson.component.aia.model.base.util;

public class ByteArrayUtils {
	
	public byte[] concatenateByteArrays(final byte[] oneBytes, final byte[] zeroBytes, final byte[] twoBytes) {
		final byte[] allBytesTogether = new byte[oneBytes.length + zeroBytes.length + twoBytes.length];
		System.arraycopy(oneBytes, 0, allBytesTogether, 0, oneBytes.length);
		System.arraycopy(zeroBytes, 0, allBytesTogether, oneBytes.length, zeroBytes.length);
		System.arraycopy(twoBytes, 0, allBytesTogether, zeroBytes.length + oneBytes.length, twoBytes.length);
		return allBytesTogether;
	}

}
