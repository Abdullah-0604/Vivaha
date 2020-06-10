package com.example.zorker.vivaha;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zorker.vivaha.Account.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.MyViewAdapter> {
   private List<UserDetails> mlist = new ArrayList<>();
   private Context mcontext;

   public SearchRecyclerViewAdapter(List<UserDetails> list,Context context)
    {

        mlist = list;
        mcontext = context;


    }
    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       View view = LayoutInflater.from(mcontext).inflate(R.layout.rv_search_container_list,null,false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewAdapter myViewAdapter, int i) {
        myViewAdapter.name.setText(mlist.get(i).getU_fname() + " " + mlist.get(i).getU_lname());
        myViewAdapter.age.setText(mlist.get(i).getU_age()+"");
        myViewAdapter.gender.setText(mlist.get(i).getU_gender());
        myViewAdapter.religion.setText(mlist.get(i).getU_religion()+",");
        myViewAdapter.community.setText(mlist.get(i).getU_community());
        myViewAdapter.language.setText(mlist.get(i).getU_language());
        myViewAdapter.height_feet.setText(mlist.get(i).getU_height_feet()+"");
        myViewAdapter.height_inch.setText(mlist.get(i).getU_height_inch()+"");
        final String user_id = mlist.get(i).getU_id().toString();
        Picasso.get().load(mlist.get(i).getU_search_picture()).placeholder(R.mipmap.default_profile).into(myViewAdapter.iv_profile_pic);
        myViewAdapter.button_search_list_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent usersprofileIntent = new Intent(mcontext,OtherUsersProfile.class);
                usersprofileIntent.putExtra("other_user_id",user_id);
                mcontext.startActivity(usersprofileIntent);


            }
        });



    }

    @Override
    public int getItemCount() {

        return mlist.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder
    {
        TextView name,age,gender,religion,community,language,height_feet,height_inch;
        Button button_search_list_view_profile;
        CircleImageView iv_profile_pic;
        public MyViewAdapter(@NonNull View itemView) {
            super(itemView);
            iv_profile_pic = (CircleImageView) itemView.findViewById(R.id.iv_profile_Pic_search_list);
            name = (TextView) itemView.findViewById(R.id.tv_search_list_name);
            age = (TextView) itemView.findViewById(R.id.tv_search_list_age);
            gender = (TextView) itemView.findViewById(R.id.tv_search_list_gender);
            religion = (TextView) itemView.findViewById(R.id.tv_search_list_religion);
            community = (TextView) itemView.findViewById(R.id.tv_search_list_community);
            language = (TextView) itemView.findViewById(R.id.tv_search_list_language);
            height_feet = (TextView) itemView.findViewById(R.id.tv_search_list_height_feet);
            height_inch = (TextView) itemView.findViewById(R.id.tv_search_list_height_inch);
            button_search_list_view_profile = (Button) itemView.findViewById(R.id.button_search_list_view_profile);




        }
    }
}
