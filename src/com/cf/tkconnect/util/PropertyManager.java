/**
 * PropertyManager.java
 */
      
package com.cf.tkconnect.util;

import java.util.*;
import java.io.*;
import com.cf.tkconnect.log.Log;
import com.cf.tkconnect.log.LogSource;


import com.cf.tkconnect.util.PropertyManager;

/**
 * Manages properties for the entire Jive system. Properties are merely
 * pieces of information that need to be saved in between server restarts.
 * <p>
 * At the moment, properties are stored in a Java Properties file. In a version
 * of Jave coming soon, the properties file format will move to XML. XML
 * properties will allow hierarchical property structures which may mean the
 * API of this class will have to change.
 * <p>
 * Jave properties are only meant to be set and retrevied by core Jave classes.
 * Therefore, skin writers should probably ignore this class.
 * <p>
 * This class is implemented as a singleton since many classloaders seem to
 * take issue with doing classpath resource loading from a static context.
 */
public class PropertyManager {

	static Log logger = LogSource.getInstance(PropertyManager.class);
    private static PropertyManager manager = null;
    private static Object managerLock = new Object();
    //private static String propsName = "./tkconnect_user.properties";
    private static PropertyManager system_manager = null;
    private static String systemproperties = "tkconnect_system.properties";
    private static PropertyManager custom_manager = null;
    private static String customproperties = "tkconnect_custom.properties";

    /**
     * Returns a Messenger property
     *
     * @param name the name of the property to return.
     * @returns the property value specified by name.
     */
	public static synchronized void setPropsName(String name) { 
		;//propsName = name;
	} 
	
	public static synchronized void setCustomPropsName(String name) { 
		customproperties = name;
	}
	
	
	public static String getCustomProperty(String name) {
        // always get the properties fresh dynamically
        synchronized(managerLock) {
        	custom_manager = new PropertyManager(customproperties);
        }
        return custom_manager.getCustomProp(name);
    }
	
	public static String getCustomProperty(String name, String defval) {
		if (custom_manager == null) {
            synchronized(managerLock) {
                if (custom_manager == null) {
                	custom_manager = new PropertyManager(customproperties);
                }
            }
        }
        return custom_manager.getCustomProp(name,defval);
    }
	public static synchronized void setSysPropsName(String name) { 
		systemproperties = name;
	}
	
	
	public static String getSysProperty(String name) {
        // always get the properties fresh dynamically
        synchronized(managerLock) {
        	system_manager = new PropertyManager(systemproperties);
        }
        return system_manager.getSysProp(name);
    }
	
	public static String getSysProperty(String name, String defval) {
		if (system_manager == null) {
            synchronized(managerLock) {
                if (system_manager == null) {
                	system_manager = new PropertyManager(systemproperties);
                }
            }
        }
        return system_manager.getSysProp(name,defval);
    }
    public static String getProperty(String name) {
	
        // always get the properties fresh dynamically
        synchronized(managerLock) {
                manager = new PropertyManager(systemproperties);
        }
        return manager.getProp(name);
    }

    public static String getProperty(String name, String defval) {
        if (system_manager == null) {
            synchronized(managerLock) {
                if (system_manager == null) {
                	system_manager = new PropertyManager(systemproperties);
                }
            }
        }
        return system_manager.getSysProp(name,defval);
    }

    public static Enumeration getPropertyNames() {
        if (manager == null) {
            synchronized(managerLock) {
                if (manager == null) {
                    manager = new PropertyManager(systemproperties);
                }
            }
        }
        return manager.getPropNames();
    }

    /**
     * Returns true if the properties are readable. This method is mainly
     * valuable at setup time to ensure that the properties file is setup
     * correctly.
     */
    public static boolean propertyFileIsReadable() {
        if (manager == null) {
            synchronized(managerLock) {
                if (manager == null) {
                    manager = new PropertyManager(systemproperties);
                }
            }
        }
        return manager.propFileIsReadable();
    }

