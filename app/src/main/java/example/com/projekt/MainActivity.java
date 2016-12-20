package example.com.projekt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private GLSurfaceView mGLView;
    private OpenGLRenderer mRenderer;
    private ScaleGestureDetector mScaleDetector;
    private float TOUCH_SCALE_FACTOR;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); //pobranie klasy menadżera okana by wyciągnąć z niej szerkość ekranu
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        TOUCH_SCALE_FACTOR =  180.f/width; // pełen obrótu (radiany) na przesunięcie po całym ekranie

        mScaleDetector = new ScaleGestureDetector(this,new ScaleListener());

        mGLView = new GLSurfaceView(this); //przeniesienie widoku do activity
        setContentView(mGLView);

        mRenderer = new OpenGLRenderer();
        mGLView.setRenderer(mRenderer);

        if(!isStoragePermissionGranted()){ //sprawdzenie pozwolenia na zapis na karcie sd
            Toast.makeText(this,"Brak pozwolenia na zapis na karcie sd",Toast.LENGTH_LONG).show();
        }
        else
            loadFileAndStartAnim();

    }


    private void loadFileAndStartAnim(){
        FileLoader fileLoader = new FileLoader(this);
        mRenderer.startAniamtion(fileLoader.getAnimationData());
    }
    // http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow
    public  boolean isStoragePermissionGranted() { //funkcja sprawdzająca czy aplikacja ma uprawnienia do odczytu z karty sd
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted"); //zapisanie w logu że nadano pozwolenie
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");//zapisanie w logu że nie nadano pozwolenia
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1); //poproszenie o pozwolenie i pokazanie okienka z pytaniem o pozwolnie
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted"); //zapisanie w logu że nadano pozwolenie
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //funkcja która zostaje wywoływana po uzyskaniu odpowiedz dot permission/zezwoleń
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]); //ponieważ okienko z zapytaniem o pozowlnie pokazuje się "na" oknie aplikacji to zostaja ona zatrzymana, dlatego gdy system zakończy pytanie użytkownika i uruchomi ponownie aplikaccję to musi ją poinformować o decyzji użytkowanika, dlatego zostaje przez system wywołana ta funkcja która zostaje następnie obsłużona przez aplikację
            // w naszym przypadku gdy wszystko się uda wczytujemy plik z karty sd
            loadFileAndStartAnim();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mScaleDetector.onTouchEvent(e);
        float x = e.getX();
        float y = e.getY();
        if (e.getPointerCount() > 1) return true; //jeżeli wiecej niż 2 dotyki to nie obracaj
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                mRenderer.setAngleX(mRenderer.getmAngleX() + dx * TOUCH_SCALE_FACTOR);
                mRenderer.setAngleY(mRenderer.getmAngleY() - dy * TOUCH_SCALE_FACTOR);
                mGLView.requestRender();
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float newDistance = mRenderer.getDistance() * 1 / detector.getScaleFactor();
            newDistance = Math.max(5f, Math.min(newDistance, 50f));
            mRenderer.setDistance(newDistance);
            mGLView.invalidate();
            return true;
        }
    }

}