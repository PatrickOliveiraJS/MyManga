package com.example.mymanga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymanga.databinding.ActivityPhoneBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {
    public static final String TAG = "PhoneActivity";
    ActivityPhoneBinding binding;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String mVerificationId;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.phoneLl.setVisibility(View.VISIBLE);
        binding.codeLl.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        pd.setTitle(R.string.pd_title);
        pd.setCanceledOnTouchOutside(false);

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pd.dismiss();
                displayToast(e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);
                Log.d(TAG, "onCodeSent: " + verificationId);
                mVerificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();
                binding.phoneLl.setVisibility(View.GONE);
                binding.codeLl.setVisibility(View.VISIBLE);
                displayToast(getString(R.string.msg_phone_codeSend));
                String numPhone = getString(R.string.msg_phone_sendCode)+binding.etPhone.getText().toString().trim();
                binding.tvSendCode.setText(numPhone);
            }
        };

        binding.btnPhone.setOnClickListener(view -> {
            String phone = binding.etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                displayToast(getString(R.string.msg_phone_null));
            } else {
                startPhoneNumberVerification(phone);
            }
        });

        binding.tvResendCode.setOnClickListener(view -> {
            String phone = binding.etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                displayToast(getString(R.string.msg_phone_null));
            } else {
                resendVerificationCode(phone, forceResendingToken);
            }
        });

        binding.btnPhoneCode.setOnClickListener(view -> {
            String code = binding.etPhoneCode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                displayToast(getString(R.string.msg_phone_code));
            } else {
                verifyPhoneNumberWithCode(mVerificationId, code);
            }
        });
    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        pd.setMessage(getString(R.string.msg_phone_resendCode));
        pd.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pd.setMessage(getString(R.string.msg_phone_sigin));
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
            pd.dismiss();
            String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
            displayToast(getString(R.string.msg_phone_success) + phone);
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }).addOnFailureListener(e -> {
            pd.dismiss();
            displayToast(e.getMessage());
        });
    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage(getString(R.string.msg_phone_resend));
        pd.show();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallback)
                .setForceResendingToken(token)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneNumberVerification(String phone) {
        pd.setMessage(getString(R.string.msg_phone_verify));
        pd.show();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallback)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void displayToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}