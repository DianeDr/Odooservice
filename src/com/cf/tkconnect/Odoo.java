package com.cf.tkconnect;

import java.io.File;

import com.cf.tkconnect.log.WebLinkLogLoader;
import com.cf.tkconnect.util.FileUtils;
import com.cf.tkconnect.util.InitialSetUp;
import com.cf.tkconnect.util.PingServer;
import com.cf.tkconnect.util.WSConstants;



import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.IOException;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import com.cf.tkconnect.util.FileUtils;


import static com.cf.tkconnect.util.WSConstants.*;
import com.cf.tkconnect.util.WSUtil;


public class Odoo extends JFrame implements ActionListener, WindowListener{

	
	String propPath = "bin/tkconnect_system.properties";

	InitialSetUp setup = null;
	
	
	private static final long serialVersionUID = 1L;

	String authKey = "";

	String url = "";
	private String connectMode = "odoo";
	
	Icon icon = null;
	boolean started = false;

	boolean useProxy = false;

	JPanel basePanel = null;

	JPanel mainPanel = null;

	JPanel topPanel = null;

	JPanel proxyPanel = null;

	JPanel bottomPanel = null;

	JPanel buttonPanel = null;

	/*
	 * Labels
	 */
	Font f = new Font("Arial", Font.PLAIN, 12);

	JLabel urlLabel = new JLabel("Odoo URL: ", JLabel.TRAILING);

	JLabel intervalLabel = new JLabel("Polling (in mSeconds): ",
			JLabel.TRAILING);

	JLabel pHostLabel = new JLabel("Address: ", JLabel.TRAILING);

	JLabel pPortLabel = new JLabel("Port: ", JLabel.TRAILING);

	JLabel dirLabel = new JLabel("Directory Location: ", JLabel.TRAILING);

	JLabel shortNameLabel = new JLabel("User Name: ", JLabel.TRAILING);

	JLabel authLabel = new JLabel("Password: ", JLabel.TRAILING);

//	JLabel serviceLabel = new JLabel("Default Service Name: ", JLabel.TRAILING);

	/*
	 * TextFields
	 */
	JTextField urlField = new JTextField(10);

	JTextField intervalField = new JTextField(10);

	JTextField pHostField = new JTextField(10);

	JTextField pPortField = new JTextField(6);

	JTextField dirField = new JTextField(10);

	JTextField shortNameField = new JTextField(10);

	JTextField authField = new JPasswordField(10);

//	JTextField serviceField = new JTextField(10);

	/*
	 * checkbox
	 */
	JCheckBox proxyCheck = new JCheckBox("Use Proxy Server");

	/*
	 * Buttons
	 */
	JButton startBtn = new JButton("Start");

	JButton stopBtn = new JButton("Stop");

	JButton exitBtn = new JButton("Exit");


	/*
	 * Removed locking in 8.5 as we are doing an installation and also portable
	 * to UNIX FileLock lock = null;
	 * 
	 * FileChannel channel = null;
	 */
	/*
	 * color code for bg
	 */
	int r = (0xd8e0e3 & 0xff0000) >> 16;

	int g = (0xd8e0e3 & 0xff00) >> 8;

	int b = 0xd8e0e3 & 0xff;

	Color bg = new Color(r, g, b);



	public Odoo( String ohome){
		
		System.out.println("odoo construc start::"+ohome);
		System.out.println("odoo init logs");
		WSConstants.appHome = ohome+ File.separator;
		FileUtils.updateLog4jProperties(ohome + "log" + File.separator);
		WebLinkLogLoader.initLoggers();
		
		
	}
	
	
	
