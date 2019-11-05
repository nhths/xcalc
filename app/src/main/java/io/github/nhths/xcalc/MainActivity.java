package io.github.nhths.xcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, TextWatcher {

    Button button_one, button_two, button_three, button_four, button_five, button_six, button_seven,
            button_eight, button_nine, button_dot, button_null, button_del, button_ex, button_plus, button_minus, button_equal;

    HorizontalScrollView hsvformula,hsvanswer;

    FormulaContainer fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();

        hsvformula = (HorizontalScrollView) findViewById(R.id.scroll_formula_container);
        hsvanswer = (HorizontalScrollView) findViewById(R.id.scroll_answer_container);

        TextView formula_conatiner = (TextView) findViewById(R.id.formulacontainer);
        formula_conatiner.addTextChangedListener(this);

        TextView answer_container = (TextView) findViewById(R.id.answer_container);
        answer_container.addTextChangedListener(this);

        fc = new FormulaContainer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View button) {
        if(button == button_del){
            fc.clearLastSymbol();
        }else if(button == button_dot){
            fc.addSymbol(ButtonsType.DOT,((Button)button).getText() + "");
        }else if(button == button_ex){
            fc.addSymbol(ButtonsType.EX,((Button)button).getText() + "");
        }else if(button == button_plus){
            fc.addSymbol(ButtonsType.PLUS,((Button)button).getText() + "");
        }else if(button == button_minus){
            fc.addSymbol(ButtonsType.MINUS,((Button)button).getText() + "");
        }else if(button == button_equal){
            fc.addSymbol(ButtonsType.EQUAL,((Button)button).getText() + "");
        }else{
            fc.addSymbol(ButtonsType.NUMB,((Button)button).getText() + "");
        }
    }

    @Override
    public boolean onLongClick(View button) {
        if(button == button_del) {
            fc.clearAllSymbols();
            return true;
        }

        return false;
    }

    void initButtons(){
        button_one = (Button) findViewById(R.id.one);
        button_one.setOnClickListener(this);

        button_two = (Button) findViewById(R.id.two);
        button_two.setOnClickListener(this);

        button_three = (Button) findViewById(R.id.three);
        button_three.setOnClickListener(this);

        button_four = (Button) findViewById(R.id.four);
        button_four.setOnClickListener(this);

        button_five = (Button) findViewById(R.id.five);
        button_five.setOnClickListener(this);

        button_six = (Button) findViewById(R.id.six);
        button_six.setOnClickListener(this);

        button_seven = (Button) findViewById(R.id.seven);
        button_seven.setOnClickListener(this);

        button_eight = (Button) findViewById(R.id.eight);
        button_eight.setOnClickListener(this);

        button_nine = (Button) findViewById(R.id.nine);
        button_nine.setOnClickListener(this);

        button_dot = (Button) findViewById(R.id.dot);
        button_dot.setOnClickListener(this);

        button_null = (Button) findViewById(R.id.onenull);
        button_null.setOnClickListener(this);

        button_del = (Button) findViewById(R.id.del);
        button_del.setOnClickListener(this);
        ((View)button_del).setOnLongClickListener(this);

        button_ex = (Button) findViewById(R.id.ex);
        button_ex.setOnClickListener(this);

        button_plus = (Button) findViewById(R.id.plus);
        button_plus.setOnClickListener(this);

        button_minus = (Button) findViewById(R.id.minus);
        button_minus.setOnClickListener(this);

        button_equal = (Button) findViewById(R.id.equal);
        button_equal.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        hsvformula.fullScroll(View.FOCUS_RIGHT);
        hsvanswer.fullScroll(View.FOCUS_LEFT);
    }
}

class FormulaContainer {

    volatile ArrayList<String> symb_arr = new ArrayList<String>();
    volatile TextView formula_container, answer_container;
    volatile MainActivity activity;
    XValueFinder value_finder;

    public FormulaContainer(MainActivity activity){
        this.formula_container = (TextView) activity.findViewById(R.id.formulacontainer);
        this.answer_container = (TextView) activity.findViewById(R.id.answer_container);
        this.activity = activity;
        value_finder = new XValueFinder();
    }

