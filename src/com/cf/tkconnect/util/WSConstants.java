package com.cf.tkconnect.util;
/*
 * Author Cyril 
 */

public class WSConstants {

	public static  final int CONTINUE_CODE = 100;
	public static  final String CONTINUE_VAL = "Continue";
	public static final  int OK_CODE = 200;
	public static final  String OK_VAL = "OK";
	public static final  int BAD_DATA_CODE = 300;
	public static final  String BAD_DATA_VAL = "Bad Data";
	public static  final int SERVER_ERROR_CODE = 500;
	public static  final String SERVER_ERROR_VAL = "Server Error.";
	public static  final int INVALID_SERVER_URL_CODE = 406;
	public static  final String INVALID_SERVER_URL_VAL = "Invalid Server Url.";
	public static  final int FORBIDDEN_URL_CODE = 403;
	public static  final String FORBIDDEN_URL_VAL = "Forbidden.";
	public static final  int METHOD_NOT_ALLOWED_CODE = 405;
	public static final  String METHOD_NOT_ALLOWED_VAL = "Method Not Allowed.";
	public static  final int OUT_OF_RESOURCES_CODE = 503;
	public static  final String OUT_OF_RESOURCES_VAL = "Out of Resources.";
	public static  final int NOT_IMPLEMENTED_CODE = 501;
	public static  final String NOT_IMPLEMENTED_VAL = "Not Implemented.";
	public static  final int INVALID_DATA_CODE = 505;
	public static  final String INVALID_DATA_VAL = "Invalid data.";

	public static  final int INVALID_LOGIN_CODE = 506;
	public static  final String INVALID_LOGIN_VAL = "Invalid login.";

	public static  final int SERVER_DIRECTORY_ERROR_CODE = 507;
	public static  final String SERVER_DIRECTORY_ERROR_VAL = "Error in creating directory for business process.";

	// unifier codes 
	public static final  int INVALID_AUTH_CODE = 601;
	public static final  String INVALID_AUTH_VAL = "Invalid Shortname/Authentication Code.";
	public static final  int INVALID_PROJECT_NUMBER_CODE = 601;
	public static final  String INVALID_PROJECT_NUMBER_VAL = "Invalid Project Number.";
	public static final  int INVALID_BP_NAME_CODE = 602;
	public static final  String INVALID_BP_NAME_VAL = "Invalid Business Process Name.";

	public static final  int INCORRECT_BP_NAME_CODE = 603;
	public static final  String INCORRECT_BP_NAME_VAL = "Business Process Name:";
	
	public static final  int NO_BP_PROJECT_CODE = 604;
	public static final  String NO_BP_PROJECT_VAL = "No Budget for the project.";

	public static final  int INVALID_BUDGET_CODE = 612;
	public static final  String INVALID_BUDGET_VAL = "Invalid budget.";

	
	public static  final int INVALID_XML_CODE = 608;
	public static  final String INVALID_XML_VAL = "XML is not valid.";		 	
	// messages
	public static  final int INVALID_PROJECT_NO_CODE = 609;
	public static  final String INVALID_PROJECT_NO_VAL = "Project Number is not correct.";

	public static  final int COST_LINEITEM_SUPPORT_ONLY_CODE = 610;
	public static  final String COST_LINEITEM_SUPPORT_ONLY_VAL = "Only Cost and Line Item BPs are supported.";

 	public static  final int CREATE_BP_TYPES_ERROR_CODE = 611;
 	public static  final String CREATE_BP_TYPES_ERROR_VAL = "Only Cost and Line Item BPs are supported.";
	
	// BP speicfic starts with 650
	public static  final int BP_FAILED_CODE = 650;
	public static  final String BP_FAILED_VAL ="Create BP failed because ";
	public static  final int WBSCODE_ABSENT_CODE = 651;
	public static  final String WBSCODE_ABSENT_VAL = "WBS Code is not present.";
	public static  final int WBSCODE_EXISTS_1_CODE = 652;
	public static  final String WBSCODE_EXISTS_1_VAL = "WBS Code: ";
	public static  final int WBSCODE_EXISTS_2_CODE = 653;
	public static  final String WBSCODE_EXISTS_2_VAL = " already exists.";
	public static  final int WBSCODE_FORMAT_CODE = 654;
	public static  final String WBSCODE_FORMAT_VAL = "WBS Code is not in the correct format.";
	public static  final int WBSCODE_NO_CREATE_CODE = 655;
	public static  final String WBSCODE_NO_CREATE_VAL = "Could not create WBS Item.";
	
