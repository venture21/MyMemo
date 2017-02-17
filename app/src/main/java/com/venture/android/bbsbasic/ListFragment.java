package com.venture.android.bbsbasic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.venture.android.bbsbasic.domain.Memo;
import com.venture.android.bbsbasic.interfaces.ListInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class ListFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<Memo> datas = new ArrayList<>();

    Context context = null;
    ListInterface listInterface = null;

    View view = null;
    RecyclerView recyclerView;
    ListAdapter listAdapter;

    ImageButton btnNew;
    ImageButton btnDel;
    ImageButton btnMultiSel;


    public ListFragment() {
    }

    public static ListFragment newInstance(int columnCount) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null)
            return view;

        view = inflater.inflate(R.layout.layout_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        listAdapter = new ListAdapter(context, datas);
        recyclerView.setAdapter(listAdapter);

        btnNew  = (ImageButton) view.findViewById(R.id.btnNew);
        btnMultiSel  = (ImageButton) view.findViewById(R.id.btnMultiSel);
        btnDel = (ImageButton) view.findViewById(R.id.btnDel);

        btnNew.setOnClickListener(this);
        btnMultiSel.setOnClickListener(this);
        btnDel.setOnClickListener(this);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.listInterface = (ListInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setData(List<Memo> datas){
        this.datas = datas;
    }

    public void refreshAdapter() {
        listAdapter = new ListAdapter(context, datas);
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNew:
                listInterface.goDetail();
                break;
            case R.id.btnMultiSel:
                Log.i(TAG,"========= Press btnMultiDel ==========");
                btnMultiSel.setVisibility(v.GONE);
                btnDel.setVisibility(v.VISIBLE);

                for(Memo memo:datas){
                    memo.setVisible(true);
                }
                listAdapter = new ListAdapter(context, datas);
                recyclerView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();

//                    for(int i=0;i<datas.size();i++) {
//                        Log.i(TAG,"= "+i);
//                        Log.i(TAG,"= "+datas.get(i).isCheckbox());
//                    }
                break;
            case R.id.btnDel:
                Log.i(TAG,"=========== Press btnDel =============");
                btnDel.setVisibility(v.GONE);
                btnMultiSel.setVisibility(v.VISIBLE);

                for(int i=0;i<datas.size();i++){
                    if(datas.get(i).isCheckbox()) {
                        try {
                            Log.i(TAG,"======== Goto delFromList =============");
                            Log.i(TAG,"= "+datas.get(i).getId());
                            listInterface.delFromList(datas.get(i));

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                for(Memo memo:datas){
                    memo.setVisible(false);
                }
                listAdapter = new ListAdapter(context, datas);
                recyclerView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
                break;

        }
    }
}