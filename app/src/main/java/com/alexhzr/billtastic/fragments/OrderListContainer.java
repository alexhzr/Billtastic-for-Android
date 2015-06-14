package com.alexhzr.billtastic.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.util.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


public class OrderListContainer extends Fragment {

    static class ItemOrderList {
        private boolean todas;
        private int idCliente;
        private int estadoFactura;
        private String query;

        ItemOrderList(boolean todas, int idCliente, int estadoFactura, String query) {
            this.todas = todas;
            this.idCliente = idCliente;
            this.estadoFactura = estadoFactura;
            this.query = query;
        }

        Fragment createFragment() {
            return OrderList.newInstance(todas, query, idCliente, estadoFactura);
        }

        public boolean isTodas() {
            return todas;
        }

        public int getIdCliente() {
            return idCliente;
        }

        public int getEstadoFactura() {
            return estadoFactura;
        }

        public String getQuery() {
            return query;
        }
    }

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private List<ItemOrderList> mTabs = new ArrayList<ItemOrderList>();
    private int idCliente;
    private String query;
    private boolean todas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idCliente = getArguments().getInt("idCliente");
        query = getArguments().getString("query");
        todas = getArguments().getBoolean("todas");

        mTabs.add(new ItemOrderList (todas, idCliente, 2, query));

        mTabs.add(new ItemOrderList (todas, idCliente, 0, query));

        mTabs.add(new ItemOrderList (todas, idCliente, 1, query));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_list_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                switch (mTabs.get(position).getEstadoFactura()){
                    case 0:
                        return getResources().getColor(R.color.tab_red);
                    case 1:
                        return getResources().getColor(R.color.tab_green);
                    case 2:
                        return getResources().getColor(R.color.tab_yellow);
                    default:
                        return Color.BLUE;
                }
            }
        });
    }

    class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            switch (mTabs.get(position).getEstadoFactura()){
                case 0:
                    title = getString(R.string.s_orderlist_pending);
                    break;
                case 1:
                    title = getString(R.string.s_orderlist_paid);
                    break;
                case 2:
                    title = getString(R.string.s_orderlist_draft);
                    break;
                default:
                    title = getString(R.string.s_orderlist_other);
                    break;
            }
            return title;
        }
    }
}