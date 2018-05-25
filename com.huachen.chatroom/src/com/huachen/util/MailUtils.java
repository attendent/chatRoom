package com.huachen.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailUtils {
    
    public static void sendEmail(final String sender,final String password,String[] receivers, String title, String mailContent, File[] attachements, String mimetype, String charset) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        Authenticator authenticator = new Authenticator() {
        	@Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        };
        //使用Properties创建Session
        Session session = Session.getDefaultInstance(props, authenticator);
        try {
            //使用session创建MIME类型的消息
            MimeMessage mimeMessage = new MimeMessage(session);
            //设置发件人邮件
            mimeMessage.setFrom(new InternetAddress(sender));
            //获取所有收件人邮箱地址
            InternetAddress[] receiver = new InternetAddress[receivers.length];
            for (int i=0; i<receivers.length; i++) {
                receiver[i] = new InternetAddress(receivers[i]);
            }
            //设置收件人邮件
            mimeMessage.setRecipients(Message.RecipientType.TO, receiver);
            //设置标题
            mimeMessage.setSubject(title, charset);
            //设置邮件发送时间
            mimeMessage.setSentDate(new Date());
            //创建附件
            Multipart multipart = new MimeMultipart();
            //创建邮件内容
            MimeBodyPart body = new MimeBodyPart();
            //设置邮件内容
            body.setContent(mailContent, (mimetype!=null && !"".equals(mimetype) ? mimetype : "text/plain")+ ";charset="+ charset);
            multipart.addBodyPart(body);//发件内容
            //设置附件
            if(attachements!=null){
                for (File attachement : attachements) {
                    MimeBodyPart attache = new MimeBodyPart();
                    attache.setDataHandler(new DataHandler(new FileDataSource(attachement)));
                    String fileName = getLastName(attachement.getName());
                    attache.setFileName(MimeUtility.encodeText(fileName, charset, null));
                    multipart.addBodyPart(attache);
                }
            }
            //设置邮件内容（使用Multipart方式）
            mimeMessage.setContent(multipart);
            //发送邮件
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String getLastName(String fileName) {
        int pos = fileName.lastIndexOf("\\");
        if (pos > -1) {
            fileName = fileName.substring(pos + 1);
        }
        pos = fileName.lastIndexOf("/");
        if (pos > -1) {
            fileName = fileName.substring(pos + 1);
        }
        return fileName;
    }
}