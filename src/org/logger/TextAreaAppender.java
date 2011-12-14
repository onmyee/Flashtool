package org.logger;

import java.awt.Color;
import java.awt.Font;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.apache.log4j.Level;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.system.OS;

public class TextAreaAppender extends WriterAppender {
	
	static private JTextPane jTextArea = null;
	static private StringBuilder builder = new StringBuilder();
	static Font font = new Font("Serif", Font.PLAIN, 12);
	private static String timestamp=getTimeStamp();

    public static String getTimeStamp() {
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
    	df.setTimeZone( TimeZone.getTimeZone("PST"));  
    	String date = ( df.format(new Date()));    
    	DateFormat df1 = new SimpleDateFormat("hh-mm-ss") ;    
    	df1.setTimeZone( TimeZone.getDefault()) ;  
    	String time = ( df1.format(new Date()));
    	return date+"_"+time;
    }

	public static void writeFile() {
		String logname=OS.getWorkDir()+OS.getFileSeparator()+"flashtool_"+timestamp+".log";
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(logname));
			writer.write(builder.toString());
		}
		catch (IOException exception) {}
		finally {
			if (writer != null) {
				try {
					MyLogger.getLogger().info("Log written to "+logname);
					writer.close();
				}
				catch (Exception exception) {}
			}
		}
	
	}
	
	/** Set the target JTextArea for the logging information to appear. */
	static public void setTextArea(JTextPane jTextArea) {
		TextAreaAppender.jTextArea = jTextArea;
		TextAreaAppender.jTextArea.setEditorKit(new HTMLEditorKit());
		TextAreaAppender.jTextArea.setEditable(false);
	}
	/**
	 * Format and then append the loggingEvent to the stored
	 * JTextArea.
	 */
	public void append(LoggingEvent loggingEvent) {
		final String message = this.layout.format(loggingEvent);
		final Level level = loggingEvent.getLevel();

		// Append formatted message to textarea using the Swing Thread.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				builder.append(message);
				if (level==Level.ERROR) {
					append(Color.red,message);
				}
				else if (level==Level.WARN) {
					append(Color.blue,message);
				}
				else {
					append(Color.black,message);
				}
			}
		});
	}
	
	public static void append(Color c,String message) {
        // Start with the current input attributes for the JTextPane. This
        // should ensure that we do not wipe out any existing attributes
        // (such as alignment or other paragraph attributes) currently
        // set on the text area.
        MutableAttributeSet attrs = jTextArea.getInputAttributes();

        // Set the font family, size, and style, based on properties of
        // the Font object. Note that JTextPane supports a number of
        // character attributes beyond those supported by the Font class.
        // For example, underline, strike-through, super- and sub-script.
        StyleConstants.setFontFamily(attrs, font.getFamily());
        StyleConstants.setFontSize(attrs, font.getSize());
        StyleConstants.setItalic(attrs, (font.getStyle() & Font.ITALIC) != 0);
        StyleConstants.setBold(attrs, (font.getStyle() & Font.BOLD) != 0);

        // Set the font color
        StyleConstants.setForeground(attrs, c);

        // Retrieve the pane's document object
        StyledDocument doc = jTextArea.getStyledDocument();

        // Replace the style for the entire document. We exceed the length
        // of the document by 1 so that text entered at the end of the
        // document uses the attributes.
        try {
        doc.insertString(doc.getLength(), message, attrs);
        jTextArea.scrollRectToVisible(jTextArea.modelToView(doc.getLength()));
        }
        catch (Exception e) {}
    }

	public String getString() {
		return builder.toString();
	}
}