package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * AbstractIndex的具体实现类
 */
public class Index extends AbstractIndex {
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Index : {\n");
        for (AbstractTerm term : getDictionary()) {
            stringBuilder.append(term).append(":\n");
            PostingList postingList =(PostingList)termToPostingListMapping.get(term);
            stringBuilder.append(postingList);
        }
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        //建立Id和路径的索引
        docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        //建立该文档内每个term与posting的对应关系
        Map<AbstractTerm,Posting> map = new HashMap<>();
        //遍历该文档的所有TermTuple
        for (AbstractTermTuple tuple : document.getTuples()) {
            //如果已经创建了该term的posting
            if(map.containsKey(tuple.term)){
                Posting nowPosting = map.get(tuple.term);
                nowPosting.addFreq(tuple.freq);
                nowPosting.addPosition(tuple.curPos);
            }
            //如果还未创建该term的posting
            else{
                ArrayList<Integer> list = new ArrayList<>();
                list.add(tuple.curPos);
                Posting posting = new Posting(document.getDocId(),tuple.freq,list);
                map.put(tuple.term,posting);
            }
        }
        //将该文档的所有posting加入到termToPostingListMapping
        map.forEach((abstractTerm,posting)->{
            posting.sort();
            putToTermToPostingListMapping(abstractTerm,posting);
        });
    }

    /**
     * 将指定的term和posting的对应关系添加到termToPostingListMapping中
     * @param term 要添加的term
     * @param posting 对应的posting
     */
    private void putToTermToPostingListMapping(AbstractTerm term,Posting posting){
        if(termToPostingListMapping.containsKey(term)){
            termToPostingListMapping.get(term).add(posting);
        }else{
            PostingList postingList = new PostingList();
            postingList.add(posting);
            termToPostingListMapping.put(term,postingList);
        }
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        try {
            readObject(new ObjectInputStream(Files.newInputStream(file.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try {
            writeObject(new ObjectOutputStream(Files.newOutputStream(file.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return termToPostingListMapping.get(term);
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return termToPostingListMapping.keySet();
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        for (AbstractTerm term : getDictionary()) {
            AbstractPostingList postingList = termToPostingListMapping.get(term);
            postingList.sort();
            for (int i = 0; i < postingList.size(); i++) {
                postingList.get(i).sort();
            }
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.docIdToDocPathMapping);
            out.writeObject(this.termToPostingListMapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docIdToDocPathMapping = (Map<Integer, String>) in.readObject();
            this.termToPostingListMapping = (Map<AbstractTerm, AbstractPostingList>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