	public static void main(String[] args) {
		try {
			String appHome = System.getProperty("ODOO_HOME");
			Odoo frame = new Odoo(appHome);
			frame.init();
			frame.pack();
			frame.setVisible(true);
			frame.loadValues();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent evt) {
		String btnName = evt.getActionCommand();

		if (btnName.equals("Exit")) {
			WebLinkLogLoader.getLogger(Odoo.class).debug(
					"Shutting down Odoo...Client");
			stopPollingThread();
			System.exit(0);
		} else if (btnName.equals("Stop")) {
			WSUtil.setStop(true);
			stopBtn.setEnabled(false);
			startBtn.setEnabled(true);
			WebLinkLogLoader.getLogger(Odoo.class).debug("Odoo Client Stopped...");
			//WebLinkLogLoader.JobLogger.debug("Odoo Stopped...");
			enableAllFields(true);
			stopPollingThread();
		} else if (btnName.equals("Use Proxy Server")) {
			toggleProxyFields(proxyCheck.isSelected());
		} else if (btnName.equals("Start")) {
			String err = validateValues();
			if (err != null) {
				JOptionPane.showMessageDialog(this, err, "Error",
						JOptionPane.OK_OPTION, icon);
			} else {
				startBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				// update all the files
				//updateProperties();
				started = true;
				WSUtil.setStop(false);
				enableAllFields(false);
				this.setState(JFrame.ICONIFIED);
				try {
					setup = new InitialSetUp();
					setup.init(appHome);
					//InitialSetUp.odoo.put(arg0, arg1) = authKey;
					//InitialSetUp.url = url;
					//InitialSetUp.useProxy = useProxy;
					
				} catch (Exception ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}
	
	public boolean startOdooClient(){
		
		WSUtil.setStop(false);
		try {
//			SetUpManager.authKey = authKey;
//			SetUpManager.url = url;
			//SetUpManager.useProxy = useProxy;
			System.out.println("Odoo start client");
			InitialSetUp.isusermode = false;
			setup = new InitialSetUp();
			setup.init(appHome);
			connectMode = "fileconvertor";
			
		} catch (Exception ioe) {
			ioe.printStackTrace();
			return false;
		}
		started = true;
		return started;
	}
	
	
	
	public boolean stopOdooClient(){
		WSUtil.setStop(true);
		stopPollingThread();
		WebLinkLogLoader.getLogger(Odoo.class).debug("Odoo client Stopped...");
		return true;
	}

	private void stopPollingThread() {
		if ( setup != null ) {
			setup.stopPolling();
		}
	}

	private void enableAllFields(boolean enable) {
		urlField.setEnabled(enable);
		authField.setEnabled(enable);
		pHostField.setEnabled(enable);
		pPortField.setEnabled(enable);
		dirField.setEnabled(enable);
		intervalField.setEnabled(enable);
//		serviceField.setEnabled(enable);
		shortNameField.setEnabled(enable);
		proxyCheck.setEnabled(enable);

	}

	private void toggleProxyFields(boolean chk) {
		useProxy = chk;
		pHostField.setEnabled(chk);
		pPortField.setEnabled(chk);
	}

	private String validateValues() {
		url = urlField.getText();
	/*	if (url == null || url.trim().length() == 0) {
			return "Server URL field cannot be empty!";
		} else {
			// check the server connection right away
			System.out.println((new java.util.Date())+" in validateValues url:"+url);
			String pingVal = PingServer.getServerResponse(url);
			if (pingVal != null) {
				return "Cannot connect to server! \n" + pingVal;
			}
		}
		*/
		String interval = intervalField.getText();
		int intVal = 0;
		try {
			intVal = Integer.parseInt(interval);
			if(intVal < MIN_INTERVAL)
				return "Polling interval value cannot be less than "+MIN_INTERVAL;
		}
		catch(NumberFormatException e) {
			return "Invalid value for Interval field.";
		}
		authKey = authField.getText();
/*		if (authKey == null || authKey.trim().length() == 0) {
			return "Authentication Key field cannot be empty!";
		}
		*/
		if (useProxy) {
			if (pHostField.getText().trim().length() == 0
					|| pPortField.getText().trim().length() == 0) {
				return "Invalid Proxy setting values!";
			}
		}
		String basedir = dirField.getText();
		/*	if (basedir == null || basedir.trim().length() == 0) {
				return "The Directory Location field cannot be empty!";
			} else {
			*/
		if (basedir.indexOf(":") < 0) {
			basedir = ".." + File.separator + basedir;
		}
		File f = new File(basedir);
		f.mkdirs();
		if (!f.exists()) {
			return "Unable to create directory!";
		} else {
			try {
				String p = f.getCanonicalPath();
				dirField.setText(p);
			} catch (Exception e) {
			}
		}
		//}
		return null;
	}

	public void loadValues() {
		// check if an instance of Odoo is running already
		if (isOdooRunning()) {
			JOptionPane.showMessageDialog(this,
					"An instance of Odoo is already running!", "Error",
					JOptionPane.OK_OPTION, icon);
			System.exit(0);
		}

		Properties props = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(appHome + propPath);
			props.load(fis);
			urlField.setText(props.getProperty(WSConstants.COMPANY_URL));
			dirField.setText(props.getProperty(PROPERTY_BASE_DIRECTORY));
			intervalField.setText(props.getProperty(PROPERTY_SCAN_INTERVAL));
			pHostField.setText(props.getProperty(PROPERTY_PROXYHOST));
			pPortField.setText(props.getProperty(PROPERTY_PROXYPORT));
			shortNameField.setText(props.getProperty("tkconnect.odoo.username"));
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private boolean isOdooRunning() {
		return false;
		/*
		 * try { // Get a file channel for the file File tempdir = new
		 * File("C:/temp"); if(!tempdir.exists()) { tempdir.mkdir(); } File f =
		 * new File("C:/temp/uc.lck"); //channel = new RandomAccessFile(f,
		 * "rw").getChannel(); try { BufferedWriter bw = new BufferedWriter(new
		 * FileWriter(f .getAbsolutePath())); bw.write(" "); bw.flush();
		 * bw.close(); //lock = channel.tryLock(); } catch (Exception e) {
		 * e.printStackTrace(); return true; } return false; } catch (Exception
		 * e) { return true; } This is blocked to remove any OS dependent file
		 * path and rely more on registry
		 * 
		 */
	}

	public void updateProperties() {
		Properties props = new Properties();
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(appHome + propPath);
			props.load(fis);
			props.setProperty(COMPANY_URL, urlField.getText().trim());
			props.setProperty(PROPERTY_BASE_DIRECTORY, dirField.getText()
					.trim());
			props.setProperty(PROPERTY_SCAN_INTERVAL, intervalField.getText()
					.trim());
			props.setProperty(PROPERTY_PROXYHOST, pHostField.getText().trim());
			props.setProperty(PROPERTY_PROXYPORT, pPortField.getText().trim());
			props.setProperty("tkconnect.odoo.username", shortNameField.getText()
					.trim());
		//	fos = new FileOutputStream(appHome + propPath);
		//	props.store(fos, "WARNING! DO NOT EDIT this file manually.");
		
		} catch (Exception ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void init() {
		
		System.out.println("Odoo init");
		appHome = appHome.endsWith("/") ? appHome : appHome + "/";
		WebLinkLogLoader.initLoggers();
		FileUtils.updateLog4jProperties(appHome + "log" + File.separator);

		initializeBGColor();
		buildMainpanel();
		buildButtonPanel();
		basePanel = new JPanel();
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
		basePanel.add(mainPanel);
		basePanel.add(buttonPanel);
		this.getContentPane().add(basePanel);
		this.setTitle("TKConnect - odoo ");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage(appHome + "images/odoo.gif");
		this.setIconImage(img);
		icon = new ImageIcon(appHome + "images/stop.gif");
		Dimension d = new Dimension(510, 400);
		this.setSize(d);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(SCREEN_SIZE.width / 2 - this.getWidth() / 2,
				SCREEN_SIZE.height / 2 - this.getHeight() / 2);
		basePanel.setSize(d);
		this.setContentPane(basePanel);
		this.addWindowListener(this);
	}

	private void initializeBGColor() {
		UIManager.put("OptionPane.background", bg);
		UIManager.put("OptionPane.font", f);
		UIManager.put("OptionPane.messageFont", f);
		UIManager.put("Panel.background", bg);
		UIManager.put("Button.background", bg);
	}

	private void buildMainpanel() {
		buildTopPanel();
		buildProxyPanel();
		buildBottomPanel();
		mainPanel = new JPanel();
		TitledBorder tb = new TitledBorder("Settings");
		tb.setTitleJustification(TitledBorder.DEFAULT_JUSTIFICATION);
		mainPanel.setBorder(tb);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(proxyPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
	}

	private void buildTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new SpringLayout());
		dirLabel.setFont(f);
		intervalLabel.setFont(f);
		topPanel.add(dirLabel);
		dirLabel.setLabelFor(dirField);
		topPanel.add(dirField);
		topPanel.add(getStarLabel());
		topPanel.add(intervalLabel);
		intervalLabel.setLabelFor(intervalField);
		topPanel.add(intervalField);
		topPanel.add(getEmptyLabel());
		SpringUtilities.makeCompactGrid(topPanel, 2, 3, // rows,// cols
				10, 10, // initX, initY
				10, 10); // xPad, yPad
	}

	private void buildProxyPanel() {
		proxyPanel = new JPanel();
		JPanel chkPanel = new JPanel();
		chkPanel.setLayout(new BorderLayout());
		proxyCheck.setBackground(bg);
		proxyCheck.setFont(f);
		proxyCheck.addActionListener(this);
		chkPanel.add(proxyCheck, BorderLayout.WEST);
		proxyPanel.setLayout(new BoxLayout(proxyPanel, BoxLayout.Y_AXIS));
		TitledBorder tb = new TitledBorder("Proxy Server");
		tb.setTitleJustification(TitledBorder.DEFAULT_JUSTIFICATION);
		proxyPanel.setBorder(tb);
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		pHostLabel.setFont(f);
		p.add(pHostLabel);
		p.add(pHostField);
		pPortLabel.setFont(f);
		p.add(pPortLabel);
		p.add(pPortField);
		proxyPanel.add(chkPanel);
		proxyPanel.add(p);
		toggleProxyFields(false);
	}

	private void buildBottomPanel() {
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new SpringLayout());
		urlLabel.setFont(f);
		bottomPanel.add(urlLabel);
		bottomPanel.add(urlField);
		bottomPanel.add(getStarLabel());
		shortNameLabel.setFont(f);
		bottomPanel.add(shortNameLabel);
		bottomPanel.add(shortNameField);
		bottomPanel.add(getEmptyLabel());
		authLabel.setFont(f);
		bottomPanel.add(authLabel);
		bottomPanel.add(authField);
/*		bottomPanel.add(getStarLabel());
		serviceLabel.setFont(f);
		bottomPanel.add(serviceLabel);
		bottomPanel.add(serviceField);
		*/
		bottomPanel.add(getEmptyLabel());
		SpringUtilities.makeCompactGrid(bottomPanel, 3, 3, // rows,// cols
				10, 10, // initX, initY
				10, 10); // xPad, yPad
	}

	private void buildButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(startBtn);
		buttonPanel.add(stopBtn);
		buttonPanel.add(exitBtn);
		startBtn.addActionListener(this);
		stopBtn.addActionListener(this);
		exitBtn.addActionListener(this);
		stopBtn.setEnabled(false);
		startBtn.setBackground(bg);
		stopBtn.setBackground(bg);
		exitBtn.setBackground(bg);
	}

	private JLabel getStarLabel() {
		JLabel l = new JLabel("*");
		l.setFont(f);
		l.setForeground(Color.RED);
		return l;
	}

	private JLabel getEmptyLabel() {
		return new JLabel("");
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		WebLinkLogLoader.getLogger(Odoo.class).debug("Shutting down Odoo...");
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowGainedFocus(WindowEvent e) {
	}

	public void windowLostFocus(WindowEvent e) {
	}

	public void windowStateChanged(WindowEvent e) {
	}
}



