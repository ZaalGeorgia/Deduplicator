package dedup.core;

import java.util.List;

public abstract class SortingStorage<K, D extends Comparable<D>> implements ChunkSplittingStorage<K,D> {

	private static final int DEFAULT_CHUNK_SIZE = 5000;
	private int chunkSize = DEFAULT_CHUNK_SIZE;

	public SortingStorage() {
		super();
	}

	@Override
	public void storeChunk(List<D> chunk) {
		chunk.sort(D::compareTo);
		spill(chunk);
	}

	protected abstract void spill(List<D> chunk);

	public int getChunkSize() {
		return chunkSize;
	}

	@Override
	public void setChunkSize(int size) {
		chunkSize = size;
	}

}