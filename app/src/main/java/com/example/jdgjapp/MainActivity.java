package com.example.jdgjapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRgTab = (RadioGroup) findViewById(R.id.rg_main);
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_news:
                        changeFragment(NewsFragment.class.getName());
                        break;
                    case R.id.rb_work:
                        changeFragment(WorkFragment.class.getName());
                        break;
                    case R.id.rb_address_list:
                        changeFragment(AddressListFragment.class.getName());
                        break;
                    case R.id.rb_my:
                        changeFragment(MyFragment.class.getName());
                        break;
                }
            }
        });
        if(savedInstanceState == null){
            changeFragment(NewsFragment.class.getName());
        }
    }


    /**
     * show target fragment
     *
     * @param tag
     */
    public void changeFragment(String tag) {
        hideFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (tag.equals(NewsFragment.class.getName())) {
                fragment = NewsFragment.newInstance();
            } else if (tag.equals(WorkFragment.class.getName())) {
                fragment = WorkFragment.newInstance();
            } else if (tag.equals(AddressListFragment.class.getName())) {
                fragment = AddressListFragment.newInstance();
            } else if (tag.equals(MyFragment.class.getName())) {
                fragment = MyFragment.newInstance();
            }
            mFragmentList.add(fragment);
            transaction.add(R.id.fl_container, fragment, fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();

    }

    private void hideFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (Fragment f : mFragmentList) {
            ft.hide(f);
        }
        ft.commit();
    }

}
