package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.parse.AbstractTermTupleStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * <pre>
 * FilterReader用于读取配置文件config.properties来创建Filter对象
 * </pre>
 */
public class FilterReader {

    private AbstractTermTupleStream ts;

    /**
     * 构造函数
     * @param ts 输入流
     */
    public FilterReader(AbstractTermTupleStream ts){
        this.ts = ts;
    }

    /**
     * 根据配置文件得到Filter对象
     * @return 得到的Filter对象
     */
    public AbstractTermTupleStream getFilter(){

        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            //未找到配置文件
            e.printStackTrace();
            return ts;
        }

        // 获取过滤器类名
        String filterConfig = properties.getProperty("filter.class");
        //未设置相关配置
        if(filterConfig==null){
            return ts;
        }
        String[] filterClassNames = filterConfig.split("\\|");
        if(filterClassNames.length==1&&filterClassNames[0].isEmpty()){
            //配置设置为空
            return ts;
        }
        try {
            for (String filterClassName : filterClassNames) {
                Class<?> filterClass =Class.forName(filterClassName);
                Constructor<?> constructor= filterClass.getDeclaredConstructor(AbstractTermTupleStream.class);
                ts  = (AbstractTermTupleStream) constructor.newInstance(ts);

            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return ts;
    }

}
