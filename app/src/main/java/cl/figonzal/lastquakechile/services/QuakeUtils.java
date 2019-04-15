package cl.figonzal.lastquakechile.services;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cl.figonzal.lastquakechile.R;

public class QuakeUtils {

    /**
     * Funcion que calcula la diferencia en milisegundos
     * entre el tiempo del sismo y la hora actual
     *
     * @param fecha_local parametro que entrega la fecha local desde el modelo en cardview
     * @return retorna la diferencia en milisegundos
     */
    private static long calculateDiff (Date fecha_local) {

        long diff;
        Date currentTime = new Date();

        long sismo_tiempo = fecha_local.getTime();
        long actual_tiempo = currentTime.getTime();

        diff = actual_tiempo - sismo_tiempo;

        return diff;

    }

    /**
     * Convierte desde UTC a Local de dispositivo (Según zona horaria)
     *
     * @param date Parametro date Utc
     * @return retorna el date en local
     */
    public static Date utcToLocal(Date date) {

        String timeZone = Calendar.getInstance().getTimeZone().getID();
        return new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
    }

    /**
     * Funcion encargada de entregar un mapeo los tiempos calculados y retornarlos en dias,horas,
     * minutos, segundos, de alguna fecha.
     * @param fecha fecha local del modelo de sismo desde cardview
     */
    public static Map<String, Long> dateToDHMS (Date fecha) {

        long diff = calculateDiff(fecha);
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        Map<String, Long> tiempos = new HashMap<>();
        tiempos.put("dias", days);
        tiempos.put("horas", hours);
        tiempos.put("minutos", minutes);
        tiempos.put("segundos", seconds);

        return tiempos;
    }

