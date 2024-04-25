package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Document;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.parse.impl.TermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.*;
import java.util.List;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args) throws IOException {

        DocumentBuilder documentBuilder = new DocumentBuilder();
        Index index = new Index();
        List<String> docPaths = FileUtil.list(Config.DOC_DIR,".txt");
        int id = 1;
        for (String docPath : docPaths) {
            Document document= (Document) documentBuilder.build(id,docPath, new File(docPath));
            index.addDocument(document);
            id++;
        }
        System.out.println(index);
        try {
            File file = new File(Config.INDEX_DIR+"index");

            if(file.exists()){
                if(!file.delete()){
                    throw new IOException();
                }
            }

            if(file.createNewFile()){
                index.save(file);
            }else{
                throw new IOException();
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
