package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.util.Config;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args){
        Index index = new Index();
        File file = new File(Config.INDEX_DIR+"index");
        if(!file.exists()){
            throw new RuntimeException(new FileNotFoundException());
        }
        index.load(file);
        System.out.println(index);
    }
}
