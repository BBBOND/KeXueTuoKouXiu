package com.bbbond.kexuetuokouxiu.app.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.adapter.SearchAdapter;
import com.bbbond.kexuetuokouxiu.app.fragment.HomeFragment;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.db.ProgrammeDao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private SearchView mSearchView;
    private Toolbar toolbar;
    private RecyclerView rvSearch;

    private SearchAdapter adapter;
    private List<Programme> programmeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvSearch = (RecyclerView) findViewById(R.id.rv_search);
        rvSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
        rvSearch.addItemDecoration(new DividerItemDecoration(SearchActivity.this, DividerItemDecoration.VERTICAL));
        adapter = new SearchAdapter(SearchActivity.this, programmeList);
        adapter.setOnClickListener(new SearchAdapter.OnClickListener() {
            @Override
            public void click(int position, String id) {
                Programme programme = programmeList.get(position);
                String[] categories = HomeFragment.categories[6];
                String category = programme.getCategory();
                boolean isMedia = true;
                for (String c : categories) {
                    if (c.equals(category)) {
                        isMedia = false;
                        break;
                    }
                }
                if (isMedia) {
                    if (programme.getMediaUrl() == null || programme.getMediaUrl().equals("") || programme.getMediaUrl().equals("null")) {
                        Uri uri = Uri.parse(programmeList.get(position).getLink());
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } else {
                        Intent intent = new Intent(SearchActivity.this, PlayingActivity.class);
                        intent.putExtra(PlayingActivity.PROGRAMME, programme);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(SearchActivity.this, ArticleDetailActivity.class);
                    intent.putExtra(ArticleDetailActivity.ARTICLE, programmeList.get(position));
                    startActivity(intent);
                }
            }
        });
        rvSearch.setAdapter(adapter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("搜索节目");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchAutoComplete.isShown()) {
                    try {
                        //如果搜索框中有文字，则会先清空文字，但网易云音乐是在点击返回键时直接关闭搜索框
                        mSearchAutoComplete.setText("");
                        Method method = mSearchView.getClass().getDeclaredMethod("onCloseClicked");
                        method.setAccessible(true);
                        method.invoke(mSearchView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);

        //修改搜索框控件间的间隔（这样只是为了在细节上更加接近网易云音乐的搜索框）
        LinearLayout search_edit_frame = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) search_edit_frame.getLayoutParams();
        params.leftMargin = 0;
        search_edit_frame.setLayoutParams(params);

        //设置搜索框有字时显示叉叉，无字时隐藏叉叉
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.equals("")) {
                    programmeList.clear();
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                } else {
                    List<Programme> list = ProgrammeDao.getInstance().getProgrammeListByKey(newText);
                    if (list != null) {
                        programmeList.clear();
                        programmeList.addAll(list);
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        //设置输入框提示文字样式
        mSearchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        mSearchAutoComplete.setTextColor(getResources().getColor(android.R.color.background_light));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
