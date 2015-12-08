# Setup #

These setup instructions are for Single Sign on with Google Accounts. If you are looking into setting this up for Googe Apps please see
[Google Accounts vs Google Apps for your domain](GoogleAccountsvsGoogleApps.md)

## Prerequisites ##

  * Google App Engine account
  * Google user name
  * Salesforce org with
    * Administrator user access (to set up single sign on)
    * Non-administrator user access (to test single sign on)
    * "Single Sign On" enabled (may need to call salesforce support to do this - [Salesforce Documentation for Delegated Authenication](https://login.salesforce.com/help/doc/en/sso_delauthentication_configuring.htm))
  * Eclipse with the Google App Engine plug-in installed

## Google App Engine set up ##

  1. Install the code from this project into an Google App Engine Eclipse project
    1. Create a new project from "Checkout Projects from SVN"
    1. Select read only SVN project
    1. Then "Check out as a project configured using the New Project Wizard"
    1. Select Google "Web Application Project"
    1. For package choose "com.sfdc"
    1. De-select "Use Google Web Toolkit"
    1. Due to SVN project configuration(on my part) not all files are correctly set up for project so -
      1. Copy appengine-web.xml in war/WEB-INF from another Google "Web Application Project" into new project and change the application id
      1. Copy lib directory in war/WEB-INF from another Google "Web Application Project" into new project
  1. Change _secretKeyword_ String in Token.java to be your secret Keyword. For example this could be a random set of characters.
  1. Upload project into the Google App Engine
  1. Test it is working by logging into "https://your_google_app_name.appspot.com/home" with Google user name. Where **your\_google\_app\_name** is the Google App Engine application you have created.

## salesforce.com set up ##

  1. Enable "Is Single Sign-On Enabled" option on a profile
  1. Associate a new Non-administrator user with "Is Single Sign-On Enabled" profile. The salesforce user name for this user will be the same as the Google user name for example xxx@gmail.com
  1. Set Delegated Gateway URL to "https://your_google_app_name.appspot.com/authority" (Setup - Administration Setup - Security Controls - Single Sign-On Setting). Where **your\_google\_app\_name** is the Google App Engine application you have created.
  1. Login into "https://your_google_app_name.appspot.com/home" with Google user name. Where **your\_google\_app\_name** is the Google App Engine application you have created.
  1. Test it is working by clicking "Login to Salesforce" link on the Google App Engine page

### salesforce.com Portal Login ###

The default setup is to login into salesforce via the main application as opposed to going via one of the salesforce portals. To login into the salesforce portal the Google App Engine application url needs a parameter passed into it.

  1. Login into "https://your_google_app_name.appspot/?salesforceloginurl=" plus url encoded portal login address i.e. "https://your_google_app_name.appspot.com/?salesforceloginurl=https%3a%2f%2fna1.salesforce.com%2fsecur%2flogin_portal.jsp%3forgId%3dyour_orgId%26portalId%3dyour_portalId". Where **your\_google\_app\_name** is the Google App Engine application you have created. Where **your\_orgId** is your salesforce org id and **your\_portalId** is your portalID
    * URL for logining into portal can be found in salesforce
    * URL encoding of portal login url could be done at "http://www.opinionatedgeek.com/dotnet/tools/urlencode/Encode.aspx" for example
  1. Test it is working by clicking "Login to Salesforce" link