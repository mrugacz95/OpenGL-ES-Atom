package example.com.projekt;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class OpenGLRenderer implements Renderer {

    private final float[] mat_ambient = {1.0f, 1.0f, 0.0f, 0.5f};
    private FloatBuffer mat_ambient_buf;
    private final float[] mat_diffuse = {1.0f, 1.0f, 0.0f, 0.5f};
    private FloatBuffer mat_diffuse_buf;

    private float mAngleX;
    private float mAngleY;
    private float distance = 20;

    private long lastFrameTime;
    private void initBuffers() {
        ByteBuffer bufTemp = ByteBuffer.allocateDirect(mat_ambient.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        mat_ambient_buf = bufTemp.asFloatBuffer();
        mat_ambient_buf.put(mat_ambient);
        mat_ambient_buf.position(0);

        bufTemp = ByteBuffer.allocateDirect(mat_diffuse.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        mat_diffuse_buf = bufTemp.asFloatBuffer();
        mat_diffuse_buf.put(mat_diffuse);
        mat_diffuse_buf.position(0);
    }

    private Sphere sphere = new Sphere();

    public volatile float SwiatloX = 50f;
    public volatile float SwiatloY = 50f;
    public volatile float SwiatloZ = 50f;
    AnimationData aniamtionData = null;

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)                              //zmiany powierzchni, na przykład szerokości i wysokości okna,Dzięki niej możemy konfigurować kamerę oraz objętość widzenia.
    {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 90.0f, (float) width / height, 1.0f, 100.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);                            // On the perspective correction
        gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);                                                    // Tło: czarne
        gl.glShadeModel(GL10.GL_SMOOTH);                                                            //
        gl.glClearDepthf(1.0f);                                                                     // Reset the depth buffer
        gl.glEnable(GL10.GL_DEPTH_TEST);                                                            // Start the depth test
        gl.glDepthFunc(GL10.GL_LEQUAL);                                                             // Type the depth test
        initBuffers();

        gl.glEnable(GL10.GL_LIGHTING);                                                              //wlaczenie oswietlania/ koloru
        gl.glEnable(GL10.GL_LIGHT0);                                                              // nakladanie materialu na kule

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambient_buf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffuse_buf);


        float[] light_position = {SwiatloX, SwiatloY, SwiatloZ, 0.0f};                               //Pozycja źródła światła
        ByteBuffer mpbb = ByteBuffer.allocateDirect(light_position.length * 4);
        mpbb.order(ByteOrder.nativeOrder());
        FloatBuffer mat_posiBuf = mpbb.asFloatBuffer();
        mat_posiBuf.put(light_position);
        mat_posiBuf.position(0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, mat_posiBuf);


        lastFrameTime = SystemClock.elapsedRealtime();
    }

    private float[] mRotationMatrix = new float[16];

    private void drawSphere(float x, float y, float z, GL10 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        sphere.draw(gl);
        gl.glPopMatrix();
    }

    private void drawSphere(Point3d point3d, GL10 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(point3d.x, point3d.y, point3d.z);
        sphere.draw(gl);
        gl.glPopMatrix();
    }

    private void drawSphere(AnimationData.TimeStamp timeStamp, GL10 gl) {
        for(Point3d point3d : timeStamp.getPositions()){
            drawSphere(point3d,gl);
        }
    }
    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);


        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0f, 0f, distance, 0f, 0f, 0f, 0f, 1f, 0f);
        gl.glRotatef(mAngleX, 0, 1, 0);
        gl.glRotatef(mAngleY, 0, 0, 1);
        gl.glPushMatrix(); //zapisanie aktualnego widoku
        if(aniamtionData!=null){ //jeżeli nie ma pliku to pokazujemy noramlnie 9 atmów
            long currentTime = SystemClock.elapsedRealtime(); //jak jest to liczymy czas który upłynął
            long deltaTime = currentTime - lastFrameTime;
            if(deltaTime>100){
                aniamtionData.nextFrame();
                lastFrameTime = currentTime;
            }
            drawAnimationFrame(gl);

        }
        else {
            drawSphere(0, 0, 0, gl);
            drawSphere(-3.0f, -3.0f, -3.0f, gl);
            drawSphere(-3.0f, -3.0f, 3.0f, gl);
            drawSphere(3.0f, -3.0f, 3.0f, gl);
            drawSphere(3.0f, -3.0f, -3.0f, gl);
            drawSphere(-3.0f, 3.0f, -3.0f, gl);
            drawSphere(-3.0f, 3.0f, 3.0f, gl);
            drawSphere(3.0f, 3.0f, 3.0f, gl);
            drawSphere(3.0f, 3.0f, -3.0f, gl);
        }
    }

    public void setAngleX(float angle) {
        mAngleX = angle;
    }

    public void setAngleY(float angle) {
        mAngleY = angle;
    }

    public float getmAngleX() {
        return mAngleX;
    }

    public float getmAngleY() {
        return mAngleY;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void startAniamtion(AnimationData aniamtionData) {
        this.aniamtionData = aniamtionData;
    }
    private void drawAnimationFrame(GL10 gl){
        drawSphere(aniamtionData.getCurrentTimeStamp(),gl); //narysowanie obenej klatki z atomami
    }

}