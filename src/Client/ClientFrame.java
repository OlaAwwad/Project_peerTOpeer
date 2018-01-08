package Client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Server.CustomFormatter;
import Server.ExistedClient;
import Server.LoginClients;

//====================================================================================================== //
//----------------------------------------- Class : ClientFrame -----------------------------------------//
//====================================================================================================== //
//Nom de la classe : ClientFrame                                                                         //
//Description de la classe : Cette classe permet de créer la frame du client                             //
//------------------------------------------------------------------------------------------------------ //
//Remarque : -                                                                                           //
//------------------------------------------------------------------------------------------------------ //
@SuppressWarnings("serial")
public class ClientFrame extends JFrame{
	
	/*Client identification*/
	protected static String name ;
	protected static String id;
	protected static String ip;
	protected static File clientFolder;
	protected static int nbClients;

	/*Sockets*/
	private Socket clientSocket = null;
	/*Output Stream*/
	private ObjectOutputStream oOut = null;
	/*Input Stream*/
	private ObjectInputStream oIn = null;

	/*FRONT END*/	
	private CardLayout c = new CardLayout();
	private JPanel cardPanel = new JPanel(c);
	private JPanel content = new JPanel(new BorderLayout(0,0));	
	private JPanel north = new JPanel();
	protected static JPanel center = new JPanel();
	private JPanel centerCenter = new JPanel();
	protected static JPanel centerSouth = new JPanel();
	private JPanel south = new JPanel();
	private JPanel clientsPanel = new JPanel(new BorderLayout(0,0));
	private JPanel filesPanel = new JPanel(new BorderLayout(0,0));
	
	
	private Button myFiles = new Button("my files");
	protected static JComboBox<ExistedClient> clientsList = new JComboBox<ExistedClient>();
	private JLabel clientLabel;
	protected static JTextPane text = new JTextPane();
	protected static StyledDocument doc = text.getStyledDocument();
	private JScrollPane scrollText = new JScrollPane(text);
	
	protected static DefaultListModel<File> modelClient = new DefaultListModel<File>();
	protected static DefaultListModel<File> modelServer = new DefaultListModel<File>();
	protected static JList<File>listServerFiles = new JList<File>(modelServer);
	protected static JList<File>listClientFiles = new JList<File>(modelClient);
	private JScrollPane scrollClient = new JScrollPane(listClientFiles);
	private JScrollPane scrollServer = new JScrollPane(listServerFiles);

	private Button sendButton = new Button("share", new ImageIcon("Icon/up.png"));
	private Button getButton = new Button("files shared", new ImageIcon("Icon/refresh.png"));
	private Button loadButton = new Button("download", new ImageIcon("Icon/dl.png"));
	private Button addButton = new Button("add a file",new ImageIcon("Icon/folder.png"));
	
	private Font font15 = new Font("Century Gothic", Font.BOLD, 15);
	private Font font45 = new Font("Century Gothic", Font.BOLD, 45);
	private Font font18 = new Font("Century Gothic", Font.BOLD, 18);
	private Color background = new Color(255,204,153);
	
	private ImageIcon snowman = new ImageIcon("Icon/snowman.png");
	private JLabel title = new JLabel("Snowman client", snowman, JLabel.LEFT);
	
	
 	protected static Logger myLogger = Logger.getLogger("TestLog");
 	
