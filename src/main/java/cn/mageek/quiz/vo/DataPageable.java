package cn.mageek.quiz.vo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 分页
 * @Author: Mageek Chiu
 * @Date: 2017-10-20:17:22
 */
public class DataPageable<T>  {
   private int curPage;//当前页
    private int numPage;//每页数量
   private int allPage;//总页数
    private int allNUm;//总数
    private List<T> contentList;//当前页的内容
    public List<Integer> pageList;

    public List<Integer> getPageList() {
        return pageList;
    }

    public void setPageList(List<Integer> pageList) {
        this.pageList = pageList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;//分类标准

    public DataPageable() {
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getAllPage() {
        return allPage;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }

    public List<T> getContentList() {
        return contentList;
    }

    public void setContentList(List<T> contentList) {
        this.contentList = contentList;
    }
    public int getNumPage() {
        return numPage;
    }

    public void setNumPage(int numPage) {
        this.numPage = numPage;
    }
    public int getAllNUm() {
        return allNUm;
    }

    public void setAllNUm(int allNUm) {
        this.allNUm = allNUm;
    }

    public void generatePageList(){
        int from ;
        int to ;
        if(curPage>3){//当前页大于3
            from = curPage-2;
        }else{
            from = 1;
        }
        if(curPage>allPage-3){
            to = allPage;
        }else{
            to = curPage+2;
        }

//        for (int i=from;i<=to;i++){
//            pageList.add(i);
//        }
        //用Java8快速生成
        pageList = Stream.iterate(from,item->item+1).limit(to).collect(Collectors.toList());

        pageList.stream().forEach((Integer string) ->{
            System.out.println(string);
        });
    }
}
