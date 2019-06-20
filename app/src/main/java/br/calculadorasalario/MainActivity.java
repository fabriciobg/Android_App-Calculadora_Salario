package br.calculadorasalario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText edtSalarioBruto, edtDependentes, edtAliquotaINSS, edtBaseINSS, edtValorINSS,
            edtBaseIRPF, edtAliquotaIRPF, edtDeducaoIRPF, edtValorIRPF, edtSalarioLiquido;
    Button btnGragico;

    double salarioBruto = 0, aliquotaINSS = 0, baseINSS = 0, valorINSS = 0;
    double baseIRPF = 0, aliquotaIRPF = 0, deducaoIRPF = 0, valorIRPF = 0, salarioLiquido = 0;
    int dependentes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização de componentes
        initComponents();

        // Listener para identificar a mudança de valores no campo de salário.
        edtSalarioBruto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Tratando entrada vazia (if compacto > caso vazio, salario = 0, senão, salário igual ao que foi digitado)
                salarioBruto = (editable.toString().matches("")) ? 0 : Double.parseDouble(editable.toString());
                calcularINSS();
                calcularIRPF();

            }
        });

        edtDependentes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dependentes = editable.toString().matches("") ? 0 : Integer.parseInt(editable.toString());
                calcularIRPF();
            }
        });
    }

    private void calcularIRPF() {
        // Base do IRPF
        baseIRPF = salarioBruto - valorINSS - (dependentes*189.59);

        // Alíquota e dedução do IRPF
        if(baseIRPF < 1903.98) {
            aliquotaIRPF = 0;
            deducaoIRPF = 0;
        } else if (baseIRPF <= 2826.05) {
            aliquotaIRPF = 0.075;
            deducaoIRPF = 142.80;
        } else if (baseIRPF <= 3751.05) {
            aliquotaIRPF = 0.15;
            deducaoIRPF = 354.80;
        } else if (baseIRPF <= 4664.08) {
            aliquotaIRPF = 0.225;
            deducaoIRPF = 636.13;
        } else {
            aliquotaIRPF = 0.275;
            deducaoIRPF = 869.36;
        }

        // Valor do IRPF
        valorIRPF = (baseIRPF*aliquotaIRPF) - deducaoIRPF;

        // Salário líquido
        salarioLiquido = salarioBruto - valorINSS - valorIRPF;

        // Setando os valores nos campos
        edtBaseIRPF.setText(String.format("R$  %,.2f", baseIRPF));
        edtAliquotaIRPF.setText(String.format("%.2f", (aliquotaIRPF*100)) + "%");
        edtDeducaoIRPF.setText(String.format("R$  %.2f", deducaoIRPF));
        edtValorIRPF.setText(String.format("R$  %,.2f", valorIRPF));
        edtSalarioLiquido.setText(String.format("R$  %,.2f", salarioLiquido));
    }

    private void calcularINSS() {
        // Alíquota INSS
        if(salarioBruto <= 1751.81) {
            aliquotaINSS = 0.08;
        } else if(salarioBruto <= 2919.72){
            aliquotaINSS = 0.09;
        } else {
            aliquotaINSS = 0.11;
        }

        // Base INSS
        if(salarioBruto > 5839.45){
            baseINSS = 5839.45;
        } else {
            baseINSS = salarioBruto;
        }

        // Valor INSS
        valorINSS = aliquotaINSS * baseINSS;

        // Setando valores nos campos
        edtAliquotaINSS.setText(String.format("%.2f", (aliquotaINSS*100)) + "%");
        edtBaseINSS.setText(String.format("R$  %,.2f", baseINSS));
        edtValorINSS.setText(String.format("R$  %.2f", valorINSS));

    }

    private void initComponents() {
        edtSalarioBruto = findViewById(R.id.edtSalarioBruto);
        edtDependentes = findViewById(R.id.edtDependentes);
        edtAliquotaINSS = findViewById(R.id.edtAliquotaINSS);
        edtBaseINSS = findViewById(R.id.edtBaseINSS);
        edtValorINSS = findViewById(R.id.edtValorINSS);
        edtBaseIRPF = findViewById(R.id.edtBaseIRPF);
        edtAliquotaIRPF = findViewById(R.id.edtAliquotaIRPF);
        edtDeducaoIRPF = findViewById(R.id.edtDeducaoIRPF);
        edtValorIRPF = findViewById(R.id.edtValorIRPF);
        edtSalarioLiquido = findViewById(R.id.edtSalarioLiquido);
        btnGragico = findViewById(R.id.btnGrafico);

        btnGragico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gerarGrafico();
            }
        });
    }

    private void gerarGrafico() {
        if(salarioBruto != 0) {
            Intent graficoPizza = new Intent(MainActivity.this, PizzaGraph.class);
            graficoPizza.putExtra("salarioLiquido", Float.parseFloat(String.valueOf(salarioLiquido)));
            graficoPizza.putExtra("valorINSS", Float.parseFloat(String.valueOf(valorINSS)));
            graficoPizza.putExtra("valorIRPF", Float.parseFloat(String.valueOf(valorIRPF)));
            startActivity(graficoPizza);
        }
    }
}
