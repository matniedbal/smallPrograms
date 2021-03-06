package personnelInfo;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import personnelInfo.mechanics.Company;
import personnelInfo.mechanics.Encrypting;
import personnelInfo.mechanics.Person;
import personnelInfo.mechanics.SortPersonType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class PersonnelController {


    @FXML
    public TextField additionalSearchTextField;
    @FXML
    public VBox vBoxWithWorkers;
    @FXML
    public ChoiceBox<String> workerStatusChoiceBox;
    @FXML
    public Font x2;
    @FXML
    public Font x1;
    @FXML
    public TextField encryptMoveField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField positionTextField;
    @FXML
    private TextField workersIdTextField;
    @FXML
    private TextField companyNameTextField;
    @FXML
    private TextField numberOfWorkersTextField;
    @FXML
    private ChoiceBox<String> sortByChoiceBox;
    @FXML
    private ChoiceBox<String> workersTypeShowChoiceBox;
    private List<WorkerField> buttonsWithWorkers;
    private Company company;
    private Person actualPerson;
    private Button actualWorkerButton;
    private int encryptMove;

    public PersonnelController() {
    }

    @FXML
    private void initialize(){
        initializeChoiceBoxes();
    }

    private void initializeChoiceBoxes(){
        ObservableList<String> sortByChoiceBoxList = FXCollections
                .observableArrayList(
                        SortType.SORT_BY_ID.toString(),SortType.SORT_BY_ID_REV.toString(),
                        SortType.SORT_BY_NAME.toString(),SortType.SORT_BY_NAME_REV.toString(),
                        SortType.SORT_BY_SURNAME.toString(),SortType.SORT_BY_SURNAME_REV.toString(),
                        SortType.SORT_BY_AGE.toString(),SortType.SORT_BY_AGE_REV.toString(),
                        SortType.SORT_BY_POSITION.toString(),SortType.SORT_BY_POSITION_REV.toString());

        ObservableList<String> workersTypeShowChoiceBoxList = FXCollections
                .observableArrayList(
                        WorkersType.ACTUAL_WORKER.toString(),
                        WorkersType.ACTUAL_AND_REMOVED.toString(),
                        WorkersType.REMOVED_WORKER.toString());

        ObservableList<String> workersTypeList = FXCollections
                .observableArrayList(
                        WorkersType.ACTUAL_WORKER.toString(),
                        WorkersType.REMOVED_WORKER.toString());

        sortByChoiceBox.setValue(SortType.SORT_BY_ID.toString());
        sortByChoiceBox.setItems(sortByChoiceBoxList);
        workersTypeShowChoiceBox.setValue(WorkersType.ACTUAL_WORKER.toString());
        workersTypeShowChoiceBox.setItems(workersTypeShowChoiceBoxList);
        workerStatusChoiceBox.setValue(WorkersType.ACTUAL_WORKER.toString());
        workerStatusChoiceBox.setItems(workersTypeList);
    }


    @FXML
    private void confirmAndNext(ActionEvent actionEvent) {
        try {
            actualPerson.setAGE(Integer.parseInt(ageTextField.getText()));
            actualPerson.setNAME(nameTextField.getText());
            actualPerson.setSURNAME(surnameTextField.getText());
            actualPerson.setPosition(positionTextField.getText());
            actualPerson.setWorkerType(workerType(workerStatusChoiceBox.getValue().trim()));
            actualWorkerButton.setText(actualPerson.print());
        }catch(Exception ex){
            System.out.println("error");
        }
    }

    private WorkersType workerType(String value){
        if ("REMOVED_WORKER".equals(value)) {
            return WorkersType.REMOVED_WORKER;
        }else if ("ACTUAL_WORKER".equals(value)) {
            return WorkersType.ACTUAL_WORKER;
        }else return null;
    }

    @FXML
    private void acceptCompany(ActionEvent actionEvent) {
        try {
            vBoxWithWorkers.getChildren().clear();
            company = new Company(companyNameTextField.getText(), Integer.parseInt(numberOfWorkersTextField.getText()));
            buttonsWithWorkers = new LinkedList<>();
            for (int i = 0; i < company.getListOfWorkers().size(); i++) {
                buttonsWithWorkers.add(new WorkerField(company.getListOfWorkers().get(i)));
                workerButton(buttonsWithWorkers.get(i).getButton(), i);
                vBoxWithWorkers.getChildren().add(buttonsWithWorkers.get(i).getButton());
            }
        } catch (Exception ex) {
            System.out.println("error");
        }
    }

    private void workerButton(Button button, int i) {
        button.setPrefSize(430, 30);
        button.setOnAction(eventHandler -> {
            setTextFields(i);
            actualPerson = buttonsWithWorkers.get(i).getPerson();
            actualWorkerButton = buttonsWithWorkers.get(i).getButton();
        });
        buttonsWithWorkers.get(i).getButton().setText(company.getListOfWorkers().get(i).print());
        button.setWrapText(true);
    }

    private void setTextFields(int i) {
       nameTextField.setText(company.getListOfWorkers().get(i).getNAME());
       surnameTextField.setText(company.getListOfWorkers().get(i).getSURNAME());
       ageTextField.setText(String.valueOf(company.getListOfWorkers().get(i).getAGE()));
        positionTextField.setText(company.getListOfWorkers().get(i).getPosition());
        workersIdTextField.setText(String.valueOf(company.getListOfWorkers().get(i).getID()));
        workerStatusChoiceBox.setValue(company.getListOfWorkers().get(i).getWorkerType().toString());
    }

    @FXML
    private void addWorker(ActionEvent actionEvent) {
        try {
            company.addWorker();
            refreshWorkerButtons();
        }catch(Exception ex) {
            System.out.println("error");
        }
    }

    @FXML
    private void removeWorker(ActionEvent actionEvent) {
        try {
            company.removeWorker(actualPerson.getID());
            refreshWorkerButtons();
        }catch(Exception ex) {
            System.out.println("error");
        }
    }

    @FXML
    private void searchWorkers(ActionEvent actionEvent) {
        try {
            sort(sortByChoiceBox.getValue());
            refreshWorkerButtons();
        }catch(Exception ex) {
            System.out.println("error");
        }
    }


    private void refreshWorkerButtons(){

        try {
            vBoxWithWorkers.getChildren().clear();
            buttonsWithWorkers = new LinkedList<>();
            int counter = 0;
            for(int i = 0;i < company.getListOfWorkers().size(); i++) {

                if((returnWorkersType(workersTypeShowChoiceBox.getValue()) == company.getListOfWorkers().get(i).getWorkerType()
                        || returnWorkersType(workersTypeShowChoiceBox.getValue()) == WorkersType.ACTUAL_AND_REMOVED)
                        && (company.getListOfWorkers().get(i).print().toLowerCase().contains(additionalSearchTextField.getText()))) {

                    buttonsWithWorkers.add(new WorkerField(company.getListOfWorkers().get(i)));
                    buttonsWithWorkers.get(counter).getButton().setPrefSize(430, 30);
                    int finalCounter = counter;
                    buttonsWithWorkers.get(counter).getButton().setOnAction(eventHandler -> {
                        setTextFields(finalCounter);
                        actualPerson = buttonsWithWorkers.get(finalCounter).getPerson();
                        actualWorkerButton =buttonsWithWorkers.get(finalCounter).getButton();
                    });
                    buttonsWithWorkers.get(counter).getButton().setText(company.getListOfWorkers().get(i).print());
                    vBoxWithWorkers.getChildren().add(buttonsWithWorkers.get(counter).getButton());
                    counter++;
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    private void sort(String value){
        switch(value){
            case "SORT_BY_ID":
                company.sort(SortPersonType.ID,1);
                break;
            case "SORT_BY_NAME":
                company.sort(SortPersonType.NAME,1);
                break;
            case "SORT_BY_SURNAME":
                company.sort(SortPersonType.SURNAME,1);
                break;
            case "SORT_BY_AGE":
                company.sort(SortPersonType.AGE,1);
                break;
            case "SORT_BY_POSITION":
                company.sort(SortPersonType.POSITION,1);
                break;
            case "SORT_BY_ID_REV":
                company.sort(SortPersonType.ID,-1);
                break;
            case "SORT_BY_NAME_REV":
                company.sort(SortPersonType.NAME,-1);
                break;
            case "SORT_BY_SURNAME_REV":
                company.sort(SortPersonType.SURNAME,-1);
                break;
            case "SORT_BY_AGE_REV":
                company.sort(SortPersonType.AGE,-1);
                break;
            case "SORT_BY_POSITION_REV":
                company.sort(SortPersonType.POSITION,-1);
            default:
                company.sort(SortPersonType.ID,1);
                break;
        }
    }

    private WorkersType returnWorkersType(String value){
        switch (value){
            case"ACTUAL_WORKER" : return WorkersType.ACTUAL_WORKER;
            case"ACTUAL_AND_REMOVED" : return WorkersType.ACTUAL_AND_REMOVED;
            case"REMOVED_WORKER":return WorkersType.REMOVED_WORKER;
            default: return null;
        }
    }

    @FXML
    private void menuItemNewCompany(ActionEvent actionEvent) {
        clearEverything();
    }

    private void clearEverything() {
        buttonsWithWorkers.clear();
        vBoxWithWorkers.getChildren().clear();
        company.clear();
        surnameTextField.setText("");
        nameTextField.setText("");
        additionalSearchTextField.setText("");
        surnameTextField.setText("");
        ageTextField.setText("");
        positionTextField.setText("");
        workersIdTextField.setText("");
        companyNameTextField.setText("");
        numberOfWorkersTextField.setText("");
        refreshWorkerButtons();
    }

    @FXML
    private void menuItemClose(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    private void menuItemLoad(ActionEvent actionEvent) {
        try {
            encryptMove = Integer.parseInt(encryptMoveField.getText());

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PCSI files (*.pcsi)", "*.pcsi");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(new Stage());
            List<String[]> temp = new LinkedList<>();
            if (file != null) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    if(scanner.hasNextLine())temp.add(scanner.nextLine().split(";"));
                }
            }
            companyNameTextField.setText(Encrypting.decrypt(temp.get(0)[0].trim(),encryptMove));
            numberOfWorkersTextField.setText(temp.get(1)[0].trim());
            acceptCompany(new ActionEvent());

            System.out.println(temp.size());

            for (int i =2 ; i < temp.size()-1; i++) {

                company.getListOfWorkers().get(i-2).setNAME(Encrypting.decrypt(temp.get(i)[1],encryptMove));
                company.getListOfWorkers().get(i-2).setSURNAME(Encrypting.decrypt(temp.get(i)[2], encryptMove));
                company.getListOfWorkers().get(i-2).setAGE(Integer.parseInt(temp.get(i)[3].trim()));
                company.getListOfWorkers().get(i-2).setPosition(Encrypting.decrypt(temp.get(i)[4],encryptMove));
                company.getListOfWorkers().get(i-2).setWorkerType(returnWorkersType(Encrypting.decrypt(temp.get(i)[5],encryptMove)));
            }

            refreshWorkerButtons();
        } catch (NumberFormatException e) {
            System.out.println("No encrypt movement");
        } catch (FileNotFoundException e) {
            System.out.println("File not found error");
        }
    }



    @FXML
    private void menuItemSave(ActionEvent actionEvent) {
       try {
           encryptMove = Integer.parseInt(encryptMoveField.getText());
           company.sort(SortPersonType.ID,1);
           if (company != null) {
               FileChooser fileChooser = new FileChooser();
               FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PCSI files (*.pcsi)", "*.pcsi");
               fileChooser.getExtensionFilters().add(extFilter);
               File file = fileChooser.showSaveDialog(new Stage());
               if (file != null) {
                   saveTextToFile(Encrypting.encrypt(company.toString(),encryptMove), file);
               }
           }
       }catch(Exception ex){
           System.out.println("Error");
       }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void menuItemInformation(ActionEvent actionEvent) {
        System.out.println("Made by Mateusz Niedbał");
    }

    public void renameCompany(ActionEvent actionEvent) {

        try {
            company.setName(companyNameTextField.getText());
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
