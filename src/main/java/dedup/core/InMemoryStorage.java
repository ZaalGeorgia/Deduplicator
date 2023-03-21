package dedup.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryStorage<D extends Comparable<D>> extends SortingStorage<String,D> {
	
	private Map<String,List<D>> storage = new HashMap<>();

	
	@Override
	public void store(Iterable<D> source) {
		store(source, this::generateUniqueKey);
	}
	
	@Override
	public Iterable<String> keys() {
		return storage.keySet();
	}

	@Override
	public Iterable<D> get(String key) {
		return storage.getOrDefault(key, List.of());
	}
	
	private String generateUniqueKey() {
		String key = null;
		do {
			key = UUID.randomUUID().toString();
		} while (storage.containsKey(key));
		return key;
	}
	
	protected void spill(List<D> chunk) {
		storage.put(generateUniqueKey(), chunk);
	}

	@Override
	public boolean isEmpty() {
		return storage.isEmpty();
	}

}
