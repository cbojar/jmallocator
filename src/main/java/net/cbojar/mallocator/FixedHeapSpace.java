package net.cbojar.mallocator;

import java.util.*;

public final class FixedHeapSpace implements HeapSpace {
	private final byte[] heapSpace;

	private FixedHeapSpace(final int byteCount) {
		this.heapSpace = new byte[byteCount];
	}

	public static HeapSpace allocate(final int byteCount) {
		if(byteCount < 0) {
			throw new IllegalArgumentException("Cannot allocate a negative number of bytes");
		}

		return new FixedHeapSpace(byteCount);
	}

	@Override
	public int totalBytes() {
		return heapSpace.length;
	}

	@Override
	public byte get(final int index) {
		checkIndex(index);
		return heapSpace[index];
	}

	@Override
	public int getAsInt(final int index) {
		return get(index) & 0xFF;
	}

	@Override
	public void set(int index, byte value) {
		checkIndex(index);
		heapSpace[index] = value;
	}

	private void checkIndex(final int index) {
		if(index < 0 || index >= totalBytes()) {
			throw new IndexOutOfBoundsException(String.format("Index %d is out of range", Integer.valueOf(index)));
		}
	}

	@Override
	public int resize(final int newByteCount) {
		return totalBytes();
	}

	@Override
	public String toString() {
		final List<String> byteStrings = new ArrayList<>();

		for(byte b : heapSpace) {
			byteStrings.add(String.format("0x%02X ", b));
		}

		return new StringBuilder()
				.append("[")
				.append(String.join(" ", byteStrings))
				.append("]")
				.toString();
	}
}
