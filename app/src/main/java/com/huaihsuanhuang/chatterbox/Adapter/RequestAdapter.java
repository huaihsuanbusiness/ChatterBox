package com.huaihsuanhuang.chatterbox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huaihsuanhuang.chatterbox.Account.ProfileActivity;
import com.huaihsuanhuang.chatterbox.Model.Requestlistmodel;
import com.huaihsuanhuang.chatterbox.R;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private Context context;
    private List<Requestlistmodel> requestlistmodelList;

    public RequestAdapter(Context context, List<Requestlistmodel> requestlistmodelList) {
        this.context = context;
        this.requestlistmodelList = requestlistmodelList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.users_item, parent, false);
        return new RequestAdapter.RequestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, final int position) {
        holder.user_name.setText(requestlistmodelList.get(position).getName());
        holder.users_status.setText("Hi I sent a request to you!");
        String thumb = requestlistmodelList.get(position).getThumb_image();
        if (!thumb.equals("null")) {
            Glide.with(context)
                    .load(thumb)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(holder.users_image);
        } else {
            holder.users_image.setImageResource(R.mipmap.empty_profile);
        }

        holder.users_carditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("uid", requestlistmodelList.get(position).getUserid());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return requestlistmodelList.size();

    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        ImageView users_image;
        TextView users_status;
        CardView users_carditem;

        RequestViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.users_name);
            users_image = itemView.findViewById(R.id.users_image);
            users_status = itemView.findViewById(R.id.users_status);
            users_carditem = itemView.findViewById(R.id.users_carditem);


        }
    }
}
