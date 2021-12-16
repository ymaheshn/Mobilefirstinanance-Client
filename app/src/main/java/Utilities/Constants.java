package Utilities;

import java.util.ArrayList;

import onboard.ClientDataDTO;
import onboard.TabDto;
import onboard.WorkFlowTemplateDto;

/**
 * Created by gufran khan on 10-06-2018.
 */

public class Constants {
    /*defining constants varibales to identify the fragments*/
    public static final int ONBOARD_FRAGMENT = 1;
    public static final int LOANS_FRAGMENT = 2;
    public static final int MAP_FRAGMENT = 3;
    public static final int VAS_FRAGMENT = 4;
    public static final int SAVINGS_FRAGMENT = 5;
    public static final int ADD_CLIENT_FRAGMENT = 6;
    public static final int KYC_FRAGMENT = 6;
    public static final int CASH_FLOW_FRAGMENT = 7;
    public static final int CREDIT_BUREAU_FRAGMENT = 8;
    public static final int CREDIT_SCORE_FRAGMENT = 9;
    public static final int APPROVAL_FRAGMENT = 10;
    public static final int LOAN_COLLECTIONS_FRAGMENT = 11;
    public static final int PROFILE_DETAILS_FRAGMENT = 12;
    public static final int DASHBOARD_FRAGMENT = 13;
    public static final int SHUFPTI_FRAGMENT = 14;
    public static final int SEARCH_PROFILE_LIST_FRAGMENT = 15;
    public static int CURRENT_FRAGMENT = -1;

    /*tab names constants*/
    public static final String KYC = "KYC";
    public static final String CASH_FLOW = "CashFlow";
    public static final String CREDIT_BUREAU = "Credit Bureau";
    public static final String CREDIT_SCORE = "Credit Score";
    public static final String APPROVAL = "Approval";
    public static final String PROFILE_DETAILS = "ProfileDetails";



    /*header items*/
    public static final String SCAN = "SCAN";
    public static final String SEARCH = "SEARCH";

    public static ClientDataDTO clientDataDTO = null;
    public static boolean isFromAddClient = false;
    public static WorkFlowTemplateDto workFlowTemplateDt = null;

    public static final String CONTACT_URL = "https://odedtech.atlassian.net/servicedesk/customer/portal/3";

    public interface KeyExtras {
        String CONTRACT_ID = "contract_id";
        String LINKED_PROFILE = "linked_profile";
        String LINKED_PROFILE_COLLECTION = "linked_profile_collection";
    }

    public interface DatabaseConstants {
        String DATABASE_NAME = "mff_offline";
        String DATABASE_CONTRACTS_NAME = "contracts_offline";
    }

    public interface DynamicUiTypes {
        String TYPE_PHONE = "Phone";
        String TYPE_LIST = "List";
        String TYPE_NUMBER = "Number";
        String TYPE_VARCHAR = "Varchar";
        String TYPE_BRANCH = "Branch";
        String TYPE_DATE = "Date";
        String TYPE_IMAGE = "image";
    }

    public static final String FLAVOR_CLIENT = "client";
    public static final String FLAVOR_AGENT = "agent";
}
