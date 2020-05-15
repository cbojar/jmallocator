package net.cbojar.mallocator;

public interface HeapSpace {
	public static HeapSpace copy(final HeapSpace from, final HeapSpace to) {
			final int bytesToCopy = Math.min(from.totalBytes(), to.totalBytes());

			for (int i = 0; i < bytesToCopy; i++) {
				to.set(i, from.get(i));
			}

			return to;
	}

	int totalBytes();

	byte get(int index);

	int getAsInt(int index);

	void set(int index, byte value);

	int resize(int newByteCount);
}
