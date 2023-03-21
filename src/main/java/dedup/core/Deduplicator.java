package dedup.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Consumer;

public class Deduplicator<K, D extends Comparable<D>> {

	private Storage<K, D> storage;
	private Consumer<List<D>> duplicatesPrcessor;

	public Deduplicator(Storage<K, D> storage) {
		this(storage, l -> {});
	}

	public Deduplicator(Storage<K, D> storage, Consumer<List<D>> duplicatesPrcessor) {
		this.storage = storage;
		this.duplicatesPrcessor = duplicatesPrcessor;
	}
	
	public Iterator<D> iterator() {
		if (storage.isEmpty()) {
			throw new IllegalStateException("storage is empty. use loadSource to fill it.");
		}

		var pq = new PriorityQueue<Pair<K, D>>();

		var chunkIterators = new HashMap<K, Iterator<D>>();

		for (K key : storage.keys()) {
			var sortedChunkIterator = storage.get(key).iterator();
			chunkIterators.put(key, sortedChunkIterator);
			var pair = new Pair<K, D>(key, sortedChunkIterator.next());
			pq.add(pair);
		}

		Iterator<D> mergingIterator = new Iterator<D>() {

			@Override
			public boolean hasNext() {
				return !pq.isEmpty();
			}

			@Override
			public D next() {
				var pair = pq.poll();
				Iterator<D> chunkIterator = chunkIterators.get(pair.key);
				if (chunkIterator.hasNext()) {
					pq.add(new Pair<K, D>(pair.key, chunkIterator.next()));
				}
				return pair.data;
			}

		};

		return new DeduplicatingIterator<D>(mergingIterator, duplicatesPrcessor);
	}

	public void loadSource(Iterable<D> source) {
		storage.store(source);
	}
}
