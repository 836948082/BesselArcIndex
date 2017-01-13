package com.runtai.besselarcindex;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtai.besselarcindex.adapter.PinnedHeaderListViewAdapter;
import com.runtai.besselarcindex.bean.HeroPerson;
import com.runtai.besselarcindex.bean.Person;
import com.runtai.besselarcindex.bean.PersonList;
import com.runtai.besselarcindexlibrary.utils.AsyncResonseHandler;
import com.runtai.besselarcindexlibrary.utils.InternalStorageUtils;
import com.runtai.besselarcindexlibrary.view.LetterIndexer;
import com.runtai.besselarcindexlibrary.view.PinnedHeaderListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends Activity {

    private PinnedHeaderListView pinnedHeaderListView;
    private ArrayList<Person> persons;                                //英雄好汉列表数据集 （除过分组标签，列表所有数据，无序）
    private LinkedHashMap<String, List<Person>> personMpas;           //英雄好汉列表 分组标签对应的数据集合（有序）
    private PinnedHeaderListViewAdapter<Person> adapter;              //英雄好汉列表适配器
    private LetterIndexer letterIndexer;
    private TextView tv_index_center;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        listen();
        //初始化数据
        initData();
    }

    private void initView() {
        pinnedHeaderListView = (PinnedHeaderListView) findViewById(R.id.pinnedheader_listview);
        pinnedHeaderListView.setPinnedHeaderView(this.getLayoutInflater().inflate(
                R.layout.pinnedheaderlistview_header_layout, pinnedHeaderListView, false));
        letterIndexer = (LetterIndexer) findViewById(R.id.letter_index);
        tv_index_center = (TextView) findViewById(R.id.tv_index_center);
    }

    private void listen() {
        letterIndexer.setOnTouchLetterChangedListener(new LetterIndexer.OnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String letter, int index) {

                // 从集合中查找第一个拼音首字母为letter的索引, 进行跳转
                for (int i = 0; i < persons.size(); i++) {
                    Person person = persons.get(i);
                    String s = person.getLetter();
                    if (TextUtils.equals(s, letter)) {
                        // 匹配成功, 中断循环, 将列表移动到指定的位置
                        pinnedHeaderListView.setSelection(i);
                        break;
                    }
                }
            }

            @Override
            public void onTouchActionUp(String letter) {
                showLetter(letter);
            }
        });
    }

    /**
     * 显示字母提示
     *
     * @param letter
     */
    protected void showLetter(String letter) {
        tv_index_center.setVisibility(View.VISIBLE);
        tv_index_center.setText(letter);

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 隐藏
                tv_index_center.setVisibility(View.GONE);
            }
        }, 1000);

    }

    protected void initData() {
        InternalStorageUtils.asynReadInternalFile(this, "names.config", new AsyncResonseHandler() {
            @Override
            protected void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    Gson gson = new Gson();
                    HeroPerson hero = gson.fromJson(content,
                            new TypeToken<HeroPerson>() {
                            }.getType());

                    List<PersonList> personList = hero.getSections();
                    persons = new ArrayList<>();
                    personMpas = new LinkedHashMap<>();

                    //特殊字符
                    List<Person> specialChar = new ArrayList<>();
                    Person charPerson;
                    for (int i = 0; i < 5; i++) {
                        charPerson = new Person();
                        charPerson.setName("#特殊字符" + i);
                        charPerson.setLetter("#");
                        specialChar.add(charPerson);
                    }
                    personMpas.put("特殊字符", specialChar);
                    persons.addAll(specialChar);

                    //得到右侧字母索引的内容
                    int letterLength = personList.size() + personMpas.size();
                    String[] constChar = new String[letterLength];
                    constChar[0] = "#";


                    List<Person> bandItems;
                    for (int i = 0; i < personList.size(); i++) {
                        bandItems = personList.get(i).getPersons();
                        persons.addAll(bandItems);
                        personMpas.put(personList.get(i).getIndex(), bandItems);
                        constChar[i + 1] = personList.get(i).getIndex();
                    }

                    adapter = new PinnedHeaderListViewAdapter<>(MainActivity.this, personMpas, pinnedHeaderListView,
                            letterIndexer, constChar, 20, 20);
                    pinnedHeaderListView.setOnScrollListener(adapter);
                    pinnedHeaderListView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
