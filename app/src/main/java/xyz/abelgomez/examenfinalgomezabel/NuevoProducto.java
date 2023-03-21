package xyz.abelgomez.examenfinalgomezabel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NuevoProducto extends AppCompatActivity {

    private ImageView imageViewProducto;
    private EditText editTextNombre;
    private EditText editTextDescripcion;
    private EditText editTextPrecio;
    private Button buttonGuardar;

    private byte[] imagenProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);

        imageViewProducto = findViewById(R.id.imageViewProducto);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextPrecio = findViewById(R.id.editTextPrecio);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        imageViewProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir la galería para seleccionar una imagen
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enviar una solicitud POST para agregar el nuevo producto
                agregarProducto();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            try {
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imagenProducto = stream.toByteArray();

                imageViewProducto.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Agrega un nuevo producto a la API Fake Store utilizando Volley
     */
    private void agregarProducto() {
        String nombre = editTextNombre.getText().toString();
        String descripcion = editTextDescripcion.getText().toString();
        double precio = Double.parseDouble(editTextPrecio.getText().toString());

        String url = "https://fakestoreapi.com/products";

        JSONObject jsonRequest = new JSONObject();

        try{
            jsonRequest.put("title", nombre);
            jsonRequest.put("description", descripcion);
            jsonRequest.put("price", precio);
            jsonRequest.put("category", "N/A");
            jsonRequest.put("image", Base64.encodeToString(imagenProducto, Base64.DEFAULT));
            JSONObject ratingObj = new JSONObject();
            ratingObj.put("rate", 0);
            ratingObj.put("count", 0);
            jsonRequest.put("rating", ratingObj);
        }catch(JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Maneja la respuesta de la API
                        Toast.makeText(NuevoProducto.this, "Producto agregado con éxito.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NuevoProducto.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(NuevoProducto.this, "Ocurrió un error al agregar el producto.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NuevoProducto.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(postRequest);
    }

}
