package com.example.kobi;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

//import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;

import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;


public class HelloController {
    private ConstraintsTable mainTable=new ConstraintsTable();
    private ArrayList<Employee> employees=new ArrayList<>();
    private Scheduling schedul=new Scheduling();

    ArrayList<CheckBox> shiftHead=new ArrayList();
    ArrayList<Integer> notSent=new ArrayList();
    ArrayList<Button> watchButton=new ArrayList();

    @FXML
    private GridPane gridPaneSetting;
    @FXML
    private GridPane gridPaneStatus;
    @FXML
    private TextArea sendMessage;
    @FXML
    private GridPane gridPaneScheduling;
    @FXML
    private Button buttonSch;
    @FXML
    private Button buttonClear;

    @FXML
    public void initialize() {
        readSettingFile();
        //  readConstFile();
        openExel();
        updateApp();
        preScheduling();
    }

    public void readSettingFile(){
        int newCode;
        String newName;

        try {
            File myObj = new File("target/classes/setting.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                newCode = myReader.nextInt();
                newName = myReader.next()+" "+myReader.next();
                int[] newAbility=new int[4];
                newAbility[0]= myReader.nextInt();
                newAbility[1]= myReader.nextInt();
                newAbility[2]= myReader.nextInt();
                newAbility[3]= myReader.nextInt();
                employees.add(new Employee(newCode,newName,newAbility));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public void updateApp(){

        for (int i=0; i<7;i++) { //update the dates for the next week

            LocalDate day = java.time.LocalDate.now().plusDays(7);
            day = day.minusDays(LocalDate.now().getDayOfWeek().getValue());

            Label date=new Label(day.toString());
            date.setMaxWidth(Double.MAX_VALUE);
            date.setAlignment(Pos.CENTER);
            gridPaneScheduling.add(date,6-i,2);

        }
        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(110d / employees.size());
        RowConstraints rc2 = new RowConstraints();
        rc2.setPercentHeight(150d / employees.size());

        for (int i=0;i<employees.size();i++){
            //Sent
            Label tempName=new Label(employees.get(i).name);

            tempName.setStyle("-fx-font-weight: bold;");
            tempName.setMaxWidth(Double.MAX_VALUE);
            tempName.setAlignment(Pos.CENTER_RIGHT);

            gridPaneStatus.add(tempName,3,i);
            gridPaneStatus.getRowConstraints().add(rc2);
            Button wahtcConst=new Button("צפה באילוצים");

            wahtcConst.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event)  {
                    int index=findEmployeeByName(tempName.getText());
                    GridPane personalConst = new GridPane();
                    StackPane secondaryLayout = new StackPane();
                    secondaryLayout.getChildren().add(personalConst);
                    for (int i=0; i<7; i++){
                        Label day=new Label();
                        if (i==0)
                             day.setText("ראשון");
                        if (i==1)
                            day.setText("שני");
                        if (i==2)
                            day.setText("שלישי");
                        if (i==3)
                            day.setText("רביעי");
                        if (i==4)
                            day.setText("חמישי");
                        if (i==5)
                            day.setText("שישי");
                        if (i==6)
                            day.setText("שבת");

                        day.setMaxWidth(Double.MAX_VALUE);
                        day.setAlignment(Pos.CENTER);
                        day.setStyle("-fx-font-weight: bold;");
                        personalConst.add(day,7-i,0);
                    }
                    Label shift=new Label("יום");
                    shift.setMaxWidth(Double.MAX_VALUE);
                    shift.setAlignment(Pos.CENTER);
                    shift.setStyle("-fx-font-weight: bold;");
                    personalConst.add(shift,8,1);
                    shift=new Label("לילה");
                    shift.setMaxWidth(Double.MAX_VALUE);
                    shift.setAlignment(Pos.CENTER);
                    shift.setStyle("-fx-font-weight: bold;");
                    personalConst.add(shift,8,2);

                    for(int i = 0; i < 2; ++i) {
                        for(int j = 0; j < 7; ++j) {
                            if(employees.get(index).constraints[i][j]==1){
                                Label canWork=new Label("יכול");
                                canWork.setMaxWidth(Double.MAX_VALUE);
                                canWork.setAlignment(Pos.CENTER);
                                //  canWork.setStyle("-fx-font-weight: bold;");
                                canWork.setTextFill(Color.BLUE);
                                personalConst.add(canWork,7-j,i+1);
                            }
                        }
                    }

                    personalConst.setHgap(15);
                    personalConst.setVgap(15);
                    personalConst.setGridLinesVisible(true);
                    personalConst.setAlignment(Pos.CENTER);

                    Label title=new Label("אילוצים - "+tempName.getText());
                    //title.setPadding(new Insets(20, 0, 40, 0));
                    title.setMaxWidth(Double.MAX_VALUE);
                    title.setAlignment(Pos.TOP_CENTER);

                    secondaryLayout.getChildren().add(title);

                    Scene secondScene = new Scene(secondaryLayout, 550, 350);
                    Stage newWindow = new Stage();
                    newWindow.getIcons().add(new Image("file:src/main/resources/com/example/kobi/icon2.jpg"));

                    newWindow.setTitle("אילוצים - "+tempName.getText());
                    newWindow.setScene(secondScene);
                    newWindow.show();
                }
            });


            Label tempSend=new Label();
            if(employees.get(i).sentTime.equals("0")){
                tempSend.setText("לא הגיש");
                tempSend.setTextFill(Color.RED);
                wahtcConst.setDisable(true);
                notSent.add(i);
            } else{
                tempSend.setText("הגיש");
                tempSend.setTextFill(Color.GREEN);
                Label tempSentTime=new Label(employees.get(i).sentTime);
                tempSentTime.setMaxWidth(Double.MAX_VALUE);
                tempSentTime.setAlignment(Pos.CENTER_RIGHT);
                gridPaneStatus.add(tempSentTime,1,i);
            }

            tempSend.setStyle("-fx-font-weight: bold;");
            gridPaneStatus.add(tempSend,2,i);
            gridPaneStatus.add(wahtcConst,0,i);
            watchButton.add(wahtcConst);

            //Setting
            Label tempCde=new Label(String.valueOf(employees.get(i).code));
            gridPaneSetting.add(tempCde,6,i);
            Label tempName2=new Label(employees.get(i).name);

            tempName2.setMaxWidth(Double.MAX_VALUE);
            tempName2.setAlignment(Pos.CENTER_RIGHT);//////////////////////????

            tempName2.setStyle("-fx-font-weight: bold;");
            gridPaneSetting.add(tempName2,4,i);
            gridPaneSetting.getRowConstraints().add(rc);

            CheckBox tempShiftHead=new CheckBox("רמש");
            if(employees.get(i).ability[0]==1)
                tempShiftHead.setSelected(true);
            tempShiftHead.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            gridPaneSetting.add(tempShiftHead,3,i);
            shiftHead.add(tempShiftHead);

            CheckBox tempControl=new CheckBox("בקרה");
            if(employees.get(i).ability[1]==1)
                tempControl.setSelected(true);
            tempControl.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            gridPaneSetting.add(tempControl,2,i);

            CheckBox tempSecurityGuard=new CheckBox("מאבטח");
            if(employees.get(i).ability[2]==1)
                tempSecurityGuard.setSelected(true);
            tempSecurityGuard.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            gridPaneSetting.add(tempSecurityGuard,1,i);

            CheckBox tempDriver=new CheckBox("נהג");
            if(employees.get(i).ability[3]==1)
                tempDriver.setSelected(true);
            tempDriver.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            gridPaneSetting.add(tempDriver,0,i);
        }

        if(notSent.size()>0){
            String notSentMassage="";
            for(int i=0; i< notSent.size();i++)
                notSentMassage=notSentMassage+employees.get(notSent.get(i)).name+", ";
            sendMessage.setText(notSentMassage+"להגיש בבקשה אילוצים");
        }

        Label[][] labelSche=new Label[7][6];
        for(int i=0;i<7;i++)
            for(int j=0;j<6;j++)
            {
                labelSche[i][j]=new Label();
                gridPaneScheduling.add(labelSche[i][j],6-i,j+2);
            }

    }

