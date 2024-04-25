package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.index.impl.Posting;
import hust.cs.javacourse.search.index.impl.PostingList;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class IndexSearcher extends AbstractIndexSearcher {

    //是否已经打开
    private boolean isOpen=false;

    /**
     * 从指定索引文件打开索引，加载到index对象里. 一定要先打开索引，才能执行search方法
     * @param indexFile ：指定索引文件
     */
    @Override
    public void open(String indexFile) {
        index.load(new File(indexFile));
        isOpen = true;
    }

    /**
     * 根据单个检索词进行搜索
     * @param queryTerm ：检索词
     * @param sorter ：排序器
     * @return ：命中结果数组
     */
    @Override
    public Hit[] search(AbstractTerm queryTerm, Sort sorter) {
        if(!isOpen) throw new RuntimeException("还未打开Index文件");
        //提取该单词的PostingList
        PostingList postingList = (PostingList) index.termToPostingListMapping.get(queryTerm);
        Hit[] hits = new Hit[postingList.size()];
        //遍历PostingList，创建Hit
        for (int i = 0; i < postingList.size(); i++) {
            Posting posting = postingList.get(i);
            int docId = posting.getDocId();
            Map<AbstractTerm, AbstractPosting> map = new TreeMap<>();
            map.put(queryTerm,posting);
            hits[i]=new Hit(docId,index.docIdToDocPathMapping.get(docId),map);
        }
        return hits;
    }

    /**
     *
     * 根据二个检索词进行搜索
     * @param queryTerm1 ：第1个检索词
     * @param queryTerm2 ：第2个检索词
     * @param sorter ：    排序器
     * @param combine ：   多个检索词的逻辑组合方式
     * @return ：命中结果数组
     */
    @Override
    public Hit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        if(!isOpen) throw new RuntimeException("还未打开Index文件");
        //提取两个单词的PostingList
        PostingList postingList1 = (PostingList) index.termToPostingListMapping.get(queryTerm1);
        PostingList postingList2 = (PostingList) index.termToPostingListMapping.get(queryTerm2);
        Hit[] hits = new Hit[postingList1.size()+postingList2.size()];
        int hitsIndex = 0;
        if(combine == LogicalCombination.AND){

            //用两个索引，分别指向当前两个postingList的最前方，由于postingList的docId是顺序排放的，因而每次比较两个index对应的docID
            //若相等，则两个index加1，将该次的结果创建Hit结果。若不相等，则docId更小的一方的index+1
            int index1=0,index2=0;
            while (index1<postingList1.size()&&index2<postingList2.size()){
                Posting posting1 = postingList1.get(index1);
                Posting posting2 = postingList2.get(index2);
                int docId1 = posting1.getDocId();
                int docId2 = posting2.getDocId();
                if(docId1==docId2){
                    String docPath = index.docIdToDocPathMapping.get(docId1);
                    Map<AbstractTerm, AbstractPosting> map = new TreeMap<>();
                    map.put(queryTerm1,posting1);
                    map.put(queryTerm2,posting2);
                    hits[hitsIndex++]=new Hit(docId1,docPath,map);
                    index1++;
                    index2++;
                }else if(docId1>docId2){
                    index2++;
                }else{
                    index1++;
                }
            }
            return hits;
        }else{
            //用两个索引，分别指向当前两个postingList的最前方，由于postingList的docId是顺序排放的，因而每次比较两个index对应的docID
            //若相等，则两个index加1，将该次的结果创建Hit结果。若不相等，则docId更小的一方创建为Hit结果并index+1
            int index1=0,index2=0;
            while (index1<postingList1.size()&&index2<postingList2.size()){
                Posting posting1 = postingList1.get(index1);
                Posting posting2 = postingList2.get(index2);
                int docId1 = posting1.getDocId();
                int docId2 = posting2.getDocId();
                if(docId1==docId2){
                    //两个一起创建Hit
                    hits[hitsIndex++] = creatHitWithTwoPosting(posting1,posting2,queryTerm1,queryTerm2);
                    index1++;
                    index2++;
                }else if(docId1>docId2){
                    //创建postingList2的hit
                    hits[hitsIndex++]=createHitWithPosting(posting2,queryTerm2);
                    index2++;
                }else{
                    //创建postingList1的hit
                    hits[hitsIndex++]=createHitWithPosting(posting1,queryTerm1);
                    index1++;
                }
            }
            //将剩余的创建为Hit
            while (index1<postingList1.size()){
                hits[hitsIndex++] = createHitWithPosting(postingList1.get(index1),queryTerm1);
                index1++;
            }
            while (index2<postingList2.size()){
                hits[hitsIndex++] = createHitWithPosting(postingList2.get(index2),queryTerm2);
            }
        }
        return hits;
    }

    /**
     * 根据两个posting创建Hit对象
     * @param posting1 第一个posting
     * @param posting2 第二个posting
     * @param queryTerm1 第一个检索词
     * @param queryTerm2 第二个检索词
     * @return 创建的Hit对象
     */
    private Hit creatHitWithTwoPosting(Posting posting1,Posting posting2,AbstractTerm queryTerm1,
                                       AbstractTerm queryTerm2){
        int docId = posting1.getDocId();
        String docPath = index.docIdToDocPathMapping.get(docId);
        Map<AbstractTerm, AbstractPosting> map = new TreeMap<>();
        map.put(queryTerm1,posting1);
        map.put(queryTerm2,posting2);
        return new Hit(docId,docPath,map);
    }

    /**
     * 根据一个posting创建Hit对象
     * @param posting 目标posting
     * @param queryTerm 检索词
     * @return 目标Hit对象
     */
    private Hit createHitWithPosting(Posting posting,AbstractTerm queryTerm){
        int docId = posting.getDocId();
        String docPath = index.docIdToDocPathMapping.get(docId);
        Map<AbstractTerm, AbstractPosting> map = new TreeMap<>();
        map.put(queryTerm,posting);
        return new Hit(docId,docPath,map);
    }
}
