package ro.pub.cs.systems.eim.practicaltest01var04;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PracticalTest01Var04MainActivity extends AppCompatActivity {

    private CheckBox checkBox1, checkBox2;
    private EditText editText1, editText2;
    private TextView infoTextView;
    private Button displayInfoButton, navigateButton; // Adăugarea butonului de navigare

    // Codul de cerere pentru a identifica rezultatul întors de activitatea secundară
    private final int REQUEST_CODE = 1; // Alegerea unui număr la întâmplare pentru REQUEST_CODE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var04_main);

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        infoTextView = findViewById(R.id.infoTextView);
        displayInfoButton = findViewById(R.id.displayInfoButton);
        navigateButton = findViewById(R.id.navigateButton); // Inițializarea butonului de navigare

        if (savedInstanceState != null) {
            // Restaurarea stării pentru editText1 și editText2
            editText1.setText(savedInstanceState.getString("editText1Text"));
            editText2.setText(savedInstanceState.getString("editText2Text"));
        }

        displayInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder info = new StringBuilder();
                if (checkBox1.isChecked()) {
                    String text1 = editText1.getText().toString();
                    if (text1.isEmpty()) {
                        Toast.makeText(PracticalTest01Var04MainActivity.this, "Text for Checkbox 1 is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    info.append(text1);
                }
                if (checkBox2.isChecked()) {
                    String text2 = editText2.getText().toString();
                    if (text2.isEmpty()) {
                        Toast.makeText(PracticalTest01Var04MainActivity.this, "Text for Checkbox 2 is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(info.length() > 0) info.append(", ");
                    info.append(text2);
                }
                infoTextView.setText(info.toString());
            }
        });

        // Setarea listener-ului pentru butonul de navigare
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PracticalTest01Var04MainActivity.this, PracticalTest01Var04SecondaryActivity.class);
                intent.putExtra("name", editText1.getText().toString());
                intent.putExtra("group", editText2.getText().toString());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Salvarea stării pentru editText1 și editText2
        outState.putString("editText1Text", editText1.getText().toString());
        outState.putString("editText2Text", editText2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurarea stării pentru editText1 și editText2
        editText1.setText(savedInstanceState.getString("editText1Text"));
        editText2.setText(savedInstanceState.getString("editText2Text"));
    }

    // Metoda pentru gestionarea rezultatului întors de activitatea secundară
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Operation completed successfully", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Operation canceled", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void displayInformation(View view) {
        if (!editText1.getText().toString().isEmpty() && !editText2.getText().toString().isEmpty()) {
            Intent intent = new Intent(this, PracticalTest01Var04Service.class);
            intent.putExtra("name", editText1.getText().toString());
            intent.putExtra("group", editText2.getText().toString());
            startService(intent);
        } else {
            Toast.makeText(this, "Both fields must be completed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Var04Service.class);
        stopService(intent);
        super.onDestroy();
    }
    public class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals("ro.pub.cs.systems.eim.practicaltest01var04.NAME_ACTION")) {
                    String name = intent.getStringExtra("name");
                    Log.d("BroadcastReceiver", "Nume primit: " + name);
                } else if (intent.getAction().equals("ro.pub.cs.systems.eim.practicaltest01var04.GROUP_ACTION")) {
                    String group = intent.getStringExtra("group");
                    Log.d("BroadcastReceiver", "Grupa primită: " + group);
                }
            }
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ro.pub.cs.systems.eim.practicaltest01var04.NAME_ACTION");
        intentFilter.addAction("ro.pub.cs.systems.eim.practicaltest01var04.GROUP_ACTION");
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

}
