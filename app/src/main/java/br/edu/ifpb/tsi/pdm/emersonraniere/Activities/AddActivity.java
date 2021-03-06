package br.edu.ifpb.tsi.pdm.emersonraniere.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.edu.ifpb.tsi.pdm.emersonraniere.boaacaododia.R;
import br.edu.ifpb.tsi.pdm.emersonraniere.Model.Evento;
import br.edu.ifpb.tsi.pdm.emersonraniere.Database.EventoDAO;

public class AddActivity extends AppCompatActivity {
    private EventoDAO dao;
    private Button saveButton, cancelButton;
    private EditText et_data, et_acao;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadingComponents();
        loadingListeners();
        getData();

        this.dao = new EventoDAO(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu_settings){
            Intent it = new Intent(this, ConfiguracoesActivity.class);
            startActivity(it);
        }
        if (id == R.id.action_menu_ultimas){
            Intent it = new Intent(this, UltimasActivity.class);
            startActivity(it);
        }
        if (id == R.id.action_menu_diario){
            Intent it = new Intent(this, DiarioActivity.class);
            startActivity(it);
        }
        if (id == R.id.action_menu_sobre){
            Intent it = new Intent(this, SobreActivity.class);
            startActivity(it);
        }
        if (id == R.id.action_menu_exit){
            Intent it = new Intent(Intent.ACTION_MAIN);
            it.addCategory(Intent.CATEGORY_HOME);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            Toast.makeText(this, "Obrigado", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadingListeners(){
        this.saveButton.setOnClickListener(new saveButton());
        this.et_data.setOnClickListener(new dateDialog());
        this.cancelButton.setOnClickListener(new cancelButton());
    }

    public void loadingComponents() {
        this.saveButton = (Button) findViewById(R.id.btn_add_adicionar);
        this.cancelButton = (Button) findViewById(R.id.btn_add_cancelar);
        this.et_acao = (EditText) findViewById(R.id.addPage_et_action);
        this.et_data = (EditText) findViewById(R.id.addPage_et_date);
    }

    public class cancelButton implements  View.OnClickListener {
        @Override
        public  void onClick(View v){
            finish();
        }
    }

    public class saveButton implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            try {
                // Converter o Campo de data em Long

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date saveDate = formatter.parse(et_data.getText().toString());
                long dateInLong = saveDate.getTime();

                // Cria o Evento e adiciona

                final Evento e = new Evento();
                e.setData(dateInLong);
                e.setAcao(et_acao.getText().toString());
                dao.inserir(e);
                Toast.makeText(AddActivity.this, "Ação adicionada", Toast.LENGTH_SHORT).show();
                finish();
            } catch (ParseException e1) {
                Toast.makeText(AddActivity.this, "Digite uma data válida!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class dateDialog implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            dpd = new DatePickerDialog(AddActivity.this, date, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
//            dpd.getDatePicker().setCalendarViewShown(false); // Não funciona
//            dpd.getDatePicker().setSpinnersShown(true); // Não funciona
            dpd.show();
        }
    }

    private void getData (){
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                setFormatter();
            }
        };
}

    private void setFormatter(){
        String myFormat = "dd/MM/yyyy";
        DateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_data.setText(sdf.format(myCalendar.getTime()));
    }

}
