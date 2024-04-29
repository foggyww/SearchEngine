package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;

public class TermTuple extends AbstractTermTuple {

    public TermTuple(){

    }

    public TermTuple(Term term,int curPos){
        this.term = term;
        this.curPos = curPos;
    }

    /**
     * 判断二个三元组内容是否相同
     * @param obj ：要比较的另外一个三元组
     * @return 如果内容相等（三个属性内容都相等）返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TermTuple){
            AbstractTermTuple tuple = (AbstractTermTuple) obj;
            return this.term.equals(tuple.term) && this.curPos == tuple.curPos;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "term = "+this.term+" freq = "+this.freq+" curPos= "+this.curPos;
    }
}
