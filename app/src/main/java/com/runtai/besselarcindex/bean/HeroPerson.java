package com.runtai.besselarcindex.bean;

import java.util.List;

/**
 * 英雄好汉
 */
public class HeroPerson {

    private int total;
    private List<PersonList> sections;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PersonList> getSections() {
        return sections;
    }

    public void setSections(List<PersonList> sections) {
        this.sections = sections;
    }

}

