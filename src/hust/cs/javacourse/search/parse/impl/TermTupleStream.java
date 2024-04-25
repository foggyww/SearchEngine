package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;

public abstract class TermTupleStream extends AbstractTermTupleStream {
    @Override
    public abstract TermTuple next();
}
