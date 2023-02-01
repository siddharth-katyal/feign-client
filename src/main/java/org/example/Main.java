package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        FeignClient feignClient = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(FeignClient.class))
                .logLevel(Logger.Level.FULL)
                .target(FeignClient.class, "https://reqres.in/api/users");

        String option = "";
        label:
        do{
            System.out.println("Select Option:\n1. Find Single User\n2. Display all User\n3. Add new User\n4. Update Existing User\n5. Delete User\n6. Exit");
            Scanner sc = new Scanner(System.in);
            option = sc.next();
            switch (option) {
                case "1":
                    printSingleUser(feignClient);
                    break;
                case "2":
                    printAllUsers(feignClient);
                    break;
                case "3":
                    postUser(feignClient);
                    break;
                case "4":
                    updateUser(feignClient);
                    break;
                case "5":
                    deleteUser(feignClient);
                    break;
                case "6":
                    break label;
                default:
                    System.out.println("please enter valid choice");
                    break;
            }
        }while (true);
    }

     static void printSingleUser(FeignClient feignClient)
    {
        System.out.println("Enter ID of user to search");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        String singleUserAsString="";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SingleUser singleUser = feignClient.findById(id);
            try {
                singleUserAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(singleUser);
            }
            catch(JsonProcessingException e){
                e.printStackTrace();
            }
            System.out.println(singleUserAsString);
        } catch (Exception e)
        {
            System.out.println("Not Found");
        }
    }

    static void printAllUsers(FeignClient feignClient)
    {
        int page = 1;
        int total_pages=1;
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<AllUsers> allUsers = new ArrayList<>();
        ArrayList<String> listAsString = new ArrayList<>();
        try {
            allUsers.add(feignClient.findAll(page));

            try {
                listAsString.add(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(allUsers.get(page-1)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println(listAsString);
            total_pages = allUsers.get(page-1).total_pages;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Press n for next page, p for previous page, e for exit");
        Scanner sc = new Scanner(System.in);
        String input = sc.next();
        while(!input.equals("e"))
        {
            if(input.equals("n"))
            {
                page++;
                if(page<=total_pages)
                {
                    try {
                        allUsers.add(feignClient.findAll(page));

                        try {
                            listAsString.add(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(allUsers.get(page-1)));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        System.out.println(listAsString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    System.out.println("Page Limit Reached");
                }
            }
            else if(input.equals("p"))
            {
                page--;
                if(page>0)
                {
                    try {
                        allUsers.add(feignClient.findAll(page));

                        try {
                            listAsString.add(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(allUsers.get(page-1)));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        System.out.println(listAsString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    System.out.println("Page Limit Reached");
                }
            }
            else
            {
                System.out.println("Enter Valid Input");
            }
            System.out.println("Press n for next page, p for previous page, e for exit");
            input = sc.next();
        }
    }

    static void postUser(FeignClient feignClient)
    {
        System.out.println("Enter Details for User to be added");
        Scanner sc = new Scanner(System.in);
        ObjectMapper objectMapper = new ObjectMapper();
        String createDataResponseAsString = "";
        CreateData createData = new CreateData();
        System.out.println("Enter Name");
        String name = sc.next();
        System.out.println("Enter Job");
        String job = sc.next();
        createData.setName(name);
        createData.setJob(job);
        try {
            CreateDataResponse createDataResponse = feignClient.create(createData);
            try {
                createDataResponseAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createDataResponse);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println(createDataResponseAsString);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static void updateUser(FeignClient feignClient)
    {
        System.out.println("Enter Details for User to be Updated");
        Scanner sc = new Scanner(System.in);
        ObjectMapper objectMapper = new ObjectMapper();
        CreateData updateData = new CreateData();
        System.out.println("Enter Name");
        String name = sc.next();
        System.out.println("Enter Job");
        String job = sc.next();
        updateData.setName(name);
        updateData.setJob(job);
        System.out.println("Enter ID");
        int id = sc.nextInt();
        String updateDataResponseAsString = "";
        try {
            UpdateDataResponse updateDataResponse = feignClient.update(id,updateData);

            try {
                updateDataResponseAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updateDataResponse);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println(updateDataResponseAsString);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static void deleteUser(FeignClient feignClient)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter ID of user to be deleted");
        int id = sc.nextInt();
        try {
            feignClient.delete(id);
            System.out.println("Deleted");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}