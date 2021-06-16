package com.example.mymanga;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymanga.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private static final int REQUEST_CODE = 100;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressButton progressButton;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.pbtnPhone.textView.setText(getString(R.string.tv_phone));

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_id_token))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        binding.pbtnGoogle.cardView.setOnClickListener(view -> {
            progressButton = new ProgressButton(binding.pbtnGoogle.cardView, this);
            progressButton.buttonActivate();
            Intent intent = new Intent(googleSignInClient.getSignInIntent());
            onActivityResult(REQUEST_CODE, 0, intent);
            loading();
        });

        binding.pbtnPhone.cardView.setOnClickListener(view -> {
            progressButton = new ProgressButton(binding.pbtnPhone.cardView, this);
            progressButton.buttonActivate();
            startActivity(new Intent(this, PhoneActivity.class));
            loading();
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, "updateUI: Already logged in");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = signInAccountTask.getResult(ApiException.class);
                firebaseWithGoogleAccount(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            FirebaseUser user = mAuth.getCurrentUser();
            String uid = user.getUid();
            String email = user.getEmail();
            Log.d(TAG, "onSuccess: Email: " + email + "\nUid: " + uid);
            if (authResult.getAdditionalUserInfo().isNewUser()) {
                Log.d(TAG, "onSuccess: Account created...\n" + email);
                Toast.makeText(this, "Usuário criado", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "onSuccess: Existing user...\n" + email);
                Toast.makeText(this, "Usuário já existe", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: Login fail " + e.getMessage()));
    }

    private void loading() {
        Handler handler = new Handler();
        handler.postDelayed(() -> progressButton.buttonFinished(), 3000);
    }
}