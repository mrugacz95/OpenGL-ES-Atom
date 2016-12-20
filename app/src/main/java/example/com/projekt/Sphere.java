package example.com.projekt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

// Calculation of spherical vertex
class Sphere {
    FloatBuffer vBuf;
    float krok = 15.0f;
    int vertexNumber =(int)Math.floor(3*(180/krok+1) * 360/krok); //ilość wierzchołków
    public Sphere() { //tworzymy buffer raz
        float katA, katB, cos, sin, r1, r2, h1, h2;
        float[][] v = new float[2][3]; //zainicjowanie tablicy przechowującej następne dwa wierzchołki
        ByteBuffer vbb;

        vbb = ByteBuffer.allocateDirect(vertexNumber * 3 * 4); //buffor o wielkości ilości wierzchołków, każdy z położeniami x,y,z, każdy 4 bajtowy, bo pojedyńczy float jest 32 bitowy
        vbb.order(ByteOrder.nativeOrder());
        vBuf = vbb.asFloatBuffer();

        for (katA = -90.0f; katA < 90.0f; katA += krok) {

            r1 = (float) Math.cos(katA * Math.PI / 180.0);
            r2 = (float) Math.cos((katA + krok) * Math.PI / 180.0);
            h1 = (float) Math.sin(katA * Math.PI / 180.0);
            h2 = (float) Math.sin((katA + krok) * Math.PI / 180.0);

            for (katB = 0.0f; katB <= 360.0f; katB += krok)
            {

                cos = (float) Math.cos(katB * Math.PI / 180.0);
                sin = -(float) Math.sin(katB * Math.PI / 180.0);

                v[0][0] = (r2 * cos);
                v[0][1] = (h2);
                v[0][2] = (r2 * sin);
                v[1][0] = (r1 * cos);
                v[1][1] = (h1);
                v[1][2] = (r1 * sin);

                vBuf.put(v[0]);
                vBuf.put(v[1]);

            }
        }

    }

    public void draw(GL10 gl) { //używamy wcześniej stworzonego buffora, wiec nie trzeba go generować na nowo

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);


        vBuf.position(0);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertexNumber); //wyświetlenie wszystkoch wierzchołków z buffora

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    }

    public void draw2(GL10 gl) {
        float katA, katB, cos, sin, r1, r2, h1, h2;
        float krok = 5.0f;
        float[][] v = new float[32][3];
        ByteBuffer vbb;
        FloatBuffer vBuf;

        vbb = ByteBuffer.allocateDirect(v.length * v[0].length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vBuf = vbb.asFloatBuffer();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        for (katA = -90.0f; katA < 90.0f; katA += krok) {
            int n = 0;

            r1 = (float) Math.cos(katA * Math.PI / 180.0);
            r2 = (float) Math.cos((katA + krok) * Math.PI / 180.0);
            h1 = (float) Math.sin(katA * Math.PI / 180.0);
            h2 = (float) Math.sin((katA + krok) * Math.PI / 180.0);

            for (katB = 0.0f; katB <= 360.0f; katB += krok)                                         // Fixed latitude, 360 degrees rotation to traverse a weft
            {

                cos = (float) Math.cos(katB * Math.PI / 180.0);
                sin = -(float) Math.sin(katB * Math.PI / 180.0);

                v[n][0] = (r2 * cos);
                v[n][1] = (h2);
                v[n][2] = (r2 * sin);
                v[n + 1][0] = (r1 * cos);
                v[n + 1][1] = (h1);
                v[n + 1][2] = (r1 * sin);

                vBuf.put(v[n]);
                vBuf.put(v[n + 1]);

                n += 2;

                if (n > 31) {
                    vBuf.position(0);

                    gl.glVertexPointer(1, GL10.GL_FLOAT, 0, vBuf);
                    gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf);
                    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);

                    n = 0;
                    katB -= krok;
                }
            }

            vBuf.position(0);

            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);
            gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);

        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    }
}