package networking;

import com.odedtech.mff.mffapp.BuildConfig;

/**
 * Created by gufran khan on 22-06-2018.
 */

public class WebServiceURLs {

    public static String BASE_URL = BuildConfig.BASE_URL;
    public static String LOGIN_URL = BASE_URL + ":5052/login";
    public static String LOGIN_URL_USERNAME_PASSWORD = BASE_URL + ":5055/LoginService/login";
    public static String PHONENUMBER_OTP = BASE_URL + ":5052/verify";
    public static String PORT_URL = BASE_URL + ":7181/mff/api/oauthDetails?access_token=";
    public static String ALL_DYNAMIC_FORMS_URL = "/ProfileService/profiles/forms/mobileApp?access_token=";
    public static String ALL_DYNAMIC_FORM_BODY_URL = "/ProfileService/profiles/forms/FORM_ID?access_token=";
    public static String SAVE_KYC_DATA_URL = "/ProfileService/profiles?access_token=";
    public static String UPDATE_KYC_DATA_URL = "/ProfileService/profiles/PROFILE_ID?access_token=";
    public static String ALL_PROFILES_URL = "/ProfileService/profiles?pageNumber=PAGE_NUMBER&numberOfRecords=NUMBER_OF_RECORDS&access_token=";
    public static String SEARCH_PROFILES_URL = "/ProfileService/searchProfiles?name=NAME&access_token=";
    public static String GET_LINKED_PORTFOLIO_URL = "/PortfolioService/getLinkedportfolio?access_token=";
    public static String GET_EVENTS_BY_CONTRACTUUID_URL = "/PortfolioService/geteventsBycontractUUID?access_token=";
    public static String PROFILE_LINK_STATUS_URL = "/WorkflowService/mobileApp/PROFILE_ID?access_token=";
    public static String CREDIT_BUREAU_POST_URL = "/WorkflowService/workflowTemplateDetails?access_token=";
    public static String CASH_FLOW_POST_URL = "/WorkflowService/workflowTemplateDetails?access_token=";
    public static String CASH_FLOW_UPDATE_URL = "/WorkflowService/workflowTemplateDetails/WORKFLOW_PROFILE_ID?access_token=";
    public static String CASH_FLOW_GET_INFO_URL = "/WorkflowService/mobileApp/workflowProfileID/WORKFLOW_PROFILE_ID/templateID/TEMPLATE_ID?access_token=";
    public static String CREDIT_BUREAU_GET_INFO_URL = "/WorkflowService/mobileApp/workflowProfileID/WORKFLOW_PROFILE_ID/templateID/TEMPLATE_ID?access_token=";
    public static String SAVE_CONTRACT_DATA = "/PortfolioService//savePortfolioRepayment?access_token=";
    public static String UPLOAD_PROFILE = "/ProfileService/upload/files/WORKFLOW_PROFILE_ID/profilePicture?access_token=";
    public static String UPLOAD_BUSINESS = "/ProfileService/upload/files/WORKFLOW_PROFILE_ID/business?access_token=";
    public static String UPLOAD_PERSONAL = "/ProfileService/upload/files/WORKFLOW_PROFILE_ID/personal?access_token=";
    public static String VERIFY_WORKFLOW = "/WorkflowService/workflowTemplateDetails/verify?access_token=";
    public static String VERIFY_BRANCHES = "/BranchService/branches/treeLevel?access_token=";
    public static String SAVE_CONTRACT_DATA_NEW = "/PortfolioService/savePortfolioRepayment?access_token=";
    public static String HIERARCHY_SEARCH = "/PortfolioService/hierarchySearch?access_token=";


    // Client
    public static String CLIENT_LOGIN_URL = BASE_URL + ":5059/client/login";
    public static String CLIENT_USER_CREATION_URL = BASE_URL + ":7190/mff/api/users";
    public static String CLIENT_VERIFY_PHONE = BASE_URL + ":5059/client/verify";

}