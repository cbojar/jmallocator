package net.cbojar.mallocator;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class FixedHeapSpaceTest {
	@Test
	public void shouldAllocateAFixedHeapSpace() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(2);

		assertThat(heapSpace.totalBytes(), is(2));
	}

	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllocateANegativeSizedFixedHeapSpace() {
		FixedHeapSpace.allocate(-1);
	}

	@Test
	public void shouldAllocateAZeroedFixedHeapSpace() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(2);

		assertThat(heapSpace.get(0), is((byte)0));
		assertThat(heapSpace.get(1), is((byte)0));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void shouldNotReadANegativeIndex() {
		FixedHeapSpace.allocate(2).get(-1);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void shouldNotReadAnIndexPastTheEnd() {
		FixedHeapSpace.allocate(2).get(2);
	}

	@Test
	public void shouldStoreValuesInHeapSpace() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(2);

		heapSpace.set(0, (byte)127);

		assertThat(heapSpace.get(0), is((byte)127));
		assertThat(heapSpace.get(1), is((byte)0));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void shouldNotStoreANegativeIndex() {
		FixedHeapSpace.allocate(2).set(-1, (byte)0);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void shouldNotStoreAnIndexPastTheEnd() {
		FixedHeapSpace.allocate(2).set(2, (byte)0);
	}

	@Test
	public void shouldNotResizedAFixedHeap() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(2);

		assertThat(heapSpace.resize(1), is(2));
		assertThat(heapSpace.totalBytes(), is(2));
	}

	@Test
	public void shouldReadAsAnInt() {
		final HeapSpace heapSpace = FixedHeapSpace.allocate(2);

		heapSpace.set(0, (byte)-1);

		assertThat(heapSpace.getAsInt(0), is(255));
	}
}
