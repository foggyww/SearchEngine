package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentBuilder extends AbstractDocumentBuilder {

    public DocumentBuilder(){

    }
    /**
     * <pre>
     * 由解析文本文档得到的TermTupleStream,构造Document对象.
     * @param docId             : 文档id
     * @param docPath           : 文档绝对路径
     * @param termTupleStream   : 文档对应的TermTupleStream
     * @return ：Document对象
     * </pre>
     */
    @Override
    public Document build(int docId, String docPath, AbstractTermTupleStream termTupleStream) {

//        AbstractTermTupleStream ts = new FilterReader(termTupleStream).getFilter();
        AbstractTermTupleStream ts = termTupleStream;
        //创建文档的termTupleList
        List<AbstractTermTuple> termTupleList = new ArrayList<>();
        AbstractTermTuple termTuple;
        //遍历termTupleStream
        while ((termTuple=ts.next())!=null){
            termTupleList.add(termTuple);
        }
        //创建文档
        return new Document(docId,docPath,termTupleList);
    }

    /**
     * <pre>
     * 由给定的File,构造Document对象.
     * 该方法利用输入参数file构造出AbstractTermTupleStream子类对象后,内部调用
     *      AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream)
     * @param docId     : 文档id
     * @param docPath   : 文档绝对路径
     * @param file      : 文档对应File对象
     * @return          : Document对象
     * </pre>
     */
    @Override
    public Document build(int docId, String docPath, File file) {
        try {

            AbstractTermTupleStream ts = new TermTupleScanner(new BufferedReader(
                    new FileReader(file)));
            //内部调用build，根据流创建
            return build(docId,docPath,ts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
