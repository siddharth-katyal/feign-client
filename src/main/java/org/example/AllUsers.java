package org.example;
import java.util.ArrayList;


@lombok.Data
public class AllUsers {
    public int page;
    public int per_page;
    public int total;
    public int total_pages;
    public ArrayList<Data> data;
    public Support support;
}
