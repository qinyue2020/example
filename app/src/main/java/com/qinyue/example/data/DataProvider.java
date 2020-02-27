package com.qinyue.example.data;


import com.qinyue.example.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 静态数据
 *
 * 创建人:qinyue
 * 创建日期:2020/2/26
 */
public class DataProvider {

    public static List<Object> getUsertGuides() {
        List<Object> list = new ArrayList<>();
        list.add(R.drawable.guide_img_1);
        list.add(R.drawable.guide_img_2);
        list.add(R.drawable.guide_img_3);
        list.add(R.drawable.guide_img_4);
        return list;
    }

}
