package com.ttcs1.tflite;

import android.graphics.Bitmap;

import java.util.List;


public interface Classifier {

    class Recognition {
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         * * Một định danh duy nhất cho những gì đã được công nhận. Cụ thể cho lớp, không phải là trường hợp của
         *          * đối tượng.
         */
        private final String id;

        /**
         * Display name for the recognition. Tên hiển thị.
         */
        private final String title;

        /**
         * Whether or not the model features quantized or float weights. Mô hình có các trọng số lượng tử hóa hoặc trọng lượng thả nổi hay không.
         */
        private final boolean quant;

        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better.
         */
        private final Float confidence;

        public Recognition(
                final String id, final String title, final Float confidence, final boolean quant) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.quant = quant;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        @Override
        public String toString() {
            String resultString = "";
            if (title != null) {
                resultString += title + " ";
            }
            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }
            return resultString.trim();
        }
    }


    List<Recognition> recognizeImage(Bitmap bitmap);

    void close();
}
