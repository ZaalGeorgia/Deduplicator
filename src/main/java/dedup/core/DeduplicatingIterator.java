package dedup.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

final class DeduplicatingIterator<D extends Comparable<D>> implements Iterator<D> {
	private final Iterator<D> sortedSequenceIterator;
	D prev = null;
	D next = null;
	D nextnext = null;
	private Consumer<List<D>> duplicatesProcessor;
	private List<D> lookup = new LinkedList<>();

	DeduplicatingIterator(Iterator<D> sortedSequenceIterator) {
		this(sortedSequenceIterator, l -> {
		});
	}

	DeduplicatingIterator(Iterator<D> sortedSequenceIterator, Consumer<List<D>> duplicatesProcessor) {
		this.sortedSequenceIterator = sortedSequenceIterator;
		this.duplicatesProcessor = duplicatesProcessor;
	}

	@Override
	public boolean hasNext() {
		if (next != null)
			return true;
		if (lookup.isEmpty() && !sortedSequenceIterator.hasNext()) {
			return false;
		}
		D h = null;
		if (lookup.isEmpty()) {
			lookup.add(sortedSequenceIterator.next());
		}
		h = lookup.get(0);
		D t = null;
		while(sortedSequenceIterator.hasNext()) {
			t = sortedSequenceIterator.next();
			lookup.add(t);
			if (h != t) {
				break;
			}
		} 
		next = h;
		List<D> duplicates = lookup.isEmpty()? List.of(h): lookup.stream().filter(d -> d.equals(next)).toList();
		lookup.removeAll(duplicates);
		duplicatesProcessor.accept(duplicates);
		return true;
	}

	@Override
	public D next() {
		if (!hasNext()) {
			throw new IllegalStateException("no next element");
		}
		if (prev != null && prev.compareTo(next) > 0) {
			throw new IllegalStateException("source not sorted");
		}
		prev = next;
		next = null;
		return prev;
	}
}