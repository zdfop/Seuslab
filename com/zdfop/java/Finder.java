import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Finder {

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final ExecutorService calculateService = Executors.newSingleThreadExecutor();
    private int timeout = 300;

    public void setTimeout(int sec) {
        timeout = sec;
    }

    public int findIndex(String sequence) throws ExecutionException, InterruptedException {

        try {
            return calculateService.invokeAny(List.of(() -> {
                int length = sequence.length();
                AtomicInteger index = new AtomicInteger(0);
                StringBuilder builder = new StringBuilder("3.");
                while (!Thread.currentThread().isInterrupted()) {
                    calculate(builder, index, length);
                    int resultIndex = builder.lastIndexOf(sequence);
                    if (resultIndex > -1) {
                        return resultIndex;
                    }
                }
                return -1;
            }), timeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new InterruptedCalculateException();
        } finally {
            shutdown();
        }
    }

    private void calculate(StringBuilder builder, AtomicInteger index, int count) throws InterruptedException, ExecutionException {
        Set<Callable<IndexCallable>> callables = new HashSet<>();
        for (int i = 0; i < count; i++) {
            int currIndex = index.getAndIncrement();
            callables.add(() -> {
                Pi pi = new Pi();
                return new IndexCallable(currIndex, pi.getNumber(currIndex));
            });
        }

        List<Future<IndexCallable>> futures = executorService.invokeAll(callables, timeout, TimeUnit.SECONDS);

        List<IndexCallable> list = new ArrayList<>();
        for (Future<IndexCallable> future : futures) {
            list.add(future.get());
        }
        List<IndexCallable> sorted = list.stream().sorted().toList();
        sorted.forEach(ic -> builder.append(ic.getChr()));
    }

    private void shutdown() {
        executorService.shutdownNow();
        calculateService.shutdownNow();
    }

    static class IndexCallable implements Comparable<IndexCallable> {

        private final int index;
        private final Character chr;

        IndexCallable(int index, Character chr) {
            this.index = index;
            this.chr = chr;
        }

        public int getIndex() {
            return index;
        }

        public Character getChr() {
            return chr;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            IndexCallable that = (IndexCallable) o;
            return index == that.index && Objects.equals(chr, that.chr);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, chr);
        }

        @Override
        public int compareTo(IndexCallable o) {
            return Integer.compare(index, o.getIndex());
        }
    }
}
