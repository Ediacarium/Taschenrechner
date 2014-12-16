/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taschenrechner;

import java.util.Scanner;

/**
 *
 * @author alex
 */
public class Taschenrechner {

    /**
     * Versucht einen mathematischen Ausdruck auszuwerten, wenn es nicht klappt
     * wird eine zufällige Exception geworfen
     *
     * @param eval Der mathematische Ausdruck
     * @return das Ergebnis
     * @throws an exception if it doesn't work
     */
    public static double evaluate(String eval) {

        double result = 0;
        int firstBracketIndex = eval.indexOf("(");
        if (firstBracketIndex >= 0) {
            int firstBracketEnd = findBracketEnd(eval.substring(firstBracketIndex, eval.length()));
            String bracket = eval.substring(firstBracketIndex + 1, firstBracketIndex + firstBracketEnd);
            double bracketResult = evaluate(bracket);
//            eval = eval.replaceAll( "\\(" + bracket + "\\)", ""+bracketResult);
            eval = eval.substring(0, firstBracketIndex) + bracketResult + eval.substring(firstBracketIndex + firstBracketEnd + 1, eval.length());
        }
        int lineIndex = eval.indexOf("+");
        lineIndex = lineIndex >= 0 ? lineIndex : eval.indexOf("-");
        if (lineIndex >= 0) {
            String lineLeft = eval.substring(0, lineIndex);
            String lineRight = eval.substring(lineIndex, eval.length());
            double lineResult = evaluateLine(lineLeft, lineRight);
            return lineResult;
        }
        int pointIndex = eval.indexOf("*");
        pointIndex = pointIndex >= 0 ? pointIndex : eval.indexOf("/");
        pointIndex = pointIndex >= 0 ? pointIndex : eval.indexOf("%");
        if (pointIndex >= 0) {
            String pointLeft = eval.substring(0, pointIndex);
            String pointRight = eval.substring(pointIndex, eval.length());
            double pointResult = evaluatePoint(pointLeft, pointRight);
            return pointResult;
        }
        int powIndex = eval.indexOf("^");
        if (powIndex >= 0) {
            String powLeft = eval.substring(0, powIndex);
            String powRight = eval.substring(powIndex + 1, eval.length());
            double powResult = evaluatePow(powLeft, powRight);
            return powResult;
        }
        return Double.parseDouble(eval);
    }

    /**
     * Findet die Schließende Klammer zu der am Anfang beginnenden Öffnenden
     *
     * @param eval Der restliche String der Rechnung beginnend mit dem öffnenden
     * Klammer
     * @return die Position der schließenden Klammer
     */
    public static int findBracketEnd(String eval) {
        int bracketLayer = 0;
        for (int i = 0; i < eval.length(); i++) {
            switch (eval.charAt(i)) {
                case '(':
                    bracketLayer++;
                    break;
                case ')':
                    bracketLayer--;
                    break;
                default:
                    break;
            }
            if (bracketLayer == 0) {
                return i;
            }
        }
        throw new IllegalArgumentException(eval);
    }

    public static double evaluatePow(String first, String second) {
        return Math.pow(evaluate(first), evaluate(second));
    }

    /**
     *
     * rechnet entweder first * second oder first / second first % second
     * abhängig von dem ersten Buchstaben von second
     *
     * @param first der erste Operand
     * @param second Operation + zweiter Operand
     * @return das Ergebnis
     */
    public static double evaluatePoint(String first, String second) {
        if (second.charAt(0) == '*') {
            return evaluate(first) * evaluate(second.substring(1));
        } else if (second.charAt(0) == '/') {
            return evaluate(first) / evaluate(second.substring(1));
        } else if (second.charAt(0) == '%') {
            return evaluate(first) % evaluate(second.substring(1));
        }
        throw new IllegalArgumentException(first + second);
    }

    /**
     *
     * rechnet entweder first + second oder first - second abhängig von dem
     * ersten Buchstaben von second
     *
     * @param first der erste Operand
     * @param second Operation + zweiter Operand
     * @return das Ergebnis
     */
    public static double evaluateLine(String first, String second) {
        if (first.length() == 0) {
            first = "0";
        }
        if (second.charAt(0) == '+') {
            return evaluate(first) + evaluate(second.substring(1));
        } else if (second.charAt(0) == '-') {
            return evaluate(first) - evaluate(second.substring(1));
        }
        throw new IllegalArgumentException(first + second);
    }

    /**
     *
     * Main Methode. Liest eine Rechnung von der Comandozeile und gibt das
     * Ergebnis aus
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String line = "";
        while (!line.equals("exit")) {
            System.out.println("Bitte Rechnung eingeben: ");
            line = s.nextLine();
            try {
                System.out.println(line + " = " + evaluate(line));
            } catch (Exception e) {
                System.out.println("Bitte gültige Rechnung eingeben.");
            }
        }
    }
}
