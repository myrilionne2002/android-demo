package myrilionne2002.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class calculatrice extends AppCompatActivity {
    private EditText editTextCalc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculatrice);
        //Initialisation des composants graphiques
        public void initComponents initComponents();
        editTextCalc = (EditText) findViewById(R.id.et_calc);

        /**
         * Evenement lors de l'appui sur une touche
         * @param command
         */
        public void appendCommand (String CharSequence command;
        command){
            //Calculer le résultat de l'opération
            if (command.equals("=")) {
                String formula = editTextCalc.getText().toString();
                double sum = eval(formula);
                editTextCalc.setText(fmt(sum));
            }
            //Effacer la zone de saisie
            else if (command.equals("C")) {
                editTextCalc.setText("");
            }
            //Ajouter un chiffre ou un opérateur mathématique
            else {
                //[0-9] ou .,+,*,/,-
                editTextCalc.append(command);
            }
        }

        /**
         * Initialisation des composants avec l'ajout du style et de l'evenement click
         * Il y a plusieurs boucles qui cherchent les enfants jusqu'aux buttons
         */
        () {
            TableLayout table_layout = (TableLayout) findViewById(R.id.table_layout);
            for (int i = 0; i < table_layout.getChildCount(); i++) {
                View table_row = table_layout.getChildAt(i);
                if (table_row instanceof TableRow) {
                    TableRow row = (TableRow) table_row;
                    for (int j = 0; j < row.getChildCount(); j++) {
                        View table_button = row.getChildAt(j);
                        if (table_button instanceof Button) {
                            Button button = (Button) table_button;
                            //RÃ©cupÃ¨re le layout existant
                            TableRow.LayoutParams layoutParams = (TableRow.LayoutParams) button.getLayoutParams();
                            //Ajouter les propriÃ©tÃ©s de taille
                            layoutParams.width = TableLayout.LayoutParams.MATCH_PARENT;
                            layoutParams.height = TableLayout.LayoutParams.WRAP_CONTENT;
                            layoutParams.weight = 1.0f;
                            layoutParams.setMargins(2, 2, 2, 2);
                            //Fixer les LayoutParams
                            button.setLayoutParams(layoutParams);
                            //Change la couleur des boutons
                            String c = button.getText().toString();
                            if (TextUtils.isDigitsOnly(c)) {
                                button.setBackgroundColor(Color.parseColor("#555555"));
                            } else {
                                button.setBackgroundColor(Color.parseColor("#333333"));
                            }

                            button.setTextColor(Color.parseColor("#ffffff"));
                            //  button.setTextAppearance(this, R.style.largeText);

                            //Fixer le listener pour l'appui
                            button.setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                    String buttonPressed = ((Button) v).getText().toString();
                                    appendCommand(buttonPressed);
                                }
                            });
                        }
                    }
                }
            }
        }
        /**
         * Formater un nombre sans virgule au besoin
         * @param d
         * @return
         */
        private static String fmt ( double d){
            if (d == (long) d) {
                return String.format("%d", (long) d);
            } else {
                return String.format("%s", d);
            }
        }

        /**
         * InterprÃ©teur mathÃ©matique dont l'auteur est
         * http://stackoverflow.com/users/964243/boann
         * @param str
         * @return
         */
        public static double eval ( final String str){
            class Parser {

                int pos = -1, c;

                void eatChar() {
                    c = (++pos < str.length()) ? str.charAt(pos) : -1;
                }

                void eatSpace() {
                    while (Character.isWhitespace(c)) {
                        eatChar();
                    }
                }

                double parse() {
                    eatChar();
                    double v = parseExpression();
                    if (c != -1) {
                        throw new RuntimeException("Unexpected: " + (char) c);
                    }
                    return v;
                }

                // Grammar:
                // expression = term | expression `+` term | expression `-` term
                // term = factor | term `*` factor | term `/` factor | term brackets
                // factor = brackets | number | factor `^` factor
                // brackets = `(` expression `)`
                double parseExpression() {
                    double v = parseTerm();
                    for (; ; ) {
                        eatSpace();
                        if (c == '+') { // addition
                            eatChar();
                            v += parseTerm();
                        } else if (c == '-') { // subtraction
                            eatChar();
                            v -= parseTerm();
                        } else {
                            return v;
                        }
                    }
                }

                double parseTerm() {
                    double v = parseFactor();
                    for (; ; ) {
                        eatSpace();
                        if (c == '/') { // division
                            eatChar();
                            v /= parseFactor();
                        } else if (c == '*' || c == '(') { // multiplication
                            if (c == '*') {
                                eatChar();
                            }
                            v *= parseFactor();
                        } else {
                            return v;
                        }
                    }
                }

                double parseFactor() {
                    double v;
                    boolean negate = false;
                    eatSpace();
                    if (c == '(') { // brackets
                        eatChar();
                        v = parseExpression();
                        if (c == ')') {
                            eatChar();
                        }
                    } else { // numbers
                        if (c == '+' || c == '-') { // unary plus & minus
                            negate = c == '-';
                            eatChar();
                            eatSpace();
                        }
                        StringBuilder sb = new StringBuilder();
                        while ((c >= '0' && c <= '9') || c == '.') {
                            sb.append((char) c);
                            eatChar();
                        }
                        if (sb.length() == 0) {
                            throw new RuntimeException("Unexpected: " + (char) c);
                        }
                        v = Double.parseDouble(sb.toString());
                    }
                    eatSpace();
                    if (c == '^') { // exponentiation
                        eatChar();
                        v = Math.pow(v, parseFactor());
                    }
                    if (negate) {
                        v = -v; // exponentiation has higher priority than unary minus: -3^2=-9
                    }
                    return v;
                }
            }
            return new Parser().parse();
        }
    }

    private void initComponents() {
    }



