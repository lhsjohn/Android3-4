package com.example.lihuashuo.android3_4;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class MainActivity extends Activity {

    // 该程序模拟填充长度为100的数组
    private int[] data = new int[100];
    int hasData = 0;
    // 定义进度对话框的标识
    final int PROGRESS_DIALOG = 0x112;
    // 记录进度对话框的完成百分比
    int progressStatus = 0;
    ProgressDialog pd;
    // 定义一个负责更新的进度的Handler
    Handler handler;
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button execBn = (Button) findViewById(R.id.exec);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==PROGRESS_DIALOG){
                pd.setProgress(progressStatus);
                }
            }
        };

        execBn.setOnClickListener(new OnClickListener() {
            public void onClick(View source) {
                showDialog(PROGRESS_DIALOG);
            }
        });

    }
    @Override
    public Dialog onCreateDialog(int id, Bundle status) {
        System.out.println("------create------");
        switch (id) {
            case PROGRESS_DIALOG:
                // 创建进度对话框
                pd = new ProgressDialog(this);
                pd.setMax(100);
                // 设置对话框的标题
                pd.setTitle("任务完成百分比");
                // 设置对话框 显示的内容
                pd.setMessage("耗时任务的完成百分比");
                // 设置对话框不能用“取消”按钮关闭
                pd.setCancelable(false);
                // 设置对话框的进度条风格
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                // 设置对话框的进度条是否显示进度
                pd.setIndeterminate(false);
                break;
        }
        return pd;
    }
    // 该方法将在onCreateDialog方法调用之后被回调
    @Override
    public void onPrepareDialog(int id, Dialog dialog) {
        System.out.println("------prepare------");
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case PROGRESS_DIALOG:
                // 对话框进度清零
                pd.incrementProgressBy(-pd.getProgress());
                new Thread() {
                    public void run() {
                        while (progressStatus < 100) {
                            // 获取耗时操作的完成百分比
                            progressStatus = doWork();
                            // 发送消息到Handler，请补全代码
                           handler.sendEmptyMessage(PROGRESS_DIALOG);
                        }
                        // 如果任务已经完成
                        if (progressStatus >= 100) {
                            // 关闭对话框
                            pd.dismiss();
                        }
                    }
                }.start();
                break;
        }
    }
    // 模拟一个耗时的操作。
    public int doWork() {
        // 为数组元素赋值
        data[hasData++] = (int) (Math.random() * 100);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return hasData;
    }


}
