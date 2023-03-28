package ma.enset.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Chat;
import ma.enset.stubs.chatServiceGrpc;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    static chatServiceGrpc.chatServiceStub asyStub;
    static String userName;

    public static void login(){
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",2222)
                .usePlaintext()
                .build();
        asyStub= chatServiceGrpc.newStub(managedChannel);
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter your name : ");
        userName=myObj.nextLine();
        Chat.connect requestLogin= Chat.connect.newBuilder()
                .setUserName(userName)
                .build();
        asyStub.login(requestLogin, new StreamObserver<Chat.messageResponse>() {

            @Override
            public void onNext(Chat.messageResponse messageResponse) {
                System.out.println();
                System.out.println(messageResponse);
                System.out.println();
            }
            @Override
            public void onError(Throwable throwable) {
            }
            @Override
            public void onCompleted() {
            }
        });
    }

    public static void send(){
        Scanner myObj = new Scanner(System.in);
        while (true){
            String content=myObj.nextLine();
            String[] message =content.split("=>");
            Chat.messageRequest request= Chat.messageRequest.newBuilder()
                    .setFrom(userName)
                    .setTo(message[0])
                    .setMessage(message[1])
                    .build();
                asyStub.sendTo(request, new StreamObserver<Chat.messageResponse>() {
                    @Override
                    public void onNext(Chat.messageResponse messageResponse) {
                    }
                    @Override
                    public void onError(Throwable throwable) {
                    }
                    @Override
                    public void onCompleted() {
                    }
                });
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        login();
        Thread.sleep(2500);
        send();
        System.in.read();
    }
}