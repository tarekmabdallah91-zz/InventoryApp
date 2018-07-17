/*
Copyright 2018 tarekmabdallah91@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.example.tarek.inventoreyapp.adpater;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.views.ItemFragment;

public class FragmentAdapter extends FragmentPagerAdapter {


    private final String[] titles;// = new String[]{""all products" , synced" , "not synced" ,"favoured"};

    public FragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        titles = context.getResources().getStringArray(R.array.fragment_titles);

    }

    @Override
    public Fragment getItem(int position) {
        ItemFragment itemFragment = new ItemFragment();
        itemFragment.setFragmentItemPosition(position);
        return itemFragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
