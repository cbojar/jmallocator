package net.cbojar.mallocator;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class ResizableHeapSpaceTest {
	@Test
	public void shouldAllocateAFixedHeapSpace() {
		final HeapSpace heapSpace = ResizableHeapSpace.allocate(2);

		assertThat(heapSpace.totalBytes(), is(2));
	}

	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllocateANegativeSizedFixedHeapSpace() {
		ResizableHeapSpace.allocate(-1);
	}

	@Test
	public void shouldAllocateAZeroedFixedHeapSpace() {
		final HeapSpace heapSpace = ResizableHeapSpace.allocate(2);

		assertThat(heapSpace.get(0), is((byte)0));
		assertThat(heapSpace.get(1), is((byte)0));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void shouldNotReadANegativeIndex() {
		ResizableHeapSpace.allocate(2).get(-1);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void shouldNotReadAnIndexPastTheEnd() {
		ResizableHeapSpace.allocate(2).get(2);
	}

	@Test
	public void shouldStoreValuesInHeapSpace() {
		final HeapSpace heapSpace = ResizableHeapSpace.allocate(2);

		heapSpace.set(0, (byte)127);

		assertThat(heapSpace.get(0), is((byte)127));
		assertThat(heapSpace.get(1), is((byte)0));
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void shouldNotStoreANegativeIndex() {
		ResizableHeapSpace.allocate(2).set(-1, (byte)0);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void shouldNotStoreAnIndexPastTheEnd() {
		ResizableHeapSpace.allocate(2).set(2, (byte)0);
	}

	@Test
	public void shouldResizeTheHeapSmaller() {
		final HeapSpace heapSpace = ResizableHeapSpace.allocate(2);

		heapSpace.set(0, (byte)1);
		heapSpace.set(1, (byte)2);

		assertThat(heapSpace.resize(1), is(1));
		assertThat(heapSpace.totalBytes(), is(1));
		assertThat(heapSpace.get(0), is((byte)1));
	}

	@Test
	public void shouldResizeTheHeapLarger() {
		final HeapSpace heapSpace = ResizableHeapSpace.allocate(2);

		heapSpace.set(0, (byte)1);
		heapSpace.set(1, (byte)2);

		assertThat(heapSpace.resize(4), is(4));
		assertThat(heapSpace.totalBytes(), is(4));
		assertThat(heapSpace.get(0), is((byte)1));
		assertThat(heapSpace.get(1), is((byte)2));
		assertThat(heapSpace.get(2), is((byte)0));
		assertThat(heapSpace.get(3), is((byte)0));
	}

	@Test
	public void shouldReadAsAnInt() {
		final HeapSpace heapSpace = ResizableHeapSpace.allocate(2);

		heapSpace.set(0, (byte)-1);

		assertThat(heapSpace.getAsInt(0), is(255));
	}
}
