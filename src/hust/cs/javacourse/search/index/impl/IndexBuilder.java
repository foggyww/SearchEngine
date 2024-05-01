package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.File;
import java.util.List;

public class IndexBuilder extends AbstractIndexBuilder {

    public IndexBuilder(AbstractDocumentBuilder docBuilder) {
        super(docBuilder);
    }

    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        Index index = new Index();
        List<String> docPaths = FileUtil.list(rootDirectory);
        for (String docPath : docPaths) {
            Document document=(Document)docBuilder.build(docId,docPath, new File(docPath));
            index.addDocument(document);
            docId++;
        }
        return index;
    }
}
