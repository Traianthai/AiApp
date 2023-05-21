package com.ttcs1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ttcs1.dal.SQLiteHelper;
import com.ttcs1.ml.SsdMobilenetV11Metadata1;
import com.ttcs1.model.Item;
import com.ttcs1.model.Leaf;
import com.ttcs1.tflite.Classifier;
import com.ttcs1.tflite.TensorFlowImageClassifier;

import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AnalysisActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView txtDescription,txtTitle;

    private static final int INPUT_SIZE = 224;
    private Classifier classifier;
    private Classifier classifier1;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        imageView = findViewById(R.id.imageView);
        Intent intent = getIntent();
        String currentPhotoPath = (String) intent.getExtras().get("uri");
        Item item = (Item) intent.getExtras().get("itemuri");
        Bitmap bitmap = setPic(currentPhotoPath);
        imageView.setImageBitmap(bitmap);

        // đưa về kích thước nhận diện
        ImageProcessor imageProcessor = new ImageProcessor.Builder().add(new ResizeOp(300, 300, ResizeOp.ResizeMethod.BILINEAR)).build();
        TensorImage image = TensorImage.fromBitmap(bitmap);
        image = imageProcessor.process(image);
        List<String> labels;

        SsdMobilenetV11Metadata1 model;
        try {
            labels = FileUtil.loadLabels(this, "labels.txt");
            model = SsdMobilenetV11Metadata1.newInstance(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SsdMobilenetV11Metadata1.Outputs outputs = model.process(image);
        float[] locations = outputs.getLocationsAsTensorBuffer().getFloatArray();
        float[] classes = outputs.getClassesAsTensorBuffer().getFloatArray();
        float[] scores = outputs.getScoresAsTensorBuffer().getFloatArray();
        float[] numberOfDetections = outputs.getNumberOfDetectionsAsTensorBuffer().getFloatArray();

        Bitmap bitmap_classifier = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);

        try {
            classifier1 = TensorFlowImageClassifier.create(
                    getAssets(),
                    "model.tflite",
                    "model.txt",
                    INPUT_SIZE,
                    true);
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AnalysisActivity.this);
            builder.setMessage(e.toString());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        final List<Classifier.Recognition> results2 = classifier1.recognizeImage(bitmap_classifier);
//        Toast.makeText(AnalysisActivity.this,results2.get(0).getTitle(),Toast.LENGTH_SHORT).show();

        if(results2.get(0).getTitle().equals("Unknow")){
            String res = "Phát hiện:";
            for (int index = 0; index < scores.length; index++) {
                if (scores[index] > 0.5) {
                    res +=" "+  labels.get((int) classes[index]);
                }
            }
            res += "\n \n";
            res += "Hmmm... Có vẻ bạn chưa cung cấp đúng hình ảnh.";
//            Toast.makeText(AnalysisActivity.this,"Không xác định",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(AnalysisActivity.this);
            builder.setMessage(res);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            try {
                classifier = TensorFlowImageClassifier.create(
                        getAssets(),
                        item.getModel(),
                        item.getLabel(),
                        INPUT_SIZE,
                        false);
                final List<Classifier.Recognition> results1 = classifier.recognizeImage(bitmap_classifier);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap_classifier.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte imageInByte[] = stream.toByteArray();
                String res = "";
                for (Classifier.Recognition i : results1) {
                    res += i.toString() + "\n";
                }
                SQLiteHelper db = new SQLiteHelper(getApplicationContext());
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Leaf l = new Leaf(db.getRecordCount()+1,item.getName(),res,currentPhotoPath,simpleDateFormat.format(date),imageInByte);
                db.addItem(l);
                db.close();
                Intent desc = new Intent(AnalysisActivity.this, DescriptionActivity.class);
                desc.putExtra("leaf",l);
                startActivity(desc);
            } catch (Exception e) {
                Toast.makeText(AnalysisActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap setPic(String currentPhotoPath){
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = rotateBitmap(BitmapFactory.decodeFile(currentPhotoPath, bmOptions),90);
        return  bitmap;
    }
    public Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}