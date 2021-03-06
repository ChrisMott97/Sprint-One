package org.gsep.manager;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 * Client
 *
 * @author Humzah Malik
 * @version 1.0
 *
 */
public class StoreManagerModel {

	static ArrayList<File> filesSong = new ArrayList<>();
	static Socket soc;
	static final String HOST = "localhost";
	static final int PORT = 3335;

	/**
	 * Method that calls the methods readFile() and zipFile(). The purpose of run() is to invoke the client side methods all at once.
	 * @throws Exception
	 */


	StoreManagerModel(ArrayList<File> filesSong){

		this.filesSong = filesSong;
		try {
			soc = new Socket(HOST, PORT);
		} catch (IOException e) {
			System.out.println("Guitar Zero Lite server is down. Please try again later");
			//Close application
			System.exit(0);
		}

	}


	/**
	 * Method that reads a file and returns its contents in a string.
	 * @author humzahmalik
	 * @return
	 * @throws IOException
	 */
	public static String readFile() throws IOException
	{
		//The first element in the list is the file containing the name. 
		String path = filesSong.get(0).toString();
		Charset encoding = StandardCharsets.UTF_8;
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		//hold contents of file in name
		return new String(encoded, encoding);
	}

	
	/**
	 * Method that sends array of files over socket to server
	 * @author humzahmalik and niha
	 * @throws IOException
	 */
	public static void sendFile() throws IOException {

		DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
		
		//Notify server that file is about to be sent
		dos.writeUTF("Send," +filesSong.size()+ "," +readFile());
		System.out.println("File sending process starting.");
		
		//Start i at 1 so first element in list, which is the file containing the title, is sent.
		for(int i =1; i <filesSong.size(); i++) {
			FileInputStream fis = new FileInputStream(filesSong.get(i));
			//Write file name along with its length
			dos.writeUTF(filesSong.get(i).getName() + "," + (int) filesSong.get(i).length() );
			//Create byte stream of correct length
			byte[] b = new byte[(int) filesSong.get(i).length()];
			//Read file into input stream
			fis.read(b, 0, b.length);
			OutputStream os = soc.getOutputStream();
			//Write file over output stream
			os.write(b,0,b.length);
			
			//Completion message
			System.out.println("Comleted sending over " + filesSong.get(i).getName() + " to the Guitar Zero Lite server!");
		}

	}
	


}