package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.*;
import java.util.List;
import java.util.Locale;

import static hust.cs.javacourse.search.util.Config.STRING_SPLITTER_REGEX;

public class TermTupleScanner extends AbstractTermTupleScanner {

    //单词分割器
    StringSplitter splitter = new StringSplitter();
    //当前行所分割出来的单词列表
    private List<String> words;
    //单词列表的当前索引
    private int wordIndex;
    //当前是第几个单词
    int nowCurPos = 0;

    private TermTupleScanner(){
    }

    public TermTupleScanner(BufferedReader input) throws IOException {
        super(input);
        //初始化分割器
        splitter.setSplitRegex(STRING_SPLITTER_REGEX);
    }
    @Override
    public AbstractTermTuple next() {
        try {
            //如果当前的单词列表还没用尽就继续返回一个单词
            if (words!=null &&wordIndex < words.size()) {
                return getTermTuple();
            }
            //如果当前单词列表用尽了，就再读一行
            //扫描的当前行
            String currentLine;
            while ((currentLine =input.readLine())!=null) {
                words = splitter.splitByRegex(currentLine); // 使用正则表达式将行分割成单词
                //读到空行，继续读
                if(words.isEmpty()) continue;
                //非空行，返回一个term
                wordIndex = 0;
                return getTermTuple();
            }
            //读完了，关闭流返回null
            close();
            return null;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取单词列表的当前下标单词对应的TermTuple，并将wordIndex和nowCurPos往后置一位
     * @return 当前下标单词
     */
    public TermTuple getTermTuple(){
        String word;
        if(Config.IGNORE_CASE){ //如果构建时忽略大小写
            word= words.get(wordIndex).toLowerCase(Locale.ROOT);
        }else {
            word = words.get(wordIndex);
        }
        //将索引和指针指向下一个
        TermTuple termTuple = new TermTuple(
                new Term(word),
                nowCurPos
        );
        wordIndex++;
        nowCurPos++;
        return termTuple;
    }

}
