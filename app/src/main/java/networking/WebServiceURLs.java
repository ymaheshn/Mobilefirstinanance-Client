package networking;

import com.odedtech.mff.mffapp.BuildConfig;

/**
 * Created by gufran khan on 22-06-2018.
 */

public class WebServiceURLs {

    public static String BASE_URL = BuildConfig.BASE_URL;
    public static String LOGIN_URL = BASE_URL + ":5052/login";
    public static String PHONENUMBER_OTP = BASE_URL + ":5052/verify";
    public static String PORT_URL = BASE_URL + ":7181/mff/api/oauthDetails?access_token=";
    public static String ALL_DYNAMIC_FORMS_URL = "/mff/api/profiles/forms?access_token=";
    public static String SAVE_KYC_DATA_URL = "/mff/api/profiles?access_token=";
    public static String UPDATE_KYC_DATA_URL = "/mff/api/profiles/PROFILE_ID?access_token=";
    public static String ALL_PROFILES_URL = "/mff/api/profiles?pageNumber=PAGE_NUMBER&numberOfRecords=NUMBER_OF_RECORDS&access_token=";
    public static String SEARCH_PROFILES_URL = "/mff/api/searchProfiles?name=NAME&access_token=";
    public static String GET_LINKED_PORTFOLIO_URL = "/mff/api/getLinkedportfolio?access_token=";
    public static String GET_LINKED_PORTFOLIO_PROFILE_URL = "/mff/api/getAllLinkedusertoportfolio/PROFILE_ID?access_token=";
    public static String PROFILE_LINK_STATUS_URL = "/mff/api/mobileApp/PROFILE_ID?access_token=";
    public static String CREDIT_BUREAU_POST_URL = "/mff/api/workflowTemplateDetails?access_token=";
    public static String CASH_FLOW_POST_URL = "/mff/api/workflowTemplateDetails?access_token=";
    public static String CASH_FLOW_UDATE_URL = "/mff/api/workflowTemplateDetails/WORKFLOW_PROFILE_ID?access_token=";
    public static String CASH_FLOW_GET_INFO_URL = "/mff/api/mobileApp/workflowProfileID/WORKFLOW_PROFILE_ID/templateID/TEMPLATE_ID?access_token=";
    public static String CREDIT_BUREAU_GET_INFO_URL = "/mff/api/mobileApp/workflowProfileID/WORKFLOW_PROFILE_ID/templateID/TEMPLATE_ID?access_token=";
    public static String SAVE_CONTRACT_DATA = "/mff/api/savePortfolioRepayment?access_token=";
    public static String UPLOAD_PROFILE = "/upload/files/WORKFLOW_PROFILE_ID/profilePicture?access_token=";
    public static String UPLOAD_BUSINESS = "/upload/files/WORKFLOW_PROFILE_ID/business?access_token=";
    public static String UPLOAD_PERSONAL = "/upload/files/WORKFLOW_PROFILE_ID/personal?access_token=";
    public static String VERIFY_WORKFLOW = "/mff/api/workflowTemplateDetails/verify?access_token=";
    public static String VERIFY_BRANCHES = "/mff/api/branches/treeLevel?access_token=";

    // Client
    public static String CLIENT_LOGIN_URL = BASE_URL + ":5059/client/login";
    public static String CLIENT_USER_CREATION_URL = BASE_URL + ":7190/mff/api/users";
    public static String CLIENT_VERIFY_PHONE = BASE_URL + ":5059/client/verify";

}