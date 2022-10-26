package top.aiqiang.sortui.controllers;

import org.springframework.stereotype.Component;
import top.aiqiang.sortui.algorithm.*;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/{name}")
public class WebSocketServer {

    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的用户名
     */
    private String name;

    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<String, Sort> sortSet = new ConcurrentHashMap<>();


    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "name") String name) {
        this.session = session;
        this.name = name;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分

        System.out.println("[WebSocket] 连接成功，当前连接人数为：" + sortSet.size());
    }


    @OnClose
    public void OnClose() {
        sortSet.remove(this.name);
        System.out.println("[WebSocket] 退出成功，当前连接人数为：" + sortSet.size());
    }

    @OnMessage
    public void OnMessage(String message) throws IOException, InterruptedException {
        System.out.println("[WebSocket] 收到消息:" + message);
        String[] n = message.split(",");
        if (n[0].equals("new")) {
            sortSet.remove(this.name);
            int[] nums = new int[n.length - 2];
            for (int i = 0; i < n.length - 2; i++) {
                nums[i] = Integer.parseInt(n[i + 2]);
            }
            switch (n[1]) {
                case "quick":
                    Sort sort1 = new QuickSort(nums);
                    sortSet.put(name, sort1);
                    break;
                case "bubble":
                    Sort sort2 = new BubbleSort(nums);
                    sortSet.put(name, sort2);
                    break;
                case "select":
                    Sort sort3 = new SelectSort(nums);
                    sortSet.put(name, sort3);
                    break;
                case "insert":
                    Sort sort4 = new InsertSort(nums);
                    sortSet.put(name, sort4);
                    break;
                case "shell":
                    Sort sort5 = new ShellSort(nums);
                    sortSet.put(name, sort5);
                    break;

            }

        } else if (n[0].equals("doStep")) {
            sortSet.get(this.name).doStep(1, session);
        } else if (n[0].equals("doRound")) {
            sortSet.get(this.name).doRound(1, session);
        }
        else if (n[0].equals("doAuto")) {
            Double speed = Double.parseDouble(n[1]);
            sortSet.get(this.name).doAuto(speed, session);
            this.session.getBasicRemote().sendText(String.valueOf(sortSet.get(this.name)));
        }


    }


}
