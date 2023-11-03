package com.example.pestidentifier;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pestidentifier.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    TextView result, confidence;
    ImageView imageView;
    Button picture, open_gallery;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        //go_to_main = findViewById(R.id.go_to_dash);
        open_gallery = findViewById(R.id.btn_open_gallery);

        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });



        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    public void classifyImage(Bitmap image) {

        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize*imageSize];
            image.getPixels(intValues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF)*(1.f/255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF)*(1.f/255.f));
                    byteBuffer.putFloat((val & 0xFF)*(1.f/255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"aphids", "armyworm", "beetle", "bollworm", "grasshoper", "mites", "mosquito", "sawfly", "stem_borer"};

            if (classes[maxPos] == "aphids" ){

                String boldValue = "\033[0;1m";

                String desc_title = "Description :\n";
                String description = "Aphids are small, soft-bodied insects that feed on the sap of plants. They are common pests of a wide\n" +
                        "variety of plants, including vegetables, fruits, flowers, and trees. Aphids are typically pear-shaped and\n" +
                        "about 1/8 inch long. They can be green, yellow, red, brown, or black in color. Aphids have two long\n" +
                        "antennae and a pair of tube-like structures on their abdomen called cornicles.\n";

                String control_measures_title = "Control Measures :\n";
                String control_measures = "Control with natural or organic sprays like a soap-and-water mixture, neem oil, or\n" +
                        "essential oils.";

                //Combine all the information into a single formatted text line
                String message = desc_title + description + control_measures_title+control_measures;


                result.setText("\t"+classes[maxPos].toUpperCase()+"\n\n"+message);

            }
            else if (classes[maxPos] == "armyworm" ) {

                String boldValue = "\033[0;1m";

                String desc_title = "Description :\n";
                String description = "Armyworms are the caterpillars of certain moth species in the family Noctuidae. They are common pests\n" +
                        "of lawns, pastures, and crops. Armyworms are known for their ability to move in large groups, like an\n" +
                        "army, to find food.\n\n";

                String control_measures_title = "Control Measures :\n";
                String control_measures = "Use neem oil or garlic-based sprays as natural repellents to deter armyworms from\n" +
                        "feeding on plants. Practices such as crop rotation, intercropping, and sanitation can help to reduce the\n" +
                        "risk of armyworm infestations.";

                //Combine all the information into a single formatted text line
                String message = desc_title + description + control_measures_title+control_measures;

                result.setText(classes[maxPos].toUpperCase()+"\n\n"+message);

            }
            else if (classes[maxPos] == "beetle" ) {

                String boldValue = "\033[0;1m";

                String desc_title = "Description :\n";
                String description = "Beetles are insects that form the order Coleoptera, in the superorder Endopterygota. Their front pair of\n" +
                        "wings are hardened into wing-cases, elytra, distinguishing them from most other insects. Beetles come in\n" +
                        "all shapes and sizes, from the tiny grain beetle, which is less than 1 millimeter long, to the giant Goliath\n" +
                        "beetle, which can grow up to 16 centimeters long. Beetles can also be found in a wide variety of colors,\n" +
                        "from bright and flashy to drab and camouflaged.\n\n";

                String control_measures_title = "Control Measures :\n";
                String control_measures = "Handpick adult beetles and larvae if feasible, especially in smaller garden or farming\n" +
                        "areas. Rotate crops to disrupt the beetle's life cycle and reduce their population.";

                //Combine all the information into a single formatted text line
                String message = desc_title + description + control_measures_title+control_measures;


                result.setText(classes[maxPos].toUpperCase()+"\n\n"+message);

            }
            else if (classes[maxPos] == "bollworm" ) {

                String boldValue = "\033[0;1m";

                String desc_title = "Description :\n";
                String description = "The bollworm is a nocturnal insect, meaning that it is most active at night. The adult moths are brown in\n" +
                        "color and have a wingspan of about 1.5 inches. It is a major pest of a wide variety of crops, including\n" +
                        "cotton, maize, sorghum, tomatoes, and tobacco.\n\n";

                String control_measures_title = "Control Measures :\n";
                String control_measures = "Apply systemic insecticides that are absorbed by the plant and circulated throughout,\n" +
                        "making the plant toxic to bollworms feeding on it. Eg transgenic cotton, Bollguard II";

                //Combine all the information into a single formatted text line
                String message = desc_title + description + control_measures_title+control_measures;

                result.setText(classes[maxPos].toUpperCase()+"\n\n"+message);
            }
            else if (classes[maxPos] == "grasshoper" ) {

                String boldValue = "\033[0;1m";

                String desc_title = "Description :\n";
                String description = "Locusts are a type of grasshopper that undergo a behavioral and physiological transformation, turning\n" +
                        "from solitary individuals into highly gregarious and swarming creatures under specific conditions. Locusts\n" +
                        "damage crops by voraciously feeding on plants, consuming leaves, stems, flowers, and seeds.\n\n";

                String control_measures_title = "Control Measures :\n";
                String control_measures = "Combine two quarts of warm water with half a teaspoon of liquid dish soap and add\n" +
                        "three teaspoons of neem oil. Spraying this mixture onto plant will slow down Locusts and even stunt their growth\n";

                //Combine all the information into a single formatted text line
                String message = desc_title + description + control_measures_title+control_measures;


                result.setText(classes[maxPos].toUpperCase()+"\n\n"+message);
            }

            else if (classes[maxPos] == "mites" ) {

                String boldValue = "\033[0;1m";

                String desc_title = "Description :\n";
                String description = "Mites are tiny arachnids that are closely related to spiders and scorpions. Mites are very small, ranging in\n" +
                        "size from about 1/100 of an inch to about 1/10 of an inch long. They are typically brown, red, or black in\n" +
                        "color. Mites have eight legs and two body segments.\n\n";

                String control_measures_title = "Control Measures :\n";
                String control_measures = "Apply natural miticides like diatomaceous earth, which consists of sharp particles that\n" +
                        "can penetrate and dehydrate mites.\n";

                //Combine all the information into a single formatted text line
                String message = desc_title + description + control_measures_title+control_measures;

                result.setText(classes[maxPos].toUpperCase()+"\n\n"+message);
            }


            else if (classes[maxPos] == "mosquito" ) {

                String boldValue = "\033[0;1m";

                String desc_title = "Description :\n";
                String description = "Mosquitoes are small, flying insects. Mosquitoes have two long, thin antennae and two pairs of wings.\n\n";

                String control_measures_title = "Control Measures :\n";
                String control_measures = "Apply essential oils like citronella, eucalyptus, or lavender diluted in water to deter mosquitoes\n" +
                        "Spray this solution around your garden. Mosquitoes breed in water. Eliminating breeding sites around your home can help to reduce mosquito populations.\n";

                //Combine all the information into a single formatted text line
                String message = desc_title + description + control_measures_title+control_measures;

                result.setText(classes[maxPos].toUpperCase()+"\n\n"+message+"\n");
            }
            else if (classes[maxPos] == "sawfly" ) {


                String boldValue = "\033[0;1m";

                String desc_title = "Description :\n";
                String description = "Sawflies are a group of insects that are closely related to wasps and bees. Sawflies are typically small to\n" +
                        "medium-sized insects, with a body length ranging from a few millimeters to a few centimeters. They have\n" +
                        "two pairs of wings, and their abdomen is often brightly colored.\n\n";

                String control_measures_title = boldValue + "Control Measures\n";
                String control_measures = "Apply horticultural oil to suffocate sawflies and their eggs. This method is safe for plants and the surrounding ecosystem.\n";

                //Combine all the information into a single formatted text line
                String message = desc_title + description + control_measures_title+control_measures;



                result.setText(classes[maxPos].toUpperCase()+"\n\n"+message+"\n");
            }
            else if (classes[maxPos] == "stem_borer" ) {

                String boldValue = "\033[0;1m";

                String desc_title = "Description :\n";
                String description = "Sawflies are a group of insects that are closely related to wasps and bees. Sawflies are typically small to\n" +
                        "medium-sized insects, with a body length ranging from a few millimeters to a few centimeters. They have\n" +
                        "two pairs of wings, and their abdomen is often brightly colored.\n\n";

                String control_measures_title = "Control Measures\n";
                String control_measures = "Apply horticultural oil to suffocate sawflies and their eggs. This method is safe for plants and the surrounding ecosystem.\n";

                //Combine all the information into a single formatted text line
                String message = desc_title + description + control_measures_title+control_measures;



                result.setText(classes[maxPos].toUpperCase()+"\n\n"+message+"\n");
            }
            else{
                result.setText("Sorry Image not part of known classifications\n");
            }

            //result.setText(classes[maxPos]);

            String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }

            confidence.setText(s);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
            result.setText("Sorry Not Found");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(),image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
            classifyImage(image);

        } else if (requestCode == 10) {

            if (data!=null){
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);

                    //int dimension = Math.min(bitmap.getWidth(),bitmap.getHeight());
                    //bitmap = ThumbnailUtils.extractThumbnail(bitmap,dimension,dimension);

                    //classifyImage(bitmap);

                }catch (IOException e){
                    e.printStackTrace();
                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}