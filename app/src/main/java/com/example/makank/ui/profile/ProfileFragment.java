package com.example.makank.ui.profile;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.makank.R;
import com.example.makank.adapter.TabViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class ProfileFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager firstViewPager;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firstViewPager = view.findViewById(R.id.viewpager_content);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(firstViewPager);
        setupViewPager(firstViewPager);
        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PersonalFragment(),"الحساب الشخصي");
        adapter.addFragment(new GroupFragment(), "المجموعة");
        adapter.addFragment(new RequestFragment(), "الطلبات");
        viewPager.setAdapter(adapter);
            }
    @Override
    public void onStart() {
        super.onStart();
        setupViewPager(firstViewPager);

    }
}

