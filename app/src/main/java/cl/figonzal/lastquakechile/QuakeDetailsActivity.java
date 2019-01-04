package cl.figonzal.lastquakechile;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.Locale;
import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class QuakeDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_details);

        //Setting toolbar
        Toolbar toolbar = findViewById(R.id.tool_bar_detail);
        setSupportActionBar(toolbar);

        //Muestra la flecha en toolbar para volver atras
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Obtener datos desde intent
        Bundle b = getIntent().getExtras();

        //Find de recursos
        TextView tv_ciudad = findViewById(R.id.tv_ciudad_detail);
        TextView tv_referencia = findViewById(R.id.tv_referencia_detail);
        TextView tv_escala = findViewById(R.id.tv_escala);
        TextView tv_magnitud = findViewById(R.id.tv_magnitud_detail);
        TextView tv_profundidad = findViewById(R.id.tv_epicentro);
        TextView tv_fecha_local = findViewById(R.id.tv_fecha);
        ImageView iv_mag_color = findViewById(R.id.iv_mag_color);
        final ImageView iv_mapa = findViewById(R.id.iv_map_quake);
        CardView card_view_mapa = findViewById(R.id.card_view_map);

        if (b != null) {
            String ciudad = b.getString(getString(R.string.INTENT_CIUDAD));
            String referencia = b.getString(getString(R.string.INTENT_REFERENCIA));
            String latitud = b.getString(getString(R.string.INTENT_LATITUD));
            String longitud = b.getString(getString(R.string.INTENT_LONGITUD));

            String fecha_local = b.getString(getString(R.string.INTENT_FECHA_LOCAL));

            Double magnitud = b.getDouble(getString(R.string.INTENT_MAGNITUD));
            Double profundidad = b.getDouble(getString(R.string.INTENT_PROFUNDIDAD));
            String escala = b.getString(getString(R.string.INTENT_ESCALA));
            Boolean sensible = b.getBoolean(getString(R.string.INTENT_SENSIBLE));
            String foto_url = b.getString(getString(R.string.INTENT_LINK_FOTO));

            //Setear titulo de ciudad en activity
            getSupportActionBar().setTitle(ciudad);

            //Setear nombre ciudad
            tv_ciudad.setText(ciudad);

            //Setear referencia
            tv_referencia.setText(referencia);

            //Setear magnitud en en circulo de color
            tv_magnitud.setText(String.format(getString(R.string.magnitud), magnitud));

            //Setear el color de background dependiendo de magnitud del sismo
            iv_mag_color.setColorFilter(getColor(QuakeUtils.getMagnitudeColor(magnitud)));

            //Setear profundidad
            tv_profundidad.setText(String.format(Locale.US, getString(R.string.quake_details_profundidad), profundidad));

            //Setear fecha
            tv_fecha_local.setText(fecha_local);

            /*
                Seccion Tipo Escala
             */
            if (escala != null) {

                switch (escala) {
                    case "Ml":
                        tv_escala.setText(String.format(getString(R.string.quake_details_escala), "Magnitud Local (Ml)"));
                        break;

                    case "Mw":
                        tv_escala.setText(String.format(getString(R.string.quake_details_escala), "Magnitud Momento (Mw)"));
                        break;

                }
            }

            /*
                Seccion Sismo sensible
             */
            /*if (sensible) {
                tv_sensible.setText(String.format(getString(R.string.quake_details_sensible), "percibido"));
            } else {
                tv_sensible.setText(String.format(getString(R.string.quake_details_sensible), "no percibido"));
            }*/

            /*
                Seccion image
             */
            Uri uri = Uri.parse(foto_url);
            Glide.with(this)
                    .load(uri)
                    .apply(
                            new RequestOptions()
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.not_found)
                    )
                    .transition(withCrossFade())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            iv_mapa.setImageDrawable(getDrawable(R.drawable.not_found));
                            return false;
                        }

                        //No es necesario usarlo (If u want)
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(iv_mapa);
        }
    }
}

