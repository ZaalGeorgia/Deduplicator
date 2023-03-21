package dedup.use;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import dedup.core.Deduplicator;
import dedup.core.InMemoryStorage;

public class UsageExample {
	
	public static void main(String[] args) {
		
		process(List.of("A"));
		process(List.of("D", "C", "B", "A"));
		process(List.of("D", "D", "D", "D"));
		process(List.of("D", "D", "E", "A", "B", "D", "C", "A", "E", "C"));
		process(List.of("D", "D", "D", "D"), List.of("D", "G", "X", "D"));
		
	}

	@SafeVarargs
	private static void process(List<String> ...data ) {
		var storage = new InMemoryStorage<String>();
		storage.setChunkSize(2);
		
		Consumer<List<String>> duplicatesProcessor = System.out::println;
		var deduplipator = new Deduplicator<String, String>(storage, duplicatesProcessor);
		for (List<String> source : data) {
			deduplipator.loadSource(source);
		}
		Iterator<String> deduplicate = deduplipator.iterator();
		
		while (deduplicate.hasNext()) {
			System.out.println(deduplicate.next());
		}
		System.out.println("--");
	}

}
