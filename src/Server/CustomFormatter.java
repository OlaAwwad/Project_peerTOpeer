package Server;

import java.io.ByteArrayOutputStream;

/**
*  	Project name		: PeerToPeer
*	Class				: CustomFormatter
*
* 	Date of creation	: 28.12.2017
* 	
*	Description :
*	Class used to generate logs
* 
* @author Nicolas Piguet
*/

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class CustomFormatter extends Formatter {
	/**
	 * Format the logs
	 */
	public String format( LogRecord record )
	{
		// Objet pour assembler des cha�nes de caract�res
		StringBuffer sb = new StringBuffer("");
		
		// Formateur de dates
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		
		// Ajout de la date au log
		sb.append( sdf.format( record.getMillis() ) );
		
		// Ajout de la classe et de la m�thode qui a demand� le log
		sb.append( "; class.method()=" );
		sb.append( record.getSourceClassName() );
		sb.append( "." );
		sb.append( record.getSourceMethodName() );
		
		// Ajout du niveau de s�v�rit� de l'�v�nement
		sb.append( "(); level=" );
		sb.append( record.getLevel().getName() );
		
		// Ajout du message de l'�v�nement
		sb.append( " : ");
		sb.append( record.getMessage() );
		
		// Si le log a une Exception associ�e, on l'affiche
		if ( record.getThrown() != null )
		{
			try
			{
				sb.append("\r\n\t");
				
				// Objet qui r�cup�re les bytes du flux de sortie et les place dans un tableau
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				// Objet qui affiche des caract�res dans un flux de sortie (ici, ce sera dans baos)
				PrintStream ps = new PrintStream(baos, true, "UTF-8");
				
				// On demande au Throwable de s'afficher dans ps (et donc dans baos)
				// On vient de cr�er un tableau de bytes contenant les caract�res du message
				// de l'exception
				record.getThrown().printStackTrace( ps );
				
				// On convertit le tableau de bytes de baos en cha�ne de caract�res
				sb.append( new String( baos.toByteArray(), "UTF-8" ) );
			}
			
			catch( Exception e )
			{}
		}
		sb.append("\r\n");
		return sb.toString();
	}

	/**
	 * Add a new log to the log directory (level of the log, and a small description)
	 */
	public void newLog(Level level, String text) {
		Logger myLogger = Logger.getLogger("Peer-to-peer Logger");

		FileHandler fh = null;		
						
		// Check if the directory already exists
		if (Files.notExists(Paths.get("C:/logs"))) new File("C:/logs").mkdir();
		
		// Create one file.log per month
		try {
			fh = new FileHandler("C:/logs/" + new SimpleDateFormat("MMYYYY").format(new Date()) + ".log", true);
		} 
		catch (SecurityException | IOException e) {
			new CustomFormatter().newLog(Level.SEVERE, e.toString());
		}

		fh.setFormatter(CustomFormatter.this);
		myLogger.addHandler(fh);
		myLogger.log(level, text);
		fh.close();
	}
}