package com.example.mymanga;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymanga.Adapter.ItemProfileAdapter;
import com.example.mymanga.Data.ListItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    public static final int REQUEST_CODE = 123;
    FirebaseAuth firebaseAuth;
    TextView tvProfile, tvUid;
    CircleImageView ivProfile;
    RecyclerView rvProfile;
    ItemProfileAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvProfile = view.findViewById(R.id.text_profile);
        tvUid = view.findViewById(R.id.text_uid);
        ivProfile = view.findViewById(R.id.img_profile);
        rvProfile = view.findViewById(R.id.rv_profile);
        adapter = new ItemProfileAdapter(getContext(), getMyList(), getActivity());
        firebaseAuth = FirebaseAuth.getInstance();

        rvProfile.setHasFixedSize(true);
        rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProfile.setAdapter(adapter);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        checkUser(user);

        return view;
    }

    public void displayToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void checkUser(FirebaseUser user) {
        if (user != null) {
            String uid = "UID: " + user.getUid();
            if (user.getPhoneNumber() != null) {
                tvProfile.setText(user.getPhoneNumber());
                tvUid.setText(uid);
                Glide.with(this).load(R.drawable.ic_user).into(ivProfile);
            } else if (user.getDisplayName() == null){
                String s = "Anônimo";
                tvProfile.setText(s);
                tvUid.setText(uid);
                Glide.with(this).load(R.drawable.ic_user).into(ivProfile);
            } else {
                tvProfile.setText(user.getDisplayName());
                tvUid.setText(uid);
                Glide.with(this).load(user.getPhotoUrl()).into(ivProfile);
            }
        }
    }

    public void createFolder() {
        File file = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        if (file.exists()) {
            Log.d(TAG, "createFolder: the directory already exists");
            displayToast("O diretorio já existe");
        } else {
            file.mkdirs();
            if (file.isDirectory()) {
                Log.d(TAG, "createFolder: directory created with successful\n" + file.getPath());
                displayToast("Diretorio criado com sucesso");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                String message = "Falha ao criar diretorio" +
                        "\nPath: " + Environment.getExternalStorageDirectory() +
                        "\nmkdirs: " + file.mkdirs();
                builder.setMessage(message);
                builder.show();
            }
        }
    }

    public void openDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_dialog_layout);
        dialog.show();
        Button btnOpen, btnNew;
        btnNew = dialog.findViewById(R.id.btn_new);
        btnOpen = dialog.findViewById(R.id.btn_open);

        btnNew.setOnClickListener(v -> {
            Log.d(TAG, "openDialog: create a new folder");
            createFolder();
            dialog.dismiss();
        });

        btnOpen.setOnClickListener(v -> {
            Log.d(TAG, "openDialog: open a existing folder");
            openFolder();
            dialog.dismiss();
        });
    }

    public void openFolder() {
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/");
        startActivity(new Intent(Intent.ACTION_GET_CONTENT)
                .setDataAndType(uri, "*/*"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionResult: request granted");
                openDialog(getContext());
            } else {
                Log.d(TAG, "onRequestPermissionResult: request denied");
            }
        }
    }

    private ArrayList<ListItem> getMyList() {
        ArrayList<ListItem> listItems = new ArrayList<>();

        listItems.add(new ListItem(R.drawable.ic_power, "Logout"));
        listItems.add(new ListItem(R.drawable.ic_storage, "Armazenamento"));

        return listItems;
    }
}