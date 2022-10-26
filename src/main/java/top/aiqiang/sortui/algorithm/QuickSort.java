package top.aiqiang.sortui.algorithm;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Stack;

public class QuickSort extends Sort {

    int[] nums;
    boolean[] keys;
    Stack<Integer> st;

    @Override
    public String toString() {
        String res = "";

        if (this.nums.length > 0) {
            for (int i :
                    this.nums) {
                res += i + ",";
            }
            res = res.substring(0, res.length() - 1);
            res += ";";
            for (boolean i :
                    this.keys) {
                res += (i ? "1" : 0) + ",";
            }
            res = res.substring(0, res.length() - 1);
        } else {
            res = "";
        }
        System.out.println(res);
        return res;

    }

    public QuickSort(int[] nums) {
        this.nums = nums;
        this.keys = new boolean[nums.length];
        if (nums.length > 1) {
            st = new Stack<>();//建立栈
            st.push(0);//将区间入栈
            st.push(nums.length - 1);
        }
    }

    void swap(int i, int j) {
        int temp = this.nums[i];
        this.nums[i] = this.nums[j];
        this.nums[j] = temp;
    }

    int doSort(int left, int right, Session s) throws IOException {
        while (left < right) {
            while (this.nums[left] <= this.nums[right]) {
                if (left == right)
                    break;
                right--;

            }
            swap(left, right);
            s.getBasicRemote().sendText(this.toString());
            if (left == right)
                break;
            while (this.nums[left] <= this.nums[right]) {
                if (left == right)
                    break;
                left++;

            }
            swap(left, right);
            s.getBasicRemote().sendText(this.toString());

        }
        return right;
    }

    @Override
    public void doStep(int i, Session s) throws IOException {
        if (st == null || st.isEmpty()) {
            s.getBasicRemote().sendText(this.toString() + ";结束");
        } else {
            int right = st.pop();//取区间最右值
            int left = st.pop();//取区间最左值
            if (left != right) {
                int key = doSort(left, right, s);//对区间进行一趟排序，取得key值
                this.keys[key] = true;
                if (right == key)//如果左区间可以再分，就入栈
                {
                    st.push(left);
                    st.push(key - 1);
                }
                if (key == left)//如果右区间可以再分，就入栈
                {
                    st.push(key + 1);
                    st.push(right);
                }
                if (key > left && key < right) {
                    if (key > left + 1) {
                        st.push(left);
                        st.push(key - 1);
                    }
                    if (right > key + 1) {
                        st.push(key + 1);
                        st.push(right);
                    }
                }
            } else {
                this.keys[right] = true;
            }
            s.getBasicRemote().sendText(this.toString());
        }
    }

    @Override
    public void doRound(int i, Session s) throws IOException, InterruptedException {

//        this.nums = nums;
    }

    @Override
    public void doAuto(Double i, Session s) throws IOException, InterruptedException {
        while (st != null && (!st.isEmpty())) {
            int right = st.pop();//取区间最右值
            int left = st.pop();//取区间最左值
            if (left != right) {
                int key = doSort(left, right, s);//对区间进行一趟排序，取得key值
                this.keys[key] = true;
                if (right == key)//如果左区间可以再分，就入栈
                {
                    st.push(left);
                    st.push(key - 1);
                }
                if (key == left)//如果右区间可以再分，就入栈
                {
                    st.push(key + 1);
                    st.push(right);
                }
                if (key > left && key < right) {
                    if (key > left + 1) {
                        st.push(left);
                        st.push(key - 1);
                    }
                    if (right > key + 1) {
                        st.push(key + 1);
                        st.push(right);
                    }
                }
            } else {
                this.keys[right] = true;
            }
            s.getBasicRemote().sendText(this.toString());
            Thread.sleep(Long.parseLong(String.valueOf(i * 1000).split("\\.")[0]));
        }
        s.getBasicRemote().sendText(this.toString() + ";结束");

    }

}
