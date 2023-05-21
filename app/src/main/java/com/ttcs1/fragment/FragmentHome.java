package com.ttcs1.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ttcs1.AnalysisActivity;
import com.ttcs1.R;
import com.ttcs1.adapter.ItemAdapter;
import com.ttcs1.model.Item;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragmentHome extends Fragment implements ItemAdapter.ItemListener {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private int REQUEST_CODE = 123;
    private int REQUEST_IMAGE_CAPTURE = 141;
    private static final boolean QUANT = false;
    private static final int INPUT_SIZE = 224;
    private String MODEL_PATH ;
    private String LABEL_PATH ;
    private String NAME_LEAF;
    String currentPhotoPath;
    private Item itemuri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        adapter = new ItemAdapter(getList());
        GridLayoutManager manager = new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
    }

    private List<Item> getList(){
        List<Item> list = new ArrayList<>();
        list.add(new Item(R.drawable.tomato,"tomato.txt","tomato.tflite","Cà chua"));
        list.add(new Item(R.drawable.cherries,"cherry.txt","cherry.tflite","Anh đào"));
        list.add(new Item(R.drawable.corn,"corn.txt","corn.tflite","Ngô"));
        list.add(new Item(R.drawable.grapes,"grape.txt","grape.tflite","Nho"));
        list.add(new Item(R.drawable.peach,"peach.txt","peach.tflite","Đào"));
        list.add(new Item(R.drawable.chili,"pepper.txt","pepper.tflite","Ớt"));
        list.add(new Item(R.drawable.sweetpotato,"potato.txt","potato.tflite","Khoai tây"));
        list.add(new Item(R.drawable.soybean,"soybean.txt","soybean.tflite","Đậu tương"));
        list.add(new Item(R.drawable.apple,"apple.txt","apple.tflite","Táo"));
        list.add(new Item(R.drawable.strawberry,"strawberry.txt","strawberry.tflite","Dâu tây"));
//        list.add(new Item(R.drawable.tomato,"potato.txt","potato.tflite","Khoai tây"));

        return list;
    }

    @Override
    public void onItemClick(View view, int position) {
        itemuri = getList().get(position);
        try {
            dispatchTakePictureIntent();
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(e.toString());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.aiapp.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
        Toast.makeText(getContext(), currentPhotoPath, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            try{
                galleryAddPic();
                Intent intent = new Intent(getActivity(), AnalysisActivity.class);
                intent.putExtra("uri",currentPhotoPath);
                intent.putExtra("itemuri",itemuri);
                startActivity(intent);
            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(e.toString());
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
