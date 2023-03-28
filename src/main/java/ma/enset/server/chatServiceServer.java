package ma.enset.server;

import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Chat;
import ma.enset.stubs.chatServiceGrpc;
import java.util.HashMap;
import java.util.Map;

public class chatServiceServer extends chatServiceGrpc.chatServiceImplBase{
    Map<String ,StreamObserver<Chat.messageResponse>> userList=new HashMap<>();
    @Override
    public void login(Chat.connect request, StreamObserver<Chat.messageResponse> responseObserver) {
        System.out.println("user "+request.getUserName()+" is connected");
        String userName=request.getUserName();
        userList.put(userName,responseObserver);
        Chat.messageResponse response = Chat.messageResponse.newBuilder()
                .setFrom("server")
                .setMessage("Hello "+userName+" welcome to chatPlaform")
                .build();
        responseObserver.onNext(response);
    }

    @Override
    public void sendTo(Chat.messageRequest request, StreamObserver<Chat.messageResponse> responseObserver) {
        String from=request.getFrom();
        String to=request.getTo();
        String message=request.getMessage();
        Chat.messageResponse response = Chat.messageResponse.newBuilder()
                .setFrom(from)
                .setMessage(message)
                .build();
        if(userList.containsKey(to)){
            userList.get(to).onNext(response);
        }else {
            for (String name : userList.keySet()) {
                if(!name.equals(from)){
                    userList.get(name).onNext(response);
                }
            }
        }
    }
}
