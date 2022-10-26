package top.aiqiang.sortui.algorithm;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;

public class BubbleSort extends Sort {
    ArrayList<int[]> stepList = new ArrayList<>();
    int sum = 0;
    int checkSum = -1;
    int key[];
    int length = 0;

    public BubbleSort(int[] nums) {
        key = new int[nums.length];
        length = nums.length;
        bulleSort(nums, nums.length);
    }

    public int[] bulleSort(int[] arr, int length) {
        boolean checkIfChange = false;
        if (length == 0) {
            this.stepList.add(arr);
            return arr;
        }
        int finger = 0;
        int temp = 0;
        //4,3,1,5,6,8,9
        //0]1]2]3]4]5]6]
        while (finger < arr.length - 1) {
            int compare = finger + 1;
            if (arr[finger] > arr[compare]) {
                temp = arr[compare];
                arr[compare] = arr[finger];
                arr[finger] = temp;
                finger = compare;
                checkIfChange = true;
            } else {
                finger = compare;
            }
        }
        if (checkIfChange == false) {
            return arr;
        }
        int sortArr[] = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            sortArr[i] = arr[i];
        }
        stepList.add(sortArr);
//        for (int i : arr) {
//            System.out.print(i + ",");
//        }
//        System.out.println("");
        return bulleSort(arr, length - 1);
    }

    @Override
    public void doStep(int i, Session s) throws IOException {
        for (int arrS[] : stepList) {
            for (int i1 : arrS) {
                System.out.print(i1 + ",");
            }
            System.out.println("");
        }
        System.out.println("sum=" + sum);
//        for (int i : arr) {
//            System.out.print(i + ",");
//        }
//        System.out.println("");

//        this.key[sum]=stepList.get(sum)[length-1-sum];
//        this.key[stepList.get(sum)[length-1-sum]-1]=1;
        this.key[length - 1 - sum] = 1;
        s.getBasicRemote().sendText(this.toString());

    }

    @Override
    public String toString() {
        StringBuilder arrS = new StringBuilder();
        for (int i1 : stepList.get(sum)) {
            arrS.append(i1 + ",");
        }
        arrS.deleteCharAt(arrS.length() - 1);
        StringBuilder keyS = new StringBuilder();
        for (int key1 : key) {
            keyS.append(key1 + ",");
        }
        keyS.deleteCharAt(keyS.length() - 1);
        String returnResult = arrS + ";" + keyS;
        System.out.println(returnResult);

        if (sum < stepList.size() - 1) {
            sum += 1;
        }
        checkSum += 1;
        if (checkSum == stepList.size()) {
            returnResult = ";;结束";
        }
        return returnResult;
    }

    @Override
    public void doRound(int i, Session s) throws IOException {
    }

    @Override
    public void doAuto(Double i, Session s) throws IOException, InterruptedException {
        System.out.println("checkSum=" + checkSum);
        System.out.println("checksum判断" + (checkSum < stepList.size()));
        while (checkSum < stepList.size()) {
            System.out.println("checkSum=" + checkSum);
            System.out.println("checksum判断" + (checkSum == stepList.size()));
            doStep(1, s);
            Thread.sleep(Long.parseLong(String.valueOf(i * 1000).split("\\.")[0]));
        }
    }

}