    public void readConstFile(){
        String tempTime;
        String empName;
        String tempString;
        int index;
        try {
            File myObj = new File("target/classes/const.csv");
            Scanner myReader = new Scanner(myObj);
            myReader.useDelimiter(",");
            while (myReader.hasNext()) {
                tempTime=myReader.next();
                tempTime=tempTime.replace("\n", "").replace("\r", "");
                if(!myReader.hasNext())
                    break;
                empName=myReader.next();
                index=findEmployeeByName(empName);
                employees.get(index).sentTime=tempTime;
                for(int i=0; i<6; i+=1) {
                    employees.get(index).constraints[0][i] = myReader.nextInt();
                    employees.get(index).constraints[1][i] = myReader.nextInt();
                }
                employees.get(index).constraints[0][6] = Integer.parseInt(myReader.next());
                myReader.useDelimiter("\r");
                tempString=myReader.next();
                employees.get(index).constraints[1][6]  = Integer.parseInt(String.valueOf(tempString.charAt(1)));
                myReader.useDelimiter(",");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public int findEmployeeByName(String name){
        for (int i=0; i<employees.size(); i++)
            if (name.equalsIgnoreCase(employees.get(i).name))
                return i;
        return -1;
    }

    public int findEmployeeByCode(int code){
        for (int i=0; i<employees.size(); i++)
            if (code==employees.get(i).code)
                return i;
        return -1;
    }

    public void preScheduling(){
        //update main constraints table
        for (int i=0; i<employees.size();i++){
            employees.get(i).setConstraints(employees.get(i).constraints);
            mainTable.set(employees.get(i).code, employees.get(i).expandConstraints);
        }
        //shuffle
        mainTable.shuffle();
    }

    @FXML
    void onButtonClearClick(ActionEvent event) {
        readConstFile();
        updateApp();
        preScheduling();
        gridPaneScheduling.getChildren().clear();
    }

    public void onButtonClea66Click(ActionEvent event) {
        readConstFile();
        updateApp();
        preScheduling();
        gridPaneScheduling.getChildren().clear();
    }

    @FXML
    void onButtonSchClick(ActionEvent event) {
        int[] coor=new int[2];
        int indx;
        coor=mainTable.findMin();
        while(mainTable.table[coor[0]][coor[01]].size()==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("שים לב");
            alert.setHeaderText(null);
            alert.setContentText("אין מספיק אילוצים לשיבוץ מלא");
            alert.showAndWait();

            mainTable.hasScheduled[coor[0]][coor[01]]=true;
            coor=mainTable.findMin();
        }
        recursiveScheduling();

    }

    public boolean recursiveScheduling(){
        int[] minCoor, nextMinCoor =new int[2];
        int indx;// the employee index at the employees list
        int employeeCode;
        int chooseIndexTry;

        minCoor=mainTable.findMin();
        mainTable.chooseIndexTry[minCoor[0]][minCoor[01]]++; //try first/second/etc.
        chooseIndexTry=mainTable.chooseIndexTry[minCoor[0]][minCoor[01]];

        employeeCode=mainTable.table[minCoor[0]][minCoor[01]].get(chooseIndexTry);
        indx=findEmployeeByCode(employeeCode);
        schedul.schedulingTable[minCoor[0]][minCoor[01]]=employeeCode;
        ArrayList<int[]> unableIndex=mainTable.unableEmployee(employeeCode,minCoor);
        mainTable.hasScheduled[minCoor[0]][minCoor[1]]=true;

        nextMinCoor =mainTable.findMin();
        if(nextMinCoor[0]==-1){
            Label nameSch=new Label(employees.get(indx).name);
            nameSch.setMaxWidth(Double.MAX_VALUE);
            nameSch.setAlignment(Pos.CENTER);
            gridPaneScheduling.add(nameSch,6-minCoor[1],minCoor[0]+3);
            return true;
        }

        if(mainTable.table[nextMinCoor[0]][nextMinCoor[01]].size()>0){ //possible schedule the next

            Label nameSch=new Label(employees.get(indx).name);
            nameSch.setMaxWidth(Double.MAX_VALUE);
            nameSch.setAlignment(Pos.CENTER);
            gridPaneScheduling.add(nameSch,6-minCoor[1],minCoor[0]+3);

            if(recursiveScheduling())
                return true;
            else {

                mainTable.hasScheduled[minCoor[0]][minCoor[1]]=false;
                mainTable.cancelUnableEmployee(employeeCode,  unableIndex);

                if (mainTable.table[minCoor[0]][minCoor[01]].size()<=chooseIndexTry+1)
                    return false;
                else{
                    // mainTable.chooseIndexTry[minCoor[0]][minCoor[01]]++; //try first/second/etc.
                    //chooseIndexTry=mainTable.chooseIndexTry[minCoor[0]][minCoor[01]];
                    return recursiveScheduling();
                }

            }
        }
        else  //try the next index in the list
            return false;

    }

    public void openExel(){
        String tempHour;
        String empName;
        String tempDate;
        int index;

        try
        {
            FileInputStream file = new FileInputStream(new File("target/classes/const.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            Row row = rowIterator.next();
            while (rowIterator.hasNext())
            {
                row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    empName=cell.toString();
                    index=findEmployeeByName(empName);
                    tempDate= relativeDate(cellIterator.next().getDateCellValue());
                    tempHour=cellIterator.next().toString();
                    employees.get(index).sentTime=tempDate+tempHour;

                    for(int i=0; i<7; i+=1) {
                        employees.get(index).constraints[0][i] =
                                Integer.parseInt(cellIterator.next().toString());
                        employees.get(index).constraints[1][i] =
                                Integer.parseInt(cellIterator.next().toString());
                    }
                }
            }
            file.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
    public static String relativeDate(Date SendDate){
        LocalDate today = LocalDate.now();
        LocalDate a= new java.sql.Date(SendDate.getTime()).toLocalDate();

        int diff = (int) ChronoUnit.DAYS.between(a,today);
        if(diff==0)
            return "היום, ";
        if(diff==1)
            return "אתמול, ";
        if(diff==2)
            return "שלשום, ";
        return "לפני "+diff + " ימים, ";

    }

    public void aaa(final Stage primaryStage) {
        Button button = new Button();
        button.setText("Open a New Window");

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                Label secondLabel = new Label("I'm a Label on new Window");

                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);

                Scene secondScene = new Scene(secondaryLayout, 230, 100);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Second Stage");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
               // newWindow.setX(primaryStage.getX() + 200);
               // newWindow.setY(primaryStage.getY() + 100);

                newWindow.show();
            }
        });
        StackPane root = new StackPane();
        root.getChildren().add(button);

        Scene scene = new Scene(root, 450, 250);

        primaryStage.setTitle("JavaFX Open a new Window (o7planning.org)");
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}






