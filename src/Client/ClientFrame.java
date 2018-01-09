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

/**
 * Project name : PeerToPeer Class : ClientFrame
 *
 * Date of creation : 28.12.2017
 * 
 * Description : Main from for the client to comunicate with the server and other clients
 * 
 * @author Vlado Mitrovic
 */

@SuppressWarnings("serial")
public class ClientFrame extends JFrame {

	/* Client identification */
	protected static String name;
	protected static String id;
	protected static String ip;
	protected static File clientFolder;
	protected static int nbClients;

	/* Sockets */
	private Socket clientSocket = null;
	/* Output Stream */
	private ObjectOutputStream oOut = null;
	/* Input Stream */
	private ObjectInputStream oIn = null;

	/* FRONT END */
	private CardLayout c = new CardLayout();
	private JPanel cardPanel = new JPanel(c);
	private JPanel content = new JPanel(new BorderLayout(0, 0));
	private JPanel north = new JPanel();
	protected static JPanel center = new JPanel();
	private JPanel centerCenter = new JPanel();
	protected static JPanel centerSouth = new JPanel();
	private JPanel south = new JPanel();
	private JPanel clientsPanel = new JPanel(new BorderLayout(0, 0));
	private JPanel filesPanel = new JPanel(new BorderLayout(0, 0));

	private Button myFiles = new Button("my files");
	protected static JComboBox<DistantClient> clientsList = new JComboBox<DistantClient>();
	private JLabel clientLabel;
	protected static JTextPane text = new JTextPane();
	protected static StyledDocument doc = text.getStyledDocument();
	private JScrollPane scrollText = new JScrollPane(text);

	protected static DefaultListModel<File> modelClient = new DefaultListModel<File>();
	protected static DefaultListModel<File> modelServer = new DefaultListModel<File>();
	protected static JList<File> listServerFiles = new JList<File>(modelServer);
	protected static JList<File> listClientFiles = new JList<File>(modelClient);
	private JScrollPane scrollClient = new JScrollPane(listClientFiles);
	private JScrollPane scrollServer = new JScrollPane(listServerFiles);

	private Button sendButton = new Button("share", new ImageIcon("Icon/up.png"));
	private Button getButton = new Button("files shared", new ImageIcon("Icon/refresh.png"));
	private Button loadButton = new Button("download", new ImageIcon("Icon/dl.png"));
	private Button addButton = new Button("add a file", new ImageIcon("Icon/folder.png"));

	private Font font15 = new Font("Century Gothic", Font.BOLD, 15);
	private Font font45 = new Font("Century Gothic", Font.BOLD, 45);
	private Font font18 = new Font("Century Gothic", Font.BOLD, 18);
	private Color background = new Color(255, 204, 153);

	private ImageIcon snowman = new ImageIcon("Icon/snowman.png");
	private JLabel title = new JLabel("Snowman client", snowman, JLabel.LEFT);

	public static CustomFormatter ClientLogs = new CustomFormatter();