    public void addSymbol(ButtonsType button_type, String symb){
        System.out.println("символ - " + symb);

        if(button_type == ButtonsType.DOT && writeDotAccess()){

            System.out.println("точка - " + symb);
            symb_arr.add(symb);

        }else if(button_type == ButtonsType.EX && writeExAccess()){

            System.out.println("икс - " + symb);
            symb_arr.add(symb);

        }else if(button_type == ButtonsType.PLUS && writeSignAccess()){

            System.out.println("плюс - " + symb);
            symb_arr.add(symb);

        }else if(button_type == ButtonsType.MINUS && writeSignAccess()){

            System.out.println("минус - " + symb);
            symb_arr.add(symb);

        }else if(button_type == ButtonsType.EQUAL && writeEqualAccess()){

            System.out.println("равно - " + symb);
            symb_arr.add(symb);

        }else if(symb.matches("\\d") && writeNumbAccess()) {

            System.out.println("цифра - " + symb);
            symb_arr.add(symb);

        }


        updateFormulaContainer();
        updateAnswerContainer();
    }

    private void updateAnswerContainer(){
        String formula = "";

        for (String temp : symb_arr) {
            formula+=temp;
        }

        answer_container.setText(value_finder.findX(formula));
    }

    private boolean writeNumbAccess(){

        System.out.println("писать цифру ");

        if(symb_arr.size() == 0){
            System.out.println("можно");
            return true;
        }

        if (symb_arr.get(symb_arr.size()-1).equals(activity.getResources().getString(R.string.ex))){
            System.out.println("нельзя");
            return false;
        }

        System.out.println("можно");
        return true;
    }

    private boolean writeSignAccess(){

        System.out.println("писать знак ");

        if(symb_arr.size() == 0){
            System.out.println("можно");
            return true;
        }

        String last_symb = symb_arr.get(symb_arr.size()-1);

        if(last_symb.equals(activity.getResources().getString(R.string.dot))
                || last_symb.equals(activity.getResources().getString(R.string.plus))
                || last_symb.equals(activity.getResources().getString(R.string.minus))){

            System.out.println("нельзя");
            return false;
        }

        System.out.println("можно");
        return true;
    }

    private boolean writeExAccess(){

        System.out.println("писать икс ");

        if(symb_arr.size() == 0){
            System.out.println("можно");
            return true;
        }

        String last_symb = symb_arr.get(symb_arr.size()-1);

        if(last_symb.equals(activity.getResources().getString(R.string.dot))
                || last_symb.equals(activity.getResources().getString(R.string.ex))){
            System.out.println("нельзя");
            return false;
        }

        System.out.println("можно");
        return true;
    }

    private boolean writeDotAccess(){

        System.out.println("писать точку ");

        for (int i = symb_arr.size()-1; i>=0 ; i--){
            if(symb_arr.get(i).equals("+") || symb_arr.get(i).equals("-")){
                break;
            }else if(symb_arr.get(i).equals(".")){
                return false;
            }
        }

        if(symb_arr.size()>0){
            if (symb_arr.get(symb_arr.size()-1).matches("\\d")){
                System.out.println("можно");
                return true;
            }
        }

        System.out.println("нельзя");
        return false;
    }

    public boolean writeEqualAccess(){

        System.out.println("писать равно ");

        if(symb_arr.size() == 0){
            System.out.println("нельзя");
            return false;
        }

        String equal_str = activity.getResources().getString(R.string.equal);
        String last_symb = symb_arr.get(symb_arr.size()-1);

        for(String symb : symb_arr){
            if(symb.equals(equal_str)){
                System.out.println("нельзя");
                return false;
            }
        }

        if(last_symb.equals(activity.getResources().getString(R.string.ex))
                || last_symb.matches("\\d")){

            System.out.println("можно");
            return true;
        }

        System.out.println("нельзя");
        return false;
    }

    private void updateFormulaContainer(){
        String full_formula="";
        for (String temp : symb_arr) {
            full_formula+=temp;
        }
        formula_container.setText(full_formula);
    }

