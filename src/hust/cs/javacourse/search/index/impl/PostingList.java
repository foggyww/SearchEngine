package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.List;

public class PostingList extends AbstractPostingList {

    public PostingList(){

    }

    @Override
    public void add(AbstractPosting posting) {
        this.list.add(posting);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n");
        for (AbstractPosting posting : list) {
            stringBuilder.append(posting.toString()).append("\n");
        }
        stringBuilder.append("]\n");
        return stringBuilder.toString();
    }

    @Override
    public void add(List<AbstractPosting> postings) {
        this.list.addAll(postings);
    }

    @Override
    public AbstractPosting get(int index) {
        return list.get(index);
    }

    @Override
    public int indexOf(AbstractPosting posting) {
        return list.indexOf(posting);
    }

    @Override
    public int indexOf(int docId) {
        for (int i = 0; i < list.size(); i++) {
            if(docId==list.get(i).getDocId()){
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(AbstractPosting posting) {
        return list.contains(posting);
    }

    @Override
    public void remove(int index) {
        list.remove(index);
    }

    @Override
    public void remove(AbstractPosting posting) {
        list.remove(posting);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * 根据文档id的大小对PostingList进行从小到大的排序
     */
    @Override
    public void sort() {
        list.sort(Comparator.comparingInt(AbstractPosting::getDocId));
    }

    /**
     * 将PostingList序列化到文件中
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件流反序列化为PostingList
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.list = (List<AbstractPosting>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
