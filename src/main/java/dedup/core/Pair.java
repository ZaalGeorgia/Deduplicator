package dedup.core;

final class Pair<K, D extends Comparable<D>> implements Comparable<Pair<K, D>> {

	K key;
	D data;

	Pair(K key, D data) {
		this.key = key;
		this.data = data;
	}

	@Override
	public int compareTo(Pair<K, D> o) {
		return this.data.compareTo(o.data);
	}
	
}