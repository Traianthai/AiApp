package com.ttcs1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ttcs1.model.Leaf;

import java.io.ByteArrayInputStream;

public class DescriptionActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView txtDescription,txtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        initView();
    }

    private void initView() {
        imageView = findViewById(R.id.imageView);
        txtDescription = findViewById(R.id.txtDecripsion);
        txtTitle = findViewById(R.id.txtTitle);
        try {
            Intent intent = getIntent();
            Leaf leaf = (Leaf) intent.getSerializableExtra("leaf");
            Bitmap bitmap = setPic(leaf.getImg());
            imageView.setImageBitmap(getRoundedCornerBitmap(bitmap,100));
            txtDescription.setText(leaf.getInf());
            txtTitle.setText(leaf.getName());
        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(DescriptionActivity.this);
            builder.setMessage(e.toString());
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        // Thêm viền gradient
        final int startColor = 0xff777777; // Màu xanh lá cây
        final int endColor = 0xffABABAB; // Màu xanh dương
        final int strokeWidth = 50; // Độ dày của viền

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // Vẽ viền gradient
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);

        Shader shader = new LinearGradient(rectF.left, rectF.top, rectF.right, rectF.bottom, startColor, endColor, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        paint.setStrokeWidth(strokeWidth);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        return output;
    }
}