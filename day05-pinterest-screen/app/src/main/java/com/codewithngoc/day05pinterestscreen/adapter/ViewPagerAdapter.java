package com.codewithngoc.day05pinterestscreen.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.codewithngoc.day05pinterestscreen.fragment.ForYouFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ForYouFragment(); // Sử dụng cùng fragment cho mỗi tab (hoặc tạo các fragment khác nhau)
    }

    @Override
    public int getItemCount() {
        return 4; // For You, Popular, Design, Tech
    }
}