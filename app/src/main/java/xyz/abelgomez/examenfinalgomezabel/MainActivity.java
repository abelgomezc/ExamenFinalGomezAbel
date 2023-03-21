package xyz.abelgomez.examenfinalgomezabel;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private List<Producto> mProductos = null;

  // caso 4
    private static final String API_BASE_URL = "https://fakestoreapi.com/";

    FloatingActionButton fab;

    GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);

        mContext = getApplicationContext();
       gridView = findViewById(R.id.grid_view);

        // Carga los productos de la API y los muestra en el GridView
        getProductsFromApi(gridView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NuevoProducto.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getProductsFromApi(final GridView gridView) {
        // Realiza la solicitud GET utilizando Volley
        String url = API_BASE_URL + "products";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Recorremos el array de objetos JSON y creamos objetos de tipo producto
                            mProductos = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject productoJson = response.getJSONObject(i);
                                int id = productoJson.getInt("id");
                                String title = productoJson.getString("title");
                                double price = productoJson.getDouble("price");
                                String description = productoJson.getString("description");
                                String category = productoJson.getString("category");
                                String image = productoJson.getString("image");
                                JSONObject rating = productoJson.getJSONObject("rating");
                                double rate = rating.getDouble("rate");
                                int count = rating.getInt("count");
                                Producto producto = new Producto(id, title, price, description, category, image, rate, count);
                                mProductos.add(producto);
                            }

                            // Si la lista de productos en la base de datos SQLite no está vacía, mostrarla en el GridView
                            ProductoAdapterHome mAdapter = new ProductoAdapterHome(mContext, (ArrayList<Producto>) mProductos);
                            gridView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Agregamos la solicitud a la cola de solicitudes de Volley
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getProductsFromApi(gridView);

    }


}
