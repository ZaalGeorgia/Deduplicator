package dedup.use;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import dedup.core.Deduplicator;
import dedup.core.InMemoryStorage;

public class UniqueWords {

	public static void main(String[] args) {

		var storage = new InMemoryStorage<String>();
		Consumer<List<String>> duplicatesProcessor = l -> {
			if (l.size() > 1)
				System.out.println(l);
		};
		var deduplipator = new Deduplicator<String, String>(storage, duplicatesProcessor);

		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] split = line.split(" ");
				List<String> words = List.of(split);
				deduplipator.loadSource(words);
			}
		}

		Iterator<String> deduplicate = deduplipator.iterator();

		while (deduplicate.hasNext()) {
			System.out.println(deduplicate.next());
		}
		System.out.println("--");

	}

}
