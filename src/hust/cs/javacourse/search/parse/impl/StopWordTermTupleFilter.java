package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

import java.util.regex.Pattern;

import static hust.cs.javacourse.search.util.StopWords.STOP_WORDS;

public class StopWordTermTupleFilter  extends AbstractTermTupleFilter {
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public StopWordTermTupleFilter(AbstractTermTupleStream input) {
        super(input);
    }

    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple termTuple;
        //遍历流
        while ((termTuple=input.next())!=null){
            //符合筛选的部分返回
            if(filter(termTuple.term.getContent())){
                return termTuple;
            }
        }
        return null;
    }

    private boolean filter(String string){

        //筛选是否为停用词
        for (String stopWord : STOP_WORDS) {
            if(string.equals(stopWord)){
                return false;
            }
        }
        return true;
    }
}
