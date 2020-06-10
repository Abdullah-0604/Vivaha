package com.example.zorker.vivaha.Chat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zorker.vivaha.R;


public class ChatFragment extends Fragment {

    private ViewPager viewPager_chatFragment;
    private TabLayout tabLayout_chat_fragment;
    private FragmentPagerAdapter fragmentPagerAdapter;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager_chatFragment = (ViewPager) view.findViewById(R.id.viewpager_chat_fragment);
        tabLayout_chat_fragment = (TabLayout) view.findViewById(R.id.tablayout_chat_fragment);
        fragmentAdapter();
        viewPager_chatFragment.setAdapter(fragmentPagerAdapter);
        tabLayout_chat_fragment.setupWithViewPager(viewPager_chatFragment);
        defaultFragment();

    }

    private void defaultFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.viewpager_chat_fragment,new ConversationFragment());
        fragmentTransaction.commit();
    }

    private void fragmentAdapter() {

        fragmentPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i)
                {
                    case 0:
                        ConversationFragment conversationFragment = new ConversationFragment();
                        return conversationFragment;

                    case 1:
                        FriendsFragment friendsFragment = new FriendsFragment();
                        return friendsFragment;
                    default:
                        return null;
                }

            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position)
                {
                    case 0:
                        return "CONVERSATIONS";
                    case 1:
                        return "FRIENDS";
                        default:
                            return null;
                }
            }
        };
    }
}