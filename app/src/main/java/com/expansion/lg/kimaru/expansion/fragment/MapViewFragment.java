package com.expansion.lg.kimaru.expansion.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 3/30/17.
 */

public class MapViewFragment extends Fragment {

    private RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.recycleview_layout, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        createAdapter(mRecyclerView);
        MainActivity.CURRENT_TAG =MainActivity.TAG_MAP_VIEW;
        MainActivity.backFragment = new MappingFragment();
        return v;
    }

    private void createAdapter(RecyclerView recyclerView){
        final List<DefaultListMenu> content = new ArrayList<>();
        content.add(new DefaultListMenu("SubCounties",null,  new SubCountiesFragment()));
        content.add(new DefaultListMenu("Link Facilities", null, new LinkFacilitiesFragment()));
        // content.add(new DefaultListMenu("View Villages", null, new NewInterviewFragment()));

        final ParallaxRecyclerAdapter<DefaultListMenu> adapter = new ParallaxRecyclerAdapter<>(content);

        View header = getActivity().getLayoutInflater().inflate(R.layout.recycleview_header, recyclerView, false);
        ImageView headerImage = (ImageView) header.findViewById(R.id.imageViewHeader);
        headerImage.setImageResource(R.drawable.county);

        adapter.setParallaxHeader(header, recyclerView);
        adapter.setData(content);
        adapter.implementRecyclerAdapterMethods(new ParallaxRecyclerAdapter.RecyclerAdapterMethods() {
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                ((ViewHolder) viewHolder).textView.setText(adapter.getData().get(i).getTitle());
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                final ViewHolder holder = new ViewHolder(getActivity().getLayoutInflater().inflate(R.layout.recycleview_row, viewGroup, false));
                return holder;
            }

            @Override
            public int getItemCount() {
                return content.size();
            }
        });
        adapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View view) {
                //((MainActivity) getActivity()).setActionBarAlpha(percentage);
            }
        });
        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View view, int i) {
                if (i >=0 ){
//                    Intent intent = new Intent(getActivity(), adapter.getData().get(i).getClass());
//                    startActivity(intent);
                    /**
                     *
                     *  MapViewFragment mappingViewFragment = new MapViewFragment();
                     Fragment fragment = mappingViewFragment;
                     FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                     fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                     android.R.anim.fade_out);
                     fragmentTransaction.replace(R.id.frame, fragment, "mappingsview");

                     fragmentTransaction.commitAllowingStateLoss();
                     */

                    Fragment fragment;
                    fragment = adapter.getData().get(i).getFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    fragmentTransaction.replace(R.id.frame, fragment, "MappingView");
                    fragmentTransaction.commitAllowingStateLoss();


                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public ViewHolder(View itemView){
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

}
