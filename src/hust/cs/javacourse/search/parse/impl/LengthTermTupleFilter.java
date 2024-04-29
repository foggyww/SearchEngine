package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

import static hust.cs.javacourse.search.util.StopWords.STOP_WORDS;

public class LengthTermTupleFilter extends AbstractTermTupleFilter {
    /**
     * 构造函数
     *
     * @param input ：Filter的输入，类型为AbstractTermTupleStream
     */
    public LengthTermTupleFilter(AbstractTermTupleStream input) {
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

        //筛选是否过短
        if(string.length()< Config.TERM_FILTER_MINLENGTH){
            return false;
        }

        //筛选是否过长
        if(string.length()>Config.TERM_FILTER_MAXLENGTH){
            return false;
        }

        return true;
    }
}
