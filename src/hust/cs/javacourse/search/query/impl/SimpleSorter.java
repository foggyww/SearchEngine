package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.Sort;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleSorter implements Sort {
    @Override
    public void sort(List<AbstractHit> hits) {
        Collections.sort(hits);
    }

    @Override
    public double score(AbstractHit hit) {
        AtomicReference<Double> score = new AtomicReference<>((double) 0);
        hit.getTermPostingMapping().forEach(((term, posting) -> {
            score.updateAndGet(v -> v + posting.getFreq());
        }));
        return score.get();
    }
}
