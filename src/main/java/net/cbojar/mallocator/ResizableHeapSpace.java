package net.cbojar.mallocator;

public final class ResizableHeapSpace implements HeapSpace {
	private HeapSpace heapSpace;

	private ResizableHeapSpace(final HeapSpace heapSpace) {
		this.heapSpace = heapSpace;
	}

	public static HeapSpace allocate(final int byteCount) {
		if(byteCount < 0) {
			throw new IllegalArgumentException("Cannot allocate a negative number of bytes");
		}

		return new ResizableHeapSpace(FixedHeapSpace.allocate(byteCount));
	}

	@Override
	public int totalBytes() {
		return heapSpace.totalBytes();
	}

	@Override
	public byte get(final int index) {
		return heapSpace.get(index);
	}

	@Override
	public int getAsInt(final int index) {
		return heapSpace.getAsInt(index);
	}

	@Override
	public void set(int index, byte value) {
		heapSpace.set(index, value);
	}

	@Override
	public int resize(final int newByteCount) {
		heapSpace = HeapSpace.copy(heapSpace, FixedHeapSpace.allocate(newByteCount));

		return newByteCount;
	}

	@Override
	public String toString() {
		return heapSpace.toString();
	}
}
