package com.hm;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.io.InputStream;

import javax.ws.rs.POST;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Path("sendFiles")
public class FileTransfer {
	
    @POST
   // @Path("file")
	@Path("file/{filename}")

    @Consumes("*/*")
    public String uploadPdfFile(InputStream fileInputStream,@PathParam("filename") String filename) 
    {     
    	System.out.println("file name is **** "+filename);
    	// variable to connect to sftp
    /*	String SFTPHOST = "182.19.57.244";
    	int    SFTPPORT = 22;
    	String SFTPUSER = "Cloudlendingturner";
    	String SFTPPASS = "Cloudlending@321";
    	String SFTPWORKINGDIR = "/FTP/";
    	  */
    	String SFTPHOST=System.getenv("SFTPHOST");
    	int    SFTPPORT = Integer.valueOf(System.getenv("SFTPPORT"));
    	String SFTPUSER=System.getenv("SFTPUSER");
    	String SFTPPASS=System.getenv("SFTPPASS");
    	String SFTPWORKINGDIR=System.getenv("SFTPWORKINGDIR");

    	Session     session     = null;
    	Channel     channel     = null;
    	ChannelSftp channelSftp = null;

    	
    	///////////
        FTPClient client = new FTPClient();
        boolean login;

    	System.out.println("started dthe services *** "+fileInputStream);
      //  FTPClient client = new FTPClient();
        FileInputStream fis = null;
        int read = 0;
        byte[] bytes = new byte[1024];
       // boolean login;
        OutputStream out;
		try {
			// File file=new File("Application.csv");   
			File file=new File(filename); 
			out = new FileOutputStream(file);
			System.out.println("**** output stream"+out.toString());
	        while ((read = fileInputStream.read(bytes)) != -1)
	        {
	            out.write(bytes, 0, read);

	        }
	        out.flush();
			System.out.println("**** output flush"+out.toString());
	        out.close();
			System.out.println("**** output close"+out.toString());
			System.out.println("*** executed try block sucessfully ");
			// code to push file to sftp
			try{
	            JSch jsch = new JSch();
	            session = jsch.getSession(SFTPUSER,SFTPHOST,SFTPPORT);
	            session.setPassword(SFTPPASS);
	            java.util.Properties config = new java.util.Properties();
	            config.put("StrictHostKeyChecking", "no");
	            session.setConfig(config);
	            session.connect();
	            channel = session.openChannel("sftp");
	            channel.connect();
	            channelSftp = (ChannelSftp)channel;
	            channelSftp.cd(SFTPWORKINGDIR);
	         //   File f = new  File("D:/test004hm.txt");
	            channelSftp.put(new FileInputStream(file), file.getName());
	}catch(Exception ex){
	ex.printStackTrace();
	}

			///////////
	        InputStream in = new FileInputStream(file);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        StringBuilder out1 = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out1.append(line);
	            System.out.println("*** line is "+line);
	        }
	        System.out.println("content in the file "+out1.toString());   //Prints the string content read from input stream
	        reader.close();

			return out1.toString();
			/////
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("*** entered into catch block while file not found "+e1);

			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("*** entered into catch block while IOException "+e);

			e.printStackTrace();
		}  
	
        return "done";
    }


}
