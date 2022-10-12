package com.example.restaurante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText iden = findViewById(R.id.etiden);
        EditText fullname = findViewById(R.id.etfullname);
        EditText email = findViewById(R.id.etemail);
        EditText password = findViewById(R.id.etpassword);
        Button add = findViewById(R.id.btnadd);
        Button search= findViewById(R.id.btnsearch);
        Button update = findViewById(R.id.btnupdate);
        Button delete = findViewById(R.id.btndelete);





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomer(iden.getText().toString(),fullname.getText().toString(),email.getText().toString(),password.getText().toString());

            }
        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("customer")
                        .whereEqualTo("iden", iden.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            idCustomer = document.getId();
                                            fullname.setText(document.getString("fullname"));
                                            email.setText(document.getString("email"));

                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Este cliente no se encuentra en nuestra base de datos",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> customer = new HashMap<>();
                customer.put("fullname", fullname.getText().toString());
                customer.put("email", email.getText().toString());
                customer.put("password", password.getText().toString());
                customer.put("iden", iden.getText().toString());


                db.collection("customer").document(idCustomer)
                        .set(customer)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d("cliente", "DocumentSnapshot successfully written!");
                                Toast.makeText(MainActivity.this,"Cliente actualizado correctmente...",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("cliente", "Error writing document", e);
                            }
                        });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("customer").document(idCustomer)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d("cliente", "DocumentSnapshot successfully deleted!");
                                Toast.makeText(MainActivity.this,"Cliente borrado correctamente...",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("cliente", "Error deleting document", e);
                            }
                        });
            }
        });




    }

    private void saveCustomer(String sIden, String sFullName, String sEmail, String sPassword) {

        db.collection("customer")
                .whereEqualTo("iden", sIden)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {


                                Map<String, Object> customer = new HashMap<>();
                                customer.put("iden", sIden);
                                customer.put("fullname", sFullName);
                                customer.put("email", sEmail);
                                customer.put("password", sPassword);

                                db.collection("customer")
                                        .add(customer)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                Toast.makeText(getApplicationContext(), "Customer added sucessufuly...", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Log.w(TAG, "Error adding document", e);
                                                Toast.makeText(getApplicationContext(), "Error adding customer, please check if the internet conection is stable or if the service is running correctly", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"This customer already exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });





    }




}