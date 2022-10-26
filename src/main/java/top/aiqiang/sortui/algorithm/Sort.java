package top.aiqiang.sortui.algorithm;

import javax.websocket.Session;
import java.io.IOException;

public abstract class Sort {


    public abstract void doStep(int i, Session s) throws IOException;


    public abstract void doRound(int i, Session s) throws IOException, InterruptedException;
    public abstract void doAuto(Double i, Session s) throws IOException, InterruptedException;

}
