import javax.mail.Message.RecipientType;
import javax.activation.DataHandler;
import javax.mail.Session;
import javax.mail.Message;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.*;
import javax.mail.*;
import javax.mail.Authenticator.*;
import javax.mail.Message.RecipientType;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class SecretSanta {

    public static void main(String[] args) throws Exception {
        // hash of name:email
        HashMap<String, String> members = new HashMap<>();
        members.put("Eric", "ejbaier@gmail.com");
        members.put("Tanya", "tatiana.a.baier@gmail.com");
        members.put("Ryan", "ryan.baier@gmail.com");
        members.put("Rachel", "wolfsonr@gmail.com");
        members.put("Kristin", "kkhaleski@gmail.com");
        members.put("Ambi", "ambi.baier@gmail.com");
        members.put("Wayne", "whbaier@gmail.com");
        members.put("Paul", "pabaier@gmail.com");

        // List<String> to = List.addAll(members.keySet());
        List<String> toKeys = new ArrayList<>();
        toKeys.addAll(members.keySet());
        List<String> fromKeys = new ArrayList<>();
        fromKeys.addAll(members.keySet());

        HashMap<String, String> pairings = new HashMap<>();
        Random r = new Random();
        for(int i = 0; i < fromKeys.size(); i++) {
            String from = fromKeys.get(i);
            int num = r.nextInt(toKeys.size());
            String pick = toKeys.get(num);
            if(from.equals(pick)) {
                if(toKeys.size() == 1) {
                    i = -1;
                    toKeys = new ArrayList<>();
                    toKeys.addAll(members.keySet());
                    continue;
                }
                else {
                    i--;
                    continue;
                }
            }
            // System.out.println(members.get(from) + " -> " + members.get(pick));
            pairings.put(from, pick);
            toKeys.remove(num);
        }

        // members.forEach((s,k) -> System.out.println(s + " -> " + k));
        // System.out.println();
        // pairings.forEach((s,k) -> System.out.println(s + " -> " + k));
        // to.forEach(s -> System.out.println(members.get(s)));

        String host = "smtp.gmail.com";
        String user = "**********"; // put email here
        String password = "*********"; // put password here
        String port = "25";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", user);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.debug", "true");
        // for secure connection to smtp        
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");
        // for ssl
        // props.put("mail.smtp.socketFactory.port", port);
        // props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props,
        new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        }
        );

        
        for(String from : fromKeys) {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setHeader("Secret Santa Email", "Secret Santa Email");
            message.setSubject("Secret Santa Pairings!");
            message.addRecipients(Message.RecipientType.TO, members.get(from));
            message.setContent("Merry Christmas and Happy Hanukkah " + from + "! <br>" +
            "Your Secret Santa is " + pairings.get(from) + "<br><br>" + 
            "Please get them a gift of no more than $50. I suggest not talking about " +
            "who you got with anyone (not even me, I do not know who you got) - " +
            "We've all been there where someone starts talking about their gift, but we " +
            "can't say anything because we have them - it's a dead givaway. " + 
            "If you have a question about what to get your pairing either ask someone else, " +
            "but don't make a conversation out of it, or email or text the group. And try to respond " +
            "if someone else emails everyone, because they may be asking about YOU! Ultimately " + 
            "just don't be asking a lot of questions!<br><br>" +
            "Let's have fun with this and most importantly remember what the holidays are really about! " +
            "(not presents) <br><br>" + 
            "Happy gift giving!",
            "text/html; charset=utf-8");
    
            try{
                Transport transport = session.getTransport("smtps");
                transport.connect(host, user, password);
                transport.sendMessage(message, message.getAllRecipients());
                System.out.println("Mail Sent Successfully");
                transport.close();
            } catch (SendFailedException sfe){
                System.out.println(sfe);
            }
        }
    }


}