package com.dreamteam.dreamteam.EditProfile.View;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.dreamteam.dreamteam.EditProfile.EditProfilePresenter.EditProfilePresenter;
import com.dreamteam.dreamteam.EditProfile.Protocols.ViewEditProfileInterface;
import com.dreamteam.dreamteam.MainActivity;
import com.dreamteam.dreamteam.R;
import com.dreamteam.dreamteam.User.Entity.UserData.User;
import com.dreamteam.dreamteam.User.View.UserViewController;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EditProfileController extends Fragment implements ViewEditProfileInterface {

    final int MAKE_PHOTO_REQUEST = 0;
    final int GET_FROM_GALLERY_REQUEST = 1;

    public EditProfilePresenter editProfilePresenter = new EditProfilePresenter(this);

    private User user;
    private Bitmap bitmap;
    private EditText editName, editSurname;
    private Button saveButton;
    private ImageView editImageView;

    public EditProfileController(User user, Bitmap bitmap) {
        // Required empty public constructor
        this.user = user;
        this.bitmap = bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile_controller, container, false);
        editName = view.findViewById(R.id.edit_name_user_edit_text);
        editSurname = view.findViewById(R.id.edit_surname_user_edit_text);
        editImageView = view.findViewById(R.id.edit_user_image);
        saveButton = view.findViewById(R.id.save_profile_button);

        editName.setText(user.content.simpleData.name);
        editSurname.setText(user.content.simpleData.surname);
        editImageView.setImageBitmap(bitmap);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {

        editImageView.setOnClickListener(new View.OnClickListener() {//слушатель нажатия на ImageView
            @Override
            public void onClick(View v) {
                showPopupMenu(v);//при нажатии на ImageView выходит PopupMenu
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {//TODO: сделать так, что если какие-то данные не изменились, то не отправлять эти данные на сервер
            @Override
            public void onClick(View v) {//-----------------------------------------------------слушатель на кнопку Save
                if ((editName.getText().toString().equals("")))//-----------------------------------проверка на заполнение поля
                {
                    editName.setError("Введите имя");
                    editName.requestFocus();
                } else if (editSurname.getText().toString().equals("")){
                    editSurname.setError("Введите фамилию");
                    editSurname.requestFocus();
                }
                else
                {//---------------------------------------------------------------------------------код отправки изменеий на сервер
                    user.content.simpleData.name = editName.getText().toString();
                    user.content.simpleData.surname = editSurname.getText().toString();

                    bitmap = ((BitmapDrawable)editImageView.getDrawable()).getBitmap();
                    editProfilePresenter.putUser(user, bitmap);
                }
            }
        });
        super.onStart();
    }

    private void showPopupMenu(View v){//-----------------------------------------------------------метод показа PopupMenu
        PopupMenu popupMenu = new PopupMenu(getContext(), v);//TODO: посмотреть как программно создавать popup меню и как с ним работать, чтобы вынести в отдельный класс, вынести запросы камеры и галереи в интератор
        popupMenu.inflate(R.menu.photo_popup_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){//--------------------------------------------------------выбор действия в меню
                    case R.id.make_photo_popup://---------------------------------------------------получение фото с камеры
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//--------открывается камера
                        startActivityForResult(cameraIntent, MAKE_PHOTO_REQUEST);
                        return true;
                    case R.id.get_from_gallery_popup://---------------------------------------------получение фото с Галереи
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, GET_FROM_GALLERY_REQUEST);
                        return true;
                    case R.id.delete_image_popup:
                        editImageView.setImageBitmap(null);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//------------------определение действий по requestCode
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            editImageView.setImageBitmap(thumbnailBitmap);
        }
        if (requestCode == GET_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            editImageView.setImageURI(selectedImage);
        }
    }

    //выводит в Активити профиль измененного User
    @Override
    public void answerPutRequest() {//TODO: подумать как это сделать через Presenter
        MainActivity activityAction = (MainActivity) getActivity();
        activityAction.changeFragment(new UserViewController(), null);
    }
}