	public static  final int EMPTY_OBJECT_NAME_CODE = 656;
	public static  final String EMPTY_OBJECT_NAME_VAL = "Object Name is null or empty.";
	
	public static  final int ID_PRCOCESS_INCOMPLETE_CODE = 1000;
	public static  final int INVALID_ID_CODE = 1001;
// new fixed error codes

	public static  final int INV_RECORDNO_CODE = 657;
	public static  final String INV_RECORDNO_VAL = "Invalid record_no.";

	public static  final int NO_RECORDNO_CODE = 658;
	public static  final String NO_RECORDNO_VAL = "field record_no is not present.";

	public static  final int EMPTY_RECORDNO_CODE = 659;
	public static  final String EMPTY_RECORDNO_VAL = "field record_no is empty.";

	public static  final int DUPLICATE_RECORDNO_CODE = 660;
	public static  final String DUPLICATE_RECORDNO_VAL = "field record_no is duplicated.";

 	public static  final int INTEGRATION_SETTING_NOWORKFLOW_CODE = 661;
 	public static  final String INTEGRATION_SETTING_NOWORKFLOW_VAL = "Integration settings for default workflow link Id not set.";
 	
 	public static  final int INTEGRATION_SETTING_NOWORKFLOW_OR_NOCREATOR_CODE = 662;
 	public static  final String INTEGRATION_SETTING_NOWORKFLOW_OR_NOCREATOR_VAL = "Integration settings for default workflow or creator or BP not set.";
 	
 	
 	public static  final int INVALID_STATUS_FIELD_CODE = 663;
 	public static  final String INVALID_STATUS_FIELD_VAL = "Status field value is not correct.";
 
 	public static  final int INVALID_PROJECT_NAME_FIELD = 664;
 	public static  final String INVALID_PROJECT_NAME_FIELD_VAL = "Project Name cannot can only contain the following characters : alphanumeric, spaces, and ()[],.;:-_&<>' ";

 	public static  final int INVALID_PROJECT_NUMBER_FIELD = 665;
 	public static  final String INVALID_PROJECT_NUMBER_FIELD_VAL = "Project Number cannot can only contain the following characters : alphanumerics and [.-_] characters are allowed ";
 	
 /// filter conditions
 	public static final String EMPTY_STR = "";
 	public static final String P_PNO   = "projectnumber";
 	public static final String P_SDATE_EQ = "startdate_equals";
 	public static final String P_SDATE_RN = "startdate_range";
	public static final String P_TYPE   = "project_type";
	public static final String P_TEMPLATE   = "template";

 	public static final String O_PROJECT   = "project_info";
 	public static final String O_USER = "user_info";
 	public static final String O_DATA = "data_option";
 	public static final String O_BP = "bp_info";
	
	public static final String DATE_FORMAT = "MM-dd-yyyy";
	public static final String TIME_FORMAT = "MM-dd-yyyy hh:mm:ss";
	
	/*
	 * Tags     for ulink input tkconnect
	 */
	
	public static final String INPUTXML = "inputxml";
	public static final String TKCONNECT_TAG = "tkconnect";
 	public static final String SHORTNAME = "_shortname";
 	public static final String AUTHENTICATIONKEY = "_authenticationkey";
 	public static final String PROJECTNUMBER = "_projectnumber";
 	public static final String CLONEPROJECTNUMBER = "_copyfromproject";
 	public static final String BPNAME = "_bpname";
 	public static final String COLUMNNAME = "_columnname";
 	public static final String REPORTNAME = "_reportname";
 	public static final String OBJECTNAME = "_objectname";
	public static final String ISTEMPLATE = "_istemplate";
 	public static final String RECORDNUMBER = "_recordnumber";
 	public static final String PLANNINGITEM = "_planningitem";
 	public static final String SERVICENAME = "_servicename";
 	public static final String LISTWRAPPER = "list_wrapper";
 	public static final String FIELDNAMELIST = "_fieldnamelist";
 	public static final String FIELDNAME = "_fieldname";
 	public static final String FIELDNAMES = "_fieldnames";
 	public static final String FILTERVALUELIST = "_filtervaluelist";
 	public static final String FILTERVALUE = "_filtervalue";
 	public static final String FILTERCONDITION = "_filtercondition";
 	public static final String COPYFROMASSET = "_copyfromasset";
 	public static final String ASSETCLASSNAME = "_assetclassname";
 	public static final String SHEETNAME = "_sheetname";
 	public static final String SCHEDULEOPTIONS = "scheduleoptions";	
	public static final String FILTEROPTIONS = "_filteroptions";
 	