    public void clearLastSymbol(){
        if(symb_arr.size()>0){
            symb_arr.remove(symb_arr.size()-1);
        }
        updateFormulaContainer();
        updateAnswerContainer();
    }

    public void clearAllSymbols(){
        if(symb_arr.size()>0){
            symb_arr.clear();
        }
        updateFormulaContainer();
        updateAnswerContainer();
    }

}

class XValueFinder{

    private double x_coefficent_sum = 0;
    private double numbs_sum = 0;
    private double answer = 0;

    private boolean before_equal = true; // "after = before"
    private boolean x_exist = false; //for 3x and other
    private boolean is_x_values_exist = false;
    private String temp = "";

    public String findX(String formula){

        if(!formula.contains("=")){
            return "";
        }

        getNumbsSum(formula);

        if(!is_x_values_exist){
            setNullNumbs();
            return "добавьте х";
        }

        answer = numbs_sum/x_coefficent_sum;

        System.out.println("сумма цифр " + numbs_sum + "   сумма х " + x_coefficent_sum);
        System.out.println("ответ " + answer);

        return "" + setNullNumbs();
    }

    private void getNumbsSum(String formula){

        char[] char_temp_arr = formula.toCharArray();
        before_equal = true; // "after = before"
        x_exist = false; //for 3x and other
        temp = "";

        for (int i = char_temp_arr.length-1; i >= 0; i--){

            System.out.println(i + ") be " + before_equal + " =====  x exist " + x_exist + " ===== temp " + temp);

            if(char_temp_arr[i] == '='){
                System.out.println("до равно");
                addToSum(char_temp_arr[i],i);
                before_equal = false;
                continue;
            }

            if (char_temp_arr[i] == 'x'){
                System.out.println("x существует");
                x_exist = true;
                is_x_values_exist = true;
                continue;
            }

            if(char_temp_arr[i] != '='){
                temp += char_temp_arr[i];
            }

            System.out.println(i + ") be " + before_equal + " =====  x exist " + x_exist + " ===== temp " + temp);

            addToSum(char_temp_arr[i],i);

        }



    }

    private void addToSum(char char_temp, int iteration){
        if(char_temp == '+' || char_temp == '-' || char_temp == '=' || iteration == 0){

            if(temp != "" && !temp.equals("-") && !temp.equals("+")){

                if(before_equal){
                    if(x_exist){
                        System.out.println("после равно х " + temp + " --- " + reverseTemp(temp));
                        x_coefficent_sum-=Double.parseDouble(reverseTemp(temp));
                    }else {
                        System.out.println("после равно " + temp + " --- " + reverseTemp(temp));
                        numbs_sum+=Double.parseDouble(reverseTemp(temp));
                    }
                }else {
                    if(x_exist){
                        System.out.println("до равно х " + temp + " --- " + reverseTemp(temp));
                        x_coefficent_sum+=Double.parseDouble(reverseTemp(temp));
                    }else {
                        System.out.println("до равно " + temp + " --- " + reverseTemp(temp));
                        numbs_sum-=Double.parseDouble(reverseTemp(temp));
                    }
                }
            }

            temp = "";
            x_exist = false;

            System.out.println(iteration + ") be " + before_equal + " =====  x exist " + x_exist + " ===== temp " + temp);

        }
    }

    private double setNullNumbs(){
        double answer_temp = answer;
        x_coefficent_sum = 0;
        numbs_sum = 0;
        answer = 0;
        before_equal = true;
        x_exist = false;
        is_x_values_exist = false;
        String temp = "";
        return answer_temp;
    }

    private String reverseTemp(String temp){
        String reverse_temp = "";
        char[] temp_char_array = temp.toCharArray();

        for (int i = temp_char_array.length-1; i >= 0; i--) {
            reverse_temp+=temp_char_array[i];
            System.out.println("символ " + temp_char_array[i] + " отправился к " + reverse_temp);
        }

        return reverse_temp;
    }
}

enum ButtonsType{
    NUMB,
    DOT,
    EX,
    PLUS,
    MINUS,
    EQUAL;
}
