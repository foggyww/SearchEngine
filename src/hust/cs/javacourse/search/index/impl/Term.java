package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Term extends AbstractTerm {

    public Term(){

    }

    public Term(String content){
        this.content = content;
    }

    /**
     * 判断二个Term内容是否相同
     * @param obj ：要比较的另外一个Term
     * @return 如果内容相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Term){
            return ((Term)obj).content.equals(this.content);
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 比较二个Term大小（按字典序）
     * @param o： 要比较的Term对象
     * @return ： 返回二个Term对象的字典序差值
     */
    @Override
    public int compareTo(AbstractTerm o) {
        return this.content.compareToIgnoreCase(o.getContent());
    }

    /**
     * 将Term序列化到文件中
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.content);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 将文件流反序列化为Term
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.content = (String)(in.readObject());
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
