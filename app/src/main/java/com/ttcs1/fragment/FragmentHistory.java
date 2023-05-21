package com.ttcs1.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class FragmentHistory extends Fragment implements RecycleViewAdapter.ItemListener {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private SQLiteHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        adapter = new RecycleViewAdapter();
        db = new SQLiteHelper(getContext());
        List<Leaf> list = db.getAll();
        adapter.setList(list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
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
        Leaf leaf = adapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.delete_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        TextView messageView = dialogView.findViewById(R.id.message);
        TextView cancelButton =  dialogView.findViewById(R.id.cancelBT);
        TextView okButton = dialogView.findViewById(R.id.deleteBT);
        messageView.setText("Phân tích về lá "+ leaf.getName() + ": " + leaf.getInf() + "sẽ bị xóa ");
//        Toast.makeText(getContext(),"xóa vị trí" + position,Toast.LENGTH_SHORT).show();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteItem(position+1);
                List<Leaf> list = db.getAll();
                adapter.setList(list);
                Toast.makeText(getContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Leaf> list = db.getAll();
        adapter.setList(list);
    }
}