    /**
     * Funcion encargada de transformar un String a un Date
     *
     * @param context Requerido para el uso de recursos strings
     * @param sFecha  Fecha en string que será convertida en date
     *
     * @return dFecha Fecha en Date entregada por le funcion
     */
    public static Date stringToDate (Context context, String sFecha) {

        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.DATETIME_FORMAT),
                Locale.US);
        Date dFecha = null;

        try {
            dFecha = format.parse(sFecha);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dFecha;
    }

    /**
     * Funcion encargada de setear los colores de background
     * dependiendo de la magnitud del sismo
     *
     * @param magnitude Magnitud del sismo desde el cardview
     * @return id recurso desde colors.xml
     */
    public static int getMagnitudeColor(double magnitude, boolean forMapa) {

        int mag_floor = (int) Math.floor(magnitude);
        int mag_resource_id;
        switch (mag_floor) {

            case 1:
                if (forMapa) {
                    mag_resource_id = R.color.magnitude1_alpha;
                } else {
                    mag_resource_id = R.color.magnitude1;
                }
                break;

            case 2:
                if (forMapa) {
                    mag_resource_id = R.color.magnitude2_alpha;
                } else {
                    mag_resource_id = R.color.magnitude2;
                }
                break;
            case 3:

                if (forMapa) {
                    mag_resource_id = R.color.magnitude3_alpha;
                } else {
                    mag_resource_id = R.color.magnitude3;
                }
                break;
            case 4:

                if (forMapa) {
                    mag_resource_id = R.color.magnitude4_alpha;
                } else {
                    mag_resource_id = R.color.magnitude4;
                }
                break;
            case 5:

                if (forMapa) {
                    mag_resource_id = R.color.magnitude5_alpha;
                } else {
                    mag_resource_id = R.color.magnitude5;
                }
                break;
            case 6:

                if (forMapa) {
                    mag_resource_id = R.color.magnitude6_alpha;
                } else {
                    mag_resource_id = R.color.magnitude6;
                }
                break;
            case 7:
                if (forMapa) {
                    mag_resource_id = R.color.magnitude7_alpha;
                } else {
                    mag_resource_id = R.color.magnitude7;
                }
                break;
            case 8:
                if (forMapa) {
                    mag_resource_id = R.color.magnitude8_alpha;
                } else {
                    mag_resource_id = R.color.magnitude8;
                }
                break;
            case 9:
                if (forMapa) {
                    mag_resource_id = R.color.magnitude8_alpha;
                } else {
                    mag_resource_id = R.color.magnitude8;
                }
                break;

            //Si no, se elige color por defecto
            default:
                mag_resource_id = R.color.colorAccent;
                break;
        }
        return mag_resource_id;
    }

    /**
     * Funcion que permite cambiaar latitud o longitud a DMS
     *
     * @param input Longitud o Latitud
     * @return grados, minutos, segundos en un Map
     */
    public static Map<String, Double> latLonToDMS (double input) {

        Map<String, Double> dms = new HashMap<>();

        double abs = Math.abs(input);

        double lat_grados_rest = Math.floor(abs); //71
        double minutes = Math.floor((((abs - lat_grados_rest) * 3600) / 60)); // 71.43 -71 = 0.43 =25.8 = 25
        //(71.43 - 71)*3600 /60 - (71.43-71)*3600/60 = 25.8 - 25 =0.8
        double seconds = ((((abs - lat_grados_rest) * 3600) / 60) - Math.floor((((abs - lat_grados_rest) * 3600) / 60))) * 60;

        dms.put("grados", Math.floor(Math.abs(input)));
        dms.put("minutos", (double) Math.round(minutes));
        dms.put("segundos", (double) Math.round(seconds));

        return dms;
    }

    /**
     * Funcion que guardar una imagen en cache desde la descarga de glide
     *
     * @param drawable imagen de la cual se buscara la ruta
     * @param context  contexto de la actividad
     * @return Uri retorna la direccion dentro del celular donde esta la imagen
     */
    public static Uri getLocalBitmapUri(Drawable drawable, Context context) {

        Bitmap bmp;

        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) drawable).getBitmap();
        } else {
            return null;
        }

        Uri bmpUri = null;

        try {
            File file = new File(context.getCacheDir(), "share_image_" + System.currentTimeMillis() + ".jpeg");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(context, "cl.figonzal.lastquakechile.fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;

    }

    /**
     * Funcion que realiza la instalacion de un paquete dado
     *
     * @param packageName Nombre del paquete
     * @param context     Contexto que permite utilizar recursos de strings
     */
    public static void doInstallation(String packageName, Context context) {

        Intent intent;
        try {
            //Intenta abrir google play
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));

            //LOG
            Log.d(context.getString(R.string.TAG_INTENT), context.getString(R.string.TAG_INTENT_GOOGLEPLAY));
            Crashlytics.log(Log.DEBUG, context.getString(R.string.TAG_INTENT), context.getString(R.string.TAG_INTENT_GOOGLEPLAY));

            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {

            //Si gogle play no esta abre webview
            Log.d(context.getString(R.string.TAG_INTENT), context.getString(R.string.TAG_INTENT_NAVEGADOR));
            Crashlytics.log(Log.DEBUG, context.getString(R.string.TAG_INTENT), context.getString(R.string.TAG_INTENT_NAVEGADOR));

            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google" +
                    ".com/store/apps/details?id=" + packageName)));
        }
    }


    /**
     * Funcion encargada de setear el tiempo en los text views
     *
     * @param context Contexto para utilizar recursos
     * @param tiempos Variable que cuenta con el mapeo de dias,horas,minutos y segundos
     * @param tv_hora Textview que será usado para fijar el tiempo
     */
    public static void setTimeToTextView (Context context, Map<String, Long> tiempos,
                                          TextView tv_hora) {
        Long dias = tiempos.get(context.getString(R.string.UTILS_TIEMPO_DIAS));
        Long minutos = tiempos.get(context.getString(R.string.UTILS_TIEMPO_MINUTOS));
        Long horas = tiempos.get(context.getString(R.string.UTILS_TIEMPO_HORAS));
        Long segundos = tiempos.get(context.getString(R.string.UTILS_TIEMPO_SEGUNDOS));

        //Condiciones días.
        if (dias != null && dias == 0) {

            if (horas != null && horas >= 1) {
                tv_hora.setText(String.format(context.getString(R.string.quake_time_hour), horas));
            } else {
                tv_hora.setText(String.format(context.getString(R.string.quake_time_minute),
                        minutos));

                if (minutos != null && minutos < 1) {
                    tv_hora.setText(String.format(context.getString(R.string.quake_time_second),
                            segundos));
                }
            }
        } else if (dias != null && dias > 0) {

            if (horas != null && horas == 0) {
                tv_hora.setText(String.format(context.getString(R.string.quake_time_day), dias));
            } else if (horas != null && horas >= 1) {
                tv_hora.setText(String.format(context.getString(R.string.quake_time_day_hour),
                        dias, horas / 24));
            }
        }
    }

    /**
     * Funcion que permite setear la imagen de estado del sismos (Preliminar o Verificado)
     *
     * @param context   Contexto que permite acceder a los recursos de strings
     * @param estado    Estado del sismos (preliminar/verificado)
     * @param tv_estado Texview que tendrá el valor de estado
     * @param iv_estado ImageView fijada dependiendo del valor de estado
     */
    public static void setStatusImage (Context context, String estado, TextView tv_estado,
                                       ImageView iv_estado) {
        if (estado.equals("preliminar")) {
            tv_estado.setText(String.format(Locale.US,
                    context.getString(R.string.quakes_details_estado_sismo),
                    context.getString(R.string.quakes_details_estado_sismo_preliminar)));
            iv_estado.setImageDrawable(context.getDrawable(R.drawable.ic_progress_check_24));
        } else if (estado.equals("verificado")) {
            tv_estado.setText(String.format(Locale.US,
                    context.getString(R.string.quakes_details_estado_sismo),
                    context.getString(R.string.quakes_details_estado_sismo_verificado)));
            iv_estado.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_check_circle_24px));
        }
    }

    /**
     * Funcion que permite setear el textview de escala dependiendo del valor del string
     *
     * @param context   Contexto utilizado para el uso de recursos
     * @param escala    Escala del sismo puede ser Ml o Mw
     * @param tv_escala Textview que será fijado con el valor de escala
     */
    public static void setEscala (Context context, String escala, TextView tv_escala) {

        switch (escala) {
            case "Ml":
                tv_escala.setText(String.format(context.getString(R.string.quake_details_escala),
                        context.getString(R.string.quake_details_magnitud_local)));
                break;

            case "Mw":
                tv_escala.setText(String.format(context.getString(R.string.quake_details_escala), context.getString(R.string.quake_details_magnitud_momento)));
                break;

        }
    }

}
