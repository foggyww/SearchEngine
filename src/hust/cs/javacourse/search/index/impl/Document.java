package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractTermTuple;

import java.util.HashSet;
import java.util.List;

public class Document extends AbstractDocument {

    public Document(){

    }
    public Document(int docId, String docPath){
        super(docId, docPath);
    }

    public Document(int docId, String docPath,List<AbstractTermTuple> tuples){
        super(docId, docPath, tuples);
    }

    @Override
    public int getDocId() {
        return this.docId;
    }

    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    @Override
    public String getDocPath() {
        return this.docPath;
    }

    @Override
    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    @Override
    public List<AbstractTermTuple> getTuples() {
        return this.tuples;
    }

    @Override
    public void addTuple(AbstractTermTuple tuple) {
        this.tuples.add(tuple);
    }

    @Override
    public boolean contains(AbstractTermTuple tuple) {
        return tuples.contains(tuple);
    }

    @Override
    public TermTuple getTuple(int index) {
        return (TermTuple) tuples.get(index);
    }

    @Override
    public int getTupleSize() {
        return tuples.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("docId = ").append(docId).append(" docPath = ").append(docPath).append(" tuples = {\n");
        for (AbstractTermTuple tuple : tuples) {
            stringBuilder.append(tuple.toString()).append("\n");
        }
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Document){
            Document document = (Document) obj;
            HashSet<AbstractTermTuple> set1 = new HashSet<>(document.tuples);
            HashSet<AbstractTermTuple> set2 = new HashSet<>(this.tuples);
            boolean setEquals = set1.equals(set2);
            return this.docId==document.docId&&this.docPath.equals(document.docPath)&&setEquals;
        }
        return super.equals(obj);
    }
}

