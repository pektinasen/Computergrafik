package de.softwarekollektiv.cg.demos.triangle;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class OneTriangle {
    protected static void setup( GL gl, int width, int height ) {
        gl.glMatrixMode( GL.GL_PROJECTION );
        gl.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D( 0.0f, width, 0.0f, height );

        gl.glMatrixMode( GL.GL_MODELVIEW );
        gl.glLoadIdentity();

        gl.glViewport( 0, 0, width, height );
    }

    protected static void render( GL gl, int width, int height ) {
        gl.glClear( GL.GL_COLOR_BUFFER_BIT );

        // draw a triangle filling the window
        gl.glLoadIdentity();
        gl.glBegin( GL.GL_TRIANGLES );
        gl.glColor3f( 1, 0, 0 );
        gl.glVertex2f( 0, 0 );
        gl.glColor3f( 0, 1, 0 );
        gl.glVertex2f( width, 0 );
        gl.glColor3f( 0, 0, 1 );
        gl.glVertex2f( width / 2, height );
        gl.glEnd();
    }
}