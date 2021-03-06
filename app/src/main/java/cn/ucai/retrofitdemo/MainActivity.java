package cn.ucai.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.ucai.retrofitdemo.bean.BoutiqueBean;
import cn.ucai.retrofitdemo.bean.CategoryChildBean;
import cn.ucai.retrofitdemo.bean.GoodsDetailsBean;
import cn.ucai.retrofitdemo.bean.Result;
import cn.ucai.retrofitdemo.bean.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    TextView mTvText;
    Button btn,fuli,btnUsr,btnChild;

    String root = "http://101.251.196.90:8080/FuLiCenterServerV2.0/";
    String api = "https://api.github.com";
    GitHubService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        mTvText = (TextView) findViewById(R.id.tv_text);
        btn = (Button) findViewById(R.id.btn_get_user);
        fuli = (Button) findViewById(R.id.btn_get_boutique);
        btnUsr = (Button) findViewById(R.id.btn_get_user_by_name);
        btnChild = (Button) findViewById(R.id.btn_get_child);
        Log.e("main","onCreate....");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GitHubService.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserString();
            }
        });
        fuli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFuli();
            }
        });
        btnUsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser();
            }
        });
        btnChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChild();
            }
        });
    }

    private void getChild() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(root)
                .build();
        FuliServer server = retrofit.create(FuliServer.class);
        Call<List<CategoryChildBean>> call = server.getChild(String.valueOf(344));
        call.enqueue(new Callback<List<CategoryChildBean>>() {
            @Override
            public void onResponse(Call<List<CategoryChildBean>> call,
                                   Response<List<CategoryChildBean>> response) {
                Log.e("main","call="+call);
                Log.e("main","response="+response);
                List<CategoryChildBean> childBeanList = response.body();
                Log.e("main","list="+childBeanList);
                if (childBeanList!=null){
                    for (CategoryChildBean childBean:childBeanList){
                        Log.e("main","child="+childBean.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CategoryChildBean>> call, Throwable t) {

            }
        });
    }

    private void getUser() {
        Log.e("main","getUser.......");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(root)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FuliServer server = retrofit.create(FuliServer.class);
        Call<Result> resultCall = server.getUserToResult("aaa456789");
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.e("main","call......."+call);
                Log.e("main","response......."+response);
                Log.e("main","response.body......"+response.body());
                Result result = response.body();
                Log.e("main","result......."+result);
                if (result!=null){
                    Log.e("main","result.getRet......"+result.getRetData());
                    try {
                        User user = (User) result.getRetData();
                    }catch (Exception e){
                        Log.e("main","e="+e);
                    }

                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    private void getUserString() {
        Log.e("main","getUser.......");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(root)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        FuliServer server = retrofit.create(FuliServer.class);
        Call<String> call = server.getUser("aaa456789");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("main","call="+call);
                Log.e("main","response="+response);
                String s = response.body().toString();
                Log.e("main","s="+s);
                if (s!=null){
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    Log.e("main","result="+result);
                    if (result!=null && result.isRetMsg()){
                        User user = (User) result.getRetData();
                        if (user!=null){
                            Log.e("main","user="+user);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void getRepos() {
        Log.e("main","onclick....");
        Call<List<Repo>> repos = service.listRepos("clawpo");
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                List<Repo> repoList = response.body();
                Log.e("main","list="+repoList);
                if (repoList!=null) {
                    Log.e("main","list.size="+repoList.size());
                    StringBuffer sb = new StringBuffer();
                    for (Repo repo : repoList) {
                        Log.e("main", "repo=" + repo.getId());
                        sb.append(repo.getId()).append(",");
                    }
                    mTvText.setText(sb.toString());
                    Log.e("main", "list=" + sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Log.e("main","fail....");
            }
        });
    }

    public void getFuli(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(root)
                .build();
        FuliServer server = retrofit.create(FuliServer.class);
        Call<List<BoutiqueBean>> listCall = server.listBoutique();
        listCall.enqueue(new Callback<List<BoutiqueBean>>() {
            @Override
            public void onResponse(Call<List<BoutiqueBean>> call, Response<List<BoutiqueBean>> response) {
                List<BoutiqueBean> list = response.body();
                Log.e("main","list="+list);
                if (list!=null){
                    for (BoutiqueBean b:list){
                        Log.e("main","b="+b.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BoutiqueBean>> call, Throwable t) {
                Log.e("main","fail....");
            }
        });
    }

    interface GitHubService {
        ///orgs/octokit/repos
        @GET("users/{user}/repos")
        Call<List<Repo>> listRepos(@Path("user") String user);
    }

    interface FuliServer{
        @GET("findBoutiques")
        Call<List<BoutiqueBean>> listBoutique();

        @GET("findCategoryChildren")
        Call<List<CategoryChildBean>> getChild(@Query("parent_id") String parentId);

        @GET("findUserByUserName")
        Call<String> getUser(@Query("m_user_name") String username);

        @GET("findUserByUserName")
        Call<Result> getUserToResult(@Query("m_user_name") String username);

//        @GET("findGoodsDetails/cat_id=262/page_id=1/page_size=10")
        @GET("findGoodsDetails")
        Call<List<GoodsDetailsBean>> getGoodsList(
                @Query("cat_id") int catId,
                @Query("page_id") int pageId,
                @Query("page_size") int pageSize
        );

    }

}
