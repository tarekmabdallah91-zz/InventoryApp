/*
Copyright 2018 tarekmabdallah91@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.example.tarek.inventoreyapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


public class ImageUtility {

    /**
     * to convert Selected image uri to Bitmap
     * create BitmapFactory.Options object and assign it's "inJustDecodeBounds = true";
     * then convert the uri to a decodedStream to get width and height from it
     * then get the suitable scale according to the current selected image size by while loop ..
     * then create new BitmapFactory.Options object and assign the scale to it's "inSampleSize"
     * then convert the uri of the selected image to a a decodedStream to return it as a Bitmap
     *
     * @param context          of the activity
     * @param selectedImageUri the uri of the selected image
     * @param REQUIRED_SIZE    the size that we want to set the image
     * @return Bitmap
     */
    public static Bitmap uriToBitmap(Context context, Uri selectedImageUri, int REQUIRED_SIZE) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImageUri),
                    null, options);

            int width = options.outWidth;
            int height = options.outHeight;
            int scale = ProductUtility.ONE;

            while (true) {
                if (width / ProductUtility.TWO < REQUIRED_SIZE || height / ProductUtility.TWO < REQUIRED_SIZE) {
                    break;
                }
                width /= ProductUtility.TWO;
                height /= ProductUtility.TWO;
                scale *= ProductUtility.TWO;
            }
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = scale;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImageUri),
                    null, options2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * to convert bitmap to byte[]
     *
     * @param bitmap image as bitmap will be compressed as PNG and ZERO quality
     * @return as a ByteArrayOutputStream object
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, ProductUtility.ZERO, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * to convert byte[] to bitmap
     *
     * @param imageByteArray from db
     * @return as Bitmap
     */
    public static Bitmap byteArrayToBitmap(byte[] imageByteArray) {
        if (imageByteArray == null) return null;
        return BitmapFactory.decodeByteArray(imageByteArray, ProductUtility.ZERO, imageByteArray.length);
    }

    /**
     * to convert image resource id to bitmap
     *
     * @param context    of the activity
     * @param idResource of the image wanted to be converted
     * @return bitmap
     */
    private static Bitmap intIdResourceToBitmap(Context context, int idResource) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeResource(context.getResources(), idResource, options);
    }

    /**
     * one method to convert @param imageResourceId to byte[] directly
     *
     * @param context         of the activity
     * @param imageResourceId wanted to be converted
     * @return byte[]
     */
    public static byte[] intResourceIdToByteArray(Context context, int imageResourceId) {
        return bitmapToBytes(intIdResourceToBitmap(context, imageResourceId));
    }

    /**
     * one method to convert @param selectedImageUri to byte[] directly
     *
     * @param context          of the activity
     * @param selectedImageUri wanted to be converted
     * @param REQUIRED_SIZE    wanted image size
     * @return byte[]
     */
    public static byte[] uriToByteArray(Context context, Uri selectedImageUri, int REQUIRED_SIZE) {
        return bitmapToBytes(uriToBitmap(context, selectedImageUri, REQUIRED_SIZE));
    }
}
