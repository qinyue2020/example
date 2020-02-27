package com.qinyue.example.base;

import com.qinyue.example.data.DataRepository;
import com.qinyue.example.data.source.HttpDataSource;
import com.qinyue.example.data.source.LocalDataSource;
import com.qinyue.example.data.source.http.HttpDataSourceImpl;
import com.qinyue.example.data.source.http.service.DemoApiService;
import com.qinyue.example.data.source.local.LocalDataSourceImpl;
import com.qinyue.example.net.RetrofitClient;


/**
 * 注入全局的数据仓库，可以考虑使用Dagger2。（根据项目实际情况搭建，千万不要为了架构而架构）
 */
public class Injection {
    public static DataRepository provideDemoRepository() {
        //网络API服务
        DemoApiService apiService = RetrofitClient.getInstance().create(DemoApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return DataRepository.getInstance(httpDataSource, localDataSource);
    }
}
