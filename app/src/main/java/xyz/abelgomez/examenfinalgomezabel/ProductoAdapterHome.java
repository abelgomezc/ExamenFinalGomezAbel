package xyz.abelgomez.examenfinalgomezabel;

import android.widget.BaseAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;



public class ProductoAdapterHome extends BaseAdapter {
    private Context mContext;
    private ArrayList<Producto> mProductos;

    public ProductoAdapterHome(Context context, ArrayList<Producto> productos) {
        this.mContext = context;
        this.mProductos = productos;
    }

    @Override
    public int getCount() {
        return mProductos.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mProductos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_producto, null);

        // Obtener referencias a las vistas de la cuadrícula
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ImageViewimagen);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.nombre);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.precio);

        // Obtiene el producto en la posición actual
        Producto producto = mProductos.get(position);

        // Cargar la imagen del producto
        Glide.with(mContext).load(producto.getImage()).into(imageView);

        // Asignar los valores del título y precio del producto a las vistas correspondientes
        titleTextView.setText(producto.getTitle());
        priceTextView.setText(String.format(Locale.getDefault(), "$ %.2f", producto.getPrice()));

        return convertView;
    }
}


