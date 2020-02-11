package com.java.cartelera;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class AdaptaPag extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private int totalPag;
    private Fragment principal, favoritos;

    public AdaptaPag(FragmentManager fm, int totalPag) {
        super(fm);
        this.totalPag = totalPag;
        principal = new PrincipalFragment();
        favoritos = new FavoritosFragment();
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0)
            return principal;
        else
            return favoritos;
    }

    @Override
    public int getCount() {
        return totalPag;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "Películas";
        else
            return "Favoritos";
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position == 0)
            favoritos.onResume();
        else
            principal.onResume();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
