package br.calculadorasalario;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PizzaGraph extends AppCompatActivity {

    PieChart graficoPizza;
    List<PieEntry>valores = new ArrayList<>();
    PieDataSet dataset1;
    PieData dados;
    Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_graph);

        // Inicializando componentes
        initComponents();

        // Recebendo parâmetros enviados da MainActivity
        Bundle param = getIntent().getExtras();

        // Variáveis que receberão os valores enviados da MainActivity
        float salarioLiquido, valorINSS, valorIRPF;

        // Setando variáveis
        salarioLiquido = param.getFloat("salarioLiquido");
        valorINSS = param.getFloat("valorINSS");
        valorIRPF = param.getFloat("valorIRPF");

        System.out.println(param.getFloat("salarioLiquido"));
        System.out.println(salarioLiquido);
        System.out.println(valorINSS);
        System.out.println(valorIRPF);

        // Configurando aparência do gráfico de pizza
        graficoPizza.setUsePercentValues(true);
        graficoPizza.setExtraOffsets(5,10,5,5);
        graficoPizza.setDrawHoleEnabled(true);
        graficoPizza.setHoleRadius(30f);
        graficoPizza.setHoleColor(Color.TRANSPARENT);
        graficoPizza.setTransparentCircleRadius(35f);

//        graficoPizza.getDescription().setEnabled(false);
        graficoPizza.getDescription().setText("Salário");
        graficoPizza.getDescription().setTextSize(15f);

        // Atribuindo os valores e suas tags, que deverão ser mostrados no gráfico
        valores.add(new PieEntry(salarioLiquido, "Salário Líquido"));
        valores.add(new PieEntry(valorINSS, "INSS"));
        valores.add(new PieEntry(valorIRPF, "IRPF"));

        // Criando o conjunto de dados
        dataset1 = new PieDataSet(valores, "");
        dataset1.setSliceSpace(0.5f);
        dataset1.setColors(ColorTemplate.MATERIAL_COLORS);
        dataset1.setValueTextSize(15f);

        dados = new PieData(dataset1);

        graficoPizza.animateY(2000, Easing.EaseInOutCubic);
        graficoPizza.setData(dados);

    }

    private void initComponents() {
        graficoPizza = findViewById(R.id.graficoPizza);
        btnVoltar = findViewById(R.id.btnVoltar);


        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent voltar = new Intent(PizzaGraph.this, MainActivity.class);
                startActivity(voltar);
            }
        });
    }
}
