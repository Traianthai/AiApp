package com.ttcs1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttcs1.DescriptionActivity;
import com.ttcs1.R;
import com.ttcs1.adapter.RecycleViewAdapter;
import com.ttcs1.dal.SQLiteHelper;
import com.ttcs1.model.Leaf;

import java.util.List;


public class FragmentSearch extends Fragment  implements RecycleViewAdapter.ItemListener{
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private SQLiteHelper db;
    SearchView searchView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        searchView = view.findViewById(R.id.search);
        adapter = new RecycleViewAdapter();
        db = new SQLiteHelper(getContext());
        List<Leaf> list = db.getAll();
        adapter.setList(list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Leaf> leafList = db.getByName(newText);
                adapter.setList(leafList);
                return true;
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Leaf leaf = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
        intent.putExtra("leaf",leaf);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
    @Override
    public void onResume() {
        super.onResume();
        List<Leaf> list = db.getAll();
        adapter.setList(list);
    }
}