 	public static final String tkconnectNAME = "tkconnectname";
 	public static final String CMCODE = "_cmcode";
 	public static final String CLASSNAME = "_classname";
 	public static final String COPYFROMRECORD = "_copyfromrecord";
 	public static final String CREATESHELL = "_createshell";
 	public static final String UPDATESHELL = "_updateshell";
 	public static final String GETSHELLLIST = "_getshelllist";
 	public static final String SHELLTYPE = "_shelltype";
 	public static final String SHELLNUMBER = "_shellnumber";
 	public static final String COPYFROMSHELLTEMPLATE = "_copyfromshelltemplate";
	public static final String SPACETYPE = "_spacetype";	
	public static final String COPY_FROM_USER_PREFERENCE_TEMPLATE = "_copyfromuserpreferencetemplate";
	public static final String CREATEUPDATEROLE = "_createupdaterole";
 	public static final String CREATEUPDATERESOURCE = "_createupdateresource";
 	public static final String GETROLELIST = "_getrolelist";
 	public static final String GETRESCOURCELIST = "_getresourcelist";
 	
 	
 	public static final String FILELIST = "file_name";
 	public static final String ISZIPFILE = "_iszipsfile";
 	public static final String ZIPFILE = "_zipfile";
 	public static final int MIN_INTERVAL = 10000; 
 	public static final int MAX_THREADS = 10;
 	public static final int MIN_CALLBACK_INTERVAL = 7200000;// 2 hours 
 	public static final int MAX_LINEITEMS = 1000;// 
 	
 	public static final String JOBCOUNT_PROPERTY_tkconnectNAME = "/tkconnect_jc.properties";
 	public static final String JOBCOUNT_PROPERTY = "tkconnect.jobcount";
 	public static final String DOCUMENT_SERVICES="UnifierWebServices";
 	public static final String MAIN_SERVICES="mainservice";
 	
 	public static final String TK_LINK="tkconnect";
 
 	
 	
 	/*
 	 *  tkconnect Releated to property
 	 */
 	public static final String PROPERTY_tkconnectNAME = "tkconnect.properties";
 	public static final String PROPERTY_SCAN_INTERVAL = "tkconnect.directory.scan.interval";
 	public static final String UNIFIER_SYNC_INTERVAL = "tkconnect.unifier.sync.days";
 	public static final String PROPERTY_BASE_DIRECTORY = "tkconnect.directory.base"; 	
 	public static final String PROPERTY_SERVICE_NAME = "tkconnect.servicename";

	public static final String COMPANY_NAME = "tkconnect.company.name";
	public static final String COMPANY_SHORTNAME = "tkconnect.odoo.username";
 	public static final String COMPANY_AUTHCODE = "tkconnect.odoo.paswword";
 	public static final String COMPANY_URL = "tkconnect.odoo.url";
 	public static final String PROPERTY_PROXYHOST = "tkconnect.proxyhost";
 	public static final String PROPERTY_PROXYPORT = "tkconnect.proxyport";
 	public static final String PROPERTY_ISPROXYSET = "tkconnect.proxy.set";
 	public static final String PROPERTY_CALLBACK_RESPONSE = "tkconnect.callback.response";
 	public static final String PROPERTY_DIRECTORY_SERVICE = "tkconnect.directory.service";
 	public static final String PROPERTY_MAX_THREAD_COUNT = "tkconnect.max.thread.count";
 	public static final String PROPERTY_CALLBACK_THREAD_TIME = "tkconnect.callback.thread.time";
 	public static final String SEND_TK_EMAIL = "tkconnect.send.email";
 	public static final String SEND_EMAIL_ONCOMPLETE = "tkconnect.send.mail.oncomplete";
 	public static final String SEND_EMAIL_ONERROR = "tkconnect.send.mail.onerror";
 	public static final String CRON_JOB_SCHEDULER = "tkconnect.jobs.scheduler";
	