    /**
     * Returns true if the properties are writable. This method is mainly
     * valuable at setup time to ensure that the properties file is setup
     * correctly.
     */
    public static boolean propertyFileIsWritable() {
        if (manager == null) {
            synchronized(managerLock) {
                if (manager == null) {
                    manager = new PropertyManager(systemproperties);
                }
            }
        }
        return manager.propFileIsWritable();
    }

    /**
     * Returns true if the Messenger.properties file exists where the path property
     * purports that it does.
     */
    public static boolean propertyFileExists() {
        if (manager == null) {
            synchronized(managerLock) {
                if (manager == null) {
                    manager = new PropertyManager(systemproperties);
                }
            }
        }
        return manager.propFileExists();
    }

    private Properties properties = null;
    private Properties sysproperties = null;
    private Properties custproperties = null;
    private Object propertiesLock = new Object();
    private String resourceURI="tkconnect_system.properties";

    /**
     * Singleton access only.
     */
    private PropertyManager(String resourceURI) {
        this.resourceURI = resourceURI;
    }
   
    /**
     * Gets a Messenger property. Messenger properties are stored in Messenger.properties.
     * The properties file should be accesible from the classpath. Additionally,
     * it should have a path field that gives the full path to where the
     * file is located. Getting properties is a fast operation.
     */
    public String getProp(String name) {
        //If properties aren't loaded yet. We also need to make this thread
        //safe, so synchronize...
        
            synchronized(propertiesLock) {
                //Need an additional check
                if (sysproperties == null) {
                    loadProps();
                }
            }
        
        return sysproperties.getProperty(name);
    }

    public String getProp(String name, String defval) {
        //If properties aren't loaded yet. We also need to make this thread
        //safe, so synchronize...
        
            synchronized(propertiesLock) {
                //Need an additional check
                if (properties == null) {
                    loadProps();
                }
            }
        
        return properties.getProperty(name, defval);
    }

    public String getSysProp(String name, String defval) {
        //If properties aren't loaded yet. We also need to make this thread
        //safe, so synchronize...
        
            synchronized(propertiesLock) {
                //Need an additional check
                if (sysproperties == null) {
                    loadSysProps();
                }
            }
        
        return sysproperties.getProperty(name, defval);
    }

    public String getCustomProp(String name, String defval) {
        //If properties aren't loaded yet. We also need to make this thread
        //safe, so synchronize...
        
            synchronized(propertiesLock) {
                //Need an additional check
                if (custproperties == null) {
                    loadCustomProps();
                }
            }
        
        return custproperties.getProperty(name, defval);
    }
    public String getSysProp(String name) {
        //If properties aren't loaded yet. We also need to make this thread
        //safe, so synchronize...
        
            synchronized(propertiesLock) {
                //Need an additional check
                if (sysproperties == null) {
                    loadSysProps();
                }
            }
        
        return sysproperties.getProperty(name);
    }

    public String getCustomProp(String name) {
        //If properties aren't loaded yet. We also need to make this thread
        //safe, so synchronize...
        
            synchronized(propertiesLock) {
                //Need an additional check
                if (customproperties == null) {
                    loadCustomProps();
                }
            }
        
        return sysproperties.getProperty(name);
    }
    public Enumeration getPropNames() {
        //If properties aren't loaded yet. We also need to make this thread
        //safe, so synchronize...
        if (properties == null) {
            synchronized(propertiesLock) {
                //Need an additional check
                if (properties == null) {
                    loadProps();
                }
            }
        }
        return properties.propertyNames();
    }

