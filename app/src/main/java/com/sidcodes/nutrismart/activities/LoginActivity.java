package com.sidcodes.nutrismart.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sidcodes.nutrismart.R;
import com.sidcodes.nutrismart.databinding.ActivityLoginBinding;
import com.sidcodes.nutrismart.utils.UserType;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    // Tag to show logs in layout
    private static final String TAG = "LOGIN_PAGE";

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Progress Dialog to show while signing in
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //Firebase Auth instance for authentication tasks
        firebaseAuth = FirebaseAuth.getInstance();

        //configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        binding.skipButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
            finish();
        });

        //
        binding.googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Signing in with Google...");

                Intent googleSignInIntent = googleSignInClient.getSignInIntent();
                googleSignInLauncher.launch(googleSignInIntent);
            }
        });

//        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d(TAG, "onActivityResult: " + result.getResultCode());

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d(TAG, "onActivityResult: Account ID " + account.getIdToken() + "Email " + account.getEmail());
                        firebaseGoogleAuth(account.getIdToken());
                    } catch (Exception e) {
                        Log.d(TAG, "onActivityResult: " + e.getMessage());
                    }
                } else {
                    Log.d(TAG, "onActivityResult: Failed " + result.getResultCode());
                    Toast.makeText(LoginActivity.this, "Login Failed ", Toast.LENGTH_SHORT).show();
                }
            });

    private void firebaseGoogleAuth(String idToken) {
        Log.d(TAG, "firebaseGoogleAuth: " + idToken);

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    if(authResult.getAdditionalUserInfo().isNewUser()) {
                        Log.d(TAG, "onSuccess: Account created");
                        Toast.makeText(LoginActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                        createUser();
                        startActivity(new Intent(LoginActivity.this, SetupActivity.class));
                        finishAffinity();
                    } else {
                        Log.d(TAG, "onSuccess: Existing user");
                        Toast.makeText(LoginActivity.this, "Existing user", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, SetupActivity.class));
                        finishAffinity();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "firebaseGoogleAuth: Failed " + e.getMessage());
                    Toast.makeText(LoginActivity.this, "Login Failed ", Toast.LENGTH_SHORT).show();
                });
    }

    private void createUser() {
        Log.d(TAG, "createUser: Creating user");

        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        //get current timestamp e.g. to show user registration date/time
        String registeredUserUid = firebaseAuth.getUid();
        String registeredUserEmail = firebaseAuth.getCurrentUser().getEmail();
        String name = firebaseAuth.getCurrentUser().getDisplayName();

        Map<String, Object> user = new HashMap<>();
        user.put("uid", registeredUserUid);
        user.put("email", registeredUserEmail);
        user.put("name", name);
        user.put("timestamp", System.currentTimeMillis());
        user.put("areaCode", "");
        user.put("phone", "");
        user.put("profileImage", "");
        user.put("DOB", "");
        user.put("userType", UserType.GOOGLE.name());
        user.put("token", "");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(registeredUserUid)
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "createUser: Success");
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, SetupActivity.class));
                    finishAffinity();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "createUser: Failed " + e.getMessage());
                    Toast.makeText(LoginActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                });

    }
}