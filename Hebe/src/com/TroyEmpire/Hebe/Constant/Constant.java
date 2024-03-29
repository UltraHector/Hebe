package com.TroyEmpire.Hebe.Constant;

import com.TroyEmpire.Hebe.Activities.R;


/**
 * The constant used in the Hebe Project
 */
public class Constant {

	// Hebe data storage root path
	public static final String HEBE_STORAGE_ROOT = "/Hebe";
	// Urls for logging into JWC
	public static final String JWC_VALIDATION_CODE_URL = "http://jwc.jnu.edu.cn/web/ValidateCode.aspx";
	public static final String JWC_MAIN_URL = "http://jwc.jnu.edu.cn";
	public static final String JWC_LOGIN_WINDOW_URL = "http://jwc.jnu.edu.cn/web/login.aspx";
	public static final String JWC_RECEIVE_SCHEDULE_PARAMETERS_URL = "http://jwc.jnu.edu.cn/web/Secure/PaiKeXuanKe/wfrm_xk_StudentKcb.aspx";
	public static final String JWC_SCHEDULE_URL_INCLUDING_EXPORT_OPTIONS = "http://jwc.jnu.edu.cn/Web/Secure/TeachingPlan/wfrm_Prt_Report.aspx";
	public static final String JWC_RECEIVE_EXAM_SCORE_PARAMETERS_URL = "http://jwc.jnu.edu.cn/Web/Secure/Cjgl/Cjgl_Cjcx_XsCxXqCj.aspx";
	// Parameter names for post data to JWC， which are from the HTML code of JWC
	public static final String VIEWSTATE = "__VIEWSTATE";
	public static final String EVENTVALIDATION = "__EVENTVALIDATION";
	public static final String LOGIN = "btnLogin";
	// varification code is called CAPTCHA by the computer experts
	public static final String CAPTCHA = "txtFJM";
	public static final String CAPTCHA_ID = "lblFJM";
	public static final String LOGIN_WINDOW_TAG_USER_ACCOUNT_NUMBER = "txtYHBS";
	public static final String LOGIN_WINDOW_TAG_PASSWORD = "txtYHMM";

	public static final String LOGIN_SUCCESS_RESPONSE_TITILE = "整体架构";
	
	// temp storage for Hebe
	 public static final String HEBE_TEMP = "/Hebe/Temp";

	// Parameters for export the schedule, which are from the HTML code of JWC'
	// child page
	public static final String SCHEDULE_VIEWSTATE = "__VIEWSTATE";
	public static final String SCHEDULE_EVENTVALIDATION = "__EVENTVALIDATION";
	public static final String SCHEDULE_LASTFOCUS = "__LASTFOCUS";
	public static final String SCHEDULE_EVENTARGUMENT = "__EVENTARGUMENT";
	public static final String SCHEDULE_EVENTTARGET = "__EVENTTARGET";

	public static final String SCHEDULE_SEMESTER = "dlstNdxq";
	public static final String SCHEDULE_YEAR = "dlstXndZ";
	public static final String SCHEDULE_EXPORT_COURSE = "btnExpKcb";
	public static final String SCHEDULE_EXPORT_EXAM = "btnExpKsb";
	// Parameter names for getting the schedule page
	public static final String SCHEDULE_EXPORT_INTERNAL_FRAME_ID = "ReportFrameReportViewer1";
	public static final String SCHEDULE_EXPORT_INTERNAL_FRAME_ATTR = "src";
	
	// 查询成绩参数
	public static final String SCHEDULE_EVENTTARGET_VALUE = "lbtnQuery";
	public static final String SCHEDULE_EXAM_SCORE_XH = "txtXH";
	public static final String SCHEULE_EXAM_SCORE_XM = "txtXM";
	public static final String SCHEDULE_EXAM_SCORE_YXZY = "txtYXZY";
	public static final String SCHEDULE_EXAM_SCORE_XN = "dlstXndZ";
	public static final String SCHEDULE_EXAM_SCORE_XQ = "ddListXQ";
	
	// 信息平台参数
	public static final int NEWS_NUMBER_ONE_TIME_UPDATE__LIMIT = 15;
	public static final int NEWS_CACHE_LIMIT = 60;
	
	
	// Store the users' data for Hebe project
	public static final String SCHEDULE_RELATIVE_DIRECTORY = "/Hebe/Schedule";
	public static final String COURSE_SCHEDULE_NAME = "CourseSchedule.html";
	public static final String EXAM_SCHEDULE_NAME = "ExamSchedule.html";
	// Urls for Library
	public static final String LibraryUrl = "http://202.116.13.3:8080/sms/opac/search/showSearch.action?xc=5";

