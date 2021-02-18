/**
* Lab 01
* 
* @autho Enrique Oti
* @date January 22, 2021
*/

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


public class hw01 extends Application implements EventHandler<ActionEvent>{
   
   
   private final String sFileName = " 121Lab1.csv";
   
   TextField tfName;
   TextField tfNum;
   TextField tfCost;
   TextField tfOwed;
   Stage stage; 
   ArrayList<String> strings;
   int index = 0;
   
    
   public static void main(String[] args){
      //method inside the Application class,  it will setup our program as a JavaFX application 
      //then the JavaFX is ready, the "start" method will be called automatically  
      launch(args);
   }
   
   @Override
   public void start(Stage _stage) throws Exception{
      this.stage = _stage;
     
      /////////////////////////Setting window properties
      //set the window title
      _stage.setTitle("Enrique Item Orders Calculator");
   
      //HBox root layout with 8 pixels spacing
      VBox root = new VBox(8);
      
      //create a scene with a specific size (width, height), connnect with the layout
      Scene scene = new Scene(root, 400,400);
      
      //create flow pane
      GridPane topPane = new GridPane();
      topPane.setHgap(10);
      topPane.setVgap(5);
      topPane.setAlignment(Pos.CENTER);
      
      FlowPane midPane = new FlowPane(8, 8);
      midPane.setAlignment(Pos.CENTER);
      
      FlowPane bPane = new FlowPane(8, 8);
      bPane.setAlignment(Pos.CENTER);
  
      
      //*********************************create components
      //TextField is singe line, TextArea multiline
      // Top Components
      Label lblName = new Label("Item Name:");
      tfName = new TextField();
      topPane.setHalignment(lblName, HPos.RIGHT);
      topPane.addRow(0,lblName, tfName);//we can add rows directly
      
      Label lblNum = new Label("Number:");
      tfNum = new TextField();
      topPane.setHalignment(lblNum, HPos.RIGHT);
      topPane.addRow(1,lblNum, tfNum);//we can add rows directly
      
      Label lblCost = new Label("Cost:");
      tfCost = new TextField();
      topPane.setHalignment(lblCost, HPos.RIGHT);
      topPane.addRow(2,lblCost, tfCost);//we can add rows directly
      
      Label lblOwed = new Label("Ammount Owed:");
      tfOwed = new TextField();
      topPane.setHalignment(lblOwed, HPos.RIGHT);
      topPane.addRow(3,lblOwed, tfOwed);//we can add rows directly

      
     
   
      // Middle Components
      Button btnCalc = new Button("Calculate");
      Tooltip t1 = new Tooltip();
      t1.setText("Calculate the order total");
      btnCalc.setTooltip(t1);
      
      Button btnNext = new Button("Save");
      Tooltip t2 = new Tooltip();
      t2.setText("Save your order to the file");
      btnNext.setTooltip(t2);
      
      Button btnClear = new Button("Clear");
      Tooltip t3 = new Tooltip();
      t3.setText("Clear all the text fields");
      btnClear.setTooltip(t3);
      
      Button btnExit = new Button("Exit");
      Tooltip t4 = new Tooltip();
      t4.setText("Exit the program");
      btnExit.setTooltip(t4);
      
      // set up midPane
      midPane.getChildren().addAll(btnCalc, btnNext, btnClear, btnExit);
      
      
      //bottom pane
      Button load = new Button("Load");
      Tooltip t5 = new Tooltip();
      t5.setText("Load a file");
      load.setTooltip(t5);
      
      Button prev = new Button("<Prev");
      Tooltip t6 = new Tooltip();
      t6.setText("Go to the previous item in the list");
      prev.setTooltip(t6);
      
      Button next = new Button(">Next");
      Tooltip t7 = new Tooltip();
      t7.setText("Go to the next item in the list");
      next.setTooltip(t7);
      
      bPane.getChildren().addAll(load, prev, next);
      
   
      //link to root
      root.getChildren().addAll(topPane,midPane, bPane);
      tfOwed.setEditable(false);
      
      //add events
      btnClear.setOnAction(this);
      btnNext.setOnAction(this);
      btnExit.setOnAction(this);
      btnCalc.setOnAction(this);
      load.setOnAction(this);
      prev.setOnAction(this);
      next.setOnAction(this);
     
      
      //connect stage with the Scene and show it, finalization
      _stage.setScene(scene);
      _stage.show();
      
   }
   
   private void load(){
   
      try{
         FileChooser fileChooser = new FileChooser();
         File selectedFile = fileChooser.showOpenDialog(stage);
         System.out.println(selectedFile);
         strings = new ArrayList<String>();
         
         Scanner sc = new Scanner(new FileReader(selectedFile));
         while (sc.hasNextLine()){
            String line = sc.nextLine();
            strings.add(line);
         }
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Loaded " + strings.size()+ " items");
         alert.showAndWait();

         display(0);
         sc.close();
      }
      catch (Exception e) {e.printStackTrace();}
   }
   
   private void display(int index){
      String s = strings.get(index);
      String items[] = s.split(",");
      tfName.setText(items[0].replace("\"",""));
      tfNum.setText(items[1]);
      tfCost.setText(items[2]);
      tfOwed.setText(items[3]);
   }
   
   private void next(){
   if (index==strings.size()-1){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No more items to show");
      alert.showAndWait();
      return;
   }
   index++;
   display(index);
   
   }
   private void prev(){
   if (index==0){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No previous items to show");
      alert.showAndWait();
      return;
   }

   index--; 
   display(index);
   }
   
   private void write()
   {
      try {
            
         PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(new File(sFileName), true)));
               
         //get text
         String name = tfName.getText();
         String num = tfNum.getText();
         String cost = tfCost.getText();
         String owed = tfOwed.getText();
         pr.printf("\"%s\", %s, %s, %s", name, num,cost,owed); 
         pr.println();//important to change line
         pr.close();
         
         Alert alert = new Alert(AlertType.INFORMATION);         
         alert.setTitle("Information Dialog");
         alert.setHeaderText("Information");
         alert.setContentText("Writing was successful!");
         alert.showAndWait();

      
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   public void calculate(){
         int cost = Integer.parseInt(tfCost.getText());
         int num = Integer.parseInt(tfNum.getText());
         double amnt = num*cost;
         tfOwed.setText(""+String.format("%.2f", amnt));
         System.out.println("CALCULATE!");
      }
   
   public void handle(ActionEvent evt){
      System.out.println("YEAP!");
      
      
      
      Button btn = (Button)evt.getSource();
      switch(btn.getText()){
         case "Calculate":
            calculate();
            break;
            
         case "Save":
            calculate();
            write();
            System.out.println("SAVE!");
            break;
            
         case "Clear":
            tfName.clear();
            tfNum.clear();
            tfCost.clear();
            tfOwed.clear();
            tfName.setText(null);
            tfNum.setText(null);
            tfCost.setText(null);
            tfOwed.setText(null);
            System.out.println("CLEAR!");
            break;
            
        case "Exit":
            System.out.println("EXIT!");
            System.exit(0);
            break;
            
            
       case "Load":
         load();
         break;
         
       case "<Prev":
         prev();
         break;
         
       case ">Next":
         next();
         break;
         
      }
   }
}   
  
