package com.hm;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.io.InputStream;

import javax.ws.rs.POST;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

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
    	String SFTPHOST=System.getenv("SFTPHOST");
    	int    SFTPPORT = Integer.valueOf(System.getenv("SFTPPORT"));
    	String SFTPUSER=System.getenv("SFTPUSER");
    	String SFTPPASS=System.getenv("SFTPPASS");
    	String SFTPWORKINGDIR=System.getenv("SFTPWORKINGDIR");

    	Session     session     = null;
    	Channel     channel     = null;
    	ChannelSftp channelSftp = null;

    	
    	new FTPClient();
        System.out.println("started dthe services *** "+fileInputStream);
      int read = 0;
        byte[] bytes = new byte[1024];
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
				}
				catch (SftpException e) {
					e.printStackTrace();
					System.out.println("*** entered into catch block while SftpException "+e);
	
					return "ERROR - EFTP1X01 - SftpException"+e;

				}catch(JSchException ex){
					ex.printStackTrace();
					System.out.println("*** entered into catch block while JSchException "+ex);
					return "ERROR - EFTP1X01 - JSchException"+ex;
					} 
				} catch (FileNotFoundException e1) {
					System.out.println("*** entered into catch block while file not found "+e1);
		
					e1.printStackTrace();
					return "ERROR - EFTP1X01 - FileNotFoundException"+e1;

				} catch (IOException e) {
					System.out.println("*** entered into catch block while IOException "+e);
					e.printStackTrace();
					return "ERROR - EFTP1X01 - FileNotFoundException"+e;

				}  
					catch (Exception e) {
						System.out.println("*** entered into catch block while IOException "+e);
						e.printStackTrace();
						return "ERROR - EFTP1X01 - Unhandled Exception "+e;
			
					}  
	
        return "Success - SFTP1X01";
    }


}