    /**
     * Loads Messenger properties from the disk.
     */
    private void loadProps() {
    	sysproperties = new Properties();
        InputStream in = null;
		
        try {
        	File f = new File(resourceURI);
        	logger.debug("check file :"+f.exists()+" :"+f.getAbsolutePath());
        	// in = ClassLoader.getSystemResourceAsStream(resourceURI);
        	in = new FileInputStream(f);
        	// in = getClass().getClassLoader().getResourceAsStream(resourceURI);
			//System.out.println(" ****** "+in);
        	sysproperties.load(in);
        }
        catch (IOException ioe) {
            logger.error("Error reading Messenger properties in PropertyManager " + ioe);
           
        }
        finally {
            try {
                in.close();
            } catch (Exception e) { }
        }
    }
    private void loadSysProps() {
        sysproperties = new Properties();
        InputStream in = null;
		
        try {
        	File f = new File(resourceURI);
        	System.out.println("check file :"+f.exists()+" :: "+f.getAbsolutePath());
        	// in = ClassLoader.getSystemResourceAsStream(resourceURI);
           in = new FileInputStream(f);
        	// in = getClass().getClassLoader().getResourceAsStream(resourceURI);
			//System.out.println(" ****** "+in);
           sysproperties.load(in);
        }
        catch (IOException ioe) {
            logger.error("Error reading Messenger sys properties in PropertyManager " + ioe);
           
        }
        finally {
            try {
                in.close();
            } catch (Exception e) { }
        }
    }
    private void loadCustomProps() {
    	custproperties = new Properties();
        InputStream in = null;
		
        try {
        	File f = new File(resourceURI);
        	logger.debug("check custom file :"+f.exists());
        	// in = ClassLoader.getSystemResourceAsStream(resourceURI);
           in = getClass().getClassLoader().getResourceAsStream("/tkconnect_custom.properties");
           
        	// in = getClass().getClassLoader().getResourceAsStream(resourceURI);
			//System.out.println(" ****** "+in);
           custproperties.load(in);
        }
        catch (IOException ioe) {
            logger.error("Error reading Messenger properties in PropertyManager " + ioe);
           
        }
        finally {
            try {
                in.close();
            } catch (Exception e) { }
        }
    }
    /**
     * Returns true if the properties are readable. This method is mainly
     * valuable at setup time to ensure that the properties file is setup
     * correctly.
     */
    public boolean propFileIsReadable() {
        try {
            InputStream in = getClass().getResourceAsStream(resourceURI);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
 
    /**
     * Returns true if the messenger.properties file exists where the path property
     * purports that it does.
     */
    public boolean propFileExists() {
        String path = getProp("path");
		File file = new File(path);
        if (file.isFile()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns true if the properties are writable. This method is mainly
     * valuable at setup time to ensure that the properties file is setup
     * correctly.
     */
    public boolean propFileIsWritable() {
        String path = getProp("path");
		File file = new File(path);
		if (file.isFile()) {
			//See if we can write to the file
			if (file.canWrite()) {
                return true;
            }
			else {
                return false;
            }
        }
        else {
            return false;
        }
    }
   
    public static void saveUserProperties(String path) {
        try {
            Properties props = new Properties();
            props.setProperty("tkconnect.company.name",  InitialSetUp.odoo.get("company_name"));
            props.setProperty("tkconnect.company.shortname", InitialSetUp.odoo.get("company_shortname"));
            props.setProperty("tkconnect.company.authcode", InitialSetUp.odoo.get("company_authcode"));
            props.setProperty("tkconnect.company.url",InitialSetUp.odoo.get("company_url"));
            
            props.setProperty("tkconnect.directory",  InitialSetUp.basefilepath);
            props.setProperty("tkconnect.directory.base", InitialSetUp.base_directory);
            props.setProperty("tkconnect.directory.service",  InitialSetUp.directoryService);
            
            props.setProperty("tkconnect.proxyport",PropertyManager.getProperty("tkconnect.proxyport"));
            props.setProperty("tkconnect.proxyhost", PropertyManager.getProperty("tkconnect.proxyhost"));
            if(logger.isDebugEnabled())
            	logger.debug("User properties have been written to :"+path+"/tkconnect_user.properties");
            File f = new File(path+"/tkconnect_user.properties");
            OutputStream out = new FileOutputStream( f );
            props.store(out, "#User properties");
        }
        catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
