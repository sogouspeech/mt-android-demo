package com.sogou.speech.mtdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sogou.sogocommon.utils.LogUtil;
import com.sogou.speech.mt.SogouTranslate;
import com.sogou.speech.mt.TranslateListener;
import com.sogou.speech.mt.TranslateRequestConfig;
import com.sogou.speech.mt.ZhiyinInitInfo;
import com.sogou.speech.mt.v1.TranslateTextResponse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements TranslateListener, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Spinner fromSpinner;
    private Spinner destSpinner;
    private ImageView exchangeIv;
    private EditText inputContentEt;
    private Button inputTranslateBtn;
    private Button loadTranslateBtn;
    private TextView translateResultTv;
    private Button saveResultBtn;
    private SogouTranslate sogouTranslate = null;
    private String fromCode = SogouTranslate.CHINESE;
    private String destCode = SogouTranslate.ENGLISH;
    private boolean initResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults == null) {
                    LogUtil.e(TAG, "request permission failed!");
                    return;
                }
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initView();
                    //子线程中获取token
                    new Thread() {
                        @Override
                        public void run() {
                            init();
                        }
                    }.start();
                }
                break;
        }
    }

    private void requestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission_group.STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET}, 1);
        }
    }

    private void initView() {
        fromSpinner = findViewById(R.id.spinner_from_code);
        destSpinner = findViewById(R.id.spinner_dest_code);
        destSpinner.setSelection(1);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        if (destCode == SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "中文不能译中文", Toast.LENGTH_SHORT).show();
                            fromSpinner.setSelection(getLanguageSpinnerIndex(fromCode));
                        } else {
                            fromCode = SogouTranslate.CHINESE;
                        }
                        break;
                    case 1:
                        if (destCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "英文只能译中文", Toast.LENGTH_SHORT).show();
                            fromSpinner.setSelection(getLanguageSpinnerIndex(fromCode));
                        } else {
                            fromCode = SogouTranslate.ENGLISH;
                        }

                        break;
                    case 2:
                        if (destCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "日文只能译中文", Toast.LENGTH_SHORT).show();
                            fromSpinner.setSelection(getLanguageSpinnerIndex(fromCode));
                        } else {
                            fromCode = SogouTranslate.JAPANESE;
                        }

                        break;
                    case 3:
                        if (destCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "韩文只能译中文", Toast.LENGTH_SHORT).show();
                            fromSpinner.setSelection(getLanguageSpinnerIndex(fromCode));
                        } else {
                            fromCode = SogouTranslate.KOREAN;
                        }

                        break;
                    case 4:
                        if (destCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "法文只能译中文", Toast.LENGTH_SHORT).show();
                            fromSpinner.setSelection(getLanguageSpinnerIndex(fromCode));
                        } else {
                            fromCode = SogouTranslate.FRENCH;
                        }

                        break;
                    case 5:
                        if (destCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "西班牙文只能译中文", Toast.LENGTH_SHORT).show();
                            fromSpinner.setSelection(getLanguageSpinnerIndex(fromCode));
                        } else {
                            fromCode = SogouTranslate.SPANISH;
                        }

                        break;
                    case 6:
                        if (destCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "俄文只能译中文", Toast.LENGTH_SHORT).show();
                            fromSpinner.setSelection(getLanguageSpinnerIndex(fromCode));
                        } else {
                            fromCode = SogouTranslate.RUSSIAN;
                        }

                        break;
                    case 7:
                        if (destCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "德文只能译中文", Toast.LENGTH_SHORT).show();
                            fromSpinner.setSelection(getLanguageSpinnerIndex(fromCode));
                        } else {
                            fromCode = SogouTranslate.GERMAN;
                        }

                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        destSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        if (fromCode == SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "中文不能译中文", Toast.LENGTH_SHORT).show();
                            destSpinner.setSelection(getLanguageSpinnerIndex(destCode));
                        } else {
                            destCode = SogouTranslate.CHINESE;
                        }
                        break;
                    case 1:
                        if (fromCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "只能由中文译英文", Toast.LENGTH_SHORT).show();
                            destSpinner.setSelection(getLanguageSpinnerIndex(destCode));
                        } else {
                            destCode = SogouTranslate.ENGLISH;
                        }

                        break;
                    case 2:
                        if (fromCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "只能由中文译日文", Toast.LENGTH_SHORT).show();
                            destSpinner.setSelection(getLanguageSpinnerIndex(destCode));
                        } else {
                            destCode = SogouTranslate.JAPANESE;
                        }
                        break;
                    case 3:
                        if (fromCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "只能由中文译韩文", Toast.LENGTH_SHORT).show();
                            destSpinner.setSelection(getLanguageSpinnerIndex(destCode));
                        } else {
                            destCode = SogouTranslate.KOREAN;
                        }

                        break;
                    case 4:
                        if (fromCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "只能由中文译法文", Toast.LENGTH_SHORT).show();
                            destSpinner.setSelection(getLanguageSpinnerIndex(destCode));
                        } else {
                            destCode = SogouTranslate.FRENCH;
                        }

                        break;
                    case 5:
                        if (fromCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "只能由中文译西班牙文", Toast.LENGTH_SHORT).show();
                            destSpinner.setSelection(getLanguageSpinnerIndex(destCode));
                        } else {
                            destCode = SogouTranslate.SPANISH;
                        }

                    case 6:
                        if (fromCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "只能由中文译俄文", Toast.LENGTH_SHORT).show();
                            destSpinner.setSelection(getLanguageSpinnerIndex(destCode));
                        } else {
                            destCode = SogouTranslate.RUSSIAN;
                        }

                        break;
                    case 7:
                        if (fromCode != SogouTranslate.CHINESE) {
                            Toast.makeText(MainActivity.this, "只能由中文译德文", Toast.LENGTH_SHORT).show();
                            destSpinner.setSelection(getLanguageSpinnerIndex(destCode));
                        } else {
                            destCode = SogouTranslate.GERMAN;
                        }

                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        exchangeIv = findViewById(R.id.image_view_exchange);
        exchangeIv.setOnClickListener(this);
        inputContentEt = findViewById(R.id.input_content_et);
        inputTranslateBtn = findViewById(R.id.translate_input_btn);
        loadTranslateBtn = findViewById(R.id.translate_load_btn);
        translateResultTv = findViewById(R.id.translate_result);
        translateResultTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        saveResultBtn = findViewById(R.id.save_result_btn);
        inputTranslateBtn.setOnClickListener(this);
        loadTranslateBtn.setOnClickListener(this);
        saveResultBtn.setOnClickListener(this);

    }

    private void init() {
        ZhiyinInitInfo.Builder builder = new ZhiyinInitInfo.Builder();
        ZhiyinInitInfo info = builder.setBaseUrl("api.speech.sogou.com").setUuid("ewwjfci").setAppid("15Dy8oGpZg25DldDipPFqyM5HtQ").setAppkey("sLi8bHfErc399IlJzhDsvdR7tmdPb41/+k8Y/195/lfT5khGylyXdRF4CL0eYCMJxoX8DQlRLM96s9ioe+ZG4g==").setToken("").create();
        initResult = SogouTranslate.init(this.getApplicationContext(), info);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_exchange:
                String temp = fromCode;
                fromCode = destCode;
                destCode = temp;
                fromSpinner.setSelection(getLanguageSpinnerIndex(fromCode));
                destSpinner.setSelection(getLanguageSpinnerIndex(destCode));
                break;

            case R.id.translate_input_btn:
                String sourceContent = inputContentEt.getText().toString().trim();
                TranslateRequestConfig requestConfig = new TranslateRequestConfig(sourceContent, fromCode, destCode);
                LogUtil.d(TAG, "fromCode:" + fromCode + " destCode:" + destCode);
                sogouTranslate = new SogouTranslate(this);
                if (initResult) {
                    translateResultTv.setText("翻译中...");
                    sogouTranslate.translate(requestConfig);
                } else {
                    LogUtil.e(TAG, "initResult:" + initResult);
                    Toast.makeText(this, "Token获取中", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.translate_load_btn:
                String content = FileUtils.ReadTxtFile(Environment.getExternalStorageDirectory() + "/sogou/translate/translate.txt");
                int length = -1;
                if (!TextUtils.isEmpty(content)) {
                    length = content.getBytes().length;
                }
                LogUtil.d(TAG, "文本文件字节长度：" + length);
                if (length == -1) {
                    Toast.makeText(this, "文件读取失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (length > 2048) {
                    Toast.makeText(this, "文本长度多长，不能大于2048字节，当前文本" + length + "字节", Toast.LENGTH_SHORT).show();
                    return;
                }
                TranslateRequestConfig requestConfig2 = new TranslateRequestConfig(content, fromCode, destCode);
                LogUtil.d(TAG, "fromCode:" + fromCode + " destCode:" + destCode);
                sogouTranslate = new SogouTranslate(this);
                if (initResult) {
                    translateResultTv.setText("翻译中...");
                    sogouTranslate.translate(requestConfig2);
                } else {
                    LogUtil.e(TAG, "initResult:" + initResult);
                    Toast.makeText(this, "Token获取中", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.save_result_btn:
                String translateResult = translateResultTv.getText().toString().trim();
                String filePath = "/sogou/translate/translateResult" + "_" + TimeUtil.timeStampToRFC3399Format(String.valueOf(System.currentTimeMillis())) + ".txt";
                String absoluteFilePath = Environment.getExternalStorageDirectory() + filePath;
                FileUtils.writeTxtFile(translateResult, absoluteFilePath);
                Toast.makeText(this, "写入成功，路径:" + filePath, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onNext(TranslateTextResponse value) {
        String sourceText = value.getSourceText();
        final String targetText = value.getTranslatedText();
        LogUtil.d(TAG, "onNext,sourceText:" + sourceText + "   targetText:" + targetText);
        translateResultTv.post(new Runnable() {
            @Override
            public void run() {
                translateResultTv.setText(targetText);
            }
        });
    }

    @Override
    public void onError(Throwable t) {
        final String message = t.getMessage();
        LogUtil.e(TAG, "onError,message:" + message);
        translateResultTv.post(new Runnable() {
            @Override
            public void run() {
                translateResultTv.setText(message);
                sogouTranslate.release();
            }
        });
    }

    @Override
    public void onCompleted() {
        LogUtil.d(TAG, "onCompleted");
        sogouTranslate.release();
    }

    private int getLanguageSpinnerIndex(String languageCode) {
        int index = 0;
        switch (languageCode) {
            case SogouTranslate.CHINESE:
                index = 0;
                break;
            case SogouTranslate.ENGLISH:
                index = 1;
                break;

            case SogouTranslate.JAPANESE:
                index = 2;
                break;

            case SogouTranslate.KOREAN:
                index = 3;
                break;

            case SogouTranslate.FRENCH:
                index = 4;
                break;
            case SogouTranslate.SPANISH:
                index = 5;
                break;

            case SogouTranslate.RUSSIAN:
                index = 6;
                break;

            case SogouTranslate.GERMAN:
                index = 7;
                break;


        }
        return index;
    }
}
