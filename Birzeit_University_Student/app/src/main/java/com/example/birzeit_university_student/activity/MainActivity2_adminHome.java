package com.example.birzeit_university_student.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.birzeit_university_student.R;
import com.example.birzeit_university_student.activity_tools.StudentAdapter;
import com.example.birzeit_university_student.classes.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity2_adminHome extends AppCompatActivity {

    private ListView listViewStudent;
    private Button buttonAddNewStudent;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_adminhome);
        queue = Volley.newRequestQueue(this);

        // Initialize views
        listViewStudent=findViewById(R.id.listViewStudent);
        buttonAddNewStudent=findViewById(R.id.buttonAddNewStudent);

        String url = "http://10.0.2.2:5000/student";
        ArrayList<Student> al_stud=new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Student student = new Student(obj.getInt("stud_id"), obj.getString("stud_name"), obj.getString("stud_email"), obj.getString("stud_password"), obj.getString("stud_major"));
                        al_stud.add(student);
                    }catch(JSONException exception){
                        Log.d("Error", exception.toString());
                    }
                }

                StudentAdapter listAdapter = new StudentAdapter(MainActivity2_adminHome.this, al_stud);
                listViewStudent.setAdapter(listAdapter);

                AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view,
                                            int position,
                                            long id) {
                        Intent intent = new Intent(MainActivity2_adminHome.this, MainActivity3_adminStudentDetails.class);

                        intent.putExtra("stud_id", al_stud.get((int)id).getStud_id());
                        startActivity(intent);

                    }
                };
                listViewStudent.setOnItemClickListener(itemClickListener);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity2_adminHome.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Error_json", error.toString());
            }
        });

        queue.add(request);


        buttonAddNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2_adminHome.this, MainActivity4_adminAddStudent.class);
                startActivity(intent);
            }
        });

    }
}
