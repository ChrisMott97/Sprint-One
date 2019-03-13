package org.gsep.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Niha Gummakonda
 * @version 1.0 07/03/2019
 */
public class Worker implements Runnable {
    public static final int BUFFER_SIZE = 3000;
    private Socket soc;

    Worker(Socket soc){
        this.soc = soc;
    }

    @Override
    public void run() {
        BufferedWriter writer;
        BufferedReader reader;
        ObjectInputStream ois;
        
    		
        try {
        		
        		//For reading inputs
        		reader = new BufferedReader(new InputStreamReader(soc.getInputStream())); 

            
            System.out.println("Connected!!");
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
            dos.writeUTF("Welcome to socket! ");
            
	            //For creating a directory
	            String command = reader.readLine();
	            
	            if(command.contains("mkdir"))
	            {
	                System.out.println("Creating a new directory");
	                String toSplit = command;
	                String[] values = toSplit.split(",");
	                new File(values[1]).mkdirs();
	            }
	            
	            if(command.contains("fileSend"))
	            {
	            		System.out.println("Recieved send request");
	                    ois = new ObjectInputStream(soc.getInputStream());
	                    System.out.println("ReachA");
	                    FileOutputStream fos = null;
	                    byte [] buffer = new byte[BUFFER_SIZE];
	             
	                    // 1. Read file name.
	                    Object o = ois.readObject();
	             
	                    if (o instanceof String) {
	                        fos = new FileOutputStream(o.toString());
	                    } else {
	                        throwException("Something is wrong");
	                    }
	             
	                    // 2. Read file to the end.
	                    Integer bytesRead = 0;
	             
	                    do {
	                        o = ois.readObject();
	             
	                        if (!(o instanceof Integer)) {
	                            throwException("Something is wrong");
	                        }
	             
	                        bytesRead = (Integer)o;
	             
	                        o = ois.readObject();
	             
	                        if (!(o instanceof byte[])) {
	                            throwException("Something is wrong");
	                        }
	             
	                        buffer = (byte[])o;
	             
	                        // 3. Write data to output file.
	                        fos.write(buffer, 0, bytesRead);
	                       
	                    } while (bytesRead == BUFFER_SIZE);
	                     
	                    System.out.println("File transfer success");
	                     
	                    fos.close();
	                    ois.close();
	            }	    
            
        }catch (Exception e){
            System.out.println(e);
        }


    }

    
     public static void throwException(String message) throws Exception {
        throw new Exception(message);
    }
}