 	public static final String PROPERTY_USE_FTP = "tkconnect.use.ftp";
 	public static final String PROPERTY_FTP_HOST = "tkconnect.ftp.host";
 	public static final String PROPERTY_FTP_USER = "tkconnect.ftp.user";
 	public static final String PROPERTY_FTP_PASSWORD = "tkconnect.ftp.password";
 	public static final String PROPERTY_FTP_DIRECTORY = "tkconnect.ftp.directory";
 	public static final String PROPERTY_SEND_FTP_RESPONSE = "tkconnect.send.ftp.response";
 	public static final String PROPERTY_FTP_RESPONSE_DIRECTORY = "tkconnect.ftp.response.directory";
 	public static final String PROPERTY_NO_CALLBACK = "tkconnect.callback.strict";
 	public static final String TK_SUPPORT_EMAIL = "cyrilaf@gmail.com";
 	public static final String INTEG_CF_EMAIL = "cyrilaf@gmail.com";
 	public static final String SEND_EMAIL_ONSUCCESS = "tkconnect.send.mail.onsuccess";

	public static final String PROPERTY_POLL_MODE = "tkconnect.poll.mode";
	public static final String PROPERTY_SOCKET_PORT = "tkconnect.socket.port";
 	public static final String PROPERTY_WRITE_ONSUCCESS = "tkconnect.write.onsuccess";
 	public static final String PROPERTY_WRITE_ONERROR = "tkconnect.write.onerror";
 	public static final String PROPERTY_WRITE_RESPONSE = "tkconnect.write.response";
	public static final String PROPERTY_RUNNING_MODE = "tkconnect.running.mode"; // values: 0 - both , 1- standared mode, 2- extrernal mode
	public static final String PROPERTY_ATTACH_ZIP = "tkconnect.attachzip";
	public static final String PROPERTY_FILE_ROOT_PATH = "tkconnect.root.path";
	public static final String PROPERTY_WEB_FILE_ROOT_PATH = "tkconnect.directory";
	
	
 	/*
 	 * Related to tkconnect system
 	 */
 	public static final String FILE_SERVICE_DIRECTORY = "tkconnect-service";
 	public static final String REQUEST_FILE_SERVICE_DIRECTORY = "input";
 	public static final String RESPONSE_FILE_SERVICE_DIRECTORY = "output";
 	public static final String CALLBACK_FILE_SERVICE_DIRECTORY = "callback";
 	public static final String SUCCESS_FILE_SERVICE_DIRECTORY = "success";
 	public static final String EXCEL_FILE_SERVICE_DIRECTORY = "excel";
 	public static final String TEMP_FILE_SERVICE_DIRECTORY = "temp";
 	public static final String LOG_FILE_DIRECTORY = "log";
 	public static final String ERROR_FILE_DIRECTORY = "error";
 	public static final String ATTACHMENTS_FILE_DIRECTORY = "attachments";
 	public static final String BASE_FILE_DIRECTORY = "file-system";
 	
 	
 	/*
 	 * Service name constants
 	 */
 	
