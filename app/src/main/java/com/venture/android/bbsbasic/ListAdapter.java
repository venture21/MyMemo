package com.venture.android.bbsbasic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.venture.android.bbsbasic.domain.Memo;
import com.venture.android.bbsbasic.interfaces.ListInterface;

import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context context;
    Memo memo;
    List<Memo> datas;
    Intent intent = null;
    ListInterface listInterface = null;
    //ImageView preView;

    public ListAdapter(Context context, List<Memo> datas) {
        this.context = context;
        this.datas = datas;
        this.listInterface = (ListInterface) context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        memo = datas.get(position);

        holder.txtTitle.setText(memo.getTitle());
        holder.txtContents.setText(memo.getContents());
        holder.txtDate.setText(memo.getDate().toString());
        holder.position = position;
        //holder.checkBox.setChecked(memo.isCheckbox());
        if(memo.getUri()!=null) {
            Glide.with(context)
                    .load(Uri.parse(memo.getUri())) // 1. 로드할 대상 Uri
                    .override(70, 50)
                    .into(holder.preView);     // 2. 입력될 이미지뷰
        }

        if(memo.getVisible()){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContents;
        TextView txtTitle;
        TextView txtDate;
        ImageView preView;
        CheckBox checkBox;

        int position;
        CardView cardView;


        public ViewHolder(final View view) {
            super(view);
            txtTitle    = (TextView) view.findViewById(R.id.txtTitle);
            txtDate     = (TextView) view.findViewById(R.id.txtDate);
            txtContents = (TextView) view.findViewById(R.id.txtContents);
            preView     = (ImageView) view.findViewById(R.id.preView);
            checkBox    = (CheckBox) view.findViewById(R.id.checkBox);

            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnClickListener(clickListener);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    memo.setCheckbox(buttonView.isChecked());
                    Log.i(TAG,"======= CheckBox =======================");
                    for(Memo memo:datas){
                        Log.i(TAG,"= "+memo.getId());
                        Log.i(TAG,"= "+memo.isCheckbox());
                    }
                }
            });
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cardView:

                        listInterface.goDetail(position);
                        break;
                }
            }
        };
    }


}