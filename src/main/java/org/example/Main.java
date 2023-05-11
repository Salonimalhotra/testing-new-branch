package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Main {
    public static void main(String[] args) throws Exception{
        System.out.println("Hello world!");
        //Credential c = authorize();
        String spreadsheetId = "1JClPUu9QnIPCw4iN7DrdN67Dh-SXFni_4gzt49psVhc";
        //String name = "sheet2";
        //String range = "A1:B9";
        downloadData(spreadsheetId);
        }

    private static Credential authorize() throws Exception {
        String credentialLocation = System.getProperty("user.dir") + "/credentials.json";
        System.out.println(credentialLocation);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new FileReader(credentialLocation));

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow
                .Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File(System.getProperty("user.dir"))))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(googleAuthorizationCodeFlow, new LocalServerReceiver()).authorize("user");
    }

    public static void downloadData(String spreadSheetId) throws Exception {
        //Sheets sheet = new Sheets(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), authorize());
        Sheets service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), authorize())
                .setApplicationName("TestApp")
                .build();

//        List<List<Object>> data = service.spreadsheets().values()
//                .get(spreadSheetId, sheetName)
//                .execute().getValues();

        Spreadsheet spreadsheet = service.spreadsheets().get(spreadSheetId).execute();

        List<Sheet> sheets = spreadsheet.getSheets();
        List<String> sheetNames = new ArrayList<>();
        for (Sheet s : sheets) {
            sheetNames.add(s.getProperties().getTitle());
        }
        Workbook workbook = new XSSFWorkbook();
        for (String sheetName : sheetNames) {
            String range = sheetName + "!A1:ZZ";
            ValueRange response = service.spreadsheets().values().get(spreadSheetId,range).execute();
            List<List<Object>> values = response.getValues();
            if (values == null || values.isEmpty()) {
                System.out.println("No data found in sheet " + sheetName);
            } else {
                workbook.createSheet(sheetName);
                int rowNum = 0;
                for (List<Object> row : values) {
                    workbook.getSheet(sheetName).createRow(rowNum++);
                    int colNum = 0;
                    for (Object cellValue : row) {
                        workbook.getSheet(sheetName).getRow(rowNum - 1).createCell(colNum++).setCellValue(cellValue.toString());
                    }
                }
            }
        }
        FileOutputStream outputStream = new FileOutputStream("Output/output.xlsx");
        workbook.write(outputStream);
        workbook.close();
        System.out.println("Workbook downloaded and saved successfully.");

        GithubRepoCreator repo = new GithubRepoCreator("https://github.com/Salonimalhotra/testing.git","new-branch","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");
        repo.uploadFile("Changing excel");

        //return convertToArray(data);
//        GithubUpload x=new GithubUpload();
//        x.push();
    }
    private static String[][] convertToArray(List<List<Object>> data){
        String[][] array = new String[data.size()][];

        int i = 0;
        for (List<Object> row : data) {
            array[i++] = row.toArray(new String[row.size()]);
        }
        return array;
    }

    }