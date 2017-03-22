package cn.ucai.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_text)
    TextView mTvText;

    String root = "http://101.251.196.90:8080/FuLiCenterServerV2.0/findUserByUserName?m_user_name=aaa456789";
    String api = "https://api.github.com";
    GitHubService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.e("main","onCreate....");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GitHubService.class);

        Call<List<Repo>> repos = service.listRepos("clawpo");
        Log.e("main","enqueue....");
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                List<Repo> repoList = response.body();
                Log.e("main","onResponse....repoList="+repoList);
                if (repoList!=null){
                    Log.e("main","repolist.size="+repoList.size());
                    StringBuffer sb = new StringBuffer();
                    for (Repo repo:repoList){
                        sb.append(repo.getId());
                    }
                    mTvText.setText(sb.toString());
                    Log.e("main","list="+sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Log.e("main","fail....");
            }
        });
    }

    @OnClick(R.id.btn_get_user)
    public void getRepos() {
        Log.e("main","onclick....");
        Call<List<Repo>> repos = service.listRepos("octoact");
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                List<Repo> repoList = response.body();
                StringBuffer sb = new StringBuffer();
                for (Repo repo:repoList){
                    sb.append(repo.getId());
                }
                mTvText.setText(sb.toString());
                Log.e("main","list="+sb.toString());
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Log.e("main","fail....");
            }
        });
    }

    interface GitHubService {
        ///orgs/octokit/repos
        @GET("users/{user}/repos")
        Call<List<Repo>> listRepos(@Path("user") String user);
    }
}