	public ClientFrame(String username, String ip, ObjectInputStream input, ObjectOutputStream output, Socket client) {

		super("Peer to Peer");
		
		ClientFrame.ClientLogs.newLog(Level.INFO, "Launching Client frame for : " + username);
		ClientFrame.name = username;
		ClientFrame.ip = ip;
		this.clientSocket = client;
		this.oIn = input;
		this.oOut = output;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(621, 614);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(0, 0));
		addWindowListener(new ExitClick());
		paint();
	}

	// Built the frame
	public void paint() {

		add(cardPanel, BorderLayout.CENTER);

		title.setFont(font45);
		title.setForeground(Color.WHITE);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		cardPanel.add(content, "content");
		content.add(north, BorderLayout.NORTH);
		content.add(center, BorderLayout.CENTER);
		center.setLayout(new GridLayout(1, 2));

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
		listClientFiles.setBackground(new Color(224, 224, 224));

		scrollClient.setPreferredSize(new Dimension(200, 390));
		scrollClient.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollClient.setBorder(null);

		scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollText.setPreferredSize(new Dimension(420, 420));
		scrollText.setBorder(null);
		scrollText.setBackground(new Color(255, 204, 153));
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

		listServerFiles.setBackground(new Color(0, 0, 0, 180));
		listServerFiles.setForeground(Color.WHITE);

		listServerFiles.setCellRenderer(new DefaultListCellRenderer());
		listServerFiles.setBackground(new Color(224, 224, 224));

		content.add(south, BorderLayout.SOUTH);
		south.setPreferredSize(new Dimension(820, 100));
		south.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		clientLabel = new JLabel(name + " " + ip);
		clientLabel.setFont(font18);
		clientLabel.setHorizontalAlignment(JLabel.CENTER);
		clientLabel.setPreferredSize(new Dimension(621, 60));

		north.add(clientLabel);
		;

		south.add(sendButton);
		sendButton.addActionListener(new SendClick());
		south.add(getButton);
		getButton.addActionListener(new GetClick());
		south.add(loadButton);
		loadButton.addActionListener(new DownloadClick());

		north.setPreferredSize(new Dimension(820, 65));
		north.setMaximumSize(new Dimension(820, 65));
		north.setMinimumSize(new Dimension(820, 65));
		north.setBackground(new Color(255, 204, 153));

		centerCenter.setPreferredSize(new Dimension(820, 450));
		centerCenter.setMaximumSize(new Dimension(820, 450));
		centerCenter.setMinimumSize(new Dimension(820, 450));

		south.setPreferredSize(new Dimension(820, 70));
		south.setMaximumSize(new Dimension(820, 70));
		south.setMinimumSize(new Dimension(820, 70));
		south.setBackground(new Color(255, 204, 153));

		createClientFolder(name);

		loadListOfFiles(getListOfFiles(clientFolder.getAbsolutePath()));

		runServer();
	}

	// Get all files in a foldes
	public File[] getListOfFiles(String pathFolder) {
		File folder = new File(pathFolder);
		return folder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});
	}

	// Update the list of files in the frame
	public void loadListOfFiles(File[] files) {
		
		modelClient.removeAllElements();
		
		for (File file : getListOfFiles(clientFolder.getAbsolutePath()))
			modelClient.addElement(file);
		
		sendButton.doClick();

	}
	

	

	// Exit properely when you close the client windows
	class ExitClick extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			try {
				if (clientSocket != null) {
					oOut.writeObject("logout");
					oOut.flush();
					ClientFrame.ClientLogs.newLog(Level.WARNING, "Send command logout");
					oIn.close();
					oOut.close();
					clientSocket.close();
					ClientFrame.ClientLogs.newLog(Level.INFO, "Closing socket");
				}
				System.exit(0);
			} catch (IOException e1) {
				ClientFrame.ClientLogs.newLog(Level.SEVERE, "IOException : " + e1.toString());
			}
		}
	}

	// Send the shared list of files
	class SendClick implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				sendInfo();
			} catch (IOException e1) {
				ClientFrame.ClientLogs.newLog(Level.SEVERE, "IOException : " + e1.toString());
			} catch (BadLocationException e1) {
				ClientFrame.ClientLogs.newLog(Level.SEVERE, "BadLocationException : " + e1.toString());
			}
		}
	}

	// Get the list of other clients and other shared files
	class GetClick implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				getFiles();
				if (clientsList.getItemCount() == 0) {
					ClientFrame.ClientLogs.newLog(Level.WARNING, "Client doesn't share files");
				}
			} catch (ClassNotFoundException e1) {
				ClientFrame.ClientLogs.newLog(Level.SEVERE, "ClassNotFoundException : " + e1.toString());
			} catch (IOException e1) {
				ClientFrame.ClientLogs.newLog(Level.SEVERE, "IOException : " + e1.toString());
			} catch (BadLocationException e1) {
				ClientFrame.ClientLogs.newLog(Level.SEVERE, "BadLocationException : " + e1.toString());
			}
		}
	}

	// Start the downloading of the selected file
	class DownloadClick implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!listServerFiles.isSelectionEmpty()) {
				try {
					download();
				} catch (IOException e1) {
					ClientFrame.ClientLogs.newLog(Level.SEVERE, "IOException : " + e1.toString());
				} catch (InterruptedException e1) {
					ClientFrame.ClientLogs.newLog(Level.SEVERE, "InterruptedException : " + e1.toString());
				}
			}
		}
	}

	// Display the shared files of the selected client
	class SelectClient implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (clientsList.getItemCount() != 0) {
				DistantClient selected = (DistantClient) clientsList.getSelectedItem();
				modelServer.removeAllElements();

				for (File file : selected.getListFiles()) {
					modelServer.addElement(file);
				}
			}
		}
	}

	// Open file chooser to add a file to shares
	class AddClick implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setMultiSelectionEnabled(true);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int resultat = fc.showDialog(null, "Ajouter");

			if (resultat == JFileChooser.APPROVE_OPTION) {
				File[] files = fc.getSelectedFiles();

				for (File file : files) {
					try {
						File destination = new File(clientFolder.getAbsolutePath() + "/" + file.getName());
						Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
						ClientFrame.ClientLogs.newLog(Level.INFO, "Adding file : " + file.getName());
						modelClient.addElement(file);
					} catch (FileAlreadyExistsException e1) {
						ClientFrame.ClientLogs.newLog(Level.WARNING, "FileAlreadyExistsException");
					} catch (IOException e1) {
						ClientFrame.ClientLogs.newLog(Level.SEVERE, "IOException : " + e1.toString());
					}
				}
			}
		}
	}

	// Set the directory of the client
	// Create one if doesn't exist
	private void createClientFolder(String username) {

		clientFolder = new File("C:/Snowman/" + username);

		if (!clientFolder.exists()) {
			ClientFrame.ClientLogs.newLog(Level.INFO, "Create folder for the client");
			clientFolder.mkdirs();
		}
		createClientLogsFolder();
	}

	// Set the logs directory of the client
	// Create one if doesn't exist
	private void createClientLogsFolder() {
		File logsFolder = new File(clientFolder.getAbsolutePath() + "/logs");

		if (!logsFolder.exists()) {
			ClientFrame.ClientLogs.newLog(Level.INFO, "Create logs folder for the client");
			logsFolder.mkdirs();
		}
	}

	// Send list of shared files to the server
	public void sendInfo() throws IOException, BadLocationException {
		/* Envoie de la commande post */
		ClientFrame.ClientLogs.newLog(Level.INFO, "Send command post");
		oOut.writeObject("post");
		oOut.flush();

		ClientFrame.ClientLogs.newLog(Level.INFO, "Send list of files in :" + clientFolder.getAbsolutePath());
		oOut.writeObject(getListOfFiles(clientFolder.getAbsolutePath()));
		oOut.flush();

	}

	// Get list of others clients and shared files from the server
	public void getFiles() throws IOException, ClassNotFoundException, BadLocationException {
		/* Envoie de la commande get */

		ClientFrame.ClientLogs.newLog(Level.INFO, "Send command get");
		oOut.writeObject("get");
		oOut.flush();
		nbClients=0;

		nbClients = (int) oIn.readObject();

		ClientFrame.ClientLogs.newLog(Level.INFO, "Clients : " + nbClients);


		clientsList.removeAllItems();
		/* Réception des clients */
		for (int i = 0; i < nbClients; i++) {
			String name = (String) oIn.readObject();
			String ip = (String) oIn.readObject();
			File[] files = (File[]) oIn.readObject();

			ClientFrame.ClientLogs.newLog(Level.INFO, "Add user in list : " + name);

			// Only add the other clients
			if (!name.equals(this.name))
				clientsList.addItem(new DistantClient(name, ip, files));
		}
	}

	// Start the thread to download a file
	public void download() throws IOException, InterruptedException {

		Thread t = new Thread(new ThreadDownload());
		t.start();
		
		loadListOfFiles(getListOfFiles(clientFolder.getAbsolutePath()));
		
	}

	// Start the thread to upload a file
	public void runServer() {
		Thread t = new Thread(new RunServer());
		t.start();
		
	}
}