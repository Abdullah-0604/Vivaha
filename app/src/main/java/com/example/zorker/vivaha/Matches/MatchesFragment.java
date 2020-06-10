package com.example.zorker.vivaha.Matches;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zorker.vivaha.Account.QuestionsModalClass;
import com.example.zorker.vivaha.Account.UserDetails;
import com.example.zorker.vivaha.R;
import com.example.zorker.vivaha.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MatchesFragment extends Fragment {

    private FloatingActionButton floatingActionButton_search;
    private RecyclerView recyclerView_matches;
    private MatchesAdapter matchesAdapter;
    private List<UserDetails> uList = new ArrayList<>();
    private DatabaseReference mref;
    private View view;
    String gender,religion,community,age_from,age_to,height_feet,height_inch;

    public MatchesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view==null)
        view = inflater.inflate(R.layout.fragment_matches, container, false);

        RelativeLayout relativeLayout = view.findViewById(R.id.relative_matches);
        floatingActionButton_search = (FloatingActionButton) view.findViewById(R.id.floatingButton_search);
        mref = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView_matches = view.findViewById(R.id.recyclerView_matches);
        recyclerView_matches.setHasFixedSize(true);
        recyclerView_matches.setLayoutManager(new LinearLayoutManager(getContext()));


        matchesAdapter = new MatchesAdapter(uList, getContext());


        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                UserDetails uClass = dataSnapshot.getValue(UserDetails.class);

                    String uid = uClass.getU_id();
                    if (!FirebaseAuth.getInstance().getUid().equals(uid)) {
                        uList.add(uClass);
                    }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        matchesAdapter.notifyDataSetChanged();
        recyclerView_matches.setAdapter(matchesAdapter);



        searchFunction();

      return view;
    }

    private void searchFunction() {
        floatingActionButton_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_box_search,null);
                final Spinner spin_search_gender = (Spinner) view1.findViewById(R.id.spinner_search_gender);
                ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item);
                adapter.addAll("Male","Female");
                spin_search_gender.setAdapter(adapter);
                spin_search_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        gender = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final Spinner spin_search_religion = (Spinner)view1.findViewById(R.id.spinner_search_religion);
                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item);
                arrayAdapter.addAll("Hindu","Buddhist", "Muslim","Christian","No Religion","spiritual");
                spin_search_religion.setAdapter(arrayAdapter);
                spin_search_religion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        religion = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final Spinner spin_search_community = (Spinner) view1.findViewById(R.id.spinner_search_community);
                ArrayAdapter adapterrel = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item);
                adapterrel.addAll("Brahmin(upadhaya)","Brahmin(Jaisi)","Chhetri","Magar","Rai","Limbu","Gurung","Kami","Damai","Sarki","Newar","etc");
                spin_search_community.setAdapter(adapterrel);
                spin_search_community.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        community = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final Spinner spin_age_from = (Spinner) view1.findViewById(R.id.spinner_search_agefrom);
                ArrayAdapter<Integer> agefromadapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item);
                ArrayList<Integer> ageList = new ArrayList<>();
                for ( int i =18; i<=70;i++)
                    ageList.add(i);
                agefromadapter.addAll(ageList);

                spin_age_from.setAdapter(agefromadapter);
                spin_age_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        age_from = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                final Spinner spin_age_to = (Spinner) view1.findViewById(R.id.spinner_search_ageto);
                ArrayList<Integer> ageTo = new ArrayList<>();
                for (int j = 18 ; j<= 70 ; j++)
                    ageTo.add(j);
                ArrayAdapter ageToAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item);
                ageToAdapter.addAll(ageTo);
                spin_age_to.setAdapter(ageToAdapter);
                spin_age_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        age_to = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final Spinner spin_height_feet = (Spinner) view1.findViewById(R.id.spinner_search_height_feet);
                ArrayAdapter<Integer> adapter_feet = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item);
                ArrayList<Integer> feet_list= new ArrayList<>();
                for (int i = 3;i<=9;i++)
                    feet_list.add(i);
                adapter_feet.addAll(feet_list);
                spin_height_feet.setAdapter(adapter_feet);
                spin_height_feet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        height_feet = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final Spinner spin_height_inch = (Spinner) view1.findViewById(R.id.spinner_search_height_inch);
                ArrayAdapter<Integer> adapter_inch = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item);
                ArrayList<Integer> inch_list = new ArrayList<>();
                for (int j=0;j<=9;j++)
                    inch_list.add(j);
                adapter_inch.addAll(inch_list);
                spin_height_inch.setAdapter(adapter_inch);
                spin_height_inch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        height_inch = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //Button button_search_dialog = (Button) view1.findViewById(R.id.button_search);

                builder.setView(view1)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })
                        .setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Toast.makeText(getContext(), community, Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putString("gender_search",gender);
                                bundle.putString("religion_search",religion);
                                bundle.putString("community_search",community);
                                bundle.putString("age_from",age_from);
                                bundle.putString("age_to",age_to);
                                bundle.putString("height_feet",height_feet);
                                bundle.putString("height_inch",height_inch);

                                Intent search_intent = new Intent(getActivity(),SearchActivity.class);
                                search_intent.putExtras(bundle);
                                startActivity(search_intent);



                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();




            }
        });

    }


}