	// SharedPreferences file names
	public static final String USER_JWC_INFO = "com.TroyEmpire.Hebe.userJwcInfo";
	public static final String SHARED_PREFERENCE_SCHEDULE = "com.TroyEmpire.Hebe.formattedSchedule";
	public static final String SHARED_PREFERENCE_MAP_HISTORY_FILE = "com.TroyEmpire.Hebe.map";
	public static final String SHARED_PREFERENCE_MAP_HSITORY_KEY = "SHARED_PREFERENC_MAP_HISTORY_EKY";
	public static final String SHARED_PREFERENCE_MAP_CAMPUS_ID_FILE = "SHARED_PREFERENCE_MAP_CAMPUS_ID_FILE";
	public static final String SHARED_PREFERENCE_MAP_CMAPUS_ID_KEY = "SHARED_PREFERENCE_MAP_CMAPUS_ID_KEY";
	
	// SharedPreferences key-value paris
	public static final String USER_JWC_ACCOUNT_NUMBER = "accountNumber";
	public static final String USER_JWC_PASSWORD = "password";
	
	//地图常量
	public static final String MAP_LIST_HISTORY_SIZE = "10";
	public static final String MAP_LIST_FREQUENT_PLACE_SIZE = "10";
	public static final String MAP_LIST_SUGGESTION_SIZE = "100";

	public static final String NEED_UPDATE_JWC_USER_INFO = "需要更新教务处登陆信息";
	public static final String NEED_INPUT_JWC_USER_INFO = "请输入教务处登陆信息";
	public static final String FIRST_SEMESTER = "第一学期";
	public static final String SECOND_SEMESTER = "第二学期";
	public static final String WRONG_PASSWORD = "密码错误";
	public static final String NETWORK_ERROR = "网络连接失败";
	public static final String INITIATE_DATA_FAILED = "下载数据失败";
	public static final String INITIATE_DATA_SUCCEED = "更新数据成功";
	
	//地图标题
	public static final String XIAOYUANDT_PATH_TITLE = "路径";
	public static final String XIAOYUANDT_SEARCH_TITLE = "搜索";
	
	//数据库常量
	public static final String HEBE_DB_PATH = "/data/data/com.TroyEmpire.Hebe/databases";

	//餐馆数据包路径
	public static final String RESTAURANT_DATA_PATH = "/Hebe/Restaurant";
	public static final String RESTAURANT_DB_FILE_FOLDER = "RestaurantDB";
	public static final String RESTAURANT_LOGO_FOLDER= "RestaurantLogo";
	public static final String RESTAURANT_DB_FILE_NAME= "Restaurant.db";
	//地图数据包路径
	public static final String MAP_DATA_PATH = "/Hebe/Map";
	public static final String MAP_DB_FILE_FOLDER = "MapDB";
	public static final String MAP_IMAGE_FOLDER = "MapImage";
	public static final String MAP_DB_FILE_NAME = "Map.db";
	
	// Hebe客户端内置数据库 under the assets folder，用于用户第一次使用不能联网的情况
	public static final String HEBE_CLIENT_DB_DATA = "HebeDBData"; 
	
	// 下载数据包Url，更换服务器必须更换Url地址
	public static final String HEBE_SERVER_URL = "http://202.116.3.39:8080/HebeServer";
	// check connection to server status constants
	public static final String HEBE_SERVER_CONNECTION_CHECK_SUB_URL = "/whetherConnectionBlock";
	public static final String HEBE_SERVER_CONNECTION_STATUS = "Status";
	
	//  教务处查看成绩翻译成绩Html的常量
    public static final String JWC_GET_EXAM_SCORE_TABLE_ID = "dgrdList";
    
    public static final String NUMBER_OF_EXAM_SCORES = "numberOfExamScores";
    
    public static final String HEBE_MANUAL_HTML_PATH = "file:///android_asset/HebeCommons/HebeManual.html";
	
	//主界面的gridview资源，功能名字以及logo,顺序排好
	public static final String FUNCTION_NAMES[] = new String[]{
			"信息平台","校园地图","i课程","外卖系统","Hebe手册","图书馆"};
	public static final int FUNCTION_LOGOS[] = {
			R.drawable.xinxipt_logo,R.drawable.xiaoyuandt_logo,R.drawable.icourse_logo,
			R.drawable.waimaixt_logo,R.drawable.jnu_ship_launcher,R.drawable.library_logo};
	
	
	// the ratio of the latitude 

	public static final double BASE_POINT_LATITUDE = 467561.3588375351637536439664404;
	public static final double BASE_POINT_LONGITUDE = 2290762.0787466874287743866966239;
	public static final double GPS_RATIO = 20211.08543859088914996;

}