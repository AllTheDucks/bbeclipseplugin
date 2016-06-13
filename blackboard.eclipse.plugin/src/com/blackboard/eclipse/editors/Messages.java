package com.blackboard.eclipse.editors;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.blackboard.eclipse.editors.messages"; //$NON-NLS-1$

	public static String generalPage_label;
	public static String generalPage_general_title;
	public static String generalPage_general_description;
	public static String generalPage_general_name_label;
	public static String generalPage_general_handle_label;
	public static String generalPage_general_desc_label;
	public static String generalPage_general_version_label;
	public static String generalPage_general_bbVer_label;
	public static String generalPage_general_bbVer_values;
	public static String generalPage_general_bbMaxVer_label;
	public static String generalPage_general_bbMaxVer_values;
	public static String xmlPage_label;

	public static String generalPage_vendor_title;
	public static String generalPage_vendor_description;
	public static String generalPage_vendor_id_label;
	public static String generalPage_vendor_name_label;
	public static String generalPage_vendor_url_label;
	public static String generalPage_vendor_description_label;
	
	public static String generalPage_adminUrls_title;
	public static String generalPage_adminUrls_description;	
	public static String generalPage_adminUrls_configUrl_label;
	public static String generalPage_adminUrls_removeUrl_label;
	
	public static String generalPage_vendorId_tooLong_errorMsg;
	public static String generalPage_handle_tooLong_errorMsg;
	public static String generalPage_name_tooLong_errorMsg;
	
	//	Application Definitions Page properties
	public static String appDefsPage_label;
	public static String appDefsPage_application_title;
	public static String appDefsPage_application_description;
	public static String appDefsPage_application_handle_label;
	public static String appDefsPage_application_name_label;
	public static String appDefsPage_application_description_label;
	public static String appDefsPage_application_type_label;
	public static String appDefsPage_application_type_values;
	public static String appDefsPage_application_smallIcon_label;
	public static String appDefsPage_application_largeIcon_label;
	public static String appDefsPage_application_useSsl_label;
	public static String appDefsPage_application_canAllowGuest_label;
	public static String appDefsPage_application_isCourseTool_label;
	public static String appDefsPage_application_isGroupTool_label;
	public static String appDefsPage_application_isOrgTool_label;
	public static String appDefsPage_application_isSysTool_label;
	
	public static String appDefsPage_linkMaster_label;
	public static String appDefsPage_linkMaster_add_label;
	public static String appDefsPage_linkMaster_remove_label;
	public static String appDefsPage_linkDetail_label;
	public static String appDefsPage_linkDetail_type_label;
	public static String appDefsPage_linkDetail_type_values;
	public static String appDefsPage_linkDetail_name_label;
	public static String appDefsPage_linkDetail_url_label;
	public static String appDefsPage_linkDetail_description_label;
	public static String appDefsPage_linkDetail_toolbarIcon_label;
	public static String appDefsPage_linkDetail_listitemIcon_label;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
