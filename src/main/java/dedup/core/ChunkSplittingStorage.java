package dedup.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface ChunkSplittingStorage<K, D extends Comparable<D>> extends Storage<K, D> {

	void setChunkSize(int size);

	int getChunkSize();

	void storeChunk(List<D> chunk);

	@Override
	default void store(Iterable<D> source, Supplier<K> keyGen) {
		List<D> chunk = new ArrayList<>();
		for (D data : source) {
			chunk.add(data);
			if (chunk.size() == getChunkSize()) {
				storeChunk(chunk);
				chunk = new ArrayList<>();
			}
		}
		if (!chunk.isEmpty()) {
			storeChunk(chunk);
		}
	}
}
