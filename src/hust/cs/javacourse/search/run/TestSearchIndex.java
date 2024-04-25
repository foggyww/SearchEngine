package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Index;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import hust.cs.javacourse.search.query.impl.Hit;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.util.Config;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

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
        indexSearcher.open(Config.INDEX_DIR+"index");
        Hit[] hits = indexSearcher.search(new Term("activity"),new Term("benefits"), new Sort() {
            @Override
            public void sort(List<AbstractHit> hits) {

            }

            @Override
            public double score(AbstractHit hit) {
                return 0;
            }
        }, AbstractIndexSearcher.LogicalCombination.AND);
        for (Hit hit : hits) {
            System.out.println(hit);
        }
    }
}
