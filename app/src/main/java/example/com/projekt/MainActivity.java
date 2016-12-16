package example.com.projekt;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {

    private OpenGLView kpOpenGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);                                                          // a to jest zawsze na poczatku kazdej metody ;d

        requestWindowFeature(Window.FEATURE_NO_TITLE);                                                                      // Go to the title bar

       //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);       //Ustawienie wyświetlania na pełnym ekranie

        kpOpenGLView = new OpenGLView(this);
        setContentView(kpOpenGLView);


    }

}