 	public static final String CREATE_OBJECT = "createObject";
 	public static final String UPDATE_OBJECT = "updateObject";
 	public static final String GET_OBJECT_LIST = "getObjectList";
 	public static final String GET_BPLIST = "getBPList";
 	public static final String CREATE_BPRECORD = "createBPRecord";
 	public static final String CREATE_COMPLETE_BPRECORD = "createCompleteBPRecord";
 	public static final String ADD_BPLINEITEM = "addBPLineItem";
 	public static final String ADD_COMPLETE_BPLINEITEM = "addCompleteBPLineItem";
 	public static final String GET_BPRECORD = "getBPRecord";
 	public static final String GET_PLANNINGITEM = "getPlanningItem";
 	public static final String GET_COMPLETE_BPRECORD = "getCompleteBPRecord";
 	public static final String CREATE_WBS = "createWBS";
	public static final String GET_SOV = "getSOV";
 	public static final String CREATE_PROJECT = "createProject";
 	public static final String UPDATE_BPRECORD = "updateBPRecord";
	public static final String UPDATE_BPRECORDV2 = "updateBPRecordV2";
	public static final String UPDATE_BPRECORDV2_OPTIONS = "_options";
 	public static final String UPDATE_COMPLETE_BPRECORD = "updateCompleteBPRecord";
 	public static final String GET_WBSSTRUCTURE = "getWBSStructure";
 	public static final String GET_ID_RESPONSE = "getIdResponse";
 	public static final String GET_PENDING_RESPONSE = "getPendingResponse";
 	public static final String GET_COLUMN_DATA = "getColumnData";
 	public static final String UPDATE_COLUMN_DATA = "updateColumnData";
 	public static final String GET_UDR_DATA = "getUDRData";
 	public static final String CREATE_ASSET = "createAsset";
 	public static final String CREATE_ACTIVITIES = "createScheduleActivities";
 	public static final String UPDATE_ACTIVITIES = "updateScheduleActivities";
 	public static final String CREATE_ACTIVITIESV2 = "createScheduleActivitiesV2";
 	public static final String UPDATE_ACTIVITIESV2 = "updateScheduleActivitiesV2";
 	public static final String CREATE_FILE_ACTIVITIESV2 = "createScheduleActivitiesFromFileV2";
 	public static final String UPDATE_FILE_ACTIVITIESV2 = "updateScheduleActivitiesFromFileV2";
 	public static final String GET_ACTIVITIES = "getScheduleActivities";
 	public static final String CREATE_CONFIG_MODULE = "createConfigurableModuleRecord";
 	public static final String UPDATE_CONFIG_MODULE = "updateConfigurableModuleRecord";
 	public static final String CREATE_SHELL = "createShell";
 	public static final String UPDATE_SHELL = "updateShell";
 	public static final String GET_SHELL_LIST = "getShellList";
 	public static final String GET_EXCHANGE_RATES = "getExchangeRates";
 	public static final String UPDATE_EXCHANGE_RATES = "updateExchangeRates";
	public static final String GET_SPACE_LIST = "getSpaceList";
	public static final String GET_LEVEL_LIST = "getLevelList";
	public static final String CREATE_LEVEL = "createLevel";
	public static final String UPDATE_LEVEL = "updateLevel";
	public static final String CREATE_SPACE = "createSpace";
	public static final String UPDATE_SPACE = "updateSpace";
 	public static final String CREATE_USER = "createUser";
 	public static final String UPDATE_USER = "updateUser";
 	public static final String GET_USER_LIST = "getUserList";
 	public static final String CREATE_UPDATE_ROLE = "createUpdateRole";
	public static final String CREATE_UPDATE_RESOURCE = "createUpdateResource";
	public static final String GET_ROLE_LIST = "getRoleList";
	public static final String GET_RESOURCE_LIST = "getResourceList";
	public static final String UPDATE_USERGROUPMEMBERSHIP = "updateUserGroupMembership";
	public static final String UPDATE_USER_SHELL_MEMBERSHIP = "updateUserShellMembership";
 	
 	/*
 	 * tkconnect name constants
 	 */
 	public static String appHome =""; 
 	public static final String OUTPUT_FILENAME = "_response.xml";
 	public static final String ATTACH_FILENAME = "_attachment.zip";

 	

	//ulink running mode constants
	public static final int TKLINK_BOTH 		= 0;
	public static final int TKLINK_STANDARD	= 1;
	public static final int TKLINK_EXTERNAL	= 2;
	
	public static final String LINE_ITEM_TAG = "<_bp_lineitem";
 	/*
 	 * LOG4J constant properties
 	 */
 	
 	public static final String[] MAINFILEPROP = {"log4j.appender.daily.tkconnect","log4j.appender.logtkconnect.tkconnect"};
 	public static final String[] JOBFILEPROP = {"log4j.appender.job.tkconnect","log4j.appender.tkconnectjoberror.tkconnect"};
 	public static final String[] JOBFILEPROPVAL = {"tkconnectjob.log", "tkconnectjoberror.log"};
 	public static final String[] MAINLOG = {"tkconnect.log","tkconnect.log"};
 	
	/*
	 * FOR response/error/success sub-directories
	 */
 	
 	public static final String[] MONTHS = {
 		"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"
 	};
 	
 	public static final String NS_URI ="http://www.timekarma.com";
 	
 	/* Time interval in milliseconds to be specified in the Cleanup thread */
 	

 	/* Time interval in milliseconds to be specified as delay when invoking Cleanup task */
 	public static final long CLEAN_UP_DELAY = 60000; // 1 min = 60000 ms
 	
 	/* Allowed compressed tkconnect name extensions in attachments */
 	public static final String[] ALLOWED_ZIPS = new String[] { "zip", "rar", "7zip", "7z", "bz", "bz2", "bzip2", "gz", "gzip" };
}
