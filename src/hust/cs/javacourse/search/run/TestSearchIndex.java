package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.impl.Hit;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     *  搜索程序入口
     * @param args ：命令行参数
     */
    public static void main(String[] args){
        IndexSearcher indexSearcher =new IndexSearcher();
        indexSearcher.open(Config.INDEX_DIR+"index.dat");
        Hit[] hits = indexSearcher.search(new Term("activity"),new SimpleSorter());
        for(AbstractHit hit : hits){
            System.out.println(hit);
        }
    }
}
