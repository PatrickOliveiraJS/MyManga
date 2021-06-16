package com.example.mymanga.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymanga.Data.ListItem;
import com.example.mymanga.LoginActivity;
import com.example.mymanga.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;

import static com.example.mymanga.ProfileFragment.REQUEST_CODE;

public class ItemProfileAdapter extends RecyclerView.Adapter<ItemProfileAdapter.ListViewHolder> {
    private static final String TAG = ItemProfileAdapter.class.getSimpleName();
    Context context;
    ArrayList<ListItem> listItems;
    Activity activity;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;

    public ItemProfileAdapter(Context context, ArrayList<ListItem> listItems, Activity activity) {
        this.context = context;
        this.listItems = listItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_profile, parent, false);
        googleSignInClient = GoogleSignIn.getClient(context,
                GoogleSignInOptions.DEFAULT_SIGN_IN);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        String text1 = listItems.get(position).getmText1();
        int image = listItems.get(position).getmImageResource();

        holder.imageView.setImageResource(image);
        holder.textView1.setText(text1);
        holder.itemView.setOnClickListener(v -> optionClick(position));
    }

    private void optionClick(int position) {
        String item = listItems.get(position).getmText1();
        switch (item) {
            case "Logout":
                Log.d(TAG, "optionClick: logout");
                logout();
                break;
            case "Armazenamento":
                Log.d(TAG, "optionClick: storage");
                storage();
                break;
        }
    }

    private void storage() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Quando a permissão não é concedida
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Conceda Permissão");
                builder.setMessage("Leitura do Armaz. Externo e Gravações no Armaz. Externo");
                builder.setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(activity,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, REQUEST_CODE));
                builder.setNegativeButton("Cancelar", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, REQUEST_CODE);
            }
        } else {
            // Quando já possui acesso
            openDialog(context);
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
            createFolder();
            dialog.dismiss();
        });

        btnOpen.setOnClickListener(v -> {
            openFolder();
            dialog.dismiss();
        });
    }

    public void displayToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public void createFolder() {
        File file = new File(Environment.getExternalStorageDirectory(), "MangaWorld");
        if (file.exists()) {
            displayToast("O diretorio já existe");
        } else {
            file.mkdirs();
            if (file.isDirectory()) {
                displayToast("Diretorio criado com sucesso");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String message = "Falha ao criar diretorio" +
                        "\nPath: " + Environment.getExternalStorageDirectory() +
                        "\nmkdirs: " + file.mkdirs();
                builder.setMessage(message);
                builder.show();
            }
        }
    }

    public void openFolder() {
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + R.string.app_name + "/");
        context.startActivity(new Intent(Intent.ACTION_GET_CONTENT)
                .setDataAndType(uri, "*/*"));
    }

    private void logout() {
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseAuth.signOut();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_profile);
            textView1 = itemView.findViewById(R.id.tv_logout1);
        }
    }
}
