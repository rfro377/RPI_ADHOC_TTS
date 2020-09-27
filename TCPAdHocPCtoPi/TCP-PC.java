import java.io.*;
import java.net.*;

class TCPPC {

    private BufferedReader inFromUser;
    private Socket clientSocket;
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    private DataInputStream dis;

    TCPPC(){ inFromUser = new BufferedReader(new InputStreamReader(System.in));};

    public String makeConnection(String ip, String port) throws UnknownHostException, IOException {
        String response = "-";
        clientSocket = new Socket(ip, Integer.valueOf(port));
       
        outToServer = new DataOutputStream(clientSocket.getOutputStream());

        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        dis = new DataInputStream(clientSocket.getInputStream());
        response = "+";
        return response;
    }

    public String sendMessage(String[] commandarray) throws IOException {
        String message = "";
        
        for(int i = 1; i < commandarray.length;i++){
            message = message + commandarray[i];
        }

        outToServer.writeBytes(message + '\n');
        outToServer.flush();

        return "+";
    }
    public void closeconnection() throws Exception {
        try {
           clientSocket.close();
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
        clientSocket = null;
        outToServer.close();
        outToServer = null;
        
        inFromServer.close();
        inFromServer = null;
        dis.close();
        dis = null;

   }

    public static void main(String args[]) {
        TCPPC iTcppc = new TCPPC();
        iTcppc.run();
    }

    public void run(){
        String userInpuString = null;
        String[] commandarray;
        try {
            while(true){
                userInpuString = null;
                while(userInpuString == null){
                    userInpuString = inFromUser.readLine();
                }

                commandarray = userInpuString.split(" ");

                switch(commandarray[0]){
                    case "connect":
                        if(commandarray.length != 3){
                            System.out.println("Incorrect parameter number, try again!");
                            continue;
                        } 
                        if(this.makeConnection(commandarray[1], commandarray[2]) == "+"){
                            System.out.println(String.format("Connection secured at %s on %s",commandarray[1], commandarray[2]));
                        }else{
                            System.out.println("An error occured");
                        }
                    break;
                    case "message":
                    if(commandarray.length > 1){
                        sendMessage(commandarray);
                    }else{
                        System.out.println("An error occured: not enough Args");
                    }
                    break;
                    case "close":
                    closeconnection();
                    break;
                }
            }
            
            
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

} 