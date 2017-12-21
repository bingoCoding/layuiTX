package com.bingo.vo;

public class ResultPageSet<T> extends ResultSet<T> {
    private int pages;

    public ResultPageSet(){}
    public ResultPageSet(int pages){
        super();
        this.pages=pages;
    }
    public ResultPageSet(T data,int pages){
        super();
        super.data=data;
        this.pages=pages;
    }
    public ResultPageSet(T data){
        super();
        super.data=data;
    }



    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
