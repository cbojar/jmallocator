package net.cbojar.mallocator;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;

public class MemoryAllocatorTest {
	@Test
	public void shouldCreateAMemoryAllocatorForTheHeapSpace() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(8);
		heapSpace.set(3, (byte)4);
		heapSpace.set(7, (byte)1);

		final MemoryAllocator allocator = new MemoryAllocator(heapSpace);

		assertThat(allocator.readInt(4), is(1));
	}

	@Test
	public void shouldAllocateByteSpaceOnTheHeap() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(8);
		final MemoryAllocator allocator = new MemoryAllocator(heapSpace);

		final int pointer = allocator.allocateByte((byte)0x01);

		assertThat(heapSpace.get(pointer - 4), is((byte)0));
		assertThat(heapSpace.get(pointer - 3), is((byte)0));
		assertThat(heapSpace.get(pointer - 2), is((byte)0));
		assertThat(heapSpace.get(pointer - 1), is((byte)1));
		assertThat(heapSpace.get(pointer), is((byte)1));

		assertThat(allocator.readByte(pointer), is((byte)0x01));
	}

	@Test
	public void shouldAllocateCharSpaceOnTheHeap() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(8);
		final MemoryAllocator allocator = new MemoryAllocator(heapSpace);

		final int pointer = allocator.allocateChar('A');

		assertThat(heapSpace.get(pointer - 4), is((byte)0));
		assertThat(heapSpace.get(pointer - 3), is((byte)0));
		assertThat(heapSpace.get(pointer - 2), is((byte)0));
		assertThat(heapSpace.get(pointer - 1), is((byte)2));
		assertThat(heapSpace.get(pointer), is((byte)0));
		assertThat(heapSpace.get(pointer + 1), is((byte)65));

		assertThat(allocator.readChar(pointer), is('A'));
	}

	@Test
	public void shouldAllocateShortSpaceOnTheHeap() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(8);
		final MemoryAllocator allocator = new MemoryAllocator(heapSpace);

		final int pointer = allocator.allocateShort((short)0x0102);

		assertThat(heapSpace.get(pointer - 4), is((byte)0));
		assertThat(heapSpace.get(pointer - 3), is((byte)0));
		assertThat(heapSpace.get(pointer - 2), is((byte)0));
		assertThat(heapSpace.get(pointer - 1), is((byte)2));
		assertThat(heapSpace.get(pointer), is((byte)1));
		assertThat(heapSpace.get(pointer + 1), is((byte)2));

		assertThat(allocator.readShort(pointer), is((short)0x0102));
	}

	@Test
	public void shouldAllocateIntSpaceOnTheHeap() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(8);
		final MemoryAllocator allocator = new MemoryAllocator(heapSpace);

		final int pointer = allocator.allocateInt(0x01020304);

		assertThat(heapSpace.get(pointer - 4), is((byte)0));
		assertThat(heapSpace.get(pointer - 3), is((byte)0));
		assertThat(heapSpace.get(pointer - 2), is((byte)0));
		assertThat(heapSpace.get(pointer - 1), is((byte)4));
		assertThat(heapSpace.get(pointer), is((byte)1));
		assertThat(heapSpace.get(pointer + 1), is((byte)2));
		assertThat(heapSpace.get(pointer + 2), is((byte)3));
		assertThat(heapSpace.get(pointer + 3), is((byte)4));

		assertThat(allocator.readInt(pointer), is(0x01020304));
	}

	@Test
	public void shouldAllocateLongSpaceOnTheHeap() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(12);
		final MemoryAllocator allocator = new MemoryAllocator(heapSpace);

		final int pointer = allocator.allocateLong(0x0102030405060708L);

		assertThat(heapSpace.get(pointer - 4), is((byte)0));
		assertThat(heapSpace.get(pointer - 3), is((byte)0));
		assertThat(heapSpace.get(pointer - 2), is((byte)0));
		assertThat(heapSpace.get(pointer - 1), is((byte)8));
		assertThat(heapSpace.get(pointer), is((byte)1));
		assertThat(heapSpace.get(pointer + 1), is((byte)2));
		assertThat(heapSpace.get(pointer + 2), is((byte)3));
		assertThat(heapSpace.get(pointer + 3), is((byte)4));
		assertThat(heapSpace.get(pointer + 4), is((byte)5));
		assertThat(heapSpace.get(pointer + 5), is((byte)6));
		assertThat(heapSpace.get(pointer + 6), is((byte)7));
		assertThat(heapSpace.get(pointer + 7), is((byte)8));

		assertThat(allocator.readLong(pointer), is(0x0102030405060708L));
	}

	@Test
	public void shouldZeroFreeSpaceOnTheHeap() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(8);
		final MemoryAllocator allocator = new MemoryAllocator(heapSpace);

		final int pointer = allocator.allocateInt(0x01020304);
		allocator.free(pointer);

		assertThat(heapSpace.get(pointer - 4), is((byte)0));
		assertThat(heapSpace.get(pointer - 3), is((byte)0));
		assertThat(heapSpace.get(pointer - 2), is((byte)0));
		assertThat(heapSpace.get(pointer - 1), is((byte)0));
		assertThat(heapSpace.get(pointer), is((byte)0));
		assertThat(heapSpace.get(pointer + 1), is((byte)0));
		assertThat(heapSpace.get(pointer + 2), is((byte)0));
		assertThat(heapSpace.get(pointer + 3), is((byte)0));
	}

	@Test
	public void shouldReallocateSpaceOnTheHeap() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(8);
		final MemoryAllocator allocator = new MemoryAllocator(heapSpace);

		final int pointer = allocator.allocateInt(0x01020304);
		allocator.free(pointer);
		allocator.allocateInt(0x04030201);

		assertThat(heapSpace.get(pointer - 4), is((byte)0));
		assertThat(heapSpace.get(pointer - 3), is((byte)0));
		assertThat(heapSpace.get(pointer - 2), is((byte)0));
		assertThat(heapSpace.get(pointer - 1), is((byte)4));
		assertThat(heapSpace.get(pointer), is((byte)4));
		assertThat(heapSpace.get(pointer + 1), is((byte)3));
		assertThat(heapSpace.get(pointer + 2), is((byte)2));
		assertThat(heapSpace.get(pointer + 3), is((byte)1));

		assertThat(allocator.readInt(pointer), is(0x04030201));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void shouldNotReallocateUnfreeSpaceOnTheHeap() {
		final MemoryAllocator allocator = new MemoryAllocator(FixedHeapSpace.allocate(8));

		allocator.allocateInt(0x01020304);
		allocator.allocateInt(0x04030201);
	}

	@Test
	public void shouldResizeTheHeap() {
		final HeapSpace heapSpace = ResizableHeapSpace.allocate(8);
		final MemoryAllocator allocator = new MemoryAllocator(heapSpace);

		final int pointer1 = allocator.allocateInt(0x01020304);
		final int pointer2 = allocator.allocateInt();

		assertThat(pointer2, is(not(pointer1)));
		assertThat(pointer2, is(MemoryAllocator.NULLPTR));

		allocator.requestAdditionalSpace();

		assertThat(heapSpace.totalBytes(), is(16));

		final int pointer3 = allocator.allocateInt(0x04030201);

		assertThat(pointer3, is(not(pointer1)));
		assertThat(pointer3, is(not(MemoryAllocator.NULLPTR)));

		assertThat(allocator.readInt(pointer1), is(0x01020304));
		assertThat(allocator.readInt(pointer3), is(0x04030201));
	}
}