 	public ClientFrame(String username, String ip, ObjectInputStream input, ObjectOutputStream output, Socket client) {
		
 		super("Peer to Peer");
		ClientFrame.name=username;
		this.ip=ip;
		this.clientSocket=client;
		this.oIn=input;
		this.oOut=output;
		
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(621, 614);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(0,0));
		addWindowListener(new ExitClick());	
		paint();
	}
 	
	// ====================================================================================================== //
  	// ------------------------------------ Méthode : build ---------------------------------------           //
  	// ====================================================================================================== //
  	// Nom de la méthode : build                                                                              //
  	// Description de la méthode : Permet de construire la fenêtre                                            //
  	// ------------------------------------------------------------------------------------------------------ //
  	// Entrée(s) : -                                                                                          //
  	// Sortie : -                                                                                             //
  	// ------------------------------------------------------------------------------------------------------ //
  	// Remarque : -                                                                                           //
  	// ------------------------------------------------------------------------------------------------------ //
	public void paint()
	{
		
		add(cardPanel, BorderLayout.CENTER);
	

		title.setFont(font45);
		title.setForeground(Color.WHITE);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
	
		
		
		cardPanel.add(content, "content");
		content.add(north, BorderLayout.NORTH);
		content.add(center, BorderLayout.CENTER);
		center.setLayout(new GridLayout(1,2));		
		
		center.add(filesPanel);
		center.add(clientsPanel);
	
		
		filesPanel.add(addButton, BorderLayout.SOUTH);
		filesPanel.add(myFiles, BorderLayout.NORTH);
		filesPanel.add(scrollClient, BorderLayout.CENTER);

		addButton.setPreferredSize(new Dimension(200, 30));
		addButton.setForeground(Color.white);
		addButton.setFont(font15);

		addButton.addActionListener(new AddClick());		
		myFiles.setPreferredSize(new Dimension(200, 30));
		myFiles.setForeground(Color.white);
		myFiles.setFont(font15);
		myFiles.setHorizontalAlignment(JLabel.CENTER);

		
		listClientFiles.setForeground(Color.WHITE);
		listClientFiles.setCellRenderer(new DefaultListCellRenderer());
		listClientFiles.setBackground(new Color(224,224,224));
		
		scrollClient.setPreferredSize(new Dimension(200, 390));
		scrollClient.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollClient.setBorder(null);
		
		


		scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollText.setPreferredSize(new Dimension(420, 420));
		scrollText.setBorder(null);
		scrollText.setBackground(new Color(255,204,153));
		text.setOpaque(false);

		MutableAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_LEFT);
		attributes.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
		attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, background);
		attributes.addAttribute(StyleConstants.CharacterConstants.FontFamily, "Montserrat");
		attributes.addAttribute(StyleConstants.CharacterConstants.FontSize, 15);
		doc.setParagraphAttributes(0, 0, attributes, false);
	
		

		clientsPanel.add(clientsList, BorderLayout.NORTH);
		clientsList.setPreferredSize(new Dimension(200, 30));
		clientsList.addActionListener(new SelectClient());
		clientsPanel.add(scrollServer, BorderLayout.CENTER);
		
		scrollServer.setPreferredSize(new Dimension(200, 420));
		scrollServer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollServer.setBorder(null);

		listServerFiles.setBackground(new Color(0,0,0,180));
		listServerFiles.setForeground(Color.WHITE);

		listServerFiles.setCellRenderer(new DefaultListCellRenderer());
		listServerFiles.setBackground(new Color(224,224,224));
	
		content.add(south, BorderLayout.SOUTH);
		south.setPreferredSize(new Dimension(820, 100));
		south.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		clientLabel = new JLabel(name+" "+ip);
		clientLabel.setFont(font18);
		clientLabel.setHorizontalAlignment(JLabel.CENTER);
		clientLabel.setPreferredSize(new Dimension(621, 60));
		
		north.add(clientLabel);;
		
		
		south.add(sendButton);
		sendButton.addActionListener(new SendClick());		
		south.add(getButton);
		getButton.addActionListener(new GetClick());		
		south.add(loadButton);
		loadButton.addActionListener(new DownloadClick());	

		north.setPreferredSize(new Dimension(820, 65));
		north.setMaximumSize(new Dimension(820, 65));
		north.setMinimumSize(new Dimension(820, 65));
		north.setBackground(new Color(255,204,153));
		
		centerCenter.setPreferredSize(new Dimension(820, 450));
		centerCenter.setMaximumSize(new Dimension(820, 450));
		centerCenter.setMinimumSize(new Dimension(820, 450));
		
		south.setPreferredSize(new Dimension(820, 70));
		south.setMaximumSize(new Dimension(820, 70));
		south.setMinimumSize(new Dimension(820, 70));
		south.setBackground(new Color(255,204,153));
				
		
		
		createClientFolder(name);
		
		loadListOfFiles(getListOfFiles(clientFolder.getAbsolutePath()));
		
		runServer();
				
	}
	

		
		
	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : getListOfFiles ---------------------------------------  //
	  	// ====================================================================================================== //
	  	// Nom de la méthode : getListOfFiles                                                                     //
	  	// Description de la méthode : Liste tous les fichiers d'un dossier                                       //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : String pathFolder                                                                          //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		public File[] getListOfFiles(String pathFolder)
		{
			File folder = new File(pathFolder);
			return folder.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File file) {
					return file.isFile();
				}
			});
		}
		
		
	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : loadListOfFiles --------------------------------------- //
	  	// ====================================================================================================== //
	  	// Nom de la méthode : loadListOfFiles                                                                    //
	  	// Description de la méthode : Met à jour visuellement la liste de fichier                                //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : File[] files                                                                               //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		public void loadListOfFiles(File[] files)
		{
			
			for(File file : getListOfFiles(clientFolder.getAbsolutePath()))
				modelClient.addElement(file);
			
		}
		
	  	// ====================================================================================================== //
	  	// ------------------------------------ Classe : InputFocus ---------------------------------------       //
	  	// ====================================================================================================== //
	  	// Nom de la classe : InputFocus                                                                          //
	  	// Description de la méthode : Gère le focus des textfields                                               //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) :                                                                                            //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		class InputFocus implements FocusListener
		{

			private JTextField text;
			
			public InputFocus(JTextField text) {
				this.text = text;
			}
			@Override
			public void focusGained(FocusEvent e) {
				text.setText("");
				
			}

			@Override
			public void focusLost(FocusEvent e) {			
			}
			
		}
		
	 
		
	 	// ====================================================================================================== //
	  	// ------------------------------------ Classe : DisconnectClick ---------------------------------------  //
	  	// ====================================================================================================== //
	  	// Nom de la classe : DisconnectClick                                                                     //
	  	// Description de la méthode : Permet de gérer la déconnexion                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) :                                                                                            //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		class DisconnectClick implements ActionListener
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					/*Sending logout message to server*/
					oOut.writeObject("logout");
					oOut.flush();
					myLogger.setLevel(Level.INFO);
					myLogger.info("Sending to server logout message");
					
					
					
					oIn.close();
					oOut.close();
					clientSocket.close();
					myLogger.setLevel(Level.INFO);
					myLogger.info("Closing OutputStream, InputStream and Socket");
					System.exit(0);
					myLogger.setLevel(Level.INFO);
					myLogger.info("Closing window");
				} 
				catch (IOException e1) 
				{
						
					myLogger.setLevel(Level.SEVERE);
					myLogger.severe("IOException : "+e1.toString());
				}
				
			}
			
		}
	 	// ====================================================================================================== //
	  	// ------------------------------------ Classe : ExitClick ---------------------------------------  //
	  	// ====================================================================================================== //
	  	// Nom de la classe : ExitClick                                                                     //
	  	// Description de la méthode : Permet de gérer la déconnexion                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) :                                                                                            //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		class ExitClick extends WindowAdapter
		{
			@Override
			public void windowClosing(WindowEvent e) {
				try 
				{
					if(clientSocket != null)
					{
						oOut.writeObject("logout");
						oOut.flush();
						myLogger.setLevel(Level.INFO);
						myLogger.info("Sending to server logout message");
						
						oIn.close();
						oOut.close();
						clientSocket.close();
						myLogger.setLevel(Level.INFO);
						myLogger.info("Closing OutputStream, InputStream and Socket");
					}
					System.exit(0);
					myLogger.setLevel(Level.INFO);
					myLogger.info("Closing window");
				} 
				catch (IOException e1) 
				{
					myLogger.setLevel(Level.SEVERE);
					myLogger.severe("IOException : "+e1.toString());
				}

				
			}		
		}
		
	 	// ====================================================================================================== //
	  	// ------------------------------------ Classe : SendClick ---------------------------------------        //
	  	// ====================================================================================================== //
	  	// Nom de la classe : SendClick                                                                           //
	  	// Description de la méthode : Permet l'envoie des informations du client au serveur                      //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) :                                                                                            //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		class SendClick implements ActionListener
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				try 
				{
					sendInfo();
				} 
				catch (IOException e1) 
				{
					myLogger.setLevel(Level.SEVERE);
					myLogger.severe("IOException : "+e1.toString());
				} 
				catch (BadLocationException e1) 
				{
					myLogger.setLevel(Level.SEVERE);
					myLogger.severe("BadLocationException : "+e1.toString());
				}
				
			}
			
		}
		
	 	// ====================================================================================================== //
	  	// ------------------------------------ Classe : GetClick ---------------------------------------         //
	  	// ====================================================================================================== //
	  	// Nom de la classe : GetClick                                                                            //
	  	// Description de la méthode : Permet la récuparion des fichiers disponibles sur le serveur               //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) :                                                                                            //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		class GetClick implements ActionListener
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
						getFiles();
						if(clientsList.getItemCount() == 0)
						{
							new CustomFormatter().newLog(Level.SEVERE, "NO OTHER CLIENT CONNECTED");
						}
					} 
					catch (ClassNotFoundException e1) 
					{
						myLogger.setLevel(Level.SEVERE);
						myLogger.severe("ClassNotFoundException : "+e1.toString());
					} 
					catch (IOException e1) 
					{
						myLogger.setLevel(Level.SEVERE);
						myLogger.severe("IOException : "+e1.toString());
					} 
					catch (BadLocationException e1) 
					{
						myLogger.setLevel(Level.SEVERE);
						myLogger.severe("BadLocationException : "+e1.toString());
					}

				
			}
			
		}
		
	 	// ====================================================================================================== //
	  	// ------------------------------------ Classe : DownloadClick ---------------------------------------    //
	  	// ====================================================================================================== //
	  	// Nom de la classe : DownloadClick                                                                       //
	  	// Description de la méthode : Permet le téléchargement d'un fichier                                      //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) :                                                                                            //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		class DownloadClick implements ActionListener
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!listServerFiles.isSelectionEmpty())
				{
					try 
					{
						download();
					} 
					catch (IOException e1) 
					{
						myLogger.setLevel(Level.SEVERE);
						myLogger.severe("IOException : "+e1.toString());
					} 
					catch (InterruptedException e1) 
					{
						myLogger.setLevel(Level.SEVERE);
						myLogger.severe("InterruptedException : "+e1.toString());
					}
				}
				
				
			}
			
		}
		
	 	// ====================================================================================================== //
	  	// ------------------------------------ Classe : SelectClient ---------------------------------------     //
	  	// ====================================================================================================== //
	  	// Nom de la classe : SelectClient                                                                        //
	  	// Description de la méthode : affiche les fichiers par sélection de client                               //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) :                                                                                            //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		class SelectClient implements ActionListener
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(clientsList.getItemCount() != 0)
				{
					ExistedClient selected = (ExistedClient) clientsList.getSelectedItem();
					System.out.println(selected.getUserName());
					modelServer.removeAllElements();
					
					for(File file : selected.getListFiles())
					{
						modelServer.addElement(file);
					}
				}
			}

		}
		

	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : AddClick ---------------------------------------        //
	  	// ====================================================================================================== //
	  	// Nom de la méthode : AddClick                                                                           //
	  	// Description de la méthode : Permet d'ajouter un fichier à notre répertoire personnel                   //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : -                                                                                          //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		
		class AddClick implements ActionListener
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(true);
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int resultat = fc.showDialog(null, "Ajouter");
				
				if(resultat == JFileChooser.APPROVE_OPTION)
				{
					File[] files = fc.getSelectedFiles();
					
					for (File file : files) {
						try 
						{
							File destination = new File(clientFolder.getAbsolutePath()+"/"+file.getName());
							Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
							modelClient.addElement(file);
						}  
						catch (FileAlreadyExistsException e1)
						{
							myLogger.setLevel(Level.SEVERE);
							myLogger.severe("FileAlreadyExistsException : "+e1.toString());
						}
						catch (IOException e1) {
							myLogger.setLevel(Level.SEVERE);
							myLogger.severe("IOException : "+e1.toString());
						}
					}
				}
				
			}


			
		}
	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : connect ---------------------------------------         //
	  	// ====================================================================================================== //
	  	// Nom de la méthode : connect                                                                            //
	  	// Description de la méthode : Gère la connexion au serveur                                               //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : -                                                                                          //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		

		
		
	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : createClientFolder ------------------------------------ //
	  	// ====================================================================================================== //
	  	// Nom de la méthode : createClientFolder                                                                 //
	  	// Description de la méthode : Créer le répertoire de l'application et du client connecté 				  //
		// s'ils n'existe pas encore                                                                              //		
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : String username                                                                            //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		private void createClientFolder(String username)
		{
			clientFolder = new File("C:/Snowman/"+username);
			
			if(!clientFolder.exists())
				clientFolder.mkdirs();
			
			createClientLogsFolder();
		}
	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : createClientLogsFolder ---------------------------------//
	  	// ====================================================================================================== //
	  	// Nom de la méthode : createClientLogsFolder                                                             //
	  	// Description de la méthode : Créer le répertoire de l'application et du client connecté 				  //
		// s'ils n'existe pas encore                                                                              //		
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : String username                                                                            //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		private void createClientLogsFolder()
		{
			File logsFolder = new File(clientFolder.getAbsolutePath()+"/logs");
			
			if(!logsFolder.exists())
				logsFolder.mkdirs();
		}
		
		
		
		
	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : sendInfo ------------------------------------           //
	  	// ====================================================================================================== //
	  	// Nom de la méthode : sendInfo                                                   v                       //
	  	// Description de la méthode : Envoie la liste de fichiers au serveur                                     //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : -                                                                                          //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		public void sendInfo() throws IOException, BadLocationException
		{	
			/*Envoie de la commande post*/
			oOut.writeObject("post");
			oOut.flush();
			
			/*Envoie de la liste de fichiers du client*/
			oOut.writeObject(getListOfFiles(clientFolder.getAbsolutePath()));
			oOut.flush();


			
		}
		
	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : getFiles ------------------------------------           //
	  	// ====================================================================================================== //
	  	// Nom de la méthode : getFiles                                                   v                       //
	  	// Description de la méthode : Récupère les fichiers disponibles sur le serveur                           //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : -                                                                                          //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		
		public void getFiles() throws IOException, ClassNotFoundException, BadLocationException
		{		
			/*Envoie de la commande get*/
			
			oOut.writeObject("get");
			oOut.flush();

			
			/*Réception du nombre de clients dipsonibles*/
			nbClients = (int)oIn.readObject();

			System.out.println(nbClients);
				
			/*Ajoute la jcombox des clients connectés*/
			
			clientsList.removeAllItems();
			/*Réception des clients*/
			for(int i = 0; i < nbClients; i++)
			{
				String name = (String) oIn.readObject();
				String ip = (String) oIn.readObject();
				File[] files = (File[]) oIn.readObject();
		
				
				if(!name.equals(this.name))
					clientsList.addItem(new ExistedClient (name, ip, files));
					
					
				
			}
		}

		
	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : download ------------------------------------           //
	  	// ====================================================================================================== //
	  	// Nom de la méthode : download                                                                           //
	  	// Description de la méthode : Lance le thread pour télécharger le fichier                                //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : -                                                                                          //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		public void download() throws IOException, InterruptedException
		{
			
			Thread t = new Thread(new ThreadDownload());
			t.start();	
		}	
	  	// ====================================================================================================== //
	  	// ------------------------------------ Méthode : runServer ------------------------------------          //
	  	// ====================================================================================================== //
	  	// Nom de la méthode : runServer                                                                          //
	  	// Description de la méthode : Lance le thread serveur du client                                          //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Entrée(s) : -                                                                                          //
	  	// Sortie : -                                                                                             //
	  	// ------------------------------------------------------------------------------------------------------ //
	  	// Remarque : -                                                                                           //
	  	// ------------------------------------------------------------------------------------------------------ //
		public void runServer()
		{
			Thread t = new Thread(new RunServer());
			t.start();
		}
}