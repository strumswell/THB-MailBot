# THB-MailBot

Codacy Project Certifcate: [![Codacy Badge](https://api.codacy.com/project/badge/Grade/8a8c774d495c4e0284ac17344e7c097e)](https://www.codacy.com/app/strumswell/THB-MailBot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=strumswell/THB-MailBot&amp;utm_campaign=Badge_Grade)

### General 

Sends an Email containing cancelled lectures at the Technische Hochschule Brandenburg. Should be used together with the "screen" command are something like this. Standard checking interval is 6 hours. E-Mail only gets send if something changes on the website. 

![mail](https://i.imgur.com/ek4JVz6.png)

### Libraries 

 - SimpleJavaMail http://www.simplejavamail.org/
   - JavaMail http://www.oracle.com/technetwork/java/javamail/index-138643.html
   - slf4j (api & simple.jar) https://www.slf4j.org/download.html
   - email-rfc2822-validator https://github.com/bbottema/email-rfc2822-validator

### License 

THB-MailBot is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
http://creativecommons.org/licenses/by-nc-sa/3.0/