package organizesports.activities.admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.organizesports.R;
import com.app.organizesports.callbacks.StringCallback;
import com.app.organizesports.controllers.UploadController;
import com.app.organizesports.models.ElementModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class DataElementEditorActivity extends AppCompatActivity {
    public static ArrayList<ElementType> types = new ArrayList<>(Arrays.asList(new ElementType(0, "Title"),
            new ElementType(1, "Content"),
            new ElementType(2, "Image"),
            new ElementType(3, "Separator"),
            new ElementType(4, "Youtube Video Url")));

    private TextInputLayout titleLayout, contentLayout, youtubeLayout;
    private TextInputEditText title, content, youtube;


    private AutoCompleteTextView type;
    private String oldType = "";

    private static final int PICK_IMAGE = 55;
    Uri imageUri;
    ImageView image;
    String videoId = "";

    Button save;

    private ArrayList<String> typesNames = new ArrayList<>();

    private ElementType chosenType;

    LoadingHelper loadingHelper;
    boolean isEditing = false;
    ElementModel elementModel = new ElementModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_element_editor);

        type = findViewById(R.id.type);
        titleLayout = findViewById(R.id.title_field);
        contentLayout = findViewById(R.id.content_field);
        youtubeLayout = findViewById(R.id.youtube_field);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        youtube = findViewById(R.id.youtube);
        image = findViewById(R.id.image);
        save = findViewById(R.id.save);

        loadingHelper = new LoadingHelper(this);

        for(ElementType type: types) {
            typesNames.add(type.getText());
        }

        ArrayAdapter typesAdapter = new ArrayAdapter<>(this, R.layout.list_item, typesNames);
        type.setAdapter(typesAdapter);
        type.setOnItemClickListener((parent, view, position, id) -> {
            chosenType = types.get(position);
            typeChanged();
        });

        image.setOnClickListener(v -> {
            if(checkReadPermission()){
                pickImage();
            }
        });

        save.setOnClickListener(v -> {
            save();
        });

        videoUrlListener();
        getData();
    }

    @SuppressLint("SetTextI18n")
    private void getData() {
        if(SharedData.chosenElement != null) {
            isEditing = true;
            elementModel = SharedData.chosenElement;
            chosenType = types.get(elementModel.getType());
            type.getOnItemClickListener().onItemClick(null, null, chosenType.id, chosenType.id);
            type.setText(chosenType.text);
            typeChanged();

            if(elementModel.getType() == 0) { //title
                title.setText(elementModel.getContent().get(0));
            } else if(elementModel.getType() == 1) { //content
                content.setText(elementModel.getContent().get(0));
            } else if(elementModel.getType() == 2) { //image
                if (!TextUtils.isEmpty(elementModel.getContent().get(0))) {
                    image.setImageTintList(null);
                    Picasso.get()
                            .load(elementModel.getContent().get(0))
                            .into(image);
                }
            } else if(elementModel.getType() == 4) { //youtube
                youtube.setText("https://youtu.be/" + elementModel.getContent().get(0));
            }

        } else {
            isEditing = false;
        }
    }

    private void typeChanged() {
        titleLayout.setVisibility(chosenType.id == 0 ? View.VISIBLE : View.GONE);
        contentLayout.setVisibility(chosenType.id == 1 ? View.VISIBLE : View.GONE);
        image.setVisibility(chosenType.id == 2 ? View.VISIBLE : View.GONE);
        youtubeLayout.setVisibility(chosenType.id == 4 ? View.VISIBLE : View.GONE);

    }


    private void videoUrlListener() {
        youtube.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String id = s.toString();
                if(id.contains("youtube")) {
                    id = id.replace("https://www.youtube.com/watch?v=", "");
                }else if(id.contains("youtu.be")) {
                    id = id.replace("https://youtu.be/", "");
                }
                id = id.replace("https", "");
                id = id.replace("http", "");
                id = id.replace("youtu.be", "");
                id = id.replace("/", "");
                id = id.replace(":", "");

                videoId = id;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private boolean checkReadPermission(){
        int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            return false;
        }else{
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickImage();
            }
        }
    }

    private void pickImage(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    private void save() {
        if(chosenType != null) {
            if(chosenType.id == 2) { //image
                if(imageUri != null) {
                    loadingHelper.showLoading("");
                    new UploadController().uploadImage(imageUri, new StringCallback() {
                        @Override
                        public void onSuccess(String text) {
                            loadingHelper.dismissLoading();
                            elementModel.setContent(new ArrayList<>());
                            elementModel.getContent().add(text);
                            elementModel.setType(chosenType.getId());
                            saveElement();
                            onBackPressed();
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(DataElementEditorActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if(isEditing) {
                    saveElement();
                    onBackPressed();
                } else {
                    Toast.makeText(DataElementEditorActivity.this, "Add Image!", Toast.LENGTH_SHORT).show();
                }

            }else if (chosenType.id == 0){ //Title
                if(!title.getText().toString().trim().isEmpty()) {
                    elementModel.setContent(new ArrayList<>());
                    elementModel.getContent().add(title.getText().toString());
                    elementModel.setType(chosenType.getId());
                    saveElement();
                    onBackPressed();
                } else {
                    Toast.makeText(DataElementEditorActivity.this, "Not Valid!", Toast.LENGTH_SHORT).show();
                }

            }else if (chosenType.id == 1){ //Content
                if(!content.getText().toString().trim().isEmpty()) {
                    elementModel.setContent(new ArrayList<>());
                    elementModel.getContent().add(content.getText().toString());
                    elementModel.setType(chosenType.getId());
                    saveElement();
                    onBackPressed();
                } else {
                    Toast.makeText(DataElementEditorActivity.this, "Not Valid!", Toast.LENGTH_SHORT).show();
                }

            }else if (chosenType.id == 3){ //Separator
                elementModel.setContent(new ArrayList<>());
                elementModel.setType(chosenType.getId());
                saveElement();
                onBackPressed();
            }else if (chosenType.id == 4){ //Youtube
                if(!videoId.trim().isEmpty()) {
                    elementModel.setContent(new ArrayList<>());
                    elementModel.getContent().add(videoId);
                    elementModel.setType(chosenType.getId());
                    saveElement();
                    onBackPressed();
                } else {
                    Toast.makeText(DataElementEditorActivity.this, "Not Valid!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Choose Type!", Toast.LENGTH_LONG).show();
        }
    }

    private void saveElement() {
        if (!isEditing) {
            SharedData.currentElements.add(elementModel);
        } else {
            SharedData.currentElements.remove(SharedData.chosenElementIndex);
            SharedData.currentElements.add(SharedData.chosenElementIndex, elementModel);
        }
    }
    public static class ElementType {
        private int id;
        private String text;

        public ElementType(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}