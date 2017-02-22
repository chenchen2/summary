package com.example.lenovo.summary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private final String APP_ID = "1105925863";
    private LinearLayout ll;
    private Tencent mTencent;
    private String mApp_Id = "";
    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.login:
                    mApp_Id = APP_ID;
                    mTencent = Tencent.createInstance(mApp_Id,LoginActivity.this);
                    loginQQ();
                    break;
                default:
                    break;
            }

        }
    };
    private String openid;

    /**当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法，
     * onComplete  onCancel onError
     *分别表示第三方登录成功，取消 ，错误。
     * */
    private class BaseUiListener implements IUiListener {

        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onComplete(Object o) {

            Log.e("Listener","Oncpmplete"+o.toString());
            ;
            try {
                openid = ((JSONObject) o).getString("openid");
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(UiError uiError) {
            Log.e("Listener","error");
        }

        @Override
        public void onCancel() {
            Log.e("Listener","oncancel");
        }
    }


    private void loginQQ() {

        if (!mTencent.isSessionValid()) {
            IUiListener listener = new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject values) {
                    super.doComplete(values);
                }
            };

            mTencent.login(LoginActivity.this, "all", listener);
        } else {
            mTencent.logout(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.handleResultData(data,new BaseUiListener());
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        setOnListener();
    }
    private void setOnListener() {
        ll.setOnClickListener(myListener);
    }

    private void findView() {
        ll = (LinearLayout)findViewById(R.id.login);
    }
}
