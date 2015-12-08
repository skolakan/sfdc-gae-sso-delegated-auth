# Google Accounts versus Google Apps for your domain #

**Salesforce Single Sign On with Delegated Authentication using Google App Engine** was built and tested against a **Google Account** not **Google Apps for your domain**

It might work against Google Apps for your domain it just not been tested or configured due to lack of access to Google Apps for your domain.

## Deploying Google App Engine application to Google Apps for your domain ##

While this has not been tested, the following links provide starting points to try to get this working with Google Apps for your domain.

[Deploying your Application on your Google Apps URL](http://code.google.com/appengine/articles/domains.html)

[Configuring Your App's Authentication](http://code.google.com/appengine/articles/domains.html) to use Google Apps

You will also need to make the **/authority** url callable from the public internet in order for salesforce to pass the token back for authentication. [How to Implement Single Sign-On with Force.com](http://wiki.developerforce.com/index.php/How_to_Implement_Single_Sign-On_with_Force.com) provides a good overview of the interactions that take place between salesforce and the delegated authentication authority (in this case the Google App Engine application).


Please let me know if you have had success setting this up and any additional pointers on how it was done.