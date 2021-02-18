import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.Alert.*;  
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
import javafx.scene.text.*;

public class hw02 extends Application implements EventHandler<ActionEvent> {
   //window attributes
   private Stage stage;
   private Scene scene;
   int usable = 0;
   boolean notUsable = false;
   String[] names = { "First name", "Last name", "Birth day", "Birth month", "Birth year", "Weight", "Height" };
   
   //top
   Label fileLbl = new Label("File");
   TextField tfFileName = new TextField();
   Button btnOpen = new Button("Open");
   
   //middle
   Label inLbl = new Label("Records In:");
   TextField tfRecordsIn = new TextField();
   Label outLbl = new Label("Records Out:");
   TextField tfRecordsOut = new TextField();  
   
   //bottom
   TextArea taData = new TextArea();
   
   public static void main(String[] args) {
      launch(args);
   }
   
   public void start(Stage _stage) {
      // Setup window
      stage = _stage;
      stage.setTitle("Baseball - Enrique Oti");
      VBox root = new VBox(8);
      
      //top
      FlowPane fpTop = new FlowPane(8,8);
      fpTop.setAlignment(Pos.CENTER);
      fpTop.getChildren().addAll(fileLbl, tfFileName, btnOpen);
      
      //mid
      FlowPane fpMid = new FlowPane(8,8);
      fpMid.setAlignment(Pos.CENTER);
      tfRecordsIn.setPrefWidth(50);
      tfRecordsOut.setPrefWidth(50);
      
      fpMid.getChildren().addAll(inLbl, tfRecordsIn, outLbl,tfRecordsOut);
      
      
      //bottom
      FlowPane fpBottom = new FlowPane(8,8);
      fpBottom.getChildren().addAll(taData);
      fpBottom.setAlignment(Pos.CENTER);
      
      
      //root
      root.getChildren().addAll(fpTop, fpMid, fpBottom);
      
      scene = new Scene(root, 375, 400);
      stage.setScene(scene);
      
      btnOpen.setOnAction(this);
      
      stage.show();  
   }
      
      //events
   public void handle(ActionEvent ae){
      String command = ((Button)ae.getSource()).getText();
      switch(command){
         case "Open":
            open();
            break;
      
      }
   }
      
      //open
   private void open(){
   
      try{
         FileChooser fileChooser = new FileChooser();
         File selectedFile = fileChooser.showOpenDialog(stage);
         System.out.println(selectedFile);
         
         tfFileName.setText(selectedFile+"");
         
         Scanner sc = new Scanner(new FileReader(selectedFile));
         
         DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("BinaryWrite.dat"), false));
        
         
         
         int counter = 0;
         sc.nextLine();
         counter++;
         while (sc.hasNextLine()){
            notUsable = false;
            String line = sc.nextLine();
            String[] splitLine = line.split(",");
            
            
            if(line.trim().isEmpty() == false && counter!=0){
              
              if(splitLine.length < 7){
               taData.appendText("\n\nError found in: " + line + "\nNot enough fields in the record");
            }
            else {
               
               for (int i = 0; i < splitLine.length; i++) {
                                splitLine[i] = splitLine[i].trim();

                                /* Check the null values */
                                if (splitLine[i].equals("null")) {
                                    if (notUsable == true) {
                                        taData.appendText(", " + names[i]);
                                    } else {
                                        taData.appendText(String.format("\n\nError found in: %s\nOffending data: %s",
                                                line, names[i]));
                                        notUsable = true;
                                        
                                    }
                                }
                            }
                           }

               //check whether the string is usable (no null values), if yes, increment the Usable counter
               
               
               //atributes
               if(notUsable == false){
               String first = splitLine[0];
               String last = splitLine[1];
               int day = Integer.parseInt(splitLine[2]);
               int month = Integer.parseInt(splitLine[3]);
               int year = Integer.parseInt(splitLine[4]);
               int weight = Integer.parseInt(splitLine[5]);
               double height = Double.parseDouble(splitLine[6]);
               
               
               dos.writeUTF(first);
               dos.writeUTF(last);
               dos.writeInt(day);
               dos.writeInt(month);
               dos.writeInt(year);
               dos.writeInt(weight);
               dos.writeDouble(height);
               
               usable++;
               }

            }
            counter++;
            
                        
            }
         tfRecordsIn.setText(counter+"");
         sc.close();
         dos.close();
         tfRecordsOut.setText(usable+"");
         read(); 
         }
         
      catch (Exception e) {e.printStackTrace();}
   }
   
   private void read(){
      DataInputStream dis = null;
      int counter = 0;
      try{
         dis = new DataInputStream(new FileInputStream(new File("BinaryWrite.dat")));
         taData.appendText(String.format("\n\n\n%-20s %10s %8s %8s\n\n", "First & Last name", "Birthdate",
                        "Weight", "Height"));
                        
         for(int i=0; i<usable; i++){
            String first = dis.readUTF();
            String last = dis.readUTF();
            int day = dis.readInt();
            int month = dis.readInt();
            int year = dis.readInt();
            int weight = dis.readInt();
            double height = dis.readDouble();
            String date = month+"/"+day+"/"+year;
            String name = first + " " + last;
            
            
            taData.appendText(String.format("%-20s %10s %8d %8.1f\n", name, date, weight, height));
         }
         dis.close();
           
      }
      catch (EOFException e) {e.printStackTrace();}
      catch (FileNotFoundException e) {e.printStackTrace();}
      catch (IOException e) {e.printStackTrace();}
    
   
   }
   
}
