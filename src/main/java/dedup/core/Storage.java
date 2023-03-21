package dedup.core;

import java.util.function.Supplier;

public interface Storage<K, D extends Comparable<D>> {

	void store(Iterable<D> source);

	void store(Iterable<D> source, Supplier<K> keyGen);

	Iterable<K> keys();

	Iterable<D> get(K key);

	boolean isEmpty();

}