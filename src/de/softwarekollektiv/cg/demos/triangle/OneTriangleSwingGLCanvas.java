package de.softwarekollektiv.cg.demos.triangle;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A minimal program that draws with JOGL in a Swing JFrame using the AWT GLCanvas.
 *
 * @author Wade Walker
 */
public class OneTriangleSwingGLCanvas {

    public static void main( String [] args ) {
        GLCapabilities glcapabilities = new GLCapabilities();
        final GLCanvas glcanvas = new GLCanvas( glcapabilities );

        glcanvas.addGLEventListener( new GLEventListener() {
            
            @Override
            public void reshape( GLAutoDrawable glautodrawable, int x, int y, int width, int height ) {
                OneTriangle.setup( glautodrawable.getGL(), width, height );
            }
            
            @Override
            public void init( GLAutoDrawable glautodrawable ) {
            }
            
            @Override
            public void display( GLAutoDrawable glautodrawable ) {
                OneTriangle.render( glautodrawable.getGL(), glautodrawable.getWidth(), glautodrawable.getHeight() );
            }

			@Override
			public void displayChanged(GLAutoDrawable arg0, boolean arg1,
					boolean arg2) {				
			}
        });

        final JFrame jframe = new JFrame( "One Triangle Swing GLCanvas" ); 
        jframe.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowevent ) {
                jframe.dispose();
                System.exit( 0 );
            }
        });

        jframe.getContentPane().add( glcanvas, BorderLayout.CENTER );
        jframe.setSize( 640, 480 );
        jframe.setVisible( true );
    }
}