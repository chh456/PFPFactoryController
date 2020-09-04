package de.uniks.pfp.digitaltwin;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimTalkSim {

    private static String ip = "127.0.0.1";
    private static int port = 33333;
    private static Socket socket;

    private static String cfg =  "digitaltwin.cfg";


    private int sendErrorCounter = 0;
    private int numberOfSendTrys = 3;



    public SimTalkSim() {

    }

    public static boolean validateIp (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "DigitalTwin with ip " + ip + " and port " + port;
    }

    public static void main (String [] args) {
        if (args.length > 2) {
            System.out.println("Run with <ip> <port> as parameters.");
            System.exit(1);
        }

        SimTalkSim digitalTwin = new SimTalkSim();

        if (args.length > 0 && args[0] != null && validateIp(args[0]))
            digitalTwin.setIp(args[0]);

        if (args.length > 1 && args[1] != null)
            digitalTwin.setPort(args[1]);

        System.out.println(digitalTwin);

        digitalTwin.init();

        try {

            FileInputStream cfgstream = new FileInputStream(cfg);;
            BufferedReader br = new BufferedReader(new InputStreamReader(cfgstream));

            String strLine;
            String [] arguments;
            int counter = 1;

            while ((strLine = br.readLine()) != null)   {
                // System.out.println("Line " + counter + ": " + strLine);
                System.out.println("Line" + counter + ": ");
                arguments = strLine.split(" ");

                if (arguments.length == 2) {
                    // System.out.println("Sending " + arguments[0]);
                    send(arguments[0]);
                    wait(arguments[1]);
                } else if (arguments.length == 1) {
                    wait(arguments[0]);
                } else {
                    System.out.println("Skipping (error). Check your " + cfg);
                    counter++;
                    continue;
                }

                counter++;
            }

            cfgstream.close();

        } catch (FileNotFoundException e) {
            System.out.println("Config file " + cfg + " was not found.");
            printCurrentPath();
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOException while reading " + cfg);
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Socket could not be closed.");
            e.printStackTrace();
        }

    }

    public static void send(String msg) {

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Problem with sending the message " + msg);
            e.printStackTrace();
        }
        printWriter.print(msg);
        printWriter.flush();

    }

    public static void wait(String time) {
        try {
            System.out.println("Waiting for " + time + "ms");
            Thread.sleep(Integer.parseInt(time));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Check your " + cfg + ": " + time + " is not a number.");
        }
    }

    public static void printCurrentPath() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
    }

    private void init() {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println("Was not able to create socket with ip " + ip + " and port " + port);
            System.exit(1);
        }
    }

    private void setPort(String arg) {
        port = Integer.parseInt(arg);
    }

    private void setIp(String arg) {
    	System.out.println("Setting ip to " + arg);
        ip = arg;
    }

}
