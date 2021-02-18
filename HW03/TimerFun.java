//@author Enrique Oti
//@version 1.0
//@date Febuary 16, 2021

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
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import java.util.Timer;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.layout.BackgroundFill;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class TimerFun extends Application implements EventHandler<ActionEvent> {
   
   //atributes
   private Stage stage;
   private Scene scene;
   
   Timer timer;
   Timer timerRainbow;
   
   //array of colors
   Color[] color = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.INDIGO, Color.VIOLET };

   public static final int NUM_BARS = 7;

   Label[] lC = new Label[NUM_BARS];

   int cnt = 0;
   
   //creating the format
   Label timeLabel;
   SimpleDateFormat timeFormat = new SimpleDateFormat("EEE dd MMM YYYY HH:mm:ss");
   
   private boolean continueBool = true;

   public static void main(String args[]) {
      launch(args);
   }

   public void start(Stage _stage) {
      stage = _stage;
      stage.setTitle("Timer Fun");
      VBox root = new VBox(0);
   
   
      // menu bar
      MenuBar mbar = new MenuBar();
      Menu m1 = new Menu("File");
      MenuItem file = new MenuItem("Exit");
   
      Menu m2 = new Menu("About");
      MenuItem about = new MenuItem("Help");
   
      m1.getItems().addAll(file);
      m2.getItems().addAll(about);
      
      file.setOnAction(this);
      about.setOnAction(this);
      
      mbar.getMenus().addAll(m1, m2);
   
      // date and time
      FlowPane fpDate = new FlowPane(8, 8);
   
      timeLabel = new Label(timeFormat.format(new Date()));
   
      timeLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
      fpDate.getChildren().addAll(timeLabel);
      fpDate.setPadding(new Insets(10, 10, 10, 10));
      fpDate.setAlignment(Pos.CENTER);
   
      timer = new java.util.Timer();
      timer.scheduleAtFixedRate(new MyTimerTask(), 0, 1000);
      
      timerRainbow = new java.util.Timer();
      timerRainbow.scheduleAtFixedRate(new MyTimerTask2(), 2000, 500);
   
      // add items and display
      FlowPane fpRainbow = new FlowPane(8, 8);
      
      showColors();
      fpRainbow.getChildren().addAll(lC);
   
      InnerProgress ip1 = new InnerProgress("words1500.txt");  
      Thread t1 = new Thread(ip1);
      t1.start();
      
      InnerProgress ip2 = new InnerProgress("words3000.txt");  
      Thread t2 = new Thread(ip2);
      t2.start();
   
      root.getChildren().addAll(mbar, fpDate, fpRainbow, ip1, ip2);
      scene = new Scene(root, 350, 350);
      stage.setScene(scene);
      stage.show();
   }

   class MyTimerTask extends TimerTask {
      public void run() {
         Platform.runLater(
            new Runnable() {
               @Override
               public void run() {
                  timeLabel.setText(timeFormat.format(new Date()));
               }
            });
      }
   }

   class MyTimerTask2 extends TimerTask {
      public void run() {
         Platform.runLater(
            new Runnable() {
               @Override
               public void run() {
                  if (cnt == 6) {
                     cnt = 0;
                  }
                  for (int i = 0; i < color.length; i++) {
                     if (i == 6) {
                        lC[i].setBackground(
                           new Background(new BackgroundFill(color[i], CornerRadii.EMPTY, Insets.EMPTY)));
                     } else {
                        Color temp = color[i];
                        color[i] = color[i + 1];
                        lC[i].setBackground(
                           new Background(new BackgroundFill(color[i + 1], CornerRadii.EMPTY, Insets.EMPTY)));
                        color[i + 1] = temp;
                     }
                  }
                  cnt++;
               }
            });
      }
   }

   public void showColors() {
      for (int i = 0; i < color.length; i++) {
         lC[i] = new Label("");
         lC[i].setBackground(new Background(new BackgroundFill(color[i], CornerRadii.EMPTY, Insets.EMPTY)));
         lC[i].setPrefWidth(350);
      }
   }

   public void handle(ActionEvent ae) {
      MenuItem mi = (MenuItem) ae.getSource();
      switch(mi.getText()){
         case "Exit":
            System.exit(1);
            break;
         case "Help":
            Alert a = new Alert(AlertType.INFORMATION); 
            a.setTitle("Message");
            a.setContentText("Fun with Timers and Threads\nby: Enrique Oti");
            a.showAndWait();
            break;
      }
   }

   class InnerProgress extends FlowPane implements Runnable {
      private ProgressBar pbBar = new ProgressBar(0);
      private Label lblBar = new Label("");
      private Label lblValue = new Label("0");
      private int number = 0;
      private StackPane stPane = new StackPane();
      private String text = "";
      private String s;
      BufferedReader br;
      long size = 0;
      
      //long totalLength = 1500;
      double lengthPerPercent;
      long readLength = 0;
      
      int counter = 0;
      
      public InnerProgress(String filename){
         this.setPrefWidth(350);
         this.setAlignment(Pos.CENTER);
         this.setPadding(new Insets(10, 0, 0, 0));
         lblBar.setText(filename+" progress:");
         lblBar.setAlignment(Pos.CENTER_LEFT);
         
         pbBar.setPrefWidth(180);
         this.setVgap(8);
         this.setHgap(40);
         stPane.getChildren().addAll(pbBar, lblValue);
         this.getChildren().addAll(lblBar, stPane);
         
         size = new File(filename).length();
         
         lengthPerPercent = 100.0 / size;
         System.out.println(size);
         try {
            br = new BufferedReader(new FileReader(new File(filename)), 17280);
         } catch (Exception e) {
            e.printStackTrace();
         }
       
      }
      
   
   
      
   
      @Override
      public void run() {
      
         lblValue.setText("Opening file...");
         try {
            Thread.sleep((int)(2000));
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
        // }
         try{
            while((s = br.readLine())!=null && continueBool){
               text += s;
            
               try {
                  Thread.sleep((int)(1));
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            
               readLength += s.getBytes("UTF-8").length+2;
               //System.out.println("TOTAL LENGTH: "+readLength);
               final long value = (int) Math.round(readLength * 100 / size);
               //System.out.println("PERCENTAGE: "+ value);
               Platform.runLater(
                  new Runnable() {
                     @Override
                     public void run() {
                        pbBar.setProgress(value/100.0);
                        lblValue.setText(value+"%");
                        
                     }
                  });
            }
            if(continueBool){
               try {
                  Thread.sleep((int)(2000));
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
            Platform.runLater(
                  new Runnable() {
                     @Override
                     public void run() {
                     
                        pbBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                     
                     }
                  });
                  
         
            br.close();
            timerRainbow.cancel();
            continueBool = false;
            
            
         }catch(Exception e){}
      }
   }

}
