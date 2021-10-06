package com.newcoder.community.entity;


//封装分页功能
public class Page {
    private  int current=1; //页码
    private  int limit=10;  //显示数据条数
    private int rows; //数据总数
    private String path; //查询路劲

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current>=1)
        {
            this.current = current;
        }

    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit>=1 && limit<=100)
        {
            this.limit = limit;
        }

    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows>=0)
        {
            this.rows = rows;
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //获取当前页起始行
    public int getOffSet()
    {
        //current*limit-limit
        return (current-1)*limit;
    }

    //获取总页数
    public int getTotal()
    {
        //rows/limit
        if(rows%limit==0)
            return rows/limit;
        else
            return rows/limit+1;
    }

    //获取起始页码 离当前页最近的两页
    public int getFrom()
    {
        int from=current-2;
        return from<1 ? 1:from;
    }


    //获取终止页码
    public int getTo()
    {
        int to=current+2;
        int total=getTotal();
        return to>total ? total:to;
    }


}
