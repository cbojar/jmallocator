package net.cbojar.mallocator;

public class MemoryAllocator {
	public static final int NULLPTR = -1;

	private final HeapSpace heap;

	public MemoryAllocator(final HeapSpace heap) {
		this.heap = heap;
	}

	public int requestAdditionalSpace() {
		return heap.resize(heap.totalBytes() * 2);
	}

	public int allocate(final int size) {
		final int pointer = findNextFree(size);

		if (pointer == NULLPTR) {
			return NULLPTR;
		}

		heap.set(pointer - 4, (byte)(size >> 24));
		heap.set(pointer - 3, (byte)(size >> 16));
		heap.set(pointer - 2, (byte)(size >> 8));
		heap.set(pointer - 1, (byte)size);

		return pointer;
	}

	private int findNextFree(final int size) {
		final int heapSize = heap.totalBytes();
		int currentPointer = 4;

		while(currentPointer < heapSize) {
			int allocatedSize = readSize(currentPointer);

			if (allocatedSize != 0) {
				currentPointer = currentPointer + allocatedSize + 4;
				continue;
			}

			if (hasSpace(currentPointer, size)) {
				return currentPointer;
			}
		}

		return NULLPTR;
	}

	private boolean hasSpace(final int pointer, final int size) {
		for (int i = 0; i < size; i++) {
			if (heap.getAsInt(pointer + i) != 0) {
				return false;
			}
		}

		return true;
	}

	public int allocateByte() {
		return allocate(1);
	}

	public int allocateByte(final byte value) {
		final int pointer = allocateByte();
		setByte(pointer, value);
		return pointer;
	}

	public void setByte(final int pointer, final byte value) {
		heap.set(pointer, (byte)value);
	}

	public byte readByte(final int pointer) {
		return heap.get(pointer);
	}

	public int allocateChar() {
		return allocate(2);
	}

	public int allocateChar(final char value) {
		final int pointer = allocateChar();
		setChar(pointer, value);
		return pointer;
	}

	public void setChar(final int pointer, final char value) {
		heap.set(pointer, (byte)(value >> 8));
		heap.set(pointer + 1, (byte)value);
	}

	public char readChar(final int pointer) {
		return (char)((heap.getAsInt(pointer) << 8) +
				heap.getAsInt(pointer + 1));
	}

	public int allocateShort() {
		return allocate(2);
	}

	public int allocateShort(final short value) {
		final int pointer = allocateShort();
		setShort(pointer, value);
		return pointer;
	}

	public void setShort(final int pointer, final short value) {
		heap.set(pointer, (byte)(value >> 8));
		heap.set(pointer + 1, (byte)value);
	}

	public short readShort(final int pointer) {
		return (short)((heap.getAsInt(pointer) << 8) +
				heap.getAsInt(pointer + 1));
	}

	public int allocateInt() {
		return allocate(4);
	}

	public int allocateInt(final int value) {
		final int pointer = allocateInt();
		setInt(pointer, value);
		return pointer;
	}

	public void setInt(final int pointer, final int value) {
		heap.set(pointer, (byte)(value >> 24));
		heap.set(pointer + 1, (byte)(value >> 16));
		heap.set(pointer + 2, (byte)(value >> 8));
		heap.set(pointer + 3, (byte)value);
	}

	public int readInt(final int pointer) {
		return (heap.getAsInt(pointer) << 24) +
				(heap.getAsInt(pointer + 1) << 16) + 
				(heap.getAsInt(pointer + 2) << 8) + 
				heap.getAsInt(pointer + 3);
	}

	public int allocateLong() {
		return allocate(8);
	}

	public int allocateLong(final long value) {
		final int pointer = allocateLong();
		setLong(pointer, value);
		return pointer;
	}

	public void setLong(final int pointer, final long value) {
		heap.set(pointer, (byte)(value >> 56));
		heap.set(pointer + 1, (byte)(value >> 48));
		heap.set(pointer + 2, (byte)(value >> 40));
		heap.set(pointer + 3, (byte)(value >> 32));
		heap.set(pointer + 4, (byte)(value >> 24));
		heap.set(pointer + 5, (byte)(value >> 16));
		heap.set(pointer + 6, (byte)(value >> 8));
		heap.set(pointer + 7, (byte)value);
	}

	public long readLong(final int pointer) {
		long higherBits = (heap.getAsInt(pointer) << 24) +
				(heap.getAsInt(pointer + 1) << 16) + 
				(heap.getAsInt(pointer + 2) << 8) + 
				heap.getAsInt(pointer + 3);

		long lowerBits = (heap.getAsInt(pointer + 4) << 24) +
				(heap.getAsInt(pointer + 5) << 16) + 
				(heap.getAsInt(pointer + 6) << 8) + 
				heap.getAsInt(pointer + 7);

		return (higherBits << 32) | lowerBits;
	}

	public void free(final int pointer) {
		final int size = readSize(pointer);

		for (int i = -4; i < size; i++) {
			heap.set(pointer + i, (byte)0);
		}
	}

	private int readSize(final int pointer) {
		return (heap.getAsInt(pointer - 4) << 24) +
				(heap.getAsInt(pointer - 3) << 16) + 
				(heap.getAsInt(pointer - 2) << 8) + 
				heap.getAsInt(pointer - 1);
	}
}
