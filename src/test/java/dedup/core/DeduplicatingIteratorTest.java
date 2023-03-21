package dedup.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

public class DeduplicatingIteratorTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testEmptyListHasNoElements() {
		Iterator<String> it = new DeduplicatingIterator<String>(Collections.EMPTY_LIST.iterator());
		assertFalse(it.hasNext());
	}

	@SuppressWarnings("unchecked")
	@Test(expected = RuntimeException.class)
	public void testEmptyListIsNotAllowed() {
		Iterator<String> it = new DeduplicatingIterator<String>(Collections.EMPTY_LIST.iterator());
		it.hasNext();
		it.next();
	}
	
	@Test
	public void testNonSortedSourceIsNotAllowed() {
		var source = List.of("A", "D", "B");
		Iterator<String> it = new DeduplicatingIterator<String>(source.iterator());
		it.hasNext();
		it.next();
	}
	
	@Test
	public void testNoDuplicatesForOneElementInSource() {
		var source = List.of("A");
		Consumer<List<String>> duplicatesProcessor = l -> {
			assertEquals(1, l.size());
		};
		Iterator<String> it = new DeduplicatingIterator<String>(source.iterator(), duplicatesProcessor);
		it.hasNext();
		it.next();
		assertFalse(it.hasNext());
	}
	
	@Test
	public void testNoDuplicatesForUniqueElementsInSource() {
		var source = List.of("A", "B", "C");
		Consumer<List<String>> duplicatesProcessor = l -> {
			assertEquals(1, l.size());
		};
		Iterator<String> it = new DeduplicatingIterator<String>(source.iterator(), duplicatesProcessor);
		while (it.hasNext())
			it.next();
	}
	
	@Test
	public void testDetectsSimpleDuplicates() {
		var source = List.of("A", "A");
		Consumer<List<String>> duplicatesProcessor = l -> {
			assertEquals(2, l.size());
		};
		Iterator<String> it = new DeduplicatingIterator<String>(source.iterator(), duplicatesProcessor);
		it.hasNext();
		it.next();
		assertFalse(it.hasNext());
	}
	
	@Test
	public void testDetectsFirstDuplicates() {
		var source = List.of("A", "A", "B", "B", "B", "C", "C", "C", "C");
		Consumer<List<String>> duplicatesProcessor = l -> {
			assertTrue(l.size() > 1);
		};
		Iterator<String> it = new DeduplicatingIterator<String>(source.iterator(), duplicatesProcessor);
		it.hasNext();
		it.next();
		assertTrue(it.hasNext());
	}
	
	@Test
	public void testDetectsAllDuplicates() {
		var source = List.of("A", "A", "B", "B", "B", "C", "C", "C", "C");
		List<Integer> sizes = new ArrayList<>();
		Consumer<List<String>> duplicatesProcessor = l -> {
			assertTrue(l.size() > 1);
			sizes.add(l.size());
		};
		Iterator<String> it = new DeduplicatingIterator<String>(source.iterator(), duplicatesProcessor);
		while (it.hasNext())
			it.next();
		assertTrue(sizes.get(0) == 2);
		assertTrue(sizes.get(1) == 3);
		assertTrue(sizes.get(2) == 4);
	}
	
}
